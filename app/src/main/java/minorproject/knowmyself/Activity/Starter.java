package minorproject.knowmyself.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import minorproject.knowmyself.Other.UserSessionManager;

import static minorproject.knowmyself.Other.UserSessionManager.KEY_EMAIL;
import static minorproject.knowmyself.Other.UserSessionManager.PREFER_NAME;

public class Starter extends AppCompatActivity {
    private UserSessionManager mSession;
    private SharedPreferences sharedPreferences;
    private static final int REQUEST_ACCESS_FINE_LOCATION = 111;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermissions();
        sharedPreferences = getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(KEY_EMAIL, " ");
        if (username.equals(" ")) {

            Intent i = new Intent(this, LoginActivity.class);
            // Closing all the Activities from stack
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // Staring Login Activity
            startActivity(i);

        }
        else {
            Intent accountsIntent = new Intent(Starter.this, HomePage.class);
            startActivity(accountsIntent);
            Log.d("conty", "hii");
        }

    }

    public boolean checkPermissions() {
        boolean hasPermissionLocation = (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermissionLocation) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_ACCESS_FINE_LOCATION);
        }
        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(this, "Permission granted.", Toast.LENGTH_SHORT).show();
                    //reload my activity with permission granted or use the features what required the permission
                    finish();
                    startActivity(getIntent());
                } else
                {
                    Toast.makeText(this, "The app was not allowed to get your location. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
                }
            }

        }

    }
}
