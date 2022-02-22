<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
// 세션에 저장된 모든 데이터 삭제(초기화)
session.invalidate();

// //메인페이지로 포워딩
// response.sendRedirect("../main/main.jsp");
%>
<script>
	alert("로그아웃 완료");
	location.href = "../main/main.jsp";
</script>

