package dfh.dao;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.LockMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import dfh.model.Transfer;
import dfh.remote.RemoteConstant;
import dfh.utils.Constants;
import dfh.utils.DateUtil;


public class TransferDao extends UniversalDao {

	private static Logger log = Logger.getLogger(TransferDao.class);

	public TransferDao() {
	}

	public Transfer addTransfer(Long transID, String loginname, Double credit, Double remit, Boolean in, Boolean flag,String paymentid, String remark) {
		Transfer transfer = new Transfer();
		if (in.booleanValue()) {
			transfer.setSource(RemoteConstant.WEBSITE);
			transfer.setTarget(RemoteConstant.PAGESITE);
			transfer.setNewCredit(Double.valueOf(credit.doubleValue() - remit.doubleValue()));
		} else {
			transfer.setSource(RemoteConstant.PAGESITE);
			transfer.setTarget(RemoteConstant.WEBSITE);
			transfer.setNewCredit(Double.valueOf(credit.doubleValue() + remit.doubleValue()));
		}
		transfer.setId(transID);
		transfer.setLoginname(loginname);
		transfer.setCredit(credit);
		transfer.setRemit(remit);
		transfer.setFlag(flag.booleanValue() ? Constants.FLAG_TRUE : Constants.FLAG_FALSE);
		transfer.setCreatetime(DateUtil.getCurrentTimestamp());
		transfer.setRemark(remark != null ? remark : null);
		save(transfer);
		return transfer;
	}
	
	public Transfer addTransferforDsp(Long transID, String loginname, Double credit, Double remit, Boolean in, Boolean flag,String paymentid, String remark) {
		Transfer transfer = new Transfer();
		if (in.booleanValue()) {
			transfer.setSource(RemoteConstant.WEBSITE);
			transfer.setTarget(RemoteConstant.PAGESITEDSP);
			transfer.setNewCredit(Double.valueOf(credit.doubleValue() - remit.doubleValue()));
		} else {
			transfer.setSource(RemoteConstant.PAGESITEDSP);
			transfer.setTarget(RemoteConstant.WEBSITE);
			transfer.setNewCredit(Double.valueOf(credit.doubleValue() + remit.doubleValue()));
		}
		transfer.setId(transID);
		transfer.setLoginname(loginname);
		transfer.setCredit(credit);
		transfer.setRemit(remit);
		transfer.setFlag(flag.booleanValue() ? Constants.FLAG_TRUE : Constants.FLAG_FALSE);
		transfer.setCreatetime(DateUtil.getCurrentTimestamp());
		transfer.setRemark(remark != null ? remark : null);
		save(transfer);
		return transfer;
	}
	
	public Transfer addTransferforBbin(Long transID, String loginname, Double credit, Double remit, Boolean in, Boolean flag,String paymentid, String remark) {
		Transfer transfer = new Transfer();
		if (in.booleanValue()) {
			transfer.setSource(RemoteConstant.WEBSITE);
			transfer.setTarget(RemoteConstant.PAGESITEBbin);
			transfer.setNewCredit(Double.valueOf(credit.doubleValue() - remit.doubleValue()));
		} else {
			transfer.setSource(RemoteConstant.PAGESITEBbin);
			transfer.setTarget(RemoteConstant.WEBSITE);
			transfer.setNewCredit(Double.valueOf(credit.doubleValue() + remit.doubleValue()));
		}
		transfer.setId(transID);
		transfer.setLoginname(loginname);
		transfer.setCredit(credit);
		transfer.setRemit(remit);
		transfer.setFlag(0);
		transfer.setCreatetime(DateUtil.getCurrentTimestamp());
		transfer.setRemark(remark != null ? remark : null);
		save(transfer);
		return transfer;
	}
	
	public Transfer addTransferforBok(Long transID, String loginname, Double credit, Double remit, Boolean in, Boolean flag,String paymentid, String remark) {
		Transfer transfer = new Transfer();
		if (in.booleanValue()) {
			transfer.setSource(RemoteConstant.WEBSITE);
			transfer.setTarget(RemoteConstant.PAGESITEBok);
			transfer.setNewCredit(Double.valueOf(credit.doubleValue() - remit.doubleValue()));
		} else {
			transfer.setSource(RemoteConstant.PAGESITEBok);
			transfer.setTarget(RemoteConstant.WEBSITE);
			transfer.setNewCredit(Double.valueOf(credit.doubleValue() + remit.doubleValue()));
		}
		transfer.setId(transID);
		transfer.setLoginname(loginname);
		transfer.setCredit(credit);
		transfer.setRemit(remit);
		transfer.setFlag(0);
		transfer.setCreatetime(DateUtil.getCurrentTimestamp());
		transfer.setRemark(remark != null ? remark : null);
		save(transfer);
		return transfer;
	}

