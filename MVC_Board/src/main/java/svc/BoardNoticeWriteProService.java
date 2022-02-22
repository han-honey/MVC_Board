package svc;

import java.sql.Connection;

import dao.NoticeBoardDAO;
import db.JdbcUtil;
import vo.NoticeBoardDTO;

public class BoardNoticeWriteProService {
	// 글쓰기 작업 요청을 위한 registArticle() 메서드 정의
	// => 파라미터 : Action 클래스로부터 전달받는 게시물 정보(NoticeBoardDTO 객체, noticeBoard)
	// => 리턴타입 : boolean(isWriteSuccess)
	public boolean registArticle(NoticeBoardDTO noticeBoard) {
		System.out.println("BoardDriverWriteProService - registArticle()");
		
		// 1. 글쓰기 작업 요청 처리 결과를 저장할 boolean 타입 변수 선언
		boolean isWriteSuccess = false;
		
		// 2. JdbcUtil 클래스로부터 Connection Pool 에 저장된 Connection 객체 가져오기(공통)
		// => DAO 객체에서 복수개의 메서드를 통해 작업을 처리할 경우
		//    Service 클래스에서 트랜잭션 처리를 위해 commit 또는 rollback 결정해야하므로
		//    Connection 객체를 Service 클래스에서 관리 필요
		// => JdbcUtil 클래스의 getConnection() 메서드 호출
		Connection con = JdbcUtil.getConnection();
		
		// 3. 비즈니스 로직을 수행할 NoticeBoardDAO 객체 생성(공통)
		// => 단, 싱글톤 패턴으로 구현되어 있으므로 이미 생성된 인스턴스를 리턴받기
		//    NoticeBoardDAO 클래스의 getInstance() 메서드 호출
		NoticeBoardDAO noticeBoardDAO = NoticeBoardDAO.getInstance();
		
		// 4. NoticeBoardDAO 객체의 setConnection() 메서드를 호출하여 Connection 객체 전달(공통)
		// => DAO 객체에서는 전달받은 Connection 객체를 사용하여 
		//    단순히 DB 연결 후 관련 작업만 수행하도록 하기 위함
		noticeBoardDAO.setConnection(con);
		
		// 5. DAO 객체의 XXX() 메서드를 호출하여 XXX 작업 수행 및 결과 리턴받기
		// => 글쓰기 작업을 위해 NoticeBoardDAO 객체의 insertNoticeBoard() 메서드 호출
		//    파라미터 : NoticeBoardDTO 객체    리턴타입 : int(insertCount)
		int insertCount = noticeBoardDAO.insertNoticeBoard(noticeBoard);
		
		// 6. 리턴받은 결과(insertCount)에 대한 처리(Commit, Rollback 결정)
		// => JdbcUtil 클래스의 commit() 또는 rollback() 메서드를 호출
		// => 성공 : insertCount > 0 일 때 commit 작업 수행 및 isWriteSuccess 를 true 로 변경
		//    실패 : rollback 작업 수행(isWriteSuccess 기본값이 false 이므로 변경 불필요)
		// => 주의! Commit, Rollback 과정에서 Connection 객체가 필요하므로
		//    DAO 클래스 내에서 Connection 객체를 close() 하지 않도록 해야한다!
		if(insertCount > 0) {
			JdbcUtil.commit(con);
			isWriteSuccess = true;
		} else {
			JdbcUtil.rollback(con);
		}
		
		// 7. 작업 수행 완료 후 Connection 객체 반환(공통)
		// => JdbcUtil 클래스의 close() 메서드를 호출하여 반환할 Connection 객체 전달
		JdbcUtil.close(con);
		
		// 8. 글쓰기 작업 요청 처리 결과 리턴
		return isWriteSuccess;
	}
	
}













