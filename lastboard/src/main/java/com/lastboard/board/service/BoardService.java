package com.lastboard.board.service;

import java.util.List;


import com.lastboard.board.util.PageObjectCustom;
import com.lastboard.board.vo.BoardAttachVO;
import com.lastboard.board.vo.BoardVO;

public interface BoardService {

	//게시판 리스트
	public List<BoardVO> list(PageObjectCustom pageObject) throws Exception;

	//게시판 글보기
	public BoardVO view(long no, int inc) throws Exception;
	public List<BoardAttachVO> getAttachList(Long no);	
	//게시판 등록
	public int write(BoardVO vo) throws Exception;
	
	//게시판 수정
	public int update(BoardVO vo) throws Exception;
	//게시판 삭제
	public int delete(long no) throws Exception;
	
	
}
