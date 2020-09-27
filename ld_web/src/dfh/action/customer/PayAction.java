package dfh.action.customer;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import dfh.utils.*;
import org.apache.commons.lang.NullArgumentException;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import dfh.action.SubActionSupport;
import dfh.model.OnlineToken;
import dfh.model.Users;
import dfh.model.common.Dictionary;
import dfh.model.common.Response;
import dfh.model.common.ResponseEnum;
import dfh.model.common.ReturnVo;
import dfh.model.pay.PayRequestVo;
import dfh.model.pay.PayWayVo;

/**
 * Created by wander on 2017/3/2.
 */
public class PayAction extends SubActionSupport {

    private Logger log = Logger.getLogger(PayAction.class);

    private String error_info;
    private Response message = new Response();
    private List<PayWayVo> payWayVos;

    private String PAY_CENTER_URL = AxisUtil.PAY_CENTER_URL;

    /*** 用户登陆名*/
    private String loginName;
    /*** 支付平台ID */
    private Long platformId;
    /*** 使用平台 1.pc 2.web */
    private Integer usetype;
    /*** 充值金额*/
    private String orderAmount;
    /*** 银行编码*/
    private String bankCode;
    /*** 点卡类型*/
    private String cardCode;
    /*** 点卡号码*/
    private String cardNo;
    /*** 点卡密码*/
    private String cardPassword;
    
    /*** 银行卡号 */
	private String bankcard;
	/*** 银行卡户名 */
	private String bankname;
	/*** 手机号码 */
	private String phoneNumber;

    private String payUrl;
    
    //PT客户端使用
  	private String token ;
    
