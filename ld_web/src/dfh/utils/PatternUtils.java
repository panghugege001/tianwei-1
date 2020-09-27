package dfh.utils;

import java.util.regex.Pattern;

public class PatternUtils {

	//验证用户密码:正确格式为：以字母开头，长度在6~18之间，只能包含字符、数字和下划线
	private static final Pattern matchRegist = Pattern.compile("^[a-zA-Z]@\\w{5,17}$");
	
	//验证Email地址：zsaa2209@ZF.com
	private static final Pattern matchEmail = Pattern.compile("\\w{3,17}@ZF.com");
	
	//验证代理地址 填写2-6个数字或者是字母,您用来推广的网址
	private static final Pattern matchAgentAddress = Pattern.compile("\\w{2,6}");
	public static boolean matchRegist(String str){
		
		return matchRegist.matcher(str).matches();
		
	}
	

	public static boolean matchEmail(String str){
		
		return matchEmail.matcher(str).matches();
		
	}
	
	public static boolean matchAgentAddress(String str){
		return matchAgentAddress.matcher(str).matches();
	}
	
	public static void main(String[] args) {
		
		System.out.println(matchEmail.matcher("zqy1009@ZF.com").matches());
		System.out.println(matchAgentAddress("21awda"));
		
	}
}
