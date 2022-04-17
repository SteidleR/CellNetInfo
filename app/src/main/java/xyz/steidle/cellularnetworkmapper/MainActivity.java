package xyz.steidle.cellularnetworkmapper;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.CellInfo;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;

import java.util.List;

import xyz.steidle.cellularnetworkmapper.utils.CellInfoHandler;
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

  private int locationRefreshTime = 15000; // 15 seconds to update
  private int locationRefreshDistance = 500; // 500 meters to update

  private final LocationListener mLocationListener = location -> DataHolder.getInstance().setLocation(location);

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    SharedPreferences sharedPreferences1 = PreferenceManager.getDefaultSharedPreferences(this /* Activity context */);
    Log.d("Prefs", String.valueOf(sharedPreferences1.getAll()));
    locationRefreshTime = Integer.parseInt(sharedPreferences1.getString("min_update_time", String.valueOf(locationRefreshTime)));
    locationRefreshDistance = Integer.parseInt(sharedPreferences1.getString("min_update_loc", String.valueOf(locationRefreshDistance)));

    sharedPreferences1.registerOnSharedPreferenceChangeListener((sharedPreferences, s) -> {
      locationRefreshDistance = Integer.parseInt(sharedPreferences.getString("min_update_time", String.valueOf(locationRefreshTime)));
      locationRefreshDistance = Integer.parseInt(sharedPreferences.getString("min_update_loc", String.valueOf(locationRefreshDistance)));
      Log.d("Preferences:UpdateString", s);
      Log.d("Preferences:Update", String.valueOf(locationRefreshTime));
      Log.d("Preferences:Update", String.valueOf(locationRefreshDistance));
    });

    databaseHandler = new DatabaseHandler(this);

    cellInfoListView = findViewById(R.id.listScrollView);
    noCellsTextView = findViewById(R.id.noCellsWarning);
    statusTextView = findViewById(R.id.statusTextView);

    findViewById(R.id.buttonHelp).setOnClickListener(view -> openHelpActivity());
    findViewById(R.id.buttonHistory).setOnClickListener(view -> openHistoryActivity());
    findViewById(R.id.buttonSettings).setOnClickListener(view -> openSettingsActivity());

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

    LocationManager mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 12);
      return;
    }
    mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, locationRefreshTime,
            locationRefreshDistance, mLocationListener);
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

  /** creates and starts the settings activity */
  private void openSettingsActivity() {
    Intent intent = new Intent(this, SettingsActivity.class);
    startActivity(intent);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    unregisterReceiver(receiver);
  }
}
