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

public class ReceivedRequestsActivity extends AppCompatActivity {

    private int userId;
    private RestSharingService restSharingService;
    private RestWishListService restWishListService;
    private RestUserService restUserService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_received_requests);

        userId = getIntent().getIntExtra("USERID", -1);

        restSharingService = new RestSharingService();
        restWishListService = new RestWishListService();
        restUserService = new RestUserService();

        // get all wishlists where you are the owner and then check all sharings to check if
        // one of your wish lists is part of a sharing that is false:
        // Onclick one of the sharings: dialog box: confirm or cancel:

        restWishListService.getService().getAllWishListsForUser(userId, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                try {
                    JSONArray yourWishLists = new JSONArray(new String(((TypedByteArray) response.getBody()).getBytes()));

                    //int[] wishListIds = new int[yourWishLists.length()];
                    List<WishList> lists = new ArrayList<>();

                    for(int i = 0; i < yourWishLists.length(); i++) {
                        JSONObject wishListObject = yourWishLists.getJSONObject(i);

                        WishList w = new WishList();
                        w.ID = wishListObject.getInt("id");
                        w.Name = wishListObject.getString("name");
                        w.OwnerId = wishListObject.getInt("ownerId");

                        lists.add(w);
                        //wishListIds[i] = wishListObject.getInt("id");
                    }

                    //checkIfWishListsArePartOfSharings(wishListIds);
                    checkIfWishListsArePartOfSharings(lists);

                } catch (JSONException je) {
                    Toast toast = Toast.makeText(ReceivedRequestsActivity.this,
                            getApplicationContext().getString(R.string.json_exception),
                            Toast.LENGTH_SHORT);
                    View toastView = toast.getView();
                    toastView.setBackgroundResource(R.color.background_color);
                    toast.show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast toast = Toast.makeText(ReceivedRequestsActivity.this,
                        getApplicationContext().getString(R.string.get_wish_list_error_message),
                        Toast.LENGTH_SHORT);
                View toastView = toast.getView();
                toastView.setBackgroundResource(R.color.background_color);
                toast.show();
            }
        });
    }

    //public void checkIfWishListsArePartOfSharings(final int[] wishListIds)
    public void checkIfWishListsArePartOfSharings(final List<WishList> lists)
    {
        // check all sharings to check if one of your wish lists (wishListIds) is part of a
        // sharing that is false:
        // Add these then to the ListView

        restSharingService.getService().getAllSharings(new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                try {
                    JSONArray allSharings = new JSONArray(new String(((TypedByteArray) response.getBody()).getBytes()));
                    List<Sharing> requests = new ArrayList<>();
                    List<WishList> foundWishLists = new ArrayList<>();

                    for (int i = 0; i < lists.size(); i++) {

                        WishList wishList = lists.get(i);
                        //int listId = wishListIds[i];
                        int listId = wishList.ID;

                        // check all the sharings to see if this wish list is part of any:
                        for (int j = 0; j < allSharings.length(); j++) {
                            JSONObject sharingObject = allSharings.getJSONObject(j);

                            boolean confirmed = sharingObject.getBoolean("confirmed");
                            int sharingListId = sharingObject.getInt("wishListId");

                            if (!confirmed && sharingListId == listId) {
                                // add this sharing to your sharings (to add to the ListView):
                                Sharing sharing = new Sharing();
                                sharing.ID = sharingObject.getInt("id");
                                sharing.WishListId = sharingListId;
                                sharing.Confirmed = false;  // confirmed is always false here
                                sharing.UserId = sharingObject.getInt("userId");

                                requests.add(sharing);
                            }
                        }
                    }

                    pickUpAllUsers(requests, lists);

                } catch (JSONException je) {
                    Toast toast = Toast.makeText(ReceivedRequestsActivity.this,
                            getApplicationContext().getString(R.string.json_exception),
                            Toast.LENGTH_SHORT);
                    View toastView = toast.getView();
                    toastView.setBackgroundResource(R.color.background_color);
                    toast.show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast toast = Toast.makeText(ReceivedRequestsActivity.this,
                        getApplicationContext().getString(R.string.get_sharing_error_message),
                        Toast.LENGTH_SHORT);
                View toastView = toast.getView();
                toastView.setBackgroundResource(R.color.background_color);
                toast.show();
            }
        });
    }

    public void pickUpAllUsers(final List<Sharing> relevantSharings, final List<WishList> wishLists)
    {
        restUserService.getService().getAllUsers(new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                try {
                    JSONArray allUsers = new JSONArray(new String(((TypedByteArray) response.getBody()).getBytes()));
                    List<User> users = new ArrayList<>();

                    for (int i = 0; i < allUsers.length(); i++) {
                        JSONObject userObj = allUsers.getJSONObject(i);
                        User user = new User();
                        user.ID = userObj.getInt("id");
                        user.Password = "";
                        user.PhoneNumber = userObj.getString("phoneNumber");
                        user.Email = userObj.getString("email");
                        user.UserName = userObj.getString("userName");

                        users.add(user);
                    }

                    showRequests(relevantSharings, wishLists, users);

                } catch (JSONException je) {
                    Toast toast = Toast.makeText(ReceivedRequestsActivity.this,
                            getApplicationContext().getString(R.string.json_exception),
                            Toast.LENGTH_SHORT);
                    View toastView = toast.getView();
                    toastView.setBackgroundResource(R.color.background_color);
                    toast.show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast toast = Toast.makeText(ReceivedRequestsActivity.this,
                        getApplicationContext().getString(R.string.get_user_error_message),
                        Toast.LENGTH_SHORT);
                View toastView = toast.getView();
                toastView.setBackgroundResource(R.color.background_color);
                toast.show();
            }
        });
    }

    public void showRequests(List<Sharing> relevantSharings, List<WishList> wishLists, List<User> users) {
        // Add requests to the ListView, but have to get the username and wishlistname first:
        // Onclick one of the added objects: dialog box: confirm or cancel:

        ListView listView = (ListView) findViewById(R.id.receivedRequestsListView);
        List<Request> requests = new ArrayList<>();

        // for each Sharing we create a Request and fill in the values:
        for(int i = 0; i < relevantSharings.size(); i++) {
            Sharing sharing = relevantSharings.get(i);
            Request request = new Request();
            request.UserId = sharing.UserId;
            request.WishListId = sharing.WishListId;
            request.SharingID = sharing.ID;
            request.Confirmed = sharing.Confirmed;
            // lacking now: request.UserName and request.WishListName

            // getting request.WishListName:
            for(int j = 0; j < wishLists.size(); j++) {
                WishList w = wishLists.get(j);

                if(sharing.WishListId == w.ID) {
                    request.WishListName = w.Name;
                    break;  // break out of the inner loop
                }
            }

            // getting request.UserName:
            for(int k = 0; k < users.size(); k++) {
                User u = users.get(k);

                if(u.ID == sharing.UserId) {
                    request.UserName = u.UserName;
                    break; // break out of the inner loop
                }
            }

            requests.add(request);
        }

        // adding requests to the listView (showing the name of the wish list + the name of
        // the user who wants access to it):
        listView.setAdapter(new ArrayAdapter<>(ReceivedRequestsActivity.this,
                android.R.layout.simple_selectable_list_item, requests));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListView lv = (ListView) parent;
                Request r = (Request) lv.getItemAtPosition((int) id);

                // TODO: Confirm dialog box:


                /* Go to EditWishListActivity and send in the id and name of the wish list selected:
                Intent i = new Intent(FriendsWishListsActivity.this, WatchWishListActivity.class);
                i.putExtra("WISHLISTID", w.ID);
                i.putExtra("WISHLISTNAME", w.Name);
                i.putExtra("OWNERID", w.OwnerId);
                startActivity(i);
                finish();*/
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