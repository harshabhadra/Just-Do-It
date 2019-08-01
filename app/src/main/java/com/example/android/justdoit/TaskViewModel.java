package com.example.android.justdoit;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.android.justdoit.Model.CompletedTask;
import com.example.android.justdoit.Model.TaskItem;

import java.util.List;

public class TaskViewModel extends AndroidViewModel {

    Repository repository;
    LiveData<List<TaskItem>> listLiveData;
    LiveData<List<CompletedTask>>completedTaskList;

    public TaskViewModel(@NonNull Application application) {
        super(application);

        repository = new Repository(application);
        listLiveData = repository.getTaskListItem();
        completedTaskList = repository.getCompletedList();
    }

    //Get list of task
    public LiveData<List<TaskItem>> getTaskList(){
        return  listLiveData;
    }

    //Get list of completed task
    public LiveData<List<CompletedTask>>getCompletedTaskList(){
        return completedTaskList;
    }

    //Insert task
    public void insertTask(TaskItem taskItem){

        repository.insertTask(taskItem);
    }

    //Insert completed task
    public void insertCompletedTask(CompletedTask completedTask){
        repository.insertCompletedTask(completedTask);
    }

    //Delete a single task
    public void deleteSingleTask(TaskItem taskItem){
        repository.deleteTask(taskItem);
    }

    //Delete a completed task
    public void deleteCompletedTask(CompletedTask completedTask){
        repository.deleteCompletedTask(completedTask);
    }

    //Delete all task
    public void deleteAllTask(){
        repository.deleteAllTask();
    }

    //Delete all completed Task
    public void deleteAllCompletedTask(){
        repository.deleteAllCompletedTask();
    }

    //Update a task
    public void updateSingleTask(TaskItem item){
        repository.updateTask(item);
    }
}
