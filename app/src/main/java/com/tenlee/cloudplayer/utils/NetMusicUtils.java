package com.tenlee.cloudplayer.utils;

import android.util.Log;

import com.tenlee.cloudplayer.entity.Song;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tenlee on 16-3-27.
 */
public class NetMusicUtils {
    public static String BAIDU_MUSIC_URL = "http://music.baidu.com/";
    public static String BAIDU_MUSIC_DAYHOT_URL = BAIDU_MUSIC_URL + "top/dayhot/";
    public static String BAIDU_MUSIC_NEW_URL = BAIDU_MUSIC_URL + "top/dayhot/";
    public static String MUSICUU_BD_SONG_URL = "http://api.musicuu.com/music/songurl/bd_";
    public static String MUSICUU_BD_PIC_URL = "http://api.musicuu.com/music/songurl/bd_";


    static public ArrayList getDayHotMusic(String url) {
        ArrayList<Song> songList = new ArrayList<Song>();
        try {
            Document doc = Jsoup.connect(url).
                    userAgent(Constant.USER_AGENT).timeout(6 * 1000).get();
//            System.out.println(doc);
            Elements songTitles = doc.select("span.song-title ");
            Elements artists = doc.select("span.author_list");
            int size = songTitles.size();
            Log.d("mydebug", size + "-->" + artists.size());
            for (int i = 0; i < size; i++) {
                if(i >= 20) break; //最多显示20条数据
                Elements urls = songTitles.get(i).getElementsByTag("a");
                Elements arts = artists.get(i).getElementsByTag("a");
                String songUrl = urls.get(0).attr("href");
                if(!songUrl.startsWith("/song") || songUrl.length() != 15) continue; //过滤掉不合法的

                String songName = urls.get(0).text();
                String art = arts.text();
                Song song = new Song();
                String songId = songUrl.substring(6, 15);

                song.setSongId(songId);
                song.setArtist(art);
                song.setSongName(songName);
                song.setSqUrl(MUSICUU_BD_SONG_URL + "320_" + songId + ".mp3");
                song.setHqUrl(MUSICUU_BD_SONG_URL + "256_" + songId + ".mp3");
                song.setLqUrl(MUSICUU_BD_SONG_URL + "128_" + songId + ".mp3");
                song.setPicUrl(MUSICUU_BD_PIC_URL+"hd_"+songId+".jpg");


                songList.add(song);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
//        Log.d("mydebug", "getDayHotMusic" + songList.toString());
        return songList;
    }

    static public ArrayList<Song> searchMusic(String type, String key) {
        ArrayList<Song> songList = new ArrayList<>();
        return songList;
    }

}
