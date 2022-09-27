package com.lastboard.board.vo;


import java.util.Date;
import java.util.List;


import lombok.Data;

@Data
public class BoardVO {

	private long no;
	private String title;
	private String content;
	private String writer;
	private Date writeDate;
	private long hit;
	
	//댓글 개수
	private long replyCnt;
	
	//첨부파일 리스트 추가 필요
	private List<BoardAttachVO> attachList;
	
	
}
