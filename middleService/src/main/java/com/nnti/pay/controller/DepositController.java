package com.nnti.pay.controller;

import com.nnti.common.constants.Constant;
import com.nnti.common.constants.DictionaryType;
import com.nnti.common.constants.ErrorCode;
import com.nnti.common.exception.BusinessException;
import com.nnti.common.extend.zookeeper.GenerateNodePath;
import com.nnti.common.extend.zookeeper.ZookeeperFactoryBean;
import com.nnti.common.model.vo.BankInfo;
import com.nnti.common.model.vo.User;
import com.nnti.common.security.DigestUtils;
import com.nnti.common.security.RSAWithSoftware;
import com.nnti.common.utils.*;
import com.nnti.pay.controller.vo.DinpayPayVo;
import com.nnti.pay.controller.vo.PayFyVo;
import com.nnti.pay.controller.vo.PayRequestVo;
import com.nnti.pay.controller.vo.ReturnVo;
import com.nnti.pay.controller.vo.XlbPayVo;
import com.nnti.pay.controller.vo.XlbResponseVo;
import com.nnti.pay.controller.vo.ZhfPayVo;
import com.nnti.pay.model.common.Response;
import com.nnti.pay.model.vo.DepositOrder;
import com.nnti.pay.model.vo.MerchantPay;
import com.nnti.pay.service.interfaces.IBasicService;
import com.nnti.pay.service.interfaces.IDepositOrderService;
import com.nnti.pay.service.interfaces.IMerchantPayService;
import com.nnti.pay.service.interfaces.ITradeService;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by Addis on 2017/1/19.
 * 秒存
 */
@Controller
@RequestMapping("/deposit")
public class DepositController extends BasePayController {

    @Autowired
    private IBasicService basicService;
    @Autowired
    private IDepositOrderService depositOrderService;
    @Autowired
    private ITradeService tradeService;

    /*** 秒存 */
    @ResponseBody
    @RequestMapping(value = "/deposit_mc", produces = "application/json; charset=utf-8")
    public Response deposit_mc(String mctype,String orderAmount,String loginName) throws Exception {

         Assert.notEmpty(mctype,orderAmount, loginName);
         loginName = loginName.trim();

         Assert.isTrue(NumericUtil.isInteger(orderAmount), "金额格式错误");

         User user = basicService.getUser(loginName);
         validationLoginName(loginName, user);

         validationAgent(user.getRole());
         
         //作废上笔订单
         DepositOrder depositOrder = null;

         List<DepositOrder> depositOrderlist =  depositOrderService.findByLoginName(loginName);
         if (depositOrderlist != null && depositOrderlist.size() > 0) {
        	 depositOrder = depositOrderlist.get(0);
        	 depositOrder.setUpdateTime(null);
        	 depositOrder.setStatus(2);
        	 depositOrder.setRemark("用户:"+loginName+"-作废存款订单");
        	 
        	 depositOrderService.discardOrder(depositOrder);
             log.info(depositOrder.getDepositId()+"--订单作废成功");
         }  
        
            
         //创建订单   
         PayFyVo vo = tradeService.createDeposit(user.getLoginName(),mctype, Double.parseDouble(orderAmount));

         return transfer(vo);
    }
    
    
    
}
