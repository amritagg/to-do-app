package com.example.todoapp;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.todoapp.database.TaskDatabase;
import com.example.todoapp.database.TaskEntry;
import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private final LiveData<List<TaskEntry>> tasks;

    public MainViewModel(@NonNull Application application) {
        super(application);
        TaskDatabase taskDatabase = TaskDatabase.getInstance(this.getApplication());
        tasks = taskDatabase.taskDao().loadAllTasks();
    }

    public LiveData<List<TaskEntry>> getTasks() {
        return tasks;
    }
}
