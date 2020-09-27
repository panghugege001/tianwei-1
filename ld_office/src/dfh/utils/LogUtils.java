package dfh.utils;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import edu.emory.mathcs.backport.java.util.Arrays;

/**
 * 
 * @Description: 
 * 	1. 定位到调用方的类放过及行号；
 * 	2. 统一log调用类，便于log分析（程序结构化处理）；
 * 	3. 查看（满足开发、运维、客服...，都能方便很好理解）；
 * 
 * @author kerol
 * @date Jan 19, 2018 2:13:39 PM
 */
public class LogUtils {
	
	private static Logger getLogger() {
		StackTraceElement[] eles = Thread.currentThread().getStackTrace();
		if(eles.length < 4) {
			return Logger.getLogger(Arrays.asList(eles).toString());
		}
		String logClass = eles[3].getClassName() + "@" + eles[3].getMethodName() + ":" + eles[3].getLineNumber();
		return Logger.getLogger(logClass);
	}
	/**
	 * 
	 * @Description: 供外部调用
	 */
	public static Logger getLog() {
		return getLogger();
	}
	
	/**
	 * 
	 * @Title: 统一log调用类，便于log分析（程序结构化处理）、查看（满足开发、运维、客服...都能方便查看）
	 * @Description: 
	 * 	示例传参格式： "name:张三", "age:20" ——> [name:张三, age:20]
	 * 
	 * @author kerol
	 * @date Jan 19, 2018 3:16:41 PM
	 */
	public static void info(Object... args) {
		getLogger().info(Arrays.asList(args).toString());
	}
	/**
	 * 
	 * @Title: 统一log调用类，便于log分析（程序结构化处理）、查看（满足开发、运维、客服...都能方便查看）
	 * @Description: 
	 * 	示例传参格式： "name:张三", "age:格式错误", "age:20" ——> [name:张三, age:格式错误]
	 * 
	 * @author kerol
	 * @date Jan 19, 2018 3:16:41 PM
	 */
	public static void error(Object... args) {
		getLogger().error(Arrays.asList(args).toString());
	}
	/**
	 * 
	 * @Title: 统一log调用类，便于log分析（程序结构化处理）、查看（满足开发、运维、客服...都能方便查看）
	 * @Description: 
	 * 	示例传参格式： "name:张三", "age:格式错误", "age:20" ——> [name:张三, age:格式错误]
	 * 
	 * @author kerol
	 * @date Jan 19, 2018 3:16:41 PM
	 */
	public static void warn(Object... args) {
		getLogger().warn(Arrays.asList(args).toString());
	}
	
	public static void main(String[] args) {
		System.err.println(StringUtils.reverse(StringUtils.reverse(Thread.currentThread().getStackTrace()[1].getClassName()).split("\\.")[0]));
		System.err.println(Thread.currentThread().getStackTrace()[1].getMethodName());
		LogUtils.error("测试", 1, 'a', 0.5);
		
		System.err.println(3600001 / 2000);
	}
	
}
