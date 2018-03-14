package com.example.thien.footballmanagement;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.util.Log;


import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.accounts.AccountManager.KEY_PASSWORD;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    public static Context contextOfApplication;
    private static final int REQUEST_SIGNUP = 0;
    private static final String REQ_URL = "http://10.0.2.2:55903/Token";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public SharedPreferences sharedPreferences;
    private String PREF_USERNAME="";
    private String PREF_PASS = "";
    public static final String KEY_ACCESS_TOKEN = "access_token";
    public static final String MYPREF = "com.example.thien";
    private String accesstoken;

    private EditText _emailText ;//= (EditText)findViewById(R.id.input_email);
    private EditText _passwordText ;//= (EditText)findViewById(R.id.input_password);
    private Button _loginButton ;//= (Button)findViewById(R.id.btn_login);
    private TextView _signupLink ;//= (TextView)findViewById(R.id.link_signup);

    /* Save Preferences for login Information*/
    @Override
    protected void onPause() {
        super.onPause();
        savePreferences();
    }

    /* onResume event, load the preference: User and password*/
    @Override
    protected void onResume() {
        super.onResume();
        loadPreferences();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        /* Initiate and load preference*/
        contextOfApplication = getApplicationContext();
        _emailText = (EditText)findViewById(R.id.input_email);
        _passwordText = (EditText)findViewById(R.id.input_password);
        _loginButton = (Button)findViewById(R.id.btn_login);
        _signupLink = (TextView)findViewById(R.id.link_signup);
        sharedPreferences = getSharedPreferences(MYPREF,MODE_PRIVATE);
        loadPreferences();

        //Call login when clicking login button
        _loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        //Move to register activity
        _signupLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });


    }

    // Function to get the instance of application context
    public static Context getContextOfApplication(){
        return contextOfApplication;
    }


    public void login() {
        Log.d(TAG, "Login");

        //Check if login form is valid or not
        if (!validate()) {
            onLoginFailed();
            return;
        }
        //Deactive login button
        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        //Get Email & Password from Login Form
        final String email = _emailText.getText().toString();
        final String password = _passwordText.getText().toString();

        Log.d(TAG,email);
        Log.d(TAG,password);


        // TODO: Implement authentication logic here.

        /* Use Volley Library to send stringRequest to API*/

        StringRequest stringRequest = new StringRequest(Request.Method.POST, REQ_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Toast.makeText(LoginActivity.this, "RESPONSES COME", Toast.LENGTH_LONG).show();
                            JSONObject jsonObject = new JSONObject(response);
                            //Store access_token from response string to SharePreferences for further use*/
                            accesstoken = jsonObject.getString("access_token");
                            Log.d(TAG,accesstoken);
                            SharedPreferences.Editor edit = sharedPreferences.edit();
                            edit.putString(KEY_ACCESS_TOKEN,accesstoken);
                            edit.commit();

                            //If get the success response--> move to home page
                            openProfile();

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }

                    }
                },

                // Failed on Request
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this, "Volley Error =))", Toast.LENGTH_LONG).show();
                        //Log.d(TAG, error.getMessage());
                    }
                }) {


            //Configure Header parameter to send with post request
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                //params.put("Content-Type", "application/x-www-form-urlencoded");
                //params.put("Accept","application/json");
                //params.put("Content-Type","application/json");
                return params;
            }

            //Configure Body parameter to send with post request
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<String, String>();
                map.put(KEY_USERNAME, email);
                map.put(KEY_PASSWORD, password);
                map.put("grant_type", "password");
                return map;
            }

            //Configure to get Body content in application/json
            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

        //Implement retry policy
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        //add request to Volley Request Queue for being sent
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        requestQueue.add(stringRequest);


        //End

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        onLoginSuccess();
                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }

    //Start Home Activity
    private void openProfile() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra(KEY_USERNAME, _emailText.getText());
        startActivity(intent);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    // Save User & Password here
    private void savePreferences(){
        sharedPreferences.edit().putString(PREF_USERNAME,_emailText.getText().toString());
        sharedPreferences.edit().putString(PREF_PASS,_passwordText.getText().toString());
        sharedPreferences.edit().commit();
    }

    // Reload User & Password
    private void loadPreferences(){
        _emailText.setText(sharedPreferences.getString(PREF_USERNAME,""));
        _passwordText.setText(sharedPreferences.getString(PREF_PASS,""));
    }

    // Called on event of logging in successfully
    public void onLoginSuccess() {
        _loginButton.setEnabled(false);
        savePreferences();
        Toast.makeText(getBaseContext(), "Login done", Toast.LENGTH_LONG).show();
        finish();
    }

    // Called when log in failed
    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    // Check Form Input Valid or Invalid for Request
    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}
