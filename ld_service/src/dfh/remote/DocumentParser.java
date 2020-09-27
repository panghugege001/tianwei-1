package dfh.remote;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList; 
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;

import dfh.model.bean.XimaRequestBean;
import dfh.remote.bean.CheckClientResponseBean;
import dfh.remote.bean.DepositPendingResponseBean;
import dfh.remote.bean.DspResponseBean;
import dfh.remote.bean.GetTurnOverResponseBean;
import dfh.remote.bean.KenoResponseBean;
import dfh.remote.bean.LoginValidationBean;
import dfh.remote.bean.NTwoCheckClientResponseBean;
import dfh.remote.bean.NTwoClientBetBean;
import dfh.remote.bean.NTwoDepositPendingResponseBean;
import dfh.remote.bean.NTwoWithdrawalConfirmationResponseBean;
import dfh.remote.bean.Property;
import dfh.remote.bean.WithdrawalConfirmationResponseBean;
import dfh.utils.Constants;
import dfh.utils.DateUtil;
import dfh.utils.DomOperator;
import dfh.utils.HttpUtils;

public class DocumentParser {
	private static Log log = LogFactory.getLog(DocumentParser.class);

	final static String XML_DECLARATION = "<?xml version=\"1.0\" encoding=\"" + Constants.ENCODING + "\"?>";
	final static String XML_DECLARATION_UTF16 = "<?xml version=\"1.0\" encoding=\"" + Constants.ENCODING_UTF16 + "\"?>";
	final static String NEWLINE = "";

	public static DepositPendingResponseBean parseDepositPendingResponseBean(String xml) {
		xml = StringUtils.trimToEmpty(xml);
		DepositPendingResponseBean bean = null;
		Document doc = DomOperator.string2Document(xml);
		if (doc != null) {
			Element element = (Element) doc.getRootElement().selectSingleNode("//request/element");
			String id = element.attributeValue(RemoteConstant.A_ID);
			String agcode = null;
			String status = null;
			String refno = null;
			String paymentid = null;
			String errdesc = null;
			Iterator it = element.elements("properties").listIterator();
			while (it.hasNext()) {
				Element elem = (Element) it.next();
				if (elem.attributeValue(RemoteConstant.A_NAME).equals(RemoteConstant.P_ACODE))
					agcode = elem.getTextTrim();
				else if (elem.attributeValue(RemoteConstant.A_NAME).equals(RemoteConstant.P_STATUS))
					status = elem.getTextTrim();
				else if (elem.attributeValue(RemoteConstant.A_NAME).equals(RemoteConstant.P_REFNO))
					refno = elem.getTextTrim();
				else if (elem.attributeValue(RemoteConstant.A_NAME).equals(RemoteConstant.P_PAYMENTID))
					paymentid = elem.getTextTrim();
				else if (elem.attributeValue(RemoteConstant.A_NAME).equals(RemoteConstant.P_ERRDESC))
					errdesc = elem.getTextTrim();

				bean = new DepositPendingResponseBean(id, agcode, status, refno, paymentid, errdesc);
			}
		} else {
			log.info("document parse failed:" + xml);
		}
		return bean;
	}

	public static WithdrawalConfirmationResponseBean parseWithdrawalConfirmationResponseBean(String xml) {
		xml = StringUtils.trimToEmpty(xml);
		WithdrawalConfirmationResponseBean bean = null;
		Document doc = DomOperator.string2Document(xml);
		if (doc != null) {
			Element element = (Element) doc.getRootElement().selectSingleNode("//request/element");
			String id = element.attributeValue(RemoteConstant.A_ID);
			String agcode = null;
			String status = null;
			String refno = null;
			String paymentid = null;
			String errdesc = null;
			Iterator it = element.elements("properties").listIterator();
			while (it.hasNext()) {
				Element elem = (Element) it.next();
				if (elem.attributeValue(RemoteConstant.A_NAME).equals(RemoteConstant.P_ACODE))
					agcode = elem.getTextTrim();
				else if (elem.attributeValue(RemoteConstant.A_NAME).equals(RemoteConstant.P_STATUS))
					status = elem.getTextTrim();
				else if (elem.attributeValue(RemoteConstant.A_NAME).equals(RemoteConstant.P_REFNO))
					refno = elem.getTextTrim();
				else if (elem.attributeValue(RemoteConstant.A_NAME).equals(RemoteConstant.P_PAYMENTID))
					paymentid = elem.getTextTrim();
				else if (elem.attributeValue(RemoteConstant.A_NAME).equals(RemoteConstant.P_ERRDESC))
					errdesc = elem.getTextTrim();

				bean = new WithdrawalConfirmationResponseBean(id, status, refno, paymentid, errdesc);
			}
		} else {
			log.info("document parse failed:" + xml);
		}
		return bean;
	}

