package dfh.dao;

import java.util.List;

import org.apache.log4j.Logger;
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

}
