package de.rub.selab22a15;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;

import java.util.Map;

import de.rub.selab22a15.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    @IdRes Integer currentItemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
}