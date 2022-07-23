package de.rub.selab22a15;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
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
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

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
    Calendar calendar = Calendar.getInstance();


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

        statistics();
    }

    private void startSurvey() {
        Intent surveyIntent = new Intent(requireActivity(), SurveyActivity.class);
        startActivity(surveyIntent);
    }

    private void statistics() {
        CombinedChart combinedChart = requireActivity().findViewById(R.id.combinedChartBasicStatistics);

        combinedChart.setMinimumHeight(720);

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

        YAxis yAxis = combinedChart.getAxisLeft();
        yAxis.setDrawGridLines(false);
        yAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        XAxis xAxis = combinedChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum(0f);
        xAxis.setGranularity(1f);
        xAxis.setAxisMaximum(31f);

/*
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return months[(int) value % months.length];
            }
        });


*/


        new Thread(() -> {
            CombinedData combinedData = new CombinedData();

            combinedData.setData(generateMoodData());
            combinedData.setData(generateActivityDate());

            xAxis.setAxisMaximum(combinedData.getXMax() + 0.25f);

            combinedChart.setData(combinedData);
            combinedChart.invalidate();

        }).start();
    }

    private LineData generateMoodData(){
        LineData lineData = new LineData();

        List<SurveyProcessed> surveyProcessedList = new SurveyProcessedRepository(
                requireActivity().getApplication()).getAll();

        List<Entry> lineEntries = new ArrayList<>();


        for (SurveyProcessed surveyProcessed : surveyProcessedList) {
            calendar.setTimeInMillis(surveyProcessed.getTimestamp());
            lineEntries.add(new Entry(calendar.get(Calendar.SECOND),
                    surveyProcessed.getRating()));
        }

        LineDataSet lineDataSet = new LineDataSet(lineEntries, "average mood (in %)");
        lineDataSet.setHighlightEnabled(false);
        lineDataSet.setColor(Color.rgb(240, 238, 70));
        lineDataSet.setLineWidth(2.5f);
        lineDataSet.setCircleColor(Color.rgb(240, 238, 70));
        lineDataSet.setCircleRadius(5f);
        lineDataSet.setFillColor(Color.rgb(240, 238, 70));
        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        lineDataSet.setDrawValues(true);
        lineDataSet.setValueTextSize(10f);
        lineDataSet.setValueTextColor(Color.rgb(240, 238, 70));

        lineDataSet.setAxisDependency(YAxis.AxisDependency.RIGHT);

        lineData.addDataSet(lineDataSet);

        return lineData;

    }
    private BarData generateActivityDate(){

        List<ActivityProcessed> activityProcessedList = new ActivityProcessedRepository(
                requireActivity().getApplication()).getAll();


        List<BarEntry> barEntries = new ArrayList<>();

        for (ActivityProcessed activityProcessed : activityProcessedList) {
            calendar.setTimeInMillis(activityProcessed.getTimestamp());
            barEntries.add(new BarEntry(calendar.get(Calendar.SECOND),
                    activityProcessed.getWeight()));
        }

        BarData barData=new BarData();

        BarDataSet barDataSet = new BarDataSet(barEntries, "weigh acceleration activity (in m/s)");
        barDataSet.setColor(Color.rgb(60, 220, 78));
        barDataSet.setValueTextColor(Color.rgb(60, 220, 78));
        barDataSet.setValueTextSize(10f);
        barDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);

        barData.addDataSet(barDataSet);

        return barData;
    }

}