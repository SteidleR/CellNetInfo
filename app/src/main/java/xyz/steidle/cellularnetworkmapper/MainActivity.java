package xyz.steidle.cellularnetworkmapper;

import android.os.Build;
import android.os.Bundle;
import android.telephony.CellInfo;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import xyz.steidle.cellularnetworkmapper.view.CellInfoAdapter;

public class MainActivity extends AppCompatActivity {
    private CellInfoHandler cellInfoHandler;
    private ListView cellInfoListView;
    private TextView noCellsTextView;
    private TextView statusTextView;

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cellInfoListView = findViewById(R.id.listScrollView);
        noCellsTextView = findViewById(R.id.noCellsWarning);
        statusTextView = findViewById(R.id.statusTextView);

        cellInfoHandler = new CellInfoHandler(this);

        setStatus(R.string.status_searching);

        List<CellInfo> cellInfoList = cellInfoHandler.getCells();

        if (cellInfoList != null) {
            noCellsTextView.setVisibility(View.INVISIBLE);
            cellInfoListView.setAdapter(new CellInfoAdapter(this, cellInfoList));
            resetStatus();
        } else {
            noCellsTextView.setVisibility(View.VISIBLE);
            setStatus(R.string.status_wrong);
        }
    }

    private void setStatus(int strId) {
        statusTextView.setText(getResources().getString(strId));
    }

    private void resetStatus() {
        statusTextView.setText("");
    }
}