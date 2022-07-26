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
import com.google.android.material.slider.Slider;

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
    private Slider sliderScaleChart;
    MaterialCardView cardViewSurvey;
    int scaleSettingChart;


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


        FragmentActivity activity = requireActivity();

        sliderScaleChart = activity.findViewById(R.id.userStatisticsSlider);
        sliderScaleChart.addOnSliderTouchListener(touchListener);
        scaleSettingChart = (int) sliderScaleChart.getValue();


        sliderScaleChart.addOnChangeListener(new userStatisticsSliderOnChangeListener());
        sliderScaleChart.setTrackActiveTintList(sliderScaleChart.getTrackInactiveTintList());


        CombinedChart combinedChart = requireActivity().findViewById(R.id.combinedChartBasicStatistics);
        combinedChart.setMinimumHeight(900);

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
        yAxisLeft.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        yAxisLeft.setGranularity(2f);

        YAxis yAxisRight = combinedChart.getAxisRight();
        yAxisRight.setDrawGridLines(false);
        yAxisRight.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        yAxisRight.setAxisMaximum(1f);
        yAxisRight.setGranularity(0.05f);

        XAxis xAxis = combinedChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinimum(1f);
        xAxis.setGranularity(2f);
        ///xAxis.setAxisMaximum(31f);


        new Thread(() -> {
            /// @Override
            ///public void run() {

            LineData lineData = new LineData();
            BarData barData = new BarData();


            List<SurveyProcessed> surveyProcessedList = new SurveyProcessedRepository(
                    requireActivity().getApplication()).getAll();
            List<ActivityProcessed> activityProcessedList = new ActivityProcessedRepository(
                    requireActivity().getApplication()).getAll();


            List<Entry> lineEntries = new ArrayList<>();
            List<BarEntry> barEntries = new ArrayList<>();


            switch (scaleSettingChart) {
                case 0:
                    for (SurveyProcessed surveyProcessed : surveyProcessedList) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(surveyProcessed.getTimestamp());
                        lineEntries.add(new Entry(calendar.get(Calendar.HOUR_OF_DAY),
                                surveyProcessed.getRating()));
                    }
                    for (ActivityProcessed activityProcessed : activityProcessedList) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(activityProcessed.getTimestamp());
                        barEntries.add(new BarEntry(calendar.get(Calendar.HOUR_OF_DAY),
                                activityProcessed.getWeight()));
                    }
                    break;
                case 1:
                    for (SurveyProcessed surveyProcessed : surveyProcessedList) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(surveyProcessed.getTimestamp());
                        lineEntries.add(new Entry(calendar.get(Calendar.DAY_OF_WEEK),
                                surveyProcessed.getRating()));
                    }
                    for (ActivityProcessed activityProcessed : activityProcessedList) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(activityProcessed.getTimestamp());
                        barEntries.add(new BarEntry(calendar.get(Calendar.DAY_OF_WEEK),
                                activityProcessed.getWeight()));
                    }
                    break;
                case 2:
                    for (SurveyProcessed surveyProcessed : surveyProcessedList) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(surveyProcessed.getTimestamp());
                        lineEntries.add(new Entry(calendar.get(Calendar.DAY_OF_MONTH),
                                surveyProcessed.getRating()));
                    }
                    for (ActivityProcessed activityProcessed : activityProcessedList) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(activityProcessed.getTimestamp());
                        barEntries.add(new BarEntry(calendar.get(Calendar.DAY_OF_MONTH),
                                activityProcessed.getWeight()));
                    }
                    break;
            }

            LineDataSet lineDataSet = new LineDataSet(lineEntries, "average mood (-)");
            lineDataSet.setHighlightEnabled(false);
            lineDataSet.setColor(Color.rgb(240, 238, 70));
            lineDataSet.setLineWidth(2.5f);
            lineDataSet.setCircleColor(Color.rgb(240, 238, 70));
            lineDataSet.setCircleRadius(5f);
            lineDataSet.setFillColor(Color.rgb(240, 238, 70));
            lineDataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
            lineDataSet.setDrawValues(false);
            lineDataSet.setValueTextColor(Color.rgb(240, 238, 70));

            lineDataSet.setAxisDependency(YAxis.AxisDependency.RIGHT);

            lineData.addDataSet(lineDataSet);


            BarDataSet barDataSet = new BarDataSet(barEntries, "weigh acceleration activity (in m/s)");
            barDataSet.setColor(Color.rgb(60, 220, 78));
            barDataSet.setDrawValues(false);
            barDataSet.setValueTextColor(Color.rgb(60, 220, 78));
            barDataSet.setValueTextSize(10f);
            barDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);

            barData.addDataSet(barDataSet);


            activity.runOnUiThread(() -> {
                CombinedData combinedData = new CombinedData();

                combinedData.setData(barData);
                combinedData.setData(lineData);
                xAxis.setAxisMaximum(combinedData.getXMax() + 0f);
                yAxisLeft.setAxisMaximum(combinedData.getYMax() + 10f);
                yAxisRight.setAxisMaximum(lineData.getYMax() + 0.25f);
                combinedChart.setData(combinedData);
                combinedChart.invalidate();

            });


            ///         }

        }).start();
    }

    private final Slider.OnSliderTouchListener touchListener = new Slider.OnSliderTouchListener() {
        @Override
        public void onStartTrackingTouch(Slider slider) {

        }

        @Override
        public void onStopTrackingTouch(Slider slider) {

        }
    };
