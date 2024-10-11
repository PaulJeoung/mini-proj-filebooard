package model2.mvcboard;

import java.io.File;
import java.io.IOException;
import java.net.Authenticator.RequestorType;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.oreilly.servlet.MultipartRequest;

import fileupload.FileUtil;
import utils.JSFunction;

@WebServlet ("/mvcboard/edit.do")
public class EditController extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		MVCBoardDAO dao = new MVCBoardDAO();
		String idx = req.getParameter("idx");
		MVCBoardDTO dto = dao.selectView(idx); // DTO 객체 생성이 아닌 DAO에서 selectView를 가져와야 됨
		req.setAttribute("dto", dto);
		req.getRequestDispatcher("/MVCBoard/Edit.jsp").forward(req, resp);
		System.out.println(getClass() + " :: doGet() :: Edit.jsp 전달할 요소를 만들기 위해 DAO 객체 생성 후 데이터 가져오기");
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 1-1. 기존 파일이 있는 경우 업로드 하면 안됨
		
		
		// 1-2. 파일 업로드 처리 ====================================
		// 업로드 디렉토리의 물리적 경로 확인
		String saveDirectory = req.getServletContext().getRealPath("/Uploads");
		System.out.println(getClass() + " :: doPost() :: 업로드 디렉토리 경로 확인 ==> " + saveDirectory);
		
		// 초기화 매개변수로 설정한 첨부 파일 최대 용량 확인
		ServletContext application = getServletContext();
		int maxPostSize = Integer.parseInt(application.getInitParameter("maxPostSize"));
		System.out.println(getClass() + " :: doPost() :: 업로드 최대 용량 확인 ==>> " + maxPostSize);
		
		// 파일 업로드
		MultipartRequest mr = FileUtil.uploadFile(req, saveDirectory, maxPostSize);
		System.out.println(getClass() + " :: doPost() :: 파일 업로드");
		if (mr == null) {
			// 파일 업로드 실패
			JSFunction.alertBack(resp, "class model2.mvcboard.EditController :: doPost() :: 첨부 파일이 제한 용량을 초과 합니다");
			return;
		}
		
		// 2. 파일 업로드 외 처리 =====================================
		// 수정 내용을 매개 변수에서 얻어옴
		String idx = mr.getParameter("idx");
		String prevOfile = mr.getParameter("prevOfile");
		String prevSfile = mr.getParameter("prevSfile");
		
		String name = mr.getParameter("name");
		String title = mr.getParameter("title");
		String content = mr.getParameter("content");
		
		// 비밀 번호는 session에서 가져옴
		HttpSession session = req.getSession();
		String pass = (String)session.getAttribute("pass");
		System.out.println(getClass() + " :: doPost() :: 수정할 부분 매개변수 가지고 옴");
		
		// DTO에 저장
		MVCBoardDTO dto = new MVCBoardDTO();
		dto.setIdx(Integer.parseInt(idx)); // 흠... 어짜피 DB에 들어가는 값 아니니까... 쿼리에 쓰니까... DTO에 저장 할때 int 로 형 변환
		dto.setName(name);
		dto.setTitle(title);
		dto.setContent(content);
		dto.setPass(pass);
		System.out.println(getClass() + " :: doPost() :: DTO 저장");
		
		// 원본 파일명과 저장된 파일이름 설정
		String fileName = mr.getFilesystemName("ofile");
		System.out.println(getClass() + " :: doPost() :: 원본파일명 확인 ==> " + fileName);
		if(fileName != null) {
			// 첨부 파일이 있을 경우 파일명 변경
			// 새로운 파일명 생성
			String now = new SimpleDateFormat("yyyyMMdd_HmsS").format(new Date());
			String ext = fileName.substring(fileName.lastIndexOf("."));
			String newFileName = now + ext;
			System.out.println(getClass() + " :: doPost() :: 새 파일로 변경 되어 새 파일명 생성 ==> " + newFileName);
			
			// 파일명 변경
			File oldFile = new File(saveDirectory + File.separator + fileName);
			File newFile = new File(saveDirectory + File.separator + newFileName);
			oldFile.renameTo(newFile);
			
			// DTO에 저장
			dto.setOfile(fileName); // 원래 파일 이름
			dto.setSfile(newFileName); // 서버에 저장된 파일 이름
			System.out.println(getClass() + " :: doPost() :: 변경 된 파일 이름 DTO에 저장");
			// 기존 파일 삭제
			FileUtil.deleteFile(req, "/Uploads", prevSfile);
			System.out.println(getClass() + " :: doPost() :: 기존 파일 삭제");
		} else {
			// 첨부파일이 없으면 기존 이름 유지
			dto.setOfile(prevOfile);
			dto.setSfile(prevSfile);
		}
		
		// DB에 수정 내용 반영
		MVCBoardDAO dao = new MVCBoardDAO();
		int result = dao.updatePost(dto);
		System.out.println(getClass() + " :: doPost() :: DB에 수정 내용 반영 DAO updatePost()");
		dao.close();
		
		// 성공 / 실패 확인
		if (result == 1) { // 수정 성공
			session.removeAttribute("pass");
			System.out.println(getClass() + " :: doPost() :: 게시글 수정 성공");
			resp.sendRedirect("../mvcboard/view.do?idx="+idx);
		} else { // 수정 실패
			JSFunction.alertLocation(resp, "class model2.mvcboard.EditController :: doPost() :: 비밀번호 검증을 다시 진행해 주세요", "../mvcboard/view.do?idx="+idx);
		}
	}
}
