package dfh.service.implementations;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.LockMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import dfh.action.vo.AutoXima;
import dfh.dao.GGameinfoDao;
import dfh.dao.SeqDao;
import dfh.dao.TaskDao;
import dfh.dao.UserDao;
import dfh.model.Proposal;
import dfh.model.Users;
import dfh.model.Xima;
import dfh.model.enums.ProposalFlagType;
import dfh.model.enums.ProposalType;
import dfh.remote.RemoteCaller;
import dfh.service.interfaces.IGGameinfoService;
import dfh.utils.Constants;
import dfh.utils.DateUtil;

public class GGameinfoServiceImpl implements IGGameinfoService {
	
	private GGameinfoDao gameinfoDao;
	private UserDao userDao;
	private TaskDao taskDao;
	private SeqDao seqDao;
	private String msg;
	private Logger log=Logger.getLogger(GGameinfoServiceImpl.class);

	
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public void setGameinfoDao(GGameinfoDao gameinfoDao) {
		this.gameinfoDao = gameinfoDao;
	}
	
	public void setTaskDao(TaskDao taskDao) {
		this.taskDao = taskDao;
	}

	public void setSeqDao(SeqDao seqDao) {
		this.seqDao = seqDao;
	}

	@Override
	public AutoXima getAutoXimaObject(Date endTime, Date startTime,
			String loginname) {
		// TODO Auto-generated method stub
		// 取得：有效投注额，反水金额，洗码率
		Double validBetAmount = null;
		try{
			//有效投注额
			validBetAmount = RemoteCaller.getTurnOverRequest(loginname,startTime,endTime).getTurnover();
			if (validBetAmount==null || validBetAmount<=0) {
				return new AutoXima("无投注记录");
			}
			validBetAmount=Math.round(validBetAmount*100.00)/100.00;
			Users users=(Users)userDao.get(Users.class, loginname, LockMode.UPGRADE);
			//Double rate = userDao.getXimaRate(validBetAmount);
			Double rate = userDao.getXimaRate(validBetAmount);
			Double ximaAmount=validBetAmount*rate;
			ximaAmount=ximaAmount>28888?28888:Math.round(ximaAmount*100.00)/100.00;
			
			log.info("自助反水-->用户："+loginname+"，有效投注额："+validBetAmount+"，洗码率："+rate+"，起始时间："+DateUtil.formatDateForStandard(startTime)+"，结束时间："+DateUtil.formatDateForStandard(endTime)+"，当前时间："+DateUtil.formatDateForStandard(new Date()));
			
			return new AutoXima(rate, ximaAmount, validBetAmount, "");
		}catch(Exception e){
			e.printStackTrace();
		}
		return new AutoXima("查询异常，请联系客服");
	}

	@Override
	public boolean execXima(Users user, AutoXima ximaVo)throws Exception {
		// TODO Auto-generated method stub
		// 防止页面数据被恶意篡改，页面的：有效投注额、反水金额、洗码率只用于给客户查看用。
		// 这里重复调用一次页面的接口，重新计算上面三项金额：
		AutoXima ximaVo2 = this.getAutoXimaObject(ximaVo.getEndTime(), ximaVo.getStartTime(), user.getLoginname());
		
		try {
			// 顾客自助洗码提案提交
			String remark="自助洗码";
			String pno = seqDao.generateProposalPno(ProposalType.SELFXIMA);
			Xima xima = new Xima(pno, user.getRole(), user.getLoginname(), "网银支付", ximaVo2.getValidAmount(), ximaVo2.getXimaAmount(), DateUtil.convertToTimestamp(ximaVo.getStartTime()), DateUtil.convertToTimestamp(ximaVo.getEndTime()), ximaVo2.getRate(), remark);
			Proposal proposal = new Proposal(pno, user.getLoginname(), DateUtil.now(), ProposalType.SELFXIMA.getCode(), user.getLevel(), user.getLoginname(), ximaVo2.getXimaAmount(),user.getAgent(), ProposalFlagType.AUDITED.getCode(),
					Constants.FROM_FRONT, remark, "customer");
			taskDao.generateTasks(pno, user.getLoginname());
			gameinfoDao.save(xima);
			gameinfoDao.save(proposal);
			this.msg="提交成功！\\n5分钟内将为您审核完毕，并添加到您的e68账户中\\n稍候您可以通过点击【查询明细】按钮进行查询";
		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
		return true;
	}

	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return msg;
	}

	@Override
	public List searchXimaDetail(String loginname, Date startTime, Date endTime,int pageno,int length)throws Exception {
		// TODO Auto-generated method stub
		// 洗码明细查询
		int offset=(pageno-1)*length;
		List ximaList = gameinfoDao.searchXimaDetail(loginname, startTime, endTime, offset, length);
		if (ximaList==null||ximaList.size()<=0) {
			return new ArrayList();
		}
		return this.parserList(ximaList);
	}
	
	private List<AutoXima> parserList(List list){
		int size=list.size();
		List<AutoXima> ximaList=new ArrayList<AutoXima>();
		for (int i = 0; i < size; i++) {
//a.pno,a.type,a.flag,b.firstcash,b.trycredit,b.rate,b.starttime,b.endtime			
			AutoXima ximavo=new AutoXima();
			Object[] obj=(Object[]) list.get(i);
			ximavo.setPno(String.valueOf(obj[0])); 	// 洗码编号
			ximavo.setXimaType(ProposalType.getText((Integer)obj[1])); 		// 洗码类型
			ximavo.setXimaStatus(ProposalFlagType.getText((Integer)obj[2]));	// 洗码状态
			ximavo.setValidAmount((Double)obj[3]);	// 有效投注额
			ximavo.setXimaAmount((Double)obj[4]);		// 反水金额
			ximavo.setRate((Double)obj[5]);			// 洗码率
			ximavo.setStatisticsTimeRange((Date)obj[6], (Date)obj[7]);
			ximaList.add(ximavo);
		}
		return ximaList;
	}
	
