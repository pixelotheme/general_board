package com.lastboard.board.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.lastboard.board.vo.BoardAttachVO;


public interface BoardAttachMapper {

	
	//리스트 찾기 글 보기할때 이미지의 목록을 받아와서 처리한다
//	public List<BoardAttachVO> findByBno(long no);
	public List<BoardAttachVO> findByNo(long no);
	
	//void 로 리턴해도된다 - 안올라가면 바로 에러나기때문
	//게시판 글 등록시 첨부파일의 정보를 넣는다  -> 글등록후 번호를 받아와서 넣어줘야한다
	public int insert(BoardAttachVO vo);

	//게시판의 글 삭제가 될 때 글과 함께 삭제가 될 수 있도록 한다. - 이미지 수정할때 원래 db 는 지운다
	public int delete(String uuid);
	
	public int deleteAll(long no);

	
	//스케줄러에 의해서 DB에 없는 파일 들을 지우기 위한 데이터 - 어제 Ajax로 추가된 파일목록만 가져온다
	public List<BoardAttachVO> getOldFiles();
	
}
