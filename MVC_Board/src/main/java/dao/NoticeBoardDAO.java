package dao;

//import db.JdbcUtil;
import static db.JdbcUtil.close; // JdbcUtil 클래스의 close() 메서드 3개만 static import

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import vo.NoticeBoardDTO;

// 실제 비즈니스 로직을 수행할 NoticeBoardDAO 클래스 정의
// => 외부로부터 인스턴스마다 저장할 데이터를 각각 구별해야할 필요가 없으므로
//    싱글톤 패턴을 활용하여 인스턴스를 직접 생성하고 외부로 공유하도록 정의
public class NoticeBoardDAO {
	// -------------------- 싱글톤 패턴 -----------------------
	private static NoticeBoardDAO instance = new NoticeBoardDAO();
	
	private NoticeBoardDAO() {}

	public static NoticeBoardDAO getInstance() { // 리턴 전용이므로 Getter 만 필요
		return instance;
	}
	// --------------------------------------------------------
	// Service 클래스로부터 Connection 객체를 전달받아 멤버변수에 저장
	Connection con;

	public void setConnection(Connection con) { // 저장 전용이므로 Setter 만 필요
		this.con = con;
	}
	// --------------------------------------------------------
//	PreparedStatement pstmt;
//	ResultSet rs;
	// --------------------------------------------------------
	// 글쓰기 작업을 수행할 insertNoticeBoard() 메서드 정의
	// => 파라미터 : NoticeBoardDTO 객체    리턴타입 : int(insertCount)
	public int insertNoticeBoard(NoticeBoardDTO noticeBoard) {
		int insertCount = 0;
		
		// DB 작업에 필요한 공통 변수 선언(Connection 객체는 멤버변수로 존재하므로 제외)
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			// 새 글 작성 시 부여할 새 글의 번호 계산을 위해
			// 기존 게시물(레코드)의 번호(num) 중 가장 큰 번호 알아내기
			String sql = "SELECT MAX(num) FROM board";
			pstmt = con.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			// 현재 게시물의 가장 큰 번호 + 1 값을 정수형 변수 num 에 저장하기
			// => 단, 기존 게시물이 하나도 없을 경우 기본값으로 사용할 값(1)을 미리 저장하기
			int num = 1;
			
			// 현재 게시물 최대 번호값이 존재할 경우(즉, 다음 레코드가 존재할 경우)
			// 해당 번호 + 1 값을 num 변수에 저장하기
			if(rs.next()) { // 다음 레코드(최대값)가 존재할 경우
//					num = rs.getInt("MAX(num)") + 1;
				num = rs.getInt(1) + 1;
			}
			
//				System.out.println("새 글 번호 : " + num);
			
			// 새 글 번호를 포함하여 전달받은 데이터를 board 테이블에 추가(= 글쓰기)
			// => 글번호는 계산된 새 글 번호(num) 사용, 작성일 : now() 함수, 조회수 : 0
			sql = "INSERT INTO board VALUES(?,?,?,?,?,now(),0)";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, num);
			pstmt.setString(2, noticeBoard.getName());
			pstmt.setString(3, noticeBoard.getPass());
			pstmt.setString(4, noticeBoard.getSubject());
			pstmt.setString(5, noticeBoard.getContent());
			