	public static CheckClientResponseBean parseCheckClientResponseBean(String xml) {
		xml = StringUtils.trimToEmpty(xml);
		CheckClientResponseBean bean = null;
		Document doc = DomOperator.string2Document(xml);
		if (doc != null) {
			Element element = (Element) doc.getRootElement().selectSingleNode("//request/element");
			String id = element.attributeValue(RemoteConstant.A_ID);
			String userid = null;
			Double balance = null;
			String currencyid = null;
			String location = null;
			Iterator it = element.elements("properties").listIterator();
			while (it.hasNext()) {
				Element elem = (Element) it.next();
				if (elem.attributeValue(RemoteConstant.A_NAME).equals(RemoteConstant.P_USERID))
					userid = elem.getTextTrim();
				else if (elem.attributeValue(RemoteConstant.A_NAME).equals(RemoteConstant.P_BALANCE))
					balance = Double.parseDouble(elem.getTextTrim());
				else if (elem.attributeValue(RemoteConstant.A_NAME).equals(RemoteConstant.P_CURRENCYID))
					currencyid = elem.getTextTrim().equals("")?"-1":elem.getTextTrim();
				else if (elem.attributeValue(RemoteConstant.A_NAME).equals(RemoteConstant.P_LOCATION))
					location = elem.getTextTrim();

				bean = new CheckClientResponseBean(id, userid, balance, currencyid, location);
			}
		} else {
			log.info("document parse failed:" + xml);
		}
		return bean;
	}

	public static LoginValidationBean parseLoginValidationBean(String xml) {
		xml = StringUtils.trimToEmpty(xml);
		LoginValidationBean bean = null;
		Document doc = DomOperator.string2Document(xml);
		if (doc != null) {
			Element element = (Element) doc.getRootElement().selectSingleNode("//request/element");
			String id = element.attributeValue(RemoteConstant.A_ID);
			String userid = null;
			String password = null;
			Iterator it = element.elements("properties").listIterator();
			while (it.hasNext()) {
				Element elem = (Element) it.next();
				if (elem.attributeValue(RemoteConstant.A_NAME).equals(RemoteConstant.P_USERID))
					userid = elem.getTextTrim();
				else if (elem.attributeValue(RemoteConstant.A_NAME).equals(RemoteConstant.P_PASSWORD))
					password = elem.getTextTrim();

				bean = new LoginValidationBean(id, userid, password);
			}
		} else {
			log.info("document parse failed:" + xml);
		}
		return bean;
	}

	public static String createRequestXML(String action, String id, List<Property> propList) {
		String xml = XML_DECLARATION + NEWLINE;
		xml += "<request action=\"" + action + "\">" + NEWLINE;
		xml += "<element id=\"" + id + "\">" + NEWLINE;

		Iterator<Property> it = propList.listIterator();
		while (it.hasNext()) {
			Property property = it.next();
			if (StringUtils.isNotEmpty(property.getName()))
				xml += "<properties name=\"" + property.getName() + "\">" + StringUtils.trimToEmpty(property.getValue()) + "</properties>" + NEWLINE;
		}

		xml += "</element>" + NEWLINE;
		xml += "</request>" + NEWLINE;

		return xml;
	}
	
	public static String createSportBookResponseXML(String action,List<Property> propList) {
		String xml = XML_DECLARATION + NEWLINE;
		xml += "<Response Method=\"" + action + "\">" + NEWLINE;

		Iterator<Property> it = propList.listIterator();
		while (it.hasNext()) {
			Property property = it.next();
			if (StringUtils.isNotEmpty(property.getName()))
				xml += "<" + property.getName() + ">" + StringUtils.trimToEmpty(property.getValue()) + "</" + property.getName() + ">" + NEWLINE;
		}
		xml += "</Response>" + NEWLINE;

		return xml;
	}
	
	public static String createSportBookRequestXML(String action,List<Property> propList) {
		String xml = XML_DECLARATION + NEWLINE;
		xml += "<Request Method=\"" + action + "\">" + NEWLINE;

		Iterator<Property> it = propList.listIterator();
		while (it.hasNext()) {
			Property property = it.next();
			if (StringUtils.isNotEmpty(property.getName()))
				xml += "<" + property.getName() + ">" + StringUtils.trimToEmpty(property.getValue()) + "</" + property.getName() + ">" + NEWLINE;
		}
		xml += "</Request>" + NEWLINE;

		return xml;
	}
	
