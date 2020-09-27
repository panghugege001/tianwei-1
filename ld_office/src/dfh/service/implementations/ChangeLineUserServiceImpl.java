package dfh.service.implementations;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ibm.icu.text.SimpleDateFormat;

import dfh.dao.ChangeLineUserDao;
import dfh.model.Users;
import dfh.service.interfaces.IchangeLineUserService;
import org.apache.log4j.Logger;
import dfh.dao.OperatorDao;

@Service
public class ChangeLineUserServiceImpl  implements IchangeLineUserService {
	private static Logger log = Logger.getLogger(OperatorDao.class);
	private ChangeLineUserDao changeLineUserDao;

	public static  String  cs  = "ts12";
	
	public static Object object = new Object();
	
	public String  getCurrentDate() {
		 Date currentTime = new Date();
		 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 return formatter.format(currentTime);
	}

	
	@Override
	public   void changeLineUser() throws Exception{
		synchronized(object) {
			System.out.println("处理时间开始：" + getCurrentDate() );
			 List<Object[]>  userLists = changeLineUserDao.queryUserByCondition();
			 if(userLists != null ) {
				 for(int i=0;i <userLists.size(); i++) {
					 Object[] users = (Object[])userLists.get(i);
					// u.loginname, u.accountName,u.phone,u.email,u.flag,u.qq,u.intro,u.agent 
					 String agent = (String) users[7];
					 String loginname =  (String) users[0];
					 String intro =  (String) users[6];     // 换线前的 cs
					 Date createtime   =  (Date) users[8]; // 用户注册时间
					 String [] agentList =  changeLineUserDao.getAgentList();
					 boolean bo = false;
					 if(agentList !=null) {
						 bo = Arrays.asList(agentList).contains(agent);
					 }
					
					 if(!bo) {
							System.out.println("******************第"
									+ i
									+ "条开始-----------------------------****************"
									+ loginname + "******");
							if(intro !=null  && intro.contains("cs")){
								if (cs.equals("ts12")) {
									cs = "ts1";
								} else if (cs.equals("ts1")) {
									cs = "ts2";
								} else if (cs.equals("ts2")) {
									cs = "ts3";
								} else if (cs.equals("ts3")) {
									cs = "ts4";
								} else if (cs.equals("ts4")) {
									cs = "ts5";
								} else if (cs.equals("ts5")) {
									cs = "ts6";
								} else if (cs.equals("ts6")) {
									cs = "ts7";
								} else if (cs.equals("ts7")) {
									cs = "ts8";
								}else if (cs.equals("ts8")) {
									cs = "ts9";
								}else if (cs.equals("ts9")) {
									cs = "ts10";
								}else if (cs.equals("ts10")) {
									cs = "ts11";
								}else if (cs.equals("ts11")) {
									cs = "ts12";
								}
								log.info("第  " +i+ " 条开始:"+ loginname + " intro: "+ intro + " cs: " + cs )  ;

								changeLineUserDao.insertinfo(users, cs);
							// -----------------------插入数据------------------------------
							continue;
					     }
							
							
					 }
					
				 }
				 log.info("cs 分线 插入成功");
			 }
		}

	}

	 public double[] getDepositAndWinorlose(String loginname,Date createtime,Date endTimeDeposit) {
		 return changeLineUserDao.getDepositAndWinorlose(loginname,createtime,endTimeDeposit);
	 }
	public ChangeLineUserDao getChangeLineUserDao() {
		return changeLineUserDao;
	}


	public void setChangeLineUserDao(ChangeLineUserDao changeLineUserDao) {
		this.changeLineUserDao = changeLineUserDao;
	}
	
	
	
	

}
