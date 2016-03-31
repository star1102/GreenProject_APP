package com.mp3.listenit;

/**
 * Created by YDJ on 2016-03-16.
 */
public class uploadItem {

    private String singer;
    private String music;
    private String file;
    private String date;

    public uploadItem(String t, String m, String f, String d) {
        singer = t;
        music = m;
        file = f;
        date = d;

    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getMusic() {
        return music;
    }

    public void setMusic(String music) {
        this.music = music;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}