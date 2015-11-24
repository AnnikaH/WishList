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

public class WishActivity extends AppCompatActivity {

    private int wishId;
    private RestWishService restWishService;
    private String wishName;
    private String wishSpesification;
    // TODO: private Image wishImage;
    private String wishLink;
    private double wishPrice;
    private String wishWhere;
    private int wishWishListId;
    private String wishListName;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish);

        wishId = getIntent().getIntExtra("WISHID", -1);
        wishListName = getIntent().getStringExtra("WISHLISTNAME");
        userId = getIntent().getIntExtra("USERID", -1);

        restWishService = new RestWishService();

        final TextView nameTextView = (TextView) findViewById(R.id.wishName);
        final TextView spesTextView = (TextView) findViewById(R.id.wishSpesification);
        final TextView whereTextView = (TextView) findViewById(R.id.wishWhere);
        final TextView linkTextView = (TextView) findViewById(R.id.wishLink);
        final TextView priceTextView = (TextView) findViewById(R.id.wishPrice);
        // TODO: ImageView imageView = (ImageView) findViewById(R.id.wishImage);

        // get this wish:
        restWishService.getService().getWishById(wishId, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                try {
                    // There's a JSON-object that's returned from the backend:
                    JSONObject wish = new JSONObject(new String(((TypedByteArray) response.getBody()).getBytes()));

                    wishName = wish.getString("name");
                    wishSpesification = wish.getString("spesification");
                    wishPrice = wish.getDouble("price");
                    wishLink = wish.getString("link");
                    wishWhere = wish.getString("where");
                    wishWishListId = wish.getInt("wishListId");

                    nameTextView.setText(wishName);
                    spesTextView.setText(wishSpesification);
                    whereTextView.setText(wishWhere);
                    linkTextView.setText(wishLink);
                    priceTextView.setText(wishPrice + " kr");
                } catch (JSONException je) {
                    Toast toast = Toast.makeText(WishActivity.this,
                            getApplicationContext().getString(R.string.json_exception),
                            Toast.LENGTH_LONG);
                    View toastView = toast.getView();
                    toastView.setBackgroundResource(R.color.background_color);
                    toast.show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast toast = Toast.makeText(WishActivity.this,
                        getApplicationContext().getString(R.string.wish_error_message),
                        Toast.LENGTH_LONG);
                View toastView = toast.getView();
                toastView.setBackgroundResource(R.color.background_color);
                toast.show();
            }
        });
    }

    // Onclick edit-button
    public void editWish(View view) {
        Intent i = new Intent(this, EditWishActivity.class);
        i.putExtra("WISHID", wishId);
        i.putExtra("WISHNAME", wishName);
        i.putExtra("WISHSPESIFICATION", wishSpesification);

        // TODO: POSSIBLE?:
        //i.putExtra("WISHIMAGE", wishImage);

        i.putExtra("WISHLINK", wishLink);
        i.putExtra("WISHPRICE", wishPrice);
        i.putExtra("WISHWHERE", wishWhere);
        i.putExtra("WISHWISHLISTID", wishWishListId);
        startActivity(i);
        finish();
    }

    // Onclick back-button
    public void backToWishList(View View) {
        Intent i = new Intent(this, EditWishListActivity.class);
        i.putExtra("WISHLISTID", wishWishListId);
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