	public static String createGPIAccessXML(List<Property> propList) {
		String xml = XML_DECLARATION + NEWLINE;
		xml += "<resp>" + NEWLINE;

		Iterator<Property> it = propList.listIterator();
		while (it.hasNext()) {
			Property property = it.next();
			if (StringUtils.isNotEmpty(property.getName()))
				xml += "<" + property.getName() + ">" + StringUtils.trimToEmpty(property.getValue()) + "</" + property.getName() + ">" + NEWLINE;
		}
		xml += "</resp>" + NEWLINE;
		log.info("GPI登入" + xml);
		return xml;
	}

	public static XimaRequestBean parseXimaRequest(String xml) {
		xml = StringUtils.trimToEmpty(xml);
		XimaRequestBean bean = null;
		Document doc = DomOperator.string2Document(xml);
		if (doc != null) {
			// request
			Element element = (Element) doc.getRootElement();
			String startDate = element.attributeValue("startDate");
			String endDate = element.attributeValue("endDate");
			Iterator it = element.elements("xima").listIterator();
			Map<String, Double> map = new HashMap<String, Double>();
			while (it.hasNext()) {
				Element elem = (Element) it.next();
				String loginname = elem.attributeValue("loginname");
				String amount = elem.attributeValue("amount");
				map.put(loginname, Double.parseDouble(amount));
			}
			bean = new XimaRequestBean(DateUtil.parseDateForStandard(startDate), DateUtil.parseDateForStandard(endDate), map);

		} else {
			log.info("document parse failed:" + xml);
		}
		return bean;
	}
	
	public static GetTurnOverResponseBean parseGetTurnOverResponseBean(String xml) {
		xml = StringUtils.trimToEmpty(xml);
		GetTurnOverResponseBean bean = null;
		Document doc = DomOperator.string2Document(xml);
		if (doc != null) {
			Element element = (Element) doc.getRootElement().selectSingleNode("//request/element");
			String id = element.attributeValue(RemoteConstant.A_ID);
			String status = null;
			String errdesc = null;
			String currencyid = null;
			Double turnover = null;
			Iterator it = element.elements("properties").listIterator();
			while (it.hasNext()) {
				Element elem = (Element) it.next();
				if (elem.attributeValue(RemoteConstant.A_NAME).equals(RemoteConstant.P_STATUS))
					status = elem.getTextTrim();
				else if (elem.attributeValue(RemoteConstant.A_NAME).equals(RemoteConstant.P_ERRDESC))
					errdesc = elem.getTextTrim();
				else if (elem.attributeValue(RemoteConstant.A_NAME).equals(RemoteConstant.P_CURRENCYID))
					currencyid = elem.getTextTrim();
				else if (elem.attributeValue(RemoteConstant.A_NAME).equals(RemoteConstant.P_TURNOVER)){
					if(elem!=null && null!=elem.getTextTrim() && !"".equals(elem.getTextTrim())){
						turnover = Double.parseDouble(elem.getTextTrim());
					}
				}
				bean = new GetTurnOverResponseBean(id,status, errdesc, currencyid, turnover);
			}
		} else {
			log.info("document parse failed:" + xml);
		}
		return bean;
	}

	public static DspResponseBean parseDspResponseRequest(String xml){
		xml = StringUtils.trimToEmpty(xml);
		DspResponseBean bean=null;
		Document doc = DomOperator.string2Document(xml);
		if (doc != null){
			Element element = (Element) doc.getRootElement();
			String info = element.attributeValue("info");
			if(info.equals("account_not_exit")){
				info="帐号不存在";
			}else if (info.equals("error")||info.equals("network_error")) {
				info="系统繁忙";
			}else if(info.equals("not_enough_credit")){
				info="转账失败,额度不足";
			}else if (info.equals("duplicate_transfer")) {
				info="重复转账";
			}
			bean= new DspResponseBean(info);
		}else {
			log.info("document parse failed:" + xml);
		}
		return bean;
	}
	