    /**
	 * 登录游戏验证
	 * 
	 * @return
	 */
	public String gameLogin() {
		try {
			Users user = (Users) this.getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
			// 登录成功过后才能登录游戏
			if (user == null) { //pt client
				OnlineToken onlineToken = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getToken", new Object[] { token }, OnlineToken.class);
				if(null != onlineToken && ((new Date().getTime() - DateUtil.parseDateForStandard(onlineToken.getTempCreatetime()).getTime()  ) <= 1000*60*3)){
					user = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "getUser", new Object[] { onlineToken.getLoginname() }, Users.class);
					getHttpSession().setAttribute(Constants.SESSION_CUSTOMERID, user);
				}
			}
			// 代理不能登录邮箱
			if (user.getRole().equals("AGENT")) {
				return ERROR;
			}
			return SUCCESS;
		} catch (Exception e) {
			return ERROR;
		}
	}
    
    // 支付页面选择
    public String payPage() {

        try {
            Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
            String tab_id = getRequest().getParameter("tab_id");
            // 检测用户是否登录
            if (user == null) {
                setError_info("[提示]你的登录已过期，请从首页重新登录");
                return ERROR;
            }
            // 代理不能使用在线支付
            if (!user.getRole().equals("MONEY_CUSTOMER")) {
                setError_info("[提示]代理不能使用在线支付！");
                return ERROR;
            }

            getRequest().setAttribute("tab_id", tab_id);
        } catch (Exception e) {
            return ERROR;
        }
        return "payPage";
    }

    /*** 给手机端返回 */
    public String pay_way() {
        try {
            if (usetype != null) {
                _pay_way(usetype);
            } else {
                _pay_way(1);
            }

            writeText(JSON.writeValueAsString(message));
        } catch (JsonProcessingException e1) {
            log.error("异常：" + e1);
            writeText("{\"code\":\"9999\",\"desc\":\"系统异常\",\"}");
        }
        return null;
    }

    /*** 返回对应银行编号 */
    public String pay_bank() {

        String platformId = getRequest().getParameter("platformId");
        try {
            _pay_bank(platformId);
            writeText(JSON.writeValueAsString(message));
        } catch (Exception e1) {
            log.error("异常：" + e1);
            writeText("{\"code\":\"9999\",\"desc\":\"系统异常\",\"}");
        }
        return null;
    }

    private Response _pay_bank(String platformId) throws Exception {

        String result = MyWebUtils.getHttpContentByParam(AxisUtil.PAY_CENTER_URL + "/pay/pay_bank",
                MyWebUtils.getListNamevaluepair("platformId", platformId));

        message = JSON.readValue(result, Response.class);
        if (null != message.getData() && !"".equals(message.getData())) {
            result = AESUtil.decrypt(message.getData().toString());

            List<Dictionary> dictionaries = JSON.readValue(result, new TypeReference<List<Dictionary>>() {
            });
            message.setData(dictionaries);
        }

        return message;
    }

    private Response _pay_way(int usetype) {
        try {
        	Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
        	if (user == null) { 
        		log.error("登录超时!");
        		throw new RuntimeException("登录超时!");
        	}
        	String result = HttpUtils.get(AxisUtil.PAY_CENTER_URL + "/pay/pay_way?usetype=" + usetype + "&loginname=" + user.getLoginname());
            message = JSON.readValue(result, Response.class);
            if (null != message.getData() && !"".equals(message.getData())) {
                result = AESUtil.decrypt(message.getData().toString());

                payWayVos = JSON.readValue(result, new TypeReference<List<PayWayVo>>() {
                });
                message.setData(payWayVos);
            }
        } catch (Exception e) {
            log.error("系统异常：", e);
            message.setResponseEnum(ResponseEnum.SERVER_ERROR);
        }

        return message;
    }

    /*** 快捷支付 输入界面 */
    public String quick() {
        Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
        // 检测用户是否登录
        if (user == null) {
            message.setResponseEnum(ResponseEnum.LOGIN_OVER);
            return ERROR;
        }
        return SUCCESS;
    }

    /*** 点卡支付 输入界面 */
    public String point_card() {
        Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
        // 检测用户是否登录
        if (user == null) {
            message.setResponseEnum(ResponseEnum.LOGIN_OVER);
            return ERROR;
        }
        return SUCCESS;
    }
    
    /*** 点卡支付 输入界面 */
    public String point_card2() {
        Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
        // 检测用户是否登录
        if (user == null) {
            message.setResponseEnum(ResponseEnum.LOGIN_OVER);
            return ERROR;
        }
        return SUCCESS;
    }
    
    public String pay_api_json()
    {
      Users user = (Users)getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);

      if (user == null) {
        this.message.setResponseEnum(ResponseEnum.LOGIN_OVER);
        writeText("{\"code\":\"9999\",\"desc\":\"" + ResponseEnum.LOGIN_OVER.getDesc() + "\",\"}");
      }
      PayRequestVo payVo = new PayRequestVo();
      try {
        System.out.println("platformId:" + this.platformId + "loginName:" + this.loginName + "orderAmount:" + this.orderAmount + "usetype" + this.usetype + "bankcard:" + this.bankcard + "bankname:" + this.bankname);
        validatePay(this.platformId, this.loginName, this.orderAmount, this.usetype);
        payVo.setParam(this.platformId, this.loginName, this.orderAmount, this.usetype, this.bankCode, this.cardNo, this.cardCode, this.cardPassword, this.payUrl, this.bankcard, this.bankname, this.phoneNumber);
        payVo.setCustomerIp(getIp());
        payVo.setDomain(getHost());

        ReturnVo returnVo = sendHttpByAES(AxisUtil.PAY_CENTER_URL + payVo.getPayUrl(), payVo);
        System.out.println("返回参数：" + returnVo.getParams());

        writeText(this.JSON.writeValueAsString(returnVo));
      } catch (Exception e1) {
        this.log.error("异常：" + e1);
        writeText("{\"code\":\"9999\",\"desc\":\"系统异常\",\"}");
      }
      return null;
    }

    /*** 支付接口 调用支付接口 */
    public String pay_api() {
        Users user = (Users) getHttpSession().getAttribute(Constants.SESSION_CUSTOMERID);
        // 检测用户是否登录
        if (user == null) {
            message.setResponseEnum(ResponseEnum.LOGIN_OVER);
            return ERROR;
        }
        try {
            validatePay(platformId, loginName, orderAmount, usetype);
            PayRequestVo payVo = new PayRequestVo();
        	payVo.setParam(platformId, loginName, orderAmount, usetype, bankCode, cardNo, cardCode, cardPassword,
					payUrl,bankcard,bankname,phoneNumber);
            payVo.setCustomerIp(getIp());
            payVo.setDomain(getHost());
            Gson gson = new Gson();
            System.out.println("============="+gson.toJson(payVo)+"=====================");
            String url = AxisUtil.PAY_CENTER_URL + payVo.getPayUrl();
            ReturnVo returnVo = sendHttpByAES(url, payVo);
            String type = returnVo.getType();
            if ("1".equals(type)) {
                return "pay_redirect";
            } else if ("2".equals(type) || "3".equals(type)) {
                return "pay_qrcode";
            } else if ("4".equals(type)) {
                if (null != returnVo.getData() && !"".equals(returnVo.getData())) {
                    message.setDesc(returnVo.getData());
                }
            }else if ("5".equals(type)) {   
                return "pay_jaqrcode";
            }
            else if("7".equals(type)) {
                return "jhf_pay_redirect";
            }
            else if("8".equals(type)) {
                return "qe_pay_redirect";
            }
            else if("9".equals(type)){
                return "third_redirect";
            }
        } catch (Exception e) {
            log.error("支付接口异常：", e);
            message.setResponseEnum(ResponseEnum.SERVER_ERROR);
        }
        return "success";
    }

    private void validatePay(Long platformId, String loginName, String orderAmount, Integer usetype) {
        if (platformId == null || null == loginName || orderAmount == null || usetype == null || "".equals(loginName) || "".equals(orderAmount)) {
            throw new NullArgumentException("请求的字段存在字段未空!");
        }
    }

    public String db_zfb_wx_return() {
        String url = PAY_CENTER_URL + "/db/zfb_wx_return";
        writeText(httpPost(url));
        return null;
    }
    
    // 新码
 	public String xm_zfb_wx_return() {
 		String url = PAY_CENTER_URL + "/xm/zfb_wx_return.php";
 		writeText(httpPostXm(url));
 		return null;
 	}
 	
    // 亿付
 	public String yifu_zfb_wx_return() {
 		log.info("yifu_zfb_wx_return:亿付在线支付回调");
 		log.info("\n\n\n");
 		String url = PAY_CENTER_URL + "/yifu/zfb_wx_return";
 		writeText(httpPost(url));
 		return null;
 	}
    


    public String dinpay_zfb_wx_return() {
        String url = PAY_CENTER_URL + "/dinpay/zfb_wx_return";
        writeText(httpPost(url));
        return null;
    }

    public String dinpay_online_pay_return() {
        String url = PAY_CENTER_URL + "/dinpay/online_pay_return";
        writeText(httpPost(url));
        return null;
    }

    public String dinpay_point_card_return() {
        String url = PAY_CENTER_URL + "/dinpay/point_card_return";
        writeText(httpPost(url));
        return null;
    }

    public String kd_zfb_wx_return() {
        String url = PAY_CENTER_URL + "/kd/zfb_wx_return";
        writeText(httpPost(url));
        return null;
    }

    public String hc_online_pay_return() {
        String url = PAY_CENTER_URL + "/hc/online_pay_return";
        writeText(httpPost(url));
        return null;
    }

    public String qw_zfb_wx_return() {
        String url = PAY_CENTER_URL + "/qw/zfb_wx_return";
        writeText(httpPost(url));
        return null;
    }

    public String xb_zfb_wx_return() {
        String url = PAY_CENTER_URL + "/xb/zfb_wx_return";
        log.error("xb_zfb_wx_return:" + url);
        writeText(httpPost(url));
        return null;
    }

    public String xft_zfb_wx_return() {
        String url = PAY_CENTER_URL + "/xft/zfb_wx_return";
        writeText(httpPost(url));
        return null;
    }

    public String xlb_zfb_wx_return() {
        String url = PAY_CENTER_URL + "/xlb/zfb_wx_return";
        writeText(httpPost(url));
        return null;
    }

    public String xlb_online_pay_return() {
        String url = PAY_CENTER_URL + "/xlb/online_pay_return";
        writeText(httpPost(url));
        return null;
    }

    public String yf_zfb_wx_return() {
        String url = PAY_CENTER_URL + "/yf/zfb_wx_return";
        writeText(httpPost(url));
        return null;
    }

    public String jhz_zfb_wx_return() {
        String url = PAY_CENTER_URL + "/jhz/zfb_wx_return";
        writeText(httpPost(url));
        return null;
    }

    public String th_zfb_wx_return() {
        String url = PAY_CENTER_URL + "/th/zfb_wx_return";
        writeText(httpPost(url));
        return null;
    }
    public String rx_zfb_wx_return() {
        String url = PAY_CENTER_URL + "/rx/zfb_wx_return";
        writeText(httpPost(url));
        return null;
    }
    
    public String shb_zfb_wx_return() {
        String url = PAY_CENTER_URL + "/shb/zfb_wx_return";
        writeText(httpPost(url));
        return null;
    }

    public String third_return() {
        String rt = getRequest().getParameter("rt");
        String url = PAY_CENTER_URL + rt;
        writeText(httpPost(url));
        return null;
    }

    public String httpPost(String url) {
        String result;
        try {
            Map param = MyWebUtils.getRequestParameters(getRequest());
            param.put("requestIp", getIp());
            result = MyWebUtils.getHttpContentByParam(url, MyWebUtils.getListNamevaluepair(param));

        } catch (Exception e) {
            log.error("回掉支付中心异常：", e);
            result = ERROR;
        }
        return result;
    }
    
    private static ObjectMapper mapper = new ObjectMapper();
    // 新码支付
 	public String httpPostXm(String url) {
 		String result;
 		try {

 			String jsonData = MyWebUtils.readReqStr(getRequest());

 			log.error("新码接收参数：" + jsonData);
 			Map<String, String> param = new HashMap<String, String>();
 			param = mapper.readValue(jsonData, Map.class);
 			String createTime = param.get("createTime");
 			String status = param.get("status");
 			String nonceStr = param.get("nonceStr");
 			String resultDesc = param.get("resultDesc");
 			String outTradeNo = param.get("outTradeNo");
 			String sign = param.get("sign");
 			String productDesc = param.get("productDesc");
 			String orderNo = param.get("orderNo");
 			String branchId = param.get("branchId");
 			String attachContent = param.get("attachContent");
 			String resultCode = param.get("resultCode");
 			String resCode = param.get("resCode");
 			String payType = param.get("payType");
 			String resDesc = param.get("resDesc");
 			String orderAmt =  String.valueOf(param.get("orderAmt"));
 			String requestIp = param.get(getIp());

 			Map param2 = new HashMap();
 			param2.put("createTime", createTime);
 			param2.put("status", status);
 			param2.put("nonceStr", nonceStr);
 			param2.put("resultDesc", resultDesc);
 			param2.put("outTradeNo", outTradeNo);
 			param2.put("sign", sign);
 			param2.put("productDesc", productDesc);
 			param2.put("orderNo", orderNo);
 			param2.put("branchId", branchId);
 			param2.put("attachContent", attachContent);
 			param2.put("resultCode", resultCode);
 			param2.put("resCode", resCode);
 			param2.put("payType", payType);
 			param2.put("resDesc", resDesc);
 			param2.put("orderAmt", orderAmt);
 			param2.put("requestIp", requestIp);

 			log.error("param1:" + param);
 			log.error("param2:" + param2);
 			result = MyWebUtils.getHttpContentByParam(url, MyWebUtils.getListNamevaluepair(param2));
 			log.error("result:" + result);
 		} catch (Exception e) {
 			log.error("回掉支付中心异常：", e);
 			result = ERROR;
 		}
 		return result;
 	}

    

    /*** AES加密 支付发起请求 */
    protected ReturnVo sendHttpByAES(String url, PayRequestVo vo) throws Exception {
        String responseString;
        ReturnVo returnVo = new ReturnVo();

        String requestJson = JSON.writeValueAsString(vo);
        requestJson = AESUtil.encrypt(requestJson);

        responseString = MyWebUtils.getHttpContentByParam(url, MyWebUtils.getListNamevaluepair("requestData", requestJson));

        message = JSON.readValue(responseString, Response.class);

        if (message.getData() != null && !"".equals(message.getData())) {
            String decrypt = AESUtil.decrypt(message.getData().toString());

            returnVo = JSON.readValue(decrypt, ReturnVo.class);

            message.setData(returnVo);
        }

        return returnVo;
    }

    public String getError_info() {
        return error_info;
    }

    public void setError_info(String error_info) {
        this.error_info = error_info;
    }

    public Response getMessage() {
        return message;
    }

    public void setMessage(Response message) {
        this.message = message;
    }

    public List<PayWayVo> getPayWayVos() {
        return payWayVos;
    }

    public void setPayWayVos(List<PayWayVo> payWayVos) {
        this.payWayVos = payWayVos;
    }

    public Integer getUsetype() {
        return usetype;
    }

    public void setUsetype(Integer usetype) {
        this.usetype = usetype;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public Long getPlatformId() {
        return platformId;
    }

    public void setPlatformId(Long platformId) {
        this.platformId = platformId;
    }

    public String getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(String orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getCardCode() {
        return cardCode;
    }

    public void setCardCode(String cardCode) {
        this.cardCode = cardCode;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getCardPassword() {
        return cardPassword;
    }

    public void setCardPassword(String cardPassword) {
        this.cardPassword = cardPassword;
    }

    public String getPayUrl() {
        return payUrl;
    }

    public void setPayUrl(String payUrl) {
        this.payUrl = payUrl;
    }
    
    public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getBankcard() {
		return bankcard;
	}

	public void setBankcard(String bankcard) {
		this.bankcard = bankcard;
	}

	public String getBankname() {
		return bankname;
	}

	public void setBankname(String bankname) {
		this.bankname = bankname;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	
}
