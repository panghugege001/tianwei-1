package com.nnti.pay.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.nnti.common.constants.ErrorCode;
import com.nnti.common.model.vo.BankInfo;
import com.nnti.common.model.vo.Dictionary;
import com.nnti.common.model.vo.User;
import com.nnti.common.service.interfaces.IBankInfoService;
import com.nnti.common.service.interfaces.IDictionaryService;
import com.nnti.common.service.interfaces.IUserService;
import com.nnti.common.utils.Assert;
import com.nnti.common.utils.MyUtils;
import com.nnti.common.utils.StringUtil;
import com.nnti.pay.controller.vo.BankVo;
import com.nnti.pay.controller.vo.PayFyVo;
import com.nnti.pay.controller.vo.PayRequestVo;
import com.nnti.pay.controller.vo.PayWayVo;
import com.nnti.pay.controller.vo.ReturnVo;
import com.nnti.pay.model.common.Response;
import com.nnti.pay.model.vo.DepositOrder;
import com.nnti.pay.model.vo.MerchantPay;
import com.nnti.pay.model.vo.TotalDeposit;
import com.nnti.pay.service.interfaces.IBasicService;
import com.nnti.pay.service.interfaces.IDepositOrderService;
import com.nnti.pay.service.interfaces.IMerchantPayService;
import com.nnti.pay.service.interfaces.ITradeService;

/**
 * Created by wander on 2017/2/1.
 */
@Controller
@RequestMapping("/pay")
public class PayController extends BasePayController {       

    @Autowired
    private IMerchantPayService merchantPayService;
    @Autowired
    private ITradeService tradeService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IDictionaryService dictionaryService;
    @Autowired
    private IBasicService basicService;
    @Autowired
    private IDepositOrderService depositOrderService;
    @Autowired
    private IBankInfoService bankinfoService;

    @RequestMapping("/main")
    public ModelAndView pay() {
        ModelAndView model = new ModelAndView("/pay/main");
        return model;
    }

    /*** 提交查询秒存订单号 */
    @ResponseBody
    @RequestMapping(value = "/depOrder", produces = "application/json; charset=utf-8")
    public Response depOrder(String orderNo) throws Exception {
        DepositOrder depositOrder = depositOrderService.findById(orderNo);
        Assert.notEmpty(depositOrder);
        return transfer(depositOrder);
    }

