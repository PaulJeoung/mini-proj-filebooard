package utils;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

public class JSFunction {
	
	
	// 윗 부분 빠진 부분 찾아 보기
	// 메시지 알람창을 띄운 후 명시한 URL로 이동합니다.
	public static void alertLocation(HttpServletResponse resp, String msg, String url) {
		try {
			resp.setContentType("text/html;charset=UTF-8");
			PrintWriter writer = resp.getWriter();
			String script = ""
						  + "<script>"
						  + "     alert('" + msg + "');"
						  + "     location.href='" + url + "';"
						  + "</script>";
			writer.print(script);
		}
		catch (Exception e) {}
	}
	
	// 메시지 알람창을 띄 운 후 이전 페이지로 돌아갑니다.
	public static void alertBack(HttpServletResponse resp, String msg) {
		try {
			resp.setContentType("text/html;charset=UTF-8");
			PrintWriter writer = resp.getWriter();
			String script = ""
					  + "<script>"
					  + "     alert('" + msg + "');"
					  + "     history.back();"
					  + "</script>";
		writer.print(script);
	}
	catch (Exception e) {}
		
	}
}
