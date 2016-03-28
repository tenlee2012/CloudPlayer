package com.tenlee.cloudplayer.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.tenlee.cloudplayer.entity.Song;

import java.io.File;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by tenlee on 16-3-28.
 */
public class DownloadUtils {
    public static final int SUCCESS_LRC = 1;//下载歌词成功
    public static final int FAILED_LRC = 2;//下载歌词失败
    public static final int SUCCESS_MP3 = 3;//下载mp3成功
    public static final int FAILED_MP3 = 4;//下载MP3失败
    public static final int GET_MP3_URL = 5;//获取MP3URL成功
    public static final int GET_FAILED_MP3_URL = 6;//获取MP3URL失败
    public static final int MUSIC_EXISTS = 7;//音乐是否存在

    private static DownloadUtils sInstance;
    private OnDownloadListener mListener;
    private ExecutorService mThreadPool;

    public static Context context;

    /**
     * 设置回调监听器
     * @param mListener
     * @return
     */
    public DownloadUtils setListener(OnDownloadListener mListener) {
        this.mListener = mListener;
        return this;
    }

    public synchronized static DownloadUtils getsInstance() {
//        DownloadUtils.context = context;
        if (sInstance == null) {
            sInstance = new DownloadUtils();
        }
        return sInstance;
    }

    private DownloadUtils() {
        mThreadPool = Executors.newSingleThreadExecutor();
    }

    public void download(final Song searchResult) {
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case SUCCESS_LRC:
                        if (mListener != null) mListener.onDownload("歌词下载成功");
                        break;
                    case FAILED_LRC:
                        if (mListener != null) mListener.onFailed("歌词下载失败");
                        break;
                    case GET_MP3_URL:
                        downloadMusic(searchResult, 0);
                        break;
                    case GET_FAILED_MP3_URL:
                        if (mListener != null) mListener.onFailed("下载失败，该歌曲为收费或VIP类型");
                        break;
                    case SUCCESS_MP3:
                        if (mListener != null)
                            mListener.onDownload(searchResult.getSongName() + "已下载");

                        Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                        scanIntent.setData(Uri.fromFile(new File(searchResult.getSongName())));
                        context.sendBroadcast(scanIntent);
                        downloadLRC(searchResult);
                        break;
                    case FAILED_MP3:
                        if (mListener != null)
                            mListener.onFailed(searchResult.getSongName() + "下载失败");
                        break;
                    case MUSIC_EXISTS:
                        if (mListener != null) mListener.onFailed("音乐已存在");
                        break;
                }
            }

        };
    }

    public void downloadMusic(Song searchResult, int songType) {

    }
    public void downloadLRC(Song searchResult) {

    }
    public void downloadPic(Song searchResult) {

    }



    public interface OnDownloadListener {
        public void onDownload(String mp3Url);

        public void onFailed(String error);
    }
}
