/*
 * Created by Robin Steidle on 16.05.22, 20:38
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 16.05.22, 20:38
 */

package xyz.steidle.cellnetinfo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDelegate;

import org.junit.Test;

public class MainActivityTest {

    MainActivity activity = mock(MainActivity.class);

    @Test
    public void onRequestPermissionsResult() {
        // do not call initializeApplicationLogic when wrong request
        activity.onRequestPermissionsResult(12, new String[]{"android.permission.NOT_ACCESS_FINE_LOCATION"}, new int[]{1});

        // do not call initializeApplicationLogic when denied
        activity.onRequestPermissionsResult(12, new String[]{"android.permission.ACCESS_FINE_LOCATION"}, new int[]{-1});

        // call initializeApplicationLogic when allowed
        activity.onRequestPermissionsResult(12, new String[]{"android.permission.ACCESS_FINE_LOCATION"}, new int[]{0});

        // verify method is called only once
        verify(activity, atMost(1)).initializeApplicationLogic();
    }
}