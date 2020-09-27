package com.gsmc.png.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.gsmc.png.model.AgData;



public class AGINUtil{

	private static Logger log = Logger.getLogger(AGINUtil.class);

	private static final String datacagent = "DF4";
	private static final String pidtoken = "E6A862E1EFBB0862F4558933285BA1F5";
	private static final String agin_dataurl = "http://dxcnf4.gdcapi.com:3333";
	private static final String fish_dataurl="http://hdxcnf4.gdcapi.com:7733";
	private static final Integer perpage =500;
	

	public static String parseDspResponseRequest(String xml) {

		xml = StringUtils.trimToEmpty(xml);

		if (StringUtils.isBlank(xml)) {

			log.error("系统繁忙，AGIN无响应，请稍后再试！");

			return null;
		}

		Document doc = string2Document(xml);

		if (doc != null) {

			Element element = (Element) doc.getRootElement();
			String info = element.attributeValue("info");
			String msg = element.attributeValue("msg");

			if (info.equals("account_not_exit")) {

				log.error("帐号不存在！");

				return null;
			} else if (info.equals("error") || info.equals("network_error")) {

				log.error("AGIN返回信息："+msg);

				return null;
			} else if (info.equals("not_enough_credit")) {

				log.error("转账失败，额度不足！");

				return null;
			} else if (info.equals("duplicate_transfer")) {

				log.error("重复转账！");

				return null;
			}

			return info;
		} else {

			log.error("document parse failed：" + xml);

			return null;
		}
	}

	public static Document string2Document(String s) {

		Document doc = null;

		try {

			doc = DocumentHelper.parseText(s);
		} catch (Exception ex) {

			ex.printStackTrace();
		}

		return doc;
	}
	
