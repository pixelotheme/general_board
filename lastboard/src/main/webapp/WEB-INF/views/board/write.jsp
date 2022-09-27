<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
        <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시판 글 등록</title>

<!-- CDN 방식의 Bootstrap 라이브러리 등록 -> 디자인의 웹표준을 구현한 웹 라이브러리 -->
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script	src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
<script	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
	
<!-- CDN 방식의 Google Icon 라이브러리 등록 -->
<link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">

<!-- jQuery UI 라이브러리 CDN 방식으로 등록 : datepicker나  -->
<link rel="stylesheet" href="http://code.jquery.com/ui/1.13.1/themes/base/jquery-ui.css">
<script src="https://code.jquery.com/ui/1.13.1/jquery-ui.js"></script>



<!-- 파일업로드 css 복붙 -->
<style type="text/css">
.uploadResult{
	width: 100%;

}
/**해당 태그 안의 ul 태그 를선택한다
flex - 한개의 div 안에서 칸을 나눠 쓸수있도록 설정
display:flex 가 되어있어야 flex-flow 설정가능 -> row 는 안에 들어가는 데이터들이 열로 정렬된다
justify-content 영역의 정렬
align-items 그안의 요소의 정렬
ul 태그 안에 li 태그가 추가될때 계속 추가되면 div 높이가 계속 높아질수도 있어서 css 로 지정해줌
 */
.uploadResult ul{
	display: flex;
	flex-flow: row;
	justify-content: center;
	align-items: center;
}
/**
서로 간격 떨어뜨리기
*/
.uploadResult ul li{
	list-style: none;
	padding: 10px;
}
.uploadResult ul li img{ /* 리스트 앞에 동그라미 대신 이미지 사용하기위해 추가 -> 지금 코드는 이미지 사용 안함 */
	width: 20px
}

/* 큰이미지를 둘러싼 div */
.bigPictureWrapper{
/* div를 원하는 위치에 놓을 수 있다.  (top, left를 설정)*/
	position: absolute;
	/*처음에는 안보이게.*/
	display: none;
	justify-content: center;
/* 	글자 위치 */
	align-items: center;
	top: 0%;
	width: 100%;
	height: 100;
	background-color: gray;
	/*위로 올리는 순서 - 가장앞으로*/
	z-index: 100;
	/*색상 ,투명도 설정*/
	background: rgba(255,255,255,0.5);
	
}
/* 큰이미지가 들어있는 div */
.bigPicture{
	position: relative;
	display: flex;
/* 	안에있는 이미지가 센터로  */
	justify-content: center;
	align-items: center;
}

/* 큰 이미지 css */
.bigPicture img{
	width: 600px;
}
/*삭제 x의   css*/
.deleteX:hover{
	color: red;
	cursor: pointer;
	
}
</style>

