package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("*.me")
public class MemberFrontController extends HttpServlet {
	protected void doProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("MemberFrontController");
		
		// POST 방식 한글 처리
		request.setCharacterEncoding("UTF-8");
		
		// 서블릿 주소 추출
		String command = request.getServletPath();
		System.out.println("command : " + command);
		
		// "/프로젝트명/member/MemberJoinForm.me" 주소가 요청되면
		// member 폴더 내의 join.jsp 페이지로 이동(Dispatcher 방식)하고,
		// "/프로젝트명/member/MemberLoginForm.me" 주소가 요청되면
		// member 폴더 내의 login.jsp 페이지로 이동(Dispatcher 방식)하고,
		
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doProcess(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doProcess(request, response);
	}

}
