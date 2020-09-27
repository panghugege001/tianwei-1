package com.nnti.pay.service.interfaces;

import com.nnti.pay.controller.vo.PhoneFeeVo;


/**
 */
public interface IPhoneFeeService {

	String phoneFeeCallBack(PhoneFeeVo vo) throws Exception;
}
