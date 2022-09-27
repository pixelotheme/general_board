<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
  			<!-- row 시작 댓글 -->
  			<div class="row">
  				<div class="col-lg-12">
  					<!-- /.panel -->
  					  <div class="panel panel-default">
					    <div class="panel-heading">
					    <i class="fa fa-comments fa-fw"></i>Reply
					    <button id="addReplyBtn" class="btn btn-primary btn-xs pull-right">New Reply</button>
					    </div>
					    <!-- 댓글 부분 -->
					    <div class="panel-body">
					    	<ul class="chat">
					    	<!-- start reply 
					    	-ajax로 데이터를 가져와서 chat에 empty()를 이용해서 비운다음 append로 추가할예정 -->

					    	</ul>
					    	<!-- /.chat --> 
					    </div>
					    <!-- /. panel-body -->
					    
					    <!-- panel-footer 생성 페이지 처리 -->
					    <div class="panel-footer" id="footer_pagination">
					    
					    </div>
					    <!-- /. end of panel-footer : 페이지 네이션을 작성해서 넣는다(JS)
					    JS 로  java 에서 가져온 데이터를 조합해서 페이징 한다 -->
					    
					  </div>
  					<!-- /. panel -->
  				</div>
  				<!-- /.col -->
  			</div>
  			<!--  /.row 댓글 panel -->
  			
  			<!-- 댓글 등록 수정 삭제를 위한 모달 창 만들기 : 맨 처음에는 안보이게 한다 -->
  			<!-- Modal -->
			<div id="myModal" class="modal fade" role="dialog">
			  <div class="modal-dialog">
			
			    <!-- Modal content-->
			    <div class="modal-content">
			      <div class="modal-header">
			        <button type="button" class="close" data-dismiss="modal">&times;</button>
			        <h4 class="modal-title">REPLY MODAL</h4>
			      </div>
			      <div class="modal-body">
				      <!-- 댓글 작성 -->
				      <div class="form-group">
				      	<label>Reply</label>
				      	<textarea rows="3" name="reply" class="form-control" id="modalReply"></textarea>
				      </div>
				      <div class="form-group">
				      	<label>Replyer</label>
				      	<input name="replyer" class="form-control" id="modalReplyer">
				      </div>
			      </div>
			      <div class="modal-footer">
			      <button class="btn btn-primary" id="modalUpdateBtn">Update</button>
			      <button class="btn btn-danger" id="modalDeleteBtn">Delete</button>
			      <button class="btn btn-primary" id="modalRegisterBtn">Register</button>
			        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
			      </div>
			    </div>
			
			  </div>
			</div><!-- 댓글 처리를 위한 모달의 끝 -->