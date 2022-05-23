/*
 * Created by Robin Steidle on 15.05.22, 21:32
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 15.05.22, 21:32
 */

package xyz.steidle.cellnetinfo.view;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.telephony.CellInfo;
import android.view.View;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import xyz.steidle.cellnetinfo.R;

public class CellInfoAdapterTest {

    CellInfoAdapter cellInfoAdapter;
    List<CellInfo> cellInfoList = new ArrayList<>();

    @Before
    public void setUp() {
        cellInfoList.add(mock(CellInfo.class));
        cellInfoList.add(mock(CellInfo.class));

        cellInfoAdapter = new CellInfoAdapter(
                mock(Context.class),
                cellInfoList);
    }

    @Test
    public void getCount() {
        assertEquals(2, cellInfoAdapter.getCount());
    }

    @Test
    public void getIconForSignal() {
        assertEquals(R.drawable.ic_signal_0, cellInfoAdapter.getIconForSignal(1));
        assertEquals(R.drawable.ic_signal_1, cellInfoAdapter.getIconForSignal(2));
        assertEquals(R.drawable.ic_signal_2, cellInfoAdapter.getIconForSignal(3));
        assertEquals(R.drawable.ic_signal_3, cellInfoAdapter.getIconForSignal(4));

        // 0 or invalid
        assertEquals(R.drawable.ic_signal_none, cellInfoAdapter.getIconForSignal(0));
        assertEquals(R.drawable.ic_signal_none, cellInfoAdapter.getIconForSignal(123));
    }

    @Test
    public void getItem() {
        assertEquals(cellInfoList.get(0), cellInfoAdapter.getItem(0));
    }

    @Test
    public void getItemId() {
        assertEquals(1, cellInfoAdapter.getItemId(1));
    }

    @Test
    public void getView() {
        View view = mock(View.class);

    }
}