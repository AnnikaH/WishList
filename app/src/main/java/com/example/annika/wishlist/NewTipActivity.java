package com.example.annika.wishlist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

public class NewTipActivity extends AppCompatActivity {

    private int userId;
    private RestUserService restUserService;
    private RestTipService restTipService;
    private User foundUser;
    private Button userNameCheckButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_tip);

        userId = getIntent().getIntExtra("USERID", -1);

        restUserService = new RestUserService();
        restTipService = new RestTipService();

        userNameCheckButton = (Button) findViewById(R.id.userNameCheckButton);


    }

    // Onclick search-button
    public void searchForFriendByUserName(View view) {
        // Toast only if found friend

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

                            userNameCheckButton.setBackgroundResource(R.drawable.checkmark_100);

                            return; // don't need to search any more
                        }
                    }

                    // if gets here there was no user with this username:
                    userNameCheckButton.setBackgroundResource(R.drawable.delete_cross_100);

                } catch (JSONException je) {
                    Toast toast = Toast.makeText(NewTipActivity.this,
                            getApplicationContext().getString(R.string.json_exception),
                            Toast.LENGTH_SHORT);
                    View toastView = toast.getView();
                    toastView.setBackgroundResource(R.color.background_color);
                    toast.show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast toast = Toast.makeText(NewTipActivity.this,
                        getApplicationContext().getString(R.string.user_error_message),
                        Toast.LENGTH_SHORT);
                View toastView = toast.getView();
                toastView.setBackgroundResource(R.color.background_color);
                toast.show();
            }
        });
    }

    // Onclick register-button
    public void registerNewTip(View view) {

    }

    // Onclick cancel-button
    public void cancelNewTip(View view) {

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