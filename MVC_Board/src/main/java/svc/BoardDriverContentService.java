package svc;

import vo.FileBoardDTO;
import static db.JdbcUtil.*;

import java.sql.Connection;

import dao.FileBoardDAO;

public class BoardDriverContentService {

	public FileBoardDTO getArticle(int num) {
		System.out.println("BoardDriverContentService - getArticle()");
		FileBoardDTO fileBoard = null;
		
		// 공통작업-1 - JdbcUtil 클래스로부터 Connection 객체 가져오기
		Connection con = getConnection();
		
		// 공통작업-2 - FileBoardDAO 클래스로부터 FileBoardDAO 객체 가져오기
		FileBoardDAO fileBoardDAO = FileBoardDAO.getInstance();
		
		// 공통작업-3 - Connection 객체를 FileBoardDAO 객체에 전달
		fileBoardDAO.setConnection(con);
		
		// 게시물 조회 성공했을 경우 조회수 증가작업을 위해 updateReadcount() 메서드 호출
		// => 파라미터 : 글번호(num)    리턴타입 : void
		fileBoardDAO.updateReadcount(num);
		
		// FileBoardDAO 객체의 selectArticle() 메서드 호출하여 게시물 상세 정보 조회 요청
		// => 파라미터 : 글번호(num)    리턴타입 : FileBoardDTO(fileBoard)
		fileBoard = fileBoardDAO.selectArticle(num);
		
		// 게시물 조회 작업이 실패했을 경우 조회수 증가 작업에 대한 rollback 작업 수행하고
		// 성공했을 경우 commit 작업 수행
		if(fileBoard == null) {
			rollback(con);
		} else {
			commit(con);
		}
		
		// 공통작업-4 - Connection 객체 반환
		close(con);
		
		return fileBoard;
	}

}














