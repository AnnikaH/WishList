package com.example.annika.wishlist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class RegisterUserActivity extends AppCompatActivity {

    private RestUserService restUserService;
    private RestLoginService restLoginService;
    private String userNameTemp;
    private String passwordTemp;
    private Button button;
    private int userId;

    // Store in SharedPreferences:
    @Override
    protected void onPause() {
        super.onPause();

        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                .putInt("userId", userId)
                .commit();
    }

    // Get values from SharedPreferences:
    @Override
    protected void onResume() {
        super.onResume();

        userId = (getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getInt("userId", -1));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        restUserService = new RestUserService();
        restLoginService = new RestLoginService();
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

        User user = new User();
        user.UserName = userName;
        user.Password = password;
        user.Email = email;
        user.PhoneNumber = mobileNumber;

        userNameTemp = userName;
        passwordTemp = password;

        // disable button click (in case it takes some time):
        button = (Button) view;
        button.setEnabled(false);

        // post user to server database:

        restUserService.getService().addUser(user, new Callback<User>() {
            @Override
            public void success(User user, Response response) {
                Toast toast = Toast.makeText(RegisterUserActivity.this, getApplicationContext().getString(R.string.user_registered),
                        Toast.LENGTH_SHORT);
                View toastView = toast.getView();
                toastView.setBackgroundResource(R.color.background_color);
                toast.show();

                // Get ID for this newly created user:
                LoginUser loginUser = new LoginUser();
                loginUser.UserName = userNameTemp;
                loginUser.Password = passwordTemp;

                login(loginUser);
            }

            @Override
            public void failure(RetrofitError error) {
                Toast toast = Toast.makeText(RegisterUserActivity.this, getApplicationContext().getString(R.string.registration_error_message),
                        Toast.LENGTH_LONG);
                View toastView = toast.getView();
                toastView.setBackgroundResource(R.color.background_color);
                toast.show();

                // enable button again:
                button.setEnabled(true);
            }
        });
    }

    // Called after user successfully registered
    public void login(LoginUser login) {
        restLoginService.getService().logIn(login, new Callback<String>() {
            @Override
            public void success(String callback, Response response) {
                try {
                    userId = Integer.parseInt(callback); // får inn id gjennom StringContent på backend
                } catch (NumberFormatException nfe) {
                    userId = -1;
                }

                Intent i = new Intent(RegisterUserActivity.this, WishListMainActivity.class);
                startActivity(i);
                finish();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast toast = Toast.makeText(RegisterUserActivity.this, getApplicationContext().getString(R.string.get_user_error_message),
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
    public void cancelNewUser(View view) {
        finish();
    }

    // No menu
}