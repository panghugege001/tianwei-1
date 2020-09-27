package com.nnti.common.service.interfaces;

import java.util.Map;

import com.nnti.common.model.vo.BankStatus;


public interface IBankStatusService {

	String getWithDrawBankStatus(BankStatus bs) throws Exception;
}