	public static KenoResponseBean parseKenologinResponseRequest(String xml){
		xml = StringUtils.trimToEmpty(xml);
		KenoResponseBean resbean=new KenoResponseBean();
		Document doc = DomOperator.string2Document(xml);
		if (doc != null){
			Element methodResponse = (Element) doc.getRootElement();
			for(Iterator a = methodResponse.elementIterator(); a.hasNext();){
				Element params=(Element) a.next();
				//System.out.println(params.getName());
				for(Iterator c = params.elementIterator(); c.hasNext();){
					Element param =(Element) c.next();
					for(Iterator d = param.elementIterator(); d.hasNext();){
						Element value =(Element) d.next();
						for(Iterator e = value.elementIterator(); e.hasNext();){
							Element struct =(Element) e.next();
							for(Iterator f = struct.elementIterator(); f.hasNext();){
								Element member =(Element) f.next();
								for(Iterator g = member.elementIterator(); g.hasNext();){
								//	System.out.println("看循环几次");
									Element submember =(Element) g.next();
									if(submember.getName().equals("name")){
										//System.out.println(submember.getText());
										resbean.setName(submember.getText());
										if(submember.getText().equals("Link")){
											//Element stringvalue =(Element) submember.element("string");
											//System.out.println(stringvalue.getName());
										}else if(submember.getText().equals("Error")){
											
										};
									}else{
										
										for(Iterator h = submember.elementIterator(); h.hasNext();){
											Element stringvalue =(Element) h.next();
											//System.out.println(stringvalue.getName());
											if(stringvalue.getName().equals("string")){
												resbean.setValue(stringvalue.getText());
											}else if(stringvalue.getName().equals("double")){
												resbean.setAmount(Double.parseDouble(stringvalue.getText()));
											}else if(stringvalue.getName().equals("int")){
												resbean.setFundIntegrationId(Integer.parseInt(stringvalue.getText()));
											}else if(submember.getName().equals("Credit")){
												System.out.println(stringvalue.getText());
											}
										}
										
										//resbean.setValue(stringvalue.getText());
										//System.out.println(stringvalue.getText());
									}
									//System.out.println(submember.getName());
									
								}
							}
						}
					}
				}
			}
//			String info = element.attributeValue("methodResponse");
//			System.out.println(element.elementText("member"));
//			if(info.equals("account_not_exit")){
//				info="帐号不存在";
//			}else if (info.equals("error")||info.equals("network_error")) {
//				info="系统繁忙";
//			}else if(info.equals("not_enough_credit")){
//				info="转账失败,额度不足";
//			}else if (info.equals("duplicate_transfer")) {
//				info="重复转账";
//			}
		}else {
			log.info("document parse failed:" + xml);
		}
		return resbean;
	}
	
	
	public static KenoResponseBean parseKenocheckcreditResponseRequest(String xml){
		xml = StringUtils.trimToEmpty(xml);
		KenoResponseBean resbean=new KenoResponseBean();
		Document doc = DomOperator.string2Document(xml);
		if (doc != null){
			Element methodResponse = (Element) doc.getRootElement();
			for(Iterator a = methodResponse.elementIterator(); a.hasNext();){
				Element params=(Element) a.next();
				//System.out.println(params.getName());
				for(Iterator c = params.elementIterator(); c.hasNext();){
					Element param =(Element) c.next();
					for(Iterator d = param.elementIterator(); d.hasNext();){
						Element value =(Element) d.next();
						for(Iterator e = value.elementIterator(); e.hasNext();){
							Element struct =(Element) e.next();
							for(Iterator f = struct.elementIterator(); f.hasNext();){
								Element member =(Element) f.next();
								int flag=-1;
								int i=0;
								for(Iterator g = member.elementIterator(); g.hasNext();){
								//	System.out.println("看循环几次");
									i++;
									Element submember =(Element) g.next();
									if(submember.getName().equals("name")){
									//	System.out.println(submember.getText());
										
										if(submember.getText().equals("Credit")){
											resbean.setName(submember.getText());
											flag=i;
											//System.out.println(submember.elementText("value"));
										}
										if(submember.getText().equals("Error")){
											resbean.setName(submember.getText());
										}
										
										
									}else{
										int j=0;
										for(Iterator h = submember.elementIterator(); h.hasNext();){
											j++;
											Element stringvalue =(Element) h.next();
										//	System.out.println(submember.getName()+"  "+stringvalue.getName()+"  "+stringvalue.getText());
											if(flag==j){
												resbean.setAmount(Double.parseDouble(stringvalue.getText()));
											}else {
												resbean.setValue(stringvalue.getText());
											}
											
										}
										
										//resbean.setValue(stringvalue.getText());
										//System.out.println(stringvalue.getText());
									}
									//System.out.println(submember.getName());
									
								}
							}
						}
					}
				}
			}
//			String info = element.attributeValue("methodResponse");
//			System.out.println(element.elementText("member"));
//			if(info.equals("account_not_exit")){
//				info="帐号不存在";
//			}else if (info.equals("error")||info.equals("network_error")) {
//				info="系统繁忙";
//			}else if(info.equals("not_enough_credit")){
//				info="转账失败,额度不足";
//			}else if (info.equals("duplicate_transfer")) {
//				info="重复转账";
//			}
		}else {
			log.info("document parse failed:" + xml);
		}
		return resbean;
	}
	//#################波音平台#################
	//CheckOrCreateGameAccount
	public static DspResponseBean parseBBinDspResponseRequest(String xml){
		xml = StringUtils.trimToEmpty(xml);
		DspResponseBean bean=null;
		if(xml ==null || xml.equals("")){
			bean = new DspResponseBean();
			bean.setInfo("系统繁忙,BBin无响应,请稍后再试");
			return bean;
		}
		Document doc = DomOperator.string2Document(xml);
		if (doc != null){
			Element element = (Element) doc.getRootElement().element("Record").element("Code");
			String info = element.getText();
			if(info.equals("22002")){//User not exist
				info="会员不存在";
			}else if(info.equals("10002")){
				info="额度不足";
			}else if(info.equals("11000")) {
				info="重复转账";
			}else if(info.equals("10003")){
				info="转账失败";
			}else if(info.equals("44444")){
				info="系统维护中....";
			}else if(info.equals("45000") || info.equals("45006")){
				info="游戏不存在";
			}else if(info.equals("45003")){
				info="视频游戏不存在";
			}else if(info.equals("40006")){
				info="帐号长度最多10位";
			}
			bean= new DspResponseBean(info);
		}else {
			log.info("document parse failed:" + xml);
		}
		return bean;
	}
	//GetBalance
	public static DspResponseBean parseBBinRemitDspResponseRequest(String xml){
		xml = StringUtils.trimToEmpty(xml);
		DspResponseBean bean=null;
		if(xml ==null || xml.equals("")){
			bean = new DspResponseBean();
			bean.setInfo("系统繁忙,BBin无响应,请稍后再试");
			return bean;
		}
		Document doc = DomOperator.string2Document(xml);
		if (doc != null){
			Element codeelement = (Element) doc.getRootElement().element("Record").element("Code");
			String info = null;
			if(codeelement!=null && codeelement.getText()!=null && !"".equals(codeelement.getText())){
				info = codeelement.getText();
				if(info.equals("22002")){//User not exist
					info="会员不存在";
				}else if(info.equals("10002")){
					info="额度不足";
				}else if(info.equals("11000")) {
					info="重复转账";
				}else if(info.equals("10003")){
					info="转账失败";
				}else if(info.equals("44444")){
					info="系统维护中....";
				}else if(info.equals("45000") || info.equals("45006")){
					info="游戏不存在";
				}else if(info.equals("45003")){
					info="视频游戏不存在";
				}else if(info.equals("40006")){
					info="帐号长度最多10位";
				}
			}else{
				Element element = (Element) doc.getRootElement().element("Record").element("Balance");
				info = element.getText();
			}
			bean= new DspResponseBean(info);
		}else {
			log.info("document parse failed:" + xml);
		}
		return bean;
	}
	//SportBook
	public static DspResponseBean parseSBRemitDspResponseRequest(String xml){
		xml = StringUtils.trimToEmpty(xml);
		DspResponseBean bean=null;
		if(xml ==null || xml.equals("")){
			bean = new DspResponseBean();
			bean.setInfo("系统繁忙,SB体育平台无响应,请稍后再试");
			return bean;
		}
		Document doc = DomOperator.string2Document(xml);
		if (doc != null){
			Element codeelement = (Element) doc.getRootElement().element("ReturnCode");
			String info = null;
			if(codeelement!=null && codeelement.getText()!=null && !"".equals(codeelement.getText())){
				info = codeelement.getText();
				if(info.equals("000")){//SUCCESS
					Element element = (Element) doc.getRootElement().element("Balance");
					info = element.getText();
				}else if(info.equals("001")){
					info="系统错误码[001],请求失败!";
				}else if(info.equals("002")) {
					info="系统错误码[002],无法注册!";
				}else if(info.equals("003")){
					info="系统错误码[003],无该用户数据!";
				}else if(info.equals("004")){
					info="系统错误码[004],转入失败!";
				}else if(info.equals("005")){
					info="系统错误码[005],转出失败!";
				}else if(info.equals("006")){
					info="系统错误码[006],状态失败!";
				}else if(info.equals("007")){
					info="系统错误码[007],验证失败!";
				}else if(info.equals("008")){
					info="系统错误码[008],订单号重复!";
				}else{
					info = "系统繁忙,SB体育平台无响应,请稍后再试";
				}
			}else{
				info = "系统繁忙,SB体育平台无响应,请稍后再试";
			}
			bean= new DspResponseBean(info);
		}else {
			log.info("document parse failed:" + xml);
		}
		return bean;
	}
	public static DspResponseBean parseSBDspResponseRequest(String xml){
		xml = StringUtils.trimToEmpty(xml);
		DspResponseBean bean=null;
		if(xml ==null || xml.equals("")){
			bean = new DspResponseBean();
			bean.setInfo("系统繁忙,SB体育平台无响应,请稍后再试");
			return bean;
		}
		Document doc = DomOperator.string2Document(xml);
		if (doc != null){
			Element codeelement = (Element) doc.getRootElement().element("ReturnCode");
			String info = null;
			if(codeelement!=null && codeelement.getText()!=null && !"".equals(codeelement.getText())){
				info = codeelement.getText();
				if(info.equals("000")){//SUCCESS
					bean= new DspResponseBean(info);
					return bean;
				}else if(info.equals("001")){
					info="系统错误码[001],请求失败!";
				}else if(info.equals("002")) {
					info="系统错误码[002],无法注册!";
				}else if(info.equals("003")){
					info="系统错误码[003],无该用户数据!";
				}else if(info.equals("004")){
					info="系统错误码[004],转入失败!";
				}else if(info.equals("005")){
					info="系统错误码[005],转出失败!";
				}else if(info.equals("006")){
					info="系统错误码[006],状态失败!";
				}else if(info.equals("007")){
					info="系统错误码[007],验证失败!";
				}else if(info.equals("008")){
					info="系统错误码[008],订单号重复!";
				}else{
					info = "系统繁忙,SB体育平台无响应,请稍后再试";
				}
			}else{
				info = "系统繁忙,SB体育平台无响应,请稍后再试";
			}
			bean= new DspResponseBean(info);
		}else {
			log.info("document parse failed:" + xml);
		}
		return bean;
	}
	
