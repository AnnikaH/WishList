package com.example.annika.wishlist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class LogInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
    }

    // Onclick login-button:
    public void logIn(View view) {
        EditText userNameField = (EditText) findViewById(R.id.userName);
        EditText passwordField = (EditText) findViewById(R.id.password);

        String userName = userNameField.toString();
        String password = passwordField.toString();

        // kall til database (async): .../api/User/LogIn og sende med brukernavn og passord
        // få tilbake id til brukeren
        int userId = 0;

        // hvis verifiserer:
        Intent i = new Intent(this, WishListMainActivity.class);
        i.putExtra("USERID", userId);
        startActivity(i);
    }

    // Onclick register-button:
    public void registerNewUser(View view) {
        Intent i = new Intent(this, RegisterUserActivity.class);
        startActivity(i);
    }

    // No menu
}