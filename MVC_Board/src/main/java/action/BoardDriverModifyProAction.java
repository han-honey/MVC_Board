package action;

import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import svc.BoardDriverModifyProService;
import svc.BoardDriverWriteProService;
import vo.ActionForward;
import vo.FileBoardDTO;

public class BoardDriverModifyProAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("BoardDriverModifyProAction");
		
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

//		System.out.println("original_file : " + original_file);
//		System.out.println("file : " + file);
		
		// 파일명도 FileBoardDTO 객체에 저장
		fileBoard.setOriginal_file(original_file);
		fileBoard.setFile(file);
		
	
		// BoardDriverModifyProService 클래스의 isArticleWriter() 메서드 호출하여 이동
		// => 작성자 본인 여부 판별(pass 와 num 을 사용하여 일치 여부 판별)
		BoardDriverModifyProService boardDriverModifyProService = new BoardDriverModifyProService();
		boolean isArticleWriter = 
				boardDriverModifyProService.isArticleWriter(fileBoard.getNum(), fileBoard.getPass());
		
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
			// BoardDriverModifyProService 클래스의 modifyArticle() 메서드 호출하여 이동
			// => 파라미터 : FileBoardDTO 객체,  리턴타입 : boolean(isModifySuccess)
			boolean isModifySuccess = boardDriverModifyProService.modifyArticle(fileBoard);
			
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
				// 주의! 페이지번호 전달 시 request 가 아닌 multi.getParameter() 사용
				forward.setPath("BoardDriverContent.bo?num=" + fileBoard.getNum() + "&page=" + multi.getParameter("page"));
				forward.setRedirect(true); // Redirect 방식 포워딩
			}
		}
		
		// ActionForward 객체 리턴
		return forward;
	}

}
















