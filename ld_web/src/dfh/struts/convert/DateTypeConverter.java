package dfh.struts.convert;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.util.StrutsTypeConverter;

public class DateTypeConverter extends StrutsTypeConverter {

	private static final Logger log = Logger.getLogger(DateTypeConverter.class);

	// 暂时只考虑这几种日期格式
	public static final DateFormat[] ACCEPT_DATE_FORMATS = { new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), new SimpleDateFormat("yyyy-MM-dd") };

	public DateTypeConverter() {
	}

	@Override
	public Object convertFromString(Map context, String[] values, Class toClass) {
		if (StringUtils.isNotEmpty(values[0])) {
			for (DateFormat format : ACCEPT_DATE_FORMATS) {
				try {
					return format.parse(values[0]);
				} catch (ParseException e) {
					continue;
				} catch (RuntimeException e) {
					continue;
				}
			}
		}
		log.debug("can not format date string:" + values[0]);
		return null;
	}

	@Override
	public String convertToString(Map context, Object o) {
		if (o instanceof Date) {
			for (DateFormat format : ACCEPT_DATE_FORMATS) {
				try {
					return format.format((Date) o);
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
			}
		}
		return "";
	}

}
