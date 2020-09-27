package dfh.service.implementations;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import dfh.action.vo.AgentReferralsVO;
import dfh.dao.NetPayDao;
import dfh.dao.ProposalDao;
import dfh.service.interfaces.IAgentReferralsStatistic;

public class AgentReferralsStatisticImpl implements IAgentReferralsStatistic {

	private ProposalDao proposalDao;
	private NetPayDao payorderDao;
	
	
	@SuppressWarnings("unchecked")
	@Override
	public AgentReferralsVO getAgentReferralsCount(String agentName,Date start,Date end) throws Exception {
		
		List proposalList = proposalDao.getAgentReferralsCountAtProposal(agentName, start, end);
		List payorderList = payorderDao.getAgentReferralsCountAtPayorder(agentName, start, end);
		//投注额统计
		List agprofitList = proposalDao.getAgentReferralsCountAtAgProfit(agentName, start, end);
		
		Map<String ,AgentReferralsVO> users=this.userListAllToMap(proposalList, payorderList,agprofitList);
		if (users.isEmpty()) {
			return null;
		}
		AgentReferralsVO userTotal=new AgentReferralsVO();
		Set<String> userSet = users.keySet();
		Iterator<String> it = userSet.iterator();
		Integer uUser = 0;
		while(it.hasNext()){
			String key = it.next();
			AgentReferralsVO vo = users.get(key);
			if(500<=vo.getMoney() && 1000<=vo.getBet()){
				uUser++;
			}
			userTotal.setAmount(vo.getMoney());
		}
		userTotal.setPersonCount(uUser);
		return userTotal;
	}
	/**
	 * 统计存款、在线存款、及投注额会员
	 * @param proposalList
	 * @param payorderList
	 * @param payorderList
	 * @return agprofitList
	 * @throws Exception
	 */
	private Map<String ,AgentReferralsVO> userListAllToMap(List<Object[]> proposalList,List<Object[]> payorderList,List<Object[]> agprofitList)throws Exception{
		Map<String ,AgentReferralsVO> users=new HashMap<String, AgentReferralsVO>();
		if (proposalList!=null) {
			for (int i = 0; i < proposalList.size(); i++) {
				Object[] o= proposalList.get(i);
				users.put(String.valueOf(o[0]), new AgentReferralsVO(String.valueOf(o[0]), ((Number)o[1]).doubleValue()));
			}
		}
		
		if (payorderList!=null) {
			for (int i = 0; i < payorderList.size(); i++) {
				Object[] o= payorderList.get(i);
				String key=String.valueOf(o[0]);
				if (users.containsKey(key)) {
					AgentReferralsVO vo = users.remove(key);
					vo.setMoney(((Number)o[1]).doubleValue());
					users.put(key, vo);
				}else{
					users.put(String.valueOf(o[0]), new AgentReferralsVO(String.valueOf(o[0]), ((Number)o[1]).doubleValue()));	
				}
			}
		}
		
		if(agprofitList!=null){
			for (int i = 0; i < agprofitList.size(); i++) {
				Object[] o= agprofitList.get(i);
				String key=String.valueOf(o[0]);
				if (users.containsKey(key)) {
					AgentReferralsVO vo = users.get(key);
					vo.setBet(((Number)o[1]).doubleValue());
				}
			}
		}
		
		return users;
	}
	
	@Override
	public List<String> queryAgentAddress(String loginname) {
		return proposalDao.queryAgentAddress(loginname);
	}

	public ProposalDao getProposalDao() {
		return proposalDao;
	}

	public void setProposalDao(ProposalDao proposalDao) {
		this.proposalDao = proposalDao;
	}

	public NetPayDao getPayorderDao() {
		return payorderDao;
	}

	public void setPayorderDao(NetPayDao payorderDao) {
		this.payorderDao = payorderDao;
	}


}
