package xyz.steidle.cellnetinfo.view;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

import xyz.steidle.cellnetinfo.R;
import xyz.steidle.cellnetinfo.utils.DatabaseHandler;

public class CellHistoryAdapter extends BaseAdapter {

    private final Context context;
    private final List<String[]>  cellInfoList;
    private final LayoutInflater mLayoutInflater;
    private final DatabaseHandler databaseHandler;

    public CellHistoryAdapter(Context context, List<String[]>  cellInfoList, DatabaseHandler databaseHandler) {
        this.context = context;
        this.cellInfoList = cellInfoList;
        this.databaseHandler = databaseHandler;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return cellInfoList.size();
    }

    @Override
    public Object getItem(int i) {
        return cellInfoList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View vi = view;
        if (vi == null) vi = mLayoutInflater.inflate(R.layout.history_row, null);

        String[] cellInfo = cellInfoList.get(i);

        ((TextView) vi.findViewById(R.id.cell_header)).setText(
                context.getString(R.string.cell_header, cellInfo[1], cellInfo[2]));
        ((TextView) vi.findViewById(R.id.cell_mcc)).setText(cellInfo[3]);
        ((TextView) vi.findViewById(R.id.cell_mnc)).setText(cellInfo[4]);

        ((TextView) vi.findViewById(R.id.cell_cid)).setText(cellInfo[7]);
        ((TextView) vi.findViewById(R.id.cell_pci)).setText(cellInfo[8]);

        List<String[]> cellList2D = databaseHandler.getCellsFiltered(cellInfo[1], cellInfo[2],
                Integer.parseInt(cellInfo[3]), Integer.parseInt(cellInfo[4]),
                cellInfo[5] != null ? Integer.parseInt(cellInfo[5]) : -1, cellInfo[6] != null ? Integer.parseInt(cellInfo[6]) : -1);

        String[] cellList = new String[cellList2D.size()];
        i = 0;
        for (String[] cell : cellList2D) {
            cellList[i] = joinElements(cell, " ");
            i++;
        }

        Log.d("HistoryCell", Arrays.toString(cellList));

        vi.setOnClickListener(view1 -> {
            ListView listView = (ListView) view1.findViewById(R.id.cell_history_list);
            Log.d("HistoryCell", "Open History of Cell");
            listView.setAdapter(new ArrayAdapter<>(this.context, android.R.layout.simple_list_item_1, cellList));
            listView.setVisibility(View.VISIBLE);
        });

        return vi;
    }

    // method to join string elements
    static String joinElements(String[] strArray, String delimiter) {
        if (strArray.length == 0)
            return "";

        StringBuilder stringBuilder = new StringBuilder();
        for (String s : strArray)
            stringBuilder.append(s).append(delimiter);
        String joinedString = stringBuilder.toString();
        return joinedString.substring(0, joinedString.length() - 1);
    }
}
