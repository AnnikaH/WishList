package com.example.annika.wishlist;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LogInActivity extends AppCompatActivity {

    private TextView messageTextView;
    private RestLoginService restLoginService;
    private int userId;
    private Button button;

    // Store in SharedPreferences:
    @Override
    protected void onPause() {
        super.onPause();

        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                .putInt("userId", userId)
                .commit();
    }

    // Get values from SharedPreferences:
    @Override
    protected void onResume() {
        super.onResume();

        userId = (getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getInt("userId", -1));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        messageTextView = (TextView) findViewById(R.id.message);
        restLoginService = new RestLoginService();
    }

    // Onclick login-button:
    public void logIn(View view) {
        // disable button:
        button = (Button) view;
        button.setEnabled(false);

        // reset error message TextView:
        messageTextView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.background_color));
        messageTextView.setText("");

        EditText userNameField = (EditText) findViewById(R.id.userName);
        EditText passwordField = (EditText) findViewById(R.id.password);

        String userName = userNameField.getText().toString();
        String password = passwordField.getText().toString();

        LoginUser loginUser = new LoginUser();
        loginUser.UserName = userName;
        loginUser.Password = password;

        restLoginService.getService().logIn(loginUser, new Callback<String>() {
            @Override
            public void success(String callback, Response response) {
                try {
                    userId = Integer.parseInt(callback); // får inn id gjennom StringContent på backend
                } catch (NumberFormatException nfe) {
                    userId = -1;
                }

                Toast toast = Toast.makeText(LogInActivity.this, getApplicationContext().getString(R.string.found_user),
                        Toast.LENGTH_SHORT);
                View toastView = toast.getView();
                toastView.setBackgroundResource(R.color.background_color);
                toast.show();

                Intent i = new Intent(LogInActivity.this, WishListMainActivity.class);
                i.putExtra("USERID", userId);
                startActivity(i);
                finish();
            }

            @Override
            public void failure(RetrofitError error) {
                messageTextView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.cancel));
                messageTextView.setText(getApplicationContext().getString(R.string.log_in_error_message));

                // enable button again:
                button.setEnabled(true);
            }
        });
    }

    // Onclick register-button:
    public void registerNewUser(View view) {
        Intent i = new Intent(this, RegisterUserActivity.class);
        startActivity(i);
    }

    // No menu
}