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

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class EditProfileActivity extends AppCompatActivity {

    private int userId;
    private String userName;
    private String email;
    private String mobile;
    private RestUserService restUserService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        restUserService = new RestUserService();

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
        EditText editTextUserName = (EditText) findViewById(R.id.userNameEdit);
        EditText editTextPassword = (EditText) findViewById(R.id.passwordEdit);
        EditText editTextEmail = (EditText) findViewById(R.id.emailEdit);
        EditText editTextMobile = (EditText) findViewById(R.id.mobileNumberEdit);

        String newUserName = editTextUserName.getText().toString();
        String newPassword = editTextPassword.getText().toString();
        String newEmail = editTextEmail.getText().toString();
        String newMobileNumber = editTextMobile.getText().toString();

        // Validation of input:

        // backend validation: [RegularExpression("[A-ZÆØÅa-zæøå0-9_\\-]{1,30}")]
        if(!newUserName.matches("[A-ZÆØÅa-zæøå0-9_\\-]{1,30}")) {
            editTextUserName.setError(getString(R.string.user_name_error_message));
            return;
        }

        // backend validation: [RegularExpression("[A-ZÆØÅa-zæøå0-9!#$%&'*+\\-/=?\\^_`{|}~+(\\.]{8,30}")]
        if(!newPassword.matches("[A-ZÆØÅa-zæøå0-9!#$%&'*+\\-/=?\\^_`{|}~+(\\.]{8,30}")) {
            editTextPassword.setError(getString(R.string.password_error_message));
            return;
        }

        // backend validation: [RegularExpression("^[-a-z0-9~!$%^&*_=+}{\'?]+(\\.[-a-z0-9~!$%^&*_=+}{\'?]+)*@([a-z0-9_][-a-z0-9_]*(\\.[-a-z0-9_]+)*\\.(aero|arpa|biz|com|coop|edu|gov|info|int|mil|museum|name|net|org|pro|travel|mobi|[a-z][a-z])|([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}))(:[0-9]{1,5})?$")]
        if(!newEmail.matches("^[-a-z0-9~!$%^&*_=+}{\'?]+(\\.[-a-z0-9~!$%^&*_=+}{\'?]+)*@([a-z0-9_][-a-z0-9_]*(\\.[-a-z0-9_]+)*\\.(aero|arpa|biz|com|coop|edu|gov|info|int|mil|museum|name|net|org|pro|travel|mobi|[a-z][a-z])|([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}))(:[0-9]{1,5})?$")) {
            editTextEmail.setError(getString(R.string.email_error_message));
            return;
        }

        // backend validation: [RegularExpression("[0-9]{8}")]
        if(!newMobileNumber.matches("[0-9]{8}")) {
            editTextMobile.setError(getString(R.string.mobile_error_message));
            return;
        }

        // save changes in the database (put):
        User updatedUser = new User();
        //int userId = 0;
        //updatedUser.ID = userId;
        updatedUser.UserName = newUserName;
        updatedUser.Password = newPassword;
        updatedUser.Email = newEmail;
        updatedUser.PhoneNumber = newMobileNumber;

        // disable button click (in case it takes some time):
        Button button = (Button) view;
        button.setEnabled(false);

        // put:
        restUserService.getService().updateUserById(userId, updatedUser, new Callback<User>() {
            @Override
            public void success(User user, Response response) {
                Toast toast = Toast.makeText(EditProfileActivity.this, getApplicationContext().getString(R.string.user_updated),
                        Toast.LENGTH_LONG);
                View toastView = toast.getView();
                toastView.setBackgroundResource(R.color.background_color);
                toast.show();

                Intent i = new Intent(EditProfileActivity.this, ProfileActivity.class);
                i.putExtra("USERID", userId);
                startActivity(i);
                finish();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast toast = Toast.makeText(EditProfileActivity.this, getApplicationContext().getString(R.string.update_user_error_message),
                        Toast.LENGTH_LONG);
                View toastView = toast.getView();
                toastView.setBackgroundResource(R.color.background_color);
                toast.show();
            }
        });
    }

    // Onclick cancel-button:
    public void cancelEditProfile(View view)
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