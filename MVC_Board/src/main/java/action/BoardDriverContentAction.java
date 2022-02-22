package action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import svc.BoardDriverContentService;
import vo.ActionForward;
import vo.FileBoardDTO;

public class BoardDriverContentAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("BoardDriverContentAction");
		
		ActionForward forward = null;
		
		// URL 파라미터로 전달받은 데이터(num, page) 를 request 객체로부터 가져오기
		int num = Integer.parseInt(request.getParameter("num"));
		String pageNum = request.getParameter("page"); // 정수화 불필요(전달 용도)
//		System.out.println("num : " + num + ", page : " + pageNum);
		
		
		// BoardDriverContentService 클래스의 인스턴스 생성 후 getArticle() 메서드를 호출
		// => 파라미터 : 글번호(num)  리턴타입 : FileBoardDTO(fileBoard)
		BoardDriverContentService boardDriverContentService = new BoardDriverContentService();
		FileBoardDTO fileBoard = boardDriverContentService.getArticle(num);
		
		
		// request 객체에 데이터 저장(게시물 정보, 페이지 번호)
		request.setAttribute("fileBoard", fileBoard);
		request.setAttribute("page", pageNum);
		
		// ActionForward 객체를 사용하여 driver_content.jsp 페이지로 포워딩
		// => 현재 요청 서블릿 주소("BoardDriverContent.bo")를 유지하고,
		//    request 객체를 유지해야하므로 Dispatcher 방식으로 포워딩
		forward = new ActionForward();
		forward.setPath("./driver_content.jsp");
		forward.setRedirect(false);
		
		return forward;
	}

}














