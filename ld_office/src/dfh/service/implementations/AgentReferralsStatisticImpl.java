package dfh.service.implementations;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import dfh.action.vo.AgentReferralsVO;
import dfh.dao.NetPayDao;
import dfh.dao.ProposalDao;
import dfh.dao.UserDao;
import dfh.model.Users;
import dfh.service.interfaces.IAgentReferralsStatistic;
import dfh.utils.Constants;

public class AgentReferralsStatisticImpl implements IAgentReferralsStatistic {

	// 检索类型：
	public static int AGENTNAME=0;
	public static int AGENTURL=1;
	public static int AGENTID=2;
	
	private UserDao userDao;
	private String errormsg;
	private ProposalDao proposalDao;
	private NetPayDao payorderDao;
	
	
	@SuppressWarnings("unchecked")
	@Override
	public AgentReferralsVO getAgentReferralsCount(String agentName,Date start,Date end) throws Exception {
		// TODO Auto-generated method stub
		/*
		Object[] proposalObject=proposalDao.getAgentReferralsCountAtProposal(agentName, start, end);
		Object[] payorderObject=payorderDao.getAgentReferralsCountAtPayorder(agentName, start, end);
		
		AgentReferralsVO vo=null;
		if (((Number)proposalObject[0]).intValue()>0) {
			vo=new AgentReferralsVO();
			vo.setPersonCount(((Number)proposalObject[0]).intValue());
			vo.setAmount(((Number)proposalObject[1]).doubleValue());
		}
		if (((Number)payorderObject[0]).intValue()>0) {
			if (vo==null) {
				vo=new AgentReferralsVO();
			}
			vo.setPersonCount(((Number)payorderObject[0]).intValue());
			vo.setAmount(((Number)payorderObject[1]).doubleValue());
		}
		
		return vo;
		*/
		
		List proposalList = proposalDao.getAgentReferralsCountAtProposal(agentName, start, end);
		List payorderList = payorderDao.getAgentReferralsCountAtPayorder(agentName, start, end);
		
		Map<String ,AgentReferralsVO> users=this.userListToMap(proposalList, payorderList);
		if (users.isEmpty()) {
			return null;
		}
		AgentReferralsVO userTotal=new AgentReferralsVO();
		Set<String> userSet = users.keySet();
		Iterator<String> it = userSet.iterator();
		while(it.hasNext()){
			String key = it.next();
			AgentReferralsVO vo = users.get(key);
			userTotal.setAmount(vo.getMoney());
		}
		userTotal.setPersonCount(userSet.size());
		return userTotal;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AgentReferralsVO> getAgentReferralsList(String agentName,
			Date start, Date end, int pageno, int length) throws Exception {
		// TODO Auto-generated method stub
		int offset=(pageno-1)*length;
		List proposalList = proposalDao.getAgentReferralsDetailAtProposal(agentName, start, end, offset, length);
		List payorderList = payorderDao.getAgentReferralsDetailAtPayorder(agentName, start, end, offset, length);
		
		Map<String ,AgentReferralsVO> users=this.userListToMap(proposalList, payorderList);
		
		List<AgentReferralsVO> userList=new ArrayList<AgentReferralsVO>();
		Iterator<String> it = users.keySet().iterator();
		while(it.hasNext()){
			String key = it.next();
			userList.add(users.get(key));
		}
		
		return userList;
	}
	
	
	@Override
	public Users getUser(String agentFlag, int flagType) throws Exception {
		
		Users user=null;
		if (AgentReferralsStatisticImpl.AGENTNAME==flagType) {
			user=(Users) userDao.get(Users.class, agentFlag);
		}else if (AgentReferralsStatisticImpl.AGENTURL==flagType) {
			user = userDao.getAgentByWebsite2(agentFlag);
		}else{
			user=userDao.getUserByAgcode(agentFlag);
		}
		return user;
	}
	
	
	
	private Map<String ,AgentReferralsVO> userListToMap(List<Object[]> proposalList,List<Object[]> payorderList)throws Exception{
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
		
		return users;
	}
	
	
	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public String getErrormsg() {
		return errormsg;
	}

	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
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
