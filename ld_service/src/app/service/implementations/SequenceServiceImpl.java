package app.service.implementations;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;
import org.hibernate.LockMode;
import dfh.model.Seq;
import dfh.utils.Constants;
import app.dao.BaseDao;
import app.service.interfaces.ISequenceService;

public class SequenceServiceImpl implements ISequenceService {
	
	private Logger log = Logger.getLogger(SequenceServiceImpl.class);
	
	private BaseDao baseDao;
	
	public String generateUserId() {
		
		Seq seq = null;
		
		Object obj = baseDao.get(Seq.class, Constants.SEQ_USERID, LockMode.UPGRADE);
		log.info("generateUserId方法获取ID结果：" + obj);
		
		if (obj != null) {
			
			seq = (Seq) obj;
			String seqValue = seq.getSeqValue();
			seq.setSeqValue(String.valueOf((Integer.parseInt(seqValue) + 1)));
			
			baseDao.update(seq);
		} else {
			
			seq = new Seq();
			seq.setSeqName(Constants.SEQ_USERID);
			seq.setSeqValue("20001");
			
			baseDao.save(seq);
		}
		
		return seq.getSeqValue();
	}
	
	public String generateProposalNo(Integer code) {
	
		DateFormat sdf = new SimpleDateFormat("yyMMdd");
		String str = sdf.format(new Date());
		
		return code + str + RandomStringUtils.randomAlphanumeric(10).toLowerCase();
	}
	
	public String generateTransferId() {
		
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.YEAR, 5);
		
		DateFormat yyMMddHHmmssSSS = new SimpleDateFormat("yyMMddHHmmssSSS");
		
		String str = yyMMddHHmmssSSS.format(calendar.getTime());
		
		return str + RandomStringUtils.randomNumeric(4);
	}
	
	public String generateYhjId() {
		
		DateFormat sdf = new SimpleDateFormat("yyMMdd");
		String str = sdf.format(new Date());
		
		return "yhj" + str + RandomStringUtils.randomAlphanumeric(10).toLowerCase();
	}
	
	public BaseDao getBaseDao() {
		return baseDao;
	}
	
	public void setBaseDao(BaseDao baseDao) {
		this.baseDao = baseDao;
	}
	
}