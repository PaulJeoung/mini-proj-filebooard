package model2.mvcboard;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import fileupload.FileUtil;
import utils.JSFunction;

@WebServlet ("/mvcboard/pass.do")
public class PassController extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setAttribute("mode", req.getParameter("mode"));
		req.getRequestDispatcher("/MVCBoard/Pass.jsp").forward(req, resp);
		System.out.println(getClass() + " :: doGet() :: pass.jsp 뷰 포워딩");
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 매개 변수 저장
		String idx = req.getParameter("idx");
		String mode = req.getParameter("mode");
		String pass = req.getParameter("pass");
		
		// 비밀번호 확인
		MVCBoardDAO dao = new MVCBoardDAO();
		boolean confirmed = dao.confirmPassword(pass, idx);
		System.out.println(getClass() + " :: doPost() :: DAO에서 객체 생성 후 confirmPassword DB 존재 여부 조회");
		dao.close();
		
		if (confirmed) { // 비밀번호 일치
			if (mode.equals("edit")) { // 수정모드
				HttpSession session = req.getSession();
				session.setAttribute("pass", pass);
				System.out.println(getClass() + " :: doPost() :: 비밀번호 검증 완료 후 세션에 저장");
				resp.sendRedirect("../mvcboard/edit.do?idx="+idx);
				System.out.println(getClass() + " :: doPost() :: idx 기준으로 edit.jsp로 리다이렉트 ==>> " + idx);
			} else if (mode.equals("delete")) {
				dao = new MVCBoardDAO();
				MVCBoardDTO dto = dao.selectView(idx);
				int result = dao.deletePost(idx);
				System.out.println(getClass() + " :: doPost() :: 비밀번호 검증 완료 후 DAO에서 deletePost 쿼리 호출 ==>> 삭제 idx : " + idx);
				dao.close();
				
				if (result == 1) { // 게시물 삭제 성공시 첨부파일도 삭제
					String saveFileName = dto.getSfile();
					FileUtil.deleteFile(req, "/Uploads", saveFileName);
					System.out.println(getClass() + " :: doPost() :: 첨부파일도 삭제 확인");
				}
				JSFunction.alertLocation(resp, "삭제되었습니다", "../mvcboard/list.do");
			}
		} else { // 비밀번호 불일치
			JSFunction.alertBack(resp, "비밀번호 검증에 실패 했습니다");
		}
	}
}
