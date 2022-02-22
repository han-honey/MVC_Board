<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
// 세션에 저장된 아이디(sessionId 속성값) 가져와서 id 변수에 저장하기
String id = (String)session.getAttribute("sessionId");
%>    
<header>
  <!-- login join -->
  <%if(id == null) { %>
  	<!-- 세션 아이디가 존재하지 않을 경우(= null 일 경우) login, join 링크 표시 -->
  	<div id="login"><a href="../member/login.jsp">login</a> | <a href="../member/join.jsp">join</a></div>
  <%} else { %>
	  <!-- 세션 아이디가 존재할 경우(아니면) 로그인된 아이디(xxx 님)와 logout 링크(logout.jsp) 표시 -->
	  <div id="login"><a href="#"><%=id %></a>님 | <a href="../member/logout.jsp">logout</a></div>
  <%} %>
  
  <div class="clear"></div>
  <!-- 로고들어가는 곳 -->
  <div id="logo"><img src="../images/logo.gif"></div>
  <!-- 메뉴들어가는 곳 -->
  <nav id="top_menu">
  	<ul>
  		<li><a href="../main/Main.bo">HOME</a></li>
  		<li><a href="../company/Company.bo">COMPANY</a></li>
  		<li><a href="../company/Solutions.bo">SOLUTIONS</a></li>
		<li><a href="../center/BoardNoticeList.bo">CUSTOMER CENTER</a></li>
  		<li><a href="../mail/MailForm.bo">CONTACT US</a></li>
  	</ul>
  </nav>
</header>













