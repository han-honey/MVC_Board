<%@page import="vo.NoticeBoardDTO"%>
<%@page import="vo.PageInfo"%>
<%@page import="vo.FileBoardDTO"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
// Action 클래스로부터 전달받은 request 객체에서 저장된 데이터 꺼내기
// => "pageInfo", "articleList" 속성명으로 저장된 객체 가져오기
ArrayList<NoticeBoardDTO> articleList = (ArrayList<NoticeBoardDTO>)request.getAttribute("articleList");
PageInfo pageInfo = (PageInfo)request.getAttribute("pageInfo");
int pageNum = pageInfo.getPageNum(); // 현재 페이지 번호
int maxPage = pageInfo.getMaxPage(); // 최대 페이지 번호
int startPage = pageInfo.getStartPage(); // 시작 페이지 번호
int endPage = pageInfo.getEndPage(); // 끝 페이지 번호
int listCount = pageInfo.getListCount(); // 총 게시물 수
%>	
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>center/notice_list.jsp</title>
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
			<h1>Notice List</h1>
			<table id="notice">
				<tr>
					<th class="tno">No.</th>
					<th class="ttitle">Title</th>
					<th class="twrite">Writer</th>
					<th class="tdate">Date</th>
					<th class="tread">Read</th>
				</tr>
				<%
				// ArrayList 객체가 null 이 아니고, 저장된 객체가 1개 이상일 때만 목록 출력
				if(articleList != null && articleList.size() > 0) {
					for(NoticeBoardDTO noticeBoard : articleList) {
						%>
						<tr onclick="location.href='./BoardNoticeContent.bo?num=<%=noticeBoard.getNum() %>&page=<%=pageNum%>'">
							<td><%=noticeBoard.getNum() %></td>
							<td class="left"><%=noticeBoard.getSubject() %></td>
							<td><%=noticeBoard.getName() %></td>
							<td><%=noticeBoard.getDate() %></td>
							<td><%=noticeBoard.getReadcount() %></td>
						</tr>
						<%
					}
				} else {
				%>
				<!-- 게시물 목록이 없을 경우 -->
				<tr>
					<td colspan="5">작성된 게시물이 없습니다.</td>
				</tr>
				<%} %>
			</table>
			<div id="table_search">
				<input type="button" value="글쓰기" class="btn" 
						onclick="location.href='./BoardNoticeWriteForm.bo'">
			</div>
			<div id="table_search">
				<form action="#" method="post">
					<input type="text" name="search" class="input_box">
					<input type="submit" value="Search" class="btn">
				</form>
			</div>
			
			<!-- 페이지 목록 출력하는 곳 -->
			<div class="clear"></div>
			<div id="page_control">
				<!-- 
				이전페이지 버튼은 현재 페이지 번호가 시작페이지 번호보다 클 때만 링크 표시하며
				현재 페이지번호 - 1 값을 파라미터로 전달
				-->
				<%if(pageNum == startPage) { %>
					Prev&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<%} else { %>
					<a href="./BoardNoticeList.bo?page=<%=pageNum - 1%>">Prev</a>
				<%} %>
				
				<!-- 페이지 목록은 시작 페이지번호부터 끝 페이지 번호까지 차례대로 표시 -->
				<%for(int i = startPage; i <= endPage; i++) { %>
					<!-- 페이지 번호 클릭 시 driver.jsp 페이지로 페이지번호(page)를 전달 -->
					<!-- 단, 현재 페이지 번호는 하이퍼링크 없이 표시 -->
					<%if(i == pageNum) { %>
						<%=i %>
					<%} else { %>
						<a href="./BoardNoticeList.bo?page=<%=i%>"><%=i %></a>
					<%} %>
				<%} %>
				
				<!-- 
				다음페이지 버튼은 현재 페이지 번호가 끝페이지 번호보다 작을 때만 링크 표시하며
				현재 페이지번호 + 1 값을 파라미터로 전달
				-->
				<%if(pageNum == endPage) { %>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Next
				<%} else { %>
					<a href="./BoardNoticeList.bo?page=<%=pageNum + 1%>">Next</a>
				<%} %>
			</div>
		</article>

		<div class="clear"></div>
		<!-- 푸터 들어가는곳 -->
		<jsp:include page="../inc/bottom.jsp" />
		<!-- 푸터 들어가는곳 -->
	</div>
</body>
</html>









