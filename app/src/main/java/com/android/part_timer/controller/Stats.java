package com.android.part_timer.controller;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.part_timer.R;
import com.android.part_timer.database.entity.LogData;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;

import static com.android.part_timer.controller.Home.appDatabase;

public class Stats extends Fragment {

    //Variables to display the values in barchart are initialized
    double mon_val = 0,tue_val = 0, wed_val = 0,
            thu_val = 0,fri_val = 0, sat_val = 0,
            sun_val = 0,jan_val=0,feb_val=0,mar_val=0,apr_val=0,
            may_val=0,jun_val=0,jul_val=0,aug_val=0,
            sep_val=0,oct_val=0,nov_val=0,dec_val=0;
    int month_num, day_num;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.activity_stats, container, false);
        //drop_down spinner to select between week and month
        Spinner drop_down;
        final ArrayAdapter<CharSequence> adapter;
        AsyncTask.execute(new Runnable() {
            double progress_val;
            double val ;
            @Override
            public void run() {
                //Retrieving list of weekly data form the database
                final List<LogData> logData = appDatabase.logDataDaoModel().getWeek();
                for (int j = 0; j < logData.size(); j++) {
                    //Accessing the list data per day number of assigning the difference in checkout and checkin time
                    // to the respective values in the day
                    day_num = logData.get(j).getDayNum();
                    switch (day_num) {
                        case 0:
                            sun_val = milliToHours(logData.get(j).getWeekly_diff());
                            continue;
                        case 1:
                            mon_val = milliToHours(logData.get(j).getWeekly_diff());
                            continue;
                        case 2:
                            tue_val = milliToHours(logData.get(j).getWeekly_diff());
                            continue;
                        case 3:
                            wed_val = milliToHours(logData.get(j).getWeekly_diff());
                            continue;
                        case 4:
                            thu_val = milliToHours(logData.get(j).getWeekly_diff());
                            continue;
                        case 5:
                            fri_val = milliToHours(logData.get(j).getWeekly_diff());
                            continue;
                        case 6:
                            sat_val = milliToHours(logData.get(j).getWeekly_diff());
                            continue;
                    }
                }
                //Calculating the sum of values to set as the progress value
                val= mon_val+tue_val+wed_val+thu_val+fri_val+sat_val+sun_val;
                progress_val = (val /20)*100;
                ProgressBar progress = v.findViewById(R.id.progress);
                progress.setProgress((int) progress_val);
                TextView progress_text = v.findViewById(R.id.hrs);
                //If the value is int we set the integral part or else we will set the float value
                if ((int) val == val) {
                    progress_text.setText((int) val + "");
                } else {
                    progress_text.setText(String.format("%.2f",val));
                }
            }
        });

        final BarChart barChart = v.findViewById(R.id.chart);
        drop_down = v.findViewById(R.id.drop_down);
        adapter = ArrayAdapter.createFromResource(getContext(), R.array.drop_down, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        drop_down.setAdapter(adapter);
        drop_down.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, final int i, long l) {
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        //Retrieving list of weekly data form the database
                        final List<LogData> logData = appDatabase.logDataDaoModel().getWeek();
                        //Retrieving list of monthly data form the database
                        List<LogData> monthLogdata = appDatabase.logDataDaoModel().getMonthly();
                        for (int j = 0; j < logData.size(); j++) {
                            day_num = logData.get(j).getDayNum();
                            switch (day_num) {
                                case 0:
                                    sun_val = milliToHours(logData.get(j).getWeekly_diff());
                                    continue;
                                case 1:
                                    mon_val = milliToHours(logData.get(j).getWeekly_diff());
                                    continue;
                                case 2:
                                    tue_val = milliToHours(logData.get(j).getWeekly_diff());
                                    continue;
                                case 3:
                                    wed_val = milliToHours(logData.get(j).getWeekly_diff());
                                    continue;
                                case 4:
                                    thu_val = milliToHours(logData.get(j).getWeekly_diff());
                                    continue;
                                case 5:
                                    fri_val = milliToHours(logData.get(j).getWeekly_diff());
                                    continue;
                                case 6:
                                    sat_val = milliToHours(logData.get(j).getWeekly_diff());
                                    continue;
                            }
                        }

                        //Based on the drop down selected it goes into the respective case and displays the value for either week or month
                        switch (i)
                        {
                            case 0:
                                //Adding the values to respective indices
                                ArrayList<BarEntry> entries = new ArrayList<>();
                                entries.add(new BarEntry((float) mon_val, 0));
                                entries.add(new BarEntry((float) tue_val, 1));
                                entries.add(new BarEntry((float) wed_val, 2));
                                entries.add(new BarEntry((float) thu_val, 3));
                                entries.add(new BarEntry((float) fri_val, 4));
                                entries.add(new BarEntry((float) sat_val, 5));
                                entries.add(new BarEntry((float) sun_val, 6));

                                BarDataSet bardataset = new BarDataSet(entries, "Cells");
                                bardataset.setColor(Color.BLUE);

                                ArrayList<String> labels = new ArrayList<>();

                                //Labels to denote day of the week
                                labels.add("Mon");
                                labels.add("Tue");
                                labels.add("Wed");
                                labels.add("Thu");
                                labels.add("Fri");
                                labels.add("Sat");
                                labels.add("Sun");

                                BarData weekly_data = new BarData(labels, bardataset);
                                weekly_data.notifyDataChanged();
                                barChart.notifyDataSetChanged();
                                if (weekly_data!=null){
                                    barChart.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            barChart.invalidate();
                                        }
                                    });
                                }

                                barChart.setData(weekly_data);
                                //By default the grid lines are shown in the background
                                //It was disabled by the following function by setting it to false
                                barChart.getXAxis().setDrawGridLines(false);
                                barChart.getAxisLeft().setDrawGridLines(false);
                                barChart.getAxisRight().setDrawGridLines(false);
                                barChart.getLegend().setEnabled(false);
                                barChart.setDescription("");
                                barChart.setTouchEnabled(false);
                                break;

                            case 1:

                                for(int i =0;i<monthLogdata.size();i++) {
                                    month_num = monthLogdata.get(i).getMonth();
                                    switch (month_num){
                                        case 1 : jan_val = milliToHours(monthLogdata.get(i).getMonthly_total());
                                        case 2 : feb_val = milliToHours(monthLogdata.get(i).getMonthly_total());
                                        case 3 : mar_val = milliToHours(monthLogdata.get(i).getMonthly_total());
                                        case 4 : apr_val = milliToHours(monthLogdata.get(i).getMonthly_total());
                                        case 5 : may_val = milliToHours(monthLogdata.get(i).getMonthly_total());
                                        case 6 : jun_val = milliToHours(monthLogdata.get(i).getMonthly_total());
                                        case 7 : jul_val = milliToHours(monthLogdata.get(i).getMonthly_total());
                                        case 8 : aug_val = milliToHours(monthLogdata.get(i).getMonthly_total());
                                        case 9 : sep_val = milliToHours(monthLogdata.get(i).getMonthly_total());
                                        case 10 : oct_val = milliToHours(monthLogdata.get(i).getMonthly_total());
                                        case 11 : nov_val = milliToHours(monthLogdata.get(i).getMonthly_total());
                                        case 12 : dec_val = milliToHours(monthLogdata.get(i).getMonthly_total());
                                    }
                                }
                                ArrayList<BarEntry> entries_monthly = new ArrayList<>();
                                entries_monthly.add(new BarEntry((float) jan_val, 0));
                                entries_monthly.add(new BarEntry((float) feb_val, 1));
                                entries_monthly.add(new BarEntry((float) mar_val, 2));
                                entries_monthly.add(new BarEntry((float) apr_val, 3));
                                entries_monthly.add(new BarEntry((float) may_val, 4));
                                entries_monthly.add(new BarEntry((float) jun_val, 5));
                                entries_monthly.add(new BarEntry((float) jul_val, 6));
                                entries_monthly.add(new BarEntry((float) aug_val, 7));
                                entries_monthly.add(new BarEntry((float) sep_val, 8));
                                entries_monthly.add(new BarEntry((float) oct_val, 9));
                                entries_monthly.add(new BarEntry((float) nov_val, 10));
                                entries_monthly.add(new BarEntry((float) dec_val, 11));


                                BarDataSet bardataset_monthly = new BarDataSet(entries_monthly, "Cells");
                                bardataset_monthly.setColor(Color.BLUE);

                                ArrayList<String> labels_monthly = new ArrayList<>();
                                //Labels to show month
                                labels_monthly.add("Jan");
                                labels_monthly.add("Feb");
                                labels_monthly.add("Mar");
                                labels_monthly.add("Apr");
                                labels_monthly.add("May");
                                labels_monthly.add("Jun");
                                labels_monthly.add("Jul");
                                labels_monthly.add("Aug");
                                labels_monthly.add("Sep");
                                labels_monthly.add("Oct");
                                labels_monthly.add("Nov");
                                labels_monthly.add("Dec");

                                BarData data_monthly = new BarData(labels_monthly, bardataset_monthly);
                                data_monthly.notifyDataChanged();
                                barChart.notifyDataSetChanged();
                                if (data_monthly!=null){
                                    barChart.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            barChart.invalidate();
                                        }
                                    });
                                }

                                barChart.setData(data_monthly);
                                barChart.getXAxis().setDrawGridLines(false);
                                barChart.getAxisLeft().setDrawGridLines(false);
                                barChart.getAxisRight().setDrawGridLines(false);
                                barChart.getLegend().setEnabled(false);
                                barChart.setDescription("");
                                barChart.setTouchEnabled(false);
                                break;
                        }
                    }
                });
            }

            //To display the barchart when no option is selected
            //When no option in the drop_down is selected weekly_data is shown by default
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        final List<LogData> logData = appDatabase.logDataDaoModel().getWeek();
                        for (int j = 0; j < logData.size(); j++) {
                            day_num = logData.get(j).getDayNum();
                            switch (day_num) {
                                case 0:
                                    sun_val = milliToHours(logData.get(j).getWeekly_diff());
                                    continue;
                                case 1:
                                    mon_val = milliToHours(logData.get(j).getWeekly_diff());
                                    continue;
                                case 2:
                                    tue_val = milliToHours(logData.get(j).getWeekly_diff());
                                    continue;
                                case 3:
                                    wed_val = milliToHours(logData.get(j).getWeekly_diff());
                                    continue;
                                case 4:
                                    thu_val = milliToHours(logData.get(j).getWeekly_diff());
                                    continue;
                                case 5:
                                    fri_val = milliToHours(logData.get(j).getWeekly_diff());
                                    continue;
                                case 6:
                                    sat_val = milliToHours(logData.get(j).getWeekly_diff());
                                    continue;
                            }
                        }

                        ArrayList<BarEntry> entries = new ArrayList<>();
                        entries.add(new BarEntry((float) mon_val, 0));
                        entries.add(new BarEntry((float) tue_val, 1));
                        entries.add(new BarEntry((float) wed_val, 2));
                        entries.add(new BarEntry((float) thu_val, 3));
                        entries.add(new BarEntry((float) fri_val, 4));
                        entries.add(new BarEntry((float) sat_val, 5));
                        entries.add(new BarEntry((float) sun_val, 6));

                        BarDataSet bardataset = new BarDataSet(entries, "Cells");
                        ArrayList<String> labels = new ArrayList<>();

                        labels.add("Mon");
                        labels.add("Tue");
                        labels.add("Wed");
                        labels.add("Thu");
                        labels.add("Fri");
                        labels.add("Sat");
                        labels.add("Sun");

                        BarData data = new BarData(labels, bardataset);
                        data.notifyDataChanged();
                        barChart.setData(data);
                        barChart.notifyDataSetChanged();
                        if (data!=null){
                            barChart.post(new Runnable() {
                                @Override
                                public void run() {
                                    barChart.invalidate();
                                }
                            });
                        }
                        barChart.getXAxis().setDrawGridLines(false);
                        barChart.getAxisLeft().setDrawGridLines(false);
                        barChart.getAxisRight().setDrawGridLines(false);
                        barChart.getLegend().setEnabled(false);
                        barChart.setDescription("");
                        barChart.setTouchEnabled(false);

                    }
                });

            }
        });
        return v;
    }

    //function to convert milliseconds to hours
    public double milliToHours(long milli) {

        double sec, min, hrs;
        sec = milli / 1000;
        min = sec / 60;
        sec = sec % 60;
        hrs = min / 60;
        return hrs;
    }
}