	public Transfer addTransferforKneo(Long transID, String loginname, Double credit, Double remit, Boolean in, Boolean flag,String paymentid, String remark) {
		Transfer transfer = new Transfer();
		if (in.booleanValue()) {
			transfer.setSource(RemoteConstant.WEBSITE);
			transfer.setTarget(RemoteConstant.PAGESITEKENO);
			transfer.setNewCredit(Double.valueOf(credit.doubleValue() - remit.doubleValue()));
		} else {
			transfer.setSource(RemoteConstant.PAGESITEKENO);
			transfer.setTarget(RemoteConstant.WEBSITE);
			transfer.setNewCredit(Double.valueOf(credit.doubleValue() + remit.doubleValue()));
		}
		transfer.setId(transID);
		transfer.setLoginname(loginname);
		transfer.setCredit(credit);
		transfer.setRemit(remit);
		transfer.setFlag(flag.booleanValue() ? Constants.FLAG_TRUE : Constants.FLAG_FALSE);
		transfer.setCreatetime(DateUtil.getCurrentTimestamp());
		transfer.setRemark(remark != null ? remark : null);
		save(transfer);
		return transfer;
	}
	
	public void departedTranfer(Integer id, String remark) {
		Transfer transfer = (Transfer) get(Transfer.class, id, LockMode.UPGRADE);
		if (transfer != null) {
			transfer.setFlag(Constants.FLAG_DEPARTED);
			transfer.setRemark(remark != null ? (new StringBuilder(String.valueOf(transfer.getRemark()))).append(";").append(remark).toString() : transfer.getRemark());
			update(transfer);
			log.info("departed tranfer successfully");
		} else {
			log.error((new StringBuilder("not found this tranfer record:id[")).append(id).append("]").toString());
		}
	}

	public Transfer getFailTranfer(String loginname, boolean in) {
		DetachedCriteria dCriteria = DetachedCriteria.forClass(Transfer.class);
		dCriteria = dCriteria.add(Restrictions.eq("loginname", loginname)).add(Restrictions.eq("flag", Constants.FLAG_FALSE));
		if (in)
			dCriteria = dCriteria.add(Restrictions.eq("source", RemoteConstant.WEBSITE));
		else
			dCriteria = dCriteria.add(Restrictions.eq("source", RemoteConstant.PAGESITE));
		dCriteria = dCriteria.addOrder(Order.desc("createtime"));
		List list = getHibernateTemplate().findByCriteria(dCriteria);
		if (list.size() > 0)
			return (Transfer) list.get(0);
		else
			return null;
	}

	public Transfer getLastTransfer(String loginname, Boolean isIn) {
		if (StringUtils.isEmpty(loginname) || isIn == null)
			return null;

		DetachedCriteria dc = DetachedCriteria.forClass(Transfer.class).add(Restrictions.eq("loginname", loginname)).add(Restrictions.eq("source", isIn ? RemoteConstant.WEBSITE : RemoteConstant.PAGESITE))
				.addOrder(Order.desc("createtime"));
		List<Transfer> list = getHibernateTemplate().findByCriteria(dc,0,1);
//		System.out.println(list.size()+";"+list.get(0).getId()+";"+list.get(list.size()-1).getId());
		if (list != null && list.size() > 0)
			return list.get(0);
		else
			return null;
	}

	
	public void repairTransfer(Integer id, String remark) {
		Transfer transfer = (Transfer) get(Transfer.class, id, LockMode.UPGRADE);
		if (transfer != null) {
			transfer.setFlag(Constants.FLAG_TRUE);
			transfer.setRemark(remark != null ? (new StringBuilder(String.valueOf(transfer.getRemark()))).append(";").append(remark).toString() : transfer.getRemark());
			update(transfer);
			log.info("repair tranfer successfully");
		} else {
			log.error((new StringBuilder("not found this tranfer record:id[")).append(id).append("]").toString());
		}
	}

}
