/*
 * Created by Robin Steidle on 14.05.22, 17:51
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 14.05.22, 17:51
 */

package xyz.steidle.cellnetinfo.utils;

import static org.junit.Assert.*;

import android.location.Location;

import org.junit.Test;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DataHolderTest {

    @Test
    @Order(1)
    public void getInstance() {
        assertEquals(DataHolder.getInstance().getClass(), DataHolder.class);
    }

    @Test
    @Order(2)
    public void setLocation() {
        Location location = new Location("TestSetLocation");
        DataHolder.getInstance().setLocation(location);
        assertEquals(location, DataHolder.getInstance().getLocation());
    }
}