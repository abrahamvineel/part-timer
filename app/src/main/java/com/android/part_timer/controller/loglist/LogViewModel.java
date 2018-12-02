package com.android.part_timer.controller.loglist;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import com.android.part_timer.database.entity.LogData;

public class LogViewModel extends AndroidViewModel {

    public LogViewModel(@NonNull Application application) {
        super(application);
        repository = new LogRepository(application);
        allLogs = repository.getAllNotes();
    }

    private LogRepository repository;
    private LiveData<List<LogData>> allLogs;

    public void insert(LogData logData) {
        repository.insert(logData);
    }

    public void update(LogData logData) {
        repository.update(logData);
    }

    public void delete(LogData logData) {
        repository.delete(logData);
    }

    public void deleteAllNotes() {
        repository.deleteAllNotes();
    }

    public LiveData<List<LogData>> getAllLogs() {
        return allLogs;
    }
}
