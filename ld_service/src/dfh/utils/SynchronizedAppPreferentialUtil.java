package dfh.utils;  


import org.apache.commons.lang.RandomStringUtils;

import dfh.exception.GenericDfhRuntimeException;
import dfh.model.Users;
import dfh.service.interfaces.ISelfYouHuiService;
import net.sf.json.JSONObject; 

public class SynchronizedAppPreferentialUtil {
	
	private static SynchronizedAppPreferentialUtil instance = null;
	public static final int appRewardVersion = 1;
	public static final String appPreferential = "APP下载彩金";
	
	private SynchronizedAppPreferentialUtil(){
	} 
	
	public static SynchronizedAppPreferentialUtil getInstance() {
		if (instance == null) {
			return instance = new SynchronizedAppPreferentialUtil();
		} else {
			return instance;
		}
	}
	
	public synchronized String depositAppDownloadReward(ISelfYouHuiService selfYouHuiService , String loginname , String platform){
		String msg = selfYouHuiService.platformAmountAndRecordAPPRewardLog(loginname , platform);
		Users user = (Users) selfYouHuiService.get(Users.class,loginname);
		String [] args = msg.split(";") ;
		if(args.length == 3){
			Double changeMoney = Double.valueOf(args[0]) ;
			String transID = args[1];
			String name = args[2];
			
			if(platform.equals("PT")){
				Boolean deposit = PtUtil.getDepositPlayerMoney(loginname, changeMoney);
				if (deposit != null && deposit) {
					return name + "成功";
				} else {
					throw new GenericDfhRuntimeException(name + "不成功 ， 数据回滚。");
				}
			}else if(platform.equals("TTG")){
				Boolean deposit = PtUtil1.addPlayerAccountPraper(loginname, changeMoney);
				if (deposit != null && deposit) {
					return name + "成功";
				} else {
					throw new GenericDfhRuntimeException(name + "不成功 ， 数据回滚。(网络故障)");
				}
			}else if(platform.equals("NT")){
				JSONObject ntm = JSONObject.fromObject(NTUtils.changeMoney(loginname, changeMoney));
				if (ntm.getBoolean("result")){
					return name + "成功";
				} else{
					throw new GenericDfhRuntimeException(name + "不成功 ， 数据回滚。"+ntm.getString("error"));
				}
			}else if(platform.equals("QT")){
				String deposit = QtUtil.getDepositPlayerMoney(loginname, changeMoney, transID);
				if (deposit != null && QtUtil.RESULT_SUCC.equals(deposit)) {
					return name + "成功";
				} else {
					throw new GenericDfhRuntimeException(name + "不成功 ， 数据回滚。(网络故障)");
				}
			}else if(platform.equals("MG")){
				String depositMsg = "error";
				try {
					String id = DateUtil.getYYMMDDHHmmssSSS4TransferNo() + RandomStringUtils.randomNumeric(4);
					depositMsg = MGSUtil.transferToMG(loginname,user.getPassword(), Double.valueOf(changeMoney.intValue()),id);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (depositMsg == null) {
					return name + "成功";
				} else {
					throw new GenericDfhRuntimeException(name + "不成功 ， 数据回滚。(网络故障)");
				}
			}else if(platform.equals("DT")){
				String deposit = DtUtil.withdrawordeposit(loginname, changeMoney);
				if (null != deposit  && deposit.equals("success")) {
					return name + "成功";
				} else {
					throw new GenericDfhRuntimeException(name + "不成功 ， 数据回滚。(网络故障)");
				}
			}else{
				return platform+"app下载彩金领取失败";
			}
		}else{
			return msg ;
		}
	}
}
