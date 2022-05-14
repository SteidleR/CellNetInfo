package xyz.steidle.cellnetinfo.utils;

import android.location.Location;
import android.telephony.CellInfo;

import java.util.List;

public class DataHolder {
    private List<CellInfo> cellInfoList = null;
    private Location location;

    public List<CellInfo> getCellInfoList() {return cellInfoList;}
    public void setCellInfoList(List<CellInfo> cellInfoList) {this.cellInfoList = cellInfoList;}

    private static final DataHolder holder = new DataHolder();
    public static DataHolder getInstance() {return holder;}

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}