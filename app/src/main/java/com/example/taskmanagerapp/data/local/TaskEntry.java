package com.example.taskmanagerapp.data.local;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "task_entries")
public class TaskEntry {

    @PrimaryKey(autoGenerate = true)
    private int entryId;

    private String taskTitle;
    private String taskBody;

    public TaskEntry(String taskTitle, String taskBody) {
        this.taskTitle = taskTitle;
        this.taskBody = taskBody;
    }

    public int getEntryId() {
        return entryId;
    }

    public void setEntryId(int entryId) {
        this.entryId = entryId;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public String getTaskBody() {
        return taskBody;
    }
}