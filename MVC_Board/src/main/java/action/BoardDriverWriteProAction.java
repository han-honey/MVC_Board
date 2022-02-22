package action;

import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import svc.BoardDriverWriteProService;
import vo.ActionForward;
import vo.FileBoardDTO;

/*
 * XXXAction 클래스가 공통으로 갖는 execute() 메서드를 직접 정의하지 않고
 * Action 인터페이스를 상속받아 추상메서드를 구현하여 실수를 예방할 수 있음
 * => 추상메서드 execute() 구현을 강제 => 코드의 통일성과 안정성 향상
 */
public class BoardDriverWriteProAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("BoardDriverWriteProAction");
		
		// 포워딩 정보를 저장할 ActionForward 타입 변수 선언
		ActionForward forward = null;
		
		// -------------------------------------------------------------------------------------
		// 비즈니스 로직(데이터베이스 처리)을 위한 데이터 준비 작업 수행
		// => 게시판 글쓰기 폼에서 글 작성 후 글 쓰기 버튼을 클릭 시 현재 액션 클래스로 이동하며
		//    DB 작업 수행에 필요한 데이터를 준비(= 입력받은 게시글 정보 가져오기)
		// => 파일 업로드가 가능한 게시판이므로 MultipartRequest 객체를 통해
		//    name, pass, subject, content, file 파라미터 가져오기
		
		// 파일 업로드 관련 정보 처리를 위해 MultipartRequest 객체를 활용
		// 1. 업로드할 파일이 저장되는 이클립스 프로젝트 상의 경로를 변수에 저장
		String uploadPath = "/upload";
		// 2. 업로드 할 파일의 크기를 정수형태로 지정(10MB 로 지정)
//		    단, 파일 크기 지정 시 byte 단위부터 계산 과정을 차례대로 활용하면 유지보수 시 편리함
		// int fileSize = 10485760; // 10MB 크기 직접 지정 시
		int fileSize = 1024 * 1024 * 10; // byte(1) -> KB(1024 Byte) -> MB(1024 KB) -> 10MB 단위로 변환

		// 3. 현재 프로젝트(서블릿)를 처리하는 객체를 서블릿 컨텍스트라고 하며
		// 이 서블릿 컨텍스트를 객체 형태로 가져와서(request 객체의 getServletContext() 메서드 활용) 
		// 프로젝트 상의 가상 업로드 경로에 대한 실제 업로드 경로를 알아내기(getRealPath() 메서드 활용)
		ServletContext context = request.getServletContext();
		// 4. 업로드할 파일이 저장되는 실제 경로를 저장할 변수 선언
		String realPath = context.getRealPath(uploadPath); // 파라미터로 가상의 업로드 폴더명 전달
		// D:\Shared\JSP\workspace_jsp\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps\Funweb 폴더 내에
		// upload 폴더가 생성되어 있음
		// => 주의! 워크스페이스 내의 프로젝트 폴더를 바로 진입하면 있는 upload 폴더는 가상의 폴더이다!
//		System.out.println(realPath);

		// 5. 게시물 작성 후 글쓰기 버튼 클릭 시 게시물 정보는 request 객체에 담겨 전달되므로
//		    해당 파라미터들을 가져와서 BoardDTO 객체에 저장하기 위해서
//		    request.getParameter() 메서드 대신 MultipartRequest 객체를 통해 가져와야 함
//		    => 주의! MultipartRequest 객체를 사용하려면 cos.jar 라이브러리 필수!
//		       (WEB-INF / lib 폴더에 복사 및 Build path 등록 필요)
		// MultipartRequest 객체 생성
		MultipartRequest multi = new MultipartRequest(
			request, // 1) 실제 요청 정보가 포함된 request 객체
			realPath, // 2) 실제 업로드 폴더 경로
			fileSize, // 3) 업로드 파일 크기
			"UTF-8", // 4) 한글 파일명에 대한 인코딩 방식
			new DefaultFileRenamePolicy() // 5) 중복 파일명에 대한 처리를 담당하는 객체
		);

		// 6. MultipartRequest 객체의 getParameter() 사용하여 폼 파라미터 데이터 가져와서 
