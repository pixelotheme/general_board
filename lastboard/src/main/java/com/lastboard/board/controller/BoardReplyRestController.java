package com.lastboard.board.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lastboard.board.service.ReplyService;
import com.lastboard.board.util.PageObjectCustom;
import com.lastboard.board.vo.ReplyVO;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

@RestController 
@RequestMapping("/replies")
@Log4j
//@AllArgsConstructor - qualifier 사용못해서 주석처리
public class BoardReplyRestController {

	//만약 이름 중복으로 인해 못찾는다면 @Qualifier 로 지정해주면 된다
	@Autowired
	@Qualifier("replyServiceImpl")
	private ReplyService service;
	
	// 리스트 상태 + 콜렉션 타입으로 리턴, 해당 위치에 데이터만 입력하면 알아서 key-value 처럼 연관지어 파라미터에 넣어준다 
	@GetMapping(value = "/pages/{no}/{page}",
			//xml,json 타입 둘다 가능하다고 명시적으로 설정(json 은 UTF-8 로 변환한다)
			produces = {
					MediaType.APPLICATION_XML_VALUE,
					MediaType.APPLICATION_JSON_UTF8_VALUE
			}
			)
//	public ResponseEntity<List<ReplyVO>> getList(
	public ResponseEntity<Map<String, Object>> getList(
			@PathVariable("page") int page,
			@PathVariable("no") Long no
			){
		log.info("getList(Reply)..........");
		
		PageObjectCustom pageObject= new PageObjectCustom(page, 10);
		log.info(pageObject);
		
		//실행결과 찍어보기
		List<ReplyVO> list = service.getList(pageObject, no);
		
		//list 의 주소를 가져와  주소값만 vo 에 넣은 상황이라 
		//꺼내온 vo 를 수정하는 것은 list를 수정한것과 같다 -> 만약 new를 해서 생성했다면 달랐을것
		//-- 페이징으로 dto 로 변경 -> list 선언된 변수 for문으로 돌리자~
		for(ReplyVO vo : list) {
			//엔터처리
			vo.setReply(vo.getReply().replace("\n", "<br>"));
		}
		System.out.println("getList 메서드란? : "+list);
		
		log.info(list);
		
		Map<String, Object> map = new HashMap<String, Object>();
		//ajax 일때 페이징 처리 하기위해 map 으로 넘긴다
		map.put("list", list);
		map.put("pageObject", pageObject);
		
		//<> 비워두면 위에 리턴타입고 같이 설정된다, (데이터,상태)
		return new ResponseEntity<Map<String, Object>>(map,HttpStatus.OK);
	}
	
	
	//댓글 등록, consumes<소모하다> - applicationd에서 json 형식으로 넘어온다, produces - 전달할때 타입 (순수한 문자열 데이터로 넘어간다)
	@PostMapping(value = "/new", consumes = "application/json",produces = {MediaType.TEXT_PLAIN_VALUE})
	public ResponseEntity<String> create(@RequestBody ReplyVO vo){
		
		log.info("ReplyVO : " +vo);
		//입력 성공시 1 반환
		int insertCount = service.register(vo);
		
		log.info("Reply INSERT COUNT: " + insertCount);
		
		
		//삼항연산자 사용, 원래 에러나면 예외처리로 가야하는데 그렇게 안하고 있는상황
		return insertCount == 1 ? new ResponseEntity<String>("register success", HttpStatus.OK): new ResponseEntity<String>( HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	
	//업데이트
	@RequestMapping(method = {RequestMethod.PUT, RequestMethod.PATCH},
			value = "/{rno}", //url - /replues/1
			consumes = "application/json", //넘어오는 데이터 json
			produces = {MediaType.TEXT_PLAIN_VALUE} // 넘겨주는 데이터 -> 순수 text 문자열 데이터
			)
	public ResponseEntity<String> modify(@RequestBody ReplyVO vo, @PathVariable("rno") Long rno){
		//@RequestBody - json 타입 그대로 받는다 , 
		
		//따로 받아 넣어줘야한다 Pathvariable 로 넘겨준것이라 그럼 
		vo.setRno(rno);
		
		log.info(vo);
		
		return service.modify(vo) == 1 ? new ResponseEntity<String>("update success", HttpStatus.OK):new ResponseEntity<String>(HttpStatus.NOT_MODIFIED);
		
		
	}
	
	//text 그대로 넘긴다
	@DeleteMapping(value = "/{rno}", produces = {MediaType.TEXT_PLAIN_VALUE})
	public ResponseEntity<String> remove(@PathVariable("rno") long rno){
		
		log.info("remove - rno : "+rno);
		
		return service.remove(rno) == 1
				? new ResponseEntity<String>("delete success", HttpStatus.OK)
						: new ResponseEntity<String>(HttpStatus.NOT_FOUND);
		
	}
		
	
}
