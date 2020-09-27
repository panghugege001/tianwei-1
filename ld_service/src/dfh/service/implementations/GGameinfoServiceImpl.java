package dfh.service.implementations;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.LockMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import dfh.action.vo.AutoXima;
import dfh.action.vo.AutoXimaReturnVo;
import dfh.dao.GGameinfoDao;
import dfh.dao.PlatformDao;
import dfh.dao.ProposalDao;
import dfh.dao.SeqDao;
import dfh.dao.TaskDao;
import dfh.dao.TradeDao;
import dfh.dao.UserDao;
import dfh.model.Activity;
import dfh.model.Const;
import dfh.model.PlatformData;
import dfh.model.Proposal;
import dfh.model.PtProfit;
import dfh.model.Users;
import dfh.model.Xima;
import dfh.model.bean.Bean4Xima;
import dfh.model.enums.CreditChangeType;
import dfh.model.enums.ProposalFlagType;
import dfh.model.enums.ProposalType;
import dfh.remote.RemoteCaller;
import dfh.service.interfaces.IGGameinfoService;
import dfh.utils.Constants;
import dfh.utils.DateUtil;
import dfh.utils.DtUtil;
import dfh.utils.SlotUtil;
import dfh.utils.StringUtil;
import net.sf.json.JSONObject;



public class GGameinfoServiceImpl implements IGGameinfoService {

	private GGameinfoDao gameinfoDao;
	private UserDao userDao;
	private TaskDao taskDao;
	private SeqDao seqDao;
	private TradeDao tradeDao;
	private String msg;
	private PlatformDao platformDao;
	private ProposalDao proposalDao ;
	
	public ProposalDao getProposalDao() {
		return proposalDao;
	}

	public void setProposalDao(ProposalDao proposalDao) {
		this.proposalDao = proposalDao;
	}

	private Logger log = Logger.getLogger(GGameinfoServiceImpl.class);

	public PlatformDao getPlatformDao() {
		return platformDao;
	}

