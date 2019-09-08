package com.technoidtintin.android.justdoit;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.technoidtintin.android.justdoit.Model.CompletedTask;
import com.technoidtintin.android.justdoit.Model.TaskItem;
import com.technoidtintin.android.justdoit.Worker.NotificationWorker;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class TaskViewModel extends AndroidViewModel {

    Repository repository;
    public WorkManager workManager;
    LiveData<List<TaskItem>> listLiveData;
    LiveData<List<CompletedTask>>completedTaskList;

    public TaskViewModel(@NonNull Application application) {
        super(application);

        workManager = WorkManager.getInstance(application);
        repository = new Repository(application);
        listLiveData = repository.getTaskListItem();
        completedTaskList = repository.getCompletedList();
    }

    public void setNotification(String message, long delay){

        OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(NotificationWorker.class)
                .setInitialDelay(delay, TimeUnit.MINUTES)
                .setInputData(stringToData(message))
                .build();
        workManager.enqueue(oneTimeWorkRequest);
    }

    public void stopNottification(){
        workManager.cancelAllWork();
    }

    private Data stringToData(String s){

        Data.Builder builder = new Data.Builder();
        builder.putString("message", s);
        return builder.build();
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
