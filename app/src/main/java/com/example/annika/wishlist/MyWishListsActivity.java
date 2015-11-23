package com.example.annika.wishlist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MyWishListsActivity extends AppCompatActivity implements NewWishListDialog.DialogClickListener {

    private int userId;

    // NewWishListDialog-method
    @Override
    public void onSaveClick()
    {

    }

    // NewWishListDialog-method
    @Override
    public void onCancelClick()
    {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wish_lists);

        userId = getIntent().getIntExtra("USERID", -1);

        // get all wish lists for this user:


    }

    // Onclick new list-button
    public void createNewList(View view)
    {
        // dialogbox (NewWishListDialog) hvor kan fylle inn navn på den nye listen vi vil lage:
        String message = getString(R.string.new_list_dialog_message);
        NewWishListDialog dialog = NewWishListDialog.newInstance(message);
        dialog.show(getFragmentManager(), "CREATE");
        // waiting for the user to make a choice: Create og Cancel
    }

    // Onclick main menu-button
    public void goToMainMenu(View view)
    {
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