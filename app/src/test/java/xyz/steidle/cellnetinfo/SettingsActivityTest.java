/*
 * Created by Robin Steidle on 16.05.22, 21:52
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 16.05.22, 21:52
 */

package xyz.steidle.cellnetinfo;

import android.os.storage.StorageManager;
import android.util.Log;

import com.google.android.material.snackbar.Snackbar;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import xyz.steidle.cellnetinfo.utils.DatabaseHandler;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Log.class)
public class SettingsActivityTest {

    SettingsActivity.SettingsFragment settingsFragment;

    @Mock
    DatabaseHandler databaseHandler;

    @Mock
    Snackbar snackbarHistory;

    @Mock
    Snackbar snackbarExport;

    @Mock
    StorageManager storageManager;

    @Before
    public void setUp() {
        settingsFragment = new SettingsActivity.SettingsFragment(databaseHandler, snackbarHistory, snackbarExport, storageManager);
    }

    @Test
    public void settingsFragmentSetClearHistoryOnClick() {
        PowerMockito.mockStatic(Log.class);

        settingsFragment.setClearHistoryOnClick();

        PowerMockito.verifyStatic(Log.class, Mockito.times(1));
        Log.d(SettingsActivity.LOGTAG, "Empty Preference 'clear_hist'");
    }

    @Test
    public void settingsFragmentSetExportHistoryOnClick() {
    }
}