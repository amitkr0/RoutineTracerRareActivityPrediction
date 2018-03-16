package minorproject.knowmyself.Service;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import minorproject.knowmyself.Database.ServicesDBHelper;
import minorproject.knowmyself.Other.SensorData;
import minorproject.knowmyself.R;

import static minorproject.knowmyself.Other.UserSessionManager.KEY_USERID;
import static minorproject.knowmyself.Other.UserSessionManager.PREFER_NAME;

public class SensorService extends Service implements LocationListener{

    protected LocationManager locationManager;
    protected String YES_ACTION="YES_ACTION",
            MAYBE_ACTION="NO_ACTION",
            NO_ACTION="NO_ACTION";
    private static final float ROTATION_THRESHOLD = 2.0f;
    private static final int ROTATION_WAIT_TIME_MS = 100;
    private static final double DISTANCE_THRESHOLD = 250.0;
    private SensorManager sensorManager;
    private boolean isColor = false;
    private long lastUpdate;
    float[] mGeomagnetic;
    float[] mGravity;
    // making filedata protected
    protected String accData;  // accelerometer data
    protected String gyroData; // gyroscope data
    protected String locData;  // location data (lat,long)
    protected String magData;  // magnetic orientation data
//    double prevLat = 25.5938, prevLong = 85.1605;
    private static String TIME;
    private static  String ACC_X;
    private static  String ACC_Y;
    private static  String ACC_Z;
    private static String GYRO_1;
    private static String GYRO_2;
    private static String GYRO_3;
    private static String MAG_1;
    private static String MAG_2;
    private static String MAG_3;
    private static String latitude;
    private static String longitude;
    private static String speed;
    private static String response;
    private static String uniqueID;
    private SensorManager mSensorManager;
    Location location;
    private Sensor mAccelerometer,mGyroscope,mMagnetic;
    private static SensorEventListener sensorEventListener;
    private SharedPreferences sharedPreferences;

    public SensorService() {
    }



    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
        sharedPreferences = getSharedPreferences(PREFER_NAME, Context.MODE_PRIVATE);
        Toast.makeText(this, "In onStartCommand", Toast.LENGTH_LONG).show();
        uniqueID=UUID.randomUUID().toString();
        /*sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mGyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mMagnetic = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        sensorManager.registerListener(sensorEventListener, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(sensorEventListener, mGyroscope, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(sensorEventListener, mMagnetic, SensorManager.SENSOR_DELAY_NORMAL);*/

        saveDataFile();
        stopSelf();
        return START_STICKY;

    }

    private void startAllJob() {
        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mGyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mMagnetic = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        lastUpdate = System.currentTimeMillis();


        sensorEventListener = new SensorEventListener() {

            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }

            public void onSensorChanged(SensorEvent event) {

                if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                    mGravity = event.values;
//                    Log.d("SENSORR","acceleromter");
                    getAccelerometer(event);
                }
                else
//                    Toast.makeText(getApplicationContext(),"Device do not have accelerometer sensor",
//                            Toast.LENGTH_SHORT).show();


                if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
//                    Log.d("SENSORR","gyroscope");
                    detectRotation(event);
                }
                else
//                    Toast.makeText(getApplicationContext(),"Device do not have gyroscope sensor",
//                            Toast.LENGTH_SHORT).show();

                if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
//                    Log.d("SENSORR","compass");
                    mGeomagnetic = event.values;
                }
                else
//                    Toast.makeText(getApplicationContext(),"Device do not have compass sensor",
//                            Toast.LENGTH_SHORT).show();

                detectOrientation();

            }
        };
        sensorManager.registerListener(sensorEventListener, mGyroscope, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(sensorEventListener, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(sensorEventListener, mMagnetic, SensorManager.SENSOR_DELAY_NORMAL);
//        sensorManager.unregisterListener(sensorEventListener);
    }

  //  @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void addNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";
        long[] pattern = {0, 100, 200, 300};

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_DEFAULT);

            // Configure the notification channel.
            notificationChannel.setDescription("Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationManager.createNotificationChannel(notificationChannel);

        }

