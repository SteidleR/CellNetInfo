package xyz.steidle.cellularnetworkmapper;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import xyz.steidle.cellularnetworkmapper.utils.DatabaseHandler;
import xyz.steidle.cellularnetworkmapper.view.CellHistoryAdapter;

public class HistoryActivity extends AppCompatActivity {

    DatabaseHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        dbHandler = new DatabaseHandler(getApplicationContext());

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        findViewById(R.id.btn_export).setOnClickListener(view -> exportHistory());

        loadCellHistory();
    }

    /**
     * Loads cell information to user interface
     */
    private void loadCellHistory() {
        ListView listView = (ListView) findViewById(R.id.list);

        listView.setAdapter(new CellHistoryAdapter(this, dbHandler.getAllCellsCsv()));
        dbHandler.close();
    }

    /** function to export the history as csv file */
    private void exportHistory() {
        String csv = createStoragePath("history.csv");

        Log.i("HistoryActivity", "Exporting history to csv (" + csv + ")");
        CSVWriter writer;
        try {
            writer = new CSVWriter(new FileWriter(csv));

            List<String[]> data = dbHandler.getAllCellsCsv();

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
            StorageManager storageManager = (StorageManager) getSystemService(Context.STORAGE_SERVICE);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // return to Main when back button is pressed.
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}