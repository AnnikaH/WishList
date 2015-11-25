package com.example.annika.wishlist;

import android.app.FragmentManager;
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

public class WatchWishActivity extends AppCompatActivity implements WishBoughtDialog.DialogClickListener {

    private int wishId;
    private int wishListId;
    private String wishListName;
    private int ownerId;
    private RestWishService restWishService;

    // WishBoughtDialog-method
    public void onBuyClick() {

    }

    // WishBoughtDialog-method
    public void onCancelBuyClick() {
        // do nothing
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_wish);

        wishId = getIntent().getIntExtra("WISHID", -1);
        wishListId = getIntent().getIntExtra("WISHLISTID", -1);
        wishListName = getIntent().getStringExtra("WISHLISTNAME");
        ownerId = getIntent().getIntExtra("OWNERID", -1);

        //i.putExtra("WISHNAME", w.Name);
        //i.putExtra("WISHSPESIFICATION", w.Spesification);
        // TODO: POSSIBLE?:
        //i.putExtra("WISHIMAGE", w.Image);
        //i.putExtra("WISHLINK", w.Link);
        //i.putExtra("WISHPRICE", w.Price);
        //i.putExtra("WISHWHERE", w.Where);
        //i.putExtra("WISHWISHLISTID", w.WishListId);

        restWishService = new RestWishService();

        final TextView nameTextView = (TextView) findViewById(R.id.wishName);
        final TextView spesTextView = (TextView) findViewById(R.id.wishSpesification);
        final TextView whereTextView = (TextView) findViewById(R.id.wishWhere);
        final TextView linkTextView = (TextView) findViewById(R.id.wishLink);
        final TextView priceTextView = (TextView) findViewById(R.id.wishPrice);
        // TODO: ImageView imageView = (ImageView) findViewById(R.id.wishImage);
        final TextView boughtTextView = (TextView) findViewById(R.id.wishBought);

        // get this wish:
        restWishService.getService().getWishById(wishId, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                try {
                    // There's a JSON-object that's returned from the backend:
                    JSONObject wish = new JSONObject(new String(((TypedByteArray) response.getBody()).getBytes()));

                    String wishName = wish.getString("name");
                    String wishSpesification = wish.getString("spesification");
                    double wishPrice = wish.getDouble("price");
                    String wishLink = wish.getString("link");
                    String wishWhere = wish.getString("where");
                    boolean wishBought = wish.getBoolean("bought");

                    nameTextView.setText(wishName);
                    spesTextView.setText(wishSpesification);
                    whereTextView.setText(wishWhere);
                    linkTextView.setText(wishLink);
                    priceTextView.setText(wishPrice + " kr");

                    if(wishBought) {
                        boughtTextView.setBackgroundResource(R.drawable.checked_checkbox_100);
                    } else {
                        boughtTextView.setBackgroundResource(R.drawable.delete_cross_100);
                    }

                } catch (JSONException je) {
                    Toast toast = Toast.makeText(WatchWishActivity.this,
                            getApplicationContext().getString(R.string.json_exception),
                            Toast.LENGTH_SHORT);
                    View toastView = toast.getView();
                    toastView.setBackgroundResource(R.color.background_color);
                    toast.show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast toast = Toast.makeText(WatchWishActivity.this,
                        getApplicationContext().getString(R.string.wish_error_message),
                        Toast.LENGTH_LONG);
                View toastView = toast.getView();
                toastView.setBackgroundResource(R.color.background_color);
                toast.show();
            }
        });
    }

    // Onclick buy-button
    public void markWishAsBought(View view) {
        String message = getString(R.string.buy_message);
        WishBoughtDialog dialog = WishBoughtDialog.newInstance(message);
        dialog.show(getFragmentManager(), "BUY");
        // waiting for the user to make a choice: Mark as bought or Cancel
    }

    // Onclick cancel-button
    public void backToWishList(View view) {
        Intent i = new Intent(this, WatchWishListActivity.class);
        i.putExtra("WISHLISTID", wishListId);
        i.putExtra("WISHLISTNAME", wishListName);
        i.putExtra("OWNERID", ownerId);
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