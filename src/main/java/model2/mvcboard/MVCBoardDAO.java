package model2.mvcboard;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import common.DBConnPool;

public class MVCBoardDAO extends DBConnPool { // Connection Pool 상속
	public MVCBoardDAO() {
		super();
	}
	
	// 검색 조건에 맞는 게시물의 갯수를 반환
	public int selectCount(Map<String, Object> map) {
		int totalCount = 0;
		// 쿼리문 준비
		String query = "SELECT COUNT(*) FROM sqlplus.mvcboard";
		System.out.println(getClass() + " :: selectCount() :: 쿼리문 준비");
		// 검색 조건이 있다면 where 절로 추가
		if (map.get("searchWord") != null) {
			query += "WHERE " + map.get("searchField") + " " + " LIKE '%" + map.get("searchWord") + "%'";
			System.out.println(getClass() + " :: selectCount() :: 검색조건을 발견 해서 IF문에서 WHERE 절 추가");
		}
		System.out.println(getClass() + " :: selectCount() :: 최종 쿼리문: " + query);
		try {
			stmt = con.createStatement(); // 쿼리문 생성
			rs = stmt.executeQuery(query); // 쿼리문 실행
			System.out.println(getClass() + " :: selectCount() :: 쿼리문 생성 후 실행 실행");
			rs.next();
			totalCount = rs.getInt(1);
			System.out.println(getClass() + " :: selectCount() :: 토탈 카운트 확인 갯수 반환 ==> " + totalCount);
		}
		catch (Exception e) {
			System.out.println(getClass()+ ":: selectCount() :: 게시물 카운트 중 예외발생");
			e.printStackTrace();
		}
		return totalCount; // 게시물 갯수를 서블릿으로 변환
	}
	
	// 검색 조건에 맞는 게시물 목록을 반환(페이징 기능 포함)
	public List<MVCBoardDTO> selectListPage(Map<String, Object> map) {
		List<MVCBoardDTO> board = new Vector<MVCBoardDTO>();
		
		// 기본 쿼리문 준비
		/*
		String query = " "
				+ "SELECT * FROM ("
				+ "     SELECT tb.*, ROWNUM rNUM FROM ("
				+ "          SELECT "
				+ "* FROM sqlplus.mvcboard" ;
		*/
		String query = "SELECT * FROM sqlplus.mvcboard";
		// 검색 조건이 있다면 where 절 추가
		if (map.get("searchWord") != null) {
			/*
			query += "WHERE " + map.get("searchField")
			+ " LIKE '%" + map.get("searchWord") + "%'";
			*/
			query += " WHERE " + map.get("searchField") + " LIKE '%" + map.get("searchWord") + "%' ";
			System.out.println(getClass() + " :: selectListPage() :: 검색 조건이 있는 경우 where 절 추가");
		}
		/*
		query += "          ORDER BY idx DESC "
			  + "     ) Tb "
			  + " )"
			  + "WHERE rNUM BETWEEN ? AND ?"; // 게시물 구간은 인파라미터로
		
		// Oracle query -->> MySQL Query
		SELECT * FROM sqlplus.mvcboard
			WHERE title LIKE '%자료실%'
			ORDER BY idx DESC
			LIMIT ? OFFSET ?;
			LIMIT 10 OFFSET 0;
		*/
		// 게시물 구간 설정은 나중에...
		// query += " ORDER BY idx DESC LIMIT ?, ?";
		query += " ORDER BY idx DESC LIMIT 10 OFFSET 0"; // 10, 0
		
		System.out.println(getClass() + " :: selectListPage() :: 쿼리문 준비 \n                 " + query);
		try {
			psmt = con.prepareStatement(query); // 동적 쿼리문 생성
			System.out.println(getClass() + " :: selectListPage() :: 동적쿼리문 생성");
						
//			psmt.setString(1, map.get("start").toString());
//			psmt.setString(2, map.get("end").toString());
			
			rs = psmt.executeQuery(); // 쿼리문 실행
			System.out.println(getClass() + " :: selectListPage() :: 동적 쿼리문 실행");
			
			// 반환된 게시물 목록을 List 컬렉션에 추가
			while (rs.next()) {
				int idx = rs.getInt(1);
		        String name = rs.getString(2);
		        String title = rs.getString(3);
		        String content = rs.getString(4);
		        Date postdate = rs.getDate(5);
		        String ofile = rs.getString(6);
		        String sfile = rs.getString(7);
		        int downcount = rs.getInt(8);
		        String pass = rs.getString(9);
		        int visitcount = rs.getInt(10);
		        
		        // 결과를 콘솔에 출력
		        System.out.println("idx: " + idx + ", name: " + name + ", title: " + title + 
		                           ", content: " + content + ", postdate: " + postdate + 
		                           ", ofile: " + ofile + ", sfile: " + sfile + 
		                           ", downcount: " + downcount + ", pass: " + pass + 
		                           ", visitcount: " + visitcount);
				
				MVCBoardDTO dto = new MVCBoardDTO();
				// 데이터 타입 확인 필요. idx 수정
				dto.setIdx(rs.getInt(1));
				dto.setName(rs.getString(2));
				dto.setTitle(rs.getString(3));
				dto.setContent(rs.getString(4));
				dto.setPostdate(rs.getDate(5));
				dto.setOfile(rs.getString(6));
				dto.setSfile(rs.getString(7));
				dto.setDowncount(rs.getInt(8));
				dto.setPass(rs.getString(9));
				dto.setVisitcount(rs.getInt(10));
				
				board.add(dto);
			}
			System.out.println(getClass() + " :: selectListPage() :: 반환된 게시물 목록을 List에 while 문으로 추가");
		}
		catch (Exception e) {
			System.out.println(getClass() + ":: selectListPage() :: 게시물 조회 중 예외 발생");
			e.printStackTrace();
		}
		System.out.println(getClass() + " :: selectListPage() :: 게시글 목록 리스트 형태로 반환");
		return board; // 목록 반환
	}
	
