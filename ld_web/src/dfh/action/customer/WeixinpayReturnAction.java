package dfh.action.customer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import chinapnr2.SecureLink;
import dfh.action.SubActionSupport;
import dfh.utils.AxisUtil;
import dfh.utils.GsonUtil;

public class WeixinpayReturnAction extends SubActionSupport {

	private static final long serialVersionUID = -3168288366868514557L;

	private Logger log = Logger.getLogger(WeixinpayReturnAction.class);
	private static HashMap<String, Long> cacheMap = new HashMap<String, Long>();
	
	public String weixinpayReturn() {
		log.info("微信支付直连 回调开始===========================================================================");
		ServletInputStream stream=null;
		try {
			// 消息类型
			String return_code = this.getRequest().getParameter("return_code");
			String return_code1 = (String) this.getRequest().getAttribute("return_code");
			// 商户号
			String return_msg = "";
			stream=this.getRequest().getInputStream();
			if(("SUCCESS").equals(return_code)){
				 return_msg = this.getRequest().getParameter("return_msg");
			}else{
				log.info("微信支付回调参数错误return_code===="+return_code);
				//return null;
			}
			//BufferedInputStream in = new BufferedInputStream(stream);  
			BufferedReader br = new BufferedReader(new InputStreamReader(stream));
			String line = "";
			StringBuffer sbf = new StringBuffer("");
			for (line = br.readLine(); line != null; line = br.readLine()) {
				System.out.println(line);
				sbf.append(line);
			}
			Iterator Elements = parse(sbf.toString());
			return_code = "";
			String appid="";
			String mch_id="";
			String sign="";
			String result_code="";
			String err_code="";
			String err_code_des="";
			Double total_fee=0.0;
			String out_trade_no="";//订单号
			String transaction_id="";//微信支付订单号
			String attach="";
			String time_end="";
			while (Elements.hasNext()) {
				Element elem = (Element) Elements.next();
				System.out.println("节点" + elem.getName() + "\ttext="
						+ elem.getText());
				if (elem.getName().equals("return_code")) {
					return_code = elem.getText();
					log.info("获取return_code的值2===="+return_code);
				} else if (elem.getName().equals("appid")) {
					appid = elem.getText();
				}else if (elem.getName().equals("mch_id")) {
					mch_id = elem.getText();
				}else if (elem.getName().equals("sign")) {
					sign = elem.getText();
				}else if (elem.getName().equals("result_code")) {
					result_code = elem.getText();
				}else if (elem.getName().equals("err_code")) {
					err_code = elem.getText();
				}else if (elem.getName().equals("err_code_des")) {
					err_code_des = elem.getText();
				}else if (elem.getName().equals("total_fee")) {
					total_fee = Double.parseDouble(elem.getText().toString())/100;
				}else if (elem.getName().equals("out_trade_no")) {
					out_trade_no = elem.getText();
				}else if (elem.getName().equals("attach")) {
					attach = elem.getText();
				}else if (elem.getName().equals("time_end")) {
					time_end = elem.getText();
				}else if (elem.getName().equals("transaction_id")) {
					transaction_id = elem.getText();
				}
			}
			if (!"SUCCESS".equalsIgnoreCase(return_code+"")) {
				log.info("微信支付回调失败："+return_msg+";err_code"+err_code+";err_code_des"+err_code_des);
			}
			if (!"SUCCESS".equalsIgnoreCase(result_code+"")) {
				log.info("微信支付失败,玩家支付失败："+return_msg+";err_code"+err_code+";err_code_des"+err_code_des);
			}else{
				String result = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "WeixinpayReturn", new Object[] { out_trade_no,total_fee,"0"}, String.class);	
				result="success";
			if("success".equals(result)){
				 HttpServletResponse response = ServletActionContext.getResponse();
				 response.setContentType("text/html; charset=utf-8");
				 response.getWriter().write("<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>");
			}
			}
		} catch (Exception e) {
			//LOG.info("微信支付回调异常：" + e.toString());
			return null;
		} finally{
			try {
				stream.close();
				stream=null;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		log.info("微信支付直连 回调结束===========================================================================");
		return null;
	}
	

	public static Iterator parse(String protocolXML) {
		try {
			Document doc = (Document) DocumentHelper.parseText(protocolXML);
			Element books = doc.getRootElement();
			System.out.println("根节点" + books.getName());
			// Iterator users_subElements =
			// books.elementIterator("UID");//指定获取那个元素
			Iterator Elements = books.elementIterator();
			return Elements;
			/*
			 * while(Elements.hasNext()){ Element user =
			 * (Element)Elements.next();
			 * System.out.println("节点"+user.getName()+"\ttext="+user.getText());
			 * List subElements = user.elements(); // List user_subElements =
			 * user.elements("username");指定获取那个元素 //
			 * System.out.println("size=="+subElements.size()); // for( int
			 * i=0;i<subElements.size();i++){ // Element ele =
			 * (Element)subElements.get(i); //
			 * System.out.print(ele.getName()+" : "+ele.getText()+" "); // }
			 * System.out.println(); }
			 */
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