	public static String createNTwoResponseXML(String action, String id, Map<String,String> propMap, String status) {
		StringBuffer xml = new StringBuffer();
		xml.append(XML_DECLARATION_UTF16);
		xml.append("<message>");
		xml.append("<status>").append(status).append("</status>");
		xml.append("<result action=\"").append(action).append("\">");
		xml.append("<element id=\"").append(id).append("\">");
		for (Entry<String, String> entry: propMap.entrySet()) {
			xml.append("<properties name=\"").append(entry.getKey()).append("\">").append(StringUtils.trimToEmpty(entry.getValue())).append("</properties>");
		}
		xml.append("</element></result></message>");
		return xml.toString();
	}
	
	public static NTwoDepositPendingResponseBean parseNTwoDepositPendingResponseBean(String xml) {
		xml = StringUtils.trimToEmpty(xml);
		NTwoDepositPendingResponseBean bean = null;
		Document doc = DomOperator.string2Document(xml);
		if (doc != null) {
			Element element = (Element) doc.getRootElement().selectSingleNode("//result/element");
			String id = element.attributeValue(RemoteConstant.A_ID);
			String agcode = null;
			String status = null;
			String refno = null;
			String paymentid = null;
			String errdesc = null;
			Iterator it = element.elements("properties").listIterator();
			while (it.hasNext()) {
				Element elem = (Element) it.next();
				if (elem.attributeValue(RemoteConstant.A_NAME).equals(RemoteConstant.P_ACODE)){
					agcode = elem.getTextTrim();
				}
				else if (elem.attributeValue(RemoteConstant.A_NAME).equals(RemoteConstant.P_STATUS)){
					status = elem.getTextTrim();
				}
				else if (elem.attributeValue(RemoteConstant.A_NAME).equals(RemoteConstant.P_REFNO)){
					refno = elem.getTextTrim();
				}
				else if (elem.attributeValue(RemoteConstant.A_NAME).equals(RemoteConstant.P_PAYMENTID)){
					paymentid = elem.getTextTrim();
				}
				else if (elem.attributeValue(RemoteConstant.A_NAME).equals(RemoteConstant.P_ERRDESC)){
					errdesc = elem.getTextTrim();
				}
			}
			bean = new NTwoDepositPendingResponseBean(id, agcode, status, refno, paymentid, errdesc);
		} else {
			log.info("document parse failed:" + xml);
		}
		return bean;
	}
	
