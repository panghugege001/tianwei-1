package dfh.service.interfaces;

import java.util.List;

import dfh.exception.GenericDfhRuntimeException;
import dfh.model.DepositWechat;
import dfh.model.Netpay;

public interface NetpayService  extends UniversalService{

	String addNetpayOrder(String billno, String loginname, String password, String netpayName, boolean newAccount, String partner, Double amount, String aliasName, String phone, String email,
			String attach, String ip) throws GenericDfhRuntimeException;

	String excuteNetpayOrder(String billno, Double amount, Boolean isSucc, String message, String md5Info,String ip) throws GenericDfhRuntimeException;

	List getAllNetpay();

	Netpay getNetpayByMerno(String merno);
	
	public String repairNetpayOrderHc(String operator, String billno,
			String payAmount, String ip, String payBillno, String payTime,
			String payPlatform,String loginname,String msg);
	
	public String repairNetpayOrderBfb(String operator, String billno,
			String payAmount, String ip, String payBillno, String payTime,
			String payPlatform,String loginname,String msg);

	public String repairNetpayOrderGfb(String operator, String billno, String payAmount, String ip, String payBillno, String payTime, String payPlatform,String loginname,String msg);
	
	String repairNetpayOrder(String operator, String billno, Double amount,String ip) throws GenericDfhRuntimeException;
	
	String repairNetpayOrder(String operator, String billno, String payAmount,String ip,String payBillno,String payTime,String payPlatform,String loginname,String msg);

	String repairNetpayOrderZf(String operator, String billno, String payAmount,String ip,String payBillno,String payTime,String payPlatform,String loginname,String msg);
	
	String repairNetpayOrderZfb(String operator, String billno, String payAmount,String ip,String payBillno,String payTime,String payPlatform,String loginname,String msg);
	
	String repairGameOrder(String operator, String billno, String payAmount,String ip,String payBillno,String payTime,String payPlatform,String loginname,String msg);
	
	String repairNetpayOrderHf(String operator, String billno, String payAmount,String ip,String payBillno,String payTime,String payPlatform,String loginname,String msg);
	
	String updateNetpay(String billno, Boolean enabled, Integer next) throws GenericDfhRuntimeException;
	
   public String submitProposal(String billno, String operator, String ip, String remark);
   
   public String submitGameProposal(String billno, String operator, String ip, String remark);
   
   public String submitQuotalProposal(String billno, String operator, String ip, String remark);
   
   public String submitSbProposal(Integer id, String operator, String ip, String remark);
	
	public String submitCancel(String billno, String operator, String ip, String remark);
	
	public String submitGameCancel(String billno, String operator, String ip, String remark);
	
	public String submitQuotalCancel(String billno, String operator, String ip, String remark);

	public String repairNetpayOrderLfwx(String operator, String billno, String payAmount,String ip,String payBillno,String payTime,String payPlatform,String loginname,String msg);
	
	public String submitWechatProposal(DepositWechat dWechat, String loginname, String operator, String ip);

}