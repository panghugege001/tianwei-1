package dfh.netpay;

import javax.servlet.http.HttpServletRequest;

import dfh.security.EncryptionUtil;

public class Opay {

	public static boolean callBackValidate(HttpServletRequest request, String code, String key) {
		if ((request == null) || (code == null) || key == null)
			return false;

		String p01_service = request.getParameter("p01_service");
		String p02_out_ordercode = request.getParameter("p02_out_ordercode");
		String p03_payamount = request.getParameter("p03_payamount");
		String p04_sitecode = request.getParameter("p04_sitecode");
		String p05_subject = request.getParameter("p05_subject");
		String p06_body = request.getParameter("p06_body");
		String p07_price = request.getParameter("p07_price");
		String p08_quantity = request.getParameter("p08_quantity");
		String p10_note = request.getParameter("p10_note");
		String p11_status = request.getParameter("p11_status");
		String p12_ordercode = request.getParameter("p12_ordercode");
		String sign = request.getParameter("sign");
		StringBuffer originalBuffer = new StringBuffer();
		originalBuffer.append("p01_service=" + p01_service);
		originalBuffer.append("&p02_out_ordercode=" + p02_out_ordercode);
		originalBuffer.append("&p03_payamount=" + p03_payamount);
		originalBuffer.append("&p04_sitecode=" + p04_sitecode);
		originalBuffer.append("&p05_subject=" + p05_subject);
		originalBuffer.append("&p06_body=" + p06_body);
		originalBuffer.append("&p07_price=" + p07_price);
		originalBuffer.append("&p08_quantity=" + p08_quantity);
		originalBuffer.append("&p10_note=" + p10_note);
		originalBuffer.append("&p11_status=" + p11_status);
		originalBuffer.append("&p12_ordercode=" + p12_ordercode);
		originalBuffer.append("&merchantcode=" + code);
		originalBuffer.append("&merchantkey=" + key);
		return sign.equals(EncryptionUtil.encryptThis(originalBuffer.toString()));
	}

	public static void main(String[] args) throws Exception {
	}

	public static String getSign(String paybankcode, String puturl, String billno, String merno, Double amount, String responseUrl, String returnUrl, String code, String key) {
		String p01_service = "interface_pay";
		String p02_out_ordercode = billno;
		String p03_payamount = amount.intValue() + "";
		String p04_sitecode = merno;
		String p05_subject = "deposit";
		String p06_body = "deposit";
		String p07_price = p03_payamount;
		String p08_quantity = "1";
		String p09_notify_url = responseUrl;
		String p10_note = "deposit";
		String p13_paymode = "0";// 网银支付
		String p14_paybankcode = paybankcode;
		String p15_cardnumber = "";
		String p16_cardpassword = "";
		String p17_return_url = returnUrl;

		StringBuffer originalBuffer = new StringBuffer();
		originalBuffer.append("p01_service=" + p01_service);
		originalBuffer.append("&p02_out_ordercode=" + p02_out_ordercode);
		originalBuffer.append("&p03_payamount=" + p03_payamount);
		originalBuffer.append("&p04_sitecode=" + p04_sitecode);
		originalBuffer.append("&p05_subject=" + p05_subject);
		originalBuffer.append("&p06_body=" + p06_body);
		originalBuffer.append("&p07_price=" + p07_price);
		originalBuffer.append("&p08_quantity=" + p08_quantity);
		originalBuffer.append("&p09_notify_url=" + p09_notify_url);
		originalBuffer.append("&p10_note=" + p10_note);
		originalBuffer.append("&p13_paymode=" + p13_paymode);
		originalBuffer.append("&p14_paybankcode=" + p14_paybankcode);
		originalBuffer.append("&p15_cardnumber=" + p15_cardnumber);
		originalBuffer.append("&p16_cardpassword=" + p16_cardpassword);
		originalBuffer.append("&p17_return_url=" + p17_return_url);
		originalBuffer.append("&merchantcode=" + code);
		originalBuffer.append("&merchantkey=" + key);

		System.out.println("originalString:" + originalBuffer.toString());
		return EncryptionUtil.encryptThis(originalBuffer.toString());
	}

