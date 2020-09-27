// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   UniversalDao.java

package dfh.dao;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.ibm.icu.text.SimpleDateFormat;

import dfh.model.Customer;
import dfh.model.Proposal;
import dfh.model.SystemConfig;
import dfh.model.Users;
import dfh.utils.AESUtil;
import dfh.utils.Constants;
import dfh.utils.Arith;
import org.apache.log4j.Logger;
import java.util.HashMap;

public class ChangeLineUserDao extends HibernateDaoSupport {
	private static Logger log = Logger.getLogger(OperatorDao.class);
	public ChangeLineUserDao() {
	}

	   public List<Object[]> queryUserByCondition() {
		    Calendar cal = Calendar.getInstance(); 
	        cal.set(Calendar.HOUR_OF_DAY, 0); 
	        cal.set(Calendar.SECOND, 0); 
	        cal.set(Calendar.MINUTE, 0); 
	        cal.set(Calendar.MILLISECOND, 0); 
		    cal.add(Calendar.DATE, -30);
		    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);  
		    String weekDay = format.format(cal.getTime());
	    	StringBuffer sbf = new StringBuffer("select distinct u.loginname, u.accountName,u.phone,u.email,u.flag,u.qq,u.intro,u.agent,u.createtime  from  users u where  lastLoginTime    < '");
			sbf.append(weekDay).append("' and isCashin='0'  and  u.flag != '1' and u.intro  like '%cs%' ");
			System.out.println("sbf===" + sbf);
			return this.getSession().createSQLQuery(sbf.toString()).list();
		}

	   public String [] getAgentList() {
		   StringBuffer hql = new StringBuffer("from  SystemConfig where typeNo=?  ");
			Query query = this.getSession().createQuery(hql.toString());
			query.setParameter(0, "type101");
			List<SystemConfig> systemConfig = query.list();
			String [] strArray = null;
			if(systemConfig != null && systemConfig.size() != 0 ){
				  for(int i=0;i< systemConfig.size(); i++) {
					  SystemConfig object = systemConfig.get(i);
					  if(object.getValue() != null ) {
						return  ( (String)object.getValue()).split(";");
					  }else {
						  return null;
					  }
				  }
			}
			return null;
	   }
	   
	   
	   public   synchronized void insertinfo( Object[] users,String cs)throws Exception {

		   String email =(String) users[3];
		   String phone = (String) users[2];
		   String accountname = (String) users[1];
		   String loginname = (String) users[0];
		   String qq =(String) users[5];
		   String intro = (String) users[6];
		  log.info("email:"+email + "  phone:"+phone  + "  accountname:"+accountname + "  loginname:"+loginname + "  qq:"+qq+ "  intro:"+intro     );
				if (StringUtils.isNotBlank(email) && !email.contains("@")) {
					email = AESUtil.aesDecrypt(email, AESUtil.KEY);
				}
				if (StringUtils.isNotBlank(phone)
						&& !dfh.utils.StringUtil.isNumeric(phone)) {
					phone = AESUtil.aesDecrypt(phone, AESUtil.KEY);
				}
				Calendar calendar = Calendar.getInstance();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String date = sdf.format(calendar.getTime()); // date 是 换线时间
//				String sqlInsert = "insert into other_customer (name,email,phone,isreg,isdeposit,phonestatus,userstatus,cs,remark,createTime,batch,type,qq) VALUES "
//						+ "('"
//						+ accountname
//						+ "','"
//						+ email
//						+ "','"
//						+ phone
//						+ "','1','0','0','0','"
//						+ cs
//						+ "','"
//						+ loginname
//						+ ","
//						+ intro + ",','" + date + "','1','und','" + qq + "')";
//			    Query query =  this.getSession().createSQLQuery(sqlInsert);
//	            query.executeUpdate();
				Query query = null;
				String sqlInsert ="";
	            Customer customer = new Customer();
	            customer.setName(accountname);
	            customer.setEmail(email);
	            customer.setPhone(phone);
	            customer.setIsreg(1);
	            customer.setIsdeposit(0);
	            customer.setPhonestatus(0);
	            customer.setUserstatus(0);
	            customer.setCs(cs);
	            customer.setRemark(loginname+","+intro);
	            customer.setCreateTime(calendar.getTime());
	            customer.setBatch(1);
	            customer.setType("und");
	            customer.setQq(qq);
	            
	            getHibernateTemplate().save(customer);
	        	// 更新玩家所属客服
				String sqlUpdateUser = "update users set intro='" + cs
						+ "', warnremark=null where loginname='" + loginname + "'";
				System.out.println("sqlUpdateUser==" + sqlUpdateUser);
				query =  this.getSession().createSQLQuery(sqlUpdateUser.toString());
	            query.executeUpdate();
	            Date createtime   =  (Date) users[8]; 
				sqlInsert = "insert into change_line_user(changelinetime,username,codeafter,codebefore,createTime) values( '"
				+ date
				+ "','"
				+ loginname
				+ "','"
				+ cs
				+ "','"
				+ intro
				+ "','"
				+ createtime
				+ "' "
				+	")";
				query =  this.getSession().createSQLQuery(sqlInsert);
	            query.executeUpdate();
	   }

	
	   public double[] getDepositAndWinorlose(String loginname,Date createtime,Date endTimeDeposit) {
		   Map<String, Object> params = new HashMap<String, Object>();
			String sqlStr = "select sum(money) from payorder where loginname=:loginname and type=:orderType and createTime>=:startTimeDeposit and createTime<=:endTimeDeposit and flag=:flag";
			params.put("loginname", loginname);
			params.put("orderType", 0);  //支付成功的订单
			params.put("flag", 0);  
			params.put("startTimeDeposit", createtime );  // 开始时间 
			params.put("endTimeDeposit",endTimeDeposit ); // 换线时间 
			Query query =  this.getSession().createSQLQuery(sqlStr);
			query.setProperties(params);
			Object obj1 = query.uniqueResult();
			sqlStr = "select sum(amount) from proposal where loginname=:loginname and type=:pType and flag=:optFlag and createTime>=:startTimeDeposit and createTime<=:endTimeDeposit";
			params.put("pType", 502);   //存款提案
			params.put("optFlag", 2);   //已执行
			query = this.getSession().createSQLQuery(sqlStr);
			query.setProperties(params);
			Object obj2 = query.uniqueResult();
			//  存款总金额
			  Double deposit = 0.0;
			deposit = Arith.add(Double.parseDouble(obj1==null ? "0"  : obj1.toString()), Double.parseDouble(obj2==null ? "0" :obj2.toString()));
			// 往换线用户表插入换线数据
			//提款总金额 
			sqlStr = "select sum(amount) from proposal where loginname=:loginname and type=:pType and flag=:optFlag and createTime>=:startTimeDeposit and createTime<=:endTimeDeposit";
			params.put("pType", 503);   //提款提案
			params.put("optFlag", 2);   //已执行
			query = this.getSession().createSQLQuery(sqlStr);
			query.setProperties(params);
			Object obj3 = query.uniqueResult();
			double drawing = 0.0;
			drawing = Double.parseDouble(obj3 ==null ?"0":obj3.toString());
			// 玩家输赢
			double winorlose = drawing - deposit;
			return  new double[] {deposit,winorlose};
	   }
	   
	   
}
