package com.tenlee.cloudplayer.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.tenlee.cloudplayer.MusicApplication;

/**
 * Created by tenlee on 16-3-27.
 */
public class AppUtils {
    public static void hideInputMethod(View view) {
        InputMethodManager imm = (InputMethodManager) MusicApplication.context.
                getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
