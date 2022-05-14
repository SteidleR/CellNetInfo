package xyz.steidle.cellnetinfo.view;

import org.junit.Assert;
import org.junit.Test;

public class CellHistoryAdapterTest {

    @Test
    public void testJoinElements() {
        String[] stringList = {"1", "2", "3"};
        String joinedString = CellHistoryAdapter.joinElements(stringList, ":");
        Assert.assertEquals("1:2:3", joinedString);

        stringList = new String[]{};
        joinedString = CellHistoryAdapter.joinElements(stringList, ":");
        Assert.assertEquals("", joinedString);
    }
}