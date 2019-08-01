package com.example.android.justdoit.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.android.justdoit.Model.TaskItem;

import java.util.List;

@Dao
public interface TaskDao {

    @Insert
    public void insertTask(TaskItem taskItem);

    @Delete
    public void deleteTask(TaskItem taskItem);

    @Query("DELETE from to_do")
    public void deleteAll();

    @Update
    public void updateTask(TaskItem... taskItem);

    @Query("SELECT * from to_do ORDER BY id ASC")
    LiveData<List<TaskItem>> getAllTask();
}
