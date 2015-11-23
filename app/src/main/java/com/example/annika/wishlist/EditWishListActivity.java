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

public class EditWishListActivity extends AppCompatActivity implements DeleteWishDialog.DialogClickListener {

    private int wishListId;
    private String wishListName;
    private RestWishService restWishService;

    // DeleteWishDialog-method
    @Override
    public void onDeleteWishClick(int wishId) {
        // delete wish with id wishId

        restWishService.getService().deleteWishById(wishId, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                Toast toast = Toast.makeText(EditWishListActivity.this,
                        getApplicationContext().getString(R.string.wish_deleted),
                        Toast.LENGTH_SHORT);
                View toastView = toast.getView();
                toastView.setBackgroundResource(R.color.background_color);
                toast.show();

                // Refresh activity:
                Intent i = new Intent(EditWishListActivity.this, EditWishListActivity.class);
                i.putExtra("WISHLISTID", wishListId);
                i.putExtra("WISHLISTNAME", wishListName);
                startActivity(i);
                finish();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast toast = Toast.makeText(EditWishListActivity.this,
                        getApplicationContext().getString(R.string.wish_deleted_error_message),
                        Toast.LENGTH_SHORT);
                View toastView = toast.getView();
                toastView.setBackgroundResource(R.color.background_color);
                toast.show();
            }
        });
    }

    // DeleteWishDialog-method
    @Override
    public void onCancelDeleteWishClick() {
        // do nothing
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_wish_list);

        wishListId = getIntent().getIntExtra("WISHLISTID", -1);
        wishListName = getIntent().getStringExtra("WISHLISTNAME");

        TextView headerTextView = (TextView) findViewById(R.id.wishListNameHeader);
        headerTextView.setText(wishListName);

        restWishService = new RestWishService();

        // get all wishes for this wish list:
        restWishService.getService().getAllWishesForWishList(wishListId, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                try {
                    JSONArray wishes = new JSONArray(new String(((TypedByteArray) response.getBody()).getBytes()));

                    // place each wish into the ListView:

                    ListView listView = (ListView) findViewById(R.id.wishesListView);
                    int length = wishes.length();
                    List<Wish> allWishes = new ArrayList<>(length);

                    for (int i = 0; i < length; i++)
                    {
                        JSONObject oneWish = wishes.getJSONObject(i);

                        Wish wish = new Wish();
                        wish.ID = oneWish.getInt("id");
                        wish.Name = oneWish.getString("name");
                        wish.Spesification = oneWish.getString("spesification");

                        // TODO: wish.Image = oneWish.getString("image");

                        wish.Link = oneWish.getString("link");
                        wish.Price = oneWish.getDouble("price");
                        wish.Where = oneWish.getString("where");
                        wish.WishListId = oneWish.getInt("wishListId");

                        allWishes.add(wish);
                    }

                    listView.setAdapter(new ArrayAdapter<>(EditWishListActivity.this,
                            android.R.layout.simple_selectable_list_item, allWishes));

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            ListView lv = (ListView) parent;
                            //TextView tv = (TextView) lv.getChildAt(position);

                            Wish w = (Wish) lv.getItemAtPosition((int) id);

                            // Go to EditWishListActivity and send in the id and name of the wish list selected:
                            Intent i = new Intent(EditWishListActivity.this, EditWishActivity.class);
                            i.putExtra("WISHID", w.ID);
                            i.putExtra("WISHNAME", w.Name);
                            i.putExtra("WISHSPESIFICATION", w.Spesification);

                            // TODO: POSSIBLE?:
                            i.putExtra("WISHIMAGE", w.Image);

                            i.putExtra("WISHLINK", w.Link);
                            i.putExtra("WISHPRICE", w.Price);
                            i.putExtra("WISHWHERE", w.Where);
                            i.putExtra("WISHWISHLISTID", w.WishListId);
                            startActivity(i);
                            finish();
                        }
                    });

                    // Possible to delete wish when long click:
                    listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                            // Get the selected Wish:
                            ListView lv = (ListView) parent;
                            Wish w = (Wish) lv.getItemAtPosition((int) id);

                            // Dialogbox for deleting the wish list (DeleteWishListDialog):
                            String message = getString(R.string.delete_wish_dialog_message) + " " + w.Name + "?";
                            int wishId = w.ID;
                            DeleteWishDialog dialog = DeleteWishDialog.newInstance(message, wishId);
                            dialog.show(getFragmentManager(), "DELETE");
                            // waiting for the user to make a choice: Delete or Cancel

                            return true;
                        }
                    });
                }
                catch (JSONException je) {
                    Toast toast = Toast.makeText(EditWishListActivity.this,
                            getApplicationContext().getString(R.string.json_exception),
                            Toast.LENGTH_SHORT);
                    View toastView = toast.getView();
                    toastView.setBackgroundResource(R.color.background_color);
                    toast.show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast toast = Toast.makeText(EditWishListActivity.this,
                        getApplicationContext().getString(R.string.get_wish_error_message),
                        Toast.LENGTH_SHORT);
                View toastView = toast.getView();
                toastView.setBackgroundResource(R.color.background_color);
                toast.show();
            }
        });
    }

    // Onclick add button
    public void createNewWish(View view) {
        Intent i = new Intent(this, NewWishActivity.class);
        i.putExtra("WISHLISTID", wishListId);
        startActivity(i);
        finish();
    }

    // Onclick edit button
    public void editWishListName(View view) {


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