package dfh.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 智付自动补单
 * @date 2015-02-09
 */
public class AutoSupplementPayOrderZF extends BaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		super.doPost(req, resp);

		String tradeNo = req.getParameter("trade_no"); //交易订单号
		String amount = req.getParameter("order_amount"); //金额
		String userName = req.getParameter("extra_return_param"); //第三方支付回传参数,存储用户名
		String seqNo = req.getParameter("bank_seq_no"); //银行交易流水号
		String trade_time = req.getParameter("bank_seq_no"); //交易时间
		String merchant_code = req.getParameter("merchant_code"); //商户号

		/*String ipStr = Configuration.getInstance().getValue("SUPPLEMENT_ORDER_IP");
		Map<String, Object> ipMap = new HashMap<String, Object>();
		if (ipStr != null && !ipStr.trim().equals("")) {
			String[] ipArr = ipStr.split("#");
			for (String ip : ipArr) {
				ipMap.put(ip, null);
			}
		}*/
		String requestIP = getIpAddr(req);
		log.info("智付自动补单IP:" + requestIP + " 单号：" + tradeNo);
		try {
			String result = service.addPayorderZfBD(tradeNo, Double.valueOf(amount), userName, seqNo, trade_time, merchant_code);
			if (result == null || result.equals("此笔交易已经支付成功")) {
				log.info("智付自动补单success:" + tradeNo);
				resp.getWriter().print("success");
			} else {
				log.info("智付自动补单failed:" + tradeNo);
				resp.getWriter().print("failed");
			}
			return;
		} catch (Exception e) {
			log.error(e.getMessage());
			resp.getWriter().print("failed");
		}
	}
}
