package xyz.steidle.cellnetinfo;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import xyz.steidle.cellnetinfo.utils.DatabaseHandler;
import xyz.steidle.cellnetinfo.view.CellHistoryAdapter;

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

        loadCellHistory();
    }

    /**
     * Loads cell information to user interface
     */
    private void loadCellHistory() {
        ListView listView = (ListView) findViewById(R.id.list);

        listView.setAdapter(new CellHistoryAdapter(this, dbHandler.getAllCellsGrouped(), dbHandler));
        dbHandler.close();
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