	public static void main(String[] args) throws Exception {
		ApplicationContext ctx=new FileSystemXmlApplicationContext("D:\\Workspaces\\MyEclipse 8.x\\e68\\application\\Ea_web\\WebRoot\\WEB-INF\\applicationContext.xml");
		IGGameinfoService game=(IGGameinfoService) ctx.getBean("gameinfoService");
		Date startTime=DateUtil.fmtyyyy_MM_d("2010-05-01");
		Date endTime=DateUtil.fmtyyyy_MM_d("2010-07-01");
		List ximaList = game.searchXimaDetail("116688", startTime, endTime, 1, 20);
		System.out.println("ximaList.size:"+ximaList.size());
		
		
	}
	

	@Override
	public String getXimaEndTime(String loginname) {
		// TODO Auto-generated method stub
		// 获得最后一次洗码的时间，包含系统洗码和自助洗码
		DetachedCriteria c=DetachedCriteria.forClass(Proposal.class);
		c.add(Restrictions.eq("loginname", loginname)).add(Restrictions.eq("flag", ProposalFlagType.EXCUTED.getCode()));
		c.add(Restrictions.or(Restrictions.eq("type", ProposalType.XIMA.getCode()),Restrictions.eq("type", ProposalType.SELFXIMA.getCode())));
		c.addOrder(Order.desc("createTime"));
		List list = gameinfoDao.findByCriteria(c);
		
		//得到昨天12点
		Calendar calend = Calendar.getInstance();
		calend.add(Calendar.DAY_OF_MONTH, -1);  
		calend.set(Calendar.HOUR_OF_DAY,12 );
		calend.set(Calendar.MINUTE, 0);
		calend.set(Calendar.SECOND, 0);
		
		
		if (list==null||list.size()<=0) {
			return DateUtil.formatDateForStandard(calend.getTime());
		}
		
		Proposal proposal=(Proposal) list.get(0);
		DetachedCriteria ximaCriteria=DetachedCriteria.forClass(Xima.class);
		ximaCriteria.setProjection(Projections.property("endTime"));
		ximaCriteria.add(Restrictions.eq("pno", proposal.getPno()));
		List ximaObject = gameinfoDao.findByCriteria(ximaCriteria);
		Date endTime = null;
		if (ximaObject==null||ximaObject.size()<=0) {
			return DateUtil.formatDateForStandard(calend.getTime());
		}else{
			
			endTime = (Date) ximaObject.get(0);//最后一次洗码时间
			
			Calendar nowDown6Cal = Calendar.getInstance();
			nowDown6Cal.add(Calendar.DAY_OF_MONTH,-6);//正常是7天内

			if(nowDown6Cal.getTime().after(endTime)){
				return DateUtil.formatDateForStandard(calend.getTime());
			}else{
				return DateUtil.formatDateForStandard(endTime);
			}
		}
	}

	@Override
	public boolean checkSubmitXima(String loginname) {
		// TODO Auto-generated method stub
		DetachedCriteria c=DetachedCriteria.forClass(Proposal.class);
		//以12点为分界：时间为12点前，减一天。大于12点往后加一天
		Date nowTime = DateUtil.databaseNow.getDatabaseNow();
		Calendar t12 = Calendar.getInstance();
		t12.setTime(nowTime);
		t12.set(Calendar.HOUR_OF_DAY,12 );
		t12.set(Calendar.MINUTE, 0);
		t12.set(Calendar.SECOND, 0);
		
		Calendar split12 = Calendar.getInstance();
		
		if(nowTime.after(t12.getTime())){//12:00:00<time <23:59:59   3-12-12:00:00-->3-13-12:00:00 
			split12.add(Calendar.DAY_OF_MONTH, 1);  
			split12.set(Calendar.HOUR_OF_DAY,12 );
			split12.set(Calendar.MINUTE, 0);
			split12.set(Calendar.SECOND, 0);
			c.add(Restrictions.ge("createTime", t12.getTime()));
			c.add(Restrictions.le("createTime", split12.getTime()));
		}else{										//00:00:00 < time <12:00:00  3-12-12:00:00-->3-13-12:00:00
			split12.add(Calendar.DAY_OF_MONTH, -1);  
			split12.set(Calendar.HOUR_OF_DAY,12 );
			split12.set(Calendar.MINUTE, 0);
			split12.set(Calendar.SECOND, 0);
			c.add(Restrictions.ge("createTime", split12.getTime()));
			c.add(Restrictions.le("createTime", t12.getTime()));
		}

		c.add(Restrictions.eq("loginname", loginname));
		c.add(Restrictions.or(Restrictions.eq("flag", ProposalFlagType.SUBMITED.getCode()),Restrictions.eq("flag", ProposalFlagType.AUDITED.getCode())));
		c.add(Restrictions.or(Restrictions.eq("type", ProposalType.XIMA.getCode()),Restrictions.eq("type", ProposalType.SELFXIMA.getCode())));
		List list = gameinfoDao.findByCriteria(c);
		if (list==null||list.size()<=0) {
			return false;
		}else{
			this.msg="正在结算中...\\n稍候您可通过点击【查询明细】按钮进行查询，或咨询在线客服";
			return true; // 已经提交过洗码结算申请
		}
		
	}

	@Override
	public AutoXima getTotalCount(String loginname, Date startTime, Date endTime)
			throws Exception {
		// TODO Auto-generated method stub
		return gameinfoDao.getTotalCount(loginname, startTime, endTime);
	}


	

}
