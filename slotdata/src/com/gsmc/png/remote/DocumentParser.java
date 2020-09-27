package com.gsmc.png.remote;

import java.math.BigDecimal;
import java.util.Iterator;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;

import com.gsmc.png.utils.DomOperator;


public class DocumentParser {
	private static Log log = LogFactory.getLog(DocumentParser.class);

	final static String XML_DECLARATION = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
	final static String XML_DECLARATION_UTF16 = "<?xml version=\"1.0\" encoding=\"UTF-16\"?>";
	final static String NEWLINE = "";

	
	@SuppressWarnings("rawtypes")
	public static TTGSearchNetWinBean parseTTGSearchNetWinBean(String xml, String prefix) {
		xml = StringUtils.trimToEmpty(xml);
		TTGSearchNetWinBean bean = null;
		Document doc = DomOperator.string2Document(xml);
		if (doc != null) {
			
			Element element = (Element) doc.getRootElement();
			
			String totalRecords = element.attributeValue("totalRecords");
			String pageSize = element.attributeValue(TTGRemoteConstant.PAGESIZE);
			String isMore = element.attributeValue(TTGRemoteConstant.ISMORE);
			String startIndexKey = element.attributeValue(TTGRemoteConstant.START_INDEX_KEY);
			String requestId = element.attributeValue(TTGRemoteConstant.REQUEST_ID);
			bean = new TTGSearchNetWinBean(Integer.parseInt(totalRecords), Integer.parseInt(pageSize), isMore, startIndexKey, requestId);
			
			Element detailsElement = (Element) doc.getRootElement().selectSingleNode("//searchdetail/details");
			Iterator netWinIterator = detailsElement.elements("netwin").listIterator();
			while (netWinIterator.hasNext()) {
				Element newWinElement = (Element) netWinIterator.next();
				Element playerElement = newWinElement.element("player");
				Element summaryElement = newWinElement.element("summary");
				
				String playerId = playerElement.attributeValue(TTGRemoteConstant.PLAYER_ID);
				int index = prefix.length();
				playerId = playerId.substring(index);
				
				BigDecimal totalWin = new BigDecimal(summaryElement.attributeValue(TTGRemoteConstant.TOTAL_WIN));
				BigDecimal totalWager = new BigDecimal(summaryElement.attributeValue(TTGRemoteConstant.TOTAL_WAGER));
				TTGNetWinBean netWinBean = new TTGNetWinBean(playerId, totalWin, totalWager);
				bean.getTtgNetWinBeanList().add(netWinBean);
			}
		} else {
			log.info("document parse failed:" + xml);
		}
		return bean;
	}
	
	public static void main(String[] args) {
		
		
	}

}