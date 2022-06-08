package xyz.steidle.cellnetinfo.utils;


import static org.junit.jupiter.api.Assertions.assertEquals;

import androidx.appcompat.app.AppCompatDelegate;

import org.junit.Test;

import xyz.steidle.cellnetinfo.R;

public class HelperTest {
    @Test
    public void getThemeFromPref() {
        String mode;

        mode = "Follow System";
        assertEquals(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM, Helper.getThemeFromPref(mode));

        mode = "Always Dark";
        assertEquals(AppCompatDelegate.MODE_NIGHT_YES, Helper.getThemeFromPref(mode));

        mode = "Always Light";
        assertEquals(AppCompatDelegate.MODE_NIGHT_NO, Helper.getThemeFromPref(mode));

        mode = "Nonsense";
        assertEquals(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM, Helper.getThemeFromPref(mode));
    }


    @Test
    public void getIconForSignal() {
        assertEquals(R.drawable.ic_signal_0, Helper.getIconForSignal(1));
        assertEquals(R.drawable.ic_signal_1, Helper.getIconForSignal(2));
        assertEquals(R.drawable.ic_signal_2, Helper.getIconForSignal(3));
        assertEquals(R.drawable.ic_signal_3, Helper.getIconForSignal(4));

        // 0 or invalid
        assertEquals(R.drawable.ic_signal_none, Helper.getIconForSignal(0));
        assertEquals(R.drawable.ic_signal_none, Helper.getIconForSignal(123));
    }
}