package com.example.android.justdoit;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class Repository {

    TaskDao taskDao;
    LiveData<List<TaskItem>> taskListItem;

    public Repository(Application application){

        TaskDatabase taskDatabase = TaskDatabase.getDatabase(application);
        taskDao = taskDatabase.taskDao();
        taskListItem = taskDao.getAllTask();
    }

    //Get all List of tasks
    public LiveData<List<TaskItem>>getTaskListItem(){
        return taskListItem;
    }

    //Insert a task
    public void insertTask(TaskItem taskItem){

        new insertAsyncTask(taskDao).execute(taskItem);
    }

    //delete a task item
    public void deleteTask(TaskItem taskItem){

        new deleteAsyncTask(taskDao).execute(taskItem);
    }

    //Delete all tasks
    public void deleteAllTask(){

        new deleteAllAsyncTask(taskDao).execute();
    }

    //Update a single task
    public void updateTask(TaskItem taskItem){

        new updateAsyncTask(taskDao).execute(taskItem);
    }

    //AsyncTask to insert a task
    private class insertAsyncTask extends AsyncTask<TaskItem, Void, Void>{

        TaskDao taskDao;
        insertAsyncTask(TaskDao taskDao){
            this.taskDao = taskDao;
        }

        @Override
        protected Void doInBackground(TaskItem... taskItems) {

            taskDao.insertTask(taskItems[0]);
            return null;
        }
    }

    //AsyncTask to delete a single task
    private class deleteAsyncTask extends AsyncTask<TaskItem, Void, Void>{

        TaskDao taskDao;
        deleteAsyncTask(TaskDao taskDao){
            this.taskDao = taskDao;
        }

        @Override
        protected Void doInBackground(TaskItem... taskItems) {

            taskDao.deleteTask(taskItems[0]);
            return null;
        }
    }

    //Async task to Delete All tasks
    private class deleteAllAsyncTask extends AsyncTask<Void, Void, Void>{

        TaskDao taskDao;
        deleteAllAsyncTask(TaskDao taskDao){
            this.taskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            taskDao.deleteAll();
            return null;
        }
    }

    //AsyncTask to update a task
    private class updateAsyncTask extends AsyncTask<TaskItem, Void, Void>{

        TaskDao taskDao;

        updateAsyncTask(TaskDao taskDao){
            this.taskDao = taskDao;
        }

        @Override
        protected Void doInBackground(TaskItem... taskItems) {

            taskDao.updateTask(taskItems[0]);
            return null;
        }
    }
}
