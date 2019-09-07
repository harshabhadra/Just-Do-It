package com.technoidtintin.android.justdoit;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.technoidtintin.android.justdoit.Dao.CompletedDao;
import com.technoidtintin.android.justdoit.Dao.TaskDao;
import com.technoidtintin.android.justdoit.Model.CompletedTask;
import com.technoidtintin.android.justdoit.Model.TaskItem;

@Database(entities = {TaskItem.class,CompletedTask.class}, version = 6, exportSchema = false)
public abstract class TaskDatabase extends RoomDatabase {

    public abstract TaskDao taskDao();
    public abstract CompletedDao completedDao();

    private static TaskDatabase INSTANCE;

    public static TaskDatabase getDatabase(Context context){
        if (INSTANCE == null){
            synchronized (TaskDatabase.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            TaskDatabase.class,"task_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }

        return INSTANCE;
    }
}
