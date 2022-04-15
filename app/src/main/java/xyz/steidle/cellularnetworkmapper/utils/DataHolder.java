package xyz.steidle.cellularnetworkmapper.utils;

import android.telephony.CellInfo;

import java.util.List;

public class DataHolder {
    private List<CellInfo> cellInfoList = null;

    public List<CellInfo> getCellInfoList() {return cellInfoList;}
    public void setCellInfoList(List<CellInfo> cellInfoList) {this.cellInfoList = cellInfoList;}

    private static final DataHolder holder = new DataHolder();
    public static DataHolder getInstance() {return holder;}
}