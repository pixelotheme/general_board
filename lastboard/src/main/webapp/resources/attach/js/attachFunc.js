/**
 * 첨부파일에대한 변수와 함수를 선언
 */
console.log("----------------------------");
console.log("attachFunc.js loading ....");

//전역변수로 사용할 변수 선언
var uploadResult = "";//파일명 리스트를 보여줄 ul 태그 선택
//	var bno = ''; // 첨부파일 을 가져올 글번호
	var no = ''; // 첨부파일 을 가져올 글번호
	
	// ------------첨부파일 보여주는 함수 선언 ()--------------------
	
	//파일명을 보여줄 함수
	function showUploadedFile(list){
			//ul태그 안의 값 비우기 -> 계속나오게 하려면 empty 주석처리
			//uploadResult.empty("");
			
			var str ="";
			//list 를 데이터가 있는만큼 반복 처리한다 , foreach 와 같다 = each()
			// each(function(인덱스, 변수)){} - 0부터 있는만큼 꺼낸다,
			//obj 로 list 가 꺼내지는데 vo 값 안에 image 라고 boolean 값이 저장되어있다
			
			$(list).each(function(i, obj){
				var fileCallPath = ""; // x 만들때 활용하려 전역변수로 처리
				
				//str += "<li>"; 
				//obj 는 JSON 데이터로 넘어왔다 - uploadAjax 에 보면 리턴타입이 보인다 해당 vo의 변수로 넣어준다 
				str += "<li data-path='"+obj.uploadPath+"' data-uuid='"+obj.uuid
				+"' data-filename='"+obj.fileName+"' data-type='"+obj.fileType+"' >"; 
	
				str += "<div>";  //삭제를 위한 x 표시 추가
				//if(!obj.image){ 
				//넘어올때 fileType으로 넘어온다
				if(!obj.fileType){ 
					//이미지가 아닌경우 - 파일을 다운로드 할수있게 한다
					
					//다운로드 받을 파일 정보를 만든다
					fileCallPath = encodeURIComponent(
							// 원본 파일 다운받아서 s_ 아님
							obj.uploadPath + "/" + obj.uuid + "_" +obj.fileName
							);
					// 컨트롤러에 매핑된 url 은 /download?fileName -> controller 에서 파일자원 가져와 
					//str += "<a href='/download?fileName="+fileCallPath+"'>"; - li 태그로 클릭하도록 한다
					//str	+= "<span class='glyphicon glyphicon-floppy-open' ></span> " 
					str	+="<span>" + obj.fileName + "</span><br>"
					+  "<span class='glyphicon glyphicon-floppy-open' style='font-size:50px;'></span> " ;
	
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
					//str += "<a href=\"javascript:showImage(\'"+originPath+"\')\">";
					
					// 맨앞에 / 를 붙여 최상위에서 부터 찾자 ~
					str	+= "<img src='/display?fileName="+fileCallPath+"'>";
	
					str += "</a>"
						}// end of if else
						//밖에 쌍따옴표가 있어서 \' 라고 안써도 되지만 써두었다~, attachDTO 에 image 변수 꺼내온다 -> 삼항연산자 사용
				//str += "<span class=\'deleteX\' data-file=\'"+fileCallPath+"\' data-type=\'"+(obj.image? "image":"file")+"\'>"; // x 태그 넣기
				//str += " x"
				//str += "</span>"; 삭제처리는 수정에서
				str	+= "</div>";
				str	+= "</li>";
					});// end of foreach
				//ul태그에 추가
				uploadResult.append(str);
		}// end of showUploadedFile
	
		// ------------첨부파일 보여주는 함수 선언 끝 --------- 
			

	//-----------------------큰이미지를 보여주는 (원래 이미지 파일) 함수 -------------------
	//큰이미지를 보여주는 (원래 이미지 파일) 함수 -> javascript:showImage() 로 작성하면 동작되도록 script tag 아래 둔다
	// $(function(){}) 보다 먼저 생성된다
	function showImage(fileCallPath){
		//동작 하는지 확인
		alert(fileCallPath)
		//css 값을 변경 (요소,값)
		
		$(".bigPictureWrapper").css("display","flex").show();
	
		//text(),html()
		$(".bigPicture")
		// 태그를 그대로 넣어서 
	// 	.html("<img src='/display?fileName="+encodeURI(fileCallPath)+"'>")
		.html("<img src='/display?fileName="+fileCallPath+"'>")
		//animate - 1초(1000)동안에 0~100% 크기로 움직인다 - 600px 까지 잡아놨다 
		.animate({width:'100%', height:'100%'},1000); // 1초동안에 보여진다
		;
		
		}
	
	//-----------------------큰이미지를 보여주는 (원래 이미지 파일) 함수 끝 -------------------