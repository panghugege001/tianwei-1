package dfh.service.implementations;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import dfh.model.*;
import dfh.utils.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.google.common.base.Stopwatch;

import dfh.exception.GenericDfhRuntimeException;
import dfh.icbc.getdata.dao.IGetdataDao;
import dfh.model.enums.CreditChangeType;
import dfh.security.SpecialEnvironmentStringPBEConfig;
import dfh.service.interfaces.IGetdateService;
import dfh.service.interfaces.NotifyService;
import dfh.spider.PTBetVO;

public class GetdataServiceImpl implements IGetdateService{

	private static Logger log = Logger.getLogger(GetdataServiceImpl.class);
	private IGetdataDao getDataDao;
	private NotifyService notifyService;

	@Override
	public void updateoperator(String bankStr) {
		Double maxAmount = 500d;
		if("ICBC".equals(bankStr)){
			IcbcTransfers icbcTransfers = getDataDao.getIcbcDataBySql();
			System.out.println("=============================" + icbcTransfers);
			if(icbcTransfers!=null){
				log.info("本次ICBC处理的记录数是：1，处理时间是："+new Date());
				String notes = icbcTransfers.getNotes();//取得附言信息
				double amount = icbcTransfers.getAmount();//取得转账金额
				String acceptName = icbcTransfers.getAcceptName();
				Double fee=icbcTransfers.getFee();//取得转账手续费
				if(notes != null && !"".equals(notes)){//如果附言不为空
				 	notes  = StringUtil.trim(notes);
				 	DetachedCriteria c = DetachedCriteria.forClass(Users.class);
					c.add(Restrictions.eq("loginname",notes));
					List<Users> users =getDataDao.findByCriteria(c);
					//System.out.println(c);
					Users user = null;
					//System.out.println(users.size());
					if(users != null && users.size()==1){
						user = users.get(0);
						if (user != null) {
							log.info(user.getLoginname()+" add Cashin proposal,the amount is "+amount);
							IcbcTransfers icbcTransfer = (IcbcTransfers)getDataDao.get(IcbcTransfers.class, icbcTransfers.getTransfeId(), LockMode.UPGRADE);
							if(icbcTransfer.getStatus()==0){
								icbcTransfer.setStatus(1);
								icbcTransfer.setNotes(notes+"  "+user.getLoginname());
								icbcTransfer.setTimecha(DateUtil.getTimecha(new Timestamp(icbcTransfer.getPayDate().getTime()),new Timestamp(icbcTransfer.getDate().getTime())));
								icbcTransfer.setOvertime(DateUtil.getOvertime(new Timestamp(icbcTransfer.getPayDate().getTime()),new Timestamp(icbcTransfer.getDate().getTime())));
								getDataDao.update(icbcTransfer);
								/**
								 * update cashin
								 */
								Proposal proposal=new Proposal();
								try {
//									String pno = getDataDao.generateProposalPno("502");
									String pno = getDataDao.generateProposalIcbcPno("502") ;
									if(fee ==null)
										fee=0.0;
									
									Cashin cashin = new Cashin(pno,user.getRole(), user.getLoginname(),StringUtil.trim(user.getAccountName()),amount,"XXXXXXXXXXXXXXXX","工商银行","");
									cashin.setCashintime(DateUtil.now()); 
									
									/*************存款红利0.005*******************/
									if(user.getLevel()>100){
										/**
										 * 限制手续费最高1000元
										 */
										Calendar cday = Calendar.getInstance();
										cday.set(Calendar.HOUR_OF_DAY, 0);
										cday.set(Calendar.MINUTE, 0);
										cday.set(Calendar.SECOND, 0);
										Date startday = cday.getTime();
										cday.set(Calendar.HOUR_OF_DAY, 23);
										cday.set(Calendar.MINUTE, 59);
										cday.set(Calendar.SECOND, 59);
										Date endday = cday.getTime();
										DetachedCriteria dc = DetachedCriteria.forClass(Cashin.class);
										dc.add(Restrictions.eq("loginname", user.getLoginname()));
										dc.add(Restrictions.and(Restrictions.ge("cashintime", startday),Restrictions.lt("cashintime", endday)));
										dc.setProjection(Projections.sum("fee"));
										List list = getDataDao.findByCriteria(dc);
										/**
										 * 多笔
										 */
										if(list!=null && !list.isEmpty() && null!=list.get(0)){
											Double d = (Double)list.get(0);
											Double dam = maxAmount-d;//系统剩余可返还的手续费
											if(dam<=0){
												fee = 0.00;
											}else{
												fee = amount*0.005;
												if(fee>dam){
													fee = dam;
												}
											}
										}else{
											/**
											 * 只有一笔
											 */
											fee = amount*0.005;
											if(fee>maxAmount){
												fee = maxAmount;
											}
										}
									}else{
										fee = amount*0.005;
									}
									System.out.println(fee+"＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝");
									/*******************************************/
									cashin.setFee(fee);
									amount=amount+fee;
									proposal.setPno(pno);
									proposal.setProposer("system");
									proposal.setCreateTime(DateUtil.now());
									proposal.setType(502);
									proposal.setQuickly(user.getLevel());
									proposal.setLoginname(user.getLoginname());
									proposal.setAmount(amount);
									proposal.setAgent(user.getAgent());
									proposal.setFlag(2);
									proposal.setWhereisfrom("system");
									proposal.setRemark("网银转账");
									proposal.setBankaccount(acceptName);
									proposal.setSaveway("网银");
									
									getDataDao.save(cashin);
									getDataDao.save(proposal);
									getDataDao.generateTasks(pno, "system");
									getDataDao.changeCreditBySql(user, proposal.getAmount(), "CASHIN", pno, "");
									
									// 设置用户 isCashin字段
									if (user.getIsCashin().intValue() == Constants.FLAG_FALSE.intValue()) {
										getDataDao.updateUserIsCashInSql(user.getLoginname());
									}
									
									//改变我方银行的金额
									getDataDao.changeAmountByNameSql(proposal.getBankaccount(), proposal.getAmount()-fee,pno);
									getDataDao.excuteTask(pno, "system", "");
									getDataDao.insertOperationLog("system", "EXCUTE", "ip:" + "" + ";pno:" + pno);

									log.info("本次ICBC处理的时间是："+new Date());
									
									try {
										// 根据客户定制的服务，决定是否发送邮件和短信
										String service = user.getAddress();
										if (service != null && service.indexOf("9") != -1) {// 发短信
											String smsmsg = notifyService.sendSMSByProposal(proposal);
											log.info("ICBC send SMS:" + smsmsg);
										}
									} catch (Exception e) {
										log.error("ICBC发送短信异常:",e);
									}
								} catch (Exception e) {
									e.printStackTrace();
									throw new GenericDfhRuntimeException(e.getMessage());
								}
							}	
						}else{
							log.info("id为："+notes+"，无此用户");
							icbcTransfers.setStatus(2);
							getDataDao.update(icbcTransfers);
						}
					}else{
						log.info("id为："+notes+"，无此用户");
						icbcTransfers.setStatus(2);
						getDataDao.update(icbcTransfers);
					}
				}else {
					//存款附言里面无内容或者整型溢出
					icbcTransfers.setStatus(2);
					getDataDao.update(icbcTransfers);
				}
			}else{
				log.info("本次ICBC处理的记录数是：0，处理时间是："+new Date());
			}
		}else if("CMB".equals(bankStr)){
			CmbTransfers cmbTransfers = getDataDao.getCmbDataBySql();
			if(cmbTransfers!=null){
				log.info("本次CMB处理的记录数是：1，处理时间是："+new Date());
				String notes = cmbTransfers.getNotes();//取得附言信息
				double amount = cmbTransfers.getAmount();//取得转账金额
				Double fee=0.00;
				if(notes != null && !"".equals(notes)){//如果附言不为空
				 	notes  = StringUtil.trim(notes);
				 	DetachedCriteria c = DetachedCriteria.forClass(Users.class);
					c.add(Restrictions.eq("loginname",notes));
					List<Users> users =getDataDao.findByCriteria(c);
					//System.out.println(c);
					Users user = null;
					//System.out.println(users.size());
					if(users != null && users.size()==1){
						user = users.get(0);
						if (user != null) {
							log.info(user.getLoginname()+" add Cashin proposal,the amount is "+amount);
							CmbTransfers cmbTransfer = (CmbTransfers)getDataDao.get(CmbTransfers.class, cmbTransfers.getTransfeId(), LockMode.UPGRADE);
							if(cmbTransfer.getStatus()==0){
								cmbTransfer.setStatus(1);
								cmbTransfer.setNotes(notes+"  "+user.getLoginname());
								cmbTransfer.setTimecha(DateUtil.getTimecha(new Timestamp(cmbTransfer.getPayDate().getTime()),new Timestamp(cmbTransfer.getDate().getTime())));
								cmbTransfer.setOvertime(DateUtil.getOvertime(new Timestamp(cmbTransfer.getPayDate().getTime()),new Timestamp(cmbTransfer.getDate().getTime())));
								getDataDao.update(cmbTransfer);
								/**
								 * update cashin
								 */
								Proposal proposal=new Proposal();
								try {
									String pno = getDataDao.generateProposalPno("502");
									if("客户转账".equals(cmbTransfer.getJylx())){
										//建行加急转账按0.005收取，最低2元，25元封顶
										fee = amount*0.005;
										if(fee<=2){
											fee=2.00;
										}else if(fee>=25){
											fee=25.00;
										}
									}
									
									Cashin cashin = null;
									if("客户转账".equals(cmbTransfer.getJylx())){
										 cashin = new Cashin(pno,user.getRole(), user.getLoginname(),StringUtil.trim(user.getAccountName()),amount,"XXXXXXXXXXXXXXXX","建设银行","");
									}else{
										 cashin = new Cashin(pno,user.getRole(), user.getLoginname(),StringUtil.trim(user.getAccountName()),amount,"XXXXXXXXXXXXXXXX","招商银行","");
									}
									
									cashin.setCashintime(DateUtil.now()); 
									
									/*************存款红利0.005*******************/
									if(user.getLevel()>100){
										/**
										 * 限制手续费最高1000元
										 */
										Calendar cday = Calendar.getInstance();
										cday.set(Calendar.HOUR_OF_DAY, 0);
										cday.set(Calendar.MINUTE, 0);
										cday.set(Calendar.SECOND, 0);
										Date startday = cday.getTime();
										cday.set(Calendar.HOUR_OF_DAY, 23);
										cday.set(Calendar.MINUTE, 59);
										cday.set(Calendar.SECOND, 59);
										Date endday = cday.getTime();
										DetachedCriteria dc = DetachedCriteria.forClass(Cashin.class);
										dc.add(Restrictions.eq("loginname", user.getLoginname()));
										dc.add(Restrictions.and(Restrictions.ge("cashintime", startday),Restrictions.lt("cashintime", endday)));
										dc.setProjection(Projections.sum("fee"));
										List list = getDataDao.findByCriteria(dc);
										
										
										/**
										 * 多笔
										 */
										if(list!=null && !list.isEmpty() && null!=list.get(0)){
											Double d = (Double)list.get(0);
											
											Double dam = maxAmount-d;//系统剩余可返还的手续费
											if(dam<=0){
												fee = 0.00;
											}else{
												fee = amount*0.005;
												if(fee>dam){
													fee = dam;
												}
											}
										}else{
											/**
											 * 只有一笔
											 */
											fee = amount*0.005;
											if(fee>maxAmount){
												fee = maxAmount;
											}
										}
									}else{
										fee = amount*0.005;
									}
									/*******************************************/
									cashin.setFee(fee);
									amount=amount+fee;
									proposal.setPno(pno);
									proposal.setProposer("system");
									proposal.setCreateTime(DateUtil.now());
									proposal.setType(502);
									proposal.setQuickly(user.getLevel());
									proposal.setLoginname(user.getLoginname());
									proposal.setAmount(amount);
									proposal.setAgent(user.getAgent());
									proposal.setFlag(2);
									proposal.setWhereisfrom("system");
									proposal.setRemark("网银转账");
									proposal.setBankaccount(cmbTransfer.getAcceptName());
									proposal.setSaveway("网银");
									
									getDataDao.save(cashin);
									getDataDao.save(proposal);
									getDataDao.generateTasks(pno, "system");
									getDataDao.changeCreditBySql(user, proposal.getAmount(), "CASHIN", pno, "");
									
									// 设置用户 isCashin字段
									if (user.getIsCashin().intValue() == Constants.FLAG_FALSE.intValue()) {
										getDataDao.updateUserIsCashInSql(user.getLoginname());
									}
									
									//改变我方银行的金额
									getDataDao.changeAmountByNameSql(proposal.getBankaccount(), proposal.getAmount()-fee,pno);
									getDataDao.excuteTask(pno, "system", "");
									getDataDao.insertOperationLog("system", "EXCUTE", "ip:" + "" + ";pno:" + pno);

									log.info("本次CMB处理的时间是："+new Date());
									
									try {
										// 根据客户定制的服务，决定是否发送邮件和短信
										String service = user.getAddress();
										if (service != null && service.indexOf("9") != -1) {// 发短信
											String smsmsg = notifyService.sendSMSByProposal(proposal);
											log.info("icbc send SMS:" + smsmsg);
										}
									} catch (Exception e) {
										log.error("icbc发送短信异常:",e);
									}
								} catch (Exception e) {
									e.printStackTrace();
									throw new GenericDfhRuntimeException(e.getMessage());
								}
							}	
						}else{
							log.info("id为："+notes+"，无此用户");
							cmbTransfers.setStatus(2);
							getDataDao.update(cmbTransfers);
						}
					}else{
						log.info("id为："+notes+"，无此用户");
						cmbTransfers.setStatus(2);
						getDataDao.update(cmbTransfers);
					}
				}else {
					//存款附言里面无内容或者整型溢出
					cmbTransfers.setStatus(2);
					getDataDao.update(cmbTransfers);
				}
			}else{
				log.info("本次CMB处理的记录数是：0，处理时间是："+new Date());
			}
		}else if("ABC".equals(bankStr)){
			AbcTransfers abcTransfers = getDataDao.getAbcDataBySql();
			if(abcTransfers!=null){
				log.info("本次ABC处理的记录数是：1，处理时间是："+new Date());
				String notes = abcTransfers.getJyzy();//取得附言信息
				double amount = abcTransfers.getAmount();//取得转账金额
				Double fee=0.00;
				if(notes != null && !"".equals(notes)){//如果附言不为空
				 	notes  = StringUtil.trim(notes);
				 	DetachedCriteria c = DetachedCriteria.forClass(Users.class);
					c.add(Restrictions.eq("randnum",notes));
					List<Users> users =getDataDao.findByCriteria(c);
					//System.out.println(c);
					Users user = null;
					//System.out.println(users.size());
					if(users != null && users.size()==1){
						user = users.get(0);
						if (user != null) {
							log.info(user.getLoginname()+" add Cashin proposal,the amount is "+amount);
							AbcTransfers abcTransfer = (AbcTransfers)getDataDao.get(AbcTransfers.class, abcTransfers.getTransfeId(), LockMode.UPGRADE);
							if(abcTransfer.getStatus()==0){
								abcTransfer.setStatus(1);
								abcTransfer.setJyzy(notes+"  "+user.getLoginname());
								abcTransfer.setTimecha(DateUtil.getTimecha(new Timestamp(abcTransfer.getPayDate().getTime()),new Timestamp(abcTransfer.getDate().getTime())));
								abcTransfer.setOvertime(DateUtil.getOvertime(new Timestamp(abcTransfer.getPayDate().getTime()),new Timestamp(abcTransfer.getDate().getTime())));
								getDataDao.update(abcTransfer);
								/**
								 * update cashin
								 */
								Proposal proposal=new Proposal();
								try {
									String pno = getDataDao.generateProposalPno("502");
									/**
									 * 就是本地农行转农行 手续费0 
									 * 异地农行转农行 手续费按交易金额的0.4%,最低1元/笔，最高20元/笔
									 * 
									 * (目前不分是否异地本地，手续费按交易金额的0.4%,最低1元/笔，最高20元/笔)
									 */
									fee = amount*0.004;
									if(fee<=1){
										fee=1.00;
									}else if(fee>=20){
										fee=20.00;
									}
									
									Cashin cashin = new Cashin(pno,user.getRole(), user.getLoginname(),StringUtil.trim(user.getAccountName()),amount,"XXXXXXXXXXXXXXXX","农业银行","");
									cashin.setCashintime(DateUtil.now()); 
									/*************存款红利0.005*******************/
									if(user.getLevel()>100){
										/**
										 * 限制手续费最高1000元
										 */
										Calendar cday = Calendar.getInstance();
										cday.set(Calendar.HOUR_OF_DAY, 0);
										cday.set(Calendar.MINUTE, 0);
										cday.set(Calendar.SECOND, 0);
										Date startday = cday.getTime();
										cday.set(Calendar.HOUR_OF_DAY, 23);
										cday.set(Calendar.MINUTE, 59);
										cday.set(Calendar.SECOND, 59);
										Date endday = cday.getTime();
										DetachedCriteria dc = DetachedCriteria.forClass(Cashin.class);
										dc.add(Restrictions.eq("loginname", user.getLoginname()));
										dc.add(Restrictions.and(Restrictions.ge("cashintime", startday),Restrictions.lt("cashintime", endday)));
										dc.setProjection(Projections.sum("fee"));
										List list = getDataDao.findByCriteria(dc);
										/**
										 * 多笔
										 */
										if(list!=null && !list.isEmpty() && null!=list.get(0)){
											Double d = (Double)list.get(0);
											Double dam = maxAmount-d;//系统剩余可返还的手续费
											if(dam<=0){
												fee = 0.00;
											}else{
												fee = amount*0.005;
												if(fee>dam){
													fee = dam;
												}
											}
										}else{
											/**
											 * 只有一笔
											 */
											fee = amount*0.005;
											if(fee>maxAmount){
												fee = maxAmount;
											}
										}
									}else{
										fee = amount*0.005;
									}
									/*******************************************/
									cashin.setFee(fee);
									amount=amount+fee;
									proposal.setPno(pno);
									proposal.setProposer("system");
									proposal.setCreateTime(DateUtil.now());
									proposal.setType(502);
									proposal.setQuickly(user.getLevel());
									proposal.setLoginname(user.getLoginname());
									proposal.setAmount(amount);
									proposal.setAgent(user.getAgent());
									proposal.setFlag(2);
									proposal.setWhereisfrom("system");
									proposal.setRemark("网银转账");
									proposal.setBankaccount(abcTransfer.getAcceptName());
									proposal.setSaveway("网银");
									
									getDataDao.save(cashin);
									getDataDao.save(proposal);
									getDataDao.generateTasks(pno, "system");
									getDataDao.changeCreditBySql(user, proposal.getAmount(), "CASHIN", pno, "");
									
									// 设置用户 isCashin字段
									if (user.getIsCashin().intValue() == Constants.FLAG_FALSE.intValue()) {
										getDataDao.updateUserIsCashInSql(user.getLoginname());
									}
									
									//改变我方银行的金额
									getDataDao.changeAmountByNameSql(proposal.getBankaccount(), proposal.getAmount()-fee,pno);
									getDataDao.excuteTask(pno, "system", "");
									getDataDao.insertOperationLog("system", "EXCUTE", "ip:" + "" + ";pno:" + pno);

									log.info("本次ABC处理的时间是："+new Date());
									
									try {
										// 根据客户定制的服务，决定是否发送邮件和短信
										String service = user.getAddress();
										if (service != null && service.indexOf("9") != -1) {// 发短信
											String smsmsg = notifyService.sendSMSByProposal(proposal);
											log.info("abc send SMS:" + smsmsg);
										}
									} catch (Exception e) {
										log.error("abc发送短信异常:",e);
									}
								} catch (Exception e) {
									e.printStackTrace();
									throw new GenericDfhRuntimeException(e.getMessage());
								}
							}	
						}else{
							log.info("随机数为："+notes+"，无此用户");
							abcTransfers.setStatus(2);
							getDataDao.update(abcTransfers);
						}
					}else{
						log.info("随机数为："+notes+"，无此用户");
						abcTransfers.setStatus(2);
						getDataDao.update(abcTransfers);
					}
				}else {
					//存款附言里面无内容或者整型溢出
					abcTransfers.setStatus(2);
					getDataDao.update(abcTransfers);
				}
			}else{
				log.info("本次ABC处理的记录数是：0，处理时间是："+new Date());
			}
		}
		
	}
	
