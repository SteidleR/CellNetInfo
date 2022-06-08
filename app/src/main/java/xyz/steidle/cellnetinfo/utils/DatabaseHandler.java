package xyz.steidle.cellnetinfo.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.os.Build;
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
import android.telephony.CellSignalStrengthCdma;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthNr;
import android.telephony.CellSignalStrengthTdscdma;
import android.telephony.CellSignalStrengthWcdma;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to handle database creation, insert, update
 */
public class DatabaseHandler extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 9;
    public static final String DATABASE_NAME = "cellsHistory";

    private static final String TABLE_CELLS = "cells";
    private static final String TABLE_CDMA = "cdma_cells";
    private static final String TABLE_NR = "nr_cells";
    private static final String TABLE_LTE = "lte_cells";
    private static final String TABLE_GSM = "gsm_cells";
    private static final String TABLE_TDSCDMA = "tdscdma_cells";
    private static final String TABLE_WCDMA = "wcdma_cells";

    private static final String KEY_ID = "id";
    private static final String KEY_STANDARD = "standard";
    private static final String KEY_PROVIDER = "provider";
    private static final String KEY_MCC = "mcc";
    private static final String KEY_MNC = "mnc";
    private static final String KEY_TAC = "tac";
    private static final String KEY_LAC = "lac";
    private static final String KEY_CID = "cid";
    private static final String KEY_PCI = "pci";
    private static final String KEY_NRARFCN = "nrarfcn";
    private static final String KEY_CSIRSRP = "csirsrp";
    private static final String KEY_CSIRSRQ = "csirsrq";
    private static final String KEY_CSISINR = "csisinr";
    private static final String KEY_SSRSRP = "ssrsrp";
    private static final String KEY_SSRSRQ = "ssrsrq";
    private static final String KEY_SSSINR = "sssinr";
    private static final String KEY_BANDWIDTH = "bandwidth";
    private static final String KEY_RSRP = "rsrp";
    private static final String KEY_RSRQ = "rsrq";
    private static final String KEY_RSSI = "rssi";
    private static final String KEY_CQI = "cqi";
    private static final String KEY_ARFCN = "arfcn";
    private static final String KEY_BSIC = "bsic";
    private static final String KEY_BITERROR = "biterrorrate";
    private static final String KEY_UARFCN = "uarfcn";
    private static final String KEY_RSCP = "rscp";
    private static final String KEY_PSC = "psc";
    private static final String KEY_ECNO = "ecno";
    private static final String KEY_SYS_ID = "sysId";
    private static final String KEY_SIGNAL = "signal";
    private static final String KEY_TIMESTAMP = "timestamp";
    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_LONGITUDE = "longitude";

    private static final String INTEGER_VALUE = " INTEGER";
    private static final String TEXT_VALUE = " TEXT";
    private static final String REAL_VALUE = " REAL";

    private static final String LOG_TAG = "DatabaseHandler";

    private static final String BASE_CREATE_TABLE_QUERY = "CREATE TABLE %s ("
            + KEY_ID        + INTEGER_VALUE + " PRIMARY KEY,"
            + KEY_TIMESTAMP + TEXT_VALUE + ","
            + KEY_STANDARD  + TEXT_VALUE + ","
            + KEY_PROVIDER  + TEXT_VALUE + ","
            + KEY_MCC       + INTEGER_VALUE + ","
            + KEY_MNC       + INTEGER_VALUE + ","
            + KEY_SIGNAL    + INTEGER_VALUE + ","
            + KEY_LATITUDE  + REAL_VALUE + ","
            + KEY_LONGITUDE + REAL_VALUE + ",";

    private static final String KEY_NET_ID = "netId";
    private static final String KEY_BASE_ID = "baseId";
    private static final String KEY_BASE_LAT = "latitudeBase";
    private static final String KEY_BASE_LONG = "longitudeBase";
    private static final String KEY_CDMA_RSSI = "cdmaRssi";
    private static final String KEY_EVDO_RSSI = "evdoRssi";
    private static final String KEY_EVDO_SNR = "evdoSnr";

    DataHolder dataHolder;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        dataHolder = DataHolder.getInstance();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        createNrTable(sqLiteDatabase);
        createLteTable(sqLiteDatabase);
        createGsmTable(sqLiteDatabase);
        createCdmaTable(sqLiteDatabase);
        createTdscdmaTable(sqLiteDatabase);
        createWcdmaTable(sqLiteDatabase);
    }

    protected void createNrTable(SQLiteDatabase sqLiteDatabase) {

        String createNrTableSql = String.format(BASE_CREATE_TABLE_QUERY, TABLE_NR)
                + KEY_TAC       + INTEGER_VALUE + ","
                + KEY_CID       + INTEGER_VALUE + ","
                + KEY_PCI       + INTEGER_VALUE + ","
                + KEY_NRARFCN   + INTEGER_VALUE + ","
                + KEY_CSIRSRP   + INTEGER_VALUE + ","
                + KEY_CSIRSRQ   + INTEGER_VALUE + ","
                + KEY_CSISINR   + INTEGER_VALUE + ","
                + KEY_SSRSRP    + INTEGER_VALUE + ","
                + KEY_SSRSRQ    + INTEGER_VALUE + ","
                + KEY_SSSINR    + INTEGER_VALUE + ")";

        Log.d(LOG_TAG, createNrTableSql);
        sqLiteDatabase.execSQL(createNrTableSql);
    }

    protected void createLteTable(SQLiteDatabase sqLiteDatabase) {
        String createLteTableSql = String.format(BASE_CREATE_TABLE_QUERY, TABLE_LTE)
                + KEY_LAC       + INTEGER_VALUE + ","
                + KEY_CID       + INTEGER_VALUE + ","
                + KEY_PCI       + INTEGER_VALUE + ","
                + KEY_BANDWIDTH + INTEGER_VALUE + ","
                + KEY_RSRP   + INTEGER_VALUE + ","
                + KEY_RSRQ   + INTEGER_VALUE + ","
                + KEY_RSSI   + INTEGER_VALUE + ","
                + KEY_CQI    + INTEGER_VALUE + ")";

        Log.d(LOG_TAG, createLteTableSql);
        sqLiteDatabase.execSQL(createLteTableSql);
    }

    protected void createGsmTable(SQLiteDatabase sqLiteDatabase) {
        String createGsmTableSql = String.format(BASE_CREATE_TABLE_QUERY, TABLE_GSM)
                + KEY_LAC       + INTEGER_VALUE + ","
                + KEY_CID       + INTEGER_VALUE + ","
                + KEY_PCI       + INTEGER_VALUE + ","
                + KEY_ARFCN     + INTEGER_VALUE + ","
                + KEY_BSIC      + INTEGER_VALUE + ","
                + KEY_BITERROR  + INTEGER_VALUE + ","
                + KEY_RSSI      + INTEGER_VALUE + ")";

        Log.d(LOG_TAG, createGsmTableSql);
        sqLiteDatabase.execSQL(createGsmTableSql);
    }

    protected void createCdmaTable(SQLiteDatabase sqLiteDatabase) {
        String createCdmaTableSql = "CREATE TABLE " + TABLE_CDMA +" ("
                + KEY_ID        + INTEGER_VALUE + " PRIMARY KEY,"
                + KEY_TIMESTAMP + TEXT_VALUE + ","
                + KEY_STANDARD  + TEXT_VALUE + ","
                + KEY_PROVIDER  + TEXT_VALUE + ","
                + KEY_SIGNAL    + INTEGER_VALUE + ","
                + KEY_LATITUDE  + REAL_VALUE + ","
                + KEY_LONGITUDE + REAL_VALUE + ","
                + KEY_SYS_ID + INTEGER_VALUE + ","
                + KEY_NET_ID + INTEGER_VALUE + ","
                + KEY_BASE_ID + INTEGER_VALUE + ","
                + KEY_BASE_LAT + INTEGER_VALUE + ","
                + KEY_BASE_LONG + INTEGER_VALUE + ","
                + KEY_CDMA_RSSI + INTEGER_VALUE + ","
                + KEY_EVDO_RSSI + INTEGER_VALUE + ","
                + KEY_EVDO_SNR + INTEGER_VALUE + ")";

        Log.d(LOG_TAG, createCdmaTableSql);
        sqLiteDatabase.execSQL(createCdmaTableSql);
    }

    protected void createTdscdmaTable(SQLiteDatabase sqLiteDatabase) {
        String createTdscdmaTableSql = String.format(BASE_CREATE_TABLE_QUERY, TABLE_TDSCDMA)
                + KEY_LAC       + INTEGER_VALUE + ","
                + KEY_CID       + INTEGER_VALUE + ","
                + KEY_PCI       + INTEGER_VALUE + ","
                + KEY_UARFCN    + INTEGER_VALUE + ","
                + KEY_RSCP      + INTEGER_VALUE + ")";

        Log.d(LOG_TAG, createTdscdmaTableSql);
        sqLiteDatabase.execSQL(createTdscdmaTableSql);
    }

    protected void createWcdmaTable(SQLiteDatabase sqLiteDatabase) {
        String createWcdmaTableSql = String.format(BASE_CREATE_TABLE_QUERY, TABLE_WCDMA)
                + KEY_LAC       + INTEGER_VALUE + ","
                + KEY_CID       + INTEGER_VALUE + ","
                + KEY_PCI       + INTEGER_VALUE + ","
                + KEY_UARFCN    + INTEGER_VALUE + ","
                + KEY_PSC       + INTEGER_VALUE + ","
                + KEY_ECNO      + INTEGER_VALUE + ")";

        Log.d(LOG_TAG, createWcdmaTableSql);
        sqLiteDatabase.execSQL(createWcdmaTableSql);
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Drop older table if existed
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_CELLS);

        // Create tables again
        onCreate(sqLiteDatabase);
    }

    /** Creates a new SQLite Table row from CellInfo
     * @param cellInfo CellInfo object
     */
    public void addCell(CellInfo cellInfo) {
        Location location = dataHolder.getLocation();

        if (cellInfo == null || location == null || !isValidCell(cellInfo))
            return;

        SQLiteDatabase db = this.getWritableDatabase();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && cellInfo instanceof CellInfoNr) {
            insertNrCell(db, (CellInfoNr) cellInfo, location);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && cellInfo instanceof CellInfoTdscdma) {
            insertTdscdmaCell(db, (CellInfoTdscdma) cellInfo, location);
        } else if (cellInfo instanceof CellInfoLte) {
            insertLteCell(db, (CellInfoLte) cellInfo, location);
        } else if (cellInfo instanceof CellInfoGsm) {
            insertGsmCell(db, (CellInfoGsm) cellInfo, location);
        } else if (cellInfo instanceof CellInfoCdma) {
            insertCdmaCell(db, (CellInfoCdma) cellInfo, location);
        } else if (cellInfo instanceof CellInfoWcdma) {
            insertWcdmaCell(db, (CellInfoWcdma) cellInfo, location);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    protected void insertNrCell(@NonNull SQLiteDatabase db, CellInfoNr cellInfo, @NonNull Location location) {
        ContentValues values = new ContentValues();

        addBaseValues(values, location, cellInfo);

        values.put(KEY_TAC, CellParser.getLacTac(cellInfo).second);

        CellSignalStrengthNr cellSignalStrengthNr = (CellSignalStrengthNr) cellInfo.getCellSignalStrength();
        CellIdentityNr cellIdentityNr = (CellIdentityNr) cellInfo.getCellIdentity();

        values.put(KEY_CID, CellParser.getCellId(cellInfo));
        values.put(KEY_PCI, CellParser.getPci(cellInfo));
        values.put(KEY_NRARFCN, cellIdentityNr.getNrarfcn());
        values.put(KEY_CSIRSRP, cellSignalStrengthNr.getCsiRsrp());
        values.put(KEY_CSIRSRQ, cellSignalStrengthNr.getCsiRsrq());
        values.put(KEY_CSISINR, cellSignalStrengthNr.getCsiSinr());
        values.put(KEY_SSRSRP, cellSignalStrengthNr.getSsRsrp());
        values.put(KEY_SSRSRQ, cellSignalStrengthNr.getSsRsrq());
        values.put(KEY_SSSINR, cellSignalStrengthNr.getSsSinr());

        db.insert(TABLE_NR, null, values);
        db.close();
    }

    protected void insertLteCell(@NonNull SQLiteDatabase db, CellInfoLte cellInfo, @NonNull Location location) {
        ContentValues values = new ContentValues();

        addBaseValues(values, location, cellInfo);

        values.put(KEY_LAC, CellParser.getLacTac(cellInfo).second);
        values.put(KEY_CID, CellParser.getCellId(cellInfo));
        values.put(KEY_PCI, CellParser.getPci(cellInfo));

        CellSignalStrengthLte cellSignalStrengthLte = cellInfo.getCellSignalStrength();
        CellIdentityLte cellIdentityLte = cellInfo.getCellIdentity();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            values.put(KEY_BANDWIDTH, cellIdentityLte.getBandwidth());
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            values.put(KEY_RSRP, cellSignalStrengthLte.getRsrp());
            values.put(KEY_RSRQ, cellSignalStrengthLte.getRsrq());
            values.put(KEY_CQI, cellSignalStrengthLte.getCqi());
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.put(KEY_RSSI, cellSignalStrengthLte.getRssi());
        }

        db.insert(TABLE_LTE, null, values);
        db.close();
    }

    protected void insertGsmCell(@NonNull SQLiteDatabase db, CellInfoGsm cellInfo, @NonNull Location location) {
        ContentValues values = new ContentValues();

        addBaseValues(values, location, cellInfo);

        values.put(KEY_LAC, CellParser.getLacTac(cellInfo).second);
        values.put(KEY_CID, CellParser.getCellId(cellInfo));
        values.put(KEY_PCI, CellParser.getPci(cellInfo));

        CellSignalStrengthGsm cellSignalStrengthGsm = cellInfo.getCellSignalStrength();
        CellIdentityGsm cellIdentityGsm = cellInfo.getCellIdentity();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            values.put(KEY_ARFCN, cellIdentityGsm.getArfcn());
            values.put(KEY_BSIC, cellIdentityGsm.getBsic());
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.put(KEY_BITERROR, cellSignalStrengthGsm.getBitErrorRate());
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            values.put(KEY_RSSI, cellSignalStrengthGsm.getRssi());
        }

        db.insert(TABLE_GSM, null, values);
        db.close();
    }

    protected void insertCdmaCell(@NonNull SQLiteDatabase db, CellInfoCdma cellInfo, @NonNull Location location) {
        ContentValues values = new ContentValues();

        values.put(KEY_SIGNAL, CellParser.getSignalStrength(cellInfo));
        values.put(KEY_TIMESTAMP, String.valueOf(System.currentTimeMillis() / 1000));
        values.put(KEY_LATITUDE, location.getLatitude());
        values.put(KEY_LONGITUDE, location.getLongitude());
        values.put(KEY_STANDARD, CellParser.getGeneration(cellInfo));
        values.put(KEY_PROVIDER, CellParser.getProvider(cellInfo));

        CellSignalStrengthCdma cellSignalStrengthCdma = cellInfo.getCellSignalStrength();
        CellIdentityCdma cellIdentityCdma = cellInfo.getCellIdentity();

        values.put(KEY_SYS_ID, cellIdentityCdma.getSystemId());
        values.put(KEY_NET_ID, cellIdentityCdma.getNetworkId());
        values.put(KEY_BASE_ID, cellIdentityCdma.getBasestationId());
        values.put(KEY_BASE_LAT, cellIdentityCdma.getLatitude());
        values.put(KEY_BASE_LONG, cellIdentityCdma.getLongitude());
        values.put(KEY_CDMA_RSSI, cellSignalStrengthCdma.getCdmaDbm());
        values.put(KEY_EVDO_RSSI, cellSignalStrengthCdma.getEvdoDbm());
        values.put(KEY_EVDO_SNR, cellSignalStrengthCdma.getEvdoSnr());

        db.insert(TABLE_CDMA, null, values);
        db.close();
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    protected void insertTdscdmaCell(@NonNull SQLiteDatabase db, CellInfoTdscdma cellInfo, @NonNull Location location) {
        ContentValues values = new ContentValues();

        addBaseValues(values, location, cellInfo);

        values.put(KEY_LAC, CellParser.getLacTac(cellInfo).second);
        values.put(KEY_CID, CellParser.getCellId(cellInfo));
        values.put(KEY_PCI, CellParser.getPci(cellInfo));

        CellSignalStrengthTdscdma cellSignalStrength = cellInfo.getCellSignalStrength();
        CellIdentityTdscdma cellIdentity = cellInfo.getCellIdentity();

        values.put(KEY_UARFCN, cellIdentity.getUarfcn());
        values.put(KEY_RSCP, cellSignalStrength.getRscp());

        db.insert(TABLE_TDSCDMA, null, values);
        db.close();
    }

    protected void insertWcdmaCell(@NonNull SQLiteDatabase db, CellInfoWcdma cellInfo, @NonNull Location location) {
        ContentValues values = new ContentValues();

        addBaseValues(values, location, cellInfo);

        values.put(KEY_LAC, CellParser.getLacTac(cellInfo).second);
        values.put(KEY_CID, CellParser.getCellId(cellInfo));
        values.put(KEY_PCI, CellParser.getPci(cellInfo));

        CellSignalStrengthWcdma cellSignalStrength = cellInfo.getCellSignalStrength();
        CellIdentityWcdma cellIdentity = cellInfo.getCellIdentity();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            values.put(KEY_UARFCN, cellIdentity.getUarfcn());
        }

        values.put(KEY_PSC, cellIdentity.getPsc());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            values.put(KEY_ECNO, cellSignalStrength.getEcNo());
        }

        db.insert(TABLE_TDSCDMA, null, values);
        db.close();
    }

    protected void addBaseValues(ContentValues values, Location location, CellInfo cellInfo) {
        values.put(KEY_SIGNAL, CellParser.getSignalStrength(cellInfo));
        values.put(KEY_TIMESTAMP, String.valueOf(System.currentTimeMillis() / 1000));
        values.put(KEY_LATITUDE, location.getLatitude());
        values.put(KEY_LONGITUDE, location.getLongitude());
        values.put(KEY_STANDARD, CellParser.getGeneration(cellInfo));
        values.put(KEY_PROVIDER, CellParser.getProvider(cellInfo));
        values.put(KEY_MCC, CellParser.getMcc(cellInfo));
        values.put(KEY_MNC, CellParser.getMnc(cellInfo));
    }

    public Cursor createCursorFromQuery(String query) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery(query, null);
    }

    /** Returns all cells from cursor with given columns
     * @param cursor Database cursor to fetch cells from
     * @param indices Array of indices of columns to return
     * @return list of cells as String arrays
     */
    public List<String[]> getCellsFromCursor(Cursor cursor, int[] indices) {
        List<String[]> cellList = new ArrayList<>();

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                List<String> row = new ArrayList<>();
                for (int i : indices) {
                    row.add(cursor.getString(i));
                }
                String[] rowArray = new String[indices.length];
                rowArray = row.toArray(rowArray);
                cellList.add(rowArray);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return cellList;
    }

    public List<String[]> getAllNrCells() {
        int[] indices = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18};
        String selectQuery = "SELECT * FROM " + TABLE_NR;
        return getCellsFromCursor(createCursorFromQuery(selectQuery), indices);
    }

    public List<String[]> getAllNrCellsGrouped() {
        int[] indices = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18};
        String selectQuery = "SELECT * FROM " + TABLE_NR + " GROUP BY " + KEY_MCC + "," + KEY_MNC + "," + KEY_CID + "," + KEY_TAC + ";";
        return getCellsFromCursor(createCursorFromQuery(selectQuery), indices);
    }

    public List<String[]> getAllNrLocations(String mcc, String mnc, String cid) {
        int[] indices = {0, 1, 2, 3};
        String selectQuery = "SELECT %s, %s, %s, %s" +
                " FROM " + TABLE_NR + " WHERE " + KEY_MCC + "=%s AND " + KEY_MNC + "=%s AND " + KEY_CID + "=%s;";
        selectQuery = String.format(selectQuery, KEY_TIMESTAMP, KEY_SIGNAL, KEY_LATITUDE, KEY_LONGITUDE, Integer.parseInt(mcc), Integer.parseInt(mnc), Integer.parseInt(cid));
        return getCellsFromCursor(createCursorFromQuery(selectQuery), indices);
    }

    public List<String[]> getAllLteCells() {
        int[] indices = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};
        String selectQuery = "SELECT * FROM " + TABLE_LTE;
        return getCellsFromCursor(createCursorFromQuery(selectQuery), indices);
    }

    public List<String[]> getAllLteCellsGrouped() {
        int[] indices = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};
        String selectQuery = "SELECT * FROM " + TABLE_LTE + " GROUP BY " + KEY_MCC + "," + KEY_MNC + "," + KEY_CID + "," + KEY_LAC + ";";
        return getCellsFromCursor(createCursorFromQuery(selectQuery), indices);
    }

    public List<String[]> getAllLteLocations(String mcc, String mnc, String cid) {
        int[] indices = {0, 1, 2, 3};
        String selectQuery = "SELECT %s, %s, %s, %s" +
                " FROM " + TABLE_LTE + " WHERE " + KEY_MCC + "==%s AND " + KEY_MNC + "==%s AND " + KEY_CID + "==%s;";
        selectQuery = String.format(selectQuery, KEY_TIMESTAMP, KEY_SIGNAL, KEY_LATITUDE, KEY_LONGITUDE, Integer.parseInt(mcc), Integer.parseInt(mnc), Integer.parseInt(cid));
        return getCellsFromCursor(createCursorFromQuery(selectQuery), indices);
    }

    public List<String[]> getAllGsmCells() {
        int[] indices = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
        String selectQuery = "SELECT * FROM " + TABLE_GSM;
        return getCellsFromCursor(createCursorFromQuery(selectQuery), indices);
    }

    public List<String[]> getAllGsmCellsGrouped() {
        int[] indices = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18};
        String selectQuery = "SELECT * FROM " + TABLE_GSM + " GROUP BY " + KEY_MCC + "," + KEY_MNC + "," + KEY_CID + "," + KEY_LAC + ";";
        return getCellsFromCursor(createCursorFromQuery(selectQuery), indices);
    }

    public List<String[]> getAllGsmLocations(String mcc, String mnc, String cid) {
        int[] indices = {0, 1, 2, 3};
        String selectQuery = "SELECT %s, %s, %s, %s" +
                " FROM " + TABLE_GSM + " WHERE " + KEY_MCC + "==%s AND " + KEY_MNC + "==%s AND " + KEY_CID + "==%s;";
        selectQuery = String.format(selectQuery, KEY_TIMESTAMP, KEY_SIGNAL, KEY_LATITUDE, KEY_LONGITUDE, Integer.parseInt(mcc), Integer.parseInt(mnc), Integer.parseInt(cid));
        return getCellsFromCursor(createCursorFromQuery(selectQuery), indices);
    }

    public List<String[]> getAllCdmaCells() {
        int[] indices = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14};
        String selectQuery = "SELECT * FROM " + TABLE_CDMA;
        return getCellsFromCursor(createCursorFromQuery(selectQuery), indices);
    }

    public List<String[]> getAllCdmaCellsGrouped() {
        int[] indices = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18};
        String selectQuery = "SELECT * FROM " + TABLE_CDMA + " GROUP BY " + KEY_SYS_ID + "," + KEY_NET_ID + "," + KEY_BASE_ID + ";";
        return getCellsFromCursor(createCursorFromQuery(selectQuery), indices);
    }

    public List<String[]> getAllTdscdmaCells() {
        int[] indices = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};
        String selectQuery = "SELECT * FROM " + TABLE_TDSCDMA;
        return getCellsFromCursor(createCursorFromQuery(selectQuery), indices);
    }

    public List<String[]> getAllTdscdmaCellsGrouped() {
        int[] indices = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18};
        String selectQuery = "SELECT * FROM " + TABLE_TDSCDMA + " GROUP BY " + KEY_MCC + "," + KEY_MNC + "," + KEY_CID + "," + KEY_LAC + ";";
        return getCellsFromCursor(createCursorFromQuery(selectQuery), indices);
    }

    public List<String[]> getAllTdscdmaLocations(String mcc, String mnc, String cid) {
        int[] indices = {0, 1, 2, 3};
        String selectQuery = "SELECT %s, %s, %s, %s" +
                " FROM " + TABLE_TDSCDMA + " WHERE " + KEY_MCC + "==%s AND " + KEY_MNC + "==%s AND " + KEY_CID + "==%s;";
        selectQuery = String.format(selectQuery, KEY_TIMESTAMP, KEY_SIGNAL, KEY_LATITUDE, KEY_LONGITUDE, Integer.parseInt(mcc), Integer.parseInt(mnc), Integer.parseInt(cid));
        return getCellsFromCursor(createCursorFromQuery(selectQuery), indices);
    }

    public List<String[]> getAllWcdmaCells() {
        int[] indices = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14};
        String selectQuery = "SELECT * FROM " + TABLE_WCDMA;
        return getCellsFromCursor(createCursorFromQuery(selectQuery), indices);
    }

    public List<String[]> getAllWcdmaCellsGrouped() {
        int[] indices = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18};
        String selectQuery = "SELECT * FROM " + TABLE_WCDMA + " GROUP BY " + KEY_MCC + "," + KEY_MNC + "," + KEY_CID + "," + KEY_LAC + ";";
        return getCellsFromCursor(createCursorFromQuery(selectQuery), indices);
    }

    public List<String[]> getAllWcdmaLocations(String mcc, String mnc, String cid) {
        int[] indices = {0, 1, 2, 3};
        String selectQuery = "SELECT %s, %s, %s, %s" +
                " FROM " + TABLE_WCDMA + " WHERE " + KEY_MCC + "==%s AND " + KEY_MNC + "==%s AND " + KEY_CID + "==%s;";
        selectQuery = String.format(selectQuery, KEY_TIMESTAMP, KEY_SIGNAL, KEY_LATITUDE, KEY_LONGITUDE, Integer.parseInt(mcc), Integer.parseInt(mnc), Integer.parseInt(cid));
        return getCellsFromCursor(createCursorFromQuery(selectQuery), indices);
    }

    /** Check if cell is valid or if it contains empty values for mcc, mnc and provider
     * @param cellInfo CellInfo object
     * @return true if cell is valid else false
     */
    protected boolean isValidCell(CellInfo cellInfo) {
        return !CellParser.getProvider(cellInfo).isEmpty() &&
                !CellParser.getMcc(cellInfo).isEmpty() &&
                !CellParser.getMnc(cellInfo).isEmpty();
    }

    /** Clear the history stored in database */
    public void clearCellTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_CELLS;
        db.execSQL(query);
        db.close();
    }
}
