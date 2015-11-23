package com.example.annika.wishlist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedInput;

public class MyWishListsActivity extends AppCompatActivity implements NewWishListDialog.DialogClickListener {

    private int userId;
    private RestWishListService restWishListService;

    // NewWishListDialog-method
    @Override
    public void onSaveClick(String wishListName)
    {
        // create/add new wish list for this user (with userId)

        WishList wishList = new WishList();
        wishList.Name = wishListName;
        wishList.OwnerId = userId;

        // post:
        restWishListService.getService().addWishList(wishList, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                Toast toast = Toast.makeText(MyWishListsActivity.this, getApplicationContext().getString(R.string.wish_list_added),
                        Toast.LENGTH_SHORT);
                View toastView = toast.getView();
                toastView.setBackgroundResource(R.color.background_color);
                toast.show();

                // and refresh the activity
                Intent i = new Intent(MyWishListsActivity.this, MyWishListsActivity.class);
                i.putExtra("USERID", userId);
                startActivity(i);
                finish();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast toast = Toast.makeText(MyWishListsActivity.this, getApplicationContext().getString(R.string.wish_list_added_error_message),
                        Toast.LENGTH_SHORT);
                View toastView = toast.getView();
                toastView.setBackgroundResource(R.color.background_color);
                toast.show();
            }
        });
    }

    // NewWishListDialog-method
    @Override
    public void onCancelClick()
    {
        // do nothing
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wish_lists);

        userId = getIntent().getIntExtra("USERID", -1);

        restWishListService = new RestWishListService();

        // get all wish lists for this user:
        restWishListService.getService().getAllWishListsForUser(userId, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                try {
                    JSONArray wishLists = new JSONArray(new String(((TypedByteArray) response.getBody()).getBytes()));

                    Log.d("ALL WISH LISTS: ", wishLists.toString());

                    // place each wish list into the view:
                    ListView listView = (ListView) findViewById(R.id.wishListsListView);

                    int length = wishLists.length();
                    List<String> listContents = new ArrayList<>(length);

                    for (int i = 0; i < length; i++)
                    {
                        listContents.add(wishLists.getString(i));
                    }

                    listView.setAdapter(new ArrayAdapter<>(MyWishListsActivity.this, android.R.layout.simple_list_item_1,
                            listContents));

                    /*listView.setAdapter(new ArrayAdapter<String>(MyWishListsActivity.this,
                            android.R.layout.simple_list_item_1, listContents));*/


                    /*for(int i = 0; i < wishLists.length(); i++)
                    {
                        JSONObject oneList = wishLists.getJSONObject(i);

                        TextView textView = new TextView(getApplicationContext());
                        textView.setText(oneList.getString("name"));
                    }*/
                }
                catch (JSONException je) {
                    Toast toast = Toast.makeText(MyWishListsActivity.this, getApplicationContext().getString(R.string.json_exception),
                            Toast.LENGTH_LONG);
                    View toastView = toast.getView();
                    toastView.setBackgroundResource(R.color.background_color);
                    toast.show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast toast = Toast.makeText(MyWishListsActivity.this, getApplicationContext().getString(R.string.wish_list_added_error_message),
                        Toast.LENGTH_SHORT);
                View toastView = toast.getView();
                toastView.setBackgroundResource(R.color.background_color);
                toast.show();
            }
        });
    }

    // Onclick new list-button
    public void createNewList(View view)
    {
        // dialogbox (NewWishListDialog) hvor kan fylle inn navn på den nye listen vi vil lage:
        String message = getString(R.string.new_list_dialog_message);
        NewWishListDialog dialog = NewWishListDialog.newInstance(message);
        dialog.show(getFragmentManager(), "CREATE");
        // waiting for the user to make a choice: Create og Cancel
    }

    // Onclick main menu-button
    public void goToMainMenu(View view)
    {
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