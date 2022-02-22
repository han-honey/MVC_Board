package action;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import svc.BoardNoticeListService;
import svc.BoardNoticeRecentListService;
import vo.ActionForward;
import vo.NoticeBoardDTO;
import vo.PageInfo;

public class BoardNoticeRecentListAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("BoardNoticeRecentListAction");
		
		ActionForward forward = null;
		
		// ------------------------------------------------------------------------------------
		// 게시물 목록을 가져오기 전 최근 게시물 목록 5개 조회를 위한 getListCount() 메서드 호출
		// BoardNoticeRecentListService 클래스의 인스턴스 생성
		BoardNoticeRecentListService boardNoticeRecentListService = new BoardNoticeRecentListService();
		int listLimit = 3; // 페이지 당 보여줄 게시물 수
		// 게시물 목록 가져오기 위해 BoardNoticeRecentListService 인스턴스의 getNoticeRecentList() 메서드 호출
		// => 파라미터 : 조회할 목록 갯수(listLimit)
		//    리턴타입 : ArrayList<NoticeBoardDTO>(recentList)
		ArrayList<NoticeBoardDTO> recentList = 
				boardNoticeRecentListService.getNoticeRecentList(listLimit);
		// -------------------------------------------------------------------------------------
		// 뷰페이지에서 사용할 데이터(최근 게시물 목록 정보)를 request 객체에 저장
		request.setAttribute("recentList", recentList);
		// -------------------------------------------------------------------------------------
		// 게시물 목록 조회 작업 완료 후 ActionForward 객체를 통해 뷰페이지(notice_list.jsp)로 포워딩
		// => 요청 서블릿 주소(BoardNoticeList.bo)가 유지되어야 하고,
		//    request 객체가 유지되어야 하므로 Dispatcher 방식 포워딩 필요
		forward = new ActionForward();
		forward.setPath("./main.jsp");
		forward.setRedirect(false); // 생략 가능
		
		return forward;
	}

}
