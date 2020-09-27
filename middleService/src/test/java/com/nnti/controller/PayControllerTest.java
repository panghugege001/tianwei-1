package com.nnti.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.nnti.common.exception.BusinessException;
import com.nnti.common.security.AESUtil;
import com.nnti.common.utils.MyWebUtils;
import com.nnti.pay.model.common.Response;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wander on 2017/2/20.
 */
public class PayControllerTest {

    private Gson gson = new Gson();
    ObjectMapper om = new ObjectMapper();

    @Test
    public void pay_way() throws BusinessException, IOException {
        String url = "http://localhost:17080/pay/pay_way";
        String result = MyWebUtils.getHttpContentByBtParam(url, "usetype=2&loginname=woodytest");
        System.out.println(result);
        Response data = gson.fromJson(result, Response.class);
        data.getData();
        result = AESUtil.decrypt(data.getData().toString());
        System.out.println(result);
    }

    @Test
    public void auto() throws BusinessException {
        String url = "http://localhost:17080/autoSupplement/all";

        Map map = new HashMap();
        map.put("orderid", "asdf");
        map.put("money", "10");
        map.put("loginname", "woodytest");
        String result = MyWebUtils.getHttpContentByParam(url, MyWebUtils.getListNamevaluepair(map));
        System.out.println(result);
    }
    
    @Test
    public void cmb() throws BusinessException {
        String url = "http://119.28.11.28:7080/pay/deposit/mfb_zfb_wy";
        String str = "{\"sign\":\"fceb3f7406f7bcab0da9d1ef2f9ca0a0edc549d21954c4a6fdeafdea9d55b391\",\"type\":\"deposit_result\",\"data\":\"[{\\\"order_number\\\":\\\"131554092060000000-163.00\\\",\\\"deposit_cardnumber\\\":\\\"6214830158987759\\\",\\\"amount\\\":10.00,\\\"fee\\\":0.00,\\\"deposit_time\\\":\\\"2017-11-17 16:20:06\\\",\\\"client_accountname\\\":\\\"hyq\\\",\\\"client_postscript\\\":\\\"hyq123\\\",\\\"client_transtype\\\":\\\"wyzz\\\"}]\"}";
        String str2 = "{\"sign\":\"fceb3f7406f7bcab0da9d1ef2f9ca0a0edc549d21954c4a6fdeafdea9d55b391\",\"type\":\"deposit_result\",\"data\":\"[{\\\"order_number\\\":\\\"131554092060000000-163.00\\\"}]\"}";
        System.out.println(str);
        String result = MyWebUtils.getHttpContentByBtParam(url, "deposit_result="+str);
        System.out.println(result);
    }
}
