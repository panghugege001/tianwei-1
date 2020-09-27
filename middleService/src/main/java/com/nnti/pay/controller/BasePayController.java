package com.nnti.pay.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.nnti.common.constants.Constant;
import com.nnti.common.constants.ErrorCode;
import com.nnti.common.exception.BusinessException;
import com.nnti.common.model.vo.Dictionary;
import com.nnti.common.model.vo.User;
import com.nnti.common.security.AESUtil;
import com.nnti.common.service.interfaces.IDictionaryService;
import com.nnti.common.utils.Assert;
import com.nnti.common.utils.DateUtil;
import com.nnti.common.utils.MyUtils;
import com.nnti.common.utils.MyWebUtils;
import com.nnti.pay.controller.vo.PayRequestVo;
import com.nnti.pay.controller.vo.ReturnVo;
import com.nnti.pay.model.common.Response;
import com.nnti.pay.model.vo.MerchantPay;
import com.nnti.pay.service.interfaces.IMerchantPayService;

/**
 * Created by wander on 2017/1/26.
 */
public class BasePayController {
    protected Logger log = Logger.getLogger(BasePayController.class);
    protected Gson gson = new Gson();
    protected ObjectMapper JSON = new ObjectMapper();
    protected String SUCCESS = "SUCCESS";
    protected String success = "success";
    protected String OK = "OK";
    protected String ok = "ok";
    protected String ERROR = "ERROR";
    protected String error = "error";

    private static final String PAY_CENTER_KEY = "smkldospdosldaaa";

    @Autowired
    private IDictionaryService dictionaryService;
    @Autowired
    private IMerchantPayService merchantPayService;

    public Object getSession(HttpServletRequest req, String session) {
        return req.getSession().getAttribute(session);
    }

