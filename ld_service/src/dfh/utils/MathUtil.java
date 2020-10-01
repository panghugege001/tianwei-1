package dfh.utils;

import java.math.BigDecimal;
import java.text.NumberFormat;

public class MathUtil {

	public MathUtil() {
	}

	public static boolean isNum(String str) {
		return str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
	}

	public static Double add(Double v1, Double v2) {
		BigDecimal b1 = new BigDecimal(v1.toString());
		BigDecimal b2 = new BigDecimal(v2.toString());
		return b1.add(b2).doubleValue();
	}

	public static Double sub(Double v1, Double v2) {
		BigDecimal b1 = new BigDecimal(v1.toString());
		BigDecimal b2 = new BigDecimal(v2.toString());
		return b1.subtract(b2).doubleValue();
	}

	public static Double mul(Double v1, Double v2) {
		BigDecimal b1 = new BigDecimal(v1.toString());
		BigDecimal b2 = new BigDecimal(v2.toString());
		return b1.multiply(b2).doubleValue();
	}

	public static Double div(Double v1, Double v2) {
		return div(v1, v2, 10);
	}

	public static Double div(Double v1, Double v2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		} else {
			BigDecimal b1 = new BigDecimal(v1.toString());
			BigDecimal b2 = new BigDecimal(v2.toString());
			return b1.divide(b2, scale, 4).doubleValue();
		}
	}

	public static double doubleFormat(double value, int scale) {
		return new Double(String.format("%." + scale + "f", value));
	}

	public static String doubleTrans(double value) {
		value = new Double(String.format("%.4f", value));
		NumberFormat nf = NumberFormat.getInstance();
		nf.setGroupingUsed(false);
		nf.setMaximumFractionDigits(4);
		return nf.format(value);
	}
}
