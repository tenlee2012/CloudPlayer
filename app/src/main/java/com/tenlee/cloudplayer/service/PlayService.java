package com.tenlee.cloudplayer.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.tenlee.cloudplayer.MusicApplication;
import com.tenlee.cloudplayer.entity.Mp3Info;
import com.tenlee.cloudplayer.utils.Constant;
import com.tenlee.cloudplayer.utils.MediaUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 音乐播放的服务组件
 * 实现功能: 播放,暂停,上/下一首,获取当前歌曲播放进度
 */

public class PlayService extends Service implements  MediaPlayer.OnCompletionListener,
            MediaPlayer.OnErrorListener{

    private MediaPlayer mPlayer;
    private int currentPositionInMp3list; //当前正在播放的歌曲位置
    public ArrayList<Mp3Info> mp3List;
    private boolean isPause = false;
    public final static int MY_MUSIC_LIST = 1;
    public final static int LOVE_MUSIC_LIST = 2;
    public int nowMusicList = MY_MUSIC_LIST; //当期播放的音乐列表

    private Random random;

    public static final int ORDER_PLAY = 1; //顺序播放
    public static final int RANDOM_PLAY = 2; //随机播放
    public static final int SINGE_PLAY = 3; //单曲循环
    private int play_mode = ORDER_PLAY; //播放模式，比如单曲循环等


    //单例线程池
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    private MusicUpdateListener musicUpdateListener;

    public PlayService() {
    }

    public int getNowMusicList() {
        return nowMusicList;
    }

    public void setNowMusicList(int nowMusicList) {
        this.nowMusicList = nowMusicList;
    }

    public int getPlay_mode() {
        return play_mode > 0 ? play_mode : 1;
    }

    /**
     *
     * @param play_mode
     * ORDER_PLAY = 1
     * RANDOM_PLAY = 2
     * SINGE_PLAY = 3
     */
    public void setPlay_mode(int play_mode) {
        this.play_mode = play_mode;
    }

    /**
     * 是否处于暂停
     * @return
     */
    public boolean isPause() {
        return isPause;
    }

    /**
     * 是否处于播放，isPuase为false时候并不一定播放
     * @return
     */
    public boolean isPlaying() {
        if(mPlayer != null) {
            return mPlayer.isPlaying();
        }
        return false;
    }

    public class PlayBinder extends Binder{
        public PlayService getPlayService() {
            return PlayService.this;
        }

    }
    @Override
    public IBinder onBind(Intent intent) {
//        throw new UnsupportedOperationException("Not yet implemented");
        return new PlayBinder();
    }

    /**
     * 服务创建时调用
     */
    @Override
    public void onCreate() {
        super.onCreate();
        mPlayer = new MediaPlayer();
        mp3List = MediaUtils.getMp3Infos(this);

        currentPositionInMp3list = -1;
        isPause = false;
        random = new Random();

        currentPositionInMp3list = MusicApplication.sp.getInt(Constant.CURRENTPOSITIONINMP3LIST, 0);
        //保证一定在123之中
        play_mode = MusicApplication.sp.getInt(Constant.PLAY_MODE, PlayService.ORDER_PLAY) % 3 + 1;
        Log.d("mydebug", "play service play mode is " + play_mode);

        mPlayer.setOnCompletionListener(this); //注册监听事件
        mPlayer.setOnErrorListener(this); //注册监听事件

        executorService.execute(updateStatusRunnable);//线程池执行,更新进度
    }

    Runnable updateStatusRunnable = new Runnable() {
        @Override
        public void run() {
            while (true) {
                if (musicUpdateListener != null && mPlayer != null && mPlayer.isPlaying()) {
                    musicUpdateListener.onPublish(getCurrentPositionOfMp3());
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
            executorService = null;
        }
        mPlayer = null;
        mp3List = null;
        musicUpdateListener = null;
    }

    public ArrayList<Mp3Info> getMp3List() {
        return mp3List;
    }

    public void setMp3List(ArrayList<Mp3Info> mp3List) {
        this.mp3List = mp3List;
    }

    /**
     * 返回当前播放歌曲在播放列表中的位置
     * @return
     */
    public int getCurrentPositionInMp3list() {
        return currentPositionInMp3list;
    }

    /**
     * 返回歌曲当前播放的进度
     * @return
     */
    public int getCurrentPositionOfMp3() {
        return mPlayer.getCurrentPosition();
    }

    /**
     * 播放歌曲，从头开始播放
     * @param position
     */
    public void musicPlay(int position) {
        isPause = false;
//        if (position == currentPositionInMp3list) { //如果点即相同的音乐,则暂停/播放
//            if(mPlayer.isPlaying()){
//                musicPause(); //正在播放的时候暂停
//            } else {
//                musicStart(); //正在暂停的时候播放
//            }
//            if (musicUpdateListener != null) {
//                musicUpdateListener.onChange(getCurrentPositionInMp3list());
//            }
//            return;
//        }

        if (position >= 0 && position < mp3List.size()) {
            Mp3Info mp3Info = mp3List.get(position);

            try {
                mPlayer.reset();
                mPlayer.setDataSource(this, Uri.parse(mp3Info.getUrl()));
                mPlayer.prepare();
                mPlayer.start();
                currentPositionInMp3list = position;
            } catch (IOException e) {
                Log.d("mydebug", e.toString());
            }
            if (musicUpdateListener != null) {
                musicUpdateListener.onChange(getCurrentPositionInMp3list());
            }
        }
    }

    /**
     * 从暂停状态开始播放
     */
    public void musicStart() {
        if(mPlayer != null && !mPlayer.isPlaying()) {
            mPlayer.start();
            isPause = false;
        }
    }

    /**
     * 歌曲暂停
     */
    public void musicPause() {
        if(mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.pause();
            isPause = true;
        }
    }

    /**
     * 下一首歌曲
     */
    public void musicNext() {
        whatNextPlay();
    }

    /**
     * 上一首歌曲
     */
    public void musicPrev() {
        int posttion = (currentPositionInMp3list - 1 + mp3List.size()) % (mp3List.size());
        musicPlay(posttion);
    }

    /**
     * 获得歌曲的时间
     * @return
     */
    public int getDuration() {
        return mPlayer.getDuration();
    }

    public void seekTo(int msec) {
        mPlayer.seekTo(msec);
    }
    /**
     * 更新状态的接口
     */
    public interface MusicUpdateListener {
        public void onPublish(int progress);
        public void onChange(int position);
    }
    //音乐界面更新监听器
    public void setMusicUpdateListener(MusicUpdateListener musicUpdateListener) {
        this.musicUpdateListener = musicUpdateListener;
    }

    /**
     * 下一个要播放什么
     */
    public void whatNextPlay() {
        switch (play_mode) {
            case ORDER_PLAY:
                int posttion = (currentPositionInMp3list + 1) % (mp3List.size());
                musicPlay(posttion);
                break;
            case RANDOM_PLAY:
                musicPlay(random.nextInt(mp3List.size()));
                break;
            case SINGE_PLAY:
                musicPlay(currentPositionInMp3list);
                break;
            default:
                break;
        }
    }
    //OnCompletionListener接口须实现
    @Override
    public void onCompletion(MediaPlayer mp) {
        whatNextPlay();
    }
    //OnErrorListener接口须实现
    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mp.reset();
        return false;
    }

}
