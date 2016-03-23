package com.tenlee.cloudplayer.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.exception.DbException;
import com.tenlee.cloudplayer.MusicApplication;
import com.tenlee.cloudplayer.R;
import com.tenlee.cloudplayer.entity.Mp3Info;
import com.tenlee.cloudplayer.adapter.ViewPagerAdapter;
import com.tenlee.cloudplayer.service.PlayService;
import com.tenlee.cloudplayer.utils.MediaUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class PlayActivity extends BaseActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener{

    private static final int START_PLAY = 0x2;

    private TextView play_textView_title, play_textView_start_time, play_textView_end_time;
    private ImageView play_imageView_album, play_imageView_next, play_imageView_pause,
            play_imageView_pre, play_imageView_play_mode, play_imageView_favorite;
    private SeekBar seekBar;
    private static final  int UPDATE_TIME = 0x1; //更新播放进度的标记

    private ViewPager viewPager;
    private List<View> listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_play);

        initView();
        initViewPager();
        setListener();

        myHandler = new MyHandler(this);
    }

    private void initViewPager() {
        listView = new ArrayList<View>();
        View album_image_view = getLayoutInflater().inflate(R.layout.album_image_layout, null);
        play_imageView_album = (ImageView) album_image_view.findViewById(R.id.play_imageView_album);
        play_textView_title = (TextView) album_image_view.findViewById(R.id.play_textView_title);
        listView.add(album_image_view);

        View lrc_view = getLayoutInflater().inflate(R.layout.lrc_layout, null);
        listView.add(lrc_view);

        viewPager.setAdapter(new ViewPagerAdapter(listView));
    }

    private void initView() {
        play_textView_start_time = (TextView) findViewById(R.id.play_textView_start_time);
        play_textView_end_time = (TextView) findViewById(R.id.play_textView_end_time);
        play_imageView_next = (ImageView) findViewById(R.id.play_imageView_next);
        play_imageView_pause = (ImageView) findViewById(R.id.play_imageView_pause);
        play_imageView_pre = (ImageView) findViewById(R.id.play_imageView_pre);
        play_imageView_play_mode = (ImageView) findViewById(R.id.play_imageView_play_mode);
        play_imageView_favorite = (ImageView) findViewById(R.id.play_imageView_favorite);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
    }

    private void setListener() {
        play_imageView_next.setOnClickListener(this);
        play_imageView_pause.setOnClickListener(this);
        play_imageView_pre.setOnClickListener(this);
        play_imageView_play_mode.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(this);
        play_imageView_favorite.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        bindPlayService();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("mydebug", "playactivity onPause");
        unBindPlayService();
    }

    @Override //seekbar的方法
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if(fromUser) { //是否来自用户的拖动
            Log.d("mydebug", "seekbar change from user");
            playService.musicPause();
            playService.seekTo(progress);
            playService.musicStart();
        }
    }

    @Override //seekBar
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override //seekBar
    public void onStopTrackingTouch(SeekBar seekBar) {

    }


    private static MyHandler myHandler;

    static public class MyHandler extends Handler {

        private WeakReference<PlayActivity> weakReference;
        private PlayActivity playActivity;

        public MyHandler(PlayActivity activity) {
            weakReference = new WeakReference<PlayActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            playActivity = weakReference.get();
            if (playActivity != null) {
                switch (msg.what) {
                    case UPDATE_TIME:
                        playActivity.play_textView_start_time.setText(
                                MediaUtils.formatTime(msg.arg1)
                        );
                        break;
                }
            }
        }
    }

    @Override
    public void publish(int progress) {
//        play_textView_start_time.setText(MediaUtils.formatTime(progress));
        Message msg = myHandler.obtainMessage(UPDATE_TIME);
        msg.arg1 = progress;
        myHandler.sendMessage(msg);
        seekBar.setProgress(progress);
    }

    @Override
    public void change(int position) {
        Log.d("mydebug", "playActivity change" + position);
        if (position >= 0 && position < playService.mp3List.size()) {
            Mp3Info mp3Info = playService.mp3List.get(position);
            play_textView_title.setText(mp3Info.getTitle());
            Bitmap albumBitmap = MediaUtils.getArtwork(this, mp3Info.getId(),
                    mp3Info.getAlbumId(), true, true);
            play_imageView_album.setImageBitmap(albumBitmap);
            play_textView_end_time.setText(MediaUtils.formatTime(mp3Info.getDuration()));
            if (playService.isPlaying())
                play_imageView_pause.setImageResource(R.mipmap.player_btn_pause_normal);
            else
                play_imageView_pause.setImageResource(R.mipmap.player_btn_play_normal);
            seekBar.setProgress(0);
            seekBar.setMax((int) mp3Info.getDuration());
        } else {
            play_imageView_pause.setImageResource(R.mipmap.player_btn_pause_normal);
        }
        Log.d("mydebug", "play activity play mode is " + playService.getPlay_mode());
        switch (playService.getPlay_mode()) {
            case PlayService.ORDER_PLAY:
                play_imageView_play_mode.setImageResource(R.mipmap.order);
                play_imageView_play_mode.setTag(PlayService.ORDER_PLAY);
                break;
            case PlayService.RANDOM_PLAY:
                play_imageView_play_mode.setImageResource(R.mipmap.random);
                play_imageView_play_mode.setTag(PlayService.RANDOM_PLAY);
                break;
            case PlayService.SINGE_PLAY:
                play_imageView_play_mode.setImageResource(R.mipmap.single);
                play_imageView_play_mode.setTag(PlayService.SINGE_PLAY);
                break;
            default:
                break;
        }
        Mp3Info mp3Info = playService.mp3List.get(playService.getCurrentPositionInMp3list());
        try {
            Mp3Info likeMp3 = MusicApplication.dbUtils.findById(Mp3Info.class,
                    mp3Info.getId());
            if (likeMp3 == null) {
                play_imageView_favorite.setImageResource(R.mipmap.xin_bai);
            } else {
                play_imageView_favorite.setImageResource(R.mipmap.xin_hong);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.play_imageView_pause: {
                if (playService.isPlaying()) {
                    play_imageView_pause.setImageResource(R.mipmap.player_btn_play_normal);
                    playService.musicPause();
                } else if (playService.isPause()) {
                    play_imageView_pause.setImageResource(R.mipmap.player_btn_pause_normal);
                    playService.musicStart();
                } else {
                    playService.musicPlay(0);
                }
                break;
            }
            case R.id.play_imageView_next: {
                playService.musicNext();
                break;
            }
            case R.id.play_imageView_pre: {
                playService.musicNext();
                break;
            }
            case R.id.play_imageView_play_mode: {
                Log.d("mydebug", "you click play mode");
                int mode = (int) play_imageView_play_mode.getTag();
                switch (mode) {
                    case PlayService.SINGE_PLAY:
                        play_imageView_play_mode.setImageResource(R.mipmap.order);
                        play_imageView_play_mode.setTag(PlayService.ORDER_PLAY);
                        playService.setPlay_mode(PlayService.ORDER_PLAY);
                        Toast.makeText(this, getString(R.string.order_play),
                                Toast.LENGTH_SHORT).show();
                        break;
                    case PlayService.ORDER_PLAY:
                        play_imageView_play_mode.setImageResource(R.mipmap.random);
                        play_imageView_play_mode.setTag(PlayService.RANDOM_PLAY);
                        playService.setPlay_mode(PlayService.RANDOM_PLAY);
                        Toast.makeText(this, getString(R.string.random_play),
                                Toast.LENGTH_SHORT).show();
                        break;
                    case PlayService.RANDOM_PLAY:
                        play_imageView_play_mode.setImageResource(R.mipmap.single);
                        play_imageView_play_mode.setTag(PlayService.SINGE_PLAY);
                        playService.setPlay_mode(PlayService.SINGE_PLAY);
                        Toast.makeText(this, getString(R.string.single_play),
                                Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
                break;
            }
            case R.id.play_imageView_favorite: {
                Log.d("mydebug", "play_imageView_favorite");
                Mp3Info mp3Info = playService.mp3List.get(playService.getCurrentPositionInMp3list());
                try {
                    Mp3Info likeMp3 = MusicApplication.dbUtils.findById(Mp3Info.class,
                            mp3Info.getId());
                    Log.d("mydebug", mp3Info.toString());
                    if (likeMp3 == null) {
                        MusicApplication.dbUtils.save(mp3Info);
                        play_imageView_favorite.setImageResource(R.mipmap.xin_hong);
                        Log.d("mydebug", "save");
                    } else {
                        MusicApplication.dbUtils.deleteById(Mp3Info.class, mp3Info.getId());
                        play_imageView_favorite.setImageResource(R.mipmap.xin_bai);
                        Log.d("mydebug", "delete");
                    }

                } catch (DbException e) {
                    e.printStackTrace();
                }
                break;
            }
            default:
                break;
        }
    }
}
