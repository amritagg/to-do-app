package com.example.todoapp.database;

import android.content.Context;
import android.util.Log;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {TaskEntry.class}, version = 1, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class TaskDatabase extends RoomDatabase {

    private static final String LOG = TaskDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "todolist.db";
    private static TaskDatabase sTaskDatabase;

    public static TaskDatabase getInstance(Context context){
        if(sTaskDatabase == null){
            synchronized (LOCK){
                Log.e(LOG, "Creating a new Database");
                sTaskDatabase = Room.databaseBuilder(context.getApplicationContext(),
                        TaskDatabase.class,
                        TaskDatabase.DATABASE_NAME)
                        .build();
            }
        }

        Log.e(LOG, "Getting the database Instance");
        return sTaskDatabase;
    }

    public abstract TaskDao taskDao();

}
