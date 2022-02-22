package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import action.Action;
import action.BoardDriverContentAction;
import action.BoardDriverDeleteProAction;
import action.BoardDriverListAction;
import action.BoardDriverModifyFormAction;
import action.BoardDriverModifyProAction;
import action.BoardDriverReplyFormAction;
import action.BoardDriverReplyProAction;
import action.BoardDriverWriteProAction;
import action.BoardNoticeContentAction;
import action.BoardNoticeDeleteProAction;
import action.BoardNoticeListAction;
import action.BoardNoticeModifyFormAction;
import action.BoardNoticeModifyProAction;
import action.BoardNoticeRecentListAction;
import action.BoardNoticeWriteProAction;
import vo.ActionForward;

// 서블릿 주소가 "...생략.../프로젝트명/xxx.bo" 일 경우 BoardFrontController 로 요청이 전달됨
@WebServlet("*.bo")
public class BoardFrontController extends HttpServlet {
	
	protected void doProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("BoardFrontController");
		
		// POST 방식에 대한 한글 처리
		request.setCharacterEncoding("UTF-8");
		
		// 입력받은 서블릿 주소에 대한 판별을 수행하기 위해 주소표시줄 URL 정보 가져오기
		// => 서블릿 주소가 BoardList.bo 일 때와 BoardWriteForm.bo 일 때의 동작이 다르므로
		//    URL 로부터 서블릿 주소(xxx.bo)를 추출하여 문자열 비교를 통해 작업을 구분해야함
		
		// 참고) 요청받은 주소(URL) 전체 추출
//		String requestURL = request.getRequestURL().toString();
//		System.out.println("requestURL : " + requestURL);
		// requestURL : http://localhost:8080/MVC_Board/BoardList.bo 출력됨
		
		// 1. 요청 주소 중 URI 부분(/프로젝트명/서블릿주소) 추출
//		String requestURI = request.getRequestURI();
//		System.out.println("requestURI : " + requestURI);
		// requestURI : /MVC_Board/BoardList.bo 출력됨
		
		// 2. 요청 주소 중 컨텍스트 경로(/프로젝트명) 추출
//		String contextPath = request.getContextPath();
//		System.out.println("contextPath : " + contextPath);
		// contextPath : /MVC_Board 출력됨
		
		// 3. 요청 주소 중 서블릿 주소 부분(프로젝트명 뒷부분) 추출
		// => 1번 & 2번 과정에서 추출된 RequestURI 와 ContextPath 를 활용하여 서블릿 주소 추출 가능
		//    (과거에 사용했던 방식이며 불편함)
		// => RequestURI 에서 ContextPath 만큼 제외한 문자열 추출 시 서블릿 주소가 추출됨
		//    (RequestURI 문자열의 substring() 메서드를 호출하여 ContextPath 문자열 길이 전달)
//		String command = requestURI.substring(contextPath.length());
//		System.out.println("command : " + command);
		// ---------------------------------------------------------------------
		// 위의 1 ~ 3번 과정을 하나의 메서드로 압축하여 제공 => getServletPath()
		String command = request.getServletPath();
		System.out.println("command : " + command);
		// ---------------------------------------------------------------------
		// if문을 사용하여 추출된 서블릿 주소(command)를 판별하여
		// 각 주소에 따라 서로 다른 작업(액션) 요청
		// 1. "/BoardDriverWriteForm.bo" 주소 판별
//		if(command.equals("/BoardDriverWriteForm.bo")) {
//			// Driver 게시판 글쓰기 폼을 표시하기 위한 View 페이지(JSP) 로 포워딩 작업 수행
//			// => 포워딩 할 대상 파일 : center 폴더 내의 driver_write.jsp
//			// => 별도의 비즈니스 로직(DB 작업) 없이 JSP 페이지로 바로 연결
//			// => 포워딩 하는 JSP 페이지의 URL 이 노출되지 않아야 하므로 Dispatcher 방식 포워딩
//			// => 주의! 현재 요청 주소의 위치가 프로젝트명/BoardDriverWriteForm.bo 이므로
//			//    프로젝트명(= 컨텍스트루트)의 위치는 webapp 폴더에 해당한다.
//			//    따라서, driver_write.jsp 파일의 위치는 하위폴더인 center 의 내부에 위치함
//			
//			// 주의! 서블릿 주소를 "프로젝트명/서블릿주소" 형태로 입력할 경우
//			// if 문을 통해 "/서블릿주소" 문자열을 판별하는데 이 때 서블릿이 실행되는 위치는
//			// 프로젝트명에서 실행되고 있으므로 webapp 폴더와 같은 위치에서 실행됨
//			// 따라서, 현재 컨텍스트 경로인 webapp 폴더를 기준으로 상대주소를 설정해야함
//			// => 즉, webapp/center/driver_write.jsp 파일을 지정하는 경우
//			//    webapp 폴더는 생략하고 center 폴더의 driver_write.jsp 파일을 지정하면 됨
//			// => 해당 파일 내에서 지정되는 상대주소도 webapp 폴더 기준으로 설정해야함
//			RequestDispatcher dispatcher = request.getRequestDispatcher("./center/driver_write.jsp");
//			dispatcher.forward(request, response);
//		}
		
