package xyz.steidle.cellularnetworkmapper;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import xyz.steidle.cellularnetworkmapper.utils.DatabaseHandler;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        findViewById(R.id.buttonBack).setOnClickListener(view -> finish());

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
}