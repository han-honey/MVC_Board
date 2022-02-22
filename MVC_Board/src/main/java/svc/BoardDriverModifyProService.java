package svc;

import static db.JdbcUtil.*;

import java.sql.Connection;

import dao.FileBoardDAO;
import vo.FileBoardDTO;

public class BoardDriverModifyProService {
	
	// 글 삭제를 위한 본인 확인(패스워드 일치) 작업을 요청할 isArticleWriter() 메서드 정의
	public boolean isArticleWriter(int num, String pass) {
		System.out.println("BoardDriverModifyProService - isArticleWriter()");
		boolean isArticleWriter = false;
		
		// 공통작업-1 - JdbcUtil 클래스로부터 Connection 객체 가져오기
		Connection con = getConnection();
		
		// 공통작업-2 - FileBoardDAO 클래스로부터 FileBoardDAO 객체 가져오기
		FileBoardDAO fileBoardDAO = FileBoardDAO.getInstance();
		
		// 공통작업-3 - Connection 객체를 FileBoardDAO 객체에 전달
		fileBoardDAO.setConnection(con);
		
		// FileBoardDAO 클래스의 isArticleWriter() 메서드 호출하여
		// 작성자 본인 여부 판별(pass 와 num 을 사용하여 일치 여부 판별) 요청
		// => 파라미터 : 글번호, 패스워드    리턴타입 : boolean
		isArticleWriter = fileBoardDAO.isArticleWriter(num, pass);
		
		// 공통작업-4 - Connection 객체 반환
		close(con);
		
		return isArticleWriter;
	}

	// 글 수정 작업을 요청할 modifyArticle() 메서드 정의
	public boolean modifyArticle(FileBoardDTO fileBoard) {
		boolean isModifySuccess = false;
		
		// 공통작업-1 - JdbcUtil 클래스로부터 Connection 객체 가져오기
		Connection con = getConnection();
		
		// 공통작업-2 - FileBoardDAO 클래스로부터 FileBoardDAO 객체 가져오기
		FileBoardDAO fileBoardDAO = FileBoardDAO.getInstance();
		
		// 공통작업-3 - Connection 객체를 FileBoardDAO 객체에 전달
		fileBoardDAO.setConnection(con);
		
		// FileBoardDAO 클래스의 updateArticle() 메서드 호출하여 글 수정 작업 요청
		// => 파라미터 : 게시물 정보(FileBoardDTO 객체)    리턴타입 : int(updateCount)
		int updateCount = fileBoardDAO.updateArticle(fileBoard);
		
		// 리턴받은 수정 결과를 판별하여
		// updateCount > 0 일 경우 commit 작업 수행 및 isModifySuccess 를 true 로 변경
		// 아니면 rollback 작업 수행
		if(updateCount > 0) {
			commit(con);
			isModifySuccess = true;
		} else {
			rollback(con);
		}
		
		// 공통작업-4 - Connection 객체 반환
		close(con);
		
		return isModifySuccess;
	}

}














