package xyz.steidle.cellnetinfo.utils;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.telephony.CellInfo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;


@RunWith(PowerMockRunner.class)
@PrepareForTest(CellParser.class)
public class DatabaseHandlerTest {
    String onCreateSql = "CREATE TABLE cells(id INTEGER PRIMARY KEY,standard TEXT,provider TEXT,mcc INTEGER,mnc INTEGER,tac INTEGER,lac INTEGER,cid INTEGER,pci INTEGER,signal INTEGER,timestamp TEXT,latitude REAL,longitude REAL)";
    DatabaseHandler databaseHandler;

    @Before
    public void setUp() {
        databaseHandler = new DatabaseHandler(mock(Context.class));
    }

    private void mockCell(CellInfo cellInfo, String returnProvider, String returnMcc, String returnMnc) {
        when(CellParser.getProvider(cellInfo)).thenReturn(returnProvider);
        when(CellParser.getMcc(cellInfo)).thenReturn(returnMcc);
        when(CellParser.getMnc(cellInfo)).thenReturn(returnMnc);
    }

    @Test
    public void isValidCell() {
        CellInfo cellInfo = mock(CellInfo.class);

        PowerMockito.mockStatic(CellParser.class);

        // Empty provider
        mockCell(cellInfo, "", "123", "321");
        databaseHandler.isValidCell(cellInfo);

        // empty MCC
        mockCell(cellInfo, "PROVIDER", "", "321");
        databaseHandler.isValidCell(cellInfo);

        // empty MCC
        mockCell(cellInfo, "PROVIDER", "123", "");
        databaseHandler.isValidCell(cellInfo);

        // verify call count of static class
        PowerMockito.verifyStatic(CellParser.class, times(3));
        CellParser.getProvider(cellInfo);
        PowerMockito.verifyStatic(CellParser.class, times(2));
        CellParser.getMcc(cellInfo);
        PowerMockito.verifyStatic(CellParser.class);
        CellParser.getMnc(cellInfo);
    }

    @Test
    public void onCreate() {
        SQLiteDatabase sqLiteDatabase = mock(SQLiteDatabase.class);

        databaseHandler.onCreate(sqLiteDatabase);
        verify(sqLiteDatabase, times(1)).execSQL(onCreateSql);
    }

    @Test
    public void onUpgrade() {
        SQLiteDatabase sqLiteDatabase = mock(SQLiteDatabase.class);

        databaseHandler.onUpgrade(sqLiteDatabase, 0, 1);
        verify(sqLiteDatabase, times(1)).execSQL(onCreateSql);
    }

    @Test
    public void addCell() {
        // invalid location
        CellInfo cellInfo = mock(CellInfo.class);
        databaseHandler.dataHolder = mock(DataHolder.class);
        when(databaseHandler.dataHolder.getLocation()).thenReturn(null);
        databaseHandler.addCell(null);
        verifyNoInteractions(cellInfo);
    }

    @Test
    public void insertCell() {
        SQLiteDatabase sqLiteDatabase = mock(SQLiteDatabase.class);

        databaseHandler.insertCell(sqLiteDatabase, mock(CellInfo.class), mock(Location.class));

        verify(sqLiteDatabase, times(1)).insert(Mockito.anyString(), Mockito.isNull(), Mockito.any(ContentValues.class));
    }

    @Test
    public void getCellsFromCursor() {

        // testing only 1 cell in database
        Cursor cursor = mock(Cursor.class);
        when(cursor.moveToFirst()).thenReturn(true);
        when(cursor.moveToNext()).thenReturn(false);

        when(cursor.getString(1)).thenReturn("one");
        when(cursor.getString(5)).thenReturn("five");

        List<String[]> expectedResult = new ArrayList<>();
        expectedResult.add(new String[]{"one", "five"});

        List<String[]> result = databaseHandler.getCellsFromCursor(cursor, new int[]{1, 5});

        verifyArrayListEquals(expectedResult, result);

        // testing multiple cells in database
        cursor = mock(Cursor.class);
        when(cursor.moveToFirst()).thenReturn(true);
        when(cursor.moveToNext()).thenReturn(true).thenReturn(false);

        when(cursor.getString(1)).thenReturn("one").thenReturn("abc");
        when(cursor.getString(5)).thenReturn("five").thenReturn("efg");
        when(cursor.getString(6)).thenReturn("six").thenReturn("hij");

        expectedResult = new ArrayList<>();
        expectedResult.add(new String[]{"one", "five", "six"});
        expectedResult.add(new String[]{"abc", "efg", "hij"});

        result = databaseHandler.getCellsFromCursor(cursor, new int[]{1, 5, 6});
        verifyArrayListEquals(expectedResult, result);
    }

    public void verifyArrayListEquals(List<String[]> a1, List<String[]> a2) throws AssertionError {
        for (int i=0; i<a1.size(); i++) {
            assertArrayEquals(a1.get(i), a2.get(i));
        }
    }
}