package com.nnti.common.service.implementations;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nnti.common.dao.master.IMasterBankInfoDao;
import com.nnti.common.dao.slave.ISlaveBankInfoDao;
import com.nnti.common.dao.slave.ISlaveCommonDao;
import com.nnti.common.model.vo.BankCardinfo;
import com.nnti.common.model.vo.BankInfo;
import com.nnti.common.model.vo.OperationLog;
import com.nnti.common.model.vo.User;
import com.nnti.common.service.interfaces.IBankInfoService;
import com.nnti.common.service.interfaces.ICommonService;
import com.nnti.common.utils.Assert;
import com.nnti.common.utils.DateUtil;
import com.nnti.pay.controller.BasePayController;
import com.nnti.pay.model.vo.MerchantPay;
import com.nnti.pay.model.vo.TotalDeposit;

import sun.util.logging.resources.logging;

@Service
@Transactional(rollbackFor = Exception.class)
public class BankInfoServiceImpl implements IBankInfoService {
	
	protected Logger log = Logger.getLogger(BankInfoServiceImpl.class);

	@Autowired
	private IMasterBankInfoDao masterBankInfoDao;
	@Autowired
	private ISlaveBankInfoDao slaveBankInfoDao;
	@Autowired
	private ISlaveCommonDao slaveCommonDao;


	public List<BankInfo> findBankInfoList(BankInfo bankInfo) throws Exception {

		return slaveBankInfoDao.findBankInfoList(bankInfo);
	}

	public List<BankInfo> findBankInfoList2(Map<String, Object> map) throws Exception {

		return masterBankInfoDao.findBankInfoList2(map);
	}

	public BankInfo findById(Integer id) throws Exception {

		return slaveBankInfoDao.findById(id);
	}

	public List<BankInfo> findZfbBankInfo(BankInfo bankInfo) throws Exception {

		return slaveBankInfoDao.findZfbBankInfo(bankInfo);
	}

	public int update(BankInfo bankInfo) throws Exception {

		return masterBankInfoDao.update(bankInfo);
	}

	public int update2(Map<String, Object> params) throws Exception {
		return masterBankInfoDao.update2(params);
	}

	@Override
	public BankInfo getBank(String bankCard) throws Exception {
		return slaveBankInfoDao.getBank(bankCard);
	}

	@Override
	public int controlBank(String bankCard, Integer paySwitch) throws Exception {
		BankInfo bankinfo = getBank(bankCard);
		bankinfo.setIsShow(paySwitch);
		Integer ret = masterBankInfoDao.update(bankinfo);
		return ret;
	}

	@Override
	public BankInfo findBankinfoByParams(User user,String paytype,TotalDeposit totalDeposit,String [] banknames) throws Exception {
		
		// 判断玩家注册是否满足七天
		Calendar cals = Calendar.getInstance();
		cals.setTime(new Date());
		cals.add(Calendar.DAY_OF_MONTH, -7);

		String[] vpnnames = new String[3];
		BankInfo bankinfo = null;
		
		// 注册满足七天
		if (user.getCreateTime().getTime() < cals.getTime().getTime()) {   
			vpnnames[0] = "A";
			vpnnames[1] = "C";
			vpnnames[2] = "D";
		} else {
			vpnnames[0] = "B";
			vpnnames[1] = "C";
			vpnnames[2] = "D";
		}

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("type", 1);
		params.put("isshow", 1);
		params.put("useable", 0);
		params.put("banknames", banknames);
		params.put("vpnnames", vpnnames);
		params.put("paytype", paytype);
		//params.put("userrole", Assert.levels[user.getLevel()]);
		params.put("userrole",user.getLevel());
		List<BankInfo> list =  masterBankInfoDao.findBankinfoByParams(params);
		
		
		  log.info("List<BankInfo> list:"+list);
		  
		  log.info("deposit"+totalDeposit.getAllDeposit());
		
		
		if(list!=null && list.size()>0 && list.get(0)!=null){
			bankinfo = list.get(0);
			log.info("DepositMin"+bankinfo.getDepositMin());
			
			if(totalDeposit.getAllDeposit() > bankinfo.getDepositMin()){
				return bankinfo ;
			}
		}
		return null;
	}

	@Override
	public List<BankInfo> findDepositBankInfo(BankInfo bankInfo) throws Exception {
		return slaveBankInfoDao.findDepositBankInfo(bankInfo);
	}

	public BankCardinfo findBankInfo(Map<String, Object> map) throws Exception {
		return masterBankInfoDao.findBankInfo(map);
	}
	
	public boolean  findBankStatus(String bankName) {
	   int counts =  masterBankInfoDao.findBankStatus(bankName);
	   if(counts != 0 ) {
		   return true;
	   }else {
		   return false;
	   }
	}
	
}