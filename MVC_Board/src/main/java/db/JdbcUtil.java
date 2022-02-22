package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class JdbcUtil {
	// DB 관련 보조 작업(연결, 자원반환, 커밋, 롤백)을 수행하는 메서드 정의
	// => DB 자원은 Connection Pool(DBCP) 로부터 Connection 객체를 가져와서 사용
	// => 단, 모든 메서드는 인스턴스 생성없이 호출하기 위해 static 메서드로 정의
	// 1. DB 연결 작업을 수행한 후 Connection 객체를 리턴하는 getConnection() 메서드 정의
	//    => 파라미터 : 없음    리턴타입 : javax.sql.Connection
	public static Connection getConnection() {
		Connection con = null;
		
		try {
			// context.xml 에 설정된 DBCP(커넥션풀)로부터 Connection 객체 가져와서 리턴하기
			// 1. 톰캣으로부터 컨텍스트 객체 가져오기(context.xml 파일의 Context 태그 부분 가져오기)
			// => InitialContext 객체 생성하여 Context 타입으로 업캐스팅하여 저장
			Context initCtx = new InitialContext();
			
			// 2. 생성된 Context 객체(initCtx)로부터 context.xml 에 정의된 Resource 태그 부분 가져오기
			// => 파라미터로 "java:comp/env" 문자열 전달
			// => 리턴타입 Object -> Context 타입으로 형변환 필요
//			Context envCtx = (Context)initCtx.lookup("java:comp/env");
			
			// 3. Resource 태그 내의 name 속성(리소스 이름 "jdbc/MySQL")을 가져오기 위해
			//    생성된 Context 객체(envCtx)의 lookup() 메서드를 호출
			// => 파라미터로 리소스 이름("jdbc/MySQL") 문자열 전달
			// => 리턴되는 객체를 javax.sql.DataSource 타입으로 변환하여 리턴받기
//			DataSource ds = (DataSource)envCtx.lookup("jdbc/MySQL");
			
			// ------ 참고! 2번과 3번 작업을 하나로 결합하는 경우 ------
			// => InitialContext 객체의 lookup() 메서드 호출하여 2번과 3번 문자열 결합하여 전달
			// => 설정 내용에 따라 jdbc/MySQL 부분만 달라질 수 있다!
			DataSource ds = (DataSource)initCtx.lookup("java:comp/env/jdbc/MySQL");
			// ----------------------------------------------------------------------
			
			// 4. DataSource 객체(= 커넥션풀)로부터 미리 생성되어 있는 Connection 객체 가져오기
			con = ds.getConnection();
			// 주의! context.xml 파일 내의 계정명(username)과 패스워드(password) 미등록 시
			// getConnection() 메서드 호출 시 파라미터에 전달도 가능함
			// => Properties 객체 활용하여 아이디와 패스워드를 변경 시 유연하게 대처 가능
			// ex) Connection con = ds.getConnection("root", "1234");
			
			
			// -------------------------------- 옵션 ---------------------------------
			// 트랜잭션 적용을 위해 데이터베이스(MySQL)의 Auto Commit 기능 해제할 경우
			// => Connection 객체의 setAutoCommit() 메서드를 호출하여 false 값 전달
			con.setAutoCommit(false); // Auto Commit 기능 해제(설정 시 true 전달)
			// => DML 구문(INSERT, UPDATE, DELETE) 수행 후 반드시 commit 작업 필수!
			// => 만약, 되돌려야할 경우 rollback 작업 필수!
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// 5. Connection 객체 리턴
		return con;
	}
	
	// 2. DB 작업 완료 후 자원(Connection, PreparedStatement, ResultSet) 반환을 위한 close() 메서드 정의
	// => 파라미터만 3개의 타입을 따로 전달받고 수행할 작업은 close() 메서드 호출이 동일하므로
	//    메서드 이름을 close() 메서드로 통일하여 3개의 메서드를 오버로딩
	public static void close(Connection con) {
		if(con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void close(PreparedStatement pstmt) {
		if(pstmt != null) {
			try {
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void close(ResultSet rs) {
		if(rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	// 3. 트랜잭션 처리에 필요한 커밋, 롤백 작업을 수행하기 위한 commit(), rollback() 메서드 정의
	// => 단, 데이터베이스 연결 객체 생성 시 Auto Commit 기능 해제 필수!
	// => 파라미터 : Connection 객체(con)   리턴타입 : void
	public static void commit(Connection con) {
		try {
			con.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void rollback(Connection con) {
		try {
			con.rollback();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}










