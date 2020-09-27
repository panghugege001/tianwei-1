package dfh.action.customer;

import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import chinapnr2.SecureLink;
import dfh.action.SubActionSupport;
import dfh.utils.AxisUtil;
import dfh.utils.GsonUtil;

public class HfReturnAction extends SubActionSupport {

	private static final long serialVersionUID = -3168288366868514557L;

	private Logger log = Logger.getLogger(HfReturnAction.class);
	private static HashMap<String, Long> cacheMap = new HashMap<String, Long>();
	
	public String payReturn() {
		PrintWriter out = null;
		try {
			out = this.getResponse().getWriter();
			// 消息类型
			String CmdId = this.getRequest().getParameter("CmdId");
			// 商户号
			String MerId = this.getRequest().getParameter("MerId");
			// 应答返回码
			String RespCode = this.getRequest().getParameter("RespCode");
			// 钱管家交易唯一标识
			String TrxId = this.getRequest().getParameter("TrxId");
			// 订单金额
			String OrdAmt = this.getRequest().getParameter("OrdAmt");
			// 币种
			String CurCode = this.getRequest().getParameter("CurCode");
			// 商品编号
			String Pid = this.getRequest().getParameter("Pid");
			// 订单号
			String OrdId = this.getRequest().getParameter("OrdId");
			// 商户私有域
			String MerPriv = this.getRequest().getParameter("MerPriv");
			// 返回类型
			String RetType = this.getRequest().getParameter("RetType");
			// 分账明细
			String DivDetails = this.getRequest().getParameter("DivDetails");
			// 银行ID
			String GateId = this.getRequest().getParameter("GateId");
			if(StringUtils.isBlank(GateId)){
				GateId = this.getRequest().getParameter("mypost").substring(7);
			}
			// 签名
			String ChkValue = this.getRequest().getParameter("ChkValue");
			//log.info("TRADE 接收订单号为" + OrdId + "的交易数据,交易状态" + RespCode + " 交易流水号：" + TrxId);
			///***************************begin
			/*String str = "payReturn"+OrdId+TrxId ;
			Long nowTiem = System.currentTimeMillis();
	        Iterator<Map.Entry<String, Long>> it = cacheMap.entrySet().iterator();
	        while (it.hasNext()) {
	            Map.Entry<String, Long> entry = it.next();
	            String keyStr = entry.getKey();
	            Long longTime = cacheMap.get(keyStr);
	            if (nowTiem - longTime > 1000 * 100) {
	                it.remove();
	            }
	        }
	        if (cacheMap.get(str) != null) {
//	            GsonUtil.GsonObject("60秒内不能重复提交同一平台。");
	        	log.info("汇付100秒内重复提交");
	            return null;
	        }
	        cacheMap.put(str, nowTiem);*/
			///***************************end
			// 判断响应是否成功 000000 表示成功
			if (RespCode == null || !RespCode.equals("000000")) {
				out.print("ERROR");
				log.info("汇付响应状态错误：" + RespCode);
				return null;
			}
			// 判断商户号是否为是该商户号
			if (!MerId.equals("872720")) {
				out.print("ERROR");
				log.info("汇付商户号不正确:" + MerId);
				return null;
			}
			// 验签
			String merData = CmdId + MerId + RespCode + TrxId + OrdAmt + CurCode + Pid + OrdId + MerPriv + RetType + DivDetails + GateId;
			SecureLink sl = new SecureLink();
			String path = this.getClass().getResource("").toURI().getPath() + "PgPubk.key";
			int ret = sl.VeriSignMsg(path, merData, ChkValue);
			if (ret != 0) {
				out.print("ERROR");
				//LOG.info("TRADE 接收订单号为" + OrdId + "的交易后台处理出错 交易流水号：" + TrxId + "***错误码***" + ret);
				return null;
			}
			// 判断金额
			if (OrdAmt == null) {
				OrdAmt = "0.00";
			}
			// OrdId订单号 MerPriv商户私有域 TrxId钱管家交易唯一标识 MerId商户号
			String returnmsg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "addPayorderHf", new Object[] { OrdId, Double.parseDouble(OrdAmt), MerPriv, TrxId }, String.class);
			if (returnmsg == null) {
				out.print("RECV_ORD_ID_" + OrdId);
				log.info("交易成功,你支付的" + OrdAmt + "元已经到账,此次交易的订单号为" + OrdId + " 交易流水号：" + TrxId);
				return null;
			}
			out.print("RECV_ORD_ID_" + OrdId);
			return null;
		} catch (Exception e) {
			out.print("ERROR");
			//LOG.info("汇付异常：" + e.toString());
			return null;
		} finally {
			if (out != null) {
				out.flush();
				out.close();
			}
		}
	}
	
	
	public String payReturn1() {
		log.info("汇付自动回调action---开始");
		PrintWriter out = null;
		try {
			out = this.getResponse().getWriter();
			// 消息类型
			String CmdId = this.getRequest().getParameter("CmdId");
			// 商户号
			String MerId = this.getRequest().getParameter("MerId");
			// 应答返回码
			String RespCode = this.getRequest().getParameter("RespCode");
			// 钱管家交易唯一标识
			String TrxId = this.getRequest().getParameter("TrxId");
			// 订单金额
			String OrdAmt = this.getRequest().getParameter("OrdAmt");
			// 币种
			String CurCode = this.getRequest().getParameter("CurCode");
			// 商品编号
			String Pid = this.getRequest().getParameter("Pid");
			// 订单号
			String OrdId = this.getRequest().getParameter("OrdId");
			// 商户私有域
			String MerPriv = this.getRequest().getParameter("MerPriv");
			// 返回类型
			String RetType = this.getRequest().getParameter("RetType");
			// 分账明细
			String DivDetails = this.getRequest().getParameter("DivDetails");
			// 银行ID
			String GateId = this.getRequest().getParameter("GateId");
			// 签名
			String ChkValue = this.getRequest().getParameter("ChkValue");
			if(StringUtils.isBlank(GateId)){
				GateId = this.getRequest().getParameter("mypost").substring(7);
			}
			log.info("汇付自动--TRADE 接收订单号为" + OrdId + "的交易数据,交易状态" + RespCode + " 交易流水号：" + TrxId);
			///***************************begin
			/*String str = "payReturn"+OrdId+TrxId ;
			Long nowTiem = System.currentTimeMillis();
	        Iterator<Map.Entry<String, Long>> it = cacheMap.entrySet().iterator();
	        while (it.hasNext()) {
	            Map.Entry<String, Long> entry = it.next();
	            String keyStr = entry.getKey();
	            Long longTime = cacheMap.get(keyStr);
	            if (nowTiem - longTime > 1000 * 30) {
	                it.remove();
	            }
	        }
	        if (cacheMap.get(str) != null) {
//	            GsonUtil.GsonObject("60秒内不能重复提交同一平台。");
	        	log.info("汇付30秒内重复提交");
	            return null;
	        }
	        cacheMap.put(str, nowTiem);*/
			///***************************end
			// 判断响应是否成功 000000 表示成功
			if (RespCode == null || !RespCode.equals("000000")) {
				out.print("ERROR");
				log.info("汇付自动--汇付响应状态错误：" + RespCode);
				return null;
			}
			// 判断商户号是否为是该商户号
		/*	if (!MerId.equals("872720")) {
				out.print("ERROR");
				log.info("汇付商户号不正确:" + MerId);
				return null;
			}*/
			
			
			// 验签
			String merData = CmdId + MerId + RespCode + TrxId + OrdAmt + CurCode + Pid + OrdId + MerPriv + RetType + DivDetails + GateId;
			SecureLink sl = new SecureLink();
			String path = this.getClass().getResource("").toURI().getPath() + "PgPubk.key";
			int ret = sl.VeriSignMsg(path, merData, ChkValue);
			if (ret != 0) {
				out.print("ERROR");
				//LOG.info("汇付自动--TRADE 接收订单号为" + OrdId + "的交易后台处理出错 交易流水号：" + TrxId + "***错误码***" + ret);
				return null;
			}
			// 判断金额
			if (OrdAmt == null) {
				OrdAmt = "0.00";
			}
			// OrdId订单号 MerPriv商户私有域 TrxId钱管家交易唯一标识 MerId商户号
			String returnmsg = AxisUtil.getObjectOne(AxisUtil.getClient(AxisUtil.PUBLICWEBSERVICEURL + "UserWebService", false), AxisUtil.NAMESPACE, "addPayorderHf1", new Object[] { OrdId, Double.parseDouble(OrdAmt), MerPriv, TrxId,MerId }, String.class);
			if (returnmsg == null) { 
				out.print("RECV_ORD_ID_" + OrdId);
				log.info("汇付自动--交易成功,你支付的" + OrdAmt + "元已经到账,此次交易的订单号为" + OrdId + " 交易流水号：" + TrxId);
				return null;  
			}
			out.print("RECV_ORD_ID_" + OrdId);
			log.info("汇付自动回调action---结束");
			return null;
		} catch (Exception e) {
			out.print("ERROR");
			//LOG.info("汇付自动--汇付异常：" + e.toString());
			return null;
		} finally {
			if (out != null) {
				out.flush();
				out.close();
			}
		}
	}
}
