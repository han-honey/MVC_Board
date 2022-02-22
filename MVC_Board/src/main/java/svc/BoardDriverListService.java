package svc;

import java.sql.Connection;
import java.util.ArrayList;

import dao.FileBoardDAO;
import vo.FileBoardDTO;

//import db.JdbcUtil;
// static import 기능을 사용하면 특정 클래스 내의 static 메서드들을
// 클래스명 없이 메서드명만으로 호출 가능해짐
// 기본 문법 : import static 패키지명.클래스명.메서드명; 또는 import static 패키지명.클래스명.*;
// => 주의! 메서드명 지정 시 소괄호() 는 명시하지 않음
//import static db.JdbcUtil.getConnection; // getConnection() 메서드만 import
import static db.JdbcUtil.*; // JdbcUtil 클래스의 모든 static 메서드를 import

public class BoardDriverListService {
	// 현재 file_board 테이블의 총 게시물 수 가져오기 위한 getListCount() 메서드 정의
	// => 파라미터 : 없음   리턴타입 : int(listCount)
	public int getListCount() {
		System.out.println("BoardDriverListService - getListCount()");
		
		int listCount = 0;
		
		// 공통작업-1 - JdbcUtil 클래스로부터 Connection 객체 가져오기
//		Connection con = JdbcUtil.getConnection();
		Connection con = getConnection();
		
		// 공통작업-2 - FileBoardDAO 클래스로부터 FileBoardDAO 객체 가져오기
		FileBoardDAO fileBoardDAO = FileBoardDAO.getInstance();		
		
		// 공통작업-3 - Connection 객체를 FileBoardDAO 객체에 전달
		fileBoardDAO.setConnection(con);
		
		// FileBoardDAO 객체의 selectListCount() 메서드를 호출하여
		// 현재 file_board 테이블의 전체 게시물 수 조회 요청
		// => 파라미터 : 없음   리턴타입 : int(listCount)
		listCount = fileBoardDAO.selectListCount();
		
		// SELECT 작업만 수행한 경우 Commit 또는 Rollback 작업 불필요
		
		// 공통작업-4 - Connection 객체 반환
		close(con);
		
		return listCount;
	}

	
	// 게시물 목록 조회 작업 요청을 위한 getAritcleList() 메서드 정의
	// => 파라미터 : 읽어올 페이지번호(pageNum), 조회할 목록 갯수(listLimit)
	//    리턴타입 : ArrayList<FileBoardDTO>(articleList)
	public ArrayList<FileBoardDTO> getArticleList(int pageNum, int listLimit) {
		System.out.println("BoardDriverListService - getArticleList()");
		ArrayList<FileBoardDTO> articleList = null;
		
		// 공통작업-1 - JdbcUtil 클래스로부터 Connection 객체 가져오기
		Connection con = getConnection();
		
		// 공통작업-2 - FileBoardDAO 클래스로부터 FileBoardDAO 객체 가져오기
		FileBoardDAO fileBoardDAO = FileBoardDAO.getInstance();		
		
		// 공통작업-3 - Connection 객체를 FileBoardDAO 객체에 전달
		fileBoardDAO.setConnection(con);
		
		// 게시물 목록 조회 작업 요청을 위해 Service 클래스의 selectArticleList() 메서드 호출
		// => 파라미터 : 페이지번호(pageNum), 페이지당 게시물 수(listLimit)
		// => 리턴타입 : ArrayList<FileBoardDTO>(articleList)
		articleList = fileBoardDAO.selectArticleList(pageNum, listLimit);
		
		// 공통작업-4 - Connection 객체 반환
		close(con);
		
		return articleList;
	}
}












