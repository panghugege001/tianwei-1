package dfh.service.implementations;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jxl.Cell;
import jxl.CellType;
import jxl.LabelCell;
import jxl.NumberCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;

import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.csvreader.CsvReader;

import dfh.action.vo.AginFishVo;
import dfh.action.vo.KgVo;
import dfh.action.vo.XimaVO;
import dfh.dao.ProposalDao;
import dfh.dao.SeqDao;
import dfh.model.Activity;
import dfh.model.AgProfit;
import dfh.model.AgTryProfit;
import dfh.model.AgentCustomer;
import dfh.model.Customer;
import dfh.model.Proposal;
import dfh.model.PtCommissions;
import dfh.model.PtCoupon;
import dfh.model.PtProfit;
import dfh.model.RecordMail;
import dfh.model.SystemConfig;
import dfh.model.Users;
import dfh.model.Xima;
import dfh.model.bean.Bean4Xima;
import dfh.model.bean.XimaDataVo;
import dfh.model.enums.ProposalFlagType;
import dfh.model.enums.ProposalType;
import dfh.model.enums.VipLevel;
import dfh.utils.Arith;
import dfh.utils.Constants;
import dfh.utils.DateUtil;
import dfh.utils.SlotUtil;
import edu.emory.mathcs.backport.java.util.Arrays;

public class SystemBatchAutoAddProposal extends AbstractBatchGameinfoServiceImpl  {
	
	private Logger log=Logger.getLogger(SystemBatchAutoAddProposal.class);
	private ProposalDao proposalDao;
	private Sheet sheet = null;
	private Workbook wb = null;
	private InputStream stream = null;
	private SeqDao seqDao;
	
