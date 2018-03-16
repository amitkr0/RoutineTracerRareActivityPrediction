package minorproject.knowmyself.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

import minorproject.knowmyself.Other.SensorData;


public class ServicesDBHelper extends SQLiteOpenHelper{
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // DatabaseHelper dbh = new DatabaseHelper(this);
    public Context context ;
    // Database Name
    private static final String DATABASE_NAME = "Services.db";
    // User table name
    private static final String TABLE_USER = "SensorService";
    // User Table Columns names
    private static final String COLUMN_USER_USER_ID = "userid";
    private static String ID = "NOTIFICATION_ID";
    private static String TIME = "TIME";
    private static  String ACC_X = "ACC_X";
    private static  String ACC_Y = "ACC_Y";
    private static  String ACC_Z = "ACC_Z";
    private static String GYRO_1 =  "GYRO_X";
    private static String GYRO_2 =  "GYRO_Y";
    private static String GYRO_3 =  "GYRO_Z";
    private static String MAG_1 =  "MAG_1";
    private static String MAG_2 =  "MAG_2";
    private static String MAG_3 =  "MAG_3";
    private static String latitude =  "LATITUDE";
    private static String longitude =  "LONGITUDE";
    private static String speed =  "SPEED";
    private static String response =  "RESPONSE";
    // create table sql query
    private String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
            +  COLUMN_USER_USER_ID + " TEXT," +
            ID + " TEXT UNIQUE," +
            ACC_X + " TEXT,"+ACC_Y
            + " TEXT," +ACC_Z + " TEXT," + GYRO_1 + " TEXT," +
            GYRO_2 + " TEXT," + GYRO_3 + " TEXT," + MAG_1 + " TEXT,"
            + MAG_2 + " TEXT," + MAG_3 + " TEXT," + latitude + " TEXT,"
            + longitude + " TEXT," + speed + " TEXT," + TIME + " TEXT,"+
            response + " TEXT"
            + ")";

    // drop table sql query
    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER;


    public ServicesDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //Drop User Table if exist
        db.execSQL(DROP_USER_TABLE);

        // Create tables again
        onCreate(db);

    }


    public void addServices(SensorData sensor) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_USER_ID,sensor.getUserid() );
        values.put(ID,sensor.getID());
        values.put(TIME,sensor.getTIME());
        values.put(ACC_X, sensor.getAccX());
        values.put(ACC_Y, sensor.getAccY());
        values.put(ACC_Z, sensor.getAccZ());
        values.put(GYRO_1, sensor.getGyro1());
        values.put(GYRO_2, sensor.getGyro2());
        values.put(GYRO_3, sensor.getGyro3());
        values.put(MAG_1, sensor.getMag1());
        values.put(MAG_2, sensor.getMag2());
        values.put(MAG_3, sensor.getMag3());
        values.put(latitude, sensor.getLatitude());
        values.put(longitude, sensor.getLongitude());
        values.put(speed, sensor.getSpeed());
        values.put(response,sensor.getResponse());

        // Inserting Row
        db.insert(TABLE_USER, null, values);
//        Toast.makeText(context,"Inserted -- (ServicesDBHelper)",Toast.LENGTH_SHORT).show();
        db.close();
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_USER);
        return numRows;
    }


    public ArrayList<String> getRow(String uniqueID){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from SensorService where NOTIFICATION_ID='"+uniqueID+"'" ,null );
        ArrayList<String> arrayList = new ArrayList<>();
        Log.d("abc","anklnk");
        if (res.moveToFirst()) {
                String string = res.getString(res.getColumnIndex(COLUMN_USER_USER_ID));
                arrayList.add(string);
                Log.d("abc",string);
                string = res.getString(res.getColumnIndex(ID));
                arrayList.add(string);
                Log.d("abc",string);
                string = res.getString(res.getColumnIndex(latitude));
                arrayList.add(string);
                Log.d("abc",string);
        }
        return arrayList;
    }

    public ArrayList<HashMap<String,String>> getSensorData() {
        ArrayList<HashMap<String,String>> arrayList = new ArrayList<>();
        HashMap<String,String> hashMap = new HashMap<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from SensorService", null );
        if (res.moveToFirst()) {
            do{
                String string = res.getString(res.getColumnIndex(COLUMN_USER_USER_ID));
                hashMap.put("userid",string);
                string = res.getString(res.getColumnIndex(ID));
                hashMap.put(ID,string);
                string = res.getString(res.getColumnIndex(TIME));
                hashMap.put(TIME,string);
                string = res.getString(res.getColumnIndex(ACC_X));
                hashMap.put(ACC_X,string);
                string = res.getString(res.getColumnIndex(ACC_Y));
                hashMap.put(ACC_Y,string);
                string = res.getString(res.getColumnIndex(ACC_Z));
                hashMap.put(ACC_Z,string);
                string = res.getString(res.getColumnIndex(GYRO_1));
                hashMap.put(GYRO_1,string);
                string = res.getString(res.getColumnIndex(GYRO_2));
                hashMap.put(GYRO_2,string);
                string = res.getString(res.getColumnIndex(GYRO_3));
                hashMap.put(GYRO_3,string);
                string = res.getString(res.getColumnIndex(MAG_1));
                hashMap.put(MAG_1,string);
                string = res.getString(res.getColumnIndex(MAG_2));
                hashMap.put(MAG_2,string);
                string = res.getString(res.getColumnIndex(MAG_3));
                hashMap.put(MAG_3,string);
                string = res.getString(res.getColumnIndex(latitude));
                hashMap.put(latitude,string);
                string = res.getString(res.getColumnIndex(longitude));
                hashMap.put(longitude,string);
                string = res.getString(res.getColumnIndex(response));
                hashMap.put(response,string);
                arrayList.add(hashMap);
            }while (res.moveToNext());
        }
        return arrayList;
    }

    public void updateResponse(String updatedResponse,String uniqueID){
        SQLiteDatabase db = this.getReadableDatabase();
        String update = "UPDATE SensorService SET RESPONSE = '"+ updatedResponse +"' WHERE NOTIFICATION_ID = '" + uniqueID+"'";
        db.execSQL(update);
        System.out.println(uniqueID);
//        Toast.makeText(context,"UPDATED",Toast.LENGTH_LONG).show();
    }
    public JSONArray getResults() {
        SQLiteDatabase db = this.getReadableDatabase();
        String searchQuery = "SELECT * FROM SensorService";//todo change contacy
        Cursor cursor = db.rawQuery(searchQuery, null);

        JSONArray resultSet = new JSONArray();

        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {

            int totalColumn = cursor.getColumnCount();
            JSONObject rowObject = new JSONObject();

            for (int i = 0; i < totalColumn; i++) {
                if (cursor.getColumnName(i) != null) {
                    try {
                        if (cursor.getString(i) != null) {
                            Log.d("TAG_NAME", cursor.getString(i));
                            rowObject.put(cursor.getColumnName(i), cursor.getString(i));
                        } else {
                            rowObject.put(cursor.getColumnName(i), "");
                        }
                    } catch (Exception e) {
                        Log.d("TAG_NAME", e.getMessage());
                    }
                }
            }
            resultSet.put(rowObject);
            cursor.moveToNext();
        }
        cursor.close();
        Log.d("TAG_NAME", resultSet.toString());
        return resultSet;
    }

}
