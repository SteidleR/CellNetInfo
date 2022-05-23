package xyz.steidle.cellnetinfo.utils;

import android.os.Build;
import android.telephony.CellIdentity;
import android.telephony.CellIdentityCdma;
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
import android.telephony.CellSignalStrength;
import android.text.TextUtils;

import androidx.core.util.Pair;

import xyz.steidle.cellnetinfo.R;

/**
 * Utility functions to parse CellInfo objects
 */
public class CellParser {

    static int VERSION_SDK_INT = Build.VERSION.SDK_INT;

    /** CellParser constructor, throws IllegalStateException because class is not intended to use as non-static class  */
    CellParser() {
        throw new IllegalStateException("Utility class");
    }

    /** Return generation of cell as string
     * @param cellInfo CellInfo object
     * @return String, generation of cell net
     */
    public static String getGeneration(CellInfo cellInfo) {
        String generation = "Unknown";
        if (VERSION_SDK_INT >= Build.VERSION_CODES.R && cellInfo instanceof CellInfoNr) {
            generation = "NR";
        } else if (VERSION_SDK_INT >= Build.VERSION_CODES.Q && cellInfo instanceof CellInfoTdscdma) {
            generation = "TDSCDMA";
        } else if (cellInfo instanceof CellInfoLte) {
            generation = "LTE";
        } else if (cellInfo instanceof CellInfoGsm) {
            generation = "GSM";
        } else if (cellInfo instanceof CellInfoWcdma) {
            generation = "WCDMA";
        } else if (cellInfo instanceof CellInfoCdma) {
            generation = "CDMA";
        }

        return generation;
    }

    /** Return provider of cell network as string
     * @param cellInfo CellInfo object
     * @return String, provider of cell net
     */
    public static String getProvider(CellInfo cellInfo) {
        CellIdentity cellIdentity = null;
        String provider = "Unknown";

        if (VERSION_SDK_INT >= Build.VERSION_CODES.P) {
            if (VERSION_SDK_INT >= Build.VERSION_CODES.R && cellInfo instanceof CellInfoNr) {
                // Build version >= 30 and 5G network
                cellIdentity = cellInfo.getCellIdentity();
            } else if (cellInfo instanceof CellInfoLte) {
                cellIdentity = ((CellInfoLte) cellInfo).getCellIdentity();
            } else if (cellInfo instanceof CellInfoGsm) {
                cellIdentity = ((CellInfoGsm) cellInfo).getCellIdentity();
            } else if (cellInfo instanceof CellInfoCdma) {
                cellIdentity = ((CellInfoCdma) cellInfo).getCellIdentity();
            } else if (cellInfo instanceof CellInfoWcdma) {
                cellIdentity = ((CellInfoWcdma) cellInfo).getCellIdentity();
            }

            if (cellIdentity != null && !TextUtils.isEmpty(cellIdentity.getOperatorAlphaShort()))
                provider = (String) cellIdentity.getOperatorAlphaShort();
        }

        return provider;
    }

