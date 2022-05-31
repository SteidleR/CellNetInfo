/*
 * Created by Robin Steidle on 30.05.22, 14:19
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 30.05.22, 14:19
 */

package xyz.steidle.cellnetinfo.utils;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Intent;
import android.os.HandlerThread;
import android.telephony.CellInfo;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

public class ReloadTest {

    @Test
    public void onCreate() {
        Reload reload = new Reload();
        reload.onCreate();

        assertNotNull(reload.cellInfoHandler);
        assertNotNull(reload.databaseHandler);
    }

    @Test
    public void loadCells() {
        Reload reload = new Reload();

        DatabaseHandler databaseHandler = mock(DatabaseHandler.class);
        reload.databaseHandler = databaseHandler;

        CellInfo cellInfo = mock(CellInfo.class);

        final boolean[] wasExecutedCorrect = {false};

        doAnswer((Answer<Void>) invocation -> {
            wasExecutedCorrect[0] = true;
            return null;
        }).when(databaseHandler).addCell(Mockito.any(CellInfo.class));

        reload.cellInfoHandler = mock(CellInfoHandler.class);
        List<CellInfo> returnValue = new ArrayList<>();
        returnValue.add(cellInfo);
        when(reload.cellInfoHandler.getCells()).thenReturn(returnValue);
        reload.loadCells();

        assertTrue(wasExecutedCorrect[0]);
    }

    @Test
    public void onDestroy() {
        HandlerThread handlerThread = mock(HandlerThread.class);

        Reload reload = new Reload();
        reload.handlerThread = handlerThread;

        reload.onDestroy();
        verify(handlerThread, times(1)).quit();
    }

    @Test
    public void onBind() {
        // should return always null
        assertNull(new Reload().onBind(mock(Intent.class)));
    }
}