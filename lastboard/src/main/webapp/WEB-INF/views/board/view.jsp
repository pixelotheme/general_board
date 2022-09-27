<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
    <%@taglib prefix="pageNav" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시판 글보기</title>

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

<!-- 댓글 css -->
<link rel="stylesheet" href="/resources/dist/css/sb-admin-2.css">

<style type="text/css">
.list-group-item > label{
	width: 100px
	
}
</style>

<!-- 댓글을 위한 라이브러리등록  -->
<script type="text/javascript" src="/resources/reply/js/reply.js" ></script>
<!-- 전역변수, 함수선언 을 위한 선언 먼저 -->
<script type="text/javascript" src="/resources/reply/js/replyFunc.js" ></script>
<!-- 값 입력, 함수호출 -->
<script type="text/javascript" src="/resources/reply/js/replyEvent.js" ></script>

<!-- 첨부파일을 위한 라이브러리등록  -->
		<!-- 첨부파일 CSS -->
<link rel="stylesheet" href="/resources/attach/css/attachFile.css">
<!-- 전역변수, 함수선언 을 위한 선언 먼저 -->
<script type="text/javascript" src="/resources/attach/js/attachFunc.js" ></script>
<!-- 값 입력, 함수호출 -->
<script type="text/javascript" src="/resources/attach/js/attachEvent.js" ></script>


<script type="text/javascript">
$(function(){

	
	// 리스트버튼 클릭 이벤트 -> 글보기로 이동
	$("#listBtn").on("click", function(){
		// alert("글보기로 이동");
		location = "list.do?"
					+ "page=" + "${param.page}"
					+ "&perPageNum=" + "${param.perPageNum}"
					+ "&key=" + "${param.key}"
					+ "&word=" + "${param.word}";
	});

	// 글수정 버튼 이벤트
	$("#updateBtn").on("click", function(){
		// alert("글등록으로 이동");
		location = "update.do?no=${vo.no}"
		+ "&page=" + "${param.page}"
		+ "&perPageNum=" + "${param.perPageNum}"
		+ "&key=" + "${param.key}"
		+ "&word=" + "${param.word}";
	});
	
	// 삭제 버튼 이벤트
	$("#deleteBtn").on("click", function(){
		// alert("새로고침 클릭");
		if(confirm("삭제?")){
		location = "delete.do?no=${vo.no}&perPageNum=${param.perPageNum}";
			}
	});
	
});
</script>
</head>
<body>
<div class="container">
	<!-- 페이지 제목 줄 -->
	<div class="row">
		<div class="col-md-12">
		<h2>게시판 글보기</h2>
		</div>
	</div>
	<!-- /. 페이지 제목줄 끝 -->
	

	
	<!-- 데이터 표시 -->
	
	<div class="row">
		<div class="col-md-12">
			<!-- 데이터 들어가는공간 -->
			<div class="list-group">
					<div class="list-group-item">
						<label>번호</label>
						<span id="no" data-no="${vo.no }">${vo.no }</span>
					</div>
					<div class="list-group-item">
						<label>제목</label>
						<span>${vo.title }</span>
					</div>
					<div class="list-group-item">
						<label>내용</label>
						<span>
						<!-- 띄어쓰기 적용 때문에 pre를 써봄 - 원래는 java 단에서 처리했었음 -->
						<pre>${vo.content }</pre></span>
					</div>
					<div class="list-group-item">
						<label>작성자</label>
						<span>${vo.writer }</span>
					</div>
					<div class="list-group-item">
						<label>작성일</label>
						<span><fmt:formatDate value="${vo.writeDate }" pattern="yyyy-MM-dd"/></span>
					</div>
					<div class="list-group-item">
						<label>조회수</label>
						<span>${vo.hit }</span>
					</div>
			</div>			
		</div>
	</div>
	
	<!-- /.데이터 표시 끝 -->
  	<!-- 첨부파일 표시 가져오기 -->
<%@include file="../includes/attachFile.jsp" %>
	
	
	<!--  버튼 -->
	<div class="row">
		<div class="col-md-4">
			<div class="btn-group" style="padding: 20px;">
			  <button type="button" class="btn btn-default" id="updateBtn">수정</button>
			  <button type="button" class="btn btn-default" id="deleteBtn">삭제</button>
			  <button type="button" class="btn btn-default" id="listBtn">리스트</button>
			</div>
		</div>
	</div>

	<!-- 버튼 끝-->
	<%@include file="../includes/reply.jsp" %>
	
	
</div>
</body>
</html>