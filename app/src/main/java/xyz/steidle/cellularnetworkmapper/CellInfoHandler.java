package xyz.steidle.cellularnetworkmapper;

import android.content.Context;
import android.telephony.CellInfo;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.Arrays;
import java.util.List;

public class CellInfoHandler {
  private final TelephonyManager telephonyManager;

  public CellInfoHandler(Context context) {
    telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
  }

  public List<CellInfo> getCells() {
    List<CellInfo> cells = null;
    try {
      cells = telephonyManager.getAllCellInfo();
    } catch (SecurityException e) {
      Log.e("CellInfoHandler", Arrays.toString(e.getStackTrace()));
    }
    return cells;
  }
}
