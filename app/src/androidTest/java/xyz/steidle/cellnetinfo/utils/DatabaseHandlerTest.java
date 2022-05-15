/*
 * Created by Robin Steidle on 15.05.22, 20:27
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 15.05.22, 20:24
 */

package xyz.steidle.cellnetinfo.utils;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import android.content.Context;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
@FixMethodOrder()
public class DatabaseHandlerTest {

    DatabaseHandler databaseHandler;

    @Before
    public void setUp() {
        Context context = mock(Context.class);
        databaseHandler = new DatabaseHandler(context);
    }
    @After
    public void tearDown() {
        databaseHandler.close();
    }

    @Test(expected = Test.None.class)
    public void addCellNull() {
        // test that a null cell wont break anything
        databaseHandler.addCell(null);
    }

    @Test
    public void addCell() {

    }

    @Test
    public void getAllCells() {
    }

    @Test
    public void getAllCellsGrouped() {
    }

    @Test
    public void getAllCellsCsv() {
    }

    @Test
    public void getCellsFiltered() {
    }

    @Test
    public void clearCellTable() {
        databaseHandler.clearCellTable();
        assertEquals(0, databaseHandler.getAllCellsCsv().size());
    }
}