	@Override
	public String autoAddXimaProposal(File file,Double rate) throws Exception {
		// TODO Auto-generated method stub
		String msg=null;
		Date starttime=DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), -(1*24-12));
		Date endtime=DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), 12);
		
		Date bbinkeno_starttime=DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), -(1*24-0));
		Date bbinkeno_endtime=DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), 0);
		stream = new FileInputStream(file.toString());
		wb = Workbook.getWorkbook(stream);
		if (wb == null) {
			log.info("打开文件失败");
			return "打开文件失败";
		}
		sheet = wb.getSheet(0); // 取得工作表
		
		int rows = sheet.getRows(); // 行数
		int cols = sheet.getColumns();// 列数
		
		
		try {
			Calendar now = Calendar.getInstance();
			int week = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);//week =2 is monday || sunday
			int year = Calendar.getInstance().get(Calendar.YEAR);
			int month = Calendar.getInstance().get(Calendar.MONTH);
			int daymonth = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
			for (int i = 1; i < rows; i++) {
				if(rate==0.02){//AG and BBIN and AGIN rebate
					String string=this.getStringValue(i, 0).trim();
					if(string.startsWith("k_")){
						String loginname=string.substring(2);
						Users user = (Users) this.getUserDao().get(Users.class,loginname);
						if (user==null) {
							log.info("用户："+loginname+"不存在");
							continue;
						}
						//double newrate = 0.008;
						double newrate = getSlotRate(user);
						
						//获取活动返水
						Date date=new Date();
						DetachedCriteria dc=DetachedCriteria.forClass(Activity.class);
						dc.add(Restrictions.eq("activityStatus", 1));
						dc = dc.add(Restrictions.le("backstageStart", date));
						dc = dc.add(Restrictions.gt("backstageEnd", date));
						dc = dc.add(Restrictions.like("userrole", "%"+String.valueOf(user.getLevel())+"%"));
						List<Activity> listActivity = this.getUserDao().findByCriteria(dc);
						if(listActivity!=null&&listActivity.size()>0&&listActivity.get(0)!=null){
							Activity activity=listActivity.get(0);
							if(activity.getActivityPercent()!=null){
								newrate = activity.getActivityPercent();
							}
						}
						
						//************************************************
						Double agbet = Double.parseDouble(getStringValue(i, 3).trim());
						Double agprofit = Double.parseDouble(getStringValue(i, 4).trim());
						//白金以及白金以上单日返水无限制
						Double agRebateLimit = SlotUtil.getRebateLimit(user);
//						if(user.getLevel() >= VipLevel.BAIJIN.getCode().intValue()){
//							agRebateLimit = 9999999999d;
//						}else{
//							agRebateLimit = user.getAgrebate() ;
//						}
						
						XimaVO ximaObject = new XimaVO(agbet,user.getLoginname(),newrate,agRebateLimit);
						/***********************************/
						//进行自助反水缩减   昨天12点到今天12点之间的自助反水
						ximaObject = cutSelfXima(ximaObject, starttime, endtime, ProposalType.AGSELFXIMA, newrate, agRebateLimit) ;
						/***********************************/
						
						String remark = "ag系统洗码";
						String pno = this.getSeqDao().generateProposalPno(ProposalType.XIMA);
						log.info("正在处理提案号："+pno+",反水金额："+Math.round(ximaObject.getXimaAmouont()*100.00)/100.00+"...");
						Xima xima = new Xima(pno, user.getRole(), user.getLoginname(), "网银支付", ximaObject.getValidBetAmount(), ximaObject.getXimaAmouont(), DateUtil.convertToTimestamp(bbinkeno_starttime), DateUtil.convertToTimestamp(bbinkeno_endtime), ximaObject.getRate(), remark);
						Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.XIMA.getCode(), user.getLevel(), user.getLoginname(), ximaObject.getXimaAmouont(),user.getAgent(), ProposalFlagType.AUDITED.getCode(),
								Constants.FROM_BACK, remark, Constants.GENERATE_AUTO);
							this.getTaskDao().generateTasks(pno, "system");
							this.getProposalService().save(xima);
							this.getProposalService().save(proposal);
						AgProfit agProfit=new AgProfit(pno, "system", DateUtil.now(), ProposalType.XIMA.getCode(), user.getLevel(), loginname, -1*agprofit, user.getAgent(), ProposalFlagType.EXCUTED.getCode(), Constants.FROM_BACK, "用户AG平台输赢值");
						agProfit.setPlatform("ag");
						agProfit.setBettotal(agbet);
						this.getProposalService().save(agProfit);
					}else if(string.startsWith("ki_")){//agin 系统洗码
						String loginname=string.substring(3);
						Users user = (Users) this.getUserDao().get(Users.class,loginname);
						if (user==null) {
							log.info("用户："+loginname+"不存在");
							continue;
						}
						
						double newrate = SlotUtil.getLiveRate(user);
						//获取活动返水
						Date date=new Date();
						DetachedCriteria dc=DetachedCriteria.forClass(Activity.class);
						dc.add(Restrictions.eq("activityStatus", 1));
						dc = dc.add(Restrictions.le("backstageStart", date));
						dc = dc.add(Restrictions.gt("backstageEnd", date));
						dc = dc.add(Restrictions.like("userrole", "%"+String.valueOf(user.getLevel())+"%"));
						List<Activity> listActivity = this.getUserDao().findByCriteria(dc);
						if(listActivity!=null&&listActivity.size()>0&&listActivity.get(0)!=null){
							Activity activity=listActivity.get(0);
							if(activity.getActivityPercent()!=null){
								newrate = activity.getActivityPercent();
							}
						}
						
						Double aginbet = Double.parseDouble(getStringValue(i, 3).trim());
						Double aginprofit = Double.parseDouble(getStringValue(i, 4).trim());
						Double aginRebateLimit = SlotUtil.getRebateLimit(user);
						
						/****************************************************/
						XimaVO ximaObject = new XimaVO(aginbet,user.getLoginname(),newrate,aginRebateLimit);
						/***********************************/
						//进行自助反水缩减   昨天12点到今天12点之间的自助反水
						ximaObject = cutSelfXima(ximaObject, starttime, endtime, ProposalType.AGINSELFXIMA, newrate, aginRebateLimit) ;
						/***********************************/
						String remark = "agin系统洗码";
						
						String pno = this.getSeqDao().generateProposalPno(ProposalType.XIMA);
						log.info("正在处理提案号："+pno+",反水金额："+Math.round(ximaObject.getXimaAmouont()*100.00)/100.00+"...");
						Xima xima = new Xima(pno, user.getRole(), user.getLoginname(), "网银支付", aginbet, ximaObject.getXimaAmouont(), DateUtil.convertToTimestamp(bbinkeno_starttime), DateUtil.convertToTimestamp(bbinkeno_endtime), ximaObject.getRate(), remark);
						Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.XIMA.getCode(), user.getLevel(), user.getLoginname(), ximaObject.getXimaAmouont(),user.getAgent(), ProposalFlagType.AUDITED.getCode(),
								Constants.FROM_BACK, remark, Constants.GENERATE_AUTO);
							this.getTaskDao().generateTasks(pno, "system");
							this.getProposalService().save(xima);
							this.getProposalService().save(proposal);
						AgProfit agProfit=new AgProfit(pno, "system", DateUtil.now(), ProposalType.XIMA.getCode(), user.getLevel(), loginname, -1*aginprofit, user.getAgent(), ProposalFlagType.EXCUTED.getCode(), Constants.FROM_BACK, "用户AGIN平台输赢值");
						agProfit.setPlatform("agin");
						agProfit.setBettotal(aginbet);
						this.getProposalService().save(agProfit);
					}else if (string.startsWith("eb")) {
						String loginname=string.substring(2);
						Users user = (Users) this.getUserDao().get(Users.class,loginname);
						if (user==null) {
							log.info("用户："+loginname+"不存在");
							continue;
						}

						 
						//自定义bbin反水
						double newrate = SlotUtil.getLiveRate(user);
						
						/**
						 * 进行国庆7天系统反水处理
						 */
//						if(2013==year && Calendar.OCTOBER == month && daymonth>1 && daymonth<=8){
//							if (user.getLevel() >= VipLevel.XINGJI.getCode().intValue()) {
//								newrate = 0.012;
//							}
//						}
						/**
						 * 进行每周五12点-周六12点白金vip以上的系统反水处理
						 */
//						if(Calendar.SATURDAY == week){
//							if (user.getLevel() >= VipLevel.HUANGJIN.getCode().intValue()) {
//								newrate = 0.015;
//							}
//						}
						
						//获取活动返水
						Date date=new Date();
						DetachedCriteria dc=DetachedCriteria.forClass(Activity.class);
						dc.add(Restrictions.eq("activityStatus", 1));
						dc = dc.add(Restrictions.le("backstageStart", date));
						dc = dc.add(Restrictions.gt("backstageEnd", date));
						dc = dc.add(Restrictions.like("userrole", "%"+String.valueOf(user.getLevel())+"%"));
						List<Activity> listActivity = this.getUserDao().findByCriteria(dc);
						if(listActivity!=null&&listActivity.size()>0&&listActivity.get(0)!=null){
							Activity activity=listActivity.get(0);
							if(activity.getActivityPercent()!=null){
								newrate = activity.getActivityPercent();
							}
						}
						
						//************************************************
						//白金以及白金以上单日返水无限制
						Double bbinRebateLimit = SlotUtil.getRebateLimit(user);
//						if(user.getLevel() >= VipLevel.BAIJIN.getCode().intValue()){
//							bbinRebateLimit = 9999999999d;
//						}else{
//							bbinRebateLimit = user.getBbinrebate() ;
//						}
						XimaVO ximaObject = new XimaVO(this.getNumberValue(i, 4),user.getLoginname(),newrate,bbinRebateLimit);
						/***********************************/
						//进行自助反水缩减   昨天12点到今天12点之间的自助反水
						ximaObject = cutSelfXima(ximaObject, starttime, endtime, ProposalType.BBINSELFXIMA, newrate, bbinRebateLimit) ;
						/***********************************/
						String remark = "bbin系统洗码";
						String pno = this.getSeqDao().generateProposalPno(ProposalType.XIMA);
						log.info("正在处理提案号："+pno+",反水金额："+Math.round(ximaObject.getXimaAmouont()*100.00)/100.00+"...");
						Xima xima = new Xima(pno, user.getRole(), user.getLoginname(), "网银支付", ximaObject.getValidBetAmount(), ximaObject.getXimaAmouont(), DateUtil.convertToTimestamp(starttime), DateUtil.convertToTimestamp(endtime), ximaObject.getRate(), remark);
						Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.XIMA.getCode(), user.getLevel(), user.getLoginname(), ximaObject.getXimaAmouont(),user.getAgent(), ProposalFlagType.AUDITED.getCode(),
								Constants.FROM_BACK, remark, Constants.GENERATE_AUTO);
							this.getTaskDao().generateTasks(pno, "system");
							this.getProposalService().save(xima);
							this.getProposalService().save(proposal);
						AgProfit agProfit=new AgProfit(pno, "system", DateUtil.now(), ProposalType.XIMA.getCode(), user.getLevel(), user.getLoginname(), this.getNumberValue(i, 3), user.getAgent(), ProposalFlagType.EXCUTED.getCode(), Constants.FROM_BACK, "用户BBIN平台输赢值");
						agProfit.setPlatform("bbin");
						agProfit.setBettotal(this.getNumberValue(i, 4));
						this.getProposalService().save(agProfit);
					}
				}else if(rate==0.01){//EA rebate
					String string=this.getStringValue(i, 0).trim();
					Users user = (Users) this.getUserDao().get(Users.class,string.substring(6,string.length()));
					if (user==null) {
						log.info("用户："+string.substring(6,string.length())+"，不存在");
						continue;
					}
					
					double newrate = SlotUtil.getLiveRate(user);
					
					//获取活动返水
					Date date=new Date();
					DetachedCriteria dc=DetachedCriteria.forClass(Activity.class);
					dc.add(Restrictions.eq("activityStatus", 1));
					dc = dc.add(Restrictions.le("backstageStart", date));
					dc = dc.add(Restrictions.gt("backstageEnd", date));
					dc = dc.add(Restrictions.like("userrole", "%"+String.valueOf(user.getLevel())+"%"));
					List<Activity> listActivity = this.getUserDao().findByCriteria(dc);
					if(listActivity!=null&&listActivity.size()>0&&listActivity.get(0)!=null){
						Activity activity=listActivity.get(0);
						if(activity.getActivityPercent()!=null){
							newrate = activity.getActivityPercent();
						}
					}
					
					//白金以及白金以上单日返水无限制
					Double eaRebateLimit = SlotUtil.getRebateLimit(user);
//					if(user.getLevel() >= VipLevel.BAIJIN.getCode().intValue()){
//						eaRebateLimit = 9999999999d;
//					}else{
//						eaRebateLimit = user.getEarebate() ;
//					}
					//************************************************
					XimaVO ximaObject = new XimaVO(this.getNumberValue(i, 1),user.getLoginname(),newrate,eaRebateLimit);
					
					/***********************************/
					//进行自助反水缩减   昨天12点到今天12点之间的自助反水
					
					DetachedCriteria c=DetachedCriteria.forClass(Proposal.class);
					c.add(Restrictions.gt("createTime", starttime));
					c.add(Restrictions.le("createTime", endtime));
					c.add(Restrictions.eq("loginname", user.getLoginname()));
					
					c.add(Restrictions.or(Restrictions.eq("flag", ProposalFlagType.EXCUTED.getCode()),
					Restrictions.eq("flag", ProposalFlagType.AUDITED.getCode())));
					
					c.add(Restrictions.eq("type", ProposalType.SELFXIMA.getCode()));
					c.setProjection(Projections.property("pno"));
					List list = this.getProposalService().findByCriteria(c);
					if(list!=null && !list.isEmpty()){
						DetachedCriteria x=DetachedCriteria.forClass(Xima.class);
						x.add(Restrictions.in("pno", list.toArray()));
						x.setProjection(Projections.sum("firstCash"));
						List sumx = this.getProposalService().findByCriteria(x);
						Double d = 0.0;
						Double dremain =0.0;
						if(sumx!=null && !sumx.isEmpty() && null!=sumx.get(0) ){
							if((Double)sumx.get(0)*newrate<eaRebateLimit){//第一笔自助反水小于28888后,下一笔系统洗码不管有无都会执行
								dremain = eaRebateLimit-(Double)sumx.get(0)*newrate;//剩下可反水额度
								d = ximaObject.getValidBetAmount()-(Double)sumx.get(0);
								if(d<0){
									d=0.0;
								}
							}
							ximaObject.setValidBetAmount(d);
							ximaObject.setXimaAmouont(d*newrate>dremain?dremain:d*newrate);
						}
					}
					/***********************************/
					
					
					String remark = "ea系统洗码";
					String pno = this.getSeqDao().generateProposalPno(ProposalType.XIMA);
					log.info("正在处理提案号："+pno+",反水金额："+Math.round(ximaObject.getXimaAmouont()*100.00)/100.00+"...");
					Xima xima = new Xima(pno, user.getRole(), user.getLoginname(), "网银支付", ximaObject.getValidBetAmount(), ximaObject.getXimaAmouont(), DateUtil.convertToTimestamp(starttime), DateUtil.convertToTimestamp(endtime), ximaObject.getRate(), remark);
					Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.XIMA.getCode(), user.getLevel(), user.getLoginname(), ximaObject.getXimaAmouont(),user.getAgent(), ProposalFlagType.AUDITED.getCode(),
						Constants.FROM_BACK, remark, Constants.GENERATE_AUTO);
					this.getTaskDao().generateTasks(pno, "system");
					this.getProposalService().save(xima);
					this.getProposalService().save(proposal);
					AgProfit agProfit=new AgProfit(pno, "system", DateUtil.now(), ProposalType.XIMA.getCode(), user.getLevel(), user.getLoginname(), (0-this.getNumberValue(i, 2)), user.getAgent(), ProposalFlagType.EXCUTED.getCode(), Constants.FROM_BACK, "用户ea平台输赢值");
					agProfit.setPlatform("ea");
					agProfit.setBettotal(this.getNumberValue(i, 1));
					this.getProposalService().save(agProfit);
					
				}else if(rate == 0.03){//keno rebate  0.015   k_
					
					String string=this.getStringValue(i, 0).trim();
					/*if(string.startsWith("k_")){
						String loginname=string.substring(2);
						Users user = (Users) this.getUserDao().get(Users.class,loginname);
						if (user==null) {
							log.info("用户："+loginname+"不存在");
							continue;
						}
						
						double newrate = 0.020;
						newrate = user.getKenorate().doubleValue();
						newrate = 0.020;
						XimaVO ximaObject = new XimaVO(this.getNumberValue(i, 1),user.getLoginname(),newrate,user.getKenorebate());
						*//***********************************//*
						//进行自助反水缩减   keno昨天0点0分到今天23点59之间的自助反水
						ximaObject = cutSelfXima(ximaObject, bbinkeno_starttime, bbinkeno_endtime, ProposalType.KENOSELFXIMA, newrate, user.getKenorebate()) ;
						*//***********************************//*
						String remark = "keno系统洗码";
						String pno = this.getSeqDao().generateProposalPno(ProposalType.XIMA);
						log.info("正在处理提案号："+pno+",反水金额："+Math.round(ximaObject.getXimaAmouont()*100.00)/100.00+"...");
						Xima xima = new Xima(pno, user.getRole(), user.getLoginname(), "网银支付", ximaObject.getValidBetAmount(), ximaObject.getXimaAmouont(), DateUtil.convertToTimestamp(bbinkeno_starttime), DateUtil.convertToTimestamp(bbinkeno_endtime), ximaObject.getRate(), remark);
						Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.XIMA.getCode(), user.getLevel(), user.getLoginname(), ximaObject.getXimaAmouont(),user.getAgent(), ProposalFlagType.AUDITED.getCode(),
								Constants.FROM_BACK, remark, Constants.GENERATE_AUTO);
							this.getTaskDao().generateTasks(pno, "system");
							this.getProposalService().save(xima);
							this.getProposalService().save(proposal);
						AgProfit agProfit=new AgProfit(pno, "system", DateUtil.now(), ProposalType.XIMA.getCode(), user.getLevel(), loginname, this.getNumberValue(i, 2), user.getAgent(), ProposalFlagType.EXCUTED.getCode(), Constants.FROM_BACK, "用户KENO平台输赢值");
						agProfit.setPlatform("keno");
						agProfit.setBettotal(this.getNumberValue(i, 1));
						this.getProposalService().save(agProfit);
					}*/
				}else if(rate == 0.04){
					String string=this.getStringValue(i, 1).trim();
					if(string.startsWith("e68_")){
						String loginname=string.substring(4);
						Users user = (Users) this.getUserDao().get(Users.class,loginname);
						if (user==null) {
							log.info("用户："+loginname+"不存在");
							continue;
						}
						
						double newrate = 0.004;
						//白金以及白金以上单日返水无限制
						Double sbRebateLimit = SlotUtil.getRebateLimit(user);
//						if(user.getLevel() >= VipLevel.BAIJIN.getCode().intValue()){
//							sbRebateLimit = 9999999999d;
//						}else{
//							sbRebateLimit = user.getSbrebate() ;
//						}
						XimaVO ximaObject = new XimaVO(this.getNumberValue(i, 3),user.getLoginname(),newrate,sbRebateLimit);
						/***********************************/
						//进行自助反水缩减   昨天12点到今天12点之间的自助反水
						ximaObject = cutSelfXima(ximaObject, starttime, endtime, ProposalType.SBSELFXIMA, newrate, sbRebateLimit) ;
						/***********************************/
						String remark = "SB系统洗码";
						String pno = this.getSeqDao().generateProposalPno(ProposalType.XIMA);
						log.info("正在处理提案号："+pno+",反水金额："+Math.round(ximaObject.getXimaAmouont()*100.00)/100.00+"...");
						Xima xima = new Xima(pno, user.getRole(), user.getLoginname(), "网银支付", ximaObject.getValidBetAmount(), ximaObject.getXimaAmouont(), DateUtil.convertToTimestamp(bbinkeno_starttime), DateUtil.convertToTimestamp(bbinkeno_endtime), ximaObject.getRate(), remark);
						Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.XIMA.getCode(), user.getLevel(), user.getLoginname(), ximaObject.getXimaAmouont(),user.getAgent(), ProposalFlagType.AUDITED.getCode(),
								Constants.FROM_BACK, remark, Constants.GENERATE_AUTO);
							this.getTaskDao().generateTasks(pno, "system");
							this.getProposalService().save(xima);
							this.getProposalService().save(proposal);
						AgProfit agProfit=new AgProfit(pno, "system", DateUtil.now(), ProposalType.XIMA.getCode(), user.getLevel(), loginname, -1*this.getNumberValue(i, 4), user.getAgent(), ProposalFlagType.EXCUTED.getCode(), Constants.FROM_BACK, "用户SB平台输赢值");
						agProfit.setPlatform("sb");
						agProfit.setBettotal(this.getNumberValue(i, 3));
						this.getProposalService().save(agProfit);
					}
				}else if(rate == 0.06){//keno2
					
					String string=this.getStringValue(i, 0).trim();
					if(string.startsWith("k_")){
						String loginname=string.substring(2);
						Users user = (Users) this.getUserDao().get(Users.class,loginname);
						if (user==null) {
							log.info("用户："+loginname+"不存在");
							continue;
						}
						
						double newrate = 0.020;
						
						//白金以及白金以上单日返水无限制
						Double kenoRebateLimit = SlotUtil.getRebateLimit(user);
//						if(user.getLevel() >= VipLevel.BAIJIN.getCode().intValue()){
//							kenoRebateLimit = 9999999999d;
//						}else{
//							kenoRebateLimit = user.getKenorebate() ;
//						}
						
						XimaVO ximaObject = new XimaVO(this.getNumberValue(i, 1),user.getLoginname(),newrate,kenoRebateLimit);
						/***********************************/
						//进行自助反水缩减   keno2昨天0点0分到今天23点59之间的自助反水
						ximaObject = cutSelfXima(ximaObject, bbinkeno_starttime, bbinkeno_endtime, ProposalType.KGSELFXIMA, newrate, kenoRebateLimit) ;
						/***********************************/
						String remark = "keno2系统洗码";
						String pno = this.getSeqDao().generateProposalPno(ProposalType.XIMA);
						log.info("正在处理提案号："+pno+",反水金额："+Math.round(ximaObject.getXimaAmouont()*100.00)/100.00+"...");
						Xima xima = new Xima(pno, user.getRole(), user.getLoginname(), "网银支付", ximaObject.getValidBetAmount(), ximaObject.getXimaAmouont(), DateUtil.convertToTimestamp(bbinkeno_starttime), DateUtil.convertToTimestamp(bbinkeno_endtime), ximaObject.getRate(), remark);
						Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.XIMA.getCode(), user.getLevel(), user.getLoginname(), ximaObject.getXimaAmouont(),user.getAgent(), ProposalFlagType.AUDITED.getCode(),
								Constants.FROM_BACK, remark, Constants.GENERATE_AUTO);
							this.getTaskDao().generateTasks(pno, "system");
							this.getProposalService().save(xima);
							this.getProposalService().save(proposal);
						AgProfit agProfit=new AgProfit(pno, "system", DateUtil.now(), ProposalType.XIMA.getCode(), user.getLevel(), loginname, this.getNumberValue(i, 2), user.getAgent(), ProposalFlagType.EXCUTED.getCode(), Constants.FROM_BACK, "用户KENO2平台输赢值");
						agProfit.setPlatform("keno2");
						agProfit.setBettotal(this.getNumberValue(i, 1));
						this.getProposalService().save(agProfit);
					}
				}else if(rate == 0.07){//ebet
					String loginname=this.getStringValue(i, 0).trim();
					if(loginname.startsWith("2")){
						loginname=loginname.substring(1);
						Users user = (Users) this.getUserDao().get(Users.class,loginname);
						if (user==null) {
							log.info("用户："+loginname+"不存在");
							continue;
						}
//						double newrate = user.getRate().doubleValue();
						double newrebate = SlotUtil.getRebateLimit(user);
						double newrate = SlotUtil.getLiveRate(user);
						
						//XimaVO ximaObject = new XimaVO(this.getNumberValue(i, 1),user.getLoginname(),newrate,newrebate);
						Date date=new Date();
						DetachedCriteria dc=DetachedCriteria.forClass(Activity.class);
						dc.add(Restrictions.eq("activityStatus", 1));
						dc = dc.add(Restrictions.le("backstageStart", date));
						dc = dc.add(Restrictions.gt("backstageEnd", date));
						dc = dc.add(Restrictions.like("userrole", "%"+String.valueOf(user.getLevel())+"%"));
						List<Activity> listActivity = this.getUserDao().findByCriteria(dc);
						if(listActivity!=null&&listActivity.size()>0&&listActivity.get(0)!=null){
							Activity activity=listActivity.get(0);
							if(activity.getActivityPercent()!=null){
								newrate = activity.getActivityPercent();
							}
						}
						
						XimaVO ximaObject = new XimaVO(this.getNumberValue(i, 1),user.getLoginname(),newrate,newrebate);
						String remark = "ebet系统洗码";
						String pno = this.getSeqDao().generateProposalPno(ProposalType.XIMA);
						log.info("正在处理提案号："+pno+",反水金额："+Math.round(ximaObject.getXimaAmouont()*100.00)/100.00+"...");
						Xima xima = new Xima(pno, user.getRole(), user.getLoginname(), "网银支付", ximaObject.getValidBetAmount(), ximaObject.getXimaAmouont(), DateUtil.convertToTimestamp(bbinkeno_starttime), DateUtil.convertToTimestamp(bbinkeno_endtime), ximaObject.getRate(), remark);
						Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.XIMA.getCode(), user.getLevel(), user.getLoginname(), ximaObject.getXimaAmouont(),user.getAgent(), ProposalFlagType.AUDITED.getCode(),
								Constants.FROM_BACK, remark, Constants.GENERATE_AUTO);
							this.getTaskDao().generateTasks(pno, "system");
							this.getProposalService().save(xima);
							this.getProposalService().save(proposal);
						AgProfit agProfit=new AgProfit(pno, "system", DateUtil.now(), ProposalType.XIMA.getCode(), user.getLevel(), loginname, this.getNumberValue(i, 2), user.getAgent(), ProposalFlagType.EXCUTED.getCode(), Constants.FROM_BACK, "用户EBET平台输赢值");
						agProfit.setPlatform("ebet");
						agProfit.setBettotal(this.getNumberValue(i, 1));
						this.getProposalService().save(agProfit);
					}
				}else if(rate == 0.09){//ttg   
					String loginname=this.getStringValue(i, 1).trim();
					if(!loginname.startsWith("LF123_k")){
						log.info("用户："+loginname+"不是龙都平台玩家");
						continue;
					}
					loginname=loginname.substring(7);
					Users user = (Users) this.getUserDao().get(Users.class,loginname);
					if (user==null) {
						log.info("用户："+loginname+"不存在");
						continue;
					}
					//double newrate = user.getAgrate().doubleValue();
					
					double newrate = SlotUtil.getSlotRate(user);
//					PT新会员 忠实是0.4  星级赌神和金牌赌神是0.6  白金和钻石是0.8  至尊是1-------  废弃
					//定为新会员及忠实会员 0.6% ，星级、金牌、白金 0.8% ，钻石至尊 1%
					
					String nowdate = DateUtil.fmtyyyyMMdd(new Date());
					if(nowdate.equals("20160805") || nowdate.equals("20160812") || nowdate.equals("20160819")){

						/*if(user.getLevel() <= VipLevel.COMMON.getCode().intValue()){
							newrate = 0.008;
						}else if(user.getLevel() >= VipLevel.XINGJI.getCode().intValue() && user.getLevel() <= VipLevel.ZHIZUN.getCode().intValue()){
							newrate = 0.015;
						}*/
						newrate = 0.015;
					}
					
					Double newrebate = SlotUtil.getRebateLimit(user) ;
//					if(user.getLevel() <= VipLevel.BAIJIN.getCode()){
//						newrebate = 28888.00 ;
//					}else{
//						newrebate = 99999999999.00 ;
//					}
					
					Double agbet = Double.parseDouble(getStringValue(i, 9).trim().replace(",", ""));//投注额
					//agbet=agbet*6;
					String str=getStringValue(i, 10).trim();//如果玩家赢钱，导入的数据为负数  
					str=str.replace(",", "");
					/*if(str.contains("(")){//excel        
						str.replace("(", "");
						str.replace(")", "");
					}else{
						str ="-"+str;
					}*/
					Double agprofit =-1*Double.parseDouble(str);
					//agprofit=agprofit*6;
					XimaVO ximaObject = new XimaVO(agbet,user.getLoginname(),newrate,newrebate);
					/***********************************/
					//进行自助反水缩减   昨天0点到今天0点之间的自助反水
					ximaObject = cutSelfXima(ximaObject, bbinkeno_starttime, bbinkeno_endtime, ProposalType.TTGSELFXIMA, newrate, newrebate) ;
					/***********************************/
					String remark = "ttg系统洗码";
					
					String pno = this.getSeqDao().generateProposalPno(ProposalType.XIMA);
					log.info("正在处理提案号："+pno+",反水金额："+Math.round(ximaObject.getXimaAmouont()*100.00)/100.00+"...");
					Xima xima = new Xima(pno, user.getRole(), user.getLoginname(), "网银支付", ximaObject.getValidBetAmount(), ximaObject.getXimaAmouont(), DateUtil.convertToTimestamp(bbinkeno_starttime), DateUtil.convertToTimestamp(bbinkeno_endtime), ximaObject.getRate(), remark);
					Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.XIMA.getCode(), user.getLevel(), user.getLoginname(), ximaObject.getXimaAmouont(),user.getAgent(), ProposalFlagType.AUDITED.getCode(),
							Constants.FROM_BACK, remark, Constants.GENERATE_AUTO);
						this.getTaskDao().generateTasks(pno, "system");
						this.getProposalService().save(xima);
						this.getProposalService().save(proposal);
					AgProfit agProfit=new AgProfit(pno, "system", DateUtil.now(), ProposalType.XIMA.getCode(), user.getLevel(), loginname, -1*agprofit, user.getAgent(), ProposalFlagType.EXCUTED.getCode(), Constants.FROM_BACK, "用户TTG平台输赢值");
					agProfit.setPlatform("ttg");
					agProfit.setBettotal(agbet);
					this.getProposalService().save(agProfit);
				} else if(rate == 0.1){//GPI
					String loginname = this.getStringValue(i, 0);
					
				}else if(rate == 0.11){//qt
					String loginname=this.getStringValue(i, 1).trim();
					Users user = (Users) this.getUserDao().get(Users.class,loginname);
					if (user==null) {
						log.info("用户："+loginname+"不存在");
						continue;
					}
					
					double newrate = SlotUtil.getSlotRate(user);
					
					//白金以及白金以上单日返水无限制
					Double qtRebateLimit = SlotUtil.getRebateLimit(user);
//					if(user.getLevel() >= VipLevel.BAIJIN.getCode().intValue()){
//						qtRebateLimit = 9999999999d;
//					}else{
//						qtRebateLimit = user.getPtrebate() ;
//					}
					
					Double agbet = Double.parseDouble(getStringValue(i, 9).trim().replace(",", ""));//投注额
					String str = getStringValue(i, 10).trim();//出款
					str=str.replace(",", "");

					Double agprofit = Double.parseDouble(str);
					XimaVO ximaObject = new XimaVO(agbet, user.getLoginname(), newrate, qtRebateLimit);
					/***********************************/
					//进行自助反水缩减   昨天0点到今天0点之间的自助反水
					ximaObject = cutSelfXima(ximaObject, bbinkeno_starttime, bbinkeno_endtime, ProposalType.QTSELFXIMA, newrate, qtRebateLimit) ;
					/***********************************/
					String remark = "qt系统洗码";
					
					String pno = this.getSeqDao().generateProposalPno(ProposalType.XIMA);
					log.info("正在处理提案号："+pno+",反水金额："+Math.round(ximaObject.getXimaAmouont()*100.00)/100.00+"...");
					Xima xima = new Xima(pno, user.getRole(), user.getLoginname(), "网银支付", ximaObject.getValidBetAmount(), ximaObject.getXimaAmouont(), DateUtil.convertToTimestamp(bbinkeno_starttime), DateUtil.convertToTimestamp(bbinkeno_endtime), ximaObject.getRate(), remark);
					Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.XIMA.getCode(), user.getLevel(), user.getLoginname(), ximaObject.getXimaAmouont(),user.getAgent(), ProposalFlagType.AUDITED.getCode(),
							Constants.FROM_BACK, remark, Constants.GENERATE_AUTO);
						this.getTaskDao().generateTasks(pno, "system");
						this.getProposalService().save(xima);
						this.getProposalService().save(proposal);
					AgProfit agProfit=new AgProfit(pno, "system", DateUtil.now(), ProposalType.XIMA.getCode(), user.getLevel(), loginname, agprofit, user.getAgent(), ProposalFlagType.EXCUTED.getCode(), Constants.FROM_BACK, "用户QT平台输赢值");
					agProfit.setPlatform("qt");
					agProfit.setBettotal(agbet);
					this.getProposalService().save(agProfit);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			
		}finally{
			this.closeFile();
			file.delete();
		}
		
		return msg;
	}
	/**
	 * BBin报表导入
	 * @param file
	 * @return
	 * */
	@Override
	public String systemXimaForBBin(File file,String gamekind){
		String msg = null;

		try {
			stream = new FileInputStream(file.toString());
			wb = Workbook.getWorkbook(stream);

			if (wb == null) {
				log.info("打开文件失败");
				return "打开文件失败";
			}
			sheet = wb.getSheet(0); // 取得工作表
			int rows = sheet.getRows(); // 行数
			log.info("获取bbin洗码条数："+rows);
			List<AginFishVo> gfs = new ArrayList<AginFishVo>() ;
			for (int i = 2; i < rows; i++) {

				String name=this.getStringValue(i, 1).trim();
				if(!name.startsWith("k")){
					continue;
				}
				name = name.substring(1);
				AginFishVo vo = new AginFishVo();
				Users user = (Users) this.getUserDao().get(Users.class,name);
				if (user==null) {
					log.info("用户："+name+"不存在");
					continue;
				}

				vo.setLoginname(name);
				Double bet;
				Double paicai;//派彩
				try {
					bet = this.getNumberValue(i, 5);
				} catch (Exception e) {
					bet = Double.parseDouble(this.getStringValue(i, 5).trim());
				}
				try {
					paicai = this.getNumberValue(i,4);//派彩
				} catch (Exception e1) {
					paicai = Double.parseDouble(this.getStringValue(i, 4).trim());//派彩
				}

				Double profit = paicai;
				vo.setBet(bet);
				vo.setProfit(profit*(-1));
				vo.setUser(user);
				gfs.add(vo) ;
			}

			Date starttime=DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), -(1*24-12));
			Date endtime=DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), 12);

			for (AginFishVo gfVo : gfs) {
				Users user = gfVo.getUser();
				String loginname = null;

				String string=gfVo.getLoginname();
				Double bet = gfVo.getBet();	//投注额
				Double amount = gfVo.getProfit(); //玩家总输赢

				if(string.startsWith("zb")){
					loginname = string.substring(2,string.length());
				}else{
					loginname = string.substring(1,string.length());
				}

				double newrate = SlotUtil.getSlotRate(user);


				//白金以及白金以上单日返水无限制
				Double rebateLimit = SlotUtil.getRebateLimit(user);

				XimaVO ximaObject = new XimaVO(bet,user.getLoginname(),newrate,rebateLimit);


				DetachedCriteria c=DetachedCriteria.forClass(Proposal.class);
				c.add(Restrictions.gt("createTime", starttime));
				c.add(Restrictions.le("createTime", endtime));
				c.add(Restrictions.eq("loginname", user.getLoginname()));

				c.add(Restrictions.or(Restrictions.eq("flag", ProposalFlagType.EXCUTED.getCode()),
						Restrictions.eq("flag", ProposalFlagType.AUDITED.getCode())));

				c.add(Restrictions.eq("type", ProposalType.SELFXIMA.getCode()));
				c.setProjection(Projections.property("pno"));
				List list = this.proposalDao.findByCriteria(c);
				if(list!=null && !list.isEmpty()){
					DetachedCriteria x=DetachedCriteria.forClass(Xima.class);
					x.add(Restrictions.in("pno", list.toArray()));
					x.setProjection(Projections.sum("firstCash"));
					List sumx = this.proposalDao.findByCriteria(x);
					Double d = 0.0;
					Double dremain =0.0;
					if(sumx!=null && !sumx.isEmpty() && null!=sumx.get(0)){
						if((Double)sumx.get(0)*newrate<user.getEarebate()){//第一笔自助反水小于28888后,下一笔系统洗码不管有无都会执行
							dremain = user.getEarebate()-(Double)sumx.get(0)*newrate;//剩下可反水额度
							d = ximaObject.getValidBetAmount()-(Double)sumx.get(0);
							if(d<0){
								d=0.0;
							}
						}
						ximaObject.setXimaAmouont(d*newrate>dremain?dremain:d*newrate);
					}
				}
				/***********************************/
				String platform = gamekind.equals("3")?"bbinvid":"bbinele";
				String remark = platform+"系统洗码";
				String pno = this.getSeqDao().generateProposalPno(ProposalType.XIMA);
				/*log.info("正在处理提案号："+pno+",反水金额："+Math.round(ximaObject.getXimaAmouont()*100.00)/100.00+"...");
				Xima xima = new Xima(pno, user.getRole(), user.getLoginname(), "网银支付", bet, ximaObject.getXimaAmouont(), DateUtil.convertToTimestamp(starttime), DateUtil.convertToTimestamp(endtime), ximaObject.getRate(), remark);
				Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.XIMA.getCode(), user.getLevel(), user.getLoginname(), ximaObject.getXimaAmouont(),user.getAgent(), ProposalFlagType.AUDITED.getCode(),
						Constants.FROM_BACK, remark, Constants.GENERATE_AUTO);
				this.getTaskDao().generateTasks(pno, "system");
				if(gamekind.equals("3")) {//真人才有反水
					this.proposalDao.save(xima);
					this.proposalDao.save(proposal);
				}*/
				AgProfit agProfit=new AgProfit(pno, "system", DateUtil.now(), ProposalType.XIMA.getCode(), user.getLevel(), user.getLoginname(), 0-amount, user.getAgent(), ProposalFlagType.EXCUTED.getCode(), Constants.FROM_BACK, "用户"+platform+"平台输赢值");
				agProfit.setPlatform(platform);
				agProfit.setBettotal(bet);
				this.proposalDao.save(agProfit);
			}
		} catch (Exception e) {
			log.error("bbin洗码错误：", e);
			msg = e.toString();
		} finally {
			this.closeFile();
			file.delete();
		}

		return msg ;
	}



	@Override
	public List<Bean4Xima> excelToNTwoVo(File file) {
		try {
			stream = new FileInputStream(file.toString());
			wb = Workbook.getWorkbook(stream);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (wb == null) {
			log.info("打开文件失败");
			return null;
		}
		sheet = wb.getSheet(0); // 取得工作表
		int rows = sheet.getRows(); // 行数
		List<Bean4Xima> gfs = new ArrayList<Bean4Xima>() ;
		try {
			for (int i = 1; i < rows; i++) {
				String name=this.getStringValue(i, 0).trim();
				name = name.substring(3);
				Bean4Xima vo = new Bean4Xima();
				Users user = (Users) this.getUserDao().get(Users.class,name);
				if (user==null) {
					log.info("用户："+name+"不存在");
					continue;
				}
				Double bet;
				Double win;
				try {
					bet = this.getNumberValue(i, 2);
				} catch (Exception e) {
					bet = Double.parseDouble(this.getStringValue(i,2).trim());
				}
				try {
					win = this.getNumberValue(i, 3);
				} catch (Exception e1) {
					win = Double.parseDouble(this.getStringValue(i, 3).trim());
				}

				vo.setUserName(user.getLoginname());
				vo.setBetAmount(Arith.round(bet, 2));
				vo.setProfit(-Arith.round(win, 2));
				gfs.add(vo) ;
			}
		} catch (Exception e) {
			log.error("NTWO EXCEL转换错误：", e);
			return null;
		} finally {
			this.closeFile();
			file.delete();
		}

		return gfs;
	}

	public String systemXimaForKg(File file){
		String msg = null;
		try {
			stream = new FileInputStream(file.toString());
			wb = Workbook.getWorkbook(stream);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		if (wb == null) {
			log.info("打开文件失败");
			return "打开文件失败";
		}
		sheet = wb.getSheet(0); // 取得工作表
		int rows = sheet.getRows(); // 行数
		List<KgVo> kgs = new ArrayList<KgVo>() ;
		for (int i = 1; i < rows; i++) {
			KgVo vo = new KgVo();
			String name=this.getStringValue(i, 0).trim().substring(2);
			Double bet = this.getNumberValue(i, 1) ; 
			Double profit = this.getNumberValue(i, 2) ;
			
			Users user = (Users) this.getUserDao().get(Users.class,name);
			if (user==null) {
				log.info("用户："+name+"不存在");
				continue;
			}
			
			vo.setLoginname(name);
			if(loginnameIsExist(kgs, name)){
				continue ;
			}
			for(int j = i + 1 ; j < rows ; j++){
				String subName = this.getStringValue(j, 0).trim().substring(2);
				Double subBet = this.getNumberValue(j, 1) ; 
				Double subProfit = this.getNumberValue(j, 2) ;
				if(subName.equals(name)){
					bet += subBet ;
					profit += subProfit ;
				}
			}
			vo.setBet(bet);
			vo.setProfit(profit);
			vo.setUser(user);
			kgs.add(vo) ;
		}
		
		Date starttime=DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), -(1*24-0));
		Date endtime  =DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), 0);
		
		for (KgVo kgVo : kgs) {
			double newrate = 0.020;
			//白金以及白金以上单日返水无限制
			Double keno2RebateLimit = SlotUtil.getRebateLimit(kgVo.getUser());
//			if(kgVo.getUser().getLevel() >= VipLevel.BAIJIN.getCode().intValue()){
//				keno2RebateLimit = 9999999999d;
//			}else{
//				keno2RebateLimit = kgVo.getUser().getPtrebate() ;
//			}
			
			XimaVO ximaObject = new XimaVO(kgVo.getBet(),kgVo.getLoginname(),newrate,keno2RebateLimit);
			/***********************************/
			//进行自助反水缩减   keno2昨天0点0分到今天23点59之间的自助反水
			ximaObject = cutSelfXima(ximaObject, starttime, endtime, ProposalType.KGSELFXIMA, newrate, keno2RebateLimit) ;
			/***********************************/
			String remark = "keno2系统洗码";
			String pno = this.getSeqDao().generateProposalPno(ProposalType.XIMA);
			log.info("正在处理提案号："+pno+",反水金额："+Math.round(ximaObject.getXimaAmouont()*100.00)/100.00+"...");
			Xima xima = new Xima(pno, kgVo.getUser().getRole(), kgVo.getUser().getLoginname(), "网银支付", ximaObject.getValidBetAmount(), ximaObject.getXimaAmouont(), DateUtil.convertToTimestamp(starttime), DateUtil.convertToTimestamp(endtime), ximaObject.getRate(), remark);
			Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.XIMA.getCode(), kgVo.getUser().getLevel(), kgVo.getLoginname(), ximaObject.getXimaAmouont(),kgVo.getUser().getAgent(), ProposalFlagType.AUDITED.getCode(),
					Constants.FROM_BACK, remark, Constants.GENERATE_AUTO);
				this.getTaskDao().generateTasks(pno, "system");
				this.getProposalService().save(xima);
				this.getProposalService().save(proposal);
			AgProfit agProfit=new AgProfit(pno, "system", DateUtil.now(), ProposalType.XIMA.getCode(), kgVo.getUser().getLevel(), kgVo.getLoginname(), kgVo.getProfit(), kgVo.getUser().getAgent(), ProposalFlagType.EXCUTED.getCode(), Constants.FROM_BACK, "用户KENO2平台输赢值");
			agProfit.setPlatform("keno2");
			agProfit.setBettotal(kgVo.getBet());
			this.getProposalService().save(agProfit);
		}
		return msg ;
	}
	
	public boolean loginnameIsExist(List<KgVo> kgs , String loginname){
		for (KgVo kgVo : kgs) {
			if(kgVo.getLoginname().equals(loginname)){
				return true ; 
			}
		}
		return false ;
	}
	
    public String autoAddXimaPtProposal(List<PtProfit> ptProfit) throws Exception {
    	Date starttime=DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), -(1*24-12));
		Date endtime=DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), 12);
    	if(ptProfit==null || ptProfit.size()<=0 ||ptProfit.get(0)==null){
			return "数据问空！";
		}
		for (PtProfit pt : ptProfit) {
			String loginName=pt.getLoginname();
			Users user = (Users) this.getUserDao().get(Users.class,loginName);
			if (user==null) {
				log.info("用户："+loginName+"，不存在");
				continue;
			}
			double newrate = SlotUtil.getSlotRate(user);
			//获取活动返水
			Date date=new Date();
			DetachedCriteria dc=DetachedCriteria.forClass(Activity.class);
			dc.add(Restrictions.eq("activityStatus", 1));
			dc = dc.add(Restrictions.le("backstageStart", date));
			dc = dc.add(Restrictions.gt("backstageEnd", date));
			dc = dc.add(Restrictions.like("userrole", "%"+String.valueOf(user.getLevel())+"%"));
			List<Activity> listActivity = this.getUserDao().findByCriteria(dc);
			if(listActivity!=null&&listActivity.size()>0&&listActivity.get(0)!=null){
				Activity activity=listActivity.get(0);
				if(activity.getActivityPercent()!=null){
					newrate = activity.getActivityPercent();
				}
			}
			
			//************************************************
			XimaVO ximaObject = new XimaVO(pt.getBetCredit(),user.getLoginname(),newrate,user.getPtrebate());
			
			/***********************************/
			//进行自助反水缩减   昨天12点到今天12点之间的自助反水
			/*DetachedCriteria c=DetachedCriteria.forClass(Proposal.class);
			c.add(Restrictions.gt("createTime", starttime));
			c.add(Restrictions.le("createTime", endtime));
			c.add(Restrictions.eq("loginname", user.getLoginname()));
			
			c.add(Restrictions.or(Restrictions.eq("flag", ProposalFlagType.EXCUTED.getCode()),
			Restrictions.eq("flag", ProposalFlagType.AUDITED.getCode())));
			
			c.add(Restrictions.eq("type", ProposalType.SELFXIMAPT.getCode()));
			c.setProjection(Projections.property("pno"));
			List list = this.getProposalService().findByCriteria(c);
			if(list!=null && !list.isEmpty()){
				DetachedCriteria x=DetachedCriteria.forClass(Xima.class);
				x.add(Restrictions.in("pno", list.toArray()));
				x.setProjection(Projections.sum("firstCash"));
				List sumx = this.getProposalService().findByCriteria(x);
				Double d = 0.0;
				Double dremain =0.0;
				if(sumx!=null && !sumx.isEmpty() && null!=sumx.get(0) ){
					if((Double)sumx.get(0)*newrate<28888){//第一笔自助反水小于28888后,下一笔系统洗码不管有无都会执行
						dremain = 28888-(Double)sumx.get(0)*newrate;//剩下可反水额度
						d = ximaObject.getValidBetAmount()-(Double)sumx.get(0);
						if(d<0){
							d=0.0;
						}
					}
					ximaObject.setXimaAmouont(d*newrate>dremain?dremain:d*newrate);
				}
			}*/
			/***********************************/
			
			String remark = "PT系统洗码";
			String pno = this.getSeqDao().generateProposalPno(ProposalType.XIMA);
			log.info("正在处理提案号："+pno+",反水金额："+Math.round(ximaObject.getXimaAmouont()*100.00)/100.00+"...");
			Xima xima = new Xima(pno, user.getRole(), user.getLoginname(), "网银支付", pt.getBetCredit(), ximaObject.getXimaAmouont(), DateUtil.convertToTimestamp(starttime), DateUtil.convertToTimestamp(endtime), ximaObject.getRate(), remark);
			Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.XIMA.getCode(), user.getLevel(), user.getLoginname(), ximaObject.getXimaAmouont(),user.getAgent(), ProposalFlagType.AUDITED.getCode(),
				Constants.FROM_BACK, remark, Constants.GENERATE_AUTO);
			this.getTaskDao().generateTasks(pno, "system");
			this.getProposalService().save(xima);
			this.getProposalService().save(proposal);
			AgProfit agProfit=new AgProfit(pno, "system", DateUtil.now(), ProposalType.XIMA.getCode(), user.getLevel(), user.getLoginname(), pt.getAmount(), user.getAgent(), ProposalFlagType.EXCUTED.getCode(), Constants.FROM_BACK, "用户PT平台输赢值");
			agProfit.setPlatform("pt");
			agProfit.setBettotal(pt.getBetCredit());
			this.getProposalService().save(agProfit);
		}
		return null;
	}
    
    
    /**
	 * 
	* @methods cutSelfXima 
	* @description <p>方法的详细说明</p> 
	* @author erick
	* @date 2014年12月16日 下午5:31:01
	* @param ximaVo  洗码实体
	* @param starttime
	* @param endtime
	* @param type 自助洗码类型
	* @param newrate 最新反水比例
	* @param rebate 每天最多反水金额
	* @return 参数说明
	* @return XimaVO 返回结果的说明
	 */
	public XimaVO cutSelfXima(XimaVO ximaVo , Date  starttime, Date  endtime, ProposalType type , Double newrate , Double rebate){
		//进行自助反水缩减   昨天12点到今天12点之间的自助反水(pt和keno2是昨天一天的自助反水)
		
		DetachedCriteria c=DetachedCriteria.forClass(Proposal.class);
		c.add(Restrictions.gt("createTime", starttime));
		c.add(Restrictions.le("createTime", endtime));
		c.add(Restrictions.eq("loginname", ximaVo.getLoginname()));
		
		c.add(Restrictions.or(Restrictions.eq("flag", ProposalFlagType.EXCUTED.getCode()),
		Restrictions.eq("flag", ProposalFlagType.AUDITED.getCode())));
		
		c.add(Restrictions.eq("type", type.getCode()));
		c.setProjection(Projections.property("pno"));
		List list = this.getProposalService().findByCriteria(c);
		if(list!=null && !list.isEmpty()){
			DetachedCriteria x=DetachedCriteria.forClass(Xima.class);
			x.add(Restrictions.in("pno", list.toArray()));
			x.setProjection(Projections.sum("firstCash"));
			List sumx = this.getProposalService().findByCriteria(x);
			Double d = 0.0;
			Double dremain =0.0;
			if(sumx!=null && !sumx.isEmpty() && null!=sumx.get(0)){
				if((Double)sumx.get(0)*newrate< rebate ){//第一笔自助反水小于28888后,下一笔系统洗码不管有无都会执行
					dremain = rebate - (Double)sumx.get(0)*newrate;//剩下可反水额度
					d = ximaVo.getValidBetAmount()-(Double)sumx.get(0);
					if(d<0){
						d=0.0;
					}
					ximaVo.setValidBetAmount(d);
				}
				ximaVo.setXimaAmouont(d*newrate>dremain?dremain:d*newrate);
			}
		}
		return ximaVo ;
	}
	
	@Override
	@SuppressWarnings("unused")
	public String addPhone(File file) throws Exception {
	 	List<String> vList = this.getSystemConfigList("type222");
		List<Customer> list=new ArrayList<Customer> ();
		final String insertSql = "INSERT INTO other_customer (name, phone, email,isreg,isdeposit, phonestatus, userstatus,createTime,qq,type) VALUES ";
		int i=1;
		StringBuilder bulder=null;
		final int batchSize = 1000;
		String msg=null;
		stream = new FileInputStream(file.toString());
	    CsvReader csvReader=new CsvReader(stream, Charset.forName("gbk"));
		if (csvReader == null) {
			log.info("打开文件失败");
			return "打开文件失败";
		}
		try {	
			int total=0; //逐条读取记录，直至读完 
			int count=0;
			String sconfigmValue="";
			csvReader.readHeaders();
			while (csvReader.readRecord()) {
				 total++;
				 String[] csvStr = csvReader.getValues();
				 if(csvStr.length<4){
					 continue;
				 }
				
				 if(!isPhoneNumber(csvStr[1].trim())&&!checkEmail(csvStr[2].trim())){
						continue;
				 }
				 if(count>vList.size()-1){
					 count=0;
					 sconfigmValue=vList.get(count);
					 count++;
				 }else{
					 sconfigmValue=vList.get(count);
					 count++;
				 }
				 
				Customer customer=new Customer();
				customer.setName(csvStr[0].trim());
				 if(isPhoneNumber(csvStr[1].trim())){
					 customer.setPhone(csvStr[1].trim());
				 }
				 if(checkEmail(csvStr[2].trim())){
					 customer.setEmail(csvStr[2].trim());
				 }
				customer.setQq(csvStr[3].trim());
				customer.setIsdeposit(0);
				customer.setIsreg(0);
				customer.setPhonestatus(0);
				customer.setUserstatus(0);
				customer.setCreateTime(DateUtil.getDate(new Date(),0));
				
				customer.setType(sconfigmValue);
				list.add(customer);
				
				log.info("正在新增处理数量:**********************"+total);
	
		}
		
		for (Customer customer : list) {
			if(null==bulder){
				bulder=new StringBuilder(insertSql);
			}
			bulder.append("(");
			bulder.append("'"+customer.getName()+"'");
			
			bulder.append(",");
			bulder.append("'"+customer.getPhone()+"'");
			bulder.append(",");
			bulder.append("'"+customer.getEmail()+"'");
			bulder.append(",");
			
			bulder.append(customer.getIsreg());
			bulder.append(",");
			
			bulder.append(customer.getIsdeposit());
			bulder.append(",");
			
			bulder.append(customer.getPhonestatus());
			bulder.append(",");
			
			bulder.append(customer.getUserstatus());
			bulder.append(",");
			
			bulder.append("'"+customer.getCreateTime()+"'");
			bulder.append(",");
			bulder.append("'"+customer.getQq()+"'");
			bulder.append(",");
			bulder.append("'"+customer.getType()+"'");
			bulder.append("),");
			
			if(i%batchSize==0){
				bulder.deleteCharAt(bulder.lastIndexOf(","));
				this.getProposalService().excuteSql(bulder.toString(), null);
				System.out.println("已插入："+i+"条数据");
				bulder=null;
			}
			i++;
		}
		bulder.deleteCharAt(bulder.lastIndexOf(","));
		this.getProposalService().excuteSql(bulder.toString(), null);//不足1000条的
		log.info("***********处理完毕 :"+total+"***********");
	
	} catch (Exception e) {
		e.printStackTrace();
		return e.toString();
	}finally{
		stream.close();
		file.delete();
	}
	return msg;

	
	
	
	
	
	}
	
	
	
	@Override
	public void excuteAutoXimaProposal() throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public String addPtCoupon(File file,String loginname) throws Exception {
		Integer batch=1;
		try {
			Customer custom = getProposalService().getCustomer();
			if(custom!=null && custom.getBatch()!=null){
				batch=custom.getBatch()+1;
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			batch=1;
		}
		// TODO Auto-generated method stub
		String msg;
		msg = null;
		stream = new FileInputStream(file.toString());
		wb = Workbook.getWorkbook(stream);
		if (wb == null) {
			log.info("打开文件失败");
			return "打开文件失败";
		}
		sheet = wb.getSheet(0); // 取得工作表
		int rows = sheet.getRows(); // 行数
		try {
			for (int i = 1; i < rows; i++) {
				String email=this.getStringValue(i, 0).trim();
				DetachedCriteria c=DetachedCriteria.forClass(PtCoupon.class);
				c.add(Restrictions.eq("email", email));
				List list = this.getProposalService().findByCriteria(c);
			    if(list!=null && list.size()>0){
					continue;
			    }else{
			    	PtCoupon ptCoupon=new PtCoupon();
			    	ptCoupon.setEmail(email);
			    	ptCoupon.setType("0");  //默认为0 表示未使用
			    	
			    	String str = "P8";
					String sqlCouponId = seqDao.generateYhjID();
					String codeOne = dfh.utils.StringUtil.getRandomString(3);
					String codeTwo = dfh.utils.StringUtil.getRandomString(3);
					String code = ( str + codeOne + sqlCouponId + codeTwo);
					ptCoupon.setCode(code);
					ptCoupon.setOperator(loginname);
					this.getProposalService().save(ptCoupon);
					log.info("正在新增处理***********"+email+"***********");
			    }
			}
		} catch (Exception e) {
			e.printStackTrace();
			return e.toString();
		}finally{
			this.closeFile();
			file.delete();
		}
		return msg;
	}
	
	
	/**
	 * @param mobiles
	 * @return
	 */
	public static boolean isMobileNO(String mobiles) {
		Pattern p = Pattern.compile("^((13[0-9])|(15[0-9])|(18[0-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	@Override
	public void updateXimaStatus() throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	public void closeFile() {
		try {
			wb.close();
			// wb_writeable.close();
			stream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("文件关闭------------>error");
		}
	}
	
	private boolean isNullCurRow(int row) {
		int cols = sheet.getColumns();// 列数
		for (int i = 0; i < cols; i++) {
			String cellValue = this.getStringValue(row, i); // 当前单元格的值
			if (cellValue != null && !"".equals(cellValue)) {
				return true;
			}
		}
		return false;
	}
	
	public String getStringValue(int rows, int cols) {
		Cell c = sheet.getCell(cols, rows);
		
		String s = c.getContents();
		if (c.getType() == CellType.LABEL) {
			LabelCell labelc00 = (LabelCell) c;
			s = labelc00.getString();
		}
		
		return s;
	}
	
	public double getNumberValue(int rows, int cols) {
		Cell c = sheet.getCell(cols, rows);
		
		NumberCell nc = (NumberCell) c;
		double s = nc.getValue();
		if (c.getType() == CellType.NUMBER) {
			NumberCell labelc00 = (NumberCell) c;
			s = labelc00.getValue();
		}
		// System.out.println(s);
		return s;
	}
	
	public ProposalDao getProposalDao() {
		return proposalDao;
	}

	public void setProposalDao(ProposalDao proposalDao) {
		this.proposalDao = proposalDao;
	}

	public static void main(String[] args) {
		String string="QY01_test1";
		//System.out.println(string.substring(6,string.length()));
//		System.out.println(Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
//		Calendar calendar = Calendar.getInstance();
//		calendar.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);//得到当前日期所在周的第一天
//		calendar.set(Calendar.HOUR_OF_DAY, 9);
//		System.out.println(calendar.getTime());
		double d = 0.008;
		System.out.println(d);
	}

	@Override
	public String autoAddCommissions(File file) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void excuteCommissions() throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String addAgTry(File file) throws Exception {
		String msg=null;
		stream = new FileInputStream(file.toString());
		wb = Workbook.getWorkbook(stream);
		if (wb == null) {
			log.info("打开文件失败");
			return "打开文件失败";
		}
		sheet = wb.getSheet(0); // 取得工作表
		
		int rows = sheet.getRows(); // 行数
		int cols = sheet.getColumns();// 列数
		
		
		try {
			for (int i = 1; i < rows; i++) {
					String string=this.getStringValue(i, 0).trim();
					if(string.startsWith("et_")){
						String loginname=string;
						AgTryProfit agtryprofit=new AgTryProfit(DateUtil.now(),loginname, this.getNumberValue(i, 9), "用户agtry平台输赢值","agtry",this.getNumberValue(i, 3),Integer.parseInt(this.getStringValue(i, 1)));
						this.getProposalService().save(agtryprofit);
				 }
			}
		} catch (Exception e) {
			e.printStackTrace();
			
		}finally{
			this.closeFile();
			file.delete();
		}
		
		return msg;
	}

	public SeqDao getSeqDao() {
		return seqDao;
	}

	public void setSeqDao(SeqDao seqDao) {
		this.seqDao = seqDao;
	}

	@Override
	public String addLiveGameCommissions(String executetime) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String addAgentPhone(File file) throws Exception {
		String msg;
		msg = null;
		stream = new FileInputStream(file.toString());
		wb = Workbook.getWorkbook(stream);
		if (wb == null) {
			log.info("打开文件失败");
			return "打开文件失败";
		}
		sheet = wb.getSheet(0); // 取得工作表
		int rows = sheet.getRows(); // 行数
		int cols = sheet.getColumns();// 列数
		int count=0;
		try {
			String types="";
			String [] typesArg={};
			DetachedCriteria dc = DetachedCriteria.forClass(SystemConfig.class);
				dc = dc.add(Restrictions.eq("typeNo", "type102"));
				dc = dc.add(Restrictions.eq("flag", "否"));
			List<SystemConfig> configs = this.getProposalService().findByCriteria(dc);
			if(null!=configs&&configs.size()>0){
				types=configs.get(0).getValue();
				typesArg = types.split(";");
			}else{
				return "请配置代理分配码,以英文下分号分开";
			}
			
			for (int i = 1; i < rows; i++) {
				String str="";
				String name=this.getStringValue(i, 0).trim();
				String phone=this.getStringValue(i, 1).trim();
				String email=this.getStringValue(i, 2).trim();
		    	str = typesArg[count] ;
		    	count++;
		    	if(count==typesArg.length){
		    		count = 0;
		    	}
		    	
		    	AgentCustomer customer=new AgentCustomer();
				customer.setName(name);
				customer.setPhone(phone);
				customer.setEmail(email);
				customer.setIsdeposit(0);
				customer.setIsreg(0);
				customer.setPhonestatus(0);
				customer.setUserstatus(0);
				customer.setCreateTime(new Date());
				customer.setType(str);
				this.getProposalService().save(customer);
				log.info("正在新增处理***********"+phone+"***********");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return e.toString();
		}finally{
			this.closeFile();
			file.delete();
		}
		return msg;
	}
	
	/**
	 * 捕鱼洗码
	 * @param file
	 * @return
	 * */
	@Override
	public String systemXimaForGf(File file){
		String msg = null;
		try {
			stream = new FileInputStream(file.toString());
			wb = Workbook.getWorkbook(stream);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		if (wb == null) {
			log.info("打开文件失败");
			return "打开文件失败";
		}
		sheet = wb.getSheet(0); // 取得工作表
		int rows = sheet.getRows(); // 行数
		List<AginFishVo> gfs = new ArrayList<AginFishVo>() ;
		for (int i = 1; i < rows; i++) {
			
			String name=this.getStringValue(i, 0).trim();
			if(!name.startsWith("ki_")){
				continue;
			}
			name = name.substring(3);
			AginFishVo vo = new AginFishVo();
			Users user = (Users) this.getUserDao().get(Users.class,name);
			if (user==null) {
				log.info("用户："+name+"不存在");
				continue;
			}
			
			vo.setLoginname(name);
			Double bet;
			Double paicai;//派彩
			Double jackpot;//jackpot交收
			try {
				bet = this.getNumberValue(i, 4);
			} catch (Exception e) {
				bet = Double.parseDouble(this.getStringValue(i, 4).trim());
			}
			try {
				paicai = this.getNumberValue(i, 5);//派彩		
			} catch (Exception e1) {
				paicai = Double.parseDouble(this.getStringValue(i, 5).trim());//派彩
			}
			try {
				jackpot = this.getNumberValue(i, 6);//jackpot交收
			} catch (Exception e2) {
				jackpot = Double.parseDouble(this.getStringValue(i, 6).trim());//jackpot交收
			}
//			Double bet = Double.parseDouble(this.getStringValue(i, 4).trim());
//			Double paicai = Double.parseDouble(this.getStringValue(i, 5).trim());//派彩
//			Double jackpot = Double.parseDouble(this.getStringValue(i, 6).trim());//jackpot交收
			Double profit = Arith.mul(-1, Arith.sub(paicai, jackpot));
			vo.setBet(bet);
			vo.setProfit(profit);
			vo.setUser(user);
			gfs.add(vo) ;
		}
		
		Date starttime = DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), -(1*24-0));
		Date endtime  =DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), 0);
		//减自助反水的时间与agin一样。
//		Date starttime=DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), -(1*24-12));
//		Date endtime=DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), 12);
		
		try {
			for (AginFishVo gfVo : gfs) {
				double newrate = SlotUtil.getSlotRate(gfVo.getUser());
				Double keno2RebateLimit = SlotUtil.getRebateLimit(gfVo.getUser());
				
				XimaVO ximaObject = new XimaVO(gfVo.getBet(),gfVo.getLoginname(),newrate,keno2RebateLimit);
				/***********************************/
				//减自助反水的时间与agin一样。12点-12点
				//ximaObject = cutSelfXima(ximaObject, starttime, endtime, ProposalType.KGSELFXIMA, newrate, keno2RebateLimit) ;
				/***********************************/
				String remark = "aginfish系统洗码";
				String pno = this.getSeqDao().generateProposalPno(ProposalType.XIMA);
				log.info("正在处理提案号："+pno+",反水金额："+Math.round(ximaObject.getXimaAmouont()*100.00)/100.00+"...");
				Xima xima = new Xima(pno, gfVo.getUser().getRole(), gfVo.getUser().getLoginname(), "网银支付", ximaObject.getValidBetAmount(), ximaObject.getXimaAmouont(), DateUtil.convertToTimestamp(starttime), DateUtil.convertToTimestamp(endtime), ximaObject.getRate(), remark);
				Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.XIMA.getCode(), gfVo.getUser().getLevel(), gfVo.getLoginname(), ximaObject.getXimaAmouont(),gfVo.getUser().getAgent(), ProposalFlagType.AUDITED.getCode(),
						Constants.FROM_BACK, remark, Constants.GENERATE_AUTO);
				this.getTaskDao().generateTasks(pno, "system");
//				this.getProposalService().save(xima);
//				this.getProposalService().save(proposal);
				AgProfit agProfit=new AgProfit(pno, "system", DateUtil.now(), ProposalType.XIMA.getCode(), gfVo.getUser().getLevel(), gfVo.getLoginname(), gfVo.getProfit(), gfVo.getUser().getAgent(), ProposalFlagType.EXCUTED.getCode(), Constants.FROM_BACK, "用户aginfish平台输赢值");
				agProfit.setPlatform("aginfish");
				agProfit.setBettotal(gfVo.getBet());
				this.getProposalService().save(agProfit);
			}
		} catch (Exception e) {
			
			log.error("aginfish洗码错误：", e);
			msg = e.toString();
		} finally {
			this.closeFile();
			file.delete();
		}
		
		return msg ;
	}
	
	
	/**
	 * Ag老虎机洗码
	 * @param file
	 * @return
	 * */
	@Override
	public String systemXimaForAgSlot(File file){
		String msg = null;
		try {
			stream = new FileInputStream(file.toString());
			wb = Workbook.getWorkbook(stream);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		if (wb == null) {
			log.info("打开文件失败");
			return "打开文件失败";
		}
		sheet = wb.getSheet(0); // 取得工作表
		int rows = sheet.getRows(); // 行数
		List<AginFishVo> gfs = new ArrayList<AginFishVo>() ;
		for (int i = 1; i < rows; i++) {
			
			String name=this.getStringValue(i, 0).trim();
			if(!name.startsWith("ki_")){
				continue;
			}
			name = name.substring(3);
			AginFishVo vo = new AginFishVo();
			Users user = (Users) this.getUserDao().get(Users.class,name);
			if (user==null) {
				log.info("用户："+name+"不存在");
				continue;
			}
			
			vo.setLoginname(name);
			Double bet;
			Double paicai;//派彩
//			Double jackpot;//jackpot交收
			try {
				bet = this.getNumberValue(i, 4);
			} catch (Exception e) {
				bet = Double.parseDouble(this.getStringValue(i, 4).trim());
			}
			try {
				paicai = this.getNumberValue(i, 6);//派彩		
			} catch (Exception e1) {
				paicai = Double.parseDouble(this.getStringValue(i, 6).trim());//派彩
			}

//			Double bet = Double.parseDouble(this.getStringValue(i, 4).trim());
//			Double paicai = Double.parseDouble(this.getStringValue(i, 5).trim());//派彩
//			Double jackpot = Double.parseDouble(this.getStringValue(i, 6).trim());//jackpot交收
			Double profit = paicai;
			vo.setBet(bet);
			vo.setProfit(profit);
			vo.setUser(user);
			gfs.add(vo) ;
		}
		
		Date starttime = DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), -(1*24-0));
		Date endtime  = DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), 0);
		//减自助反水的时间与agin一样。12点-12点
