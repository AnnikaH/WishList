package com.example.annika.wishlist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

public class WatchWishListActivity extends AppCompatActivity {

    private int wishListId;
    private String wishListName;
    private int ownerId;
    private String ownerUserName;
    private RestUserService restUserService;
    private RestWishService restWishService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_wish_list);

        wishListId = getIntent().getIntExtra("WISHLISTID", -1);
        wishListName = getIntent().getStringExtra("WISHLISTNAME");
        ownerId = getIntent().getIntExtra("OWNERID", -1);

        restUserService = new RestUserService();
        restWishService = new RestWishService();

        // get the user name of the owner of the wish list:
        restUserService.getService().getUserById(ownerId, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                try {
                    JSONObject userObject = new JSONObject(new String(((TypedByteArray) response.getBody()).getBytes()));
                    ownerUserName = userObject.getString("userName");

                    // setText in TextView: wishListName and ownerUserName
                    TextView headerTextView = (TextView) findViewById(R.id.wishListNameHeader);
                    headerTextView.setText(ownerUserName + ": " + wishListName);

                    // TODO: get all wishes for this wishlist and show them in the ListView (friendsWishesListView):
                    // onclick each wish: see details for the wish


                } catch (JSONException je) {
                    Toast toast = Toast.makeText(WatchWishListActivity.this,
                            getApplicationContext().getString(R.string.json_exception),
                            Toast.LENGTH_SHORT);
                    View toastView = toast.getView();
                    toastView.setBackgroundResource(R.color.background_color);
                    toast.show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast toast = Toast.makeText(WatchWishListActivity.this,
                        getApplicationContext().getString(R.string.get_user_error_message),
                        Toast.LENGTH_SHORT);
                View toastView = toast.getView();
                toastView.setBackgroundResource(R.color.background_color);
                toast.show();
            }
        });
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