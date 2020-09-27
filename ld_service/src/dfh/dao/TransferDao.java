package dfh.dao;

import dfh.model.DepositOrder;
import dfh.model.Transfer;
import dfh.remote.RemoteConstant;
import dfh.utils.Arith;
import dfh.utils.Constants;
import dfh.utils.DateUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import java.util.Date;
import java.util.List;
import java.util.Map;


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
		if(StringUtils.isNotEmpty(paymentid)){
			transfer.setSource(paymentid);
		}
		transfer.setId(transID);
		transfer.setLoginname(loginname);
		transfer.setCredit(credit);
		transfer.setRemit(remit);
		transfer.setCreatetime(DateUtil.getCurrentTimestamp());
		transfer.setFlag(Constants.FLAG_TRUE);
		transfer.setRemark(remark != null ? remark : null);
		save(transfer);
		return transfer;
	}
	
	public Transfer addTransferforDt(Long transID, String loginname, Double localCredit, Double remit, Boolean in, Boolean flag,String paymentid, String remark) {
		Transfer transfer = new Transfer();
		if(!paymentid.equals("")||StringUtils.isNotEmpty(paymentid)){
			transfer.setPaymentid(paymentid);
		}
		if (in.booleanValue()) { 
			transfer.setSource(RemoteConstant.WEBSITE);
			transfer.setTarget(RemoteConstant.PAGESITEDT);
			transfer.setNewCredit(Double.valueOf(localCredit.doubleValue() - remit.doubleValue()));
		} else {
			transfer.setSource(RemoteConstant.PAGESITEDT);
			transfer.setTarget(RemoteConstant.WEBSITE);
			transfer.setNewCredit(Double.valueOf(localCredit.doubleValue() + remit.doubleValue()));
		}
		if(StringUtils.isNotEmpty(paymentid)){
			transfer.setSource(paymentid);
		}
		transfer.setId(transID);
		transfer.setLoginname(loginname);
		transfer.setCredit(localCredit);
		transfer.setRemit(remit);
		transfer.setCreatetime(DateUtil.getCurrentTimestamp());
		transfer.setFlag(Constants.FLAG_TRUE);
		transfer.setRemark("转入成功");
		save(transfer);
		return transfer;
	}
	
	public Transfer addTransferforSlot(Long transID, String loginname, Double localCredit, Double remit, Boolean in, Boolean flag,String paymentid, String remark) {
		Transfer transfer = new Transfer();
		if(!paymentid.equals("")||StringUtils.isNotEmpty(paymentid)){
			transfer.setPaymentid(paymentid);
		}
		if (in.booleanValue()) { 
			transfer.setSource(RemoteConstant.WEBSITE);
			transfer.setTarget(RemoteConstant.PAGESITESLOT);
			transfer.setNewCredit(Double.valueOf(localCredit.doubleValue() - remit.doubleValue()));
		} else {
			transfer.setSource(RemoteConstant.PAGESITESLOT);
			transfer.setTarget(RemoteConstant.WEBSITE);
			transfer.setNewCredit(Double.valueOf(localCredit.doubleValue() + remit.doubleValue()));
		}
		if(StringUtils.isNotEmpty(paymentid)){
			transfer.setSource(paymentid);
		}
		transfer.setId(transID);
		transfer.setLoginname(loginname);
		transfer.setCredit(localCredit);
		transfer.setRemit(remit);
		transfer.setCreatetime(DateUtil.getCurrentTimestamp());
		transfer.setFlag(Constants.FLAG_TRUE);
		transfer.setRemark("转入成功");
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
		transfer.setCreatetime(DateUtil.getCurrentTimestamp());
		transfer.setFlag(Constants.FLAG_TRUE);
		transfer.setRemark("转入成功");
		save(transfer);
		return transfer;
	}
	
	public Transfer addTransferforAginDsp(Long transID, String loginname, Double credit, Double remit, Boolean in, Boolean flag,String paymentid, String remark) {
		Transfer transfer = new Transfer();
		if (in.booleanValue()) {
			transfer.setSource(RemoteConstant.WEBSITE);
			transfer.setTarget(RemoteConstant.PAGESITEAGINDSP);
			transfer.setNewCredit(Double.valueOf(credit.doubleValue() - remit.doubleValue()));
		} else {
			transfer.setSource(RemoteConstant.PAGESITEAGINDSP);
			transfer.setTarget(RemoteConstant.WEBSITE);
			transfer.setNewCredit(Double.valueOf(credit.doubleValue() + remit.doubleValue()));
		}
		if(StringUtils.isNotEmpty(paymentid)){
			transfer.setSource(paymentid);
		}
		transfer.setId(transID);
		transfer.setLoginname(loginname);
		transfer.setCredit(credit);
		transfer.setRemit(remit);
		transfer.setCreatetime(DateUtil.getCurrentTimestamp());
		transfer.setFlag(Constants.FLAG_TRUE);
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
		transfer.setCreatetime(DateUtil.getCurrentTimestamp());
		transfer.setFlag(Constants.FLAG_TRUE);
		transfer.setRemark("转入成功");
		save(transfer);
		return transfer;
	}
	
	public Transfer addTransferforNewPt(Long transID, String loginname, Double localCredit, Double remit, Boolean in, Boolean flag,String paymentid, String remark) {
		Transfer transfer = new Transfer();
		if(!paymentid.equals("")||StringUtils.isNotEmpty(paymentid)){
			transfer.setPaymentid(paymentid);
		}
		if (in.booleanValue()) {
			transfer.setSource(RemoteConstant.WEBSITE);
			transfer.setTarget(RemoteConstant.PAGESITENEWPT);
			transfer.setNewCredit(Double.valueOf(localCredit.doubleValue() - remit.doubleValue()));
		} else {
			transfer.setSource(RemoteConstant.PAGESITENEWPT);
			transfer.setTarget(RemoteConstant.WEBSITE);
			transfer.setNewCredit(Double.valueOf(localCredit.doubleValue() + remit.doubleValue()));
		}
		if(StringUtils.isNotEmpty(paymentid)){
			transfer.setSource(paymentid);
		}
		transfer.setId(transID);
		transfer.setLoginname(loginname);
		transfer.setCredit(localCredit);
		transfer.setRemit(remit);
		transfer.setCreatetime(DateUtil.getCurrentTimestamp());
		transfer.setFlag(Constants.FLAG_TRUE);
		transfer.setRemark("转入成功");
		save(transfer);
		return transfer;
	}
	
	public Transfer addTransferINNewPt8Yuan(Long transID, String loginname,Double localCredit,   Double remit, String remark , String target) {
		Transfer transfer = new Transfer();
		transfer.setSource(RemoteConstant.WEBSITE);
		transfer.setTarget(target);
		transfer.setNewCredit(localCredit);
		transfer.setId(transID);
		transfer.setLoginname(loginname);
		transfer.setCredit(localCredit);
		transfer.setRemit(remit);
		transfer.setFlag(0);
		transfer.setCreatetime(DateUtil.getCurrentTimestamp());
		transfer.setRemark(remark != null ? remark : null);
		save(transfer);
		return transfer;
	}
	
	public Transfer addTransferforPt(Long transID, String loginname, Double credit, Double remit, Boolean in, Boolean flag,String paymentid, String remark) {
		Transfer transfer = new Transfer();
		if (in.booleanValue()) {
			transfer.setSource(RemoteConstant.WEBSITE);
			transfer.setTarget(RemoteConstant.PAGESITEPT);
			transfer.setNewCredit(Double.valueOf(credit.doubleValue() - remit.doubleValue()));
		} else {
			transfer.setSource(RemoteConstant.PAGESITEPT);
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
	
	public Transfer addTransferforSky(Long transID, String loginname, Double credit, Double remit, Boolean in, Boolean flag,String paymentid, String remark) {
		Transfer transfer = new Transfer();
		if (in.booleanValue()) {
			transfer.setSource(RemoteConstant.WEBSITE);
			transfer.setTarget(RemoteConstant.PAGESITEPT);
			transfer.setNewCredit(Double.valueOf(credit.doubleValue() - remit.doubleValue()));
		} else {
			transfer.setSource(RemoteConstant.PAGESITEPT);
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
	
	 
	public Transfer addTransferforTt(Long transID, String loginname, Double localCredit, Double remit, Boolean in, Boolean flag,String paymentid, String remark) {
		Transfer transfer = new Transfer();
		if(!paymentid.equals("")||StringUtils.isNotEmpty(paymentid)){
			transfer.setPaymentid(paymentid);
		}
		if (in.booleanValue()) { 
			transfer.setSource(RemoteConstant.WEBSITE);
			transfer.setTarget(RemoteConstant.PAGESITETT);
			transfer.setNewCredit(Double.valueOf(localCredit.doubleValue() - remit.doubleValue()));
		} else {
			transfer.setSource(RemoteConstant.PAGESITETT);
			transfer.setTarget(RemoteConstant.WEBSITE);
			transfer.setNewCredit(Double.valueOf(localCredit.doubleValue() + remit.doubleValue()));
		}
		if(StringUtils.isNotEmpty(paymentid)){
			transfer.setSource(paymentid);
		}
		transfer.setId(transID);
		transfer.setLoginname(loginname);
		transfer.setCredit(localCredit);
		transfer.setRemit(remit);
		transfer.setCreatetime(DateUtil.getCurrentTimestamp());
		transfer.setFlag(Constants.FLAG_TRUE);
		transfer.setRemark("转入成功");
		save(transfer);
		return transfer;
	}
	
	public Transfer addTransferforQt(Long transID, String loginname, Double localCredit, Double remit, Boolean in, Boolean flag,String paymentid, String remark) {
		Transfer transfer = new Transfer();
		if(!paymentid.equals("")||StringUtils.isNotEmpty(paymentid)){
			transfer.setPaymentid(paymentid);
		}
		if (in.booleanValue()) {
			transfer.setSource(RemoteConstant.WEBSITE);
			transfer.setTarget(RemoteConstant.PAGESITEQT);
			transfer.setNewCredit(Double.valueOf(localCredit.doubleValue() - remit.doubleValue()));
		} else {
			transfer.setSource(RemoteConstant.PAGESITEQT);
			transfer.setTarget(RemoteConstant.WEBSITE);
			transfer.setNewCredit(Double.valueOf(localCredit.doubleValue() + remit.doubleValue()));
		}
		if(StringUtils.isNotEmpty(paymentid)){
			transfer.setSource(paymentid);
		}
		transfer.setId(transID);
		transfer.setLoginname(loginname);
		transfer.setCredit(localCredit);
		transfer.setRemit(remit);
		transfer.setCreatetime(DateUtil.getCurrentTimestamp());
		transfer.setFlag(Constants.FLAG_TRUE);
		transfer.setRemark("转入成功");
		save(transfer);
		return transfer;
	}
	
	public Transfer addTransferforSB(Long transID, String loginname, Double credit, Double remit, Boolean in, Boolean flag,String paymentid, String remark) {
		Transfer transfer = new Transfer();
		if (in.booleanValue()) {
			transfer.setSource(RemoteConstant.WEBSITE);
			transfer.setTarget(RemoteConstant.PAGESITESb);
			transfer.setNewCredit(Double.valueOf(credit.doubleValue() - remit.doubleValue()));
		} else {
			transfer.setSource(RemoteConstant.PAGESITESb);
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
		transfer.setCreatetime(DateUtil.getCurrentTimestamp());
		transfer.setFlag(Constants.FLAG_TRUE);
		transfer.setRemark("转入成功");
		save(transfer);
		return transfer;
	}
	
	public Transfer addTransferforKneo2(Long transID, String loginname, Double credit, Double remit, Boolean in, Boolean flag,String paymentid, String remark) {
		Transfer transfer = new Transfer();
		if (in.booleanValue()) {
			transfer.setSource(RemoteConstant.WEBSITE);
			transfer.setTarget(RemoteConstant.PAGESITEKENO2);
			transfer.setNewCredit(Double.valueOf(credit.doubleValue() - remit.doubleValue()));
		} else {
			transfer.setSource(RemoteConstant.PAGESITEKENO2);
			transfer.setTarget(RemoteConstant.WEBSITE);
			transfer.setNewCredit(Double.valueOf(credit.doubleValue() + remit.doubleValue()));
		}
		transfer.setId(transID);
		transfer.setLoginname(loginname);
		transfer.setCredit(credit);
		transfer.setRemit(remit);
		transfer.setCreatetime(DateUtil.getCurrentTimestamp());
		transfer.setFlag(Constants.FLAG_TRUE);
		transfer.setRemark("转入成功");
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
	

	public Transfer addTransferForNewPT(Long transID, String loginname, Double credit, Double remit, Boolean in, Boolean flag,String paymentid, String remark) {
		Transfer transfer = new Transfer();
		if (in.booleanValue()) {
			transfer.setSource(RemoteConstant.WEBSITE);
			transfer.setTarget(RemoteConstant.PAGESITENEWPT);
			transfer.setNewCredit(Double.valueOf(credit.doubleValue() - remit.doubleValue()));
		} else {
			transfer.setSource(RemoteConstant.PAGESITENEWPT);
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
	
	public Transfer addTransferforSixLottery(Long transID, String loginname, Double credit, Double remit, Boolean in, Boolean flag,String paymentid, String remark) {
		Transfer transfer = new Transfer();
		if (in.booleanValue()) {
			transfer.setSource(RemoteConstant.WEBSITE);
			transfer.setTarget(RemoteConstant.PAGESITESIXLOTTERY);
			transfer.setNewCredit(Double.valueOf(credit.doubleValue() - remit.doubleValue()));
		} else {
			transfer.setSource(RemoteConstant.PAGESITESIXLOTTERY);
			transfer.setTarget(RemoteConstant.WEBSITE);
			transfer.setNewCredit(Double.valueOf(credit.doubleValue() + remit.doubleValue()));
		}
		transfer.setId(transID);
		transfer.setLoginname(loginname);
		transfer.setCredit(credit);
		transfer.setRemit(remit);
		transfer.setCreatetime(DateUtil.getCurrentTimestamp());
		transfer.setFlag(Constants.FLAG_TRUE);
		transfer.setRemark("转入成功");
		save(transfer);
		return transfer;
	}

	public Transfer addTransferforJc(Long transID, String loginname, Double credit, Double remit, Boolean in, String remark) {
		Transfer transfer = new Transfer();
		if (in.booleanValue()) {
			transfer.setSource(RemoteConstant.WEBSITE);
			transfer.setTarget(RemoteConstant.PAGESITEJC);
			transfer.setNewCredit(Double.valueOf(credit.doubleValue() - remit.doubleValue()));
		} else {
			transfer.setSource(RemoteConstant.PAGESITEJC);
			transfer.setTarget(RemoteConstant.WEBSITE);
			transfer.setNewCredit(Double.valueOf(credit.doubleValue() + remit.doubleValue()));
		}
		transfer.setId(transID);
		transfer.setLoginname(loginname);
		transfer.setCredit(credit);
		transfer.setRemit(remit);
		transfer.setCreatetime(DateUtil.getCurrentTimestamp());
		transfer.setFlag(Constants.FLAG_TRUE);
		transfer.setRemark(remark);
		save(transfer);
		return transfer;
	}
	
	public Transfer addTransferforNT(Long transID, String loginname, Double credit, Double remit, Boolean in,String paymentid,  String remark) {
		Transfer transfer = new Transfer();
		if(!paymentid.equals("")||StringUtils.isNotEmpty(paymentid)){
			transfer.setPaymentid(paymentid);
		}
		if (in.booleanValue()) {
			transfer.setSource(RemoteConstant.WEBSITE);
			transfer.setTarget(RemoteConstant.PAGESITENT);
			transfer.setNewCredit(Double.valueOf(credit.doubleValue() - remit.doubleValue()));
		} else {
			transfer.setSource(RemoteConstant.PAGESITENT);
			transfer.setTarget(RemoteConstant.WEBSITE);
			transfer.setNewCredit(Double.valueOf(credit.doubleValue() + remit.doubleValue()));
		}
		if(StringUtils.isNotEmpty(paymentid)){
			transfer.setSource(paymentid);
		}
		transfer.setId(transID);
		transfer.setLoginname(loginname);
		transfer.setCredit(credit);
		transfer.setRemit(remit);
		transfer.setCreatetime(DateUtil.getCurrentTimestamp());
		transfer.setFlag(Constants.FLAG_TRUE);
		transfer.setRemark(remark);
		save(transfer);
		return transfer;
	}
	
	public Transfer addTransferforMG(Long transID, String loginname, Double credit, Double remit, Boolean in,String paymentid, String remark) {
		Transfer transfer = new Transfer();
		if (in.booleanValue()) {
			transfer.setSource(RemoteConstant.WEBSITE);
			transfer.setTarget(RemoteConstant.PAGESITEMG);
			transfer.setNewCredit(Double.valueOf(credit.doubleValue() - remit.doubleValue()));
		} else {
			transfer.setSource(RemoteConstant.PAGESITEMG);
			transfer.setTarget(RemoteConstant.WEBSITE);
			transfer.setNewCredit(Double.valueOf(credit.doubleValue() + remit.doubleValue()));
		}
		if(StringUtils.isNotEmpty(paymentid)){
			transfer.setSource(paymentid);
		}
		transfer.setId(transID);
		transfer.setLoginname(loginname);
		transfer.setCredit(credit);
		transfer.setRemit(remit);
		transfer.setCreatetime(DateUtil.getCurrentTimestamp());
		transfer.setFlag(Constants.FLAG_TRUE);
		transfer.setRemark(remark);
		save(transfer);
		return transfer;
	}
	
	public Transfer addTransferforDT(Long transID, String loginname, Double credit, Double remit, Boolean in, String remark) {
		Transfer transfer = new Transfer();
		if (in.booleanValue()) {
			transfer.setSource(RemoteConstant.WEBSITE);
			transfer.setTarget(RemoteConstant.PAGESITEDT);
			transfer.setNewCredit(Double.valueOf(credit.doubleValue() - remit.doubleValue()));
		} else {
			transfer.setSource(RemoteConstant.PAGESITEDT);
			transfer.setTarget(RemoteConstant.WEBSITE);
			transfer.setNewCredit(Double.valueOf(credit.doubleValue() + remit.doubleValue()));
		}
		transfer.setId(transID);
		transfer.setLoginname(loginname);
		transfer.setCredit(credit);
		transfer.setRemit(remit);
		transfer.setCreatetime(DateUtil.getCurrentTimestamp());
		transfer.setFlag(Constants.FLAG_TRUE);
		transfer.setRemark(remark);
		save(transfer);
		return transfer;
	}

	/**
	 * 使用原生sql, 获取double值
	 * @return
	 */
	public Double getDoubleValueBySql(String sql, Map<String, Object> params){
		//Query query = getSession().createSQLQuery("select SUM(bet) from platform_data where loginname=? and starttime>=?");
		//query.setParameter(0, startTime);
		Query query = getSession().createSQLQuery(sql);
		query.setProperties(params);
		Object result = query.uniqueResult();
		return null==result?0.00:(Double)result;
	}
	
	public Transfer addTransferforEbet(Long transID, String loginname, Double credit, Double remit, Boolean in, Boolean flag, String paymentid, String remark) {
		Transfer transfer = new Transfer();
		if (in.booleanValue()) {
			transfer.setSource(RemoteConstant.WEBSITE);
			transfer.setTarget(RemoteConstant.PAGESITEEBET);
			transfer.setNewCredit(Double.valueOf(credit.doubleValue() - remit.doubleValue()));
		} else {
			transfer.setSource(RemoteConstant.PAGESITEEBET);
			transfer.setTarget(RemoteConstant.WEBSITE);
			transfer.setNewCredit(Double.valueOf(credit.doubleValue() + remit.doubleValue()));
		}
		transfer.setId(transID);
		transfer.setLoginname(loginname);
		transfer.setCredit(credit);
		transfer.setRemit(remit);
		transfer.setCreatetime(DateUtil.getCurrentTimestamp());
		transfer.setFlag(Constants.FLAG_TRUE);
		transfer.setRemark("转入成功");
		save(transfer);
		return transfer;
	}
	
	public Transfer addTransferforGPI(Long transID, String loginname, Double credit, Double remit, Boolean in, Boolean flag,String paymentid, String remark) {
		Transfer transfer = new Transfer();
		if (in.booleanValue()) {
			transfer.setSource(RemoteConstant.WEBSITE);
			transfer.setTarget(RemoteConstant.PAGESITEGPI);
			transfer.setNewCredit(Double.valueOf(credit.doubleValue() - remit.doubleValue()));
		} else {
			transfer.setSource(RemoteConstant.PAGESITEGPI);
			transfer.setTarget(RemoteConstant.WEBSITE);
			transfer.setNewCredit(Double.valueOf(credit.doubleValue() + remit.doubleValue()));
		}
		transfer.setId(transID);
		transfer.setLoginname(loginname);
		transfer.setCredit(credit);
		transfer.setRemit(remit);
		transfer.setCreatetime(DateUtil.getCurrentTimestamp());
		transfer.setFlag(Constants.FLAG_TRUE);
		transfer.setRemark("转入成功");
		save(transfer);
		return transfer;
	}
		  
	public Transfer addTransferforAgent(Long transID, String loginname, Double credit, Double remit, Boolean in) {
		Transfer transfer = new Transfer();
		if (in) {
			//基于游戏账号
			transfer.setSource(RemoteConstant.AGENT);
			transfer.setTarget(RemoteConstant.WEBSITE);
			transfer.setNewCredit(Arith.add(credit, remit));
			transfer.setRemit(remit);
			transfer.setRemark("游戏账号转入成功");
		} else {
			//基于代理账号
			transfer.setSource(RemoteConstant.AGENT);
			transfer.setTarget(RemoteConstant.WEBSITE);
			transfer.setNewCredit(Arith.sub(credit, remit));
			transfer.setRemit(-remit);
			transfer.setRemark("代理账号转出成功");
		}
		transfer.setId(transID);
		transfer.setLoginname(loginname);
		transfer.setCredit(credit);
		transfer.setCreatetime(DateUtil.getCurrentTimestamp());
		transfer.setFlag(Constants.FLAG_TRUE);
		
		save(transfer);
		return transfer;
	}
	
	public Transfer addTransferNTwo(Long transID, String loginname, Double credit, Double remit, Boolean in, Boolean flag,String paymentid, String remark) {
		Transfer transfer = new Transfer();
		if (in.booleanValue()) {
			transfer.setSource(RemoteConstant.WEBSITE);
			transfer.setTarget(RemoteConstant.PAGESITENTWO);
			transfer.setNewCredit(Double.valueOf(credit.doubleValue() - remit.doubleValue()));
		} else {
			transfer.setSource(RemoteConstant.PAGESITENTWO);
			transfer.setTarget(RemoteConstant.WEBSITE);
			transfer.setNewCredit(Double.valueOf(credit.doubleValue() + remit.doubleValue()));
		}
		transfer.setId(transID);
		transfer.setLoginname(loginname);
		transfer.setCredit(credit);
		transfer.setRemit(remit);
		transfer.setCreatetime(DateUtil.getCurrentTimestamp());
		transfer.setFlag(Constants.FLAG_TRUE);
		transfer.setRemark("转入成功");
		save(transfer);
		return transfer;
	}

	public Transfer addTransferForEBetApp(Long transID, String loginname, Double credit, Double remit, Boolean in, String remark) {
		return addTransferFor(RemoteConstant.PAGESITEEBETAPP,transID,loginname,credit,remit,in,remark);
	}

	private Transfer addTransferFor(String platform, Long transID, String loginname, Double credit, Double remit, Boolean in, String remark) {
		Transfer transfer = new Transfer();
		if (in.booleanValue()) {
			transfer.setSource(RemoteConstant.WEBSITE);
			transfer.setTarget(platform);
			transfer.setNewCredit(Double.valueOf(credit.doubleValue() - remit.doubleValue()));
		} else {
			transfer.setSource(platform);
			transfer.setTarget(RemoteConstant.WEBSITE);
			transfer.setNewCredit(Double.valueOf(credit.doubleValue() + remit.doubleValue()));
		}
		transfer.setId(transID);
		transfer.setLoginname(loginname);
		transfer.setCredit(credit);
		transfer.setRemit(remit);
		transfer.setCreatetime(DateUtil.getCurrentTimestamp());
		transfer.setFlag(Constants.FLAG_TRUE);
		transfer.setRemark(remark);
		save(transfer);
		return transfer;
	}

	public Transfer addTransferforMg(Long transID, String loginname, Double localCredit, Double remit, Boolean in, Boolean flag,String paymentid, String remark) {
		Transfer transfer = new Transfer();
		if(!paymentid.equals("")||StringUtils.isNotEmpty(paymentid)){
			transfer.setPaymentid(paymentid);
		}
		if (in.booleanValue()) {
			transfer.setSource(RemoteConstant.WEBSITE);
			transfer.setTarget(RemoteConstant.PAGESITEMG);
			transfer.setNewCredit(Double.valueOf(localCredit.doubleValue() - remit.doubleValue()));
		} else {
			transfer.setSource(RemoteConstant.PAGESITEMG);
			transfer.setTarget(RemoteConstant.WEBSITE);
			transfer.setNewCredit(Double.valueOf(localCredit.doubleValue() + remit.doubleValue()));
		}
		transfer.setId(transID);
		transfer.setLoginname(loginname);
		transfer.setCredit(localCredit);
		transfer.setRemit(remit);
		transfer.setCreatetime(DateUtil.getCurrentTimestamp());
		transfer.setFlag(Constants.FLAG_TRUE);
		transfer.setRemark("转入成功");
		save(transfer);
		return transfer;
	}
	public Transfer addTransferforSba(Long transID, String loginname, Double localCredit, Double remit, Boolean in, Boolean flag,String paymentid, String remark) {
		Transfer transfer = new Transfer();
		if (in.booleanValue()) {
			transfer.setSource(RemoteConstant.WEBSITE);
			transfer.setTarget(RemoteConstant.PAGESITESBA);
			transfer.setNewCredit(Double.valueOf(localCredit.doubleValue() - remit.doubleValue()));
		} else {
			transfer.setSource(RemoteConstant.PAGESITESBA);
			transfer.setTarget(RemoteConstant.WEBSITE);
			transfer.setNewCredit(Double.valueOf(localCredit.doubleValue() + remit.doubleValue()));
		}
		transfer.setId(transID);
		transfer.setLoginname(loginname);
		transfer.setCredit(localCredit);
		transfer.setRemit(remit);
		transfer.setCreatetime(DateUtil.getCurrentTimestamp());
		transfer.setFlag(Constants.FLAG_TRUE);
		transfer.setRemark("转入成功");
		save(transfer);
		return transfer;
	}
	public Transfer addTransferforPng(Long transID, String loginname, Double localCredit, Double remit, Boolean in, Boolean flag,String paymentid, String remark) {
		Transfer transfer = new Transfer();
		if (in.booleanValue()) {
			transfer.setSource(RemoteConstant.WEBSITE);
			transfer.setTarget(RemoteConstant.PAGESITEPNG);
			transfer.setNewCredit(Double.valueOf(localCredit.doubleValue() - remit.doubleValue()));
		} else {
			transfer.setSource(RemoteConstant.PAGESITEPNG);
			transfer.setTarget(RemoteConstant.WEBSITE);
			transfer.setNewCredit(Double.valueOf(localCredit.doubleValue() + remit.doubleValue()));
		}
		if(StringUtils.isNotEmpty(paymentid)){
			transfer.setSource(paymentid);
		}
		transfer.setId(transID);
		transfer.setLoginname(loginname);
		transfer.setCredit(localCredit);
		transfer.setRemit(remit);
		transfer.setCreatetime(DateUtil.getCurrentTimestamp());
		transfer.setFlag(Constants.FLAG_TRUE);
		transfer.setRemark("转入成功");
		save(transfer);
		return transfer;
	}
	
	/**
	 * 微信秒存 获取玩家未支付的订单的数量
	 * @param userName
	 * @return
	 */
	
	public Integer getUnDepositOrderCount(DepositOrder payOrder) {
		Criteria criteria = getSession().createCriteria(DepositOrder.class);	
		criteria.add(Restrictions.or(Restrictions.eq("amount", payOrder.getAmount()), Restrictions.eq("loginname", payOrder.getLoginname())));
		criteria.add(Restrictions.eq("status", 0));  //未支付状态
		criteria.add(Restrictions.eq("type", "3"));  //微信
		System.out.println((Integer) criteria.setProjection(Projections.rowCount()).uniqueResult());
		return (Integer) criteria.setProjection(Projections.rowCount()).uniqueResult();
     }
	
	
	/**
	 *  保存微信秒存订单
	 * @param sql
	 * @return
	 */
	public Integer saveDepositPayOrder(String sql){
		Query insertQuery = this.getSession().createSQLQuery(sql);
		int numSize = insertQuery.executeUpdate() ;
		System.out.println("此次添加微信秒存处理数为："+numSize);
		return numSize;
	}
	
	
	/**
	 * 查询最近几天的转账记录
	 * @param loginname
	 * @param startDate
	 * @return
	 * @throws Exception
	 */
	public List<Transfer> findTransferList(String loginname,Date startDate)throws Exception{
		Criteria c = this.getSession().createCriteria(Transfer.class);
		c.add(Restrictions.ge("createtime", startDate));
		c.add(Restrictions.eq("loginname", loginname));
		return c.list();
	}
}
