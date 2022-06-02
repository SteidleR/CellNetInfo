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
import android.telephony.CellSignalStrengthGsm;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthTdscdma;
import android.telephony.CellSignalStrengthWcdma;
import android.text.TextUtils;

import androidx.annotation.RequiresApi;
import androidx.core.util.Pair;

import xyz.steidle.cellnetinfo.R;

/**
 * Utility functions to parse CellInfo objects
 */
public class CellParser {

    static int VERSIONSDKINT = Build.VERSION.SDK_INT;

    /** CellParser constructor, throws IllegalStateException because class is not intended to use as non-static class  */
    CellParser() throws IllegalStateException {
        throw new IllegalStateException("Utility class");
    }

    /** Return generation of cell as string
     * @param cellInfo CellInfo object
     * @return String, generation of cell net
     */
    public static String getGeneration(CellInfo cellInfo) {
        String generation = "Unknown";
        if (VERSIONSDKINT >= Build.VERSION_CODES.R && cellInfo instanceof CellInfoNr) {
            generation = "NR";
        } else if (VERSIONSDKINT >= Build.VERSION_CODES.Q && cellInfo instanceof CellInfoTdscdma) {
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
        String provider = "?";

        if (VERSIONSDKINT >= Build.VERSION_CODES.P) {
            if (VERSIONSDKINT >= Build.VERSION_CODES.R && cellInfo instanceof CellInfoNr) {
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

        if (VERSIONSDKINT >= Build.VERSION_CODES.P) {
            // Build version >= 28
            if (VERSIONSDKINT >= Build.VERSION_CODES.R && cellInfo instanceof CellInfoNr) {
                // Build version >= 30 and 5G network
                CellIdentityNr cellIdentity = (CellIdentityNr) cellInfo.getCellIdentity();
                mcc = cellIdentity.getMccString();
            } else if (VERSIONSDKINT >= Build.VERSION_CODES.Q && cellInfo instanceof CellInfoTdscdma) {
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

        if (VERSIONSDKINT >= Build.VERSION_CODES.P) {
            // Build version >= 28
            if (VERSIONSDKINT >= Build.VERSION_CODES.R && cellInfo instanceof CellInfoNr) {
                // Build version >= 30 and 5G network
                CellIdentityNr cellIdentity = (CellIdentityNr) cellInfo.getCellIdentity();
                mnc = cellIdentity.getMncString();
            } else if (VERSIONSDKINT >= Build.VERSION_CODES.Q && cellInfo instanceof CellInfoTdscdma) {
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

        if (mnc == null)
            mnc = "";

        return mnc;
    }

    /** Extract LAC or TAC from given CellInfo
     * @param cellInfo CellInfo object
     * @return Pair containing resource id for row name and Lac/ Tac of cell
     */
    public static Pair<Integer, Integer> getLacTac(CellInfo cellInfo) {
        int code = 0;
        int displayName = R.string.cell_lac;

        if (VERSIONSDKINT >= Build.VERSION_CODES.R && cellInfo instanceof CellInfoNr) {
            // Build version >= 30 and 5G network
            CellIdentityNr cellIdentity = (CellIdentityNr) cellInfo.getCellIdentity();
            code = cellIdentity.getTac();
            displayName = R.string.cell_tac;
        } else if (VERSIONSDKINT >= Build.VERSION_CODES.Q && cellInfo instanceof CellInfoTdscdma) {
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

        if (VERSIONSDKINT >= Build.VERSION_CODES.R && cellInfo instanceof CellInfoNr) {
            // Build version >= 30 and 5G network
            CellIdentityNr cellIdentity = (CellIdentityNr) cellInfo.getCellIdentity();
            cid = cellIdentity.getNci();
        } else if (VERSIONSDKINT >= Build.VERSION_CODES.Q && cellInfo instanceof CellInfoTdscdma) {
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

        return cid;
    }

    /** Extract Physical Cell ID from LTE or 5G (Nr) CellInfo
     * @param cellInfo CellInfo object
     * @return integer, -1 if not supported | range 0-503 for LTE | range 0-1007 for 5G
     */
    public static int getPci(CellInfo cellInfo) {
        int pci = -1;

        if (VERSIONSDKINT >= Build.VERSION_CODES.R && cellInfo instanceof CellInfoNr) {
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
        if (VERSIONSDKINT >= Build.VERSION_CODES.R) {
            // Build version >= 30 and 5G network
            return cellInfo.getCellSignalStrength();
        } else if (VERSIONSDKINT >= Build.VERSION_CODES.Q && cellInfo instanceof CellInfoTdscdma) {
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

    /** Retrieves the latitude and longitude of a CDMA base station
     * @param cellInfoCdma CellInfo object, where Cell is CDMA
     * @return Pair (int, int) : Base station latitude and longitude
     */
    public static Pair<Integer, Integer> getCdmaLocation(CellInfoCdma cellInfoCdma) {
        CellIdentityCdma cellIdentity = cellInfoCdma.getCellIdentity();
        return new Pair<>(cellIdentity.getLatitude(), cellIdentity.getLongitude());
    }

    /** Retrieves base station id of CDMA cell
     * @param cellInfoCdma CellInfo object, where Cell is CDMA
     * @return int : base station id
     */
    public static int getCdmaBaseStationId(CellInfoCdma cellInfoCdma) {
        CellIdentityCdma cellIdentity = cellInfoCdma.getCellIdentity();
        return cellIdentity.getBasestationId();
    }

    /** Retrieves network id of CDMA cell
     * @param cellInfoCdma CellInfo object, where Cell is CDMA
     * @return int : network id
     */
    public static int getCdmaNetworkId(CellInfoCdma cellInfoCdma) {
        CellIdentityCdma cellIdentity = cellInfoCdma.getCellIdentity();
        return cellIdentity.getNetworkId();
    }

    /** Retrieves network id of CDMA cell
     * @param cellInfoCdma CellInfo object, where Cell is CDMA
     * @return int : network id
     */
    public static int getCdmaSystemId(CellInfoCdma cellInfoCdma) {
        CellIdentityCdma cellIdentity = cellInfoCdma.getCellIdentity();
        return cellIdentity.getSystemId();
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static int getNrarfcn(CellInfoNr cellInfoNr) {
        CellIdentityNr cellIdentity = (CellIdentityNr) cellInfoNr.getCellIdentity();
        return cellIdentity.getNrarfcn();
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public static int getBandwidth(CellInfoLte cellInfoLte) {
        CellIdentityLte cellIdentity = cellInfoLte.getCellIdentity();
        return cellIdentity.getBandwidth();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static int getArfcn(CellInfo cellInfo) {
        int arfcn = CellInfo.UNAVAILABLE;
        if (cellInfo instanceof CellInfoGsm) {
            CellIdentityGsm cellIdentityGsm = ((CellInfoGsm) cellInfo).getCellIdentity();
            arfcn = cellIdentityGsm.getArfcn();
        } else if (VERSIONSDKINT >= Build.VERSION_CODES.Q && cellInfo instanceof CellInfoTdscdma) {
            CellIdentityTdscdma cellIdentityTdscdma = ((CellInfoTdscdma) cellInfo).getCellIdentity();
            arfcn = cellIdentityTdscdma.getUarfcn();
        } else if (cellInfo instanceof CellInfoWcdma) {
            CellIdentityWcdma cellIdentityWcdma = ((CellInfoWcdma) cellInfo).getCellIdentity();
            arfcn = cellIdentityWcdma.getUarfcn();
        }

        return arfcn;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static int getBsic(CellInfoGsm cellInfo) {
        CellIdentityGsm cellIdentity = cellInfo.getCellIdentity();
        return cellIdentity.getBsic();
    }

    public static int getPsc(CellInfoWcdma cellInfo) {
        CellIdentityWcdma cellIdentityWcdma = cellInfo.getCellIdentity();
        return cellIdentityWcdma.getPsc();
    }

    public static int getEcNo(CellInfoWcdma cellInfo) {
        if (VERSIONSDKINT >= Build.VERSION_CODES.R) {
            CellSignalStrengthWcdma cellSignalStrengthWcdma = cellInfo.getCellSignalStrength();
            return cellSignalStrengthWcdma.getEcNo();
        } else {
            return CellInfo.UNAVAILABLE;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static int getRscp(CellInfoTdscdma cellInfo) {
        CellSignalStrengthTdscdma cellSignalStrength = cellInfo.getCellSignalStrength();
        return cellSignalStrength.getRscp();
    }

    public static int getBitErrorRate(CellInfoGsm cellInfo) {
        if (VERSIONSDKINT >= Build.VERSION_CODES.Q) {
            CellSignalStrengthGsm cellSignalStrength = cellInfo.getCellSignalStrength();
            return cellSignalStrength.getBitErrorRate();
        } else {
            return Integer.MAX_VALUE;
        }
    }

    public static int getRssi(CellInfo cellInfo) {
        if (cellInfo instanceof CellInfoGsm) {
            CellSignalStrengthGsm cellSignalStrength = ((CellInfoGsm) cellInfo).getCellSignalStrength();
            return cellSignalStrength.getRssi();
        } else if (cellInfo instanceof CellInfoLte) {
            CellSignalStrengthLte cellSignalStrength = ((CellInfoLte) cellInfo).getCellSignalStrength();
            return cellSignalStrength.getRssi();
        }
        return Integer.MAX_VALUE;
    }

    public static int getRsrp(CellInfoLte cellInfo) {
        CellSignalStrengthLte cellSignalStrength = cellInfo.getCellSignalStrength();
        return cellSignalStrength.getRsrp();
    }

    public static int getRsrq(CellInfoLte cellInfo) {
        CellSignalStrengthLte cellSignalStrength = cellInfo.getCellSignalStrength();
        return cellSignalStrength.getRsrq();
    }

    public static int getRssnr(CellInfoLte cellInfo) {
        CellSignalStrengthLte cellSignalStrength = cellInfo.getCellSignalStrength();
        return cellSignalStrength.getRssnr();
    }

    public static int getCqi(CellInfoLte cellInfo) {
        CellSignalStrengthLte cellSignalStrength = cellInfo.getCellSignalStrength();
        return cellSignalStrength.getCqi();
    }
}
