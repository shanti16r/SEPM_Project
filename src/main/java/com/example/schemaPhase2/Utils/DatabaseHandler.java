package com.example.schemaPhase2.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.schemaPhase2.Model.ToDoModel;

import java.net.PasswordAuthentication;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int VERSION=1;
    private static final String NAME = "toDoListDatabase";
    private static final String TODO_TABLE = "todo";
    private static final String LOGIN_TABLE = "login";
    private static final String ID = "id";
    private static final String LOGIN_ID = "login_id";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "Password";
    private static final String TASK = "task";

    private static final String TASKTIME = "taskTime";
    private static final String TASKDATE = "taskDate";

    private static final String STATUS = "status";
    private static final String CREATE_TODO_TABLE = " CREATE TABLE " + TODO_TABLE + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                                                   + TASK + " TEXT, "+ TASKDATE + " TEXT, "+ TASKTIME + " TEXT, " + STATUS + " INTEGER)";
    private static final String CREATE_LOGIN_TABLE = " CREATE TABLE " + LOGIN_TABLE + "(" + LOGIN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + USERNAME + " TEXT, "
            + PASSWORD + " TEXT)";
    private SQLiteDatabase db;

    public DatabaseHandler(Context context){
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_TODO_TABLE);
        db.execSQL(CREATE_LOGIN_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        //Drop the older tables
        db.execSQL("DROP TABLE IF EXISTS " + TODO_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + LOGIN_TABLE);
        //Create tables again
        onCreate(db);
    }

    public Boolean insertData(String username, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("password", password);
        long result = db.insert("login", null, contentValues);
        if(result==-1) return false;
        else
            return true;
    }

    public Boolean checkUsername(String username){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM login WHERE username = ?", new String[]{username});
        if(cursor.getCount()>0)
            return true;
        else
            return false;
    }

    public Boolean checkUsernamePassword(String username, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from login where username = ? and password = ?", new String[]{username, password});
        if(cursor.getCount()>0)
            return true;
        else
            return false;
    }


    public void openDatabase(){
        db = this.getWritableDatabase();
    }

    public void insertTask(ToDoModel task){
        ContentValues cv = new ContentValues();
        cv.put(TASK, task.getTask());

        cv.put(TASKTIME, task.getTaskTime());
        cv.put(TASKDATE, task.getTaskDate());

        cv.put(STATUS, 0);
        db.insert(TODO_TABLE, null, cv);

    }

    public List<ToDoModel> getAllTasks(){
        List<ToDoModel> taskList = new ArrayList<>();
        Cursor cur = null;
        db.beginTransaction();
        try{
            cur = db.query(TODO_TABLE, null,null,null, null,null,null,null);
            if(cur != null){
                if(cur.moveToFirst()){
                    do{
                        ToDoModel task = new ToDoModel();
                        task.setId(cur.getInt(cur.getColumnIndex(ID)));
                        task.setTask(cur.getString(cur.getColumnIndex(TASK)));

                        task.setTaskTime(cur.getString(cur.getColumnIndex(TASKTIME)));
                        task.setTaskDate(cur.getString(cur.getColumnIndex(TASKDATE)));


                        task.setStatus(cur.getInt(cur.getColumnIndex(STATUS)));
                        taskList.add(task);
                    }while(cur.moveToNext());
                }
            }

        }
        finally{
            db.endTransaction();
            assert cur != null;
            cur.close();
        }
        return taskList;

    }
    public void updateStatus(int id, int status){
        ContentValues cv = new ContentValues();
        cv.put(STATUS, status);
        db.update(TODO_TABLE, cv, ID + "= ?", new String[] {String.valueOf(id)});
    }

    public void updateTask(int id, String task, String taskDate, String taskTime){
        ContentValues cv = new ContentValues();
        cv.put(TASK, task);
        cv.put(TASKDATE, taskDate);
        cv.put(TASKTIME,taskTime);
        db.update(TODO_TABLE, cv, ID +"= ?", new String[] {String.valueOf(id)});
    }

    public void deleteTask(int id){
        db.delete(TODO_TABLE, ID + "= ?", new String[] {String.valueOf(id)});
    }

}
