package dfh.service.implementations;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import dfh.icbc.getdata.dao.IGetdataDao;
import dfh.model.Cashin;
import dfh.model.CmbTransfers;
import dfh.model.DepositOrder;
import dfh.model.Mcquota;
import dfh.model.Proposal;
import dfh.model.Users;
import dfh.service.interfaces.IBankSecDepositService;
import dfh.service.interfaces.NotifyService;
import dfh.utils.Arith;
import dfh.utils.Constants;
import dfh.utils.DateUtil;
import dfh.utils.StringUtil;

public class BankSecDepositServiceImpl extends UniversalServiceImpl implements IBankSecDepositService {
	
	private static Logger log = Logger.getLogger(BankSecDepositServiceImpl.class);
	private IGetdataDao getDataDao;
	private NotifyService notifyService;
	
	
	public IGetdataDao getGetDataDao() {
		return getDataDao;
	}
	public void setGetDataDao(IGetdataDao getDataDao) {
		this.getDataDao = getDataDao;
	}
	public NotifyService getNotifyService() {
		return notifyService;
	}
	public void setNotifyService(NotifyService notifyService) {
		this.notifyService = notifyService;
	}
	
	
	@Override
	public List<CmbTransfers> getTranferInfos() {
		DetachedCriteria dc_cmb = DetachedCriteria.forClass(CmbTransfers.class);
		dc_cmb.add(Restrictions.eq("status", 0));
		List<CmbTransfers> cmbList = getDataDao.findByCriteria(dc_cmb);
		return cmbList;
	}
	@Override
	public void dealProcess(CmbTransfers cmb) throws Exception {
		String uaccountname = cmb.getUaccountname();//获取存款姓名
		String uaccountno = cmb.getUaccountno();//获取存款人卡号
		Double amount = cmb.getAmount();//获取存款金额
		String remark = cmb.getRemark();//获取备注
		String moneyType = cmb.getJylx();//获取转账类型
		String notes = cmb.getNotes().trim();//取得附言信息
		log.info("开始匹配处理----------------------------------》"+uaccountname+"|"+amount+"|"+remark+"|"+moneyType+"|"+notes);
		
		// 处理通联存款记录(如果附言为空，则不做匹配操作)
		if (StringUtils.isNotBlank(moneyType) && moneyType.contains("通联") && StringUtils.isBlank(notes)) {
			notes = "无用户";
		}
		
		String resultRemark = remark == null?"":remark;
		
		if (StringUtils.isNotBlank(moneyType) && moneyType.contains("通联")) {
			moneyType = "通联";
		}
		else if(moneyType.equals("微信二维码收款")){
			moneyType = "微信二维码收款";   
		}
		else if(moneyType.equals("支付宝新版")){
			moneyType = "支付宝新版";
		}
		else if(moneyType.equals("支付宝二维码收款")){
			moneyType = "支付宝二维码收款";   
		}
		else if(moneyType.contains("支付宝")){
			moneyType = "支付宝";
		}
		/*else if(moneyType.contains("微信")) {   
			moneyType = "微信";  
			payType="3";
		}*/
		else if(moneyType.contains("微信转账")){
			moneyType = "微信转账";   
		}
		
		else if(moneyType.contains("云闪付")){
			moneyType = "云闪付";   
		}
		else if(moneyType.contains("银行二维码")){
			moneyType = "银行二维码";   
		}
		else if(moneyType.contains("同略云银行")){
			moneyType = "同略云银行";   
		}
		else {
			moneyType = "网银";
		}
		
		if ((resultRemark.contains("补单") && StringUtils.isNotBlank(notes)) || (moneyType.equals("微信") && !notes.equals("无附言"))) {
			log.info("进入附言匹配模式1----------------------------------》"+uaccountname+"|"+amount+"|"+remark+""+moneyType+"|"+notes);
			DepositOrder depositOrder = (DepositOrder) getDataDao.get(DepositOrder.class, notes);
			if(null == depositOrder || depositOrder.getStatus() == 1){
				log.error("补单失败，存款订单生成或匹配失败" + notes +"");
				cmb.setStatus(2);
				getDataDao.update(cmb);
		 		return ;
			}else{
				Users user = (Users) getDataDao.get(Users.class, depositOrder.getLoginname());
				if(null == user){
					log.error(depositOrder.getLoginname()+"玩家账号不存在");
				}else{
					if(cmb.getStatus()==0){
						log.info(moneyType+"类型|depositOrder："+depositOrder);   
						addMoneyToUser(cmb, user, moneyType , depositOrder);
					}else{
						log.error(cmb.getTransfeId() + "该笔转账已经处理");
					}
				}
			}
		}
		else if(moneyType.equals("通联")){
			log.info("进入通联 玩家账号匹配模式----------------------------------》"+uaccountname+"|"+amount+"|"+remark+""+moneyType+"|"+notes);
			Users user = (Users) getDataDao.get(Users.class, notes);
			if(null == user){
				log.error("匹配失败，玩家账号不存在" + notes +"");
				cmb.setStatus(2);
				getDataDao.update(cmb);
		 		return ;
			}else{
					if(cmb.getStatus()==0){
						addMoneyToUser(cmb, user, moneyType , null);
					}else{
						log.error(cmb.getTransfeId() + "该笔转账已经处理");
					}
				}
		}
		
		else if(moneyType.equals("微信转账")){
			log.info("进入微信转账 玩家账号匹配模式----------------------------------》"+uaccountname+"|"+amount+"|"+remark+""+moneyType+"|"+notes);
			DetachedCriteria dc = DetachedCriteria.forClass(DepositOrder.class);
			dc = dc.add(Restrictions.eq("amount", amount));
			dc = dc.add(Restrictions.eq("status", 0));
			dc = dc.add(Restrictions.eq("type", "4"));
			List<DepositOrder> depOrders = getDataDao.findByCriteria(dc);
			if(null == depOrders || depOrders.size() == 0 || null == depOrders.get(0)){
				log.info("没有找到存款对应的订单  "+uaccountname+"|"+amount+"|"+remark+"");
				cmb.setStatus(2);
				getDataDao.update(cmb);
		 		return ;
			}
			if(depOrders.size() > 1){
				log.info("存在相同未处理额度，暂时不能处理该笔--------------------》"+uaccountname+"|"+amount+"|"+remark+"");
				cmb.setStatus(2);
				getDataDao.update(cmb);
		 		return ;
			}
			DepositOrder order = depOrders.get(0);
	 		
	 		Users user = (Users) getDataDao.get(Users.class, order.getLoginname());
	 		if(null == user){
				log.error(order.getLoginname()+"玩家账号不存在或者被冻结");
			}else{
				if(cmb.getStatus()==0){
					addMoneyToUser(cmb, user, moneyType , order);
					//释放额度值
					closeMcQuots(cmb);
				}else{
					log.error(cmb.getTransfeId() + "该笔转账已经处理");
				}
			}
		}
		
		else if(moneyType.equals("云闪付")){
			log.info("进入云闪付存款匹配  玩家账号匹配模式----------------------------------》"+uaccountname+"|"+amount+"|"+remark+""+moneyType+"|"+notes);
			DetachedCriteria dc = DetachedCriteria.forClass(DepositOrder.class);
			uaccountno = uaccountno.substring(uaccountno.length()-4,uaccountno.length());
			
			dc = dc.add(Restrictions.eq("amount", amount));
			dc = dc.add(Restrictions.eq("ubankno", uaccountno));
			dc = dc.add(Restrictions.eq("status", 0));
			dc = dc.add(Restrictions.eq("type", "5"));
			List<DepositOrder> depOrders = getDataDao.findByCriteria(dc);
			if(null == depOrders || depOrders.size() == 0 || null == depOrders.get(0)){
				log.info("没有找到存款对应的订单  "+uaccountname+"|"+amount+"|"+remark+"");
				cmb.setStatus(2);
				getDataDao.update(cmb);
		 		return ;
			}
			if(depOrders.size() > 1){
				log.info("存在相同未处理额度，暂时不能处理该笔--------------------》"+uaccountname+"|"+amount+"|"+remark+"");
				cmb.setStatus(2);
				getDataDao.update(cmb);
		 		return ;
			}
			DepositOrder order = depOrders.get(0);
	 		
	 		Users user = (Users) getDataDao.get(Users.class, order.getLoginname());
	 		if(null == user){
				log.error(order.getLoginname()+"玩家账号不存在或者被冻结");
			}else{
				if(cmb.getStatus()==0){
					addMoneyToUser(cmb, user, moneyType , order);
					//释放额度值
					closeMcQuots(cmb);
				}else{
					log.error(cmb.getTransfeId() + "该笔转账已经处理");
				}
			}
		 }else if(moneyType.equals("银行二维码")){
				log.info("进入银行二维码存款匹配  玩家账号匹配模式----------------------------------》"+uaccountname+"|"+amount+"|"+remark+""+moneyType+"|"+notes);
				DetachedCriteria dc = DetachedCriteria.forClass(DepositOrder.class);
//				uaccountno = uaccountno.substring(uaccountno.length()-4,uaccountno.length());
				
				dc = dc.add(Restrictions.eq("amount", amount));
//				dc = dc.add(Restrictions.eq("ubankno", uaccountno));
				dc = dc.add(Restrictions.eq("status", 0));
				dc = dc.add(Restrictions.eq("type", "6"));
				List<DepositOrder> depOrders = getDataDao.findByCriteria(dc);
				if(null == depOrders || depOrders.size() == 0 || null == depOrders.get(0)){
					log.info("没有找到存款对应的订单  "+uaccountname+"|"+amount+"|"+remark+"");
					cmb.setStatus(2);
					getDataDao.update(cmb);
			 		return ;
				}
				if(depOrders.size() > 1){
					log.info("存在相同未处理额度，暂时不能处理该笔--------------------》"+uaccountname+"|"+amount+"|"+remark+"");
					cmb.setStatus(2);
					getDataDao.update(cmb);
			 		return ;
				}
				DepositOrder order = depOrders.get(0);
		 		
		 		Users user = (Users) getDataDao.get(Users.class, order.getLoginname());
		 		if(null == user){
					log.error(order.getLoginname()+"玩家账号不存在或者被冻结");
				}else{
					if(cmb.getStatus()==0){
						addMoneyToUser(cmb, user, moneyType , order);
						//释放额度值
						closeMcQuots(cmb);
					}else{
						log.error(cmb.getTransfeId() + "该笔转账已经处理");
					}
				}
		 }else if(moneyType.equals("同略云银行")){
				log.info("进入同略云银行付存款匹配  玩家账号匹配模式----------------------------------》"+uaccountname+"|"+amount+"|"+remark+""+moneyType+"|"+notes);
				DetachedCriteria dc = DetachedCriteria.forClass(DepositOrder.class);				
				dc = dc.add(Restrictions.eq("amount", amount));
				dc = dc.add(Restrictions.eq("status", 0));
				dc = dc.add(Restrictions.eq("type", "7"));
				List<DepositOrder> depOrders = getDataDao.findByCriteria(dc);
				if(null == depOrders || depOrders.size() == 0 || null == depOrders.get(0)){
					log.info("没有找到存款对应的订单  "+uaccountname+"|"+amount+"|"+remark+"");
					cmb.setStatus(2);
					getDataDao.update(cmb);
			 		return ;
				}
				if(depOrders.size() > 1){
					log.info("存在相同未处理额度，暂时不能处理该笔--------------------》"+uaccountname+"|"+amount+"|"+remark+"");
					cmb.setStatus(2);
					getDataDao.update(cmb);
			 		return ;
				}
				DepositOrder order = depOrders.get(0);
		 		
		 		Users user = (Users) getDataDao.get(Users.class, order.getLoginname());
		 		if(null == user){
					log.error(order.getLoginname()+"玩家账号不存在或者被冻结");
				}else{
					if(cmb.getStatus()==0){
						addMoneyToUser(cmb, user, moneyType , order);
						//释放额度值
						closeMcQuots(cmb);
					}else{
						log.error(cmb.getTransfeId() + "该笔转账已经处理");
					}
				}
			
        	}else if(moneyType.equals("微信二维码收款")){
				log.info("进入微信二维码存款匹配  玩家账号匹配模式----------------------------------》"+uaccountname+"|"+amount+"|"+remark+""+moneyType+"|"+notes);
				DetachedCriteria dc = DetachedCriteria.forClass(DepositOrder.class);
//				uaccountno = uaccountno.substring(uaccountno.length()-4,uaccountno.length());
				
				dc = dc.add(Restrictions.eq("amount", amount));
				dc = dc.add(Restrictions.eq("depositId", notes.trim()));
				dc = dc.add(Restrictions.eq("status", 0));
				dc = dc.add(Restrictions.eq("type", "8"));
				List<DepositOrder> depOrders = getDataDao.findByCriteria(dc);
				if(null == depOrders || depOrders.size() == 0 || null == depOrders.get(0)){
					log.info("没有找到存款对应的订单  "+uaccountname+"|"+amount+"|"+remark+"");
					cmb.setStatus(2);
					getDataDao.update(cmb);
			 		return ;
				}
				if(depOrders.size() > 1){
					log.info("存在相同未处理额度，暂时不能处理该笔--------------------》"+uaccountname+"|"+amount+"|"+remark+"");
					cmb.setStatus(2);
					getDataDao.update(cmb);
			 		return ;
				}
				DepositOrder order = depOrders.get(0);
		 		
		 		Users user = (Users) getDataDao.get(Users.class, order.getLoginname());
		 		if(null == user){
					log.error(order.getLoginname()+"玩家账号不存在或者被冻结");
				}else{
					if(cmb.getStatus()==0){
						addMoneyToUser(cmb, user, moneyType , order);
						//释放额度值
						closeMcQuots(cmb);
					}else{
						log.error(cmb.getTransfeId() + "该笔转账已经处理");
					}
				}
			}else if(moneyType.equals("支付宝二维码收款")){
				log.info("进入支付宝二维码存款匹配  玩家账号匹配模式----------------------------------》"+uaccountname+"|"+amount+"|"+remark+""+moneyType+"|"+notes);
				DetachedCriteria dc = DetachedCriteria.forClass(DepositOrder.class);
//				uaccountno = uaccountno.substring(uaccountno.length()-4,uaccountno.length());
				
				dc = dc.add(Restrictions.eq("amount", amount));
				dc = dc.add(Restrictions.eq("depositId", notes.trim()));
				dc = dc.add(Restrictions.eq("status", 0));
				dc = dc.add(Restrictions.eq("type", "9"));
				List<DepositOrder> depOrders = getDataDao.findByCriteria(dc);
				if(null == depOrders || depOrders.size() == 0 || null == depOrders.get(0)){
					log.info("没有找到存款对应的订单  "+uaccountname+"|"+amount+"|"+remark+"");
					cmb.setStatus(2);
					getDataDao.update(cmb);
			 		return ;
				}
				if(depOrders.size() > 1){
					log.info("存在相同未处理额度，暂时不能处理该笔--------------------》"+uaccountname+"|"+amount+"|"+remark+"");
					cmb.setStatus(2);
					getDataDao.update(cmb);
			 		return ;
				}
				DepositOrder order = depOrders.get(0);
		 		
		 		Users user = (Users) getDataDao.get(Users.class, order.getLoginname());
		 		if(null == user){
					log.error(order.getLoginname()+"玩家账号不存在或者被冻结");
				}else{
					if(cmb.getStatus()==0){
						addMoneyToUser(cmb, user, moneyType , order);
						//释放额度值
						closeMcQuots(cmb);
					}else{
						log.error(cmb.getTransfeId() + "该笔转账已经处理");
					}
				}
		}else{
			  log.info("uaccountname:"+uaccountname+"notes："+cmb.getNotes());
			if(uaccountname==null || uaccountname.equals("")){
				log.info("uaccountname为空不匹配：  "+uaccountname+"|"+amount+"|"+remark+"");
				cmb.setStatus(2);
				getDataDao.update(cmb);
		 		return ;
			}
			DetachedCriteria dc = DetachedCriteria.forClass(DepositOrder.class);
			dc = dc.add(Restrictions.eq("uaccountname", uaccountname));
			dc = dc.add(Restrictions.eq("amount", amount));
			dc = dc.add(Restrictions.eq("status", 0));
			
			List<DepositOrder> depOrders = getDataDao.findByCriteria(dc);
			if(null == depOrders || depOrders.size() == 0 || null == depOrders.get(0)){
				log.info("没有找到存款对应的订单  "+uaccountname+"|"+amount+"|"+remark+"");
				cmb.setStatus(2);
				getDataDao.update(cmb);
		 		return ;
			}
			if(depOrders.size() > 1){
				log.info("可能存在同姓名--------------------》"+uaccountname+"|"+amount+"|"+remark+"");
				cmb.setStatus(2);
				getDataDao.update(cmb);
		 		return ;
			}
			DepositOrder order = depOrders.get(0);
	 		
	 		Users user = (Users) getDataDao.get(Users.class, order.getLoginname());
	 		if(null == user){
				log.error(order.getLoginname()+"玩家账号不存在或者被冻结");
			}else{
				if(cmb.getStatus()==0){
					addMoneyToUser(cmb, user, moneyType , order);
				}else{
					log.error(cmb.getTransfeId() + "该笔转账已经处理");
				}
			}
		}
	}

