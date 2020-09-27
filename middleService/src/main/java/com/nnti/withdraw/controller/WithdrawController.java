package com.nnti.withdraw.controller;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nnti.common.constants.FunctionCode;
import com.nnti.common.constants.NotifyUrl;
import com.nnti.common.controller.BaseController;
import com.nnti.common.extend.zookeeper.GenerateNodePath;
import com.nnti.common.extend.zookeeper.ZookeeperFactoryBean;
import com.nnti.common.model.dto.DataTransferDTO;
import com.nnti.common.model.vo.BankInfo;
import com.nnti.common.service.interfaces.IBankInfoService;
import com.nnti.common.utils.ConfigUtil;
import com.nnti.common.utils.DateUtil;
import com.nnti.withdraw.model.dto.WithdrawDTO;
import com.nnti.withdraw.model.vo.FPayorder;
import com.nnti.withdraw.service.interfaces.IFPayorderService;
import com.nnti.withdraw.service.interfaces.IWithdrawService;

@RestController
@RequestMapping("/withdraw")
public class WithdrawController extends BaseController {

	private static Logger log = Logger.getLogger(WithdrawController.class);

	@Autowired
	IWithdrawService withdrawService;

	@Autowired
	private IFPayorderService fPayorderService;
	
    @Autowired
    private IBankInfoService bankinfoService;
	
