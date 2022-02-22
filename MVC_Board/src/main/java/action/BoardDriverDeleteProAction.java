package action;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import svc.BoardDriverDeleteProService;
import vo.ActionForward;

public class BoardDriverDeleteProAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("BoardDriverDeleteProAction");
		
		ActionForward forward = null;
		
		// 파라미터 가져오기(num, page, pass) => page 생략
		int num = Integer.parseInt(request.getParameter("num"));
		String pass = request.getParameter("pass");
//		System.out.println("num = " + num + ", pass : " + pass);
		
		// BoardDriverDeleteProService 클래스의 isArticleWriter() 메서드 호출하여
		// 작성자 본인 여부 판별(pass 와 num 을 사용하여 일치 여부 판별) 요청
		// => 파라미터 : 글번호, 패스워드    리턴타입 : boolean
		BoardDriverDeleteProService boardDriverDeleteProService = new BoardDriverDeleteProService();
		boolean isArticleWriter = boardDriverDeleteProService.isArticleWriter(num, pass);
		
		// 판별 결과에 따른 작업 수행
		// => 불일치 시 자바스크립트 사용하여 "삭제 권한이 없습니다!" 출력 후 이전페이지로 이동
		if(!isArticleWriter) {
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println("<script>");
			out.println("alert('삭제 권한이 없습니다!')");
			out.println("history.back()");
			out.println("</script>");
			out.flush();
		} else {
			// 일치 시 삭제 작업 수행 
			// BoardDriverDeleteProService 클래스의 removeArticle() 메서드 호출하여 이동
			// => 파라미터 : 글번호(num),  리턴타입 : boolean(isDeleteSuccess)
			boolean isDeleteSuccess = boardDriverDeleteProService.removeArticle(num);
			
			// 삭제 결과 판별 작업 수행
			if(!isDeleteSuccess) { // 삭제 실패 시
				response.setContentType("text/html; charset=UTF-8");
				PrintWriter out = response.getWriter();
				out.println("<script>"); // 자바스크립트 시작 태그
				out.println("alert('글 삭제 실패!')"); // 메세지는 작은따옴표('')로 표기해야함
				out.println("history.back()");
				out.println("</script>"); // 자바스크립트 끝 태그
			} else { // 삭제 성공 시
				forward = new ActionForward();
				forward.setPath("BoardDriverList.bo?page=" + request.getParameter("page"));
				forward.setRedirect(true); // Redirect 방식 포워딩
			}
		}
		
		return forward;
	}

}















