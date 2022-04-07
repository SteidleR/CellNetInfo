package xyz.steidle.cellularnetworkmapper.view;

import android.content.Context;
import android.os.Build;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellInfo;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import java.util.List;
import xyz.steidle.cellularnetworkmapper.R;

public class CellInfoAdapter extends BaseAdapter {
  Context context;
  List<CellInfo> cellInfoList;
  private static LayoutInflater mLayoutInflater;

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

  @RequiresApi(api = Build.VERSION_CODES.P)
  @Override
  public View getView(int i, View view, ViewGroup viewGroup) {
    View vi = view;
    if (vi == null) vi = mLayoutInflater.inflate(R.layout.row, null);

    TextView header = (TextView) vi.findViewById(R.id.cell_header);
    TextView body = (TextView) vi.findViewById(R.id.cell_text);

    System.out.println(cellInfoList.get(i));

    CharSequence bodyText;

    if (cellInfoList.get(i) instanceof CellInfoLte) {
      CellIdentityLte cellIdentity = ((CellInfoLte) cellInfoList.get(i)).getCellIdentity();

      bodyText = cellIdentity.toString();
    } else {
      CellIdentityGsm cellIdentity = ((CellInfoGsm) cellInfoList.get(i)).getCellIdentity();

      bodyText = cellIdentity.toString();
    }

    header.setText(getCellHeader(cellInfoList.get(i)));
    body.setText(bodyText);

    return vi;
  }

  @RequiresApi(api = Build.VERSION_CODES.P)
  public CharSequence getCellHeader(CellInfo cellInfo) {
    CharSequence operator;
    CharSequence generation;

    if (cellInfo instanceof CellInfoLte) {
      CellIdentityLte cellIdentity = ((CellInfoLte) cellInfo).getCellIdentity();

      if (TextUtils.isEmpty(cellIdentity.getOperatorAlphaShort()))
        operator = "Unknown";
      else
        operator = cellIdentity.getOperatorAlphaShort();

      generation = "LTE";
    } else {
      CellIdentityGsm cellIdentity = ((CellInfoGsm) cellInfo).getCellIdentity();

      if (TextUtils.isEmpty(cellIdentity.getOperatorAlphaShort()))
        operator = "Unknown";
      else
        operator = cellIdentity.getOperatorAlphaShort();

      generation = "GSM";
    }

    return context.getString(R.string.cell_header, operator, generation);
  }
}
