// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   SeqServiceImpl.java

package dfh.service.implementations;

import org.apache.commons.lang.RandomStringUtils;
import org.hibernate.LockMode;

import dfh.model.Actionlogs;
import dfh.model.Seq;
import dfh.model.enums.ActionLogType;
import dfh.service.interfaces.SeqService;
import dfh.utils.Constants;
import dfh.utils.DateUtil;
import dfh.utils.StringUtil;

// Referenced classes of package dfh.service.implementations:
//			UniversalServiceImpl

public class SeqServiceImpl extends UniversalServiceImpl implements SeqService {

	static final Integer NETPAY_DIGITS = Integer.valueOf(8);
	static final Integer PROPOSAL_DIGITS = Integer.valueOf(4);

	public SeqServiceImpl() {
	}

	// 得到在线支付维一订单号
	public String generateNetpayBillno() {
		String id = "";
		Seq seq = (Seq) get(Seq.class, "SEQ_NETPAYID", LockMode.UPGRADE);
		if (seq != null) {
			String seqvalue = seq.getSeqValue();
			if (seqvalue.toString().startsWith(DateUtil.getYYMMDD()))
				id = DateUtil.getYYMMDD() + StringUtil.formatNumberToDigits(Integer.valueOf(Integer.parseInt(seqvalue.substring(6)) + 1), NETPAY_DIGITS);
			else
				id = DateUtil.getYYMMDD() + StringUtil.formatNumberToDigits(Integer.valueOf(1), NETPAY_DIGITS);
			seq.setSeqValue(id);
			update(seq);
		} else {
			seq = new Seq();
			seq.setSeqName("SEQ_NETPAYID");
			id = DateUtil.getYYMMDD() + StringUtil.formatNumberToDigits(Integer.valueOf(1), NETPAY_DIGITS);
			seq.setSeqValue(id);
			save(seq);
		}
		return Constants.NETPAYORDER + id;
	}
	
	// 用户登录AG平台单号 update sun 15位的转账编号
	public String generateLoginID(String loginname) {
		/*Seq seq = (Seq) get(Seq.class, "SEQ_LOGINID", LockMode.UPGRADE);
		if (seq != null) {
			String seqvalue = seq.getSeqValue();
			seq.setSeqValue((Long.parseLong(seqvalue) + 1) + "");
			update(seq);
		} else {
			seq = new Seq();
			seq.setSeqName("SEQ_LOGINID");
			seq.setSeqValue("200000000000000");
			save(seq);
		}*/
		Actionlogs actionlog = new Actionlogs();
		actionlog.setLoginname(loginname);
		actionlog.setCreatetime(DateUtil.now());
		actionlog.setAction(ActionLogType.LOGIN_AG_GAME.getCode());
		save(actionlog);
//		return seq.getSeqValue();
		return DateUtil.getYYMMDDN() + RandomStringUtils.randomAlphanumeric(15).toLowerCase();
	}
	
	// 用户登录Bbin平台单号 update sun 15位的转账编号
	public String generateLoginBbinID(String loginname) {
		/*Seq seq = (Seq) get(Seq.class, "SEQ_LOGINID", LockMode.UPGRADE);
		if (seq != null) {
			String seqvalue = seq.getSeqValue();
			seq.setSeqValue((Long.parseLong(seqvalue) + 1) + "");
			update(seq);
		} else {
			seq = new Seq();
			seq.setSeqName("SEQ_LOGINID");
			seq.setSeqValue("200000000000000");
			save(seq);
		}*/
		Actionlogs actionlog = new Actionlogs();
		actionlog.setLoginname(loginname);
		actionlog.setCreatetime(DateUtil.now());
		actionlog.setAction(ActionLogType.LOGIN_BBIN_GAME.getCode());
		save(actionlog);
//		return seq.getSeqValue();
		return DateUtil.getYYMMDDN() + RandomStringUtils.randomAlphanumeric(15).toLowerCase();
	}
	
	// 用户登录Keno平台单号 update sun 15位的转账编号
	public String generateLoginKenoID(String loginname) {
		Seq seq = (Seq) get(Seq.class, "SEQ_LOGINID", LockMode.UPGRADE);
		if (seq != null) {
			String seqvalue = seq.getSeqValue();
			seq.setSeqValue((Long.parseLong(seqvalue) + 1) + "");
			update(seq);
		} else {
			seq = new Seq();
			seq.setSeqName("SEQ_LOGINID");
			seq.setSeqValue("200000000000000");
			save(seq);
		}
		Actionlogs actionlog = new Actionlogs();
		actionlog.setLoginname(loginname);
		actionlog.setCreatetime(DateUtil.now());
		actionlog.setAction(ActionLogType.LOGIN_KENO_GAME.getCode());
		save(actionlog);
		return seq.getSeqValue();
	}

	// 用户转入转出单号 update sun 15位的转账编号
	public String generateTransferID() {
		Seq seq = (Seq) get(Seq.class, "SEQ_TRANSFERID", LockMode.UPGRADE);
		if (seq != null) {
			String seqvalue = seq.getSeqValue();
			seq.setSeqValue((Long.parseLong(seqvalue) + 1) + "");
			update(seq);
		} else {
			seq = new Seq();
			seq.setSeqName("SEQ_TRANSFERID");
			seq.setSeqValue("200000000000000");
			save(seq);
		}
		return seq.getSeqValue();
	}

	public String generateVisitorID() {
		Seq seq = (Seq) get(Seq.class, Constants.SEQ_VISITORID, LockMode.UPGRADE);
		if (seq != null) {
			String seqValue = seq.getSeqValue();
			seq.setSeqValue((Integer.parseInt(seqValue) + 1) + "");
			update(seq);
		} else {
			seq = new Seq();
			seq.setSeqName(Constants.SEQ_VISITORID);
			seq.setSeqValue("1000000000");
			save(seq);
		}
		return seq.getSeqValue();
	}

	public String generateCommissionID() {
		Seq seq = (Seq) get(Seq.class, Constants.SEQ_COMMISSIONID, LockMode.UPGRADE);
		if (seq != null) {
			String seqValue = seq.getSeqValue();
			seq.setSeqValue((Integer.parseInt(seqValue) + 1) + "");
			update(seq);
		} else {
			seq = new Seq();
			seq.setSeqName(Constants.SEQ_COMMISSIONID);
			seq.setSeqValue("100000000");
			save(seq);
		}
		return Constants.COMMISSIONORDER + seq.getSeqValue();
	}

}
