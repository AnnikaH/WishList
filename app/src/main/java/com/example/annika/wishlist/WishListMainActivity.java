package com.example.annika.wishlist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class WishListMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list_main);
    }

    // OnClick profile_button:
    public void profileClicked(View view) {
        Intent i = new Intent(this, ProfileActivity.class);
        startActivity(i);
    }

    // OnClick my_wish_lists_button:
    public void myWishListsClicked(View view) {
        Intent i = new Intent(this, MyWishListsActivity.class);
        startActivity(i);
    }

    // OnClick tips_friends_button:
    public void tipsFriendsClicked(View view) {
        Intent i = new Intent(this, TipsFriendsActivity.class);
        startActivity(i);
    }

    // OnClick friends_wish_lists_button:
    public void friendsWishListsClicked(View view) {
        Intent i = new Intent(this, FriendsWishListsActivity.class);
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

        if (id == R.id.settings) {
            Intent i = new Intent(this, SettingsActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}