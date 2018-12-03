package com.android.part_timer.controller;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import java.util.Date;
import java.util.List;

import static android.support.constraint.Constraints.TAG;
import static com.android.part_timer.controller.Home.appDatabase;

public class Stats extends Fragment {

    double mon_val = 0,tue_val = 0, wed_val = 0,
            thu_val = 0,fri_val = 0, sat_val = 0,
            sun_val = 0,jan_val=0,feb_val=0,mar_val=0,apr_val=0,
            may_val=0,jun_val=0,jul_val=0,aug_val=0,
            sep_val=0,oct_val=0,nov_val=0,dec_val=0;
    int month_num, day_num;
    LogData mon, tue, wed, thu, fri, sat, sun;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.activity_stats, container, false);

        Spinner drop_down;
        final ArrayAdapter<CharSequence> adapter;
        AsyncTask.execute(new Runnable() {
            double progress_val;
            double val ;
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
                val= mon_val+tue_val+wed_val+thu_val+fri_val+sat_val+sun_val;
                progress_val = ((double)val /20)*100;
                ProgressBar progress = (ProgressBar) v.findViewById(R.id.progress);
                progress.setProgress((int) progress_val);
                TextView progress_text = (TextView) v.findViewById(R.id.hrs);
                progress_text.setText((int)val + "");
            }
        });

        final BarChart barChart = v.findViewById(R.id.chart);
        drop_down = (Spinner) v.findViewById(R.id.drop_down);
        adapter = ArrayAdapter.createFromResource(getContext(), R.array.drop_down, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        drop_down.setAdapter(adapter);
        drop_down.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, final int i, long l) {
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        final List<LogData> logData = appDatabase.logDataDaoModel().getWeek();
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

                        switch (i)
                        {
                            case 0:
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

                                labels.add("Mon");
                                labels.add("Tue");
                                labels.add("Wed");
                                labels.add("Thu");
                                labels.add("Fri");
                                labels.add("Sat");
                                labels.add("Sun");

                                BarData weekly_data = new BarData(labels, bardataset);
                                weekly_data.notifyDataChanged(); // NOTIFIES THE DATA OBJECT
                                barChart.notifyDataSetChanged(); // let the chart know it's data changed
                                if (weekly_data!=null){
                                    barChart.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            barChart.invalidate();
                                        }
                                    });
                                } // refresh


                                barChart.setData(weekly_data);
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
                                data_monthly.notifyDataChanged(); // NOTIFIES THE DATA OBJECT
                                barChart.notifyDataSetChanged(); // let the chart know it's data changed
                                if (data_monthly!=null){
                                    barChart.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            barChart.invalidate();
                                        }
                                    });
                                } // refresh

                                barChart.setData(data_monthly);
                                barChart.getXAxis().setDrawGridLines(false);
                                barChart.getAxisLeft().setDrawGridLines(false);
                                barChart.getAxisRight().setDrawGridLines(false);
                                barChart.getLegend().setEnabled(false);
                                barChart.setDescription("");
                                barChart.setTouchEnabled(false);
                                //barChart.animateY(250);
                                break;
                        }
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        // LogData mon, tue, wed, thu, fri, sat, sun;
                        //int mon_val =0, tue_val=0, wed_val=0,thu_val=0,fri_val=0,sat_val=0,sun_val=0;
                        final List<LogData> logData = appDatabase.logDataDaoModel().getWeek();
                        for (int j = 0; j < logData.size(); j++) {
                            day_num = logData.get(j).getDayNum();
                            Log.v(TAG,"day num in nothing selected"+day_num);
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
                                    //wed = logData.get(j);
                                    wed_val = milliToHours(logData.get(j).getWeekly_diff());
                                    continue;
                                case 4:
                                    //thu = logData.get(j);
                                    thu_val = milliToHours(logData.get(j).getWeekly_diff());
                                    continue;
                                case 5:
                                    //fri = logData.get(j);
                                    fri_val = milliToHours(logData.get(j).getWeekly_diff());
                                    continue;
                                case 6:
                                    //sat = logData.get(j);
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
                        data.notifyDataChanged(); // NOTIFIES THE DATA OBJECT
                        barChart.setData(data);
                        barChart.notifyDataSetChanged(); // let the chart know it's data changed
                        if (data!=null){
                            barChart.post(new Runnable() {
                                @Override
                                public void run() {
                                    barChart.invalidate();
                                }
                            });
                        } // refresh
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



    public double dateDifference(Date startDate, Date endDate) {

        //milliseconds
        long difference = endDate.getTime() - startDate.getTime();
        Log.v(TAG, "diff" + difference);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;

        long hours = (long) ((double)difference / hoursInMilli);
        Log.v(TAG, "hours " + hours);
        difference = difference % hoursInMilli;

        double minutes = difference / minutesInMilli;
        minutes = minutes/60;

        double total = (int) hours + minutes;
        return total;

    }

    public double milliToHours(long milli) {

        double sec, min, hrs;
        sec = milli / 1000;
        min = sec / 60;
        sec = sec % 60;
        hrs = min / 60;
        Log.v(TAG, " hours " + hrs);
        return hrs;
    }

}

