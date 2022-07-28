package de.rub.selab22a15.helpers;

import android.graphics.Color;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;

import de.rub.selab22a15.App;
import de.rub.selab22a15.database.local.ActivityProcessed;
import de.rub.selab22a15.database.local.ActivityProcessedRepository;
import de.rub.selab22a15.database.local.SurveyProcessed;
import de.rub.selab22a15.database.local.SurveyProcessedRepository;

public class Statistics {
    private final LineChart chart;

    public Statistics(LineChart chart) {
        this.chart = chart;
        initialize();
    }

    public void draw() {
        draw(0L, Long.MAX_VALUE);
    }

    public void draw(long start, long end) {
        new Thread(() -> {
            LineDataSet activityDataSet = getActivityDataSet(start, end);
            LineDataSet surveyDataSet = getSurveyDataSet(start, end);

            if (activityDataSet.getEntryCount() == 0 && surveyDataSet.getEntryCount() == 0) {
                Reset();
                return;
            }

            ArrayList<ILineDataSet> dataSets = new ArrayList<>();

            dataSets.add(activityDataSet);
            dataSets.add(surveyDataSet);
            LineData lineData = new LineData(dataSets);

            chart.getXAxis().setAxisMinimum(lineData.getXMin());
            chart.getXAxis().setAxisMaximum(lineData.getXMax());

            chart.getAxisLeft().setAxisMaximum(activityDataSet.getYMax());
            chart.getAxisRight().setAxisMaximum(1f);

            chart.setData(lineData);
            chart.invalidate();
        }).start();
    }

    private void initialize() {
        chart.setMinimumHeight(800);
        chart.getDescription().setEnabled(false);
        chart.setTouchEnabled(true);
        chart.setDragEnabled(true);
        chart.setScaleEnabled(false);
        chart.setScaleXEnabled(false);
        chart.setScaleYEnabled(true);
        chart.setPinchZoom(false);
        chart.setBackgroundColor(Color.WHITE);
        chart.setDrawGridBackground(false);

        Legend legend = chart.getLegend();
        legend.setEnabled(true);
        legend.setWordWrapEnabled(true);
        legend.setDrawInside(false);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);

        YAxis yAxisLeft = chart.getAxisLeft();
        yAxisLeft.setDrawLabels(true);
        yAxisLeft.setDrawGridLines(false);
        yAxisLeft.setTextSize(12f);
        yAxisLeft.setAxisMinimum(0f);
        yAxisLeft.setGranularity(5f);

        YAxis yAxisRight = chart.getAxisRight();
        yAxisRight.setDrawLabels(true);
        yAxisRight.setDrawGridLines(false);
        yAxisRight.setTextSize(12f);

        yAxisRight.setAxisMinimum(0f);
        yAxisRight.setAxisMaximum(1f);
        yAxisRight.setGranularity(0.05f);

        XAxis xAxis = chart.getXAxis();
        xAxis.setDrawLabels(false);
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(5f);
    }

    private void Reset() {
        chart.clear();
    }

    private LineDataSet getActivityDataSet(long start, long end) {
        List<Entry> entries = new ArrayList<>();
        List<ActivityProcessed> activities = new ActivityProcessedRepository(App.getInstance())
                .getRangeUnsafe(start, end);

        LineDataSet lineDataSetActivity;

        for (ActivityProcessed activity : activities) {
            entries.add(new Entry(activity.getTimestamp(), activity.getWeight()));
        }

        lineDataSetActivity = new LineDataSet(entries, "Activity");

        lineDataSetActivity.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        lineDataSetActivity.setCubicIntensity(0.2f);

        lineDataSetActivity.setHighlightEnabled(false);

        lineDataSetActivity.setColor(Color.rgb(0, 53, 96));

        lineDataSetActivity.setLineWidth(2.5f);

        lineDataSetActivity.setDrawCircles(true);
        lineDataSetActivity.setCircleColor(Color.rgb(0, 53, 96));

        lineDataSetActivity.setDrawCircleHole(false);

        lineDataSetActivity.setDrawFilled(true);
        lineDataSetActivity.setFillColor(Color.rgb(0, 53, 96));
        lineDataSetActivity.setFillAlpha(50);

        lineDataSetActivity.setDrawValues(false);
        lineDataSetActivity.setValueTextColor(Color.rgb(0, 53, 96));
        lineDataSetActivity.setValueTextSize(12f);

        lineDataSetActivity.setAxisDependency(YAxis.AxisDependency.LEFT);

        return lineDataSetActivity;
    }

    private LineDataSet getSurveyDataSet(long start, long end) {
        List<Entry> entries = new ArrayList<>();
        List<SurveyProcessed> surveys = new SurveyProcessedRepository(App.getInstance())
                .getRangeUnsafe(start, end);
        LineDataSet lineDataSetSurvey;

        for (SurveyProcessed survey : surveys) {
            entries.add(new Entry(survey.getTimestamp(), survey.getRating()));
        }

        lineDataSetSurvey = new LineDataSet(entries, "Mood");

        lineDataSetSurvey.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        lineDataSetSurvey.setCubicIntensity(0.2f);

        lineDataSetSurvey.setHighlightEnabled(false);

        lineDataSetSurvey.setColor(Color.rgb(238, 114, 3));

        lineDataSetSurvey.setLineWidth(2.5f);

        lineDataSetSurvey.setDrawCircles(true);
        lineDataSetSurvey.setCircleColor(Color.rgb(238, 114, 3));

        lineDataSetSurvey.setDrawCircleHole(false);

        lineDataSetSurvey.setDrawFilled(false);

        lineDataSetSurvey.setDrawValues(false);
        lineDataSetSurvey.setValueTextColor(Color.rgb(238, 114, 3));
        lineDataSetSurvey.setValueTextSize(12f);

        lineDataSetSurvey.setAxisDependency(YAxis.AxisDependency.RIGHT);

        return lineDataSetSurvey;

    }
}