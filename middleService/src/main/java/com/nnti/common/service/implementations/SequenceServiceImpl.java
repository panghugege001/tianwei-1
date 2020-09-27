package com.nnti.common.service.implementations;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import com.nnti.common.service.interfaces.ISequenceService;
import com.nnti.common.utils.DateUtil;

@Service
public class SequenceServiceImpl implements ISequenceService {

	public String generateProposalNo(String type) throws Exception {
		
		return type + DateUtil.format(DateUtil.YYMMDD, new Date()) + RandomStringUtils.randomAlphanumeric(10).toLowerCase();
	}

	public String generateTransferId() throws Exception {
		
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.YEAR, 5);
		
		SimpleDateFormat yyMMddHHmmssSSS = new SimpleDateFormat("yyMMddHHmmssSSS");
		String str = yyMMddHHmmssSSS.format(calendar.getTime());
		
		return str + RandomStringUtils.randomNumeric(4);
	}
	
	public String generateYhjId() throws Exception {
		
		DateFormat sdf = new SimpleDateFormat("yyMMdd");
		String str = sdf.format(new Date());
		
		return "yhj" + str + RandomStringUtils.randomAlphanumeric(10).toLowerCase();
	}
}