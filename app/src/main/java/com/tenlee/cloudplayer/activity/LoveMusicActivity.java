package com.tenlee.cloudplayer.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lidroid.xutils.exception.DbException;
import com.tenlee.cloudplayer.MusicApplication;
import com.tenlee.cloudplayer.R;
import com.tenlee.cloudplayer.adapter.MyMusicListAdapter;
import com.tenlee.cloudplayer.entity.Mp3Info;
import com.tenlee.cloudplayer.service.PlayService;

import java.util.ArrayList;

public class LoveMusicActivity extends BaseActivity implements AdapterView.OnItemClickListener{

    private ArrayList<Mp3Info> like_mp3list;
    private MyMusicListAdapter adapter;
    private ListView listView_like;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_love_music);

        listView_like = (ListView) findViewById(R.id.listView_like);
        listView_like.setOnItemClickListener(this);

        initdata();
    }

    private void initdata() {
        try {
            like_mp3list = (ArrayList<Mp3Info>) MusicApplication.dbUtils.findAll(Mp3Info.class);
            Log.d("mydebug", "LoveMusic is " + like_mp3list.size());
            adapter = new MyMusicListAdapter(this, like_mp3list);
            listView_like.setAdapter(adapter);
        } catch (DbException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        bindPlayService();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unBindPlayService();
    }

    @Override
    public void publish(int progress) {

    }

    @Override
    public void change(int position) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(playService.getNowMusicList() != PlayService.LOVE_MUSIC_LIST) {
            playService.setMp3List(like_mp3list);
            playService.setNowMusicList(PlayService.MY_MUSIC_LIST);
        }
        playService.musicPlay(position);
    }
}
