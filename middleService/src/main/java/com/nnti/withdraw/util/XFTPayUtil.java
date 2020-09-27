package com.nnti.withdraw.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;

import com.nnti.common.exception.BusinessException;
import com.nnti.common.security.DigestUtils;
import com.nnti.common.utils.DateUtil;
import com.nnti.common.utils.MyWebUtils;

import net.sf.json.JSONObject;


/**
 * 信付通代付
 * 
 * @author
 * 
 */
public class XFTPayUtil {
	private static Logger log = Logger.getLogger(XFTPayUtil.class);
	
	
	private static Map<String, HashMap<String, String>> classMap = new HashMap<String, HashMap<String, String>>();
	static {
		
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("merId", "100000000003664");
		map.put("apikey", "xD1XpA1erpvS4N7x2WavUJ89B13BcV8jh4sWDOQGdeo0FiQObVBhex32K5RP9cfj");
		map.put("apiUrl", "https://client.xfuoo.com/agentPay/v1/batch/");
		map.put("apiUrl", "https://client.xfuoo.com/agentPay/v1/batch/");
		map.put("工商银行","中国工商银行");
		map.put("招商银行","招商银行");
		map.put("平安银行","平安银行");
		map.put("农业银行","中国农业银行");
		map.put("建设银行","中国建设银行");
		map.put("交通银行","交通银行");
		map.put("民生银行","中国民生银行");
		map.put("光大银行","中国光大银行");
		map.put("兴业银行","兴业银行");
		map.put("中国银行","中国银行");
		map.put("中信银行","中信银行");
		map.put("邮政银行","中国邮政储蓄银行");
		map.put("华夏银行","华夏银行");
		map.put("北京银行","北京银行");
		map.put("上海银行","上海银行");
		map.put("长沙银行","长沙银行");
		map.put("上海浦东发展银行","上海浦东发展银行");
		classMap.put("dy", map);
	} 
	

	public static Map<String, HashMap<String, String>> getClassMap() {
		return classMap;
	}