//		Date starttime=DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), -(1*24-12));
//		Date endtime=DateUtil.date2Timestampyyyy_MM_dd_HH(new Date(), 12);
		
		try {
			for (AginFishVo gfVo : gfs) {
				double newrate = SlotUtil.getSlotRate(gfVo.getUser());
				
				//白金以及白金以上单日返水无限制
				Double keno2RebateLimit = SlotUtil.getRebateLimit(gfVo.getUser());
//				if(gfVo.getUser().getLevel() >= VipLevel.BAIJIN.getCode().intValue()){
//					keno2RebateLimit = 9999999999d;
//				}else{
//					keno2RebateLimit = gfVo.getUser().getPtrebate() == null? 28888 : gfVo.getUser().getPtrebate();
//				}
				
				XimaVO ximaObject = new XimaVO(gfVo.getBet(),gfVo.getLoginname(),newrate,keno2RebateLimit);
				/***********************************/
				//减自助反水的时间与agin一样。12点-12点
				//ximaObject = cutSelfXima(ximaObject, starttime, endtime, ProposalType.KGSELFXIMA, newrate, keno2RebateLimit) ;
				/***********************************/
				String remark = "aginslot系统洗码";
				String pno = this.getSeqDao().generateProposalPno(ProposalType.XIMA);
				log.info("正在处理提案号："+pno+",反水金额："+Math.round(ximaObject.getXimaAmouont()*100.00)/100.00+"...");
				Xima xima = new Xima(pno, gfVo.getUser().getRole(), gfVo.getUser().getLoginname(), "网银支付", ximaObject.getValidBetAmount(), ximaObject.getXimaAmouont(), DateUtil.convertToTimestamp(starttime), DateUtil.convertToTimestamp(endtime), ximaObject.getRate(), remark);
				Proposal proposal = new Proposal(pno, "system", DateUtil.now(), ProposalType.XIMA.getCode(), gfVo.getUser().getLevel(), gfVo.getLoginname(), ximaObject.getXimaAmouont(),gfVo.getUser().getAgent(), ProposalFlagType.AUDITED.getCode(),
						Constants.FROM_BACK, remark, Constants.GENERATE_AUTO);
				this.getTaskDao().generateTasks(pno, "system");
				this.getProposalService().save(xima);
				this.getProposalService().save(proposal);
				AgProfit agProfit=new AgProfit(pno, "system", DateUtil.now(), ProposalType.XIMA.getCode(), gfVo.getUser().getLevel(), gfVo.getLoginname(),-1*gfVo.getProfit(), gfVo.getUser().getAgent(), ProposalFlagType.EXCUTED.getCode(), Constants.FROM_BACK, "用户aginslot平台输赢值");
				agProfit.setPlatform("aginslot");
				agProfit.setBettotal(gfVo.getBet());
				this.getProposalService().save(agProfit);
			}
		} catch (Exception e) {
			log.error("aginslot洗码错误：", e);
			msg = e.toString();
		} finally {
			this.closeFile();
			file.delete();
		}
		
		return msg ;
	}
	
	@Override
	public String addMail(File file) throws Exception {
		Integer batch=1;
		try {
			Customer custom = getProposalService().getCustomer();
			if(custom!=null && custom.getBatch()!=null){
				batch=custom.getBatch()+1;
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			batch=1;
		}
		// TODO Auto-generated method stub
		String msg;
		msg = null;
		stream = new FileInputStream(file.toString());
		WorkbookSettings workbooksetting = new WorkbookSettings();
		workbooksetting .setCellValidationDisabled(true);
		wb = Workbook.getWorkbook(stream,workbooksetting);
		if (wb == null) {
			log.info("打开文件失败");
			return "打开文件失败";
		}
		sheet = wb.getSheet(0); // 取得工作表
		int rows = sheet.getRows(); // 行数
		int cols = sheet.getColumns();// 列数
		int count=1;
		try {
			
	Pattern pattern = Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
		DateUtil date=new DateUtil();
		for (int i = 1; i < rows; i++) {
			String mail=this.getStringValue(i, 0).trim();
			  RecordMail recordMail = new RecordMail();
				Matcher matcher = pattern.matcher(mail);
				if (true == matcher.matches()) {
					recordMail.setStatus(0);
				} else {
					recordMail.setStatus(1);
				}

				recordMail.setEmail(mail);
				recordMail.setCreatetime(date.now());
				recordMail.setRemark("");
				this.getProposalService().save(recordMail);
				
			}
		
		} catch (Exception e) {
			e.printStackTrace();
			return e.toString();
		}finally{
			this.closeFile();
			file.delete();
		}
		return msg;
	}

	@Override
	public void convertAndSaveCommissions(List<PtCommissions> commLists,
			String platform, List<Proposal> proposals, List<Object> newPtLists) {
		// TODO Auto-generated method stub
		
	}
 
	@SuppressWarnings("unchecked")
	public List<String> getSystemConfigList(String value) throws Exception{
		String sql=" select value  from  systemconfig where typeNo=:typeNo ";
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("typeNo", value);
		List<String> list = proposalDao.getListBySql(sql, params);
		List<String> asList = Arrays.asList(list.get(0).split(";"));
		return asList;
	}
	/**
	 * 判断电话号码（手机和一般电话），正则表达式 
	 * @param input
	 * @return
	 */
	@SuppressWarnings("static-access")
	private boolean isPhoneNumber(String input){  
	    String regex="^((13[0-9])|(14[0-9])|(15[0-9])|(17[0-9])|(18[0-9]))\\d{8}$";  
	    Pattern p = Pattern.compile(regex);  
	    return p.matches(regex, input);  
	}
	 /**
     * 验证邮箱
     * @param email
     * @return
     */
    @SuppressWarnings("static-access")
	public  boolean checkEmail(String email){
        String regex = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern p = Pattern.compile(regex);
        return p.matches(regex,email);
    }
    
    @Override
	public List<XimaDataVo> excelToPNGVo(File file) {
		try {
			stream = new FileInputStream(file.toString());
			wb = Workbook.getWorkbook(stream);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		if (wb == null) {
			log.info("打开文件失败");
			return null;
		}
		sheet = wb.getSheet(0); // 取得工作表
		int rows = sheet.getRows(); // 行数
		List<XimaDataVo> gfs = new ArrayList<XimaDataVo>() ;
		try {
			for (int i = 15; i < rows; i++) {
				
				String name=this.getStringValue(i, 0).trim();
				if(!name.startsWith("k_")){
					continue;
				}
				name = name.substring(2);
				XimaDataVo vo = new XimaDataVo();
				Users user = (Users) this.getUserDao().get(Users.class,name);
				if (user==null) {
					log.info("用户："+name+"不存在");
					continue;
				}
				
				Double bet;
				Double net;

				try {
					bet = this.getNumberValue(i, 5);
				} catch (Exception e) {
					bet = Double.parseDouble(this.getStringValue(i,5).trim());
				}
				try {
					net = this.getNumberValue(i, 9);
				} catch (Exception e1) {
					net = Double.parseDouble(this.getStringValue(i, 9).trim());
				}
				vo.setLoginname(name);
				vo.setTotal_bet(Arith.round(bet, 2));
				vo.setTotal_win(Arith.round(net, 2));
				vo.setUser(user);
				gfs.add(vo) ;
			}
		} catch (Exception e) {
			log.error("PNG EXCEL转换错误：", e);
			return null;
		} finally {
			this.closeFile();
			file.delete();
		}
		
		return gfs;
	}
    
    @Override
   	public List<Bean4Xima> excelToPtSkyVo(File file) {
   		try {
   			stream = new FileInputStream(file.toString());
   			wb = Workbook.getWorkbook(stream);
   		} catch (Exception e) {
   			e.printStackTrace();
   		} 
   		if (wb == null) {
   			log.info("打开文件失败");
   			return null;
   		}
   		sheet = wb.getSheet(0); // 取得工作表
   		int rows = sheet.getRows(); // 行数
   		List<Bean4Xima> gfs = new ArrayList<Bean4Xima>() ;
   		try {
   			for (int i = 1; i < rows; i++) {   				
   				String name=this.getStringValue(i, 0).trim();
                System.out.println(name);
   				name = name.substring(1);
   				Bean4Xima vo = new Bean4Xima();
   				Users user = (Users) this.getUserDao().get(Users.class,name);
   				if (user==null) {
   					log.info("用户："+name+"不存在");
   					continue;
   				}
   				
   				Double bet;
   				Double win;
				Double jackpot;


   				try {
   					bet = this.getNumberValue(i, 3);
   				} catch (Exception e) {
   					bet = Double.parseDouble(this.getStringValue(i,3).trim());
   				}

   				try {
   					win = this.getNumberValue(i, 4);
   				} catch (Exception e1) {
   					win = Double.parseDouble(this.getStringValue(i, 4).trim());
   				}
				try {
					jackpot = this.getNumberValue(i, 5);
				} catch (Exception e1) {
					jackpot = Double.parseDouble(this.getStringValue(i, 5).trim());
				}

				win = Arith.add(win, jackpot);
   				vo.setUserName(name);
   				vo.setBetAmount(Arith.round(bet, 2));
   				vo.setProfit(Arith.round(win, 2));
   				gfs.add(vo) ;
   			}
   		} catch (Exception e) {
   			log.error("PtSky EXCEL转换错误：", e);
   			return null;
   		} finally {
   			this.closeFile();
   			file.delete();
   		}
   		
   		return gfs;
   	}
    @Override
	public List<Bean4Xima> excelToDTFishVo(File file) {
   		try {
   			stream = new FileInputStream(file.toString());
   			wb = Workbook.getWorkbook(stream);
   		} catch (Exception e) {
   			e.printStackTrace();
   		} 
   		if (wb == null) {
   			log.info("打开文件失败");
   			return null;
   		}
   		sheet = wb.getSheet(0); // 取得工作表
   		int rows = sheet.getRows(); // 行数
   		List<Bean4Xima> gfs = new ArrayList<Bean4Xima>() ;
   		try {
   			for (int i = 1; i < rows; i++) {   				
   				String name=this.getStringValue(i, 3).trim();
   				name = name.substring(1);
   				Bean4Xima vo = new Bean4Xima();
   				Users user = (Users) this.getUserDao().get(Users.class,name);
   				if (user==null) {
   					log.info("用户："+name+"不存在");
   					continue;
   				}
   				
   				Double bet;
   				Double win;
   				Double jackpot;

   				try {
   					bet = this.getNumberValue(i, 5);
   				} catch (Exception e) {
   					bet = Double.parseDouble(this.getStringValue(i,5).trim());
   				}

   				try {
   					win = this.getNumberValue(i, 6);
   				} catch (Exception e1) {
   					win = Double.parseDouble(this.getStringValue(i, 6).trim());
   				}

   				vo.setUserName(user.getLoginname());
   				vo.setBetAmount(Arith.round(bet, 2));
   				vo.setProfit(Arith.round(win, 2));
   				gfs.add(vo) ;
   			}
   		} catch (Exception e) {
   			log.error("HYG EXCEL转换错误：", e);
   			return null;
   		} finally {
   			this.closeFile();
   			file.delete();
   		}
   		
   		return gfs;
   	}  
    
	//反水上限
	private double getRebateLimit(Users user) {
		double rebateLimit = 8888.0;
		if(user.getLevel() == VipLevel.TIANJIANG.getCode().intValue()){
			rebateLimit = 18888;
		}else if(user.getLevel() == VipLevel.TIANWANG.getCode().intValue()){
			rebateLimit = 18888;
		}else if(user.getLevel() == VipLevel.XINGJUN.getCode().intValue()){
			rebateLimit = 28888;
		}else if(user.getLevel() == VipLevel.ZHENJUN.getCode().intValue()){
			rebateLimit = 28888;
		}else if(user.getLevel() == VipLevel.XIANJUN.getCode().intValue()){
			rebateLimit = 28888; 
		}else if(user.getLevel() == VipLevel.DIJUN.getCode().intValue()){
			rebateLimit = 28888; 
		}else if(user.getLevel() == VipLevel.TIANZUN.getCode().intValue()){
			rebateLimit = 28888; 
		}else if(user.getLevel() == VipLevel.TIANDI.getCode().intValue()){
			rebateLimit = 88888; 
		}
		return rebateLimit;
	}
	//老虎机反水比例
	private double getSlotRate(Users user) {
		double newrate = 0.005;
		if(user.getLevel() == VipLevel.TIANJIANG.getCode().intValue()){
			newrate = 0.006;
		}else if(user.getLevel() == VipLevel.TIANWANG.getCode().intValue()){
			newrate = 0.007;
		}else if(user.getLevel() == VipLevel.XINGJUN.getCode().intValue()){
			newrate = 0.008;
		}else if(user.getLevel() == VipLevel.ZHENJUN.getCode().intValue()){
			newrate = 0.009;
		}else if(user.getLevel() == VipLevel.XIANJUN.getCode().intValue()){
			newrate = 0.010; 
		}else if(user.getLevel() == VipLevel.DIJUN.getCode().intValue()){
			newrate = 0.011; 
		}else if(user.getLevel() == VipLevel.TIANZUN.getCode().intValue()){
			newrate = 0.012; 
		}else if(user.getLevel() == VipLevel.TIANDI.getCode().intValue()){
			newrate = 0.015; 
		}
		return newrate;
	}
	
	//真人反水比例
	private double getVedioRate(Users user) {
		double newrate = 0.005;
		if(user.getLevel() == VipLevel.TIANJIANG.getCode().intValue()){
			newrate = 0.006;
		}else if(user.getLevel() == VipLevel.TIANWANG.getCode().intValue()){
			newrate = 0.007;
		}else if(user.getLevel() == VipLevel.XINGJUN.getCode().intValue()){
			newrate = 0.008;
		}else if(user.getLevel() == VipLevel.ZHENJUN.getCode().intValue()){
			newrate = 0.009;
		}else if(user.getLevel() == VipLevel.XIANJUN.getCode().intValue()){
			newrate = 0.010; 
		}else if(user.getLevel() == VipLevel.DIJUN.getCode().intValue()){
			newrate = 0.011; 
		}else if(user.getLevel() == VipLevel.TIANZUN.getCode().intValue()){
			newrate = 0.012; 
		}else if(user.getLevel() == VipLevel.TIANDI.getCode().intValue()){
			newrate = 0.015; 
		}
		return newrate;
	}
	//体育反水比例
	private double getSportsRate(Users user) {
		double newrate = 0.005;
		if(user.getLevel() == VipLevel.TIANJIANG.getCode().intValue()){
			newrate = 0.006;
		}else if(user.getLevel() == VipLevel.TIANWANG.getCode().intValue()){
			newrate = 0.007;
		}else if(user.getLevel() == VipLevel.XINGJUN.getCode().intValue()){
			newrate = 0.008;
		}else if(user.getLevel() == VipLevel.ZHENJUN.getCode().intValue()){
			newrate = 0.009;
		}else if(user.getLevel() == VipLevel.XIANJUN.getCode().intValue()){
			newrate = 0.010; 
		}else if(user.getLevel() == VipLevel.DIJUN.getCode().intValue()){
			newrate = 0.011; 
		}else if(user.getLevel() == VipLevel.TIANZUN.getCode().intValue()){
			newrate = 0.012; 
		}else if(user.getLevel() == VipLevel.TIANDI.getCode().intValue()){
			newrate = 0.015; 
		}
		return newrate;
	}
    
}
