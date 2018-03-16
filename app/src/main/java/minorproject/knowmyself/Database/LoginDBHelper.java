package minorproject.knowmyself.Database;

import minorproject.knowmyself.Other.UserProfile;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class LoginDBHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // DatabaseHelper dbh = new DatabaseHelper(this);
    public Context context ;
    // Database Name
    private static final String DATABASE_NAME = "UserManager.db";

    // User table name
    private static final String TABLE_USER = "UserData";
    // User Table Columns names
    private static final String COLUMN_USER_USER_ID = "userid";
    private static final String COLUMN_USER_NAME = "name";
    private static final String COLUMN_USER_EMAIL = "email";
    private static final String COLUMN_USER_GUARDIAN_EMAIL = "guardian_email";
    private static final String COLUMN_USER_PASSWORD = "password";
    private static final String COLUMN_USER_CONTACT = "contact";

    // create table sql query
    private String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
            +  COLUMN_USER_USER_ID + " TEXT PRIMARY KEY," +
            COLUMN_USER_NAME + " TEXT,"+COLUMN_USER_EMAIL
            + " TEXT," +COLUMN_USER_GUARDIAN_EMAIL + " TEXT," + COLUMN_USER_CONTACT + " TEXT," +
            COLUMN_USER_PASSWORD + " TEXT" + ")";

    // drop table sql query
    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER;
    /**
     * Constructor
     *
     * @param context
     */
    public LoginDBHelper(Context context) {
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

    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }


    public void open() throws SQLException {
        close();
        this.getWritableDatabase();
    }

    /**
     * This method is to create user record
     *
     * @param user
     */
    public void addUser(UserProfile user) {
        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_USER_ID, user.getuserid());
        values.put(COLUMN_USER_NAME, user.getName());
        values.put(COLUMN_USER_EMAIL, user.getEmail());
        values.put(COLUMN_USER_GUARDIAN_EMAIL, user.getGuardemail());
        values.put(COLUMN_USER_CONTACT, user.getContact());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());

        // Inserting Row
        db.insert(TABLE_USER, null, values);
        Toast.makeText(context,"Inserted -- (LoinDBHelper)",Toast.LENGTH_LONG).show();
        Log.d("Continue","Data Inserted");
        db.close();
    }

    public String getData(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select userid from UserData where email="+email+"", null );
        String userID = "";
        if (res.moveToFirst()) {
            userID = (res.getString(res.getColumnIndex(COLUMN_USER_USER_ID)));
        }
        return userID;
    }

    /**
     * This method is to fetch all user and return the list of user records
     *
     * @return list
     */
    public List<UserProfile> getAllUser() {
        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_EMAIL,
                COLUMN_USER_NAME,
                COLUMN_USER_USER_ID,
                COLUMN_USER_GUARDIAN_EMAIL,
                COLUMN_USER_CONTACT,
                COLUMN_USER_PASSWORD
        };
        // sorting orders
        String sortOrder =
                COLUMN_USER_NAME + " ASC";
        List<UserProfile> userList = new ArrayList<UserProfile>();

        SQLiteDatabase db = this.getReadableDatabase();

        // query the user table
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id,user_name,user_email,user_password FROM user ORDER BY user_name;
         */
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order


        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                UserProfile user = new UserProfile();
                user.setuserid(cursor.getString(cursor.getColumnIndex(COLUMN_USER_USER_ID)));
                user.setName(cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME)));
                user.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_USER_EMAIL)));
                user.setGuardemail(cursor.getString(cursor.getColumnIndex(COLUMN_USER_GUARDIAN_EMAIL)));
                user.setContact(cursor.getString(cursor.getColumnIndex(COLUMN_USER_CONTACT)));
                user.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_USER_PASSWORD)));
                // Adding user record to list
                userList.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return user list
        return userList;
    }

    /**
     * This method to update user record
     *
     * @param user
     */
    public void updateUser(UserProfile user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_USER_ID, user.getuserid());
        values.put(COLUMN_USER_NAME, user.getName());
        values.put(COLUMN_USER_EMAIL, user.getEmail());
        values.put(COLUMN_USER_GUARDIAN_EMAIL, user.getGuardemail());
        values.put(COLUMN_USER_CONTACT, user.getContact());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());

        // updating row
        db.update(TABLE_USER, values, COLUMN_USER_NAME + " = ?",
                new String[]{String.valueOf(user.getName())});
        db.close();
    }

    /**
     * This method is to delete user record
     *
     * @param user
     */
    public void deleteUser(UserProfile user) {
        SQLiteDatabase db = this.getWritableDatabase();
        // delete user record by id
        db.delete(TABLE_USER, COLUMN_USER_NAME + " = ?",
                new String[]{String.valueOf(user.getName())});
        db.close();
    }

    public ArrayList<HashMap<String,String>> getUserData() {
        ArrayList<HashMap<String,String>> arrayList = new ArrayList<>();
        HashMap<String,String> hashMap = new HashMap<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from UserData", null );
        if (res.moveToFirst()) {
            do{
                String string = res.getString(res.getColumnIndex(COLUMN_USER_USER_ID));
                hashMap.put(COLUMN_USER_USER_ID,string);
                string = res.getString(res.getColumnIndex(COLUMN_USER_NAME));
                hashMap.put(COLUMN_USER_NAME,string);
                string = res.getString(res.getColumnIndex(COLUMN_USER_EMAIL));
                hashMap.put(COLUMN_USER_EMAIL,string);
                string = res.getString(res.getColumnIndex(COLUMN_USER_GUARDIAN_EMAIL));
                hashMap.put(COLUMN_USER_GUARDIAN_EMAIL,string);
                string = res.getString(res.getColumnIndex(COLUMN_USER_CONTACT));
                hashMap.put(COLUMN_USER_CONTACT,string);
                string = res.getString(res.getColumnIndex(COLUMN_USER_PASSWORD));
                hashMap.put(COLUMN_USER_PASSWORD,string);
                arrayList.add(hashMap);
            }while (res.moveToNext());
        }
        return arrayList;
    }
    /**
     * This method to check user exist or not
     *
     * @param email
     * @return true/false
     */
    public boolean checkUser(String email) {

        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_NAME
        };

        SQLiteDatabase db = this.getReadableDatabase();

        // selection criteria
        String selection = COLUMN_USER_EMAIL + " = ?";

        // selection argument
        String[] selectionArgs = {email};

        // query user table with condition
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com';
         */
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0) {
            return true;
        }

        return false;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_USER);
        return numRows;
    }
    
    /**
     * This method to check user exist or not
     *
     * @param email
     * @param password
     * @return true/false
     */
    public boolean checkUser(String email, String password) {

        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_NAME
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_USER_EMAIL + " = ?" + " AND " + COLUMN_USER_PASSWORD + " = ?";

        // selection arguments
        String[] selectionArgs = {email, password};

        // query user table with conditions
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com' AND user_password = 'qwerty';
         */
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order

        int cursorCount = cursor.getCount();

        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }

        return false;
    }
    public JSONArray getResults() {
        SQLiteDatabase db = this.getReadableDatabase();
        String searchQuery = "SELECT * FROM UserData";
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