package com.gsmc.png.utils;
 
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.joda.time.LocalDateTime;

import com.gsmc.png.model.PlatformData;
import com.gsmc.png.model.enums.GamePlatform;
import com.gsmc.png.remote.DocumentParser;
import com.gsmc.png.remote.TTGNetWinBean;
import com.gsmc.png.remote.TTGSearchNetWinBean;

/**
 * 新的PT工具类
 * 
 * @author jad
 * 
 */
public class TTGUtil {

	private static Logger log = Logger.getLogger(TTGUtil.class);
	
	public static final String PALY_START = "DY8_";
	private static final String affiliate = "DY8";
	private static final String TTG_API_LOGIN_PASSWORD = "QAZqazQAZ!@#";
	private static final String TTG_API_HOST = "ams5-df.ttms.co";//"ams-df.stg.ttms.co";
	private static final Integer TTG_API_PORT = Integer.parseInt("7443");
	private static final String TTG_API_PLAYERS_NETWIN_URL = "/dataservice/datafeed/playernetwin/";
	
	public static List<TTGNetWinBean> searchTTGNetWin(Calendar saerchDate) throws Exception {
		List<TTGNetWinBean> result = new ArrayList<TTGNetWinBean>();
		try {
			String xml = createNetWinXML(null, saerchDate);
			String response = doPostNetWinXML(xml);
			TTGSearchNetWinBean bean = DocumentParser.parseTTGSearchNetWinBean(response,PALY_START);
			result.addAll(bean.getTtgNetWinBeanList());
			if (bean.isMore()) {//超过10000笔数据
				searchMoreTTGNetWin(result, bean, saerchDate);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			throw new Exception("TTG 获取玩家NetWin数据错误");
		}
		return result;
	}
	
	public static List<PlatformData> convertToPlatformData(List<TTGNetWinBean> list,  Calendar searchDate) {
		List<PlatformData> result = new ArrayList<PlatformData>();
		Date sDate = getStartDate(searchDate).getTime();
		Date eDate = getEndDate(searchDate).getTime();
        Date updateTime = LocalDateTime.now().toDate();
        for (TTGNetWinBean bean : list) {
			PlatformData data = new PlatformData();
			data.setUuid(UUID.randomUUID().toString());
			data.setPlatform(GamePlatform.TTG.name());
			data.setLoginname(bean.getPlayerId());
			data.setBet(bean.getTotalWager().doubleValue());
			data.setProfit(bean.getTotalWin().negate().doubleValue());
			data.setStarttime(sDate);
			data.setEndtime(eDate);
			data.setUpdatetime(updateTime);
			result.add(data);
		}
		return result;
	}
	
	private static void searchMoreTTGNetWin(List<TTGNetWinBean> result, TTGSearchNetWinBean bean, Calendar saerchDate) throws Exception {
		log.warn("TTG玩家数据超过10000笔...即将执行多次查询");
		int count = 0;
		
		Map<String, TTGNetWinBean> map = new LinkedHashMap<String, TTGNetWinBean>();
		do {
			count = count+1; 
			log.warn("TTG玩家数据超过10000笔...多次查询第"+count+"次");
			String startIndexKey = bean.getStartIndexKey();
			String searchIndexXml = createNetWinXML(startIndexKey, saerchDate);
			String searchIndexResponse = doPostNetWinXML(searchIndexXml);
			bean = DocumentParser.parseTTGSearchNetWinBean(searchIndexResponse,PALY_START);
			for (TTGNetWinBean ttgNetWingBean : bean.getTtgNetWinBeanList()) {//由于数据太多,使用map避免出现重复数据
				map.put(ttgNetWingBean.getPlayerId(), ttgNetWingBean);
			}
			if (count >10) {//数据超过十万笔,请确认资料及代码,避免无穷回圈,防止错误发生
				log.error("TTG玩家数据超过100000笔...多次查询第"+count+"次");
				throw new Exception("TTG玩家数据超过100000笔,请确认资料及代码...多次查询第"+count+"次");
			}
		}while(bean.isMore());
		
		for (Entry<String, TTGNetWinBean> entry : map.entrySet()) {//map过滤掉重复玩家资料后,放回result
			result.add(entry.getValue());
		}
	}
	
	/**
	 * 产生NetWin xml
	 * @param startIndexKey (第一次搜寻为""空白)
	 * @param saerchDate
	 * @return
	 */
	private static String createNetWinXML(String startIndexKey, Calendar saerchDate) {
		if (startIndexKey == null) {
			startIndexKey = "";
		}
		if (saerchDate == null) {
			saerchDate = Calendar.getInstance();
		}
		StringBuffer sb = new StringBuffer();
		sb.append("<searchdetail startInexKey=\"").append(startIndexKey).append("\" requestId=\"").append(DateUtil.getDateFmtID()).append("\">")
		  .append("<daterange startDate=\"").append(DateUtil.fmtyyyyMMdd(saerchDate.getTime()))
		  .append("\" endDate=\"").append(DateUtil.fmtyyyyMMdd(saerchDate.getTime())).append("\" />")
		  .append("<account currency=\"CNY\" />")
		  .append("<partner partnerId=\"").append(affiliate).append("\" includeSubPartner=\"Y\" />")
		  .append("</searchdetail>");
		return sb.toString();
	}
	
	private static String doPostNetWinXML(String xml) throws Exception {
		return HttpUtils.postTTGXMLBySSL(TTG_API_HOST, TTG_API_PORT, TTG_API_PLAYERS_NETWIN_URL, TTG_API_LOGIN_PASSWORD, affiliate, xml);
	}
	
	
	private static Calendar getStartDate(Calendar searchDate) {
		Calendar calendar = (Calendar)searchDate.clone();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return calendar;
	}
	
	private static Calendar getEndDate(Calendar searchDate) {
		Calendar calendar = (Calendar)searchDate.clone();
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		return calendar;
	}
	
	public static void main(String args[]) throws Exception {
        searchTTGNetWin(DateUtil.getDateBeforeAfter(-3));
	}
	
	
}
