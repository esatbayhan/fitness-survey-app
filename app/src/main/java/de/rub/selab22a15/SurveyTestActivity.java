package de.rub.selab22a15;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import de.rub.selab22a15.db.Mood;
import de.rub.selab22a15.db.MoodRepository;

public class SurveyTestActivity extends AppCompatActivity {

    private SeekBar sbSatisfied;
    private SeekBar sbCalm;
    private SeekBar sbWell;
    private SeekBar sbRelaxed;
    private SeekBar sbEnergetic;
    private SeekBar sbAwake;

    private Button btnSurveyClear;
    private Button btnSurveySave;

    private MoodRepository moodRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_test);

        sbSatisfied = findViewById(R.id.sbSatisfied);
        sbCalm = findViewById(R.id.sbCalm);
        sbWell = findViewById(R.id.sbWell);
        sbRelaxed = findViewById(R.id.sbRelaxed);
        sbEnergetic = findViewById(R.id.sbEnergetic);
        sbAwake = findViewById(R.id.sbAwake);

        btnSurveyClear = findViewById(R.id.btnSurveyClear);
        btnSurveySave = findViewById(R.id.btnSurveySave);

        moodRepository = new MoodRepository(getApplication());

        btnSurveyClear.setOnClickListener(new btnSurveyClearOnClick());
        btnSurveySave.setOnClickListener(new btnSurveySaveOnClick());
    }

    private void clearSurvey() {
        sbSatisfied.setProgress(0);
        sbCalm.setProgress(0);
        sbWell.setProgress(0);
        sbRelaxed.setProgress(0);
        sbEnergetic.setProgress(0);
        sbAwake.setProgress(0);
    }

    private class btnSurveyClearOnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            clearSurvey();
        }
    }

    private class btnSurveySaveOnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Mood mood = new Mood(
                    System.currentTimeMillis(),
                    sbSatisfied.getProgress(),
                    sbCalm.getProgress(),
                    sbWell.getProgress(),
                    sbRelaxed.getProgress(),
                    sbEnergetic.getProgress(),
                    sbAwake.getProgress()
            );

            moodRepository.insert(mood);

            clearSurvey();
        }
    }
}