package dfh.action.bbs;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.interceptor.ServletRequestAware;

import dfh.action.SubActionSupport;
import dfh.model.bean.BbsTieBean;
import dfh.service.interfaces.BbsService;
import dfh.utils.GsonUtil;

public class BbsAction  extends SubActionSupport implements ServletRequestAware{
	private HttpServletRequest request;
	
	private String type ;
	private Integer size ;
	private String order ;
	
	public String getOrder() {
		return order;
	}



	public void setOrder(String order) {
		this.order = order;
	}



	public Integer getSize() {
		return size;
	}



	public void setSize(Integer size) {
		this.size = size;
	}



	public String getType() {
		return type;
	}



	public void setType(String type) {
		this.type = type;
	}



	public BbsService getBbsService() {
		return bbsService;
	}



	public void setBbsService(BbsService bbsService) {
		this.bbsService = bbsService;
	}



	private  BbsService bbsService ;
	
	//获取最新帖子
	public String getLastNewTiebak(){
		List<BbsTieBean> list = bbsService.findAllbak(type) ;
		if(null != list && list.size()>0){
			GsonUtil.GsonObject(list);
		}else{
			GsonUtil.GsonObject("empty");
		}
		return null ;
	}
	
	//获取最新帖子
	public String getLastNewTie(){
		List<BbsTieBean> listDisplayOrder = bbsService.findAll(type , size,order) ;
//		List<BbsTieBean> list = bbsService.findAll(type , 3 , "dateline") ;
//		for (BbsTieBean bbsTieBean : list) {
//			listDisplayOrder.add(bbsTieBean);
//		}
		if(null != listDisplayOrder && listDisplayOrder.size()>0){
			GsonUtil.GsonObject(listDisplayOrder);
		}else{
			GsonUtil.GsonObject("empty");
		}
		return null ;
	}
	
	
	


	@Override
	public void setServletRequest(HttpServletRequest arg0) {
		// TODO Auto-generated method stub
		request=arg0;
	}

}
