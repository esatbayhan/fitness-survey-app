package de.rub.selab22a15;

import static de.rub.selab22a15.App.APPLICATION_PREFERENCES;
import static de.rub.selab22a15.App.KEY_IS_FIRST_START;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import de.rub.selab22a15.databinding.ActivityMainBinding;
import de.rub.selab22a15.receivers.SurveyAlarmReceiver;

public class MainActivity extends AppCompatActivity {

    @IdRes
    Integer currentItemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handleFirstStart();

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        switchFragment(new HomeFragment(), R.id.btmNavHome);

        binding.bottomNavigation.setOnItemSelectedListener(item -> onClick(item.getItemId()));
    }

    private boolean onClick(@IdRes int itemId) {
        if (currentItemId == itemId) {
            return false;
        }

        Fragment fragment;

        if (itemId == R.id.btmNavHome) {
            fragment = new HomeFragment();
        } else if (itemId == R.id.btmNavActivity) {
            fragment = new ActivityFragment();
        } else if (itemId == R.id.btmNavSettings) {
            fragment = new SettingsFragment();
        } else {
            Log.e("MainActivity", "Wrong itemId, got: " + itemId);
            return false;
        }

        switchFragment(fragment, itemId);
        return true;
    }

    private void switchFragment(Fragment fragment, @IdRes int itemId) {
        currentItemId = itemId;
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frmLytMainActivity, fragment);
        fragmentTransaction.commit();
    }

    private void handleFirstStart() {
        SharedPreferences sharedPreferences = getSharedPreferences(
                APPLICATION_PREFERENCES, MODE_PRIVATE);

        boolean isFirstStart = sharedPreferences.getBoolean(KEY_IS_FIRST_START, true);

        if (!isFirstStart) {
            return;
        }

        SurveyAlarmReceiver.setAlarm(getApplicationContext());
        sharedPreferences.edit().putBoolean(KEY_IS_FIRST_START, false).apply();
    }
}