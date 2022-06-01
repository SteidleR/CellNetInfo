package xyz.steidle.cellnetinfo.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.telephony.CellInfo;
import android.util.Log;

import androidx.core.util.Pair;

import java.util.ArrayList;
import java.util.List;

import xyz.steidle.cellnetinfo.R;

/**
 * Class to handle database creation, insert, update
 */
public class DatabaseHandler extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 8;
    public static final String DATABASE_NAME = "cellsHistory";

    private static final String TABLE_CELLS = "cells";

    private static final String KEY_ID = "id";
    private static final String KEY_STANDARD = "standard";
    private static final String KEY_PROVIDER = "provider";
    private static final String KEY_MCC = "mcc";
    private static final String KEY_MNC = "mnc";
    private static final String KEY_TAC = "tac";
    private static final String KEY_LAC = "lac";
    private static final String KEY_CID = "cid";
    private static final String KEY_PCI = "pci";
    private static final String KEY_SIGNAL = "signal";
    private static final String KEY_TIMESTAMP = "timestamp";
    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_LONGITUDE = "longitude";

    private static final int[] columnsGroupedQuery = {10, 1, 2, 3, 4, 5, 6, 7, 8};
    private static final int[] columnsCsvQuery = {10, 1, 2, 3, 4, 5, 6, 7, 8, 9, 11, 12};
    private static final int[] columnsFilteredQuery = {10, 11, 12};

    DataHolder dataHolder;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        dataHolder = DataHolder.getInstance();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String integerValue = " INTEGER";
        String textValue = " TEXT";
        String realValue = " REAL";

        String createCellsTableSql = "CREATE TABLE " + TABLE_CELLS + "("
                + KEY_ID        + integerValue  + " PRIMARY KEY,"
                + KEY_STANDARD  + textValue     + ","
                + KEY_PROVIDER  + textValue     + ","
                + KEY_MCC       + integerValue  + ","
                + KEY_MNC       + integerValue  + ","
                + KEY_TAC       + integerValue  + ","
                + KEY_LAC       + integerValue  + ","
                + KEY_CID       + integerValue  + ","
                + KEY_PCI       + integerValue  + ","
                + KEY_SIGNAL    + integerValue  + ","
                + KEY_TIMESTAMP + textValue     + ","
                + KEY_LATITUDE  + realValue     + ","
                + KEY_LONGITUDE + realValue     + ")";

        Log.d("DatabaseHandler", createCellsTableSql);

        sqLiteDatabase.execSQL(createCellsTableSql);
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
        insertCell(db, cellInfo, location);
    }

    /** Inserts new cell into database, assumes all valid
     * @param db
     * @param cellInfo
     * @param location
     */
    protected void insertCell(SQLiteDatabase db, CellInfo cellInfo, Location location) {
        ContentValues values = new ContentValues();

        values.put(KEY_STANDARD, CellParser.getGeneration(cellInfo));
        values.put(KEY_PROVIDER, CellParser.getProvider(cellInfo));
        values.put(KEY_MCC, CellParser.getMcc(cellInfo));
        values.put(KEY_MNC, CellParser.getMnc(cellInfo));

        Pair<Integer, Integer> lacTac = CellParser.getLacTac(cellInfo);
        if (lacTac.first.equals(R.string.cell_tac))
            values.put(KEY_LAC, lacTac.second);
        else
            values.put(KEY_TAC, lacTac.second);

        values.put(KEY_CID, CellParser.getCellId(cellInfo));
        values.put(KEY_PCI, CellParser.getPci(cellInfo));
        values.put(KEY_SIGNAL, CellParser.getSignalStrength(cellInfo));
        values.put(KEY_TIMESTAMP, String.valueOf(System.currentTimeMillis() / 1000));

        values.put(KEY_LATITUDE, location.getLatitude());
        values.put(KEY_LONGITUDE, location.getLongitude());

        db.insert(TABLE_CELLS, null, values);
        db.close();
    }

    /** Returns all unique cells stored in database table as a list of string arrays
     * @return List of String arrays representing database rows
     */
    public List<String[]> getAllCellsGrouped() {
        String selectQuery = "SELECT * FROM " + TABLE_CELLS + " GROUP BY " + KEY_MCC + "," + KEY_MNC + "," + KEY_LAC + "," + KEY_TAC + ";";
        return getCellsFromCursor(createCursorFromQuery(selectQuery), columnsGroupedQuery);
    }

    /** Returns all cells stored in database table as a list of string arrays
     * @return List of String arrays representing database rows
     */
    public List<String[]> getAllCellsCsv() {
        String selectQuery = "SELECT  * FROM " + TABLE_CELLS;
        return getCellsFromCursor(createCursorFromQuery(selectQuery), columnsCsvQuery);
    }

    public List<String[]> getCellsFiltered(String standard, String provider, int mcc, int mnc, int tac, int lac) {
        String selectQuery = "SELECT * FROM " + TABLE_CELLS + " WHERE " +
                KEY_STANDARD + " LIKE '" + standard + "' AND " +
                KEY_PROVIDER + " LIKE '" + provider + "' AND " +
                KEY_MCC + " = " + mcc + " AND " +
                KEY_MNC + " = " + mnc +
                (lac != -1 ? " AND " + KEY_LAC + " = " + lac : "") +
                (tac != -1 ? " AND " + KEY_TAC + " = " + tac : "") + ";";

        return getCellsFromCursor(createCursorFromQuery(selectQuery), columnsFilteredQuery);
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
