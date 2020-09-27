package com.nnti.common.utils;

import com.nnti.common.constants.Constant;
import com.nnti.common.exception.BusinessException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;

public final class NumericUtil {

	// 默认除法运算精度
	private static final int DEF_DIV_SCALE = 2;

	private static NumberFormat nf;

	static {

		nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);
		nf.setMinimumFractionDigits(2);
		nf.setGroupingUsed(false);
	}

	public NumericUtil() {

	}

	public static void main(String[] args) throws Exception {

		// System.out.println(isNumeric(String.valueOf(-11.00)));
		System.out.println(NumericUtil.sub(600, 100));
	}

	// 判断是否为整数
	public static boolean isNumeric(String str) {

		Pattern pattern = Pattern.compile(Constant.REG_NUMERIC);

		return pattern.matcher(str).matches();
	}

	// 判断是金额格式两位小数点
	public static boolean isMoney(String str) {

		if (StringUtils.isBlank(str)) {

			return false;
		}

		Pattern pattern = Pattern.compile(Constant.REG_NUMERIC_DOUBLE);

		return pattern.matcher(str).matches();
	}

	// 判断是否为正整数
	public static boolean isInteger(String str) {

		Pattern pattern = Pattern.compile(Constant.REG_INTEGER);

		return pattern.matcher(str).matches();
	}

	// 数值格式化，保留两位小数，数值会进行四舍五入
	public static String formatDouble(Double number) {

		return nf.format(number);
	}

	// 提供精确的乘法运算
	public static Double mul(double v1, double v2) throws BusinessException {

		Assert.notEmpty(v1, v2);

		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));

		return b1.multiply(b2).doubleValue();
	}

	// 提供精确的小数位四舍五入
	public static Double round(double v, int scale) throws BusinessException {

		Assert.notEmpty(v, scale);

		BigDecimal b = new BigDecimal(Double.toString(v));
		BigDecimal one = new BigDecimal("1");

		return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	// 提供精确的加法运算
	public static Double add(Double v1, Double v2) throws BusinessException {

		Assert.notEmpty(v1, v2);

		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));

		return b1.add(b2).doubleValue();
	}

	// 提供精确的减法运算
	public static Double sub(double v1, double v2) throws BusinessException {

		Assert.notEmpty(v1, v2);

		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));

		return b1.subtract(b2).doubleValue();
	}

	// 提供精确的除法运算
	public static Double div(double v1, double v2) throws BusinessException {

		Assert.notEmpty(v1, v2);

		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));

		return b1.divide(b2, DEF_DIV_SCALE, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	// 判断数字大于0
	public static <T extends Number> boolean isGtZero(T ls) {

		return ls != null && ls.doubleValue() > 0;
	}
}