/*
    private LineData generateMoodData(){
        LineData lineData = new LineData();

        List<SurveyProcessed> surveyProcessedList = new SurveyProcessedRepository(
                requireActivity().getApplication()).getAll();

        List<Entry> lineEntries = new ArrayList<>();


        switch (scaleSettingChart) {
            case 0:
                for (SurveyProcessed surveyProcessed : surveyProcessedList) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(surveyProcessed.getTimestamp());
                    lineEntries.add(new Entry(calendar.get(Calendar.HOUR_OF_DAY),
                            surveyProcessed.getRating()));
                }
                break;
            case 1:
                for (SurveyProcessed surveyProcessed : surveyProcessedList) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(surveyProcessed.getTimestamp());
                    lineEntries.add(new Entry(calendar.get(Calendar.DAY_OF_WEEK),
                            surveyProcessed.getRating()));
                }
                break;
            case 2:
                for (SurveyProcessed surveyProcessed : surveyProcessedList) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(surveyProcessed.getTimestamp());
                    lineEntries.add(new Entry(calendar.get(Calendar.DAY_OF_MONTH),
                            surveyProcessed.getRating()));
                }
                break;
        }

        LineDataSet lineDataSet = new LineDataSet(lineEntries, "average mood (-)");
        lineDataSet.setHighlightEnabled(false);
        lineDataSet.setColor(Color.rgb(240, 238, 70));
        lineDataSet.setLineWidth(2.5f);
        lineDataSet.setCircleColor(Color.rgb(240, 238, 70));
        lineDataSet.setCircleRadius(5f);
        lineDataSet.setFillColor(Color.rgb(240, 238, 70));
        lineDataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        lineDataSet.setDrawValues(true);

        lineDataSet.setValueTextColor(Color.rgb(240, 238, 70));

        lineDataSet.setAxisDependency(YAxis.AxisDependency.RIGHT);

        lineData.addDataSet(lineDataSet);

        return lineData;

    }
    private BarData generateActivityDate(){

        List<ActivityProcessed> activityProcessedList = new ActivityProcessedRepository(
                requireActivity().getApplication()).getAll();


        List<BarEntry> barEntries = new ArrayList<>();

        switch (scaleSettingChart) {
            case 0:
                for (ActivityProcessed activityProcessed : activityProcessedList) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(activityProcessed.getTimestamp());
                    barEntries.add(new BarEntry(calendar.get(Calendar.HOUR_OF_DAY),
                            activityProcessed.getWeight()));
                }
                break;
            case 1:
                for (ActivityProcessed activityProcessed : activityProcessedList) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(activityProcessed.getTimestamp());
                    barEntries.add(new BarEntry(calendar.get(Calendar.DAY_OF_WEEK),
                            activityProcessed.getWeight()));
                }
                break;
            case 2:
                for (ActivityProcessed activityProcessed : activityProcessedList) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(activityProcessed.getTimestamp());
                    barEntries.add(new BarEntry(calendar.get(Calendar.DAY_OF_MONTH),
                            activityProcessed.getWeight()));
                }
                break;
        }

        for (ActivityProcessed activityProcessed : activityProcessedList) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(activityProcessed.getTimestamp());
            barEntries.add(new BarEntry(calendar.get(Calendar.HOUR_OF_DAY),
                    activityProcessed.getWeight()));
        }

        BarData barData=new BarData();

        BarDataSet barDataSet = new BarDataSet(barEntries, "weigh acceleration activity (in m/s)");
        barDataSet.setColor(Color.rgb(60, 220, 78));
        barDataSet.setDrawValues(false);
        barDataSet.setValueTextColor(Color.rgb(60, 220, 78));
        barDataSet.setValueTextSize(10f);
        barDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);

        barData.addDataSet(barDataSet);

        return barData;
    }
*/
}