	public void setPlatformDao(PlatformDao platformDao) {
		this.platformDao = platformDao;
	}
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
	public AutoXima getAutoXimaObject(Date endTime, Date startTime, String loginname) {
		// 取得：有效投注额，反水金额，洗码率
		Double validBetAmount = null;
		try {
			// 有效投注额
			validBetAmount = RemoteCaller.getTurnOverRequest(loginname, startTime, endTime).getTurnover();
			if (validBetAmount == null || validBetAmount <= 0) {
				return new AutoXima("无投注记录");
			}
			validBetAmount = Math.round(validBetAmount * 100.00) / 100.00;
			Users users = (Users) userDao.get(Users.class, loginname);
			
			double rate = SlotUtil.getLiveRate(users);

			// 获取活动返水
			Date date = new Date();
			DetachedCriteria dc = DetachedCriteria.forClass(Activity.class);
			dc.add(Restrictions.eq("activityStatus", 1));
			dc = dc.add(Restrictions.le("activityStart", date));
			dc = dc.add(Restrictions.gt("activityEnd", date));
			dc = dc.add(Restrictions.like("userrole", "%" + String.valueOf(users.getLevel()) + "%"));
			List<Activity> list = gameinfoDao.findByCriteria(dc);
			if (list != null && list.size() > 0 && list.get(0) != null) {
				Activity activity = list.get(0);
				if (activity.getActivityPercent() != null) {
					rate = activity.getActivityPercent();
				}
			}

			Double ximaAmount = validBetAmount * rate;
			ximaAmount = ximaAmount > users.getEarebate() ? users.getEarebate() : Math.round(ximaAmount * 100.00) / 100.00;

			log.info("自助反水-->用户：" + loginname + "，有效投注额：" + validBetAmount + "，洗码率：" + rate + "，起始时间：" + DateUtil.formatDateForStandard(startTime) + "，结束时间：" + DateUtil.formatDateForStandard(endTime) + "，当前时间：" + DateUtil.formatDateForStandard(new Date()));

			return new AutoXima(rate, ximaAmount, validBetAmount, "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new AutoXima("查询异常，请联系客服");
	}
	
	//获取某个时间段的mg投注额
	private Double getMgBetsAmount(String loginname, String startTime, String endTime) {
		String sql = "select SUM(amnt) bet from mg_data where mbrCode=:loginname and transType='bet' and transTime >=:startTime and transTime<:endTime and transId not in (select refTransId from mg_data where transType='refund' and mbrCode=:loginname) ";
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginname", loginname);
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		return platformDao.getOneDouble(sql, params);
	}
	
	//获取某个时间段的CQ9投注额
	private Double getCq9BetsAmount(String loginname, String startTime, String endTime) {
		String sql = "select SUM(validBetAmount) bet from cq_data where playName=:loginname and betTime >=:startTime and betTime<:endTime";
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginname", loginname);
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		return platformDao.getOneDouble(sql, params);
	}
	
	//获取某个时间段的PG投注额
	private Double getPgBetsAmount(String loginname, String startTime, String endTime) {
		String sql = "select SUM(validBetAmount) bet from pg_data where playName=:loginname and betTime >=:startTime and betTime<:endTime";
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginname", loginname);
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		return platformDao.getOneDouble(sql, params);
	}
	
	@Override
	public Double getGameValidBetAmount(String loginname, String platform,Date startTime, Date endTime) {
		ProposalType proposalType = null ;
		boolean ptotherflag = false ;
		boolean isnt=false;
		if(platform.equals("ea")){
			proposalType = ProposalType.SELFXIMA ;
		}else if(platform.equals("ag")){
			proposalType = ProposalType.AGSELFXIMA ;
		}else if(platform.equals("agin")){
			proposalType = ProposalType.AGINSELFXIMA ;
		}else if(platform.equals("bbin")){
			proposalType = ProposalType.BBINSELFXIMA ;
		}else if(platform.equals("sb")){
			proposalType = ProposalType.SBSELFXIMA ;
		}else if(platform.equals("kg")){
			proposalType = ProposalType.KGSELFXIMA ;
		}else if(platform.equals("keno")){
			proposalType = ProposalType.KENOSELFXIMA ;
		}else if(platform.equals("pttiger")){
			proposalType = ProposalType.PTTIGERSELFXIMA ;
		}else if(platform.equals("ptother")){
			ptotherflag = true ;
			proposalType = ProposalType.PTOTHERSELFXIMA ;
		}else if(platform.equals("sixlottery")){
			proposalType = ProposalType.SIXLOTTERYSELFXIMA ;
		}else if(platform.equals("ebet")){
			proposalType = ProposalType.EBETSELFXIMA ;
		}else if(platform.equals("gpi")){
			proposalType = ProposalType.GPISELFXIMA ;
		}else if(platform.equals("ttg")){
			proposalType = ProposalType.TTGSELFXIMA ;
		}else if(platform.equals("qt")){
			proposalType = ProposalType.QTSELFXIMA ;
		}else if(platform.equals("nt")){
			isnt=true;
			proposalType = ProposalType.NTSELFXIMA;
		}else if(platform.equals("mg")){
			proposalType = ProposalType.MGSELFXIMA;
		}else if(platform.equals("dt")){
			proposalType = ProposalType.DTSELFXIMA;
		}else if(platform.equals("cq9")){
			proposalType = ProposalType.CQ9SELFXIMA;
		}else if(platform.equals("pg")){
			proposalType = ProposalType.PGSELFXIMA;
		}
		// 先获取昨天12点到今天12点之间所有的投注额 ， 然后减去这段时间所有自助和系统执行的反水的投注额
		DetachedCriteria dc = DetachedCriteria.forClass(PlatformData.class);
		dc.add(Restrictions.eq("loginname", loginname));
		if(ptotherflag){  //如果想自助反水pt其它游戏 ， 需要先获得ptall , 然后减去pttiger 的投注额
			dc.add(Restrictions.eq("platform", "pttiger"));
		}else{
			if(platform.equals("kg")){ //KG后台是分三个excel抓取的 ， 这里需要算下总和
				dc.add(Restrictions.in("platform", new String[]{"kg","ssc","pk10"}));
			}else if(platform.equals("gpi")){
				dc.add(Restrictions.in("platform", new String[]{"gpi","rslot","png","bs","ctxm"}));
			}else{
				dc.add(Restrictions.eq("platform", platform));
			}
		}

		Calendar startCal = Calendar.getInstance();
		startCal.set(Calendar.HOUR_OF_DAY, 12);
		startCal.set(Calendar.MINUTE, 0);
		startCal.set(Calendar.SECOND, 0);
		if (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) > 0
				&& Calendar.getInstance().get(Calendar.HOUR_OF_DAY) < 12) {
			startCal.add(Calendar.DATE, -1);
		}
		// pt 或者  kg ethan抓的数据是从零点到23点59分
		Calendar calendP = Calendar.getInstance();//pt和kg洗码的开始时间
		calendP.set(Calendar.HOUR_OF_DAY,0 );
		calendP.set(Calendar.MINUTE, 0);
		calendP.set(Calendar.SECOND, 0);
		if(platform.contains("pt") || platform.equals("kg") || platform.equals("keno") || platform.equals("sixlottery") || platform.equals("ebet") || platform.equals("gpi")|| platform.equals("ttg") || platform.equals("qt") || platform.equals("mg") || platform.equals("dt")){
			dc.add(Restrictions.eq("starttime", calendP.getTime()));
		}else{
			dc.add(Restrictions.eq("starttime", startCal.getTime()));
		}
		
		List list = null;
		if(platform.equals("dt")){//dt,mg通过网络抓取数据。
			List listd = DtUtil.getbet(loginname, DateUtil.formatDateForStandard(startTime), DateUtil.formatDateForStandard(endTime));
			//List listd = DtUtil.getbet(loginname, DateUtil.fmtyyyyMMddHHmmss(startTime), DateUtil.fmtyyyyMMddHHmmss(endTime));
			
			if(listd != null && listd.size() > 0){
				
				Object obj = listd.get(0);
				Map<String,Object> map = (Map<String,Object>)obj;
				String betPrice = map.get("betPrice")+"";
				Double betCredit = Double.parseDouble(betPrice);// 投注额
				
				return betCredit;
			} else {
				return 0.0;
			}
		} else if(platform.equals("mg")){
			try {
				Double betCredit = getMgBetsAmount(loginname, DateUtil.formatDateForStandard(startTime), DateUtil.formatDateForStandard(endTime));
				if(betCredit == null || betCredit <= 0){
					return 0.0;
				}
				return betCredit;
			} catch (Exception e) {
				e.printStackTrace();
				return 0.0;
			}
		}  else if(platform.equals("cq9")){
			try {
				Double betCredit = getCq9BetsAmount(loginname, DateUtil.formatDateForStandard(startTime), DateUtil.formatDateForStandard(endTime));
				if(betCredit == null || betCredit <= 0){
					return 0.0;
				}
				return betCredit;
			} catch (Exception e) {
				e.printStackTrace();
				return 0.0;
			}
		}else if(platform.equals("pg")){
			try {
				Double betCredit = getPgBetsAmount(loginname, DateUtil.formatDateForStandard(startTime), DateUtil.formatDateForStandard(endTime));
				if(betCredit == null || betCredit <= 0){
					return 0.0;
				}
				return betCredit;
			} catch (Exception e) {
				e.printStackTrace();
				return 0.0;
			}
		}else {
			dc.setProjection(Projections.sum("bet"));
			list = platformDao.getHibernateTemplate().findByCriteria(dc);
		}
		
		
		if (isnt){ //如果自助洗码是nt,则从ptprofit表中获取投注额
			DetachedCriteria ntdc = DetachedCriteria.forClass(PtProfit.class);
			ntdc.add(Restrictions.eq("loginname", loginname));
			ntdc.add(Restrictions.eq("starttime", calendP.getTime()));
			ntdc.setProjection(Projections.sum("betCredit"));
			list = platformDao.getHibernateTemplate().findByCriteria(ntdc);
		}
		
		Double allValidBetAmount = (Double) list.get(0)==null?0:(Double) list.get(0);
		
		if(ptotherflag){
			//此时list中返回的是pttiger的投注额 ， 要想获取ptother的投注额，需要总的减去tiger
			DetachedCriteria dc1 = DetachedCriteria.forClass(PlatformData.class);
			dc1.add(Restrictions.eq("loginname", loginname));
			dc1.add(Restrictions.eq("platform", "ptall"));
			dc1.add(Restrictions.eq("starttime", calendP.getTime()));
			dc1.setProjection(Projections.sum("bet"));
			List list1 = platformDao.getHibernateTemplate().findByCriteria(dc1);
			allValidBetAmount = ((Double) list1.get(0)==null?0:(Double) list1.get(0)) - allValidBetAmount ;
			allValidBetAmount = allValidBetAmount>0?allValidBetAmount : 0.0 ;  //有可能会出现老虎机的投注额 比 ptall还要大  这时候 ptother投注额设为0
		}
		
		log.info(loginname + "到目前为止总的投注额是：" + allValidBetAmount);

		// 获取昨天12点到今天12点之间这段时间所有自助反水的投注额(pt和kg是今天一天的所有自助反水)
		DetachedCriteria c = DetachedCriteria.forClass(Proposal.class);
		if(platform.contains("pt") || platform.equals("kg") || platform.equals("keno") || platform.equals("sixlottery") || platform.equals("ebet") || platform.equals("gpi")|| platform.equals("ttg") || platform.equals("qt") || platform.equals("nt")){
			c.add(Restrictions.gt("createTime", calendP.getTime()));
		}else{
			c.add(Restrictions.gt("createTime", startCal.getTime()));
		}
		c.add(Restrictions.le("createTime", new Date()));
		c.add(Restrictions.eq("loginname", loginname));
		c.add(Restrictions.or(
				Restrictions.eq("flag", ProposalFlagType.EXCUTED.getCode()),
				Restrictions.eq("flag", ProposalFlagType.AUDITED.getCode())));

		c.add(Restrictions.eq("type", proposalType.getCode()));
		
		
		c.setProjection(Projections.property("pno"));
		List selflist = proposalDao.findByCriteria(c);
		Double selfXiMaAmount = 0.0;
		if (selflist != null && !selflist.isEmpty()) {
			DetachedCriteria x = DetachedCriteria.forClass(Xima.class);
			x.add(Restrictions.in("pno", selflist.toArray()));
			x.setProjection(Projections.sum("firstCash"));
			List sumx = proposalDao.findByCriteria(x);
			if (sumx != null && !sumx.isEmpty() && null != sumx.get(0)) {
				selfXiMaAmount = (Double) sumx.get(0);
			}
		}

		return allValidBetAmount - selfXiMaAmount;
	}
	
	@Override
	public AutoXima getAgAutoXimaObject(Date endTime, Date startTime,
			String loginname) {
		// 取得：有效投注额，反水金额，洗码率
		Double validBetAmount = null;
		try{
			//有效投注额
			validBetAmount = getGameValidBetAmount(loginname, "ag", startTime, endTime) ;
			if (validBetAmount==null || validBetAmount<=0) {
				return new AutoXima("无投注记录");
			}
			validBetAmount=Math.round(validBetAmount*100.00)/100.00;
			Users users=(Users)userDao.get(Users.class, loginname);
			//Double rate = userDao.getXimaRate(validBetAmount);
			double rate = SlotUtil.getLiveRate(users);
			
			/*********************************************/
			Double ximaAmount=validBetAmount*rate;
			ximaAmount=ximaAmount>users.getEarebate()?users.getEarebate():Math.round(ximaAmount*100.00)/100.00;
			
			log.info("AG自助反水-->用户："+loginname+"，有效投注额："+validBetAmount+"，洗码率："+rate+"，起始时间："+DateUtil.formatDateForStandard(startTime)+"，结束时间："+DateUtil.formatDateForStandard(endTime)+"，当前时间："+DateUtil.formatDateForStandard(new Date()));
			return new AutoXima(rate, ximaAmount, validBetAmount, "");
		}catch(Exception e){
			e.printStackTrace();
		}
		return new AutoXima("查询异常，请联系客服");
	}

	@Override
	public AutoXima getPNGAutoXimaObject(Date endTime, Date startTime, String loginname) {
		// 取得：有效投注额，反水金额，洗码率
		Double validBetAmount = null;
		try{
			//有效投注额
			validBetAmount = getGameValidBetAmount(loginname, "png", startTime, endTime) ;
			if (validBetAmount==null || validBetAmount<=0) {
				return new AutoXima("无投注记录");
			}
			validBetAmount=Math.round(validBetAmount*100.00)/100.00;
			Users users=(Users)userDao.get(Users.class, loginname);

			double rate = SlotUtil.getSlotRate(users);

			/*********************************************/
			Double ximaAmount = validBetAmount * rate;
			ximaAmount = ximaAmount>users.getEarebate()?users.getEarebate():Math.round(ximaAmount*100.00)/100.00;

			log.info("PNG自助反水-->用户："+loginname+"，有效投注额："+validBetAmount+"，洗码率："+rate+"，起始时间："+DateUtil.formatDateForStandard(startTime)+"，结束时间："+DateUtil.formatDateForStandard(endTime)+"，当前时间："+DateUtil.formatDateForStandard(new Date()));
			return new AutoXima(rate, ximaAmount, validBetAmount, "");
		}catch(Exception e){
			e.printStackTrace();
		}
		return new AutoXima("查询异常，请联系客服");
	}

	@Override
	public String execXima(Users user, Date endTime, Date startTime,String platform) throws Exception {
		String timeStart_str = getOtherXimaEndTime(user.getLoginname(), platform);
		Date timeStart = DateUtil.parseDateForStandard(timeStart_str);//解决用到该时间查询流水自助洗码bug
		Date timeEnd = new Date();
		// 这里重复调用一次页面的接口，重新计算上面三项金额：
		ProposalType proposalType = null ;
		String remark="自助洗码;秒反水;executed";
		AutoXima ximaVo2 = null ;
		if(platform.equals("ea")){
			proposalType = ProposalType.SELFXIMA ;
			ximaVo2 = this.getAutoXimaObject(endTime,startTime, user.getLoginname());
		}else if(platform.equals("ag")){
			remark = "ag自助洗码;秒反水;executed";
			proposalType = ProposalType.AGSELFXIMA ;
			ximaVo2 = this.getAgAutoXimaObject(endTime,startTime, user.getLoginname());
		}else if(platform.equals("agin")){
			remark = "agin自助洗码;秒反水;executed";
			proposalType = ProposalType.AGINSELFXIMA ;
			ximaVo2 = this.getAginAutoXimaObject(endTime,startTime, user.getLoginname());
		}else if(platform.equals("bbin")){
			remark = "bbin自助洗码;秒反水;executed";
			proposalType = ProposalType.BBINSELFXIMA ;
			ximaVo2 = this.getBbinAutoXimaObject(endTime,startTime, user.getLoginname());
		}else if(platform.equals("sb")){
			remark = "sb自助洗码;秒反水;executed";
			proposalType = ProposalType.SBSELFXIMA ;
			ximaVo2 = this.getSbAutoXimaObject(endTime,startTime, user.getLoginname());
		}else if(platform.equals("kg")){
			remark = "kg自助洗码;秒反水;executed";
			proposalType = ProposalType.KGSELFXIMA ;
			ximaVo2 = this.getKgAutoXimaObject(endTime,startTime, user.getLoginname());
		}else if(platform.equals("keno")){
			remark = "keno自助洗码;秒反水;executed";
			proposalType = ProposalType.KENOSELFXIMA ;
			ximaVo2 = this.getKenoAutoXimaObject(endTime, startTime, user.getLoginname()) ;
		}else if(platform.equals("pttiger")){
			remark = "pttiger自助洗码;秒反水;executed";
			proposalType = ProposalType.PTTIGERSELFXIMA ;
			ximaVo2 = this.getPtTigerAutoXimaObject(endTime,startTime, user.getLoginname());
		}else if(platform.equals("ptother")){
			remark = "ptother自助洗码;秒反水;executed";
			proposalType = ProposalType.PTOTHERSELFXIMA ;
			ximaVo2 = this.getPtOtherAutoXimaObject(endTime,startTime, user.getLoginname());
		}else if(platform.equals("sixlottery")){
			remark = "sixlottery自助洗码;秒反水;executed";
			proposalType = ProposalType.SIXLOTTERYSELFXIMA ;
			ximaVo2 = this.getSixLotteryAutoXimaObject(endTime,startTime, user.getLoginname());
		}else if(platform.equals("ebet")){
			remark = "ebet自助洗码;秒反水;executed";
			proposalType = ProposalType.EBETSELFXIMA ;
			ximaVo2 = this.getEbetAutoXimaObject(endTime,startTime, user.getLoginname());
		}else if(platform.equals("gpi")){
			remark = "gpi自助洗码;秒反水;executed";
			proposalType = ProposalType.GPISELFXIMA ;
			ximaVo2 = this.getGPIAutoXimaObject(endTime,startTime, user.getLoginname());
		}else if(platform.equals("ttg")){
			remark = "ttg自助洗码;秒反水;executed";
			proposalType = ProposalType.TTGSELFXIMA ;
			ximaVo2 = this.getTTGAutoXimaObject(endTime,startTime, user.getLoginname());
		}else if(platform.equals("qt")){
			remark = "qt自助洗码;秒反水;executed";
			proposalType = ProposalType.QTSELFXIMA ;
			ximaVo2 = this.getQTAutoXimaObject(endTime,startTime, user.getLoginname());
		}else if(platform.equals("nt")){
			remark = "nt自助洗码;秒反水;executed";
			proposalType = ProposalType.NTSELFXIMA ;
			endTime = timeEnd;
			startTime = timeStart;
			ximaVo2 = this.getNTAutoXimaObject(endTime,startTime, user.getLoginname());
		}else if(platform.equals("mg")){
			remark = "mg自助洗码;秒反水;executed";
			proposalType = ProposalType.MGSELFXIMA ;
			endTime = timeEnd;
			startTime = timeStart;
			ximaVo2 = this.getMGAutoXimaObject(endTime,startTime, user.getLoginname());
		}else if(platform.equals("dt")){
			remark = "dt自助洗码;秒反水;executed";
			proposalType = ProposalType.DTSELFXIMA ;
			endTime = timeEnd;
			startTime = timeStart;
			ximaVo2 = this.getDTAutoXimaObject(endTime,startTime, user.getLoginname());
		}else if(platform.equals("cq9")){
			remark = "cq9自助洗码;秒反水;executed";
			proposalType = ProposalType.CQ9SELFXIMA ;
			endTime = timeEnd;
			startTime = timeStart;
			ximaVo2 = this.getCQ9AutoXimaObject(endTime,startTime, user.getLoginname());
		}else if(platform.equals("pg")){
			remark = "pg自助洗码;秒反水;executed";
			proposalType = ProposalType.PGSELFXIMA ;
			endTime = timeEnd;
			startTime = timeStart;
			ximaVo2 = this.getPGAutoXimaObject(endTime,startTime, user.getLoginname());
		}/*else if(platform.equals("png")){
			remark = "png自助洗码;秒反水;executed";
			proposalType = ProposalType.PNGSELFXIMA ;
			endTime = timeEnd;
			startTime = timeStart;
			ximaVo2 = this.getPNGAutoXimaObject(endTime,startTime, user.getLoginname());
		}*/
		
		if(ximaVo2.getXimaAmount()<1){
			return "攒够1块以上再来自助反水吧-_-";
		}
		
		
		//计算自助反水总额是否大于28888
		DetachedCriteria d=DetachedCriteria.forClass(Proposal.class);
		//以12点为分界：时间为12点前，减一天。大于12点往后加一天
		Date nowTime = DateUtil.databaseNow.getDatabaseNow();
		Calendar t12 = Calendar.getInstance();
		t12.setTime(nowTime);
		t12.set(Calendar.HOUR_OF_DAY,12 );
		t12.set(Calendar.MINUTE, 0);
		t12.set(Calendar.SECOND, 0);
		
		Calendar split12 = Calendar.getInstance();
		if(platform.contains("pt") || platform.contains("kg") || platform.equals("keno") || platform.equals("sixlottery") || platform.equals("ebet") || platform.contains("gpi")|| platform.contains("ttg") || platform.contains("qt") || platform.contains("mg") || platform.contains("dt")){
			Calendar calendP = Calendar.getInstance();
			calendP.set(Calendar.HOUR_OF_DAY,0 );
			calendP.set(Calendar.MINUTE, 0);
			calendP.set(Calendar.SECOND, 0);
			d.add(Restrictions.ge("createTime", calendP.getTime()));
			d.add(Restrictions.le("createTime", new Date()));
			
		}else {
			if(nowTime.after(t12.getTime())){//12:00:00<time <23:59:59   3-12-12:00:00-->3-13-12:00:00 
				split12.add(Calendar.DAY_OF_MONTH, 1);  
				split12.set(Calendar.HOUR_OF_DAY,12 );
				split12.set(Calendar.MINUTE, 0);
				split12.set(Calendar.SECOND, 0);
				d.add(Restrictions.ge("createTime", t12.getTime()));
				d.add(Restrictions.le("createTime", split12.getTime()));
			}else{										//00:00:00 < time <12:00:00  3-12-12:00:00-->3-13-12:00:00
				split12.add(Calendar.DAY_OF_MONTH, -1);  
				split12.set(Calendar.HOUR_OF_DAY,12 );
				split12.set(Calendar.MINUTE, 0);
				split12.set(Calendar.SECOND, 0);
				d.add(Restrictions.ge("createTime", split12.getTime()));
				d.add(Restrictions.le("createTime", t12.getTime()));
			}
		}

		d.add(Restrictions.eq("loginname", user.getLoginname()));
		d.add(Restrictions.eq("flag", ProposalFlagType.EXCUTED.getCode()));
		
		d.add(Restrictions.eq("type", proposalType.getCode()));
		d.setProjection(Projections.sum("amount"));
		List sumList = gameinfoDao.findByCriteria(d);
		if(null!=sumList && !sumList.isEmpty() && null!=sumList.get(0)){
			Double dsum =(Double)sumList.get(0);
			if(dsum+ximaVo2.getXimaAmount()>user.getEarebate()){
				return "当天反水总金额最多"+user.getEarebate()+"元!";
			}
		}
		// TODO Auto-generated method stub
		// 防止页面数据被恶意篡改，页面的：有效投注额、反水金额、洗码率只用于给客户查看用。
		try {
			// 顾客自助洗码提案提交
			
			
			String pno = seqDao.generateProposalPno(proposalType);
			Xima xima = new Xima(pno, user.getRole(), user.getLoginname(), "网银支付", ximaVo2.getValidAmount(), ximaVo2.getXimaAmount(), DateUtil.convertToTimestamp(startTime), DateUtil.convertToTimestamp(endTime), ximaVo2.getRate(), remark);
			Proposal proposal = new Proposal(pno, user.getLoginname(), DateUtil.now(), proposalType.getCode(), user.getLevel(), user.getLoginname(), ximaVo2.getXimaAmount(),user.getAgent(), ProposalFlagType.EXCUTED.getCode(),
					Constants.FROM_FRONT, remark, "customer");
			if(ximaVo2.getXimaAmount() >= 100){//反水金额100以上需审核
				proposal.setPassflag(2);
			} else {
				remark += ";自动审核";
				proposal.setRemark(remark);
				xima.setRemark(remark);
				proposal.setPassflag(1);
			}
			proposal.setExecuteTime(new Date());
			taskDao.generateTasks(pno, user.getLoginname());
			gameinfoDao.save(xima);
			gameinfoDao.save(proposal);
			tradeDao.changeCredit(xima.getLoginname(), Double.valueOf(Math.abs(xima.getTryCredit().doubleValue())),
					CreditChangeType.XIMA_CONS.getCode(), pno, remark);
			return "秒反水执行成功！\n并添加到您的龙都账户中\n稍候您可以通过点击【查询明细】按钮进行查询";
		} catch (Exception e) {
			throw e;
		}
	}

	public AutoXima getAutoXimaPtObject(String loginname) {
		// TODO Auto-generated method stub
		// 取得：有效投注额，反水金额，洗码率
		Double validBetAmount = 0.0;
		Double bet = null;
		Double line = null;
		Double multiplier = null;
		try {
			
			Users users = (Users) userDao.get(Users.class, loginname);
			if (users == null) {
				return new AutoXima("用户不存在！");
			}
			/**
			 * 获取前面已经返还的自助反水
			 */
			SimpleDateFormat sdf = new SimpleDateFormat("hh");
			Date now = DateUtil.now();
			Integer hi = Integer.parseInt(sdf.format(now));
			DetachedCriteria d = DetachedCriteria.forClass(PtProfit.class);
			d.add(Restrictions.eq("loginname", loginname));
			
			DetachedCriteria c = DetachedCriteria.forClass(Proposal.class);
			c.add(Restrictions.eq("loginname", loginname)).add(Restrictions.eq("flag", ProposalFlagType.EXCUTED.getCode()));
			c.add(Restrictions.eq("type", ProposalType.SELFXIMAPT.getCode()));
			if(hi>12){
				Calendar start = Calendar.getInstance();
				start.setTime(now);
				start.set(Calendar.HOUR_OF_DAY, 12);
				start.set(Calendar.MINUTE, 0);
				start.set(Calendar.SECOND, 0);
				Date starttime = start.getTime();
				start.set(Calendar.HOUR_OF_DAY, 11);
				start.set(Calendar.MINUTE, 59);
				start.set(Calendar.SECOND, 59);
				start.add(Calendar.DAY_OF_MONTH, 1);
				Date endtime = start.getTime();
				c.add(Restrictions.and(Restrictions.ge("createTime", starttime),Restrictions.lt("createTime",endtime)));
				d.add(Restrictions.and(Restrictions.eq("starttime", starttime),Restrictions.eq("endtime",endtime)));
			}else{
				Calendar start = Calendar.getInstance();
				start.setTime(now);
				start.add(Calendar.DAY_OF_MONTH, -1);
				start.set(Calendar.HOUR_OF_DAY, 12);
				start.set(Calendar.MINUTE, 0);
				start.set(Calendar.SECOND, 0);
				Date starttime = start.getTime();
				start.add(Calendar.DAY_OF_MONTH, 1);
				start.set(Calendar.HOUR_OF_DAY, 11);
				start.set(Calendar.MINUTE, 59);
				start.set(Calendar.SECOND, 59);
				Date endtime = start.getTime();
				c.add(Restrictions.and(Restrictions.ge("createTime", starttime),Restrictions.lt("createTime",endtime)));
				d.add(Restrictions.and(Restrictions.eq("starttime", starttime),Restrictions.eq("endtime",endtime)));
			}
			c.setProjection(Projections.sum("amount"));
			List alist = userDao.findByCriteria(c);
			List betlist = userDao.findByCriteria(d);
			if(betlist!=null && !betlist.isEmpty()){
				validBetAmount = (Double)betlist.get(0);
				Double pd = 0.00;
				if(alist!=null && !alist.isEmpty()){
					pd = (Double)alist.get(0);
				}
				validBetAmount = validBetAmount-pd;
				if(validBetAmount<=0){
					return new AutoXima("无投注记录");
				}
			}else{
				return new AutoXima("无投注记录");
			}
			
			if (validBetAmount <= 0) {
				return new AutoXima("无投注记录");
			}
			validBetAmount = Math.round(validBetAmount * 100.00) / 100.00;
			Double rate = users.getRate();
			// 获取活动返水
			Date date = new Date();
			DetachedCriteria dc = DetachedCriteria.forClass(Activity.class);
			dc.add(Restrictions.eq("activityStatus", 1));
			dc = dc.add(Restrictions.le("activityStart", date));
			dc = dc.add(Restrictions.gt("activityEnd", date));
			dc = dc.add(Restrictions.like("userrole", "%" + String.valueOf(users.getLevel()) + "%"));
			List<Activity> list = gameinfoDao.findByCriteria(dc);
			if (list != null && list.size() > 0 && list.get(0) != null) {
				Activity activity = list.get(0);
				if (activity.getActivityPercent() != null) {
					rate = activity.getActivityPercent();
				}
			}

			Double ximaAmount = validBetAmount * rate;
			ximaAmount = ximaAmount > users.getPtrebate() ? users.getPtrebate() : Math.round(ximaAmount * 100.00) / 100.00;

			log.info("自助反水-->用户：" + loginname + "，有效投注额：" + validBetAmount + "，洗码率：" + rate  + "，当前时间：" + DateUtil.formatDateForStandard(new Date()));

			return new AutoXima(rate, ximaAmount, validBetAmount, "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new AutoXima("查询异常，请联系客服");
	}

	@Override
	public String execXimaPt(Users user, Date endTime, Date startTime) throws Exception {
		// 这里重复调用一次页面的接口，重新计算上面三项金额：
		AutoXima ximaVo2 = this.getAutoXimaPtObject(user.getLoginname());
		// 计算自助反水总额是否大于28888
		DetachedCriteria d = DetachedCriteria.forClass(Proposal.class);
		// 以12点为分界：时间为12点前，减一天。大于12点往后加一天
		Date nowTime = DateUtil.databaseNow.getDatabaseNow();
		Calendar t12 = Calendar.getInstance();
		t12.setTime(nowTime);
		t12.set(Calendar.HOUR_OF_DAY, 12);
		t12.set(Calendar.MINUTE, 0);
		t12.set(Calendar.SECOND, 0);

		Calendar split12 = Calendar.getInstance();

		if (nowTime.after(t12.getTime())) {// 12:00:00<time <23:59:59
											// 3-12-12:00:00-->3-13-12:00:00
			split12.add(Calendar.DAY_OF_MONTH, 1);
			split12.set(Calendar.HOUR_OF_DAY, 12);
			split12.set(Calendar.MINUTE, 0);
			split12.set(Calendar.SECOND, 0);
			d.add(Restrictions.ge("createTime", t12.getTime()));
			d.add(Restrictions.le("createTime", split12.getTime()));
		} else { // 00:00:00 < time <12:00:00 3-12-12:00:00-->3-13-12:00:00
			split12.add(Calendar.DAY_OF_MONTH, -1);
			split12.set(Calendar.HOUR_OF_DAY, 12);
			split12.set(Calendar.MINUTE, 0);
			split12.set(Calendar.SECOND, 0);
			d.add(Restrictions.ge("createTime", split12.getTime()));
			d.add(Restrictions.le("createTime", t12.getTime()));
		}

		d.add(Restrictions.eq("loginname", user.getLoginname()));
		d.add(Restrictions.eq("flag", ProposalFlagType.EXCUTED.getCode()));
		d.add(Restrictions.eq("type", ProposalType.SELFXIMAPT.getCode()));
		d.setProjection(Projections.sum("amount"));
		List sumList = gameinfoDao.findByCriteria(d);
		if (null != sumList && !sumList.isEmpty() && null != sumList.get(0)) {
			Double dsum = (Double) sumList.get(0);
			if (dsum + ximaVo2.getXimaAmount() > user.getPtrebate() ) {
				return "当天反水总金额最多"+user.getPtrebate() +"元!";
			}
		}
		// 防止页面数据被恶意篡改，页面的：有效投注额、反水金额、洗码率只用于给客户查看用。
		try {
			// 顾客自助洗码提案提交
			String remark = "PT自助洗码;秒反水;executed";
			String pno = seqDao.generateProposalPno(ProposalType.SELFXIMAPT);
			Xima xima = new Xima(pno, user.getRole(), user.getLoginname(), "网银支付", ximaVo2.getValidAmount(), ximaVo2.getXimaAmount(), DateUtil.convertToTimestamp(startTime), DateUtil.convertToTimestamp(endTime), ximaVo2.getRate(), remark);
			Proposal proposal = new Proposal(pno, user.getLoginname(), DateUtil.now(), ProposalType.SELFXIMAPT.getCode(), user.getLevel(), user.getLoginname(), ximaVo2.getXimaAmount(), user.getAgent(), ProposalFlagType.EXCUTED.getCode(), Constants.FROM_FRONT, remark, "customer");
			proposal.setPassflag(2);
			taskDao.generateTasks(pno, user.getLoginname());
			gameinfoDao.save(xima);
			gameinfoDao.save(proposal);
			tradeDao.changeCredit(xima.getLoginname(), Double.valueOf(Math.abs(xima.getTryCredit().doubleValue())), CreditChangeType.XIMA_CONS.getCode(), pno, remark);

			return "秒反水执行成功！并添加到您的龙都账户中\\n您可以通过点击【查询明细】按钮进行查询";
		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
	}

	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return msg;
	}

	@Override
	public List<AutoXima> searchXimaDetail(String loginname, Date startTime, Date endTime, int pageno, int length) throws Exception {
		// TODO Auto-generated method stub
		// 洗码明细查询
		int offset = (pageno - 1) * length;
		List ximaList = gameinfoDao.searchXimaDetail(loginname, startTime, endTime, offset, length);
		if (ximaList == null || ximaList.size() <= 0) {
			return new ArrayList();
		}
		return this.parserList(ximaList);
	}

	private List<AutoXima> parserList(List list) {
		int size = list.size();
		List<AutoXima> ximaList = new ArrayList<AutoXima>();
		for (int i = 0; i < size; i++) {
			// a.pno,a.type,a.flag,b.firstcash,b.trycredit,b.rate,b.starttime,b.endtime
			AutoXima ximavo = new AutoXima();
			Object[] obj = (Object[]) list.get(i);
			ximavo.setPno(String.valueOf(obj[0])); // 洗码编号
			ximavo.setXimaType(ProposalType.getText((Integer) obj[1])); // 洗码类型
			ximavo.setXimaStatus(ProposalFlagType.getText((Integer) obj[2])); // 洗码状态
			ximavo.setValidAmount((Double) obj[3]); // 有效投注额
			ximavo.setXimaAmount((Double) obj[4]); // 反水金额
			ximavo.setRate((Double) obj[5]); // 洗码率
			ximavo.setStatisticsTimeRange((Date) obj[6], (Date) obj[7]);
			ximaList.add(ximavo);
		}
		return ximaList;
	}

	@Override
	public String getXimaEndTime(String loginname) {
		// 获得最后一次洗码的时间，包含系统洗码和自助洗码
		DetachedCriteria c = DetachedCriteria.forClass(Proposal.class);
		c.add(Restrictions.eq("loginname", loginname)).add(Restrictions.eq("flag", ProposalFlagType.EXCUTED.getCode()));
		c.add(Restrictions.or(Restrictions.and(Restrictions.eq("type", ProposalType.XIMA.getCode()), Restrictions.like("remark", "ea系统洗码%")), Restrictions.eq("type", ProposalType.SELFXIMA.getCode())));
		c.addOrder(Order.desc("createTime"));
		List list = gameinfoDao.findByCriteria(c);

		// 得到昨天12点
		Calendar calend = Calendar.getInstance();
		calend.add(Calendar.DAY_OF_MONTH, -1);
		calend.set(Calendar.HOUR_OF_DAY, 12);
		calend.set(Calendar.MINUTE, 0);
		calend.set(Calendar.SECOND, 0);

		if (list == null || list.size() <= 0) {
			return DateUtil.formatDateForStandard(calend.getTime());
		}

		Proposal proposal = (Proposal) list.get(0);
		DetachedCriteria ximaCriteria = DetachedCriteria.forClass(Xima.class);
		ximaCriteria.setProjection(Projections.property("endTime"));
		ximaCriteria.add(Restrictions.eq("pno", proposal.getPno()));
		List ximaObject = gameinfoDao.findByCriteria(ximaCriteria);
		Date endTime = null;
		if (ximaObject == null || ximaObject.size() <= 0) {
			return DateUtil.formatDateForStandard(calend.getTime());
		} else {

			endTime = (Date) ximaObject.get(0);// 最后一次洗码时间

			Calendar nowDown6Cal = Calendar.getInstance();
			nowDown6Cal.add(Calendar.DAY_OF_MONTH, -6);// 正常是7天内

			if (nowDown6Cal.getTime().after(endTime)) {
				return DateUtil.formatDateForStandard(calend.getTime());
			} else {
				return DateUtil.formatDateForStandard(endTime);
			}
		}
	}

	public String getXimaEndPtTime(String loginname) {
		// 获得最后一次洗码的时间，包含系统洗码和自助洗码
		DetachedCriteria c = DetachedCriteria.forClass(Proposal.class);
		c.add(Restrictions.eq("loginname", loginname)).add(Restrictions.eq("flag", ProposalFlagType.EXCUTED.getCode()));
		c.add(Restrictions.or(Restrictions.and(Restrictions.eq("type", ProposalType.XIMA.getCode()), Restrictions.like("remark", "pt系统洗码%")), Restrictions.eq("type", ProposalType.SELFXIMAPT.getCode())));
		c.addOrder(Order.desc("createTime"));
		List list = gameinfoDao.findByCriteria(c);

		// 得到昨天12点
		Calendar calend = Calendar.getInstance();
		calend.add(Calendar.DAY_OF_MONTH, -1);
		calend.set(Calendar.HOUR_OF_DAY, 12);
		calend.set(Calendar.MINUTE, 0);
		calend.set(Calendar.SECOND, 0);

		if (list == null || list.size() <= 0) {
			return DateUtil.formatDateForStandard(calend.getTime());
		}

		Proposal proposal = (Proposal) list.get(0);
		DetachedCriteria ximaCriteria = DetachedCriteria.forClass(Xima.class);
		ximaCriteria.setProjection(Projections.property("endTime"));
		ximaCriteria.add(Restrictions.eq("pno", proposal.getPno()));
		List ximaObject = gameinfoDao.findByCriteria(ximaCriteria);
		Date endTime = null;
		if (ximaObject == null || ximaObject.size() <= 0) {
			return DateUtil.formatDateForStandard(calend.getTime());
		} else {

			endTime = (Date) ximaObject.get(0);// 最后一次洗码时间

			Calendar nowDown6Cal = Calendar.getInstance();
			nowDown6Cal.add(Calendar.DAY_OF_MONTH, -6);// 正常是7天内

			if (nowDown6Cal.getTime().after(endTime)) {
				return DateUtil.formatDateForStandard(calend.getTime());
			} else {
				return DateUtil.formatDateForStandard(endTime);
			}
		}
	}

	@Override
	public AutoXimaReturnVo checkSubmitXima(String loginname , String platform) {
		AutoXimaReturnVo autoXimaReturnVo = new AutoXimaReturnVo();
		DetachedCriteria c=DetachedCriteria.forClass(Proposal.class);
		c.add(Restrictions.eq("loginname", loginname));
		c.add(Restrictions.eq("flag", ProposalFlagType.EXCUTED.getCode()));
		
		if(platform.equals("ea")){
			c.add(Restrictions.eq("type", ProposalType.SELFXIMA.getCode()));
		}else if(platform.equals("ag")){
			c.add(Restrictions.eq("type", ProposalType.AGSELFXIMA.getCode()));
		}else if(platform.equals("agin")){
			c.add(Restrictions.eq("type", ProposalType.AGINSELFXIMA.getCode()));
		}else if(platform.equals("bbin")){
			c.add(Restrictions.eq("type", ProposalType.BBINSELFXIMA.getCode()));
		}else if(platform.equals("kg")){
			c.add(Restrictions.eq("type", ProposalType.KGSELFXIMA.getCode()));
		}else if(platform.equals("keno")){
			c.add(Restrictions.eq("type", ProposalType.KENOSELFXIMA.getCode()));
		}else if(platform.equals("sb")){
			c.add(Restrictions.eq("type", ProposalType.SBSELFXIMA.getCode()));
		}else if(platform.equals("pttiger")){
			c.add(Restrictions.eq("type", ProposalType.PTTIGERSELFXIMA.getCode()));
		}else if(platform.equals("ptother")){
			c.add(Restrictions.eq("type", ProposalType.PTOTHERSELFXIMA.getCode()));
		}else if(platform.equals("sixlottery")){
			c.add(Restrictions.eq("type", ProposalType.SIXLOTTERYSELFXIMA.getCode()));
		}else if(platform.equals("ebet")){
			c.add(Restrictions.eq("type", ProposalType.EBETSELFXIMA.getCode()));
		}else if(platform.equals("gpi")){
			c.add(Restrictions.eq("type", ProposalType.GPISELFXIMA.getCode()));
		}else if(platform.equals("ttg")){
			c.add(Restrictions.eq("type", ProposalType.TTGSELFXIMA.getCode()));
		}else if(platform.equals("qt")){
			c.add(Restrictions.eq("type", ProposalType.QTSELFXIMA.getCode()));
		}else if(platform.equals("nt")){
			c.add(Restrictions.eq("type", ProposalType.NTSELFXIMA.getCode()));
		}else if(platform.equals("mg")){
			c.add(Restrictions.eq("type", ProposalType.MGSELFXIMA.getCode()));
		}else if(platform.equals("dt")){
			c.add(Restrictions.eq("type", ProposalType.DTSELFXIMA.getCode()));
		}else if(platform.equals("png")){
			c.add(Restrictions.eq("type", ProposalType.PNGSELFXIMA.getCode()));
		}else if(platform.equals("slot")){
			c.add(Restrictions.in("type", SlotUtil.PLAFROMVALIDXIMA.values()));
			c.add(Restrictions.eq("passflag", 2));
		}
		
		c.addOrder(Order.desc("createTime"));
		List<Proposal> list = gameinfoDao.findByCriteria(c);
		if (list==null||list.size()<=0) {
			autoXimaReturnVo.setB(false);
		}else{
			if(null!=list.get(0)){
				Proposal p = list.get(0);
				if(2==p.getPassflag()){
					autoXimaReturnVo.setB(true);
					autoXimaReturnVo.setMsg("上一笔自助洗码审核中...\\n稍候您可通过点击【查询明细】按钮进行查询，或咨询在线客服");// 已经提交过洗码结算申请
				}else{
					autoXimaReturnVo.setB(false);
				}
			}
		}
		return autoXimaReturnVo;
	}

	public AutoXimaReturnVo checkSubmitPtXima(String loginname) {
		AutoXimaReturnVo autoXimaReturnVo = new AutoXimaReturnVo();
		// TODO Auto-generated method stub
		DetachedCriteria c = DetachedCriteria.forClass(Proposal.class);
		c.add(Restrictions.eq("loginname", loginname));
		c.add(Restrictions.eq("flag", ProposalFlagType.EXCUTED.getCode()));
		c.add(Restrictions.eq("type", ProposalType.SELFXIMAPT.getCode()));
		c.addOrder(Order.desc("createTime"));
		List<Proposal> list = gameinfoDao.findByCriteria(c);
		if (list == null || list.size() <= 0) {
			autoXimaReturnVo.setB(false);
		} else {
			if (null != list.get(0)) {
				Proposal p = list.get(0);
				if (2 == p.getPassflag()) {
					autoXimaReturnVo.setB(true);
					autoXimaReturnVo.setMsg("上一笔自助洗码审核中...\\n稍候您可通过点击【查询明细】按钮进行查询，或咨询在线客服");// 已经提交过洗码结算申请
				} else {
					autoXimaReturnVo.setB(false);
				}
			}
		}
		return autoXimaReturnVo;
	}

	@Override
	public AutoXima getTotalCount(String loginname, Date startTime, Date endTime) throws Exception {
		// TODO Auto-generated method stub
		return gameinfoDao.getTotalCount(loginname, startTime, endTime);
	}

	public void setTradeDao(TradeDao tradeDao) {
		this.tradeDao = tradeDao;
	}
	
	/**
	 * 
	* @methods getAgXimaEndTime 
	* @description <p>获取ag洗码时间</p>
	* @author erick
	* @date 2014年12月15日 上午11:33:25
	* @param loginname
	* @return 参数说明
	* @return String 返回结果的说明
	 */
	@SuppressWarnings("deprecation")
	public String getOtherXimaEndTime(String loginname , String platform){
		ProposalType proposalType = null ; 
		String remark = "ea系统洗码%" ;
		boolean timFlag = false ;  //是否为美东时间（有些平台的洗码时间为美东时间）
		if(platform.equals("ea")){
			proposalType = ProposalType.SELFXIMA ;
		}else if(platform.equals("ag")){
			remark = "ag系统洗码%" ;
			proposalType = ProposalType.AGSELFXIMA ;
		}else if(platform.equals("agin")){
			remark = "agin系统洗码%" ;
			proposalType = ProposalType.AGINSELFXIMA ;
		}else if(platform.equals("bbin")){
			timFlag = true ;
			
			remark = "bbin系统洗码%" ;
			proposalType = ProposalType.BBINSELFXIMA ;
		}else if(platform.equals("sb")){
			timFlag = true ;
			
			remark = "sb系统洗码%" ;
			proposalType = ProposalType.SBSELFXIMA ;
		}else if(platform.equals("kg")){
			remark = "keno2系统洗码%" ;
			proposalType = ProposalType.KGSELFXIMA ;
		}else if(platform.equals("keno")){
			remark = "keno系统洗码%" ;
			proposalType = ProposalType.KGSELFXIMA ;
		}else if(platform.equals("pttiger")){
			remark = "pttiger系统洗码%" ;
			proposalType = ProposalType.PTTIGERSELFXIMA ;
		}else if(platform.equals("ptother")){
			remark = "ptother系统洗码%" ;
			proposalType = ProposalType.PTOTHERSELFXIMA ;
		}else if(platform.equals("sixlottery")){
			remark = "sixlottery系统洗码%" ;
			proposalType = ProposalType.SIXLOTTERYSELFXIMA ;
		}else if(platform.equals("ebet")){
			remark = "ebet系统洗码%";
			proposalType = ProposalType.EBETSELFXIMA ;
		}else if(platform.equals("gpi")){
			remark = "GPI系统洗码%" ;
			proposalType = ProposalType.GPISELFXIMA;
		}else if(platform.equals("ttg")){
			remark = "ttg系统洗码%" ;
			proposalType = ProposalType.TTGSELFXIMA;
		}else if(platform.equals("qt")){
			remark = "qt系统洗码%" ;
			proposalType = ProposalType.QTSELFXIMA;
		}else if(platform.equals("cq9")){
			remark = "cq9系统洗码%" ;
			proposalType = ProposalType.CQ9SELFXIMA;
		}else if(platform.equals("pg")){
			remark = "pg系统洗码%" ;
			proposalType = ProposalType.PGSELFXIMA;
		}else if(platform.equals("nt")){
			return this.getNetSelfXimaTime(loginname,ProposalType.NTSELFXIMA);
		}else if(platform.equals("mg")){// 由于mg,dt洗码是直接以接口抓取时间，所以用getNetSelfXimaTime获取
			return this.getNetSelfXimaTime(loginname,ProposalType.MGSELFXIMA);
		}else if(platform.equals("dt")){
			return this.getNetSelfXimaTime(loginname,ProposalType.DTSELFXIMA);
		}
		// 获得最后一次洗码的时间，包含系统洗码和自助洗码 
		DetachedCriteria c=DetachedCriteria.forClass(Proposal.class);
		c.add(Restrictions.eq("loginname", loginname)).add(Restrictions.eq("flag", ProposalFlagType.EXCUTED.getCode()));
		c.add(Restrictions.or(Restrictions.and(Restrictions.eq("type",
				ProposalType.XIMA.getCode()), Restrictions.like("remark",
				remark)), Restrictions.eq("type", proposalType.getCode())));
		c.addOrder(Order.desc("createTime"));
		List list = gameinfoDao.findByCriteria(c);
		
		//得到昨天12点  //pt或者keno2 系统洗码时间是从昨天早晨零点零分到昨晚23分59秒
		Calendar calend = Calendar.getInstance();
		if(platform.contains("pt") || platform.contains("kg") || platform.equals("keno") || platform.equals("sixlottery") || platform.equals("ebet")|| platform.equals("gpi")|| platform.equals("ttg") || platform.equals("qt") || platform.equals("nt")){
			calend.set(Calendar.HOUR_OF_DAY,0 );
			calend.set(Calendar.MINUTE, 0);
			calend.set(Calendar.SECOND, 0);
		}else{
			if(calend.getTime().getHours()>0 && calend.getTime().getHours()<12 ){
				calend.add(Calendar.DAY_OF_MONTH, -1);
			}
			calend.set(Calendar.HOUR_OF_DAY,12 );
			calend.set(Calendar.MINUTE, 0);
			calend.set(Calendar.SECOND, 0);
		}
		
		
		
		
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
			if(timFlag && endTime.getHours() == 0 && endTime.getMinutes() == 0){  //系统洗码的时间美东时间转为北京时间(bbin和sb)
				endTime.setHours(12);
			}
			if(platform.contains("pt") || platform.contains("kg") || platform.equals("keno") || platform.equals("sixlottery") || platform.equals("ebet")|| platform.equals("gpi")|| platform.equals("ttg") || platform.equals("qt") || platform.equals("nt") || platform.equals("mg") || platform.equals("dt")){
				if(endTime.getHours() == 0 && endTime.getMinutes() == 0){
					endTime.setDate(new Date().getDate());
				}
			}
			
//			Calendar nowDown6Cal = Calendar.getInstance();
//			nowDown6Cal.add(Calendar.DAY_OF_MONTH,-6);//正常是7天内
			
			if(calend.getTime().after(endTime)){
				return DateUtil.formatDateForStandard(calend.getTime());
			}else{
				return DateUtil.formatDateForStandard(endTime);
			}
		}
	
	}

	/**
	 * 该方法是用时间获取投注额的方法，以今天0点为准，如果最后自助洗码时间比改时间大，返回自助洗码时间，否则返回该时间
	 * @param loginname
	 * @param proposalType
	 * @return
	 * */
	private String getNetSelfXimaTime(String loginname, ProposalType proposalType) {
		
		Calendar calend = Calendar.getInstance();
		calend.set(Calendar.HOUR_OF_DAY,0 );
		calend.set(Calendar.MINUTE, 0);
		calend.set(Calendar.SECOND, 0);
		
		// 获得最后一次zizhu洗码的时间
		DetachedCriteria c=DetachedCriteria.forClass(Proposal.class);
		c.add(Restrictions.eq("loginname", loginname)).add(Restrictions.eq("flag", ProposalFlagType.EXCUTED.getCode()));
		c.add(Restrictions.eq("type", proposalType.getCode()));
		c.addOrder(Order.desc("createTime"));
		//TODO 此处以后加优化，只获取几条数据。
		List list = gameinfoDao.findByCriteria(c, 0, 1);
		
		if(list != null && list.size() > 0){
			Proposal p = (Proposal) list.get(0);
			DetachedCriteria ximaCriteria=DetachedCriteria.forClass(Xima.class);
			ximaCriteria.setProjection(Projections.property("endTime"));
			ximaCriteria.add(Restrictions.eq("pno", p.getPno()));
			List ximaObject = gameinfoDao.findByCriteria(ximaCriteria);
			
			if (ximaObject==null||ximaObject.size()<=0) {
				return DateUtil.formatDateForStandard(calend.getTime());
			}else{
				Date endTime = (Date) ximaObject.get(0);//最后一次zizhu洗码时间
				
				if(endTime.compareTo(calend.getTime()) > 0){
					return DateUtil.formatDateForStandard(endTime);
				} else {
					return DateUtil.formatDateForStandard(calend.getTime());
				}
			}
		} else {
			return DateUtil.formatDateForStandard(calend.getTime());
		}
	}

	@Override
	public AutoXima getAginAutoXimaObject(Date endTime, Date startTime,
			String loginname) {
		// TODO Auto-generated method stub
		// 取得：有效投注额，反水金额，洗码率
		Double validBetAmount = null;
		try{
			//有效投注额
			validBetAmount = getGameValidBetAmount(loginname, "agin", startTime, endTime) ;
			if (validBetAmount==null || validBetAmount<=0) {
				return new AutoXima("无投注记录");
			}
			validBetAmount=Math.round(validBetAmount*100.00)/100.00;
			Users users=(Users)userDao.get(Users.class, loginname);
			
			Double rate = SlotUtil.getLiveRate(users);
			
			/*********************************************/
			Double ximaAmount=validBetAmount*rate;
			ximaAmount=ximaAmount>users.getEarebate()?users.getEarebate():Math.round(ximaAmount*100.00)/100.00;
			
			log.info("AGIN自助反水-->用户："+loginname+"，有效投注额："+validBetAmount+"，洗码率："+rate+"，起始时间："+DateUtil.formatDateForStandard(startTime)+"，结束时间："+DateUtil.formatDateForStandard(endTime)+"，当前时间："+DateUtil.formatDateForStandard(new Date()));
			
			return new AutoXima(rate, ximaAmount, validBetAmount, "");
		}catch(Exception e){
			e.printStackTrace();
		}
		return new AutoXima("查询异常，请联系客服");
	}

	@Override
	public AutoXima getBbinAutoXimaObject(Date endTime, Date startTime,
			String loginname) {
		// 取得：有效投注额，反水金额，洗码率
		Double validBetAmount = null;
		try{
			//有效投注额
			validBetAmount = getGameValidBetAmount(loginname, "bbin", startTime, endTime) ;
			if (validBetAmount==null || validBetAmount<=0) {
				return new AutoXima("无投注记录");
			}
			validBetAmount=Math.round(validBetAmount*100.00)/100.00;
			Users users=(Users)userDao.get(Users.class, loginname);
			Double rate = SlotUtil.getLiveRate(users);
			
			/*********************************************/
			Double ximaAmount=validBetAmount*rate;
			ximaAmount=ximaAmount>users.getEarebate()?users.getEarebate():Math.round(ximaAmount*100.00)/100.00;
			
			log.info("Bbin自助反水-->用户："+loginname+"，有效投注额："+validBetAmount+"，洗码率："+rate+"，起始时间："+DateUtil.formatDateForStandard(startTime)+"，结束时间："+DateUtil.formatDateForStandard(endTime)+"，当前时间："+DateUtil.formatDateForStandard(new Date()));
			
			return new AutoXima(rate, ximaAmount, validBetAmount, "");
		}catch(Exception e){
			e.printStackTrace();
		}
		return new AutoXima("查询异常，请联系客服");
	}

	@Override
	public AutoXima getKgAutoXimaObject(Date endTime, Date startTime,
			String loginname) {
		// TODO Auto-generated method stub
		// 取得：有效投注额，反水金额，洗码率
		Double validBetAmount = null;
		try{
			//有效投注额
			validBetAmount = getGameValidBetAmount(loginname, "kg", startTime, endTime) ;
			if (validBetAmount==null || validBetAmount<=0) {
				return new AutoXima("无投注记录");
			}
			validBetAmount=Math.round(validBetAmount*100.00)/100.00;
			Users users=(Users)userDao.get(Users.class, loginname, LockMode.UPGRADE);
			//Double rate = userDao.getXimaRate(validBetAmount);
			Double rate = users.getRate();
			
			rate = 0.02;
			
			/*********************************************/
			Double ximaAmount=validBetAmount*rate;
			ximaAmount=ximaAmount>users.getEarebate()?users.getEarebate():Math.round(ximaAmount*100.00)/100.00;
			
			log.info("Kg自助反水-->用户："+loginname+"，有效投注额："+validBetAmount+"，洗码率："+rate+"，起始时间："+DateUtil.formatDateForStandard(startTime)+"，结束时间："+DateUtil.formatDateForStandard(endTime)+"，当前时间："+DateUtil.formatDateForStandard(new Date()));
			
			return new AutoXima(rate, ximaAmount, validBetAmount, "");
		}catch(Exception e){
			e.printStackTrace();
		}
		return new AutoXima("查询异常，请联系客服");
	}
	
	@Override
	public AutoXima getKenoAutoXimaObject(Date endTime, Date startTime,
			String loginname) {
		// TODO Auto-generated method stub
		// 取得：有效投注额，反水金额，洗码率
		Double validBetAmount = null;
		try{
			//有效投注额
			validBetAmount = getGameValidBetAmount(loginname, "keno", startTime, endTime) ;
			if (validBetAmount==null || validBetAmount<=0) {
				return new AutoXima("无投注记录");
			}
			validBetAmount=Math.round(validBetAmount*100.00)/100.00;
			Users users=(Users)userDao.get(Users.class, loginname, LockMode.UPGRADE);
			//Double rate = userDao.getXimaRate(validBetAmount);
			Double rate = users.getRate();
			
			rate = 0.020;
			
			/*********************************************/
			Double ximaAmount=validBetAmount*rate;
			ximaAmount=ximaAmount>users.getEarebate()?users.getEarebate():Math.round(ximaAmount*100.00)/100.00;
			
			log.info("Keno自助反水-->用户："+loginname+"，有效投注额："+validBetAmount+"，洗码率："+rate+"，起始时间："+DateUtil.formatDateForStandard(startTime)+"，结束时间："+DateUtil.formatDateForStandard(endTime)+"，当前时间："+DateUtil.formatDateForStandard(new Date()));
			
			return new AutoXima(rate, ximaAmount, validBetAmount, "");
		}catch(Exception e){
			e.printStackTrace();
		}
		return new AutoXima("查询异常，请联系客服");
	}

	@Override
	public AutoXima getSbAutoXimaObject(Date endTime, Date startTime,
			String loginname) {
		// TODO Auto-generated method stub
		// 取得：有效投注额，反水金额，洗码率
		Double validBetAmount = null;
		try{
			//有效投注额
			validBetAmount = getGameValidBetAmount(loginname, "sb", startTime, endTime) ;
			if (validBetAmount==null || validBetAmount<=0) {
				return new AutoXima("无投注记录");
			}
			validBetAmount=Math.round(validBetAmount*100.00)/100.00;
			Users users=(Users)userDao.get(Users.class, loginname, LockMode.UPGRADE);
			//Double rate = userDao.getXimaRate(validBetAmount);
			Double rate = users.getRate();
			
			rate = 0.004;
			
			/*********************************************/
			Double ximaAmount=validBetAmount*rate;
			ximaAmount=ximaAmount>users.getEarebate()?users.getEarebate():Math.round(ximaAmount*100.00)/100.00;
			
			log.info("Sb自助反水-->用户："+loginname+"，有效投注额："+validBetAmount+"，洗码率："+rate+"，起始时间："+DateUtil.formatDateForStandard(startTime)+"，结束时间："+DateUtil.formatDateForStandard(endTime)+"，当前时间："+DateUtil.formatDateForStandard(new Date()));
			
			return new AutoXima(rate, ximaAmount, validBetAmount, "");
		}catch(Exception e){
			e.printStackTrace();
		}
		return new AutoXima("查询异常，请联系客服");
	}

	@Override
	public AutoXima getPtTigerAutoXimaObject(Date endTime, Date startTime,String loginname) {
		// 取得：有效投注额，反水金额，洗码率
		Double validBetAmount = null;
		try{
			//有效投注额
			validBetAmount = getGameValidBetAmount(loginname, "pttiger", startTime, endTime) ;
			if (validBetAmount==null || validBetAmount<=0) {
				return new AutoXima("无投注记录");
			}
			validBetAmount=Math.round(validBetAmount*100.00)/100.00;
			Users users=(Users)userDao.get(Users.class, loginname);
			//Double rate = userDao.getXimaRate(validBetAmount);
//			Double rate = users.getRate();
			
//			PT新会员 忠实是0.4  星级赌神和金牌赌神是0.6  白金和钻石是0.8  至尊是1
			//2016-2-1 修改新会员忠实会员0.6% 星级金牌白金0.8% 钻石至尊1%
			double rate = SlotUtil.getSlotRate(users);
			
			/*********************************************/
			Double ximaAmount=validBetAmount*rate;
			ximaAmount=ximaAmount>users.getEarebate()?users.getEarebate():Math.round(ximaAmount*100.00)/100.00;
			
			log.info("PtTiger自助反水-->用户："+loginname+"，有效投注额："+validBetAmount+"，洗码率："+rate+"，起始时间："+DateUtil.formatDateForStandard(startTime)+"，结束时间："+DateUtil.formatDateForStandard(endTime)+"，当前时间："+DateUtil.formatDateForStandard(new Date()));
			
			return new AutoXima(rate, ximaAmount, validBetAmount, "");
		}catch(Exception e){
			e.printStackTrace();
		}
		return new AutoXima("查询异常，请联系客服");
	}
	
	@Override
	public AutoXima getPtOtherAutoXimaObject(Date endTime, Date startTime,
			String loginname) {
		// 取得：有效投注额，反水金额，洗码率
		Double validBetAmount = null;
		try{
			//有效投注额
			validBetAmount = getGameValidBetAmount(loginname, "ptother", startTime, endTime) ;
			if (validBetAmount==null || validBetAmount<=0) {
				return new AutoXima("无投注记录");
			}
			validBetAmount=Math.round(validBetAmount*100.00)/100.00;
			Users users=(Users)userDao.get(Users.class, loginname, LockMode.UPGRADE);
			//Double rate = userDao.getXimaRate(validBetAmount);
			double rate = SlotUtil.getPTOtherRate(users);
			
			/*********************************************/
			Double ximaAmount=validBetAmount*rate;
			ximaAmount=ximaAmount>users.getEarebate()?users.getEarebate():Math.round(ximaAmount*100.00)/100.00;
			
			log.info("PtOther自助反水-->用户："+loginname+"，有效投注额："+validBetAmount+"，洗码率："+rate+"，起始时间："+DateUtil.formatDateForStandard(startTime)+"，结束时间："+DateUtil.formatDateForStandard(endTime)+"，当前时间："+DateUtil.formatDateForStandard(new Date()));
			
			return new AutoXima(rate, ximaAmount, validBetAmount, "");
		}catch(Exception e){
			e.printStackTrace();
		}
		return new AutoXima("查询异常，请联系客服");
	}
	
	@Override
	public AutoXima getSixLotteryAutoXimaObject(Date endTime, Date startTime,
			String loginname) {
		// 取得：有效投注额，反水金额，洗码率
		Double validBetAmount = null;
		try{
			//有效投注额
			validBetAmount = getGameValidBetAmount(loginname, "sixlottery", startTime, endTime) ;
			if (validBetAmount==null || validBetAmount<=0) {
				return new AutoXima("无投注记录");
			}
			validBetAmount=Math.round(validBetAmount*100.00)/100.00;
			Users users=(Users)userDao.get(Users.class, loginname, LockMode.UPGRADE);
			//Double rate = userDao.getXimaRate(validBetAmount);
			Double rate = users.getRate();
			
			rate = 0.004 ;
			
			/*********************************************/
			Double ximaAmount=validBetAmount*rate;
			ximaAmount=ximaAmount>users.getEarebate()?users.getEarebate():Math.round(ximaAmount*100.00)/100.00;
			
			log.info("PtOther自助反水-->用户："+loginname+"，有效投注额："+validBetAmount+"，洗码率："+rate+"，起始时间："+DateUtil.formatDateForStandard(startTime)+"，结束时间："+DateUtil.formatDateForStandard(endTime)+"，当前时间："+DateUtil.formatDateForStandard(new Date()));
			
			return new AutoXima(rate, ximaAmount, validBetAmount, "");
		}catch(Exception e){
			e.printStackTrace();
		}
		return new AutoXima("查询异常，请联系客服");
	}
	
	@Override
	public AutoXima getEbetAutoXimaObject(Date endTime, Date startTime,
			String loginname) {
		// TODO Auto-generated method stub
		// 取得：有效投注额，反水金额，洗码率
		Double validBetAmount = null;
		try{
			//有效投注额
			validBetAmount = getGameValidBetAmount(loginname, "ebet", startTime, endTime) ;
			if (validBetAmount==null || validBetAmount<=0) {
				return new AutoXima("无投注记录");
			}
			validBetAmount=Math.round(validBetAmount*100.00)/100.00;
			Users users=(Users)userDao.get(Users.class, loginname, LockMode.UPGRADE);
			
			Double rate = SlotUtil.getLiveRate(users);
			
			/*********************************************/
			Double ximaAmount=validBetAmount*rate;
			ximaAmount=ximaAmount>users.getEarebate()?users.getEarebate():Math.round(ximaAmount*100.00)/100.00;
			
			log.info("Bbin自助反水-->用户："+loginname+"，有效投注额："+validBetAmount+"，洗码率："+rate+"，起始时间："+DateUtil.formatDateForStandard(startTime)+"，结束时间："+DateUtil.formatDateForStandard(endTime)+"，当前时间："+DateUtil.formatDateForStandard(new Date()));
			
			return new AutoXima(rate, ximaAmount, validBetAmount, "");
		}catch(Exception e){
			e.printStackTrace();
		}
		return new AutoXima("查询异常，请联系客服");
	}

	@Override
	public AutoXima getGPIAutoXimaObject(Date endTime, Date startTime, String loginname) {
		// 取得：有效投注额，反水金额，洗码率
		Double validBetAmount = null;
		try{
			//有效投注额
			validBetAmount = getGameValidBetAmount(loginname, "gpi", startTime, endTime) ;
			if (validBetAmount==null || validBetAmount<=0) {
				return new AutoXima("无投注记录");
			}
			validBetAmount=Math.round(validBetAmount*100.00)/100.00;
			Users users=(Users)userDao.get(Users.class, loginname);
			
			Double rate = SlotUtil.getLiveRate(users);
			
			/*********************************************/
			Double ximaAmount = validBetAmount * rate;
			ximaAmount = ximaAmount>users.getEarebate()?users.getEarebate():Math.round(ximaAmount*100.00)/100.00;
			
			log.info("GPI自助反水-->用户："+loginname+"，有效投注额："+validBetAmount+"，洗码率："+rate+"，起始时间："+DateUtil.formatDateForStandard(startTime)+"，结束时间："+DateUtil.formatDateForStandard(endTime)+"，当前时间："+DateUtil.formatDateForStandard(new Date()));
			return new AutoXima(rate, ximaAmount, validBetAmount, "");
		}catch(Exception e){
			e.printStackTrace();
		}
		return new AutoXima("查询异常，请联系客服");
	}
	
	@Override
	public AutoXima getQTAutoXimaObject(Date endTime, Date startTime, String loginname) {
		// 取得：有效投注额，反水金额，洗码率
		Double validBetAmount = null;
		try{
			//有效投注额
			validBetAmount = getGameValidBetAmount(loginname, "qt", startTime, endTime) ;
			if (validBetAmount==null || validBetAmount<=0) {
				return new AutoXima("无投注记录");
			}
			validBetAmount=Math.round(validBetAmount*100.00)/100.00;
			Users users=(Users)userDao.get(Users.class, loginname, LockMode.UPGRADE);
			
			double rate = SlotUtil.getSlotRate(users);
			
			/*********************************************/
			Double ximaAmount = validBetAmount * rate;
			ximaAmount = ximaAmount>users.getEarebate()?users.getEarebate():Math.round(ximaAmount*100.00)/100.00;
			
			log.info("QT自助反水-->用户："+loginname+"，有效投注额："+validBetAmount+"，洗码率："+rate+"，起始时间："+DateUtil.formatDateForStandard(startTime)+"，结束时间："+DateUtil.formatDateForStandard(endTime)+"，当前时间："+DateUtil.formatDateForStandard(new Date()));
			return new AutoXima(rate, ximaAmount, validBetAmount, "");
		}catch(Exception e){
			e.printStackTrace();
		}
		return new AutoXima("查询异常，请联系客服");
	}
	
	@Override
	public AutoXima getTTGAutoXimaObject(Date endTime, Date startTime, String loginname) {
		// 取得：有效投注额，反水金额，洗码率
		Double validBetAmount = null;
		try{
			//有效投注额
			validBetAmount = getGameValidBetAmount(loginname, "ttg", startTime, endTime) ;
			if (validBetAmount==null || validBetAmount<=0) {
				return new AutoXima("无投注记录");
			}
			validBetAmount=Math.round(validBetAmount*100.00)/100.00;
			Users users=(Users)userDao.get(Users.class, loginname, LockMode.UPGRADE);
			
			double rate = SlotUtil.getSlotRate(users);
			
			/*********************************************/
			Double ximaAmount = validBetAmount * rate;
			ximaAmount = ximaAmount>users.getEarebate()?users.getEarebate():Math.round(ximaAmount*100.00)/100.00;
			
			log.info("GPI自助反水-->用户："+loginname+"，有效投注额："+validBetAmount+"，洗码率："+rate+"，起始时间："+DateUtil.formatDateForStandard(startTime)+"，结束时间："+DateUtil.formatDateForStandard(endTime)+"，当前时间："+DateUtil.formatDateForStandard(new Date()));
			return new AutoXima(rate, ximaAmount, validBetAmount, "");
		}catch(Exception e){
			e.printStackTrace();
		}
		return new AutoXima("查询异常，请联系客服");
	}
	
	@Override
	public AutoXima getNTAutoXimaObject(Date endTime, Date startTime, String loginname) {
		// 取得：有效投注额，反水金额，洗码率
		Double validBetAmount = null;
		try{
			//有效投注额
			validBetAmount = getGameValidBetAmount(loginname, "nt", startTime, endTime) ;
			if (validBetAmount==null || validBetAmount<=0) {
				return new AutoXima("无投注记录");
			}
			validBetAmount=Math.round(validBetAmount*100.00)/100.00;
			Users users=(Users)userDao.get(Users.class, loginname, LockMode.UPGRADE);
			
			double rate = SlotUtil.getSlotRate(users);
			
			/*********************************************/
			Double ximaAmount = validBetAmount * rate;
			ximaAmount = ximaAmount>users.getEarebate()?users.getEarebate():Math.round(ximaAmount*100.00)/100.00;
			
			log.info("NT自助反水-->用户："+loginname+"，有效投注额："+validBetAmount+"，洗码率："+rate+"，起始时间："+DateUtil.formatDateForStandard(startTime)+"，结束时间："+DateUtil.formatDateForStandard(endTime)+"，当前时间："+DateUtil.formatDateForStandard(new Date()));
			return new AutoXima(rate, ximaAmount, validBetAmount, "");
		}catch(Exception e){
			e.printStackTrace();
		}
		return new AutoXima("查询异常，请联系客服");
	}
	
	@Override
	public AutoXima getMGAutoXimaObject(Date endTime, Date startTime, String loginname) {
		// 取得：有效投注额，反水金额，洗码率
		Double validBetAmount = null;
		try{
			//有效投注额
			validBetAmount = getGameValidBetAmount(loginname, "mg", startTime, endTime) ;
			if (validBetAmount==null || validBetAmount<=0) {
				return new AutoXima("无投注记录");
			}
			validBetAmount=Math.round(validBetAmount*100.00)/100.00;
			Users users=(Users)userDao.get(Users.class, loginname, LockMode.UPGRADE);
			
			double rate = SlotUtil.getSlotRate(users);
			
			/*********************************************/
			Double ximaAmount = validBetAmount * rate;
			ximaAmount = ximaAmount>users.getEarebate()?users.getEarebate():Math.round(ximaAmount*100.00)/100.00;
			
			log.info("MG自助反水-->用户："+loginname+"，有效投注额："+validBetAmount+"，洗码率："+rate+"，起始时间："+DateUtil.formatDateForStandard(startTime)+"，结束时间："+DateUtil.formatDateForStandard(endTime)+"，当前时间："+DateUtil.formatDateForStandard(new Date()));
			return new AutoXima(rate, ximaAmount, validBetAmount, "");
		}catch(Exception e){
			e.printStackTrace();
		}
		return new AutoXima("查询异常，请联系客服");
	}
	
	@Override
	public AutoXima getCQ9AutoXimaObject(Date endTime, Date startTime, String loginname) {
		// 取得：有效投注额，反水金额，洗码率
		Double validBetAmount = null;
		try{
			//有效投注额
			validBetAmount = getGameValidBetAmount(loginname, "cq9", startTime, endTime) ;
			if (validBetAmount==null || validBetAmount<=0) {
				return new AutoXima("无投注记录");
			}
			validBetAmount=Math.round(validBetAmount*100.00)/100.00;
			Users users=(Users)userDao.get(Users.class, loginname, LockMode.UPGRADE);
			
			double rate = SlotUtil.getSlotRate(users);
			
			/*********************************************/
			Double ximaAmount = validBetAmount * rate;
			ximaAmount = ximaAmount>users.getEarebate()?users.getEarebate():Math.round(ximaAmount*100.00)/100.00;
			
			log.info("CQ9自助反水-->用户："+loginname+"，有效投注额："+validBetAmount+"，洗码率："+rate+"，起始时间："+DateUtil.formatDateForStandard(startTime)+"，结束时间："+DateUtil.formatDateForStandard(endTime)+"，当前时间："+DateUtil.formatDateForStandard(new Date()));
			return new AutoXima(rate, ximaAmount, validBetAmount, "");
		}catch(Exception e){
			e.printStackTrace();
		}
		return new AutoXima("查询异常，请联系客服");
	}
	
	@Override
	public AutoXima getPGAutoXimaObject(Date endTime, Date startTime, String loginname) {
		// 取得：有效投注额，反水金额，洗码率
		Double validBetAmount = null;
		try{
			//有效投注额
			validBetAmount = getGameValidBetAmount(loginname, "pg", startTime, endTime) ;
			if (validBetAmount==null || validBetAmount<=0) {
				return new AutoXima("无投注记录");
			}
			validBetAmount=Math.round(validBetAmount*100.00)/100.00;
			Users users=(Users)userDao.get(Users.class, loginname, LockMode.UPGRADE);
			
			double rate = SlotUtil.getSlotRate(users);
			
			/*********************************************/
			Double ximaAmount = validBetAmount * rate;
			ximaAmount = ximaAmount>users.getEarebate()?users.getEarebate():Math.round(ximaAmount*100.00)/100.00;
			
			log.info("PG自助反水-->用户："+loginname+"，有效投注额："+validBetAmount+"，洗码率："+rate+"，起始时间："+DateUtil.formatDateForStandard(startTime)+"，结束时间："+DateUtil.formatDateForStandard(endTime)+"，当前时间："+DateUtil.formatDateForStandard(new Date()));
			return new AutoXima(rate, ximaAmount, validBetAmount, "");
		}catch(Exception e){
			e.printStackTrace();
		}
		return new AutoXima("查询异常，请联系客服");
	}
	
	@Override
	public AutoXima getDTAutoXimaObject(Date endTime, Date startTime, String loginname) {
		// 取得：有效投注额，反水金额，洗码率
		Double validBetAmount = null;
		try{
			//有效投注额
			validBetAmount = getGameValidBetAmount(loginname, "dt", startTime, endTime) ;
			if (validBetAmount==null || validBetAmount<=0) {
				return new AutoXima("无投注记录");
			}
			validBetAmount=Math.round(validBetAmount*100.00)/100.00;
			Users users=(Users)userDao.get(Users.class, loginname, LockMode.UPGRADE);
			
			
			double rate = SlotUtil.getSlotRate(users);
			/*********************************************/
			Double ximaAmount = validBetAmount * rate;
			ximaAmount = ximaAmount>users.getEarebate()?users.getEarebate():Math.round(ximaAmount*100.00)/100.00;
			
			log.info("DT自助反水-->用户："+loginname+"，有效投注额："+validBetAmount+"，洗码率："+rate+"，起始时间："+DateUtil.formatDateForStandard(startTime)+"，结束时间："+DateUtil.formatDateForStandard(endTime)+"，当前时间："+DateUtil.formatDateForStandard(new Date()));
			return new AutoXima(rate, ximaAmount, validBetAmount, "");
		}catch(Exception e){
			e.printStackTrace();
		}
		return new AutoXima("查询异常，请联系客服");
	}
	
	/**
	 * 该方法是用时间获取投注额的方法，以今天0点为准，如果最后自助洗码时间比改时间大，返回自助洗码时间，否则返回该时间
	 * @author CK
	 * @param loginname
	 * @param proposalType
	 * @return
	 * */
	private String getSlotSelfXimaTime(String loginname) {
		
		JSONObject object=new JSONObject(); 
	
		Calendar calend = Calendar.getInstance();
		calend.set(Calendar.HOUR_OF_DAY,0 );
		calend.set(Calendar.MINUTE, 0);
		calend.set(Calendar.SECOND, 0);

		for (Iterator iterator = SlotUtil.PLAFROMVALIDXIMA.keySet().iterator(); iterator.hasNext();) {
			String ximaType = (String) iterator.next();
			// 获得最后一次自助洗码的时间
			DetachedCriteria c=DetachedCriteria.forClass(Proposal.class);
			c.add(Restrictions.eq("loginname", loginname)).add(Restrictions.eq("flag", ProposalFlagType.EXCUTED.getCode()));
			c.add(Restrictions.eq("type", SlotUtil.PLAFROMVALIDXIMA.get(ximaType)));
			c.addOrder(Order.desc("createTime"));
			//TODO 此处以后加优化，只获取几条数据。
			List list = gameinfoDao.findByCriteria(c);
			
			if(list != null && list.size() > 0){
				Proposal p = (Proposal) list.get(0);
				DetachedCriteria ximaCriteria=DetachedCriteria.forClass(Xima.class);
				ximaCriteria.setProjection(Projections.property("endTime"));
				ximaCriteria.add(Restrictions.eq("pno", p.getPno()));
				List ximaObject = gameinfoDao.findByCriteria(ximaCriteria);
				
				if (ximaObject==null||ximaObject.size()<=0) {
					object.put(ximaType, DateUtil.formatDateForStandard(calend.getTime()));
				}else{
					Date endTime = (Date) ximaObject.get(0);            //最后一次自助洗码时间
					
					if(endTime.compareTo(calend.getTime()) > 0){
						object.put(ximaType, DateUtil.formatDateForStandard(endTime));
					} else {
						object.put(ximaType, DateUtil.formatDateForStandard(calend.getTime()));
					}
				}
			} else {
				object.put(ximaType,DateUtil.formatDateForStandard(calend.getTime()));
			}
		}
	    object.put("endTime", DateUtil.formatDateForStandard(new Date()));
	    object.put("message", "success");
	    return object.toString();
		
	}
	
	
	/**
	 * 取得：有效投注额，反水金额，洗码率
	 */
	public AutoXima getSlotAutoXimaObject( String loginname) {
		
		JSONObject jResult=new JSONObject();
		
		try{
			//获取今天有效投注额
			List<Bean4Xima> list = getSlotValidBetAmount(loginname,DateUtil.formatDateForStandard(DateUtil.ntStart()),DateUtil.formatDateForStandard(DateUtil.ntEnd()));
		
			if (list==null || list.size()==0) {
				return new AutoXima("无投注记录");
			}
			
			String dateResult=this.getSlotSelfXimaTime(loginname); //获取最后一次洗码时间

			JSONObject dateJsonOj =  JSONObject.fromObject(dateResult);
			
			for (int i = 0; i < list.size(); i++) {
				
				Double validBetAmount=list.get(i).getBetAmount();
				
				validBetAmount=Math.round(validBetAmount*100.00)/100.00;
				Users users=(Users)userDao.get(Users.class, loginname);
				
				double rate = SlotUtil.getSlotRate(users);
				
				/*********************************************/
				Double ximaAmount = validBetAmount * rate;
				ximaAmount = ximaAmount>users.getPtrebate()?users.getPtrebate():Math.round(ximaAmount*100.00)/100.00;
				
				log.info(list.get(i).getPlatfrom()+"自助反水-->用户："+loginname+"，有效投注额："+validBetAmount+"，洗码率："+rate+"，当前时间："+DateUtil.formatDateForStandard(new Date()));

				String startTime = dateJsonOj.get(list.get(i).getPlatfrom()).toString();
				String endTime = dateJsonOj.get("endTime").toString();
				if(ximaAmount == users.getPtrebate().doubleValue()){
					jResult.put(list.get(i).getPlatfrom(),JSONObject.fromObject(new AutoXima(rate,startTime,endTime, ximaAmount, ximaAmount/rate)));
				}else{
					jResult.put(list.get(i).getPlatfrom(),JSONObject.fromObject(new AutoXima(rate, startTime,endTime,ximaAmount, validBetAmount)));
				}
			}
			return new AutoXima(jResult.toString(),"success");
		}catch(Exception e){
			e.printStackTrace();
		}
		return new AutoXima(jResult.toString(),"查询异常，请联系客服");
	}
	
	public List<Bean4Xima> getSlotValidBetAmount(String loginname, String startTime, String endTime) {

		List<Bean4Xima> list = SlotUtil.getPlayerBetsByTime(loginname, startTime, endTime);

		List<Bean4Xima> rList = new ArrayList<Bean4Xima>();

		for (int i = 0; i < list.size(); i++) {

			Bean4Xima bean4Xima = list.get(i);
			// slot是总计，可以移除
			if (StringUtil.equals("slot", bean4Xima.getPlatfrom()) || SlotUtil.PLAFROMVALIDXIMA.get(bean4Xima.getPlatfrom()) == null) {
				continue;
			} else {
				Calendar calendP = Calendar.getInstance(); // 开始时间
				calendP.set(Calendar.HOUR_OF_DAY, 0);
				calendP.set(Calendar.MINUTE, 0);
				calendP.set(Calendar.SECOND, 0);

				DetachedCriteria c = DetachedCriteria.forClass(Proposal.class);
				c.add(Restrictions.gt("createTime", calendP.getTime()));
				c.add(Restrictions.le("createTime", new Date()));
				c.add(Restrictions.eq("loginname", loginname));
				c.add(Restrictions.or(Restrictions.eq("flag", ProposalFlagType.EXCUTED.getCode()),
						Restrictions.eq("flag", ProposalFlagType.AUDITED.getCode())));
				c.add(Restrictions.eq("type", SlotUtil.PLAFROMVALIDXIMA.get(bean4Xima.getPlatfrom())));
				c.setProjection(Projections.property("pno"));

				List selflist = proposalDao.findByCriteria(c); //

				Double selfXiMaAmount = 0.0;

				if (selflist != null && !selflist.isEmpty()) {
					DetachedCriteria x = DetachedCriteria.forClass(Xima.class);
					x.add(Restrictions.in("pno", selflist.toArray()));
					x.setProjection(Projections.sum("firstCash"));
					List sumx = proposalDao.findByCriteria(x);
					if (sumx != null && !sumx.isEmpty() && null != sumx.get(0)) {
						selfXiMaAmount = (Double) sumx.get(0);
					}
				}

				bean4Xima.setBetAmount(bean4Xima.getBetAmount() - selfXiMaAmount); // 本次扣去之前得洗码
				rList.add(bean4Xima);
			}

		}
		return rList;
	}
    
    @Override
   	public String execXimaSlot(String loginname,AutoXima autoXima,Date timeEnd)throws Exception {
	
 	
		ProposalType proposalType = null ;
		String remark="自助洗码;秒反水;executed";
		
		Date endTime = timeEnd;
		Date startTime = DateUtil.parseDateForStandard(autoXima.getStartTimeStr());
		remark = autoXima.getPlatfrom()+"自助洗码;秒反水;executed";
		proposalType = proposalType.getType(SlotUtil.PLAFROMVALIDXIMA.get(autoXima.getPlatfrom())) ;
		

		        if (checkGameIsProtect(autoXima.getPlatfrom())) {
					return autoXima.getPlatfrom()+":自助反水维护中,";
				}
				
			    if (autoXima.getXimaAmount()==0) {
			    	return autoXima.getPlatfrom()+":不满足反水条件,";
				}
			    
				if(autoXima.getXimaAmount()<1){
					return autoXima.getPlatfrom()+":攒够1块以上再来自助反水,";
				}
					
			Users user=proposalDao.getUsers(loginname);
				
		    DetachedCriteria d=DetachedCriteria.forClass(Proposal.class);    //计算自助反水总额是否大于28888

			Calendar calendP = Calendar.getInstance();
			calendP.set(Calendar.HOUR_OF_DAY,0 );
			calendP.set(Calendar.MINUTE, 0);
			calendP.set(Calendar.SECOND, 0);
			d.add(Restrictions.ge("createTime", calendP.getTime()));

			d.add(Restrictions.eq("loginname", user.getLoginname()));
			d.add(Restrictions.eq("flag", ProposalFlagType.EXCUTED.getCode()));
			
			d.add(Restrictions.eq("type", proposalType.getCode()));
			d.setProjection(Projections.sum("amount"));
			List sumList = gameinfoDao.findByCriteria(d);
			if(null!=sumList && !sumList.isEmpty() && null!=sumList.get(0)){
				Double dsum =(Double)sumList.get(0);
				if(dsum + autoXima.getXimaAmount()>user.getPtrebate()){
					return autoXima.getPlatfrom()+":当天反水总金额最多"+user.getPtrebate()+"元!";
				}
			}
				
				
			try {

					String pno = seqDao.generateProposalPno(proposalType);
					Xima xima = new Xima(pno, user.getRole(), user.getLoginname(), "网银支付", autoXima.getValidAmount(), autoXima.getXimaAmount(), DateUtil.convertToTimestamp(startTime), DateUtil.convertToTimestamp(endTime), autoXima.getRate(), remark);
					Proposal proposal = new Proposal(pno, user.getLoginname(), DateUtil.now(), proposalType.getCode(), user.getLevel(), user.getLoginname(), autoXima.getXimaAmount(),user.getAgent(), ProposalFlagType.EXCUTED.getCode(),
							Constants.FROM_FRONT, remark, "customer");
					if(autoXima.getXimaAmount() >= 100){//反水金额100以上需审核
						proposal.setPassflag(2);
					} else {
						remark += ";自动审核";
						proposal.setRemark(remark);
						xima.setRemark(remark);
						proposal.setPassflag(1);
					}
					proposal.setExecuteTime(new Date());
					taskDao.generateTasks(pno, user.getLoginname());
					gameinfoDao.save(xima);
					gameinfoDao.save(proposal);
					tradeDao.changeCredit(xima.getLoginname(), Double.valueOf(Math.abs(xima.getTryCredit().doubleValue())),CreditChangeType.XIMA_CONS.getCode(), pno, remark);
					return autoXima.getPlatfrom()+":秒返水执行成功,";
				} catch (Exception e) {
					throw e;
				}
	}
    
    public boolean checkGameIsProtect(String platform) {

		try {
			DetachedCriteria dc = DetachedCriteria.forClass(Const.class);
			dc.add(Restrictions.eq("id", platform + "自助洗码"));
			Const const1 = (Const) gameinfoDao.findByCriteria(dc).get(0);
			if (const1.getValue().equals("0")) {
				return true;
			}

		} catch (Exception e) {
			return true;
		}

		return false;
	}
    
}
