package com.example.thien.footballmanagement;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class RegisterActivity extends AppCompatActivity {

    private EditText nameText, emailText, passwordText, confirmText;
    private Button   signupButton;
    private TextView loginLink;
    private final String TAG = this.getClass().getSimpleName();
    private static final String REQ_URL = "http://10.0.2.2:55903/api/account/register";
    private static final String KEY_USERNAME = "Username";
    private static final String KEY_EMAIL = "Email";
    private static final String KEY_PASSWORD = "Password";
    private static final String KEY_CONFIRM = "ConfirmPassword";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nameText = (EditText) findViewById(R.id.input_name);
        emailText = (EditText) findViewById(R.id.input_email);
        passwordText = (EditText) findViewById(R.id.input_password);
        confirmText = (EditText) findViewById(R.id.input_confirmPassword);
        signupButton = (Button) findViewById(R.id.btn_signup);
        loginLink = (TextView) findViewById(R.id.link_login);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        });
    }

    public void signup() {
        Log.d(TAG, "Register");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this,
                R.style.AppTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        final String name = nameText.getText().toString();
        final String email = emailText.getText().toString();
        final String password = passwordText.getText().toString();
        final String confirmPassword = confirmText.getText().toString();

        Log.d(TAG,name);
        Log.d(TAG,email);
        Log.d(TAG,password);
        Log.d(TAG,confirmPassword);

        // TODO: Implement your own signup logic here.

        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/x-www-form-urlencoded");


        RequestBody formBody = new FormBody.Builder()
                .add(KEY_USERNAME, name)
                .add(KEY_EMAIL,email)
                .add(KEY_PASSWORD,password)
                .add(KEY_CONFIRM,confirmPassword)
                .build();
        Request request = new Request.Builder()
                .url(REQ_URL)
                .post(formBody)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();


        Log.d(TAG,formBody.toString());
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(getBaseContext(), "Request failed", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Log.d(TAG, response.body().string());
                        } catch (IOException ioe) {
                            Log.d(TAG,"Error during get body");
                        }
                    }
                });
            }
        });


        // END

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSignupSuccess();
                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    public void onSignupSuccess() {
        signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Register failed", Toast.LENGTH_LONG).show();

        signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = nameText.getText().toString();
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();
        String confirmPass = confirmText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            nameText.setError("at least 3 characters");
            valid = false;
        } else {
            nameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError("enter a valid email address");
            valid = false;
        } else {
            emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            passwordText.setError(null);
        }

        //if(password!= confirmPass)valid = false;

        return valid;
    }
}
