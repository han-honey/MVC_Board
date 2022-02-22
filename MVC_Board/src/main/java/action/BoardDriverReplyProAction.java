package action;

import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import svc.BoardDriverReplyProService;
import vo.ActionForward;
import vo.FileBoardDTO;

public class BoardDriverReplyProAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("BoardDriverReplyProAction");
		
		// 포워딩 정보를 저장할 ActionForward 타입 변수 선언
		ActionForward forward = null;
		
		// -------------------------------------------------------------------------------------
		// 비즈니스 로직(데이터베이스 처리)을 위한 데이터 준비 작업 수행
		// => 게시판 글쓰기 폼에서 글 작성 후 글 쓰기 버튼을 클릭 시 현재 액션 클래스로 이동하며
		//    DB 작업 수행에 필요한 데이터를 준비(= 입력받은 게시글 정보 가져오기)
		// => 파일 업로드가 가능한 게시판이므로 MultipartRequest 객체를 통해
		//    name, pass, subject, content, file 파라미터 가져오기
		

		String uploadPath = "/upload";
		int fileSize = 1024 * 1024 * 10;
		ServletContext context = request.getServletContext();
		String realPath = context.getRealPath(uploadPath); // 파라미터로 가상의 업로드 폴더명 전달

		// MultipartRequest 객체 생성
		MultipartRequest multi = new MultipartRequest(
			request, // 1) 실제 요청 정보가 포함된 request 객체
			realPath, // 2) 실제 업로드 폴더 경로
			fileSize, // 3) 업로드 파일 크기
			"UTF-8", // 4) 한글 파일명에 대한 인코딩 방식
			new DefaultFileRenamePolicy() // 5) 중복 파일명에 대한 처리를 담당하는 객체
		);

		FileBoardDTO fileBoard = new FileBoardDTO();
		fileBoard.setNum(Integer.parseInt(multi.getParameter("num")));
		fileBoard.setName(multi.getParameter("name"));
		fileBoard.setPass(multi.getParameter("pass"));
		fileBoard.setSubject(multi.getParameter("subject"));
		fileBoard.setContent(multi.getParameter("content"));
		
		// 단, 파일 정보를 가져올 때 getParameter() 메서드 사용 불가!
		// 1) 파일명을 관리하는 객체에 접근 가져오기
		String fileElement = multi.getFileNames().nextElement().toString();
		// 2) 1번에서 가져온 이름을 활용하여 원본 파일명과 실제 업로드 된 파일명 가져오기
		String original_file = multi.getOriginalFileName(fileElement);
		String file = multi.getFilesystemName(fileElement);
		// => 주의! 수정 시 파일 업로드를 수행하지 않으면 파일명에 null 값 전달됨
		//    (데이터베이스 작업 시 파일명 있을 경우/없을 경우 구분해서 수정 필요)
		// 파일명도 FileBoardDTO 객체에 저장
		fileBoard.setOriginal_file(original_file);
		fileBoard.setFile(file);
		
		// 답글 관련 정보도 FileBoardDTO 객체에 저장
		fileBoard.setRe_ref(Integer.parseInt(multi.getParameter("re_ref")));
		fileBoard.setRe_lev(Integer.parseInt(multi.getParameter("re_lev")));
		fileBoard.setRe_seq(Integer.parseInt(multi.getParameter("re_seq")));
		
		// -------------------------------------------------------------------------------------
		// 비즈니스 로직(DB 작업)을 실제로 수행하기 위해
		// BoardDriverReplyProService 클래스 인스턴스 생성 후 registReplyArticle() 메서드 호출
		// => 파라미터 : Action 클래스로부터 전달받는 게시물 정보(FileBoardDTO 객체, fileBoard)
		// => 리턴타입 : boolean(isReplySuccess)
		BoardDriverReplyProService boardDriverReplyProService = new BoardDriverReplyProService();
		boolean isReplySuccess = boardDriverReplyProService.registReplyArticle(fileBoard);
		
		// 데이터베이스 작업 완료 후 성공/실패 여부에 따라 포워딩 작업 수행
		if(!isReplySuccess) { // 실패 시(isReplySuccess == false)
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println("<script>"); // 자바스크립트 시작 태그
			out.println("alert('글 등록 실패!')"); // 메세지는 작은따옴표('')로 표기해야함
			out.println("history.back()");
			out.println("</script>"); // 자바스크립트 끝 태그
		} else { // 성공 시
			forward = new ActionForward();
			forward.setPath("./BoardDriverList.bo");
			forward.setRedirect(true);
		}
		
		// ActionForward 객체 리턴
		return forward;
	}

}
















