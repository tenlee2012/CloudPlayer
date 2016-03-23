package com.tenlee.cloudplayer.entity;

import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;

/**
 * Created by tenlee on 16-3-2.
 */
public class Mp3Info {

    @Id
    @NoAutoIncrement
    private long id;
    private String title;
    private String album; //专辑
    private String artist; //艺术家
    private long albumId; //
    private long duration; //时长
    private long size; //大小
    private String url; //路径
    private int isMusic; //是否为音乐

    public void setId(long id) { this.id = id;}

    public void setTitle(String title) { this.title = title; }

    public void setAlbum(String album) { this.album = album; }

    public void setArtist(String artist) { this.artist = artist; }

    public void setAlbumId(long albumId) { this.albumId = albumId; }

    public void setDuration(long duration) { this.duration = duration; }

    public void setSize(long size) { this.size = size; }

    public void setUrl(String url) { this.url = url; }

    public void setIsMusic(int isMusic) { this.isMusic = isMusic; }

    public long getId() { return id; }

    public String getTitle() { return title; }

    public String getAlbum() { return album; }

    public String getArtist() { return artist; }

    public long getAlbumId() { return albumId; }

    public long getDuration() { return duration; }

    public long getSize() { return size; }

    public String getUrl() { return url; }

    public int getIsMusic() { return isMusic; }

    @Override
    public String toString() {
        return "Mp3Info{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", album='" + album + '\'' +
                ", artist='" + artist + '\'' +
                ", albumId=" + albumId +
                ", duration=" + duration +
                ", size=" + size +
                ", url='" + url + '\'' +
                ", isMusic=" + isMusic +
                '}';
    }
}
