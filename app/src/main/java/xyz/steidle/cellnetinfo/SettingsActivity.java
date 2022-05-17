package xyz.steidle.cellnetinfo;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.util.Log;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import xyz.steidle.cellnetinfo.utils.DatabaseHandler;

public class SettingsActivity extends AppCompatActivity {

    protected DatabaseHandler databaseHandler;
    protected SettingsFragment settingsFragment;
    protected static final String LOGTAG = "SettingsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        CoordinatorLayout snackbarLayout = (CoordinatorLayout) findViewById(R.id.myCoordinatorLayout);
        Snackbar snackbarClear = Snackbar.make(snackbarLayout, R.string.history_cleared, BaseTransientBottomBar.LENGTH_SHORT);
        Snackbar snackbarExport = Snackbar.make(snackbarLayout, R.string.history_exported, BaseTransientBottomBar.LENGTH_SHORT);

        StorageManager storageManager = (StorageManager) getSystemService(Context.STORAGE_SERVICE);

        databaseHandler = new DatabaseHandler(this);

        settingsFragment = new SettingsFragment(databaseHandler, snackbarClear, snackbarExport, storageManager);

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
        DatabaseHandler databaseHandler;
        Snackbar snackbarHistory;
        Snackbar snackbarExport;
        StorageManager storageManager;

        /** Settings Fragment for creating Preferences screen
         * @param databaseHandler database handler instance
         * @param snackbarHistory Snackbar to show when deleting history
         * @param snackbarExport Snackbar to show when exporting history
         * @param storageManager StorageManager instance
         */
        public SettingsFragment(DatabaseHandler databaseHandler,
                                Snackbar snackbarHistory,
                                Snackbar snackbarExport,
                                StorageManager storageManager) {
            this.databaseHandler = databaseHandler;
            this.snackbarHistory = snackbarHistory;
            this.snackbarExport = snackbarExport;
            this.storageManager = storageManager;
        }

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            setClearHistoryOnClick();
            setExportHistoryOnClick();

        }

        /** Creates new Click Listener to clear History on preference click */
        public void setClearHistoryOnClick() {
            Preference pref = findPreference("clear_hist");
            if (pref != null) {
                pref.setOnPreferenceClickListener(preference -> {
                    Log.i(LOGTAG,"Clearing history");
                    databaseHandler.clearCellTable();
                    snackbarHistory.show();
                    return false;
                });
            } else {
                Log.d(LOGTAG, "Empty Preference 'clear_hist'");
            }
        }

        /** Create onclick for preference screen to export history */
        public void setExportHistoryOnClick() {
            Preference pref = findPreference("export_hist");
            if (pref != null) {
                pref.setOnPreferenceClickListener(preference -> {
                    Log.i(LOGTAG,"Exporting history");
                    exportHistory();
                    snackbarExport.show();
                    return false;
                });
            } else {
                Log.d(LOGTAG, "Empty Preference 'clear_hist'");
            }
        }

        /** function to export the history as csv file */
        private void exportHistory() {
            String csv = createStoragePath("history.csv");

            Log.i(LOGTAG, "Exporting history to csv (" + csv + ")");
            CSVWriter writer;
            try {
                writer = new CSVWriter(new FileWriter(csv));

                List<String[]> data = databaseHandler.getAllCellsCsv();

                writer.writeAll(data); // data is adding to csv
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /** Creates path to Download folder
         * @param fileName the name of the file to store history in, should be "something.csv"
         * @return path to Download/
         */
        private String createStoragePath(String fileName) {
            String storageDirectoryPath;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if(storageManager == null) {
                    storageDirectoryPath = Environment.getExternalStorageDirectory().getAbsolutePath();
                } else {
                    storageDirectoryPath = storageManager.getPrimaryStorageVolume().getDirectory().getAbsolutePath();
                }
            } else {
                storageDirectoryPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            }

            return storageDirectoryPath + "/Download/" + fileName;
        }
    }
}