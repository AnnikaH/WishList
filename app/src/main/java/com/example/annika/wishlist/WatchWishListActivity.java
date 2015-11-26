package com.example.annika.wishlist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

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
        restUserService.getService().getUserById(ownerId, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                try {
                    JSONObject userObject = new JSONObject(new String(((TypedByteArray) response.getBody()).getBytes()));
                    ownerUserName = userObject.getString("userName");

                    // setText in TextView: wishListName and ownerUserName
                    TextView headerTextView = (TextView) findViewById(R.id.wishListNameHeader);
                    headerTextView.setText(ownerUserName + ": " + wishListName);

                    showAllWishes();

                } catch (JSONException je) {
                    Toast toast = Toast.makeText(WatchWishListActivity.this,
                            getApplicationContext().getString(R.string.json_exception),
                            Toast.LENGTH_SHORT);
                    View toastView = toast.getView();
                    toastView.setBackgroundResource(R.color.background_color);
                    toast.show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast toast = Toast.makeText(WatchWishListActivity.this,
                        getApplicationContext().getString(R.string.get_user_error_message),
                        Toast.LENGTH_SHORT);
                View toastView = toast.getView();
                toastView.setBackgroundResource(R.color.background_color);
                toast.show();
            }
        });
    }

    public void showAllWishes() {
        // Get all wishes for this wishList and show them in the ListView (friendsWishesListView):
        // onclick each wish: see details for the wish

        restWishService.getService().getAllWishesForWishList(wishListId, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                try {
                    JSONArray wishes = new JSONArray(new String(((TypedByteArray) response.getBody()).getBytes()));

                    // place each wish into the ListView:

                    ListView listView = (ListView) findViewById(R.id.friendsWishesListView);
                    int length = wishes.length();
                    List<Wish> allWishes = new ArrayList<>(length);

                    for (int i = 0; i < length; i++)
                    {
                        JSONObject oneWish = wishes.getJSONObject(i);

                        Wish wish = new Wish();
                        wish.ID = oneWish.getInt("id");
                        wish.Name = oneWish.getString("name");
                        wish.Spesification = oneWish.getString("spesification");
                        wish.Link = oneWish.getString("link");
                        wish.Price = oneWish.getDouble("price");
                        wish.Where = oneWish.getString("where");
                        wish.WishListId = oneWish.getInt("wishListId");

                        allWishes.add(wish);
                    }

                    listView.setAdapter(new ArrayAdapter<>(WatchWishListActivity.this,
                            android.R.layout.simple_selectable_list_item, allWishes));

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            ListView lv = (ListView) parent;
                            Wish w = (Wish) lv.getItemAtPosition((int) id);

                            // Go to EditWishListActivity and send in the id and name of the wish list selected:
                            Intent i = new Intent(WatchWishListActivity.this, WatchWishActivity.class);
                            i.putExtra("WISHID", w.ID);
                            i.putExtra("WISHLISTID", wishListId);
                            i.putExtra("WISHLISTNAME", wishListName);
                            i.putExtra("OWNERID", ownerId);
                            startActivity(i);
                            finish();
                        }
                    });
                }
                catch (JSONException je) {
                    Toast toast = Toast.makeText(WatchWishListActivity.this,
                            getApplicationContext().getString(R.string.json_exception),
                            Toast.LENGTH_SHORT);
                    View toastView = toast.getView();
                    toastView.setBackgroundResource(R.color.background_color);
                    toast.show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast toast = Toast.makeText(WatchWishListActivity.this,
                        getApplicationContext().getString(R.string.get_wish_error_message),
                        Toast.LENGTH_SHORT);
                View toastView = toast.getView();
                toastView.setBackgroundResource(R.color.background_color);
                toast.show();
            }
        });
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