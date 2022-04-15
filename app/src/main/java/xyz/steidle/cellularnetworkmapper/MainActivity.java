package xyz.steidle.cellularnetworkmapper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.CellInfo;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.List;
import java.util.concurrent.TimeUnit;

import xyz.steidle.cellularnetworkmapper.utils.DataHolder;
import xyz.steidle.cellularnetworkmapper.utils.DatabaseHandler;
import xyz.steidle.cellularnetworkmapper.view.CellInfoAdapter;

public class MainActivity extends AppCompatActivity {
  private ListView cellInfoListView;
  private TextView noCellsTextView;
  private TextView statusTextView;
  private BroadcastReceiver receiver;
  private CellInfoHandler cellInfoHandler;
  private DatabaseHandler databaseHandler;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    databaseHandler = new DatabaseHandler(this);

    cellInfoListView = findViewById(R.id.listScrollView);
    noCellsTextView = findViewById(R.id.noCellsWarning);
    statusTextView = findViewById(R.id.statusTextView);

    findViewById(R.id.buttonHelp).setOnClickListener(view -> openHelpActivity());
    findViewById(R.id.buttonHistory).setOnClickListener(view -> openHistoryActivity());

    cellInfoHandler = new CellInfoHandler(this);

    IntentFilter intentFilter = new IntentFilter();
    intentFilter.addAction("android.intent.action.TIME_TICK");

    receiver = new BroadcastReceiver() {
      @Override
      public void onReceive(Context context, Intent intent) {
        reloadCells();
      }
    };
    registerReceiver(receiver, intentFilter);

    reloadCells();
  }

  /** Restart cell scan and displays all cells in list. */
  protected void reloadCells() {
    setStatus(R.string.status_searching);

    List<CellInfo> cellInfoList = cellInfoHandler.getCells();

    for (CellInfo cellInfo : cellInfoList)
      databaseHandler.addCell(cellInfo);

    if (!cellInfoList.isEmpty()) {
      noCellsTextView.setVisibility(View.INVISIBLE);
      cellInfoListView.setAdapter(new CellInfoAdapter(this, cellInfoList));
      resetStatus();
    } else {
      noCellsTextView.setVisibility(View.VISIBLE);
      setStatus(R.string.status_wrong);
    }
  }

  /** Sets status in status bar from given resource id.
   * @param strId Status String ID
   */
  private void setStatus(int strId) {
    Log.d("MainActivity", getResources().getString(strId));
    statusTextView.setText(getResources().getString(strId));
  }

  /** Sets status text to empty */
  private void resetStatus() {
    statusTextView.setText("");
  }

  /** creates and starts the help activity */
  private void openHelpActivity() {
    Intent intent = new Intent(this, HelpActivity.class);
    startActivity(intent);
  }

  /** creates and starts the history activity */
  private void openHistoryActivity() {
    Intent intent = new Intent(this, HistoryActivity.class);
    startActivity(intent);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    unregisterReceiver(receiver);
  }
}
