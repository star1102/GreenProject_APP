package db;

public class MusicDataBean {
	private int id;
	private String genre;
	private String song_name;
	private String singer_name;
	private String album_name;
	private String track_no;
	private String album_img;
	private String upload_date;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getGenre() {
		return genre;
	}
	public void setGenre(String genre) {
		this.genre = genre;
	}
	public String getSong_name() {
		return song_name;
	}
	public void setSong_name(String song_name) {
		this.song_name = song_name;
	}
	public String getSinger_name() {
		return singer_name;
	}
	public void setSinger_name(String singer_name) {
		this.singer_name = singer_name;
	}
	public String getAlbum_name() {
		return album_name;
	}
	public void setAlbum_name(String album_name) {
		this.album_name = album_name;
	}
	public String getTrack_no() {
		return track_no;
	}
	public void setTrack_no(String track_no) {
		this.track_no = track_no;
	}
	public String getAlbum_img() {
		return album_img;
	}
	public void setAlbum_img(String album_img) {
		this.album_img = album_img;
	}
	public String getUpload_date() {
		return upload_date;
	}
	public void setUpload_date(String upload_date) {
		this.upload_date = upload_date;
	}
	
	
}