	/****
	 * 新秒存 招商银行    
	 */
	@Override
	public void updateoperatorNew(String bankname) {
		log.info("新秒存定时任务开始，bankname：" + bankname);
		if("ICBC".equals(bankname)){
			DetachedCriteria dc_icbc = DetachedCriteria.forClass(IcbcTransfers.class);
			dc_icbc.add(Restrictions.eq("status", 0));
			List<IcbcTransfers> icbcList = this.findByCriteria(dc_icbc);
//			IcbcTransfers icbcTransfers = getDataDao.getIcbcDataBySql(); 
			
			if(icbcList != null && icbcList.size() > 0){
				log.info("ICBC需处理条数：" + icbcList.size());
				for(IcbcTransfers icbcTransfers : icbcList){
					/*if(icbcTransfers!=null){*/
						log.info("本次ICBC处理的记录数是：1");
						String notes = icbcTransfers.getNotes();//取得附言信息
						double amount = icbcTransfers.getAmount();//取得转账金额
						String acceptName = icbcTransfers.getAcceptName();
						int status=icbcTransfers.getStatus(); //存款是否处理的状态位 ，0未处理 1已充值 2充值超时处理
						Double fee=icbcTransfers.getFee();//取得转账手续费
						String randomStr = notes ;
						if(notes != null && !"".equals(notes)){//如果附言不为空
						 	notes  = StringUtil.trim(notes);
						 	
						 	DepositOrder depositOrder = (DepositOrder) getDataDao.get(DepositOrder.class, notes, LockMode.UPGRADE);
						 	if(null == depositOrder){
						 		log.info("附言："+notes+"没有和存款订单匹配上");
						 		icbcTransfers.setStatus(2);
								getDataDao.update(icbcTransfers);
						 		return ;
						 	}
						 	if(depositOrder.getStatus()==1 || !depositOrder.getBankname().equals("工商银行") || 
						 			(!depositOrder.getBankno().equals(icbcTransfers.getAcceptCardnum()))
						 			|| (!depositOrder.getAccountname().equals(icbcTransfers.getAcceptName()))){
						 		log.info("depositOrder"+depositOrder.getBankno()+"   "+depositOrder.getAccountname()+" "+depositOrder.getStatus());
						 		log.info("icbcTransfers"+icbcTransfers.getAcceptCardnum()+"   "+icbcTransfers.getAcceptName());
						 		icbcTransfers.setStatus(2);
								getDataDao.update(icbcTransfers);
						 		return ;
						 	}else{
						 		depositOrder.setStatus(1);
						 		depositOrder.setUpdatetime(new Date());
						 		getDataDao.update(depositOrder);
						 	}
						 	notes = depositOrder.getLoginname();
						 	
						 	
						 	DetachedCriteria c = DetachedCriteria.forClass(Users.class);
							c.add(Restrictions.eq("loginname",notes));
							List<Users> users =getDataDao.findByCriteria(c);
							//System.out.println(c);
							Users user = null;
							//System.out.println(users.size());
							if(users != null && users.size()==1){
								user = users.get(0);
								if (user != null) {
									log.info(user.getLoginname()+" add Cashin proposal,the amount is "+amount);
									IcbcTransfers icbcTransfer = (IcbcTransfers)getDataDao.get(IcbcTransfers.class, icbcTransfers.getTransfeId(), LockMode.UPGRADE);
									if(icbcTransfer.getStatus()==0){
										icbcTransfer.setStatus(1);
										icbcTransfer.setNotes(randomStr+"  "+user.getLoginname());
										icbcTransfer.setTimecha(DateUtil.getTimecha(new Timestamp(icbcTransfer.getPayDate().getTime()),new Timestamp(icbcTransfer.getDate().getTime())));
										icbcTransfer.setOvertime(DateUtil.getOvertime(new Timestamp(icbcTransfer.getPayDate().getTime()),new Timestamp(icbcTransfer.getDate().getTime())));
										getDataDao.update(icbcTransfer);
										/**
										 * update cashin
										 */
										Proposal proposal=new Proposal();
										try {
//											String pno = getDataDao.generateProposalPno("502");
											String pno = getDataDao.generateProposalIcbcPno("502");
											if(fee ==null)
												fee=0.0;
											
											Cashin cashin = new Cashin(pno,user.getRole(), user.getLoginname(),StringUtil.trim(user.getAccountName()),amount,"XXXXXXXXXXXXXXXX","工商银行","");
											cashin.setCashintime(DateUtil.now()); 
											/*************存款红利0.005*******************/
											/**
											 * 2016-02-01 取消网银存款红利
											 */
											/*if(user.getLevel()<=10){
												*//**
												 * 限制手续费最高1000元
												 *//*
												Calendar cday = Calendar.getInstance();
												cday.set(Calendar.HOUR_OF_DAY, 0);
												cday.set(Calendar.MINUTE, 0);
												cday.set(Calendar.SECOND, 0);
												Date startday = cday.getTime();
												cday.set(Calendar.HOUR_OF_DAY, 23);
												cday.set(Calendar.MINUTE, 59);
												cday.set(Calendar.SECOND, 59);
												Date endday = cday.getTime();
												DetachedCriteria dc = DetachedCriteria.forClass(Cashin.class);
												dc.add(Restrictions.eq("loginname", user.getLoginname()));
												dc.add(Restrictions.and(Restrictions.ge("cashintime", startday),Restrictions.lt("cashintime", endday)));
												dc.setProjection(Projections.sum("fee"));
												List list = getDataDao.findByCriteria(dc);
												*//**
												 * 多笔
												 *//*
												if(list!=null && !list.isEmpty() && null!=list.get(0)){
													Double d = (Double)list.get(0);
													Double dam = 500-d;//系统剩余可返还的手续费
													if(dam<=0){
														fee = 0.00;
													}else{
														fee = amount*0.005;
														if(fee>dam){
															fee = dam;
														}
													}
												}else{
													*//**
													 * 只有一笔
													 *//*
													fee = amount*0.005;
													if(fee>500){
														fee = 500.00;
													}
												}
											}else{
												fee = amount*0.005;
											}*/
											/*******************************************/
											//cashin.setFee(fee);
											//amount=amount+fee;
											proposal.setPno(pno);
											proposal.setProposer("system");
											proposal.setCreateTime(DateUtil.now());
											proposal.setType(502);
											proposal.setQuickly(user.getLevel());
											proposal.setLoginname(user.getLoginname());
											proposal.setAmount(amount);
											proposal.setAgent(user.getAgent());
											proposal.setFlag(2);
											proposal.setWhereisfrom("system");
											proposal.setRemark("网银转账");
											proposal.setBankaccount(acceptName);
											proposal.setSaveway("网银");
											
											getDataDao.save(cashin);
											getDataDao.save(proposal);
											getDataDao.generateTasks(pno, "system");
											getDataDao.changeCreditBySql(user, proposal.getAmount(), "CASHIN", pno, "");
											
											// 设置用户 isCashin字段
											if (user.getIsCashin().intValue() == Constants.FLAG_FALSE.intValue()) {
												getDataDao.updateUserIsCashInSql(user.getLoginname());
											}
											
											//改变我方银行的金额
											//getDataDao.changeAmountByNameSql(proposal.getBankaccount(), proposal.getAmount()-fee,pno);
											getDataDao.changeAmountByNameSql(proposal.getBankaccount(), proposal.getAmount(),pno);
											getDataDao.excuteTask(pno, "system", "");
											getDataDao.insertOperationLog("system", "EXCUTE", "ip:" + "" + ";pno:" + pno);

											log.info("本次ICBC处理的时间是："+new Date());
											
											try {
												// 根据客户定制的服务，决定是否发送邮件和短信
												String service = user.getAddress();
												if (service != null && service.indexOf("9") != -1) {// 发短信
													String smsmsg = notifyService.sendSMSByProposal(proposal);
													log.info("icbc send SMS:" + smsmsg);
												}
											} catch (Exception e) {
												log.error("icbc发送短信异常:",e);
											}
										} catch (Exception e) {
											e.printStackTrace();
											throw new GenericDfhRuntimeException(e.getMessage());
										}
									}	
								}else{
									log.info("id为："+notes+"，无此用户");
									icbcTransfers.setStatus(2);
									getDataDao.update(icbcTransfers);
								}
							}else{
								log.info("id为："+notes+"，无此用户");
								icbcTransfers.setStatus(2);
								getDataDao.update(icbcTransfers);
							}
						}else {
							//存款附言里面无内容或者整型溢出
							icbcTransfers.setStatus(2);
							getDataDao.update(icbcTransfers);
						}
					/*}else{
						log.info("本次ICBC处理的记录数是：0");
					}*/
				}
				
				log.info("ICBC需处理条数：" + icbcList.size() + "处理完毕！");
			}
			
			
		}else if("CMB".equals(bankname)){
			
			DetachedCriteria dc_cmb = DetachedCriteria.forClass(CmbTransfers.class);
			dc_cmb.add(Restrictions.eq("status", 0));
			List<CmbTransfers> cmbList = this.findByCriteria(dc_cmb);
			//根据扫描到的订单信息查询管理银行账户 属于支付宝 还是网银
//			CmbTransfers cmbTransfers = getDataDao.getCmbDataBySql();
			if(cmbList != null && cmbList.size() > 0){
				log.info("CMB处理条数：" + cmbList.size());
				for(CmbTransfers cmbTransfers : cmbList){

					log.info("本次CMB处理的记录数是：1");
					//获取收款卡号    
					String acceptNo = cmbTransfers.getAcceptCardnum();
					//获取存款姓名
					String uaccountname = cmbTransfers.getUaccountname();
					//获取存款金额
					Double amount = cmbTransfers.getAmount();
					//获取备注
					String remark = cmbTransfers.getRemark();
					//获取转账类型
					String  moneyType = cmbTransfers.getJylx();
					if(moneyType.contains("支付宝")){
						moneyType = "支付宝";
					}
					else {
						moneyType = "网银";
					}
					//获取卡号
					String bankcard="";
					//费率
					Double fee=0.00;
					//获取管理银行收款卡信息
//					log.info(">>>>银行卡号："+acceptNo);
//					DetachedCriteria dca = DetachedCriteria.forClass(Bankinfo.class);
//					dca.add(Restrictions.eq("bankcard",acceptNo));
//					dca.add(Restrictions.eq("useable",0));
//					List<Bankinfo> bankinfos =getDataDao.findByCriteria(dca);
//					log.info(">>>>bankinfos："+bankinfos+">>>size"+bankinfos.size());
//					Bankinfo bankinfo = null;
//				 if(bankinfos != null && bankinfos.size()>0){  
//						bankinfo = bankinfos.get(0);  
//						log.info(">>>>bankinfo对象"+bankinfo);    
//						if (bankinfo != null) {
//							log.info(">>>卡号："+bankinfo.getBankcard()+">>>bankname："+bankinfo.getBankname()+">>>username："+bankinfo.getUsername());
//							log.info(">>>paytype："+bankinfo.getPaytype()+">>>>remark:"+remark);             
							//网银付款 paytype = 0
							//Integer paytype = bankinfo.getPaytype().intValue(); 
							//String   resultRemark = remark == null?"":remark;
							//if((paytype==1&& resultRemark!="")  ||  paytype == 0  ||  resultRemark.contains("补单") ){
							String   resultRemark = remark == null?"":remark;
							//if(bankinfo.getPaytype().intValue() == -1 ||  resultRemark.contains("补单") ){
							if(resultRemark.contains("补单") ){
								String notes = cmbTransfers.getNotes();//取得附言信息
								 amount = cmbTransfers.getAmount();//取得转账金额
								 bankcard = cmbTransfers.getAcceptCardnum();//取得卡号
								log.info("本次CMB处理是："+cmbTransfers.getUaccountname()+">>>网银付款");
								
								String randomStr = notes ;
								if(notes != null && !"".equals(notes)){//如果附言不为空
								 	notes  = StringUtil.trim(notes);
								 	//DepositOrder depositOrder = (DepositOrder) getDataDao.get(DepositOrder.class, notes, LockMode.UPGRADE);
									DetachedCriteria dca1 = DetachedCriteria.forClass(DepositOrder.class);
									dca1.add(Restrictions.eq("depositId",notes));
									dca1.add(Restrictions.eq("bankno",bankcard));
									List<DepositOrder> depositOrders =getDataDao.findByCriteria(dca1);  
									DepositOrder depositOrder = null;
									if(depositOrders != null && depositOrders.size()>0){
										depositOrder = depositOrders.get(0);
									}
								 	if(null == depositOrder){
								 		log.info("附言："+notes+"没有和存款订单匹配上");
								 		cmbTransfers.setStatus(2);
										getDataDao.update(cmbTransfers);
								 		return ;
								 	}
								 	if(depositOrder.getStatus()==1|| !depositOrder.getBankname().equals("网银") && !depositOrder.getBankname().equals("支付宝")  &&   !depositOrder.getBankname().equals("招商银行")
								 			|| (!depositOrder.getBankno().equals(cmbTransfers.getAcceptCardnum())) || (!depositOrder.getAccountname().equals(cmbTransfers.getAcceptName()))
								 			){
								 		log.info("depositOrder"+depositOrder.getBankno()+"   "+depositOrder.getAccountname()+" "+depositOrder.getStatus());
								 		log.info("icbcTransfers"+cmbTransfers.getAcceptCardnum()+"   "+cmbTransfers.getAcceptName());
								 		cmbTransfers.setStatus(2);
								 		cmbTransfers.setLoginname(depositOrder.getLoginname());
										getDataDao.update(cmbTransfers);
								 		return ;
								 	}else{
								 		depositOrder.setStatus(1);
								 		depositOrder.setUpdatetime(new Date());
								 		getDataDao.update(depositOrder);
								 	}
								 	notes = depositOrder.getLoginname();
								 	
								 	
								 	DetachedCriteria c = DetachedCriteria.forClass(Users.class);
									c.add(Restrictions.eq("loginname",notes));
									List<Users> users =getDataDao.findByCriteria(c);
									//System.out.println(c);
									Users user = null;
									//System.out.println(users.size());
									if(users != null && users.size()==1){
										user = users.get(0);
										if (user != null) {
											log.info(user.getLoginname()+" add Cashin proposal,the amount is "+amount);
											CmbTransfers cmbTransfer = (CmbTransfers)getDataDao.get(CmbTransfers.class, cmbTransfers.getTransfeId());
											if(cmbTransfer.getStatus()==0){
												cmbTransfer.setStatus(1);
												cmbTransfers.setLoginname(depositOrder.getLoginname());
												cmbTransfer.setNotes(randomStr+"  "+user.getLoginname());
												cmbTransfer.setTimecha(DateUtil.getTimecha(new Timestamp(cmbTransfer.getPayDate().getTime()),new Timestamp(cmbTransfer.getDate().getTime())));
												cmbTransfer.setOvertime(DateUtil.getOvertime(new Timestamp(cmbTransfer.getPayDate().getTime()),new Timestamp(cmbTransfer.getDate().getTime())));
												getDataDao.update(cmbTransfer);
												/**
												 * update cashin
												 */
												Proposal proposal=new Proposal();
												try {
													String pno = getDataDao.generateProposalCmbPno("502");
													if("客户转账".equals(cmbTransfer.getJylx())){
														//建行加急转账按0.005收取，最低2元，25元封顶
														fee = amount*0.005;
														if(fee<=2){
															fee=2.00;
														}else if(fee>=25){
															fee=25.00;
														}
													}
													
													Cashin cashin = null;
													if("客户转账".equals(cmbTransfer.getJylx())){
														 //cashin = new Cashin(pno,user.getRole(), user.getLoginname(),StringUtil.trim(user.getAccountName()),amount,"XXXXXXXXXXXXXXXX","建设银行","");
														 cashin = new Cashin(pno,user.getRole(), user.getLoginname(),StringUtil.trim(user.getAccountName()),amount,"XXXXXXXXXXXXXXXX",cmbTransfer.getAcceptName(),"");
													}else{
														 cashin = new Cashin(pno,user.getRole(), user.getLoginname(),StringUtil.trim(user.getAccountName()),amount,"XXXXXXXXXXXXXXXX",cmbTransfer.getAcceptName(),"");
													}
													Double available = this.getAvailable(user.getLoginname());
													Double depositfee = amount*0.005>available?available:amount*0.005;
													cashin.setFee(depositfee);
													Timestamp now = DateUtil.now();
													cashin.setCashintime(now); 
													proposal.setPno(pno);
													proposal.setProposer("system");
													proposal.setCreateTime(now);
													proposal.setType(502);
													proposal.setQuickly(user.getLevel());
													proposal.setLoginname(user.getLoginname());
													proposal.setAmount(amount);
													proposal.setAgent(user.getAgent());
													proposal.setFlag(2);
													proposal.setExecuteTime(now);
													proposal.setWhereisfrom("system");
													proposal.setRemark(moneyType+"转账");
													proposal.setBankaccount(cmbTransfer.getAcceptName());
													proposal.setSaveway(moneyType);
													proposal.setGifTamount(depositfee); //秒存加赠0.5%的金额，上限为1000元
													
													getDataDao.save(cashin);
													getDataDao.save(proposal);
													getDataDao.generateTasks(pno, "system");
//													getDataDao.changeCreditBySql(user, proposal.getAmount()+proposal.getAmount()*0.005, "CASHIN", pno, "");
													getDataDao.changeCreditBySql(user, proposal.getAmount()+depositfee, "CASHIN", pno, "");
													
													// 设置用户 isCashin字段
													if (user.getIsCashin().intValue() == Constants.FLAG_FALSE.intValue()) {
														getDataDao.updateUserIsCashInSql(user.getLoginname());
													}
													
													//改变我方银行的金额
													//getDataDao.changeAmountByNameSql(proposal.getBankaccount(), proposal.getAmount()-fee,pno);
													getDataDao.changeAmountByNameSql(proposal.getBankaccount(), proposal.getAmount(),pno);
													getDataDao.excuteTask(pno, "system", "");
													getDataDao.insertOperationLog("system", "EXCUTE", "ip:" + "" + ";pno:" + pno);

													log.info("本次CMB处理的时间是："+new Date());
													
													try {
														// 根据客户定制的服务，决定是否发送邮件和短信
														String service = user.getAddress();
														if (service != null && service.indexOf("9") != -1) {// 发短信
															String smsmsg = notifyService.sendSMSByProposal(proposal);
															log.info("cmb send SMS:" + smsmsg);
														}
													} catch (Exception e) {
														log.error("cmb发送短信异常:",e);
													}
												} catch (Exception e) {
													e.printStackTrace();
													throw new GenericDfhRuntimeException(e.getMessage());
												}
											}	
										}else{
											log.info("id为："+notes+"，无此用户");
											cmbTransfers.setStatus(2);
											getDataDao.update(cmbTransfers);
										}
									}else{
										log.info("id为："+notes+"，无此用户");
										cmbTransfers.setStatus(2);
										getDataDao.update(cmbTransfers);
									}
								}else {
									//存款附言里面无内容或者整型溢出
									cmbTransfers.setStatus(2);
									getDataDao.update(cmbTransfers);
								}
							}
							//支付宝付款 paytype = 1 网银存款paytype = 0
							//else if(bankinfo.getPaytype().intValue() == 1 || bankinfo.getPaytype().intValue() == 0){
							else{
								log.info("本次CMB处理是："+cmbTransfers.getUaccountname()+">>支付宝付款");
								//采用玩家存款银行账号 存款姓名 存款额度来匹配
								DetachedCriteria dc = DetachedCriteria.forClass(DepositOrder.class);
								dc = dc.add(Restrictions.eq("uaccountname", uaccountname));
								dc = dc.add(Restrictions.eq("amount", amount));
								dc = dc.add(Restrictions.eq("status", 0));
								Order o = Order.asc("createtime");
								List<DepositOrder> list1 = getDataDao.findByCriteria(dc);    
								log.info(dc+"1>>>>>>");
								if(list1 != null && list1.size()==1){
								log.info(dc+"2>>>>>>");
								DepositOrder depositOrder =  list1.get(0);   
								log.info(dc+"3>>>>>>");
							 	if(null == depositOrder){
							 		log.info("姓名："+uaccountname+"，金额："+amount+"没有和存款订单匹配上");
							 		cmbTransfers.setStatus(2);
									getDataDao.update(cmbTransfers);
							 		return ;
							 	}
							 	//status=0 未存款 status =1 已存款 status=2 失败
							 	//if(depositOrder.getStatus()==1|| !depositOrder.getBankname().equals("招商银行") 
							 	if(depositOrder.getStatus()==1|| !depositOrder.getBankname().equals("支付宝") &&  !depositOrder.getBankname().equals("网银") &&   !depositOrder.getBankname().equals("招商银行")
							 			|| (!depositOrder.getBankno().equals(cmbTransfers.getAcceptCardnum())) || (!depositOrder.getAccountname().equals(cmbTransfers.getAcceptName()))
							 			){
							 		log.info("depositOrder"+depositOrder.getBankno()+"   "+depositOrder.getAccountname()+" "+depositOrder.getStatus());
							 		log.info("icbcTransfers"+cmbTransfers.getAcceptCardnum()+"   "+cmbTransfers.getAcceptName());
							 		cmbTransfers.setStatus(2);
							 		cmbTransfers.setLoginname(depositOrder.getLoginname());
									getDataDao.update(cmbTransfers);
							 		return ;
							 	}else{
							 		depositOrder.setStatus(1);
							 		depositOrder.setUpdatetime(new Date());
							 		getDataDao.update(depositOrder);
							 	}
							 	String loginname = depositOrder.getLoginname();
							 	
							 	
							 	DetachedCriteria c = DetachedCriteria.forClass(Users.class);
								c.add(Restrictions.eq("loginname",loginname));
								List<Users> users =getDataDao.findByCriteria(c);
								Users user = null;
								if(users != null && users.size()==1){
									user = users.get(0);
									if (user != null) {
										log.info(user.getLoginname()+" add Cashin proposal,the amount is "+amount);
										CmbTransfers cmbTransfer = (CmbTransfers)getDataDao.get(CmbTransfers.class, cmbTransfers.getTransfeId());
										if(cmbTransfer.getStatus()==0){
											cmbTransfer.setStatus(1);
											cmbTransfer.setLoginname(loginname);
											//cmbTransfer.setNotes(randomStr+"  "+user.getLoginname());
											cmbTransfer.setTimecha(DateUtil.getTimecha(new Timestamp(cmbTransfer.getPayDate().getTime()),new Timestamp(cmbTransfer.getDate().getTime())));
											cmbTransfer.setOvertime(DateUtil.getOvertime(new Timestamp(cmbTransfer.getPayDate().getTime()),new Timestamp(cmbTransfer.getDate().getTime())));
											getDataDao.update(cmbTransfer);
											/**
											 * update cashin
											 */
											Proposal proposal=new Proposal();
											try {
												String pno = getDataDao.generateProposalCmbPno("502");
												if("客户转账".equals(cmbTransfer.getJylx())){
													//建行加急转账按0.005收取，最低2元，25元封顶
													fee = amount*0.005;
													if(fee<=2){
														fee=2.00;
													}else if(fee>=25){
														fee=25.00;
													}
												}
												
												Cashin cashin = null;
												if("客户转账".equals(cmbTransfer.getJylx())){
													 //cashin = new Cashin(pno,user.getRole(), user.getLoginname(),StringUtil.trim(user.getAccountName()),amount,"XXXXXXXXXXXXXXXX","建设银行","");
													 cashin = new Cashin(pno,user.getRole(), user.getLoginname(),StringUtil.trim(user.getAccountName()),amount,"XXXXXXXXXXXXXXXX",cmbTransfer.getAcceptName(),"");
												}else{
													 cashin = new Cashin(pno,user.getRole(), user.getLoginname(),StringUtil.trim(user.getAccountName()),amount,"XXXXXXXXXXXXXXXX",cmbTransfer.getAcceptName(),"");
												}
												
												Double available = this.getAvailable(user.getLoginname());
												Double depositfee = amount*0.005>available?available:amount*0.005;
												cashin.setFee(depositfee);
												Timestamp now = DateUtil.now();
												cashin.setCashintime(now); 
												/*************存款红利0.005*******************/
												/**
												 * 2016-02-01 取消网银存款红利
												 */
												/*if(user.getLevel()<=10){
													*//**
													 * 限制手续费最高1000元
													 *//*
													Calendar cday = Calendar.getInstance();
													cday.set(Calendar.HOUR_OF_DAY, 0);
													cday.set(Calendar.MINUTE, 0);
													cday.set(Calendar.SECOND, 0);
													Date startday = cday.getTime();
													cday.set(Calendar.HOUR_OF_DAY, 23);
													cday.set(Calendar.MINUTE, 59);
													cday.set(Calendar.SECOND, 59);
													Date endday = cday.getTime();
													DetachedCriteria dc = DetachedCriteria.forClass(Cashin.class);
													dc.add(Restrictions.eq("loginname", user.getLoginname()));
													dc.add(Restrictions.and(Restrictions.ge("cashintime", startday),Restrictions.lt("cashintime", endday)));
													dc.setProjection(Projections.sum("fee"));
													List list = getDataDao.findByCriteria(dc);
													*//**
													 * 多笔
													 *//*
													if(list!=null && !list.isEmpty() && null!=list.get(0)){
														Double d = (Double)list.get(0);
														Double dam = 500-d;//系统剩余可返还的手续费
														if(dam<=0){
															fee = 0.00;
														}else{
															fee = amount*0.005;
															if(fee>dam){
																fee = dam;
															}
														}
													}else{
														*//**
														 * 只有一笔
														 *//*
														fee = amount*0.005;
														if(fee>500){
															fee = 500.00;
														}
													}
												}else{
													fee = amount*0.005;
												}*/
												/*******************************************/
												//cashin.setFee(fee);
												//amount=amount+fee;
												proposal.setPno(pno);
												proposal.setProposer("system");
												proposal.setCreateTime(now);
												proposal.setType(502);
												proposal.setQuickly(user.getLevel());
												proposal.setLoginname(user.getLoginname());
												proposal.setAmount(amount);
												proposal.setAgent(user.getAgent());
												proposal.setFlag(2);
												proposal.setExecuteTime(now);
												proposal.setWhereisfrom("system");
												proposal.setRemark(moneyType+"转账");
												proposal.setBankaccount(cmbTransfer.getAcceptName());
												proposal.setSaveway(moneyType);
												proposal.setGifTamount(depositfee); //秒存加赠0.5%的金额，上限为1000元
												
												getDataDao.save(cashin);
												getDataDao.save(proposal);
												getDataDao.generateTasks(pno, "system");
//												getDataDao.changeCreditBySql(user, proposal.getAmount()+proposal.getAmount()*0.005, "CASHIN", pno, "");   
												getDataDao.changeCreditBySql(user, proposal.getAmount()+depositfee, "CASHIN", pno, "");   
												
												// 设置用户 isCashin字段  
												if (user.getIsCashin().intValue() == Constants.FLAG_FALSE.intValue()) {
													getDataDao.updateUserIsCashInSql(user.getLoginname());
												}
												//改变我方银行的金额
												//getDataDao.changeAmountByNameSql(proposal.getBankaccount(), proposal.getAmount()-fee,pno);
												getDataDao.changeAmountByNameSql(proposal.getBankaccount(), proposal.getAmount(),pno);
												getDataDao.excuteTask(pno, "system", "");
												getDataDao.insertOperationLog("system", "EXCUTE", "ip:" + "" + ";pno:" + pno);
	    
												log.info("本次CMB处理的时间是："+new Date());   
												
												try {
													// 根据客户定制的服务，决定是否发送邮件和短信
													String service = user.getAddress();
													if (service != null && service.indexOf("9") != -1) {// 发短信
														String smsmsg = notifyService.sendSMSByProposal(proposal);
														log.info("cmb send SMS:" + smsmsg);
													}
												} catch (Exception e) {
													log.error("cmb发送短信异常:",e);
												}
											} catch (Exception e) {
												e.printStackTrace();
												throw new GenericDfhRuntimeException(e.getMessage());
											}
										}	
									}else{
										log.info("id为："+loginname+"，无此用户");
										cmbTransfers.setStatus(2);
										getDataDao.update(cmbTransfers);
									}
								}else{
									log.info("id为："+loginname+"，无此用户");
									cmbTransfers.setStatus(2);
									getDataDao.update(cmbTransfers);
								}
							}
							else {
								//存款附言里面无内容或者整型溢出
								cmbTransfers.setStatus(2);
								getDataDao.update(cmbTransfers);
							}
						   }
					  // }
					//} else {
					//	log.error(">>>>银行卡号："+acceptNo + "异常，数量：" + bankinfos.size());
					//}
				//}
			
				}
			}
			log.info("CMB处理条数：" + cmbList.size() + "处理完毕！");
		}
		
	}

