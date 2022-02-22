<%@page import="vo.NoticeBoardDTO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
// NoticeBoardDTO 객체를 request 객체로부터 꺼내기(Object -> NoticeBoardDTO 변환 필수)
NoticeBoardDTO noticeBoard = (NoticeBoardDTO)request.getAttribute("noticeBoard");
%>	
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>center/driver_update.jsp</title>
<link href="../css/default.css" rel="stylesheet" type="text/css">
<link href="../css/subpage.css" rel="stylesheet" type="text/css">
</head>
<body>
	<div id="wrap">
		<!-- 헤더 들어가는곳 -->
		<jsp:include page="../inc/top.jsp" />
		<!-- 헤더 들어가는곳 -->

		<!-- 본문들어가는 곳 -->
		<!-- 본문 메인 이미지 -->
		<div id="sub_img_center"></div>
		<!-- 왼쪽 메뉴 -->
		<nav id="sub_menu">
			<ul>
				<li><a href="./BoardNoticeList.bo">Notice</a></li>
				<li><a href="#">Public News</a></li>
				<li><a href="./BoardDriverList.bo">Driver Download</a></li>
				<li><a href="#">Service Policy</a></li>
			</ul>
		</nav>
		<!-- 본문 내용 -->
		<article>
			<h1>Driver Modify</h1>
			<form action="./BoardNoticeModifyPro.bo" method="post">
				<!-- 글번호(num), 페이지번호(page) 는 input type="hidden" 속성으로 넘김 -->
				<input type="hidden" name="num" value="<%=request.getParameter("num") %>">
				<input type="hidden" name="page" value="<%=request.getParameter("page") %>">
				<table id="notice">
					<tr>
						<td>작성자</td>
						<td><input type="text" name="name" value="<%=noticeBoard.getName()%>" readonly="readonly"></td>
					</tr>
					<tr>
						<td>비밀번호</td>
						<td><input type="password" name="pass" required="required"></td>
					</tr>
					<tr>
						<td>제목</td>
						<td><input type="text" name="subject" value="<%=noticeBoard.getSubject()%>" required="required"></td>
					</tr>
					<tr>
						<td>내용</td>
						<td><textarea rows="10" cols="20" name="content" required="required"><%=noticeBoard.getContent()%></textarea></td>
					</tr>
				</table>

				<div id="table_search">
					<input type="submit" value="글수정" class="btn">
				</div>
			</form>
			<div class="clear"></div>
		</article>


		<div class="clear"></div>
		<!-- 푸터 들어가는곳 -->
		<jsp:include page="../inc/bottom.jsp" />
		<!-- 푸터 들어가는곳 -->
	</div>
</body>
</html>


