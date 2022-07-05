package de.rub.selab22a15;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.rub.selab22a15.activities.SurveyActivity;
import de.rub.selab22a15.database.local.ActivityProcessed;
import de.rub.selab22a15.database.local.ActivityProcessedRepository;
import de.rub.selab22a15.workers.DatabaseProcessingWorker;

public class HomeFragment extends Fragment {
    private static final String LOG_TAG = "HomeFragment";
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
        MaterialButton buttonCardViewSurveyRemind = activity.findViewById(R.id.buttonCardViewSurveyRemind);
        MaterialButton buttonCardViewSurveyStart = activity.findViewById(R.id.buttonCardViewSurveyStart);

        buttonCardViewSurveyRemind.setOnClickListener(v -> cardViewSurvey.setVisibility(View.GONE));
        buttonCardViewSurveyStart.setOnClickListener(v -> startSurvey());

        Button proc = activity.findViewById(R.id.process);
        Button init = activity.findViewById(R.id.init);

        proc.setOnClickListener(v -> DatabaseProcessingWorker.start(requireContext()));
        init.setOnClickListener(v -> initChart());
    }

    private void initChart() {
        LineChart lineChart = requireActivity().findViewById(R.id.lineChartBasicStatistics);
        lineChart.setMinimumHeight(720);

        new Thread(() -> {
            List<ActivityProcessed> activityProcessedList = new ActivityProcessedRepository(
                    requireActivity().getApplication()).getAll();

            List<Entry> entries = new ArrayList<>();

            for (ActivityProcessed activityProcessed : activityProcessedList) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(activityProcessed.getTimestamp());
                entries.add(new Entry(calendar.get(Calendar.HOUR_OF_DAY), activityProcessed.getWeight()));
            }

            LineDataSet dataSet = new LineDataSet(entries, "Label");
            dataSet.setHighlightEnabled(false);
            dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);

            LineData lineData = new LineData(dataSet);

            lineChart.setData(lineData);
            lineChart.invalidate();
        }).start();
    }

    private void startSurvey() {
        Intent surveyIntent = new Intent(requireActivity(), SurveyActivity.class);
        startActivity(surveyIntent);
    }
}