	public static NTwoWithdrawalConfirmationResponseBean parseNTwoWithdrawalConfirmationResponseBean(String xml) {
		xml = StringUtils.trimToEmpty(xml);
		NTwoWithdrawalConfirmationResponseBean bean = null;
		Document doc = DomOperator.string2Document(xml);
		if (doc != null) {
			Element element = (Element) doc.getRootElement().selectSingleNode("//result/element");
			String id = element.attributeValue(RemoteConstant.A_ID);
			String agcode = null;
			String status = null;
			String refno = null;
			String paymentid = null;
			String errdesc = null;
			Iterator it = element.elements("properties").listIterator();
			while (it.hasNext()) {
				Element elem = (Element) it.next();
				if (elem.attributeValue(RemoteConstant.A_NAME).equals(RemoteConstant.P_ACODE)){
					agcode = elem.getTextTrim();
				}
				else if (elem.attributeValue(RemoteConstant.A_NAME).equals(RemoteConstant.P_STATUS)){
					status = elem.getTextTrim();
				}
				else if (elem.attributeValue(RemoteConstant.A_NAME).equals(RemoteConstant.P_REFNO)){
					refno = elem.getTextTrim();
				}
				else if (elem.attributeValue(RemoteConstant.A_NAME).equals(RemoteConstant.P_PAYMENTID)){
					paymentid = elem.getTextTrim();
				}
				else if (elem.attributeValue(RemoteConstant.A_NAME).equals(RemoteConstant.P_ERRDESC)){
					errdesc = elem.getTextTrim();
				}
			}
			bean = new NTwoWithdrawalConfirmationResponseBean(id, status, refno, paymentid, errdesc);
		} else {
			log.info("document parse failed:" + xml);
		}
		return bean;
	}
	
