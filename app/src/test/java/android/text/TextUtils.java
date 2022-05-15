/*
 * Created by Robin Steidle on 15.05.22, 10:36
 * Copyright (c) 2022 . All rights reserved.
 * Last modified 15.05.22, 10:36
 */

package android.text;

public class TextUtils {
    public static boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }
}