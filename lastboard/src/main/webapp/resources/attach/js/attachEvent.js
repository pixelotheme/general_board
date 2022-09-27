/**
 * 첨부파일에대한 이벤트 선언, 모든 로딩이 끝나고 이벤트가 실행되야한다
 */
$(function(){
		console.log("---------------------------");
		console.log("attachEvent.JS 실행");
		
		//전역변수 값 세팅
		uploadResult = $(".uploadResult ul");		//파일명 리스트를 보여줄 ul 태그 선택
		
	
	//////-----------------첨부파일 스크립트-------------------------/////////
	// 		선언 하자마자 바로 불러온다 - 익명함수 - 한번쓰고 버린다
	(function(){
	//첨부파일데이터 불러오는 Ajax 처리함수 - 
	//string 으로 저장
//	bno =  $("#bno").data("bno");
	no =  $("#no").data("no");

	//Ajax 함수 (url, 넘겨주는data, 성공시 실행 함수(가져오는데이터){처리문})
//	$.getJSON("/board/getAttachList", {bno : bno}, function(arr){
	$.getJSON("/board/getAttachList", {no : no}, function(arr){
		console.log(arr); // 정보 가져오고 콘솔창에 표시
		//ul 태그 안에 넣을 li 태그를 작성해서 ul태그안에 넣는다 
		showUploadedFile(arr);
		
		});
	})(); // - () 실행하자, 선언 하자마자 바로 불러온 함수의 끝	



//--------------------------------------사진클릭스 큰화면 보이기처리-------------------
	//큰이미지를 둘러 싸고 있는 div를 클릭하면 안보이게 하는 이벤트 처리
	$(".bigPictureWrapper").on("click",function(e){
		$(".bigPicture").animate({width:"0%", height:"0%"}, 1000);
		//특정 시간이 되면 끝낸다 -> hide가 되고 1초가 지나면 끝낸다
		setTimeout(
				() => {
					console.log("자동실행")
					$(this).hide()
					},
				 1000);
		});//큰이미지를 둘러 싸고 있는 div를 클릭하면 안보이게 하는 이벤트 처리 끝
			//--------------------------------------사진클릭스 큰화면 보이기처리끝-------------------

			
//--------------------------------------부파일을 둘러싸고있는 li 태그를 클릭하면 처리되는 이벤트-------------------
		//첨부파일을 둘러싸고있는 li 태그를 클릭하면 처리되는 이벤트
		$(".uploadResult").on("click", "li", function(e){

// 			alert("첨부파일 클릭");
			var liObj = $(this);
			//작업할 파일명 세팅(서버에 올라간 파일명 그대로)
			
			var prePath = liObj.data("path")+"/"
			+liObj.data("uuid")+"_"+liObj.data("filename");
// 			alert(prePath);

// 			alert(prePath.replace(new RegExp(/\\/g),"/"))
			
// 			var path = encodeURIComponent(liObj.data("path")+"/"
// 					+liObj.data("uuid")+"_"+liObj.data("filename"));
			var path = encodeURIComponent(prePath);

			//이미지인 경우
			if(liObj.data("type")){
				// \\ -> / 로 바꾼다
				showImage(path.replace(new RegExp(/\\/g),"/"));
				
				}else{
					//이미지가 아닌경우 download
					//alert("이미지가 아닙니다");
					location = "/download?fileName="+path;
					//self 는 지금 열려있는 창 을 의미한다 - iframe 을 만들경우 필요하다
// 					self.location = "/download?fileName="+path;
				}
			
			});
		//첨부파일을 둘러싸고있는 li 태그를 클릭하면 처리되는 이벤트 끝
	
})