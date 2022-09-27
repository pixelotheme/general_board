package com.lastboard.board.service;

import java.util.List;

import com.lastboard.board.util.PageObjectCustom;
import com.lastboard.board.vo.ReplyVO;


public interface ReplyService {

	//댓글 리스트
	public List<ReplyVO> getList(PageObjectCustom pageObject, Long no);
	//pageObject 사용으로바꿈
//	public ReplyPageDTO getList(Criteria cri, Long bno);
	
	
	//댓글 등록 
	public int register(ReplyVO vo);

	public int modify(ReplyVO vo);
	
	//댓글 삭제
	public int remove(long rno);
}
