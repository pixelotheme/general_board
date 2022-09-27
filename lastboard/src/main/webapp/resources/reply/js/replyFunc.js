/**
 * 댓글에서 사용할 변수와 함수를 선언해 놓는다
* 전역변수, 일반 함수 선언 미리해둬야 이벤트 처리떄 불러와서 사용할수있다
 */
console.log("----------------------------");
console.log("replyFunc.js loading ....");
var noValue = ""; // 게시판 보기에서 글번호를 찾아서 세팅
var replyUL = "";// 댓글 목록을 표시한 객체 선택

	//호출 시켜야 실행된다, 댓글리스트 처리함수--=======================================
	function showList(page){

		console.log("show list - pgae : "+page);
		console.log("show list - noValue : "+noValue);
		//param 서버에 전달되는 데이터, callback 서버에 갔다오면 처리되는 함수- 데이터 가지고 온뒤 처리, error 에러가 날때 처리되는 함수
		//페이징 추가
		replyService.getList(
				{no:noValue,page: page||1},
	
				//list 가 넘어와서 단순이 이름 지정한것, replyCnt 대신 pageObject가 넘어온다
				function(pageObject, list){

					
					
					//넘어오는 데이터 확인하기
					console.log("pageObject : "+JSON.stringify(pageObject));
					console.log("list : " + JSON.stringify(list));

					//선택한 페이지 가 -1 로 들어온다면 해당 게시글의 댓글 수 / 10.0 -> 올림한다
					// 즉 -1 이면 마지막 페이지로 이동시키자 -> 교재에서는 정렬을 asc 으로 해서 내가 방금 댓글써도 마지막페이지 로 가야 댓글이보인다 
					// 새로운 글을 추가할경우 -1 값으로 getList(-1) 호출해버린다 
// 					if(page == -1){

// 						//pageNum - 현재페이지 저장하는 변수 / 전체 댓글 개수/10.0 하면 마지막 페이지가 나온다
// 						pageNum = Math.ceil(replyCnt/10.0);
// 						showList(pageNum);
// 						return
// 						}

					// list 가 넘어오지 않았거나 데이터가 없는경우 처리하지 않게 한다.
					if(list == null || list.length == 0){
						//replyUL -> 비워놔라- 댓글 부분 모두 비운다 - UL태그는 놔두고
						replyUL.html("");
						return;
						}
					//데이터가 있는 경우의 처리
					var str = "";
					// || 연산자는 좌측 우측변의 true인 값을 사용한다 -> length 가 null 이면 0으로 한다, 초기화가 2개 i,len
					for(var i = 0, len = list.length || 0; i < len; i++){
						//clearfix 줄칸이 겹치는것을 막는것
						//rno를 li 태그에 숨겨 놨다(data-rno) -> 찾아 올때는 li 태그 선택후 .data("rno")
						//-> 다시 세팅할떄 .data("rno", 1) = 1이 세팅된다
						str += "<li class='left clearfix' data-rno='" + list[i].rno + "'>";
						str += "	<div>";
						str += "		<div class='header'>";
						str += "			<strong class='primary-font'>"+list[i].replyer + "</strong>";
						str += "			<small class='pull-right text-muted'>"+replyService.displayTime(list[i].replyDate) + "</small>";
						str += "			<p>" + list[i].reply + "</p>";
						str += "		</div>";
						str += "	</div>";
						str += "</li>"
						
						}//for문 끝 - str완성 - 데이터가 있는만큼 li tag가 생긴다
						// 태그를 추가해준다 - 원래있던 것은 사라지고 덮어 쓰기 한다
						replyUL.html(str);

						//callback 처리에 페이징 호출
//						showReplyPage(replyCnt)
						showReplyPage(pageObject)

						
					}//function(list) 의 끝
				); //replyService.getList() 의 끝
	}// showList() 의 끝

	//댓글 페이지네이션을 위한 변수
	var pageNum = 1;
	var replyPageFooter = "";	//입력은 eventㅇ에서
	
	
		// 댓글의 페이지네이션을 위한 처리 - pageDTO 에 있는 계산을 여기서함===============================================
	function showReplyPage(pageObject){
		// 보여줄 페이지네이션 정보 계산
//			pageObject로 계산해서 나왔다


			//******** 페이지 표시하기 ***************
			
			var str = "<ul class='pagination pull-right'>";

			if(pageObject.startPage > 1){
				// 이전 페이지로 이동 활성화
				str += "<li class='page-item'><a class='page-link' href='"+(pageObject.startPage -1)+"'>Previous</a></li>";
				}
			for(var i = pageObject.startPage ; i <= pageObject.endPage; i++){
				//해당 번호와 현재 페이지가 같다면 active 
				var active = pageObject.page == i? "active":"";
				str += "<li class='page-item "+active+"'><a class='page-link' href='"+i+"'>"+i+"</a></li>";
				}
			if(pageObject.endPage < pageObject.totalPage){
				// 다음 페이지로 이동
				str += "<li class='page-item'><a class='page-link' href='"+(pageObject.endPage + 1)+"'>next</a></li>";
				}
			str += "</ul>"

			console.log(str);
			//만든 페이징 태그 넣어준다
			replyPageFooter.html(str);
				
		}// 댓글의 페이지네이션을 위한 처리 끝======================
		