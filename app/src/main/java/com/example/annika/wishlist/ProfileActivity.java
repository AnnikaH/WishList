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

public class ProfileActivity extends AppCompatActivity implements DeleteProfileDialog.DialogClickListener {

    private RestUserService restUserService;
    private TextView userNameTextView;
    private TextView emailTextView;
    private TextView mobileTextView;
    private int userId;
    private String userName;
    private String email;
    private String mobile;

    // DeleteProfileDialog-method
    public void onDeleteProfileClick(int profileId) {

        restUserService.getService().deleteUserById(profileId, new Callback<User>() {
            @Override
            public void success(User user, Response response) {
                Toast toast = Toast.makeText(ProfileActivity.this,
                        getApplicationContext().getString(R.string.profile_deleted),
                        Toast.LENGTH_SHORT);
                View toastView = toast.getView();
                toastView.setBackgroundResource(R.color.background_color);
                toast.show();

                // Log out (isn't a valid user any more)
                Intent i = new Intent(ProfileActivity.this, LogInActivity.class);
                startActivity(i);
                finish();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast toast = Toast.makeText(ProfileActivity.this,
                        getApplicationContext().getString(R.string.profile_deleted_error_message),
                        Toast.LENGTH_LONG);
                View toastView = toast.getView();
                toastView.setBackgroundResource(R.color.background_color);
                toast.show();
            }
        });
    }

    // DeleteProfileDialog-method
    public void onCancelDeleteProfileClick() {
        // do nothing
    }

    // Store in SharedPreferences:
    @Override
    protected void onPause() {
        super.onPause();

        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                .putInt("userId", userId)
                .putString("userName", userName)
                .putString("email", email)
                .putString("mobile", mobile)
                .commit();
    }

    // Get values from SharedPreferences:
    @Override
    protected void onResume() {
        super.onResume();

        userId = (getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getInt("userId", -1));
        userName = (getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getString("userName", ""));
        email = (getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getString("email", ""));
        mobile = (getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getString("mobile", ""));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        restUserService = new RestUserService();

        userNameTextView = (TextView) findViewById(R.id.userNameTextView);
        emailTextView = (TextView) findViewById(R.id.emailTextView);
        mobileTextView = (TextView) findViewById(R.id.mobileNumberTextView);

        userId = getIntent().getIntExtra("USERID", -1);

        // get user by id:
        restUserService.getService().getUserById(userId, new Callback<Response>() {
            @Override
            public void success(Response callback, Response response) {
                try {
                    // There's a JSON-object that's returned from the backend:
                    JSONObject user = new JSONObject(new String(((TypedByteArray) response.getBody()).getBytes()));

                    userName = user.get("userName").toString();
                    email = user.get("email").toString();
                    mobile = user.get("phoneNumber").toString();

                    userNameTextView.setText(userName);
                    emailTextView.setText(email);
                    mobileTextView.setText(mobile);
                } catch (JSONException je) {
                    Toast toast = Toast.makeText(ProfileActivity.this, getApplicationContext().getString(R.string.json_exception),
                            Toast.LENGTH_LONG);
                    View toastView = toast.getView();
                    toastView.setBackgroundResource(R.color.background_color);
                    toast.show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast toast = Toast.makeText(ProfileActivity.this, getApplicationContext().getString(R.string.get_user_error_message),
                        Toast.LENGTH_LONG);
                View toastView = toast.getView();
                toastView.setBackgroundResource(R.color.background_color);
                toast.show();
            }
        });
    }

    // Onclick edit profile-button
    public void editProfile(View view)
    {
        Intent i = new Intent(this, EditProfileActivity.class);
        i.putExtra("USERID", userId);
        i.putExtra("USERNAME", userName);
        i.putExtra("EMAIL", email);
        i.putExtra("MOBILE", mobile);
        startActivity(i);
        finish();
    }

    // Onclick delete profile-button
    public void deleteProfile(View view) {
        // dialog box with warning:
        String message = getString(R.string.delete_profile_dialog_message);
        DeleteProfileDialog dialog = DeleteProfileDialog.newInstance(message, userId);
        dialog.show(getFragmentManager(), "DELETE");
        // waiting for the user to make a choice: Delete or Cancel
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