	public void setGetDataDao(IGetdataDao getDataDao) {
		this.getDataDao = getDataDao;
	}

	@Override
	public Object save(Object o) {
		// TODO Auto-generated method stub
		return getDataDao.save(o);
	}

	@Override
	public List findByCriteria(DetachedCriteria criteria) {
		// TODO Auto-generated method stub
		return getDataDao.findByCriteria(criteria);
	}

	public void setNotifyService(NotifyService notifyService) {
		this.notifyService = notifyService;
	}

	@Override
	public void processStatusData(Users ptUser) {
		try {
			/** *********************************** */
			// 获取时间 GMT+8 换成 GMT
			/** *********************************** */
			// 获取开始时间
			Date startTime = null;
			Date endTime = null;
			Date date = DateUtil.now();
			
			SimpleDateFormat sdfHHH = new SimpleDateFormat("HH");
			Integer hh = Integer.parseInt(sdfHHH.format(date));
			/**
			 * 每天中午系统洗码11:30 -13:00期间进行,12:40:00-13:00:00将 ptflag =0
			 */
			Calendar a = Calendar.getInstance();
			a.set(Calendar.HOUR_OF_DAY,12);
			a.set(Calendar.MINUTE, 40);
			a.set(Calendar.SECOND, 0);
			
			Calendar b = Calendar.getInstance();
			b.set(Calendar.HOUR_OF_DAY,13);
			b.set(Calendar.MINUTE, 0);
			b.set(Calendar.SECOND, 0);
			
			if(date.after(a.getTime()) && date.before(b.getTime())){
				ptUser.setPtflag(0);
				getDataDao.update(ptUser);
				NTUtils.closeSession(ptUser.getLoginname());
				return;
			}
			
			if (hh < 12) {
				Calendar cals = Calendar.getInstance();
				cals.setTime(date);
				cals.set(Calendar.HOUR_OF_DAY, 12);
				cals.set(Calendar.MINUTE, 0);
				cals.set(Calendar.SECOND, 0);
				cals.add(Calendar.HOUR_OF_DAY, -7);
				endTime = cals.getTime();
				
				cals.add(Calendar.DAY_OF_MONTH, -1); // 减1天
				cals.set(Calendar.HOUR_OF_DAY, 12);
				cals.set(Calendar.MINUTE, 0);
				cals.set(Calendar.SECOND, 0);
				cals.add(Calendar.HOUR_OF_DAY, -7);
				startTime = cals.getTime();
				
			} else {
				Calendar cals = Calendar.getInstance();
				cals.setTime(date);
				cals.set(Calendar.HOUR_OF_DAY, 12);
				cals.set(Calendar.MINUTE, 0);
				cals.set(Calendar.SECOND, 0);
				cals.add(Calendar.HOUR_OF_DAY, -7);
				startTime = cals.getTime();
				cals.add(Calendar.DAY_OF_MONTH, 1); // 加1天
				cals.set(Calendar.HOUR_OF_DAY, 12);
				cals.set(Calendar.MINUTE, 0);
				cals.set(Calendar.SECOND, 0);
				cals.add(Calendar.HOUR_OF_DAY, -7);
				endTime = cals.getTime();
			}
			// 获取数据
			String loginString = NTUtils.getEffectiveBets(ptUser.getLoginname(), startTime, endTime);
			JSONObject jsonObj = JSONObject.fromObject(loginString);
			if(jsonObj.containsKey("stat")){
				// 获取数据
				Double payout = 0.00;
				Double betCredit = 0.00;
				for (Object object : jsonObj.getJSONArray("stat")) {
					List listObject = (List) object;
					Double multiplier = 0.00;
					Double lines = 0.00;
					Double bets = 0.00;
					if (Integer.parseInt((String) listObject.get(7)) == 0) {
						multiplier = (Double.parseDouble((String) listObject.get(2)));
						lines = (Double.parseDouble((String) listObject.get(3)));
						bets = (Double.parseDouble((String) listObject.get(4)));
					}
					payout=payout+(Double.parseDouble((String) listObject.get(6)) / 100);
					betCredit = betCredit + (multiplier * lines * bets / 100);
				}
				Calendar s = Calendar.getInstance();
				s.setTime(startTime);
				s.add(Calendar.HOUR_OF_DAY, 7);
				
				Calendar e = Calendar.getInstance();
				e.setTime(endTime);
				e.add(Calendar.HOUR_OF_DAY, 7);
				
				getDataDao.processPtProfit(ptUser,betCredit,payout,s.getTime(),e.getTime());
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.toString());
			throw new RuntimeException(e.getMessage());
		}
	}
	
