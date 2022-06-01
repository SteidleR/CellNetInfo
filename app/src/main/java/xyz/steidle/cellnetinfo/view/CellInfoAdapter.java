package xyz.steidle.cellnetinfo.view;

import android.content.Context;
import android.os.Build;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoNr;
import android.telephony.CellInfoTdscdma;
import android.telephony.CellInfoWcdma;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.util.Pair;

import java.util.List;
import xyz.steidle.cellnetinfo.R;
import xyz.steidle.cellnetinfo.utils.CellParser;

public class CellInfoAdapter extends BaseAdapter {
  Context context;
  List<CellInfo> cellInfoList;
  protected LayoutInflater mLayoutInflater;

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

    Log.d("CellInfoAdapter", cellInfo.toString());

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && cellInfo instanceof CellInfoNr)
      return getViewNr(cellInfo);
    else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && cellInfo instanceof CellInfoTdscdma)
      return getViewTdscdma(cellInfo);
    else if (cellInfo instanceof CellInfoLte)
      return getViewLte(cellInfo);
    else if (cellInfo instanceof CellInfoGsm)
      return getViewGsm(cellInfo);
    else if (cellInfo instanceof CellInfoCdma)
      return getViewCdma(cellInfo);
    else if (cellInfo instanceof CellInfoWcdma)
      return getViewWcdma(cellInfo);

    return view;
  }

  public View getViewCdma(CellInfo cellInfo) {
    View vi = mLayoutInflater.inflate(R.layout.row_cdma, null);

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

  @RequiresApi(api = Build.VERSION_CODES.Q)
  public View getViewNr(CellInfo cellInfo) {
    View vi = mLayoutInflater.inflate(R.layout.row_nr, null);
    setViewCellHeader(vi, cellInfo);

    TextView nrarfcnText = vi.findViewById(R.id.cell_nrarfcn);
    int nrarfcn = CellParser.getNrarfcn((CellInfoNr) cellInfo);
    if (nrarfcn == CellInfo.UNAVAILABLE)
      nrarfcnText.setVisibility(View.GONE);
    else
      nrarfcnText.setText(String.valueOf(nrarfcn));

    return vi;
  }

  public View getViewLte(CellInfo cellInfo) {
    View vi = mLayoutInflater.inflate(R.layout.row_lte, null);
    setViewCellHeader(vi, cellInfo);

    TextView bandwidthText = vi.findViewById(R.id.cell_bandwidth);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
      int bandwidth = CellParser.getBandwidth((CellInfoLte) cellInfo);
      if (bandwidth == CellInfo.UNAVAILABLE)
        ((TableRow) bandwidthText.getParent()).setVisibility(View.GONE);
      else
        bandwidthText.setText(bandwidth + "kHz");
    } else {
      ((TableRow) bandwidthText.getParent()).setVisibility(View.GONE);
    }

    return vi;
  }

  public View getViewGsm(CellInfo cellInfo) {
    View vi = mLayoutInflater.inflate(R.layout.row_gsm, null);
    setViewCellHeader(vi, cellInfo);

    TextView arfcnText = vi.findViewById(R.id.cell_arfcn);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      int arfcn = CellParser.getArfcn(cellInfo);
      if (arfcn == CellInfo.UNAVAILABLE)
        ((TableRow) arfcnText.getParent()).setVisibility(View.GONE);
      else
        arfcnText.setText(String.valueOf(arfcn));
    } else {
      ((TableRow) arfcnText.getParent()).setVisibility(View.GONE);
    }

    TextView bsicText = vi.findViewById(R.id.cell_bsic);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      int bsic = CellParser.getBsic((CellInfoGsm) cellInfo);
      if (bsic == CellInfo.UNAVAILABLE)
        ((TableRow) bsicText.getParent()).setVisibility(View.GONE);
      else
        bsicText.setText(String.valueOf(bsic));
    } else {
      ((TableRow) bsicText.getParent()).setVisibility(View.GONE);
    }

    return vi;
  }

  @RequiresApi(api = Build.VERSION_CODES.P)
  public View getViewTdscdma(CellInfo cellInfo) {
    View vi = mLayoutInflater.inflate(R.layout.row_tdscdma, null);
    setViewCellHeader(vi, cellInfo);

    TextView uarfcnText = vi.findViewById(R.id.cell_uarfcn);
    int uarfcn = CellParser.getArfcn(cellInfo);
    if (uarfcn == CellInfo.UNAVAILABLE)
      ((TableRow) uarfcnText.getParent()).setVisibility(View.GONE);
    else
      uarfcnText.setText(String.valueOf(uarfcn));

    return vi;
  }

  public View getViewWcdma(CellInfo cellInfo) {
    View vi = mLayoutInflater.inflate(R.layout.row_wcdma, null);
    setViewCellHeader(vi, cellInfo);

    TextView uarfcnText = vi.findViewById(R.id.cell_uarfcn);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      int uarfcn = CellParser.getArfcn(cellInfo);
      if (uarfcn == CellInfo.UNAVAILABLE)
        ((TableRow) uarfcnText.getParent()).setVisibility(View.GONE);
      else
        uarfcnText.setText(String.valueOf(uarfcn));
    } else {
      ((TableRow) uarfcnText.getParent()).setVisibility(View.GONE);
    }

    TextView pscText = vi.findViewById(R.id.cell_psc);
    int psc = CellParser.getPsc((CellInfoWcdma) cellInfo);
    if (psc == CellInfo.UNAVAILABLE)
      ((TableRow) pscText.getParent()).setVisibility(View.GONE);
    else
      uarfcnText.setText(String.valueOf(psc));

    return vi;
  }

  public void setViewCellHeader(View vi, CellInfo cellInfo) {
    TextView headerText = vi.findViewById(R.id.cell_header);
    TextView mccText = vi.findViewById(R.id.cell_mcc);
    TextView mncText = vi.findViewById(R.id.cell_mnc);
    TextView dbmText = vi.findViewById(R.id.cell_strength_text);
    ImageView signalImage = vi.findViewById(R.id.img_signal);
    TextView lacDescrText = vi.findViewById(R.id.cell_lac_descr);
    TextView lacText = vi.findViewById(R.id.cell_lactac);
    TextView cidText = vi.findViewById(R.id.cell_cid);
    TextView pciText = vi.findViewById(R.id.cell_pci);

    CharSequence operator = CellParser.getProvider(cellInfo);
    CharSequence generation = CellParser.getGeneration(cellInfo);

    headerText.setText(context.getString(R.string.cell_header, operator, generation));

    String mcc = CellParser.getMcc(cellInfo);
    if (mcc.equals(""))
      mcc = context.getString(R.string.unknown);
    mccText.setText(mcc);

    String mnc = CellParser.getMnc(cellInfo);
    if (mnc.equals(""))
      mnc = context.getString(R.string.unknown);
    mncText.setText(mnc);

    Pair<Integer, Integer> lacPair = CellParser.getLacTac(cellInfo);
    lacDescrText.setText(lacPair.first);
    if (lacPair.second == CellInfo.UNAVAILABLE)
      lacText.setText("?");
    else
      lacText.setText(String.valueOf(lacPair.second));

    long cid = CellParser.getCellId(cellInfo);
    if (cid == CellInfo.UNAVAILABLE_LONG || cid == CellInfo.UNAVAILABLE)
      cidText.setText("?");
    else
      cidText.setText(String.valueOf(cid));

    int pci = CellParser.getPci(cellInfo);
    if (pci == -1 || pci == CellInfo.UNAVAILABLE) {
      vi.findViewById(R.id.cell_pci_descr).setVisibility(View.GONE);
      pciText.setVisibility(View.GONE);
    } else {
      vi.findViewById(R.id.cell_pci_descr).setVisibility(View.VISIBLE);
      pciText.setVisibility(View.VISIBLE);
      pciText.setText(String.valueOf(pci));
    }

    signalImage.setImageResource(getIconForSignal(CellParser.getSignalStrength(cellInfo)));

    dbmText.setText(context.getString(R.string.cell_signal, CellParser.getSignalDbm(cellInfo)));
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
