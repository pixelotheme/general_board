package com.lastboard.board.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.lastboard.board.util.PageObjectCustom;
import com.lastboard.board.vo.ReplyVO;

public interface ReplyMapper {

	//bno 를 따로 받아서 와야함 - 파라미터로 전달되는값 
	//파라미터로 데이터를 2개 넘길때 param 으로 넘기면 묶어서 넘어간다
	//페이지 하려면 댓글 총 개수도 가져와야함 -> 전체 페이지 개수 구하기위함
	public List<ReplyVO> getListWithPaging(
			@Param("pageObject") PageObjectCustom pageObject,
			@Param("no") Long no
			);
	
	//해당 글번호의 댓글 전체 데이터 개수 가져오기
	public long getCountByNo(long no);
	
	
	//댓글 등록
	public int insert(ReplyVO vo);
	
	public int update(ReplyVO vo);
	
	//댓글 삭제
	public int delete(long rno);
}
