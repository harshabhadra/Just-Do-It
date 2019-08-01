package com.example.android.justdoit;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.android.justdoit.Dao.CompletedDao;
import com.example.android.justdoit.Dao.TaskDao;
import com.example.android.justdoit.Model.CompletedTask;
import com.example.android.justdoit.Model.TaskItem;

import java.util.List;

public class Repository {

    TaskDao taskDao;
    CompletedDao completedDao;
    LiveData<List<TaskItem>> taskListItem;
    LiveData<List<CompletedTask>>completedList;

    public Repository(Application application) {

        TaskDatabase taskDatabase = TaskDatabase.getDatabase(application);
        taskDao = taskDatabase.taskDao();
        completedDao = taskDatabase.completedDao();
        taskListItem = taskDao.getAllTask();
        completedList = completedDao.getAllTask();
    }

    //Get all List of tasks
    public LiveData<List<TaskItem>> getTaskListItem() {
        return taskListItem;
    }

    //Get all completed tasks
    public LiveData<List<CompletedTask>>getCompletedList(){
        return completedList;
    }

    //Insert a task
    public void insertTask(TaskItem taskItem) {

        new insertAsyncTask(taskDao).execute(taskItem);
    }

    //Insert a task when completed
    public void insertCompletedTask(CompletedTask completedTask){

        new insertCompletedAsyncTask(completedDao).execute(completedTask);
    }

    //delete a task item
    public void deleteTask(TaskItem taskItem) {

        new deleteAsyncTask(taskDao).execute(taskItem);
    }

    //Delete a task from completed list
    public void deleteCompletedTask(CompletedTask completedTask){

        new deleteCompletedTask(completedDao).execute(completedTask);
    }

    //Delete all tasks
    public void deleteAllTask() {

        new deleteAllAsyncTask(taskDao).execute();
    }

    //delete all completed task
    public void deleteAllCompletedTask(){

        new deleteAllCompletedTask(completedDao).execute();
    }

    //Update a single task
    public void updateTask(TaskItem taskItem) {

        new updateAsyncTask(taskDao).execute(taskItem);
    }

    //AsyncTask to insert a task
    private class insertAsyncTask extends AsyncTask<TaskItem, Void, Void> {

        TaskDao taskDao;

        insertAsyncTask(TaskDao taskDao) {
            this.taskDao = taskDao;
        }

        @Override
        protected Void doInBackground(TaskItem... taskItems) {

            taskDao.insertTask(taskItems[0]);
            return null;
        }
    }

    //AsyncTask to insert a completed task
    private class insertCompletedAsyncTask extends AsyncTask<CompletedTask, Void, Void>{

        CompletedDao dao;

        insertCompletedAsyncTask(CompletedDao dao){
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(CompletedTask... completedTasks) {
            dao.insertTask(completedTasks[0]);
            return null;
        }
    }

    //AsyncTask to delete a single task
    private class deleteAsyncTask extends AsyncTask<TaskItem, Void, Void> {

        TaskDao taskDao;

        deleteAsyncTask(TaskDao taskDao) {
            this.taskDao = taskDao;
        }

        @Override
        protected Void doInBackground(TaskItem... taskItems) {

            taskDao.deleteTask(taskItems[0]);
            return null;
        }
    }

    //AsyncTask to delete a completed Task
    private class deleteCompletedTask extends AsyncTask<CompletedTask, Void, Void>{

        CompletedDao dao;

        deleteCompletedTask(CompletedDao dao){
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(CompletedTask... completedTasks) {

            dao.deleteTask(completedTasks[0]);
            return null;
        }
    }

    //Async task to Delete All tasks
    private class deleteAllAsyncTask extends AsyncTask<Void, Void, Void> {

        TaskDao taskDao;

        deleteAllAsyncTask(TaskDao taskDao) {
            this.taskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            taskDao.deleteAll();
            return null;
        }
    }

    //AsyncTask to delete all completedTask
    private class deleteAllCompletedTask extends AsyncTask<Void, Void, Void>{
        CompletedDao dao;

        deleteAllCompletedTask(CompletedDao dao){
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            dao.deleteAll();
            return null;
        }
    }

    //AsyncTask to update a task
    private class updateAsyncTask extends AsyncTask<TaskItem, Void, Void> {

        TaskDao taskDao;

        updateAsyncTask(TaskDao taskDao) {
            this.taskDao = taskDao;
        }

        @Override
        protected Void doInBackground(TaskItem... taskItems) {

            taskDao.updateTask(taskItems[0]);
            return null;
        }
    }
}
