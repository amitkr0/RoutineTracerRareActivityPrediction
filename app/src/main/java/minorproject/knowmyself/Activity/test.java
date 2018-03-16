package minorproject.knowmyself.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.widget.TextView;

import minorproject.knowmyself.Other.UserSessionManager;
import minorproject.knowmyself.R;

import static minorproject.knowmyself.Other.UserSessionManager.KEY_EMAIL;

public class test extends Activity {
    private UserSessionManager mSession;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        /*sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(KEY_EMAIL, " ");
        TextView tx= findViewById(R.id.testtext);
        tx.setText(username+"go");*/

    }

}
