package dfh.netpay;

import javax.servlet.http.HttpServletRequest;

public interface NetpayInterfaces {
	/**
	 * 回调验证
	 * 
	 * @param request
	 * @param merkey
	 *            key值
	 * @return
	 */
	public  boolean callBackValidate(HttpServletRequest request, String merkey);
	/**
	 * 得到md5值
	 * @param puturl 支付网关
	 * @param billno 本地单号
	 * @param merno 商户号
	 * @param amount 金额
	 * @param responseUrl  回调url
	 * @param merkey  商户key
	 * @return
	 */
	public String getHamc(String puturl, String billno, String merno, Double amount, String responseUrl, String merkey);

	/**
	 * 
	 * @param puturl
	 *            支付网关
	 * @param billno
	 *            本地单号
	 * @param merno
	 *            商户号
	 * @param amount
	 *            金额
	 * @param responseUrl
	 *            回调url
	 * @param merkey
	 *            商户key
	 * @return
	 */
	public String requestURL(String puturl, String billno, String merno, Double amount, String responseUrl, String merkey);
	
	/**
	 * 
	 * @param puturl
	 *            支付网关
	 * @param billno
	 *            本地单号
	 * @param merno
	 *            商户号
	 * @param amount
	 *            金额
	 * @param responseUrl
	 *            回调url
	 * @param merkey
	 *            商户key
     * @param ip
	 *            客户端ip地址
	 * @return
	 */
	public String requestURL(String puturl, String billno, String merno, Double amount, String responseUrl, String merkey,String ip);

}
