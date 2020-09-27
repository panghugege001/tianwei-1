package dfh.service.interfaces;

import java.util.List;

import dfh.model.bean.BbsTieBean;

public interface BbsService {
	
	public List<BbsTieBean> findAllbak(String type) ;
	public List<BbsTieBean> findAll(String type , Integer size , String order ) ;

}
