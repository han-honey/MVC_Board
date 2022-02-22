<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>member/join.jsp</title>
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
		  	<h1>Join Us</h1>
		  	<form action="joinPro.jsp" method="post" id="join" name="fr">
		  		<fieldset>
		  			<legend>Basic Info</legend>
		  			<label>User Id</label>
		  			<input type="text" name="id" class="id" id="id" readonly="readonly">
		  			<input type="button" value="dup. check" class="dup" id="btn"><br>
		  			
		  			<label>Password</label>
		  			<input type="password" name="pass" id="pass"><br> 			
		  			
		  			<label>Retype Password</label>
		  			<input type="password" name="pass2" id="pass2">
		  			<span id="retypePassCheckResult"><!-- 패스워드 일치 여부 결과 표시하는 영역 --></span><br>
		  			
		  			<label>Name</label>
		  			<input type="text" name="name" id="name"><br>
		  			
		  			<label>E-Mail</label>
		  			<input type="email" name="email" id="email"><br>
		  			
		  			<label>Mobile Phone Number</label>
		  			<input type="text" name="mobile" ><br>
		  		</fieldset>
		  		
		  		<fieldset>
		  			<legend>Optional</legend>
		  			<label>Address</label>
		  			<input type="text" name="address" ><br>
		  			<label>Phone Number</label>
		  			<input type="text" name="phone" ><br>
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


