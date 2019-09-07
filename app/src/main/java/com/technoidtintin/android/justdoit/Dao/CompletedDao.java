package com.technoidtintin.android.justdoit.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.technoidtintin.android.justdoit.Model.CompletedTask;

import java.util.List;

@Dao
public interface CompletedDao {

    @Insert
    public void insertTask(CompletedTask task);

    @Delete
    public void deleteTask(CompletedTask task);

    @Query("DELETE from completed_task")
    public void deleteAll();

    @Query("SELECT * from completed_task ORDER BY id ASC")
    LiveData<List<CompletedTask>> getAllTask();
}
