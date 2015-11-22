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
        // disable button click (in case it takes some time):
        Button button = (Button) view;
        button.setEnabled(false);

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

        User user = new User();
        //int userId = 0;
        //user.ID = userId;
        user.UserName = userName;
        user.Password = password;
        user.Email = email;
        user.PhoneNumber = mobileNumber;

        userNameTemp = userName;
        passwordTemp = password;

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
            }
        });

        /* update:
            restService.getService().updateUserById(_Student_Id, user, new Callback<User>() {
                @Override
                public void success(User user, Response response) {
                    Toast.makeText(RegisterUserActivity.this, "User updated...@string later", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(RegisterUserActivity.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
        });*/
    }

    // Called after user successfully registered
    public void login(LoginUser login)
    {
        restLoginService.getService().logIn(login, new Callback<String>() {
            @Override
            public void success(String callback, Response response) {
                int userId;

                try {
                    userId = Integer.parseInt(callback); // får inn id gjennom StringContent på backend
                }
                catch(NumberFormatException nfe) {
                    userId = -1;
                }

                Intent i = new Intent(RegisterUserActivity.this, WishListMainActivity.class);
                i.putExtra("USERID", userId);
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
            }
        });
    }

    // Onclick cancel-button
    public void cancelNewUser(View view) {
        finish();
    }

    // No menu
}