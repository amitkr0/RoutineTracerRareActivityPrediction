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

import java.util.ArrayList;
import java.util.HashMap;

import minorproject.knowmyself.Other.UserLocation;

public class MapDBHelper extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "MapDetails.db";
    private static final String TABLE_NAME = "PlaceDetails";
    private static final String userid = "userid";
    private static final String latitude = "latitude";
    private static final String longitude = "longitude";
    private static final String inTime = "inTime";
    private static final String outTime = "outTime";
    private static final String  placeClass = "placeClass";
    private Context _context;

    public MapDBHelper(Context context) {

        super(context, DATABASE_NAME , null, 1);
        _context = context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table PlaceDetails " +
                        "(userid text, latitude text not null,longitude text not null,inTime text," +
                        "outTime text,placeClass integer default 0)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        // Create tables again
        onCreate(db);
    }

    public boolean insertContact (UserLocation ob,String userID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("userid",userID);
        contentValues.put("latitude",Double.toString(ob.getLatLng().latitude) );
        contentValues.put("longitude",Double.toString(ob.getLatLng().longitude));
        contentValues.put("inTime",ob.getInTime());
        contentValues.put("outTime",ob.getOutTime());
        contentValues.put("placeType",ob.getLocationType());
        db.insert("PlaceDetails", null, contentValues);
        Toast.makeText(_context,"Inserted  -- (mApDBHelper",Toast.LENGTH_LONG).show();
        return true;
    }

    public Cursor getData(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from PlaceDetails where userid="+id+"", null );
        return res;
    }

    public ArrayList<HashMap<String,String>> getMapData() {
        ArrayList<HashMap<String,String>> arrayList = new ArrayList<>();
        HashMap<String,String> hashMap = new HashMap<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from PlaceDetails ", null );
        if (res.moveToFirst()) {
            do{
                String string = res.getString(res.getColumnIndex(userid));
                hashMap.put(userid,string);
                string = res.getString(res.getColumnIndex(latitude));
                hashMap.put(latitude,string);
                string = res.getString(res.getColumnIndex(longitude));
                hashMap.put(longitude,string);
                string = res.getString(res.getColumnIndex(inTime));
                hashMap.put(inTime,string);
                string = res.getString(res.getColumnIndex(outTime));
                hashMap.put(outTime,string);
                string = res.getString(res.getColumnIndex(placeClass));
                hashMap.put(placeClass,string);
                arrayList.add(hashMap);
            }while (res.moveToNext());
        }
        return arrayList;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        return numRows;
    }

    public boolean updateContact (String id,UserLocation ob) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("latitude",ob.getLatLng().latitude );
        contentValues.put("longitude",ob.getLatLng().longitude);
        contentValues.put("inTime",ob.getInTime());
        contentValues.put("outTime",ob.getOutTime());
        contentValues.put("placeType",ob.getLocationType());
        db.update("PlaceDetails", contentValues, "userid = ? ", new String[] { id } );
        return true;
    }

    public Integer deleteContact (String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("PlaceDetails",
                "id = ? ",
                new String[] { id });
    }

    public JSONArray getResults() {
        SQLiteDatabase db = this.getReadableDatabase();
        String searchQuery = "SELECT * FROM PlaceDetails";
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
