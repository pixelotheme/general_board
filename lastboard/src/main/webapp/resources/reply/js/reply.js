/**
 * 게시판 댓글 관련 JS 
 */
 
 //js가 동작된다.
 console.log("Reply Module.........");
 
 //replyService JS 객체 생성 -> JSON 형태 { } 로 만듦
 //마지막 () 는 실행한다는 메서드 , 변수를 불러오면 function이 불러와진다
 //->변수 = (function- 실행할 처리문)(실행해라);    
 //-> return 해서 나온 값이 JSON 데이터 다 
 //결과를 도출한 값을 변수에 저장한 상태이다 - return한 값만 변수에 들어간다
 var replyService = (function(){
 
	 //이름이 있는 함수 - > 호출하기 전까지는 실행되지 않는다
	 //param 서버에 전달되는 데이터, callback 서버에 갔다오면 처리되는 함수- 데이터 가지고 온뒤 처리, error 에러가 날때 처리되는 함수
	 //callback,error 함수는 get.jsp 의 js 부분에서 작성해서 넣어준다
	 function getList(param, callback, error){
	 	
	 	//alert("댓글 가져오기 처리");
	 	//param.bno 의  .앞에 있는것은 object(객체 이다) -> json data 이다
	 	//get.jsp 에서 함수 불러올때 파라미터로 값 넘겨줘야한다 - json 데이터로 넘겨준다
//	 	var bno = param.bno;
	 	var no = param.no;
	 	//page 가 없으면 에러가 안나고 1로  설정된다
	 	var page = param.page || 1;
	 	
	 	//ajax를 통해서 json 데이터로 댓글 리스트를 가져오는 처리
	 	//$.getJSON(url,[data,]function~~) - data 생략가능 ->
	 	//reply컨트롤러에 @PathVariable 설정을 해놔서 ajax url 만으로 데이터가 넘어간다
﻿
	 	$.getJSON(
	 	
	 	//url
//	 		"/replies/pages/"+no+"/"+page+".json",
	 		"/replies/pages/"+no+"/"+page+".json",
	 	//data- 성공적으로 데이터가 왔다면 data 에 들어간다
	 	//function(서버에서 전달하는 데이터) - 컨트롤러에서 댓글 리스트 뽑은 데이터가 나온다
	 	//현재 댓글 데이터 없음 - JSON 형태로 나온다
	 	// function(data){~~} 데이터 가져오기 성공했을때 실행한다 -> 실패하면 .fail 이 실행됨
	 		//List<ReplyVO> 가 넘어온다 == data == JSON 형식으로 온다 
	 		// -> foreach로 돌려서 꺼내준다 (callback 함수를 짜준다)
	 		function(data){
	 			//오브젝트로 안보고 스트링으로 만들어준다 - 바로 출력 가능 
	 			//- 이전에는 Object object 라고 나왔었음
//	 			alert(JSON.stringify(data));
	 			
	 			//callback함수가 있으면 callback 함수를 실행하자. get.jsp 의 js 부분에서 작성해서 넣어준다
	 			if(callback){ // data{replyCnt, list} 형식으로 넘어온다 - 구분해서 써줘야한다
	 			//callback 은 이미 함수 = function 라서 () 붙여주면 실행한다
	 			//위에서 넣을지말지 결정하는것
			//﻿replyPageDTO -> [0] = replycnt, [1] = list 라서 ajax data 도 변경시켜줘야함
	 			 callback(data.pageObject, data.list);
	 			
	 			} //end of callback
	 			//callback 함수가 없으면 처리되는 처리문
	 			else{
	 			
	 			//data 자체는 있다고 나옴 배열 자체는 있다, 길이로 보면 없다
	 			if(data.length > 0){ alert("데이터가 있습니다.");}
	 			else{alert("데이터가 없습니다.");}
	 			
	 			
	 			}
	 			
	 		}
	 		//데이터 가져오기 실패시 처리되는 함수
	 	).fail(function(xhr, status, error){
	 		//에러가 있으면 실행된다
	 		if(error)
	 			error();
	 			
	 	});//getJSON() 의 끝
	 	
 	}//getList 함수의 끝
	 
	 function displayTime(timeValue){
	 	//오늘 날짜 객체 생성 - 24 시간이 지났는지 알나내기 위해서 필요
	 	var today = new Date();
	 	
	 	//댓글 작성 시간과의 차이
	 	//날짜객체.getTime() -> long 타입의 날짜 데이터가 나온다, timeValue 는 이미 숫자로 넘어온다
	 	var gap = today.getTime() - timeValue;
	 	
	 	// 댓글 작성 날짜에 대한 형식을 만들려 작성 날짜를 날짜 객체로 만들어야 한다.
	 	var dateObj = new Date(timeValue);
	 	var str = "";
	 	
	 	//24시간이 안지난경우 - 시분초를 출력한다 - ms 단위로 변환한다
	 	if(gap < (24 * 60 * 60 *1000)){
	 		//주어진 날짜의 현지 시간 기준 시,분,초를 반환합니다.
	 		var hh = dateObj.getHours(); 
	 		var mi = dateObj.getMinutes();
	 		var ss = dateObj.getSeconds();
	 		
	 		// join(구분) 배열을 이어주는 함수. 배열사이에 구분문자를 넣어 줘서 이어준다.
	 		return [
	 		(hh > 9 ? '':'0')+hh, ':', 
	 		(mi > 9 ? '':'0') + mi, ':',
	 		(ss > 9 ? '':'0') + ss
	 		].join('');
	 	}
	 	//24시간이 지난경우 - 날짜를 출력한다 - ms 단위로 변환한다
	 	else{
	 		//주어진 날짜의 현지 시간 기준 연,월,일 을 반환합니다.
	 		var yy = dateObj.getFullYear(); 
	 		//월 의 날짜 객체는 0 ~ 11 까지만 운용한다 . 우리가 사용하는 월은 +1 처리해야 한다
	 		var mm = dateObj.getMonth() +1;// getMonth() is zero-based
	 		var dd = dateObj.getDate();
	 		
	 		// join(구분) 배열을 이어주는 함수. 배열사이에 구분문자를 넣어 줘서 이어준다.
	 		
	 		return [
	 		yy, '-', 
	 		(mm > 9 ? '':'0') + mi, '-',
	 		(dd > 9 ? '':'0') + dd
	 		].join('');
	 	}
	 	
	 } //end of diplayTime 
		 
	//댓글 등록
	//add(JSON데이터, 등록 성공시 처리 함수(), 등록 오류 처리함수)
	function add(reply, callback, error){
		
		console.log("add reply ....... reply : " + JSON.stringify(reply));
		
		//ajax를 이용해서 서버에 데이터 전달(RUL - /reply/new)
		//중괄호 써줘야함
		$.ajax(
			{
				type : "post", //전달방식
//				url : "/replies/new", //url
				url : "/replies/new", //url
				data : JSON.stringify(reply), // 브라우저에서 서버로 전달되는 데이터
				contentType : "application/json; charset=utf-8", // 전달 데이터의 형식
				//result = 성공하면 리턴되어 받아오는 데이터 
				success : function(result, status, xhr){ //서버 처리가 성공 후 브라우저에서 실행되는 함수
							if(callback){
								callback(result);
							}
				},
				error : function(xhr, status, er){ // 서버 처리가 실패 후 브라우저에서 실행되는 함수
					//에러가 생기면 실행해라 add 함수의 error 메서드를 실행
							if(error) {
								error(er);
							}
							else{
								//현재 error 함수처리 안넘겨서 직접 찍어본다~
								console.log(xhr);
								console.log(status);
								console.log(er);
							}		
				}
				
			}//ajax 중괄호 끝
		); //ajax 끝
		
		
	}// 댓글 등록의 끝
	
	//댓글 수정 함수
	function update(reply, callback, error){
		
//		alert("update reply(json) : " + JSON.stringify(reply)) 
		
		//ajax 처리
		$.ajax(
			{
				type : "put", //또는 path 가능 controller랑 맞춰주면 된다 - 현재는 두개다 선언되어있어서 아무거나 가능
//				url : "/replies/"+ reply.rno,
				url : "/replies/"+ reply.rno,
				data : JSON.stringify(reply),
				contentType : "application/json; charset=utf-8",
				//문자열로 result에 넘어온다
				success : function(result, status, xhr){
					if(callback) {
					callback(result);
					}
				},
				//오류나면 결과값이없다
				error : function(xhr, status, er){
					if(error) {
						error(er)
					}else{
						console.log(xhr);
						console.log(status);
						console.log(er);
					}
				}
				
			}
		)//end of ajax
		
	}//end of update
	
	//댓글 삭제 함수
	function remove(rno, callback, error){
		
		alert("remove rno : " + rno) 
		
		//ajax 처리
		
		$.ajax(
			{
				type : "delete",//또는 path 가능 controller랑 맞춰주면 된다 - deletemapping 선언되어있음
//				url : "/replies/"+ rno,
				url : "/replies/"+ rno,
//				data : JSON.stringify(reply), - 넘어가는 데이터가 없다 url 로 넘길뿐이다
//				contentType : "application/json; charset=utf-8",
				//문자열로 result에 넘어온다
				success : function(result, status, xhr){
					if(callback) {
					callback(result);
					}
				},
				//오류나면 결과값이없다
				error : function(xhr, status, er){
					if(error) {
						error(er)
					}else{
						console.log(xhr);
						console.log(status);
						console.log(er);
					}
				}
				
			}
		)//end of ajax
		
	}//end of update

		 return {
		 //getList 의 이름으로 실행된 getList값 - {getList : getList}
	//해당 메서드를 실행한 내용을 넘겨준다
		 	getList : getList,
			add : add,
			update : update,
			remove : remove,
		 	displayTime : displayTime
		 };
	 
 })();
 