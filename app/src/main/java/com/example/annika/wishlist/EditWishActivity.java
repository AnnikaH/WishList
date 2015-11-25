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

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class EditWishActivity extends AppCompatActivity {

    private String wishListName;
    private int userId;
    private int wishWishListId;
    private int wishId;
    private String wishName;
    private String wishSpesification;
    // TODO: private Image wishImage;
    private String wishLink;
    private double wishPrice;
    private String wishWhere;
    private RestWishService restWishService;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_wish);

        restWishService = new RestWishService();

        wishListName = getIntent().getStringExtra("WISHLISTNAME");
        userId = getIntent().getIntExtra("USERID", -1);
        wishId = getIntent().getIntExtra("WISHID", -1);
        wishName = getIntent().getStringExtra("WISHNAME");
        wishSpesification = getIntent().getStringExtra("WISHSPESIFICATION");
        // TODO: wishImage = getIntent().get....Extra("WISHIMAGE");
        wishLink = getIntent().getStringExtra("WISHLINK");
        wishPrice = getIntent().getDoubleExtra("WISHPRICE", -1);
        wishWhere = getIntent().getStringExtra("WISHWHERE");
        wishWishListId = getIntent().getIntExtra("WISHWISHLISTID", -1);

        // alternative: call database (getWish)

        EditText nameEdit = (EditText) findViewById(R.id.wishNameEdit);
        EditText spesEdit = (EditText) findViewById(R.id.wishSpesificationEdit);
        EditText linkEdit = (EditText) findViewById(R.id.wishLinkEdit);
        EditText priceEdit = (EditText) findViewById(R.id.wishPriceEdit);
        EditText whereEdit = (EditText) findViewById(R.id.wishWhereEdit);

        nameEdit.setText(wishName);
        spesEdit.setText(wishSpesification);
        linkEdit.setText(wishLink);
        priceEdit.setText(wishPrice + "");
        whereEdit.setText(wishWhere);
    }

    // Onclick save-button
    public void saveEditWish(View view) {
        EditText nameEdit = (EditText) findViewById(R.id.wishNameEdit);
        EditText spesEdit = (EditText) findViewById(R.id.wishSpesificationEdit);
        EditText linkEdit = (EditText) findViewById(R.id.wishLinkEdit);
        EditText priceEdit = (EditText) findViewById(R.id.wishPriceEdit);
        EditText whereEdit = (EditText) findViewById(R.id.wishWhereEdit);
        // TODO: ImageView imageView = (ImageView) findViewById(R.id.wishImage);

        String newName = nameEdit.getText().toString();
        String newSpes = spesEdit.getText().toString();
        String newLink = linkEdit.getText().toString();
        String newPrice = priceEdit.getText().toString();
        String newWhere = whereEdit.getText().toString();
        // TODO: Get image from imageView

        // Input validation:

        // backend: [RegularExpression("^[a-zæøåA-ZÆØÅ0-9., \\-]{2,30}$")]
        if(!newName.matches("^[a-zæøåA-ZÆØÅ0-9., \\-]{2,30}$")) {
            nameEdit.setError(getString(R.string.name_error_message));
            return;
        }

        // backend: not required, but if filled in: [RegularExpression("^[0-9\\.]{0,9}$")]
        if(!newPrice.matches("^[0-9\\.]{0,9}$")) {
            priceEdit.setError(getString(R.string.price_error_message));
            return;
        }

        Wish editedWish = new Wish();
        editedWish.ID = wishId;
        editedWish.Name = newName;
        editedWish.Spesification = newSpes;
        editedWish.Link = newLink;
        editedWish.WishListId = wishWishListId;
        editedWish.Where = newWhere;
        // TODO: editedWish.Image = image;
        editedWish.Bought = false;

        try {
            editedWish.Price = Double.parseDouble(newPrice);
        } catch (NumberFormatException nfe) {
            editedWish.Price = 0;
        }

        // disable button click (in case it takes some time):
        button = (Button) view;
        button.setEnabled(false);

        // put wish:
        restWishService.getService().updateWishById(wishId, editedWish, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                Toast toast = Toast.makeText(EditWishActivity.this,
                        getApplicationContext().getString(R.string.wish_updated),
                        Toast.LENGTH_SHORT);
                View toastView = toast.getView();
                toastView.setBackgroundResource(R.color.background_color);
                toast.show();

                Intent i = new Intent(EditWishActivity.this, WishActivity.class);
                i.putExtra("WISHID", wishId);
                i.putExtra("WISHLISTNAME", wishListName);
                i.putExtra("USERID", userId);
                startActivity(i);
                finish();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast toast = Toast.makeText(EditWishActivity.this,
                        getApplicationContext().getString(R.string.wish_updated_error_message),
                        Toast.LENGTH_LONG);
                View toastView = toast.getView();
                toastView.setBackgroundResource(R.color.background_color);
                toast.show();

                // enable button again:
                button.setEnabled(true);
            }
        });
    }

    // Onclick cancel-button
    public void backToWish(View view) {
        Intent i = new Intent(this, WishActivity.class);
        i.putExtra("WISHID", wishId);
        i.putExtra("WISHLISTNAME", wishListName);
        i.putExtra("USERID", userId);
        startActivity(i);
        finish();
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