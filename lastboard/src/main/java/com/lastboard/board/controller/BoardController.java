package com.lastboard.board.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lastboard.board.service.BoardService;
import com.lastboard.board.util.PageObjectCustom;
import com.lastboard.board.vo.BoardAttachVO;
import com.lastboard.board.vo.BoardVO;

import lombok.extern.log4j.Log4j;

@Controller
@Log4j
@RequestMapping("/board")
public class BoardController {

	@Autowired
	@Qualifier("bsi")
	private BoardService service;
	
	@Autowired
	private UploadController uploadController;
	
	//게시판 리스트
	@GetMapping("/list.do")
	public String list(PageObjectCustom pageObject, Model model) throws Exception{
		
		log.info("게시판 리스트------------------");
		
		model.addAttribute("list", service.list(pageObject));
		
		//pageObject 데이터
		model.addAttribute("pageObject", pageObject);
		
		
		return "board/list";
		
	}

	//게시판 글보기
	@GetMapping("/view.do")
	public String view(long no, int inc, Model model) throws Exception{
		
		log.info("게시판 보기-----------------");
		model.addAttribute("vo", service.view(no, inc));
		return "board/view";
		
	}
	
	//보기에서 보여 줄 첨부파일 메서드 - Ajax를 통해서 가져 간다
	//json 으로 보내준다 -> 이럴때는 순수데이터라고 꼭 지정해줘야한다 - ResponseBody
	@GetMapping(value = "/getAttachList", 
			//결과를 JSON - 한글 UTF-8 로 처리
			produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
	@ResponseBody // jsp 나 다른곳으로 이동하지않고 url 로 바로 전달된다 -> RestController 로 선언하면 안써도 된다
	public ResponseEntity<List<BoardAttachVO>> getAttachList(long no){
		ResponseEntity<List<BoardAttachVO>> entity = null;
		
		log.info("getAttachList - no : " + no);
	
		entity = new ResponseEntity<List<BoardAttachVO>>(service.getAttachList(no),HttpStatus.OK);
		//json 데이터로 넘어간다 - produces 로 지정해줬다
		return entity;
	}
	
	
	
	//게시판 등록
	@GetMapping("/write.do")
	public String writeForm(int perPageNum) throws Exception{
		
		log.info("게시판 등록 폼 ------------------");
		
		return "board/write";
		
	}
	//게시판 등록
	@PostMapping("/write.do")
	public String write(BoardVO vo, int perPageNum) throws Exception{
		
		log.info("게시판 등록------------------");
		//다중 파일들은 이미 스크립트 통해 실제파일은 ajax로 올라감 - 파일명만 리스트로 넘어온다
		service.write(vo);
		
		return "redirect:list.do?perPageNum="+perPageNum;
		
	}
	//게시판 글 수정 폼
	@GetMapping("/update.do")
	public String updateForm(long no , Model model) throws Exception{
		
		log.info("게시판 글수정 폼------------------");
		
		//db 에서 데이터를 가져와서 화면에 세팅
		model.addAttribute("vo", service.view(no, 0));
		
		return "board/update";
		
	}
	//게시판  글 수정 처리
	@PostMapping("/update.do")
	public String update(PageObjectCustom pageObject,BoardVO vo) throws Exception{
		
		log.info("게시판 수정 처리------------------");
		int result = service.update(vo);
		
		if(result ==1 ) {
			log.info("게시판 수정 완료");
		}else {
			log.info("게시판 글번호를 확인해주세요");
		}
		
		
		return "redirect:view.do?no="+vo.getNo()
				+"&inc=0"
				+"&perPageNum="+pageObject.getPerPageNum()
				+"&page="+pageObject.getPage()
				+"&key="+pageObject.getKey()
				+"&word="+pageObject.getWord()
				;
		
	}
	//게시판 삭제 처리
	@GetMapping("/delete.do")
	public String delete(long no, int perPageNum, RedirectAttributes rttr) throws Exception{
		
		log.info("게시판 삭제------------------");
		
		List<BoardAttachVO> attachList = service.getAttachList(no);
		
		int result = service.delete(no);
		if(result ==1 ) {
			log.info("게시판 삭제 완료");

			
		}else {
			log.info("게시판 글번호를 확인해주세요");
		}
		log.info(attachList);
		deleteFiles(attachList);
		return "redirect:list.do?perPageNum="+perPageNum;
		
	}
	
	
	private void deleteFiles(List<BoardAttachVO> attachList) {
	    
	    if(attachList == null || attachList.size() == 0) {
	      return;
	    }
	    
	    log.info("delete attach files...................");
	    log.info(attachList);
	    
	    attachList.forEach(attach -> {
	      try {        
	    	  //위치마다 잘 정해줘야함
	        Path file  = Paths.get("C:\\upload\\image\\"+attach.getUploadPath()+"\\" + attach.getUuid()+"_"+ attach.getFileName());
	        
	        Files.deleteIfExists(file);
	        
	        if(Files.probeContentType(file).startsWith("image")) {
	        
	          Path thumbNail = Paths.get("C:\\upload\\image\\"+attach.getUploadPath()+"\\s_" + attach.getUuid()+"_"+ attach.getFileName());
	          
	          Files.delete(thumbNail);
	        }
	
	      }catch(Exception e) {
	        log.error("delete file error" + e.getMessage());
	      }//end catch
	    });//end foreachd
	  }
	
	
}
