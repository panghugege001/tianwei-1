// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   LogDao.java

package dfh.dao;

import dfh.model.Actionlogs;
import dfh.model.Creditlogs;
import dfh.model.Operationlogs;
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
}
