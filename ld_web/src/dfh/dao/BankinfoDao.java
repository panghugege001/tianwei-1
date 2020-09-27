package dfh.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import dfh.model.BankCreditlogs;
import dfh.model.Bankinfo;
import dfh.utils.DateUtil;

public class BankinfoDao extends UniversalDao{
	
	public Integer getCount(String username,Integer type,String bankname) {
		
		DetachedCriteria dc = DetachedCriteria.forClass(Bankinfo.class);
		if (username!=null &&!username.equals("")) {
			dc.add(Restrictions.eq("username", username));
		}
		if (type !=null && type!=0) {
			dc.add(Restrictions.eq("type", type));
		}
		if (bankname!=null &&!bankname.equals("")) {
			dc.add(Restrictions.eq("bankname", bankname));
		}
		dc.setProjection(Projections.rowCount());
		return (Integer) getHibernateTemplate().findByCriteria(dc).get(0);
	}
	
	public List findAll(String username,Integer type,String bankname,int offset,int length)throws Exception{
		Criteria c = this.getSession().createCriteria(Bankinfo.class);
		if (username!=null &&!username.equals("")) {
			c.add(Restrictions.eq("username", username));
		}
		if (type !=null && type!=0) {
			c.add(Restrictions.eq("type", type));
		}
		if (bankname!=null &&!bankname.equals("")) {
			c.add(Restrictions.eq("bankname", bankname));
		}
		return c.setFirstResult(offset).setMaxResults(length).list();
	}
	
	/*
	 * 根据id调整取款账户的余额
	 */
	public void changeAmount(String id,Integer type,Integer useable,double amount){
		try {
			Bankinfo bankinfo = (Bankinfo)getHibernateTemplate().get(Bankinfo.class, Integer.parseInt(id));
			bankinfo.setAmount(bankinfo.getAmount()+amount);
			this.update(bankinfo);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
	
	/*
	 * 根据银行用户们调整存款账户的余额
	 */

	public void changeAmountByName(String username,double amount){
		DetachedCriteria c = DetachedCriteria.forClass(Bankinfo.class);
		c.add(Restrictions.eq("username", username));
		c.add(Restrictions.eq("type", 1));
		c.add(Restrictions.eq("useable", 0));
		
		List<Bankinfo> bankinfos=getHibernateTemplate().findByCriteria(c);
		Bankinfo bankinfo=null;
		
		
		try {
			if (bankinfos.size()!=0) {
				bankinfo = bankinfos.get(0);
				bankinfo.setAmount(bankinfo.getAmount()+amount);
				this.update(bankinfo);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
	
	/*
	 * 根据银行类型调整在线存款账户的余额
	 */

	public void changeAmountOnline(Integer type,double amount,String pno){
		DetachedCriteria c = DetachedCriteria.forClass(Bankinfo.class);
		c.add(Restrictions.eq("type", type));
		c.add(Restrictions.eq("useable", 0));
		
		List<Bankinfo> bankinfos=getHibernateTemplate().findByCriteria(c);
		Bankinfo bankinfo=null;
		
		
		try {
			if (bankinfos.size()!=0) {
				bankinfo = bankinfos.get(0);
				Double credit = bankinfo.getAmount();
				bankinfo.setAmount(bankinfo.getAmount()+amount*99.4/100);
				this.update(bankinfo);
				
				//添加银行额度流水
				BankCreditlogs bankCreditlogs = new BankCreditlogs();
				bankCreditlogs.setCreatetime(DateUtil.getCurrentTimestamp());
				bankCreditlogs.setBankname(bankinfo.getUsername());
				bankCreditlogs.setType(dfh.model.enums.BankCreditChangeType.NETPAY.getCode());
				bankCreditlogs.setNewCredit(credit+amount);
				bankCreditlogs.setCredit(credit);
				bankCreditlogs.setRemit(amount);
				bankCreditlogs.setRemark("referenceNo:"+pno);
				save(bankCreditlogs);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
	
	
	/*
	 * 根据银行用户们调整存款账户的余额,为手工修改银行额度设计
	 */

	public String changeBankCreditManual(String username,double amount){
		String msg=null;
		DetachedCriteria c = DetachedCriteria.forClass(Bankinfo.class);
		c.add(Restrictions.eq("username", username));
		c.add(Restrictions.eq("useable", 0));
		
		List<Bankinfo> bankinfos=getHibernateTemplate().findByCriteria(c);
		Bankinfo bankinfo=null;
		
		
		try {
			if (bankinfos.size()!=0) {
				bankinfo = bankinfos.get(0);
				bankinfo.setAmount(bankinfo.getAmount()+amount);
				this.update(bankinfo);
			}else {
				msg="The bank account doesn’t exist,please check it again";
				return msg;
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		return msg;
	}

}
