package de.rub.selab22a15;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.github.mikephil.charting.charts.CombinedChart;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.datepicker.MaterialDatePicker;

import de.rub.selab22a15.activities.SurveyActivity;
import de.rub.selab22a15.helpers.Statistics;
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

        // Survey
        cardViewSurvey = activity.findViewById(R.id.cardViewSurvey);
        MaterialButton buttonCardViewSurveyStart = activity.findViewById(R.id.buttonCardViewSurveyStart);
        buttonCardViewSurveyStart.setOnClickListener(v -> startSurvey());

        // Statistics
        CombinedChart combinedChart = activity.findViewById(R.id.combinedChartStatistics);
        Statistics statistics = new Statistics(combinedChart);
        MaterialButton buttonCardViewSelectDate = activity.findViewById(R.id.buttonCardViewSelectDate);
        buttonCardViewSelectDate.setOnClickListener(v -> {
            MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
            builder.setTitleText(R.string.stringSelectDate);
            MaterialDatePicker<Pair<Long, Long>> datePicker = builder.build();
            datePicker.addOnPositiveButtonClickListener(selection -> statistics.draw(selection.first, selection.second));
            datePicker.show(getParentFragmentManager(), null);
        });
        statistics.draw();
    }

    private void startSurvey() {
        Intent surveyIntent = new Intent(requireActivity(), SurveyActivity.class);
        startActivity(surveyIntent);
    }
}