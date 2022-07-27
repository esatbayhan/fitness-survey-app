package de.rub.selab22a15.helpers;

import android.graphics.Color;
import android.util.Log;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

import de.rub.selab22a15.App;
import de.rub.selab22a15.database.local.ActivityProcessed;
import de.rub.selab22a15.database.local.ActivityProcessedRepository;
import de.rub.selab22a15.database.local.SurveyProcessed;
import de.rub.selab22a15.database.local.SurveyProcessedRepository;

public class Statistics {
    private final CombinedChart chart;

    public Statistics(CombinedChart chart) {
        this.chart = chart;
        initialize();
    }

    public void draw() {
        draw(0L, Long.MAX_VALUE);
    }

    public void draw(long start, long end) {
        new Thread(() -> {
            CombinedData combinedData = new CombinedData();
            LineData activityData = getActivityData(start, end);
            LineData surveyData = getSurveyData(start, end);
            combinedData.setData(surveyData);
            combinedData.setData(activityData);

            chart.getXAxis().setAxisMinimum(combinedData.getXMin());
            chart.getXAxis().setAxisMaximum(combinedData.getXMax());

            chart.getAxisLeft().setAxisMaximum(activityData.getYMax());
            chart.getAxisRight().setAxisMaximum(surveyData.getYMax());

            chart.setData(combinedData);
            chart.invalidate();
        }).start();
    }

    private void initialize() {
        chart.setMinimumHeight(800);
        chart.getDescription().setEnabled(false);
        chart.setBackgroundColor(Color.WHITE);
        chart.setDrawGridBackground(false);
        chart.setDrawBarShadow(false);
        chart.setHighlightFullBarEnabled(false);
        chart.setScaleXEnabled(false);
        chart.setScaleYEnabled(true);

        Legend legend = chart.getLegend();
        legend.setWordWrapEnabled(true);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);

        YAxis yAxisLeft = chart.getAxisLeft();
        yAxisLeft.setDrawGridLines(false);
        yAxisLeft.setAxisMinimum(0f);
        yAxisLeft.setGranularity(2f);

        YAxis yAxisRight = chart.getAxisRight();
        yAxisRight.setDrawGridLines(false);
        yAxisRight.setAxisMinimum(0f);
        yAxisRight.setAxisMaximum(1f);
        yAxisRight.setGranularity(0.05f);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(5f);
    }

    private LineData getActivityData(long start, long end) {
        return new LineData(getActivityDataset(start, end));
    }

    private LineDataSet getActivityDataset(long start, long end) {
        List<Entry> entries = new ArrayList<>();
        List<ActivityProcessed> activities = new ActivityProcessedRepository(App.getInstance())
                .getRangeUnsafe(start, end);
        LineDataSet set;

        Log.d("stat_log", activities.size() + "");

        for (ActivityProcessed activity : activities) {
            entries.add(new Entry(activity.getTimestamp(), activity.getWeight()));
        }

        set = new LineDataSet(entries, "Activity");

//        set.setColor(Color.rgb(0, 53, 96));
//        set.setDrawValues(false);
//        set.setValueTextColor(Color.rgb(0, 53, 96));
//        set.setValueTextSize(12f);
//        set.setValueTextColor(Color.rgb(0, 53, 96));
//        set.setAxisDependency(YAxis.AxisDependency.LEFT);

        return set;
    }

    private LineData getSurveyData(long start, long end) {
        return new LineData(getSurveyDataSet(start, end));
    }

    private LineDataSet getSurveyDataSet(long start, long end) {
        List<Entry> entries = new ArrayList<>();
        List<SurveyProcessed> surveys = new SurveyProcessedRepository(App.getInstance())
                .getRangeUnsafe(start, end);
        LineDataSet lineDataSet;

        for (SurveyProcessed survey : surveys) {
            entries.add(new Entry(survey.getTimestamp(), survey.getRating()));
        }

        lineDataSet = new LineDataSet(entries, "Mood");

        lineDataSet.setHighlightEnabled(false);
        lineDataSet.setColor(Color.rgb(238, 114, 3));
        lineDataSet.setLineWidth(2.5f);
        lineDataSet.setCircleColor(Color.rgb(238, 114, 3));
        lineDataSet.setCircleRadius(5f);
        lineDataSet.setFillColor(Color.rgb(238, 114, 3));
        lineDataSet.setCircleHoleColor(Color.rgb(238, 114, 3));

        lineDataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        lineDataSet.setDrawValues(false);
        lineDataSet.setValueTextColor(Color.rgb(238, 114, 3));
        lineDataSet.setValueTextSize(12f);

        lineDataSet.setAxisDependency(YAxis.AxisDependency.RIGHT);

        return lineDataSet;
    }
}
