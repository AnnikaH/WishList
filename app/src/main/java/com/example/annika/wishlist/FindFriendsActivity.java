package com.example.annika.wishlist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

public class FindFriendsActivity extends AppCompatActivity {

    private RestUserService restUserService;
    private RestWishListService restWishListService;
    private User foundUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friends);

        // getIntent()...?

        restUserService = new RestUserService();
        restWishListService = new RestWishListService();
    }

    // Onclick search-button:
    public void searchForFriendByUserName(View view) {
        EditText userNameEdit = (EditText) findViewById(R.id.userNameEditSearch);
        final String userNameSearched = userNameEdit.getText().toString();

        restUserService.getService().getAllUsers(new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                try {
                    JSONArray allUsers = new JSONArray(new String(((TypedByteArray) response.getBody()).getBytes()));

                    for(int i = 0; i < allUsers.length(); i++) {
                        JSONObject user = allUsers.getJSONObject(i);

                        if(user.getString("userName").equals(userNameSearched)) {
                            foundUser = new User();
                            foundUser.ID = user.getInt("id");
                            foundUser.UserName = user.getString("userName");
                            foundUser.Email = user.getString("email");
                            foundUser.PhoneNumber = user.getString("phoneNumber");
                            foundUser.Password = "";

                            getFoundUsersWishLists();

                            return; // don't need to search any more
                        }
                    }
                } catch (JSONException je) {
                    Toast toast = Toast.makeText(FindFriendsActivity.this,
                            getApplicationContext().getString(R.string.json_exception),
                            Toast.LENGTH_SHORT);
                    View toastView = toast.getView();
                    toastView.setBackgroundResource(R.color.background_color);
                    toast.show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast toast = Toast.makeText(FindFriendsActivity.this,
                        getApplicationContext().getString(R.string.user_error_message),
                        Toast.LENGTH_SHORT);
                View toastView = toast.getView();
                toastView.setBackgroundResource(R.color.background_color);
                toast.show();
            }
        });
    }

    public void getFoundUsersWishLists() {
        int userId = foundUser.ID;
        ListView listView = (ListView) findViewById(R.id.friendWishListsListView);

        restWishListService.getService().getAllWishListsForUser(userId, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {

            }

            @Override
            public void failure(RetrofitError error) {

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