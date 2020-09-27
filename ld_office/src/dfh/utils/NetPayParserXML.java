package dfh.utils;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import dfh.action.vo.NetPayVO;

/**
 * <pre>
 * netpay xml parser
 * 响应码请参考文档
 * </pre>
 * @author Administrator
 *
 */
public class NetPayParserXML {
	

	public String String2XmlOfNetPay(String responseStatus){
		Document doc=DocumentHelper.createDocument();
		doc.setXMLEncoding("gbk");
		
		Element root = doc.addElement("netpay");
		root.addElement("responseCode").addText(responseStatus); 
		//root.addElement("message").addText("success");// 这里可以省略，备后续扩展。
		
		return doc.asXML();
	}
	
	
	public NetPayVO xml2BeanOfNetPay(String xml) throws DocumentException, IllegalAccessException, InvocationTargetException{
		Document doc=DocumentHelper.parseText(xml);
		NetPayVO netpay=new NetPayVO();
		
		netpay.setResponseCode(doc.selectSingleNode("/netpay/responseCode").getText());
		netpay.setPayResult(netpay.getResponseCode());
		List rows = doc.selectNodes("/netpay/rows/row");
		int length=rows.size();
		for (int i = 0; i < length; i++) {
			Element row=(Element) rows.get(i);
			BeanUtils.setProperty(netpay, row.attributeValue("rowname"), row.getTextTrim());
		}
		
		return netpay;
		
	}
	
	
	public String String2XmlOfLogin(String responseStatus,String message,String nickname){
		Document doc=DocumentHelper.createDocument();
		doc.setXMLEncoding("gbk");
		Element root = doc.addElement("netpay");
		root.addElement("responseCode").addText(responseStatus);
		if (responseStatus=="1") {
			Element rows = root.addElement("rows");
			Element row = rows.addElement("row");
				row.addAttribute("rowno", "101");
				row.addAttribute("rowname", "nickname");
				row.addText(nickname);
		}
		
		return doc.asXML();
	}
	
	
	public NetPayVO xml2BeanOfLogin(String xml) throws DocumentException, IllegalAccessException, InvocationTargetException{
		Document doc=DocumentHelper.parseText(xml);
		List rows = doc.selectNodes("/netpay/rows/row");
		int length=rows.size();
		NetPayVO netpay=new NetPayVO();
		for (int i = 0; i < length; i++) {
			Element row=(Element) rows.get(i);
			BeanUtils.setProperty(netpay, row.attributeValue("rowname"), row.getTextTrim());
		}
		return netpay;
	}

}
