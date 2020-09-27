package dfh.service.interfaces;

import java.util.Date;
import java.util.List;

import dfh.action.vo.AgentReferralsVO;

public interface IAgentReferralsStatistic {

	
	public AgentReferralsVO getAgentReferralsCount(String agentName,Date start,Date end)throws Exception;
	//查询推广地址
	public List<String> queryAgentAddress(String loginname);
	
}
