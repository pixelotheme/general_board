/**
 * 댓글의 이벤트 처리

 */
//하단 부분의 HTML 객체를 선택해야 하므로 HTML 문서 로딩후 실행되도록 형태를 정의
$(function(){
		// reply.js 파일에 서만든 변수replyService를 사용해보자
	// 불러오면 바로 실행되는 이유는 .js 에서 () 를 마지막에 붙여줬기때문이다
// 	console.log(replyService);
// 	console.log(replyService.name);
	console.log("---------------------------");
	console.log("replyEvent.JS 실행");
	
	
	//미리 선언된 전역변수에 대한 값을 선언해줘야한다
	noValue = $("#no").data("no"); // 게시판 보기에서 글번호를 찾아서 세팅
	replyUL = $(".chat");// 댓글 목록을 표시한 객체 선택	
	
	console.log("replyEvent - no :" + noValue);
		//댓글 페이지네이션을 위한 변수
	replyPageFooter = $("#footer_pagination");
	
	
		//자동으로 한번은 댓글이 실행되게 한다. 페이지 : 1
	//페이징 수정 - 선택한 숫가 넘어가도록 해야한다
	showList(1);

	//===============여러 이벤트 가져오기 -======================================
	//댓글 등록을 위한 모달 창을 보이게 하는 버튼 이벤트
	$("#addReplyBtn").on("click", function(e){

//		alert("댓글 등록 창 열기 클릭");
		//모달창의 데이터를 지운다 - 취소로 나갔다 들어왔을떄 아무것도 없이 해줘야함
		$("#modalReply, #modalReplyer").val("");

		//필요한 입력항목은 보이게, 필요없는 항목은 보이지 않게
		$("#modalUpdateBtn, #modalDeleteBtn").hide();
		
		$("#modalRegisterBtn").show();		
		
		//모달창을 보이게 한다
		$("#myModal").modal("show");
		});

	//모달창의 등록버튼 이벤트 -> 댓글 등록처리 진행 -======================================
	$("#modalRegisterBtn").on("click", function(){
//		alert(noValue);
		//데이터 수집	
			var reply = {
				reply : $("#modalReply").val(),
				replyer : $("#modalReplyer").val(),
				no : noValue	
			} 

		//object 로 넘김
		replyService.add(
				reply,
				//등록 성공시 실행 함수
				function(result){
					//callback 함수- reply 컨트롤러 리턴값인 register success 가 나온다
					alert(result);

					//댓글 리스트를 새롭게 뿌려준다 (방금 등록한 댓글 보이게)
					showList(1);
					}
				);//end add
		
			//댓글 등록 AJAX 함수로 데이터 전달해서 댓글 등록 진행
			//replyService.add({reply : "JS TEST", replyer : "tester", bno : bnoValue});

			//데이터를 넘기고 모달을 닫는다
			$("#myModal").modal("hide");
		});//모달창의 등록버튼 이벤트 -> 댓글 등록처리 진행 끝
		
		
		
	//댓글을 클릭하면 수정 / 삭제하는 이벤트 - 모달 창을 연다, 위에서 chat 클래스 받아둠======================================
	$(replyUL).on("click", "li", function(){

		//rno를 찾아 와서 modal에 세팅하자. (data-* 활용) - 숨겨서 세팅
		var rno = $(this).data("rno");
// 		alert(rno);

		//수정할 데이터 세팅, text 로 받아오면 <br>태그가 사라짐
		var reply = $(this).find("p").html()
// 		alert(reply);
		$("#modalReply").val(reply.replaceAll("<br>", "\n"));

		var replyer = $(this).find("strong").text();
		$("#modalReplyer").val(replyer);

		//모달에 해당 내용 세팅 - 모달창을 열어서 수정하기 시작하니까 가져오기 편하게 미리 설정해둔다
		$("#myModal").data("rno", rno);

		//꺼내보자
// 		alert($("#myModal").data("rno"));
		
		//필요한 입력항목은 보이게, 필요없는 항목은 보이지 않게
		$("#modalRegisterBtn").hide();
		$("#modalUpdateBtn, #modalDeleteBtn").show();	

		$("#myModal").modal("show");

		
		});// 댓글을 클릭하면 수정 / 삭제하는 이벤트의 끝

	// 댓글 수정 처리 이벤트======================================
	$("#modalUpdateBtn").on("click", function(){

		//데이터 수집 - JSON 데이터로 만들어서 처리한다, 등록과 비슷
		var reply = 
			{
				reply : $("#modalReply").val(),
				replyer : $("#modalReplyer").val(),
				rno : $("#myModal").data("rno")
				};

		//모달을 먼저 닫는다
		$("#myModal").modal("hide");
		
		//파라미터 넘긴다, callback 함수 실행될떄 result 받아서 처리 시작한다 (.js 파일과 동일하게 맞춰줬다)
		replyService.update(
				reply,
				function(result){
			
					alert(result);
					//페이징 처리로 수정
					showList(pageNum);
				});
			
		})// 댓글 수정 처리 이벤트의 끝

		//댓글 삭제 처리 이벤트======================================
		$("#modalDeleteBtn").on("click", function(){
			replyService.remove(
					$("#myModal").data("rno"), 

					function(result){
						//.js 에서 result 로 받는다 --callback 함수 로 받아온다
						alert(result); //서버에서 전달하는 메세지 출력
						$("#myModal").modal("hide");
						showList(1); //1페이지로 가자~
					},
					//에러 시 작동 - error 가 넘어와 실행시킨다
					function(er){
						alert("Error.... : " + er)
						}

					);
			})// 댓글 삭제 처리 이벤트 끝


//============================================================================
		//페이지네이션 클릭시 작동, 클릭한 곳 안에 태그 안에 태그 를 찾기위한 방법 - 띄어쓰기, 부등호는 바로아래 selector다 
		//반드시 on 함수를 사용해야만 한다 - ajax 를 통해 새로 생긴 태그에는 이벤트가 주어지지 않는다 - 페이지가 html 로 변환될떄 이벤트가 부여되는데 이미 변환끝나고 들어오는것이다 
		replyPageFooter.on("click","li a", function(e){
		//a 태그 페이지 이동 무시
		e.preventDefault();
		console.log("page click");

		//클릭한 a태그의 href 속성 값을 가져온다, selector 로 선택한 태그가 대상이 된다
		var targetPageNum = $(this).attr("href");

		console.log("targetPageNum : "+ targetPageNum);
		//전역변수인 pageNum에 넣으면 다른 함수에서도 사용이 가능하다
		pageNum = targetPageNum;

		showList(pageNum);
		
		
		});//end of 댓글 페이지네이션

})