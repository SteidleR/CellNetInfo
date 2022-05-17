package xyz.steidle.cellnetinfo.view;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import android.content.Context;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import xyz.steidle.cellnetinfo.utils.DatabaseHandler;

public class CellHistoryAdapterTest {

    CellHistoryAdapter cellHistoryAdapter;
    DatabaseHandler databaseHandler;
    List<String[]> cellInfoList = new ArrayList<>();
    String[] stringArray1;
    String[] stringArray2;

    @Before
    public void setUp() {
        Context context = mock(Context.class);
        databaseHandler = mock(DatabaseHandler.class);

        stringArray1 = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        stringArray2 = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11"};

        cellInfoList.add(stringArray1);
        cellInfoList.add(stringArray2);

        cellHistoryAdapter = new CellHistoryAdapter(context, cellInfoList, databaseHandler);
    }

    @Test
    public void joinElements() {
        String[] stringList = {"1", "2", "3"};
        String joinedString = CellHistoryAdapter.joinElements(stringList, ":");
        assertEquals("1:2:3", joinedString);

        stringList = new String[]{};
        joinedString = CellHistoryAdapter.joinElements(stringList, ":");
        assertEquals("", joinedString);
    }

    @Test
    public void getCount() {
        assertEquals(2, cellHistoryAdapter.getCount());
    }

    @Test
    public void getItem() {
        assertEquals(stringArray2, cellHistoryAdapter.getItem(1));
    }

    @Test
    public void getItemId() {
        assertEquals(1L, cellHistoryAdapter.getItemId(1));
    }
}