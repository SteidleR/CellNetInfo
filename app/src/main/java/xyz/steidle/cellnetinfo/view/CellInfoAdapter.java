package xyz.steidle.cellnetinfo.view;

import android.content.Context;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.util.Pair;

import java.util.List;
import xyz.steidle.cellnetinfo.R;
import xyz.steidle.cellnetinfo.utils.CellParser;

public class CellInfoAdapter extends BaseAdapter {
  Context context;
  List<CellInfo> cellInfoList;
  private final LayoutInflater mLayoutInflater;

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

  // @RequiresApi(api = Build.VERSION_CODES.P)
  @Override
  public View getView(int i, View view, ViewGroup viewGroup) {
    CellInfo cellInfo = cellInfoList.get(i);

    if (cellInfo instanceof CellInfoCdma)
      return getViewCdma(view, cellInfo);

    View vi = view;
    if (vi == null) vi = mLayoutInflater.inflate(R.layout.row, null);

    // store row elements to fill with data
    TextView headerText = vi.findViewById(R.id.cell_header);
    TextView mccText = vi.findViewById(R.id.cell_mcc);
    TextView mncText = vi.findViewById(R.id.cell_mnc);
    TextView lacDescrText = vi.findViewById(R.id.cell_lac_descr);
    TextView lacText = vi.findViewById(R.id.cell_lactac);
    TextView cidText = vi.findViewById(R.id.cell_cid);
    TextView pciText = vi.findViewById(R.id.cell_pci);
    TextView dbmText = vi.findViewById(R.id.cell_strength_text);
    ImageView signalImage = vi.findViewById(R.id.img_signal);

    Log.d("CellInfoAdapter", cellInfo.toString());

    CharSequence operator = CellParser.getProvider(cellInfo);
    CharSequence generation = CellParser.getGeneration(cellInfo);

    headerText.setText(context.getString(R.string.cell_header, operator, generation));

    String mcc = CellParser.getMcc(cellInfo);
    if (mcc.equals(""))
      mcc = context.getString(R.string.unknown);
    mccText.setText(mcc);

    String mnc = CellParser.getMnc(cellInfo);
    if (mcc.equals(""))
      mnc = context.getString(R.string.unknown);
    mncText.setText(mnc);

    Pair<Integer, Integer> lacPair = CellParser.getLacTac(cellInfo);
    lacDescrText.setText(lacPair.first);
    lacText.setText(String.valueOf(lacPair.second));

    cidText.setText(String.valueOf(CellParser.getCellId(cellInfo)));

    int pci = CellParser.getPci(cellInfo);
    if (pci == -1) {
      vi.findViewById(R.id.cell_pci_descr).setVisibility(View.GONE);
      pciText.setVisibility(View.GONE);
    } else {
      vi.findViewById(R.id.cell_pci_descr).setVisibility(View.VISIBLE);
      pciText.setVisibility(View.VISIBLE);
      pciText.setText(String.valueOf(pci));
    }

    signalImage.setImageResource(getIconForSignal(CellParser.getSignalStrength(cellInfo)));

    dbmText.setText(context.getString(R.string.cell_signal, CellParser.getSignalDbm(cellInfo)));

    return vi;
  }

  public View getViewCdma(View view, CellInfo cellInfo) {
    View vi = view;
    if (vi == null) vi = mLayoutInflater.inflate(R.layout.row_cdma, null);

    TextView headerText = vi.findViewById(R.id.cell_header);
    TextView sysIdText = vi.findViewById(R.id.cell_sys);
    TextView netIdText = vi.findViewById(R.id.cell_netid);
    TextView bsIdText = vi.findViewById(R.id.cell_bsid);
    TextView latText = vi.findViewById(R.id.cell_lat);
    TextView longText = vi.findViewById(R.id.cell_long);

    CharSequence operator = CellParser.getProvider(cellInfo);
    CharSequence generation = CellParser.getGeneration(cellInfo);

    headerText.setText(context.getString(R.string.cell_header, operator, generation));

    CellInfoCdma cellInfoCdma = (CellInfoCdma) cellInfo;

    sysIdText.setText(CellParser.getCdmaSystemId(cellInfoCdma));
    netIdText.setText(CellParser.getCdmaNetworkId(cellInfoCdma));
    bsIdText.setText(CellParser.getCdmaBaseStationId(cellInfoCdma));

    Pair<Integer, Integer> latLongPair = CellParser.getCdmaLocation(cellInfoCdma);
    latText.setText(latLongPair.first);
    longText.setText(latLongPair.second);

    return vi;
  }

  /** Returns id of icon for signal strength
   * @param strength cell signal strength range 0-4
   * @return resource id of icon
   */
  protected int getIconForSignal(int strength) {
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
