package com.tenlee.cloudplayer.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.tenlee.cloudplayer.service.PlayService;
import com.tenlee.cloudplayer.utils.Constant;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by tenlee on 16-3-2.
 */
public abstract class BaseActivity extends FragmentActivity {

    public PlayService playService;
    private boolean isBound = false;


    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    private ServiceConnection serviceConnection;

    /**
     * 绑定服务
     */
    public void bindPlayService() {
        if(!isBound) {
            serviceConnection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    PlayService.PlayBinder playBinder = (PlayService.PlayBinder) service;
                    playService = playBinder.getPlayService();
                    playService.setMusicUpdateListener(musicUpdateListener); //放进去

                    musicUpdateListener.onChange(playService.getCurrentPositionInMp3list());
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {
                    playService = null;
                }
            };
            Intent intent = new Intent(this, PlayService.class);
            bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
            isBound = true;
        }
    }
    public void unBindPlayService() {
        if(isBound) {
            Log.d("mydebug", "unBindPlayService");
            unbindService(serviceConnection);
            isBound = false;
        }
    }

    private PlayService.MusicUpdateListener musicUpdateListener = new PlayService.MusicUpdateListener() {
        @Override
        public void onPublish(int progress) {
            publish(progress);
        }

        @Override
        public void onChange(int position) {
            change(position);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    public abstract void publish(int progress);

    public abstract void change(int position);
}
