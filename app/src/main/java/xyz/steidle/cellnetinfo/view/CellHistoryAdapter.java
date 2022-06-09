package xyz.steidle.cellnetinfo.view;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.Arrays;
import java.util.List;

import xyz.steidle.cellnetinfo.R;
import xyz.steidle.cellnetinfo.utils.DatabaseHandler;

public class CellHistoryAdapter extends BaseAdapter {

    private final Context context;
    private final LayoutInflater mLayoutInflater;
    private final DatabaseHandler databaseHandler;
    private final MapView map;
    private final FrameLayout mFrameLayout;

    private final List<String[]>  cellList;

    public CellHistoryAdapter(Context context, DatabaseHandler databaseHandler, MapView map) {
        this.context = context;
        this.databaseHandler = databaseHandler;
        this.map = map;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mFrameLayout = (FrameLayout) map.getParent();

        cellList = databaseHandler.getAllNrCellsGrouped();
        cellList.addAll(databaseHandler.getAllLteCellsGrouped());
        cellList.addAll(databaseHandler.getAllCdmaCellsGrouped());
        cellList.addAll(databaseHandler.getAllGsmCellsGrouped());
        cellList.addAll(databaseHandler.getAllTdscdmaCellsGrouped());
        cellList.addAll(databaseHandler.getAllWcdmaCellsGrouped());
    }

    @Override
    public int getCount() {
        return cellList.size();
    }

    @Override
    public Object getItem(int i) {
        return cellList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        String[] cellInfo = cellList.get(i);

        View vi;

        switch (cellInfo[2]) {
            case "NR":
                vi = getViewNr(cellInfo);
                break;
            case "LTE":
                vi = getViewLte(cellInfo);
                break;
            case "GSM":
                vi = getViewGsm(cellInfo);
                break;
            case "TDSCDMA":
                vi = getViewTdscdma(cellInfo);
                break;
            case "WCDMA":
                vi = getViewWcdma(cellInfo);
                break;
            case "CDMA":
                vi = getViewCdma(cellInfo);
                break;
            default:
                vi = view;
        }

        return vi;
    }

    public View getViewNr(String[] cellInfo) {
        View vi = mLayoutInflater.inflate(R.layout.row_nr, null);

        setViewBase(cellInfo, vi);

        ((TextView) vi.findViewById(R.id.cell_nrarfcn)).setText(cellInfo[12]);
        ((TextView) vi.findViewById(R.id.cell_csirsrp)).setText(cellInfo[13]);
        ((TextView) vi.findViewById(R.id.cell_csirsrq)).setText(cellInfo[14]);
        ((TextView) vi.findViewById(R.id.cell_csisinr)).setText(cellInfo[15]);
        ((TextView) vi.findViewById(R.id.cell_ssrsrp)).setText(cellInfo[16]);
        ((TextView) vi.findViewById(R.id.cell_ssrsrq)).setText(cellInfo[17]);
        ((TextView) vi.findViewById(R.id.cell_sssinr)).setText(cellInfo[18]);

        List<String[]> locations = databaseHandler.getAllCellLocationsFromTable(DatabaseHandler.TABLE_NR, cellInfo[4], cellInfo[5], cellInfo[10]);
        createMapView(locations, vi.findViewById(R.id.root));

        return vi;
    }

    public View getViewLte(String[] cellInfo) {
        View vi = mLayoutInflater.inflate(R.layout.row_lte, null);

        setViewBase(cellInfo, vi);

        ((TextView) vi.findViewById(R.id.cell_bandwidth)).setText(cellInfo[12]);
        ((TextView) vi.findViewById(R.id.cell_rsrp)).setText(cellInfo[13]);
        ((TextView) vi.findViewById(R.id.cell_rsrq)).setText(cellInfo[14]);
        ((TextView) vi.findViewById(R.id.cell_rssi)).setText(cellInfo[15]);
        ((TextView) vi.findViewById(R.id.cell_cqi)).setText(cellInfo[16]);

        List<String[]> locations = databaseHandler.getAllCellLocationsFromTable(DatabaseHandler.TABLE_LTE, cellInfo[4], cellInfo[5], cellInfo[10]);
        createMapView(locations, vi.findViewById(R.id.root));

        return vi;
    }

    public View getViewGsm(String[] cellInfo) {
        View vi = mLayoutInflater.inflate(R.layout.row_gsm, null);

        setViewBase(cellInfo, vi);

        ((TextView) vi.findViewById(R.id.cell_arfcn)).setText(cellInfo[12]);
        ((TextView) vi.findViewById(R.id.cell_bsic)).setText(cellInfo[13]);
        ((TextView) vi.findViewById(R.id.cell_biterrorrate)).setText(cellInfo[14]);
        ((TextView) vi.findViewById(R.id.cell_rssi)).setText(cellInfo[15]);

        List<String[]> locations = databaseHandler.getAllCellLocationsFromTable(DatabaseHandler.TABLE_GSM, cellInfo[4], cellInfo[5], cellInfo[10]);
        createMapView(locations, vi.findViewById(R.id.root));

        return vi;
    }

