package xyz.steidle.cellularnetworkmapper.utils;

import android.content.Context;
import android.os.Build;
import android.telephony.CellIdentity;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityNr;
import android.telephony.CellIdentityTdscdma;
import android.telephony.CellIdentityWcdma;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoNr;
import android.telephony.CellInfoTdscdma;
import android.telephony.CellInfoWcdma;
import android.text.TextUtils;

import androidx.annotation.RequiresApi;

import xyz.steidle.cellularnetworkmapper.R;

/**
 * Utility functions to parse CellInfo objects
 */
public class CellParser {
    Context context;

    public CellParser(Context context) {
        this.context = context;
    }

    /** Create Header string from CellInfo
     * @param cellInfo CellInfo object
     * @return Header for row element, format: '{Operator} : {Generation}'
     */
    // @RequiresApi(api = Build.VERSION_CODES.P)
    public CharSequence getCellHeader(CellInfo cellInfo) {
        CharSequence header;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            // Build version >= 28
            header = getCellHeaderP(cellInfo);
        } else {
            header = getCellHeaderLegacy(cellInfo);
        }

        return header;
    }

    /** Creates a Header string from CellInfo for app with Android Version >= 28
     * @param cellInfo CellInfo object
     * @return Header for row element, format: '{Operator} : {Generation}'
     */
    @RequiresApi(api = Build.VERSION_CODES.P)
    private CharSequence getCellHeaderP(CellInfo cellInfo) {
        CharSequence operator = context.getString(R.string.unknown);
        CharSequence generation = context.getString(R.string.unknown);

        CellIdentity cellIdentity = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && cellInfo instanceof CellInfoNr) {
            // Build version >= 30 and 5G network
            cellIdentity = cellInfo.getCellIdentity();
            generation = "5G";
        } else if (cellInfo instanceof CellInfoLte) {
            cellIdentity = ((CellInfoLte) cellInfo).getCellIdentity();
            generation = "LTE";
        } else if (cellInfo instanceof CellInfoGsm) {
            cellIdentity = ((CellInfoGsm) cellInfo).getCellIdentity();
            generation = "GSM";
        } else if (cellInfo instanceof CellInfoCdma) {
            cellIdentity = ((CellInfoCdma) cellInfo).getCellIdentity();
            generation = "CDMA";
        } else if (cellInfo instanceof CellInfoWcdma) {
            cellIdentity = ((CellInfoWcdma) cellInfo).getCellIdentity();
            generation = "WCDMA";
        }

        if (cellIdentity != null && !TextUtils.isEmpty(cellIdentity.getOperatorAlphaShort()))
            operator = cellIdentity.getOperatorAlphaShort();

        return context.getString(R.string.cell_header, operator, generation);
    }

    /** Creates a Header string from CellInfo for app with Android Version < 28
     * @param cellInfo CellInfo object
     * @return Header for row element, format: '{Operator} : {Generation}'
     */
    private CharSequence getCellHeaderLegacy(CellInfo cellInfo) {
        CharSequence generation;

        if (cellInfo instanceof CellInfoLte) {
            generation = "LTE";
        } else {
            generation = "GSM";
        }

        return context.getString(R.string.cell_header, context.getString(R.string.unknown), generation);
    }

    /** Extract MCC from given CellInfo
     * @param cellInfo CellInfo object
     * @return String representation of MCC
     */
    public String getMcc(CellInfo cellInfo) {
        String mcc = context.getString(R.string.unknown);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            // Build version >= 28
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && cellInfo instanceof CellInfoNr) {
                // Build version >= 30 and 5G network
                CellIdentityNr cellIdentity = (CellIdentityNr) cellInfo.getCellIdentity();
                mcc = cellIdentity.getMccString();
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && cellInfo instanceof CellInfoTdscdma) {
                // Build version >= 29 and TDSCDMA network
                CellIdentityTdscdma cellIdentity = ((CellInfoTdscdma) cellInfo).getCellIdentity();
                mcc = cellIdentity.getMccString();
            } else if (cellInfo instanceof CellInfoLte) {
                CellIdentityLte cellIdentity = ((CellInfoLte) cellInfo).getCellIdentity();
                mcc = cellIdentity.getMccString();
            } else if (cellInfo instanceof CellInfoGsm) {
                CellIdentityGsm cellIdentity = ((CellInfoGsm) cellInfo).getCellIdentity();
                mcc = cellIdentity.getMccString();
            } else if (cellInfo instanceof CellInfoWcdma) {
                CellIdentityWcdma cellIdentity = ((CellInfoWcdma) cellInfo).getCellIdentity();
                mcc = cellIdentity.getMccString();
            }
        } else {
            int intMcc = -1;

            if (cellInfo instanceof CellInfoLte) {
                CellIdentityLte cellIdentity = ((CellInfoLte) cellInfo).getCellIdentity();
                intMcc = cellIdentity.getMcc();
            } else if (cellInfo instanceof CellInfoGsm) {
                CellIdentityGsm cellIdentity = ((CellInfoGsm) cellInfo).getCellIdentity();
                intMcc = cellIdentity.getMcc();
            } else if (cellInfo instanceof CellInfoWcdma) {
                CellIdentityWcdma cellIdentity = ((CellInfoWcdma) cellInfo).getCellIdentity();
                intMcc = cellIdentity.getMcc();
            }

            mcc = Integer.toString(intMcc);
        }

        return mcc;
    }

    /** Extract MNC from given CellInfo
     * @param cellInfo CellInfo object
     * @return String representation of MNC
     */
    public String getMnc(CellInfo cellInfo) {
        String mnc = context.getString(R.string.unknown);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            // Build version >= 28
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && cellInfo instanceof CellInfoNr) {
                // Build version >= 30 and 5G network
                CellIdentityNr cellIdentity = (CellIdentityNr) cellInfo.getCellIdentity();
                mnc = cellIdentity.getMccString();
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && cellInfo instanceof CellInfoTdscdma) {
                // Build version >= 29 and TDSCDMA network
                CellIdentityTdscdma cellIdentity = ((CellInfoTdscdma) cellInfo).getCellIdentity();
                mnc = cellIdentity.getMccString();
            } else if (cellInfo instanceof CellInfoLte) {
                CellIdentityLte cellIdentity = ((CellInfoLte) cellInfo).getCellIdentity();
                mnc = cellIdentity.getMccString();
            } else if (cellInfo instanceof CellInfoGsm) {
                CellIdentityGsm cellIdentity = ((CellInfoGsm) cellInfo).getCellIdentity();
                mnc = cellIdentity.getMccString();
            } else if (cellInfo instanceof CellInfoWcdma) {
                CellIdentityWcdma cellIdentity = ((CellInfoWcdma) cellInfo).getCellIdentity();
                mnc = cellIdentity.getMccString();
            }
        } else {
            if (cellInfo instanceof CellInfoLte) {
                CellIdentityLte cellIdentity = ((CellInfoLte) cellInfo).getCellIdentity();
                mnc = Integer.toString(cellIdentity.getMcc());
            } else if (cellInfo instanceof CellInfoGsm) {
                CellIdentityGsm cellIdentity = ((CellInfoGsm) cellInfo).getCellIdentity();
                mnc = Integer.toString(cellIdentity.getMcc());
            } else if (cellInfo instanceof CellInfoWcdma) {
                CellIdentityWcdma cellIdentity = ((CellInfoWcdma) cellInfo).getCellIdentity();
                mnc = Integer.toString(cellIdentity.getMcc());
            }
        }

        return mnc;
    }
}