	public static void setClassMap(Map<String, HashMap<String, String>> classMap) {
		XFTPayUtil.classMap = classMap;
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
	public static String api_add_order(String orderNo, String amount, String bankName, String bankAddress,String accName, String cardNumber,String product,String depositType,String batchdate) throws BusinessException {
		log.info(orderNo+"|"+amount+"|"+ bankName+"|"+ bankAddress+"|"+accName+"|"+ cardNumber+"|"+product+"|"+depositType+"|"+batchdate);
		HashMap<String , String> map =  classMap.get(product);
		String apikey = map.get("apikey");
		String apiUrl = map.get("apiUrl");
		String merId = map.get("merId");
		bankName = map.get(bankName);
		log.info("信付通代付下单接口："+merId+"|"+apikey+"|"+apiUrl);
		
		//请求参数
		String batchAmount = amount;//总金额
		String batchBiztype = "00000";//提交批次类型，默认00000
		//将“序号,银行账户,开户名,开户行名称,分行,支行,公/私,金额,币种,省,市,手机号,证件类型,证件号,用户协议号,商户订单号,备注,手续费金额”按顺序排列，以英文逗号分隔
		//batchContent=1,6222111111111111111,王xx,上海浦东发展银行,分行,支行,0,1.5,CNY,广东省,深圳市,,,,,2018011517006,mark
		String batchContent = "1,"+cardNumber+","+accName+","+bankName+",分行,支行,0,"+amount+",CNY,广东省,深圳市,,,,,"+orderNo+",系统提交";//明细
		String batchCount = "1";//总笔数
		String batchDate = DateUtil.format(DateUtil.YYYYMMDD, new Date());//提交日期
		String batchNo = orderNo;//批次号，不能重复
		String batchVersion = "00";//版本号，固定值为00
		String charset = "UTF-8";//输入编码：GBK，UTF-8
		String merchantId = merId;//商户号 ID
		String bizType = "CUSTOMER";//商户类型 CUSTOMER商户 AGENT   代理商
		String merchantAlias = "大运";//商户别称
		String signType = "SHA";//签名方：SHA

		String param = "batchAmount="+batchAmount+"&batchBiztype="+batchBiztype+"&batchContent="+batchContent
					  +"&batchCount="+batchCount+"&batchDate="+batchDate+"&batchNo="+batchNo
					  +"&batchVersion="+batchVersion+"&charset="+charset+"&merchantId="+merchantId+apikey;
        String sign = SHA.sign(param, "SHA").toUpperCase();//签名
        log.info("签名：" + sign);
        
        Map<String,String> pays = new TreeMap<String,String>();
        pays.put("batchAmount",batchAmount);
        pays.put("batchBiztype",batchBiztype);
        pays.put("batchContent",batchContent);
        pays.put("batchCount",batchCount);
        pays.put("batchDate",batchDate);
        pays.put("batchNo",batchNo);
        pays.put("batchVersion",batchVersion);
        pays.put("charset",charset);
        pays.put("merchantId",merchantId);
        pays.put("bizType",bizType);
        pays.put("merchantAlias",merchantAlias);
        pays.put("signType",signType);
        pays.put("sign",sign);
        
    	
		String result = MyWebUtils.getHttpContentByParam(apiUrl+merchantId+"-"+batchNo, MyWebUtils.getListNamevaluepair(pays));
		log.info("订单号："+orderNo+"提交信付通代付返回结果："+result);
		JSONObject json = JSONObject.fromObject(result);
    	String code = json.getString("respCode");
    	//String msg = json.getString("respMessage");
		return code;
	}

	
	/***
	 * 查询订单
	 * @param orderNo
	 * @return
	 */
	public static String api_check_order(String orderNo,String product,String batchDate) {
		
		HashMap<String , String> map =  classMap.get(product);
		String apikey = map.get("apikey");
		String apiUrl = map.get("apiUrl");
		String merId = map.get("merId");
		log.info("信付通代付查询订单接口："+merId+"|"+apikey+"|"+apiUrl);

		//batchDate 提交日期
		String batchNo = orderNo;
		String batchVersion = "00";
		String charset = "UTF-8";
		String merchantId = merId;
		String signType = "SHA";//签名方：SHA
		
		String param = "batchDate="+batchDate+"&batchNo="+batchNo+"&batchVersion="+batchVersion+"&charset="+charset+"&merchantId="+merchantId;
		
		String sign = SHA.sign(param+apikey, "SHA").toUpperCase();//签名
		
        String msg = "处理中";
		try {
			String url = apiUrl+merchantId+"-"+batchNo+"?"+param+"&signType="+signType+"&sign="+sign;
			String result = httpget(url);
			log.info("订单号："+orderNo+"提交信付通代付查询返回结果："+result);
			JSONObject json = JSONObject.fromObject(result);
			if(json.containsKey("batchContent")){
				String batchContent = json.getString("batchContent");
				String[] obj = batchContent.split(",");
				String status = obj[12];
				if("成功".equals(status)){
					msg = status;
				}
				if("失败".equals(status)){
					msg = status;
				}
			}else{
				msg = "处理中";
			}
		} catch (Exception e) {
			log.error("Response 消息: " + e);
		}
		return msg;

	}
	
	private static HttpClient createHttpClient() {
		HttpClient httpclient = new HttpClient();
		HttpClientParams params = new HttpClientParams();
		params.setParameter("http.method.retry-handler", new DefaultHttpMethodRetryHandler(0, false));
		params.setParameter("http.protocol.content-charset", "UTF-8");
		params.setParameter("http.socket.timeout", 100000);
		httpclient.setParams(params);
		return httpclient;
	}
	
	/**
	 * 发送get请求
	 * @param method api名称
	 * @param parameters 参数
	 * @return
	 */
	private static String httpget(String url) {

		HttpClient httpClient = createHttpClient();
		GetMethod med = new GetMethod(url);
		med.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		BufferedReader reader = null;
		try {
			httpClient.executeMethod(med);
			reader = new BufferedReader(new InputStreamReader(med.getResponseBodyAsStream()));  
			StringBuffer stringBuffer = new StringBuffer();  
			String str = "";  
			while((str = reader.readLine())!=null){  
			   stringBuffer.append(str);  
			}
			String result = stringBuffer.toString();
			int responseCode = med.getStatusCode();
			log.info("请求的url:" + url);
			log.info("响应代码:" + responseCode);
			log.info("响应报文:"+result);
			if(responseCode != 200){
				return null;
			}
			return result;
		} catch (Exception e) {
			log.error("请求第三方异常:"+e.getMessage());
			e.printStackTrace();
		} finally {
			if (med != null) {
				med.releaseConnection();
			}
			if(reader != null){
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
       
   public static void main(String[] args) throws ParseException, BusinessException {
	
	   String batchdate = DateUtil.format(DateUtil.YYYYMMDD, new Date());
	   String orderid = "dy_20180918235646se8kb";
	   System.out.println("订单号："+orderid);
//	   String json=XFTPayUtil.api_add_order(orderid,"100",  "中国工商银行", "广东","王成香", "6212261602022220877","dy",null,batchdate);
//	   log.info(":返回结果：" + json);
	   System.out.println(XFTPayUtil.api_check_order(orderid,"dy",batchdate));
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