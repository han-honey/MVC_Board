package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import action.Action;
import vo.ActionForward;

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
		Action action = null;
		
		ActionForward forward = null;
		
		if(command.equals("/member/MemberJoinForm.me")) {
			forward = new ActionForward();
			forward.setPath("./join.jsp");
			forward.setRedirect(false);
			
		} else if(command.equals("/member/MemberJoginPro.me")) {
//			forward = new ActionForward();
//			forward.setPath("./join.jsp");
//			forward.setRedirect(false);
			
		} else if(command.equals("/member/MemberLoginForm.me")) {
			forward = new ActionForward();
			forward.setPath("./login.jsp");
			forward.setRedirect(false);
				
		} else if(command.equals("/member/MemberLoginPro.me")) {
//			forward = new ActionForward();
//			forward.setPath("./join.jsp");
//			forward.setRedirect(false);
			
		}
		
		if(forward != null) {
			// 포워딩 방식에 따라 Redirect 또는 Dispatcher 방식으로 포워딩
			// => ActionForward 객체의 isRedirect() 메서드 리턴값이 true 이면 Redirect 방식
			//    아니면(= false 이면) Dispatcher 방식으로 포워딩 수행
			//    => 이 때, 포워딩에 필요한 요청 URL 정보는 getPath() 메서드로 리턴받기
			if(forward.isRedirect()) { // Redirect 방식일 경우
				// Redirect 방식으로 포워딩 => response 객체의 sendRedirect() 메서드 호출
				response.sendRedirect(forward.getPath());
			} else { // Dispatcher 방식일 경우
				// RequestDispatcher 객체의 forward() 메서드 호출
				RequestDispatcher dispatcher = request.getRequestDispatcher(forward.getPath());
				dispatcher.forward(request, response);
			}
		}
		
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doProcess(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doProcess(request, response);
	}

}
