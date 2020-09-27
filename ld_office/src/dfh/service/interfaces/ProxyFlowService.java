package dfh.service.interfaces;

import java.util.Date;
import java.util.List;

import dfh.model.ProxyFlow;

public interface ProxyFlowService {
	
	public List<ProxyFlow> getProxyFlowList(String loginname,String partner,Date start, Date end, Integer CountOfPage, Integer currentPage);
	
	public Integer getProxyFlowListCount(String loginname,String partner,Date start, Date end);

}
