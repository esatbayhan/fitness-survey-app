package de.rub.selab22a15.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import de.rub.selab22a15.R;

public class SurveyNoteFragment extends Fragment {
    private static final int DISCARD_THRESHOLD = 3;

    private TextInputEditText editTextSurveyNote;

    public String getNote() {
        String note = Objects.requireNonNull(editTextSurveyNote.getText()).toString();

        if (note.length() < DISCARD_THRESHOLD) {
            return null;
        }

        return note;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_survey_note, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentActivity activity = requireActivity();
        editTextSurveyNote = activity.findViewById(R.id.editTextSurveyNote);
        MaterialButton buttonSurveyNoteClear = activity.findViewById(R.id.buttonSurveyNoteClear);

        buttonSurveyNoteClear.setOnClickListener(v -> editTextSurveyNote.setText(""));
    }
}