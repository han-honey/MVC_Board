package action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import svc.BoardNoticeContentService;
import vo.ActionForward;
import vo.NoticeBoardDTO;

public class BoardNoticeModifyFormAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("BoardNoticeModifyFormAction");
		
		ActionForward forward = null;
		
		// URL 파라미터로 전달받은 데이터(num) 를 request 객체로부터 가져오기
		int num = Integer.parseInt(request.getParameter("num"));
		
		// BoardNoticeContentService 클래스의 인스턴스 생성 후 getArticle() 메서드를 호출
		// => 파라미터 : 글번호(num)  리턴타입 : NoticeBoardDTO(noticeBoard)
		BoardNoticeContentService boardNoticeContentService = new BoardNoticeContentService();
		NoticeBoardDTO noticeBoard = boardNoticeContentService.getArticle(num);
		
		// request 객체에 데이터 저장(게시물 정보)
		// => Dispatcher 방식으로 이동하게 되면 URL 이 유지되므로 num, page 가 URL 에 존재함
		request.setAttribute("noticeBoard", noticeBoard);
		
		// ActionForward 객체를 사용하여 notice_update.jsp 페이지로 포워딩
		// => 현재 요청 서블릿 주소("BoardNoticeModifyForm.bo")를 유지하고,
		//    request 객체를 유지해야하므로 Dispatcher 방식으로 포워딩
		forward = new ActionForward();
		forward.setPath("./notice_update.jsp");
		forward.setRedirect(false);
		
		return forward;
	}

}
