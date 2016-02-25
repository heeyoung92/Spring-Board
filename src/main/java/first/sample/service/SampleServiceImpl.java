package first.sample.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import first.common.util.FileUtils;
import first.sample.dao.SampleDAO;

@Service("sampleService")
public class SampleServiceImpl implements SampleService{
	Logger log = Logger.getLogger(this.getClass());
    
	@Resource(name="fileUtils")
    private FileUtils fileUtils;
     
	@Resource(name="sampleDAO")
	private SampleDAO sampleDAO;
	
	@Override
	public Map<String, Object> selectBoardList(Map<String, Object> map) throws Exception {
		return sampleDAO.selectBoardList(map);
		
	}
	
	@Override
	public void insertBoard(Map<String, Object> map, HttpServletRequest request) throws Exception {
	    sampleDAO.insertBoard(map);
	    
	    //FIleUtils 클래스를 이용하여 파일을 저장하고 그 데이터를 가져온 후, DB에 저장
	    List<Map<String,Object>> list = fileUtils.parseInsertFileInfo(map, request);
	    for(int i=0, size=list.size(); i<size; i++){
	            sampleDAO.insertFile(list.get(i));
	    }
	    
	    /*
	    MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest)request;  //첨부파일은 Multipart형식의 데이터이며, HttpServletRequest에 담겨서 서버로 전송
	    Iterator<String> iterator = multipartHttpServletRequest.getFileNames();  //Iterator 패턴을 이용하여 request에 전송된 모든 name을 이용
	    MultipartFile multipartFile = null;
	    
	    while(iterator.hasNext()){
	        multipartFile = multipartHttpServletRequest.getFile(iterator.next()); 	//MultipartFile 객체에 request에서 파일 객체를 가져오기
	        if(multipartFile.isEmpty() == false){ 									// 실제 파일정보가 있는지 검사한 후, 파일 정보 추출
	            log.debug("------------- file start -------------");
	            log.debug("name : "+multipartFile.getName());
	            log.debug("filename : "+multipartFile.getOriginalFilename());
	            log.debug("size : "+multipartFile.getSize());
	            log.debug("-------------- file end --------------\n");
	        }
	    }
	    */
	}

	@Override
	public Map<String, Object> selectBoardDetail(Map<String, Object> map) throws Exception {
	    sampleDAO.updateHitCnt(map);
	    Map<String, Object> resultMap = new HashMap<String,Object>();
	    Map<String, Object> tempMap = sampleDAO.selectBoardDetail(map);		// 게시글 상세정보
	    resultMap.put("map", tempMap);
	     
	    List<Map<String,Object>> list = sampleDAO.selectFileList(map);		// 게시글 첨부파일 목록
	    resultMap.put("list", list);
	    
	    return resultMap;
	}
	
	@Override
	public Map<String, Object> updateBoardDetail(Map<String, Object> map) throws Exception {
	    Map<String, Object> resultMap = sampleDAO.selectBoardDetail(map);
	    return resultMap;
	}
	
	@Override
	public void updateBoard(Map<String, Object> map, HttpServletRequest request) throws Exception{
	    sampleDAO.updateBoard(map);
	  
	    sampleDAO.deleteFileList(map);		// 해당 게시글 첨부파일을 모두 DEL_GB = Y 로 변경
	    List<Map<String,Object>> list = fileUtils.parseUpdateFileInfo(map, request);
	    Map<String,Object> tempMap = null;
	    for(int i=0, size=list.size(); i<size; i++){
	        tempMap = list.get(i);
	        if(tempMap.get("IS_NEW").equals("Y")){
	            sampleDAO.insertFile(tempMap);
	        }
	        else{
	            sampleDAO.updateFile(tempMap);
	        }
	    }
	}
	
	@Override
	public void deleteBoard(Map<String, Object> map) throws Exception {
	    sampleDAO.deleteBoard(map);
	}
}
