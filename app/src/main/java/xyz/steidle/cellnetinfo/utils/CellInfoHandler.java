package xyz.steidle.cellnetinfo.utils;

import android.content.Context;
import android.telephony.CellInfo;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.Arrays;
import java.util.List;

/**
 * Used to retrieve cell info objects from telephony manager
 */
public class CellInfoHandler {
  private final TelephonyManager telephonyManager;

  public CellInfoHandler(Context context) {
    telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
  }

  /** retrieves cell info objects from telephony manager
   * @return List of cell info objects
   */
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
