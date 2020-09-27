package dfh.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import dfh.model.BankCreditlogs;
import dfh.model.Bankinfo;
import dfh.model.enums.CommonGfbEnum;
import dfh.model.enums.CommonZfEnum;
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
//				bankinfo.setAmount(credit+amount*99.4/100);
//				this.update(bankinfo);
				
				//添加银行额度流水
				BankCreditlogs bankCreditlogs = new BankCreditlogs();
				bankCreditlogs.setCreatetime(DateUtil.getCurrentTimestamp());
				bankCreditlogs.setBankname(bankinfo.getUsername());
				bankCreditlogs.setType(dfh.model.enums.BankCreditChangeType.NETPAY.getCode());
				bankCreditlogs.setNewCredit(credit+amount*99.4/100);
				bankCreditlogs.setCredit(credit);
				bankCreditlogs.setRemit(amount*99.4/100);
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
	 * Zf根据银行类型调整在线存款账户的余额
	 */

	public void changeZfAmountOnline(Integer type,double amount,String pno){
		DetachedCriteria c = DetachedCriteria.forClass(Bankinfo.class);
		c.add(Restrictions.eq("type", type));
		c.add(Restrictions.eq("useable", 0));
		
		List<Bankinfo> bankinfos=getHibernateTemplate().findByCriteria(c);
		Bankinfo bankinfo=null;
		
		
		try {
			if (bankinfos.size()!=0) {
				bankinfo = bankinfos.get(0);
				Double credit = bankinfo.getAmount();
				//bankinfo.setAmount(credit+amount*99.4/100);
				//this.update(bankinfo);
				
				//添加银行额度流水
				BankCreditlogs bankCreditlogs = new BankCreditlogs();
				bankCreditlogs.setCreatetime(DateUtil.getCurrentTimestamp());
				bankCreditlogs.setBankname(bankinfo.getUsername());
				bankCreditlogs.setType(dfh.model.enums.BankCreditChangeType.NETPAY.getCode());
				bankCreditlogs.setNewCredit(credit+amount*99.4/100);
				bankCreditlogs.setCredit(credit);
				bankCreditlogs.setRemit(amount*99.4/100);
				bankCreditlogs.setRemark("referenceNo:"+pno);
				save(bankCreditlogs);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
	
	
	/*****
	 * 海尔支付
	 * @param type
	 * @param amount
	 * @param pno
	 */
		
	public void changeHaierAmountOnline(Integer type,double amount,String pno){
		DetachedCriteria c = DetachedCriteria.forClass(Bankinfo.class);
		c.add(Restrictions.eq("type", type));
		c.add(Restrictions.eq("useable", 0));
		
		List<Bankinfo> bankinfos=getHibernateTemplate().findByCriteria(c);
		Bankinfo bankinfo=null;
		try {
			if (bankinfos.size()!=0) {
				bankinfo = bankinfos.get(0);
				Double credit = bankinfo.getAmount();
				//bankinfo.setAmount(bankinfo.getAmount()+amount*99.5/100);
				//this.update(bankinfo);
				//添加银行额度流水
				BankCreditlogs bankCreditlogs = new BankCreditlogs();
				bankCreditlogs.setCreatetime(DateUtil.getCurrentTimestamp());
				bankCreditlogs.setBankname(bankinfo.getUsername());
				bankCreditlogs.setType(dfh.model.enums.BankCreditChangeType.NETPAY.getCode());
				bankCreditlogs.setNewCredit(credit+amount*99.5/100);
				bankCreditlogs.setCredit(credit);
				bankCreditlogs.setRemit(amount*99.5/100);
				bankCreditlogs.setRemark("referenceNo:"+pno);
				save(bankCreditlogs);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
	
	
	
	
	public void changeZfDianKaAmountOnline(Integer type,double amount,String pno){
		DetachedCriteria c = DetachedCriteria.forClass(Bankinfo.class);
		c.add(Restrictions.eq("type", type));
		c.add(Restrictions.eq("useable", 0));
		
		List<Bankinfo> bankinfos=getHibernateTemplate().findByCriteria(c);
		Bankinfo bankinfo=null;
		try {
			if (bankinfos.size()!=0) {
				bankinfo = bankinfos.get(0);
				Double credit = bankinfo.getAmount();
				//bankinfo.setAmount(credit+amount);
				//this.update(bankinfo);
				
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
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
	
	/*
	 * Zf根据银行类型调整在线存款账户的余额
	 */

	public void changeZfAmountOnlineTwo(Integer type,double amount,String pno){
		DetachedCriteria c = DetachedCriteria.forClass(Bankinfo.class);
		c.add(Restrictions.eq("type", type));
		c.add(Restrictions.eq("useable", 0));
		
		List<Bankinfo> bankinfos=getHibernateTemplate().findByCriteria(c);
		Bankinfo bankinfo=null;
		
		
		try {
			if (bankinfos.size()!=0) {
				bankinfo = bankinfos.get(0);
				Double credit = bankinfo.getAmount();
				//bankinfo.setAmount(credit+amount*99.6/100);
				//this.update(bankinfo);
				
				//添加银行额度流水
				BankCreditlogs bankCreditlogs = new BankCreditlogs();
				bankCreditlogs.setCreatetime(DateUtil.getCurrentTimestamp());
				bankCreditlogs.setBankname(bankinfo.getUsername());
				bankCreditlogs.setType(dfh.model.enums.BankCreditChangeType.NETPAY.getCode());
				bankCreditlogs.setNewCredit(credit+amount*99.6/100);
				bankCreditlogs.setCredit(credit);
				bankCreditlogs.setRemit(amount*99.6/100);
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
	 * Zf根据银行类型调整在线存款账户的余额
	 */

	public void changeZf1AmountOnline(Integer type,double amount,String pno){
		DetachedCriteria c = DetachedCriteria.forClass(Bankinfo.class);
		c.add(Restrictions.eq("type", type));
		c.add(Restrictions.eq("useable", 0));
		
		List<Bankinfo> bankinfos=getHibernateTemplate().findByCriteria(c);
		Bankinfo bankinfo=null;
		
		
		try {
			if (bankinfos.size()!=0) {
				bankinfo = bankinfos.get(0);
				Double credit = bankinfo.getAmount();
				//bankinfo.setAmount(credit+amount*99.2/100);
				//this.update(bankinfo);
				
				//添加银行额度流水
				BankCreditlogs bankCreditlogs = new BankCreditlogs();
				bankCreditlogs.setCreatetime(DateUtil.getCurrentTimestamp());
				bankCreditlogs.setBankname(bankinfo.getUsername());
				bankCreditlogs.setType(dfh.model.enums.BankCreditChangeType.NETPAY.getCode());
				bankCreditlogs.setNewCredit(credit+amount*99.2/100);
				bankCreditlogs.setCredit(credit);
				bankCreditlogs.setRemit(amount*99.2/100);
				bankCreditlogs.setRemark("referenceNo:"+pno);
				save(bankCreditlogs);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
	
	public void changeCommonZfAmountOnline(CommonZfEnum cmzf,double amount,String pno){
		DetachedCriteria c = DetachedCriteria.forClass(Bankinfo.class);
		c.add(Restrictions.eq("type", cmzf.getBankinfoType()));
		c.add(Restrictions.eq("useable", 0));
		
		List<Bankinfo> bankinfos=getHibernateTemplate().findByCriteria(c);
		Bankinfo bankinfo=null;
		
		
		try {
			if (bankinfos.size()!=0) {
				bankinfo = bankinfos.get(0);
				Double credit = bankinfo.getAmount();
				//bankinfo.setAmount(bankinfo.getAmount()+amount*cmzf.getBankinfoRate());
				//this.update(bankinfo);
				//添加银行额度流水
				BankCreditlogs bankCreditlogs = new BankCreditlogs();
				bankCreditlogs.setCreatetime(DateUtil.getCurrentTimestamp());
				bankCreditlogs.setBankname(bankinfo.getUsername());
				bankCreditlogs.setType(dfh.model.enums.BankCreditChangeType.NETPAY.getCode());
				bankCreditlogs.setNewCredit(credit+amount*cmzf.getBankinfoRate());
				bankCreditlogs.setCredit(credit);
				bankCreditlogs.setRemit(amount*cmzf.getBankinfoRate());
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
	 * 根据银行类型调整在线存款账户的余额
	 */
	public void changeZfbAmountOnline(Integer type,double amount,String pno){
		DetachedCriteria c = DetachedCriteria.forClass(Bankinfo.class);
		c.add(Restrictions.eq("type", type));
		c.add(Restrictions.eq("useable", 0));
		
		List<Bankinfo> bankinfos=getHibernateTemplate().findByCriteria(c);
		Bankinfo bankinfo=null;
		
		
		try {
			if (bankinfos.size()!=0) {
				bankinfo = bankinfos.get(0);
				Double credit = bankinfo.getAmount();
				/*
				 *套餐基本交易流量: 人民币45万
				 *在本合同有效期内，甲方实际交易流量超出基本交易流量的部分，应按超出部分单笔的1.2%另行向乙方计付超量服务费。
				 **/
				Double newCredit = credit > 450000 ? (credit+amount*98.8/100) : (credit+amount);
				bankinfo.setAmount(newCredit);
				this.update(bankinfo);
				//添加银行额度流水
				BankCreditlogs bankCreditlogs = new BankCreditlogs();
				bankCreditlogs.setCreatetime(DateUtil.getCurrentTimestamp());
				bankCreditlogs.setBankname(bankinfo.getUsername());
				bankCreditlogs.setType(dfh.model.enums.BankCreditChangeType.NETPAY.getCode());
				bankCreditlogs.setNewCredit(newCredit);
				bankCreditlogs.setCredit(credit);
				bankCreditlogs.setRemit(credit > 450000 ? (amount*98.8/100) : (amount));
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
	 * 根据银行类型调整在线存款账户的余额
	 */

	public void changeHfAmountOnline(Integer type,double amount,String pno){
		DetachedCriteria c = DetachedCriteria.forClass(Bankinfo.class);
		c.add(Restrictions.eq("type", type));
		c.add(Restrictions.eq("useable", 0));
		
		List<Bankinfo> bankinfos=getHibernateTemplate().findByCriteria(c);
		Bankinfo bankinfo=null;
		
		
		try {
			if (bankinfos.size()!=0) {
				bankinfo = bankinfos.get(0);
				System.out.println("调整在线存款开始--------"+bankinfo.getBankname());
				Double credit = bankinfo.getAmount();
				//bankinfo.setAmount(bankinfo.getAmount()+amount*99.6/100);
				//this.update(bankinfo);
				//添加银行额度流水
				BankCreditlogs bankCreditlogs = new BankCreditlogs();
				bankCreditlogs.setCreatetime(DateUtil.getCurrentTimestamp());
				bankCreditlogs.setBankname(bankinfo.getUsername());
				bankCreditlogs.setType(dfh.model.enums.BankCreditChangeType.NETPAY.getCode());
				bankCreditlogs.setNewCredit(credit+amount*99.6/100);
				bankCreditlogs.setCredit(credit);
				bankCreditlogs.setRemit(amount*99.6/100);
				bankCreditlogs.setRemark("referenceNo:"+pno);
				save(bankCreditlogs);
				System.out.println("调整在线存款结束--------"+bankCreditlogs.getBankname());
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

	public void changeHfAmountOnline1(Integer type,double amount,String pno){
		DetachedCriteria c = DetachedCriteria.forClass(Bankinfo.class);
		c.add(Restrictions.eq("type", type));
		c.add(Restrictions.eq("useable", 0));
		
		List<Bankinfo> bankinfos=getHibernateTemplate().findByCriteria(c);
		Bankinfo bankinfo=null;
		
		
		try {
			if (bankinfos.size()!=0) {
				bankinfo = bankinfos.get(0);
				System.out.println("调整在线存款开始--------"+bankinfo.getBankname());
				Double credit = bankinfo.getAmount();
				//bankinfo.setAmount(bankinfo.getAmount()+amount*99.5/100);
				//this.update(bankinfo);
				//添加银行额度流水
				BankCreditlogs bankCreditlogs = new BankCreditlogs();
				bankCreditlogs.setCreatetime(DateUtil.getCurrentTimestamp());
				bankCreditlogs.setBankname(bankinfo.getUsername());
				bankCreditlogs.setType(dfh.model.enums.BankCreditChangeType.NETPAY.getCode());
				bankCreditlogs.setNewCredit(credit+amount*99.5/100);
				bankCreditlogs.setCredit(credit);
				bankCreditlogs.setRemit(amount*99.5/100);
				bankCreditlogs.setRemark("referenceNo:"+pno);
				save(bankCreditlogs);
				System.out.println("调整在线存款结束--------"+bankCreditlogs.getBankname());
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
	
	/**
	 * 根据银行类型调整在线存款账户的余额(汇潮)
	 * @param type
	 * @param amount
	 * @param pno
	 */
	public void updateHCAmountOnline(Integer type,double amount,String pno){
		DetachedCriteria c = DetachedCriteria.forClass(Bankinfo.class);
		c.add(Restrictions.eq("type", type));
		c.add(Restrictions.eq("useable", 0));
		
		List<Bankinfo> bankinfos=getHibernateTemplate().findByCriteria(c);
		Bankinfo bankinfo=null;
		
		
		try {
			if (bankinfos.size()!=0) {
				bankinfo = bankinfos.get(0);
				Double credit = bankinfo.getAmount();
				//bankinfo.setAmount(bankinfo.getAmount()+amount*99.4/100);
				//this.update(bankinfo);
				//添加银行额度流水
				BankCreditlogs bankCreditlogs = new BankCreditlogs();
				bankCreditlogs.setCreatetime(DateUtil.getCurrentTimestamp());
				bankCreditlogs.setBankname(bankinfo.getUsername());
				bankCreditlogs.setType(dfh.model.enums.BankCreditChangeType.NETPAY.getCode());
				bankCreditlogs.setNewCredit(credit+amount*99.4/100);
				bankCreditlogs.setCredit(credit);
				bankCreditlogs.setRemit(amount*99.4/100);
				bankCreditlogs.setRemark("referenceNo:"+pno);
				save(bankCreditlogs);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
	
	/**
	 * 汇潮一麻袋网银
	 * @param type
	 * @param amount
	 * @param pno
	 */
	public void updateHCYmdAmountOnline(Integer type,double amount,String pno){
		DetachedCriteria c = DetachedCriteria.forClass(Bankinfo.class);
		c.add(Restrictions.eq("type", type));
		c.add(Restrictions.eq("useable", 0));
		
		List<Bankinfo> bankinfos=getHibernateTemplate().findByCriteria(c);
		Bankinfo bankinfo=null;
		try {
			if (bankinfos.size()!=0) {
				bankinfo = bankinfos.get(0);
				Double credit = bankinfo.getAmount();
//				bankinfo.setAmount(bankinfo.getAmount()+amount*99.4/100);
//				this.update(bankinfo);
				//添加银行额度流水
				BankCreditlogs bankCreditlogs = new BankCreditlogs();
				bankCreditlogs.setCreatetime(DateUtil.getCurrentTimestamp());
				bankCreditlogs.setBankname(bankinfo.getUsername());
				bankCreditlogs.setType(dfh.model.enums.BankCreditChangeType.NETPAY.getCode());
				bankCreditlogs.setNewCredit(credit+amount*99.4/100);
				bankCreditlogs.setCredit(credit);
				bankCreditlogs.setRemit(amount*99.4/100);
				bankCreditlogs.setRemark("referenceNo:"+pno);
				save(bankCreditlogs);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
	
	/**
	 * 根据银行类型调整在线存款账户的余额(币付宝)
	 * @param type
	 * @param amount
	 * @param pno
	 */
	public void updateBfbAmountOnline(Integer type,double amount,String pno){
		DetachedCriteria c = DetachedCriteria.forClass(Bankinfo.class);
		c.add(Restrictions.eq("type", type));
		c.add(Restrictions.eq("useable", 0));
		
		List<Bankinfo> bankinfos=getHibernateTemplate().findByCriteria(c);
		Bankinfo bankinfo=null;
		
		try {
			if (bankinfos.size()!=0) {
				bankinfo = bankinfos.get(0);
				Double credit = bankinfo.getAmount();
				//bankinfo.setAmount(bankinfo.getAmount()+amount*99.5/100);
				//this.update(bankinfo);
				//添加银行额度流水
				BankCreditlogs bankCreditlogs = new BankCreditlogs();
				bankCreditlogs.setCreatetime(DateUtil.getCurrentTimestamp());
				bankCreditlogs.setBankname(bankinfo.getUsername());
				bankCreditlogs.setType(dfh.model.enums.BankCreditChangeType.NETPAY.getCode());
				bankCreditlogs.setNewCredit(credit+amount*99.5/100);
				bankCreditlogs.setCredit(credit);
				bankCreditlogs.setRemit(amount*99.5/100);
				bankCreditlogs.setRemark("referenceNo:"+pno);
				save(bankCreditlogs);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
	
	public void updateGfbAmountOnline(CommonGfbEnum cmgfb, double amount, String pno){
		DetachedCriteria c = DetachedCriteria.forClass(Bankinfo.class);
		c.add(Restrictions.eq("type", cmgfb.getBankinfoType()));
		c.add(Restrictions.eq("useable", 0));
		
		List<Bankinfo> bankinfos=getHibernateTemplate().findByCriteria(c);
		Bankinfo bankinfo=null;
		
		try {
			if (bankinfos.size()!=0) {
				bankinfo = bankinfos.get(0);
				Double credit = bankinfo.getAmount();
				//bankinfo.setAmount(bankinfo.getAmount()+amount*cmgfb.getBankinfoRate());
				//this.update(bankinfo);
				//添加银行额度流水
				BankCreditlogs bankCreditlogs = new BankCreditlogs();
				bankCreditlogs.setCreatetime(DateUtil.getCurrentTimestamp());
				bankCreditlogs.setBankname(bankinfo.getUsername());
				bankCreditlogs.setType(dfh.model.enums.BankCreditChangeType.NETPAY.getCode());
				bankCreditlogs.setNewCredit(credit+amount*cmgfb.getBankinfoRate());
				bankCreditlogs.setCredit(credit);
				bankCreditlogs.setRemit(amount*cmgfb.getBankinfoRate());
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
	
	
	/*
	 * 调整银行额度
	 */
	public void changeWeiXinAmountOnline(Integer type,double amount,String pno){
		DetachedCriteria c = DetachedCriteria.forClass(Bankinfo.class);
		c.add(Restrictions.eq("type", type));
		c.add(Restrictions.eq("useable", 0));
		List<Bankinfo> bankinfos=getHibernateTemplate().findByCriteria(c);
		Bankinfo bankinfo=null;
		try {
			if (bankinfos.size()!=0) {
				bankinfo = bankinfos.get(0);
				System.out.println("调整在线存款开始--------"+bankinfo.getBankname());
				Double credit = bankinfo.getAmount();
//				bankinfo.setAmount(bankinfo.getAmount()+amount*99.4/100);
//				this.update(bankinfo);
				//添加银行额度流水 
				BankCreditlogs bankCreditlogs = new BankCreditlogs();
				bankCreditlogs.setCreatetime(DateUtil.getCurrentTimestamp());
				bankCreditlogs.setBankname(bankinfo.getUsername());
				bankCreditlogs.setType(dfh.model.enums.BankCreditChangeType.NETPAY.getCode());
				bankCreditlogs.setNewCredit(credit+amount*99.4/100);
				bankCreditlogs.setCredit(credit);
				bankCreditlogs.setRemit(amount*99.4/100);
				bankCreditlogs.setRemark("referenceNo:"+pno);
				save(bankCreditlogs);
				System.out.println("调整在线存款结束--------"+bankCreditlogs.getBankname());
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}

	
	
	/*
	 * 乐富微信支付
	 */
	public void changeLfwxzfAmountOnline(Integer type,double amount,String pno){
		DetachedCriteria c = DetachedCriteria.forClass(Bankinfo.class);
		c.add(Restrictions.eq("type", type));
		c.add(Restrictions.eq("useable", 0));
		List<Bankinfo> bankinfos=getHibernateTemplate().findByCriteria(c);
		Bankinfo bankinfo=null;
		try {
			if (bankinfos.size()!=0) {
				bankinfo = bankinfos.get(0);
				System.out.println("调整在线存款开始--------"+bankinfo.getBankname());
				Double credit = bankinfo.getAmount();
				//bankinfo.setAmount(bankinfo.getAmount()+amount*99.4/100);
				//this.update(bankinfo);
				//添加银行额度流水
				BankCreditlogs bankCreditlogs = new BankCreditlogs();
				bankCreditlogs.setCreatetime(DateUtil.getCurrentTimestamp());
				bankCreditlogs.setBankname(bankinfo.getUsername());
				bankCreditlogs.setType(dfh.model.enums.BankCreditChangeType.NETPAY.getCode());
				bankCreditlogs.setNewCredit(credit+amount*99.4/100);
				bankCreditlogs.setCredit(credit);
				bankCreditlogs.setRemit(amount*99.4/100); 
				bankCreditlogs.setRemark("referenceNo:"+pno);
				save(bankCreditlogs);
				System.out.println("调整在线存款结束--------"+bankCreditlogs.getBankname());
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
	/*
	 * 新贝微信支付
	 */
	public void changeXbwxzfAmountOnline(Integer type,double amount,String pno){
		DetachedCriteria c = DetachedCriteria.forClass(Bankinfo.class);
		c.add(Restrictions.eq("type", type));
		c.add(Restrictions.eq("useable", 0));
		List<Bankinfo> bankinfos=getHibernateTemplate().findByCriteria(c);
		Bankinfo bankinfo=null;
		try {
			if (bankinfos.size()!=0) {
				bankinfo = bankinfos.get(0);
				System.out.println("调整在线存款开始--------"+bankinfo.getBankname());
				Double credit = bankinfo.getAmount();
				//bankinfo.setAmount(bankinfo.getAmount()+amount*98.8/100);
				//this.update(bankinfo);
				//添加银行额度流水
				BankCreditlogs bankCreditlogs = new BankCreditlogs();
				bankCreditlogs.setCreatetime(DateUtil.getCurrentTimestamp());
				bankCreditlogs.setBankname(bankinfo.getUsername());
				bankCreditlogs.setType(dfh.model.enums.BankCreditChangeType.NETPAY.getCode());
				bankCreditlogs.setNewCredit(credit+amount*98.8/100);
				bankCreditlogs.setCredit(credit);
				bankCreditlogs.setRemit(amount*98.8/100); 
				bankCreditlogs.setRemark("referenceNo:"+pno);
				save(bankCreditlogs);
				System.out.println("调整在线存款结束--------"+bankCreditlogs.getBankname());
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
	
	public void changeKdZfAmountOnline(Integer type,double amount,String pno){
		DetachedCriteria c = DetachedCriteria.forClass(Bankinfo.class);
		c.add(Restrictions.eq("type", type));
		c.add(Restrictions.eq("useable", 0));
		List<Bankinfo> bankinfos=getHibernateTemplate().findByCriteria(c);
		Bankinfo bankinfo=null;
		try {
			if (bankinfos.size()!=0) {
				bankinfo = bankinfos.get(0);
				System.out.println("调整在线存款开始--------"+bankinfo.getBankname());
				Double credit = bankinfo.getAmount();
				//bankinfo.setAmount(bankinfo.getAmount()+amount*97.8/100);
				//this.update(bankinfo);
				//添加银行额度流水
				BankCreditlogs bankCreditlogs = new BankCreditlogs();
				bankCreditlogs.setCreatetime(DateUtil.getCurrentTimestamp());
				bankCreditlogs.setBankname(bankinfo.getUsername());
				bankCreditlogs.setType(dfh.model.enums.BankCreditChangeType.NETPAY.getCode());
				bankCreditlogs.setNewCredit(credit+amount*97.8/100);
				bankCreditlogs.setCredit(credit);
				bankCreditlogs.setRemit(amount*97.8/100); 
				bankCreditlogs.setRemark("referenceNo:"+pno);
				save(bankCreditlogs);
				System.out.println("调整在线存款结束--------"+bankCreditlogs.getBankname());
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
	
	public void changeHhbZfAmountOnline(Integer type,double amount,String pno){
		DetachedCriteria c = DetachedCriteria.forClass(Bankinfo.class);
		c.add(Restrictions.eq("type", type));
		c.add(Restrictions.eq("useable", 0));
		List<Bankinfo> bankinfos=getHibernateTemplate().findByCriteria(c);
		Bankinfo bankinfo=null;
		try {
			if (bankinfos.size()!=0) {
				bankinfo = bankinfos.get(0);
				System.out.println("调整在线存款开始--------"+bankinfo.getBankname());
				Double credit = bankinfo.getAmount();
				//bankinfo.setAmount(bankinfo.getAmount()+amount*98.7/100); //TODO 汇付宝费率暂时1.3
				//this.update(bankinfo);
				//添加银行额度流水
				BankCreditlogs bankCreditlogs = new BankCreditlogs();
				bankCreditlogs.setCreatetime(DateUtil.getCurrentTimestamp());
				bankCreditlogs.setBankname(bankinfo.getUsername());
				bankCreditlogs.setType(dfh.model.enums.BankCreditChangeType.NETPAY.getCode());
				bankCreditlogs.setNewCredit(credit+amount*98.7/100);
				bankCreditlogs.setCredit(credit);
				bankCreditlogs.setRemit(amount*98.7/100); 
				bankCreditlogs.setRemark("referenceNo:"+pno);
				save(bankCreditlogs);
				System.out.println("调整在线存款结束--------"+bankCreditlogs.getBankname());
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}    

	public void changeKdWxZfAmountOnline(Integer type,double amount,String pno, Boolean isPhone){
		Double fee = 98.6;
		if(isPhone){
			fee = 97.6 ;
		}   
		DetachedCriteria c = DetachedCriteria.forClass(Bankinfo.class);
		c.add(Restrictions.eq("type", type));
		c.add(Restrictions.eq("useable", 0));
		List<Bankinfo> bankinfos=getHibernateTemplate().findByCriteria(c);
		Bankinfo bankinfo=null;
		try {
			if (bankinfos.size()!=0) {
				bankinfo = bankinfos.get(0);
				System.out.println("调整在线存款开始--------"+bankinfo.getBankname());
				Double credit = bankinfo.getAmount();
				//bankinfo.setAmount(bankinfo.getAmount()+amount*fee/100);
				//this.update(bankinfo);
				//添加银行额度流水
				BankCreditlogs bankCreditlogs = new BankCreditlogs();
				bankCreditlogs.setCreatetime(DateUtil.getCurrentTimestamp());
				bankCreditlogs.setBankname(bankinfo.getUsername());
				bankCreditlogs.setType(dfh.model.enums.BankCreditChangeType.NETPAY.getCode());
				bankCreditlogs.setNewCredit(credit+amount*fee/100);
				bankCreditlogs.setCredit(credit);
				bankCreditlogs.setRemit(amount*fee/100); 
				bankCreditlogs.setRemark("referenceNo:"+pno);
				save(bankCreditlogs);
				System.out.println("调整在线存款结束--------"+bankCreditlogs.getBankname());
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
	
	
	/***
	 * 口袋微信支付2
	 * @param type
	 * @param amount
	 * @param pno
	 * @param isPhone
	 */
	public void changeKdWxZfAmountOnline2(Integer type,double amount,String pno, Boolean isPhone){   
		Double fee = 98.6;
		if(isPhone){     
			fee = 97.6 ;
		}
		DetachedCriteria c = DetachedCriteria.forClass(Bankinfo.class);
		c.add(Restrictions.eq("type", type));
		c.add(Restrictions.eq("useable", 0));
		List<Bankinfo> bankinfos=getHibernateTemplate().findByCriteria(c);
		Bankinfo bankinfo=null;
		try {
			if (bankinfos.size()!=0) {
				bankinfo = bankinfos.get(0);
				System.out.println("调整在线存款开始--------"+bankinfo.getBankname());
				Double credit = bankinfo.getAmount();
				//bankinfo.setAmount(bankinfo.getAmount()+amount*fee/100);
				//this.update(bankinfo);  
				//添加银行额度流水
				BankCreditlogs bankCreditlogs = new BankCreditlogs();
				bankCreditlogs.setCreatetime(DateUtil.getCurrentTimestamp());
				bankCreditlogs.setBankname(bankinfo.getUsername());
				bankCreditlogs.setType(dfh.model.enums.BankCreditChangeType.NETPAY.getCode());
				bankCreditlogs.setNewCredit(credit+amount*fee/100);
				bankCreditlogs.setCredit(credit);
				bankCreditlogs.setRemit(amount*fee/100); 
				bankCreditlogs.setRemark("referenceNo:"+pno);
				save(bankCreditlogs);
				System.out.println("调整在线存款结束--------"+bankCreditlogs.getBankname());
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
	
	/***
	 * 口袋支付宝2 and 口袋微信支付1 and 口袋微信支付2 and 口袋微信支付3
	 * @param type
	 * @param amount
	 * @param pno
	 * @param isPhone
	 */
	public void changeKdWxZfsAmountOnline(Integer type,double amount,String pno, Double fee){   
		DetachedCriteria c = DetachedCriteria.forClass(Bankinfo.class);
		c.add(Restrictions.eq("type", type));
		c.add(Restrictions.eq("useable", 0));
		List<Bankinfo> bankinfos=getHibernateTemplate().findByCriteria(c);
		Bankinfo bankinfo=null;
		try {
			if (bankinfos.size()!=0) {
				bankinfo = bankinfos.get(0);
				System.out.println("调整在线存款开始--------"+bankinfo.getBankname());
				Double credit = bankinfo.getAmount();
				//bankinfo.setAmount(bankinfo.getAmount()+amount*fee/100);
				//this.update(bankinfo);  
				//添加银行额度流水
				BankCreditlogs bankCreditlogs = new BankCreditlogs();
				bankCreditlogs.setCreatetime(DateUtil.getCurrentTimestamp());
				bankCreditlogs.setBankname(bankinfo.getUsername());
				bankCreditlogs.setType(dfh.model.enums.BankCreditChangeType.NETPAY.getCode());
				bankCreditlogs.setNewCredit(credit+amount*fee/100);
				bankCreditlogs.setCredit(credit);
				bankCreditlogs.setRemit(amount*fee/100); 
				bankCreditlogs.setRemark("referenceNo:"+pno);
				save(bankCreditlogs);
				System.out.println("调整在线存款结束--------"+bankCreditlogs.getBankname());
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
	
	/*****
	 * 汇付宝微信
	 * @param type
	 * @param amount
	 * @param pno
	 */
	public void changeHhbWxZfAmountOnline(Integer type,double amount,String pno){
		DetachedCriteria c = DetachedCriteria.forClass(Bankinfo.class);
		c.add(Restrictions.eq("type", type));
		c.add(Restrictions.eq("useable", 0));
		List<Bankinfo> bankinfos=getHibernateTemplate().findByCriteria(c);
		Bankinfo bankinfo=null;
		try {
			if (bankinfos.size()!=0) {
				bankinfo = bankinfos.get(0);
				System.out.println("调整在线存款开始--------"+bankinfo.getBankname());
				Double credit = bankinfo.getAmount();
				//bankinfo.setAmount(bankinfo.getAmount()+amount*98.7/100); //TODO 汇付宝费率暂时1.3
				//this.update(bankinfo);
				//添加银行额度流水
				BankCreditlogs bankCreditlogs = new BankCreditlogs();
				bankCreditlogs.setCreatetime(DateUtil.getCurrentTimestamp());
				bankCreditlogs.setBankname(bankinfo.getUsername());
				bankCreditlogs.setType(dfh.model.enums.BankCreditChangeType.NETPAY.getCode());
				bankCreditlogs.setNewCredit(credit+amount*98.7/100);
				bankCreditlogs.setCredit(credit);
				bankCreditlogs.setRemit(amount*98.7/100); 
				bankCreditlogs.setRemark("referenceNo:"+pno);
				save(bankCreditlogs);
				System.out.println("调整在线存款结束--------"+bankCreditlogs.getBankname());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
	
	/***
	 * 聚宝支付宝支付
	 * @param type
	 * @param amount
	 * @param pno
	 */
	public void changeJubZfbAmountOnline(Integer type,double amount,String pno){
		DetachedCriteria c = DetachedCriteria.forClass(Bankinfo.class);
		c.add(Restrictions.eq("type", type));
		c.add(Restrictions.eq("useable", 0));
		List<Bankinfo> bankinfos=getHibernateTemplate().findByCriteria(c);
		Bankinfo bankinfo=null;
		try { 
			if (bankinfos.size()!=0) {
				bankinfo = bankinfos.get(0);
				System.out.println("调整在线存款开始--------"+bankinfo.getBankname());
				Double credit = bankinfo.getAmount();
				//bankinfo.setAmount(bankinfo.getAmount()+amount*97.7/100);
				//this.update(bankinfo);
				//添加银行额度流水
				BankCreditlogs bankCreditlogs = new BankCreditlogs();
				bankCreditlogs.setCreatetime(DateUtil.getCurrentTimestamp());
				bankCreditlogs.setBankname(bankinfo.getUsername());
				bankCreditlogs.setType(dfh.model.enums.BankCreditChangeType.NETPAY.getCode());
				bankCreditlogs.setNewCredit(credit+amount*97.7/100);
				bankCreditlogs.setCredit(credit);
				bankCreditlogs.setRemit(amount*97.7/100); 
				bankCreditlogs.setRemark("referenceNo:"+pno);
				save(bankCreditlogs);
				System.out.println("调整在线存款结束--------"+bankCreditlogs.getBankname());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
	
	/*****
	 * 迅联宝微信支付
	 * @param type
	 * @param amount
	 * @param pno
	 * @param isPhone
	 */
	public void changeXlbAmountOnline(Integer type,double amount,String pno,Boolean isPhone){
		Double fee = 98.7;
		if(isPhone){     
			fee = 98.7 ;
		}
		DetachedCriteria c = DetachedCriteria.forClass(Bankinfo.class);
		c.add(Restrictions.eq("type", type));
		c.add(Restrictions.eq("useable", 0));
		List<Bankinfo> bankinfos=getHibernateTemplate().findByCriteria(c);
		Bankinfo bankinfo=null;
		try {    
			if (bankinfos.size()!=0) {
				bankinfo = bankinfos.get(0);
				System.out.println("调整在线存款开始--------"+bankinfo.getBankname());
				Double credit = bankinfo.getAmount();
				//bankinfo.setAmount(bankinfo.getAmount()+amount*fee/100);
				//this.update(bankinfo);
				//添加银行额度流水
				BankCreditlogs bankCreditlogs = new BankCreditlogs();
				bankCreditlogs.setCreatetime(DateUtil.getCurrentTimestamp());
				bankCreditlogs.setBankname(bankinfo.getUsername());
				bankCreditlogs.setType(dfh.model.enums.BankCreditChangeType.NETPAY.getCode());
				bankCreditlogs.setNewCredit(credit+amount*fee/100);
				bankCreditlogs.setCredit(credit);
				bankCreditlogs.setRemit(amount*fee/100); 
				bankCreditlogs.setRemark("referenceNo:"+pno);
				save(bankCreditlogs);
				System.out.println("调整在线存款结束--------"+bankCreditlogs.getBankname());
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
	
	/*****
	 * 迅联宝支付宝
	 * @param type
	 * @param amount
	 * @param pno
	 * @param isPhone
	 */
	public void changeXlbZfbAmountOnline(Integer type,double amount,String pno,Boolean isPhone){
		Double fee = 98.5;
		if(isPhone){     
			fee = 98.5;
		}
		DetachedCriteria c = DetachedCriteria.forClass(Bankinfo.class);
		c.add(Restrictions.eq("type", type));
		c.add(Restrictions.eq("useable", 0));
		List<Bankinfo> bankinfos=getHibernateTemplate().findByCriteria(c);
		Bankinfo bankinfo=null;
		try {    
			if (bankinfos.size()!=0) {
				bankinfo = bankinfos.get(0);
				System.out.println("调整在线存款开始--------"+bankinfo.getBankname());
				Double credit = bankinfo.getAmount();
				//bankinfo.setAmount(bankinfo.getAmount()+amount*fee/100);
				//this.update(bankinfo);
				//添加银行额度流水
				BankCreditlogs bankCreditlogs = new BankCreditlogs();
				bankCreditlogs.setCreatetime(DateUtil.getCurrentTimestamp());
				bankCreditlogs.setBankname(bankinfo.getUsername());
				bankCreditlogs.setType(dfh.model.enums.BankCreditChangeType.NETPAY.getCode());
				bankCreditlogs.setNewCredit(credit+amount*fee/100);
				bankCreditlogs.setCredit(credit);
				bankCreditlogs.setRemit(amount*fee/100); 
				bankCreditlogs.setRemark("referenceNo:"+pno);
				save(bankCreditlogs);
				System.out.println("调整在线存款结束--------"+bankCreditlogs.getBankname());
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
	
	/*****
	 * 迅联宝网银支付
	 * @param type
	 * @param amount
	 * @param pno
	 * @param isPhone
	 */
	public void changeXlbWyAmountOnline(Integer type,double amount,String pno){
		Double fee = 99.4;
		DetachedCriteria c = DetachedCriteria.forClass(Bankinfo.class);
		c.add(Restrictions.eq("type", type));
		c.add(Restrictions.eq("useable", 0));
		List<Bankinfo> bankinfos=getHibernateTemplate().findByCriteria(c);
		Bankinfo bankinfo=null;
		try { 
			if (bankinfos.size()!=0) {
				bankinfo = bankinfos.get(0);
				System.out.println("调整在线存款开始--------"+bankinfo.getBankname());
				Double credit = bankinfo.getAmount();
				//bankinfo.setAmount(bankinfo.getAmount()+amount*fee/100);
				//this.update(bankinfo);
				//添加银行额度流水
				BankCreditlogs bankCreditlogs = new BankCreditlogs();
				bankCreditlogs.setCreatetime(DateUtil.getCurrentTimestamp());
				bankCreditlogs.setBankname(bankinfo.getUsername());
				bankCreditlogs.setType(dfh.model.enums.BankCreditChangeType.NETPAY.getCode());
				bankCreditlogs.setNewCredit(credit+amount*fee/100);
				bankCreditlogs.setCredit(credit);
				bankCreditlogs.setRemit(amount*fee/100); 
				bankCreditlogs.setRemark("referenceNo:"+pno);
				save(bankCreditlogs);
				System.out.println("调整在线存款结束--------"+bankCreditlogs.getBankname());
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
	
	
	/***
	 * 优付支付宝 and 微信
	 * @param type
	 * @param amount
	 * @param pno
	 */
	public void changeYfZfAmountOnline(Integer type,double amount,String pno,Double fee){
		DetachedCriteria c = DetachedCriteria.forClass(Bankinfo.class);
		c.add(Restrictions.eq("type", type));
		c.add(Restrictions.eq("useable", 0));
		List<Bankinfo> bankinfos=getHibernateTemplate().findByCriteria(c);
		Bankinfo bankinfo=null;
		try { 
			if (bankinfos.size()!=0) {
				bankinfo = bankinfos.get(0);
				System.out.println("调整在线存款开始--------"+bankinfo.getBankname());
				Double credit = bankinfo.getAmount();
//				bankinfo.setAmount(bankinfo.getAmount()+amount*fee/100);
//				this.update(bankinfo);
				//添加银行额度流水
				BankCreditlogs bankCreditlogs = new BankCreditlogs();
				bankCreditlogs.setCreatetime(DateUtil.getCurrentTimestamp());
				bankCreditlogs.setBankname(bankinfo.getUsername());
				bankCreditlogs.setType(dfh.model.enums.BankCreditChangeType.NETPAY.getCode());
				bankCreditlogs.setNewCredit(credit+amount*fee/100);
				bankCreditlogs.setCredit(credit);
				bankCreditlogs.setRemit(amount*fee/100); 
				bankCreditlogs.setRemark("referenceNo:"+pno);
				save(bankCreditlogs);
				System.out.println("调整在线存款结束--------"+bankCreditlogs.getBankname());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
	
	/***
	 * 银宝支付宝支付
	 * @param type
	 * @param amount
	 * @param pno
	 */
	public void changeYbZfbAmountOnline(Integer type,double amount,String pno, Boolean isPhone){   
		DetachedCriteria c = DetachedCriteria.forClass(Bankinfo.class);
		c.add(Restrictions.eq("type", type));
		c.add(Restrictions.eq("useable", 0));
		List<Bankinfo> bankinfos=getHibernateTemplate().findByCriteria(c);
		Bankinfo bankinfo=null;
		try { 
			if (bankinfos.size()!=0) {
				bankinfo = bankinfos.get(0);
				System.out.println("调整在线存款开始--------"+bankinfo.getBankname());
				Double credit = bankinfo.getAmount();
				//bankinfo.setAmount(bankinfo.getAmount()+amount*98.8/100);
				//this.update(bankinfo);
				//添加银行额度流水
				BankCreditlogs bankCreditlogs = new BankCreditlogs();
				bankCreditlogs.setCreatetime(DateUtil.getCurrentTimestamp());
				bankCreditlogs.setBankname(bankinfo.getUsername());
				bankCreditlogs.setType(dfh.model.enums.BankCreditChangeType.NETPAY.getCode());
				bankCreditlogs.setNewCredit(credit+amount*98.8/100);
				bankCreditlogs.setCredit(credit);
				bankCreditlogs.setRemit(amount*98.8/100); 
				bankCreditlogs.setRemark("referenceNo:"+pno);
				save(bankCreditlogs);
				System.out.println("调整在线存款结束--------"+bankCreditlogs.getBankname());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
	
	/***
	 * 新贝支付宝支付
	 * @param type
	 * @param amount
	 * @param pno
	 */
	public void changeXbZfbAmountOnline(Integer type,double amount,String pno){
		DetachedCriteria c = DetachedCriteria.forClass(Bankinfo.class);
		c.add(Restrictions.eq("type", type));
		c.add(Restrictions.eq("useable", 0));
		List<Bankinfo> bankinfos=getHibernateTemplate().findByCriteria(c);
		Bankinfo bankinfo=null;
		try { 
			if (bankinfos.size()!=0) {
				bankinfo = bankinfos.get(0);
				System.out.println("调整在线存款开始--------"+bankinfo.getBankname());
				Double credit = bankinfo.getAmount();
				//bankinfo.setAmount(bankinfo.getAmount()+amount*98.8/100);
				//this.update(bankinfo);
				//添加银行额度流水
				BankCreditlogs bankCreditlogs = new BankCreditlogs();
				bankCreditlogs.setCreatetime(DateUtil.getCurrentTimestamp());
				bankCreditlogs.setBankname(bankinfo.getUsername());
				bankCreditlogs.setType(dfh.model.enums.BankCreditChangeType.NETPAY.getCode());
				bankCreditlogs.setNewCredit(credit+amount*98.8/100);
				bankCreditlogs.setCredit(credit);
				bankCreditlogs.setRemit(amount*98.8/100); 
				bankCreditlogs.setRemark("referenceNo:"+pno);
				save(bankCreditlogs);
				System.out.println("调整在线存款结束--------"+bankCreditlogs.getBankname());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
	
	/***
	 * 千网支付宝 and 微信
	 * @param type
	 * @param amount
	 * @param pno
	 */
	public void changeQwZfAmountOnline(Integer type,double amount,String pno,Double fee){   
		DetachedCriteria c = DetachedCriteria.forClass(Bankinfo.class);
		c.add(Restrictions.eq("type", type));
		c.add(Restrictions.eq("useable", 0));
		List<Bankinfo> bankinfos=getHibernateTemplate().findByCriteria(c);
		Bankinfo bankinfo=null;
		try { 
			if (bankinfos.size()!=0) {
				bankinfo = bankinfos.get(0);
				System.out.println("调整在线存款开始--------"+bankinfo.getBankname());
				Double credit = bankinfo.getAmount();
//				bankinfo.setAmount(bankinfo.getAmount()+amount*fee/100);
//				this.update(bankinfo);
				//添加银行额度流水
				BankCreditlogs bankCreditlogs = new BankCreditlogs();
				bankCreditlogs.setCreatetime(DateUtil.getCurrentTimestamp());
				bankCreditlogs.setBankname(bankinfo.getUsername());
				bankCreditlogs.setType(dfh.model.enums.BankCreditChangeType.NETPAY.getCode());
				bankCreditlogs.setNewCredit(credit+amount*fee/100);
				bankCreditlogs.setCredit(credit);
				bankCreditlogs.setRemit(amount*fee/100); 
				bankCreditlogs.setRemark("referenceNo:"+pno);
				save(bankCreditlogs);
				System.out.println("调整在线存款结束--------"+bankCreditlogs.getBankname());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
}