	/**
	 * 根据时间抓取集团下所有玩家的投注额
	 */
	private Object ntLock = new Object();//对象锁
	@Override
	public void processStatusData(Date date) {
		try {
			Date sdate = MatchDateUtil.parseDatetime(MatchDateUtil.formatDate(date)+" 00:00:00");
			Date edate = MatchDateUtil.parseDatetime(MatchDateUtil.formatDate(date)+" 23:59:59");
			
			synchronized (ntLock) {
				//访问NT接口查询余额
				String groups = NTUtils.getGroupBets(sdate, edate);
				JSONObject jo = JSONObject.fromObject(groups);
				if (!jo.getBoolean("result")){
					log.error("processStatusData error, return msg:"+jo.getString("error"));
					return;
				}
				JSONArray ary = jo.getJSONArray("report"); //解析report数据
				log.info("NT本地次抓取到有游戏输赢的玩家数量为："+ary.size());
				for (int i=0;i<ary.size();i++) {
					JSONArray ud = ary.getJSONArray(i);
					String loginname=ud.getString(0).substring(1);//去掉loginname的开头
					//TODO 处理NT金额问题,需要除以100
					Double bet=ud.getDouble(1)/100;
					Double payout=ud.getDouble(2)/100;
					Double amount = bet-payout;
					//持久化NT数据 TODO 是否可以更新数据
					getDataDao.processNTProfit(loginname,bet,payout,sdate,edate);
				}
			}
		} catch (Exception e) {
			log.error("processStatusData error:",e);
			throw new RuntimeException(e.getMessage());
		}
	}
	
