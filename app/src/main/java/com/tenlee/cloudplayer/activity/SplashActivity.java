package com.tenlee.cloudplayer.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Message;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.tenlee.cloudplayer.R;
import com.tenlee.cloudplayer.service.PlayService;

import java.lang.ref.WeakReference;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashActivity extends Activity {

    private static final int START_ACTIVITY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);

        Intent intent = new Intent(this, PlayService.class);
        startService(intent);//启动服务

        hanlder = new SplashHandler(this);
        hanlder.sendEmptyMessageDelayed(START_ACTIVITY, 3000);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    private static SplashHandler hanlder = null;

    static private class SplashHandler extends Handler{
        private WeakReference<SplashActivity> weakReference;
        private SplashActivity splashActivity = null;
        public SplashHandler(SplashActivity splashActivity) {
            weakReference = new WeakReference<SplashActivity>(splashActivity);
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            splashActivity = weakReference.get();
            if(splashActivity != null) {
                switch (msg.what) {
                    case START_ACTIVITY:
                        splashActivity.startActivity(new Intent(splashActivity, MainActivity.class));
                        splashActivity.finish();
                        break;
                }
            }
        }
    };
}
