package com.nnti.common.advice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nnti.common.constants.Constant;
import com.nnti.common.constants.FunctionCode;
import com.nnti.common.exception.BusinessException;
import com.nnti.common.security.AESUtil;
import com.nnti.common.utils.MyUtils;
import com.nnti.pay.model.common.Response;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice(basePackages = {"com.nnti.pay.controller"})
public class CommonViewControllerAdvice {

    private static Logger log = Logger.getLogger(CommonViewControllerAdvice.class);

    private ObjectMapper JSON = new ObjectMapper();

    @InitBinder
    public void initAdvice(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String requestJsonData = request.getParameter("requestData");

        if (MyUtils.isNotEmpty(requestJsonData)) {

            requestJsonData = AESUtil.decrypt(requestJsonData, Constant.PAY_KEY);

            log.info("CommonViewControllerAdvice 请求参数：" + requestJsonData);

            request.setAttribute("requestJsonData", requestJsonData);
        }
    }

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Response exceptionAdvice(Exception e) {

        Response model = new Response();

        BusinessException be = null;

        log.error("系统异常:", e);

        if (e instanceof BusinessException) {

            be = (BusinessException) e;
        } else if (e instanceof Exception) {

            be = new BusinessException(FunctionCode.SC_10001.getCode(), FunctionCode.SC_10001.getType());
        }

        model.setCode(be.getCode());
        model.setDesc(be.getMessage());

        return model;
    }
}