    public View getViewTdscdma(String[] cellInfo) {
        View vi = mLayoutInflater.inflate(R.layout.row_tdscdma, null);

        setViewBase(cellInfo, vi);

        ((TextView) vi.findViewById(R.id.cell_uarfcn)).setText(cellInfo[12]);
        ((TextView) vi.findViewById(R.id.cell_rscp)).setText(cellInfo[13]);

        List<String[]> locations = databaseHandler.getAllCellLocationsFromTable(DatabaseHandler.TABLE_TDSCDMA, cellInfo[4], cellInfo[5], cellInfo[10]);
        createMapView(locations, vi.findViewById(R.id.root));

        return vi;
    }

    public View getViewWcdma(String[] cellInfo) {
        View vi = mLayoutInflater.inflate(R.layout.row_wcdma, null);

        setViewBase(cellInfo, vi);

        ((TextView) vi.findViewById(R.id.cell_uarfcn)).setText(cellInfo[12]);
        ((TextView) vi.findViewById(R.id.cell_psc)).setText(cellInfo[13]);
        ((TextView) vi.findViewById(R.id.cell_ecno)).setText(cellInfo[14]);

        List<String[]> locations = databaseHandler.getAllCellLocationsFromTable(DatabaseHandler.TABLE_WCDMA, cellInfo[4], cellInfo[5], cellInfo[10]);
        createMapView(locations, vi.findViewById(R.id.root));

        return vi;
    }

    public View getViewCdma(String[] cellInfo) {
        View vi = mLayoutInflater.inflate(R.layout.row_cdma, null);

        ((TextView) vi.findViewById(R.id.cell_header)).setText(context.getString(R.string.cell_header, cellInfo[3], cellInfo[2]));

        ((TextView) vi.findViewById(R.id.cell_sys)).setText(cellInfo[7]);
        ((TextView) vi.findViewById(R.id.cell_netid)).setText(cellInfo[8]);
        ((TextView) vi.findViewById(R.id.cell_lat)).setText(cellInfo[9]);
        ((TextView) vi.findViewById(R.id.cell_bsid)).setText(cellInfo[10]);
        ((TextView) vi.findViewById(R.id.cell_long)).setText(cellInfo[11]);
        ((TextView) vi.findViewById(R.id.cell_rssi)).setText(cellInfo[12]);
        ((TextView) vi.findViewById(R.id.cell_evdo_rssi)).setText(cellInfo[13]);
        ((TextView) vi.findViewById(R.id.cell_evdo_snr)).setText(cellInfo[14]);

        List<String[]> locations = databaseHandler.getAllCdmaCellsGrouped();
        vi.findViewById(R.id.root).setOnClickListener(view -> {
            mFrameLayout.setVisibility(View.VISIBLE);

            Log.d("HistoryActivity", String.valueOf(locations));

            for (String[] location : locations) {
                Marker marker = new Marker(map);
                marker.setPosition(new GeoPoint(Float.parseFloat(location[10]), Float.parseFloat(location[11])));
                marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);

                map.getOverlays().add(marker);
            }

            GeoPoint location = new GeoPoint(Float.parseFloat(locations.get(0)[10]), Float.parseFloat(locations.get(0)[11]));
            map.getController().setCenter(location);
        });

        return vi;
    }

    public void setViewBase(String[] cellInfo, View vi) {
        ((TextView) vi.findViewById(R.id.cell_header)).setText(context.getString(R.string.cell_header, cellInfo[3], cellInfo[2]));
        ((TextView) vi.findViewById(R.id.cell_mcc)).setText(cellInfo[4]);
        ((TextView) vi.findViewById(R.id.cell_mnc)).setText(cellInfo[5]);
        ((TextView) vi.findViewById(R.id.cell_lactac)).setText(cellInfo[9]);
        ((TextView) vi.findViewById(R.id.cell_pci)).setText(cellInfo[11]);
        ((TextView) vi.findViewById(R.id.cell_cid)).setText(cellInfo[10]);

        vi.findViewById(R.id.img_signal).setVisibility(View.INVISIBLE);
    }

    protected void createMapView(List<String[]> locations, View root) {
        root.setOnClickListener(view -> {
            mFrameLayout.setVisibility(View.VISIBLE);

            Log.d("HistoryActivity", String.valueOf(locations));

            for (String[] location : locations) {
                Marker marker = new Marker(map);
                marker.setPosition(new GeoPoint(Float.parseFloat(location[2]), Float.parseFloat(location[3])));
                marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);

                map.getOverlays().add(marker);
            }

            GeoPoint location = new GeoPoint(Float.parseFloat(locations.get(0)[2]), Float.parseFloat(locations.get(0)[3]));
            map.getController().setCenter(location);
        });
    }
}
