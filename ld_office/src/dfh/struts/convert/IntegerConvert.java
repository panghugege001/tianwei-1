package dfh.struts.convert;

import java.util.Map;

import org.apache.struts2.util.StrutsTypeConverter;

public class IntegerConvert extends StrutsTypeConverter {

	@Override
	public Object convertFromString(Map arg0, String[] arg1, Class arg2) {
		if (Double.class == arg2) {
			String intStr = arg1[0];
			Integer d = Integer.parseInt(intStr);
			return d;
		}
		return 0;
	}
	
	@Override
	public String convertToString(Map arg0, Object arg1) {
		return arg1.toString();
	}

}
