package dfh.remote;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
import dfh.remote.bean.Property;
import dfh.remote.bean.SportBookLoginValidationBean;
import dfh.remote.bean.WithdrawalConfirmationResponseBean;
import dfh.utils.Constants;
import dfh.utils.DateUtil;
import dfh.utils.DomOperator;

public class DocumentParser {
	private static Log log = LogFactory.getLog(DocumentParser.class);

	final static String XML_DECLARATION = "<?xml version=\"1.0\" encoding=\"" + Constants.ENCODING + "\"?>";
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
	
	public static SportBookLoginValidationBean parseSportBookLoginValidationBean(String xml) {
		xml = StringUtils.trimToEmpty(xml);
		SportBookLoginValidationBean bean = null;
		Document doc = DomOperator.string2Document(xml);
		if (doc != null) {
			Element elementToken = (Element) doc.getRootElement().selectSingleNode("//Request/Token");
			Element elementLoginName = (Element) doc.getRootElement().selectSingleNode("//Request/LoginName");
			String token = elementToken.getText();
			String loginName = elementLoginName.getText();
			bean = new SportBookLoginValidationBean(token,loginName);
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
			}else if(info.equals("40008")){
				info="正在维护！";
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
				if(info.equals("10003")){//User not exist
					info="转账失败";
				}else if(info.equals("11000")){
					info="重复转账";
				}else if(info.equals("10002")) {
					info="余额不足";
				}else if(info.equals("10004")){
					info="key不得为空";
				}else if(info.equals("10005")){
					info="额度检查错误";
				}else if(info.equals("10006")){
					info="remit需为正整数或浮点数";
				}else if(info.equals("10007")){
					info="交易序号不得为空或0";
				}else if(info.equals("10008")){
					info="remit不得小于等于0";
				}else if(info.equals("10009")){
					info="newcredit不能小于0";
				}else if(info.equals("10010")){
					info="credit不能小于0";
				}else if(info.equals("10011")){
					info="转账格式错误";
				}else if(info.equals("10012")){
					info="action必须是IN";
				}else if(info.equals("10013")){
					info="action格式错误";
				}else if(info.equals("10014")){
					info="交易序号长度过长";
				}else if(info.equals("10015")){
					info="账号长度过长";
				}else if(info.equals("21001")){
					info="账号重复";
				}else if(info.equals("21000") || info.equals("21002")){
					info="账号新增失败";
				}else if(info.equals("23000")){
					info="体系错误";
				}else if(info.equals("10009")){
					info="newcredit不能小于0";
				}else if(info.equals("21100")){
					info="账号新增成功";
				}else if(info.equals("23101")){
					info="启用账号成功";
				}else if(info.equals("23104")){
					info="启用账号失败";
				}else if(info.equals("23100")){
					info="停用账号成功";
				}else if(info.equals("23103")){
					info="停用账号失败";
				}else if(info.equals("23105")){
					info="变更密码成功";
				}else if(info.equals("23106")){
					info="变更密码失败";
				}else if(info.equals("22000")){
					info="使用者未登录";
				}else if(info.equals("22001")){
					info="使用者未登出";
				}else if(info.equals("22002")){
					info="用户不存在";
				}else if(info.equals("22003")){
					info="没有用户在此代理下";
				}else if(info.equals("22004")){
					info="uppername非會員階層";
				}else if(info.equals("22010")){
					info="代理不存在";
				}else if(info.equals("22011")){
					info="会员不存在";
				}else if(info.equals("22012")){
					info="使用者不存在";
				}else if(info.equals("40001")){
					info="Hall not exist";
				}else if(info.equals("40002")){
					info="会员已经停用";
				}else if(info.equals("40003")){
					info="代理已经停用";
				}else if(info.equals("40004")){
					info="會員階層錯誤";
				}else if(info.equals("40005")){
					info="代理階層錯誤";
				}else if(info.equals("40006")){
					info="帐号长度最多10位";
				}else if(info.equals("40007")){
					info="密碼長度錯誤";
				}else if(info.equals("40008")){
					info="正在维护";
				}else if(info.equals("40009")){
					info="超過限制筆數";
				}else if(info.equals("40010")){
					info="開始日期驗證錯誤";
				}else if(info.equals("40011")){
					info="結束日期驗證錯誤";
				}else if(info.equals("40012")){
					info="開始時間驗證錯誤";
				}else if(info.equals("40013")){
					info="結束時間驗證錯誤";
				}else if(info.equals("40014")){
					info="日期驗證錯誤";
				}else if(info.equals("44000")){
					info="key驗證錯誤";
				}else if(info.equals("44001")){
					info="參數未帶齊";
				}else if(info.equals("44002")){
					info="無權限";
				}else if(info.equals("44003")){
					info="API忙碌中";
				}else if(info.equals("44004")){
					info="API忙碌中";
				}else if(info.equals("45000")){
					info="遊戲不存在";
				}else if(info.equals("45003")){
					info="視訊遊戲不存在";
				}else if(info.equals("45005")){
					info="機率遊戲不存在";
				}else if(info.equals("45006")){
					info="遊戲不存在";
				}else if(info.equals("44444")){
					info="系統維護中";
				}else if(info.equals("44900")){
					info="IP不被允許";
				}else if(info.equals("47000")){
					info="登入keyB錯誤";
				}else if(info.equals("47001")){
					info="登入keyB不存在";
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