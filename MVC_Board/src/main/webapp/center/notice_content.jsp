<%@page import="vo.NoticeBoardDTO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
// BoardNoticeContentAction 클래스로부터 전달받은 request 객체에서 데이터 꺼내기
NoticeBoardDTO noticeBoard = (NoticeBoardDTO)request.getAttribute("noticeBoard");
String pageNum = (String)request.getAttribute("page");
%>	
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>center/driver_content.jsp</title>
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
				<li><a href="./BoardNoticeList.bo">Driver Download</a></li>
				<li><a href="#">Service Policy</a></li>
			</ul>
		</nav>
		<!-- 본문 내용 -->
		<article>
			<h1>Driver Content</h1>
			<table id="notice">
				<tr>
					<td>글번호</td>
					<td><%=noticeBoard.getNum() %></td>
					<td>글쓴이</td>
					<td><%=noticeBoard.getName() %></td>
				</tr>
				<tr>
					<td>작성일</td>
					<td><%=noticeBoard.getDate() %></td>
					<td>조회수</td>
					<td><%=noticeBoard.getReadcount() %></td>
				</tr>
				<tr>
					<td>제목</td>
					<td colspan="3"><%=noticeBoard.getSubject() %></td>
				</tr>
				<tr>
					<td>내용</td>
					<td colspan="3"><%=noticeBoard.getContent() %></td>
				</tr>
			</table>

			<div>
				<textarea rows="4" cols="30"></textarea>
				<input type="button" value="댓글달기" onclick="#">
			</div>


			<div id="table_search">
				<input type="button" value="글수정" class="btn" onclick="location.href='./BoardNoticeModifyForm.bo?num=<%=noticeBoard.getNum()%>&page=<%=request.getParameter("page")%>'"> 
				<input type="button" value="글삭제" class="btn" onclick="location.href='./BoardNoticeDeleteForm.bo?num=<%=noticeBoard.getNum()%>&page=<%=request.getParameter("page")%>'"> 
				<input type="button" value="글목록" class="btn" onclick="location.href='./BoardNoticeList.bo?page=<%=request.getParameter("page")%>'">
			</div>

			<div class="clear"></div>
		</article>

		<div class="clear"></div>
		<!-- 푸터 들어가는곳 -->
		<jsp:include page="../inc/bottom.jsp" />
		<!-- 푸터 들어가는곳 -->
	</div>
</body>
</html>


