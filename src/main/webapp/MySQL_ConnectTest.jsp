<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.Connection"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>MySQL 드라이버 연결 테스트</title>
</head>
<body>
	<h3>MySQL Driver Connection Test Page</h3>
	<%
		String jdbcUrl = "jdbc:mysql://localhost:3306/sqlplus";
		String dbid = "root";
		String dbpw = "1234";
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connection = DriverManager.getConnection(jdbcUrl, dbid, dbpw);
			out.println("MySQL Connection Success : "+ dbid);
		} catch (Exception e) {
			out.println("MySQL Connection is Fail NOW (URL : " + jdbcUrl + ", ID : " + dbid + ", PW : " + dbpw + ")<br/>"+e.getMessage());
		}
	%>
</body>
</html>
