package xyz.steidle.cellnetinfo.utils;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.telephony.CellInfo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;


@RunWith(PowerMockRunner.class)
@PrepareForTest(CellParser.class)
public class DatabaseHandlerTest {

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
        PowerMockito.verifyStatic(CellParser.class, Mockito.times(3));
        CellParser.getProvider(cellInfo);
        PowerMockito.verifyStatic(CellParser.class, Mockito.times(2));
        CellParser.getMcc(cellInfo);
        PowerMockito.verifyStatic(CellParser.class);
        CellParser.getMnc(cellInfo);
    }
}