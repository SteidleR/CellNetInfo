package xyz.steidle.cellularnetworkmapper.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.telephony.CellInfo;

import androidx.core.util.Pair;

import java.util.ArrayList;
import java.util.List;

import xyz.steidle.cellularnetworkmapper.R;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "cellsHistory";
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

    CellParser cellParser;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        cellParser = new CellParser(context);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String stringValue = " INTEGER,";

        String createCellsTableSql = "CREATE TABLE " + TABLE_CELLS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_STANDARD + " TEXT,"
                + KEY_PROVIDER + " TEXT,"
                + KEY_MCC + stringValue
                + KEY_MNC + stringValue
                + KEY_TAC + stringValue
                + KEY_LAC + stringValue
                + KEY_CID + stringValue
                + KEY_PCI + stringValue
                + KEY_SIGNAL + stringValue
                + KEY_TIMESTAMP + " TEXT" + ")";

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
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_STANDARD, cellParser.getGeneration(cellInfo));
        values.put(KEY_PROVIDER, cellParser.getProvider(cellInfo));
        values.put(KEY_MCC, cellParser.getMcc(cellInfo));
        values.put(KEY_MNC, cellParser.getMnc(cellInfo));

        Pair<Integer, Integer> lacTac = cellParser.getLacTac(cellInfo);
        if (lacTac.first.equals(R.string.cell_tac))
            values.put(KEY_LAC, lacTac.second);
        else
            values.put(KEY_TAC, lacTac.second);

        values.put(KEY_CID, cellParser.getCellId(cellInfo));
        values.put(KEY_PCI, cellParser.getPci(cellInfo));
        values.put(KEY_SIGNAL, cellParser.getSignalStrength(cellInfo));
        values.put(KEY_TIMESTAMP, String.valueOf(System.currentTimeMillis() / 1000));

        db.insert(TABLE_CELLS, null, values);
        db.close();
    }

    /** Returns all cells stored in database table as string
     * @return List of Strings containing representation of data rows
     */
    public List<String> getAllCells() {
        List<String> contactList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CELLS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                String cell = cursor.getString(10) + " "
                        + cursor.getString(1) + " "
                        + cursor.getString(2) + " "
                        + cursor.getString(3) + " "
                        + cursor.getString(4) + " "
                        + cursor.getString(5) + " "
                        + cursor.getString(6) + " "
                        + cursor.getString(7) + " "
                        + cursor.getString(8) + " "
                        + cursor.getString(9) + " ";
                contactList.add(cell);
            } while (cursor.moveToNext());
        }

        return contactList;
    }

}