    public String getIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        log.debug("getIpAddr-->" + ip);
        if (StringUtils.isNotEmpty(ip)) {
            String[] ipArray = ip.split(",");
            return ipArray[0];
        } else {
            return ip;
        }
    }

    protected String getHost(HttpServletRequest request) {
        StringBuffer url = request.getRequestURL();
        String tempContextUrl = url.delete(url.length() - request.getRequestURI().length(), url.length()).append("/").toString();
        return tempContextUrl;
    }

    /*** 判断加白ip */
    protected boolean validationTrustIp(HttpServletRequest request, String type) throws Exception {
        Dictionary dictionary = new Dictionary(type);
        List<Dictionary> ips = dictionaryService.findByCondition(dictionary);
        String requestIp = request.getParameter("requestIp");
        String ip = getIp(request);
        log.info("requestIp:" + requestIp + ",访问IP：" + ip);
        if (ips != null && ips.size() > 0) {
            for (Dictionary d : ips) {
                if (d.getDictValue().equals(requestIp)) {
                    return true;
                }
            }
        } else {
            return true;
        }
        return false;
    }

    /**** 加密数据 */
    protected Response transfer(Object data) throws Exception {

        Response response = new Response();
        response.setResponseEnum(ErrorCode.SC_10000);
        response.setData(AESUtil.encrypt(JSON.writeValueAsString(data), Constant.PAY_KEY));

        return response;
    }

    /*** 获取加密参数 */
    protected PayRequestVo pasePayRequest(HttpServletRequest req, String requestData) throws BusinessException {
        PayRequestVo vo = null;

        String requestJsonData = requestData;
        if (!MyUtils.isNotEmpty(requestData)) {
            requestJsonData = MyWebUtils.getStringAttribute(req, "requestJsonData");
        }
        Assert.notEmpty(requestJsonData);
        requestJsonData = AESUtil.decrypt(requestJsonData, Constant.PAY_KEY);
        vo = gson.fromJson(requestJsonData, PayRequestVo.class);

        return vo;
    }

    /**** 验证用户不存在 */
    protected void validationLoginName(String loginName, User user) throws BusinessException {
        BusinessException be = new BusinessException();
        if (!MyUtils.isNotEmpty(user)) {
            be.setCode(ErrorCode.SC_30000_107.getCode());
            be.setMessage(String.format(ErrorCode.SC_30000_107.getType(), loginName));
            throw be;
        }
    }

    /**** 新玩家需满 date天 才能支付 */
    protected void validationNewUser(User user, Integer date) throws BusinessException {
        BusinessException be = new BusinessException();
        Assert.notEmpty(date, user, user.getCreateTime());
        if (user.getCreateTime().getTime() > DateUtil.getDateByDateNumber(date).getTime()) {
            be.setCode(ErrorCode.SC_30000_112.getCode());
            be.setMessage(String.format(ErrorCode.SC_30000_112.getType(), Math.abs(date)));
            throw be;
        }
    }

    /*** 验证金额熔断 支付限制 判断当月存款金额 */
    protected void validationAmountCut(String loginName, MerchantPay merchantPay) throws Exception {
        if (merchantPay.getAmountCut() != 0) {
            Double custAmountOnMonth = merchantPayService.countCustAmountOnMonth(loginName);
            log.info("当月充值金额：" + custAmountOnMonth);
            if ((merchantPay.getAmountCut() < 0 && custAmountOnMonth > Math.abs(merchantPay.getAmountCut())) ||
                    (merchantPay.getAmountCut() > 0 && custAmountOnMonth < Math.abs(merchantPay.getAmountCut()))) {
                throw new BusinessException(ErrorCode.SC_30000_103);
            }
        }
    }

    /*** 验证金额熔断 支付限制 判断总存款金额*/
    protected void validationAmountCutAll(String loginName, MerchantPay merchantPay) throws Exception {
        if (merchantPay.getAmountCut() != 0) {
            Double custAmountOnAll = merchantPayService.countCustAmountOnAll(loginName);
            log.info("总存款金额：" + custAmountOnAll);
            if ((merchantPay.getAmountCut() < 0 && custAmountOnAll > Math.abs(merchantPay.getAmountCut())) ||
                    (merchantPay.getAmountCut() > 0 && custAmountOnAll < Math.abs(merchantPay.getAmountCut()))) {
                throw new BusinessException(ErrorCode.SC_30000_103);
            }
        }
    }

    /*** 代理不能使用在线支付*/
    protected void validationAgent(String agent) throws BusinessException {
        BusinessException be = new BusinessException();
        if (!Constant.MONEY_CUSTOMER.equals(agent)) {
            be.setCode(ErrorCode.SC_30000_101.getCode());
            be.setMessage(String.format(ErrorCode.SC_30000_101.getType()));
            throw be;
        }
    }

    /*** 点卡 */
    protected void validationCard(String cardType ,String cardCode) throws Exception {
        BusinessException be = new BusinessException();
        Dictionary dic = dictionaryService.findByOne(cardType, cardCode, 1);
        if (dic == null || dic.getUseable() == 0) {
            be.setCode(ErrorCode.SC_30000_115.getCode());
            be.setMessage(String.format(ErrorCode.SC_30000_115.getType()));
            throw be;
        }
    }

    /**** 验证金额*/
    protected void validationMerchantPay(String orderAmount, MerchantPay merchantPay) throws BusinessException {
        BusinessException be = new BusinessException();
        //判断支付通道是否开启
        if (!MyUtils.isNotEmpty(merchantPay) ||
                Constant.PAY_SWITCH_OFF.equals(merchantPay.getPaySwitch()) ||
                Constant.PAY_USEABLE_OFF.equals(merchantPay.getUseable())) {
            be.setCode(ErrorCode.SC_30000_102.getCode());
            be.setMessage(ErrorCode.SC_30000_102.getType());
            throw be;
        }

        // 判断订单金额
        if (!MyUtils.isNotEmpty(orderAmount)) {
            be.setCode(ErrorCode.SC_30000_104.getCode());
            be.setMessage(ErrorCode.SC_30000_104.getType());
            throw be;
        }

        //判断最低存款
        if (Double.valueOf(orderAmount) < merchantPay.getMinPay()) {
            be.setCode(ErrorCode.SC_30000_105.getCode());
            be.setMessage(String.format(ErrorCode.SC_30000_105.getType(), merchantPay.getMinPay()));
            throw be;
        }

        //判断最高存款
        if (Double.valueOf(orderAmount) > merchantPay.getMaxPay()) {
            be.setCode(ErrorCode.SC_30000_106.getCode());
            be.setMessage(String.format(ErrorCode.SC_30000_106.getType(), merchantPay.getMaxPay()));
            throw be;
        }
    }

    /*** AES加密 支付发起请求 */
    protected Response sendHttpByAES(String url, PayRequestVo vo) throws Exception {
        String responseString;
        ReturnVo returnVo = new ReturnVo();

        String requestJson = JSON.writeValueAsString(vo);
        requestJson = AESUtil.encrypt(requestJson, Constant.PAY_KEY);

        responseString = MyWebUtils.getHttpContentByParam(url, MyWebUtils.getListNamevaluepair("requestData", requestJson));

        log.info("接口返回：" + responseString);

        Response message = JSON.readValue(responseString, Response.class);

        if (message.getData() != null && !"".equals(message.getData())) {
            String decrypt = AESUtil.decrypt(message.getData().toString(), Constant.PAY_KEY);

            returnVo = JSON.readValue(decrypt, ReturnVo.class);

            message.setData(returnVo);
        }

        return message;
    }
}