	// 게시글 데이터를 받아 DB에 추가 (파일 업로드 지원)
	public int insertWrite(MVCBoardDTO dto) {
		int result = 0;
		try {
			/*
			 * ALTER TABLE sqlplus.mvcboard MODIFY idx INT AUTO_INCREMENT;
			 * ALTER TABLE sqlplus.mvcboard AUTO_INCREMENT = 80;
			 */
			String query = "INSERT INTO sqlplus.mvcboard ( "
						+ "name, title, content, ofile, sfile, pass) "
						+ " VALUES ( "
						+ "?,?,?,?,?,?)"; // idx 를 AUTO_INCREMENT로 변경 하고 해당 컬럼 삭제
			System.out.println(getClass() + " :: insertWrite() :: 쿼리 준비 ==> "+query);
			psmt = con.prepareStatement(query);
			psmt.setString(1, dto.getName());
			psmt.setString(2, dto.getTitle());
			psmt.setString(3, dto.getContent());
			psmt.setString(4, dto.getOfile());
			psmt.setString(5, dto.getSfile());
			psmt.setString(6, dto.getPass());
			System.out.println(getClass() + " :: insertWrite() :: psmt에 입력한 내용을 쿼리문에 담아서 준비");
			result = psmt.executeUpdate();
			System.out.println(getClass() + " :: insertWrite() :: 쿼리 실행 완료");
		}
		catch (Exception e) {
			System.out.println(getClass() + " :: insertWrite() :: 게시물 입력 중 예외 발생");
			e.printStackTrace();
		}
		return result;
	}
	
