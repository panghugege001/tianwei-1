package dfh.netpay;

import dfh.model.enums.NetpayTypeEnum;

public class NetpayProvider {

	NetpayInterfaces yeepay;
	NetpayInterfaces cncard;
	NetpayInterfaces nps;
	NetpayInterfaces ips;
	NetpayInterfaces tenpay;
	NetpayInterfaces onetopay;


	public NetpayInterfaces getNetpay(String netPayName) {
		if (NetpayTypeEnum.YEEPAY.getCode().equals(netPayName))
			return yeepay;
		else if (NetpayTypeEnum.CNCARD.getCode().equals(netPayName))
			return cncard;
		else if (NetpayTypeEnum.NPS.getCode().equals(netPayName))
			return nps;
		else if (NetpayTypeEnum.IPS.getCode().equals(netPayName))
			return ips;
		else if (NetpayTypeEnum.TENPAY.getCode().equals(netPayName))
			return tenpay;
		else if (NetpayTypeEnum.OPAY.getCode().equals(netPayName))
			return onetopay;
		return null;
	}

	/**
	 * 配制回调的actionname名称 如果没有中间站时使用
	 */
	public String getCallBackActionName(String netPayName) {
		String defaultCallbackName = "callBack";
		if (NetpayTypeEnum.YEEPAY.getCode().equals(netPayName))
			return defaultCallbackName;
		else if (NetpayTypeEnum.CNCARD.getCode().equals(netPayName))
			return defaultCallbackName + netPayName;
		else if (NetpayTypeEnum.NPS.getCode().equals(netPayName))
			return defaultCallbackName + netPayName;
		else if (NetpayTypeEnum.IPS.getCode().equals(netPayName))
			return defaultCallbackName + netPayName;
		else if (NetpayTypeEnum.TENPAY.getCode().equals(netPayName))
			return defaultCallbackName + netPayName;
		else if (NetpayTypeEnum.OPAY.getCode().equals(netPayName))
			return defaultCallbackName + netPayName;
		return null;
	}

	/**
	 * 配制回调地址
	 */
	public String getCallBackAddress(String netPayName) {
		if (NetpayTypeEnum.YEEPAY.getCode().equals(netPayName))
			return "http://www.lb888.cc/pay/rYeepay.jsp";
		else if (NetpayTypeEnum.CNCARD.getCode().equals(netPayName))
			return "http://www.lb888.cc/pay/rCncard.jsp";
		else if (NetpayTypeEnum.NPS.getCode().equals(netPayName))
			return "http://www.lb888.cc/pay/rNps.jsp";
		else if (NetpayTypeEnum.IPS.getCode().equals(netPayName))
			return "http://www.lb888.cc/pay/rIps.jsp";
		else if (NetpayTypeEnum.TENPAY.getCode().equals(netPayName))
			return "http://www.lb888.cc/pay/rTenpay.jsp";
		else if (NetpayTypeEnum.OPAY.getCode().equals(netPayName))
			return "http://www.lb888.cc/pay/rOpay.jsp";
		return null;
	}

	/**
	 * 配制请求地址
	 */
	public String getSubmitAddress(String netPayName) {
		if (NetpayTypeEnum.YEEPAY.getCode().equals(netPayName))
			return "http://www.lb888.cc/pay/netpay.jsp";
		else if (NetpayTypeEnum.CNCARD.getCode().equals(netPayName))
			return "http://www.lb888.cc/pay/netpay.jsp";
		else if (NetpayTypeEnum.NPS.getCode().equals(netPayName))
			return "http://www.lb888.cc/pay/netpay.jsp";
		else if (NetpayTypeEnum.IPS.getCode().equals(netPayName))
			return "http://www.lb888.cc/pay/netpay.jsp";
		else if (NetpayTypeEnum.TENPAY.getCode().equals(netPayName))
			return "http://www.lb888.cc/pay/netpay.jsp";
		else if (NetpayTypeEnum.OPAY.getCode().equals(netPayName))
			return "http://www.lb888.cc/pay/netpay.jsp";
		return null;
	}

	public NetpayInterfaces getYeepay() {
		return yeepay;
	}

	public void setYeepay(NetpayInterfaces yeepay) {
		this.yeepay = yeepay;
	}

	public NetpayInterfaces getCncard() {
		return cncard;
	}

	public void setCncard(NetpayInterfaces cncard) {
		this.cncard = cncard;
	}

	public NetpayInterfaces getNps() {
		return nps;
	}

	public void setNps(NetpayInterfaces nps) {
		this.nps = nps;
	}

	public NetpayInterfaces getIps() {
		return ips;
	}

	public void setIps(NetpayInterfaces ips) {
		this.ips = ips;
	}

	public NetpayInterfaces getTenpay() {
		return tenpay;
	}

	public void setTenpay(NetpayInterfaces tenpay) {
		this.tenpay = tenpay;
	}
	
	public NetpayInterfaces getOnetopay() {
		return onetopay;
	}

	public void setOnetopay(NetpayInterfaces onetopay) {
		this.onetopay = onetopay;
	}

}
