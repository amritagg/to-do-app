package com.example.todoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import com.example.todoapp.database.TaskDatabase;
import com.example.todoapp.database.TaskEntry;
import java.util.Date;

public class AddTaskActivity extends AppCompatActivity {

    public static final String EXTRA_TASK_ID = "extraTaskId";
    public static final String INSTANCE_TASK_ID = "instanceTaskId";

    public static final int PRIORITY_HIGH = 1;
    public static final int PRIORITY_MED = 2;
    public static final int PRIORITY_LOW = 3;

    private static final int DEFAULT_TASK_ID = -1;

    EditText mEditText;
    RadioGroup mRadioGroup;
    Button mButton;

    TaskDatabase mDb;

    private int mTaskId = DEFAULT_TASK_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        initViews();

        mDb = TaskDatabase.getInstance(getApplicationContext());

        if(savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_TASK_ID)){
            mTaskId = savedInstanceState.getInt(INSTANCE_TASK_ID, DEFAULT_TASK_ID);
        }

        Intent intent = getIntent();
        if(intent != null && intent.hasExtra(EXTRA_TASK_ID)){
            mButton.setText(getString(R.string.update_button));
            if(mTaskId == DEFAULT_TASK_ID){
                mTaskId = intent.getIntExtra(EXTRA_TASK_ID, DEFAULT_TASK_ID);

                AddTaskViewModelFactory factory = new AddTaskViewModelFactory(mDb, mTaskId);
                AddTaskViewModel model = new ViewModelProvider(this, factory).get(AddTaskViewModel.class);
                model.getTask().observe(this, this::populateUI);
            }
        }
    }

    private void populateUI(TaskEntry task) {

        if(task == null) return;

        mEditText.setText(task.getDescription());
        setPriorityInViews(task.getPriority());

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(INSTANCE_TASK_ID, mTaskId);
        super.onSaveInstanceState(outState);
    }

    private void initViews(){
        mEditText = findViewById(R.id.edit_text);
        mRadioGroup = findViewById(R.id.radio_group);
        mButton = findViewById(R.id.addButton);
        mButton.setOnClickListener(v -> onSaveButtonClicked());
    }

    private void onSaveButtonClicked() {
        String description = mEditText.getText().toString();
        int priority = getPriorityFromViews();
        Date date = new Date();

        final TaskEntry taskEntry = new TaskEntry(description, priority, date);

        TaskExecutors.getInstance().diskIO().execute(() -> {
            if(mTaskId == DEFAULT_TASK_ID) {
                mDb.taskDao().insertTask(taskEntry);
            }else {
                taskEntry.setId(mTaskId);
                mDb.taskDao().updateTask(taskEntry);
            }
            finish();
        });
    }

    @SuppressLint("NonConstantResourceId")
    public int getPriorityFromViews(){
        int priority = 1;
        int checkId = ((RadioGroup) findViewById(R.id.radio_group)).getCheckedRadioButtonId();

        switch (checkId){
            case R.id.radButton1:
                priority = PRIORITY_HIGH;
                break;
            case R.id.radButton2:
                priority = PRIORITY_MED;
                break;
            case R.id.radButton3:
                priority = PRIORITY_LOW;
        }
        return priority;
    }

    public void setPriorityInViews(int priority){
        switch (priority){
            case PRIORITY_HIGH:
                ((RadioGroup) findViewById(R.id.radio_group)).check(R.id.radButton1);
                break;
            case PRIORITY_MED:
                ((RadioGroup) findViewById(R.id.radio_group)).check(R.id.radButton2);
                break;
            case PRIORITY_LOW:
                ((RadioGroup) findViewById(R.id.radio_group)).check(R.id.radButton3);
        }
    }
}