	public Boolean processNewStatusData(String PLAYERNAME,String CODE,String CURRENCYCODE,String ACTIVEPLAYERS,String BALANCECHANGE,String DEPOSITS,String WITHDRAWS,String BONUSES,String COMPS,String PROGRESSIVEBETS,String PROGRESSIVEWINS,String BETS,String WINS,String NETLOSS,String NETPURCHASE,String NETGAMING,String HOUSEEARNINGS,String RNUM,String DATATIME){
		return getDataDao.processNewPtProfit(PLAYERNAME,CODE,CURRENCYCODE,ACTIVEPLAYERS,BALANCECHANGE,DEPOSITS,WITHDRAWS,BONUSES,COMPS,PROGRESSIVEBETS,PROGRESSIVEWINS,BETS,WINS,NETLOSS,NETPURCHASE,NETGAMING,HOUSEEARNINGS,RNUM,DATATIME);
	}
	
	public Boolean dealNewPtData(String PLAYERNAME,String FULLNAME,String VIPLEVEL,String COUNTRY,String GAMES,String CURRENCYCODE,String BETS,String WINS,String INCOME,String RNUM,String dateTime){
		return getDataDao.dealNewPtDataDao(PLAYERNAME,FULLNAME,VIPLEVEL,COUNTRY,GAMES,CURRENCYCODE,BETS,WINS,INCOME,RNUM,dateTime);
	}

	@Override
	public Boolean updateNewPtData(String playername,String dateTime, Double betsTiger,
			Double winsTiger) {
		return getDataDao.updateNewPtData(playername,dateTime, betsTiger, winsTiger);
	}
	
	@Override
	public void updateSixLotteryPlatForm(PlatformData data) {
		getDataDao.updateSixLotteryPlatForm(data) ;
	}

	@Override
	public void updateQtPlatForm(PlatformData data) {
		getDataDao.updateQtPlatForm(data) ;
	}
	
	@Override
	public List<PlatformData> selectQtData(Date startT, Date endT){
		return getDataDao.selectQtData(startT, endT);
	}
	
	public AlipayAccount getAlipayAccount(String alipayAccount  , Integer disable){
		DetachedCriteria dc = DetachedCriteria.forClass(AlipayAccount.class);
		dc.add(Restrictions.eq("alipayAccount", alipayAccount));
		if(null != disable){
			dc.add(Restrictions.eq("disable", disable));
		}
		List<AlipayAccount> list = getDataDao.findByCriteria(dc);
		if(null != list && list.size()>0 && null != list.get(0)){
			return list.get(0);
		}else{
			return null ;
		}
	}
	
	@Override
	public void updateAlipayStatus() {
		Double maxAmount = 1000.0;
		AlipayTransfers alipayTransfers = getDataDao.getAlipayDataBySql();
		if(alipayTransfers!=null){
			log.info("本次Alipay处理的记录数是：1");
			String notes = alipayTransfers.getNotes();//取得附言信息
			double amount = alipayTransfers.getAmount();//取得转账金额
			String acceptName = alipayTransfers.getAcceptName();
			Double fee=alipayTransfers.getFee();//取得转账手续费
			String randomStr = notes ;
			if(notes != null && !"".equals(notes)){//如果附言不为空
			 	notes  = StringUtil.trim(notes);
			 	
			 	//附言模式
			 	if(alipayTransfers.getPaytype() == 1){
			 		DepositOrder depositOrder = (DepositOrder) getDataDao.get(DepositOrder.class, notes);
			 		if(null == depositOrder){
			 			log.info("附言："+notes+"没有和存款订单匹配上");
			 			alipayTransfers.setStatus(2);
			 			getDataDao.update(alipayTransfers);
			 			return ;
			 		}
			 		if(depositOrder.getStatus()==1|| !depositOrder.getBankname().equals("支付宝") || (!depositOrder.getBankno().equals(alipayTransfers.getAcceptNo())) || (!depositOrder.getAccountname().equals(alipayTransfers.getAcceptName()))){
			 			log.info("附言："+notes+"没有和存款订单匹配上");
			 			log.info("depositOrder"+depositOrder.getBankno()+"   "+depositOrder.getAccountname()+" "+depositOrder.getStatus());
			 			log.info("icbcTransfers"+alipayTransfers.getAcceptNo()+"   "+alipayTransfers.getAcceptName());
			 			alipayTransfers.setStatus(2);
			 			getDataDao.update(alipayTransfers);
			 			return ;
			 		}else{
			 			depositOrder.setStatus(1);
			 			depositOrder.setUpdatetime(new Date());
			 			getDataDao.update(depositOrder);
			 		}
			 		notes = depositOrder.getLoginname();
			 	}else if(alipayTransfers.getPaytype() == 2){
			 		//二维码扫描模式
			 		AlipayAccount alipayAccount = getAlipayAccount(notes, 0);
			 		if(null != alipayAccount){
			 			notes = alipayAccount.getLoginname();
			 		}else{
			 			log.info("二维码扫描支付："+notes+"没有未禁用的绑定记录");
			 			alipayTransfers.setStatus(2);
			 			getDataDao.update(alipayTransfers);
			 			return ;
			 		}
			 	}
			 	
			 	
			 	DetachedCriteria c = DetachedCriteria.forClass(Users.class);
				c.add(Restrictions.eq("loginname",notes));
				List<Users> users =getDataDao.findByCriteria(c);
				Users user = null;
				if(users != null && users.size()==1){
					user = users.get(0);
					if (user != null) {
						log.info(user.getLoginname()+" add Cashin proposal,the amount is "+amount);
						AlipayTransfers alipayTransfer = (AlipayTransfers)getDataDao.get(AlipayTransfers.class, alipayTransfers.getTransferId());
						if(alipayTransfer.getStatus()==0){
							alipayTransfer.setStatus(1);
							alipayTransfer.setNotes(randomStr+"  "+user.getLoginname());
							alipayTransfer.setTimecha(DateUtil.getTimecha(new Timestamp(alipayTransfer.getPayDate().getTime()),new Timestamp(alipayTransfer.getDate().getTime())));
							alipayTransfer.setOvertime(DateUtil.getOvertime(new Timestamp(alipayTransfer.getPayDate().getTime()),new Timestamp(alipayTransfer.getDate().getTime())));
							getDataDao.update(alipayTransfer);
							/**
							 * update cashin
							 */
							Proposal proposal=new Proposal();
							try {
								// String pno =
								// getDataDao.generateProposalAlipayPno(ProposalType.CASHIN.getCode().toString());
								String pno = new Date().getTime() + RandomStringUtils.randomNumeric(4).toString();
								if(fee ==null)
									fee=0.0;
								
								Cashin cashin = new Cashin(pno,user.getRole(), user.getLoginname(),StringUtil.trim(user.getAccountName()),amount,"XXXXXXXXXXXXXXXX","支付宝","");
								cashin.setCashintime(DateUtil.now()); 
								
								/*************存款红利0.005*******************/
								if(user.getLevel()>100){
									/**
									 * 限制手续费最高1000元
									 */
									Calendar cday = Calendar.getInstance();
									cday.set(Calendar.HOUR_OF_DAY, 0);
									cday.set(Calendar.MINUTE, 0);
									cday.set(Calendar.SECOND, 0);
									Date startday = cday.getTime();
									cday.set(Calendar.HOUR_OF_DAY, 23);
									cday.set(Calendar.MINUTE, 59);
									cday.set(Calendar.SECOND, 59);
									Date endday = cday.getTime();
									DetachedCriteria dc = DetachedCriteria.forClass(Cashin.class);
									dc.add(Restrictions.eq("loginname", user.getLoginname()));
									dc.add(Restrictions.and(Restrictions.ge("cashintime", startday),Restrictions.lt("cashintime", endday)));
									dc.setProjection(Projections.sum("fee"));
									List list = getDataDao.findByCriteria(dc);
									/**
									 * 多笔
									 */
									if(list!=null && !list.isEmpty() && null!=list.get(0)){
										Double d = (Double)list.get(0);
										Double dam = maxAmount-d;//系统剩余可返还的手续费
										if(dam<=0){
											fee = 0.00;
										}else{
											fee = amount*0.005;
											if(fee>dam){
												fee = dam;
											}
										}
									}else{
										/**
										 * 只有一笔
										 */
										fee = amount*0.005;
										if(fee>maxAmount){
											fee = maxAmount;
										}
									}
								}else{
									fee = amount*0.005;
								}
								System.out.println(fee+"＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝");
								/*******************************************/
//								cashin.setFee(fee);
//								amount=amount+fee;
								proposal.setPno(pno);
								proposal.setProposer("system");
								proposal.setCreateTime(DateUtil.now());
								proposal.setType(502);
								proposal.setQuickly(user.getLevel());
								proposal.setLoginname(user.getLoginname());
								proposal.setAmount(amount);
								proposal.setAgent(user.getAgent());
								proposal.setFlag(2);
								proposal.setWhereisfrom("system");
								proposal.setRemark("网银转账");
								proposal.setBankaccount(acceptName);
								proposal.setSaveway("网银");
								
								getDataDao.save(cashin);
								getDataDao.save(proposal);
								getDataDao.generateTasks(pno, "system");
								getDataDao.changeCreditBySql(user, proposal.getAmount(), "CASHIN", pno, "");
								
								// 设置用户 isCashin字段
								if (user.getIsCashin().intValue() == Constants.FLAG_FALSE.intValue()) {
									getDataDao.updateUserIsCashInSql(user.getLoginname());
								}
								
								//改变我方银行的金额
//								getDataDao.changeAmountByName(proposal.getBankaccount(), proposal.getAmount(),pno);
								getDataDao.changeAmountByNameSql(proposal.getBankaccount(), proposal.getAmount(),pno);
								getDataDao.excuteTask(pno, "system", "");
								getDataDao.insertOperationLog("system", "EXCUTE", "ip:" + "" + ";pno:" + pno);

								log.info("本次Alipay处理的时间是："+new Date());
								
								try {
									// 根据客户定制的服务，决定是否发送邮件和短信
									String service = user.getAddress();
									if (service != null && service.indexOf("9") != -1) {// 发短信
										String smsmsg = notifyService.sendSMSByProposal(proposal);
										log.info("Alipay send SMS:" + smsmsg);
									}
								} catch (Exception e) {
									log.error("Alipay发送短信异常:",e);
								}
							} catch (Exception e) {
								e.printStackTrace();
								throw new GenericDfhRuntimeException(e.getMessage());
							}
						}	
					}else{
						log.info("id为："+notes+"，无此用户");
						alipayTransfers.setStatus(2);
						getDataDao.update(alipayTransfers);
					}
				}else{
					log.info("id为："+notes+"，无此用户");
					alipayTransfers.setStatus(2);
					getDataDao.update(alipayTransfers);
				}
			}else {
				//存款附言里面无内容或者整型溢出
				alipayTransfers.setStatus(2);
				getDataDao.update(alipayTransfers);
			}
		}else{
			log.info("本次Alipay处理的记录数是：0");
		}
	}


