package xyz.steidle.cellularnetworkmapper.view;

import android.content.Context;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellInfo;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import xyz.steidle.cellularnetworkmapper.R;
import xyz.steidle.cellularnetworkmapper.utils.CellParser;

public class CellInfoAdapter extends BaseAdapter {
  Context context;
  CellParser cellParser;
  List<CellInfo> cellInfoList;
  private final LayoutInflater mLayoutInflater;

  public CellInfoAdapter(Context context, List<CellInfo> cellInfoList) {
    this.context = context;
    this.cellInfoList = cellInfoList;
    mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    cellParser = new CellParser(context);
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

  // @RequiresApi(api = Build.VERSION_CODES.P)
  @Override
  public View getView(int i, View view, ViewGroup viewGroup) {
    View vi = view;
    if (vi == null) vi = mLayoutInflater.inflate(R.layout.row, null);

    TextView header = vi.findViewById(R.id.cell_header);
    TextView country = vi.findViewById(R.id.cell_mcc);
    TextView body = vi.findViewById(R.id.cell_text);

    Log.d("CellInfoAdapter", cellInfoList.get(i).toString());

    CharSequence bodyText;

    if (cellInfoList.get(i) instanceof CellInfoLte) {
      CellIdentityLte cellIdentity = ((CellInfoLte) cellInfoList.get(i)).getCellIdentity();

      bodyText = cellIdentity.toString();
    } else {
      CellIdentityGsm cellIdentity = ((CellInfoGsm) cellInfoList.get(i)).getCellIdentity();

      bodyText = cellIdentity.toString();
    }

    header.setText(cellParser.getCellHeader(cellInfoList.get(i)));
    country.setText(cellParser.getMcc(cellInfoList.get(i)));
    body.setText(bodyText);

    return vi;
  }
}
