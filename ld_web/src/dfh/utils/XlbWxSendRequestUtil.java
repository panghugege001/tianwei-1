package dfh.utils;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;

import dfh.action.customer.DinpayAction;
import dfh.action.tpp.paramenum.XLBWXFormParamEnum;

/**
 * 支付
 */
public class XlbWxSendRequestUtil {
	private static Logger log = Logger.getLogger(XlbWxSendRequestUtil.class);
	
	
	@SuppressWarnings("deprecation")
	public static String sendRequest(Map<Enum, Object> map) {
		HttpClient httpClient = HttpUtils.createHttpClient();
//		PostMethod method = new PostMethod("http://pay.suotonghaoyuan.cn/xlbWx1.asp");
		PostMethod method = new PostMethod(map.get(XLBWXFormParamEnum.url).toString());
		method.setRequestHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 5.1; rv:22.0) Gecko/20100101 Firefox/22.0");
		method.setRequestHeader("Connection", "close");
		method.setRequestHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.22 (KHTML, like Gecko) Chrome/25.0.1364.160 Safari/537.22");
		method.setRequestHeader("Referer", "http://pay.kalunrantu.cn/mbpaybank.asp");
		NameValuePair urlE = new NameValuePair( "url" , map.get(XLBWXFormParamEnum.url).toString());
		NameValuePair apiNameE = new NameValuePair( "apiName" , map.get(XLBWXFormParamEnum.apiName).toString());
		NameValuePair apiVersionE = new NameValuePair( "apiVersion" , map.get(XLBWXFormParamEnum.apiVersion).toString());
		NameValuePair platformIDE = new NameValuePair( "platformID" , map.get(XLBWXFormParamEnum.platformID).toString());
		NameValuePair merchNoE = new NameValuePair( "merchNo" , map.get(XLBWXFormParamEnum.merchNo).toString());
		NameValuePair orderNoE = new NameValuePair( "orderNo" , map.get(XLBWXFormParamEnum.orderNo).toString());
		NameValuePair tradeDateE = new NameValuePair( "tradeDate" , map.get(XLBWXFormParamEnum.tradeDate).toString());
		NameValuePair amtE = new NameValuePair( "amt" , map.get(XLBWXFormParamEnum.amt).toString());
		NameValuePair merchUrlE = new NameValuePair( "merchUrl" , map.get(XLBWXFormParamEnum.merchUrl).toString());
		NameValuePair merchParamE = new NameValuePair( "merchParam" , map.get(XLBWXFormParamEnum.merchParam).toString());
		NameValuePair tradeSummaryE = new NameValuePair( "tradeSummary" , map.get(XLBWXFormParamEnum.tradeSummary).toString());
		NameValuePair signMsgE = new NameValuePair( "signMsg" , map.get(XLBWXFormParamEnum.signMsg).toString());
		NameValuePair choosePayTypeE = new NameValuePair( "choosePayType" , map.get(XLBWXFormParamEnum.choosePayType).toString());
		NameValuePair bankCodeE = new NameValuePair( "bankCode" , map.get(XLBWXFormParamEnum.bankCode).toString());
		NameValuePair overTimeE = new NameValuePair( "overTime" , map.get(XLBWXFormParamEnum.overTime).toString());
		NameValuePair customerIPE = new NameValuePair( "customerIP" , map.get(XLBWXFormParamEnum.customerIP).toString());
		method.setRequestBody( new NameValuePair[]{apiNameE , apiVersionE , platformIDE , merchNoE , orderNoE , tradeDateE , amtE , 
				merchUrlE , merchParamE , tradeSummaryE , signMsgE,choosePayTypeE,bankCodeE,overTimeE,customerIPE});
		String result = "";
		try {
			httpClient.executeMethod(method);
			result = method.getResponseBodyAsString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (method != null) {
				method.releaseConnection();
			}
		}
//		log.info(result);
		return result;
	}
	
	public static String exportQrcode(String info){
		try {
			String resp_code = compileVerifyData("<respCode>(.*?)</respCode>", info) ;
			String resp_desc = compileVerifyData("<respDesc>(.*?)</respDesc>", info) ;
			String signMsg = compileVerifyData("<signMsg>(.*?)</signMsg>", info) ;
			String respData = "<respData>"+compileVerifyData("<respData>(.*?)</respData>",info)+"</respData>";
			if(resp_code.equals("00") && signMsg.equals(DinpayAction.signByMD5(respData, XLBWXFormParamEnum.key.getValue().toString()))){
				//解密 , 验签
				String qrcode = compileVerifyData("<codeUrl>(.*?)</codeUrl>", info) ;
					String codeUrl  = new String(Base64Util.decryptBASE64(qrcode),"UTF-8") ;
				return codeUrl ;
			}else{
				log.info("智付微信生成二维码失败resp_desc:"+resp_code+"  resp_desc:" + resp_desc);
				return null ;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null ;
	}
	
	public  static String compileVerifyData(String pattern,String verifyData){
    	Pattern p=Pattern.compile(pattern);
    	Matcher m=p.matcher(verifyData);
    	String result = "";
    	while(m.find()){
    		result = m.group(1);
        }
    	return result;
    }
	
	public static void main(String[] args) {
		String msg = "<?xml version=\"1.0\" encoding=\"utf-8\" ?><moboAccount><respData><respCode>00</respCode><respDesc>交易完成</respDesc><codeUrl>d2VpeGluOi8vd3hwYXkvYml6cGF5dXJsP3ByPW1WMmJzckg=</codeUrl></respData><signMsg>8BEA970D161C5668DE67253DDA417FBD</signMsg></moboAccount>";
//		System.out.println(exportQrcode(msg));;
		try {
			String respData = "<respData>"+compileVerifyData("<respData>(.*?)</respData>",msg)+"</respData>";
			System.out.println(respData);
			System.out.println(DinpayAction.signByMD5(respData, XLBWXFormParamEnum.key.getValue().toString()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
