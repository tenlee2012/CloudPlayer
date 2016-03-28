package com.tenlee.cloudplayer.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.tenlee.cloudplayer.activity.MainActivity;
import com.tenlee.cloudplayer.entity.Song;
import com.tenlee.cloudplayer.utils.DownloadUtils;

import java.io.File;

/**
 * Created by tenlee on 16-3-28.
 */
public class DownloadDialogFragment extends DialogFragment {

    private Song song; //当前要下载的歌曲对象
    private MainActivity mainActivity;
    private String[] items;  //对话框显示选项

    public static DownloadDialogFragment newInstance(Song song) {
        DownloadDialogFragment downloadDialogFragment = new DownloadDialogFragment();
        downloadDialogFragment.song = song;
        return downloadDialogFragment;
    }

    @Override
    public void onAttach(Context context) { //先执行
        super.onAttach(context);
        mainActivity = (MainActivity) getActivity();
        items = new String[] {"下载低品质", "下载中品质", "下载高品质", "取消"};
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
        builder.setCancelable(true);
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        downloadMusic(0);
                        break;
                    case 1:
                        downloadMusic(1);
                        break;
                    case 2:
                        downloadMusic(2);
                        break;
                    case 3:
                        dialog.dismiss();
                        break;
                }
            }
        });
        return builder.show();
    }

    public void downloadMusic(int which) {
        Toast.makeText(mainActivity, "正在下载..." + song.getSongName(), Toast.LENGTH_SHORT).show();
        DownloadUtils.getsInstance().setListener(new DownloadUtils.OnDownloadListener() {
            @Override
            public void onDownload(String mp3Url) {
                Toast.makeText(mainActivity, "下载成功", Toast.LENGTH_SHORT).show();
                //扫描新下载的歌曲
//                Uri contentUri = Uri.fromFile(new File(mp3Url));
//                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
//                        contentUri);
//                getContext().sendBroadcast(mediaScanIntent);
            }

            @Override
            public void onFailed(String error) {
                Toast.makeText(mainActivity, "下载失败", Toast.LENGTH_SHORT).show();
            }
        }).download(song);
    }
}
