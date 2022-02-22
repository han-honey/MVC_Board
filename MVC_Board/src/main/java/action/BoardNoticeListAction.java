package action;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import svc.BoardNoticeListService;
import vo.ActionForward;
import vo.NoticeBoardDTO;
import vo.PageInfo;

public class BoardNoticeListAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("BoardNoticeListAction");
		
		ActionForward forward = null;
		
		// -------------------------------------------------------------------------------------
		int pageNum = 1; // 현재 페이지를 저장할 변수
		
		// request 객체를 통해 전달받은 페이지 번호(page 파라미터)가 있을 경우(null 이 아닐 경우)
		// => 전달받은 page 파라미터 값을 pageNum 변수에 저장
		if(request.getParameter("page") != null) {
			pageNum = Integer.parseInt(request.getParameter("page"));
		}
		
		// ------------------------------------------------------------------------------------
		// 게시물 목록을 가져오기 전 전체 게시물 목록 갯수 조회를 위한 getListCount() 메서드 호출
		// BoardNoticeListService 클래스의 인스턴스 생성 후
		// getListCount() 메서드를 호출하여 현재 board 테이블의 총 게시물 수 가져오기
		// => 파라미터 : 없음   리턴타입 : int(listCount)
		BoardNoticeListService boardNoticeListService = new BoardNoticeListService();
		
		int listCount = boardNoticeListService.getListCount(); // 전체 게시물 수
//		System.out.println("전체 게시물 수 : " + listCount);
		
		int listLimit = 10; // 페이지 당 보여줄 게시물 수
		int pageLimit = 10; // 페이지 당 보여줄 페이지 수
		
		// ----------------------------------------------------------------------------
		// 게시물 목록 가져오기 위해 boardNoticeListService 인스턴스의 getArticleList() 메서드 호출
		// => 파라미터 : 읽어올 페이지번호(pageNum), 조회할 목록 갯수(listLimit)
		//    리턴타입 : ArrayList<NoticeBoardDTO>(articleList)
		ArrayList<NoticeBoardDTO> articleList = 
				boardNoticeListService.getArticleList(pageNum, listLimit);
		
		// ----------------------------------------------------------------------------
		// 페이징 처리를 위한 계산 작업
		// 1. 전체 페이지 수 계산(총 게시물 수 / 페이지 당 게시물 수 + 0.9)
		// => 총 게시물 수 / 페이지 당 게시물 수를 실수로 연산하려면 double 타입 형변환
		// => 계산된 결과값을 다시 정수형으로 변환
//			int maxPage = (int)((double)listCount / listLimit + 0.9);
		
		// Math.ceil() 메서드를 사용하여 올림 처리할 경우
		int maxPage = (int)Math.ceil((double)listCount / listLimit);	
		
		// 2. 현재 페이지에서 보여줄 시작 페이지 번호(1, 11, 21 등의 시작 번호) 계산
		int startPage = ((int)((double)pageNum / pageLimit + 0.9) - 1) * pageLimit + 1;
		
		// 3. 현재 페이지에서 보여줄 끝 페이지 번호(10, 20, 30 등의 끝 번호) 계산
		int endPage = startPage + pageLimit - 1;
		
		// 4. 끝 페이지가 현재 페이지에서 표시할 최대 페이지 수보다 클 경우
		//    끝 페이지 번호를 총 페이지 수로 대체
		if(endPage > maxPage) {
			endPage = maxPage;
		}
		
		// 페이지 정보를 저장할 PageInfo 객체 생성 후 페이지 정보 저장
		PageInfo pageInfo = new PageInfo(pageNum, maxPage, startPage, endPage, listCount);
		// -------------------------------------------------------------------------------------
		// 뷰페이지에서 사용할 데이터(페이지 정보, 게시물 목록 정보)를 request 객체에 저장
		request.setAttribute("pageInfo", pageInfo);
		request.setAttribute("articleList", articleList);
		// -------------------------------------------------------------------------------------
		// 게시물 목록 조회 작업 완료 후 ActionForward 객체를 통해 뷰페이지(notice_list.jsp)로 포워딩
		// => 요청 서블릿 주소(BoardNoticeList.bo)가 유지되어야 하고,
		//    request 객체가 유지되어야 하므로 Dispatcher 방식 포워딩 필요
		forward = new ActionForward();
		forward.setPath("./notice_list.jsp");
		forward.setRedirect(false); // 생략 가능
		
		return forward;
	}

}
