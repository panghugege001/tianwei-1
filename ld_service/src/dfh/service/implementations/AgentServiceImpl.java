package dfh.service.implementations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import dfh.action.vo.AgentReferralsVO;
import dfh.model.Users;
import dfh.service.interfaces.AgentService;
import dfh.service.interfaces.CustomerService;
import dfh.service.interfaces.IAgentReferralsStatistic;

public class AgentServiceImpl implements AgentService {

	private static Logger log = Logger.getLogger(AgentServiceImpl.class);
	private IAgentReferralsStatistic agentReferralsStatistic;
	private CustomerService customerService;
	
	public List<Integer> getAgentSubUserInfo(String agent,Date start,Date end){
		List<Users> list = customerService.getSubUsers(agent);
		/**
		 * 下线会员 统计
		 */
		Integer subUser = 0;
		if(null!=list && !list.isEmpty()){
			subUser = list.size();
		}
		/**
		 * 计算代理下线活跃会员人数 统计
		 */
		Integer activeUser = 0;
		
		try{
			AgentReferralsVO  agentvo = agentReferralsStatistic.getAgentReferralsCount(agent, start, end);
			if(null!=agentvo){
				activeUser = agentvo.getPersonCount();	
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		List<Integer> l = new ArrayList<Integer>();
		l.add(subUser);
		l.add(activeUser);
		return l;
	}
	
	@Override
	public List<String> queryAgentAddress(String loginname) {
		List<String> addrList = agentReferralsStatistic.queryAgentAddress(loginname);
		return addrList;
	}

	public IAgentReferralsStatistic getAgentReferralsStatistic() {
		return agentReferralsStatistic;
	}

	public void setAgentReferralsStatistic(
			IAgentReferralsStatistic agentReferralsStatistic) {
		this.agentReferralsStatistic = agentReferralsStatistic;
	}

	public CustomerService getCustomerService() {
		return customerService;
	}

	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}
}
