package dfh.dao;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class BankStatusDao extends HibernateDaoSupport{
	
	public String getBankStatus(String bankname) {
		String status = (String)getSession().createQuery("select status from BankStatus where bankname = :bankname").setString("bankname",bankname).uniqueResult();
		return status;
	}
	
}