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
import com.tenlee.cloudplayer.entity.Song;
import com.tenlee.cloudplayer.utils.MediaUtils;

import java.util.ArrayList;

/**
 * Created by tenlee on 16-3-26.
 */
public class NetMusicAdapter extends BaseAdapter implements Scrollable {

    private Context context;
    private ArrayList<Song> songList;

    public NetMusicAdapter() {}

    public NetMusicAdapter(Context context, ArrayList<Song> songList) {
        this.context = context;
        this.songList = songList;
    }

    public ArrayList<Song> getSongList() {
        return songList;
    }

    public void setSongList(ArrayList<Song> songList) {
        this.songList = songList;
    }

    @Override
    public int getCount() {
        return songList.size();
    }

    @Override
    public Object getItem(int position) {
        return songList.get(position);
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
                    R.layout.net_item_music_list, null);

            vh = new ViewHolder();
            vh.textView_title = (TextView) convertView.findViewById(R.id.net_textView_title);
            vh.textView_singer = (TextView) convertView.findViewById(R.id.net_textView_singer);
            convertView.setTag(vh);
        }
        vh = (ViewHolder) convertView.getTag();
        Song mp3info = songList.get(position);
        vh.textView_singer.setText(mp3info.getArtist());
        vh.textView_title.setText(mp3info.getSongName());
        return convertView;
    }

    @Override
    public String getIndicatorForPosition(int childposition, int groupposition) {
        return Character.toString(songList.get(childposition).getSongName().charAt(0));
    }

    @Override
    public int getScrollPosition(int childposition, int groupposition) {
        return childposition;
    }
    static public class ViewHolder {
        TextView textView_title;
        TextView textView_singer;
    }
}
