package com.lastboard.board.vo;

import java.util.Date;

import lombok.Data;

@Data
public class ReplyVO {

	private long rno;
//	private long bno;
	private long no;
	private String reply;
	private String replyer;
	private Date replyDate;
	private Date updateDate;
}
