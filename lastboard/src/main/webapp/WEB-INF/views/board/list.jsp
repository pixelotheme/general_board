<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
    <%@taglib prefix="pageNav" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시판 리스트</title>
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

<style type="text/css">
.dataRow:hover{
	background: #eee;
	cursor: pointer;
}
/*div 끼리의 구분선 할때 선택자 */
.dataRow > div:first-child{
	border-bottom: 1px dotted #ddd;
}
</style>

<script type="text/javascript">
$(function(){

	//검색 데이터 세팅
	$("#key").val("${empty(pageObject.key)?'t':pageObject.key}");
	$("#word").val("${pageObject.word}");
	$("#perPageNum").val("${pageObject.perPageNum}");
	
	// 리스트데이터 클릭 이벤트 -> 글보기로 이동
	$(".dataRow").on("click", function(){
		// alert("글보기로 이동");
		var no = $(this).find(".no").text();
		location = "view.do?no="+no + "&inc=1"
					+ "&page=" + "${pageObject.page}"
					+ "&perPageNum=" + "${pageObject.perPageNum}"
					+ "&key=" + "${pageObject.key}"
					+ "&word=" + "${pageObject.word}";
	});

	// 글등록 버튼 이벤트
	$("#writeBtn").on("click", function(){
		// alert("글등록으로 이동");
		location = "write.do?perPageNum=${pageObject.perPageNum}";
	});
	
	// 새로고침 버튼 이벤트
	$("#reflashBtn").on("click", function(){
		// alert("새로고침 클릭");
		location.reload();
	});
	
	// 한 페이지에 보여줄 데이터 개수 변경 이벤트
	$("#perPageNum").on("change", function(){
		// alert("새로고침 클릭");
		location="list.do?perPageNum=" + $(this).val();
	});
});
</script>
</head>
<body>
<div class="container">
	<!-- 페이지 제목 줄 -->
	<div class="row">
		<div class="col-md-12">
		<h2>게시판 리스트</h2>
		</div>
	</div>
	<!-- /. 페이지 제목줄 끝 -->
	
	<!-- 검색 줄 -->
	<div class="row" style="padding: 5px;">
		<!-- 검색 -->
		<div class="col-md-8">
			<form class="form-inline">
				<input type="hidden" name="perPageNum" value="${pageObject.perPageNum }">
				<div class="input-group">
				  	<select class="form-control" name="key" id="key">
				  		<option value="t">제목</option>
				  		<option value="c">내용</option>
				  		<option value="w">작성자</option>
				  		<option value="tc">제목/내용</option>
				  		<option value="tw">제목/작성자</option>
				  		<option value="cw">내용/작성자</option>
				  		<option value="tcw">모두</option>
				  	</select>
			  	</div>
				<div class="input-group">
				  <input type="text" class="form-control" placeholder="Search" name="word"
				   id="word">
				  <div class="input-group-btn">
				    <button class="btn btn-default" type="submit">
				      <i class="glyphicon glyphicon-search"></i>
				    </button>
				  </div>
			  </div>
			</form>
		</div>
		<!-- 한페이지의 데이터 개수 -->
		<div class="col-md-4 text-right">
			<form class="form-inline">
				<select id="perPageNum" name="perPageNum" class="form-control">
					<option>10</option>
					<option>15</option>
					<option>20</option>
				</select>
			</form>
		</div>
	</div>
	<!-- 검색 줄의 끝 -->
	
	<!-- 데이터 표시 -->
	
	<div class="row">
		<div class="col-md-12">
			<!-- 데이터 들어가는공간 -->
			<div class="list-group">
				<c:forEach items="${list }" var="vo">
					<div class="list-group-item dataRow">
						<div>
							<strong><span class="no">${vo.no }</span>. ${vo.title }</strong>
							<span class="badge pull-right">${vo.hit }</span>
						</div>
						<div>
							<span>${vo.writer }</span>
							<span class="pull-right">
								<fmt:formatDate value="${vo.writeDate }" pattern="yyyy-MM-dd"/>
							</span>
						</div>
					</div><!-- //데이터 출력 -->
				</c:forEach>
			</div>			
		</div>
	</div>
	
	<!-- /.데이터 표시 끝 -->
	
	<!-- 페이지 처리, 버튼 -->
	<div class="row">
		<div class="col-md-4">
			<div class="btn-group" style="padding: 20px;">
			  <button type="button" class="btn btn-default" id="writeBtn">등록</button>
			  <button type="button" class="btn btn-default" id="refreshBtn">새로고침</button>
			</div>
		</div>
		<div class="col-md-8 pull-right text-right">
			<pageNav:pageNav listURI="list.do" pageObject="${pageObject }"/>
		</div>
	</div>
	<!-- 페이지 처리 끝-->
	
	
	
</div>
</body>
</html>