<!-- script태그의 기본 언어는 javascript이므로 생략 가능하다 -->
<script type="text/javascript">
$(function(){
	
	var formObj = $("form[role='form']");

	//submit 이벤트가 나올경우
	$("button[type='submit']").on("click", function(e){
		// 서브밋 이벤트 무시 - 진행은 계속 시킨다.
		e.preventDefault();

		console.log("submit 클릭");

		var str = "";
		//올라간 파일정보 가져오기 - each 문으로 반복문
		$(".uploadResult ul li").each(function(i, obj){ //li 태그 foreach -> li tag 안에 data-filename = 'data' 형식으로 있다
			var jobj = $(obj); 

// 			console.dir(jobj);
			console.log(jobj);
			console.log(jobj.data("filename")); // li안에 data-filename 가져온다

			//컨트롤러에서 name= vo 의 변수에 맞춰준다 attachList[넘어가는 리스트의 인덱스 번호] - 라고 이름에 정의해줘야함, jobj 는 data 이름 선언한대로 
			//컨트롤러에서는 BoardVO board 라고 선언만해주면된다
			str += "<input type='hidden' name='attachList["+i+"].fileName' value='"+jobj.data("filename")+"'>";
			str += "<input type='hidden' name='attachList["+i+"].uuid' value='"+jobj.data("uuid")+"'>";
			str += "<input type='hidden' name='attachList["+i+"].uploadPath' value='"+jobj.data("path")+"'>";
			str += "<input type='hidden' name='attachList["+i+"].fileType' value='"+jobj.data("type")+"'>";
			});//foreach 문의 끝 - 추가한 input 태그 작성

			//폼에 추가하고 바로 전송한다, 작성된 input 태그를 추가하고 서버에 전송한다
			//submit 이벤트 발생하면 맨위로 올라가 다시 실행된다 - 윗부분 수정
			formObj.append(str).submit();
			
		
		});

	//정규표현식 슬레시를 붙여 바로 쓸수있다
	// 	var regex = /~~/ 로도 가능         . = 모든 문자 , * = 1개이상의 문자개수, \. 특수문자 그대로 사용, $ = 끝을 의미한다, i = 대소문자 구분 안한다
	//이미지 파일 : jpg, JPG, Jpg .... 만다~ 
	// 이미지인 경우 - 섬네일 파일을 따로 만든다. - 이미지의 판단과 섬네일 만드는 작업은 JAVA(Controller) 에서 한다
	var regex = new RegExp("(.*?)\.(exe|sh|zip|alz)$","i"); // 직접 생성가능하다 객체생성한것임,올릴수 없는 확장자 검사
	var maxSize = 5242800; //5MB 까지 가능하다(한개 파일당 용량제한), 서버에서는 20MB 로 제한되어있음


	//파일에 대한 데이터 유효성 함수
	function checkExtension(fileName, fileSize){
		
	//  		파일 사이즈 검사
		if (fileSize > maxSize){
				alert("파일 사이즈 초과")
			}
		//파일의 종류 검사, text() 메서드를 사용한다- 정규표현식 객체의 함수다
		if (regex.test(fileName)){
				alert("해당 종류의 파일은 업로드할 수 없습니다.");
				return false;
			}

		return true;
	}


	//파일 올리기 버튼 이벤트 -> 파일 올리기버튼 대신 
	//-> 파일 선택이 되면 값이 변하는 change 이벤트 처리
// 	$("#uploadBtn").on("click",function(e){
	$("input[type='file']").on("change",function(e){
		console.log("파일 첨부가 되었다.");
		//return false; 들어오는지 확인
		//jquery 기능중 
		//form에 첨부되는 데이터를 가져오기위해 가상의 form 태그를 만드는것과 같다
		var formData = new FormData();
		console.log(formData);

		//input태그에서 name이 uploadFile인 것을 선택  -> inputFile은 여러개
		var inputFile = $("input[name='uploadFile']")
		console.log(inputFile);
		//첫번째 태그를 가져온다 -> files 로 파일들을 꺼내온다(multiple 이 달려있기때문 s붙는다)
		var files = inputFile[0].files;
		console.log(files);

		//files 로 꺼낸 데이터를 formData에 집어넣는다
		//한개씩 꺼내서 추가한다
		for(var i =0; i < files.length; i++){

			//파일 종류, 용량체크 - true 가 넘어오면 return false 시킨다
			if(!checkExtension(files[i].name, files[i].size)){
				return false;
				}
			//upload컨트롤러의 파라미터 이름 uploadFile - 즉 key(name)에 해당하는 이름이다
			formData.append("uploadFile", files[i]);
			}
		
	$.ajax({
		url : "/uploadAjaxAction",
		processData : false,//넘어가는 데이터
		contentType : false, // 받는 데이터
		data : formData, // 넘어가는 데이터 - 위에서 formData.append 시켜줬다
		type : 'post',
		dataType : 'json', //파라미터 데이터 타입
		success : function(result){ //list 가 넘어온다 AtachFileDTO 타입으로 반환된다
				console.log("업로드한 파일의 정보 - result : "+JSON.stringify(result))
				
				//업로드된 파일명 표시
				showUploadedFile(result)
				
				//input 태그 값을 없애자
// 			$(".uploadDiv >input").val("");
				//교재는 첨부파일의 div 객체 복사해 놨다가 처리가 다 끝나면 원래대로 복사해둔 것을 보이는 곳으로 다시 복원 복사한다
// 				var clonObj = $(".uploadDiv").clone();
// 				$(".uploadDiv").html(cloneObj.html());

			//인코딩 된 문자를 디코딩 시켜 한글로 변환(빈칸을 + 로 변경시켜 변환시킨다, 빈칸이 있으면 url 이 끊어지기때문에 +시켜연결시킨다)
// 			alert(decodeURI(result.replaceAll("+"," ")));
			}
		})//end of ajax
		
	}) //end of  파일 선택이 되면 값이 변하는 change 이벤트 처리 

	
	//파일명 리스트를 보여줄 ul 태그 선택
	var uploadResult = $(".uploadResult ul");
	//파일명을 보여줄 함수
	function showUploadedFile(list){
			//ul태그 안의 값 비우기 -> 계속나오게 하려면 empty 주석처리
// 			uploadResult.empty("");
			
			var str ="";
			//list 를 데이터가 있는만큼 반복 처리한다 , foreach 와 같다 = each()
			// each(function(인덱스, 변수)){} - 0부터 있는만큼 꺼낸다,
			//obj 로 list 가 꺼내지는데 vo 값 안에 image 라고 boolean 값이 저장되어있다
			
			$(list).each(function(i, obj){
				var fileCallPath = ""; // x 만들때 활용하려 전역변수로 처리

				//obj 는 JSON 데이터로 넘어왔다 - uploadAjax 에 보면 리턴타입이 보인다 해당 vo의 변수로 넣어준다 
				str += "<li data-path='"+obj.uploadPath+"' data-uuid='"+obj.uuid
				+"' data-filename='"+obj.fileName+"' data-type='"+obj.image+"' >"; 
				str += "<div>";  //삭제를 위한 x 표시 추가
				if(!obj.image){ 
					//이미지가 아닌경우 - 파일을 다운로드 할수있게 한다
					
					//다운로드 받을 파일 정보를 만든다
					fileCallPath = encodeURIComponent(
							// 원본 파일 다운받아서 s_ 아님
							obj.uploadPath + "/" + obj.uuid + "_" +obj.fileName
							);
					// 컨트롤러에 매핑된 url 은 /download?fileName -> controller 에서 파일자원 가져와 
					str += "<a href='/download?fileName="+fileCallPath+"'>";
					//
					str	+= "<span class='glyphicon glyphicon-floppy-open'></span> " 
					+ obj.fileName;

					str += "</a>";
				
					}
				else{ 
					// byte 타입의 정보를 인코딩 시켜 한글,문자로 쓸수있다(decode 로 원하는 타입으로 가져온다 -> 가져왔을때 인코딩시킨다)
					// spring에서 알아서 디코딩 시켜준다 
					//이미지 인 경우 - 불러올 파일 정보를 만든다. -> 한글처리해서 붙여주는 작업
					fileCallPath = encodeURIComponent(
							// yy/MM/dd/(uploadPath) + s_ + uuid_ + fileName
							obj.uploadPath + "/s_" + obj.uuid + "_" +obj.fileName
							);

					// url 쓸때 \ 로 구분하자  - 원본 이미지는 s_가 없다
					var originPath = obj.uploadPath + "\\" + obj.uuid + "_" +obj.fileName

					// /g 는  \\ 역슬레시 2개 나오는 패턴은 모드 / 로 바꾸자는 패턴
					//폴더구분 역슬레시는 / 로 바꿔주자 ->  java에서는 \\가 \로 자동으로 변환되어 처리된다 -> /로 변환시키자
					// 위치기반 url = / 를 사용하기 때문에 /로 바꿔주자(파일 시스템에서 \\ 를 쓴다)
					originPath = originPath.replace(new RegExp(/\\/g), "/");

					// 특수문자 " 쓰기위해 \" 라고 씀
					// javascript:showImage() -> javascript tag 바로 아래 showImage() 를 찾는다 => 구조상 해당 명령문보다 위에있아야한다
					str += "<a href=\"javascript:showImage(\'"+originPath+"\')\">";
					
					// 맨앞에 / 를 붙여 최상위에서 부터 찾자 ~
					str	+= "<img src='/display?fileName="+fileCallPath+"'>";

					str += "</a>"
						}// end of if else
						//밖에 쌍따옴표가 있어서 \' 라고 안써도 되지만 써두었다~, attachDTO 에 image 변수 꺼내온다 -> 삼항연산자 사용
				str += "<span class=\'deleteX\' data-file=\'"+fileCallPath+"\' data-type=\'"+(obj.image? "image":"file")+"\'>"; // x 태그 넣기
				str += " x"
				str += "</span>";
				str	+= "</div>";
				str	+= "</li>";
					});// end of foreach
				//ul태그에 추가
				uploadResult.append(str);
		}// end of showUploadedFile(파일명 보여주기함수)

		//파일 삭제 이벤트, JS 가 실행되기 전에 있던것 과 구분해줘야함 - 원래 있었던 태그를 찾아줘야 찾을수있다
		// 바로 클릭 이벤트 넣을수 없고 on 으로 한번 거쳐서 가야함
		//- 이벤트는 JS에 의해서 나중에 만들어진 객체 : 그래서 반드시 on 함수를 사용.
		//$(html.body 에 존재하는 객체 선택).on(이벤트, 앞에 선택된 객체 안에서 찾을 객체, 실행 함수)
		$(".uploadResult").on("click", ".deleteX", function(e){
//	 		alert("파일 삭제");
			
			var deleteX = $(this);

			//서버에 넘겨줄 데이터 수집
			var targetFile = $(this).data("file");
			var type = $(this).data("type");
			console.log(targetFile);
			console.log(type);

			$.ajax({
				url: '/deleteFile', //호출 url
				data: {fileName : targetFile, type : type}, //삭제를 위한 정보
				dataType: 'text', // 서버에서 반환해주는 데이터 형식
				type: 'post', //전달 방식
				success: function(result){ //서버의 처리가 성공하면 실행되는 함수 
					alert(result);
					//성공시 li 태그를 지워준다
					//클릭한 태그 안에 서 가장 먼저만나는 태그를 선택한다(위에서 아래로)
					//, empty - li 안에 태그를 비운다, remove - 선택된 li 자체가 사라진다
					// 알람창에 정보 안뜸 
//	 				alert($(this).prop("tagName"));
					//ajax 로 넘어오면 this 가 달라진다 - 밖에서 저장시킴
					deleteX.closest("li").remove(); 
					}
				});//$.ajax
			});// end of 파일 삭제
	
})// $(function(){}) 끝
</script>


