package com.lastboard.board.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import com.lastboard.board.vo.AttachFileDTO;

import lombok.extern.log4j.Log4j;
import net.coobird.thumbnailator.Thumbnailator;

@Controller
@Log4j
public class UploadController {

	//파일을 저장하는 폴더 위치 , 자바는 \ 하나만쓰면 특수문자로 구문한다(한줄 바꿈이 된다) 그래서 2개씀
	//서버와 상관이 없는 위치 -> 업로드된 파일은 url로 직접 연결이 안된다.(서버밖에있기때문에)
	String uploadFolder = "c:\\upload\\image";

	
	
	//업로드 폼
	@GetMapping("/uploadForm")
	public void uploadForm() {
		log.info("upload form ");
		
	}
	
	//업로드 처리
	@PostMapping("/uploadFormAction")
	public void uploadFormPost(MultipartFile[] uploadFile, Model model) throws Exception {
		
		//파일을 저장하는 폴더 위치 , 자바는 \ 하나만쓰면 특수문자로 구문한다(한줄 바꿈이 된다) 그래서 2개씀
		//서버와 상관이 없는 위치 -> 업로드된 파일은 url로 직접 연결이 안된다.(서버밖에있기때문에)
		String uploadFolder = "c:\\upload\\image";
		
		// 폴더가 없으면 오류 -> 처리해 주자
		//폴더 타입으로 만들어주자
		File forder = new File(uploadFolder);
		//폴더가 없으면 만들어주자
		if(!forder.exists()) forder.mkdirs();//모든 경로를 만든다 mkdir 은 마지막 파일만 만든다
		
		
		//올라온 파일 정보 출력하고 저장하자. 배열이므로 반복문 처리 - 향상된 for(= foreach)
		for(MultipartFile multipartFile : uploadFile) {
			
			log.info("--------------------------------------");
			//클라이언트의 파일명 출력
			String fileName = multipartFile.getOriginalFilename();
			log.info("아직서버에 있는 File Name :"+ fileName);
			//작업하고있는 파일의 용량
			log.info("아직서버에 있는 File Size :"+multipartFile.getSize());
			
			//서버에 저장처리 -> 저장되는 파일명 
			//폴더와 파일의 정보를 같이 넣어서 만들수도 있고 , 폴더와 파일을 따로 구분해서 넣어서 만들수있다
			//(폴더 경로, 서버에 올라가있는 파일 이름)
			//저장되는 파일명
			File saveFile = new File(uploadFolder, fileName);
			
			//저장처리
			multipartFile.transferTo(saveFile);
			
		}
		
		
	}// end of method 
	
	
	//ajax 업로드 폼  - ajax 파일 업로드를 위한 JSP 메서드
	@GetMapping("/uploadAjax")
	public void uploadAjaxForm() {
		log.info("upload ajax ");
		
	}
	