	@Override
	public void processValiteDeposit() {
		
		//额度验证存款
		Double maxAmount = 500d;
		ValidateAmountDeposit item =  getDataDao.getValidateAmountDeposit();
		if(item != null){
			log.info("本次额度验证存款处理的记录数是：1，处理时间是："+new Date());
			String notes = item.getNote();         //取得附言信息
			Double amount = item.getAmount();       //取得转账金额
			String acceptName = item.getAcceptName();
			Double fee = item.getFee();             //取得转账手续费
			
			if(amount != null){  //金额不为空
			 	DetachedCriteria c = DetachedCriteria.forClass(PayOrderValidation.class);
				c.add(Restrictions.eq("amount", amount));
				c.add(Restrictions.eq("status", "0"));    //未到账状态
				List<PayOrderValidation> orders = getDataDao.findByCriteria(c);
				Users user = null;
				PayOrderValidation order = null;
				if(orders != null && orders.size()==1){
					order = orders.get(0);
					c = DetachedCriteria.forClass(Users.class);
					c.add(Restrictions.eq("loginname", order.getUserName()));
					List<Users> users = getDataDao.findByCriteria(c);
					if(users != null && users.size()==1) user = users.get(0);
					if (user != null) {
						log.info(order.getUserName()+" add Cashin proposal,the amount is "+amount);
						ValidateAmountDeposit deposit = (ValidateAmountDeposit)getDataDao.get(ValidateAmountDeposit.class, item.getTransID(), LockMode.UPGRADE);
						if(deposit.getStatus() == 0){
							deposit.setStatus(1);
							//deposit.setNote(notes+"  "+user.getLoginname());
							deposit.setTimecha(DateUtil.getTimecha(new Timestamp(deposit.getPayTime().getTime()),new Timestamp(deposit.getReadTime().getTime())));
							deposit.setOverTime(DateUtil.getOvertime(new Timestamp(deposit.getPayTime().getTime()),new Timestamp(deposit.getReadTime().getTime())));
							getDataDao.update(deposit);
							/**
							 * update cashin
							 */
							Proposal proposal = new Proposal();
							try {
								String pno = getDataDao.generateProposalPno("502");
								if(fee == null)
									fee = 0.0;
								
								Cashin cashin = new Cashin(pno, user.getRole(), user.getLoginname(),StringUtil.trim(user.getAccountName()), amount, "XXXXXXXXXXXXXXXX", deposit.getBankname(), "");
								cashin.setCashintime(DateUtil.now()); 
								
								/*************存款红利0.005*******************/
								if(user.getLevel() <= 10){
									/**
									 * 手续费限制
									 */
									Calendar cday = Calendar.getInstance();
									cday.set(Calendar.HOUR_OF_DAY, 0);
									cday.set(Calendar.MINUTE, 0);
									cday.set(Calendar.SECOND, 0);
									Date startday = cday.getTime();
									cday.set(Calendar.HOUR_OF_DAY, 23);
									cday.set(Calendar.MINUTE, 59);
									cday.set(Calendar.SECOND, 59);
									Date endday = cday.getTime();
									DetachedCriteria dc = DetachedCriteria.forClass(Cashin.class);
									dc.add(Restrictions.eq("loginname", user.getLoginname()));
									dc.add(Restrictions.and(Restrictions.ge("cashintime", startday),Restrictions.lt("cashintime", endday)));
									dc.setProjection(Projections.sum("fee"));
									List list = getDataDao.findByCriteria(dc);
									/**
									 * 多笔
									 */
									if(list!=null && !list.isEmpty() && null!=list.get(0)){
										Double d = (Double)list.get(0);
										Double dam = maxAmount-d;//系统剩余可返还的手续费
										if(dam<=0){
											fee = 0.00;
										}else{
											fee = amount*0.005;
											if(fee>dam){
												fee = dam;
											}
										}
									}else{
										/**
										 * 只有一笔
										 */
										fee = amount*0.005;
										if(fee>maxAmount){
											fee = maxAmount;
										}
									}
								}else{
									fee = amount*0.005;
								}
								//cashin.setFee(fee);//2016-2-1 TODO 取消赠送0.5%
								//amount = amount+fee;
								proposal.setPno(pno);
								proposal.setProposer("system");
								proposal.setCreateTime(DateUtil.now());
								proposal.setType(502);
								proposal.setQuickly(user.getLevel());
								proposal.setLoginname(user.getLoginname());
								proposal.setAmount(amount);
								proposal.setAgent(user.getAgent());
								proposal.setFlag(2);
								proposal.setWhereisfrom("system");
								proposal.setRemark("网银转账");
								proposal.setBankaccount(acceptName);
								proposal.setSaveway("网银");
								
								getDataDao.save(cashin);
								getDataDao.save(proposal);
								getDataDao.generateTasks(pno, "system");
								getDataDao.changeCreditBySql(user, proposal.getAmount(), "CASHIN", pno, "");
								order.setArriveTime(deposit.getPayTime());
								order.setBankcard(deposit.getAcceptNo());
								order.setStatus("1");  // 已到帐
								order.setTransferID(deposit.getTransID());
								getDataDao.update(order);
								
								// 设置用户 isCashin字段
								if (user.getIsCashin().intValue() == Constants.FLAG_FALSE.intValue()) {
									getDataDao.updateUserIsCashInSql(user.getLoginname());
								}
								
								//改变我方银行的金额
								getDataDao.changeAmountByNameSql(proposal.getBankaccount(), proposal.getAmount()/*-fee*/,pno);
								getDataDao.excuteTask(pno, "system", "");
								getDataDao.insertOperationLog("system", "EXCUTE", "ip:" + "" + ";pno:" + pno);

								log.info("本次额度验证存款处理的时间是："+new Date());
								
								try {
									// 根据客户定制的服务，决定是否发送邮件和短信
									String service = user.getAddress();
									if (service != null && service.indexOf("9") != -1) {// 发短信
										String smsmsg = notifyService.sendSMSByProposal(proposal);
										log.info("processValiteDeposit send SMS:" + smsmsg);
									}
								} catch (Exception e) {
									log.error("processValiteDeposit发送短信异常:",e);
								}
							} catch (Exception e) {
								e.printStackTrace();
								throw new GenericDfhRuntimeException(e.getMessage());
							}
						}	
					}else{
						log.info("额度验证存款ID：" + item.getTransID() + "，无法匹配到玩家");
						item.setStatus(2);  //无法匹配
						getDataDao.update(item);
					}
				}else{
					log.info("额度验证存款ID：" + item.getTransID() + ", 金额：" + item.getAmount() +", 无法匹配到有效的存款验证订单");
					item.setStatus(2);
					getDataDao.update(item);
				}
			}
		}else{
			log.info("本次额度验证存款处理的记录数是：0，处理时间是：" + new Date());
		}
	}
	
	
	
	/*****
	 *  微信额度验证
	 */
	@Override
	public void processWeixinDeposit(DepositWechat dw) {
		//微信额度验证
		//DepositWechat dw = getDataDao.getValidateWechatDeposit(); 
		if(dw != null){
			log.info("本次微信存款额度验证处理的记录数是：1，处理时间是："+new Date());  
			try {
				DetachedCriteria c = DetachedCriteria.forClass(PayOrderValidation.class);
				c.add(Restrictions.eq("amount", dw.getAmount()));
				c.add(Restrictions.eq("status", "0"));    //未到账状态
				List<PayOrderValidation> orders = getDataDao.findByCriteria(c);
				Users user = null;
				PayOrderValidation order = null;
				if(orders != null && orders.size()==1){
					order = orders.get(0);
					c = DetachedCriteria.forClass(Users.class);
					c.add(Restrictions.eq("loginname", order.getUserName()));
					List<Users> users = getDataDao.findByCriteria(c);
					if(users != null && users.size()==1) user = users.get(0);
					if(user != null){
						DepositWechat dWechat = (DepositWechat)getDataDao.get(DepositWechat.class, dw.getBillno(), LockMode.UPGRADE);
						if(dWechat.getState().intValue()==0){
							Payorder payorder=new Payorder();
							payorder.setBillno(dWechat.getBillno());
							payorder.setPayPlatform("wechat");
							payorder.setMsg("转入:"+dWechat.getWechat());
							payorder.setFlag(0);
							payorder.setNewaccount(Constants.FLAG_FALSE);
							payorder.setLoginname(user.getLoginname());
							payorder.setAliasName(user.getAccountName());
							payorder.setMoney(dWechat.getAmount());
							payorder.setPhone(user.getPhone());
							payorder.setEmail(user.getEmail());
							payorder.setCreateTime(DateUtil.now());
							save(payorder);
							getDataDao.changeCredit(user, dWechat.getAmount(), CreditChangeType.NETPAY.getCode(), dWechat.getBillno(), "referenceNo:" + dWechat.getBillno() + ";" + "转入:"+dWechat.getWechat()+";说明:处理auto");
							order.setArriveTime(DateUtil.now());
							order.setBankcard(dWechat.getWechat());
							order.setStatus("1");  // 已到帐
							order.setRemark(dWechat.getBillno());
							getDataDao.update(order);
							// 设置用户 isCashin字段
							if (user.getIsCashin().intValue() == Constants.FLAG_FALSE.intValue()) {
								getDataDao.updateUserIsCashInSql(user.getLoginname());
							}
							log.info("getWechat:"+dWechat.getWechat()+"--Amount:"+dWechat.getAmount()+"---getbillno-"+dWechat.getBillno());
							getDataDao.changeWechatAmountOnline(dWechat.getWechat(), dWechat.getAmount(), dWechat.getBillno());
							getDataDao.insertOperationLog("system", "EXCUTE", "ip:" + "" + ";(wechat单号:"+dWechat.getBillno()+" 金额："+dWechat.getAmount()+")");
							
							dWechat.setDealtime(DateUtil.now());
							dWechat.setState(1);
							dWechat.setRemark("说明:处理auto");
							dWechat.setUsername(user.getLoginname());
							getDataDao.update(dWechat);
							
							log.info("本次微信额度验证存款处理完成："+new Date());
							
							try {
								// 根据客户定制的服务，决定是否发送邮件和短信
								String service = user.getAddress();
								if (service != null && service.indexOf("9") != -1) {// 发短信
									String smsmsg = notifyService.sendSMSByPayorder(payorder);
									log.info("微信额度验证 send SMS:" + smsmsg);
								}
							} catch (Exception e) {
								log.error("微信额度验证发送短信异常:",e);
							}
						}
					}else{
						log.info("微信额度验证存款单号：" + dw.getBillno() + "，无法匹配到玩家");
						dw.setState(2);
						dw.setRemark("autoU");
						getDataDao.update(dw);
					}
				}else{
					log.info("微信额度验证存款单号：" + dw.getBillno() + ", 金额：" + dw.getAmount() +", 无法匹配到有效的存款验证订单");
					dw.setState(2);
					dw.setRemark("autoX");
					getDataDao.update(dw);
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new GenericDfhRuntimeException(e.getMessage());
			}
		} 
	}

	@Override
	public void discardOrder() {
		try {
			int count = getDataDao.discardValidateAmountOrder();
			log.info("处理过期的额度验证存款订单。设置过期订单 " + count + " 条");
		} catch (Exception e) {
			log.info("处理过期的额度验证存款订单异常：" + e.getMessage());
		}
	}
	
	@Override
	public boolean updateJCData(List<JCProfitData> jclist, String executeTime) {
		boolean ub = false;
		List<String> playlist = getDataDao.queryJCDataByDate(executeTime);
		List<JCProfitData> insList = new ArrayList<JCProfitData>();
		
		for (JCProfitData pd : jclist) {
			if (playlist.contains(pd.getPlayerName())){
				ub = getDataDao.updateJCData(pd);
				if (!ub){
					return false;
				}
				continue;
			} else {
				insList.add(pd);
			}
		}
		return getDataDao.insertJCData(insList);
	}
	
	public String getAgSlotLastTime(String platform){
		return  getDataDao.getAgSlotLastTime(platform);
	}
	
	public void processInsertAgSlotNewData(Map<String,AgDataRecord>data,Date lastTime,String platformType){
		 getDataDao.processNewAgSlotProfit(data,lastTime,platformType);
	}

	public void saveOrUpdateEBetAppData(List<PlatformData> profits){
		for(PlatformData profit : profits) {
			getDataDao.saveOrUpdateEBetAppProfit(profit);
		}
	}

	//一次处理所有未处理的支付宝订单
	@Override
	public void dealAlipayData(AlipayTransfers alipayTransfers){
		if(alipayTransfers.getStatus() != 0){
			log.info(alipayTransfers.getTransferId()+"已经在处理。");
			return ;
		}
		Double maxAmount = 1000.0;
		log.info("正在处理...");
		String notes = alipayTransfers.getNotes();// 取得附言信息
		double amount = alipayTransfers.getAmount();// 取得转账金额
		String acceptName = alipayTransfers.getAcceptName();
		Double fee = alipayTransfers.getFee();// 取得转账手续费
		String randomStr = notes;
		if (notes != null && !"".equals(notes)) {// 如果附言不为空
			notes = StringUtil.trim(notes);
			// 附言模式
			if (alipayTransfers.getPaytype() == 1) {
				DepositOrder depositOrder = (DepositOrder) getDataDao.get(DepositOrder.class, notes);
				if (null == depositOrder) {
					log.info("附言：" + notes + "没有和存款订单匹配上");
					alipayTransfers.setStatus(2);
					getDataDao.update(alipayTransfers);
					return;
				}
				if (depositOrder.getStatus() == 1 || !depositOrder.getBankname().equals("支付宝") || (!depositOrder.getBankno().equals(alipayTransfers.getAcceptNo()))
						|| (!depositOrder.getAccountname().equals(alipayTransfers.getAcceptName()))) {
					log.info("附言：" + notes + "没有和存款订单匹配上");
					log.info("depositOrder" + depositOrder.getBankno() + "   " + depositOrder.getAccountname() + " " + depositOrder.getStatus());
					log.info("icbcTransfers" + alipayTransfers.getAcceptNo() + "   " + alipayTransfers.getAcceptName());
					alipayTransfers.setStatus(2);
					getDataDao.update(alipayTransfers);
					return;
				} else {
					depositOrder.setStatus(1);
					depositOrder.setUpdatetime(new Date());
					getDataDao.update(depositOrder);
				}
				notes = depositOrder.getLoginname();
			} else if (alipayTransfers.getPaytype() == 2) {
				// 二维码扫描模式
				AlipayAccount alipayAccount = getAlipayAccount(notes, 0);
				if (null != alipayAccount) {
					notes = alipayAccount.getLoginname();
				} else {
					log.info("二维码扫描支付：" + notes + "没有未禁用的绑定记录");
					alipayTransfers.setStatus(2);
					getDataDao.update(alipayTransfers);
					return;
				}
			}

			DetachedCriteria c = DetachedCriteria.forClass(Users.class);
			c.add(Restrictions.eq("loginname", notes));
			List<Users> users = getDataDao.findByCriteria(c);
			Users user = null;
			if (users != null && users.size() == 1) {
				user = users.get(0);
				if (user != null) {
					log.info(user.getLoginname() + " add Cashin proposal,the amount is " + amount);
					AlipayTransfers alipayTransfer = (AlipayTransfers) getDataDao.get(AlipayTransfers.class, alipayTransfers.getTransferId());
					if (alipayTransfer.getStatus() == 0) {
						alipayTransfer.setStatus(1);
						alipayTransfer.setNotes(randomStr + "  " + user.getLoginname());
						alipayTransfer.setTimecha(DateUtil.getTimecha(new Timestamp(alipayTransfer.getPayDate().getTime()), new Timestamp(alipayTransfer.getDate().getTime())));
						alipayTransfer.setOvertime(DateUtil.getOvertime(new Timestamp(alipayTransfer.getPayDate().getTime()), new Timestamp(alipayTransfer.getDate().getTime())));
						getDataDao.update(alipayTransfer);
						/**
						 * update cashin
						 */
						Proposal proposal = new Proposal();
						try {
//									String pno = getDataDao.generateProposalAlipayPno(ProposalType.CASHIN.getCode().toString());
							String pno = new Date().getTime()+RandomStringUtils.randomNumeric(4).toString() ;
							if (fee == null)
								fee = 0.0;

							Cashin cashin = new Cashin(pno, user.getRole(), user.getLoginname(), StringUtil.trim(user.getAccountName()), amount, "XXXXXXXXXXXXXXXX", "支付宝", "");
							cashin.setCashintime(DateUtil.now());

							/************* 存款红利0.005 *******************/
							if (user.getLevel() > 100) {
								/**
								 * 限制手续费最高1000元
								 */
								Calendar cday = Calendar.getInstance();
								cday.set(Calendar.HOUR_OF_DAY, 0);
								cday.set(Calendar.MINUTE, 0);
								cday.set(Calendar.SECOND, 0);
								Date startday = cday.getTime();
								cday.set(Calendar.HOUR_OF_DAY, 23);
								cday.set(Calendar.MINUTE, 59);
								cday.set(Calendar.SECOND, 59);
								Date endday = cday.getTime();
								DetachedCriteria dc = DetachedCriteria.forClass(Cashin.class);
								dc.add(Restrictions.eq("loginname", user.getLoginname()));
								dc.add(Restrictions.and(Restrictions.ge("cashintime", startday), Restrictions.lt("cashintime", endday)));
								dc.setProjection(Projections.sum("fee"));
								List list = getDataDao.findByCriteria(dc);
								/**
								 * 多笔
								 */
								if (list != null && !list.isEmpty() && null != list.get(0)) {
									Double d = (Double) list.get(0);
									Double dam = maxAmount - d;// 系统剩余可返还的手续费
									if (dam <= 0) {
										fee = 0.00;
									} else {
										fee = amount * 0.005;
										if (fee > dam) {
											fee = dam;
										}
									}
								} else {
									/**
									 * 只有一笔
									 */
									fee = amount * 0.005;
									if (fee > maxAmount) {
										fee = maxAmount;
									}
								}
							} else {
								fee = amount * 0.005;
							}
							System.out.println(fee + "＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝");
							/*******************************************/
							// cashin.setFee(fee);
							// amount=amount+fee;
							proposal.setPno(pno);
							proposal.setProposer("system");
							proposal.setCreateTime(DateUtil.now());
							proposal.setType(502);
							proposal.setQuickly(user.getLevel());
							proposal.setLoginname(user.getLoginname());
							proposal.setAmount(amount);
							proposal.setAgent(user.getAgent());
							proposal.setFlag(2);
							proposal.setWhereisfrom("system");
							proposal.setRemark("网银转账");
							proposal.setBankaccount(acceptName);
							proposal.setSaveway("网银");

							getDataDao.save(cashin);
							getDataDao.save(proposal);
							getDataDao.generateTasks(pno, "system");
							getDataDao.changeCreditBySql(user, proposal.getAmount(), "CASHIN", pno, "");

							// 设置用户 isCashin字段
							if (user.getIsCashin().intValue() == Constants.FLAG_FALSE.intValue()) {
								getDataDao.updateUserIsCashInSql(user.getLoginname());
							}

							// 改变我方银行的金额
							// getDataDao.changeAmountByNameSql(proposal.getBankaccount(),
							// proposal.getAmount()-fee,pno);
							getDataDao.changeAmountByNameSql(proposal.getBankaccount(), proposal.getAmount(), pno);
							getDataDao.excuteTask(pno, "system", "");
							getDataDao.insertOperationLog("system", "EXCUTE", "ip:" + "" + ";pno:" + pno);

							log.info("本次Alipay处理的时间是：" + new Date());

							try {
								// 根据客户定制的服务，决定是否发送邮件和短信
								String service = user.getAddress();
								if (service != null && service.indexOf("9") != -1) {// 发短信
									String smsmsg = notifyService.sendSMSByProposal(proposal);
									log.info("dealAlipayData send SMS:" + smsmsg);
								}
							} catch (Exception e) {
								log.error("dealAlipayData发送短信异常:",e);
							}
						} catch (Exception e) {
							e.printStackTrace();
							throw new GenericDfhRuntimeException(e.getMessage());
						}
					}
				} else {
					log.info("id为：" + notes + "，无此用户");
					alipayTransfers.setStatus(2);
					getDataDao.update(alipayTransfers);
				}
			} else {
				log.info("id为：" + notes + "，无此用户");
				alipayTransfers.setStatus(2);
				getDataDao.update(alipayTransfers);
			}
		} else {
			// 存款附言里面无内容或者整型溢出
			alipayTransfers.setStatus(2);
			getDataDao.update(alipayTransfers);
		}
	}
	
	/**
	 * 获取当日可用优惠金额(日优惠限额500)
	 * @param loginname
	 * @return
	 * */
	private Double getAvailable(String loginname) throws Exception {
		
		if(StringUtils.isBlank(loginname)){
			throw new Exception("获取当日可用优惠金额(日优惠限额500)异常，loginname为空");
		}
		
		Double used = 0.0;
		Map<String, Object> params = new HashMap<String, Object>();
		String sqlStr = "select sum(fee) from cashin where loginname=:loginname and cashintime>=:startTimeDeposit and cashintime<=:endTimeDeposit";
		Date s = DateUtil.ntStart();
		Date d = DateUtil.ntEnd();
		params.put("loginname", loginname);
//		params.put("corpBankName", "招商银行");   //银行名称
		params.put("startTimeDeposit", s);
		params.put("endTimeDeposit", d);
		used =getDataDao.getDoubleValue(sqlStr, params);
		Double available = Arith.round(Arith.sub(500.0, used), 2);
		log.info(loginname + "已使用优惠金额：>>>" + used + "可用金额：>>>" + available);
		return available;
	}
	

	/**
	 * 秒存 定时处理
	 */
	@Override
	public void discardOrderMc() {
		try {
			int count = getDataDao.discardValidateAmountOrderMc();
			int countQuota = getDataDao.discardWxZzMcQuota();
			int countDepostiOrder = getDataDao.discardWxZzMcOrder();
			int countTlyDepostiOrder = getDataDao.discardTlyMcOrder(); 

			log.info("-------->处理 微信 | 支付宝 | 网银 | 通联 | 过期的存款订单。设置过期订单 " + count + " 条");
		} catch (Exception e) {
			log.info("-------->处理 微信 | 支付宝 | 网银 | 通联 | 过期的存款订单异常：" + e.getMessage());
		}
	}

	public void firstTime(Connection con, Date currentDate) {

		Calendar cal = Calendar.getInstance();

		cal.add(Calendar.MONTH, -4);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));