//		    FileBoardDTO 객체에 저장(주의! request.getParameter() 메서드가 아니다!)
		FileBoardDTO fileBoard = new FileBoardDTO();
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

//		System.out.println("실제 파일명 : " + file + ", 원본 파일명 : " + original_file);
		fileBoard.setOriginal_file(original_file);
		fileBoard.setFile(file);
		
//		System.out.println(fileBoard); // toString() 메서드 생략됨(전체 데이터 확인용)
		// -------------------------------------------------------------------------------------
		// 비즈니스 로직(DB 작업)을 실제로 수행하기 위해
		// BoardDriverWriteProService 클래스 인스턴스 생성 후 registArticle() 메서드 호출
		// => 파라미터 : Action 클래스로부터 전달받는 게시물 정보(FileBoardDTO 객체, fileBoard)
		// => 리턴타입 : boolean(isWriteSuccess)
		BoardDriverWriteProService boardDriverWriteProService = new BoardDriverWriteProService();
		boolean isWriteSuccess = boardDriverWriteProService.registArticle(fileBoard);
		
		// 데이터베이스 작업 완료 후 성공/실패 여부에 따라 포워딩 작업 수행
		if(!isWriteSuccess) { // 실패 시(isWriteSuccess == false)
			// 자바스크립트를 통해 오류 메세지 출력 및 이전 페이지로 이동 작업 수행
			// => 응답 객체인 response 객체를 활용하여 응답 페이지를 작성하면 자바스크립트 동작 가능
			// 1. response 객체의 setContextType() 메서드를 호출하여 문서 타입 설정
			response.setContentType("text/html; charset=UTF-8");
			// 2. response 객체로부터 문서 내용을 출력하기 위해 출력스트림 가져오기
			//    => response 객체의 getWriter() 메서드를 호출하여 PrintWriter 타입 객체 리턴받기
			PrintWriter out = response.getWriter();
			// 3. PrintWriter 객체의 println() 메서드를 호출하여 자바스크립트 코드를 문자열로 출력
			out.println("<script>"); // 자바스크립트 시작 태그
			// 자바스크립트의 alert() 함수를 호출하여 오류메세지 출력
			out.println("alert('글 등록 실패!')"); // 메세지는 작은따옴표('')로 표기해야함
			// 자바스크립트의 history.back() 함수를 호출하여 이전페이지로 돌아가기
			out.println("history.back()");
			out.println("</script>"); // 자바스크립트 끝 태그
		} else { // 성공 시
			// BoardDriverList.bo 서블릿 주소 요청하기 위해
			// 리턴형식으로 포워딩 할 주소를 BoardFrontController 쪽으로 넘겨주면
			// BoardFrontController 에서 해당 주소로 포워딩 작업 수행
			// 이 때, 포워딩 할 주소와 함께 포워딩 방식도 전달해야함
			// 1. ActionForward 인스턴스 생성
			forward = new ActionForward();
			// 2. ActionForward 객체에 포워딩 주소(URL)를 저장 => setPath() 메서드 호출
			forward.setPath("./BoardDriverList.bo");
			// 3. ActionForward 객체에 포워딩 방식을 지정 => setRedirect() 메서드 호출
			// => 새로운 작업을 수행하기 위한 새 요청이므로 Dispatcher 방식보다는
			//    Redirect 방식을 통한 포워딩이 더 적합함(= URL 주소창에 새 주소가 표시됨)
			// => Redirect 방식 포워딩을 위해서는 setRedirect() 메서드 파라미터에 true 전달
			forward.setRedirect(true);
		}
		
		// ActionForward 객체 리턴
		return forward;
	}

}
















