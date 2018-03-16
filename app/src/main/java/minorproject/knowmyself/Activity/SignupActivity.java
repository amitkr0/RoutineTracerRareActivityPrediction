package minorproject.knowmyself.Activity;

// import files
import minorproject.knowmyself.Database.LoginDBHelper;
import minorproject.knowmyself.Other.InputValidation;
import minorproject.knowmyself.Service.JobSchedulerService;
import minorproject.knowmyself.Other.UserSessionManager;
import minorproject.knowmyself.R;
import minorproject.knowmyself.Other.UserProfile;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.UUID;

import static minorproject.knowmyself.Other.UserSessionManager.PREFER_NAME;


// Ref : https://github.com/sourcey/materiallogindemo
public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";
    public Context context;
    protected InputValidation inputValidation;
    protected UserProfile user = new UserProfile();
    protected ScrollView nestedScrollView;
    protected TextInputLayout textInputLayoutEmail;
    protected TextInputLayout textInputLayoutPassword;
    private JobScheduler mJobScheduler;
    EditText _nameText;
    EditText _emailText;
    EditText _guardemailText;
    EditText _mobileText;
    EditText _passwordText;
    EditText _reEnterPasswordText;
    Button _signupButton;
    TextView _loginLink;

    protected LoginDBHelper loginDBHelper;
    SharedPreferences sharedPreferences;
    UserSessionManager session;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        session = new UserSessionManager(getApplicationContext());
        sharedPreferences = getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE);
        loginDBHelper = new LoginDBHelper(SignupActivity.this);
        //resource
        _nameText = findViewById(R.id.input_name);
        _emailText = findViewById(R.id.input_email);
        _guardemailText = findViewById(R.id.guard_email);
        _passwordText= findViewById(R.id.input_password);
        _reEnterPasswordText = findViewById(R.id.input_reEnterPassword);
        _mobileText = findViewById(R.id.input_mobile);
        _loginLink = findViewById(R.id.link_login);
        _signupButton = findViewById(R.id.btn_signup);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();

            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    @SuppressLint("WrongConstant")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(true);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.Theme_AppCompat_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
//        progressDialog.show();

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String mobile = _mobileText.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();
        String uniqueID = UUID.randomUUID().toString();
        AddUser(uniqueID);
        session.createUserLoginSession(uniqueID,_emailText.getText().toString().trim(),_passwordText.getText().toString().trim());
//        // Timer work starts
//        mJobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
//        JobInfo.Builder builder = new JobInfo.Builder(1,
//                new ComponentName(getPackageName(), JobSchedulerService.class.getName()));
//        builder.setPeriodic(10000);
//        if (mJobScheduler.schedule(builder.build()) <= 0) {
//            Toast.makeText(getApplicationContext(), "Problem in scheduling", Toast.LENGTH_LONG).show();
//        }
        Intent accountsIntent = new Intent(getApplicationContext(), MapsMarkerActivity.class);//Todo

        accountsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        accountsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        accountsIntent.putExtra("EMAIL", _emailText.getText().toString().trim());
        emptyInputEditText();
        startActivity(accountsIntent);
        finish();

        // TODO: Implement your own signup logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSignupSuccess();
                        // onSignupFailed();
             //      progressDialog.dismiss();
                    }
                }, 3000);
    }

    public void AddUser(String uniqueID) {
        UserProfile user = new UserProfile();
        user.setName(_nameText.getText().toString().trim());
        user.setEmail(_emailText.getText().toString().trim());
        user.setPassword(_passwordText.getText().toString().trim());
        user.setGuardemail(_guardemailText.getText().toString().trim());
        user.setuserid(uniqueID);
        user.setContact(_mobileText.getText().toString().trim());

        loginDBHelper.addUser(user);

        /*
        editor.putString(Email, _emailText.getText().toString().trim());
        editor.putString(Password, _passwordText.getText().toString().trim());
        editor.commit();
         */
        // Snack Bar to show success message that record saved successfully
//        Toast.makeText(getBaseContext(), "Account Successfully Formed", Toast.LENGTH_LONG).show();
        emptyInputEditText();
    }


    public void onSignupSuccess() {
        _signupButton.setEnabled(false);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;
        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String guardemail = _guardemailText.getText().toString();
        String mobile = _mobileText.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();



        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (guardemail.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(guardemail).matches()) {
            _guardemailText.setError("enter a valid email address");
            valid = false;
        } else {
            _guardemailText.setError(null);
        }

        if (mobile.isEmpty() || mobile.length() != 10) {
            _mobileText.setError("Enter Valid Mobile Number");
            valid = false;
        } else {
            _mobileText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 10 || !(reEnterPassword.equals(password))) {
            _reEnterPasswordText.setError("Password Do not match");
            valid = false;
        } else {
            _reEnterPasswordText.setError(null);
        }

        if (loginDBHelper.checkUser(_emailText.getText().toString().trim())) {
            _emailText.setError("Email already exists !!");
            valid = false;
        } else {
                _emailText.setError(null);
            }

        return valid;
    }

    private void emptyInputEditText() {
        _nameText.setText(null);
        _guardemailText.setText(null);
        _mobileText.setText(null);
        _emailText.setText(null);
        _passwordText.setText(null);
        _reEnterPasswordText.setText(null);
    }
}
