package xyz.steidle.cellularnetworkmapper.view;

import android.content.Context;
import android.telephony.CellInfo;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;
import xyz.steidle.cellularnetworkmapper.R;

public class CellInfoAdapter extends BaseAdapter {
  Context context;
  List<CellInfo> cellInfoList;
  private static LayoutInflater mLayoutInflater = null;

  public CellInfoAdapter(Context context, List<CellInfo> cellInfoList) {
    this.context = context;
    this.cellInfoList = cellInfoList;
    mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
  }

  @Override
  public int getCount() {
    return cellInfoList == null ? 0 : cellInfoList.size();
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
    if (vi == null) vi = mLayoutInflater.inflate(R.layout.row, null);
    TextView text = (TextView) vi.findViewById(R.id.cell_text);

    System.out.println(cellInfoList.get(i));

    if (cellInfoList.get(i) instanceof CellInfoLte)
      text.setText(((CellInfoLte) cellInfoList.get(i)).getCellIdentity().toString());
    else text.setText(((CellInfoGsm) cellInfoList.get(i)).getCellIdentity().toString());
    return vi;
  }
}
