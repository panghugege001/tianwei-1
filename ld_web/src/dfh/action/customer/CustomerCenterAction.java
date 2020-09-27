package dfh.action.customer;

import org.apache.log4j.Logger;

import dfh.action.SubActionSupport;
import dfh.model.Users;
import dfh.utils.AxisSecurityEncryptUtil;
import dfh.utils.AxisUtil;
import dfh.utils.GsonUtil;

public class CustomerCenterAction extends SubActionSupport{
	private static Logger log = Logger.getLogger(CustomerCenterAction.class);
	private static final long serialVersionUID = 1L;
	private String gameCode ;
	
	private String errormsg ; 

	private Double remit ;
	
	/**
	 * 获取游戏金额
	 * @return
	 */
	public String gameAmount(){
		try {
			Users customer = getCustomerFromSession();
			if(customer==null){
				GsonUtil.GsonObject("尚未登录！请重新登录！");
				return null;
			}
			if(gameCode==null || gameCode.equals("")){
				GsonUtil.GsonObject("请选择账户!");
				return null;
			}
			if(gameCode.equals("ld")){
				customer = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getUser", new Object[] { customer.getLoginname() }, Users.class);
				GsonUtil.GsonObject(customer.getCredit() + "元");
				return null;
			}
			if(gameCode.equals("ea")){
				GsonUtil.GsonObject(AxisSecurityEncryptUtil.getEaGameMoney(customer.getLoginname()) + "元");
				return null;
			}
			if(gameCode.equals("ag")){
				GsonUtil.GsonObject(AxisSecurityEncryptUtil.getAgGameMoney(customer.getLoginname())+" 元");
				return null;
			}
			if(gameCode.equals("agin")){
				GsonUtil.GsonObject(AxisSecurityEncryptUtil.getAginGameMoney(customer.getLoginname())+" 元");
				return null;
			}
			if(gameCode.equals("bbin")){
				GsonUtil.GsonObject(AxisSecurityEncryptUtil.getBbinGameMoney(customer.getLoginname())+" 元");
				return null;
			}
			if(gameCode.equals("keno")){
				GsonUtil.GsonObject(AxisSecurityEncryptUtil.getKenoGameMoney(customer.getLoginname())+" 元");
				return null;
			}
			if(gameCode.equals("keno2")){
				GsonUtil.GsonObject(AxisSecurityEncryptUtil.getKeno2GameMoney(customer.getLoginname())+" 元");
				return null;
			}
			if(gameCode.equals("sba")){
				GsonUtil.GsonObject(AxisSecurityEncryptUtil.getSbaGameMoney(customer.getLoginname())+" 元");
				return null;
			}
			if(gameCode.equals("pt")){
				Double result = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getPtPlayerMoney", new Object[] { customer.getLoginname() }, Double.class);
				GsonUtil.GsonObject(result==null?"系统繁忙":result.toString()+" 元");
				return null;
			}
			if(gameCode.equals("sixlottery")){
				GsonUtil.GsonObject(AxisSecurityEncryptUtil.getSixLotteryGameMoney(customer.getLoginname())+" 元");
				return null;
			}
			if(gameCode.equals("ebet")){
				String result = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getEbetBalance", new Object[] { customer.getLoginname() }, String.class);
				GsonUtil.GsonObject(result==null?"系统繁忙":result.toString()+" 元");
				return null;
			}
			if(gameCode.equals("ttg")){
				String result = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getTtgBalance", new Object[] { customer.getLoginname() }, String.class);
				GsonUtil.GsonObject(result==null?"系统繁忙":result.toString()+" 元");
				return null;
			}
			if(gameCode.equals("jc")){
				GsonUtil.GsonObject(AxisSecurityEncryptUtil.getJCBalance(customer.getLoginname())+" 元");
				return null;
			}
			if(gameCode.equals("gpi")){
				GsonUtil.GsonObject(AxisSecurityEncryptUtil.getGPIBalance(customer.getLoginname())+" 元");
				return null;
			}
			if(gameCode.equals("png")){
				Double result = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL_2 + "UserWebService", false), AxisUtil.NAMESPACE, "getPNGBalance", new Object[] { customer.getLoginname() }, Double.class);
				GsonUtil.GsonObject(result==null?"系统繁忙":result.toString()+" 元");
				return null;
			}
			return null;
		}  catch (Exception e) {
			GsonUtil.GsonObject("网络繁忙！请稍后再试！");
			return null;
		}
	}
	
	
	public String getGameCode() {
		return gameCode;
	}

	public void setGameCode(String gameCode) {
		this.gameCode = gameCode;
	}
	
	public String getErrormsg() {
		return errormsg;
	}
	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
	}
	public Double getRemit() {
		return remit;
	}
	public void setRemit(Double remit) {
		this.remit = remit;
	}

}
