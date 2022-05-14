package xyz.steidle.cellnetinfo.utils;

import android.location.Location;

public class DataHolder {
    private Location location;

    private static final DataHolder holder = new DataHolder();
    public static DataHolder getInstance() {return holder;}

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}