		Date d1 = cal.getTime();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		String beforeEndTime = sdf.format(d1) + " 23:59:59";

		cal.add(Calendar.DATE, 1);
		Date d2 = cal.getTime();

		String afterStartTime = sdf.format(d2) + " 00:00:00";

		String afterEndTime = sdf.format(currentDate) + " 23:59:59";

		CallableStatement call = null;

		try {

			call = con.prepareCall("{call proc_gather_first(?,?,?,?)}");

			call.setString(1, beforeEndTime);
			call.setString(2, afterStartTime);
			call.setString(3, afterEndTime);
			call.registerOutParameter(4, Types.INTEGER);

			call.execute();

			int num = call.getInt(4);

			log.info("firstTime方法调用存储过程proc_gather_first，参数为：" + beforeEndTime + "，" + afterStartTime + "，" + afterEndTime + "，执行结果：" + num);

			String month = beforeEndTime.split("-")[0] + beforeEndTime.split("-")[1];

			DataGatherMonth dataGatherMonth = new DataGatherMonth();
			dataGatherMonth.setMonth(month);
			dataGatherMonth.setCreateTime(currentDate);

			getDataDao.save(dataGatherMonth);
		} catch (Exception e) {

			e.printStackTrace();
		} finally {

			try {

				if (null != call) {

					call.close();
				}
			} catch (Exception e) {

				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings("rawtypes")
	public void detailData() {

		Date currentDate = new Date();

		log.info("detailData方法开始执行，执行时间：" + DateUtil.formatDateForStandard(currentDate));

		Boolean flag = false;
		String startTime = "";
		String endTime = "";

		Connection con = null;
		CallableStatement call1 = null;
		CallableStatement call2 = null;

		try {

			con = getConnection();
			con.setAutoCommit(false);

			String monthSql = "select month,create_time from data_gather_month";

			List monthList = getDataDao.list(monthSql, null);

			if (null == monthList || monthList.isEmpty()) {

				firstTime(con, currentDate);
			} else {

				Calendar cal = Calendar.getInstance();
				cal.setTime(currentDate);

				// 获取当前时间的小时和分钟，每当定时器执行的时间为0点0分，需要把昨日的数据更新到数据库中，以保证最新数据
				int hour = cal.get(Calendar.HOUR_OF_DAY);
				int minute = cal.get(Calendar.MINUTE);

				if ((hour == 0 && minute == 0) || ((hour == 2 || hour == 4 || hour == 6 || hour == 8 || hour == 13 || hour == 23) && minute == 10)) {

					cal.add(Calendar.DATE, -1);
					Date d1 = cal.getTime();

					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					String str = sdf.format(d1);

					startTime = str + " 00:00:00";
					endTime = str + " 23:59:59";
				} else {

					cal.set(Calendar.HOUR_OF_DAY, 0);
					cal.set(Calendar.MINUTE, 0);
					cal.set(Calendar.SECOND, 0);

					Date startDate = cal.getTime();

					cal.set(Calendar.HOUR_OF_DAY, 23);
					cal.set(Calendar.MINUTE, 59);
					cal.set(Calendar.SECOND, 59);

					Date endDate = cal.getTime();

					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

					startTime = sdf.format(startDate);
					endTime = sdf.format(endDate);
				}

				call1 = con.prepareCall("{call proc_gather_detail(?,?,?)}");

				call1.setString(1, startTime);
				call1.setString(2, endTime);
				call1.registerOutParameter(3, Types.INTEGER);

				call1.execute();

				int num = call1.getInt(3);

				log.info("detailData方法调用存储过程proc_gather_detail，参数为：" + startTime + "，" + endTime + "，执行结果：" + num);

				cal.add(Calendar.MONTH, -4);
				Date d1 = cal.getTime();

				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
				String month = sdf.format(d1);

				for (int i = 0, len = monthList.size(); i < len; i++) {

					Object[] obj = (Object[]) monthList.get(i);

					if (month.equals(String.valueOf(obj[0]))) {

						flag = true;
						break;
					}
				}

				// 该分支判断用于合并三个月以前的月份数据，只保留最近三个月的数据
				if (!flag) {

					// 得到月初
					cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
					Date strDateFrom = cal.getTime();

					// 得到月末
					cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
					Date strDateTo = cal.getTime();

					sdf = new SimpleDateFormat("yyyy-MM-dd");
					startTime = sdf.format(strDateFrom) + " 00:00:00";
					endTime = sdf.format(strDateTo) + " 23:59:59";

					call2 = con.prepareCall("{call proc_gather_merge(?,?,?)}");

					call2.setString(1, startTime);
					call2.setString(2, endTime);
					call2.registerOutParameter(3, Types.INTEGER);

					call2.execute();

					num = call2.getInt(3);

					log.info("detailData方法调用存储过程proc_gather_merge，参数为：" + startTime + "，" + endTime + "，执行结果：" + num);

					DataGatherMonth dataGatherMonth = new DataGatherMonth();
					dataGatherMonth.setMonth(month);
					dataGatherMonth.setCreateTime(currentDate);

					getDataDao.save(dataGatherMonth);
				}
			}
		} catch (Exception e) {

			e.printStackTrace();
		} finally {

			try {

				con.setAutoCommit(true);

				if (null != call1) {

					call1.close();
				}

				if (null != call2) {

					call2.close();
				}

				if (null != con) {

					con.close();
				}
			} catch (Exception e) {

				e.printStackTrace();
			}
		}

		log.info("detailData方法开始执行，执行时间：" + DateUtil.formatDateForStandard(currentDate) + "，结束时间：" + DateUtil.formatDateForStandard(new Date()));
	}

	public void summaryData() {

		Date currentDate = new Date();

		log.info("summaryData方法开始执行，执行时间：" + DateUtil.formatDateForStandard(currentDate));

		Connection con = null;
		CallableStatement call = null;

		try {

			con = getConnection();
			con.setAutoCommit(false);

			call = con.prepareCall("{call proc_gather_summary(?)}");

			call.registerOutParameter(1, Types.INTEGER);

			call.execute();

			int num = call.getInt(1);

			log.info("summaryData方法调用存储过程proc_gather_summary，执行结果：" + num);
		} catch (Exception e) {

			e.printStackTrace();
		} finally {

			try {

				con.setAutoCommit(true);

				if (null != call) {

					call.close();
				}

				if (null != con) {

					con.close();
				}
			} catch (Exception e) {

				e.printStackTrace();
			}
		}

		log.info("summaryData方法开始执行，执行时间：" + DateUtil.formatDateForStandard(currentDate) + "，结束时间：" + DateUtil.formatDateForStandard(new Date()));
	}

	private static Properties properties = new Properties();

	static {

		try {

			properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("init.properties"));
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	public String getValue(String key) {

		String value = properties.getProperty(key);

		if (StringUtils.isNotBlank(value) && value.indexOf("ENC(") != -1) {

			value = value.replace("ENC(", "").replace(")", "");
		}

		return value;
	}

	public Connection getConnection() {

		Connection con = null;

		try {

			Class.forName("com.mysql.jdbc.Driver");

			String url = SpecialEnvironmentStringPBEConfig.decryptPBEConfig(getValue("datasource.url")) + "&noAccessToProcedureBodies=true";
			String userName = SpecialEnvironmentStringPBEConfig.decryptPBEConfig(getValue("datasource.username"));
			String password = SpecialEnvironmentStringPBEConfig.decryptPBEConfig(getValue("datasource.password"));

			con = DriverManager.getConnection(url, userName, password);
		} catch (Exception e) {

			e.printStackTrace();
		}

		return con;
	}
	
	@Override
	public void collectionChessData() {
		String beforeDate = DateUtil.getchangedDate(-1)+" 00:00:00";
		String sql = "delete from chess_data where  ctime >='"+beforeDate+"'";
		getDataDao.excuteSql(sql, new HashMap());
		
		String sdate = DateUtil.getchangedDate(-1)+" 00:00:00";
		String edate = DateUtil.getchangedDate(-1)+ " 23:59:59";
		Long stime = DateUtil.parseDateForStandard(sdate).getTime()/1000;
		Long etime = DateUtil.parseDateForStandard(edate).getTime()/1000;
		
		int page = 1;
		while (true) {
			List list = ChessUtil.loadrecords(stime, etime, page);
			if(list == null){
				break;
			}else{
				List<ChessData> resultList = new ArrayList<ChessData>();
				for (int i = 0; i < list.size(); i++) {
					Map one = (Map) list.get(i);
					String kind = (String)one.get("kind");
					if(kind.equals("0")||kind.equals("1")||kind.equals("2")||kind.equals("3")){
						continue;
					}
					ChessData data = new ChessData();
					data.setKind(kind);
					data.setLogid((Integer)one.get("logid"));
					data.setCtime(DateUtil.parseDateForStandard((String)one.get("ctime")));
					data.setAcc((String)one.get("acc"));
					data.setSessionid((String)one.get("sessionid"));
					data.setUid((Integer)one.get("uid"));
					data.setPrev((Integer)one.get("prev"));
					data.setLeftamount((Integer)one.get("left"));
					data.setChg((Integer)one.get("chg"));
					data.setRealput((Integer)one.get("realput"));
					data.setAllput((Integer)one.get("allput"));
					resultList.add(data);
				}
				getDataDao.saveOrUpdateAll(resultList);
			}
			page++;
		}
	}
	@Override
	public void fayalogData() {
		String beforeDate = DateUtil.getchangedDate(-1)+" 00:00:00";
		String sql = "delete from fanya_log where  reward_at >='"+beforeDate+"'";
		getDataDao.excuteSql(sql, new HashMap());
		String sdate = DateUtil.getchangedDate(-1)+ " 00:00:00";
		String edate = DateUtil.getchangedDate(-1)+ " 23:59:59";
		int page = 1;
		while (true) {
			List<FanyaLog> list = FanYaUtil.loadrecords(sdate, edate,null, page);
			if(list.isEmpty()){
				break;
			}else{
				getDataDao.saveFanyaLogAll(list);
			}
			page++;
		}

	}

	@Override
	public void processAllPtData(JSONArray arr, Map<String, PTBetVO> tiggerAllMap, Map<String, PTBetVO> progressiveMap, String executeTime) throws ParseException {
		Stopwatch started = Stopwatch.createStarted();
		Date STARTTIME = DateUtils.parse(executeTime + " 00:00:00");
		Date ENDTIME = DateUtils.parse(executeTime + " 23:59:59");
		Date createTime = new Date();
		
		DetachedCriteria dc = DetachedCriteria.forClass(PtDataNew.class);
		dc.add(Restrictions.eq("starttime", STARTTIME));
		Criteria cri =  dc.getExecutableCriteria(getDataDao.getHibernateTemplate().getSessionFactory().getCurrentSession());  
        cri.setMaxResults(1);  
        List list = cri.list();
        if(list != null && list.size() > 0){
        	Query q= getDataDao.getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery("delete from PtDataNew where starttime=?");
        	q.setParameter(0, DateUtils.parse(executeTime + " 00:00:00"));
        	LogUtils.info("删除PtDataNew，日期" + executeTime + " 00:00:00，执行结果： " + q.executeUpdate());
        }
        
		Session session = getDataDao.getHibernateTemplate().getSessionFactory().openSession();
		org.hibernate.Transaction tx = session.beginTransaction();
		int count = 0;
		try {
			for (Object objectData : arr) {
				JSONObject jsonObjDate = JSONObject.fromObject(objectData);
				String PLAYERNAME = jsonObjDate.getString("PLAYERNAME");
				if (PLAYERNAME.startsWith(PtUtil.PALY_START)) {
					String FULLNAME = jsonObjDate.getString("FULLNAME");
					String VIPLEVEL = jsonObjDate.getString("VIPLEVEL");
					String COUNTRY = jsonObjDate.getString("COUNTRY");
					String GAMES = jsonObjDate.getString("GAMES");
					String CURRENCYCODE = jsonObjDate.getString("CURRENCYCODE");
					String BETS = jsonObjDate.getString("BETS");
					String WINS = jsonObjDate.getString("WINS");
					String INCOME = jsonObjDate.getString("INCOME");
					String RNUM = "1";

					Double BETS_TIGER = 0.0;
					Double WINS_TIGER = 0.0;
					Double PROGRESSIVE_BETS = 0.0;
					Double PROGRESSIVE_WINS = 0.0;
					Double PROGRESSIVE_FEE = 0.0;
					PTBetVO vo = tiggerAllMap.get(PLAYERNAME);
					if(vo != null){
						BETS_TIGER = vo.getBets();
						WINS_TIGER = vo.getProfit();
						PROGRESSIVE_BETS = vo.getProgressiveBet();
						PROGRESSIVE_WINS = vo.getProgressviceProfit();
					}
					if(progressiveMap.get(PLAYERNAME) != null ){
						PROGRESSIVE_FEE = progressiveMap.get(PLAYERNAME).getProgressiveFee();
					}
					getDataDao.dealAllPtData(PLAYERNAME, FULLNAME, VIPLEVEL, COUNTRY, GAMES, CURRENCYCODE, BETS, WINS, INCOME,
							RNUM, executeTime, BETS_TIGER, WINS_TIGER, PROGRESSIVE_BETS, PROGRESSIVE_WINS,
							PROGRESSIVE_FEE, createTime, STARTTIME, ENDTIME);
				} else {
					LogUtils.warn("pt抓取总投注数据，PLAYERNAME前缀不正确", PLAYERNAME);//[pt抓取总投注数据，PLAYERNAME前缀不正确, DY8DIAN123]
				}
				if (count % 200 == 0) {// 20,与JDBC批量设置相同
					// 将本批插入的对象立即写入数据库并释放内存
					LogUtils.info("批量抓取PT数据flush入库 ", "已执行累计数目： " + count, "累计时间: " + started);
					session.flush();
					session.clear();
				}
				count++;
			}
			session.flush();
			session.clear();
			tx.commit();
			LogUtils.info("批量抓取PT数据入库完成", "执行总数目： " + count, "总费时: " + started);
		} catch (Exception e) {
			if (e instanceof RuntimeException) {
				throw new RuntimeException(e);
			} else {
				LogUtils.error("批量抓取PT数据入库失败");
				e.printStackTrace();
			}
			// 发生异常，事务回滚
			if (tx.isActive()) {
				tx.rollback();
			}
		} finally {
			// session关闭操作
			if (session != null) {
				session.close();
			}
		}
	}


	@Override
	public void collectionSbaData() {
		// 先删除错误数据
		/*String beforeDate = DateUtil.getchangedDate(-1)+" 00:00:00";
		String sql = "delete from api_call_record where  createTime >='"+beforeDate+"'";
		getDataDao.excuteSql(sql, new HashMap());*/
		
		/*String sql1 = "delete from tablename where WinLostDateTime >='"+beforeDate+"'";
		String[] strArr = { "sba_data_dy"};
		List list = Arrays.asList(strArr);
		for (int i = 0; i < list.size(); i++) {
			String exeSql = sql1.replace("tablename", list.get(i)+"");
			getDataDao.excuteSql(exeSql, new HashMap());
		}*/
		//重新执行100次，去抓取沙巴投注额()
		long startTime = System.currentTimeMillis();
		for (int i = 0; i < 100; i++) {
			Boolean flag = execute();
			if(flag){
				break;
			}
	        try {
	            Thread.sleep(10000);
	        } catch (InterruptedException e) {
	            e.printStackTrace(); 
	        }
		}
		long endTime = System.currentTimeMillis();
		float seconds = (endTime - startTime) / 1000F;
		log.info("执行耗时："+Float.toString(seconds));
	}
	
	public Long getVersionKey() {
		String sql = "select version_key from api_call_record order by createtime desc ";
		List list = getDataDao.list(sql, new HashMap<String, Object>());
		if(list !=null && !list.isEmpty()&& list.get(0) !=null){
			String version_key =  (String)list.get(0);
			if(version_key != null && !version_key.equals("")){
				return Long.parseLong(version_key);
			}
		}
		return null;
	}
	
	public Boolean execute() {
		String result = "";
		try {
			Long version_key = getVersionKey();
			Map map = null;
			if (version_key != null) {
				if(version_key.longValue() != 0){
					map = ShaBaUtils.GetSportBetLog(version_key);
					if(map != null){
						result = processData(version_key,map);
					}else{
						result = "finish"; 
					}
				}else{
					//如果抓到最后一笔，记录调用次数
					map = new HashMap();
					map.put("LastVersionKey", version_key);
					map.put("BetDetails", new ArrayList());
					result = processData(version_key,map);
				}
			}else{
				map = ShaBaUtils.GetSportBetLog(0L);
				if(map != null){
					result = processData(version_key,map);
				}
			}
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}
		if("finish".equals(result)){
			return Boolean.TRUE;
		}else{
			return Boolean.FALSE;
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private String processData(Long version_key,Map map) {
		String last_version_key =  map.get("LastVersionKey")+"";
		List<SBAData> list = (List<SBAData>) map.get("BetDetails");
		if(list !=null && !list.isEmpty()){
			//List relist = createVos(list);
			getDataDao.saveOrUpdateAll(list);
		}
		if("0".equals(last_version_key) || last_version_key == null || "null".equals(last_version_key)){
			saveRecord(version_key+"");
		}else{
			saveRecord(last_version_key);
		}
		if(list ==null || list.isEmpty()){
			return "finish";
		}
		return null;
	}
	private void saveRecord(String last_version_key){
		String querysql = "select version_key from api_call_record where version_key =:version_key";
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("version_key", last_version_key);
		List list1 = getDataDao.excuteQuerySql(querysql, param);
		if(list1 !=null && !list1.isEmpty()){
			String updatesql = "update api_call_record set frequency = frequency + 1,updatetime = now() where version_key =:version_key";
			getDataDao.excuteSql(updatesql, param);
		}else{
			if(StringUtil.isNotBlank(last_version_key) && !"null".equals(last_version_key)){
				String insertsql = "insert into api_call_record values(:version_key,1,now(),now()) ";
				getDataDao.excuteSql(insertsql, param);
			}
		}
		
	}


	@Override
	public void collection761Data(String executeTime) throws Exception {
		// 先删除错误数据
		String beforeDate = executeTime+" 00:00:00";
		String sql = "delete from chess_data where  ctime >='"+beforeDate+"' and ctime <='"+executeTime+" 23:59:59'";
		getDataDao.excuteSql(sql, new HashMap());
		
		SimpleDateFormat standardFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date sDate = standardFmt.parse(executeTime+" 00:00:00");
		Date eDate = standardFmt.parse(executeTime+" 23:59:59");
		
		Long stime = sDate.getTime()/1000;
		Long etime = eDate.getTime()/1000;
		
		int page = 1;
		while(true){
			List list = ChessUtil.loadrecords(stime, etime, page);
			if(list == null){
				break;
			}else{
				List<ChessData> resultList = new ArrayList<ChessData>();
				for (int i = 0; i < list.size(); i++) {
					Map map = (Map) list.get(i);
					String kind = (String)map.get("kind");
					if(kind.equals("1")||kind.equals("2")||kind.equals("3")){
						continue;
					}
					
					ChessData vo = new ChessData();
					vo.setAcc((String)map.get("acc"));
					vo.setLogid((Integer)map.get("logid"));
					String ctime = (String) map.get("ctime");
					vo.setCtime(DateUtil.parseDateForStandard(ctime));
					vo.setSessionid((String)map.get("sessionid"));
					vo.setUid((Integer)map.get("uid"));
					vo.setKind(kind);
					vo.setPrev((Integer)map.get("prev"));
					vo.setLeftamount((Integer)map.get("left"));
					vo.setChg((Integer)map.get("chg"));
					vo.setRealput((Integer)map.get("realput"));
					vo.setAllput((Integer)map.get("allput"));
					resultList.add(vo);
				}
				getDataDao.saveOrUpdateAll(resultList);
			}
			page ++;
		}
	}
	
	@Override
	public void collectionAgData(String platform,String executeTime) throws Exception {
		String startTime = executeTime + " 00:00:00";
		List<AgData> ListAll = new ArrayList<AgData>();
		int page = 1;
		while (true) {
			List<AgData> resultList = new ArrayList<AgData>();
			String endTime = DateUtil.getchangedDateMinStr(10, startTime);
			if("AGIN".equals(platform)){
				List<AgData> list = AGINUtil.getAginorders(startTime, endTime,page);
				if(list.size() > 0 ){
					resultList.addAll(list);
				}
			}
			if("SLOT".equals(platform)){
				List<AgData> list = AGINUtil.getslotorders_ex(startTime, endTime,page);
				if(list.size() > 0 ){
					resultList.addAll(list);
				}
			}
			if("YPMONEY".equals(platform)){
				List<AgData> list = AGINUtil.getyoplayorders_ex(startTime, endTime,page);
				if(list.size() > 0 ){
					resultList.addAll(list);
				}
			}
			if("AGINFISH".equals(platform)){
				startTime = executeTime + " 12:00:00";
				endTime = DateUtil.getMontHreduce(DateUtil.parseDateForStandard(startTime), 1).substring(0,10)+" 11:59:59";
				List<AgData> list = AGINUtil.getscenesofuserreportExt(startTime, endTime,1);
				if(list.size() > 0 ){
					resultList.addAll(list);
					ListAll.addAll(list);
				}
				break;
			}
			ListAll.addAll(resultList);
			if(resultList.size() == 0){
				page = 1;
				startTime = endTime;
				if(endTime.contains("00:00:00")){
					break;
				}
			}else{
				page ++;
			}
		}
		ListAll = new ArrayList<AgData>(new HashSet<>(ListAll));
		getDataDao.saveOrUpdateAll(ListAll);
	}

	@Override
	public void updateFPayOrder() {
		DetachedCriteria dc_fpay = DetachedCriteria.forClass(FPayorder.class);
		dc_fpay.add(Restrictions.eq("flag", 1));
		List<FPayorder> fpayList = this.findByCriteria(dc_fpay);
		
		if(fpayList != null && fpayList.size() > 0){
			log.info("代付需更新条数：" + fpayList.size());
			for (int i = 0; i < fpayList.size(); i++) {
				FPayorder one = fpayList.get(i);
				FlashPayUtil.updateFPayOrder(one.getPno());
			}
		}
	}
}