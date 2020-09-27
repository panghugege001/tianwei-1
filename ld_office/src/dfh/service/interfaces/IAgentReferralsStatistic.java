package dfh.service.interfaces;

import java.util.Date;
import java.util.List;

import dfh.action.vo.AgentReferralsVO;
import dfh.model.Users;

public interface IAgentReferralsStatistic {

	
	public AgentReferralsVO getAgentReferralsCount(String agentName,Date start,Date end)throws Exception;
	
	public List<AgentReferralsVO> getAgentReferralsList(String agentName,Date start,Date end,int pageno,int length)throws Exception;
	
	public Users getUser(String agentFlag, int flagType) throws Exception;
}