	/**
	 * 发送get请求
	 * @param method api名称
	 * @param parameters 参数
	 * @return
	 */
	private static String doGet(String url) {

		HttpClient httpClient = HttpUtils.createHttpClient();
		GetMethod med = new GetMethod(url);
		med.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		BufferedReader reader = null;
		try {
			med = new GetMethod(url);
			med.setRequestHeader("Connection", "close");
			
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
	
	// 查询指定时间段内玩家的投注额
	public static List<AgData>  getAginorders(String startdate, String enddate,Integer page) {
		List<AgData> list = new ArrayList<AgData>();
		String order = "reckontime";
		String by = "DESC";
		String key = EncryptionUtil.encryptPassword(datacagent + startdate+ enddate +order+by+page+perpage+ pidtoken);
		startdate = startdate.replace(" ", "%20");
		enddate = enddate.replace(" ", "%20");
		String url = agin_dataurl + "/getorders.xml?cagent="+datacagent+"&startdate="+startdate+"&enddate="+enddate+
				"&order=reckontime&by=DESC&page="+page+"&perpage="+perpage+"&key=" + key;
		String result = doGet(url);
		
		if (StringUtil.isNotBlank(result)) {
			String status = matcher("<info>(.*?)</info>", result);
			//System.out.println(status);
			if(!"0".equals(status)){
				return list;
			}
			list = parseXmlToAgin(result);
		}

		return list;
	}
	
	@SuppressWarnings("rawtypes")
	public static List<AgData> parseXmlToAgin(String xml) {
		List<AgData> list = new ArrayList<AgData>();
		try {
			Document doc = DomOperator.string2Document(xml);
			if (doc != null) {
				Element elementData = (Element) doc.getRootElement().selectSingleNode("//result");
				if(elementData == null){
					return list;
				}
				Iterator itTypes = elementData.elements("row").listIterator();
				while (itTypes.hasNext()) {
					Element elemType = (Element) itTypes.next();
					Iterator it = elemType.attributeIterator();
					AgData one = new AgData();
					boolean flag = true;
					while (it.hasNext()) {
						Attribute attr = (Attribute) it.next();
						String name = attr.getName();
						if (name.equalsIgnoreCase("billNo")){
							String billNo = attr.getText();
							one.setBillNo(billNo);
						}else if (name.equalsIgnoreCase("gameCode")){
							String gameCode = attr.getText();
							one.setGameCode(gameCode);
						}else if (name.equalsIgnoreCase("playName")){
							String playName = attr.getText();
							one.setPlayName(playName.substring(3));
						}else if (name.equalsIgnoreCase("betAmount")){
							String betAmount = attr.getText();
							one.setBetAmount(Double.parseDouble(betAmount));
						}else if (name.equalsIgnoreCase("validBetAmount")){
							String validBetAmount = attr.getText();
							one.setValidBetAmount(Double.parseDouble(validBetAmount));
						}else if (name.equalsIgnoreCase("netAmount")){
							String netAmount = attr.getText();
							one.setNetAmount(Double.parseDouble(netAmount));
						}else if (name.equalsIgnoreCase("beforeCredit")){
							String beforeCredit = attr.getText();
							one.setBeforeCredit(Double.parseDouble(beforeCredit));
						}else if (name.equalsIgnoreCase("betTime")){
							String betTime = attr.getText();
							one.setBetTime(DateUtil.parseDateForStandard(betTime));
						}else if (name.equalsIgnoreCase("recalcuTime")){
							String recalcuTime = attr.getText();
							one.setRecalcuTime(DateUtil.parseDateForStandard(recalcuTime));
						}else if (name.equalsIgnoreCase("gameType")){
							String gameType = attr.getText();
							one.setGameType(gameType);
						}else if (name.equalsIgnoreCase("deviceType")){
							String deviceType = attr.getText();
							one.setDeviceType(deviceType);
						}else if (name.equalsIgnoreCase("platformType")){
							String platformType = attr.getText();
							one.setPlatformType(platformType);
						}else if (name.equalsIgnoreCase("flag")){
							String flags = attr.getText();
							if("0".equals(flags)){//未结算的数据不统计
								flag = false;
							}
						}
					}
					if(flag){
						list.add(one);
					}
				}
			}else{
				log.info("document parse failed:" + xml);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	// 查询指定时间段内玩家的老虎机投注额
	public static List<AgData>  getslotorders_ex(String startdate, String enddate,Integer page) {
		List<AgData> listAll = new ArrayList<AgData>();
		String order = "reckontime";
		String by = "DESC";
		String key = EncryptionUtil.encryptPassword(datacagent + startdate+ enddate +order+by+page+perpage+ pidtoken);
		String _startdate = startdate.replace(" ", "%20");
		String _enddate = enddate.replace(" ", "%20");
		String url = agin_dataurl + "/getslotorders_ex.xml?cagent="+datacagent+"&startdate="+_startdate+"&enddate="+_enddate+
				"&order=reckontime&by=DESC&page="+page+"&perpage="+perpage+"&key=" + key;
		String result = doGet(url);
		
		if (StringUtil.isNotBlank(result)) {
			String status = matcher("<info>(.*?)</info>", result);
			//System.out.println(status);
			if(!"0".equals(status)){
				return listAll;
			}
			listAll = parseXmlToAgSlot(result,"SLOT");
		}

		return listAll;
	}
	
	@SuppressWarnings("rawtypes")
	public static List<AgData> parseXmlToAgSlot(String xml,String platformType) {
		List<AgData> list = new ArrayList<AgData>();
		try {
			Document doc = DomOperator.string2Document(xml);
			if (doc != null) {
				Element elementData = (Element) doc.getRootElement().selectSingleNode("//result");
				if(elementData == null){
					return list;
				}
				Iterator itTypes = elementData.elements("row").listIterator();
				while (itTypes.hasNext()) {
					Element elemType = (Element) itTypes.next();
					Iterator it = elemType.attributeIterator();
					AgData one = new AgData();
					one.setPlatformType(platformType);
					boolean flag = true;
					while (it.hasNext()) {
						Attribute attr = (Attribute) it.next();
						String name = attr.getName();
						if (name.equalsIgnoreCase("billno")){
							String billNo = attr.getText();
							one.setBillNo(billNo);
						}else if (name.equalsIgnoreCase("username")){
							String playName = attr.getText();
							one.setPlayName(playName.substring(3));
						}else if (name.equalsIgnoreCase("account")){
							String betAmount = attr.getText();
							one.setBetAmount(Double.parseDouble(betAmount));
						}else if (name.equalsIgnoreCase("valid_account")){
							String validBetAmount = attr.getText();
							one.setValidBetAmount(Double.parseDouble(validBetAmount));
						}else if (name.equalsIgnoreCase("cus_account")){
							String netAmount = attr.getText();
							one.setNetAmount(Double.parseDouble(netAmount));
						}else if (name.equalsIgnoreCase("billtime")){
							String betTime = attr.getText();
							one.setBetTime(DateUtil.parseDateForStandard(betTime));
						}else if (name.equalsIgnoreCase("reckontime")){
							String recalcuTime = attr.getText();
							one.setRecalcuTime(DateUtil.parseDateForStandard(recalcuTime));
						}else if (name.equalsIgnoreCase("gameType")){
							String gameType = attr.getText();
							//桌面游戏、视频扑克数据记录为
							if(gameType.startsWith("TA")||gameType.startsWith("PK")){
								one.setPlatformType("AGSLOTOTHER");
							}
							one.setGameType(gameType);
						}else if (name.equalsIgnoreCase("deviceType")){
							String deviceType = attr.getText();
							one.setDeviceType(deviceType);
						}else if (name.equalsIgnoreCase("flag")){
							String flags = attr.getText();
							if("0".equals(flags)){//未结算的数据不统计
								flag = false;
							}
						}
					}
					if(flag){
						list.add(one);
					}
				}
			}else{
				log.info("document parse failed:" + xml);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	// 查询指定时间段内玩家的yoplay投注额
	public static List<AgData>  getyoplayorders_ex(String startdate, String enddate,Integer page) {
		List<AgData> listAll = new ArrayList<AgData>();
		String order = "reckontime";
		String by = "DESC";
		String key = EncryptionUtil.encryptPassword(datacagent + startdate+ enddate +order+by+page+perpage+ pidtoken);
		String _startdate = startdate.replace(" ", "%20");
		String _enddate = enddate.replace(" ", "%20");
		String url = agin_dataurl + "/getyoplayorders_ex.xml?cagent="+datacagent+"&startdate="+_startdate+"&enddate="+_enddate+
				"&order=reckontime&by=DESC&page="+page+"&perpage="+perpage+"&key=" + key;
		String result = doGet(url);
		
		if (StringUtil.isNotBlank(result)) {
			String status = matcher("<info>(.*?)</info>", result);
			System.out.println(status);
			if(!"0".equals(status)){
				return listAll;
			}
			listAll = parseXmlToAgSlot(result,"YPMONEY");
		}

		return listAll;
	}
	
	// 查询指定时间段内玩家的agfish投注额
	public static List<AgData>  getscenesofuserreportExt(String startdate, String enddate,Integer page) {
		List<AgData> list = new ArrayList<AgData>();
		String numperpage = "50000";
		Long _startdate = DateUtil.parseDateForStandard(startdate).getTime()/1000;
		Long _enddate = DateUtil.parseDateForStandard(enddate).getTime()/1000;
		String key = EncryptionUtil.encryptPassword(pidtoken + _startdate + _enddate + numperpage + page + datacagent+"“superd84b062c76a87a9322Q");
		String url = fish_dataurl + "/api?act=getscenesofuserreportExt&productid="+datacagent+"&begintime="+_startdate+"&endtime="+_enddate+
				"&numperpage="+numperpage+"&page="+page+"&pidtoken=" + pidtoken+"&sessionkey="+key;
		String result = doGet(url);
		
		if (StringUtil.isNotBlank(result)) {
			list = parseXmlToAgFish(result);
		}

		return list;
	}
	
	@SuppressWarnings("rawtypes")
	public static List<AgData> parseXmlToAgFish(String xml) {
		List<AgData> list = new ArrayList<AgData>();
		try {
			Document doc = DomOperator.string2Document(xml);
			if (doc != null) {
				Element elementData = (Element) doc.getRootElement().selectSingleNode("//Result");
				if(elementData == null){
					return list;
				}
				Iterator itTypes = elementData.elements("Items").listIterator();
				while (itTypes.hasNext()) {
					Element elemType = (Element) itTypes.next();
					Iterator it = elemType.content().listIterator();
					AgData one = new AgData();
					one.setPlatformType("AGINFISH");
					while (it.hasNext()) {
						Element elem = (Element) it.next();
						String name = elem.getName();
						if (name.equalsIgnoreCase("BillId")){
							String billNo = elem.getTextTrim();
							one.setBillNo(billNo);
						}else if (name.equalsIgnoreCase("SceneId")){
							String gameCode = elem.getTextTrim();
							one.setGameCode(gameCode);
						}else if (name.equalsIgnoreCase("UserName")){
							String playName = elem.getTextTrim();
							one.setPlayName(playName.substring(3));
						}else if (name.equalsIgnoreCase("Cost")){
							String betAmount = elem.getTextTrim();
							one.setBetAmount(Double.parseDouble(betAmount));
							one.setValidBetAmount(Double.parseDouble(betAmount));
						}else if (name.equalsIgnoreCase("Earn")){
							String netAmount = elem.getTextTrim();
							one.setNetAmount(Double.parseDouble(netAmount));
						}else if (name.equalsIgnoreCase("StartTime")){
							String betTime = elem.getTextTrim();
							one.setBetTime(new Date(Long.parseLong(betTime)*1000-12*60*60*1000));
						}else if (name.equalsIgnoreCase("EndTime")){
							String recalcuTime = elem.getTextTrim();
							one.setRecalcuTime(new Date(Long.parseLong(recalcuTime)*1000-12*60*60*1000));
						}
					}
					list.add(one);
				}
			}else{
				log.info("document parse failed:" + xml);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	
	
	public static String gametypes() {
		String result = "";
		try {
			String key = EncryptionUtil.encryptPassword(datacagent + pidtoken);
			String url = agin_dataurl + "/gametypes.xml?cagent="+datacagent+"&key=" + key;
			result = doGet(url);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}
	

	public static void main(String[] args) throws Exception {
		String startdate = "2018-10-22 12:00:00";
		String enddate = "2018-10-23 11:59:59";
		Integer page = 1;
		System.out.println(getslotorders_ex(startdate, enddate,page));
//		System.out.println(getscenesofuserreportExt(startdate, enddate,page));
//		System.out.println(gametypes());
	}
}