package com.lastboard.board.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.lastboard.board.mapper.BoardAttachMapper;
import com.lastboard.board.mapper.BoardMapper;
import com.lastboard.board.util.PageObjectCustom;
import com.lastboard.board.vo.BoardAttachVO;
import com.lastboard.board.vo.BoardVO;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Service
@Log4j
@Qualifier("bsi")
public class BoardServiceImpl implements BoardService{

	//mapper 이용해서 db 처리 - 자동 DI
	@Setter(onMethod_ = {@Autowired})
	private BoardMapper mapper;
	@Setter(onMethod_ = {@Autowired})
	private BoardAttachMapper attachMapper;	
	
	@Override
	public List<BoardVO> list(PageObjectCustom pageObject) throws Exception {
		// TODO Auto-generated method stub
		//전체 데이터의 개수를 가져오는 처리를 꼭 해야한다. 안하면 데이터가 안나온다
		pageObject.setTotalRow(mapper.getTotalRow(pageObject));
		
		log.info(pageObject);
		
		return mapper.list(pageObject);
	}

	@Override
	public BoardVO view(long no, int inc) throws Exception {
		// TODO Auto-generated method stub
		if(inc == 1) {
			mapper.increase(no);
		}
		
		return mapper.view(no);
	}

	@Override
	public int write(BoardVO vo) throws Exception {
		// TODO Auto-generated method stub
		// insert 하고 가져온 no
		int result = mapper.write(vo);
		log.info("글번호"+vo.getNo());
		log.info("파일내용들"+vo.getAttachList());
		if(vo.getAttachList() != null && vo.getAttachList().size() > 0) {
			//람다식 사용
			vo.getAttachList().forEach(attach -> {
				//BoardAttachVO 타입의attach 에 받아서 for문 돌린다
				attach.setNo(vo.getNo());
				//mapper 로 등록
				log.info(attach);
				attachMapper.insert(attach);
			});
			
		}		
		return result;
	}

	@Override
	public int update(BoardVO vo) throws Exception {
		// TODO Auto-generated method stub
		//기존 정보 다지운다
		attachMapper.deleteAll(vo.getNo());
		//게시글 수정
		int updateResult = mapper.update(vo);
		
		log.info("업로드 파일이 없어야하는데"+vo.getAttachList());
		//파일정보 다시 올린다
		if (updateResult == 1  && vo.getAttachList() != null && vo.getAttachList().size() > 0) {
			log.info("여기");
			
			vo.getAttachList().forEach(attach -> {

				attach.setNo(vo.getNo());
				attachMapper.insert(attach);
			});
		}		
		
		return updateResult;
	}

	@Override
	public int delete(long no) throws Exception {
		// TODO Auto-generated method stub
		attachMapper.deleteAll(no);
		return mapper.delete(no);
	}

	@Override
	public List<BoardAttachVO> getAttachList(Long no) {
		// TODO Auto-generated method stub
		return attachMapper.findByNo(no);
	}

}