    @ResponseBody
    @RequestMapping(value = "/pay_way", produces = "application/json; charset=utf-8")
    public Response pay_way(Integer usetype, Integer paySwitch, Integer useable, String loginname) throws Exception {

    	User user = userService.get(loginname);
    	
        if (!MyUtils.isNotEmpty(paySwitch)) {
            paySwitch = 1;
        }
        if (!MyUtils.isNotEmpty(useable)) {
            useable = 1;
        }

        List<Integer> usetypes = new ArrayList<>();
        usetypes.add(3);

        Assert.notEmpty(usetype);

        usetypes.add(usetype);

        TotalDeposit totalDeposit = basicService.checkAndUpdateDeposit(loginname);
        List<MerchantPay> mps = merchantPayService.findByPaySwitch(paySwitch, useable, null,usetypes);
        List<PayWayVo> payWayVos = new ArrayList<>();
        Map<String, Integer> pam = new HashMap<String, Integer>();
        pam.put("pay1", 1);
        pam.put("pay2", 1);
        pam.put("pay3", 1);
        pam.put("pay4", 1);
        pam.put("pay5", 1);
        pam.put("pay7", 1);
        pam.put("pay10", 1);
        pam.put("pay13", 1);
        for (MerchantPay vo : mps) {
            PayWayVo pvo = new PayWayVo();
            PayWayVo pvo2 = null;
            PayWayVo pvo3 = null;
            pvo.setId(vo.getId());
            Integer p;
            String showName;
            /*if (vo.getPayWay() == 1) {
                p = pam.get("pay1");
                //showName = "支付宝支付" + p;
                showName = vo.getShowName();
                pam.put("pay1", p + 1);
            } else if (vo.getPayWay() == 2) {
                p = pam.get("pay2");
                //showName = "微信支付" + p;
                showName = vo.getShowName();
                pam.put("pay2", p + 1);
            } else if (vo.getPayWay() == 3) {
                p = pam.get("pay3");
                //showName = "在线支付" + p;
                showName = vo.getShowName();
                pam.put("pay3", p + 1);
            } else if (vo.getPayWay() == 4) {
                p = pam.get("pay4");
                //showName = "快捷支付" + p;
                showName = vo.getShowName();
                pam.put("pay4", p + 1);
            } else if (vo.getPayWay() == 5) {
                p = pam.get("pay5");
                //showName = "点卡支付" + p;
                showName = vo.getShowName();
                pam.put("pay5", p + 1);
            } else if (vo.getPayWay() == 7) {
                p = pam.get("pay7");
                //showName = "QQ扫描支付" + p;
                showName = vo.getShowName();
                pam.put("pay7", p + 1);
            } else if (vo.getPayWay() == 10) {
                p = pam.get("pay10");
                //showName = "京东支付" + p;
                showName = vo.getShowName();
                pam.put("pay10", p + 1);
            } else {
            }*/
            showName = vo.getShowName();
            pvo.setShowName(showName);

            pvo.setPayName(vo.getPayName());
            pvo.setPayPlatform(vo.getPayPlatform());
            pvo.setFee(vo.getFee());
            pvo.setMinPay(vo.getMinPay());
            pvo.setMaxPay(vo.getMaxPay());
            pvo.setAppPay(vo.getAppPay());    
            pvo.setPayWay(vo.getPayWay());
            pvo.setPayCenterUrl(vo.getPayCenterUrl());
            pvo.setPayPlatform(vo.getPayPlatform());
            
            String levels = vo.getLevels();
            if(levels== null || "".equals(levels)){
            	continue;
            }
            //如果是京东支付
            List<String> list = Arrays.asList(new String[]{"dbjd","mbjd","xmjd","xlbjd"});
            List<String> strList = Arrays.asList(new String[]{"xmzfb1","xmzfb2"});
            if(vo.getPayWay() == 10 && list.contains(vo.getPayPlatform())){
            	 pvo2 = new PayWayVo();
            	 BeanUtils.copyProperties(pvo2, vo);
            	 pvo2.setShowName("快捷支付"+pam.get("pay4"));
            	 pam.put("pay4", pam.get("pay4") + 1);
            	 pvo2.setPayWay(4);
            }
            
            //如果是新码支付宝
            else if(vo.getPayWay() == 1 && strList.contains(vo.getPayPlatform())){
            	 pvo2 = new PayWayVo();
            	 BeanUtils.copyProperties(pvo2, vo);
            	 pvo2.setShowName("京东支付"+pam.get("pay10"));
            	 pam.put("pay10", pam.get("pay10") + 1);
            	 pvo2.setPayWay(10);
            	 
            	 pvo3 = new PayWayVo();
            	 BeanUtils.copyProperties(pvo3, vo);
            	 pvo3.setShowName("快捷支付"+pam.get("pay4"));
            	 pam.put("pay4", pam.get("pay4") + 1);
            	 pvo3.setPayWay(4);
            }
            
            if (vo.getAmountCut() > 0 && totalDeposit.getAllDeposit() >= vo.getAmountCut()) {
                payWayVos.add(pvo);
                if(null != pvo2){
                	payWayVos.add(pvo2);
                }
                if(null != pvo3){
                    payWayVos.add(pvo3);
                }
            } else if (vo.getAmountCut() < 0 && totalDeposit.getAllDeposit() <= vo.getAmountCut()) {
            	payWayVos.add(pvo);
            	if(null != pvo2){
                	payWayVos.add(pvo2);
                }
                if(null != pvo3){
                    payWayVos.add(pvo3);
                }
            } else if (vo.getAmountCut() == 0 && StringUtil.isContain(user.getLevel()+"", levels.split(","))) {
            	payWayVos.add(pvo);
            	if(null != pvo2){
                	payWayVos.add(pvo2);
                }
                if(null != pvo3){
                    payWayVos.add(pvo3);
                }
            }
        }
        return transfer(payWayVos);
    }
    