		// Action 클래스의 인스턴스를 공통으로 다루기 위한 Action 타입 변수 선언
		Action action = null;
		
		// 포워딩 정보를 저장할 ActionForward 타입 변수 선언
		ActionForward forward = null;
		
		if(command.equals("/main/Main.bo")) {
//			RequestDispatcher dispatcher = request.getRequestDispatcher("./main.jsp");
//			dispatcher.forward(request, response);
			
			// 공통 모듈로 분리된 코드를 통해 포워딩 작업을 수행하므로
			// 현재 위치에서는 포워딩 정보만 설정하면 됨
			// 1. ActionForward 클래스 인스턴스를 직접 생성하여 정보 저장
//			forward = new ActionForward();
			// 2. ActionForward 객체를 통해 포워딩 주소를 저장 
//			forward.setPath("./main.jsp");
			// 3. ActionForward 객체를 통해 포워딩 방식 지정
			// => 뷰페이지로 이동할 경우에는 Dispatcher 방식 사용(URL 정보 숨김)
//			forward.setRedirect(false); // false 값일 경우 메서드 호출 생략 가능
			// -----------------------------------------------------------------
			// 최근 공지사항 등 목록을 표시하려면 비즈니스 로직 필요
			// => 최근 목록 조회 후 조회 데이터 가지고 /main/main.jsp 페이지로 포워딩
			action = new BoardNoticeRecentListAction();
			try {
				forward = action.execute(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if(command.equals("/center/BoardDriverWriteForm.bo")) {
			// 주의! 프로젝트명(컨텍스트 경로) 뒤에 /center 구조의 폴더를 명시하고
			// 해당 폴더 구조에서 서블릿 주소를 지정하는 경우
			// 프로젝트명 = webapp 폴더이므로, "프로젝트명/서브폴더" 일 경우
			// "webapp/서브폴더명" 의 위치를 기준으로 상대주소를 설정해야함!
			// ex) "MVC_Board/center/BoardDriverWriteForm.bo" 주소 요청 시
			//     webapp 폴더의 center 폴더 내의 위치가 기준 위치가 됨
//			System.out.println("center 폴더 내의 BoardDriverWriteForm.bo 주소 요청됨");
//			RequestDispatcher dispatcher = request.getRequestDispatcher("./driver_write.jsp");
//			dispatcher.forward(request, response);
			
			// 공통 모듈로 분리된 코드를 통해 포워딩 작업을 수행하므로
			// 현재 위치에서는 포워딩 정보만 설정하면 됨
			// 1. ActionForward 클래스 인스턴스를 직접 생성하여 정보 저장
			forward = new ActionForward();
			// 2. ActionForward 객체를 통해 포워딩 주소를 저장 
			forward.setPath("./driver_write.jsp");
			// 3. ActionForward 객체를 통해 포워딩 방식 지정
			// => 뷰페이지로 이동할 경우에는 Dispatcher 방식 사용(URL 정보 숨김)
			forward.setRedirect(false); // false 값일 경우 메서드 호출 생략 가능
		} else if(command.equals("/center/BoardDriverWritePro.bo")) {
			// BoardDriverWriteProAction 클래스의 인스턴스(action)를 생성하여 execute() 메서드를 호출
			// => 파라미터 : request 객체, response 객체
			// => 리턴타입 : ActionForward(forward)
			action = new BoardDriverWriteProAction();
			try {
				forward = action.execute(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if(command.equals("/center/BoardDriverList.bo")) {
			// "/BoardDriverList.bo" 서블릿 주소 요청 시
			// => BoardDriverListAction 클래스의 execute() 메서드 호출
			// => Action 클래스에서 작업 완료 후 ActionForward 객체를 통해
			//    center 폴더의 driver_list.jsp 페이지로 포워딩
			//    (뷰페이지의 URL 을 표시하지 않도록 하기 위해 Dispatcher 방식 포워딩)
			action = new BoardDriverListAction();
			try {
				forward = action.execute(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if(command.equals("/center/BoardDriverContent.bo")) {
			// "/BoardDriverContent.bo" 서블릿 주소 요청 시
			// => BoardDriverContentAction 클래스의 execute() 메서드 호출
			// => Action 클래스에서 작업 완료 후 ActionForward 객체를 통해
			//    center 폴더의 driver_content.jsp 페이지로 포워딩
			//    (뷰페이지의 URL 을 표시하지 않도록 하기 위해 Dispatcher 방식 포워딩)
			action = new BoardDriverContentAction();
			try {
				forward = action.execute(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if(command.equals("/center/BoardDriverDeleteForm.bo")) {
			forward = new ActionForward();
			forward.setPath("./driver_delete.jsp");
			forward.setRedirect(false);
		} else if(command.equals("/center/BoardDriverDeletePro.bo")) {
			// "/BoardDriverContent.bo" 서블릿 주소 요청 시
			// => BoardDriverDeleteProAction 클래스의 execute() 메서드 호출
			action = new BoardDriverDeleteProAction();
			try {
				forward = action.execute(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if(command.equals("/center/BoardDriverModifyForm.bo")) {
			// "/BoardDriverModifyForm.bo" 서블릿 주소 요청 시
			// => BoardDriverModifyFormAction 클래스의 execute() 메서드 호출
			action = new BoardDriverModifyFormAction();
			try {
				forward = action.execute(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if(command.equals("/center/BoardDriverModifyPro.bo")) {
			// "/BoardDriverModifyPro.bo" 서블릿 주소 요청 시
			// => BoardDriverModifyProAction 클래스의 execute() 메서드 호출
			action = new BoardDriverModifyProAction();
			try {
				forward = action.execute(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if(command.equals("/center/BoardDriverReplyForm.bo")) {
			action = new BoardDriverReplyFormAction();
			try {
				forward = action.execute(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if(command.equals("/center/BoardDriverReplyPro.bo")) {
			action = new BoardDriverReplyProAction();
			try {
				forward = action.execute(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
							
		// ---------- 여기서부터 Notice 게시판 -----------
		} else if(command.equals("/center/BoardNoticeList.bo")) {
			action = new BoardNoticeListAction();
			try {
				forward = action.execute(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if(command.equals("/center/BoardNoticeWriteForm.bo")) {
			forward = new ActionForward();
			forward.setPath("./notice_write.jsp");
			forward.setRedirect(false); // false 값일 경우 메서드 호출 생략 가능
		} else if(command.equals("/center/BoardNoticeWritePro.bo")) {
			action = new BoardNoticeWriteProAction();
			try {
				forward = action.execute(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if(command.equals("/center/BoardNoticeContent.bo")) {
			action = new BoardNoticeContentAction();
			try {
				forward = action.execute(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if(command.equals("/center/BoardNoticeDeleteForm.bo")) {
			forward = new ActionForward();
			forward.setPath("./notice_delete.jsp");
			forward.setRedirect(false);
		} else if(command.equals("/center/BoardNoticeDeletePro.bo")) {
			action = new BoardNoticeDeleteProAction();
			try {
				forward = action.execute(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if(command.equals("/center/BoardNoticeModifyForm.bo")) {
			action = new BoardNoticeModifyFormAction();
			try {
				forward = action.execute(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if(command.equals("/center/BoardNoticeModifyPro.bo")) {
			action = new BoardNoticeModifyProAction();
			try {
				forward = action.execute(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
		// --------------------------------------------------------------------------------
		// ActionForward 객체에 저장된 포워딩 방식에 따른 포워딩 작업 수행(공통 모듈로 분리)
		// 단, ActionForward 객체가 존재할 경우(= null 이 아닐 경우)에만 포워딩 작업 수행
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
		// --------------------------------------------------------------------------------
		
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// GET/POST 방식 요청 구분 없이 공통으로 작업을 처리할 doProcess() 메서드 호출
		doProcess(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// GET/POST 방식 요청 구분 없이 공통으로 작업을 처리할 doProcess() 메서드 호출
		doProcess(request, response);		
	}

}
