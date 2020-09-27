package dfh.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import dfh.model.Proposal;
import dfh.model.enums.ProposalFlagType;
import dfh.model.enums.ProposalType;
import dfh.utils.DateUtil;

public class ProposalDao extends UniversalDao {

	private static Logger log = Logger.getLogger(ProposalDao.class);

	public ProposalDao() {
	}

	public boolean existNotAuditedProposal(String loginname, ProposalType type) {
		if (loginname == null || type == null)
			return false;
		DetachedCriteria dCriteria = DetachedCriteria.forClass(Proposal.class);
		dCriteria = dCriteria.add(Restrictions.eq("type", type.getCode())).add(Restrictions.eq("loginname", loginname)).
		add(Restrictions.or(Restrictions.eq("flag", ProposalFlagType.SUBMITED.getCode()),
				Restrictions.eq("flag", ProposalFlagType.AUDITED.getCode())));
		return getHibernateTemplate().findByCriteria(dCriteria).size() > 0;
	}
	
	public boolean existNotMsbankAuditedProposal(String loginname, ProposalType type) {
		if (loginname == null || type == null)
			return false;
		DetachedCriteria d1Criteria = DetachedCriteria.forClass(Proposal.class);
		d1Criteria = d1Criteria.add(Restrictions.eq("type", type.getCode()))
		.add(Restrictions.eq("passflag", 0))
		.add(Restrictions.eq("mstype", 1))
		.add(Restrictions.eq("msflag", 1))
		.add(Restrictions.eq("loginname", loginname))
		.add(Restrictions.eq("flag", ProposalFlagType.CANCLED.getCode()))
		.add(Restrictions.eq("unknowflag", 4));
		int unknowsize  = getHibernateTemplate().findByCriteria(d1Criteria).size();
		
		DetachedCriteria dCriteria = DetachedCriteria.forClass(Proposal.class);
		dCriteria = dCriteria.add(Restrictions.eq("type", type.getCode()))
		.add(Restrictions.eq("passflag", 0))
		.add(Restrictions.eq("mstype", 2))
		.add(Restrictions.eq("msflag", 1))
		.add(Restrictions.eq("loginname", loginname))
		.add(Restrictions.eq("flag", ProposalFlagType.EXCUTED.getCode()));
		int sucsize =  getHibernateTemplate().findByCriteria(dCriteria).size();
		if(unknowsize>0 || sucsize>0){
			return true;
		}else{
			return false;
		}
	}

	public boolean existNotCancledProposal(String loginname, ProposalType type) {
		if (loginname == null || type == null)
			return false;
		DetachedCriteria dCriteria = DetachedCriteria.forClass(Proposal.class);
		dCriteria = dCriteria.add(Restrictions.eq("type", type.getCode())).add(Restrictions.eq("loginname", loginname)).add(Restrictions.ne("flag", ProposalFlagType.CANCLED.getCode()));
		return getHibernateTemplate().findByCriteria(dCriteria).size() > 0;
	}

	public boolean existNotFinishedProposal(String loginname, ProposalType type) {
		if (loginname == null || type == null)
			return false;
		DetachedCriteria dCriteria = DetachedCriteria.forClass(Proposal.class);
		dCriteria = dCriteria.add(Restrictions.eq("type", type.getCode())).add(Restrictions.eq("loginname", loginname)).add(
				Restrictions.or(Restrictions.eq("flag", ProposalFlagType.SUBMITED.getCode()), Restrictions.eq("flag", ProposalFlagType.AUDITED.getCode())));
		return getHibernateTemplate().findByCriteria(dCriteria).size() > 0;
	}
	
	public boolean existHadFinishedProposal(String loginname, ProposalType type) {
		if (loginname == null || type == null)
			return false;
		DetachedCriteria dCriteria = DetachedCriteria.forClass(Proposal.class);
		dCriteria = dCriteria.add(Restrictions.eq("type", type.getCode())).add(Restrictions.eq("loginname", loginname))
		.add(Restrictions.eq("flag", ProposalFlagType.EXCUTED.getCode()));
		return getHibernateTemplate().findByCriteria(dCriteria).size() > 0;
	}
	
	public List<Proposal> getCashoutToday(String loginname) {
		DetachedCriteria dCriteria = DetachedCriteria.forClass(Proposal.class);
		dCriteria = dCriteria.add(Restrictions.eq("type", ProposalType.CASHOUT.getCode())).add(Restrictions.eq("loginname", loginname))
				.add(Restrictions.eq("flag", ProposalFlagType.EXCUTED.getCode())).add(Restrictions.ge("createTime", DateUtil.getToday()));
		return getHibernateTemplate().findByCriteria(dCriteria);
	}

	public boolean existNotFinishedProposal(String loginname, ProposalType type, Integer count) {
		if (loginname == null || type == null)
			return false;
		DetachedCriteria dCriteria = DetachedCriteria.forClass(Proposal.class);
		dCriteria = dCriteria.add(Restrictions.eq("type", type.getCode())).add(Restrictions.eq("loginname", loginname)).add(
				Restrictions.or(Restrictions.eq("flag", ProposalFlagType.SUBMITED.getCode()), Restrictions.eq("flag", ProposalFlagType.AUDITED.getCode())));
		return findByCriteria(dCriteria).size() > count.intValue();
	}
	
	@SuppressWarnings("unchecked")
	public List getAgentReferralsCountAtProposal(String agentName,Date start,Date end)throws Exception{
		String sql="select p.loginname,sum(amount) from proposal p inner join users u on p.loginname=u.loginname where u.agent = ? and p.createtime > ? and p.createtime <= ? and p.type=? and p.flag=? group by p.loginname";
		Query q = this.getSession().createSQLQuery(sql);
		q.setParameter(0, agentName).setParameter(1, start).setParameter(2, end);
		q.setParameter(3, ProposalType.CASHIN.getCode()).setParameter(4, ProposalFlagType.EXCUTED.getCode());
		return q.list();
	}
	
	public List getAgentReferralsCountAtAgProfit(String agentName,Date start,Date end)throws Exception{
		
		String sql="select a.loginname,sum(bettotal) from agprofit a  where a.agent = ? and a.createTime > ? and a.createTime <= ? and a.flag=2 group by a.loginname";
		Query q = this.getSession().createSQLQuery(sql);
		q.setParameter(0, agentName).setParameter(1, start).setParameter(2, end);
		return q.list();
	}

	public List queryAgentAddress(String loginname){
		String sql ="select t.address from agent_address t where t.loginname='"+loginname+"' and t.address not like 'http://www.%' ";
		List list = this.getSession().createSQLQuery(sql).list();
		return list; 
	}
	
	public Double getDoubleValueBySql(String sql, Map<String, Object> params){
		Query query = getSession().createSQLQuery(sql);
		query.setProperties(params);
		Object result = query.uniqueResult();
		return null==result?0.00:(Double)result;
	}
}
