package de.rub.selab22a15;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

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
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.rub.selab22a15.activities.SurveyActivity;
import de.rub.selab22a15.database.local.ActivityProcessed;
import de.rub.selab22a15.database.local.ActivityProcessedRepository;
import de.rub.selab22a15.database.local.SurveyProcessed;
import de.rub.selab22a15.database.local.SurveyProcessedRepository;
import de.rub.selab22a15.workers.DatabaseProcessingWorker;

public class HomeFragment extends Fragment {
    MaterialCardView cardViewSurvey;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DatabaseProcessingWorker.start(requireContext());
        FragmentActivity activity = requireActivity();

        cardViewSurvey = activity.findViewById(R.id.cardViewSurvey);
        MaterialButton buttonCardViewSurveyStart = activity.findViewById(R.id.buttonCardViewSurveyStart);
        buttonCardViewSurveyStart.setOnClickListener(v -> startSurvey());

        MaterialButton buttonCardViewSelectDate = activity.findViewById(R.id.buttonCardViewSelectDate);
        buttonCardViewSelectDate.setOnClickListener(v -> {
            MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
            builder.setTitleText(R.string.stringSelectDate);

            MaterialDatePicker<Pair<Long, Long>> datePicker = builder.build();
            datePicker.addOnPositiveButtonClickListener(selection -> statistics(selection.first, selection.second));
            datePicker.show(getParentFragmentManager(), null);
        });

