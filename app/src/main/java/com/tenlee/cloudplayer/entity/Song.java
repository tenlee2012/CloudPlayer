package com.tenlee.cloudplayer.entity;

/**
 * Created by tenlee on 16-3-20.
 */
public class Song {

    int id;
    private String SongId;
    private String SongName;
    private String SubTitle;
    private String Artist;
    private String ArtistSubTitle;
    private String AlbumId;
    private String Album;
    private String AlbumSubTitle;
    private String AlbumArtist;
    private String CollectName;
    private String Length;
    private String Size;
    private String BitRate;
    private String FlacUrl;
    private String AacUrl;
    private String SqUrl;
    private String HqUrl;
    private String LqUrl;
    private String ListenUrl;
    private String CopyUrl;
    private String PicUrl;
    private String LrcUrl;
    private String KlokLrc;
    private String MvId;
    private String MvUrl;
    private String VideoUrl;
    private String Language;
    private String Company;
    private String Year;
    private String Disc;
    private String TrackNum;
    private String Type;

    public int getId() {
        return id;
    }

    public String getSongId() {
        return SongId;
    }

    public String getSongName() {
        return SongName;
    }

    public String getSubTitle() {
        return SubTitle;
    }

    public String getArtist() {
        return Artist;
    }

    public String getArtistSubTitle() {
        return ArtistSubTitle;
    }

    public String getAlbumId() {
        return AlbumId;
    }

    public String getAlbum() {
        return Album;
    }

    public String getAlbumSubTitle() {
        return AlbumSubTitle;
    }

    public String getAlbumArtist() {
        return AlbumArtist;
    }

    public String getCollectName() {
        return CollectName;
    }

    public String getLength() {
        return Length;
    }

    public String getSize() {
        return Size;
    }

    public String getBitRate() {
        return BitRate;
    }

    public String getFlacUrl() {
        return FlacUrl;
    }

    public String getAacUrl() {
        return AacUrl;
    }

    public String getSqUrl() {
        return SqUrl;
    }

    public String getHqUrl() {
        return HqUrl;
    }

    public String getLqUrl() {
        return LqUrl;
    }

    public String getListenUrl() {
        return ListenUrl;
    }

    public String getCopyUrl() {
        return CopyUrl;
    }

    public String getPicUrl() {
        return PicUrl;
    }

    public String getLrcUrl() {
        return LrcUrl;
    }

    public String getKlokLrc() {
        return KlokLrc;
    }

    public String getMvId() {
        return MvId;
    }

    public String getMvUrl() {
        return MvUrl;
    }

    public String getVideoUrl() {
        return VideoUrl;
    }

    public String getLanguage() {
        return Language;
    }

    public String getCompany() {
        return Company;
    }

    public String getYear() {
        return Year;
    }

    public String getDisc() {
        return Disc;
    }

    public String getTrackNum() {
        return TrackNum;
    }

    public String getType() {
        return Type;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSongId(String songId) {
        SongId = songId;
    }

    public void setSubTitle(String subTitle) {
        SubTitle = subTitle;
    }

    public void setSongName(String songName) {
        SongName = songName;
    }

    public void setArtist(String artist) {
        Artist = artist;
    }

    public void setArtistSubTitle(String artistSubTitle) {
        ArtistSubTitle = artistSubTitle;
    }

    public void setAlbumId(String albumId) {
        AlbumId = albumId;
    }

    public void setAlbum(String album) {
        Album = album;
    }

    public void setAlbumSubTitle(String albumSubTitle) {
        AlbumSubTitle = albumSubTitle;
    }

    public void setAlbumArtist(String albumArtist) {
        AlbumArtist = albumArtist;
    }

    public void setCollectName(String collectName) {
        CollectName = collectName;
    }

    public void setLength(String length) {
        Length = length;
    }

    public void setSize(String size) {
        Size = size;
    }

    public void setBitRate(String bitRate) {
        BitRate = bitRate;
    }

    public void setFlacUrl(String flacUrl) {
        FlacUrl = flacUrl;
    }

    public void setAacUrl(String aacUrl) {
        AacUrl = aacUrl;
    }

    public void setSqUrl(String sqUrl) {
        SqUrl = sqUrl;
    }

    public void setHqUrl(String hqUrl) {
        HqUrl = hqUrl;
    }

    public void setLqUrl(String lqUrl) {
        LqUrl = lqUrl;
    }

    public void setListenUrl(String listenUrl) {
        ListenUrl = listenUrl;
    }

    public void setCopyUrl(String copyUrl) {
        CopyUrl = copyUrl;
    }

    public void setPicUrl(String picUrl) {
        PicUrl = picUrl;
    }

    public void setLrcUrl(String lrcUrl) {
        LrcUrl = lrcUrl;
    }

    public void setKlokLrc(String klokLrc) {
        KlokLrc = klokLrc;
    }

    public void setMvId(String mvId) {
        MvId = mvId;
    }

    public void setMvUrl(String mvUrl) {
        MvUrl = mvUrl;
    }

    public void setVideoUrl(String videoUrl) {
        VideoUrl = videoUrl;
    }

    public void setLanguage(String language) {
        Language = language;
    }

    public void setCompany(String company) {
        Company = company;
    }

    public void setYear(String year) {
        Year = year;
    }

    public void setDisc(String disc) {
        Disc = disc;
    }

    public void setTrackNum(String trackNum) {
        TrackNum = trackNum;
    }

    public void setType(String type) {
        Type = type;
    }

    @Override
    public String toString() {
        return "Song{" +
                "id=" + id +
                ", SongId=" + SongId +
                ", SongName='" + SongName + '\'' +
                ", SubTitle='" + SubTitle + '\'' +
                ", Artist='" + Artist + '\'' +
                ", ArtistSubTitle='" + ArtistSubTitle + '\'' +
                ", AlbumId=" + AlbumId +
                ", Album='" + Album + '\'' +
                ", AlbumSubTitle='" + AlbumSubTitle + '\'' +
                ", AlbumArtist='" + AlbumArtist + '\'' +
                ", CollectName='" + CollectName + '\'' +
                ", Length='" + Length + '\'' +
                ", Size='" + Size + '\'' +
                ", BitRate='" + BitRate + '\'' +
                ", FlacUrl='" + FlacUrl + '\'' +
                ", AacUrl='" + AacUrl + '\'' +
                ", SqUrl='" + SqUrl + '\'' +
                ", HqUrl='" + HqUrl + '\'' +
                ", LqUrl='" + LqUrl + '\'' +
                ", ListenUrl='" + ListenUrl + '\'' +
                ", CopyUrl='" + CopyUrl + '\'' +
                ", PicUrl='" + PicUrl + '\'' +
                ", LrcUrl='" + LrcUrl + '\'' +
                ", KlokLrc='" + KlokLrc + '\'' +
                ", MvId='" + MvId + '\'' +
                ", MvUrl='" + MvUrl + '\'' +
                ", VideoUrl='" + VideoUrl + '\'' +
                ", Language='" + Language + '\'' +
                ", Company='" + Company + '\'' +
                ", Year='" + Year + '\'' +
                ", Disc='" + Disc + '\'' +
                ", TrackNum='" + TrackNum + '\'' +
                ", Type='" + Type + '\'' +
                '}';
    }
}
