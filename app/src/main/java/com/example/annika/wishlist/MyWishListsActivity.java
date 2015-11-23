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

public class MyWishListsActivity extends AppCompatActivity implements NewWishListDialog.DialogClickListener,
        DeleteWishListDialog.DialogClickListener {

    private int userId;
    private RestWishListService restWishListService;

    // NewWishListDialog-method
    @Override
    public void onSaveClick(String wishListName)
    {
        // create/add new wish list for this user (with userId)

        WishList wishList = new WishList();
        wishList.Name = wishListName;
        wishList.OwnerId = userId;

        // post:
        restWishListService.getService().addWishList(wishList, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                Toast toast = Toast.makeText(MyWishListsActivity.this,
                        getApplicationContext().getString(R.string.wish_list_added),
                        Toast.LENGTH_SHORT);
                View toastView = toast.getView();
                toastView.setBackgroundResource(R.color.background_color);
                toast.show();

                // and refresh the activity
                Intent i = new Intent(MyWishListsActivity.this, MyWishListsActivity.class);
                i.putExtra("USERID", userId);
                startActivity(i);
                finish();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast toast = Toast.makeText(MyWishListsActivity.this,
                        getApplicationContext().getString(R.string.wish_list_added_error_message),
                        Toast.LENGTH_SHORT);
                View toastView = toast.getView();
                toastView.setBackgroundResource(R.color.background_color);
                toast.show();
            }
        });
    }

    // NewWishListDialog-method
    @Override
    public void onCancelClick() {
        // do nothing
    }

    // DeleteWishListDialog-method
    @Override
    public void onDeleteClick(int wishListId) {
        // delete this wish list with id wishListId

        restWishListService.getService().deleteWishListById(wishListId, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                Toast toast = Toast.makeText(MyWishListsActivity.this,
                        getApplicationContext().getString(R.string.wish_list_deleted),
                        Toast.LENGTH_SHORT);
                View toastView = toast.getView();
                toastView.setBackgroundResource(R.color.background_color);
                toast.show();

                // Refresh activity:
                Intent i = new Intent(MyWishListsActivity.this, MyWishListsActivity.class);
                i.putExtra("USERID", userId);
                startActivity(i);
                finish();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast toast = Toast.makeText(MyWishListsActivity.this,
                        getApplicationContext().getString(R.string.wish_list_deleted_error_message),
                        Toast.LENGTH_SHORT);
                View toastView = toast.getView();
                toastView.setBackgroundResource(R.color.background_color);
                toast.show();
            }
        });
    }

    // DeleteWishListDialog-method
    public void onCancelDeleteClick() {
        // do nothing
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wish_lists);

        userId = getIntent().getIntExtra("USERID", -1);

        restWishListService = new RestWishListService();

        // get all wish lists for this user:
        restWishListService.getService().getAllWishListsForUser(userId, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                try {
                    JSONArray wishLists = new JSONArray(new String(((TypedByteArray) response.getBody()).getBytes()));

                    // place each wish list into the ListView:

                    ListView listView = (ListView) findViewById(R.id.wishListsListView);
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

                    listView.setAdapter(new ArrayAdapter<>(MyWishListsActivity.this,
                            android.R.layout.simple_selectable_list_item, lists));

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            ListView lv = (ListView) parent;
                            //TextView tv = (TextView) lv.getChildAt(position);

                            WishList w = (WishList) lv.getItemAtPosition((int) id);

                            // Go to EditWishListActivity and send in the id and name of the wish list selected:
                            Intent i = new Intent(MyWishListsActivity.this, EditWishListActivity.class);
                            i.putExtra("WISHLISTID", w.ID);
                            i.putExtra("WISHLISTNAME", w.Name);
                            startActivity(i);
                            finish();
                        }
                    });

                    // Possible to delete wish list when long click:
                    listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                            // Get the selected WishList:
                            ListView lv = (ListView) parent;
                            WishList w = (WishList) lv.getItemAtPosition((int) id);

                            // Dialogbox for deleting the wish list (DeleteWishListDialog):
                            String message = getString(R.string.delete_list_dialog_message) + " " + w.Name + "?";
                            int wishListId = w.ID;
                            DeleteWishListDialog dialog = DeleteWishListDialog.newInstance(message, wishListId);
                            dialog.show(getFragmentManager(), "DELETE");
                            // waiting for the user to make a choice: Delete or Cancel

                            return true;
                        }
                    });
                }
                catch (JSONException je) {
                    Toast toast = Toast.makeText(MyWishListsActivity.this, getApplicationContext().getString(R.string.json_exception),
                            Toast.LENGTH_SHORT);
                    View toastView = toast.getView();
                    toastView.setBackgroundResource(R.color.background_color);
                    toast.show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast toast = Toast.makeText(MyWishListsActivity.this, getApplicationContext().getString(R.string.wish_list_added_error_message),
                        Toast.LENGTH_SHORT);
                View toastView = toast.getView();
                toastView.setBackgroundResource(R.color.background_color);
                toast.show();
            }
        });
    }

    // Onclick new list-button
    public void createNewList(View view) {
        // Dialogbox (NewWishListDialog) where the user can fill in name of new list:
        String message = getString(R.string.new_list_dialog_message);
        NewWishListDialog dialog = NewWishListDialog.newInstance(message);
        dialog.show(getFragmentManager(), "CREATE");
        // waiting for the user to make a choice: Create or Cancel
    }

    // Onclick main menu-button
    public void goToMainMenu(View view) {
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