	//ajax 업로드 처리
	@PostMapping(value = "/uploadAjaxAction", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
	@ResponseBody // 이걸 써줘야 순수한 데이터가 그대로 넘어간다 (controller에 @RestController 안붙여줘셔 그럼)
	public ResponseEntity<List<AttachFileDTO>> uploadAjaxPost(MultipartFile[] uploadFile) throws Exception {
		
		
		//jsp 로 안가기때문에 model 안쓴다
		log.info("upload ajax post............");
		
		//파일 정보를 담는 객체 - ajax 처리중 추가
		List<AttachFileDTO> list = new ArrayList<AttachFileDTO>();
		
		//파일을 저장하는 폴더 위치 , 자바는 \ 하나만쓰면 특수문자로 구문한다(한줄 바꿈이 된다) 그래서 2개씀
		//서버와 상관이 없는 위치 -> 업로드된 파일은 url로 직접 연결이 안된다.(서버밖에있기때문에)
//		String uploadFolder = "c:\\upload\\image";
		
		// 폴더가 없으면 오류 -> 처리해 주자
		//날짜폴더 작성 - getFolder()
		String uploadFolderPath = getFolder();
		//폴더 타입으로 만들어주자, 현재 날짜 기준으로 파일 타입을 만들어주자 - 자동으로 뒤에 + 된다
		File uploadPath = new File(uploadFolder,uploadFolderPath);
		//폴더가 없으면 만들어주자
		if(!uploadPath.exists()) uploadPath.mkdirs();//모든 경로를 만든다 mkdir 은 마지막 파일만 만든다
		
		
		//올라온 파일 정보 출력하고 저장하자. 배열이므로 반복문 처리 - 향상된 for(= foreach)
		for(MultipartFile multipartFile : uploadFile) {
			
			//첨부파일 정보저장 객체 생성
			AttachFileDTO attachDTO = new AttachFileDTO();
			
			
			log.info("--------------------------------------");
			//클라이언트의 파일명 출력
			String fileName = multipartFile.getOriginalFilename();
			
			//중복이 되지 않는 데이터 생성
			UUID uuid = UUID.randomUUID();
			
			log.info("아직서버에 있는 File Name :"+ fileName);
			//작업하고있는 파일의 용량
			log.info("아직서버에 있는 File Size :"+multipartFile.getSize());
			
			//서버에 저장처리 -> 저장되는 파일명 
			
			//서버에 올라가는 파일명
			String uploadFileName = uuid.toString()+"_"+ fileName;

			
			
			//폴더와 파일의 정보를 같이 넣어서 만들수도 있고 , 폴더와 파일을 따로 구분해서 넣어서 만들수있다
			//(폴더 경로, 서버에 올라가있는 파일 이름)
			//저장되는 파일명
			File saveFile = new File(uploadPath, uploadFileName);
			
			//저장처리
			multipartFile.transferTo(saveFile);

			//화면에 보여줄 파일 데이터의 정보를 수집한다.
			
			//순수한 파일명 uuid 제외한 이름
			attachDTO.setFileName(fileName);
			//교제의 uploadFolderPath 와 같다, string 타입으로 꺼내줘야함
//			attachDTO.setUploadPath(uploadPath.getName()); 이름만나옴
			attachDTO.setUploadPath(uploadFolderPath); //날짜 폴더자체만 저장한다 / 보안상~
			attachDTO.setUuid(uuid.toString()); // 중복이 안된다~ pk 로 지정
			
			//데이터 확인
			log.info(attachDTO);
			// 이미지 리스트를 위해서 작은 이미지 처리가 필요하다. - 섬네일 처리
			if(checkImageType(saveFile)) {
				//if문 통과하면 image 라서 통과한것
				attachDTO.setImage(true);
				
				//바이트 단위 파일 저장을 위한 객체로 만들어 준다 , file(saveFile) 로 만들어버리면 덮어씌우기가 되어 이름에 변형을 준다
				// new File() 로 파일을 새로 만들어준다
				FileOutputStream thumbnail = new FileOutputStream(new File(uploadPath , "s_"+uploadFileName));
				
				//원본 이미지파일을 사용해서 섬네일 저장 파일로 넣는다
				//파일을 일정크기로 만들어 저장시킨다, multipart에서 바이트 단위로 꺼내서 잘라준다
				Thumbnailator.createThumbnail(multipartFile.getInputStream(), thumbnail,100,100);
				
				//사용한 객체를 닫자.
				thumbnail.close();
				
			}//end of if
			
			list.add(attachDTO);
			
		} //end of for
		
		//한글이 특수문자등.... 특별한 문자로 인코딩된다(= 코드화 시킨다)
//		return URLEncoder.encode("파일업로드", "utf-8");
		//json utf-8 로 이미 설정되어있다, 파일 업로드는 그대로 업로드 되고 -> return으로 해당 데이터를 반환한다
		return new ResponseEntity<List<AttachFileDTO>>(list, HttpStatus.OK);
		
	}// end of method 
	
	//파일을 보여주는 메서드
	@GetMapping("/display")
	@ResponseBody //순수한 byte 타입으로 넘긴다 (url이나 다른 정보가 아닌)
	// url / display?fileName=~~ 로 url 을 넘겨줘야함
	public ResponseEntity<byte[]> getFile(String fileName) throws Exception{
		
		ResponseEntity<byte[]> result = null;

		//넘어오는 데이터 확인
		log.info("fileName:" + fileName);
		
		//전달할 파일을 연결 -- 전역변수로 다시 지정한 uploadFolder 를 파일 객체로 만들어준다 
		// -> c:\\~~ 서버가 아닌 다른 위치에 저장
		//uploadFolder = "c:\\upload\\image";
		// fileName = yy/MM/dd/ + s_ + uuid_ + fileName -> 찾으면 실제 사진 을 찾을수 있다
		// 파일 주소(c:\\upload\\image) + 파일 이름( yyyy/MM/dd + s_+ uuid + _ + fileName) 
		File file = new File(uploadFolder,fileName);
		
		//타입을 지정해준다 - jpg, png....
		HttpHeaders header = new HttpHeaders();
		//probe 으로 타입을 넣어준다 - jpg,png.....
		header.add("Content-Type", Files.probeContentType(file.toPath()));
		//파일 자체를  Byte배열 타입으로 복사해둔다, header 에 파일의 확장자타입을 저장해준것을 넘겨준다
		//<byte[]> 소문자로 넣어줘야함 - 넘겨줄 이미지를 바이트타입으로 만들어 넘겨준다
		result = new ResponseEntity<byte[]>(FileCopyUtils.copyToByteArray(file),header,HttpStatus.OK);
		
		
		return result;
	}
	
	//이미지가 아닌 파일의 다운로드 메서드, 넘기는 데이터 APPLICATION_OCTET_STREAM_VALUE -  
	@GetMapping(value = "/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	@ResponseBody
	//("사용자 브라우저 이름") 받아오는 어노테이션 @RequestHeader("User-Agent")
	public ResponseEntity<Resource> downloadFile(@RequestHeader("User-Agent") String userAgent, String fileName) throws Exception{
		ResponseEntity<Resource> entity = null;
		
		//jsp 에서 fileName 조합해서 넘어온다 -> 추가로 앞에 c:\\upload\\image 붙여줘야 서버 밖에 파일을 가져온다
		log.info("fileName : " + fileName);
		//크롬 Mozilla/~~~~~~~
		log.info("userAgent : " + userAgent);
		
		//uploadFolder = "c:\\upload\\image"; = 마지막에 \ 없다
		// 해당 이름에 해당하는 경로 및 파일을 가져와 자원타입에 저장한다
		Resource resource = new FileSystemResource(uploadFolder+"\\"+fileName);
		log.info("resource : " + resource);
		
		//다운로드 할때 원본파일 다운로드 하자 - 
		//파일 이름을 resource 에서 꺼낸다. - 별도로 작성해 줘도 된다,/
		//- header에 붙여서 사용하면 resourceName 으로 다운로드 된다
		String resourceName = resource.getFilename();
		log.info("resourceName(UUID 포함) : " + resourceName);

		//uuid + _ 언더바까지 잘라낸다(+1 한다) - 순수한 이름만 나온다(uuid는  "_" 가 없다 모두 "-" 로 표시한다)
		//파일의 정보는 들어가있고 이름만 바뀐다
		resourceName = resourceName.substring(resourceName.indexOf("_")+1);
		log.info("resourceName(UUID 제거된 이름) : " + resourceName);
		
		String downloadname = null;
		
		//브라우저에 따른 한글처리 - IE, Edge 문제
		if(userAgent.contains("Trident")) {
			
			log.info("IE browser: ");
			//IE일떄 - 인코딩했을때 \+ 가 들어오는데 그건 공백으로 치환해준다
			downloadname = URLEncoder.encode(resourceName, "UTF-8").replace("\\+", " ");
			log.info("downloadname : " + downloadname);

		}else if (userAgent.contains("Edg")) {
			//edge 는 edg 까지만 나와서 edg 라고 변경
			log.info("Edge browser: ");
			// edge 처리 - 직접 인코딩 처리만 해주면된다 
			downloadname = URLEncoder.encode(resourceName, "UTF-8");
			log.info("downloadname : " + downloadname);

		}else {
			log.info("Chrome browser: ");
			//그외 브라우저 - 크롬, 사파리...
			downloadname = new String(resourceName.getBytes("UTF-8"),"ISO-8859-1");
			log.info("downloadname : " + downloadname);
		}
		
		//response의 보내질 데이터의 헤더 세팅을 위한 객체 생성
		HttpHeaders headers = new HttpHeaders();
		
		//header 를 다운로드받기위해 content-Disposition , "attachment; 로 다운로드 시킨다"
		//new String 으로 객체 생성해서 값을 넣어주면 서 인코딩 도 같이한다 / 뒤에 값이 기존값 -> 앞에 (UTF-8) 로 변경시켜줘라
		// string (resourceName.get~~) 할때 resourceName 에  uuid 때고 s_ 때고 처음 파일명만 넣어주면 그 이름으로 다운받을수 있다
		//downloadname 은 단순히 이름일뿐 
		headers.add("Content-Disposition", "attachment; filename=" + downloadname);
		log.info("headers : " + headers);
		
		//resource 에 실제 파일정보가 담겨있고 , headers 에 이름과 파일확장자정보가 들어있다
		entity = new ResponseEntity<Resource>(resource, headers, HttpStatus.OK);
		log.info("entity : "+entity);
		
		return entity;
		
	} // 이미지가 아닌 파일의 다운로드 메서드 끝
	
	//파일 삭제 메서드
	//dto 로 안받고 파라미터로 받음 // 모든 예외처리는 advice에서 한다
	@PostMapping(value = "/deleteFile")
	@ResponseBody // 컨트롤러에 restController 를 안붙여놔서 붙여줘야한다 (JSON 방식 jsp 에 보내기위해서), stringify 안붙이고 json 형식과 다르게 파라미터만넘김
	public ResponseEntity<String> deleteFile(String fileName, String type) throws Exception{
		
		log.info("fileName :" + fileName + "type : "+"type");
		// 인코딩되어(코드화 되어 넘어온) 넘어오 fileName -> 문자열로 가능하게
		// 삭제하려면 파일 객체로 만든다, 콤마를 쓰면 \ 가 자동붙는데 + 로 연결하는건 안붙는다
		File file = new File(uploadFolder + "\\" + URLDecoder.decode(fileName, "utf-8"));
		
		//확인
		log.info("존재 여부 : "+ file.exists());
		//s_ 파일 삭제한다.
		boolean result = file.delete();// deleteOnexit() - 있으면 삭제하자 - 삭제가 되면 true
		
		log.info(result);
		
		//이미지인 경우 섬네일 파일은 fileName(s_ 를 지운) 파일도 삭제하여야한다
		if(type.equals("image")) {
			//파일객체의 절대경로를 가져온다, replace 를 사용하는 경우 중간 파일명 안에 s_ 가 있는경우 사라지는 상황이 생긴다
//			String largeFileName = file.getAbsolutePath().replace("s_", "");
			
			//파일객체로 만든후 삭제하면된다
//			new File(largeFileName).delete(); // 한줄로 끝 
//			log.info(largeFileName);
			
			//uuid_s_fileName - 처음 s 가 나온 인덱스 찾아서 +2 되는부분 을 찾고 앞뒤를 연결시킨다
			//뒤에서 \\ 의 위치를 찾아서  위치 이후의 맨처음 나오는 s_를 찾는다. ->, indexof ("찾는 문자열",찾는시작인덱스지정) 
			//replaceFirst 로 처음나오는 값만 바꾸기 메서드를 사용한다
			String largeFileName = file.getAbsolutePath(); //s_ 가 들어가있다
			log.info("s_가 들어 있는 파일명 : "+largeFileName); 
			
			// \\위치 앞은 폴더라서 그냥 붙인다
			int pos = largeFileName.lastIndexOf("\\"); // 맨 마지막 의 \\ 위치를 잡는다
			// 파일명 전의 폴더 경로까지 자르기  + 파일명 부터 가져오고 처음나오는 s_ 만 자르기
			largeFileName = largeFileName.substring(0,pos)+largeFileName.substring(pos).replaceFirst("s_", ""); 
			log.info("s_가 제거된 파일명 : "+largeFileName); 
			
			new File(largeFileName).delete(); // 한줄로 끝 
			log.info(largeFileName);
			
			
		}
		
		return new ResponseEntity<String>("success deleted.", HttpStatus.OK);
		
		
	}
	
	
	//날짜 폴더 작성을 위한 메서드 - 매핑과는 상관이 없다
	private String getFolder() {
		//jsp의 fmt와 동일하다 -"yyyy-MM-dd-hh-mm" 분단위도 가능
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		// 오늘날짜 객체
		Date date = new Date();
		
		String str = sdf.format(date); ///형식 yyyy-MM-dd 오늘날짜기준 으로 바뀐다
		
//		System.out.println(str);
//		System.out.println(File.separator);
//		System.out.println(str.replace("-", File.separator));

		return str.replace("-", File.separator);
		
	}
	
	//이미지 파일에 대한 판다
	private boolean checkImageType(File file) throws Exception {
		
		//
		String contentType = Files.probeContentType(file.toPath());
		
		log.info("checkImageType().contentType"+contentType);
		// 시작이 이미지인 경우 리턴값 true, 아닌경우 false
		return contentType.startsWith("image"); // contentType.indexOf("image") == 0 - 처음 인덱스에 image 시작인가?
	}
	
} //end of class