    /** Extract MCC from given CellInfo
     * @param cellInfo CellInfo object
     * @return String representation of MCC
     */
    public static String getMcc(CellInfo cellInfo) {
        String mcc = "";

        if (VERSION_SDK_INT >= Build.VERSION_CODES.P) {
            // Build version >= 28
            if (VERSION_SDK_INT >= Build.VERSION_CODES.R && cellInfo instanceof CellInfoNr) {
                // Build version >= 30 and 5G network
                CellIdentityNr cellIdentity = (CellIdentityNr) cellInfo.getCellIdentity();
                mcc = cellIdentity.getMccString();
            } else if (VERSION_SDK_INT >= Build.VERSION_CODES.Q && cellInfo instanceof CellInfoTdscdma) {
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
            int intMcc = 0;

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

        if (mcc == null)
            mcc = "";

        return mcc;
    }

    /** Extract MNC from given CellInfo
     * @param cellInfo CellInfo object
     * @return String representation of MNC
     */
    public static String getMnc(CellInfo cellInfo) {
        String mnc = "";

        if (VERSION_SDK_INT >= Build.VERSION_CODES.P) {
            // Build version >= 28
            if (VERSION_SDK_INT >= Build.VERSION_CODES.R && cellInfo instanceof CellInfoNr) {
                // Build version >= 30 and 5G network
                CellIdentityNr cellIdentity = (CellIdentityNr) cellInfo.getCellIdentity();
                mnc = cellIdentity.getMncString();
            } else if (VERSION_SDK_INT >= Build.VERSION_CODES.Q && cellInfo instanceof CellInfoTdscdma) {
                // Build version >= 29 and TDSCDMA network
                CellIdentityTdscdma cellIdentity = ((CellInfoTdscdma) cellInfo).getCellIdentity();
                mnc = cellIdentity.getMncString();
            } else if (cellInfo instanceof CellInfoLte) {
                CellIdentityLte cellIdentity = ((CellInfoLte) cellInfo).getCellIdentity();
                mnc = cellIdentity.getMncString();
            } else if (cellInfo instanceof CellInfoGsm) {
                CellIdentityGsm cellIdentity = ((CellInfoGsm) cellInfo).getCellIdentity();
                mnc = cellIdentity.getMncString();
            } else if (cellInfo instanceof CellInfoWcdma) {
                CellIdentityWcdma cellIdentity = ((CellInfoWcdma) cellInfo).getCellIdentity();
                mnc = cellIdentity.getMncString();
            }
        } else {
            if (cellInfo instanceof CellInfoLte) {
                CellIdentityLte cellIdentity = ((CellInfoLte) cellInfo).getCellIdentity();
                mnc = Integer.toString(cellIdentity.getMnc());
            } else if (cellInfo instanceof CellInfoGsm) {
                CellIdentityGsm cellIdentity = ((CellInfoGsm) cellInfo).getCellIdentity();
                mnc = Integer.toString(cellIdentity.getMnc());
            } else if (cellInfo instanceof CellInfoWcdma) {
                CellIdentityWcdma cellIdentity = ((CellInfoWcdma) cellInfo).getCellIdentity();
                mnc = Integer.toString(cellIdentity.getMnc());
            }
        }

        return mnc;
    }

    /** Extract LAC or TAC from given CellInfo
     * @param cellInfo CellInfo object
     * @return Pair containing resource id for row name and Lac/ Tac of cell
     */
    public static Pair<Integer, Integer> getLacTac(CellInfo cellInfo) {
        int code = 0;
        int displayName = R.string.cell_lac;

        if (VERSION_SDK_INT >= Build.VERSION_CODES.R && cellInfo instanceof CellInfoNr) {
            // Build version >= 30 and 5G network
            CellIdentityNr cellIdentity = (CellIdentityNr) cellInfo.getCellIdentity();
            code = cellIdentity.getTac();
            displayName = R.string.cell_tac;
        } else if (VERSION_SDK_INT >= Build.VERSION_CODES.Q && cellInfo instanceof CellInfoTdscdma) {
            // Build version >= 29 and TDSCDMA network
            CellIdentityTdscdma cellIdentity = ((CellInfoTdscdma) cellInfo).getCellIdentity();
            code = cellIdentity.getLac();
        } else if (cellInfo instanceof CellInfoLte) {
            CellIdentityLte cellIdentity = ((CellInfoLte) cellInfo).getCellIdentity();
            code = cellIdentity.getTac();
            displayName = R.string.cell_tac;
        } else if (cellInfo instanceof CellInfoGsm) {
            CellIdentityGsm cellIdentity = ((CellInfoGsm) cellInfo).getCellIdentity();
            code = cellIdentity.getLac();
        } else if (cellInfo instanceof CellInfoWcdma) {
            CellIdentityWcdma cellIdentity = ((CellInfoWcdma) cellInfo).getCellIdentity();
            code = cellIdentity.getLac();
        }

        return new Pair<>(displayName, code);
    }

    /** Extract CI or CID from given CellInfo
     * @param cellInfo CellInfo object
     * @return Long representing cell id
     */
    public static long getCellId(CellInfo cellInfo) {
        long cid = 0;

        if (VERSION_SDK_INT >= Build.VERSION_CODES.R && cellInfo instanceof CellInfoNr) {
            // Build version >= 30 and 5G network
            CellIdentityNr cellIdentity = (CellIdentityNr) cellInfo.getCellIdentity();
            cid = cellIdentity.getNci();
        } else if (VERSION_SDK_INT >= Build.VERSION_CODES.Q && cellInfo instanceof CellInfoTdscdma) {
            // Build version >= 29 and TDSCDMA network
            CellIdentityTdscdma cellIdentity = ((CellInfoTdscdma) cellInfo).getCellIdentity();
            cid = cellIdentity.getCid();
        } else if (cellInfo instanceof CellInfoLte) {
            CellIdentityLte cellIdentity = ((CellInfoLte) cellInfo).getCellIdentity();
            cid = cellIdentity.getCi();
        } else if (cellInfo instanceof CellInfoGsm) {
            CellIdentityGsm cellIdentity = ((CellInfoGsm) cellInfo).getCellIdentity();
            cid = cellIdentity.getCid();
        } else if (cellInfo instanceof CellInfoWcdma) {
            CellIdentityWcdma cellIdentity = ((CellInfoWcdma) cellInfo).getCellIdentity();
            cid = cellIdentity.getCid();
        }

        // ToDo: add CDMA?

        return cid;
    }

    /** Extract Physical Cell ID from LTE or 5G (Nr) CellInfo
     * @param cellInfo CellInfo object
     * @return integer, -1 if not supported | range 0-503 for LTE | range 0-1007 for 5G
     */
    public static int getPci(CellInfo cellInfo) {
        int pci = -1;

        if (VERSION_SDK_INT >= Build.VERSION_CODES.R && cellInfo instanceof CellInfoNr) {
            // Build version >= 30 and 5G network
            CellIdentityNr cellIdentity = (CellIdentityNr) cellInfo.getCellIdentity();
            pci = (int) cellIdentity.getPci();
        } else if (cellInfo instanceof CellInfoLte) {
            CellIdentityLte cellIdentity = ((CellInfoLte) cellInfo).getCellIdentity();
            pci = cellIdentity.getPci();
        }

        return pci;
    }

    /** Retrieves signal strength of cell from 0-4
     * @param cellInfo CellInfo object
     * @return integer, 0-4 representing singal strength, no signal: 0 | best signal: 4
     */
    public static int getSignalStrength(CellInfo cellInfo) {
        int signalStrength = 0;

        CellSignalStrength cellSignalStrength = getCellSignalStrength(cellInfo);

        if (cellSignalStrength != null)
            signalStrength = cellSignalStrength.getLevel();

        return signalStrength;
    }

    /** Returns the signal strength of cell in dBm
     * @param cellInfo CellInfo object
     * @return integer, dBm signal strength of cell
     */
    public static int getSignalDbm(CellInfo cellInfo) {
        int signalDbm = 0;

        CellSignalStrength cellSignalStrength = getCellSignalStrength(cellInfo);

        if (cellSignalStrength != null)
            signalDbm = cellSignalStrength.getDbm();

        return signalDbm;
    }

    /** Gets the CellSignalStrength object of the cell object
     * @param cellInfo CellInfo object
     * @return CellSignalStrength object
     */
    public static CellSignalStrength getCellSignalStrength(CellInfo cellInfo) {
        if (VERSION_SDK_INT >= Build.VERSION_CODES.R) {
            // Build version >= 30 and 5G network
            return cellInfo.getCellSignalStrength();
        } else if (VERSION_SDK_INT >= Build.VERSION_CODES.Q && cellInfo instanceof CellInfoTdscdma) {
            // Build version >= 29 and TDSCDMA network
            return ((CellInfoTdscdma) cellInfo).getCellSignalStrength();
        } else if (cellInfo instanceof CellInfoLte) {
            return ((CellInfoLte) cellInfo).getCellSignalStrength();
        } else if (cellInfo instanceof CellInfoGsm) {
            return ((CellInfoGsm) cellInfo).getCellSignalStrength();
        } else if (cellInfo instanceof CellInfoWcdma) {
            return ((CellInfoWcdma) cellInfo).getCellSignalStrength();
        } else if (cellInfo instanceof CellInfoCdma) {
            return ((CellInfoCdma) cellInfo).getCellSignalStrength();
        } else {
            return null;
        }
    }
}
