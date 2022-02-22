package svc;

import java.sql.Connection;

import dao.FileBoardDAO;
import db.JdbcUtil;
import vo.FileBoardDTO;

public class BoardDriverReplyProService {
	public boolean registReplyArticle(FileBoardDTO fileBoard) {
		System.out.println("BoardDriverReplyProService - registReplyArticle()");
		
		// 1. 글쓰기 작업 요청 처리 결과를 저장할 boolean 타입 변수 선언
		boolean isReplySuccess = false;
		
		// 2. JdbcUtil 클래스로부터 Connection Pool 에 저장된 Connection 객체 가져오기(공통)
		Connection con = JdbcUtil.getConnection();
		
		// 3. 비즈니스 로직을 수행할 FileBoardDAO 객체 생성(공통)
		FileBoardDAO fileBoardDAO = FileBoardDAO.getInstance();
		
		// 4. FileBoardDAO 객체의 setConnection() 메서드를 호출하여 Connection 객체 전달(공통)
		fileBoardDAO.setConnection(con);
		
		// 5. DAO 객체의 XXX() 메서드를 호출하여 XXX 작업 수행 및 결과 리턴받기
		// => 답글 작성 요청을 위한 insertReplyArticle() 메서드 호출
		int insertCount = fileBoardDAO.insertReplyArticle(fileBoard);
		
		// 6. 리턴받은 결과(insertCount)에 대한 처리(Commit, Rollback 결정)
		if(insertCount > 0) {
			JdbcUtil.commit(con);
			isReplySuccess = true;
		} else {
			JdbcUtil.rollback(con);
		}
		
		// 7. 작업 수행 완료 후 Connection 객체 반환(공통)
		JdbcUtil.close(con);
		
		// 8. 글쓰기 작업 요청 처리 결과 리턴
		return isReplySuccess;
	}
	
}