	//释放额度值
	public void closeMcQuots(CmbTransfers cmb){
		DetachedCriteria c = DetachedCriteria.forClass(Mcquota.class);
		c.add(Restrictions.eq("amount", cmb.getAmount()));
		c.add(Restrictions.eq("status", 1));
		List<Mcquota> mcquotaList = getDataDao.findByCriteria(c);
		if(mcquotaList != null && mcquotaList.size()>0){
			Mcquota quota = mcquotaList.get(0);
			quota.setStatus(0);
			getDataDao.update(quota);
			log.info(cmb.getTransfeId()+"cmbID "+cmb.getAmount()+" 额度释放成功！");
		}
	}
	
	
	public void addMoneyToUser(CmbTransfers cmb , Users user , String moneyType , DepositOrder order) throws Exception{
		String pno = getDataDao.generateProposalCmbPno("502");
		Double depAmount = cmb.getAmount();
		
		Cashin cashin =  new Cashin(pno,user.getRole(), user.getLoginname(),StringUtil.trim(user.getAccountName()),depAmount,"XXXXXXXXXXXXXXXX",cmb.getAcceptName(),"");
		
		Double available = this.getAvailable(user.getLoginname());
		Double depositfee = depAmount*0.015>available ? available:depAmount*0.015;
		
		if(order == null || moneyType.equals("通联")){
			depositfee = 0.0;
		}
		
		
		
		cashin.setFee(depositfee);
		Timestamp now = DateUtil.now();
		cashin.setCashintime(now); 
		
		Proposal proposal=new Proposal();
		proposal.setPno(pno);
		proposal.setProposer("system");
		proposal.setCreateTime(now);
		proposal.setType(502);
		proposal.setQuickly(user.getLevel());
		proposal.setLoginname(user.getLoginname());
		proposal.setAmount(depAmount);
		proposal.setAgent(user.getAgent());
		proposal.setFlag(2);
		proposal.setExecuteTime(now);
		proposal.setWhereisfrom("system");
		proposal.setRemark(moneyType+"转账");
		proposal.setBankaccount(cmb.getAcceptName());
		proposal.setSaveway(moneyType);
		proposal.setGifTamount(depositfee);
		Double amount = 0.00;
		if(order == null || moneyType.equals("通联")){
			amount = proposal.getAmount()*0.9965;
		}
		else{  
			amount = proposal.getAmount();
		}
		
		log.info("通联扣除手续费："+proposal.getPno()+"|"+amount+"|"+moneyType+"|"+order);    
		
		
		int rows  = getDataDao.changeAmountByIdSql(proposal.getBankaccount(), amount,pno);
	
		
		if(rows == 1){
			getDataDao.changeCreditForSecDeposit(user, amount+depositfee, "CASHIN", pno, "");
			
			
			getDataDao.save(cashin);
			getDataDao.save(proposal);
			
			cmb.setStatus(1);
			cmb.setLoginname(user.getLoginname());
			cmb.setNotes(cmb.getNotes()+"  "+user.getLoginname());
			cmb.setTimecha(DateUtil.getTimecha(new Timestamp(cmb.getPayDate().getTime()),new Timestamp(cmb.getDate().getTime())));
			cmb.setOvertime(DateUtil.getOvertime(new Timestamp(cmb.getPayDate().getTime()),new Timestamp(cmb.getDate().getTime())));
			getDataDao.update(cmb);
			
			if(order !=null){
				order.setStatus(1);
				order.setUpdatetime(new Date());
		 		getDataDao.update(order);
			}
			
			// 设置用户 isCashin字段
			if (user.getIsCashin().intValue() == Constants.FLAG_FALSE.intValue()) {
				getDataDao.updateUserIsCashInSql(user.getLoginname());
			}
			
		}
		
	}
	
	
	/**
	 * 获取当日可用优惠金额(日优惠限额500)
	 * @param loginname
	 * @return
	 * */
	Double getAvailable(String loginname) throws Exception {
		
		if(StringUtils.isBlank(loginname)){
			throw new Exception("获取当日可用优惠金额(日优惠限额500)异常，loginname为空");
		}
		
		Double used = 0.0;
		Map<String, Object> params = new HashMap<String, Object>();
		String sqlStr = "select sum(fee) from cashin where loginname=:loginname and cashintime>=:startTimeDeposit and cashintime<=:endTimeDeposit";
		Date s = DateUtil.ntStart();
		Date d = DateUtil.ntEnd();
		params.put("loginname", loginname);
		params.put("startTimeDeposit", s);
		params.put("endTimeDeposit", d);
		used =getDataDao.getDoubleValue(sqlStr, params);
		Double available = Arith.round(Arith.sub(500.0, used), 2);
		log.info(loginname + "已使用优惠金额：>>>" + used + "可用金额：>>>" + available);
		
		return available < 0.0? 0.0: available;
	}
	

}
