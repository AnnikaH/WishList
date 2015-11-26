package com.example.annika.wishlist;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class NewWishActivity extends AppCompatActivity {

    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;
    private Uri fileUri;

    private int wishListId;
    private String wishListName;
    private int userId;
    private RestWishService restWishService;
    private Button button;
/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Image captured and saved to fileUri specified in the Intent
                Toast.makeText(this, "Image saved to:\n" +
                        data.getData(), Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_CANCELED) {
                // User cancelled the image capture
            } else {
                // Image capture failed, advise user
            }
        }
    }
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_wish);

        wishListId = getIntent().getIntExtra("WISHLISTID", -1);
        wishListName = getIntent().getStringExtra("WISHLISTNAME");
        userId = getIntent().getIntExtra("USERID", -1);

        restWishService = new RestWishService();
    }

    /** Create a file Uri for saving an image or video */
    /*private static Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }*/

    /** Create a File for saving an image or video */
  /*  private static File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }
*/
        // Create a media file name
       /* String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
        } else if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }*/

    // Onclick take picture-button
    public void takeAPicture(View view) {
        /*// create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // create a file to save the image
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name

        // start the image capture Intent
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
*/

    }

    // Onclick register new wish-button
    public void registerNewWish(View view) {
        EditText nameEdit = (EditText) findViewById(R.id.wishName);
        EditText spesEdit = (EditText) findViewById(R.id.wishSpesification);
        EditText whereEdit = (EditText) findViewById(R.id.wishWhere);
        EditText linkEdit = (EditText) findViewById(R.id.wishLink);
        EditText priceEdit = (EditText) findViewById(R.id.wishPrice);

        //TODO: ImageView imageView = (ImageView) findViewById(R.id.wishImage);

        String name = nameEdit.getText().toString();
        String spesification = spesEdit.getText().toString();
        String where = whereEdit.getText().toString();
        String link = linkEdit.getText().toString();
        String priceString = priceEdit.getText().toString();
        double price;

        try {
          price = Double.parseDouble(priceString);
        } catch (NumberFormatException nfe) {
            price = 0;
        }

        // Input validation (not all fields have to be filled in):

        // backend: [RegularExpression("^[a-zæøåA-ZÆØÅ0-9., \\-]{2,30}$")]
        if(!name.matches("^[a-zæøåA-ZÆØÅ0-9., \\-]{2,30}$")) {
            nameEdit.setError(getString(R.string.name_error_message));
            return;
        }

        // backend: not required, but if filled in: [RegularExpression("^[0-9\\.]{0,9}$")]
        if(!priceString.matches("^[0-9\\.]{0,9}$")) {
            priceEdit.setError(getString(R.string.price_error_message));
            return;
        }

        Wish wish = new Wish();
        wish.Name = name;
        wish.Spesification = spesification;
        wish.Where = where;
        wish.Link = link;
        wish.Price = price;
        wish.WishListId = wishListId;
        // TODO: wish.Image = image;
        wish.Bought = false;

        // disable button click (in case it takes some time):
        button = (Button) view;
        button.setEnabled(false);

        // post wish to server database:
        restWishService.getService().addWish(wish, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                Toast toast = Toast.makeText(NewWishActivity.this,
                        getApplicationContext().getString(R.string.wish_registered),
                        Toast.LENGTH_SHORT);
                View toastView = toast.getView();
                toastView.setBackgroundResource(R.color.background_color);
                toast.show();

                Intent i = new Intent(NewWishActivity.this, EditWishListActivity.class);
                i.putExtra("WISHLISTID", wishListId);
                i.putExtra("WISHLISTNAME", wishListName);
                i.putExtra("USERID", userId);
                startActivity(i);
                finish();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast toast = Toast.makeText(NewWishActivity.this,
                        getApplicationContext().getString(R.string.wish_registered_error_message),
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
    public void cancelNewWish(View view) {
        Intent i = new Intent(this, EditWishListActivity.class);
        i.putExtra("WISHLISTID", wishListId);
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