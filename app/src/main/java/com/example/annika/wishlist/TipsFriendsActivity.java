package com.example.annika.wishlist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

public class TipsFriendsActivity extends AppCompatActivity implements DeleteTipDialog.DialogClickListener {

    private int userId;
    private RestTipService restTipService;

    // DeleteTipDialog-method
    public void onDeleteTipClick(int tipId) {
        // delete tip with this id:

        restTipService.getService().deleteTipById(tipId, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                Toast toast = Toast.makeText(TipsFriendsActivity.this,
                        getApplicationContext().getString(R.string.tip_deleted),
                        Toast.LENGTH_SHORT);
                View toastView = toast.getView();
                toastView.setBackgroundResource(R.color.background_color);
                toast.show();

                // Refresh activity:
                Intent i = new Intent(TipsFriendsActivity.this, TipsFriendsActivity.class);
                i.putExtra("USERID", userId);
                startActivity(i);
                finish();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast toast = Toast.makeText(TipsFriendsActivity.this,
                        getApplicationContext().getString(R.string.tip_deleted_error_message),
                        Toast.LENGTH_LONG);
                View toastView = toast.getView();
                toastView.setBackgroundResource(R.color.background_color);
                toast.show();
            }
        });
    }

    // DeleteTipDialog-method
    public void onCancelDeleteTipClick() {
        // do nothing
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips_friends);

        userId = getIntent().getIntExtra("USERID", -1);

        restTipService = new RestTipService();

        restTipService.getService().getAllTipsForReceiver(userId, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                try {
                    JSONArray allTips = new JSONArray(new String(((TypedByteArray) response.getBody()).getBytes()));
                    List<WishTip> myWishTips = new ArrayList<>();

                    for(int i = 0; i < allTips.length(); i++) {
                        JSONObject tipObj = allTips.getJSONObject(i);

                        WishTip wishTip = new WishTip();
                        wishTip.ID = tipObj.getInt("id");
                        wishTip.SenderId = tipObj.getInt("senderId");
                        wishTip.ReceiverId = tipObj.getInt("receiverId");
                        wishTip.Name = tipObj.getString("name");
                        wishTip.Spesification = tipObj.getString("spesification");
                        // TODO: get image
                        wishTip.Price = tipObj.getDouble("price");
                        wishTip.Where = tipObj.getString("where");
                        wishTip.Link = tipObj.getString("link");

                        myWishTips.add(wishTip);
                    }

                    ListView listView = (ListView) findViewById(R.id.receivedTipsListView);

                    listView.setAdapter(new ArrayAdapter<>(TipsFriendsActivity.this,
                            android.R.layout.simple_selectable_list_item, myWishTips));

                    // Onclick: see the whole tip
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            ListView lv = (ListView) parent;
                            WishTip w = (WishTip) lv.getItemAtPosition((int) id);

                            Intent i = new Intent(TipsFriendsActivity.this, TipActivity.class);
                            i.putExtra("TIPID", w.ID);
                            i.putExtra("USERID", userId);
                            startActivity(i);
                            finish();
                        }
                    });

                    // On long click: delete tip
                    listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                            // Get the selected Wish:
                            ListView lv = (ListView) parent;
                            WishTip w = (WishTip) lv.getItemAtPosition((int) id);

                            // Dialog box for deleting the wish list (DeleteTipDialog):
                            String message = getString(R.string.delete_tip_dialog_message) + " " + w.Name + "?";
                            int tipId = w.ID;
                            DeleteTipDialog dialog = DeleteTipDialog.newInstance(message, tipId);
                            dialog.show(getFragmentManager(), "DELETE");
                            // waiting for the user to make a choice: Delete or Cancel

                            return true;
                        }
                    });

                } catch (JSONException je) {
                    Toast toast = Toast.makeText(TipsFriendsActivity.this,
                            getApplicationContext().getString(R.string.json_exception),
                            Toast.LENGTH_SHORT);
                    View toastView = toast.getView();
                    toastView.setBackgroundResource(R.color.background_color);
                    toast.show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast toast = Toast.makeText(TipsFriendsActivity.this,
                        getApplicationContext().getString(R.string.get_tips_error_message),
                        Toast.LENGTH_LONG);
                View toastView = toast.getView();
                toastView.setBackgroundResource(R.color.background_color);
                toast.show();
            }
        });
    }

    // Onclick tip a friend
    public void tipAFriend(View view) {
        Intent i = new Intent(this, NewTipActivity.class);
        i.putExtra("USERID", userId);
        startActivity(i);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_wish_list_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.settings:
                Intent i = new Intent(this, SettingsActivity.class);
                startActivity(i);
                finish();
                return true;
            case R.id.log_out:
                Intent i2 = new Intent(this, LogInActivity.class);
                startActivity(i2);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}