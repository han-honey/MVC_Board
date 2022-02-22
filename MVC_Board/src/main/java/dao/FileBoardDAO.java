package dao;

//import db.JdbcUtil;
import static db.JdbcUtil.close; // JdbcUtil 클래스의 close() 메서드 3개만 static import

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import vo.FileBoardDTO;

// 실제 비즈니스 로직을 수행할 FileBoardDAO 클래스 정의
// => 외부로부터 인스턴스마다 저장할 데이터를 각각 구별해야할 필요가 없으므로
//    싱글톤 패턴을 활용하여 인스턴스를 직접 생성하고 외부로 공유하도록 정의
public class FileBoardDAO {
	// -------------------- 싱글톤 패턴 -----------------------
	private static FileBoardDAO instance = new FileBoardDAO();
	
	private FileBoardDAO() {}

	public static FileBoardDAO getInstance() { // 리턴 전용이므로 Getter 만 필요
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
	// 글쓰기(파일 업로드 포함) 작업을 수행할 insertFileBoard() 메서드 정의
	// => 파라미터 : FileBoardDTO 객체    리턴타입 : int(insertCount)
	public int insertFileBoard(FileBoardDTO fileBoard) {
		int insertCount = 0;
		
		// DB 작업에 필요한 공통 변수 선언(Connection 객체는 멤버변수로 존재하므로 제외)
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			// 새 글 작성 시 부여할 새 글의 번호 계산을 위해
			// 기존 게시물(레코드)의 번호(num) 중 가장 큰 번호 알아내기
			String sql = "SELECT MAX(num) FROM file_board";
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
			sql = "INSERT INTO file_board VALUES(?,?,?,?,?,?,?,?,?,?,now(),?)";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, num);
			pstmt.setString(2, fileBoard.getName());
			pstmt.setString(3, fileBoard.getPass());
			pstmt.setString(4, fileBoard.getSubject());
			pstmt.setString(5, fileBoard.getContent());
			pstmt.setString(6, fileBoard.getFile());
			pstmt.setString(7, fileBoard.getOriginal_file());
			// 참조글번호(re_ref)는 새글번호(num)와 동일하게 지정하고
			// 들여쓰기레벨(re_lev)과 순서번호(re_seq)는 0으로 지정
			pstmt.setInt(8, num); // re_ref
			pstmt.setInt(9, 0); // re_lev
			pstmt.setInt(10, 0); // re_seq
			pstmt.setInt(11, 0); // readcount
			
			insertCount = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			System.out.println("SQL 구문 오류 발생!");
			e.printStackTrace();
		} finally {
			// DB 자원 반환 - JdbcUtil 클래스의 static 메서드 close() 호출
//			JdbcUtil.close(rs);
//			JdbcUtil.close(pstmt);
			// static import 기능을 활용하여 JdbcUtil 클래스의 메서드를 포함할 경우
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
			String sql = "SELECT COUNT(*) FROM file_board";
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
	public ArrayList<FileBoardDTO> selectArticleList(int page, int limit) {
		ArrayList<FileBoardDTO> fileBoardList = null;
		
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
//			String sql = "SELECT * FROM file_board ORDER BY num DESC LIMIT ?,?";
			// 답글쓰기 기능을 추가했을 경우 정렬 방식의 변경
			// => 참조글(원본글) 번호(re_ref)를 기준으로 내림차순,
			//    순서번호(re_seq) 를 기준으로 오름차순 정렬
			String sql = "SELECT * FROM file_board "
					+ "ORDER BY re_ref DESC,re_seq ASC "
					+ "LIMIT ?,?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, startRow);
			pstmt.setInt(2, limit);
			rs = pstmt.executeQuery();
			
			fileBoardList = new ArrayList<FileBoardDTO>();
			
			while(rs.next()) {
				// 1개 레코드 정보를 저장할 FileBoardDTO 객체 생성 후 데이터 저장
				// => 단, 파일명은 목록 출력 대상에 포함되지 않으므로 저장 생략 가능
				FileBoardDTO fileBoard = new FileBoardDTO();
				fileBoard.setNum(rs.getInt("num"));
				fileBoard.setName(rs.getString("name"));
//						fileBoard.setPass(rs.getString("pass"));
				fileBoard.setSubject(rs.getString("subject"));
//						fileBoard.setContent(rs.getString("content"));
//						fileBoard.setFile(rs.getString("file"));
//						fileBoard.setOriginal_file(rs.getString("original_file"));
				// 답글관련 데이터도 함께 저장
				fileBoard.setRe_ref(rs.getInt("re_ref"));
				fileBoard.setRe_lev(rs.getInt("re_lev"));
				fileBoard.setRe_seq(rs.getInt("re_seq"));
				fileBoard.setDate(rs.getDate("date"));
				fileBoard.setReadcount(rs.getInt("readcount"));
				
				// 모든 레코드를 저장할 ArrayList 객체에 FileBoardDTO 객체를 추가
				fileBoardList.add(fileBoard);
			}
			
		} catch (SQLException e) {
			System.out.println("SQL 구문 오류 발생!");
			e.printStackTrace();
		} finally {
			close(rs);
			close(pstmt);
		}
		
		return fileBoardList;
	}

