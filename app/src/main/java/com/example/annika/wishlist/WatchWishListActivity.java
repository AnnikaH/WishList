package com.example.annika.wishlist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

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


        // get all wishes for this wishlist and show them in the ListView:
        // onclick each wish: see details for the wish

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