package dfh.action.pay;

import java.util.Date;

import org.apache.axis2.AxisFault;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import dfh.action.SubActionSupport;
import dfh.model.Bankinfo;
import dfh.model.PayOrderValidation;
import dfh.model.Users;
import dfh.utils.AxisUtil;
import dfh.utils.Constants;
import dfh.utils.GsonUtil;

/**
 * 额度验证存款
 */
public class ValidateAmountPayAction extends SubActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static Logger log = Logger.getLogger(ValidateAmountPayAction.class);
	
	private Integer id;
	

	//金额
	private double amount;
	
	// 错误信息
	private String error_info;
	
	/**
	 * 获取额度验证存款卡信息
	 * @return
	 */
	public String getValidateDepositBankInfo() {
		try {
			Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			// 检测用户是否登录
			if (user == null) {
				setError_info("[提示]你的登录已过期，请从首页重新登录");
				return ERROR;
			}
			//代理不能使用在线支付
			if(!user.getRole().equals("MONEY_CUSTOMER")){
				setError_info("[提示]代理不能使用在线支付！");
				return ERROR;
			}
			Bankinfo bankinfo = null;
			if(user.getLevel()!=null){
				//获取银行账号
				bankinfo = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getValidateDepositBankInfo", new Object[] {String.valueOf(user.getLevel())}, Bankinfo.class);
			}
			GsonUtil.GsonObject(bankinfo);
			return null;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			setError_info("网络繁忙，请稍后再试！");
			GsonUtil.GsonObject(getError_info());
			return null;
		}
	}
	
	/**
	 * 创建额度验证存款订单
	 */
	public void createValidateAmountPayOrder(){
		ValidateAmountPayOrderVo orderVo = new ValidateAmountPayOrderVo();
		try {
			Users customer = getCustomerFromSession();
			if(customer==null){
				orderVo.setMsg("请重新登录！");
				GsonUtil.GsonObject(orderVo);
				return;
			}
			if(amount < 10){
				orderVo.setMsg("最低存款10元");
				GsonUtil.GsonObject(orderVo);
				return;
			}
			PayOrderValidation param = new PayOrderValidation();
			param.setOriginalAmount(amount);
			param.setUserName(customer.getLoginname());
			
			if("1".equals(ServletActionContext.getRequest().getParameter("type"))){
				param.setType("wechat");
			}
			
			//param.setCreateTime(new Date());  在webservice 端设置时间，此处设置时间，传输到webservice端时间精度丢失，只有年月日
			PayOrderValidation payOrder = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "createValidatedPayOrder", new Object[]{param}, PayOrderValidation.class);
			String flag = payOrder.getCode();
			orderVo.setCode(flag);
			if(flag.equals("0")){
				orderVo.setMsg("24小时内存在三笔或以上的未支付额度验证存款订单，不能创建新订单");
			}else if(flag.equals("-1")){
				orderVo.setMsg("由于当前存款人数太多，无法处理您 " + amount + " 元的存款，请尝试其他金额");
			}else{
				orderVo.setMsg("存款订单创建成功");
				orderVo.setAmount(payOrder.getAmount());
			}
			GsonUtil.GsonObject(orderVo);
		} catch (Exception e) {
			log.error("生成额度验证存款订单异常：" + e.getMessage());
			orderVo.setCode("-2");
			orderVo.setMsg("系统异常，存款下单失败");
			GsonUtil.GsonObject(orderVo);
		}
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	public String getError_info() {
		return error_info;
	}

	public void setError_info(String error_info) {
		this.error_info = error_info;
	}
	
	
	
	public void setId(Integer id) {
		this.id = id;
	}



	class ValidateAmountPayOrderVo{
		//操作标识  1：成功
		private String code;
		private String msg;
		private Double amount;
		
		public String getCode() {
			return code;
		}
		public void setCode(String code) {
			this.code = code;
		}
		public String getMsg() {
			return msg;
		}
		public void setMsg(String msg) {
			this.msg = msg;
		}
		public Double getAmount() {
			return amount;
		}
		public void setAmount(Double amount) {
			this.amount = amount;
		}
	}
	
	/**
	 * 创建额度验证存款订单(改进)
	 */
	public void createValidateAmountPayOrderGj(){
		ValidateAmountPayOrderVo orderVo = new ValidateAmountPayOrderVo();
		try {
			Users customer = getCustomerFromSession();
			if(customer==null){
				orderVo.setMsg("请重新登录！");
				GsonUtil.GsonObject(orderVo);
				return;
			}
			if(amount < 10){
				orderVo.setMsg("[提示]最低存款10元");
				GsonUtil.GsonObject(orderVo);
				return;
			}
			if(amount > 5000){
				orderVo.setMsg("[提示]存款金额不能超过5000！");
				GsonUtil.GsonObject(orderVo);
				return;
			}
			PayOrderValidation param = new PayOrderValidation();
			param.setOriginalAmount(amount);
			param.setAmount(amount);
			param.setUserName(customer.getLoginname());
			if("1".equals(ServletActionContext.getRequest().getParameter("type"))){
				param.setType("wechat");
			}
			//param.setCreateTime(new Date());  在webservice 端设置时间，此处设置时间，传输到webservice端时间精度丢失，只有年月日
			PayOrderValidation payOrder = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "createValidatedPayOrderGj", new Object[]{param}, PayOrderValidation.class);
			String flag = payOrder.getCode();
			orderVo.setCode(flag);
			if(flag.equals("0")){
				orderVo.setMsg("存在该额度订单");
			}
			GsonUtil.GsonObject(orderVo);
		} catch (Exception e) {
			orderVo.setCode("-2");
			GsonUtil.GsonObject(orderVo);
		}
	}
	
	/**
	 * 创建额度验证存款订单
	 */
	public void createValidateAmountPayOrderTwo(){
		ValidateAmountPayOrderVo orderVo = new ValidateAmountPayOrderVo();
		try {
			Users customer = getCustomerFromSession();
			if(customer==null){
				orderVo.setMsg("请重新登录！");
				GsonUtil.GsonObject(orderVo);
				return;
			}
			if(amount < 10){
				orderVo.setMsg("[提示]最低存款10元");
				GsonUtil.GsonObject(orderVo);
				return;
			}
			if(amount > 5000){
				orderVo.setMsg("[提示]存款金额不能超过5000！");
				GsonUtil.GsonObject(orderVo);
				return;
			}
			
			PayOrderValidation param = new PayOrderValidation();
			param.setOriginalAmount(amount);
			param.setAmount(amount);
			param.setUserName(customer.getLoginname());
			if("1".equals(ServletActionContext.getRequest().getParameter("type"))){
				param.setType("wechat");
			}
			//param.setCreateTime(new Date());  在webservice 端设置时间，此处设置时间，传输到webservice端时间精度丢失，只有年月日
			PayOrderValidation payOrder = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "createValidatedPayOrderTwo", new Object[]{param}, PayOrderValidation.class);
			String flag = payOrder.getCode();
			orderVo.setCode(flag);
			if(flag.equals("0")){
				orderVo.setMsg("存在该额度，请输入其它额度");
			}else if(flag.equals("-1")){
				orderVo.setMsg("由于当前存款人数太多，无法处理您 " + amount + " 元的存款，请尝试其他金额");
			}else{
				orderVo.setMsg("存款订单创建成功");
				orderVo.setAmount(payOrder.getAmount());
			}
			GsonUtil.GsonObject(orderVo);
		} catch (Exception e) {
			log.error("生成额度验证存款订单异常：" + e.getMessage());
			orderVo.setCode("-2");
			orderVo.setMsg("系统异常，存款下单失败");
			GsonUtil.GsonObject(orderVo);
		}
	}
	
	
	/******
	 * 废除订单
	 */
	public void discardDepositOrder(){
		log.info("废弃额度验证存款订单" + id);
		ValidateAmountPayOrderVo orderVo = new ValidateAmountPayOrderVo();
		Boolean flag =true;
		try {
		    flag = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "discardDepositOrder", new Object[]{id}, Boolean.class);
			if(flag){
				orderVo.setCode("1");
				GsonUtil.GsonObject(orderVo);
			}
		} catch (AxisFault e) {
			orderVo.setCode("-2");
			GsonUtil.GsonObject(orderVo);
		}
		
	}
	
}
