package xyz.steidle.cellularnetworkmapper;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.CellInfo;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;
import xyz.steidle.cellularnetworkmapper.view.CellInfoAdapter;

public class MainActivity extends AppCompatActivity {
  private CellInfoHandler cellInfoHandler;
  private ListView cellInfoListView;
  private TextView noCellsTextView;
  private TextView statusTextView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    cellInfoListView = findViewById(R.id.listScrollView);
    noCellsTextView = findViewById(R.id.noCellsWarning);
    statusTextView = findViewById(R.id.statusTextView);

    findViewById(R.id.buttonHelp).setOnClickListener(view -> openHelpActivity());

    cellInfoHandler = new CellInfoHandler(this);

    reloadCells();
  }

  /** Restart cell scan and displays all cells in list. */
  protected void reloadCells() {
    setStatus(R.string.status_searching);

    List<CellInfo> cellInfoList = cellInfoHandler.getCells();

    if (cellInfoList != null) {
      noCellsTextView.setVisibility(View.INVISIBLE);
      cellInfoListView.setAdapter(new CellInfoAdapter(this, cellInfoList));
      resetStatus();
    } else {
      noCellsTextView.setVisibility(View.VISIBLE);
      setStatus(R.string.status_wrong);
    }
  }

  /** Sets status in status bar from given resource id.
   * @param strId Status String ID
   */
  private void setStatus(int strId) {
    statusTextView.setText(getResources().getString(strId));
  }

  /** Sets status text to empty */
  private void resetStatus() {
    statusTextView.setText("");
  }

  /** creates and starts the help activity */
  private void openHelpActivity() {
    Intent intent = new Intent(this, HelpActivity.class);
    startActivity(intent);
  }
}
