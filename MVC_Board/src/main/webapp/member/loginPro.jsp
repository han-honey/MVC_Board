<%@page import="member.MemberDAO"%>
<%@page import="member.MemberDTO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<%
	// login.jsp 페이지로부터 전달받은 폼파라미터(id, pass) 가져와서 MemberDTO 객체에 저장 후
	// MemberDAO 객체의 checkUser() 메서드를 호출하여 아이디/패스워드 일치 여부 판별
	// => 파라미터 : MemberDTO(id, pass)     리턴타입 : boolean(isLoginSuccess)
	MemberDTO member = new MemberDTO();
	member.setId(request.getParameter("id"));
	member.setPass(request.getParameter("pass"));
	
	MemberDAO memberDAO = new MemberDAO();
	boolean isLoginSuccess = memberDAO.checkUser(member);
	
	// 로그인 성공 시 메인페이지로 포워딩하고
	// 실패 시 자바스크립트를 통해 "로그인 실패" 출력 후 이전페이지로 돌아가기
	if(isLoginSuccess) { // 로그인 성공 시
		// 세션 객체(session)에 로그인 된 아이디를 sessionId 라는 속성명으로 저장하기
		session.setAttribute("sessionId", member.getId());
	
		// 만약, 로그인 상태를 자동으로 해제하지 않으려면(세션타이머를 무한대로 설정하려면)
		// session 객체의 setMaxInactiveInterval() 메서드 파라미터값을 0으로 설정
// 		session.setMaxInactiveInterval(0); // 웹브라우저 종료 또는 로그아웃 전에는 자동로그아웃 해제
	
		// 메인페이지로 포워딩
		response.sendRedirect("../main/main.jsp");
	} else { // 로그인 실패 시
		%>
		<script type="text/javascript">
			alert("아이디 또는 패스워드 틀림");
			history.back();
		</script>
		<%
	}
	%>
</body>
</html>














