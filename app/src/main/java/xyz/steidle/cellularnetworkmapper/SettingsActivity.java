package xyz.steidle.cellularnetworkmapper;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import java.util.Objects;

import xyz.steidle.cellularnetworkmapper.utils.DatabaseHandler;

public class SettingsActivity extends AppCompatActivity {

    DatabaseHandler databaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        SettingsFragment settingsFragment = new SettingsFragment();

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, settingsFragment)
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        databaseHandler = new DatabaseHandler(this);
        settingsFragment.setClearHistoryOnClick(databaseHandler);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // return to Main when back button is pressed.
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }


        /** Creates new Click Listener to clear History on preference click
         * @param databaseHandler DatabaseHandler instance
         */
        public void setClearHistoryOnClick(DatabaseHandler databaseHandler) {
            Preference pref = findPreference("clear_hist");
            if (pref != null) {
                pref.setOnPreferenceClickListener(preference -> {
                    databaseHandler.clearCellTable();
                    return false;
                });
            } else {
                Log.d("Preferences", "Empty Pref");
            }
        }
    }
}