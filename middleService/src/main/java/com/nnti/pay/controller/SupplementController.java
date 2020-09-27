package com.nnti.pay.controller;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.nnti.common.constants.DictionaryType;
import com.nnti.common.extend.zookeeper.GenerateNodePath;
import com.nnti.common.extend.zookeeper.ZookeeperFactoryBean;
import com.nnti.common.model.vo.Dictionary;
import com.nnti.common.service.interfaces.IDictionaryService;
import com.nnti.common.utils.Assert;
import com.nnti.common.utils.ConfigUtil;
import com.nnti.common.utils.MyUtils;
import com.nnti.common.utils.MyWebUtils;
import com.nnti.common.utils.NumericUtil;
import com.nnti.pay.model.vo.MerchantPay;
import com.nnti.pay.model.vo.PayOrder;
import com.nnti.pay.service.interfaces.IMerchantPayService;
import com.nnti.pay.service.interfaces.IPayOrderService;
import com.nnti.pay.service.interfaces.ITradeService;

/**
 * Created by wander on 2017/3/17.
 * 补单 接口
 */
@Controller
@RequestMapping("/supplement")
public class SupplementController extends PayController {

    @Autowired
    private IPayOrderService payOrderService;
    @Autowired
    private IMerchantPayService merchantPayService;
    @Autowired
    private ITradeService tradeService;
    @Autowired
    private IDictionaryService dictionaryService;

    @RequestMapping("/index")
    public ModelAndView supplement(HttpServletRequest req) throws Exception {
        ModelAndView model = new ModelAndView("pay/supplement");
        List<MerchantPay> pays = merchantPayService.findByCondition(new MerchantPay(null, 1,null), null);
        List<Dictionary> dictionaries = dictionaryService.findByCondition(new Dictionary(DictionaryType.POINT_CARD_TYPE.getName(), 1));
        model.addObject("pays", pays);
        model.addObject("dictionaries", dictionaries);
        return model;
    }

    @ResponseBody
    @RequestMapping("/one")
    public String one(HttpServletRequest req) {
        try {

            log.info("接收参数：" + MyWebUtils.getRequestParameters(req));

            String orderid = req.getParameter("orderid"); //订单号
            String money = req.getParameter("money"); //订单金额
            String loginname = req.getParameter("loginname"); //用户名
            String remark = req.getParameter("remark"); //备注
            String flag = req.getParameter("flag"); //补单来源 1 自动补单  默认 0

            Assert.isTrue(NumericUtil.isMoney(money), "金额格式错误");

            Assert.notEmpty(orderid, money);

            PayOrder payOrder = payOrderService.get(orderid);
            Assert.notEmpty(payOrder, payOrder.getPayPlatform());

            String payPlatform = payOrder.getPayPlatform();
            
            String loginnames = loginname;
            
            if (loginname.contains("wap_")) {
            	loginname = loginname.replaceAll("wap_", "");
            }

            

            if (!MyUtils.isNotEmpty(loginname)) {
                loginname = payOrder.getLoginName();
            }

            Boolean lockFlag = false;
            final InterProcessMutex lock = new InterProcessMutex(ZookeeperFactoryBean.zkClient, GenerateNodePath.generateUserLockForUpdate(loginname));
            try {
                lockFlag = lock.acquire(Integer.parseInt(ConfigUtil.getValue("zk.lock.timeout")), TimeUnit.SECONDS);
            } catch (Exception e) {
                log.error("玩家：" + loginname + "获取锁发生异常，异常信息：", e);
                lockFlag = true;
            }
            try {
                if (lockFlag) {

                    MerchantPay mp = merchantPayService.findByPlatform(payPlatform);
                    Assert.notEmpty(mp);
                    Assert.isTrue(mp.getUseable() == 1, "通道已禁用");

                    if (mp.getPayWay() == 5) {
                        String cardCode;
                        if (payPlatform.contains("zf")) {
                            //智付点卡
                             cardCode = StringUtils.substringBefore(orderid.split("_")[1],"4");
                        } else {
                            //新贝点卡
                            cardCode = orderid.split("_")[1];
                            cardCode = cardCode.substring(0, 6);
                        }
                        tradeService.doPayPointCardFlow(orderid, Double.valueOf(money), loginnames, cardCode, mp,remark,flag);
                    } else {
                        tradeService.doPayFlow(orderid, money, loginnames, mp, remark,flag);
                    }
                    return success;
                } else {
                    log.error("玩家：" + loginname + "未能获取锁，无法执行请求对应的方法....");
                }
            } catch (Exception e) {
                log.error("异常：" + e);
            } finally {
                try {
                    lock.release();
                } catch (Exception e) {
                    log.error("玩家：" + loginname + "释放锁发生异常, 异常信息：" + e);
                }
            }

        } catch (Exception e) {
            log.error("异常：", e);
        }
        return "failed";
    }
    
    public static void main(String[] args) {
    	 String payPlatform = "xbdk";
    	 String orderid = "qyamayuqi_1000054000018";
    	 try {
    		 final InterProcessMutex lock = new InterProcessMutex(ZookeeperFactoryBean.zkClient, GenerateNodePath.generateUserLockForUpdate("a19900822"));
    	        
		} catch (Exception e) {
			e.printStackTrace();
		}
    	   	
    	 if (payPlatform.contains("xb")) {
             //智付点卡
             String cardCode = StringUtils.substringBefore(orderid.split("_")[1],"4");
         } else {
             //新贝点卡
             String cardCode = orderid.split("_")[1];
             cardCode = cardCode.substring(0, 6);
         }
	}
}
