package xyz.steidle.cellularnetworkmapper;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import xyz.steidle.cellularnetworkmapper.utils.DatabaseHandler;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        loadCellHistory();
    }

    /**
     * Loads cell information to user interface
     */
    private void loadCellHistory() {
        ListView listView = (ListView) findViewById(R.id.list);

        DatabaseHandler dbHandler = new DatabaseHandler(getApplicationContext());

        ArrayAdapter<String> arr = new ArrayAdapter<>(
                this,
                R.layout.support_simple_spinner_dropdown_item,
                dbHandler.getAllCells()
        );

        dbHandler.close();

        listView.setAdapter(arr);
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