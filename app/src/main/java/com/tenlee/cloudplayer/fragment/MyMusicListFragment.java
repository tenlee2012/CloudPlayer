package com.tenlee.cloudplayer.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.andraskindler.quickscroll.QuickScroll;
import com.tenlee.cloudplayer.R;
import com.tenlee.cloudplayer.entity.Mp3Info;
import com.tenlee.cloudplayer.activity.MainActivity;
import com.tenlee.cloudplayer.activity.PlayActivity;
import com.tenlee.cloudplayer.adapter.MyMusicListAdapter;
import com.tenlee.cloudplayer.service.PlayService;
import com.tenlee.cloudplayer.utils.MediaUtils;

import java.util.ArrayList;

import static android.widget.AdapterView.*;

public class MyMusicListFragment extends Fragment implements View.OnClickListener{

    private static final int START_PLAY = 0x2;

    private ListView listView_my_music;
    private ArrayList<Mp3Info> mp3list;
    private MyMusicListAdapter myMusicListAdapter;
    private MainActivity mainActivity;

    private ImageView imageView_album, imageView_play_pause, imageView_next;
    private TextView textView_songName, textView_songer;
    private QuickScroll quickScroll;

    public static MyMusicListFragment newInstance() {
        MyMusicListFragment myMusicListFragment = new MyMusicListFragment();
        return myMusicListFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //加载布局文件
        View view = inflater.inflate(R.layout.my_music_list, null);
        listView_my_music = (ListView) view.findViewById(R.id.listView_my_music);
        imageView_album = (ImageView) view.findViewById(R.id.imageView_album);
        imageView_play_pause = (ImageView) view.findViewById(R.id.imageView_play_pause);
        imageView_next = (ImageView) view.findViewById(R.id.play_imageView_next);
        textView_songName = (TextView) view.findViewById(R.id.textView_songName);
        textView_songer = (TextView) view.findViewById(R.id.textView_songer);
        quickScroll = (QuickScroll) view.findViewById(R.id.quick_scroll);

        listView_my_music.setOnItemClickListener(onItemClickListener);
        imageView_play_pause.setOnClickListener(this);
        imageView_next.setOnClickListener(this);
        imageView_album.setOnClickListener(this);

//        loadData();
        return view;
    }

    /**
     * 加载本地音乐列表
     */
    public void loadData() {
        mp3list = MediaUtils.getMp3Infos(mainActivity);
        myMusicListAdapter = new MyMusicListAdapter(mainActivity, mp3list);
        listView_my_music.setAdapter(myMusicListAdapter);
        initQuickscroll();
    }

    private void initQuickscroll() {
        quickScroll.init(QuickScroll.TYPE_POPUP_WITH_HANDLE, listView_my_music, myMusicListAdapter, QuickScroll.STYLE_HOLO);
        quickScroll.setFixedSize(1);
        quickScroll.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 48);
        quickScroll.setPopupColor(QuickScroll.BLUE_LIGHT, QuickScroll.BLUE_LIGHT_SEMITRANSPARENT,
                1, Color.WHITE, 1);
    }

    @Override
    public void onResume() {
        super.onResume();
        mainActivity.bindPlayService();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("mydebug", "onPause");
        mainActivity.unBindPlayService(); //解除绑定播放服务
    }

    /**
     * 列表项的点击事件相应
     */
    public OnItemClickListener onItemClickListener = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if(mainActivity.playService.getNowMusicList() != PlayService.MY_MUSIC_LIST) {
                mainActivity.playService.setMp3List(mp3list);
                mainActivity.playService.setNowMusicList(PlayService.MY_MUSIC_LIST);
            }
            Log.d("mydebug", "OnItemClickListener you click " + position);
            mainActivity.playService.musicPlay(position);
        }
    };

    /**
     * 回调播放状态下的UI改变
     * @param position
     */

    public void changeUIStatusOnPlay(int position) {
        Log.d("mydebug", "changeUIStatusOnPlay you click " + position);
        if(position >= 0 && position < mainActivity.playService.mp3List.size()) {
            Mp3Info mp3Info = mainActivity.playService.mp3List.get(position);
            textView_songer.setText(mp3Info.getArtist());
            textView_songName.setText(mp3Info.getTitle());
            if(mainActivity.playService.isPlaying()) {
                imageView_play_pause.setImageResource(R.mipmap.player_btn_pause_normal);
            } else {
                imageView_play_pause.setImageResource(R.mipmap.player_btn_play_normal);
            }

            Bitmap albumBitmap = MediaUtils.getArtwork(mainActivity, mp3Info.getId(),
                    mp3Info.getAlbumId(), false, true);
            if (albumBitmap == null) {
                Log.d("mydebug", "bitmap is null");
            } else {
                Log.d("mydebug", "bitmap not null");
            }
            imageView_album.setImageBitmap(albumBitmap);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageView_play_pause: {
                if (mainActivity.playService.isPlaying()) {
                    mainActivity.playService.musicPause();
                    imageView_play_pause.setImageResource(R.mipmap.player_btn_play_normal);
                } else if (mainActivity.playService.isPause()) {
                    mainActivity.playService.musicStart();
                    imageView_play_pause.setImageResource(R.mipmap.player_btn_pause_normal);
                } else {
                    mainActivity.playService.musicPlay(mainActivity.playService.getCurrentPositionInMp3list());
                    imageView_play_pause.setImageResource(R.mipmap.player_btn_pause_normal);
                }
                break;
            }
            case R.id.play_imageView_next: {
                mainActivity.playService.musicNext();
                break;
            }
            case R.id.imageView_album: {
                Intent intent = new Intent(mainActivity, PlayActivity.class);
                startActivity(intent);
                break;
            }
        }
        changeUIStatusOnPlay(mainActivity.playService.getCurrentPositionInMp3list());
    }
}
