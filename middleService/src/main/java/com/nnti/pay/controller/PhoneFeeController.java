package com.nnti.pay.controller;

import com.nnti.common.utils.*;
import com.nnti.pay.controller.vo.PhoneFeeVo;
import com.nnti.pay.service.interfaces.IPhoneFeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 话费Controller
 */
@Controller
@RequestMapping("/phonefee")
public class PhoneFeeController extends BasePayController {

    @Autowired
    private IPhoneFeeService phoneFeeService;


    @ResponseBody
    @RequestMapping(value = "/phoneFeeCallBack", produces = "application/json; charset=utf-8")
    public String phoneFeeCallBack(PhoneFeeVo vo, HttpServletRequest req) {

        log.info("话费充值回调--接收参数：" + MyWebUtils.getRequestParameters(req));

        try {
            String msg = phoneFeeService.phoneFeeCallBack(vo);
            return msg;
        } catch (Exception e) {
            log.error("回调异常：", e);
            return "error";
        }
        
    }
    
}
