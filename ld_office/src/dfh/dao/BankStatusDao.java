package dfh.dao;

import java.util.List;

import org.hibernate.Query;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import dfh.model.BankStatus;

public class BankStatusDao extends HibernateDaoSupport{
	
	public String getBankStatus(String bankname) {
		String status = (String)getSession().createQuery("select status from BankStatus where bankname = :bankname").setString("bankname",bankname).uniqueResult();
		return status;
	}
	
	@SuppressWarnings("unchecked")
	public List<BankStatus> getBankStatusList() {
		Query query = getSession().createQuery("from BankStatus");
		List<BankStatus> bankStatusList = query.list();
		return bankStatusList;
	}
	
	public void updateBankStatus(Integer id,String status) {
		Query query = getSession().createQuery("update BankStatus set status =:status where id = :id").setString("status",status).setInteger("id", id);
		query.executeUpdate();
	}
	
}
