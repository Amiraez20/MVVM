package com.example.taskmanagerapp.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.taskmanagerapp.data.local.TaskDao;
import com.example.taskmanagerapp.data.local.TaskDatabase;
import com.example.taskmanagerapp.data.local.TaskEntry;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskRepository {

    private final TaskDao taskDao;
    private final LiveData<List<TaskEntry>> entryList;
    private final ExecutorService backgroundWorker;

    public TaskRepository(Application application) {
        TaskDatabase db = TaskDatabase.getInstance(application);
        taskDao = db.taskDao();
        entryList = taskDao.fetchAllEntries();
        backgroundWorker = Executors.newSingleThreadExecutor();
    }

    public void addEntry(TaskEntry entry) {
        backgroundWorker.execute(() -> taskDao.addEntry(entry));
    }

    public void removeEntry(TaskEntry entry) {
        backgroundWorker.execute(() -> taskDao.removeEntry(entry));
    }

    public void wipeAllEntries() {
        backgroundWorker.execute(taskDao::wipeAllEntries);
    }

    public LiveData<List<TaskEntry>> getEntryList() {
        return entryList;
    }
}
