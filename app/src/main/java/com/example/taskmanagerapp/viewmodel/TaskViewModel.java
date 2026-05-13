package com.example.taskmanagerapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.taskmanagerapp.data.TaskRepository;
import com.example.taskmanagerapp.data.local.TaskEntry;

import java.util.List;

public class TaskViewModel extends AndroidViewModel {

    private final TaskRepository repo;
    private final LiveData<List<TaskEntry>> entryList;

    public TaskViewModel(@NonNull Application application) {
        super(application);
        repo = new TaskRepository(application);
        entryList = repo.getEntryList();
    }

    public void addEntry(TaskEntry entry) {
        repo.addEntry(entry);
    }

    public void removeEntry(TaskEntry entry) {
        repo.removeEntry(entry);
    }

    public void wipeAllEntries() {
        repo.wipeAllEntries();
    }

    public LiveData<List<TaskEntry>> getEntryList() {
        return entryList;
    }
}
