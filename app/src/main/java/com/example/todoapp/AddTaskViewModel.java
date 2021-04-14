package com.example.todoapp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.todoapp.database.TaskDatabase;
import com.example.todoapp.database.TaskEntry;

public class AddTaskViewModel extends ViewModel {

    private final LiveData<TaskEntry> mTask;

    public AddTaskViewModel(TaskDatabase mDb, int taskId) {
        mTask = mDb.taskDao().listTaskById(taskId);
    }

    public LiveData<TaskEntry> getTask(){
        return mTask;
    }

}
