<%@page import="vo.FileBoardDTO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
// FileBoardDTO 객체를 request 객체로부터 꺼내기(Object -> FileBoardDTO 변환 필수)
FileBoardDTO fileBoard = (FileBoardDTO)request.getAttribute("fileBoard");
%>	
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>center/driver_reply.jsp</title>
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
			<h1>Driver Reply</h1>
			<!-- 파일 업로드 기능을 동작시키려면 form 태그에 
					enctype 속성값으로 "multipart/form-data" 값 지정해야함 -->
			<form action="./BoardDriverReplyPro.bo" method="post" enctype="multipart/form-data">
				<!-- 글번호(num), 페이지번호(page) 는 input type="hidden" 속성으로 넘김 -->
				<input type="hidden" name="num" value="<%=request.getParameter("num") %>">
				<input type="hidden" name="page" value="<%=request.getParameter("page") %>">
				<!-- 답글 작성에 필요한 관련 정보(re_ref, re_lev, re_seq 값도 함께 전달 -->
				<input type="hidden" name="re_ref" value="<%=fileBoard.getRe_ref() %>">
				<input type="hidden" name="re_lev" value="<%=fileBoard.getRe_lev() %>">
				<input type="hidden" name="re_seq" value="<%=fileBoard.getRe_seq() %>">
				<!-- 파일 업로드 수행을 하지 않고 기존 파일명을 전달해야할 경우 -->
<%-- 				<input type="hidden" name="original_file" value="<%=fileBoard.getOriginal_file() %>"> --%>
				<table id="notice">
					<tr>
						<td>작성자</td>
						<td><input type="text" name="name" required="required"></td>
					</tr>
					<tr>
						<td>비밀번호</td>
						<td><input type="password" name="pass" required="required"></td>
					</tr>
					<tr>
						<td>제목</td>
						<td><input type="text" name="subject" value="Re: <%=fileBoard.getSubject()%>" required="required"></td>
					</tr>
					<tr>
						<td>내용</td>
						<td><textarea rows="10" cols="20" name="content" required="required">--- 원본글 ---<%="\n" + fileBoard.getContent()%></textarea></td>
					</tr>
					<tr>
						<td>파일</td>
						<td><input type="file" name="file"></td>
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


