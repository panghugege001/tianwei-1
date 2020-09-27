package com.nnti.pay.service.implementations;

import com.nnti.common.constants.Constant;
import com.nnti.common.model.vo.User;
import com.nnti.common.model.vo.UserStatus;
import com.nnti.common.service.interfaces.IUserService;
import com.nnti.common.service.interfaces.IUserStatusService;
import com.nnti.common.utils.Assert;
import com.nnti.common.utils.DateUtil;
import com.nnti.common.utils.MyUtils;
import com.nnti.pay.model.enums.PayType;
import com.nnti.pay.model.vo.MerchantPay;
import com.nnti.pay.model.vo.PayOrder;
import com.nnti.pay.model.vo.TotalDeposit;
import com.nnti.pay.service.interfaces.IBasicService;
import com.nnti.pay.service.interfaces.IMerchantPayService;
import com.nnti.pay.service.interfaces.IPayOrderService;
import com.nnti.pay.service.interfaces.ITotalDepositService;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class BasicServiceImpl implements IBasicService {

    private Logger log = Logger.getLogger(BasicServiceImpl.class);

    private static final Long BASE_ORDER_NO = 4000000L;
    @Autowired
    private IUserStatusService userStatusService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IPayOrderService payOrderService;
    @Autowired
    private ITotalDepositService totalDepositService;
    @Autowired
    private IMerchantPayService merchantPayService;

    @Override
    public String createOrderNo(String prefix, String loginName) throws Exception {
        Assert.notEmpty(prefix, loginName);

        UserStatus userStatus = userStatusService.get(loginName);
        UserStatus userStatus_new = new UserStatus();
        userStatus_new.setLoginName(loginName);

        if (MyUtils.isNotEmpty(userStatus)) {
            String payOrderValue = userStatus.getPayOrderValue();
            Long value = Long.valueOf(payOrderValue);
            if (MyUtils.isNotEmpty(userStatus.getPayOrderValue()) &&
                    Long.valueOf(userStatus.getPayOrderValue()) > 0) {
                userStatus_new.setPayOrderValue((value + 1) + "");
            } else {
                userStatus_new.setPayOrderValue(BASE_ORDER_NO + "");
            }
            userStatusService.update(userStatus_new);
        } else {
            userStatus_new.setPayOrderValue(BASE_ORDER_NO.toString());
            userStatusService.create(userStatus_new);
        }
        return prefix + userStatus_new.getPayOrderValue();
    }

    @Override
    public String createOrderNoBySeq(String prefix, String loginName) {
        return null;
    }

    @Override
    public User getUser(String loginName) throws Exception {
        Assert.notEmpty(loginName);
        return userService.get(loginName);
    }

    @Override
    public String createBillNo(String loginName, String orderAmount, MerchantPay mp, String prefix, String per) throws Exception {

        Assert.notEmpty(loginName, orderAmount, mp);
        User user = userService.get(loginName);
        Assert.notEmpty(user);

        String platform = mp.getPayPlatform();

        /*** 100 表示前缀为空 商户平台不在订单号上出现 */
        if ("100".equals(per)) {
            per = platform = "";
        }

        String payOrderNo = createOrderNo(prefix + per, loginName);

        String system_code = mp.getSystemCode();

        PayOrder payOrder = new PayOrder();
        payOrder.setBillNo(system_code + per + platform + per + payOrderNo);
        /*** 200 表示商戶订单不能超过20，且没有备注字段 */
        if ("200".equals(per)) {
        	String billno = mp.getId()+"_"+platform+"_"+payOrderNo.substring(payOrderNo.length()-7);
        	payOrder.setBillNo(billno);
        }
        /*** 300 表示商戶订单不能超过20*/
        if ("300".equals(per)) {
        	String billno = loginName+"_"+payOrderNo.substring(payOrderNo.length()-7);
        	payOrder.setBillNo(billno);
        }
        payOrder.setPayPlatform(mp.getPayPlatform());
        payOrder.setFlag(0);
        if(mp.getPayWay() ==15 || mp.getPayWay() ==16){//15微信附言16支付宝附言
        	String fyzh = mp.getMerchantCode().split("\\.")[0]; 
        	payOrder.setBillNo(fyzh+ per +payOrderNo);
        	payOrder.setType(PayType.Init.getCode());
        	payOrder.setMsg(mp.getPayPlatform() + "待处理订单号:" + payOrderNo);
        }else{
        	payOrder.setType(PayType.WESUCESS.getCode());
        	payOrder.setMsg(mp.getPayPlatform() + "未支付订单号:" + payOrderNo);
        }
        payOrder.setNewAccount(Constant.FLAG_FALSE);
        payOrder.setLoginName(loginName);
        payOrder.setAliasName(user.getAccountName());
        payOrder.setMoney(Double.valueOf(orderAmount));
        payOrder.setPhone(user.getPhone());
        payOrder.setEmail(user.getEmail());
        payOrder.setCreateTime(DateUtil.getCurrentDate());
        payOrder.setMerchants(mp.getPayWay());
        payOrderService.insert(payOrder);

        log.info("创建订单：" + payOrder.getBillNo());
        return payOrder.getBillNo();
    }
    
    
    /***********
     * E宝获取订单号
     * @param loginName
     * @param orderAmount
     * @param mp
     * @param prefix
     * @param per
     * @return
     * @throws Exception
     */
    @Override
    public String createEbaoBillNo(String loginName, String orderAmount, MerchantPay mp, String prefix, String per,String merNo) throws Exception {

        Assert.notEmpty(loginName, orderAmount, mp);
        User user = userService.get(loginName);
        Assert.notEmpty(user);

        String platform = mp.getPayPlatform();

        /*** 100 表示前缀为空 商户平台不在订单号上出现 */
        if ("100".equals(per)) {
            per = platform = "";
        }

        String payOrderNo = createOrderNo(prefix + per, loginName);

        String system_code = mp.getSystemCode();

        PayOrder payOrder = new PayOrder();
        payOrder.setBillNo(system_code + per + platform + per + payOrderNo+merNo);
        payOrder.setPayPlatform(mp.getPayPlatform());
        payOrder.setFlag(0);
        payOrder.setType(PayType.WESUCESS.getCode());
        payOrder.setMsg(mp.getPayPlatform() + "未支付订单号:" + payOrderNo);
        payOrder.setNewAccount(Constant.FLAG_FALSE);
        payOrder.setLoginName(loginName);
        payOrder.setAliasName(user.getAccountName());
        payOrder.setMoney(Double.valueOf(orderAmount));
        payOrder.setPhone(user.getPhone());
        payOrder.setEmail(user.getEmail());
        payOrder.setCreateTime(DateUtil.getCurrentDate());
        payOrder.setMerchants(mp.getPayWay());
        payOrderService.insert(payOrder);

        log.info("创建订单：" + payOrder.getBillNo());
        return payOrder.getBillNo();
    }
    
    

    @Override
    public String generateTransferNo(int type) {

        return type + DateUtil.format(DateUtil.YYMMDD, new Date()) + RandomStringUtils.randomAlphanumeric(10).toLowerCase();
    }

    @Override
    public TotalDeposit checkAndUpdateDeposit(String loginname) throws Exception {
        TotalDeposit total;
        Assert.notEmpty(loginname);
        List<TotalDeposit> totals = totalDepositService.findByLoginName(loginname);

        if (MyUtils.isNotEmpty(totals) && totals.size() > 0) {
            total = totals.get(0);

            if (total.getUpdateTime() == null) {
                total.setUpdateTime(DateUtil.getCurrentDate());
                totalDepositService.update(total);
            }
            /*** 三天更新一次*/
            if (total.getUpdateTime().getTime() <= DateUtil.getDateByDateNumber(-2).getTime()) {
                Double custAmountOnAll = merchantPayService.countCustAmountOnAll(loginname);
                total.setAllDeposit(custAmountOnAll);
                total.setUpdateTime(DateUtil.getCurrentDate());
                totalDepositService.update(total);
            }
        } else {
            Double custAmountOnAll = merchantPayService.countCustAmountOnAll(loginname);
            TotalDeposit new_total = new TotalDeposit();
            new_total.setLoginname(loginname);
            new_total.setAllDeposit(custAmountOnAll);
            new_total.setCreateTime(DateUtil.getCurrentDate());
            new_total.setUpdateTime(DateUtil.getCurrentDate());
            totalDepositService.insert(new_total);
            total = new_total;
        }
        return total;
    }

}
