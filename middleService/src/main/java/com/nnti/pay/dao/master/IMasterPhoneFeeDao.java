package com.nnti.pay.dao.master;


import com.nnti.pay.model.vo.PhoneFeeRecord;

public interface IMasterPhoneFeeDao {

	int update(PhoneFeeRecord phoneFeeRecord);
	
	PhoneFeeRecord findPhoneFeeRecord(String order_id);
	
}