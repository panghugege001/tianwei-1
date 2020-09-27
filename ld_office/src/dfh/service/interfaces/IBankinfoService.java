package dfh.service.interfaces;

import java.util.List;

import dfh.model.Bankinfo;
import dfh.model.PayMerchant;

public interface IBankinfoService extends UniversalService{
	
	String addbankinfo(Integer id,String username,Integer type,String bankname,String remark,String vpnname, String vpnpassword,String accountno,String loginname,String password,Integer bankInfoType,String bankcard,String usb,String realname,String remoteip,String samebank,String crossbank,Double transfermoney,Integer autopay,String userrole,Double fee,String zfbImgCode,Integer scanAccount,Integer paytype,Double depositMin,Double depositMax) throws Exception;
	
	public int getBankinfoCount(String username,Integer type,String bankname) throws Exception;
	
	List getAllBankinfo(String username,Integer type,String bankname,int pageNumber,int rowCount) throws Exception;
	
	String cancle(String operator,Integer id,Integer useable);
	
	String switchmsBank(String id,Integer useable);
	
	List getBankinfo(Integer type) throws Exception;
	
	List getBusinessBankinfo() throws Exception;
	
	List getAllBankinfo() throws Exception;
	
	List getCashinBankinfo() throws Exception;
	
	String gtDepositAmount(int i, int j);
	
	String gtDepositTime(int i, int j);
	
	String gtWithdrawTime(int i, int j);
	
	Boolean updateUserrole(String userrole,Integer id);
	
	Boolean updateIsshow(String operator,Integer isshow,Integer id,String bankinfoUsername);
	
	Boolean updateIstransfer(String operator,Integer istransfer,Integer id,String bankinfoUsername);
	
	
	String updatePayMerchant(PayMerchant payMer);
	
	String savePayMerchant(PayMerchant payMer);
	
	void deletePayMerchant(Class a,int id);

	String updateBankInfo(Bankinfo bankinfo);
	
	void updateBankSwitch(Integer id , String type);
	
	void updateBankBalance();
	
	/**
	 * 同略云更新银行额度
	 */
	void updateTlyBankBalance();
	
}
