package com.example.annika.wishlist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class EditProfileActivity extends AppCompatActivity {

    private int userId;
    private String userName;
    private String email;
    private String mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        userId = getIntent().getIntExtra("USERID", -1);
        userName = getIntent().getStringExtra("USERNAME");
        email = getIntent().getStringExtra("EMAIL");
        mobile = getIntent().getStringExtra("MOBILE");

        // insert values into the EditTexts:
        EditText userNameEditText = (EditText) findViewById(R.id.userNameEdit);
        EditText passwordEditText = (EditText) findViewById(R.id.passwordEdit);
        EditText emailEditText = (EditText) findViewById(R.id.emailEdit);
        EditText mobileEditText = (EditText) findViewById(R.id.mobileNumberEdit);

        userNameEditText.setText(userName);
        passwordEditText.setText("");
        emailEditText.setText(email);
        mobileEditText.setText(mobile);
    }

    // Onclick save-button:
    public void saveChanges(View view)
    {
        // save changes in the database (put):


        Intent i = new Intent(this, ProfileActivity.class);
        i.putExtra("USERID", userId);
        startActivity(i);
        finish();
    }

    // Onclick cancel-button:
    public void cancelEditProfile()
    {
        Intent i = new Intent(this, ProfileActivity.class);
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