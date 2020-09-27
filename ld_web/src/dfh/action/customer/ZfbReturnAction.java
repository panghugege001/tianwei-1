package dfh.action.customer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import dfh.action.SubActionSupport;
import dfh.utils.AlipayUtil;
import dfh.utils.AxisUtil;
/* *
功能：支付宝服务器异步通知页面
版本：3.3
日期：2012-08-17
说明：
以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
该代码仅供学习和研究支付宝接口使用，只是提供一个参考。

//***********页面功能说明***********
创建该页面文件时，请留心该页面文件中无任何HTML代码及空格。
该页面不能在本机电脑测试，请到服务器上做测试。请确保外部可以访问该页面。
如果没有收到该页面返回的 success 信息，支付宝会在24小时内按一定的时间策略重发通知
//********************************
* */
public class ZfbReturnAction extends SubActionSupport {

	private static final long serialVersionUID = -3168288366868514557L;

	private Logger log = Logger.getLogger(ZfbReturnAction.class);
	
	public String payReturn() {
		try {
			//获取支付宝POST过来反馈信息
			Map<String,String> params = new HashMap<String,String>();
			HttpServletRequest request = getRequest();
			Map requestParams = request.getParameterMap();
			for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
				String name = (String) iter.next();
				String[] values = (String[]) requestParams.get(name);
				String valueStr = "";
				for (int i = 0; i < values.length; i++) {
					valueStr = (i == values.length - 1) ? valueStr + values[i]
							: valueStr + values[i] + ",";
				}
				//乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
				//valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
				params.put(name, valueStr);
			}
			
			//获取支付宝的通知返回参数//
			//商户订单号

			String out_trade_no = request.getParameter("out_trade_no");

			//支付宝交易号

			String trade_no = request.getParameter("trade_no");

			//交易状态
			String trade_status = request.getParameter("trade_status");

			//交易金额
			String total_fee = request.getParameter("total_fee");
			
			//公用回传参数
			String extra_common_param = request.getParameter("extra_common_param");
			
			//卖家账号
			String seller_email = request.getParameter("seller_email");
			
			//通知时间
			String notify_time = request.getParameter("notify_time");
			
			
			//获取支付宝的通知返回参数//
			
			//计算得出通知验证结果
			boolean verify_result = AlipayUtil.verify(params);
			
			if(verify_result){//验证成功
				if(trade_status.equals("TRADE_FINISHED") || trade_status.equals("TRADE_SUCCESS")){
					//判断该笔订单是否在商户网站中已经做过处理
						//如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
						//如果有做过处理，不执行商户的业务程序
					if (total_fee == null) {
						total_fee = "0.00";
					}
					String returnmsg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "addPayorderZfb", new Object[] { out_trade_no, Double.parseDouble(total_fee), extra_common_param, trade_no, notify_time }, String.class);
					if (returnmsg == null) {
						log.info("交易成功,你支付的" + Double.parseDouble(total_fee) + "元已经到账,此次交易的订单号为" + trade_no);
					} else {
						log.info("交易失败:" + returnmsg);
					}
					writeText("success");
				}else{
					writeText("fail");
					log.info("交易失败" + trade_status);
				}
			}else{
				writeText("fail");
				log.info("验证失败,请仔细核对交易信息");
			}
		} catch (Exception e) {
			log.info(e.toString());
		}
		return null;
	}
	
}
