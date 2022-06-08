/*
 * Created by Robin Steidle on 15.05.22, 21:32
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 15.05.22, 21:32
 */

package xyz.steidle.cellnetinfo.view;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoLte;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.matchers.Any;

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
    public void getItem() {
        assertEquals(cellInfoList.get(0), cellInfoAdapter.getItem(0));
    }

    @Test
    public void getItemId() {
        assertEquals(1, cellInfoAdapter.getItemId(1));
    }
}