//        Toast.makeText(this,"notification  here",Toast.LENGTH_SHORT).show();
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);

        notificationBuilder.setAutoCancel(true)
                .setSmallIcon(R.drawable.profile)
                .setTicker("KnowMySelf")
                .setPriority(Notification.PRIORITY_MAX)
                .setVibrate(pattern)
                .setContentTitle("Alert on Location Change")
                .setContentText("Kindly give your response about type of your visit to this place")
                .setContentInfo("Info");


        Intent yesReceive = new Intent(this,NotificationService.class);
        yesReceive.setAction(YES_ACTION);
        yesReceive.putExtra("ID",uniqueID);
        PendingIntent pendingIntentYes = PendingIntent.getBroadcast(this, 12345, yesReceive, PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.addAction(R.drawable.ic_action_normal, "Normal", pendingIntentYes);

        Intent maybeReceive = new Intent(this,NotificationService.class);
        maybeReceive.setAction(MAYBE_ACTION);
        yesReceive.putExtra("ID",uniqueID);
        PendingIntent pendingIntentMaybe = PendingIntent.getBroadcast(this, 12345, maybeReceive, PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.addAction(R.drawable.ic_action_seminormal, "Rare", pendingIntentMaybe);

        //No intent
        Intent noReceive = new Intent(this,NotificationService.class);
        noReceive.setAction(NO_ACTION);
        yesReceive.putExtra("ID",uniqueID);
        PendingIntent pendingIntentNo = PendingIntent.getBroadcast(this, 12345, noReceive, PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.addAction(R.drawable.ic_action_semi, "Semi-normal", pendingIntentNo);
        notificationManager.notify(/*notification id*/101, notificationBuilder.build());

    }

    private void getAccelerometer(SensorEvent event) {
        float[] values = event.values;
        // Storing all data in accData
        accData = ""+values[0] + ":" + values[1] + ":" + values[2];
        ACC_X = Float.toString(values[0]);
        ACC_Y = Float.toString(values[1]);
        ACC_Z = Float.toString(values[2]);
//        Toast.makeText(this, "Accelerometer value" + accData+" get accepted", Toast.LENGTH_LONG).show();
    }
    private void detectOrientation() {
        // Ref : http://www.codingforandroid.com/2011/01/using-orientation-sensors-simple.html
        if (mGravity != null && mGeomagnetic != null) {
            float R[] = new float[9];
            float I[] = new float[9];

            boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
            if (success) {
                float orientation[] = new float[3];
                // orientation contains: azimut, pitch and roll
                SensorManager.getOrientation(R, orientation);
                // Storing all data in magData
                magData = ""+orientation[0]+":"+orientation[1]+":"+orientation[2];
                MAG_1 = Float.toString(orientation[0]);
                MAG_2 = Float.toString(orientation[1]);
                MAG_3 = Float.toString(orientation[2]);
//                Toast.makeText(this, "Orientation value"+magData+" get accepted", Toast.LENGTH_LONG).show();
            }
        }
    }

    // Extracted from
    // https://github.com/drejkim/AndroidWearMotionSensors/blob/master/wear/src/main/java/com/drejkim/androidwearmotionsensors/SensorFragment.java
    private void detectRotation(SensorEvent event) {
        long now = System.currentTimeMillis();

        if((now - lastUpdate) > ROTATION_WAIT_TIME_MS) {
            lastUpdate = now;
            gyroData = ""+Math.abs(event.values[0]) + ":" + Math.abs(event.values[1]) + ":" + Math.abs(event.values[2]);
            GYRO_1 = Float.toString(Math.abs(event.values[0]));
            GYRO_2 = Float.toString(Math.abs(event.values[1]));
            GYRO_3 = Float.toString(Math.abs(event.values[2]));
        }
    }

    public void onLocationChanged(Location location) {
        locData = ""+location.getLatitude()+":"+location.getLongitude();
    //    Toast.makeText(this, "Location value"+locData+" get accepted", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    // check function which determine distance between two location
    // and give output whether distance has crossed minimum threshold value or not
    public boolean check(double presLat, double presLong) {
        float dis[] = new float[3];
        double pastLat = Double.parseDouble(sharedPreferences.getString("PREVLAT",""));
        double pastLong = Double.parseDouble(sharedPreferences.getString("PREVLONG",""));
        Log.d("abcde","distance between = "+pastLat+" "+pastLong);
        /*Location loc1 = new Location("");
        Location loc2 = new Location("");
        loc1.setLatitude(presLat);loc1.setLongitude(presLong);
        loc2.setLatitude(pastLat);loc2.setLongitude(pastLong);
        double distanceBetween = loc1.distanceTo(loc2);
        Log.d("abcd","distance between = "+distanceBetween);
        if(distanceBetween>DISTANCE_THRESHOLD){
            return true;
        }
        return false;*/

        location.distanceBetween(presLat,presLong,pastLat,pastLong,dis);
        Log.d("abcd","distance between = "+dis[0]+" "+dis[1]+" "+dis[2]);
        if(dis[0]>=DISTANCE_THRESHOLD) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("PREVLAT", String.valueOf(presLat));;
            editor.putString("PREVLONG", String.valueOf(presLong));
            editor.commit();
            pastLat = Double.parseDouble(sharedPreferences.getString("PREVLAT",""));
            pastLong = Double.parseDouble(sharedPreferences.getString("PREVLONG",""));
            Log.d("abcd",presLat+" "+presLong+" "+pastLat+" "+pastLong);
            Log.d("abcd","distance between = "+dis[0]+" "+dis[1]+" "+dis[2]);
            return true;
        }
        else {
            return false;
        }
    }

    // Function for saving data in file
    public void saveDataFile() {

//        startAllJob();
        Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show();


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            boolean hasPermissionLocation = (ContextCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED);
            Toast.makeText(this,"really important",Toast.LENGTH_SHORT).show();
            if (!hasPermissionLocation) {
                ActivityCompat.requestPermissions((Activity) getApplicationContext(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        111);
                Toast.makeText(this,"got it",Toast.LENGTH_SHORT).show();
            }
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            //return;
        }
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria locationCritera = new Criteria();
        String providerName = locationManager.getBestProvider(locationCritera,
                true);
        if(providerName!=null)
            location = locationManager.getLastKnownLocation(providerName);


        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) this);

        //ToDo -- to get speed
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                speed = Float.toString(location.getSpeed());
                Log.d("SENSORR","location location");
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        // Checking is done here to determine whether user has crossed
        // minimum threshold value of distance
        if(location!=null) {
            if (check(location.getLatitude(), location.getLongitude())) {
                ServicesDBHelper servicesDBHelper = new ServicesDBHelper(this);
                String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
                SensorData sensor = new SensorData();
                latitude = Double.toString(location.getLatitude());
                longitude = Double.toString(location.getLongitude());
                speed = Double.toString(location.getSpeed());
                sensor.setLatitude(latitude);
                sensor.setLongitude(longitude);
                sensor.setSpeed(speed);
                sensor.setUserid(sharedPreferences.getString(KEY_USERID, ""));
                sensor.setID(uniqueID);
                sensor.setTIME(timeStamp);
                Toast.makeText(this, "Latitude " + latitude + "Longitude " + longitude, Toast.LENGTH_SHORT).show();

                // Adding delay here so that function
                // should be completed
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Do something after 5s = 5000ms
                        startAllJob();
                        //addNotification();
                    }
                }, 2000);

//                Toast.makeText(getApplicationContext(), "After startAllJob function ", Toast.LENGTH_SHORT).show();
                sensor.setAccX(ACC_X);
                sensor.setAccY(ACC_Y);
                sensor.setAccZ(ACC_Z);
                sensor.setGyro1(GYRO_1);
                sensor.setGyro2(GYRO_2);
                sensor.setGyro3(GYRO_3);
                sensor.setMag1(MAG_1);
                sensor.setMag2(MAG_2);
                sensor.setMag3(MAG_3);
                sensor.setResponse(null);
                //ToDo -- to save in database

//                Toast.makeText(getApplicationContext(), "Reaching last of add service", Toast.LENGTH_SHORT).show();
                servicesDBHelper.addServices(sensor);
                addNotification();
//                sensorManager.unregisterListener(sensorEventListener);
            }
        }
        else
            Toast.makeText(getApplicationContext(), "Please check your wifi or GPS", Toast.LENGTH_SHORT).show();

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}