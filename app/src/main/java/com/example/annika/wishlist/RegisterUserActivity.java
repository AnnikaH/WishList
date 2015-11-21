package com.example.annika.wishlist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class RegisterUserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
    }

    // Onclick register-button:
    public void registerNewUser(View view)
    {
        // register the user and log in - and get user id and go to:

        Intent i = new Intent(this, WishListMainActivity.class);
        startActivity(i);
        finish();
    }

    // No menu
}