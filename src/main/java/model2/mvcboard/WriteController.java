package model2.mvcboard;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oreilly.servlet.MultipartRequest;

import fileupload.FileUtil;
import utils.JSFunction;

public class WriteController extends HttpServlet{
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("/MVCBoard/Write.jsp").forward(req, resp);
		System.out.println(getClass() + " :: doGet() :: Write.jsp에서 작성된 내용을 보내기 위해 reqDispathcher로 가지고 옴");
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 1. 파일 업로드 처리 ===============================
		// 업로드 디렉토리의 물리적 경로 확인
		String saveDirectory = req.getServletContext().getRealPath("/Uploads");
		System.out.println(getClass() + " :: doPost() :: 업로드 디렉토리의 물리적 경로 확인 하고, saveDirectory 변수 값 지정");
		// 저장될 실제 경로 : D:\lidia_J\eclipse-workspace\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps\my_mini_board\Uploads
		
		// 초기화 매개 변수로 설정한 첨부 파일 최대 용량 확인
		ServletContext application = getServletContext();
		int maxPostSize = Integer.parseInt(application.getInitParameter("maxPostSize"));
		System.out.println(getClass() + " :: doPost() :: 최대 파일 용량 변수 maxPostSize 지정");
		
		// 파일 업로드
		MultipartRequest mr = FileUtil.uploadFile(req, saveDirectory, maxPostSize);
		if (mr == null) {
			// 파일 업로드 실패
			JSFunction.alertLocation(resp, "첨부파일이 제한 용량을 초과 합니다.", "../mvcboard/write.do");
			System.out.println(getClass() + " :: doPost() :: 파일 업로드에 대한 null 값일때 에러");
			return;
		}
	
		// 2. 파일 업로드 외 처리 =================================
		// 폼값을 DTO에 저장
		MVCBoardDTO dto = new MVCBoardDTO(); 
	    dto.setName(mr.getParameter("name"));
	    dto.setTitle(mr.getParameter("title"));
	    dto.setContent(mr.getParameter("content"));
	    dto.setPass(mr.getParameter("pass"));
	    System.out.println(getClass() + " :: doPost() :: MVCBoardDTO 객체 생성 후 name, content, title, pass 매개변수 삽입");
		
		// 원본 파일명과 저장된 파일 이름 설정
		String fileName = mr.getFilesystemName("ofile");
		if (fileName != null) {
			// 첨부 파일이 있을 경우 파일명 변경
			// 새로운 파일명 생성
			String now = new SimpleDateFormat("yyyyMMdd_HmsS").format(new Date());
			String ext = fileName.substring(fileName.lastIndexOf("."));
			String newFileName = now + ext;
			
			// 파일명 변경
			File oldFile = new File(saveDirectory + File.separator + fileName);
			File newFile = new File(saveDirectory + File.separator + newFileName);
			oldFile.renameTo(newFile);
			
			dto.setOfile(fileName); // 원래 파일 이름
			dto.setSfile(newFileName); // 서버에 저장된 파일 이름
			System.out.println(getClass() + " :: doPost() :: 파일명과 관련된 로직 처리 ofile, sfile");
		}
		
		// DAO를 통해 DB에 게시 내용 저장
		MVCBoardDAO dao = new MVCBoardDAO();
		int result = dao.insertWrite(dto);
		dao.close();
		System.out.println(getClass() + " :: doPost() :: MVCBoardDAO를 통해서 내용을 DB에 저장");
		
		// 성공, 실패 확인
		if (result == 1) { // 글쓰기 성공
			resp.sendRedirect("../mvcboard/list.do");
			System.out.println(getClass() + " :: doPost() :: 글쓰기 성공, list.do로 리다이렉션");
		}
		else { // 글쓰기 실패
			resp.sendRedirect("../mvcboard/wirte.do");
			System.out.println(getClass() + " :: doPost() :: 글쓰기 실패, write.do로 리다이렉션");
		}
	}
}
