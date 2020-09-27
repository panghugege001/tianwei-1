package com.nnti.withdraw.util;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import com.nnti.common.exception.BusinessException;
import com.nnti.common.security.DigestUtils;
import com.nnti.common.utils.MyWebUtils;


/**
 * 畅汇代付
 * 
 * @author
 * 
 */
public class CHPayUtil {
	private static Logger log = Logger.getLogger(CHPayUtil.class);
	
	
	private static Map<String, HashMap<String, String>> classMap = new HashMap<String, HashMap<String, String>>();
	static {
		
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("merId", "CHANG1527741758731");
		map.put("apikey", "5ce0968b80544d18855e5200c9408be0");
		map.put("apiUrl", "http://gateway.xjdbu.com/controller.action");
		classMap.put("dy", map);
	} 
	

	public static Map<String, HashMap<String, String>> getClassMap() {
		return classMap;
	}


	public static void setClassMap(Map<String, HashMap<String, String>> classMap) {
		CHPayUtil.classMap = classMap;
	}
	
	
	


	/***
	 * 
	 * 代付 添加订单 Description
	 * 
	 * @param pno
	 * @param operator
	 * @param ip
	 * @param loginName
	 * @return
	 * @throws BusinessException 
	 */
	public static String api_add_order(String orderNo, String amount, String bankName, String bankAddress,String accName, String cardNumber,String product,String depositType) throws BusinessException {
		log.info(orderNo+"|"+amount+"|"+ bankName+"|"+ bankAddress+"|"+accName+"|"+ cardNumber+"|"+product+"|"+depositType);
		HashMap<String , String> map =  classMap.get(product);
		String apikey = map.get("apikey");
		String apiUrl = map.get("apiUrl");
		String merId = map.get("merId");
		log.info("畅汇代付下单接口："+merId+"|"+apikey+"|"+apiUrl);
		
		String p0_Cmd = "TransPay";
		String p1_MerId = merId;
		String p2_Order = orderNo;
		String p3_CardNo = cardNumber;
		String p4_BankName = bankName;
		String p5_AtName = accName;
		String p6_Amt = amount;
		String pb_CusUserId = "";
		String pc_NewType = "PRIVATE";//代付类型
		String pd_BranchBankName = "";//代付类型为对公时必填
		String pe_Province = "";
		String pf_City = "";
		String pg_Url = "";
		String param = p0_Cmd+p1_MerId+p2_Order+p3_CardNo+p4_BankName+p5_AtName+p6_Amt+pb_CusUserId+pc_NewType+pd_BranchBankName+pe_Province+pf_City+pg_Url;
        String hmac = DigestUtils.hmacSign(param,apikey);
        log.info("签名：" + hmac);
        Map<String,String> pays = new TreeMap<String,String>();
        pays.put("p0_Cmd",p0_Cmd);
        pays.put("p1_MerId",p1_MerId);
        pays.put("p2_Order",p2_Order);
        pays.put("p3_CardNo",p3_CardNo);
        pays.put("p4_BankName",p4_BankName);
        pays.put("p5_AtName",p5_AtName);
        pays.put("p6_Amt",p6_Amt);
        //pays.put("pb_CusUserId",pb_CusUserId);
        pays.put("pc_NewType",pc_NewType);
        //pays.put("pd_BranchBankName",pd_BranchBankName);
        //pays.put("pe_Province",pe_Province);
        //pays.put("pf_City",pf_City);
        //pays.put("pg_Url",pg_Url);
        pays.put("hmac",hmac);
        
    	
		String result = MyWebUtils.getHttpContentByParam(apiUrl, MyWebUtils.getListNamevaluepair(pays));
		log.info("订单号："+orderNo+"提交畅汇代付返回结果："+result);
		String[] resAll = result.split("\n");
		String[] res2 = resAll[2].split("=");
		String r1_Code = res2[1];
		if("0000".equals(r1_Code)){
			result = r1_Code;
		}else{
			String[] res3 = resAll[3].split("=");
			result = res3[1];
			//throw new BusinessException(ErrorCode.SC_10001.getCode(), result);
		}
		return result;
	}

	
	/***
	 * 查询订单
	 * @param orderNo
	 * @return
	 */
	public static String api_check_order(String orderNo,String product) {
		
		HashMap<String , String> map =  classMap.get(product);
		String apikey = map.get("apikey");
		String apiUrl = map.get("apiUrl");
		String merId = map.get("merId");
		log.info("畅汇代付查询订单接口："+merId+"|"+apikey+"|"+apiUrl);

		String p0_Cmd = "TransQuery";
		String p1_MerId = merId;
		String p2_Order = orderNo;
		
		String param = p0_Cmd+p1_MerId+p2_Order;
        String hmac = DigestUtils.hmacSign(param,apikey);
		
		Map<String,String> pays = new TreeMap<String,String>();
        pays.put("p0_Cmd",p0_Cmd);
        pays.put("p1_MerId",p1_MerId);
        pays.put("p2_Order",p2_Order);
        pays.put("hmac", hmac);
        String result = "";
		try {
			result = MyWebUtils.getHttpContentByParam(apiUrl, MyWebUtils.getListNamevaluepair(pays));
		} catch (Exception e) {
			log.error("Response 消息: " + e);
		} finally {
			
		}
		return result;

	}
       
