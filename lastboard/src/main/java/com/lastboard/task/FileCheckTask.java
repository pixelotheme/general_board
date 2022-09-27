package com.lastboard.task;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.lastboard.board.mapper.BoardAttachMapper;
import com.lastboard.board.vo.BoardAttachVO;
import com.mchange.v2.util.CollectionUtils;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Log4j
@Component // 자동생성 - root-context.xml 에 base-package로 정의 되어있어야한다
public class FileCheckTask {

	//db처리를위한 mapper 자동 DI
	@Setter(onMethod_ = {@Autowired})
	private BoardAttachMapper attachMapper;
	
	//초 분 시 일 월 요일 (년)- 년에 해당하는부분은 옵션이다
	//주기적으로 삭제해야할 파일을 찾아서 삭제해 주는 메서드 -> 서버 stop후 start 해줘야 정상작동된다(단순restart했을때는 안될수도 있다)
//	@Scheduled(cron = "0 * * * * *")//시간 패턴 0초 일때 (60초) 실행 - 테스트용도
//	@Scheduled(cron = "0 0 2,4 * * *")//시간 패턴 2,4AM 일때 실행
	@Scheduled(cron = "10 25 21 * * *")//시간 패턴 2AM 일때 실행
	public void checkFiles()throws Exception{
		
		log.warn("File Check Task run ,.................");
		
		
		//db에 있는 파일목록 가져오기, fileType 변수가 true 면 s_ 파일도 같이 삭제해줘야한다
		List<BoardAttachVO> fileList = attachMapper.getOldFiles();
		
//		log.warn(fileList);
		//콤마를 넣어주면 \ 로 구분해준다
		//fileList 는 DB 에서 가져온 List 를 stream으로 한개씩 map에 vo라는 key 로 값을 넣는다 - 그 value로 파일객체이자 절대주소로 만들어주는 Paths.get()사용
		//, collect 로 수집 - List 형식으로 수집한다 -> Path 타입으로 List<Path> fileListPaths 변수에 수집된다
		List<Path> fileListPaths = fileList.stream()
				.map(vo -> Paths.get("C:\\upload\\image", vo.getUploadPath(), vo.getUuid() + "_" + vo.getFileName()))
				.collect(Collectors.toList());
		
		//이미지인 경우 - 위에서 받아온 파일리스트에서 fileType 을 체크한다 - boolean타입의 getter = is~~, 위와 같은 과정을 통해 s_ 파일 을 추가로 담아준다
		fileList.stream()
		.filter(vo -> vo.isFileType() == true)
		.map(vo -> Paths.get("C:\\upload\\image", vo.getUploadPath(), "s_" + vo.getUuid() + "_" + vo.getFileName()))
		.forEach(p -> fileListPaths.add(p));
		
		
		log.warn("==========================================");
		
		// DB 목록 출력
		fileListPaths.forEach(p -> log.warn("DB file : " + p));
		
		// 폴더의 파일 목록 가져오기, 생성해둔 메서드 사용- 파일객체로 폴더를 먼저 지정한다 -> toFile() 로 파일객체로 만든다(파일도 관리가능하다)
		// 저장 폴더 파일 객체만들기 - 절대경로 로그로 확인 
		File targetDir = Paths.get("C:\\upload\\image", getFolderYesterDay()).toFile();
		log.warn("targetDir : " + targetDir.getAbsolutePath());
		
		//폴더 경로 안에 파일을 리스트로 저장하는 메서드 .listFiles() 선언해서 foreach 돌려준다
		for(File f : targetDir.listFiles()) {
			//모든 파일 출력
			log.warn("f : " + f.getAbsolutePath());
		}
		
		// 저장 폴더 안에 있는 파일과 폴더를 목록으로 가져오기, listFiles(안에 필터를 지정해서 해당조건에 맞는 파일만 가져온다- db에서 꺼내온 데이터와 비교해서 같지않은것만 꺼내온다),
		//listFiles 로 꺼내온 파일을 file 에 바로 넣어준다 (람다식)
		File[] removeFiles = targetDir.listFiles(file -> !fileListPaths.contains(file.toPath()));
		
		for(File file : removeFiles) {
			//지우는 메서드
			log.warn("removeFile : " + file.getAbsolutePath());
			file.delete();
			
		}
		
	}
	
	private String getFolderYesterDay() {
		//날짜패턴을 지정해 객체 생성 - 오늘 날짜 불러오면 해당 형식으로 불러온다
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
		
		// 오늘날짜 객체 - Date, Calender 두가지 중 하나를 사용할 수 있다
		Calendar cal = Calendar.getInstance(); // getInstance 메서드에 오늘날짜 생성해서 넘겨주는 객체 생성까지해준다
		// 어제 날짜 추가함
		cal.add(Calendar.DATE, -1);
		
		//db 는 날짜를 \ 로 구분했다, getTime 을 가져오면 -> Long 타입의 숫자가 나온다(Date타입의 리턴값)
		//어제 날짜를 위에 날짜 형식으로 만들어서 문자열로 준다, sdf 의 포맷 위에서 지정해줌
		String str = sdf.format(cal.getTime());
		
		//2022-08-29 => 2022\08\29, 시스템 파일 구분자로 넣어주자~ 리툭스일때 다를수도 있기 때문이다
		return str.replace("-", File.separator);
		
	}
	
	
	
}
