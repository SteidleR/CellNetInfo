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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
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
        int sdkInt = Build.VERSION.SDK_INT;

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

        protected void writeDataToFile(String fileName, List<String[]> data) throws IOException {
            CSVWriter writer = new CSVWriter(new FileWriter(fileName));
            writer.writeAll(data);
            writer.close();
        }

        /** function to export the history as csv file */
        private void exportHistory() {
            String exportFolderPath = createStoragePath(String.format("cellnetinfo_history_%s", Calendar.getInstance().getTimeInMillis()));

            File folder = new File(exportFolderPath);
            boolean success = folder.mkdir();
            if (!success) {
                Log.e(LOGTAG, "Failed to create folder: " + folder.getAbsolutePath());
                return;
            }

            Log.i(LOGTAG, "Exporting history to folder (" + exportFolderPath + ")");

            String csvFilePath = exportFolderPath + File.separator + "history_%s.csv";
            try {
                writeDataToFile(String.format(csvFilePath, "nr"), databaseHandler.getAllNrCells());
                writeDataToFile(String.format(csvFilePath, "lte"), databaseHandler.getAllLteCells());
                writeDataToFile(String.format(csvFilePath, "gsm"), databaseHandler.getAllGsmCells());
                writeDataToFile(String.format(csvFilePath, "cdma"), databaseHandler.getAllCdmaCells());
                writeDataToFile(String.format(csvFilePath, "tdscdma"), databaseHandler.getAllTdscdmaCells());
                writeDataToFile(String.format(csvFilePath, "wcdma"), databaseHandler.getAllWcdmaCells());

            } catch (IOException e) {
                Log.e(LOGTAG, Log.getStackTraceString(e));
            }
        }

        /** Creates path to Download folder
         * @param fileName the name of the file to store history in, should be "something.csv"
         * @return path to Download/
         */
        protected String createStoragePath(String fileName) {
            String storageDirectoryPath;
            if (sdkInt >= Build.VERSION_CODES.R) {
                if(storageManager == null) {
                    storageDirectoryPath = Environment.getExternalStorageDirectory().getAbsolutePath();
                } else {
                    storageDirectoryPath = storageManager.getPrimaryStorageVolume().getDirectory().getAbsolutePath();
                }
            } else {
                storageDirectoryPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            }

            return storageDirectoryPath + File.separator +  "Download" + File.separator + fileName;
        }
    }
}