package dfh.service.implementations;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import dfh.model.bean.BbsTieBean;
import dfh.service.interfaces.BbsService;

public class BbsServiceImpl implements BbsService {
	private JdbcTemplate jdbcTemplateBBS ;
	
	public List<BbsTieBean> findAllbak(String type){
		
		String sql = "select * from pre_forum_thread where 1=1 and displayorder = 0 " ;
		if(!type.equals("1")){
			if(!this.valid(type)){
				return null;
			}
			sql += "and fid = "+type+" order by dateline desc limit 10";
		}else{
			sql += " order by dateline desc limit 10";
		}
		List list = jdbcTemplateBBS.queryForList(sql) ;
		Iterator it = list.iterator() ;
		List<BbsTieBean> result = new ArrayList<BbsTieBean>() ;
		BbsTieBean bean = null ;
		SimpleDateFormat sdf=new SimpleDateFormat("MM/dd"); 
		while(it.hasNext()){
			bean = new BbsTieBean() ;
			Map mapTie = (Map) it.next();
			bean.setId((Integer)mapTie.get("tid"));
			bean.setTitle((String)mapTie.get("subject"));
			bean.setCreateDate(sdf.format(new Date(((Long)mapTie.get("dateline"))*1000) ));
			result.add(bean);
		}
		return result ;
	}
	
	private boolean valid(String type) {
		
		if(StringUtils.isNotBlank(type)){
			String[] types = type.split(",");
			if(types != null && types.length > 0){
				for(int i = 0; i < types.length; i++){
					if(!types[i].matches("^\\d*[1-9]\\d*$")){
						return false;
					}
				}
			}
		}
		return true;
	}

	public List<BbsTieBean> findAll(String type , Integer size , String order ){
		if(size == null){
			
			size = 6;
		}
		if(order == null){
			
			order = "dateline";
		}
		
		String sql = "select * from pre_forum_thread where 1=1  and (displayorder = 1 or digest in (1,2,3))  " ;
		
		if(StringUtils.isNotBlank(type)){
			
			sql += "and fid in (" + type + ")";
		}
		sql += " order by " + order + " desc limit "+size+" ";
		
		List list = jdbcTemplateBBS.queryForList(sql) ;
		Iterator it = list.iterator() ;
		List<BbsTieBean> result = new ArrayList<BbsTieBean>() ;
		BbsTieBean bean = null ;
		SimpleDateFormat sdf=new SimpleDateFormat("MM/dd"); 
		while(it.hasNext()){
			bean = new BbsTieBean() ;
			Map mapTie = (Map) it.next();
			bean.setId((Integer)mapTie.get("tid"));
			bean.setTitle((String)mapTie.get("subject"));
			bean.setCreateDate(sdf.format(new Date(((Long)mapTie.get("dateline"))*1000) ));
			result.add(bean);
		}
		return result ;
	}
	
	
	

	public JdbcTemplate getJdbcTemplateBBS() {
		return jdbcTemplateBBS;
	}

	public void setJdbcTemplateBBS(JdbcTemplate jdbcTemplateBBS) {
		this.jdbcTemplateBBS = jdbcTemplateBBS;
	}
	

}
