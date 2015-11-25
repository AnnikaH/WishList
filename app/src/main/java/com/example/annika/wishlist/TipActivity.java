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
import org.w3c.dom.Text;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

public class TipActivity extends AppCompatActivity {

    private int tipId;
    private int userId;
    RestTipService restTipService;
    RestUserService restUserService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tip);

        tipId = getIntent().getIntExtra("TIPID", -1);
        userId = getIntent().getIntExtra("USERID", -1);

        restTipService = new RestTipService();
        restUserService = new RestUserService();

        // get the tip from the database and show the tip and the user name to the sender:
        restTipService.getService().getTipById(tipId, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                try {
                    JSONObject tip = new JSONObject(new String(((TypedByteArray) response.getBody()).getBytes()));

                    String tipName = tip.getString("name");
                    String tipSpesification = tip.getString("spesification");
                    double tipPrice = tip.getDouble("price");
                    String tipLink = tip.getString("link");
                    String tipWhere = tip.getString("where");
                    // TODO: image
                    int tipSenderId = tip.getInt("senderId");
                    // int tipReceiverId = tip.getInt("receiverId");

                    TextView nameText = (TextView) findViewById(R.id.tipName);
                    TextView spesText = (TextView) findViewById(R.id.tipSpesification);
                    TextView whereText = (TextView) findViewById(R.id.tipWhere);
                    TextView linkText = (TextView) findViewById(R.id.tipLink);
                    TextView priceText = (TextView) findViewById(R.id.tipPrice);

                    nameText.setText(tipName);
                    spesText.setText(tipSpesification);
                    whereText.setText(tipWhere);
                    linkText.setText(tipLink);
                    priceText.setText(tipPrice + " kr");

                    showSender(tipSenderId);

                } catch (JSONException je) {
                    Toast toast = Toast.makeText(TipActivity.this,
                            getApplicationContext().getString(R.string.json_exception),
                            Toast.LENGTH_SHORT);
                    View toastView = toast.getView();
                    toastView.setBackgroundResource(R.color.background_color);
                    toast.show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast toast = Toast.makeText(TipActivity.this,
                        getApplicationContext().getString(R.string.get_tip_error_message),
                        Toast.LENGTH_LONG);
                View toastView = toast.getView();
                toastView.setBackgroundResource(R.color.background_color);
                toast.show();
            }
        });
    }

    public void showSender(int senderId) {
        // find user name to sender with senderId and show it in the TextView
        restUserService.getService().getUserById(senderId, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                try {
                    JSONObject user = new JSONObject(new String(((TypedByteArray) response.getBody()).getBytes()));
                    TextView senderText = (TextView) findViewById(R.id.tipSender);
                    senderText.setText(user.getString("userName"));
                } catch (JSONException je) {
                    Toast toast = Toast.makeText(TipActivity.this,
                            getApplicationContext().getString(R.string.json_exception),
                            Toast.LENGTH_SHORT);
                    View toastView = toast.getView();
                    toastView.setBackgroundResource(R.color.background_color);
                    toast.show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast toast = Toast.makeText(TipActivity.this,
                        getApplicationContext().getString(R.string.get_user_error_message),
                        Toast.LENGTH_LONG);
                View toastView = toast.getView();
                toastView.setBackgroundResource(R.color.background_color);
                toast.show();
            }
        });
    }

    // Onclick back-button
    public void backToTips(View view) {
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