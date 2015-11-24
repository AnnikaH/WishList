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

public class NewWishActivity extends AppCompatActivity {

    private int wishListId;
    private String wishListName;
    private int userId;
    private RestWishService restWishService;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_wish);

        wishListId = getIntent().getIntExtra("WISHLISTID", -1);
        wishListName = getIntent().getStringExtra("WISHLISTNAME");
        userId = getIntent().getIntExtra("USERID", -1);

        restWishService = new RestWishService();
    }

    // Onclick register new wish-button
    public void registerNewWish(View view) {
        EditText nameEdit = (EditText) findViewById(R.id.wishName);
        EditText spesEdit = (EditText) findViewById(R.id.wishSpesification);
        EditText whereEdit = (EditText) findViewById(R.id.wishWhere);
        EditText linkEdit = (EditText) findViewById(R.id.wishLink);
        EditText priceEdit = (EditText) findViewById(R.id.wishPrice);

        //TODO: ImageView imageView = (ImageView) findViewById(R.id.wishImage);

        String name = nameEdit.getText().toString();
        String spesification = spesEdit.getText().toString();
        String where = whereEdit.getText().toString();
        String link = linkEdit.getText().toString();
        String priceString = priceEdit.getText().toString();
        double price;

        try {
          price = Double.parseDouble(priceString);
        } catch (NumberFormatException nfe) {
            price = 0;
        }

        // Input validation (not all fields have to be filled in):

        // backend: [RegularExpression("^[a-zæøåA-ZÆØÅ0-9., \\-]{2,30}$")]
        if(!name.matches("^[a-zæøåA-ZÆØÅ0-9., \\-]{2,30}$")) {
            nameEdit.setError(getString(R.string.name_error_message));
            return;
        }

        // backend: not required, but if filled in: [RegularExpression("^[0-9\\.]{0,9}$")]
        if(!priceString.matches("^[0-9\\.]{0,9}$")) {
            priceEdit.setError(getString(R.string.price_error_message));
            return;
        }

        Wish wish = new Wish();
        wish.Name = name;
        wish.Spesification = spesification;
        wish.Where = where;
        wish.Link = link;
        wish.Price = price;
        wish.WishListId = wishListId;
        // TODO: wish.Image = image;

        // disable button click (in case it takes some time):
        button = (Button) view;
        button.setEnabled(false);

        // post wish to server database:
        restWishService.getService().addWish(wish, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                Toast toast = Toast.makeText(NewWishActivity.this,
                        getApplicationContext().getString(R.string.wish_registered),
                        Toast.LENGTH_SHORT);
                View toastView = toast.getView();
                toastView.setBackgroundResource(R.color.background_color);
                toast.show();

                Intent i = new Intent(NewWishActivity.this, EditWishListActivity.class);
                i.putExtra("WISHLISTID", wishListId);
                i.putExtra("WISHLISTNAME", wishListName);
                i.putExtra("USERID", userId);
                startActivity(i);
                finish();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast toast = Toast.makeText(NewWishActivity.this,
                        getApplicationContext().getString(R.string.wish_registered_error_message),
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
    public void cancelNewWish(View view) {
        Intent i = new Intent(this, EditWishListActivity.class);
        i.putExtra("WISHLISTID", wishListId);
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