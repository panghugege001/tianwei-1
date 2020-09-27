// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   LogDao.java

package dfh.dao;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.springframework.dao.DataAccessResourceFailureException;

import dfh.model.Actionlogs;
import dfh.model.Creditlogs;
import dfh.model.Operationlogs;
import dfh.model.Users;
import dfh.model.enums.ActionLogType;
import dfh.model.enums.OperationLogType;
import dfh.utils.DateUtil;

// Referenced classes of package dfh.dao:
//			UniversalDao
public class LogDao extends UniversalDao
{

	public LogDao()
	{
	}

	public void insertActionLog(String loginname, ActionLogType action, String remark)
	{
		Actionlogs log = new Actionlogs(loginname, action.getCode(), DateUtil.now(), remark);
		save(log);
	}

	public void insertCreditLog(String loginname, String type, Double credit, Double remit, Double newCredit, String remark)
	{
		Creditlogs log = new Creditlogs();
		log.setLoginname(loginname);
		log.setType(type);
		log.setCredit(credit);
		log.setRemit(remit);
		log.setNewCredit(newCredit);
		log.setRemark(remark);
		log.setCreatetime(DateUtil.getCurrentTimestamp());
		save(log);
	}

	public void insertOperationLog(String loginname, OperationLogType action, String remark)
	{
		Operationlogs log = new Operationlogs();
		log.setLoginname(loginname);
		log.setAction(action.getCode());
		log.setRemark(remark);
		log.setCreatetime(DateUtil.getCurrentTimestamp());
		save(log);
	}
	
	public void updateUserSql(Users customer){
		try {
			String sql = "UPDATE users set randnum = ?,lastLoginIp=?,area=?,clientos=?,postcode=?,lastLoginTime=?,loginTimes=? WHERE loginname=?";
			Query insertQuery = this.getSession().createSQLQuery(sql);
			insertQuery.setParameter(0, customer.getRandnum())
			.setParameter(1, customer.getLastLoginIp())
			.setParameter(2, customer.getArea())
			.setParameter(3, customer.getClientos())
			.setParameter(4, customer.getPostcode())
			.setParameter(5, customer.getLastLoginTime())
			.setParameter(6, customer.getLoginTimes())
			.setParameter(7, customer.getLoginname());
			insertQuery.executeUpdate();
			System.out.println(sql);
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.toString());
		}
	}
}