   public static void main(String[] args) throws ParseException, BusinessException {
	
	   String orderid = "dy_20180813045415tfo5c";
//	   System.out.println("订单号："+orderid);
	   String json=CHPayUtil.api_add_order(orderid,"0.1",  "邮政储蓄银行", "广东","王", "6217995200221398917","dy",null);
	   log.info(":返回结果：" + json);
//	   System.out.println(CHPayUtil.api_check_order("DY1533822350227","dy"));
	}
 
 
    

    /**
	 * 错误码
	 * 代码目前是4位，其中前两位代表业务，后两位代表此业务的结果信息。
	 */
	public static enum ErrCode {
		C0000("0000", "成功"),
		C0301("2001", "Ip白名单不存在"),
		C0302("2002", "参数为空"),
		C0303("0303", "提款失败"),
		C0304("0304", "商户提款订单时间不正确,以yyyyMMddhhmmss 格式的时间字符串"),
		C0305("0305", "即时交易：订单状态不为已成功，不能进行提款"),
		C0306("0306", "商户提款订单处理完成，请勿重复提款"),
		C0309("0309", "商户提款订单金额不正确"),
		C0311("0311", "提款失败,提款金额大于支付余额"),
		C0312("0312", "提款请求序列号已经存在"),
		C0313("0313", "商户提款订单号已经存在"),
		C0314("0314", "报文验签失败"),
		C0315("0315", "报文验签失败"),
		C0316("0316", "商户止入或者止出"),
		C0317("0317", "提款逻辑验证失败"),
		C0318("0318", "更新余额异常"),
		C0319("0319", "手续费用计算失败"),
		C0320("0320", "提款订单创建失败"),
		C0322("0322", "网关订单异常"),
		C0324("0324", "构建提款订单异常"),
		C0325("0325", "网关订单记帐成功修改提款订单失败"),
		C0326("0326", "报文参数错误"),
		C0328("0328", "提款金额小于等于0"),
		C0329("0329", "商户提款订单处理完成，请勿重复提款"),
		C0332("0332", "请求版本不正确"),
		C0401("0401", "基本数据校验失败"),
		C0402("0402", "校验业务参数失败"),
		C0403("0403", "查询商户请求网关历史记录失败"),
		C0404("0404", "查询API-失败"),
		C0405("0405", "时间验证失败"),
		C0406("0406", "时间转换类型失败"),
		C0407("0407", "开始时间不能大于结束时间"),
		C0408("0408", "查询时间最大范围只能15天以内"),
		C0409("0409", "创建网关返回商户结果失败"),
		C0410("0410", "查询验签失败"),
		C0411("0411", "查询订单不存在"),
		C0009("0009", "订单不存在");

		
		
		private String code;
		private String text;

		private ErrCode(String code, String text) {
			this.code = code;
			this.text = text;
		}

		public String getCode() {
			return code;
		}
		
		public String getText() {
			return text;
		}

	}

}