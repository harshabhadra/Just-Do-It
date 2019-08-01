package com.example.android.justdoit.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "to_do")
public class TaskItem implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @ColumnInfo(name = "image")
    public Integer categoryImage;

    @NonNull
    @ColumnInfo(name = "name")
    public String taskName;

    @ColumnInfo(name = "description")
    public String taskDescription;

    @NonNull
    @ColumnInfo(name = "time")
    public String timeinSeconds;

    public TaskItem(Integer categoryImage, String taskName, String taskDescription, String timeinSeconds) {
        this.categoryImage = categoryImage;
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.timeinSeconds = timeinSeconds;
    }

    @Ignore
    public TaskItem(int id, Integer categoryImage, String taskName, String taskDescription, String timeinSeconds) {
        this.id = id;
        this.categoryImage = categoryImage;
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.timeinSeconds = timeinSeconds;
    }

    protected TaskItem(Parcel in) {
        id = in.readInt();
        if (in.readByte() == 0) {
            categoryImage = null;
        } else {
            categoryImage = in.readInt();
        }
        taskName = in.readString();
        taskDescription = in.readString();
        timeinSeconds = in.readString();
    }

    public static final Creator<TaskItem> CREATOR = new Creator<TaskItem>() {
        @Override
        public TaskItem createFromParcel(Parcel in) {
            return new TaskItem(in);
        }

        @Override
        public TaskItem[] newArray(int size) {
            return new TaskItem[size];
        }
    };

    public Integer getCategoryImage() {
        return categoryImage;
    }

    public void setCategoryImage(Integer categoryImage) {
        this.categoryImage = categoryImage;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public String getTimeinSeconds() {
        return timeinSeconds;
    }

    public void setTimeinSeconds(String timeinSeconds) {
        this.timeinSeconds = timeinSeconds;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        if (categoryImage == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(categoryImage);
        }
        parcel.writeString(taskName);
        parcel.writeString(taskDescription);
        parcel.writeString(timeinSeconds);
    }
}
