package dfh.dao;


import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import dfh.model.MemberSignrecord;
import dfh.model.Payorder;
import dfh.model.Proposal;
import dfh.model.Users;

public class MemberSignrecordDao extends UniversalDao {
	
	public void login(MemberSignrecord member){
		this.saveOrUpdate(member);
	}
	
	public boolean islogined(String username){
		MemberSignrecord o = (MemberSignrecord) this.get(MemberSignrecord.class, username);
		if (o==null) {
			return false;
		}
		return o.getFlag().intValue()==1?false:true;
	}
	
	public void logout(MemberSignrecord member){
		this.saveOrUpdate(member);
	}

	public Integer getPayOrderCountByUser(Users user){
		Criteria criteria = getSession().createCriteria(Payorder.class);
		criteria.add(Restrictions.eq("loginname", user.getLoginname()));    
		criteria.add(Restrictions.eq("type", 0));   //支付成功的订单
		return (Integer) criteria.setProjection(Projections.rowCount()).uniqueResult();
	}
	
	public Integer getDepositProposalCountByUser(Users user) {
		Criteria criteria = getSession().createCriteria(Proposal.class);
		criteria.add(Restrictions.eq("loginname", user.getLoginname()));  
		criteria.add(Restrictions.eq("type", 502));   //支付成功的订单
		criteria.add(Restrictions.eq("flag", 2));   //已执行状态
		return (Integer) criteria.setProjection(Projections.rowCount()).uniqueResult();
	}
}
