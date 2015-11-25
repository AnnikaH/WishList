package com.example.annika.wishlist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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

public class FindFriendsActivity extends AppCompatActivity implements RequestDialog.DialogClickListener, SMSDialog.DialogClickListener {

    private RestUserService restUserService;
    private RestWishListService restWishListService;
    private RestSharingService restSharingService;
    private User foundUser;
    private int userId;

    // RequestDialog-method:
    public void onSendRequestClick(int requestedWishListId) {
        // post a new Sharing with this wishListId and this userId and set Confirmed to false (awaiting answer):

        if(userId == -1) {
            Toast toast = Toast.makeText(FindFriendsActivity.this,
                    getApplicationContext().getString(R.string.sharing_added_error_message),
                    Toast.LENGTH_SHORT);
            View toastView = toast.getView();
            toastView.setBackgroundResource(R.color.background_color);
            toast.show();
            return;
        }

        Sharing sharing = new Sharing();
        sharing.UserId = userId;
        sharing.WishListId = requestedWishListId;
        sharing.Confirmed = false;

        // post:
        restSharingService.getService().addSharing(sharing, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                Toast toast = Toast.makeText(FindFriendsActivity.this,
                        getApplicationContext().getString(R.string.sharing_added),
                        Toast.LENGTH_SHORT);
                View toastView = toast.getView();
                toastView.setBackgroundResource(R.color.background_color);
                toast.show();

                // TODO: SMSDIALOG BOX IF THE USER WANTS TO SEND AN SMS TO NOTIFY THE OWNER OF THE WISH LIST
                String message = getString(R.string.sms_dialog_message);
                SMSDialog dialog = SMSDialog.newInstance(message);
                dialog.show(getFragmentManager(), "SMS");
                // waiting for the user to make a choice: Send or Cancel
            }

            @Override
            public void failure(RetrofitError error) {
                Toast toast = Toast.makeText(FindFriendsActivity.this,
                        getApplicationContext().getString(R.string.sharing_added_error_message),
                        Toast.LENGTH_LONG);
                View toastView = toast.getView();
                toastView.setBackgroundResource(R.color.background_color);
                toast.show();
            }
        });
    }

    // RequestDialog-method:
    public void onCancelRequestClick() {
        // do nothing
    }

    // SMSDialog-method:
    public void onSendSMSClick() {
        // send an SMS to the foundUser to notify him/her that you want access to the specified
        // wishlist (listId/listName) and that he/she needs to check their app to confirm:
        String phoneNumber = foundUser.PhoneNumber;
        String message = getString(R.string.sms_message);
        SmsManager manager = SmsManager.getDefault();
        manager.sendTextMessage(phoneNumber, null, message, null, null);

        Toast toast = Toast.makeText(FindFriendsActivity.this,
                getApplicationContext().getString(R.string.sms_sent),
                Toast.LENGTH_SHORT);
        View toastView = toast.getView();
        toastView.setBackgroundResource(R.color.background_color);
        toast.show();
    }

    // SMSDialog-method:
    public void onCancelSMSClick() {
        // do nothing
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friends);

        userId = getIntent().getIntExtra("USERID", -1);

        restUserService = new RestUserService();
        restWishListService = new RestWishListService();
        restSharingService = new RestSharingService();
    }

    // Onclick search-button:
    public void searchForFriendByUserName(View view) {
        EditText userNameEdit = (EditText) findViewById(R.id.userNameEditSearch);
        final String userNameSearched = userNameEdit.getText().toString();

        restUserService.getService().getAllUsers(new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                try {
                    JSONArray allUsers = new JSONArray(new String(((TypedByteArray) response.getBody()).getBytes()));

                    for(int i = 0; i < allUsers.length(); i++) {
                        JSONObject user = allUsers.getJSONObject(i);

                        if(user.getString("userName").equals(userNameSearched)) {
                            foundUser = new User();
                            foundUser.ID = user.getInt("id");
                            foundUser.UserName = user.getString("userName");
                            foundUser.Email = user.getString("email");
                            foundUser.PhoneNumber = user.getString("phoneNumber");
                            foundUser.Password = "";

                            getFoundUsersWishLists();

                            return; // don't need to search any more
                        }
                    }

                    // if gets here there was no user with this username:
                    Toast toast = Toast.makeText(FindFriendsActivity.this,
                            getApplicationContext().getString(R.string.no_user),
                            Toast.LENGTH_SHORT);
                    View toastView = toast.getView();
                    toastView.setBackgroundResource(R.color.background_color);
                    toast.show();

                } catch (JSONException je) {
                    Toast toast = Toast.makeText(FindFriendsActivity.this,
                            getApplicationContext().getString(R.string.json_exception),
                            Toast.LENGTH_SHORT);
                    View toastView = toast.getView();
                    toastView.setBackgroundResource(R.color.background_color);
                    toast.show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast toast = Toast.makeText(FindFriendsActivity.this,
                        getApplicationContext().getString(R.string.user_error_message),
                        Toast.LENGTH_SHORT);
                View toastView = toast.getView();
                toastView.setBackgroundResource(R.color.background_color);
                toast.show();
            }
        });
    }

    public void getFoundUsersWishLists() {
        int uId = foundUser.ID;
        final ListView listView = (ListView) findViewById(R.id.friendWishListsListView);

        restWishListService.getService().getAllWishListsForUser(uId, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                try {
                    JSONArray wishLists = new JSONArray(new String(((TypedByteArray) response.getBody()).getBytes()));

                    // place each wish list into the ListView:

                    int length = wishLists.length();
                    List<WishList> lists = new ArrayList<>(length);

                    for (int i = 0; i < length; i++)
                    {
                        JSONObject oneList = wishLists.getJSONObject(i);

                        WishList wishList = new WishList();
                        wishList.ID = oneList.getInt("id");
                        wishList.Name = oneList.getString("name");
                        wishList.OwnerId = oneList.getInt("ownerId");

                        lists.add(wishList);
                    }

                    if(lists.isEmpty()) {
                        Toast toast = Toast.makeText(FindFriendsActivity.this,
                                getApplicationContext().getString(R.string.no_wish_lists),
                                Toast.LENGTH_SHORT);
                        View toastView = toast.getView();
                        toastView.setBackgroundResource(R.color.background_color);
                        toast.show();
                    }

                    listView.setAdapter(new ArrayAdapter<>(FindFriendsActivity.this,
                            android.R.layout.simple_selectable_list_item, lists));

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            ListView lv = (ListView) parent;
                            WishList w = (WishList) lv.getItemAtPosition((int) id);

                            // Open dialog box to send request to the owner of the wish list:
                            String message = getString(R.string.request_dialog_message);
                            int wishListId = w.ID;
                            RequestDialog dialog = RequestDialog.newInstance(message, wishListId);
                            dialog.show(getFragmentManager(), "REQUEST");
                            // waiting for the user to make a choice: Send or Cancel
                        }
                    });
                } catch (JSONException je) {
                    Toast toast = Toast.makeText(FindFriendsActivity.this,
                            getApplicationContext().getString(R.string.json_exception),
                            Toast.LENGTH_SHORT);
                    View toastView = toast.getView();
                    toastView.setBackgroundResource(R.color.background_color);
                    toast.show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast toast = Toast.makeText(FindFriendsActivity.this,
                        getApplicationContext().getString(R.string.get_wish_list_error_message),
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