	public static NTwoCheckClientResponseBean parseNTwoCheckClientResponseBean(String xml) {
		xml = StringUtils.trimToEmpty(xml);
		NTwoCheckClientResponseBean bean = new NTwoCheckClientResponseBean();
		Document doc = DomOperator.string2Document(xml);
		if (doc != null) {
			Element statusElement = (Element) doc.getRootElement().selectSingleNode("//status");
			String status = statusElement.getTextTrim();
			Element element = (Element) doc.getRootElement().selectSingleNode("//result/element");
			String id = element.attributeValue(RemoteConstant.A_ID);
			String userid = "";
			BigDecimal balance = BigDecimal.ZERO;
			String currencyid = "";
			String statusCode = "";
			String errdesc = "";
			Iterator it = element.elements("properties").listIterator();
			while (it.hasNext()) {
				Element elem = (Element) it.next();
				if (elem.attributeValue(RemoteConstant.A_NAME).equals(RemoteConstant.P_USERID)) {
					userid = elem.getTextTrim();
				}
				else if (elem.attributeValue(RemoteConstant.A_NAME).equals(RemoteConstant.P_STATUS)){
					statusCode = elem.getTextTrim();
				}
				else if (elem.attributeValue(RemoteConstant.A_NAME).equals(RemoteConstant.P_BALANCE)){
					balance = new BigDecimal(elem.getTextTrim());
				}
				else if (elem.attributeValue(RemoteConstant.A_NAME).equals(RemoteConstant.P_CURRENCYID)){
					currencyid = elem.getTextTrim().equals("")?"-1":elem.getTextTrim();
				}
				else if (elem.attributeValue(RemoteConstant.A_NAME).equals(RemoteConstant.P_ERRDESC)) {
					errdesc = elem.getTextTrim();
				}
			}
			bean.setStatus(ErrorCode.SUCCESS.getText().equalsIgnoreCase(status) ? ErrorCode.SUCCESS.getCode() : statusCode);
			bean.setId(id);
			bean.setUserid(userid);
			bean.setBalance(balance);
			bean.setCurrencyid(currencyid);
			bean.setErrdesc(errdesc);
		} else {
			log.info("document parse failed:" + xml);
		}
		return bean;
	}
	
