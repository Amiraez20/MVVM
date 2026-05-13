package com.example.taskmanagerapp.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmanagerapp.R;
import com.example.taskmanagerapp.data.local.TaskEntry;
import com.example.taskmanagerapp.viewmodel.TaskViewModel;

public class HomeActivity extends AppCompatActivity {

    private TaskViewModel taskViewModel;
    private EditText inputTitle;
    private EditText inputBody;
    private Button btnSaveTask;
    private Button btnClearAll;
    private TaskListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        inputTitle  = findViewById(R.id.inputTitle);
        inputBody   = findViewById(R.id.inputBody);
        btnSaveTask = findViewById(R.id.btnSaveTask);
        btnClearAll = findViewById(R.id.btnClearAll);

        RecyclerView taskRecyclerView = findViewById(R.id.taskRecyclerView);
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskRecyclerView.setHasFixedSize(true);

        listAdapter = new TaskListAdapter();
        taskRecyclerView.setAdapter(listAdapter);

        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        taskViewModel.getEntryList().observe(this, entries -> {
            listAdapter.refreshList(entries);
        });

        btnSaveTask.setOnClickListener(v -> persistEntry());

        btnClearAll.setOnClickListener(v -> {
            taskViewModel.wipeAllEntries();
            Toast.makeText(this, "Toutes les tâches ont été supprimées", Toast.LENGTH_SHORT).show();
        });

        listAdapter.setHoldListener(entry -> {
            taskViewModel.removeEntry(entry);
            Toast.makeText(this, "Tâche supprimée", Toast.LENGTH_SHORT).show();
        });

        listAdapter.setTapListener(entry ->
                Toast.makeText(this, "Tâche : " + entry.getTaskTitle(), Toast.LENGTH_SHORT).show()
        );
    }

    private void persistEntry() {
        String title = inputTitle.getText().toString().trim();
        String body  = inputBody.getText().toString().trim();

        if (title.isEmpty() || body.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        TaskEntry newEntry = new TaskEntry(title, body);
        taskViewModel.addEntry(newEntry);

        inputTitle.setText("");
        inputBody.setText("");

        Toast.makeText(this, "Tâche enregistrée", Toast.LENGTH_SHORT).show();
    }
}