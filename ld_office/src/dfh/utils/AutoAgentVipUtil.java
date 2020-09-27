package dfh.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import dfh.dao.UserDao;
import dfh.model.AgProfit;
import dfh.model.AgentVip;
import dfh.model.AgentVipId;
import dfh.model.Commissions;
import dfh.model.Payorder;
import dfh.model.Proposal;
import dfh.model.PtCommissions;
import dfh.model.Users;
import dfh.model.enums.UserRole;
import dfh.service.interfaces.CustomerService;

public class AutoAgentVipUtil {
	private static Logger log = Logger.getLogger(AutoAgentVipUtil.class);
	
	public AutoAgentVipUtil() {
		super();
	}


	public AutoAgentVipUtil(UserDao userDao, CustomerService customerService) {
		super();
		this.userDao = userDao;
		this.customerService = customerService;
	}


	private UserDao userDao ;
	private CustomerService customerService;
	
	
	public String updateAgentVipLevel(){
		//每个月1号上午执行
		Date beginDate = new Date();
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");  
	        Calendar calendar = Calendar.getInstance();  
	        Date curDate = new Date();  
	        calendar.setTime(curDate);
	        calendar.set(Calendar.MONDAY, calendar.get(Calendar.MONDAY) - 1);
	        String createdate = sdf.format(calendar.getTime());
	        //有输赢记录的代理
	        String sql = "select agent from agprofit where agent !='' and agent not in('a_e6866','a_test013','a_ceshi01','a_judy','a_tina02','a_doris','a_test05','a_jennies','www168tl','a_kelly02','a_e6811','a_e68cs2','a_yoyo','a_kelly','a_judy02','a_e68joy','a_doris02','a_test012','a_e68carter','a_tina','a_rachel','a_jennies02','a_e68cs4','a_helen','a_test02','a_ivan01','a_helen02','a_lorde','a_rachel02','a_eva01','a_tcbet','a_huaren1','a_betfox','a_mk01','a_test002','a_e68cs5','a_haiyan')   GROUP BY agent ";
	        
	        Session session = userDao.getSessionFactory().openSession();
	        List list = null;
	        try {
	        	SQLQuery query = session.createSQLQuery(sql);
	        	list = query.list() ;
	        } catch (Exception e) {
				log.error("server error", e);
			} finally {
				session.close();
			}
			DetachedCriteria dc = DetachedCriteria.forClass(Users.class);
			dc.add(Restrictions.eq("role", UserRole.AGENT.getCode())) ;
			if(null != list && list.size()>0){
				dc.add(Restrictions.in("loginname", list.toArray())) ;
			}
			
			List<Users> agents = customerService.findByCriteria(dc);
			AgentVip vip = null;
			log.info("共有"+agents.size()+"个代理需要处理");
			for (Users agent : agents) {
				vip = new AgentVip(new AgentVipId(agent.getLoginname(),createdate));
				log.info("正在处理----------"+agent.getLoginname());
				if(agent.getCreatetime().getTime() <= DateUtil.parseDateForStandard("2015-1-1 00:00:00").getTime()){  //老代理处理方式
					Double historyfee = queryAgentCommissionsAmountHistory(agent.getLoginname()); //历史累计佣金15万以上
					Integer activeMonth =	getAgentActiveMonth(agent.getLoginname());	//活跃时间6个月
					
					vip.setRegistertime(agent.getCreatetime());
					vip.setActivemonth(activeMonth);
					vip.setHistoryfee(historyfee);
					vip.setCreatetime(new Date());
				}else{ //新代理处理方式
					Double historyfee = queryAgentCommissionsAmountHistory(agent.getLoginname()); //历史累计佣金30万以上
					Integer activeUser = getActiveUser(agent.getLoginname()).get("activeUser");
					Integer totalUser = getActiveUser(agent.getLoginname()).get("totalUser");
					vip.setActiveuser(activeUser);
					vip.setCreatetime(new Date());
					vip.setHistoryfee(historyfee);
					vip.setRegistertime(agent.getCreatetime());
					vip.setRemark("共"+totalUser+"个下线");
				}
				try {
					userDao.save(vip);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		log.info("共执行"+(new Date().getTime() - beginDate.getTime()));
		return null ;
	
	}
	
	
	//查询历史佣金总额
		public Double queryAgentCommissionsAmountHistory(String agent){
			Double amount = 0.0 ;
			DetachedCriteria dc = DetachedCriteria.forClass(PtCommissions.class);
			dc.add(Restrictions.eq("id.agent", agent));
			dc.setProjection(Projections.sum("amount"));
			List sum = userDao.findByCriteria(dc) ;
			
			if(null != sum && !sum.isEmpty() && sum.size() ==1 && sum.get(0)!=null){
				amount += Double.valueOf(sum.get(0).toString());
			}
			DetachedCriteria dc1 = DetachedCriteria.forClass(Commissions.class);
			dc1.add(Restrictions.eq("id.loginname", agent));
			dc1.setProjection(Projections.sum("amount"));
			List sum1 = userDao.findByCriteria(dc1) ;
			if(null != sum1 && !sum1.isEmpty() && sum1.size() ==1 && sum1.get(0)!=null){
				amount += Double.valueOf(sum1.get(0).toString());
			}
			return amount ;
		}
		
		//代理活跃时间
		public Integer getAgentActiveMonth(String agent){
			DetachedCriteria dc1 = DetachedCriteria.forClass(Commissions.class);
			dc1.add(Restrictions.eq("id.loginname", agent));
			List<Commissions> comms = userDao.findByCriteria(dc1) ;
			if(null != comms){
				return comms.size() ;
			}
			return 0 ;
		}
		
		//获取活跃会员
		public Map<String , Integer> getActiveUser(String agent){
			Map<String , Integer> map = new HashMap<String, Integer>();
			Calendar calendar2=Calendar.getInstance();
			calendar2.add(Calendar.MONTH, -1); 
			calendar2.set(Calendar.DAY_OF_MONTH,1);
			calendar2.set(Calendar.HOUR_OF_DAY, 0);
			calendar2.set(Calendar.MINUTE, 0);
			calendar2.set(Calendar.SECOND, 0);
			
			Calendar calendar3=Calendar.getInstance();
			calendar3.set(Calendar.DAY_OF_MONTH,1);
			calendar3.set(Calendar.HOUR_OF_DAY, 0);
			calendar3.set(Calendar.MINUTE, 0);
			calendar3.set(Calendar.SECOND, 0);
			
			DetachedCriteria dc = DetachedCriteria.forClass(Users.class);
			dc.add(Restrictions.eq("agent", agent));
			List<Users> users= userDao.findByCriteria(dc);
			if(null == users ){
				map.put("totalUser", 0) ;
				map.put("activeUser", 0) ;
				return map ;
			}
			map.put("totalUser", users.size()) ;
			Integer activeUser = 0;
			for (Users users2 : users) {
				String loginname=users2.getLoginname();
				log.info("代理活跃会员----------"+agent+"---"+users2.getLoginname());
				DetachedCriteria proposalSavedc = DetachedCriteria.forClass(Proposal.class);
				DetachedCriteria payorderdc = DetachedCriteria.forClass(Payorder.class);
				DetachedCriteria agprofitBetdc = DetachedCriteria.forClass(AgProfit.class);
				payorderdc.add(Restrictions.eq("loginname", loginname));
				proposalSavedc.add(Restrictions.eq("loginname", loginname));
				agprofitBetdc.add(Restrictions.eq("loginname", loginname));
				
				payorderdc.add(Restrictions.eq("type", 0));
				payorderdc.add(Restrictions.eq("flag", 0));
				proposalSavedc.add(Restrictions.eq("flag", 2));
				
				payorderdc.add(Restrictions.ge("createTime",calendar2.getTime() ));
				proposalSavedc.add(Restrictions.ge("createTime",calendar2.getTime() ));
				agprofitBetdc.add(Restrictions.ge("createTime",calendar2.getTime() ));
				payorderdc.add(Restrictions.le("createTime",calendar3.getTime() ));
				proposalSavedc.add(Restrictions.le("createTime",calendar3.getTime() ));
				agprofitBetdc.add(Restrictions.le("createTime",calendar3.getTime() ));
				/********************************************************/
				/**
				 * 计算代理下线活跃会员人数
				 */
				
				proposalSavedc.add(Restrictions.eq("type", 502));
				proposalSavedc.setProjection(Projections.sum("amount"));
				List saveList = userDao.findByCriteria(proposalSavedc);
				agprofitBetdc.setProjection(Projections.sum("bettotal"));
				List betList = userDao.findByCriteria(agprofitBetdc);
				payorderdc.setProjection(Projections.sum("money"));
				List payorderList = userDao.findByCriteria(payorderdc);
				
				if((saveList!=null && !saveList.isEmpty() && null!=saveList.get(0) 
						|| payorderList!=null && !payorderList.isEmpty() && null!=payorderList.get(0))
						&& betList!=null && !betList.isEmpty() && null!=betList.get(0)){
					Double dpay = 0.00;
					if(null!=payorderList.get(0)){
						dpay = (Double)payorderList.get(0);
					}
					Double dsave = 0.00;
					if(null!=saveList.get(0)){
						dsave = (Double)saveList.get(0);
					}
					Double dbet = (Double)betList.get(0);
					//当月累积存款金额大于等于500元，且当月活跃投注额大于等于1000元，即为活跃会员
					if(500<=dsave+dpay && 1000<=dbet){
						activeUser++;
					}
				}
			}
			map.put("activeUser", activeUser) ;
			return map ;
			
		}


		public UserDao getUserDao() {
			return userDao;
		}


		public void setUserDao(UserDao userDao) {
			this.userDao = userDao;
		}


		public CustomerService getCustomerService() {
			return customerService;
		}


		public void setCustomerService(CustomerService customerService) {
			this.customerService = customerService;
		}

}