	public static String requestForm(String paybankcode, String puturl, String billno, String merno, Double amount, String responseUrl, String returnUrl, String code, String key) {
		String p01_service = "interface_pay";
		String p02_out_ordercode = billno;
		String p03_payamount = amount.intValue() + "";
		String p04_sitecode = merno;
		String p05_subject = "deposit";
		String p06_body = "deposit";
		String p07_price = p03_payamount;
		String p08_quantity = "1";
		String p09_notify_url = responseUrl;
		String p10_note = "deposit";
		String p13_paymode = "0";// 网银支付
		String p14_paybankcode = paybankcode;
		String p15_cardnumber = "";
		String p16_cardpassword = "";
		String p17_return_url = returnUrl;

		StringBuffer originalBuffer = new StringBuffer();
		originalBuffer.append("p01_service=" + p01_service);
		originalBuffer.append("&p02_out_ordercode=" + p02_out_ordercode);
		originalBuffer.append("&p03_payamount=" + p03_payamount);
		originalBuffer.append("&p04_sitecode=" + p04_sitecode);
		originalBuffer.append("&p05_subject=" + p05_subject);
		originalBuffer.append("&p06_body=" + p06_body);
		originalBuffer.append("&p07_price=" + p07_price);
		originalBuffer.append("&p08_quantity=" + p08_quantity);
		originalBuffer.append("&p09_notify_url=" + p09_notify_url);
		originalBuffer.append("&p10_note=" + p10_note);
		originalBuffer.append("&p13_paymode=" + p13_paymode);
		originalBuffer.append("&p14_paybankcode=" + p14_paybankcode);
		originalBuffer.append("&p15_cardnumber=" + p15_cardnumber);
		originalBuffer.append("&p16_cardpassword=" + p16_cardpassword);
		originalBuffer.append("&p17_return_url=" + p17_return_url);
		originalBuffer.append("&merchantcode=" + code);
		originalBuffer.append("&merchantkey=" + key);

		System.out.println("originalString:" + originalBuffer.toString());
		String sign = EncryptionUtil.encryptThis(originalBuffer.toString());
		String form = "";
		form += "<form name='ETopPaySubmit' method='post' action='" + puturl + "'>";
		form += "<input type='hidden' name='p01_service' value=" + p01_service + ">";
		form += "<input type='hidden' name='p02_out_ordercode' value=" + p02_out_ordercode + ">";
		form += "<input type='hidden' name='p03_payamount' value=" + p03_payamount + ">";
		form += "<input type='hidden' name='p04_sitecode' value=" + p04_sitecode + ">";
		form += "<input type='hidden' name='p05_subject' value=" + p05_subject + ">";
		form += "<input type='hidden' name='p06_body' value=" + p06_body + ">";
		form += "<input type='hidden' name='p07_price' value=" + p07_price + ">";
		form += "<input type='hidden' name='p08_quantity' value=" + p08_quantity + ">";
		form += "<input type='hidden' name='p09_notify_url' value=" + p09_notify_url + ">";
		form += "<input type='hidden' name='p10_note' value=" + p10_note + ">";
		form += "<input type='hidden' name='p13_paymode' value=" + p13_paymode + ">";
		form += "<input type='hidden' name='p14_paybankcode' value=" + p14_paybankcode + ">";
		form += "<input type='hidden' name='p15_cardnumber' value=" + p15_cardnumber + ">";
		form += "<input type='hidden' name='p16_cardpassword' value=" + p16_cardpassword + ">";
		form += "<input type='hidden' name='p17_return_url' value=" + p17_return_url + ">";
		form += "<input type='hidden' name='sign' value=" + sign + ">";
		form += "</form>";
		form += "<script>";
		form += "document.ETopPaySubmit.submit()";
		form += "</script>";
		return form;
	}
}