package dfh.dao;

import org.apache.commons.lang.RandomStringUtils;
import org.hibernate.LockMode;

import dfh.model.Seq;
import dfh.model.enums.ProposalType;
import dfh.utils.Constants;
import dfh.utils.DateUtil;
import dfh.utils.StringUtil;

public class SeqDao extends UniversalDao {

	static final Integer PROPOSAL_DIGITS = 4;
	
	static final Integer AUTOTASK_DIGITS = 3;

	public SeqDao() {
	}

	public String generateAutoTaskID() {
		String id = "";
		Seq seq = (Seq) get(Seq.class, Constants.SEQ_AUTOTASKID, LockMode.UPGRADE);
		if (seq != null) {
			String seqvalue = seq.getSeqValue();
			if (seqvalue.toString().startsWith(DateUtil.getYYMMDD()))
				id = DateUtil.getYYMMDD() + StringUtil.formatNumberToDigits(Integer.parseInt(seqvalue.substring(6)) + 1, AUTOTASK_DIGITS);
			else
				id = DateUtil.getYYMMDD() + StringUtil.formatNumberToDigits(Integer.valueOf(1), AUTOTASK_DIGITS);

			seq.setSeqValue(id);
			update(seq);
		} else {
			seq = new Seq();
			seq.setSeqName(Constants.SEQ_AUTOTASKID);
			id = DateUtil.getYYMMDD() + StringUtil.formatNumberToDigits(Integer.valueOf(1), AUTOTASK_DIGITS);
			seq.setSeqValue(id);
			save(seq);
		}
		return  "444"+id;
	}
	
	public String generateProposalPno(ProposalType type) {
		/*String id = "";
		Seq seq = (Seq) get(Seq.class, Constants.SEQ_PROPOSALID, LockMode.UPGRADE);
		if (seq != null) {
			String seqValue = seq.getSeqValue();
			seq.setSeqValue((Long.parseLong(seqValue) + 1) + "");
			update(seq);
		} else {
			seq = new Seq();
			seq.setSeqName(Constants.SEQ_PROPOSALID);
			seq.setSeqValue("1510400000");
			save(seq);
		}
		return  type.getCode()+seq.getSeqValue();*/
		return type.getCode() + DateUtil.getYYMMDDN() + RandomStringUtils.randomAlphanumeric(10).toLowerCase();
	}
	
	public String generateYhjID() {
		/*Seq seq = (Seq) get(Seq.class, Constants.SEQ_COUPON_ID, LockMode.UPGRADE);
		if (seq != null) {
			String seqValue = seq.getSeqValue();
			seq.setSeqValue((Long.parseLong(seqValue) + 1) + "");
			update(seq);
		} else {
			seq = new Seq();
			seq.setSeqName(Constants.SEQ_COUPON_ID);
			seq.setSeqValue("1");
			save(seq);
		}
		return seq.getSeqValue();*/
		return "yhj" + DateUtil.getYYMMDDN() + RandomStringUtils.randomAlphanumeric(10).toLowerCase();
	}
	
	public String generatePt8Yuan() {
		/*Seq seq = (Seq) get(Seq.class, Constants.SEQ_PT8_SELF, LockMode.UPGRADE);
		if (seq != null) {
			String seqValue = seq.getSeqValue();
			seq.setSeqValue((Long.parseLong(seqValue) + 1) + "");
			update(seq);
		} else {
			seq = new Seq();
			seq.setSeqName(Constants.SEQ_PT8_SELF);
			seq.setSeqValue("1");
			save(seq);
		}
		return seq.getSeqValue();*/
		return DateUtil.getYYMMDDN() + RandomStringUtils.randomAlphanumeric(10).toLowerCase();
	}


}
