package com.technoidtintin.android.justdoit.Model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "completed_task")
public class CompletedTask {

    @PrimaryKey(autoGenerate = true)
    int id;

    public int cImage;
    public String cTaskName;
    public String cTaskDescription;
    public String cStartTime;

    public CompletedTask(int cImage, String cTaskName, String cTaskDescription, String cStartTime) {
        this.cImage = cImage;
        this.cTaskName = cTaskName;
        this.cTaskDescription = cTaskDescription;
        this.cStartTime = cStartTime;
    }

    @Ignore
    public CompletedTask(int id, int cImage, String cTaskName, String cTaskDescription, String cStartTime) {
        this.id = id;
        this.cImage = cImage;
        this.cTaskName = cTaskName;
        this.cTaskDescription = cTaskDescription;
        this.cStartTime = cStartTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getcImage() {
        return cImage;
    }

    public void setcImage(int cImage) {
        this.cImage = cImage;
    }

    public String getcTaskName() {
        return cTaskName;
    }

    public void setcTaskName(String cTaskName) {
        this.cTaskName = cTaskName;
    }

    public String getcTaskDescription() {
        return cTaskDescription;
    }

    public void setcTaskDescription(String cTaskDescription) {
        this.cTaskDescription = cTaskDescription;
    }

    public String getcStartTime() {
        return cStartTime;
    }

    public void setcStartTime(String cStartTime) {
        this.cStartTime = cStartTime;
    }
}
