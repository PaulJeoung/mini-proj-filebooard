## 모델2 방식 (MVC 패턴)의 자료실형 게시판 만들기
- 책에서 정한 난이도 별4 ★★★★☆
- 소스코드 위치
	- webapp/14MVCBoard
	- src/model2/mvcboard
- skill list
	- 표준언어(EL)
	- JSTL
	- File up/download
	- Servlet
	- javascript

<hr>

## 게시판 구현, 프로세스
- 비회원제, 인증없이 작성 가능
	- 작성 시 비밀번호로 validation 체크
	- 비밀번호를 이용한 수정, 삭제
- 파일 첨부 기능
	- 제한된 용량의 첨부기능
	- 첨부된 파일의 다운로드 기능
- 클라이언트
	- 브라우저
	- 서버에 Action을 보내 요청 시도
- 서버
	- 서블릿(컨트롤러) : 자바빈즈(DAO, DTO)를 통해 DB와 연결 하고, 동작을 제어함
	- JSP(뷰) : DB로 부터 받은 데이터를 화면에 그림
	- 자바빈즈(모델) : DAO, DTO를 통해 DB에 접근, 제어
- DB
	- mysql 사용
