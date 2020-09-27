package com.nnti.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.nnti.common.exception.BusinessException;
import com.nnti.common.security.AESUtil;
import com.nnti.common.utils.MyWebUtils;
import com.nnti.pay.controller.vo.PayRequestVo;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.junit.Test;

/**
 * Created by wander on 2017/1/25.
 */
public class KDZFControllerTest {

    private ObjectMapper JSON = new ObjectMapper();

    @Test
    public void kdzfb() throws Exception {

        String url = "http://localhost:17080/kd/wx_zfb";
        PayRequestVo vo = new PayRequestVo();
        vo.setLoginName("");
        vo.setPlatformId(33L);
        vo.setOrderAmount("10.00");

        Gson gson = new Gson();
        String requestJson = gson.toJson(vo);
        requestJson = AESUtil.encrypt(requestJson);

        HttpClient client = new HttpClient();
        PostMethod method = new PostMethod(url);
        method.setParameter("requestData", requestJson);

        int statusCode = client.executeMethod(method);
        if (statusCode != HttpStatus.SC_OK) {
            System.out.println("Method failed：" + method.getStatusLine());
        }
        NameValuePair code = method.getParameter("code01");
        System.out.println(code.getValue());

        byte[] responseBody = method.getResponseBody();
        String responseString = new String(responseBody);

        System.out.println("返回的明文json数据：" + responseString);
    }

    @Test
    public void zfb_wx_return() throws BusinessException {
//        String url = "http://127.0.0.1:17080/kd/zfb_wx_return";
//        String url = "http://pay.ulback.com:3113/xb/zfb_wx_return.php";
//        String url = "http://localhost:7088/xb/zfb_wx_return.php";
        String url = "http://pay.ulback.com:3113/dinpay/point_card_return.php";
        String result = MyWebUtils.getHttpContentByParam(url,
                MyWebUtils.getListNamevaluepair("P_OrderId", "ws_kdzfb_woodytest_4000005","P_Price","10","P_UserId","1004056","P_Notic","woodytest","P_ErrCode","0"));
        System.out.println(result);
    }
}
