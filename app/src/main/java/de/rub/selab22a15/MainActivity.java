package de.rub.selab22a15;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import de.rub.selab22a15.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        switchFragment(new HomeFragment());

        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.btmNavHome:
                    switchFragment(new HomeFragment());
                    break;
                case R.id.btmNavActivity:
                    switchFragment(new ActivityFragment());
                    break;
                case R.id.btmNavExport:
                    switchFragment(new ExportFragment());
                    break;
                case R.id.btmNavSettings:
                    switchFragment(new SettingsFragment());
                    break;
            }

            return true;
        });
    }

    private void switchFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frmLytMainActivity, fragment);
        fragmentTransaction.commit();
    }
}