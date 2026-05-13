package com.example.taskmanagerapp.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TaskDao {

    @Insert
    void addEntry(TaskEntry entry);

    @Delete
    void removeEntry(TaskEntry entry);

    @Query("DELETE FROM task_entries")
    void wipeAllEntries();

    @Query("SELECT * FROM task_entries ORDER BY entryId DESC")
    LiveData<List<TaskEntry>> fetchAllEntries();
}
