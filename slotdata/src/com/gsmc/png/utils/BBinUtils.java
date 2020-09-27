package com.gsmc.png.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;

import com.gsmc.png.model.BbinData;

public class BBinUtils {
	private static Logger log = Logger.getLogger(BBinUtils.class);
	
	private static final String USERKEY="45erq1@98$";
	private static final String AGENT = "dayun";
	private static final String ENCODE_TYPE = "UTF-8";
	private static final String REPORTURL = "http://getdata.tcy789.com/getdata.aspx";
	
    
    public static String matcher(String pattern, String verifyData) {

        verifyData = verifyData.replaceAll("[\r\n]", "");

        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(verifyData);
        String result = "";
        while (m.find()) {
            result += m.group(1) + ",";
        }
        if (result.length() > 1) {
            return result.substring(0, result.length() - 1);
        }
        return result;
    }
    //是否拼接value为空的key
	public static <T> String obj2UrlParam(Map<String,Object> map) {
		StringBuilder and = new StringBuilder();

		for (Map.Entry<String,Object> param : map.entrySet()) {
            if (and.length() != 0){
            	and.append('$');
            }
            and.append(param.getKey());
            and.append('=');
            and.append(param.getValue());
        }

		return and.toString();
	}
	
	/**
	 * 发送get请求
	 * @param method api名称
	 * @param parameters 参数
	 * @return
	 */
	private static String sendApi(String url) {

		HttpClient httpClient = HttpUtils.createHttpClient();
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
			//log.info("响应报文:"+result);
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
	
	
	//bbin投注记录
	/**
	 * 
	 * @param vendorid
	 * @param method gebrbv电子 gfbrbv捕鱼 gbrbv真人 gsbrbvbbin体育 glbrbv彩票
	 * @return
	 */
	public static List<BbinData> GetBettingRecord(BigInteger vendorid,String method){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("agent", AGENT);
		map.put("moneysort", "RMB");
		if("gsbrbvbbin".equals(method)){
			map.put("isjs", 1);//是否结算,1 表示结算,0 表示未结算
		}
		map.put("vendorid", vendorid);
		map.put("method", method);
		
		String params = obj2UrlParam(map);
		try {
			params = new String(Base64.encodeBase64(params.getBytes(ENCODE_TYPE)));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
		String key = DigestUtils.md5Hex(params+USERKEY).toLowerCase();
		String geturl = REPORTURL+"?params="+params+"&key="+key;
		String result = sendApi(geturl);
		//System.out.println(result);
		if (StringUtil.isNotBlank(result)) {
			String status = matcher("<result>(.*?)</result>", result);
			if("No_Data".equals(status)){
				return null;
			}
			return parseXmlToEle(result,method);
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	public static List<BbinData> parseXmlToEle(String xml,String type) {
		xml = StringUtils.trimToEmpty(xml);
		List<BbinData> list = new ArrayList<BbinData>();
		Document doc = DomOperator.string2Document(xml);
		if (doc != null) {
			Element elementData = (Element) doc.getRootElement().selectSingleNode("//result");
			if(elementData == null){
				return list;
			}
			Iterator itTypes = elementData.elements("Data").listIterator();
			while (itTypes.hasNext()) {
				Element elemType = (Element) itTypes.next();
				Iterator it = elemType.elements("properties").listIterator();
				BbinData one = new BbinData();
				one.setType(type);
				while (it.hasNext()) {
					Element elem = (Element) it.next();
					if (elem.attributeValue("name").equals("ProductID")){
						Integer productID = Integer.parseInt(elem.getTextTrim());
						one.setProductID(productID);
					}else if (elem.attributeValue("name").equals("UserName")){
						String UserName = elem.getTextTrim();
						one.setUserName(UserName.substring(3));//玩家账号
					}else if (elem.attributeValue("name").equals("OrderNumber")){
						String orderNumber = elem.getTextTrim();
						one.setOrderNumber(orderNumber);
					}else if (elem.attributeValue("name").equals("ResultType")){
						Integer resultType = Integer.parseInt(elem.getTextTrim());
						one.setResultType(resultType);
					}else if (elem.attributeValue("name").equals("BettingAmount")){
						Double bettingAmount = Double.parseDouble(elem.getTextTrim());
						one.setBettingAmount(bettingAmount);
					}else if (elem.attributeValue("name").equals("WinLoseAmount")){
						Double winLoseAmount = Double.parseDouble(elem.getTextTrim());
						one.setWinLoseAmount(winLoseAmount);
					}else if (elem.attributeValue("name").equals("Balance")){
						Double Balance = Double.parseDouble(elem.getTextTrim());
						one.setBalance(Balance);
					}else if (elem.attributeValue("name").equals("AddTime")){
						String AddTime = elem.getTextTrim();
						AddTime = AddTime.replaceAll("/", "-");
						one.setAddTime(DateUtil.parseDateForStandard(AddTime));
						one.setBallTime(DateUtil.parseDateForYYYYmmDD(AddTime));
					}else if (elem.attributeValue("name").equals("PlatformID")){
						String PlatformID = elem.getTextTrim();
						one.setPlatformID(PlatformID);
					}else if (elem.attributeValue("name").equals("VendorId")){
						BigInteger VendorId = BigInteger.valueOf(Long.parseLong(elem.getTextTrim()));
						one.setVendorId(VendorId);
					}else if (elem.attributeValue("name").equals("ValidAmount")){
						Double ValidAmount = Double.parseDouble(elem.getTextTrim());
						one.setValidAmount(ValidAmount);
					}else if (elem.attributeValue("name").equals("BallTime")){
						String BallTime = elem.getTextTrim();
						BallTime = BallTime.replaceAll("/", "-");
						one.setBallTime(DateUtil.parseDateForYYYYmmDD(BallTime));
					}
				}
				list.add(one);
			}
		} else {
			log.info("document parse failed:" + xml);
		}
		return list;
	}

	public static void main(String[] args) {
		//System.out.println(CheckAndCreateAccount(loginname));
		//System.out.println(transferToBbin(loginname,100.1,"123"));
		//System.out.println(transferFromBbin(loginname,221.0,System.currentTimeMillis()+"123"));
		//System.out.println(GetBalance(loginname));
		//System.out.println(ConfirmTransferCredit(loginname,"123"));
		//System.out.println(TransferGame(loginname, "www.dayunguoji.com"));
		//String datestart = "2018-05-01";
		//String dateend = "2018-05-12";
		//System.out.println(GetReport("", datestart,dateend,"electronic"));
		//String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?><result><Data><video><properties name=\"UserName\">dy8dytest01</properties><properties name=\"BettingTime\">3</properties><properties name=\"BettingAmount\">60.00</properties><properties name=\"BettingEffectiveAmount\">60.000000</properties><properties name=\"BettingLoseWin\">0.00</properties></video><video><properties name=\"UserName\">dy8dytest02</properties><properties name=\"BettingTime\">3</properties><properties name=\"BettingAmount\">60.00</properties><properties name=\"BettingEffectiveAmount\">60.000000</properties><properties name=\"BettingLoseWin\">0.00</properties></video><video><properties name=\"UserName\">dy8dytest03</properties><properties name=\"BettingTime\">3</properties><properties name=\"BettingAmount\">60.00</properties><properties name=\"BettingEffectiveAmount\">60.000000</properties><properties name=\"BettingLoseWin\">0.00</properties></video><video><properties name=\"UserName\">dy8dytest04</properties><properties name=\"BettingTime\">3</properties><properties name=\"BettingAmount\">60.00</properties><properties name=\"BettingEffectiveAmount\">60.000000</properties><properties name=\"BettingLoseWin\">0.00</properties></video></Data><Record><properties name=\"RecordCount\">4</properties></Record></result>";
		//System.out.println(parseXml(xml, "video"));
		//gebrbv电子 gfbrbv捕鱼 gbrbv真人 gsbrbvbbin体育 glbrbv彩票
		System.out.println(GetBettingRecord(new BigInteger("1"),"gebrbv"));
	}
}
