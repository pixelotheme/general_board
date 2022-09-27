package com.lastboard.board.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.lastboard.board.mapper.ReplyMapper;
import com.lastboard.board.util.PageObjectCustom;
import com.lastboard.board.vo.ReplyVO;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Service
@Log4j
@Qualifier("replyServiceImpl")
public class ReplyServiceImpl implements ReplyService {

	@Setter(onMethod_ = @Autowired)
	private ReplyMapper mapper;
	
	@Override
//	public ReplyPageDTO getList(Criteria cri, Long bno) {
	public List<ReplyVO> getList(PageObjectCustom pageObject, Long no) {

		log.info("get Reply List of a Board no : " + no);
		pageObject.setTotalRow(mapper.getCountByNo(no));
		
		return mapper.getListWithPaging(pageObject, no);
	}

	@Override
	public int register(ReplyVO vo) {
		// TODO Auto-generated method stub
		
		log.info("register----------" + vo);
		return mapper.insert(vo);
	}

	@Override
	public int modify(ReplyVO vo) {
		// TODO Auto-generated method stub
		log.info("modify ... vo : " + vo);
		
		return mapper.update(vo);
	}

	@Override
	public int remove(long rno) {
		// TODO Auto-generated method stub
		log.info("remove... rno : " + rno);
		
		return mapper.delete(rno);
	}

}
