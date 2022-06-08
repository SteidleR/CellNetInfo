package xyz.steidle.cellnetinfo.utils;

import androidx.appcompat.app.AppCompatDelegate;

import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import xyz.steidle.cellnetinfo.R;

public class Helper {

    private Helper() {}

    public static int getThemeFromPref(String pref) {
        int mode;
        switch (pref) {
            case "Always Light":
                mode = AppCompatDelegate.MODE_NIGHT_NO;
                break;
            case "Always Dark":
                mode = AppCompatDelegate.MODE_NIGHT_YES;
                break;
            default:
                mode = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
        }

        return mode;
    }

    /** Returns id of icon for signal strength
     * @param strength cell signal strength range 0-4
     * @return resource id of icon
     */
    public static int getIconForSignal(int strength) {
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

    public static void writeDataToFile(String fileName, List<String[]> data) throws IOException {
        CSVWriter writer = new CSVWriter(new FileWriter(fileName));
        writer.writeAll(data);
        writer.close();
    }
}