        statistics(null,null);


    }


    private void startSurvey() {
        Intent surveyIntent = new Intent(requireActivity(), SurveyActivity.class);
        startActivity(surveyIntent);
    }

    /*
    Testkopie
    private void statistics(){
        CombinedChart combinedChart = requireActivity().findViewById(R.id.combinedChartBasicStatistics);

        combinedChart.setMinimumHeight(800);

        combinedChart.getDescription().setEnabled(false);
        combinedChart.setBackgroundColor(Color.WHITE);
        combinedChart.setDrawGridBackground(false);
        combinedChart.setDrawBarShadow(false);
        combinedChart.setHighlightFullBarEnabled(false);

        Legend l = combinedChart.getLegend();
        l.setWordWrapEnabled(true);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);

        YAxis yAxisLeft = combinedChart.getAxisLeft();
        yAxisLeft.setDrawGridLines(false);
        yAxisLeft.setAxisMinimum(0f);
        yAxisLeft.setGranularity(2f);

        YAxis yAxisRight = combinedChart.getAxisRight();
        yAxisRight.setDrawGridLines(false);
        yAxisRight.setAxisMinimum(0f);
        yAxisRight.setAxisMaximum(1f);
        yAxisRight.setGranularity(0.05f);

        XAxis xAxis = combinedChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(5f);

        new Thread(() -> {


            LineData lineData = new LineData();
            BarData barData = new BarData();
            CombinedData combinedData = new CombinedData();

            combinedData.setData(generateActivityDate(barData,null,null));
            combinedData.setData(generateMoodData(lineData,null,null));

            xAxis.setAxisMaximum(combinedData.getXMax() + 0f);


            xAxis.setAxisMaximum(combinedData.getXMax() + 0f);
            yAxisLeft.setAxisMaximum(barData.getYMax() + 10f);
            yAxisRight.setAxisMaximum(lineData.getYMax() + 0.25f);
            combinedChart.setScaleXEnabled(false);
            combinedChart.setScaleYEnabled(true);
            combinedChart.setData(combinedData);
            combinedChart.invalidate();

        }).start();
    }
*/
    private void statistics(Long start, Long end) {
        CombinedChart combinedChart = requireActivity().findViewById(R.id.combinedChartBasicStatistics);

        combinedChart.setMinimumHeight(800);

        combinedChart.getDescription().setEnabled(false);
        combinedChart.setBackgroundColor(Color.WHITE);
        combinedChart.setDrawGridBackground(false);
        combinedChart.setDrawBarShadow(false);
        combinedChart.setHighlightFullBarEnabled(false);

        Legend l = combinedChart.getLegend();
        l.setWordWrapEnabled(true);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);

        YAxis yAxisLeft = combinedChart.getAxisLeft();
        yAxisLeft.setDrawGridLines(false);
        yAxisLeft.setAxisMinimum(0f);
        yAxisLeft.setGranularity(2f);

        YAxis yAxisRight = combinedChart.getAxisRight();
        yAxisRight.setDrawGridLines(false);
        yAxisRight.setAxisMinimum(0f);
        yAxisRight.setAxisMaximum(1f);
        yAxisRight.setGranularity(0.05f);

        XAxis xAxis = combinedChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(5f);

        new Thread(() -> {
/*
            ActivityProcessedRepository repo = new ActivityProcessedRepository(requireActivity().getApplication());
            Log.d("statistics_log", String.valueOf(repo.getRangeUnsafe(start, end).size()));
*/

            LineData lineData = new LineData();
            BarData barData = new BarData();
            CombinedData combinedData = new CombinedData();

            combinedData.setData(generateActivityDate(barData, start, end));
            combinedData.setData(generateMoodData(lineData, start, end));
            if (start == null && end == null) {
                xAxis.setAxisMaximum(combinedData.getXMax() + 0f);
            } else {
                xAxis.setAxisMinimum(start);
                xAxis.setAxisMaximum(end);
            }
            xAxis.setAxisMaximum(combinedData.getXMax() + 0f);
            yAxisLeft.setAxisMaximum(barData.getYMax() + 10f);
            yAxisRight.setAxisMaximum(lineData.getYMax() + 0.25f);
            combinedChart.setScaleXEnabled(false);
            combinedChart.setScaleYEnabled(true);
            combinedChart.setData(combinedData);
            combinedChart.invalidate();

        }).start();
    }


    private LineData generateMoodData(LineData lineData, Long start, Long end) {

        List<Entry> lineEntries = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();

        if (start == null && end == null) {
            List<SurveyProcessed> surveyProcessedList = new SurveyProcessedRepository(
                    requireActivity().getApplication()).getAllUnsafe();

            for (SurveyProcessed surveyProcessed : surveyProcessedList) {
                calendar.setTimeInMillis(surveyProcessed.getTimestamp());
                lineEntries.add(new Entry(calendar.get(Calendar.DAY_OF_MONTH),
                        surveyProcessed.getRating()));
            }

        } else {

            List<SurveyProcessed> surveyProcessedList = new SurveyProcessedRepository(
                    requireActivity().getApplication()).getRangeUnsafe(start, end);

            for (SurveyProcessed surveyProcessed : surveyProcessedList) {
                calendar.setTimeInMillis(surveyProcessed.getTimestamp());
                lineEntries.add(new Entry(calendar.get(Calendar.DAY_OF_MONTH),
                        surveyProcessed.getRating()));
            }

        }

        LineDataSet lineDataSet = new LineDataSet(lineEntries, "average mood (-)");
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

        lineData.addDataSet(lineDataSet);

        return lineData;
    }

    private BarData generateActivityDate(BarData barData, Long start, Long end) {

        List<BarEntry> barEntries = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();

        if (start == null && end == null) {
            List<ActivityProcessed> activityProcessedList = new ActivityProcessedRepository(
                    requireActivity().getApplication()).getAllUnsafe();

            for (ActivityProcessed activityProcessed : activityProcessedList) {
                calendar.setTimeInMillis(activityProcessed.getTimestamp());
                barEntries.add(new BarEntry(calendar.get(Calendar.DAY_OF_MONTH),
                        activityProcessed.getWeight()));
            }

        } else {
            List<ActivityProcessed> activityProcessedList = new ActivityProcessedRepository(
                    requireActivity().getApplication()).getRangeUnsafe(start, end);

            for (ActivityProcessed activityProcessed : activityProcessedList) {
                calendar.setTimeInMillis(activityProcessed.getTimestamp());
                barEntries.add(new BarEntry(calendar.get(Calendar.DAY_OF_MONTH),
                        activityProcessed.getWeight()));
            }
        }

        BarDataSet barDataSet = new BarDataSet(barEntries, "weigh acceleration activity (in m/s)");
        barDataSet.setColor(Color.rgb(0, 53, 96));
        barDataSet.setDrawValues(false);
        barDataSet.setValueTextColor(Color.rgb(0, 53, 96));
        barDataSet.setValueTextSize(12f);
        barDataSet.setValueTextColor(Color.rgb(0, 53, 96));
        barDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);

        barData.addDataSet(barDataSet);

        return barData;

    }

}