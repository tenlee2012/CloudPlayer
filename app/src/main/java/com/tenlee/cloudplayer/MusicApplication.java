package com.tenlee.cloudplayer;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.lidroid.xutils.DbUtils;
import com.tenlee.cloudplayer.utils.Constant;

/**
 * Created by tenlee on 16-3-11.
 */
public class MusicApplication extends Application {

    public static SharedPreferences sp;
    public static DbUtils dbUtils;
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        sp = getSharedPreferences(Constant.SP_NAME, Context.MODE_PRIVATE);
        dbUtils = DbUtils.create(getApplicationContext(), Constant.DB_NAME);

        context = getApplicationContext();
    }
}
