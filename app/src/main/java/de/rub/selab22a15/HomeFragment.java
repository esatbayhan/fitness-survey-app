package de.rub.selab22a15;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import de.rub.selab22a15.activities.SurveyActivity;

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
        MaterialButton buttonCardViewSurveyRemind = activity.findViewById(R.id.buttonCardViewSurveyRemind);
        MaterialButton buttonCardViewSurveyStart = activity.findViewById(R.id.buttonCardViewSurveyStart);

        buttonCardViewSurveyRemind.setOnClickListener(v -> cardViewSurvey.setVisibility(View.GONE));
        buttonCardViewSurveyStart.setOnClickListener(v -> startSurvey());
    }

    private void startSurvey() {
        Intent surveyIntent = new Intent(requireActivity(), SurveyActivity.class);
        startActivity(surveyIntent);
    }
}