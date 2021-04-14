package com.example.todoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import com.example.todoapp.database.TaskDatabase;
import com.example.todoapp.database.TaskEntry;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TaskAdapter.ItemClickListener{

    private TaskAdapter mTaskAdapter;
    private TaskDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mTaskAdapter = new TaskAdapter(this, this);
        mRecyclerView.setAdapter(mTaskAdapter);

        DividerItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(decoration);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddTaskActivity.class);
            startActivity(intent);
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                TaskExecutors.getInstance().diskIO().execute(() -> {
                    List<TaskEntry> task = mTaskAdapter.getTasks();
                    int position = viewHolder.getAdapterPosition();
                    mDb.taskDao().deleteTask(task.get(position));
                });
            }
        }).attachToRecyclerView(mRecyclerView);

        mDb = TaskDatabase.getInstance(getApplicationContext());

        setupViewModel();
    }

    private void setupViewModel() {

        MainViewModel viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        viewModel.getTasks().observe(this, taskEntries -> mTaskAdapter.setTasks(taskEntries));
    }

    @Override
    public void onItemClickListener(int itemId) {
        Intent intent = new Intent(this, AddTaskActivity.class);
        intent.putExtra(AddTaskActivity.EXTRA_TASK_ID, itemId);
        startActivity(intent);
    }

}