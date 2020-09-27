package com.nnti.withdraw.service.implementations;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nnti.common.constants.ErrorCode;
import com.nnti.common.model.vo.User;
import com.nnti.common.model.vo.UserBankInfo;
import com.nnti.common.service.interfaces.IUserService;
import com.nnti.common.utils.DateUtil;
import com.nnti.withdraw.model.bean.FlashPayNotifyBean;
import com.nnti.withdraw.model.dto.WithdrawDTO;
import com.nnti.withdraw.model.vo.FPayorder;
import com.nnti.withdraw.service.interfaces.IFPayorderService;
import com.nnti.withdraw.service.interfaces.IWithdrawService;
import com.nnti.withdraw.util.CHPayUtil;
import com.nnti.withdraw.util.FPayUtil;
import com.nnti.withdraw.util.SHA;
import com.nnti.withdraw.util.XFTPayUtil;

@Service("withdrawService")
@Transactional(rollbackFor = Exception.class)
public class WithdrawServiceImpl extends BaseService implements IWithdrawService {
	private static Logger log = Logger.getLogger(WithdrawServiceImpl.class);

	@Autowired
	private IUserService userService;
	@Autowired
	private IFPayorderService fPayorderService;
	
	@Override
	public String addCashOut(WithdrawDTO dto) throws Exception {
		//基本参数信息验证
		String msg = this.paramerValidate(dto);
		if (StringUtils.isNotBlank(msg)) {
			return msg;
		}
		
		User user = userService.get(dto.getLoginName());
		log.info("user对象："+user.getAccountName());
		//验证银行卡
		Object bankInfo = this.validateBankInfo(dto);
		
		UserBankInfo userBank = null;
		if(bankInfo instanceof String){
			return bankInfo.toString();
		}else{
			userBank = (UserBankInfo) bankInfo;
		}
		//验证答案
		if(!dto.getProduct().equals("dy")) {
			ErrorCode quesValidate = validateQuestion(user, dto.getQuestionid(), dto.getAnswer(),dto.getProduct());
			if (!StringUtils.equals(ErrorCode.SC_10000.getCode(), quesValidate.getCode())) {
				return quesValidate.getType();
			}
		}
		
		//玩家秒提使用情况
		Map<String , Object> userMfbInfo = this.isSecondWithdraw(dto, user);
		
		//开始处理提款订单
		String doinfo = dealWithdrawProposalFluence(user, userMfbInfo, userBank, dto);
		
		return doinfo;
	}

	@Override
	@Transactional

	public String notify(String data,String product) {
		String msg = null ;
		try {
			JSONObject json = JSONObject.parseObject(data);
			String dataJson = json.getString("data");
			String sign = json.getString("sign");
			FlashPayNotifyBean notify = JSON.parseObject(dataJson, FlashPayNotifyBean.class);
			
			String order_status = notify.getOrder_status();
			String order_no = notify.getOrder_no();
			String mySign = "";
			String card_number = notify.getCard_number();
			String card_balance = notify.getCard_balance();
			String fail_message = notify.getFail_message();
			
			log.info(product+"--->产品订单号："+order_no);
			HashMap<String , String> map =  FPayUtil.getClassMap().get(product);
			String secretkey = map.get("secretkey");
			log.info(product+"--->secretkey："+secretkey);
			
			mySign = SHA.sign("order_no=" + order_no + "&order_status=" + order_status+ "&fail_message=" + fail_message + "&card_number=" + card_number + "&card_balance=" + card_balance + secretkey,"SHA-256");
			
			Boolean signFlag = mySign.equals(sign) ? true : false ;
			Boolean resultFlag = order_status.equals("Complete") ? true : false ;
			if(!signFlag){
				log.error(order_no + "验签失败");
				return "验签失败";
			}
			msg = this.doNotify(resultFlag, order_no, card_number, card_balance, fail_message);
			log.info( "回调返回内容："+msg );
			if(msg.equals("success")){
				return "ok";	
			}
			else {
				return "error";
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			msg = "回调数据转json发生异常"+data;
			return msg ;
		}
		
	}
	

	@Override
	@Transactional
	public String updateFPay(String orderNo,String product) {
		FPayorder order = fPayorderService.get(orderNo);
		if(null == order){
			return "提款单子不存在";
		}
		String msg = "";
		String result ="";
		String billno = order.getBillno();
		String batchdate = DateUtil.format(DateUtil.YYYYMMDD, order.getCreateTime());
		log.info(product+"--->产品订单号："+billno);
		result = XFTPayUtil.api_check_order(order.getBillno(),product,batchdate);
		
		if("".equals(result)){
			return "请求第三方失败，请联系技术检查";
		}
		Boolean resultFlag = null;
		//返回成功
		if("成功".equals(result)){
			resultFlag = true;
		}
		else if("失败".equals(result)){
			resultFlag = false;
		}
		//返回失败和取消
		else {
			return "订单处理中";
		}
		
		String card_number = "11111111";
		String card_balance = order.getAmout()+"";
		try {
			msg = this.doNotify(resultFlag, order.getBillno(), card_number, card_balance, result);
		} catch (Exception e) {
			e.printStackTrace();
			msg = "处理异常"+e.getMessage();
		}
		return msg;
	}
	
	//畅汇代付相关代码
	public String updateFPay_CH(String orderNo,String product) {
		FPayorder order = fPayorderService.get(orderNo);
		if(null == order){
			return "提款单子不存在";
		}
		String msg = "";
		String result ="";
		String billno = order.getBillno();
		log.info(product+"--->产品订单号："+billno);
		result = CHPayUtil.api_check_order(order.getBillno(),product);
		
		if("".equals(result)){
			return "请求第三方失败，请联系技术检查";
		}
		
		JSONObject json = JSONObject.parseObject(result);
		
		String order_status = json.getString("r1_Code");
		Boolean resultFlag = null;
		//返回成功
		if(order_status.equals("0000")){
			resultFlag = true;
		}
		else if(order_status.equals("3003") || order_status.equals("3004")){
			return "订单处理中";
		}
		//返回失败和取消
		else {
			resultFlag = false;
		}
		
		String card_number = "11111111";
		String card_balance = order.getAmout()+"";
		String fail_message = json.getString("r7_Desc");
		try {
			msg = this.doNotify(resultFlag, order.getBillno(), card_number, card_balance, fail_message);
		} catch (Exception e) {
			e.printStackTrace();
			msg = "处理异常"+e.getMessage();
		}
		return msg;
	}
	
	public String addFPay(String orderNo,String operator,String remark,String ip,String product)throws Exception {
			return this.addFlashPay(orderNo,operator,remark,ip,product);
	}
	
	//重新提交
	public String resubmitFPay(String orderNo,String operator,String ip,String product)throws Exception {
		return this.resubmitFPayOrder(orderNo,operator,ip,product);
}
}
