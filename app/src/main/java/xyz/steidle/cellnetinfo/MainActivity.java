package xyz.steidle.cellnetinfo;

import android.Manifest;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.CellInfo;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;

import java.util.Arrays;
import java.util.List;

import xyz.steidle.cellnetinfo.utils.CellInfoHandler;
import xyz.steidle.cellnetinfo.utils.DataHolder;
import xyz.steidle.cellnetinfo.utils.DatabaseHandler;
import xyz.steidle.cellnetinfo.utils.Reload;
import xyz.steidle.cellnetinfo.view.CellInfoAdapter;

public class MainActivity extends AppCompatActivity {
  ListView cellInfoListView;
  TextView noCellsTextView;
  TextView statusTextView;
  BroadcastReceiver receiver;
  CellInfoHandler cellInfoHandler;
  DatabaseHandler databaseHandler;

  private int locationRefreshTime = 15000; // 15 seconds to update
  private int locationRefreshDistance = 500; // 500 meters to update

  private static final int LOCATION_REQUEST_CODE = 12;
  private boolean arePermissionsSet = false;

  private final LocationListener mLocationListener = location -> DataHolder.getInstance().setLocation(location);

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    cellInfoHandler = new CellInfoHandler(this);
    databaseHandler = new DatabaseHandler(this);

    cellInfoListView = findViewById(R.id.listScrollView);
    noCellsTextView = findViewById(R.id.noCellsWarning);
    statusTextView = findViewById(R.id.statusTextView);

    // change activity based on button press
    findViewById(R.id.buttonHelp).setOnClickListener(view -> openNewActivity(HelpActivity.class));
    findViewById(R.id.buttonHistory).setOnClickListener(view -> openNewActivity(HistoryActivity.class));
    findViewById(R.id.buttonSettings).setOnClickListener(view -> openNewActivity(SettingsActivity.class));

    initializeApplicationLogic();
  }

  /** function to initialize all logics.
   * calls location and preferences function and creates the foreground service
   */
  protected void initializeApplicationLogic() {
    handleLocationManager();
    if (arePermissionsSet) {
      handlePreferences();
      createBackgroundReload();

      if (!foregroundServiceRunning() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        Intent reloadServiceIntent = new Intent(this, Reload.class);
        startForegroundService(reloadServiceIntent);
      }
    }
  }

  /** checks if foreground service Reload.java is running */
  public boolean foregroundServiceRunning() {
    ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
    for (ActivityManager.RunningServiceInfo service : activityManager.getRunningServices(Integer.MAX_VALUE)) {
      if (Reload.class.getName().equals(service.service.getClassName())) {
        return true;
      }
    }
    return false;
  }

  /** Creates new location manager and requests location updates based on settings */
  private void handleLocationManager() {
    LocationManager mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
      return;
    }
    arePermissionsSet = true;
    mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, locationRefreshTime, locationRefreshDistance, mLocationListener);
  }

  /** Gets preference values and registers new preference change listener */
  private void handlePreferences() {
    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this /* Activity context */);
    Log.d("Prefs", String.valueOf(sharedPreferences.getAll()));
    locationRefreshTime = Integer.parseInt(sharedPreferences.getString("min_update_time", String.valueOf(locationRefreshTime)));
    locationRefreshDistance = Integer.parseInt(sharedPreferences.getString("min_update_loc", String.valueOf(locationRefreshDistance)));

    sharedPreferences.registerOnSharedPreferenceChangeListener((sharedPreferences1, s) -> {
      locationRefreshTime = Integer.parseInt(sharedPreferences1.getString("min_update_time", String.valueOf(locationRefreshTime)));
      locationRefreshDistance = Integer.parseInt(sharedPreferences1.getString("min_update_loc", String.valueOf(locationRefreshDistance)));
      Log.d("Preferences:UpdateString", s);
      Log.d("Preferences:Update", String.valueOf(locationRefreshTime));
      Log.d("Preferences:Update", String.valueOf(locationRefreshDistance));
    });
  }

  /** Handles creation of background task to reload cells */
  private void createBackgroundReload() {
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
  private void reloadCells() {
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
  protected void setStatus(int strId) {
    Log.d("MainActivity", getResources().getString(strId));
    statusTextView.setText(getResources().getString(strId));
  }

  /** Sets status text to empty */
  protected void resetStatus() {
    statusTextView.setText("");
  }

  /** starts new activity based on given class parameter
   * @param cls class of the activity to open
   */
  private void openNewActivity(Class<?> cls)  {
    Intent intent = new Intent(this, cls);
    startActivity(intent);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    unregisterReceiver(receiver);

    Intent intent = new Intent(this, Reload.class);
    stopService(intent);
  }

  /** Called when one option of permission request popup is clicked
   * @deprecated Will be changed in future
   */
  @Deprecated
  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    int indexLocationPermission = Arrays.binarySearch(permissions, Manifest.permission.ACCESS_FINE_LOCATION);

    if (requestCode == LOCATION_REQUEST_CODE && grantResults[indexLocationPermission] == 0) {
      initializeApplicationLogic();
    }
  }
}
