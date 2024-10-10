package model2.mvcboard;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import fileupload.FileUtil;

@WebServlet("/mvcboard/download.do")
public class DownloadController extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 매개변수 받기
		String ofile = req.getParameter("ofile");
		String sfile = req.getParameter("sfile");
		String idx = req.getParameter("idx");
		System.out.println(getClass() + " :: doGet() :: ofile, sfile, idx의 매개변수를 가지고 옴");
		
		// 파일 다운로드
		FileUtil.download(req, resp, "/Uploads", sfile, ofile);
		System.out.println(getClass() + " :: doGet() :: FileUtil의 download() 으로 다운로드 시도");
		// 해당 게시물의 다운로드 수 1 증가
		MVCBoardDAO dao = new MVCBoardDAO();
		dao.downloadCountPlus(idx);
		System.out.println(getClass() + " :: doGet() :: DAO에서 idx를 이용해 count를 1 증가");
		dao.close();
	}
}
