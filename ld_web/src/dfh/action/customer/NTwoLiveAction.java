package dfh.action.customer;

import java.io.PrintWriter; 
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;

import dfh.action.SubActionSupport;
import dfh.model.enums.NTwoXmlEnum;
import dfh.remote.DocumentParser;
import dfh.remote.RemoteConstant;
import dfh.remote.bean.LoginValidationBean;
import dfh.utils.AxisUtil;
import dfh.utils.DomOperator;

public class NTwoLiveAction extends SubActionSupport {
	private static Logger log = Logger.getLogger(NTwoLiveAction.class);

	public String loginGameEAApp() {
		log.info("login...");
		try { 
			String xml = IOUtils.toString(getRequest().getInputStream());
			Document document = this.vaildXml(xml);
			if (NTwoXmlEnum.CLOGIN.getAction().equals(this.validRequestAction(document))) {
				this.validClogin(xml);
			} else if (NTwoXmlEnum.USERVERF.getAction().equals(this.validRequestAction(document))) {
				this.validUserverf(document);
			} else if (NTwoXmlEnum.CGETTICKET.getAction().equals(this.validRequestAction(document))) {
				this.validGetTicket(document);
			}
		} catch (Exception e) {
			e.printStackTrace();
			this.writeResponseText(e.getMessage());
			log.error("Exception :" + e.toString());
		}
		return null;
	}
	
	private void validClogin(String xml) throws Exception{
		log.info("validClogin...");
		LoginValidationBean loginValidationBean = DocumentParser.parseLoginValidationBean(xml);
		if (loginValidationBean != null) {
			String responseXML = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL
					+ "UserWebService", false), AxisUtil.NAMESPACE, "nTwoGameLoginValidation", 
					new Object[] {loginValidationBean.getId(),loginValidationBean.getUserid(),loginValidationBean.getPwd()}, String.class);
			this.writeResponseText(responseXML);
		}
	}
	
	private void validUserverf(Document doc) throws Exception{
		log.info("validUserverf...");
		Map<String, String> map = this.getXmlPropertys(doc);
		String responseXML = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL
				+ "UserWebService", false), AxisUtil.NAMESPACE, "nTwoGameAutoLoginValidation", 
				new Object[] {map.get("elementId"), map.get("userid"), map.get("uuid"), map.get("clientip")}, String.class);
		this.writeResponseText(responseXML);
	}
	
	private void validGetTicket(Document doc) throws Exception{
		log.info("validGetTicket...");
		Map<String, String> map = this.getXmlPropertys(doc);
		String responseXML = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL
				+ "UserWebService", false), AxisUtil.NAMESPACE, "nTwoGetTicketValidation", 
				new Object[] {map.get("elementId"), map.get("username"), map.get("date"), map.get("sign")}, String.class);
		this.writeResponseText(responseXML);
	}
	
	private Map<String, String> getXmlPropertys(Document doc) {
		Map<String, String> resultMap = new HashMap<String, String>();
		Element element = (Element) doc.getRootElement().selectSingleNode("//request/element");
		String id = element.attributeValue(RemoteConstant.A_ID);
		resultMap.put("elementId", id);
		Iterator it = element.elements("properties").listIterator();
		while (it.hasNext()) {
			Element elem = (Element) it.next();
			resultMap.put(elem.attributeValue(RemoteConstant.A_NAME), elem.getTextTrim());
		}
		return resultMap;
	}
	
	private String validRequestAction(Document doc) throws Exception {
		Element element = (Element) doc.getRootElement().selectSingleNode("/request");
		String action = element.attributeValue(NTwoXmlEnum.ACTION.getAction()).trim();
		if (StringUtils.isEmpty(action) && !NTwoXmlEnum.CLOGIN.getAction().equals(action) && !NTwoXmlEnum.USERVERF.getAction().equals(action) && !NTwoXmlEnum.CGETTICKET.getAction().equals(action)) {
			throw new Exception("request action 未定义");
		}
		return action;
	}
	
	private Document vaildXml(String xml) throws Exception {
		if (StringUtils.isEmpty(xml)) {
			throw new Exception("xml为空");
		} else {
			xml = StringUtils.trimToEmpty(xml);
			Document doc = DomOperator.string2Document(xml);
			if (doc == null) {
				throw new Exception("xml为空");
			}
			return doc;
		}
	}
	
	private void writeResponseText(String text) {
		try {
        	getResponse().setContentType("text/xml;charset=UTF-8");
        	getResponse().setHeader("Charset", "UTF-8");
        	getResponse().setHeader("Cache-Control", "no-cache");
			PrintWriter out = getResponse().getWriter();
			out.write(text);
			out.flush();
			out.close();
		} catch (Exception e) {
			log.error("Exception :" + e.toString());
		}
	}
	
	

}
