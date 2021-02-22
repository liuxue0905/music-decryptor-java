
package com.lx.musicdump.music.neteast;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

public class Music {

    @SerializedName("musicId")
    @Expose
    private Integer musicId;
    @SerializedName("musicName")
    @Expose
    private String musicName;
    @SerializedName("artist")
    @Expose
    private List<List<String>> artist = null;
    @SerializedName("albumId")
    @Expose
    private Integer albumId;
    @SerializedName("album")
    @Expose
    private String album;
    @SerializedName("albumPicDocId")
    @Expose
    private String albumPicDocId;
    @SerializedName("albumPic")
    @Expose
    private String albumPic;
    @SerializedName("bitrate")
    @Expose
    private Integer bitrate;
    @SerializedName("mp3DocId")
    @Expose
    private String mp3DocId;
    @SerializedName("duration")
    @Expose
    private Long duration;
    @SerializedName("mvId")
    @Expose
    private Integer mvId;
    @SerializedName("alias")
    @Expose
    private List<String> alias = null;
    @SerializedName("transNames")
    @Expose
    private List<String> transNames = null;
    @SerializedName("format")
    @Expose
    private String format;

    public Integer getMusicId() {
        return musicId;
    }

    public void setMusicId(Integer musicId) {
        this.musicId = musicId;
    }

    public String getMusicName() {
        return musicName;
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName;
    }

    public List<List<String>> getArtist() {
        return artist;
    }

    public void setArtist(List<List<String>> artist) {
        this.artist = artist;
    }

    public Integer getAlbumId() {
        return albumId;
    }

    public void setAlbumId(Integer albumId) {
        this.albumId = albumId;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getAlbumPicDocId() {
        return albumPicDocId;
    }

    public void setAlbumPicDocId(String albumPicDocId) {
        this.albumPicDocId = albumPicDocId;
    }

    public String getAlbumPic() {
        return albumPic;
    }

    public void setAlbumPic(String albumPic) {
        this.albumPic = albumPic;
    }

    public Integer getBitrate() {
        return bitrate;
    }

    public void setBitrate(Integer bitrate) {
        this.bitrate = bitrate;
    }

    public String getMp3DocId() {
        return mp3DocId;
    }

    public void setMp3DocId(String mp3DocId) {
        this.mp3DocId = mp3DocId;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public Integer getMvId() {
        return mvId;
    }

    public void setMvId(Integer mvId) {
        this.mvId = mvId;
    }

    public List<String> getAlias() {
        return alias;
    }

    public void setAlias(List<String> alias) {
        this.alias = alias;
    }

    public List<String> getTransNames() {
        return transNames;
    }

    public void setTransNames(List<String> transNames) {
        this.transNames = transNames;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("musicId", musicId)
                .append("musicName", musicName)
                .append("artist", artist)
                .append("albumId", albumId)
                .append("album", album)
                .append("albumPicDocId", albumPicDocId)
                .append("albumPic", albumPic)
                .append("bitrate", bitrate)
                .append("mp3DocId", mp3DocId)
                .append("duration", duration)
                .append("mvId", mvId)
                .append("alias", alias)
                .append("transNames", transNames)
                .append("format", format)
                .toString();
    }
}
