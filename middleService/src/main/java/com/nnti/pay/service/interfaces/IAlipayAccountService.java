package com.nnti.pay.service.interfaces;

import com.nnti.pay.model.vo.AlipayAccount;

/**
 * Created by wander on 2017/2/6.
 */
public interface IAlipayAccountService {
    AlipayAccount findLoginNameAndDisable(AlipayAccount alipayAccount);
}
