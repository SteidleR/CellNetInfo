package xyz.steidle.cellularnetworkmapper.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import xyz.steidle.cellularnetworkmapper.R;

public class CellHistoryAdapter extends BaseAdapter {

    private final Context context;
    private final List<String[]>  cellInfoList;
    private final LayoutInflater mLayoutInflater;

    public CellHistoryAdapter(Context context, List<String[]>  cellInfoList) {
        this.context = context;
        this.cellInfoList = cellInfoList;
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

        return vi;
    }
}
