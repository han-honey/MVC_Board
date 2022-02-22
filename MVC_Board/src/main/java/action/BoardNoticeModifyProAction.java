package action;

import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import svc.BoardNoticeModifyProService;
import svc.BoardNoticeWriteProService;
import vo.ActionForward;
import vo.NoticeBoardDTO;

public class BoardNoticeModifyProAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("BoardNoticeModifyProAction");
		
		// 포워딩 정보를 저장할 ActionForward 타입 변수 선언
		ActionForward forward = null;
		
		// -------------------------------------------------------------------------------------
		// 비즈니스 로직(데이터베이스 처리)을 위한 데이터 준비 작업 수행
		// => 게시판 글쓰기 폼에서 글 작성 후 글 쓰기 버튼을 클릭 시 현재 액션 클래스로 이동하며
		//    DB 작업 수행에 필요한 데이터를 준비(= 입력받은 게시글 정보 가져오기)
		NoticeBoardDTO noticeBoard = new NoticeBoardDTO();
		noticeBoard.setNum(Integer.parseInt(request.getParameter("num")));
		noticeBoard.setName(request.getParameter("name"));
		noticeBoard.setPass(request.getParameter("pass"));
		noticeBoard.setSubject(request.getParameter("subject"));
		noticeBoard.setContent(request.getParameter("content"));
		
		// BoardNoticeModifyProService 클래스의 isArticleWriter() 메서드 호출하여 이동
		// => 작성자 본인 여부 판별(pass 와 num 을 사용하여 일치 여부 판별)
		BoardNoticeModifyProService boardNoticeModifyProService = new BoardNoticeModifyProService();
		boolean isArticleWriter = 
				boardNoticeModifyProService.isArticleWriter(noticeBoard.getNum(), noticeBoard.getPass());
		
		if(!isArticleWriter) {
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println("<script>");
			out.println("alert('수정 권한이 없습니다!')");
			out.println("history.back()");
			out.println("</script>");
			out.flush();
		} else {
			// 일치 시 삭제 작업 수행 
			// BoardNoticeModifyProService 클래스의 modifyArticle() 메서드 호출하여 이동
			// => 파라미터 : NoticeBoardDTO 객체,  리턴타입 : boolean(isModifySuccess)
			boolean isModifySuccess = boardNoticeModifyProService.modifyArticle(noticeBoard);
			
			// 수정 결과 판별 작업 수행
			if(!isModifySuccess) { // 수정 실패 시
				response.setContentType("text/html; charset=UTF-8");
				PrintWriter out = response.getWriter();
				out.println("<script>"); // 자바스크립트 시작 태그
				out.println("alert('글 수정 실패!')"); // 메세지는 작은따옴표('')로 표기해야함
				out.println("history.back()");
				out.println("</script>"); // 자바스크립트 끝 태그
			} else { // 수정 성공 시
				forward = new ActionForward();
				forward.setPath("BoardNoticeContent.bo?num=" + noticeBoard.getNum() + "&page=" + request.getParameter("page"));
				forward.setRedirect(true); // Redirect 방식 포워딩
			}
		}
		
		// ActionForward 객체 리턴
		return forward;
	}

}
