    /*****
     * 银行秒存 微信秒存 通联秒存 通道预加载 
     * @param usetype
     * @param paySwitch
     * @param useable
     * @param loginname
     * @return
     * @throws Exception
     */    
    @ResponseBody
    @RequestMapping(value = "/deposit_way", produces = "application/json; charset=utf-8")    
    public Response deposit_way(String loginname) throws Exception {

        User user = userService.get(loginname);
        String [] bankinfo =  new String[] { "招商银行", "兴业银行","平安银行","民生银行","华夏银行","中信银行","广发银行","桂林银行","邮政储蓄银行","农业银行","建设银行","通联转账","微信","微信转账","云闪付"};
        //总存款
        TotalDeposit totalDeposit = basicService.checkAndUpdateDeposit(loginname);
        
        //网银
        BankInfo wybnak = bankinfoService.findBankinfoByParams(user,"0",totalDeposit,bankinfo);
        //支付宝
        BankInfo zfbbnak = bankinfoService.findBankinfoByParams(user,"1",totalDeposit,bankinfo);
        //微信转账
        BankInfo wxzzbank = bankinfoService.findBankinfoByParams(user,"4",totalDeposit,bankinfo);
        //云闪付
        BankInfo yunbank = bankinfoService.findBankinfoByParams(user,"5",totalDeposit,bankinfo);
        
        
        log.info(wybnak+"网银"+zfbbnak+"支付宝"+wxzzbank+"微信转账"+yunbank+"云闪付");
        
        List<BankVo> bankVos = new ArrayList<>();
        //网银
        if(wybnak != null ){
            BankVo vo = new BankVo("wymc", "true");
            bankVos.add(vo);
        }
        //支付宝
        if(zfbbnak != null ){   
            BankVo vo = new BankVo("zfbmc", "true");
            bankVos.add(vo);
        }
        //微信转账
        if(wxzzbank != null ){   
            BankVo vo = new BankVo("wxzz", "true");
            bankVos.add(vo);
        }
        //云闪付
        if(yunbank != null ){   
            BankVo vo = new BankVo("yunpay", "true");
            bankVos.add(vo);
        }
        return transfer(bankVos);
    }
    
    
    
    

    @ResponseBody
    @RequestMapping(value = "/pay_bank", produces = "application/json; charset=utf-8")
    public Response pay_bank(Long platformId) throws Exception {

        MerchantPay mp = merchantPayService.findById(platformId);
        String type = mp.getPayCenterUrl().replaceAll("/", "") + "_type";

        List<Dictionary> dictionaries = dictionaryService.findByCondition(new Dictionary(type, 1));
        List<BankVo> bankVos = new ArrayList<>();
        for (Dictionary d : dictionaries) {
            BankVo vo = new BankVo(d.getDictName(), d.getDictValue(), d.getDictDesc(), d.getDictShow());
            bankVos.add(vo);
        }
        return transfer(bankVos);
    }

    @ResponseBody
    @RequestMapping(value = "/pay_way3", produces = "application/json; charset=utf-8")
    public List<PayWayVo> pay_way3() {

        List<PayWayVo> payWayVos = new ArrayList<>();
        try {
            List<MerchantPay> data = merchantPayService.findByPaySwitch(null, 1, null,null);
            for (MerchantPay mp : data) {
                PayWayVo vo = new PayWayVo();
                BeanUtils.copyProperties(vo, mp);
                //如果是京东支付
                if(vo.getPayWay() == 10){
                	 PayWayVo pvo2 = new PayWayVo();
                	 BeanUtils.copyProperties(pvo2, mp);
                	 pvo2.setShowName("快捷支付");
                	 pvo2.setPayWay(4);
                	 payWayVos.add(pvo2);
                }
                
                //如果是新码支付宝
                else if(vo.getPayWay() == 1 && vo.getPayPlatform().equals("xmzfb1") || vo.getPayPlatform().equals("xmzfb2") ){
                	 PayWayVo pvo2 = new PayWayVo();
                	 BeanUtils.copyProperties(pvo2, mp);
                	 pvo2.setShowName("京东支付");
                	 pvo2.setPayWay(10);
                	 payWayVos.add(pvo2);
                }
                payWayVos.add(vo);
            }
        } catch (Exception e) {
            log.error("获取支付开关异常：", e);
        }
        return payWayVos;
    }
    
    
    @ResponseBody
    @RequestMapping(value = "/pay_way2", produces = "application/json; charset=utf-8")
    public List<List<PayWayVo>> pay_way2(Integer payWay) { 
    	
        List<List<PayWayVo>> payWayVos = new ArrayList<>();
        List<PayWayVo> payWayVos1 = new ArrayList<>();
        List<PayWayVo> payWayVos2 = new ArrayList<>();
        try {
            List<MerchantPay> data = merchantPayService.findByPaySwitch(null, 1,payWay, null);
            for (MerchantPay mp : data) {
                PayWayVo vo = new PayWayVo();
                BeanUtils.copyProperties(vo, mp);
                payWayVos1.add(vo);
            }
            
            payWayVos.add(payWayVos1);
           
            
            //获取商户渠道（微信，支付宝，QQ ,京东 ，网银等）
            PayWayVo vo0 = new PayWayVo();
            vo0.setPayWay(2);
            vo0.setPayName("微信");
            payWayVos2.add(vo0);
            
            PayWayVo vo1 = new PayWayVo();
            vo1.setPayWay(1);
            vo1.setPayName("支付宝");
            payWayVos2.add(vo1);
            
            PayWayVo vo2 = new PayWayVo();
            vo2.setPayWay(7);
            vo2.setPayName("QQ钱包");
            payWayVos2.add(vo2);
            
            PayWayVo vo3 = new PayWayVo();
            vo3.setPayWay(10);
            vo3.setPayName("京东");
            payWayVos2.add(vo3);
            
            PayWayVo vo4 = new PayWayVo();
            vo4.setPayWay(3);
            vo4.setPayName("在线");
            payWayVos2.add(vo4);
            
            PayWayVo vo5 = new PayWayVo();
            vo5.setPayWay(13);
            vo5.setPayName("银联");
            payWayVos2.add(vo5);
            
            PayWayVo vo6 = new PayWayVo();
            vo6.setPayWay(4);
            vo6.setPayName("快捷");
            payWayVos2.add(vo6);
            
            PayWayVo vo7 = new PayWayVo();
            vo7.setPayWay(15);
            vo7.setPayName("微信附言");
            payWayVos2.add(vo7);
            
            payWayVos.add(payWayVos2);
            
            
        } catch (Exception e) {
            log.error("获取支付开关异常：", e);
        }
        return payWayVos;
    }
    
