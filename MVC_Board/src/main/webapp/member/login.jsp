<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>member/login.jsp</title>
<link href="../css/default.css" rel="stylesheet" type="text/css">
<link href="../css/subpage.css" rel="stylesheet" type="text/css">
</head>
<body>
	<div id="wrap">
		<!-- 헤더 들어가는곳 -->
		<jsp:include page="../inc/top.jsp"/>
		<!-- 헤더 들어가는곳 -->
		  
		<!-- 본문들어가는 곳 -->
		  <!-- 본문 메인 이미지 -->
		  <div id="sub_img_member"></div>
		  <!-- 왼쪽 메뉴 -->
		  <nav id="sub_menu">
		  	<ul>
		  		<li><a href="#">Join us</a></li>
		  		<li><a href="#">Privacy policy</a></li>
		  	</ul>
		  </nav>
		  <!-- 본문 내용 -->
		  <article>
		  	<h1>Login</h1>
		  	<form action="loginPro.jsp" method="post" id="join">
		  		<fieldset>
		  			<legend>Login Info</legend>
		  			<label>User Id</label>
		  			<input type="text" name="id"><br>
		  			
		  			<label>Password</label>
		  			<input type="password" name="pass"><br>
		  			
		  			<input type="checkbox" value="remember_id">ID 기억하기&nbsp;&nbsp;
		  			<input type="checkbox" value="remember_login">로그인 유지<br>
		  		</fieldset>
		
		  		<div class="clear"></div>
		  		<div id="buttons">
		  			<input type="submit" value="Submit" class="submit">
		  			<input type="reset" value="Cancel" class="cancel">
		  		</div>
		  	</form>
		  </article>
		  
		<div class="clear"></div>  
		<!-- 푸터 들어가는곳 -->
		<jsp:include page="../inc/bottom.jsp"/>
		<!-- 푸터 들어가는곳 -->
	</div>
</body>
</html>