	@RequestMapping(value = "/submit", method = { RequestMethod.POST })
	public DataTransferDTO submit(@RequestParam(value = "requestData", defaultValue = "") String requestData, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String requestJsonData = (String) request.getAttribute("requestJsonData");
		
		Gson gson = new GsonBuilder().create();
		WithdrawDTO dto = null;
		try {
			dto = gson.fromJson(requestJsonData, WithdrawDTO.class);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		log.info("--->信付通代付提交开始："+dto.getProduct()+"|"+dto.getLoginName()+"|"+dto.getTkType());
		log.info("\n\n\n");
		
		
		String msg = "";
		Boolean lockFlag = false;
		String loginName = dto.getLoginName();
		String time = DateUtil.format(DateUtil.YYYYMMDDHHMMSS, new Date());
		
		final InterProcessMutex lock = new InterProcessMutex(ZookeeperFactoryBean.zkClient, GenerateNodePath.generateUserLockForUpdate(loginName));
		try {
			lockFlag = lock.acquire(Integer.parseInt(ConfigUtil.getValue("zk.lock.timeout")), TimeUnit.SECONDS);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("玩家：" + loginName + "提款获取锁发生异常，异常信息：" + e.getMessage());
			lockFlag = true;
		}
		
		try {
			if (lockFlag) {
				
				log.info("正在处理玩家" + loginName + "的提款请求，执行时间：" + time);
				
				msg = withdrawService.addCashOut(dto);
				
				log.info("处理完成玩家" + loginName + "的提款请求");
			} else {
				
				log.error("玩家：" + loginName + "提款未能获取锁，无法执行请求对应的方法....");
				msg = "[提示]系统繁忙，请稍后重试！";
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.info("执行玩家：" + loginName + "提款请求对应的方法发生异常，异常信息：" + e.getMessage());
			msg = "[提示]系统繁忙，请稍后重试！";
		} finally {
			if (lockFlag) {
				try {
					lock.release();
				} catch (Exception e) {
					log.error("玩家：" + loginName + "提款释放锁发生异常，异常信息：" + e.getMessage());
					msg = "[提示]系统繁忙，请稍后重试！";
				}
			}
		}
		
		if (StringUtils.isNotBlank(msg) && !msg.contains("成功")) {

			failure(FunctionCode.SC_20000_112.getCode(), msg);
		}
		
		return success(FunctionCode.SC_10000.getCode(), msg, null);
	}
	
	/**
	 * 秒付宝支付回调
	 * @param data
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/notify", method = { RequestMethod.POST })
	public String notify(@RequestParam(value = "payment_result") String payment_result , HttpServletRequest request, HttpServletResponse response)  {
		String notifyUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
		log.info("秒付宝回调开始：notifyUrl:"+notifyUrl);

		String product = null;
		PrintWriter out = null;
		try {
			product = NotifyUrl.getProduct(notifyUrl);
		} catch (Exception e) {
			log.error("没有解析到产品名称，notifyUrl：" + notifyUrl, e);
			return "域名不对";
		}

		String msg = "";
		JSONObject json = JSONObject.parseObject(payment_result);
		String type = json.getString("type");
		log.info("json:"+json);
		log.info("\n\n\n");
		JSONObject dataJson = json.getJSONObject("data");
		
		String  order_no=null;
		if(dataJson.containsKey("order_no")){
			 order_no = dataJson.getString("order_no");
		}
		  
		String card_number = dataJson.getString("card_number");
		String card_balance = dataJson.getString("card_balance");

		String loginName = null;
		try {
			FPayorder fpayOrder = fPayorderService.getByBillno(order_no);
			loginName = fpayOrder.getLoginname();
		} catch (Exception e) {
			msg = "查询不到订单：" + order_no;
			log.error(msg);
			return msg;
		}

		Boolean lockFlag = false;
		final InterProcessMutex lock = new InterProcessMutex(ZookeeperFactoryBean.zkClient, GenerateNodePath.generateWithdrawNofifyLock(loginName));
		try {
			lockFlag = lock.acquire(Integer.parseInt(ConfigUtil.getValue("zk.lock.timeout")), TimeUnit.SECONDS);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("订单：" + loginName + "获取锁发生异常，异常信息：" + e.getMessage());
			lockFlag = true;
		}
		
		try {
			if (lockFlag) {
				
				//调用更新银行额度的方法
				if(type.equals("card_balance")){
					msg = updaBankCredit(card_number, card_balance, request);
					log.info("更新银行额度完毕"+card_number+"  "+msg);
					return msg;
				}
				
				log.info("正在处理订单" + order_no);

				out = response.getWriter();
				
				msg = withdrawService.notify(payment_result,product);
				
				log.info("处理完成订单" + order_no + "  " + msg) ;
			} else {
				
				log.error("订单：" + loginName + "未能获取锁，无法执行请求对应的方法....");
				msg = "[提示]系统繁忙，请稍后重试！";
			}
			out.write(msg);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("执行玩家：" + loginName + "请求对应的方法发生异常，异常信息：" + e.getMessage());
			msg = "[提示]系统繁忙，请稍后重试！";
			out.write(msg);
		} finally {
			out.close();
			if (lockFlag) {
				try {
					lock.release();
				} catch (Exception e) {
					log.error("订单：" + loginName + "释放锁发生异常，异常信息：" + e.getMessage());
					msg = "[提示]系统繁忙，请稍后重试！";
				}
			}
		}
		return null;
	}
	
	/**
	 * 更新订单状态
	 * @param orderNo
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/updateFPay", method = { RequestMethod.POST })
	public DataTransferDTO updateFPay(@RequestParam(value = "order_no", defaultValue = "") String order_no , @RequestParam(value = "product") String product, 
			HttpServletRequest request, HttpServletResponse response){
		String msg = "";

		String loginName = null;
		try {
			FPayorder order = fPayorderService.get(order_no);
			loginName = order.getLoginname();
		} catch (Exception e) {
			msg = "查询不到订单：" + order_no;
			log.error(msg);
		}
		
		Boolean lockFlag = false;
		final InterProcessMutex lock = new InterProcessMutex(ZookeeperFactoryBean.zkClient, GenerateNodePath.generateWithdrawNofifyLock(loginName));
		try {
			lockFlag = lock.acquire(Integer.parseInt(ConfigUtil.getValue("zk.lock.timeout")), TimeUnit.SECONDS);
		} catch (Exception e) {
			log.error("订单：" + loginName + "获取锁发生异常，异常信息：" + e.getMessage());
			lockFlag = true;
		}
		
		try {
			if (lockFlag) {
				log.info(product+"正在处理订单" + order_no);
				msg = withdrawService.updateFPay(order_no,product);
				log.info("处理完成订单" + order_no + "  " + msg) ;
			} else {
				
				log.error("订单：" + loginName + "未能获取锁，无法执行请求对应的方法....");
				msg = "[提示]系统繁忙，请稍后重试！";
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("执行玩家：" + loginName + "请求对应的方法发生异常，异常信息：" + e.getMessage());
			msg = "[提示]系统繁忙，请稍后重试！";
		} finally {
			if (lockFlag) {
				try {
					lock.release();
				} catch (Exception e) {
					log.error("订单：" + loginName + "释放锁发生异常，异常信息：" + e.getMessage());
					msg = "[提示]系统繁忙，请稍后重试！";
				}
			}
		}
		return new DataTransferDTO(msg);
	}
	
	/**
	 * 审核且秒提
	 * @param jobPno
	 * @param operator
	 * @param loginname
	 * @param accountNo
	 * @param accountName
	 * @param bankAddress
	 * @param remark
	 * @param getIp
	 * @return
	 */
	@RequestMapping(value = "/addFPay", method = { RequestMethod.POST })
	public DataTransferDTO addFPayOrder(@RequestParam(value = "order_no", defaultValue = "") String order_no , @RequestParam(value = "product") String product, @RequestParam(value = "operator") String operator,@RequestParam(value = "remark") String remark, @RequestParam(value = "ip") String ip,
			HttpServletRequest request, HttpServletResponse response){
		
		String msg = "";

		String loginName = null;
		try {
			FPayorder order = fPayorderService.get(order_no);
			loginName = order.getLoginname();
			/*List bankList = Arrays.asList(new Object[]{"农业银行","深圳发展银行","北京银行","中国银行","兴业银行","光大银行","建设银行","交通银行","平安银行","中信银行","民生银行","工商银行","招商银行"});
			if(!bankList.contains(order.getBankname())){
				return new DataTransferDTO("该提款银行不支持畅汇代付！");
			}*/
		} catch (Exception e) {
			msg = "查询不到订单：" + order_no;
			log.error(msg);
			return new DataTransferDTO(msg);
		}
		
		Boolean lockFlag = false;
		final InterProcessMutex lock = new InterProcessMutex(ZookeeperFactoryBean.zkClient, GenerateNodePath.generateWithdrawNofifyLock(loginName));
		log.info(loginName+"zk锁节点："+GenerateNodePath.generateWithdrawNofifyLock(loginName));
		try {
			lockFlag = lock.acquire(Integer.parseInt(ConfigUtil.getValue("zk.lock.timeout")), TimeUnit.SECONDS);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("订单：" + loginName + "获取锁发生异常，异常信息：" + e.getMessage());
			lockFlag = true;
		}
		
		try {
			if (lockFlag) {
				log.info(product+"正在处理订单" + order_no);
				msg = withdrawService.addFPay(order_no,operator,remark,ip,product);
				log.info("处理完成订单" + order_no + "  " + msg) ;
			} else {
				
				log.error("订单：" + loginName + "未能获取锁，无法执行请求对应的方法....");
				msg = "[提示]系统繁忙，请稍后重试！";
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("执行玩家：" + loginName + "请求对应的方法发生异常，异常信息：" + e.getMessage());
			msg = "[提示]系统繁忙，请稍后重试！";
		} finally {
			if (lockFlag) {
				try {
					lock.release();
				} catch (Exception e) {
					log.error("订单：" + loginName + "释放锁发生异常，异常信息：" + e.getMessage());
					msg = "[提示]系统繁忙，请稍后重试！";
				}
			}
		}
		return new DataTransferDTO(msg);
	}
	
	
	/**
	 * 重新提交
	 * @param jobPno
	 * @param operator
	 * @param loginname
	 * @param accountNo
	 * @param accountName
	 * @param bankAddress
	 * @param remark
	 * @param getIp
	 * @return
	 */
	@RequestMapping(value = "/resubmitFPay", method = { RequestMethod.POST })
	public DataTransferDTO resubmitFPay(@RequestParam(value = "order_no", defaultValue = "") String order_no , @RequestParam(value = "product") String product, @RequestParam(value = "operator") String operator,@RequestParam(value = "ip") String ip,
			HttpServletRequest request, HttpServletResponse response){
		
		String msg = "";

		String loginName = null;
		try {
			FPayorder order = fPayorderService.get(order_no);
			loginName = order.getLoginname();
			List bankList = Arrays.asList(new Object[]{"农业银行","深圳发展银行","北京银行","中国银行","兴业银行","光大银行","建设银行","交通银行","平安银行","中信银行","民生银行","工商银行","招商银行"});
			if(!bankList.contains(order.getBankname())){
				return new DataTransferDTO("该提款银行不支持畅汇代付！");
			}
		} catch (Exception e) {
			msg = "查询不到订单：" + order_no;
			log.error(msg);
		}
		
		Boolean lockFlag = false;
		final InterProcessMutex lock = new InterProcessMutex(ZookeeperFactoryBean.zkClient, GenerateNodePath.generateWithdrawNofifyLock(loginName));
		try {
			lockFlag = lock.acquire(Integer.parseInt(ConfigUtil.getValue("zk.lock.timeout")), TimeUnit.SECONDS);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("订单：" + loginName + "获取锁发生异常，异常信息：" + e.getMessage());
			lockFlag = true;
		}
		
		try {
			if (lockFlag) {
				log.info(product+"正在处理订单" + order_no);
				msg = withdrawService.resubmitFPay(order_no,operator,ip,product);
				log.info("处理完成订单" + order_no + "  " + msg) ;
			} else {
				
				log.error("订单：" + loginName + "未能获取锁，无法执行请求对应的方法....");
				msg = "[提示]系统繁忙，请稍后重试！";
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("执行玩家：" + loginName + "请求对应的方法发生异常，异常信息：" + e.getMessage());
			msg = "[提示]系统繁忙，请稍后重试！";
		} finally {
			if (lockFlag) {
				try {
					lock.release();
				} catch (Exception e) {
					log.error("订单：" + loginName + "释放锁发生异常，异常信息：" + e.getMessage());
					msg = "[提示]系统繁忙，请稍后重试！";
				}
			}
		}
		return new DataTransferDTO(msg);
	}
	
	
	
    //更新銀行額度
    @ResponseBody
    @RequestMapping(value = "/updaBankCredit", produces = "application/json; charset=utf-8")
    public String updaBankCredit(String card_number, String card_balance,HttpServletRequest req) {
    	
    	//付款卡
		Map<String , Object> map = new HashMap<String , Object>();
		map.put("useable", "0");
		map.put("bankcard", card_number.trim());
		List<BankInfo> banks;
		try {
			banks = bankinfoService.findBankInfoList2(map);
		} catch (Exception e) {
			   log.error("查询卡信息异常：", e);
	          return "查询卡信息异常";
		}
		
		BankInfo bank = null ;
		if(null != banks && banks.size() > 0){
			bank = banks.get(0);
		}else{
			log.error("未配置付款卡"+card_number);
			return "未配置付款卡"+card_number;
		}

    	 //采用新方式 (更新银行额度)
      	Map<String, Object> params = new HashMap<String, Object>();
  		params.put("id", bank.getId());  		
  		params.put("bankAmount",Double.valueOf(card_balance));
  		params.put("updatetime", new Date());
  		try {
			bankinfoService.update2(params);
		} catch (Exception e) {
			log.error("更新远程银行账户失败，卡号："+card_number);
			return "更新远程银行账户失败，卡号："+card_number;
		}
		return "更新成功";
    }
	
}