	// 게시물 상세 정보 조회 요청을 위한 selectArticle() 메서드 정의 
	// => 파라미터 : 글번호(num)    리턴타입 : FileBoardDTO(fileBoard)
	public FileBoardDTO selectArticle(int num) {
		System.out.println("FileBoardDAO - selectArticle()");
		FileBoardDTO fileBoard = null;
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			String sql = "SELECT * FROM file_board WHERE num=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, num);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				fileBoard = new FileBoardDTO();
				fileBoard.setNum(rs.getInt("num"));
				fileBoard.setName(rs.getString("name"));
				fileBoard.setSubject(rs.getString("subject"));
				fileBoard.setContent(rs.getString("content"));
				fileBoard.setFile(rs.getString("file"));
				fileBoard.setOriginal_file(rs.getString("original_file"));
				fileBoard.setRe_ref(rs.getInt("re_ref"));
				fileBoard.setRe_lev(rs.getInt("re_lev"));
				fileBoard.setRe_seq(rs.getInt("re_seq"));
				fileBoard.setDate(rs.getDate("date"));
				fileBoard.setReadcount(rs.getInt("readcount"));
			}
		} catch (SQLException e) {
			System.out.println("SQL 구문 오류 발생!");
			e.printStackTrace();
		} finally {
			close(rs);
			close(pstmt);
		} 
		
		return fileBoard;
	}

	// 게시물 조회 성공했을 경우 조회수 증가작업을 위해 updateReadcount() 메서드 호출
	// => 파라미터 : 글번호(num)    리턴타입 : void
	public void updateReadcount(int num) {
		PreparedStatement pstmt = null;
		
		try {
			String sql = "UPDATE file_board SET readcount=readcount+1 WHERE num=?";
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
			String sql = "SELECT num FROM file_board WHERE num=? AND pass=?";
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
			String sql = "DELETE FROM file_board WHERE num=?";
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
	public int updateArticle(FileBoardDTO fileBoard) {
		int updateCount = 0;
		
		PreparedStatement pstmt = null;
		
		try {
			// 파일명이 null 인지 판별을 통해 서로 다른 SQL 구문 실행
			if(fileBoard.getFile() == null) { // 파일 수정 작업을 수행하지 않을 경우
				System.out.println("파일 수정 없음");
				// 글번호(num)에 해당하는 레코드의 작성자(name), 제목(subject), 내용(content) 수정
				String sql = "UPDATE file_board SET name=?,subject=?,content=? WHERE num=?";
				pstmt = con.prepareStatement(sql);
				pstmt.setString(1, fileBoard.getName());
				pstmt.setString(2, fileBoard.getSubject());
				pstmt.setString(3, fileBoard.getContent());
				pstmt.setInt(4, fileBoard.getNum());
			} else { // 파일 수정 작업을 수행할 경우
				System.out.println("파일 수정 있음");
				// 글번호(num)에 해당하는 레코드의 작성자(name), 제목(subject), 내용(content)과
				// 원본파일명(original_file), 실제파일명(file)도 수정
				String sql = "UPDATE file_board "
						+ "SET name=?,subject=?,content=?,original_file=?,file=? WHERE num=?";
				pstmt = con.prepareStatement(sql);
				pstmt.setString(1, fileBoard.getName());
				pstmt.setString(2, fileBoard.getSubject());
				pstmt.setString(3, fileBoard.getContent());
				pstmt.setString(4, fileBoard.getOriginal_file());
				pstmt.setString(5, fileBoard.getFile());
				pstmt.setInt(6, fileBoard.getNum());
			}
			
			updateCount = pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("SQL 구문 오류 발생!");
			e.printStackTrace();
		} finally {
			close(pstmt);
		} 
		
		return updateCount;
	}

	// 답글 작성을 위한 insertReplyArticle() 메서드 정의
	public int insertReplyArticle(FileBoardDTO fileBoard) {
		int insertCount = 0;
		
		// DB 작업에 필요한 공통 변수 선언(Connection 객체는 멤버변수로 존재하므로 제외)
		PreparedStatement pstmt = null, pstmt2 = null;
		ResultSet rs = null;
		
		
		try {
			// 새 글 작성 시 부여할 새 글의 번호 계산을 위해
			// 기존 게시물(레코드)의 번호(num) 중 가장 큰 번호 알아내기
			String sql = "SELECT MAX(num) FROM file_board";
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
			// ---------------------------------------------------------------
			// 답글 작성에 필요한 데이터를 별도의 변수에 저장(편의상)
			int re_ref = fileBoard.getRe_ref();
			int re_lev = fileBoard.getRe_lev();
			int re_seq = fileBoard.getRe_seq();
			// ---------------------------------------------------------------
			/*
			 * < 답글 등록에 따른 컬럼 데이터 변화 과정 추적 >
			 * 1. 기존 게시물 정보를 조회(re_ref 기준 내림차순, re_seq 기준 오름차순 정렬)
			 * +-----+--------+--------------------+--------+--------+--------+
			 * | num | name   | subject            | re_ref | re_lev | re_seq |
			 * +-----+--------+--------------------+--------+--------+--------+
			 * |  19 | 홍길동 | 새 글              |     19 |      0 |      0 |
			 * |  18 | 홍길동 | MVC 게시물2_수정44 |     18 |      0 |      0 |
			 * |  17 | 관리자 | MVC게시물1         |     17 |      0 |      0 |
			 * |  16 | admin  | 드라이버 파일      |     16 |      0 |      0 |
			 * |  15 | admin  | asd                |     15 |      0 |      0 |
			 * +-----+--------+--------------------+--------+--------+--------+
			 * 
			 * 2. 19번 게시물(원본글)에 대한 답글 작성 시(해당 글의 첫번째 답글일 경우)
			 *    (작성되는 답글의 새 글 번호는 20번이라고 가정)
			 *    => 원본 글의 참조글번호(re_ref)를 자신(답글)의 참조글번호로 사용
			 *    => 원본 글의 답글이므로 원본글의 들여쓰기레벨값+1 값을 자신의 re_lev 값으로 사용
			 *    => 원본 글의 답글이므로 원본글의 순서번호값+1 값을 자신의 re_seq 값으로 사용)
			 * +-----+--------+--------------------+--------+--------+--------+
			 * | num | name   | subject            | re_ref | re_lev | re_seq |
			 * +-----+--------+--------------------+--------+--------+--------+
			 * |  19 | 홍길동 | 새 글              |     19 |      0 |      0 |
			 *    20   답변자   Re: 새 글의 답변         19        1        1
			 *    (19번 글의 re_ref 값 그대로 사용, re_lev + 1 값 사용, re_seq + 1 값 사용)
			 * |  18 | 홍길동 | MVC 게시물2_수정44 |     18 |      0 |      0 |
			 * |  17 | 관리자 | MVC게시물1         |     17 |      0 |      0 |
			 * +-----+--------+--------------------+--------+--------+--------+   
			 * 
			 * 3. 19번 게시물(원본글)에 대한 두번째 답글 작성 시
			 *    (작성되는 답글의 새 글 번호는 21번이라고 가정)
			 *    => 원본 글의 참조글번호(re_ref)를 자신(답글)의 참조글번호로 사용
			 *    => 원본 글의 답글이므로 원본글의 들여쓰기레벨값+1 값을 자신의 re_lev 값으로 사용
			 *    => 순서번호(re_seq)는 원본 글의 순서번호(re_seq)보다 큰 값을 갖는 레코드들의
			 *       순서번호를 모두 +1 처리하고, 자신의 순서번호는 원본글 순서번호+1 값으로 사용
			 * +-----+--------+--------------------+--------+--------+--------+
			 * | num | name   | subject            | re_ref | re_lev | re_seq |
			 * +-----+--------+--------------------+--------+--------+--------+
			 * |  19 | 홍길동 | 새 글              |     19 |      0 |      0 |
			 *    21   답변자2  Re: 새 글의 답변2        19        1        1
			 *    (19번 글의 re_ref 값 그대로 사용, re_lev + 1 값 사용, re_seq + 1 값 사용)
			 *    (단, 기존 답글인 20번 글의 re_seq 는 기존 상태(1) + 1 값으로 증가시킴)
			 *    20   답변자   Re: 새 글의 답변         19        1        2(1 -> 2 로 증가)
			 * |  18 | 홍길동 | MVC 게시물2_수정44 |     18 |      0 |      0 |
			 * |  17 | 관리자 | MVC게시물1         |     17 |      0 |      0 |
			 * +-----+--------+--------------------+--------+--------+--------+ 
			 * 
			 * 4. 19번 게시물에 대한 세 번째 답글 작성 시(새 글 번호 30번이라고 가정)
			 * +-----+--------+--------------------+--------+--------+--------+
			 * | num | name   | subject            | re_ref | re_lev | re_seq |
			 * +-----+--------+--------------------+--------+--------+--------+
			 * |  19 | 홍길동 | 새 글              |     19 |      0 |      0 |
			 *    30   답변자3  Re: 새 글의 답변3        19        1        1
			 *    (19번 글의 re_ref 값 그대로 사용, re_lev + 1 값 사용, re_seq + 1 값 사용)
			 *    (단, 기존 답글인 20번, 21 글의 re_seq 는 기존 상태 + 1 값으로 증가시킴)
			 *    21   답변자2  Re: 새 글의 답변2        19        1        2(1 -> 2 로 증가)
			 *    20   답변자   Re: 새 글의 답변         19        1        3(2 -> 3 로 증가)
			 * |  18 | 홍길동 | MVC 게시물2_수정44 |     18 |      0 |      0 |
			 * |  17 | 관리자 | MVC게시물1         |     17 |      0 |      0 |
			 * +-----+--------+--------------------+--------+--------+--------+ 
			 * ---------------------------------------------------------------------------------
			 * 5. 20번 게시물에 대한 첫 번째 답글 작성 시(새 글 번호 35번이라고 가정)
			 * +-----+--------+--------------------+--------+--------+--------+
			 * | num | name   | subject            | re_ref | re_lev | re_seq |
			 * +-----+--------+--------------------+--------+--------+--------+
			 * |  19 | 홍길동 | 새 글              |     19 |      0 |      0 |
			 *    30   답변자3  Re: 새 글의 답변3        19        1        1
			 *    21   답변자2  Re: 새 글의 답변2        19        1        2
			 *    20   답변자   Re: 새 글의 답변         19        1        3
			 *    35  답변답변  Re: Re: 새 글의 답변     19        2        4
			 *    (20번 글의 re_ref 값 그대로 사용, re_lev + 1 값 사용, re_seq + 1 값 사용)
			 * |  18 | 홍길동 | MVC 게시물2_수정44 |     18 |      0 |      0 |
			 * |  17 | 관리자 | MVC게시물1         |     17 |      0 |      0 |
			 * +-----+--------+--------------------+--------+--------+--------+
			 *  
			 * 6. 20번 게시물에 대한 두 번째 답글 작성 시(새 글 번호 36번이라고 가정)
			 * +-----+--------+--------------------+--------+--------+--------+
			 * | num | name   | subject            | re_ref | re_lev | re_seq |
			 * +-----+--------+--------------------+--------+--------+--------+
			 * |  19 | 홍길동 | 새 글              |     19 |      0 |      0 |
			 *    30   답변자3  Re: 새 글의 답변3        19        1        1
			 *    21   답변자2  Re: 새 글의 답변2        19        1        2
			 *    20   답변자   Re: 새 글의 답변         19        1        3
			 *    36  답변답변2 Re: Re: 새 글의 답변2    19        2        4
			 *    (20번 글의 re_ref 값 그대로 사용, re_lev + 1 값 사용, re_seq + 1 값 사용)
			 *    (단, 기존 답글인 35번 글의 re_seq 는 기존 상태 + 1 값으로 증가시킴)
			 *    35  답변답변  Re: Re: 새 글의 답변     19        2        5(4 -> 5 증가)
			 * |  18 | 홍길동 | MVC 게시물2_수정44 |     18 |      0 |      0 |
			 * |  17 | 관리자 | MVC게시물1         |     17 |      0 |      0 |
			 * +-----+--------+--------------------+--------+--------+--------+  
			 * ---------------------------------------------------------------------------------
			 * 7. 21번 게시물에 대한 첫 번째 답글 작성 시(새 글 번호 44번이라고 가정)
			 * +-----+--------+--------------------+--------+--------+--------+
			 * | num | name   | subject            | re_ref | re_lev | re_seq |
			 * +-----+--------+--------------------+--------+--------+--------+
			 * |  19 | 홍길동 | 새 글              |     19 |      0 |      0 |
			 *    30   답변자3  Re: 새 글의 답변3        19        1        1
			 *    21   답변자2  Re: 새 글의 답변2        19        1        2
			 *    44   답변자44 Re: Re: 새 글의 답변2    19        2        3
			 *    (21번 글의 re_ref 값 그대로 사용, re_lev + 1 값 사용, re_seq + 1 값 사용)
			 *    (단, 기존 답글인 20, 36, 35번 글의 re_seq 는 기존 상태 + 1 값으로 증가시킴)
			 *    20   답변자   Re: 새 글의 답변         19        1        4(3 -> 4 증가)
			 *    36  답변답변2 Re: Re: 새 글의 답변2    19        2        5(4 -> 5 증가)
			 *    35  답변답변  Re: Re: 새 글의 답변     19        2        6(5 -> 6 증가)
			 * |  18 | 홍길동 | MVC 게시물2_수정44 |     18 |      0 |      0 |
			 * |  17 | 관리자 | MVC게시물1         |     17 |      0 |      0 |
			 * +-----+--------+--------------------+--------+--------+--------+    
			 */
			
			// 기존 게시물에 대한 답글 등록 전 순서번호 조정 필요
			// - 원본글의 참조글번호(re_ref)가 같은 게시물 중에서
			//   원본글의 순서번호(re_seq)보다 큰 번호를 갖는 게시물을 조회
			// - 조회된 게시물의 순서번호(re_seq)를 1씩 증가시킴
			sql = "UPDATE file_board SET re_seq=re_seq+1 "
					+ "WHERE re_ref=? AND re_seq>?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, re_ref);
			pstmt.setInt(2, re_seq);
			pstmt.executeUpdate();
			
			// 새로 등록할 답글의 들여쓰기레벨(re_lev)과 순서번호(re_seq)를
			// 원본글의 각 번호보다 1씩 큰 값으로 변경하여 등록
			re_lev++;
			re_seq++;
			
			// 위의 작업에서 PreparedStatement 객체를 사용했으므로
			// 다른 SQL 구문 작성 시 기존 PreparedStatement 객체를 반환(close())하거나
			// 새로운 변수를 선언하여 사용해야함(Resource Leak 발생할 수 있음)
//			pstmt.close();
			// -----------------------------------------------------------------------
			// 새 글 번호를 포함하여 전달받은 데이터를 board 테이블에 추가(= 글쓰기)
			// => 글번호는 계산된 새 글 번호(num) 사용, 작성일 : now() 함수, 조회수 : 0
			sql = "INSERT INTO file_board VALUES(?,?,?,?,?,?,?,?,?,?,now(),?)";
			pstmt2 = con.prepareStatement(sql);
			pstmt2.setInt(1, num);
			pstmt2.setString(2, fileBoard.getName());
			pstmt2.setString(3, fileBoard.getPass());
			pstmt2.setString(4, fileBoard.getSubject());
			pstmt2.setString(5, fileBoard.getContent());
			pstmt2.setString(6, fileBoard.getFile());
			pstmt2.setString(7, fileBoard.getOriginal_file());
			// 참조글번호(re_ref)는 원본글의 참조글번호(re_ref) 그대로 사용
			// 들여쓰기레벨(re_lev)과 순서번호(re_seq)는 계산된 값 사용
			pstmt2.setInt(8, re_ref); // 참조글의 re_ref 값
			pstmt2.setInt(9, re_lev); // 참조글 re_lev 값 + 1 수행한 값
			pstmt2.setInt(10, re_seq); // 참조글 re_seq 값 + 1 수행한 값
			pstmt2.setInt(11, 0); // readcount
			
			insertCount = pstmt2.executeUpdate();
			
		} catch (SQLException e) {
			System.out.println("SQL 구문 오류 발생!");
			e.printStackTrace();
		} finally {
			close(rs);
			close(pstmt);
			close(pstmt2);
		}
		
		return insertCount;
	}
	
	
	
	
}















