package com.example.annika.wishlist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class WishListMainActivity extends AppCompatActivity {

    private int userId;

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
        setContentView(R.layout.activity_wish_list_main);

        userId = getIntent().getIntExtra("USERID", -1);
    }

    // OnClick profile_button:
    public void profileClicked(View view) {
        Intent i = new Intent(this, ProfileActivity.class);
        i.putExtra("USERID", userId);
        startActivity(i);
    }

    // OnClick my_wish_lists_button:
    public void myWishListsClicked(View view) {
        Intent i = new Intent(this, MyWishListsActivity.class);
        i.putExtra("USERID", userId);
        startActivity(i);
    }

    // OnClick tips_friends_button:
    public void tipsFriendsClicked(View view) {
        Intent i = new Intent(this, TipsFriendsActivity.class);
        i.putExtra("USERID", userId);
        startActivity(i);
    }

    // OnClick friends_wish_lists_button:
    public void friendsWishListsClicked(View view) {
        Intent i = new Intent(this, FriendsWishListsActivity.class);
        i.putExtra("USERID", userId);
        startActivity(i);
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
                return true;
            case R.id.log_out:
                Intent i2 = new Intent(this, LogInActivity.class);
                startActivity(i2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}