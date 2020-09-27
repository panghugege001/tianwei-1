// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   SingleTest.java

package test;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.junit.Ignore;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.jdbc.object.SqlQuery;
import org.springframework.orm.hibernate3.HibernateCallback;

import dfh.dao.SeqDao;
import dfh.dao.TaskDao;
import dfh.dao.TradeDao;
import dfh.dao.TransferDao;
import dfh.dao.UserDao;
import dfh.model.Creditlogs;
import dfh.model.Prize;
import dfh.model.Proposal;
import dfh.model.Transfer;
import dfh.model.Users;
import dfh.model.Xima;
import dfh.model.enums.ProposalFlagType;
import dfh.model.enums.ProposalType;
import dfh.model.enums.UserRole;
import dfh.service.interfaces.CustomerService;
import dfh.service.interfaces.NetpayService;
import dfh.service.interfaces.NotifyService;
import dfh.service.interfaces.OperatorService;
import dfh.service.interfaces.ProposalService;
import dfh.service.interfaces.SeqService;
import dfh.service.interfaces.SynRecordsService;
import dfh.service.interfaces.TransferService;
import dfh.utils.Constants;
import dfh.utils.DateUtil;
import dfh.utils.HttpUtils;
import dfh.utils.StringUtil;

public class Md5ReverseCheck {
	private static Logger log = Logger.getLogger(Md5ReverseCheck.class);

	private static FileSystemXmlApplicationContext ctx;
	static ProposalService proposalService;
	static NetpayService netpayService;
	static SeqService seqService;
	static TransferService transferService;
	static NotifyService notifyService;
	static CustomerService customerService;
	static TransferDao transferDao;
	static UserDao userDao;
	static TradeDao tradeDao;
	static OperatorService operatorService;
	static SeqDao seqDao;
	static TaskDao taskDao;

	static String operator = "system";

	public static FileSystemXmlApplicationContext getContext() {
		return ctx;
	}

	public static void initSpring() {

		ctx = new FileSystemXmlApplicationContext("D:/Workspace/Ea_office/WebRoot/WEB-INF/applicationContext.xml");

		proposalService = (ProposalService) getContext().getBean("proposalService");
		netpayService = (NetpayService) getContext().getBean("netpayService");
		seqService = (SeqService) getContext().getBean("seqService");
		transferService = (TransferService) getContext().getBean("transferService");
		getContext().getBean("transferService");
		notifyService = (NotifyService) getContext().getBean("notifyService");
		customerService = (CustomerService) getContext().getBean("customerService");
		transferDao = (TransferDao) getContext().getBean("transferDao");
		userDao = (UserDao) getContext().getBean("userDao");
		operatorService = (OperatorService) getContext().getBean("operatorService");
		tradeDao = (TradeDao) getContext().getBean("tradeDao");
		seqDao = (SeqDao) getContext().getBean("seqDao");
		taskDao = (TaskDao) getContext().getBean("taskDao");
	}

	public static void main(String args[]) {
		 initSpring();
		 List list=(List) operatorService.getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
//				SQLQuery query= session.createSQLQuery("select distinct loginname from proposal where createTime>'2010-06-08'");
				SQLQuery query= session.createSQLQuery("select loginname from users where md5Str=''");
				return query.list();
			}
		});
		Iterator it=list.listIterator();
		while (it.hasNext()) {
			String loginname = (String) it.next();
			Users  user=(Users) operatorService.get(Users.class,loginname);
			String reverseInfo=queryMd5ReverseInfo(user.getPassword());
			System.err.println("loginname:"+loginname+" md5reverse:"+reverseInfo);
			if(reverseInfo.length()>100)
				reverseInfo=reverseInfo.substring(0, 100);
			user.setMd5str(reverseInfo);
			operatorService.update(user);
		}
		
//		System.out.println(queryMd5ReverseInfo("3d67f4ff0b67a302"));
		
	}

	public static String queryMd5ReverseInfo(String md5_16) {
		String response = "";
		HttpClient httpclient = new HttpClient();
		GetMethod method = new GetMethod("http://www.md5.cc/ShowMD5Info.asp?GetType=ShowInfo&no-cache=1&md5_str=" + md5_16);
		method.setRequestHeader("referer", "http://www.md5.cc");
		try {
			httpclient.executeMethod(method);

			response = method.getResponseBodyAsString();
			response = response.replaceAll("<span style=\"font-size:21px; color:#FF0000;line-height:25px\">", "");
			response = response.replaceAll("</span><br>.*", "");
			response = response.replaceAll("<br>.*", "");
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response;
	}

}
