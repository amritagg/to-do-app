package com.example.todoapp;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.todoapp.database.TaskDatabase;

public class AddTaskViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final TaskDatabase mDb;
    private final int taskId;

    public AddTaskViewModelFactory(TaskDatabase mDb, int taskId) {
        this.mDb = mDb;
        this.taskId = taskId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new AddTaskViewModel(mDb, taskId);
    }
}
