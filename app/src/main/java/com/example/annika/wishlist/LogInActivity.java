package com.example.annika.wishlist;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LogInActivity extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        textView = (TextView) findViewById(R.id.message);
    }

    // Onclick login-button:
    public void logIn(View view) {
        EditText userNameField = (EditText) findViewById(R.id.userName);
        EditText passwordField = (EditText) findViewById(R.id.password);

        String userName = userNameField.toString();
        String password = passwordField.toString();

        getJSON task = new getJSON();
        task.execute(new String[]{"http://dotnet.cs.hioa.no/s198611/WishListAPI/api/User"});

        //Tor: task.execute(new String[]{"http://dotnet.cs.hioa.no/Web-Android/api/Kunde/Get"});

        // kall til database (async): .../api/User/LogIn og sende med brukernavn og passord
        // få tilbake id til brukeren
        int userId = 0;
        // hvis verifiserer:
        Intent i = new Intent(this, WishListMainActivity.class);
        i.putExtra("USERID", userId);
        startActivity(i);

        // finish(); ?
    }

    // ASYNCTASK-objektet:
    private class getJSON extends AsyncTask<String, Void, String> {

        // HOVEDMETODEN er doInBackground:
        // Koble til og sende/ta imot
        @Override
        protected String doInBackground(String... urls) {
            String s;
            String output = "";

            for(String url : urls) {
                try {
                    URL urlen = new URL(urls[0]);
                    HttpURLConnection conn = (HttpURLConnection) urlen.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Accept", "application/json");

                    if(conn.getResponseCode() != 200) {
                        throw new RuntimeException("Failed: HTTP error code: " + conn.getResponseCode());
                    }

                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    System.out.println("Output from server .... \n");

                    while((s = br.readLine()) != null) {
                        output = output + s;
                    }

                    conn.disconnect();
                    return output;
                }
                catch(Exception e) {
                    e.printStackTrace();
                    return "Noe gikk galt";
                }
            }

            return output;
        }

        // får tilbake fra doInBackground - dette er det som gjøres etter doInBackground
        // i onPostExecute: er alltid her vi oppdaterer skjermbildet
        // strengen vi får inn er den vi får fra doInBackground:
        @Override
        protected void onPostExecute(String s) {
            //super.onPostExecute(s);
            textView.setText(s);
        }
    }

    // Onclick register-button:
    public void registerNewUser(View view) {
        Intent i = new Intent(this, RegisterUserActivity.class);
        startActivity(i);
    }

    // No menu
}