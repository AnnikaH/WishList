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

public class FriendsWishListsActivity extends AppCompatActivity {

    private int userId;
    private RestSharingService restSharingService;
    private RestWishListService restWishListService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_wish_lists);

        userId = getIntent().getIntExtra("USERID", -1);

        restSharingService = new RestSharingService();
        restWishListService = new RestWishListService();

        // get all friends' wish lists that you have access to:
        restSharingService.getService().getAllSharingsForUser(userId, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                try {
                    JSONArray sharings = new JSONArray(new String(((TypedByteArray) response.getBody()).getBytes()));
                    //final List<WishList> sharedWishLists = new ArrayList<>();

                    //List<Request> requests = new ArrayList<>();;

                    int[] wishListIds = new int[sharings.length()];

                    // go through all the sharings and get the wishListId's for those that are confirmed:
                    for (int i = 0; i < sharings.length(); i++) {
                        JSONObject sharing = sharings.getJSONObject(i);
                        boolean confirmed = sharing.getBoolean("confirmed");

                        if (confirmed) {
                            int wishListId = sharing.getInt("wishListId");
                            wishListIds[i] = wishListId;
                        }

                        /*Request request = new Request();
                        request.SharingID = sharing.getInt("id");
                        request.Confirmed = sharing.getBoolean("confirmed");
                        request.WishListId = sharing.getInt("wishListId");
                        request.UserId = sharing.getInt("userId");*/

                        //requests.add(request);
                    }

                    pickUpWishLists(wishListIds);

                } catch (JSONException je) {
                    Toast toast = Toast.makeText(FriendsWishListsActivity.this,
                            getApplicationContext().getString(R.string.json_exception),
                            Toast.LENGTH_SHORT);
                    View toastView = toast.getView();
                    toastView.setBackgroundResource(R.color.background_color);
                    toast.show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast toast = Toast.makeText(FriendsWishListsActivity.this,
                        getApplicationContext().getString(R.string.get_wish_list_error_message),
                        Toast.LENGTH_LONG);
                View toastView = toast.getView();
                toastView.setBackgroundResource(R.color.background_color);
                toast.show();
            }
        });
    }

    public void pickUpWishLists(final int[] wishListIds) {

        restWishListService.getService().getAllWishLists(new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                try {
                    JSONArray allWishLists = new JSONArray(new String(((TypedByteArray) response.getBody()).getBytes()));
                    List<WishList> confirmedWishLists = new ArrayList<>();

                    for(int i = 0; i < allWishLists.length(); i++) {
                        JSONObject wishListObj = allWishLists.getJSONObject(i);
                        int wishListIdObj = wishListObj.getInt("id");

                        for(int j = 0; j < wishListIds.length; j++) {
                            if(wishListIds[j] == wishListIdObj) {
                                WishList oneList = new WishList();
                                oneList.ID = wishListObj.getInt("id");
                                oneList.Name = wishListObj.getString("name");
                                oneList.OwnerId = wishListObj.getInt("ownerId");

                                confirmedWishLists.add(oneList);
                            }
                        }
                    }

                    showRelevantWishLists(confirmedWishLists);

                } catch (JSONException je) {
                    Toast toast = Toast.makeText(FriendsWishListsActivity.this,
                            getApplicationContext().getString(R.string.json_exception),
                            Toast.LENGTH_SHORT);
                    View toastView = toast.getView();
                    toastView.setBackgroundResource(R.color.background_color);
                    toast.show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast toast = Toast.makeText(FriendsWishListsActivity.this,
                        getApplicationContext().getString(R.string.wish_list_error_message),
                        Toast.LENGTH_SHORT);
                View toastView = toast.getView();
                toastView.setBackgroundResource(R.color.background_color);
                toast.show();
            }
        });
    }

    public void showRelevantWishLists(List<WishList> lists) {
        // place each wish list from the list sharedWishLists into the ListView:
        ListView listView = (ListView) findViewById(R.id.friendsListsListView);
        listView.setAdapter(new ArrayAdapter<>(FriendsWishListsActivity.this,
                android.R.layout.simple_selectable_list_item, lists));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListView lv = (ListView) parent;
                WishList w = (WishList) lv.getItemAtPosition((int) id);

                // Go to EditWishListActivity and send in the id and name of the wish list selected:
                Intent i = new Intent(FriendsWishListsActivity.this, WatchWishListActivity.class);
                i.putExtra("WISHLISTID", w.ID);
                i.putExtra("WISHLISTNAME", w.Name);
                i.putExtra("OWNERID", w.OwnerId);
                startActivity(i);
                finish();
            }
        });
    }

    // Onclick find friends-button
    public void findFriends(View view) {
        Intent i = new Intent(this, FindFriendsActivity.class);
        i.putExtra("USERID", userId);
        startActivity(i);
        finish();
    }

    // Onclick received requests-button
    public void receivedRequests(View view) {
        Intent i = new Intent(this, ReceivedRequestsActivity.class);
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