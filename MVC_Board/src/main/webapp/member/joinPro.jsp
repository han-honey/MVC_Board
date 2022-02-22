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
	// join.jsp 페이지로부터 전달받은 폼파라미터를 사용하여
	// MemberDAO 객체의 insertMember() 메서드를 호출하고
	// 해당 메서드를 통해 INSERT 작업을 수행한 후
	// 회원 가입 성공 시 joinSuccess.jsp 페이지로 이동하고
	// 실패 시 자바스크립트를 통해 "회원가입실패" 메세지 출력 후 이전페이지로 돌아가기
	// 0. POST 방식 한글 처리
	request.setCharacterEncoding("UTF-8");
	
	// 1. MemberDTO 객체 생성 및 폼파라미터 데이터 저장
	MemberDTO member = new MemberDTO();
	member.setId(request.getParameter("id"));
	member.setPass(request.getParameter("pass"));
	member.setName(request.getParameter("name"));
	member.setEmail(request.getParameter("email"));
	member.setMobile(request.getParameter("mobile"));
	member.setAddress(request.getParameter("address"));
	member.setPhone(request.getParameter("phone"));
	
	// 2. MemberDAO 객체 생성
	MemberDAO memberDAO = new MemberDAO();
	
	// 3. MemberDAO 객체의 insertMember() 메서드 호출
	// => 파라미터 : MemberDTO 객체    리턴타입 : int(insertCount)
	int insertCount = memberDAO.insertMember(member);
	
	// 4. 성공/실패 판별 후 처리
	if(insertCount > 0) { // 성공 시
		// joinSuccess.jsp 페이지로 포워딩
		response.sendRedirect("./joinSuccess.jsp");
	} else { // 실패 시
		// 자바스크립트로 오류 메세지 출력 및 이전페이지로 돌아가기
		%>
		<script type="text/javascript">
			alert("회원 가입 실패!");
			history.back();
		</script>
		<%
	}
	%>
</body>
</html>











