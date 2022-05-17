package xyz.steidle.cellnetinfo.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.telephony.CellInfo;
import android.telephony.CellInfoGsm;
import android.telephony.TelephonyManager;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class CellInfoHandlerTest {

    @Test
    public void getCells() {
        // setting up context and telephony manager
        Context context = mock(Context.class);
        TelephonyManager telephonyManager = mock(TelephonyManager.class);
        when(context.getSystemService("phone")).thenReturn(telephonyManager);
        CellInfoHandler cellInfoHandler = new CellInfoHandler(context);

        // test return cellInfo list from telephonyManager
        List<CellInfo> cells = new ArrayList<>();
        cells.add(mock(CellInfoGsm.class));
        when(telephonyManager.getAllCellInfo()).thenReturn(cells);
        assertEquals(cells, cellInfoHandler.getCells());

        // test security exception handling
        when(telephonyManager.getAllCellInfo()).thenThrow(new SecurityException());
        assertNull(cellInfoHandler.getCells());
    }
}