    @ResponseBody
    @RequestMapping(value = "/pay_way4", produces = "application/json; charset=utf-8")
    public List<PayWayVo> pay_way4() {

        List<PayWayVo> payWayVos = new ArrayList<>();
        try {
            List<MerchantPay> data = merchantPayService.findByPaySwitch(null, 1, null,null);
            for (MerchantPay mp : data) {
                PayWayVo vo = new PayWayVo();
                BeanUtils.copyProperties(vo, mp);
                payWayVos.add(vo);
            }
        } catch (Exception e) {
            log.error("获取支付开关异常：", e);
        }
        return payWayVos;
    }
    
    

    @RequestMapping("/centerPay")
    public ModelAndView centerPay(PayRequestVo vo, HttpServletResponse resp, HttpServletRequest req) {

        ModelAndView model = new ModelAndView("pay/success");
        MerchantPay merchantPay = merchantPayService.findById(vo.getPlatformId());
        Response message = new Response();
        try {
            message = sendHttpByAES(req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + merchantPay.getPayCenterUrl(), vo);

            if (null != message.getData() && !"".equals(message.getData())) {
                ReturnVo returnVo = (ReturnVo) message.getData();

                String type = returnVo.getType();
                if ("1".equals(type)) {
                    model.setViewName("pay/pay_redirect");
                } else if ("2".equals(type) || "3".equals(type)) {
                    model.setViewName("pay/pay_qrcode");
                } else if ("4".equals(type)) {
                    if (null != returnVo.getData() && !"".equals(returnVo.getData())) {
                        message.setDesc(returnVo.getData());
                    }
                }
                else if("5".equals(type)){
                	model.setViewName("pay/pay_shop_qrcode");
                }
                //摩宝快捷
                else if("6".equals(type)){
                	req.setAttribute("pays", returnVo.getParams());
                	model.setViewName("pay/quickPay_apply");
                }
                else if("8".equals(type)){
                	model.setViewName("pay/pay_redirect_get");
                }
                else if("9".equals(type)){
                	model.setViewName("pay/pay_iframe");
               }
            }
        } catch (Exception e) {
            message.setResponseEnum(ErrorCode.SC_10001);
            log.error("异常：", e);
        }
        model.addObject("message", message);
        return model;
    }

    /*** 支付宝附言存款 */
    @ResponseBody
    @RequestMapping(value = "/zfb_deposit", produces = "application/json; charset=utf-8")
    public Response zfb_deposit(HttpServletRequest req, @RequestParam(value = "requestData", required = false) String requestData) throws Exception {

        PayRequestVo payRequestVo = pasePayRequest(req, requestData);

        Assert.notEmpty(payRequestVo, payRequestVo.getLoginName());

        User user = userService.get(payRequestVo.getLoginName());
        PayFyVo vo = tradeService.getZfbBanInfo(payRequestVo.getLoginName(), false);

        return transfer(vo);
    }
    
    /*** 摩宝快捷返回页面 */
    @RequestMapping("/quickPay_confirm")
    public ModelAndView quickPay_confirm(PayRequestVo vo, HttpServletResponse resp, HttpServletRequest req) {
    	 ModelAndView model = new ModelAndView("pay/quickPay_confirm");
         return model;
    }
}
