package com.android.part_timer.controller;

import android.graphics.Color;
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
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

public class Stats extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.activity_stats, container, false);

        int progress_val = 90, val = 18;
        Spinner drop_down;
        ArrayAdapter<CharSequence> adapter;


        ProgressBar progress = (ProgressBar) v.findViewById(R.id.progress);
        progress.setProgress(progress_val);

        TextView progress_text = (TextView) v.findViewById(R.id.hrs);
        progress_text.setText(val+"");

        final BarChart barChart = v.findViewById(R.id.chart);

        drop_down = (Spinner) v.findViewById(R.id.drop_down);
        adapter = ArrayAdapter.createFromResource(getContext(),R.array.drop_down,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        drop_down.setAdapter(adapter);
        drop_down.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                switch (i)
                {
                    case 0: ArrayList<BarEntry> entries = new ArrayList<>();
                        entries.add(new BarEntry(8f,0));
                        entries.add(new BarEntry(16f,1));
                        entries.add(new BarEntry(18f,2));
                        entries.add(new BarEntry(19f,3));
                        entries.add(new BarEntry(10f,4));
                        entries.add(new BarEntry(7f,5));
                        entries.add(new BarEntry(2f,6));

                        BarDataSet bardataset = new BarDataSet(entries,"Cells");
                        bardataset.setColor(Color.BLUE);

                        ArrayList<String> labels = new ArrayList<>();
                        labels.add("Sun");
                        labels.add("Mon");
                        labels.add("Tue");
                        labels.add("Wed");
                        labels.add("Thu");
                        labels.add("Fri");
                        labels.add("Sat");

                        BarData weekly_data = new BarData(labels, bardataset );

                        barChart.setData(weekly_data);
                        barChart.getXAxis().setDrawGridLines(false);
                        barChart.getAxisLeft().setDrawGridLines(false);
                        barChart.getAxisRight().setDrawGridLines(false);
                        barChart.getLegend().setEnabled(false);
                        barChart.setDescription("");
                        barChart.setTouchEnabled(false);


                        barChart.animateY(250);
                        break;

                    case 1: ArrayList<BarEntry> entries_monthly = new ArrayList<>();
                        entries_monthly.add(new BarEntry(70f,0));
                        entries_monthly.add(new BarEntry(61f,1));
                        entries_monthly.add(new BarEntry(52f,2));
                        entries_monthly.add(new BarEntry(43f,3));
                        entries_monthly.add(new BarEntry(34f,4));
                        entries_monthly.add(new BarEntry(35f,5));
                        entries_monthly.add(new BarEntry(50f,6));
                        entries_monthly.add(new BarEntry(55f,7));
                        entries_monthly.add(new BarEntry(75f,8));
                        entries_monthly.add(new BarEntry(55f,9));
                        entries_monthly.add(new BarEntry(40f,10));
                        entries_monthly.add(new BarEntry(0f,11));


                        BarDataSet bardataset_monthly = new BarDataSet(entries_monthly,"Cells");
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

                        BarData data_monthly = new BarData(labels_monthly, bardataset_monthly );
                        barChart.setData(data_monthly);
                        barChart.getXAxis().setDrawGridLines(false);
                        barChart.getAxisLeft().setDrawGridLines(false);
                        barChart.getAxisRight().setDrawGridLines(false);
                        barChart.getLegend().setEnabled(false);
                        barChart.setDescription("");
                        barChart.setTouchEnabled(false);


                        barChart.animateY(250);
                        break;
                }


            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

                ArrayList<BarEntry> entries = new ArrayList<>();
                entries.add(new BarEntry(8f,0));
                entries.add(new BarEntry(16f,1));
                entries.add(new BarEntry(18f,2));
                entries.add(new BarEntry(19f,3));
                entries.add(new BarEntry(10f,4));
                entries.add(new BarEntry(7f,5));
                entries.add(new BarEntry(2f,6));

                BarDataSet bardataset = new BarDataSet(entries,"Cells");


                ArrayList<String> labels = new ArrayList<>();
                labels.add("Sun");
                labels.add("Mon");
                labels.add("Tue");
                labels.add("Wed");
                labels.add("Thu");
                labels.add("Fri");
                labels.add("Sat");

                BarData data = new BarData(labels, bardataset );
                barChart.setData(data);

                barChart.getXAxis().setDrawGridLines(false);
                barChart.getAxisLeft().setDrawGridLines(false);
                barChart.getAxisRight().setDrawGridLines(false);
                barChart.getLegend().setEnabled(false);
                barChart.setDescription("");
                barChart.setTouchEnabled(false);

                barChart.animateY(250);
            }
        });

        return v;


    }
}

