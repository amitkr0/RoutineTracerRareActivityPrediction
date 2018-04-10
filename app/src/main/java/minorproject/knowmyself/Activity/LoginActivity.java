package minorproject.knowmyself.Activity;

// import files

import minorproject.knowmyself.Database.LoginDBHelper;
import minorproject.knowmyself.Other.InputValidation;
import minorproject.knowmyself.Other.UserSessionManager;
import minorproject.knowmyself.R;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import static minorproject.knowmyself.Other.UserSessionManager.KEY_EMAIL;
import static minorproject.knowmyself.Other.UserSessionManager.PREFER_NAME;


// Ref : https://github.com/sourcey/materiallogindemo
public class LoginActivity extends AppCompatActivity {

    public static final String Password = "passKey";
    public static final String Email = "emailKey";
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    private final AppCompatActivity activity = LoginActivity.this;
    public Context context;
    protected ScrollView nestedScrollView;
    // very important remember to initialize it with (this)
    protected LoginDBHelper loginDBHelper;
    protected InputValidation inputValidation;

    TextInputLayout textInputLayoutPassword;
    SharedPreferences sharedPreferences;

    // User Session Manager Class
    UserSessionManager session;

    private EditText _emailText;
    private EditText _passwordText;
    private Button _loginButton;
    private TextView _signupLink;
    private TextInputLayout textInputLayoutEmail;
    @SuppressLint("WrongConstant")
   @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        setContentView(R.layout.activity_login);
        // User Session Manager
        sharedPreferences = getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(KEY_EMAIL, " ");

        //resources
        _emailText = findViewById(R.id.input_email);
        _loginButton = findViewById(R.id.btn_login);
        _passwordText = findViewById(R.id.input_password);
        _signupLink = findViewById(R.id.link_signup);
        textInputLayoutEmail = findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword = findViewById(R.id.textInputLayoutPassword);


        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the Signup activity

                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

            }
        });
    }

    public void login() {


        loginDBHelper = new LoginDBHelper(LoginActivity.this);

        if (!verifyFromSQLite()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(true);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.Theme_AppCompat_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        if (loginDBHelper.checkUser(_emailText.getText().toString().trim()
                , _passwordText.getText().toString().trim())) {
            String userID = loginDBHelper.getData(_emailText.toString().trim());
            session.createUserLoginSession(userID, _emailText.getText().toString().trim(), _passwordText.getText().toString().trim());
            Intent accountsIntent = new Intent(activity, HomePage.class);//ToDo

            accountsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            accountsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            accountsIntent.putExtra("EMAIL", _emailText.getText().toString().trim());
            emptyInputEditText();
            startActivity(accountsIntent);
            finish();
            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            // On complete call either onLoginSuccess or onLoginFailed
                            onLoginSuccess();

                            // onLoginFailed();
                            //      progressDialog.dismiss();
                        }
                    }, 3000);


        } else {
            // Toast to show success message that record is wrong
            //   onLoginFailed();
            Toast.makeText(getApplicationContext(), "Error in email and password", Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
            return;
        }

        // TODO: Implement your own authentication logic here.

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

    private boolean verifyFromSQLite() {
        boolean valid = true;
        String password = _passwordText.getText().toString();
        inputValidation = new InputValidation(LoginActivity.this);
        if (!inputValidation.isInputEditTextFilled(_emailText, textInputLayoutEmail, getString(R.string.error_message_email))) {
            valid = false;
        }
        if (!inputValidation.isInputEditTextEmail(_emailText, textInputLayoutEmail, getString(R.string.error_message_email))) {
            valid = false;
        }
        if (!inputValidation.isInputEditTextFilled(_passwordText, textInputLayoutPassword, getString(R.string.error_message_password))) {
            valid = false;
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;

    }

    /**
     * This method is to empty all input edit text
     */
    private void emptyInputEditText() {
        _emailText.setText(null);
        _passwordText.setText(null);
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
        finish();
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(false);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        _loginButton.setEnabled(true);
    }

}