	/**
	 * key:login name, value:NTwoClientBetBean
	 * @param xml
	 * @return
	 */
	public static Map<String, NTwoClientBetBean> parseNTwoClientBetBeanResponse(String xml) {
		xml = StringUtils.trimToEmpty(xml);
		Document doc = DomOperator.string2Document(xml);
		Map<String, NTwoClientBetBean> beanMap = new HashMap<String, NTwoClientBetBean>();
		if (doc != null) {
			Element statusElement = (Element) doc.getRootElement().selectSingleNode("//status");
			String status = statusElement.getTextTrim();
			Element gameInfoElement = (Element) doc.getRootElement().selectSingleNode("//result/gameinfo");
			Iterator gemeIterator = gameInfoElement.elements("game").listIterator();
			while (gemeIterator.hasNext()) {
				Element gameElement = (Element) gemeIterator.next();
				Iterator dealIterator = gameElement.elements("deal").listIterator();
				while (dealIterator.hasNext()) {
					Element dealElement = (Element) dealIterator.next();
					Iterator clientbetIterator = dealElement.element("betinfo").elements("clientbet").listIterator();
					while(clientbetIterator.hasNext()) {
						Element clientbetElement = (Element) clientbetIterator.next();
						transferToMap(beanMap, clientbetElement);
					}
				}
			}
		} else {
			log.info("document parse failed:" + xml);
		}
		return beanMap;
	}
	
	/**
	 * key:login name, value:NTwoClientBetBean
	 * @param xml
	 * @return
	 */
	private static void transferToMap(Map<String, NTwoClientBetBean> beanMap, Element element) {
		String key = element.attributeValue("login");
		NTwoClientBetBean bean = beanMap.get(key);
		if (bean == null) {
			beanMap.put(key, new NTwoClientBetBean(element));
		} else {
			bean.calculateBeanValue(element);
		}
	}
	
	public static String createCheckAffiliateRequestXML(String action, String id, List<Property> propList) {
		String xml = XML_DECLARATION + NEWLINE;
		xml += "<request action=\"" + action + "\">" + NEWLINE;
		xml += "<element id=\"" + id + "\">" + NEWLINE;

		Iterator<Property> it = propList.listIterator();
		while (it.hasNext()) {
			Property property = it.next();
			if (StringUtils.isNotEmpty(property.getName()))
				if("acode".equals(property.getName())){
					xml += "<properties name=\"" + property.getName() + "\"><acode>" + StringUtils.trimToEmpty(property.getValue()) + "</acode></properties>" + NEWLINE;
				}else{
					xml += "<properties name=\"" + property.getName() + "\">" + StringUtils.trimToEmpty(property.getValue()) + "</properties>" + NEWLINE;	
				}
		}
		xml += "</element>" + NEWLINE;
		xml += "</request>" + NEWLINE;
		return xml;
	}
	
	public static String createNTwoRequestXML(String action, String id, List<Property> propList) {
		StringBuffer sb = new StringBuffer();
		sb.append(XML_DECLARATION_UTF16);
		sb.append("<request action=\"").append(action).append("\">");
		sb.append("<element id=\"").append(id).append("\">");

		Iterator<Property> it = propList.listIterator();
		while (it.hasNext()) {
			Property property = it.next();
			if (StringUtils.isNotEmpty(property.getName())){
				sb.append("<properties name=\"").append(property.getName()).append("\">").append(StringUtils.trimToEmpty(property.getValue())).append("</properties>");
			}
		}
		
		sb.append("</element>");
		sb.append("</request>");

		return sb.toString();
	}
	
	public static String createNTwoGameInfoRequestXML(String action, List<Property> propList) {
		StringBuffer sb = new StringBuffer();
		sb.append("<request xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" action=\"").append(action).append("\">");
		sb.append("<element").append(">");

		Iterator<Property> it = propList.listIterator();
		while (it.hasNext()) {
			Property property = it.next();
			if (StringUtils.isNotEmpty(property.getName())){
				sb.append("<properties name=\"").append(property.getName()).append("\">").append(StringUtils.trimToEmpty(property.getValue())).append("</properties>");
			}
		}
		
		sb.append("</element>");
		sb.append("</request>");

		return sb.toString();
	}
	
	public static void main(String[] args) {
		// List<Property> list = new ArrayList<Property>();
		// list.add(new Property(RemoteConstant.P_USERID, "test"));
		// list.add(new Property(RemoteConstant.P_ACODE, "test"));
		// list.add(new Property(RemoteConstant.P_VENDORID, RemoteConstant.VENDORID));
		// list.add(new Property(RemoteConstant.P_CURRENCYID, RemoteConstant.CURRENCYID));
		// list.add(new Property(RemoteConstant.P_Amount, NumericUtil.formatDouble(100.0)));
		// list.add(new Property(RemoteConstant.P_REFNO, "09102391023912"));
		//
		// String xml = DocumentParser.createRequestXML("cwithdrawal", "L200509210001", list);
		// System.out.println(parseDepositPendingResponseBean(xml));
	}
}