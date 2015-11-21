package com.example.annika.wishlist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;

public class RegisterUserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
    }

    // Onclick register-button:
    public void registerNewUser(View view)
    {
        // register the user and log in - and get user id and go to:
        EditText editTextUserName = (EditText) findViewById(R.id.userNameRegister);
        EditText editTextPassword = (EditText) findViewById(R.id.passwordRegister);
        EditText editTextEmail = (EditText) findViewById(R.id.emailRegister);
        EditText editTextMobileNumber = (EditText) findViewById(R.id.mobileNumberRegister);

        String userName = editTextUserName.getText().toString();
        String password = editTextPassword.getText().toString();
        String email = editTextEmail.getText().toString();
        String mobileNumber = editTextMobileNumber.getText().toString();

        // Validation of input:

        // backend validation: [RegularExpression("[A-ZÆØÅa-zæøå0-9_\\-]{1,30}")]
        if(!userName.matches("[A-ZÆØÅa-zæøå0-9_\\-]{1,30}")) {
            editTextUserName.setError(getString(R.string.user_name_error_message));
            return;
        }

        // backend validation: [RegularExpression("[A-ZÆØÅa-zæøå0-9!#$%&'*+\\-/=?\\^_`{|}~+(\\.]{8,30}")]
        if(!password.matches("[A-ZÆØÅa-zæøå0-9!#$%&'*+\\-/=?\\^_`{|}~+(\\.]{8,30}")) {
            editTextPassword.setError(getString(R.string.password_error_message));
            return;
        }

        // backend validation: [RegularExpression("^[-a-z0-9~!$%^&*_=+}{\'?]+(\\.[-a-z0-9~!$%^&*_=+}{\'?]+)*@([a-z0-9_][-a-z0-9_]*(\\.[-a-z0-9_]+)*\\.(aero|arpa|biz|com|coop|edu|gov|info|int|mil|museum|name|net|org|pro|travel|mobi|[a-z][a-z])|([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}))(:[0-9]{1,5})?$")]
        if(!email.matches("^[-a-z0-9~!$%^&*_=+}{\'?]+(\\.[-a-z0-9~!$%^&*_=+}{\'?]+)*@([a-z0-9_][-a-z0-9_]*(\\.[-a-z0-9_]+)*\\.(aero|arpa|biz|com|coop|edu|gov|info|int|mil|museum|name|net|org|pro|travel|mobi|[a-z][a-z])|([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}))(:[0-9]{1,5})?$")) {
            editTextEmail.setError(getString(R.string.email_error_message));
            return;
        }

        // backend validation: [RegularExpression("[0-9]{8}")]
        if(!mobileNumber.matches("[0-9]{8}")) {
            editTextMobileNumber.setError(getString(R.string.mobile_error_message));
            return;
        }

        int id;
        User user = new User();

        /*try {
            id = getIntent().getIntExtra("ID", -1);
            p = dbHandler.getPerson(id);
            p.setName(namePerson);
            p.setPhoneNumber(phoneNumber);
            p.setBirthday(birthday);
            p.setMessage(message);
            dbHandler.updatePerson(p);
            Toast.makeText(this, name + getString(R.string.person_edited_message), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, getString(R.string.parse_id_edited_error_message), Toast.LENGTH_SHORT).show();
            return;
        }*/

        user.UserName = userName;
        user.Password = password;
        user.Email = email;
        user.MobileNumber = mobileNumber;

        // post user to server database:

        Intent i = new Intent(this, WishListMainActivity.class);
        startActivity(i);
        finish();
    }



    // No menu
}