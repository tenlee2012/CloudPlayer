package com.tenlee.cloudplayer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.andraskindler.quickscroll.Scrollable;
import com.tenlee.cloudplayer.R;
import com.tenlee.cloudplayer.entity.Mp3Info;
import com.tenlee.cloudplayer.utils.MediaUtils;

import java.util.ArrayList;

/**
 * Created by tenlee on 16-3-2.
 */
public class MyMusicListAdapter extends BaseAdapter implements Scrollable {

    private Context context;
    private ArrayList<Mp3Info> mp3Infos;

    public MyMusicListAdapter(Context context, ArrayList<Mp3Info> mp3Infos) {
        this.context = context;
        this.mp3Infos = mp3Infos;

    }
    //设置
    public void setMp3Infos(ArrayList<Mp3Info> mp3Infos) {
        this.mp3Infos = mp3Infos;
    }

    @Override
    public int getCount() {
        return mp3Infos.size();
    }

    @Override
    public Object getItem(int position) {
        return mp3Infos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.item_music_list, null);

            vh = new ViewHolder();
            vh.textView_title = (TextView) convertView.findViewById(R.id.textView_title);
            vh.textView_singer = (TextView) convertView.findViewById(R.id.textView_singer);
            vh.textView_time = (TextView) convertView.findViewById(R.id.textView_time);
            convertView.setTag(vh);
        }
        vh = (ViewHolder) convertView.getTag();
        Mp3Info mp3info = mp3Infos.get(position);
        vh.textView_singer.setText(mp3info.getArtist());
        vh.textView_title.setText(mp3info.getTitle());
        vh.textView_time.setText(MediaUtils.formatTime(mp3info.getDuration()));
        return convertView;
    }

    @Override
    public String getIndicatorForPosition(int childposition, int groupposition) {
        return Character.toString(mp3Infos.get(childposition).getTitle().charAt(0));
    }

    @Override
    public int getScrollPosition(int childposition, int groupposition) {
        return childposition;
    }

    static public class ViewHolder {
        TextView textView_title;
        TextView textView_singer;
        TextView textView_time;
    }
}
