package dfh.utils.transfer;

import net.sf.json.JSONObject;
import dfh.exception.GenericDfhRuntimeException;
import dfh.service.interfaces.ISelfYouHuiService;
import dfh.utils.DtUtil;
import dfh.utils.GPIUtil;
import dfh.utils.MGSUtil;
import dfh.utils.NTUtils;
import dfh.utils.PtUtil;
import dfh.utils.PtUtil1;
import dfh.utils.QtUtil;

public class SynchrPt8SelfUtil {
	
	private static SynchrPt8SelfUtil instance = null;
	
	private SynchrPt8SelfUtil(){
		
	}
	
	public static SynchrPt8SelfUtil getInstance() {
		if (instance == null) {
			return instance = new SynchrPt8SelfUtil();
		} else {
			return instance;
		}
	}
	
	public synchronized String selfYouHui8Yuan(ISelfYouHuiService selfYouHuiService , String loginname ,String platform){
		String msg = selfYouHuiService.selfYouHui8Yuan(loginname , platform);
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
			}else if(platform.equals("GPI")){
				String deposit = GPIUtil.credit(loginname, changeMoney, transID);
				if (deposit != null && deposit.equals(GPIUtil.GPI_SUCCESS_CODE)) {
					return name + "成功";
				} else {
					throw new GenericDfhRuntimeException(name + "不成功 ，数据回滚。("+GPIUtil.respCodeMap.get(deposit)+")");
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
			}/*else if(platform.equals("MG")){
				String depositMsg = "error";
				try {
					depositMsg = MGSUtil.transferToMG(loginname, changeMoney);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (depositMsg == null) {
					return name + "成功";
				} else {
					throw new GenericDfhRuntimeException(name + "不成功 ， 数据回滚。(网络故障)");
				}
			}*/else if(platform.equals("DT")){
				String deposit = DtUtil.withdrawordeposit(loginname, changeMoney);
				if (null != deposit  && deposit.equals("success")) {
					return name + "成功";
				} else {
					throw new GenericDfhRuntimeException(name + "不成功 ， 数据回滚。(网络故障)");
				}
			}else{
				return platform+"领取体验金失败";
			}
		}else{
			return msg ;
		}
	}
	
	
	
}