			insertCount = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			System.out.println("SQL 구문 오류 발생!");
			e.printStackTrace();
		} finally {
			close(rs);
			close(pstmt);
		}
		
		return insertCount;
	}
	
	// 게시물 전체 갯수 조회를 위한 selectListCount() 메서드 정의
	// => 파라미터 : 없음   리턴타입 : int(listCount)
	public int selectListCount() {
		int listCount = 0;
		
		// DB 작업에 필요한 공통 변수 선언(Connection 객체는 멤버변수로 존재하므로 제외)
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			// 원하는 컬럼의 갯수를 조회하려면 COUNT(컬럼명 또는 *) 함수 사용
			String sql = "SELECT COUNT(*) FROM board";
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				listCount = rs.getInt(1); // 또는 "COUNT(*)" 지정
			}
		} catch (SQLException e) {
			System.out.println("SQL 구문 오류 발생!");
			e.printStackTrace();
		} finally {
			// static import 기능을 활용하여 JdbcUtil 클래스의 메서드를 포함할 경우
			close(rs);
			close(pstmt);
		}
		
		return listCount;
	}
	
	// 게시물 목록 조회를 위한 selectArticleList() 메서드 정의 => 페이징 처리 추가
	// => 파라미터 : 페이지번호(page), 페이지당 게시물 수(limit)
	public ArrayList<NoticeBoardDTO> selectArticleList(int page, int limit) {
		ArrayList<NoticeBoardDTO> noticeBoardList = null;
		
		// DB 작업에 필요한 공통 변수 선언(Connection 객체는 멤버변수로 존재하므로 제외)
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			// 현재 페이지에서 조회할 레코드의 첫번째 행번호 계산
			// 1페이지일 때 0, 2페이지일 때 10, 3페이지일 때 20
			// => (현재페이지번호 - 1) * 페이지 당 게시물 수
			int startRow = (page - 1) * limit;
			System.out.println("시작 행번호 : " + startRow);
			
			// 전체 게시물 목록 조회(번호 기준 내림차순 정렬)
			// => LIMIT 절 뒤의 파라미터는 시작 행번호, 레코드 수를 지정
			String sql = "SELECT * FROM board ORDER BY num DESC LIMIT ?,?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, startRow);
			pstmt.setInt(2, limit);
			rs = pstmt.executeQuery();
			
			noticeBoardList = new ArrayList<NoticeBoardDTO>();
			
			while(rs.next()) {
				// 1개 레코드 정보를 저장할 NoticeBoardDTO 객체 생성 후 데이터 저장
				// => 단, 파일명은 목록 출력 대상에 포함되지 않으므로 저장 생략 가능
				NoticeBoardDTO noticeBoard = new NoticeBoardDTO();
				noticeBoard.setNum(rs.getInt("num"));
				noticeBoard.setName(rs.getString("name"));
//						noticeBoard.setPass(rs.getString("pass"));
				noticeBoard.setSubject(rs.getString("subject"));
//						noticeBoard.setContent(rs.getString("content"));
				noticeBoard.setDate(rs.getDate("date"));
				noticeBoard.setReadcount(rs.getInt("readcount"));
				
				// 모든 레코드를 저장할 ArrayList 객체에 NoticeBoardDTO 객체를 추가
				noticeBoardList.add(noticeBoard);
			}
			
		} catch (SQLException e) {
			System.out.println("SQL 구문 오류 발생!");
			e.printStackTrace();
		} finally {
			close(rs);
			close(pstmt);
		}
		
		return noticeBoardList;
	}

	// 게시물 상세 정보 조회 요청을 위한 selectArticle() 메서드 정의 
	// => 파라미터 : 글번호(num)    리턴타입 : NoticeBoardDTO(noticeBoard)
	public NoticeBoardDTO selectArticle(int num) {
		System.out.println("NoticeBoardDAO - selectArticle()");
		NoticeBoardDTO noticeBoard = null;
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			String sql = "SELECT * FROM board WHERE num=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, num);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				noticeBoard = new NoticeBoardDTO();
				noticeBoard.setNum(rs.getInt("num"));
				noticeBoard.setName(rs.getString("name"));
				noticeBoard.setSubject(rs.getString("subject"));
				noticeBoard.setContent(rs.getString("content"));
				noticeBoard.setDate(rs.getDate("date"));
				noticeBoard.setReadcount(rs.getInt("readcount"));
			}
		} catch (SQLException e) {
			System.out.println("SQL 구문 오류 발생!");
			e.printStackTrace();
		} finally {
			close(rs);
			close(pstmt);
		} 
		
		return noticeBoard;
	}

	// 게시물 조회 성공했을 경우 조회수 증가작업을 위해 updateReadcount() 메서드 호출
	// => 파라미터 : 글번호(num)    리턴타입 : void
	public void updateReadcount(int num) {
		PreparedStatement pstmt = null;
		
		try {
			String sql = "UPDATE board SET readcount=readcount+1 WHERE num=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, num);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("SQL 구문 오류 발생!");
			e.printStackTrace();
		} finally {
			close(pstmt);
		} 
	}

	// 작성자 본인 여부 판별하는 isArticleWriter() 메서드 정의
	// => 파라미터 : 글번호, 패스워드    리턴타입 : boolean
	public boolean isArticleWriter(int num, String pass) {
		boolean isArticleWriter = false;
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			// 번호(num) 와 패스워드(pass)가 모두 일치하는 레코드 조회(컬럼은 아무거나)
			String sql = "SELECT num FROM board WHERE num=? AND pass=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, num);
			pstmt.setString(2, pass);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				// 패스워드 일치할 경우 isArticleWriter 값을 true 로 변경
				isArticleWriter = true;
			}
		} catch (SQLException e) {
			System.out.println("SQL 구문 오류 발생!");
			e.printStackTrace();
		} finally {
			close(rs);
			close(pstmt);
		} 
		
		return isArticleWriter;
	}
	
	// 글 삭제 작업을 수행할 deleteArticle() 메서드 정의
	public int deleteArticle(int num) {
		int deleteCount = 0; 
		
		PreparedStatement pstmt = null;
		
		try {
			String sql = "DELETE FROM board WHERE num=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, num);
			deleteCount = pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("SQL 구문 오류 발생!");
			e.printStackTrace();
		} finally {
			close(pstmt);
		} 
		
		return deleteCount;
	}

	// 게시물 수정 작업을 수행하는 updateArticle() 메서드 정의
	public int updateArticle(NoticeBoardDTO noticeBoard) {
		int updateCount = 0;
		
		PreparedStatement pstmt = null;
		
		try {
			// 글번호(num)에 해당하는 레코드의 작성자(name), 제목(subject), 내용(content) 수정
			String sql = "UPDATE board SET name=?,subject=?,content=? WHERE num=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, noticeBoard.getName());
			pstmt.setString(2, noticeBoard.getSubject());
			pstmt.setString(3, noticeBoard.getContent());
			pstmt.setInt(4, noticeBoard.getNum());
			
			updateCount = pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("SQL 구문 오류 발생!");
			e.printStackTrace();
		} finally {
			close(pstmt);
		} 
		
		return updateCount;
	}

	// 최근 공지사항 5개를 조회하는 selectRecentList() 메서드 정의
	public ArrayList<NoticeBoardDTO> selectRecentList(int listLimit) {
		ArrayList<NoticeBoardDTO> noticeBoardList = null;
		
		// DB 작업에 필요한 공통 변수 선언(Connection 객체는 멤버변수로 존재하므로 제외)
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			// 전체 게시물 목록 조회(번호 기준 내림차순 정렬)
			// => LIMIT 절 뒤의 파라미터는 시작 행번호(0 고정 = 최신글), 레코드 수를 지정
			String sql = "SELECT * FROM board ORDER BY num DESC LIMIT 0,?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, listLimit); // 가져올 레코드 수
			rs = pstmt.executeQuery();
			
			noticeBoardList = new ArrayList<NoticeBoardDTO>();
			
			while(rs.next()) {
				// 1개 레코드 정보를 저장할 NoticeBoardDTO 객체 생성 후 데이터 저장
				// => 단, 파일명은 목록 출력 대상에 포함되지 않으므로 저장 생략 가능
				NoticeBoardDTO noticeBoard = new NoticeBoardDTO();
				noticeBoard.setNum(rs.getInt("num"));
				noticeBoard.setSubject(rs.getString("subject"));
				noticeBoard.setContent(rs.getString("content"));
				noticeBoard.setDate(rs.getDate("date"));
				
				// 모든 레코드를 저장할 ArrayList 객체에 NoticeBoardDTO 객체를 추가
				noticeBoardList.add(noticeBoard);
			}
			
		} catch (SQLException e) {
			System.out.println("SQL 구문 오류 발생!");
			e.printStackTrace();
		} finally {
			close(rs);
			close(pstmt);
		}
		
		return noticeBoardList;
	}
		
	
	
}















