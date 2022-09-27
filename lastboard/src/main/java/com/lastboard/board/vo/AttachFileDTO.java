package com.lastboard.board.vo;

import lombok.Data;

/**
 * 
 * <strong>AttachFileDTO</strong><br>
 * <p>
 * 첨부파일 데이터 한개를 저장하기위한 DTO<br>
 * <ul>
 * 	<li>fileName -- uuid 가 포함되지 않은 순수 파일명</li>
 * 	<li>uploadPath-- 기본폴더를 제외한 날짜폴더만 저장</li>
 * 	<li>uuid</li>
 * 	<li>image</li>
 * <ul>
 * 
 * </p>
 *
 */
@Data
public class AttachFileDTO {

	private String fileName;//순수 파일명 - uuid_가 포함되어있다
	private String uploadPath; // 저장 위치 -- yyyy/MM/dd 
	private String uuid; // uuid 문자열 ->uuid_를 제거하면 파일명이 나온다
	private boolean image; //이미지가 아니면 a 태그로 다운로드도 가능하다
	
}
