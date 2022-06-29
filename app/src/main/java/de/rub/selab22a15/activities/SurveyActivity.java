package de.rub.selab22a15.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import de.rub.selab22a15.R;
import de.rub.selab22a15.fragments.SurveyMoodFragment;

public class SurveyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

        changeFragment(0);
    }

    private void changeFragment(int id) {

        Fragment fragment;

        switch (id) {
            case 0:
                fragment = new SurveyMoodFragment();
                break;
            default:
                return;
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayoutSurveyActivity, fragment)
                .commit();
    }
}