</head>
<body>
<div class="container">

<div class="row">
	<div class="col-lg-12">
		<!-- 페이지 제목 -->
		<h1 class="page-header">게시판 글등록</h1>
	</div>
	<!-- /.col-lg-12 -->
</div>
<!-- /.row -->
<div class="row">
	<div class="col-lg-12">
		<div class="panel panel-default">
			<!-- 테이블의 소제목 -->
			<div class="panel-heading">게시판 글등록</div>
			<!-- /.panel-heading -->
			<div class="panel-body">
				<form role="form" method="post">
					<div class="form-group">
						<label>제목</label> <input class="form-control" name="title" />
					</div>
					<div class="form-group">
						<label>내용</label>
						<textarea rows="3" class="form-control" name="content"></textarea>
					</div>
					<div class="form-group">
						<label>작성자</label> <input class="form-control" name="writer" />
					</div>

					<button type="submit" class="btn btn-default">등록</button>
					<button type="reset" class="btn btn-default">새로고침</button>
				</form>


			</div>
			<!-- /.panel-body -->
		</div>
		<!-- /.panel -->
	</div>
	<!-- /.col-lg-12 게시판 정보입력의 끝 -->
	
</div>
<!-- /.row  -->
	<div class="row">
		<div class="col-lg-12">
			<div class="panel panel-default">

			  <div class="panel-heading">파일등록</div>

			  <div class="panel-body">
					
					<!--따로 등록 버튼없이 진행한다
					첨부할 파일을 선택하는 div -> 파일을 선택하면 값이 변한다 -> change 이벤트가 발생 -->
					<div class="form-group uploadDiv">
					
						<!-- 폼태그가 없다 , ajax인 경우 form이 동작되지 않는다. 필요없다-->
						<input type="file" name="uploadFile" multiple>
					
					</div>
					<!-- 올라간 파일의 정보 보이기 div.-->
					<div class="uploadResult">
						<ul>
							
						</ul>
					</div>
			  
			  </div>
			</div>
		
		</div>
	</div>
	<!-- 첨부파일의 끝 -->
	
	
</div>
</body>
</html>