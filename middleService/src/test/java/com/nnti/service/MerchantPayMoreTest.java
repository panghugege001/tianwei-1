package com.nnti.service;

import com.nnti.base.BaseTest;
import com.nnti.common.exception.BusinessException;
import com.nnti.pay.model.vo.MerchantPay;
import com.nnti.pay.service.interfaces.IMerchantPayService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by wander on 2017/1/25.
 */
public class MerchantPayMoreTest extends BaseTest {

    @Autowired
    private IMerchantPayService merchantPayService;

    @Test
    public void findByMerNo() throws BusinessException {
        MerchantPay merchantPay = merchantPayService.findByMerNo("1004056");
        log.info("id:" + merchantPay.getId());
    }
}
