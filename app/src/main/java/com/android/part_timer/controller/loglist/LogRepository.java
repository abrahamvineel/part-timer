package com.android.part_timer.controller.loglist;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

import com.android.part_timer.database.AppDatabase;
import com.android.part_timer.database.dao.LogDataDao;
import com.android.part_timer.database.entity.LogData;

public class LogRepository {
    private LogDataDao logDataDao;
    private LiveData<List<LogData>> allLogs;

    public LogRepository(Application application) {
        AppDatabase database = AppDatabase.getDatabaseInstance(application);
        logDataDao = database.logDataDaoModel();
        allLogs = logDataDao.getAllLogs();
    }

    public void insert(LogData logData) {
        new InsertLogAsyncTask(logDataDao).execute(logData);
    }

    public void update(LogData logData) {
        new UpdateLogAsyncTask(logDataDao).execute(logData);
    }

    public void delete(LogData logData) {
        new DeleteLogAsyncTask(logDataDao).execute(logData);
    }

    public void deleteAllNotes() {
        new DeleteAllLogsAsyncTask(logDataDao).execute();
    }

    public LiveData<List<LogData>> getAllNotes() {
        return allLogs;
    }

    private static class InsertLogAsyncTask extends AsyncTask<LogData, Void, Void> {
        private LogDataDao logDataDao;

        private InsertLogAsyncTask(LogDataDao logDataDao) {
            this.logDataDao = logDataDao;
        }

        @Override
        protected Void doInBackground(LogData... logs) {
            logDataDao.insert(logs[0]);
            return null;
        }
    }

    private static class UpdateLogAsyncTask extends AsyncTask<LogData, Void, Void> {
        private LogDataDao logDataDao;

        private UpdateLogAsyncTask(LogDataDao logDataDao) {
            this.logDataDao = logDataDao;
        }

        @Override
        protected Void doInBackground(LogData... logs) {
            logDataDao.update(logs[0]);
            return null;
        }
    }

    private static class DeleteLogAsyncTask extends AsyncTask<LogData, Void, Void> {
        private LogDataDao logDataDao;

        private DeleteLogAsyncTask(LogDataDao logDataDao) {
            this.logDataDao = logDataDao;
        }

        @Override
        protected Void doInBackground(LogData... logs) {
            logDataDao.deleteLocation(logs[0]);
            return null;
        }
    }

    private static class DeleteAllLogsAsyncTask extends AsyncTask<Void, Void, Void> {
        private LogDataDao logDataDao;

        private DeleteAllLogsAsyncTask(LogDataDao logDataDao) {
            this.logDataDao = logDataDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            logDataDao.deleteAllLocations();
            return null;
        }
    }
}
