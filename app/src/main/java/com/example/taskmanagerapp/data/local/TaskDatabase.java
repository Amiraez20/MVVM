package com.example.taskmanagerapp.data.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {TaskEntry.class}, version = 1, exportSchema = false)
public abstract class TaskDatabase extends RoomDatabase {

    public abstract TaskDao taskDao();

    private static volatile TaskDatabase sharedInstance;

    public static TaskDatabase getInstance(Context context) {
        if (sharedInstance == null) {
            synchronized (TaskDatabase.class) {
                if (sharedInstance == null) {
                    sharedInstance = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    TaskDatabase.class,
                                    "task_manager_db"
                            )
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return sharedInstance;
    }
}
