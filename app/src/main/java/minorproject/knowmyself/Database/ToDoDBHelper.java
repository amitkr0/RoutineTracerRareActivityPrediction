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
import java.util.List;

import minorproject.knowmyself.Other.ToDoBean;

public class ToDoDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "ToDoList.db";
    public static final String TODO_TABLE_NAME = "ToDo";
    public static final String TODO_COLUMN_NAME = "event";
    public static final String TODO_COLUMN_DATE = "date";
    public static final String TODO_COLUMN_STARTTIME = "starttime";
    public static final String TODO_COLUMN_ENDTIME = "endtime";
    public static final String TODO_COLUMN_LOCATION = "location";
    private HashMap hp;
    private Context context;

    public ToDoDBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table ToDo(event text,date text,starttime text,endtime text,location text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS ToDo");
        onCreate(db);
    }

    public boolean insertToDo (String event, String date, String starttime,String endtime,String location) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("event",event);
        contentValues.put("date",date);
        contentValues.put("starttime", starttime);
        contentValues.put("endtime",endtime);
        contentValues.put("location",location);
        db.insert("ToDo", null, contentValues);
        Toast.makeText(context,"inserted here", Toast.LENGTH_SHORT).show();
        return true;
    }

    public List<ToDoBean> getData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from ToDo", null );
        List<ToDoBean> list = new ArrayList<>();
        while (res.moveToNext()) {
            String event = res.getString(0);
            String date = res.getString(1);
            String inTime = res.getString(2);
            String outTime = res.getString(3);
            String location = res.getString(4);
            ToDoBean toDoBean = new ToDoBean(event,date,inTime,outTime,location);
            list.add(toDoBean);
        }
        return list;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TODO_TABLE_NAME);
        return numRows;
    }

    public boolean deleteToDo (String activity) {
        Toast.makeText(context,"inside detlete",Toast.LENGTH_SHORT).show();
        SQLiteDatabase db = this.getWritableDatabase();
        Log.v("abcd","a      "+ db+"      p");
       /* return db.delete("ToDo",
                "event = ? ",
                new String[] { activity });
        Log.v("abcd","reached here"+activity);*/
        return db.delete(TODO_TABLE_NAME, TODO_COLUMN_NAME + "= '" + activity+"';", null) > 0;
    }

    public ArrayList<String> getToDoList() {
        ArrayList<String> array_list = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from ToDo", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(TODO_COLUMN_NAME)));
            res.moveToNext();
        }
        return array_list;
    }
    public JSONArray getResults() {
        SQLiteDatabase db = this.getReadableDatabase();
        String searchQuery = "SELECT * FROM ToDo";
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