	// 주어진 일련번호에 해당하는 게시물을 DTO에 담아 반환 합니다
	public MVCBoardDTO selectView(String idx) {
		MVCBoardDTO dto = new MVCBoardDTO(); // DTO 객체 생성
		String qeury = "SELECT * FROM sqlplus.mvcboard WHERE idx=?"; // 쿼리문 템플릿 준비
		try { 
			psmt = con.prepareStatement(qeury); // 쿼리문 준비
			psmt.setString(1, idx); // 인파라미터 설정
			rs = psmt.executeQuery(); // 쿼리문 실행
			
			if (rs.next()) { // 결과를 DTO 객체에 저장
				dto.setIdx(rs.getInt(1));
				dto.setName(rs.getString(2));
				dto.setTitle(rs.getString(3));
				dto.setContent(rs.getString(4));
				dto.setPostdate(rs.getDate(5));
				dto.setOfile(rs.getString(6));
				dto.setSfile(rs.getString(7));
				dto.setDowncount(rs.getInt(8));
				dto.setPass(rs.getString(9));
				dto.setVisitcount(rs.getInt(10));
			}
		}
		catch (Exception e) {
			System.out.println("게시물 상세보기 중 예외 발생");
			e.printStackTrace();
		}
		return dto; // 결과 반환
	}
	
	// 주어진 일련번호에 해당하는 게시물의 조회수를 1 증가 시킴
	public void updateVisitCount(String idx) {
		String query = "UPDATE sqlplus.mvcboard SET "
				     + " visitcount=visitcount+1 "
				     + " WHERE idx=?";
		try {
			psmt = con.prepareStatement(query);
			psmt.setString(1, idx);
			psmt.executeQuery();
		}
		catch (Exception e) {
			System.out.println("게시물 조회수 증가 중 예외 발생");
			e.printStackTrace();
		}
	}
	
	// 다운로드 횟수를 1 증가 시켜줌
	public void downloadCountPlus(String idx) {
		String sql = "UPDATE sqlplus.mvcboard SET "
				   + " downcount=downcount+1 "
				   + " WHERE idx=? ";
		try {
			psmt = con.prepareStatement(sql);
			psmt.setString(1, idx);
			psmt.executeUpdate();
		}
		catch (Exception e) {}
	}
	
	// 입력한 비밀번호가 지정한 일련번호의 게시물의 비밀번호와 일치하는지 확인
	public boolean confirmPassword(String pass, String idx) {
		boolean isCorr = true;
		try {
			String sql = "SELECT COUNT(*) FROM sqlplus.mvcboard WHERE pass=? AND idx=?";
			psmt = con.prepareStatement(sql);
			psmt.setString(1, pass);
			psmt.setString(2, idx);
			rs = psmt.executeQuery();
			rs.next();
			if (rs.getInt(1) == 0) {
				isCorr = false;
			}
		} catch (Exception e) {
			isCorr = false;
			e.printStackTrace();
		}
		return isCorr;
	}
	
	// 지정한 일련번호의 게시물을 삭제 합니다.
	public int deletePost(String idx) {
		int result = 0;
		try {
			String query = "DELETE FROM sqlplus.mvcboard WHERE idx=?";
			psmt = con.prepareStatement(query);
			psmt.setString(1, idx);
			result = psmt.executeUpdate();
		}
		catch (Exception e) {
			System.out.println("게시물 삭제중 예외발생");
			e.printStackTrace();
		}
		return result;
	}
	
	// 게시글 데이러를 받아 DB에 저장 되어 있던 내용을 갱신 함
	public int updatePost(MVCBoardDTO dto) {
		int result = 0;
		try {
			// 쿼리문 템플릿 준비
			String query = "UPDATE sqlplus.mvcboard"
						 + " SET title=?, name=?, content=?, ofile=?, sfile=? "
						 + " WHERE idx=? and pass=?";
			// 쿼리문 준비
			psmt = con.prepareStatement(query);
			psmt.setString(1, dto.getTitle());
			psmt.setString(2, dto.getName());
			psmt.setString(3, dto.getContent());
			psmt.setString(4, dto.getOfile());
			psmt.setString(5, dto.getSfile());
			psmt.setInt(6, dto.getIdx());
			psmt.setString(7, dto.getPass());
			
			// 쿼리문 실행
			result = psmt.executeUpdate();
		} catch (Exception e) {
			System.out.println("게시물 수정 중 예외 발생");
			e.printStackTrace();
		}
		return result;
	}
}