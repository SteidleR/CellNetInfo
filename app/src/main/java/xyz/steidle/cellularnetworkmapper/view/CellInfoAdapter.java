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

import androidx.core.util.Pair;

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

    TextView headerText = vi.findViewById(R.id.cell_header);

    TextView mccText = vi.findViewById(R.id.cell_mcc);
    TextView mncText = vi.findViewById(R.id.cell_mnc);
    TextView lacDescrText = vi.findViewById(R.id.cell_lac_descr);
    TextView lacText = vi.findViewById(R.id.cell_lactac);
    TextView cidText = vi.findViewById(R.id.cell_cid);
    TextView pciText = vi.findViewById(R.id.cell_pci);

    Log.d("CellInfoAdapter", cellInfoList.get(i).toString());

    CellInfo cellInfo = cellInfoList.get(i);

    headerText.setText(cellParser.getCellHeader(cellInfo));
    mccText.setText(cellParser.getMcc(cellInfo));
    mncText.setText(cellParser.getMnc(cellInfo));

    Pair<Integer, Integer> lacPair = cellParser.getLacTac(cellInfo);
    lacDescrText.setText(lacPair.first);
    lacText.setText(String.valueOf(lacPair.second));

    cidText.setText(String.valueOf(cellParser.getCellId(cellInfo)));

    int pci = cellParser.getPci(cellInfo);
    if (pci == -1) {
      vi.findViewById(R.id.cell_pci_descr).setVisibility(View.GONE);
      pciText.setVisibility(View.GONE);
    } else {
      vi.findViewById(R.id.cell_pci_descr).setVisibility(View.VISIBLE);
      pciText.setVisibility(View.VISIBLE);
      pciText.setText(String.valueOf(pci));
    }

    return vi;
  }
}
