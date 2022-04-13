package xyz.steidle.cellularnetworkmapper.view;

import android.content.Context;
import android.telephony.CellInfo;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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
    TextView dbmText = vi.findViewById(R.id.cell_strength_text);

    ImageView signalImage = vi.findViewById(R.id.img_signal);

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

    signalImage.setImageResource(getIconForSignal(cellParser.getSignalStrength(cellInfo)));

    dbmText.setText(context.getString(R.string.cell_signal, cellParser.getSignalDbm(cellInfo)));

    return vi;
  }

  /** Returns id of icon for signal strength
   * @param strength cell signal strength range 0-4
   * @return resource id of icon
   */
  private int getIconForSignal(int strength) {
    int resId;

    switch (strength) {
      case 1:
        resId = R.drawable.ic_signal_0;
        break;
      case 2:
        resId = R.drawable.ic_signal_1;
        break;
      case 3:
        resId = R.drawable.ic_signal_2;
        break;
      case 4:
        resId = R.drawable.ic_signal_3;
        break;
      default:
        resId = R.drawable.ic_signal_none;
    }

    return resId;
  }
}
