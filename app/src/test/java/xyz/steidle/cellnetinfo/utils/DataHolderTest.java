package xyz.steidle.cellnetinfo.utils;

import static org.junit.Assert.*;

import android.location.Location;

import org.junit.Test;

public class DataHolderTest {

    @Test
    public void getInstance() {
        assertEquals(DataHolder.getInstance().getClass(), DataHolder.class);
    }

    @Test
    public void setLocation() {
        Location location = new Location("TestSetLocation");
        DataHolder.getInstance().setLocation(location);
        assertEquals(location, DataHolder.getInstance().getLocation());
    }
}