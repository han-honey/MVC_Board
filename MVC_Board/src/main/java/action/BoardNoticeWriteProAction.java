package action;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import svc.BoardNoticeWriteProService;
import vo.ActionForward;
import vo.NoticeBoardDTO;

public class BoardNoticeWriteProAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("BoardNoticeWriteProAction");
		
		// 포워딩 정보를 저장할 ActionForward 타입 변수 선언
		ActionForward forward = null;
		
		// -------------------------------------------------------------------------------------
		// 비즈니스 로직(데이터베이스 처리)을 위한 데이터 준비 작업 수행
		// => 게시판 글쓰기 폼에서 글 작성 후 글 쓰기 버튼을 클릭 시 현재 액션 클래스로 이동하며
		//    DB 작업 수행에 필요한 데이터를 준비(= 입력받은 게시글 정보 가져오기)
		NoticeBoardDTO noticeBoard = new NoticeBoardDTO();
		noticeBoard.setName(request.getParameter("name"));
		noticeBoard.setPass(request.getParameter("pass"));
		noticeBoard.setSubject(request.getParameter("subject"));
		noticeBoard.setContent(request.getParameter("content"));
		
//		System.out.println(noticeBoard); // toString() 메서드 생략됨(전체 데이터 확인용)
		// -------------------------------------------------------------------------------------
		// 비즈니스 로직(DB 작업)을 실제로 수행하기 위해
		// BoardDriverWriteProService 클래스 인스턴스 생성 후 registArticle() 메서드 호출
		// => 파라미터 : Action 클래스로부터 전달받는 게시물 정보(NoticeBoardDTO 객체, noticeBoard)
		// => 리턴타입 : boolean(isWriteSuccess)
		BoardNoticeWriteProService boardNoticeWriteProService = new BoardNoticeWriteProService();
		boolean isWriteSuccess = boardNoticeWriteProService.registArticle(noticeBoard);
		
		// 데이터베이스 작업 완료 후 성공/실패 여부에 따라 포워딩 작업 수행
		if(!isWriteSuccess) { // 실패 시(isWriteSuccess == false)
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println("<script>"); // 자바스크립트 시작 태그
			out.println("alert('글 등록 실패!')"); // 메세지는 작은따옴표('')로 표기해야함
			out.println("history.back()");
			out.println("</script>"); // 자바스크립트 끝 태그
		} else { // 성공 시
			forward = new ActionForward();
			forward.setPath("./BoardNoticeList.bo");
			forward.setRedirect(true);
		}
		
		// ActionForward 객체 리턴
		return forward;
	}

}
















