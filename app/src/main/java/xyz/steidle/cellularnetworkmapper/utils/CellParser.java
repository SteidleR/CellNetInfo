package xyz.steidle.cellularnetworkmapper.utils;

import android.content.Context;
import android.os.Build;
import android.telephony.CellIdentity;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellInfo;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoNr;
import android.text.TextUtils;

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
        CharSequence operator = "Unknown";
        CharSequence generation = "Unknown";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            // Build version >= 28

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && cellInfo instanceof CellInfoNr) {
                // Build version >= 30 and 5G network

                CellIdentity cellIdentity = ((CellInfoNr) cellInfo).getCellIdentity();

                if (!TextUtils.isEmpty(cellIdentity.getOperatorAlphaShort()))
                    operator = cellIdentity.getOperatorAlphaShort();

                generation = "5G";
            } else if (cellInfo instanceof CellInfoLte) {
                CellIdentityLte cellIdentity = ((CellInfoLte) cellInfo).getCellIdentity();

                if (!TextUtils.isEmpty(cellIdentity.getOperatorAlphaShort()))
                    operator = cellIdentity.getOperatorAlphaShort();

                generation = "LTE";
            } else if (cellInfo instanceof CellInfoGsm) {
                CellIdentityGsm cellIdentity = ((CellInfoGsm) cellInfo).getCellIdentity();

                if (!TextUtils.isEmpty(cellIdentity.getOperatorAlphaShort()))
                    operator = cellIdentity.getOperatorAlphaShort();

                generation = "GSM";
            }
        } else {
            if (cellInfo instanceof CellInfoLte) {
                generation = "LTE";
            } else {
                generation = "GSM";
            }
        }

        return context.getString(R.string.cell_header, operator, generation);
    }

    /** Extract MCC from given CellInfo
     * @param cellInfo CellInfo object
     * @return String representation of MCC
     */
    public String getMcc(CellInfo cellInfo) {
        String mcc;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            // Build version >= 28

            if (cellInfo instanceof CellInfoLte) {
                CellIdentityLte cellIdentity = ((CellInfoLte) cellInfo).getCellIdentity();
                mcc = cellIdentity.getMccString();
            } else {
                CellIdentityGsm cellIdentity = ((CellInfoGsm) cellInfo).getCellIdentity();
                mcc = cellIdentity.getMccString();
            }
        } else {
            // Build version < 28
            if (cellInfo instanceof CellInfoLte) {
                CellIdentityLte cellIdentity = ((CellInfoLte) cellInfo).getCellIdentity();
                mcc = Integer.toString(cellIdentity.getMcc());
            } else {
                CellIdentityGsm cellIdentity = ((CellInfoGsm) cellInfo).getCellIdentity();
                mcc = Integer.toString(cellIdentity.getMcc());
            }
        }

        return mcc;
    }

    /** Extract MNC from given CellInfo
     * @param cellInfo CellInfo object
     * @return String representation of MNC
     */
    public String getMnc(CellInfo cellInfo) {
        String mnc;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            // Build version >= 28

            if (cellInfo instanceof CellInfoLte) {
                CellIdentityLte cellIdentity = ((CellInfoLte) cellInfo).getCellIdentity();
                mnc = cellIdentity.getMncString();
            } else {
                CellIdentityGsm cellIdentity = ((CellInfoGsm) cellInfo).getCellIdentity();
                mnc = cellIdentity.getMncString();
            }
        } else {
            // Build version < 28
            if (cellInfo instanceof CellInfoLte) {
                CellIdentityLte cellIdentity = ((CellInfoLte) cellInfo).getCellIdentity();
                mnc = Integer.toString(cellIdentity.getMnc());
            } else {
                CellIdentityGsm cellIdentity = ((CellInfoGsm) cellInfo).getCellIdentity();
                mnc = Integer.toString(cellIdentity.getMnc());
            }
        }

        return mnc;
    }
}
