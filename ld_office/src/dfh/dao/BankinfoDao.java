package dfh.dao;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import dfh.model.BankCreditlogs;
import dfh.model.Bankinfo;
import dfh.model.MerchantPay;
import dfh.model.enums.CommonGfbEnum;
import dfh.model.enums.CommonZfEnum;
import dfh.model.enums.CreditChangeType;
import dfh.utils.DateUtil;

public class BankinfoDao extends BaseDao<Bankinfo, String>{
	
	LogDao logDao;
	private MerchantPayDao merchantPayDao;
	
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
	public void changeBankInAmount(String id,Integer type,Integer useable,double amount,String fromByTo){
		try {
			Bankinfo bankinfo = null;
            MerchantPay mp = null;
			Double credit = 0.0;

			if(id.startsWith("o_")){
				id = id.split("o_")[1];
                mp = (MerchantPay)getHibernateTemplate().get(MerchantPay.class, Long.parseLong(id));
                credit = mp.getAmount();
                mp.setAmount(mp.getAmount()+amount);
                merchantPayDao.saveorupdate(mp);
			}else{
				if(id.startsWith("b_")){
					id = id.split("b_")[1];
				}
				bankinfo = (Bankinfo)getHibernateTemplate().get(Bankinfo.class, Integer.parseInt(id));
				credit = bankinfo.getAmount();
				bankinfo.setAmount(bankinfo.getAmount()+amount);
				saveorupdate(bankinfo);
			}

			//添加银行额度流水
			BankCreditlogs bankCreditlogs = new BankCreditlogs();
			bankCreditlogs.setCreatetime(DateUtil.getCurrentTimestamp());
            if(bankinfo != null) {
                bankCreditlogs.setBankname(bankinfo.getUsername());
            } else if(mp != null ){
                bankCreditlogs.setBankname(mp.getPayName());
            }
			if((bankinfo != null && bankinfo.getBanktype()==0) || mp != null ){
				bankCreditlogs.setType(dfh.model.enums.BankCreditChangeType.INTRANFER.getCode());
				bankCreditlogs.setRemark("银行内部转账|"+fromByTo);
			}else{
				bankCreditlogs.setType(dfh.model.enums.BankCreditChangeType.INTRANFER2.getCode());
				bankCreditlogs.setRemark("银行外部转账|"+fromByTo);
			}
			bankCreditlogs.setNewCredit(credit+amount);
			bankCreditlogs.setCredit(credit);
			bankCreditlogs.setRemit(amount);
			getHibernateTemplate().save(bankCreditlogs);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
	/*
	 * 根据id调整取款账户的余额
	 */
	public void changeAmount(String id,Integer type,Integer useable,double amount,String pno){
		try {
			Bankinfo bankinfo = (Bankinfo)getHibernateTemplate().get(Bankinfo.class, Integer.parseInt(id));
			Double credit = bankinfo.getAmount();
			bankinfo.setAmount(bankinfo.getAmount()+amount);
			saveorupdate(bankinfo);
			
			//添加银行额度流水
			BankCreditlogs bankCreditlogs = new BankCreditlogs();
			bankCreditlogs.setCreatetime(DateUtil.getCurrentTimestamp());
			bankCreditlogs.setBankname(bankinfo.getUsername());
			if(12==type){
				bankCreditlogs.setType(dfh.model.enums.BankCreditChangeType.SHIWU.getCode());
			}else{
				bankCreditlogs.setType(dfh.model.enums.BankCreditChangeType.CASHOUT.getCode());
			}
			bankCreditlogs.setNewCredit(credit+amount);
			bankCreditlogs.setCredit(credit);
			bankCreditlogs.setRemit(amount);
			bankCreditlogs.setRemark("referenceNo:"+pno);
			getHibernateTemplate().save(bankCreditlogs);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
	

	/*
	 * 根据id调整取款账户的余额
	 */
	public void changeAmountTwo(String id,Integer type,Integer useable,double amount,String pno){
		try {
			Bankinfo bankinfo = (Bankinfo)getHibernateTemplate().get(Bankinfo.class, Integer.parseInt(id));
			Double credit = bankinfo.getAmount();
			bankinfo.setAmount(bankinfo.getAmount()+amount);
			saveorupdate(bankinfo);
			
			//添加银行额度流水
			BankCreditlogs bankCreditlogs = new BankCreditlogs();
			bankCreditlogs.setCreatetime(DateUtil.getCurrentTimestamp());
			bankCreditlogs.setBankname(bankinfo.getUsername());
			if(12==type){
				bankCreditlogs.setType(dfh.model.enums.BankCreditChangeType.SHIWU.getCode());
			}else{
				bankCreditlogs.setType(dfh.model.enums.BankCreditChangeType.INTRANFERDl.getCode());
			}
			bankCreditlogs.setNewCredit(credit+amount);
			bankCreditlogs.setCredit(credit);
			bankCreditlogs.setRemit(amount);
			bankCreditlogs.setRemark("referenceNo:"+pno);
			getHibernateTemplate().save(bankCreditlogs);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
	/*
	 * 根据银行用户们调整存款账户的余额
	 */

	public void changeAmountByName(String username,double amount,String pno){
		DetachedCriteria c = DetachedCriteria.forClass(Bankinfo.class);
		c.add(Restrictions.eq("username", username));
		//c.add(Restrictions.or(Restrictions.eq("type", 1),Restrictions.eq("type", 7)));
		c.add(Restrictions.in("type", new Integer[]{1, 7, 9}));
		c.add(Restrictions.eq("useable", 0));
		
		List<Bankinfo> bankinfos=getHibernateTemplate().findByCriteria(c);
		Bankinfo bankinfo=null;
		
		
		try {
			if (bankinfos.size()!=0) {
				bankinfo = bankinfos.get(0);
				Double credit = bankinfo.getAmount();
				bankinfo.setAmount(bankinfo.getAmount()+amount);
				saveorupdate(bankinfo);
				
				//添加银行额度流水
				BankCreditlogs bankCreditlogs = new BankCreditlogs();
				bankCreditlogs.setCreatetime(DateUtil.getCurrentTimestamp());
				bankCreditlogs.setBankname(bankinfo.getUsername());
				bankCreditlogs.setType(dfh.model.enums.BankCreditChangeType.CASHIN.getCode());
				bankCreditlogs.setNewCredit(credit+amount);
				bankCreditlogs.setCredit(credit);
				bankCreditlogs.setRemit(amount);
				bankCreditlogs.setRemark("referenceNo:"+pno);
				getHibernateTemplate().save(bankCreditlogs);
				
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

	public String changeBankCreditManual(String username,double amount,String remark,String type){
		String msg=null;
		DetachedCriteria c = DetachedCriteria.forClass(Bankinfo.class);
		c.add(Restrictions.eq("username", username));
		c.add(Restrictions.eq("useable", 0));
		
		List<Bankinfo> bankinfos=getHibernateTemplate().findByCriteria(c);
		Bankinfo bankinfo=null;
		
		
		try {
			if (bankinfos.size()!=0) {
				bankinfo = bankinfos.get(0);
				
				Double credit = bankinfo.getAmount();
				logDao.insertCreditLog(bankinfo.getUsername(), CreditChangeType.getCode(type), bankinfo.getAmount(), amount, bankinfo.getAmount()+amount, StringUtils.trimToEmpty(remark));
				
				bankinfo.setAmount(bankinfo.getAmount()+amount);
				saveorupdate(bankinfo);
				
				//添加银行额度流水
				BankCreditlogs bankCreditlogs = new BankCreditlogs();
				bankCreditlogs.setCreatetime(DateUtil.getCurrentTimestamp());
				bankCreditlogs.setBankname(bankinfo.getUsername());
				bankCreditlogs.setType(CreditChangeType.getCode(type));
				bankCreditlogs.setNewCredit(credit+amount);
				bankCreditlogs.setCredit(credit);
				bankCreditlogs.setRemit(amount);
				bankCreditlogs.setRemark("手工增减银行额度|"+username);
				getHibernateTemplate().save(bankCreditlogs);
				
				
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
				saveorupdate(bankinfo);
				
				//添加银行额度流水
				BankCreditlogs bankCreditlogs = new BankCreditlogs();
				bankCreditlogs.setCreatetime(DateUtil.getCurrentTimestamp());
				bankCreditlogs.setBankname(bankinfo.getUsername());
				bankCreditlogs.setType(dfh.model.enums.BankCreditChangeType.REPAIR_PAYORDER.getCode());
				bankCreditlogs.setNewCredit(credit+amount*99.4/100);
				bankCreditlogs.setCredit(credit);
				bankCreditlogs.setRemit(amount*99.4/100);
				bankCreditlogs.setRemark("referenceNo:"+pno);
				getHibernateTemplate().save(bankCreditlogs);
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
				Double credit = bankinfo.getAmount();
				bankinfo.setAmount(bankinfo.getAmount()+amount*99.6/100);
				saveorupdate(bankinfo);
				
				//添加银行额度流水
				BankCreditlogs bankCreditlogs = new BankCreditlogs();
				bankCreditlogs.setCreatetime(DateUtil.getCurrentTimestamp());
				bankCreditlogs.setBankname(bankinfo.getUsername());
				bankCreditlogs.setType(dfh.model.enums.BankCreditChangeType.REPAIR_PAYORDER.getCode());
				bankCreditlogs.setNewCredit(credit+amount*99.6/100);
				bankCreditlogs.setCredit(credit);
				bankCreditlogs.setRemit(amount*99.6/100);
				bankCreditlogs.setRemark("referenceNo:"+pno);
				getHibernateTemplate().save(bankCreditlogs);
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
				bankinfo.setAmount(bankinfo.getAmount()+amount*99.4/100);
				saveorupdate(bankinfo);
				
				//添加银行额度流水
				BankCreditlogs bankCreditlogs = new BankCreditlogs();
				bankCreditlogs.setCreatetime(DateUtil.getCurrentTimestamp());
				bankCreditlogs.setBankname(bankinfo.getUsername());
				bankCreditlogs.setType(dfh.model.enums.BankCreditChangeType.REPAIR_PAYORDER.getCode());
				bankCreditlogs.setNewCredit(credit+amount*99.4/100);
				bankCreditlogs.setCredit(credit);
				bankCreditlogs.setRemit(amount*99.4/100);
				bankCreditlogs.setRemark("referenceNo:"+pno);
				getHibernateTemplate().save(bankCreditlogs);
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
				saveorupdate(bankinfo);
				
				//添加银行额度流水
				BankCreditlogs bankCreditlogs = new BankCreditlogs();
				bankCreditlogs.setCreatetime(DateUtil.getCurrentTimestamp());
				bankCreditlogs.setBankname(bankinfo.getUsername());
				bankCreditlogs.setType(dfh.model.enums.BankCreditChangeType.REPAIR_PAYORDER.getCode());
				bankCreditlogs.setNewCredit(newCredit);
				bankCreditlogs.setCredit(credit);
				bankCreditlogs.setRemit(credit > 450000 ? (amount*98.8/100) : (amount));
				bankCreditlogs.setRemark("referenceNo:"+pno);
				getHibernateTemplate().save(bankCreditlogs);
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
				bankinfo.setAmount(bankinfo.getAmount()+amount*99.2/100);
				saveorupdate(bankinfo);
				
				//添加银行额度流水
				BankCreditlogs bankCreditlogs = new BankCreditlogs();
				bankCreditlogs.setCreatetime(DateUtil.getCurrentTimestamp());
				bankCreditlogs.setBankname(bankinfo.getUsername());
				bankCreditlogs.setType(dfh.model.enums.BankCreditChangeType.REPAIR_PAYORDER.getCode());
				bankCreditlogs.setNewCredit(credit+amount*99.2/100);
				bankCreditlogs.setCredit(credit);
				bankCreditlogs.setRemit(amount*99.2/100);
				bankCreditlogs.setRemark("referenceNo:"+pno);
				getHibernateTemplate().save(bankCreditlogs);
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

	public void changeZf23AmountOnline(Integer type,double amount,String pno){
		DetachedCriteria c = DetachedCriteria.forClass(Bankinfo.class);
		c.add(Restrictions.eq("type", type));
		c.add(Restrictions.eq("useable", 0));
		
		List<Bankinfo> bankinfos=getHibernateTemplate().findByCriteria(c);
		Bankinfo bankinfo=null;
		
		
		try {
			if (bankinfos.size()!=0) {
				bankinfo = bankinfos.get(0);
				Double credit = bankinfo.getAmount();
				bankinfo.setAmount(bankinfo.getAmount()+amount*99.6/100);
				saveorupdate(bankinfo);
				
				//添加银行额度流水
				BankCreditlogs bankCreditlogs = new BankCreditlogs();
				bankCreditlogs.setCreatetime(DateUtil.getCurrentTimestamp());
				bankCreditlogs.setBankname(bankinfo.getUsername());
				bankCreditlogs.setType(dfh.model.enums.BankCreditChangeType.REPAIR_PAYORDER.getCode());
				bankCreditlogs.setNewCredit(credit+amount*99.6/100);
				bankCreditlogs.setCredit(credit);
				bankCreditlogs.setRemit(amount*99.6/100);
				bankCreditlogs.setRemark("referenceNo:"+pno);
				getHibernateTemplate().save(bankCreditlogs);
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
				bankinfo.setAmount(bankinfo.getAmount()+amount*cmzf.getBankinfoRate());
				saveorupdate(bankinfo);
				
				//添加银行额度流水
				BankCreditlogs bankCreditlogs = new BankCreditlogs();
				bankCreditlogs.setCreatetime(DateUtil.getCurrentTimestamp());
				bankCreditlogs.setBankname(bankinfo.getUsername());
				bankCreditlogs.setType(dfh.model.enums.BankCreditChangeType.REPAIR_PAYORDER.getCode());
				bankCreditlogs.setNewCredit(credit+amount*cmzf.getBankinfoRate());
				bankCreditlogs.setCredit(credit);
				bankCreditlogs.setRemit(amount*cmzf.getBankinfoRate());
				bankCreditlogs.setRemark("referenceNo:"+pno);
				getHibernateTemplate().save(bankCreditlogs);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
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
//				bankinfo.setAmount(bankinfo.getAmount()+amount);
//				saveorupdate(bankinfo);
				
				//添加银行额度流水
				BankCreditlogs bankCreditlogs = new BankCreditlogs();
				bankCreditlogs.setCreatetime(DateUtil.getCurrentTimestamp());
				bankCreditlogs.setBankname(bankinfo.getUsername());
				bankCreditlogs.setType(dfh.model.enums.BankCreditChangeType.REPAIR_PAYORDER.getCode());
				bankCreditlogs.setNewCredit(credit+amount);
				bankCreditlogs.setCredit(credit);
				bankCreditlogs.setRemit(amount);
				bankCreditlogs.setRemark("referenceNo:"+pno);
				getHibernateTemplate().save(bankCreditlogs);
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

	public void changeHcAmountOnline(Integer type,double amount,String pno){
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
//				saveorupdate(bankinfo);
				
				//添加银行额度流水
				BankCreditlogs bankCreditlogs = new BankCreditlogs();
				bankCreditlogs.setCreatetime(DateUtil.getCurrentTimestamp());
				bankCreditlogs.setBankname(bankinfo.getUsername());
				bankCreditlogs.setType(dfh.model.enums.BankCreditChangeType.REPAIR_PAYORDER.getCode());
				bankCreditlogs.setNewCredit(credit+amount*99.4/100);
				bankCreditlogs.setCredit(credit);
				bankCreditlogs.setRemit(amount*99.4/100);
				bankCreditlogs.setRemark("referenceNo:"+pno);
				getHibernateTemplate().save(bankCreditlogs);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
	
	public void changeBfbAmountOnline(Integer type,double amount,String pno){
		DetachedCriteria c = DetachedCriteria.forClass(Bankinfo.class);
		c.add(Restrictions.eq("type", type));
		c.add(Restrictions.eq("useable", 0));
		
		List<Bankinfo> bankinfos=getHibernateTemplate().findByCriteria(c);
		Bankinfo bankinfo=null;
		
		
		try {
			if (bankinfos.size()!=0) {
				bankinfo = bankinfos.get(0);
				Double credit = bankinfo.getAmount();
//				bankinfo.setAmount(bankinfo.getAmount()+amount*99.5/100);
//				saveorupdate(bankinfo);
				
				//添加银行额度流水
				BankCreditlogs bankCreditlogs = new BankCreditlogs();
				bankCreditlogs.setCreatetime(DateUtil.getCurrentTimestamp());
				bankCreditlogs.setBankname(bankinfo.getUsername());
				bankCreditlogs.setType(dfh.model.enums.BankCreditChangeType.REPAIR_PAYORDER.getCode());
				bankCreditlogs.setNewCredit(credit+amount*99.5/100);
				bankCreditlogs.setCredit(credit);
				bankCreditlogs.setRemit(amount*99.5/100);
				bankCreditlogs.setRemark("referenceNo:"+pno);
				getHibernateTemplate().save(bankCreditlogs);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
	
	public void changeCommonGfbAmountOnline(CommonGfbEnum cmgfb,double amount,String pno){
		DetachedCriteria c = DetachedCriteria.forClass(Bankinfo.class);
		c.add(Restrictions.eq("type", cmgfb.getBankinfoType()));
		c.add(Restrictions.eq("useable", 0));
		
		List<Bankinfo> bankinfos=getHibernateTemplate().findByCriteria(c);
		Bankinfo bankinfo=null;
		
		
		try {
			if (bankinfos.size()!=0) {
				bankinfo = bankinfos.get(0);
				Double credit = bankinfo.getAmount();
//				bankinfo.setAmount(bankinfo.getAmount()+amount*cmgfb.getBankinfoRate());
//				saveorupdate(bankinfo);
				
				//添加银行额度流水
				BankCreditlogs bankCreditlogs = new BankCreditlogs();
				bankCreditlogs.setCreatetime(DateUtil.getCurrentTimestamp());
				bankCreditlogs.setBankname(bankinfo.getUsername());
				bankCreditlogs.setType(dfh.model.enums.BankCreditChangeType.REPAIR_PAYORDER.getCode());
				bankCreditlogs.setNewCredit(credit+amount*cmgfb.getBankinfoRate());
				bankCreditlogs.setCredit(credit);
				bankCreditlogs.setRemit(amount*cmgfb.getBankinfoRate());
				bankCreditlogs.setRemark("referenceNo:"+pno);
				getHibernateTemplate().save(bankCreditlogs);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}

	public LogDao getLogDao() {
		return logDao;
	}

	public void setLogDao(LogDao logDao) {
		this.logDao = logDao;
	}
	
	
	public void changeLfwxAmountOnline(Integer type,double amount,String pno){
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
//				saveorupdate(bankinfo);
				
				//添加银行额度流水
				BankCreditlogs bankCreditlogs = new BankCreditlogs();
				bankCreditlogs.setCreatetime(DateUtil.getCurrentTimestamp());
				bankCreditlogs.setBankname(bankinfo.getUsername());
				bankCreditlogs.setType(dfh.model.enums.BankCreditChangeType.REPAIR_PAYORDER.getCode());
				bankCreditlogs.setNewCredit(credit+amount*99.4/100);
				bankCreditlogs.setCredit(credit);
				bankCreditlogs.setRemit(amount*99.4/100);
				bankCreditlogs.setRemark("referenceNo:"+pno);
				getHibernateTemplate().save(bankCreditlogs);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}

	public void changeXbwxAmountOnline(Integer type,double amount,String pno){
		DetachedCriteria c = DetachedCriteria.forClass(Bankinfo.class);
		c.add(Restrictions.eq("type", type));
		c.add(Restrictions.eq("useable", 0));
		
		List<Bankinfo> bankinfos=getHibernateTemplate().findByCriteria(c);
		Bankinfo bankinfo=null;
		try {
			if (bankinfos.size()!=0) {
				bankinfo = bankinfos.get(0);
				Double credit = bankinfo.getAmount();
//				bankinfo.setAmount(bankinfo.getAmount()+amount*99.1/100);
//				saveorupdate(bankinfo);
				
				//添加银行额度流水
				BankCreditlogs bankCreditlogs = new BankCreditlogs();
				bankCreditlogs.setCreatetime(DateUtil.getCurrentTimestamp());
				bankCreditlogs.setBankname(bankinfo.getUsername());
				bankCreditlogs.setType(dfh.model.enums.BankCreditChangeType.REPAIR_PAYORDER.getCode());
				bankCreditlogs.setNewCredit(credit+amount*99.1/100);
				bankCreditlogs.setCredit(credit);
				bankCreditlogs.setRemit(amount*99.1/100);
				bankCreditlogs.setRemark("referenceNo:"+pno);
				getHibernateTemplate().save(bankCreditlogs);
			}
			
		} catch (Exception e) {
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
				Double credit = bankinfo.getAmount();
//				bankinfo.setAmount(bankinfo.getAmount()+amount*97.8/100);
//				saveorupdate(bankinfo);
				
				//添加银行额度流水
				BankCreditlogs bankCreditlogs = new BankCreditlogs();
				bankCreditlogs.setCreatetime(DateUtil.getCurrentTimestamp());
				bankCreditlogs.setBankname(bankinfo.getUsername());
				bankCreditlogs.setType(dfh.model.enums.BankCreditChangeType.REPAIR_PAYORDER.getCode());
				bankCreditlogs.setNewCredit(credit+amount*98.5/100);
				bankCreditlogs.setCredit(credit);
				bankCreditlogs.setRemit(amount*98.5/100);
				bankCreditlogs.setRemark("referenceNo:"+pno);
				getHibernateTemplate().save(bankCreditlogs);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
	
	public void changeKdWxZfAmountOnline(Integer type,Double amount,String pno){
		DetachedCriteria c = DetachedCriteria.forClass(Bankinfo.class);
		c.add(Restrictions.eq("type", type));
		c.add(Restrictions.eq("useable", 0));
		
		List<Bankinfo> bankinfos=getHibernateTemplate().findByCriteria(c);
		Bankinfo bankinfo=null;
		try {
			if (bankinfos.size()!=0) {
				bankinfo = bankinfos.get(0);
				Double credit = bankinfo.getAmount();
//				bankinfo.setAmount(bankinfo.getAmount()+amount*98.6/100);
//				saveorupdate(bankinfo);
				
				//添加银行额度流水
				BankCreditlogs bankCreditlogs = new BankCreditlogs();
				bankCreditlogs.setCreatetime(DateUtil.getCurrentTimestamp());
				bankCreditlogs.setBankname(bankinfo.getUsername());
				bankCreditlogs.setType(dfh.model.enums.BankCreditChangeType.REPAIR_PAYORDER.getCode());
				bankCreditlogs.setNewCredit(credit+amount*98.6/100);
				bankCreditlogs.setCredit(credit);
				bankCreditlogs.setRemit(amount*98.6/100);
				bankCreditlogs.setRemark("referenceNo:"+pno);
				getHibernateTemplate().save(bankCreditlogs);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
	
	
	/*****
	 * 口袋微信支付2
	 * @param type
	 * @param amount
	 * @param pno
	 */
	public void changeKdWxZfAmountOnline2(Integer type,double amount,String pno){
		DetachedCriteria c = DetachedCriteria.forClass(Bankinfo.class);
		c.add(Restrictions.eq("type", type));
		c.add(Restrictions.eq("useable", 0));
		
		List<Bankinfo> bankinfos=getHibernateTemplate().findByCriteria(c);
		Bankinfo bankinfo=null;
		try {
			if (bankinfos.size()!=0) {
				bankinfo = bankinfos.get(0);
				Double credit = bankinfo.getAmount();
//				bankinfo.setAmount(bankinfo.getAmount()+amount*98.6/100);
//				saveorupdate(bankinfo);
				
				//添加银行额度流水
				BankCreditlogs bankCreditlogs = new BankCreditlogs();
				bankCreditlogs.setCreatetime(DateUtil.getCurrentTimestamp());
				bankCreditlogs.setBankname(bankinfo.getUsername());
				bankCreditlogs.setType(dfh.model.enums.BankCreditChangeType.REPAIR_PAYORDER.getCode());
				bankCreditlogs.setNewCredit(credit+amount*98.6/100);
				bankCreditlogs.setCredit(credit);
				bankCreditlogs.setRemit(amount*98.6/100);
				bankCreditlogs.setRemark("referenceNo:"+pno);
				getHibernateTemplate().save(bankCreditlogs);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
	
	/*****
	 * 口袋微信支付3
	 * @param type
	 * @param amount
	 * @param pno
	 */
	public void changeKdWxZfAmountOnline3(Integer type,double amount,String pno){
		DetachedCriteria c = DetachedCriteria.forClass(Bankinfo.class);
		c.add(Restrictions.eq("type", type));
		c.add(Restrictions.eq("useable", 0));
		
		List<Bankinfo> bankinfos=getHibernateTemplate().findByCriteria(c);
		Bankinfo bankinfo=null;
		try {
			if (bankinfos.size()!=0) {
				bankinfo = bankinfos.get(0);
				Double credit = bankinfo.getAmount();
//				bankinfo.setAmount(bankinfo.getAmount()+amount*98.6/100);
//				saveorupdate(bankinfo);
				
				//添加银行额度流水
				BankCreditlogs bankCreditlogs = new BankCreditlogs();
				bankCreditlogs.setCreatetime(DateUtil.getCurrentTimestamp());
				bankCreditlogs.setBankname(bankinfo.getUsername());
				bankCreditlogs.setType(dfh.model.enums.BankCreditChangeType.REPAIR_PAYORDER.getCode());
				bankCreditlogs.setNewCredit(credit+amount*98.6/100);
				bankCreditlogs.setCredit(credit);
				bankCreditlogs.setRemit(amount*98.6/100);
				bankCreditlogs.setRemark("referenceNo:"+pno);
				getHibernateTemplate().save(bankCreditlogs);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
	
	/**
	 * 海尔审核更新
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
//				bankinfo.setAmount(bankinfo.getAmount()+amount*99.5/100);
//				saveorupdate(bankinfo);
				
				//添加银行额度流水
				BankCreditlogs bankCreditlogs = new BankCreditlogs();
				bankCreditlogs.setCreatetime(DateUtil.getCurrentTimestamp());
				bankCreditlogs.setBankname(bankinfo.getUsername());
				bankCreditlogs.setType(dfh.model.enums.BankCreditChangeType.REPAIR_PAYORDER.getCode());
				bankinfo.setAmount(credit+amount*99.5/100);
				bankCreditlogs.setCredit(credit);
				bankCreditlogs.setRemit(amount*99.5/100);
				bankCreditlogs.setRemark("referenceNo:"+pno);
				getHibernateTemplate().save(bankCreditlogs);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
	
	
	/**
	 * 迅联宝网银审核更新
	 * @param type
	 * @param amount
	 * @param pno
	 */
	public void changeXlbWyAmountOnline(Integer type,double amount,String pno){
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
//				saveorupdate(bankinfo);
				
				//添加银行额度流水
				BankCreditlogs bankCreditlogs = new BankCreditlogs();
				bankCreditlogs.setCreatetime(DateUtil.getCurrentTimestamp());
				bankCreditlogs.setBankname(bankinfo.getUsername());
				bankCreditlogs.setType(dfh.model.enums.BankCreditChangeType.REPAIR_PAYORDER.getCode());
				bankinfo.setAmount(credit+amount*99.4/100);
				bankCreditlogs.setCredit(credit);
				bankCreditlogs.setRemit(amount*99.4/100);
				bankCreditlogs.setRemark("referenceNo:"+pno);
				getHibernateTemplate().save(bankCreditlogs);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
	
	/******
	 * 优付支付宝 审核
	 * @param type
	 * @param amount
	 * @param pno
	 */
	public void changeYfZfbAmountOnline(Integer type,double amount,String pno){
		DetachedCriteria c = DetachedCriteria.forClass(Bankinfo.class);
		c.add(Restrictions.eq("type", type));
		c.add(Restrictions.eq("useable", 0));
		
		List<Bankinfo> bankinfos=getHibernateTemplate().findByCriteria(c);
		Bankinfo bankinfo=null;
		try {
			if (bankinfos.size()!=0) {
				bankinfo = bankinfos.get(0);
				Double credit = bankinfo.getAmount();
//				bankinfo.setAmount(bankinfo.getAmount()+amount*98.2/100);
//				saveorupdate(bankinfo);
				
				//添加银行额度流水
				BankCreditlogs bankCreditlogs = new BankCreditlogs();
				bankCreditlogs.setCreatetime(DateUtil.getCurrentTimestamp());
				bankCreditlogs.setBankname(bankinfo.getUsername());
				bankCreditlogs.setType(dfh.model.enums.BankCreditChangeType.REPAIR_PAYORDER.getCode());
				bankCreditlogs.setNewCredit(credit+amount*98.2/100);
				bankCreditlogs.setCredit(credit);
				bankCreditlogs.setRemit(amount*98.2/100);
				bankCreditlogs.setRemark("referenceNo:"+pno);
				getHibernateTemplate().save(bankCreditlogs);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
	
	/*
	 * 汇潮一麻袋网银
	 */

	public void changeHcYmdAmountOnline(Integer type,double amount,String pno){
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
//				saveorupdate(bankinfo);
				//添加银行额度流水
				BankCreditlogs bankCreditlogs = new BankCreditlogs();
				bankCreditlogs.setCreatetime(DateUtil.getCurrentTimestamp());
				bankCreditlogs.setBankname(bankinfo.getUsername());
				bankCreditlogs.setType(dfh.model.enums.BankCreditChangeType.REPAIR_PAYORDER.getCode());
				bankCreditlogs.setNewCredit(credit+amount*99.4/100);
				bankCreditlogs.setCredit(credit);
				bankCreditlogs.setRemit(amount*99.4/100);
				bankCreditlogs.setRemark("referenceNo:"+pno);
				getHibernateTemplate().save(bankCreditlogs);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
	
	
	/**
	 * 汇付宝审核更新
	 * @param type
	 * @param amount
	 * @param pno
	 */
	public void changeHHbWxZfAmountOnline(Integer type,double amount,String pno){
		DetachedCriteria c = DetachedCriteria.forClass(Bankinfo.class);
		c.add(Restrictions.eq("type", type));
		c.add(Restrictions.eq("useable", 0));
		
		List<Bankinfo> bankinfos=getHibernateTemplate().findByCriteria(c);
		Bankinfo bankinfo=null;
		try {
			if (bankinfos.size()!=0) {
				bankinfo = bankinfos.get(0);
				Double credit = bankinfo.getAmount();
//				bankinfo.setAmount(bankinfo.getAmount()+amount*98.7/100);
//				saveorupdate(bankinfo);
				
				//添加银行额度流水
				BankCreditlogs bankCreditlogs = new BankCreditlogs();
				bankCreditlogs.setCreatetime(DateUtil.getCurrentTimestamp());
				bankCreditlogs.setBankname(bankinfo.getUsername());
				bankCreditlogs.setType(dfh.model.enums.BankCreditChangeType.REPAIR_PAYORDER.getCode());
				bankinfo.setAmount(credit+amount*98.7/100);
				bankCreditlogs.setCredit(credit);
				bankCreditlogs.setRemit(amount*98.7/100);
				bankCreditlogs.setRemark("referenceNo:"+pno);
				getHibernateTemplate().save(bankCreditlogs);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}

	
	/******
	 * 聚宝支付宝 审核
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
				Double credit = bankinfo.getAmount();
//				bankinfo.setAmount(bankinfo.getAmount()+amount*97.7/100);
//				saveorupdate(bankinfo);
				
				//添加银行额度流水
				BankCreditlogs bankCreditlogs = new BankCreditlogs();
				bankCreditlogs.setCreatetime(DateUtil.getCurrentTimestamp());
				bankCreditlogs.setBankname(bankinfo.getUsername());
				bankCreditlogs.setType(dfh.model.enums.BankCreditChangeType.REPAIR_PAYORDER.getCode());
				bankCreditlogs.setNewCredit(credit+amount*97.7/100);
				bankCreditlogs.setCredit(credit);
				bankCreditlogs.setRemit(amount*97.7/100);
				bankCreditlogs.setRemark("referenceNo:"+pno);
				getHibernateTemplate().save(bankCreditlogs);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
	
	/**
	 * 迅联宝微信
	 * @param type
	 * @param amount
	 * @param pno
	 */
	public void changeXlbAmountOnline(Integer type,double amount,String pno){
		DetachedCriteria c = DetachedCriteria.forClass(Bankinfo.class);
		c.add(Restrictions.eq("type", type));
		c.add(Restrictions.eq("useable", 0));
		List<Bankinfo> bankinfos=getHibernateTemplate().findByCriteria(c);
		Bankinfo bankinfo=null;
		try {
			if (bankinfos.size()!=0) {
				bankinfo = bankinfos.get(0);
				Double credit = bankinfo.getAmount();
//				bankinfo.setAmount(bankinfo.getAmount()+amount*98.5/100);
//				saveorupdate(bankinfo);
				//添加银行额度流水
				BankCreditlogs bankCreditlogs = new BankCreditlogs();
				bankCreditlogs.setCreatetime(DateUtil.getCurrentTimestamp());
				bankCreditlogs.setBankname(bankinfo.getUsername());
				bankCreditlogs.setType(dfh.model.enums.BankCreditChangeType.REPAIR_PAYORDER.getCode());
				bankCreditlogs.setNewCredit(credit+amount*98.7/100);
				bankCreditlogs.setCredit(credit);
				bankCreditlogs.setRemit(amount*98.7/100);
				bankCreditlogs.setRemark("referenceNo:"+pno);
				getHibernateTemplate().save(bankCreditlogs);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
	
	public void changeWechatAmountOnline(String wxh,double amount,String pno){
		DetachedCriteria c = DetachedCriteria.forClass(Bankinfo.class);
		c.add(Restrictions.eq("accountno", wxh));
		c.add(Restrictions.eq("useable", 0));
		
		List<Bankinfo> bankinfos=getHibernateTemplate().findByCriteria(c);
		Bankinfo bankinfo=null;
		try {
			if (bankinfos.size()!=0) {
				bankinfo = bankinfos.get(0);
				Double credit = bankinfo.getAmount();
//				bankinfo.setAmount(bankinfo.getAmount()+amount);
//				saveorupdate(bankinfo);
				//添加银行额度流水
				BankCreditlogs bankCreditlogs = new BankCreditlogs();
				bankCreditlogs.setCreatetime(DateUtil.getCurrentTimestamp());
				bankCreditlogs.setBankname(bankinfo.getUsername());
				bankCreditlogs.setType(dfh.model.enums.BankCreditChangeType.REPAIR_PAYORDER.getCode());
				bankCreditlogs.setNewCredit(credit+amount);
				bankCreditlogs.setCredit(credit);
				bankCreditlogs.setRemit(amount);
				bankCreditlogs.setRemark("referenceNo:"+pno);
				getHibernateTemplate().save(bankCreditlogs);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
	
	/******
	 * 优付微信 审核
	 * @param type
	 * @param amount
	 * @param pno
	 */
	public void changeYfWxAmountOnline(Integer type,double amount,String pno){
		DetachedCriteria c = DetachedCriteria.forClass(Bankinfo.class);
		c.add(Restrictions.eq("type", type));
		c.add(Restrictions.eq("useable", 0));
		
		List<Bankinfo> bankinfos=getHibernateTemplate().findByCriteria(c);
		Bankinfo bankinfo=null;
		try {
			if (bankinfos.size()!=0) {
				bankinfo = bankinfos.get(0);
				Double credit = bankinfo.getAmount();
//				bankinfo.setAmount(bankinfo.getAmount()+amount*99.0/100);
//				saveorupdate(bankinfo);
				
				//添加银行额度流水
				BankCreditlogs bankCreditlogs = new BankCreditlogs();
				bankCreditlogs.setCreatetime(DateUtil.getCurrentTimestamp());
				bankCreditlogs.setBankname(bankinfo.getUsername());
				bankCreditlogs.setType(dfh.model.enums.BankCreditChangeType.REPAIR_PAYORDER.getCode());
				bankCreditlogs.setNewCredit(credit+amount*99.0/100);
				bankCreditlogs.setCredit(credit);
				bankCreditlogs.setRemit(amount*99.0/100);
				bankCreditlogs.setRemark("referenceNo:"+pno);
				getHibernateTemplate().save(bankCreditlogs);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}

	/******
	 * 新贝支付宝 审核
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
				Double credit = bankinfo.getAmount();
//				bankinfo.setAmount(bankinfo.getAmount()+amount*98.8/100);
//				saveorupdate(bankinfo);
				
				//添加银行额度流水
				BankCreditlogs bankCreditlogs = new BankCreditlogs();
				bankCreditlogs.setCreatetime(DateUtil.getCurrentTimestamp());
				bankCreditlogs.setBankname(bankinfo.getUsername());
				bankCreditlogs.setType(dfh.model.enums.BankCreditChangeType.REPAIR_PAYORDER.getCode());
				bankCreditlogs.setNewCredit(credit+amount*98.8/100);
				bankCreditlogs.setCredit(credit);
				bankCreditlogs.setRemit(amount*98.8/100);
				bankCreditlogs.setRemark("referenceNo:"+pno);
				getHibernateTemplate().save(bankCreditlogs);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}

	/******
	 * 银宝支付宝 审核
	 * @param type
	 * @param amount
	 * @param pno
	 */
	public void changeYbZfbAmountOnline(Integer type,double amount,String pno){
		DetachedCriteria c = DetachedCriteria.forClass(Bankinfo.class);
		c.add(Restrictions.eq("type", type));
		c.add(Restrictions.eq("useable", 0));
		
		List<Bankinfo> bankinfos=getHibernateTemplate().findByCriteria(c);
		Bankinfo bankinfo=null;
		try {
			if (bankinfos.size()!=0) {
				bankinfo = bankinfos.get(0);
				Double credit = bankinfo.getAmount();
//				bankinfo.setAmount(bankinfo.getAmount()+amount*98.8/100);
//				saveorupdate(bankinfo);
				
				//添加银行额度流水
				BankCreditlogs bankCreditlogs = new BankCreditlogs();
				bankCreditlogs.setCreatetime(DateUtil.getCurrentTimestamp());
				bankCreditlogs.setBankname(bankinfo.getUsername());
				bankCreditlogs.setType(dfh.model.enums.BankCreditChangeType.REPAIR_PAYORDER.getCode());
				bankCreditlogs.setNewCredit(credit+amount*98.8/100);
				bankCreditlogs.setCredit(credit);
				bankCreditlogs.setRemit(amount*98.8/100);
				bankCreditlogs.setRemark("referenceNo:"+pno);
				getHibernateTemplate().save(bankCreditlogs);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
	
	/******
	 * 千网支付宝 审核
	 * @param type
	 * @param amount
	 * @param pno
	 */
	public void changeQwZfbAmountOnline(Integer type,double amount,String pno){
		DetachedCriteria c = DetachedCriteria.forClass(Bankinfo.class);
		c.add(Restrictions.eq("type", type));
		c.add(Restrictions.eq("useable", 0));
		
		List<Bankinfo> bankinfos=getHibernateTemplate().findByCriteria(c);
		Bankinfo bankinfo=null;
		try {
			if (bankinfos.size()!=0) {
				bankinfo = bankinfos.get(0);
				Double credit = bankinfo.getAmount();
//				bankinfo.setAmount(bankinfo.getAmount()+amount*98.2/100);
//				saveorupdate(bankinfo);
				
				//添加银行额度流水
				BankCreditlogs bankCreditlogs = new BankCreditlogs();
				bankCreditlogs.setCreatetime(DateUtil.getCurrentTimestamp());
				bankCreditlogs.setBankname(bankinfo.getUsername());
				bankCreditlogs.setType(dfh.model.enums.BankCreditChangeType.REPAIR_PAYORDER.getCode());
				bankCreditlogs.setNewCredit(credit+amount*98.2/100);
				bankCreditlogs.setCredit(credit);
				bankCreditlogs.setRemit(amount*98.2/100);
				bankCreditlogs.setRemark("referenceNo:"+pno);
				getHibernateTemplate().save(bankCreditlogs);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
	
	/*****
	 * 口袋支付宝2
	 * @param type
	 * @param amount
	 * @param pno
	 */
	public void changeKdZfb2AmountOnline(Integer type,double amount,String pno){
		DetachedCriteria c = DetachedCriteria.forClass(Bankinfo.class);
		c.add(Restrictions.eq("type", type));
		c.add(Restrictions.eq("useable", 0));
		
		List<Bankinfo> bankinfos=getHibernateTemplate().findByCriteria(c);
		Bankinfo bankinfo=null;
		try {
			if (bankinfos.size()!=0) {
				bankinfo = bankinfos.get(0);
				Double credit = bankinfo.getAmount();
//				bankinfo.setAmount(bankinfo.getAmount()+amount*98.5/100);
//				saveorupdate(bankinfo);
				
				//添加银行额度流水
				BankCreditlogs bankCreditlogs = new BankCreditlogs();
				bankCreditlogs.setCreatetime(DateUtil.getCurrentTimestamp());
				bankCreditlogs.setBankname(bankinfo.getUsername());
				bankCreditlogs.setType(dfh.model.enums.BankCreditChangeType.REPAIR_PAYORDER.getCode());
				bankCreditlogs.setNewCredit(credit+amount*98.2/100);
				bankCreditlogs.setCredit(credit);
				bankCreditlogs.setRemit(amount*98.2/100);
				bankCreditlogs.setRemark("referenceNo:"+pno);
				getHibernateTemplate().save(bankCreditlogs);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
	
	/******
	 * 千网微信 审核
	 * @param type
	 * @param amount
	 * @param pno
	 */
	public void changeQwWxAmountOnline(Integer type,double amount,String pno){
		DetachedCriteria c = DetachedCriteria.forClass(Bankinfo.class);
		c.add(Restrictions.eq("type", type));
		c.add(Restrictions.eq("useable", 0));
		
		List<Bankinfo> bankinfos=getHibernateTemplate().findByCriteria(c);
		Bankinfo bankinfo=null;
		try {
			if (bankinfos.size()!=0) {
				bankinfo = bankinfos.get(0);
				Double credit = bankinfo.getAmount();
				//bankinfo.setAmount(bankinfo.getAmount()+amount*99/100);
				//saveorupdate(bankinfo);
				
				//添加银行额度流水
				BankCreditlogs bankCreditlogs = new BankCreditlogs();
				bankCreditlogs.setCreatetime(DateUtil.getCurrentTimestamp());
				bankCreditlogs.setBankname(bankinfo.getUsername());
				bankCreditlogs.setType(dfh.model.enums.BankCreditChangeType.REPAIR_PAYORDER.getCode());
				bankCreditlogs.setNewCredit(credit+amount*99/100);
				bankCreditlogs.setCredit(credit);
				bankCreditlogs.setRemit(amount*99/100);
				bankCreditlogs.setRemark("referenceNo:"+pno);
				getHibernateTemplate().save(bankCreditlogs);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
	
	/*****
	 * 迅联宝支付宝
	 * @param type
	 * @param amount
	 * @param pno
	 */
	public void changeXlbZfbAmountOnline(Integer type,double amount,String pno){
		DetachedCriteria c = DetachedCriteria.forClass(Bankinfo.class);
		c.add(Restrictions.eq("type", type));
		c.add(Restrictions.eq("useable", 0));
		
		List<Bankinfo> bankinfos=getHibernateTemplate().findByCriteria(c);
		Bankinfo bankinfo=null;
		try {
			if (bankinfos.size()!=0) {
				bankinfo = bankinfos.get(0);
				Double credit = bankinfo.getAmount();
				bankinfo.setAmount(bankinfo.getAmount()+amount*98.5/100);
				//bankinfo.setAmount(bankinfo.getAmount()+amount*CommonHaierEnum.getRate("haier"));
				//saveorupdate(bankinfo);
				
				//添加银行额度流水
				BankCreditlogs bankCreditlogs = new BankCreditlogs();
				bankCreditlogs.setCreatetime(DateUtil.getCurrentTimestamp());
				bankCreditlogs.setBankname(bankinfo.getUsername());
				bankCreditlogs.setType(dfh.model.enums.BankCreditChangeType.REPAIR_PAYORDER.getCode());
				bankinfo.setAmount(credit+amount*98.5/100);
				bankCreditlogs.setCredit(credit);
				bankCreditlogs.setRemit(amount*98.5/100);
				bankCreditlogs.setRemark("referenceNo:"+pno);
				getHibernateTemplate().save(bankCreditlogs);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
	
    public MerchantPayDao getMerchantPayDao() {
        return merchantPayDao;
    }

    public void setMerchantPayDao(MerchantPayDao merchantPayDao) {
        this.merchantPayDao = merchantPayDao;
    }
    
	public int updateBankAmountSql(Double amount,Date updateTime, Integer id){    
		Query insertQuery = this.getSession().createSQLQuery("UPDATE bankinfo set bankamount =  ?,updatetime = ?   WHERE id=? ");       
		insertQuery.setParameter(0, amount).setParameter(1, updateTime).setParameter(2, id);
		return insertQuery.executeUpdate() ;
    }
	
}
