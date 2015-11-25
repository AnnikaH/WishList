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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

public class NewTipActivity extends AppCompatActivity {

    private int userId;
    private RestUserService restUserService;
    private RestTipService restTipService;
    private User foundUser;
    private Button userNameCheckButton;
    private boolean registerButtonPushed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_tip);

        userId = getIntent().getIntExtra("USERID", -1);

        restUserService = new RestUserService();
        restTipService = new RestTipService();

        userNameCheckButton = (Button) findViewById(R.id.userNameCheckButton);

        registerButtonPushed = false;
    }

    // Onclick search-button
    public void searchForFriendByUserName(final View view) {

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

                            userNameCheckButton.setBackgroundResource(R.drawable.checkmark_100);

                            if(registerButtonPushed) {
                                registerButtonPushed = false;   // reset
                                registerTipInDatabase(view);
                            }

                            return; // don't need to search any more
                        }
                    }

                    // if gets here there was no user with this username:
                    userNameCheckButton.setBackgroundResource(R.drawable.delete_cross_100);
                    registerButtonPushed = false;

                } catch (JSONException je) {
                    Toast toast = Toast.makeText(NewTipActivity.this,
                            getApplicationContext().getString(R.string.json_exception),
                            Toast.LENGTH_SHORT);
                    View toastView = toast.getView();
                    toastView.setBackgroundResource(R.color.background_color);
                    toast.show();

                    registerButtonPushed = false;
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast toast = Toast.makeText(NewTipActivity.this,
                        getApplicationContext().getString(R.string.user_error_message),
                        Toast.LENGTH_SHORT);
                View toastView = toast.getView();
                toastView.setBackgroundResource(R.color.background_color);
                toast.show();

                registerButtonPushed = false;
            }
        });
    }

    // Onclick register-button
    public void registerNewTip(View view) {
        registerButtonPushed = true;
        searchForFriendByUserName(view);    // to check the user name that's been written +
        // store this users values in foundUser
        // The rest of the code for what should happen if user name valid is written inside
        // searchForFriendByUserName-method because it's an async call
    }

    public void registerTipInDatabase(View view) {
        // register button has been pushed and we have the foundUser values (are valid)

        // Get tip values:
        EditText tipNameEdit = (EditText) findViewById(R.id.tipName);
        EditText tipLinkEdit = (EditText) findViewById(R.id.tipLink);
        EditText tipPriceEdit = (EditText) findViewById(R.id.tipPrice);
        EditText tipSpesEdit = (EditText) findViewById(R.id.tipSpesification);
        EditText tipWhereEdit = (EditText) findViewById(R.id.tipWhere);
        // TODO: ImageView imageView = (ImageView) findViewById(R.id.tipImage);

        String name = tipNameEdit.getText().toString();
        String link = tipLinkEdit.getText().toString();
        String priceString = tipPriceEdit.getText().toString();
        String spes = tipSpesEdit.getText().toString();
        String where = tipWhereEdit.getText().toString();
        double price;

        try {
            price = Double.parseDouble(priceString);
        } catch (NumberFormatException nfe) {
            price = 0;
        }

        // Input validation (not all fields have to be filled in):

        // backend: [RegularExpression("^[a-zæøåA-ZÆØÅ0-9., \\-]{2,30}$")]
        if(!name.matches("^[a-zæøåA-ZÆØÅ0-9., \\-]{2,30}$")) {
            tipNameEdit.setError(getString(R.string.name_error_message));
            return;
        }

        // backend: not required, but if filled in: [RegularExpression("^[0-9\\.]{0,9}$")]
        if(!priceString.matches("^[0-9\\.]{0,9}$")) {
            tipPriceEdit.setError(getString(R.string.price_error_message));
            return;
        }

        WishTip tip = new WishTip();
        tip.Link = link;
        tip.Where = where;
        tip.Name = name;
        tip.Price = price;
        tip.Spesification = spes;
        tip.SenderId = userId;
        tip.ReceiverId = foundUser.ID;
        // TODO: tip.Image = ...;

        // disable button click (in case it takes some time):
        final Button button = (Button) view;
        button.setEnabled(false);

        // post:
        restTipService.getService().addTip(tip, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                Toast toast = Toast.makeText(NewTipActivity.this,
                        getApplicationContext().getString(R.string.tip_registered),
                        Toast.LENGTH_SHORT);
                View toastView = toast.getView();
                toastView.setBackgroundResource(R.color.background_color);
                toast.show();

                Intent i = new Intent(NewTipActivity.this, TipsFriendsActivity.class);
                i.putExtra("USERID", userId);
                startActivity(i);
                finish();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast toast = Toast.makeText(NewTipActivity.this,
                        getApplicationContext().getString(R.string.tip_registered_error_message),
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
    public void cancelNewTip(View view) {
        Intent i = new Intent(this, TipsFriendsActivity.class);
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