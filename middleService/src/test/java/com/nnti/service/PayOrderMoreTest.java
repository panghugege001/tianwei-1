package com.nnti.service;

import com.nnti.base.BaseTest;
//import com.nnti.pay.model.vo.PayOrder;
import com.nnti.pay.service.interfaces.IPayOrderService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by wander on 2017/1/20.
 */
public class PayOrderMoreTest extends BaseTest {

    @Autowired
    private IPayOrderService payOrderService;

    @Test
    public void findByPage() {
//        PayOrder payOrder = new PayOrder();
//        payOrder.setBillNo("ddd");
//        payOrder.setType(2);

//        PageInfo pageInfo = new PageInfo(1, 10);
//        payOrderService.findByPage(pageInfo, payOrder);
    }
}
