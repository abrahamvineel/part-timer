package com.android.part_timer.controller.loglist;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import com.android.part_timer.R;
import com.android.part_timer.database.AppDatabase;
import com.android.part_timer.database.entity.GeneralData;
import com.android.part_timer.database.entity.LogData;

public class LogItemsListActivity extends AppCompatActivity {

    private LogViewModel logViewModel;
    public static AppDatabase appDatabase;
    private Boolean twentyFourHour=false;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_items_list);
        appDatabase = AppDatabase.getDatabaseInstance(this);

        //recycler view sets the log items in card view
        recyclerView = findViewById(R.id.log_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        //get the twenty four hour format use has selected and pass it to the activity to set the log lists
        AsyncTask.execute(new Runnable() {
            GeneralData generalData;
            @Override
            public void run() {
                generalData=appDatabase.generalDataDaoModel().getGeneralSettings();
                if(null!=generalData && generalData.getTwentyFourHour()){
                    twentyFourHour=true;
                }
                final LogListAdapter adapter = new LogListAdapter(LogItemsListActivity.this,LogItemsListActivity.this,twentyFourHour);
                recyclerView.setAdapter(adapter);
                logViewModel = ViewModelProviders.of(LogItemsListActivity.this).get(LogViewModel.class);
                //this view model observes for the data change in database
                logViewModel.getAllLogs().observe(LogItemsListActivity.this, new Observer<List<LogData>>() {
                    @Override
                    public void onChanged(@Nullable List<LogData> logs) {
                        adapter.setLogs(logs);
                    }
                });
            }
        });
    }

}
