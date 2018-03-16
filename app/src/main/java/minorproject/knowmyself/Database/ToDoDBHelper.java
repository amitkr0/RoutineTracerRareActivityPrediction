package minorproject.knowmyself.Database;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ToDoDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "ToDoList.db";
    public static final String CONTACTS_TABLE_NAME = "ToDo";
    public static final String CONTACTS_COLUMN_USERID = "userid";
    public static final String CONTACTS_COLUMN_NAME = "activity";
    public static final String CONTACTS_COLUMN_DATE = "date";
    public static final String CONTACTS_COLUMN_STARTTIME = "starttime";
    public static final String CONTACTS_COLUMN_ENDTIME = "endtime";
    public static final String CONTACTS_COLUMN_LOCATION = "location";
    private HashMap hp;

    public ToDoDBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table ToDo(userid text,activity text,date text,starttime text,endtime text,location text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS ToDo");
        onCreate(db);
    }

    public boolean insertToDo (String userid, String activity, String date, String starttime,String endtime,String location) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("userid", userid);
        contentValues.put("activity",activity);
        contentValues.put("date",date);
        contentValues.put("starttime", starttime);
        contentValues.put("endtime",endtime);
        contentValues.put("location",location);
        db.insert("ToDo", null, contentValues);
        return true;
    }

    public Cursor getData(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from ToDo where id="+id+"", null );
        return res;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, CONTACTS_TABLE_NAME);
        return numRows;
    }

    public Integer deleteToDo (String activity) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("ToDo",
                "name = ? ",
                new String[] { activity });
    }

    public ArrayList<String> getToDoList() {
        System.out.println("ml ja yaar");
        ArrayList<String> array_list = new ArrayList<String>();
        System.out.println("inside getall");
        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from ToDo", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(CONTACTS_COLUMN_NAME)));
            res.moveToNext();
        }
        return array_list;
    }
    public JSONArray getResults() {
        SQLiteDatabase db = this.getReadableDatabase();
        String searchQuery = "SELECT * FROM ToDo";//todo change contacy
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