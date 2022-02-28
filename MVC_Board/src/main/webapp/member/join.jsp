<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>member/join.jsp</title>
<link href="../css/default.css" rel="stylesheet" type="text/css">
<link href="../css/subpage.css" rel="stylesheet" type="text/css">
<script type="text/javascript">
	/*
	자바스크립트에서의 정규표현식
	- 기본적인 정규표현식 패턴 작성 방법은 동일
	  => 단, 변수에 정규표현식 저장 시 문자열 데이터가 아닌 정규표현식 객체로 취급하기 위해
	     패턴 문자열을 따옴표로 둘러싸지 않음
	     
	< 패턴 작성 및 정규표현식 적용 기본 문법 >
	var regex = /패턴문자열/;
	if(regex.exec(검증할 데이터)) {
		// 패턴이 일치할 경우 실행할 문장들...
	}
	*/
	function checkIdRegex(id){
		// 아이디 패턴 검사 결과를 표시할 span 태그 영역 객체 가져오기
		var elemSpan = document.getElementById("checkIdRegexResult");
		
		// 아이디 검사 패턴 규칙 설정(ex. 4-16자리 영문자, 숫자 조합 유효성 검사)
		var regex = /^[A-Za-z0-9]{4,16}$/; // 정규표현식 패턴에 앞뒤로 / 기호 필수(객체로 생성)

		// 만약, 아이디 첫글자는 무조건 영문자여야하는 경우
// 		var regex = /^[A-Za-z][A-Za-z0-9]{3,15}$/;
		
		// 정규 표현식 패턴 객체의 exec() 메서드를 호출하여 검사할 문자열 전달
		if(regex.exec(id)){
			elemSpan.innerHTML = "사용 가능한 아이디";
			elemSpan.style.color = "BLUE";
		} else {
			elemSpan.innerHTML = "사용 불가능한 아이디";
			elemSpan.style.color = "RED";
		}
		
	}
	function checkPassRegex(pass){
		// 패스워드 패턴 검사 결과를 표시할 span 태그 영역 객체 가져오기
		var elemSpan = document.getElementById("checkPassRegexResult");
		
		// 패스워드 검사 패턴 규치 설정
		// 1. 길이 및 사용 가능 문자에 대한 규칙 : 8-16자리 영문자, 숫자, 특수문자 !@#$% 조합 유효성
		var lengthRegex = /^[A-za-z0-9!@#$%]{8,16}$/;
		// 각 문자 형식이 모두 포함되는지 여부를 각각 체크하기 위한 패턴 설정
		// 단, 부분 패턴 검사리므로 시작(^)과 끝($) 패턴은 사용하면 안된다!
		// 2. 영문 대문자에 대한 규칙(영문 대문자 1개가 포함되어야 함)
		var engUpperRegex = /[A-Z]/;
		// 3. 영문 소문자에 대한 규칙(영문 소문자 1개가 포함되어야 함)
		var engLowerRegex = /[a-z]/;
		// 4. 숫자에 대한 규칙(숫자 1개가 포함되어야 함)
		var numberRegex = /[0-9]/;
		// 5. 특수문자(!@#$%)에 대한 규칙(특수문자 1개가 포함되어야 함)
		var specRegex = /[!@#$%]/;
		
// 		if(lengthRegex.exec(pass) && engUpperRegex.exec(pass) && engLowerRegex.exec(pass) && numberRegex.exec(pass) && specRegex.exec(pass)){
// 			elemSpan.innerHTML = "사용 가능한 비밀번호";
// 			elemSpan.style.color = "BLUE";
// 		} else {
// 			elemSpan.innerHTML = "8-16자리 영문자, 숫자, 특수문자 !@#$% 조합 필수!";
// 			elemSpan.style.color = "RED";
// 		}

		if(lengthRegex.test(pass)){
			// 영문 대문자, 소문자, 숫자, 특수문자 중 포함된 항목을 카운팅하여
			// 패스워드 복잡도 판별 후 결과를 "안전", "보통", "위험", "사용 불가"로 구분
			var count = 0;
			
			if(engUpperRegex.test(pass)) { // 영문 대문자 포함 시
				count++;
			}
			// 주의! 각 조건마다 별도로 검사해야하므로 else if 가 아닌 각각의 if문 사용 필수!
			if(engLowerRegex.test(pass)) { // 영문 소문자 포함 시
				count++;
			}
			
			if(numberRegex.test(pass)) { // 숫자 포함 시
				count++;
			}
			
			if(specRegex.test(pass)) { // 특수문자(!@#$%) 포함 시
				count++;
			}
			
			// 패턴 카운팅 결과를 사용하여 복잡도 결과 판별 후 출력
			if(count == 4){
				elemSpan.innerHTML = "사용 가능 - 안전";
				elemSpan.style.color = "GREEN";
			} else if(count == 3){
				elemSpan.innerHTML = "사용 가능 - 보통";
				elemSpan.style.color = "ORANGE";
			} else if(count == 2){
				elemSpan.innerHTML = "사용 가능 - 위험";
				elemSpan.style.color = "PURPLE";
			} else if(count == 1){
				elemSpan.innerHTML = "사용 불가 - 영문자, 숫자, 특수문자 중 2가지 이상 조합 필수!";
				elemSpan.style.color = "RED";
			}
			
		} else {
			elemSpan.innerHTML = "8-16자리 영문자, 숫자, 특수문자 !@#$% 조합 필수!";
			elemSpan.style.color = "RED";
		}
		
	}
	
</script>
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
		  			<input type="text" name="id" class="id" id="id" onkeyup="checkIdRegex(this.value)">
		  			<input type="button" value="dup. check" class="dup" id="btn">
		  			<span id="checkIdRegexResult"></span><!-- 아이디 정규표현식 검사 결과 표시 영역 -->
		  			<br>
		  			
		  			<label>Password</label>
		  			<input type="password" name="pass" id="pass" onkeyup="checkPassRegex(this.value)">
		  			<span id="checkPassRegexResult"></span><!-- 아이디 정규표현식 검사 결과 표시 영역 -->	
		  			<br>	
		  			
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


