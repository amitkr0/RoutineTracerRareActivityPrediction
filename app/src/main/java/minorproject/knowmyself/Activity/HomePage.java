package minorproject.knowmyself.Activity;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import minorproject.knowmyself.Database.ToDoDBHelper;
import minorproject.knowmyself.Fragment.Account;
import minorproject.knowmyself.Fragment.ActivityRecognize;
import minorproject.knowmyself.Fragment.Send;
import minorproject.knowmyself.Fragment.Send.OnFragmentInteractionListener;
import minorproject.knowmyself.Fragment.ToDo;
import minorproject.knowmyself.R;
import minorproject.knowmyself.Service.JobSchedulerService;
import minorproject.knowmyself.Service.SensorService;

import static minorproject.knowmyself.Other.UserSessionManager.PREFER_NAME;

public class HomePage extends AppCompatActivity
        implements ToDo.OnFragmentInteractionListener,ActivityRecognize.OnFragmentInteractionListener, Account.OnFragmentInteractionListener, OnFragmentInteractionListener, NavigationView.OnNavigationItemSelectedListener {
    private ToDoDBHelper mydb;

    private ArrayAdapter<String> mAdapter;
    private static final String TAG_TODO = "todo";
    private static final String TAG_ACCOUNT = "account";
    private static final String TAG_SHARE = "share";
    private static final String TAG_MAP = "map";
    private static final String TAG_ABOUTUS = "aboutus";
    private static final String TAG_SEND = "send";
    private JobScheduler mJobScheduler;
    public static String CURRENT_TAG = TAG_TODO;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Toast.makeText(getApplicationContext(),"Welcome !!", Toast.LENGTH_SHORT).show();
        // Timer work starts
        /*mJobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        JobInfo.Builder builder = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new JobInfo.Builder(1,
                    new ComponentName(getPackageName(), JobSchedulerService.class.getName()));
            builder.setMinimumLatency(10000);
            if (mJobScheduler.schedule(builder.build()) <= 0) {
                Toast.makeText(getApplicationContext(), "Problem in scheduling", Toast.LENGTH_LONG).show();
            }
        }*/
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setMinimumLatency(10000);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (mJobScheduler.schedule(builder.build()) <= 0) {
                Toast.makeText(getApplicationContext(), "Problem in scheduling", Toast.LENGTH_LONG).show();
            }
        }*/


        AlarmManager scheduler = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), SensorService.class );
        PendingIntent scheduledIntent = PendingIntent.getService(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        scheduler.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 15*60*1000, scheduledIntent);


        FloatingActionButton fab = findViewById(R.id.action);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (CURRENT_TAG == TAG_TODO) {

                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(HomePage.this);
                    LayoutInflater inflater = LayoutInflater.from(HomePage.this);
                    View dialogView = inflater.inflate(R.layout.new_to_do, null);
                    dialogBuilder.setView(dialogView);

                    final EditText mName = (EditText) dialogView.findViewById(R.id.editTextName);
                    final EditText mPhone = (EditText) dialogView.findViewById(R.id.editTextPhone);
                    final EditText mEmail = (EditText) dialogView.findViewById(R.id.editTextStreet);
                    final EditText mStreet = (EditText) dialogView.findViewById(R.id.editTextEmail);
                    final EditText mPlace = (EditText) dialogView.findViewById(R.id.editTextCity);
                    final ImageButton mTimePick = (ImageButton) dialogView.findViewById(R.id.timePick);
                    final EditText mSettime = dialogView.findViewById(R.id.setTime);
                    // final ImageButton saveButton = dialogView.findViewById(R.id.save);
                    dialogBuilder.setPositiveButton("hell", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int id) {
                            String name = String.valueOf(mName.getText());
                            String phone = String.valueOf(mPhone.getText());
                            String email = String.valueOf(mEmail.getText());
                            String street = String.valueOf(mStreet.getText());
                            String place = String.valueOf(mPlace.getText());
                            mydb = new ToDoDBHelper(getApplicationContext());
//                            mydb.insertToDo(name,phone,email,street,place);  //ToDo insert TOdo
                            Toast.makeText(getApplicationContext(), "Successfully added" + name, Toast.LENGTH_SHORT).show();
                            Fragment fragment = new ToDo();
                            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.mainFrame, fragment);
                            ft.commit();
                            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                            drawer.closeDrawer(GravityCompat.START);
                            Toast.makeText(getApplicationContext(), "updated", Toast.LENGTH_LONG).show();
                        }
                    });
                    dialogBuilder.setNegativeButton("no", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
                    AlertDialog alertDialog = dialogBuilder.create();
                    alertDialog.show();
                    mTimePick.setOnClickListener(new View.OnClickListener() {
                        @Override

                        public void onClick(View view) {
                            System.out.println("kya ho gya");
                            Toast.makeText(getApplicationContext(), "gog", Toast.LENGTH_LONG).show();
                            Calendar mcurrentTime = Calendar.getInstance();
                            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                            int minute = mcurrentTime.get(Calendar.MINUTE);
                            TimePickerDialog mTimePicker;
                            mTimePicker = new TimePickerDialog(HomePage.this, new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                    mSettime.setText("" + selectedHour + ":" + selectedMinute);
                                }
                            }, hour, minute, true);
                            mTimePicker.setTitle("Select Time");
                            mTimePicker.show();
                        }
                    });
                    //ui update


                }
                else if (CURRENT_TAG == TAG_MAP) {


                }
                else if (CURRENT_TAG == TAG_ABOUTUS) {


                }
                else if (CURRENT_TAG == TAG_ACCOUNT) {

                }

                else if (CURRENT_TAG == TAG_SHARE) {

                }
                else if (CURRENT_TAG == TAG_ABOUTUS) {

                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //NOTE:  Checks first item in the navigation drawer initially
        navigationView.setCheckedItem(R.id.to_do);

        //NOTE:  Open fragment1 initially.
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.mainFrame, new ToDo());
        ft.commit();


        //TO Do frag


    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fragment = null;

        if (id == R.id.to_do) {
            CURRENT_TAG = TAG_TODO;
            fragment = new ToDo();

            // Handle the camera action
        } else if (id == R.id.map_edit) {
            CURRENT_TAG = TAG_MAP;

        } else if (id == R.id.account) {
            CURRENT_TAG = TAG_ACCOUNT;
            fragment = new Account();

        } else if (id == R.id.nav_share) {
            CURRENT_TAG = TAG_SHARE;
            fragment = new ActivityRecognize();

        } else if (id == R.id.nav_send) {
            fragment = new Send();
            CURRENT_TAG = TAG_SEND;
        } else if (id == R.id.about_us) {
            CURRENT_TAG = TAG_ABOUTUS;
        }
        //NOTE: Fragment changing code
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mainFrame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(String title) {
        // NOTE:  Code to replace the toolbar title based current visible fragment
        getSupportActionBar().setTitle(title);
    }
}
