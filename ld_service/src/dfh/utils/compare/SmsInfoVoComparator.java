package dfh.utils.compare;

import java.text.ParseException;
import java.util.Comparator;

import dfh.model.bean.SmsInfoVo;
import dfh.utils.DateUtil;

public class SmsInfoVoComparator implements Comparator<SmsInfoVo> {

	@Override
	public int compare(SmsInfoVo o1, SmsInfoVo o2) {
		try {
			return DateUtil.fmtStandard(o2.getDate()).compareTo(DateUtil.fmtStandard(o1.getDate()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return -1;
	}

}
