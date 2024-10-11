package model2.mvcboard;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import utils.BoardPage;

public class ListController extends HttpServlet {
	// 성낙현의 JSP 자바 웹 프로그래밍 P491 14.2.4 컨트롤러(서블릿)
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// DAO 객체를 생성
		MVCBoardDAO dao = new MVCBoardDAO();
		
		// 모델로 전달할 검색 매개변수 및 뷰로 전달할 페이징 관련 값을 저장 하기 위해 Map 컬렉션 생성
		Map<String, Object> map = new HashMap<String, Object>();
		
		// 매개변수로 저장할 검색어를 선언
		String searchField = req.getParameter("searchField");
		String searchWord = req.getParameter("searchWord");
		if (searchField != null) {
			// 쿼리스트링으로 전달받은 매개변수 중 검색어가 있다면 Map에 저장
			map.put("searchField", searchField);
			map.put("serchWord", searchWord);
		}
		// DB에서 조회한 게시물의 갯수를 가지고 옴
		int totalCount = dao.selectCount(map);
		System.out.println(getClass() + " :: doGET() :: dao.selectCount(map)에 저장한 totalCount 값을 확인 ==> " + totalCount);
		
		/* 페이지 처리 start */
		ServletContext application = getServletContext();
		int pageSize = Integer.parseInt(application.getInitParameter("POSTS_PER_PAGE"));
		int blockPage = Integer.parseInt(application.getInitParameter("PAGES_PER_BLOCK"));
		System.out.println(getClass() + " :: doGet() :: (web.xml 참조 후) 페이지 처리 시작");
		
		// 현재 페이지 확인
		int pageNum = 1; // 기본 값
		String pageTemp = req.getParameter("pageNum"); // "5"
		System.out.println(getClass() + " :: doGet() :: pageTemp ==> "+ pageTemp + ", pageNum 값을 pageTemp로 저장");
		if (pageTemp != null && !pageTemp.equals("")) {
			pageNum = Integer.parseInt(pageTemp); // 요청 받은 페이지로 수정
		}
		// pageNum = Integer.parseInt(pageTemp);
		// 목록에 제출 할 게시물 범위 계산
		int start = (pageNum - 1) * pageSize + 1; // 첫 게시물 번호
		int end = pageNum * pageSize;
		System.out.println(getClass() + " :: doGet() :: start, end ==> " + start + ", " + end);
		map.put("start", start);
		map.put("end", end);
		/* 페이지 처리 end */
		
		List<MVCBoardDTO> boardLists = dao.selectListPage(map);
		System.out.println(getClass() + " :: doGET() :: boardLists 생성 (MVCDAO에서 쿼리 조회 후 결과 리턴)");
		// 게시물 목록 받기
		dao.close();
		
		// 뷰에 전달할 매개 변수 추가
		String pagingImg = BoardPage.pagingStr(totalCount, pageSize, blockPage, pageNum, "../mvcboard/list.do");
		System.out.println(getClass() + " :: doGET() :: List.jsp 뷰에 전달할 매개 변수 추가");
		// 바로가기 영역 HTML 문자열
		map.put("pagingImg", pagingImg);
		map.put("totalCount", totalCount);
		map.put("pageSize", pageSize);
		map.put("pageNum", pageNum);
		System.out.println(getClass() + " :: doGET() :: 바로가기 영역 HTML 문자열 담기");
		// 전달할 데이터를 request영역에 저장 후 List.jsp로 포워드
		req.setAttribute("boardLists", boardLists);
		req.setAttribute("map", map);
		req.getRequestDispatcher("/MVCBoard/List.jsp").forward(req, resp);
		System.out.println(getClass() + " :: doGET() :: List.jsp 데이터 포워드");
		
	}
}
