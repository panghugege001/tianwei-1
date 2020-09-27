package dfh.utils;

public class BankUtil {
	public static String getBankCode(String bankName){
		String code="";
		if(bankName.equals("工商银行")){
			code="icbc";
		}else if (bankName.equals("农业银行")) {
			code="abc";
		}else if (bankName.equals("建设银行")) {
			code="ccb";
		}else if (bankName.equals("兴业银行")) {
			code="cib";
		}else if (bankName.equals("深圳发展银行")) {
			code="sdb";
		}else if (bankName.equals("交通银行")) {
			code="boco";
		}else if (bankName.equals("中信银行")) {
			code="ecitic";
		}else if (bankName.equals("光大银行")) {
			code="ceb";
		}else if (bankName.equals("招商银行")) {
			code="cmb";
		}else if (bankName.equals("广东发展银行")) {
			code="gdb";
		}else if (bankName.equals("中国银行")) {
			code="boc";
		}
		return code;
	}
}
