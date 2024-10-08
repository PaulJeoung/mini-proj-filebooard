
package common;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class DBConnPool {
	
	// private final String JDBC_DRIVER = "org.gjt.mm.mysql.Driver";
	// private final String JDBC_URL = "jdbc:mysql://localhost:3306/mydb"; // 확인 필요
	// p276
	private final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	private final String JDBC_URL = "jdbc:mysql://localhost:3306/sqlplus";
	
    public Connection con;
    public Statement stmt;
    public PreparedStatement psmt;
    public ResultSet rs;
	
//	try {
//		Class.forName("com.mysql.jdbc.Driver");
//		Connection connection = DriverManager.getConnection(jdbcUrl, dbid, dbpw);
//		out.println("MySQL Connection Success : "+ dbid);
//	} catch (Exception e) {
//		out.println("MySQL Connection is Fail NOW (URL : " + jdbcUrl + ", ID : " + dbid + ", PW : " + dbpw + ")<br/>"+e.getMessage());
//	}

    // 기본 생성자
    public DBConnPool() {
        try {
            // 커넥션 풀(DataSource) 얻기
            Context initCtx = new InitialContext();
            Context ctx = (Context)initCtx.lookup("java:comp/env");
            DataSource source = (DataSource)ctx.lookup("jdbc/sqlplus"); // dbcp_myoracle

            // 커넥션 풀을 통해 연결 얻기
            con = source.getConnection();

            System.out.println(getClass() + " :: DBConnPool() :: DB 커넥션 풀 연결 성공");
        }
        catch (Exception e) {
            System.out.println(getClass() + " :: DBConnPool() :: DB 커넥션 풀 연결 실패");
            e.printStackTrace();
        }
    }

    // 연결 해제(자원 반납)
    public void close() {
        try {            
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (psmt != null) psmt.close();
            if (con != null) con.close();  // 자동으로 커넥션 풀로 반납됨

            System.out.println(getClass() + " :: close() :: DB 커넥션 풀 자원 반납");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
