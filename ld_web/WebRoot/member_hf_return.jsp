<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="dfh.utils.AxisSecurityEncryptUtil"%>
<%@page import="dfh.action.customer.HfReturnAction"%>
<%@page import="chinapnr2.SecureLink"%>
<%
	// 消息类型
	String CmdId = request.getParameter("CmdId");
	// 商户号
	String MerId = request.getParameter("MerId");
	// 应答返回码
	String RespCode = request.getParameter("RespCode");
	// 钱管家交易唯一标识
	String TrxId = request.getParameter("TrxId");
	// 订单金额
	String OrdAmt = request.getParameter("OrdAmt");
	// 币种
	String CurCode = request.getParameter("CurCode");
	// 商品编号
	String Pid = request.getParameter("Pid");
	// 订单号
	String OrdId = request.getParameter("OrdId");
	// 商户私有域
	String MerPriv = request.getParameter("MerPriv");
	// 返回类型
	String RetType = request.getParameter("RetType");
	// 分账明细
	String DivDetails = request.getParameter("DivDetails");
	// 银行ID
	String GateId = request.getParameter("GateId");
	// 签名
	String ChkValue = request.getParameter("ChkValue");
	// 判断响应是否成功 000000 表示成功
	if (RespCode==null || !RespCode.equals("000000")) {
		out.println("汇付响应状态错误：" + RespCode);
		return;
	}
	// 判断商户号是否为是该商户号
	if (!MerId.equals("872720")) {
		out.println("汇付商户号不正确:" + MerId);
		return;
	}
	// 验签
	//String merData = CmdId + MerId + OrdId + OrdAmt + CurCode + Pid + MerPriv + GateId + DivDetails;
	String merData = CmdId + MerId + RespCode + TrxId + OrdAmt + CurCode + Pid + OrdId + MerPriv + RetType + DivDetails + GateId;
	SecureLink sl = new SecureLink();
	String path = HfReturnAction.class.getResource("").toURI().getPath() + "PgPubk.key";
	int ret = sl.VeriSignMsg(path, merData, ChkValue);
	if (ret != 0) {
		out.println("TRADE 接收订单号为" + OrdId + "的交易后台处理出错 交易流水号：" + CmdId);
		return;
	}
	// 判断金额
	if (OrdAmt == null) {
		OrdAmt = "0.00";
	}
	String returnmsg = AxisSecurityEncryptUtil.addPayorderHf(OrdId, OrdAmt, MerPriv, TrxId);
	if (returnmsg == null) {
		out.println("交易成功,你支付的" + OrdAmt + "元已经到账,此次交易的订单号为" + OrdId + " 交易流水号：" + CmdId);
	} else {
		out.println("交易失败:" + returnmsg);
	}
%>