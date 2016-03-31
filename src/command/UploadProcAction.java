package command;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import db.MusicDBBean;
import db.MusicDataBean;

public class UploadProcAction implements CommandAction {

	@Override
	public String process(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		
		String path = "D:/music";
		MultipartRequest multi = new MultipartRequest(req, path , 1024*1024*10 , "UTF-8", new DefaultFileRenamePolicy());
		
		String singer_name = multi.getParameter("singer_name");
		String song_name = multi.getParameter("song_name");
		
		MusicDataBean bean = new MusicDataBean();
		
		bean.setSinger_name(singer_name);
		bean.setSong_name(song_name);
		
		MusicDBBean DB = MusicDBBean.getInstance();
		DB.insertMP3(bean);
		
		
		String strPage = req.getParameter("page");
		int intPage = 1;
		if(strPage != null) intPage = Integer.parseInt(strPage);
		
		MusicDBBean dbBean = MusicDBBean.getInstance();
		int totalCount = dbBean.getMP3Count();
		ArrayList<MusicDataBean> list = dbBean.getMP3List(intPage, 5);
		
		//페이지 계산
		int startPage = 1;
		int endPage = totalCount /5;
		endPage += (totalCount % 5 == 0 ? 0 : 1);
		
		//req.setAttribute("page", intPage);
		req.setAttribute("total", totalCount);
		req.setAttribute("list", list);
		//req.setAttribute("start", startPage);
		//req.setAttribute("end", endPage);
		
		return "/index_station.jsp";
	}

}
