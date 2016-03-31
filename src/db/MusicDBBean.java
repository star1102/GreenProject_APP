package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class MusicDBBean {
static MusicDBBean instance = new MusicDBBean();
	
	public static MusicDBBean getInstance(){
		return instance;
	}
	
	private MusicDBBean(){}
	
	private Connection getConn() throws Exception{
		Context initCtx = new InitialContext();
		Context envCtx = (Context)initCtx.lookup("java:comp/env");
		DataSource ds = (DataSource)envCtx.lookup("jdbc/project_db");
		Connection conn = ds.getConnection();
		return conn;		
	}
	
	public int getMaxID(){
		int val = 0;
		String sql = "SELECT Max(id) FROM MP3";
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			conn = getConn();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if(rs.next())
				val = rs.getInt(1);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(conn != null)conn.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		return val;
	}
	
	public int getMP3Count() {
		int val = 0;
		
		String sql = "SELECT count(id) FROM mp3";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			conn = getConn();
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if(rs.next()){
				val = rs.getInt(1);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(conn != null)conn.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return val;
	}
	
	public int insertMP3(MusicDataBean bean){
		int val = 0;
		String sql = "INSERT INTO mp3 values(?,null,?,?,null,null,?,sysdate)";
		Connection conn = null;
		PreparedStatement pstmt = null;
		try{
			conn = getConn();
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, getMaxID()+1);
			pstmt.setString(2, bean.getSong_name());
			pstmt.setString(3, bean.getSinger_name());
			pstmt.setString(4, "Y");
			val = pstmt.executeUpdate();			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(pstmt != null) pstmt.close();
				if(conn != null)conn.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return val;
	}
	
	public ArrayList<MusicDataBean> getMP3List(int curPage,int aCount){
		
		ArrayList<MusicDataBean> rValue = null;
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT id,song_name,singer_name,upload_date FROM mp3 order by id desc");
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try{
			conn = getConn();
			pstmt = conn.prepareStatement(sql.toString());
			//pstmt.setInt(1, (curPage-1)*aCount + 1);
			//pstmt.setInt(2, curPage * aCount);
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				if(rValue == null) rValue = new ArrayList();
				MusicDataBean bean = new MusicDataBean();
				bean.setId(rs.getInt("id"));
				bean.setSong_name(rs.getString("song_name"));
				bean.setSinger_name(rs.getString("singer_name"));
				bean.setUpload_date((rs.getString("upload_date").substring(0, 10)));
				rValue.add(bean);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(conn != null)conn.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return rValue;
	}
	
}
