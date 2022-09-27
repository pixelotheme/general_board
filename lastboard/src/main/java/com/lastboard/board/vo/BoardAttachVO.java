package com.lastboard.board.vo;

import lombok.Data;

@Data
public class BoardAttachVO {

	private String uuid;
	private String uploadPath;
	private String fileName;
	private boolean fileType; //image 면 true, no image : false
	
	private Long no;
}
