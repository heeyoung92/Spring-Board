package first.common.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

public class AbstractDAO {
	   protected Log log = LogFactory.getLog(AbstractDAO.class);
	     
	    @Autowired
	    private SqlSessionTemplate sqlSession;
	     
	    protected void printQueryId(String queryId) {
	        if(log.isDebugEnabled()){
	            log.debug("\t QueryId  \t:  " + queryId);
	        }
	    }
	     
	    public Object insert(String queryId, Object params){
	        printQueryId(queryId);
	        return sqlSession.insert(queryId, params);
	    }
	     
	    public Object update(String queryId, Object params){
	        printQueryId(queryId);
	        return sqlSession.update(queryId, params);
	    }
	     
	    public Object delete(String queryId, Object params){
	        printQueryId(queryId);
	        return sqlSession.delete(queryId, params);
	    }
	     
	    public Object selectOne(String queryId){
	        printQueryId(queryId);
	        return sqlSession.selectOne(queryId);
	    }
	     
	    public Object selectOne(String queryId, Object params){
	        printQueryId(queryId);
	        return sqlSession.selectOne(queryId, params);
	    }
	     
	    @SuppressWarnings("rawtypes")
	    public List selectList(String queryId){
	        printQueryId(queryId);
	        return sqlSession.selectList(queryId);
	    }
	     
	    @SuppressWarnings("rawtypes")
	    public List selectList(String queryId, Object params){
	        printQueryId(queryId);
	        return sqlSession.selectList(queryId,params);
	    }
	    
	    @SuppressWarnings({ "rawtypes", "unchecked" })
	    public Map selectPagingList(String queryId, Object params){
	        printQueryId(queryId);
	         
	        Map<String,Object> map = (Map<String,Object>)params;
	        PaginationInfo paginationInfo = null;	
	         
	        if(map.containsKey("currentPageNo") == false || StringUtils.isEmpty(map.get("currentPageNo")) == true)
	            map.put("currentPageNo","1");
	         
	        paginationInfo = new PaginationInfo();			//페이징에 필요한 정보를 가지고 있는 전자정부 프레임워크의 클래스
	        paginationInfo.setCurrentPageNo(Integer.parseInt(map.get("currentPageNo").toString()));		
	        
	        /*한 페이지에 몇개의 행을 보여줄 것인지를 설정 */
	        if(map.containsKey("PAGE_ROW") == false || StringUtils.isEmpty(map.get("PAGE_ROW")) == true){
	            paginationInfo.setRecordCountPerPage(15);
	        }
	        else{
	            paginationInfo.setRecordCountPerPage(Integer.parseInt(map.get("PAGE_ROW").toString()));
	        }
	        paginationInfo.setPageSize(10);	// 한번에 보여줄 페이지의 크기를 지정  e.g.[이전] 1~10 [다음]
	         
	        int start = paginationInfo.getFirstRecordIndex();
	        //int end = start + paginationInfo.getRecordCountPerPage();
	        map.put("START",start+1);
	        map.put("COUNT",paginationInfo.getRecordCountPerPage());
	         
	        params = map;
	         
	        
	        /* 만약 조회된 결과값이 없으면 그에 해당하는 결과를 화면에 표시할 수 있도록 TOTAL_COUNT라는 값 추가 
	         * 반환될 결과에 위에서 만든 paginationInfo 클래스와 조회 후 결과리스트를 각각 paginationInfo와 result라는 key로 저장하여 반환
	         */
	        Map<String,Object> returnMap = new HashMap<String,Object>();
	        List<Map<String,Object>> list = sqlSession.selectList(queryId,params);
	         
	        if(list.size() == 0){
	            map = new HashMap<String,Object>();
	            map.put("TOTAL_COUNT",0);  
	            list.add(map);
	             
	            if(paginationInfo != null){
	                paginationInfo.setTotalRecordCount(0);
	                returnMap.put("paginationInfo", paginationInfo);
	            }
	        }
	        else{
	            if(paginationInfo != null){
	                paginationInfo.setTotalRecordCount(Integer.parseInt(list.get(0).get("TOTAL_COUNT").toString()));
	                returnMap.put("paginationInfo", paginationInfo);
	            }
	        }
	        returnMap.put("result", list);
	        return returnMap;
	    }
}
