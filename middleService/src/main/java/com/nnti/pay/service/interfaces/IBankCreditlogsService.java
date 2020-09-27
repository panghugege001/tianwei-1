package com.nnti.pay.service.interfaces;

import com.nnti.common.exception.BusinessException;
import com.nnti.pay.model.vo.BankCreditlogs;

/**
 * Created by wander on 2017/2/7.
 */
public interface IBankCreditlogsService {
    Integer add(BankCreditlogs bankCreditlogs);

    void updateAmount(Long id, Double bankAmount, String orderId) throws BusinessException;
}
