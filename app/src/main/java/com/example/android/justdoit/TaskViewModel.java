package com.example.android.justdoit;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class TaskViewModel extends AndroidViewModel {

    Repository repository;
    LiveData<List<TaskItem>> listLiveData;

    public TaskViewModel(@NonNull Application application) {
        super(application);

        repository = new Repository(application);
        listLiveData = repository.getTaskListItem();
    }

    //Get list of task
    public LiveData<List<TaskItem>> getTaskList(){
        return  listLiveData;
    }

    //Insert task
    public void insertTask(TaskItem taskItem){

        repository.insertTask(taskItem);
    }

    //Delete a single task
    public void deleteSingleTask(TaskItem taskItem){
        repository.deleteTask(taskItem);
    }

    //Delete all task
    public void deleteAllTask(){
        repository.deleteAllTask();
    }

    //Update a task
    public void updateSingleTask(TaskItem item){
        repository.updateTask(item);
    }
}
