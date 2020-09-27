package com.nnti.pay.service.implementations;

import com.nnti.common.constants.CreditType;
import com.nnti.common.constants.ErrorCode;
import com.nnti.common.constants.UserRole;
import com.nnti.common.exception.BusinessException;
import com.nnti.common.model.vo.*;
import com.nnti.common.service.interfaces.*;
import com.nnti.common.utils.Assert;
import com.nnti.common.utils.DateUtil;
import com.nnti.common.utils.MyUtils;
import com.nnti.common.utils.NumericUtil;
import com.nnti.pay.controller.vo.PayFyVo;
import com.nnti.pay.model.vo.*;
import com.nnti.pay.service.interfaces.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by wander on 2017/1/20.
 * 交易相关业务
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TradeServiceImpl implements ITradeService {

    private final static Logger log = Logger.getLogger(TradeServiceImpl.class);
    @Autowired
    private ICreditLogService creditLogService;
    @Autowired
    private ICreditLogExtendService creditLogExtendService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IPayOrderBillNoService payOrderBillNoService;
    @Autowired
    private IBankInfoService bankInfoService;
    @Autowired
    private IBankCreditlogsService bankCreditlogsService;
    @Autowired
    private IProposalService proposalService;
    @Autowired
    private IAlipayAccountService alipayAccountService;
    @Autowired
    private IDepositOrderService depositOrderService;
    @Autowired
    private IPayOrderService payOrderService;
    @Autowired
    private IDictionaryService dictionaryService;
    @Autowired
    private IPayCreditService payCrieditService;

    @Override
    public void updateCredit(User user, Double remit, String creditType, String referenceNo, String remark) throws Exception {
        Assert.notEmpty(user, remit, referenceNo);

        if (remit == 0.0) {//有点多余
            log.info("额度变化为0，不操作..");
            return;
        }
        // 代理的额度允许为负
        if (UserRole.MONEY_CUSTOMER.getCode().equals(user.getRole()) &&
                (!NumericUtil.isGtZero(remit) && !NumericUtil.isGtZero(NumericUtil.add(user.getCredit(), remit)))) {
            throw new BusinessException(ErrorCode.SC_30000_109.getCode(), String.format(ErrorCode.SC_30000_109.getType(), user.getCredit()));
        } else {
            if (MyUtils.isNotEmpty(referenceNo)) {
                if (creditLogExtendService.count(new CreditLogExtend(referenceNo)) > 0) {
                    throw new BusinessException(ErrorCode.SC_30000_110.getCode(), String.format(ErrorCode.SC_30000_110.getType(), referenceNo));
                }
            }
            Double credit = user.getCredit();
            /*** 更新用户额度 */
            User users = new User();
            users.setLoginName(user.getLoginName());
            users.setCredit(remit);
            userService.update(users);

            /*** 记录交易日志 */
            CreditLog creditLog = new CreditLog();
            creditLog.setLoginName(user.getLoginName());
            creditLog.setType(creditType);
            creditLog.setCredit(credit);
            creditLog.setRemit(remit);
            creditLog.setCreateTime(DateUtil.getCurrentDate());
            creditLog.setNewCredit(NumericUtil.add(credit, remit));
            creditLog.setRemark(referenceNo + ";" + remark);
            creditLogService.create(creditLog);

            /*** 添加扩展表 */
            CreditLogExtend creditLogExtend = new CreditLogExtend();
            creditLogExtend.setId(creditLog.getId());
            creditLogExtend.setOrderId(referenceNo);
            creditLogExtend.setCreateTime(DateUtil.getCurrentDate());
            creditLogExtendService.create(creditLogExtend);
        }
    }

    @Override
    public void doPayFlow(String orderId, String amount, String loginName, MerchantPay mp, String remark,String flag) throws Exception {
        Assert.notEmpty(orderId, amount, loginName, mp, mp.getFee(), mp.getPcFee(), mp.getPhoneFee(), mp.getType());

        Double orderAmount = Double.valueOf(amount);

        log.info("交易开始：" + MyUtils.join("=", ",", "orderId", orderId, "orderAmount", amount, "loginName", loginName));

        Double payFee = 0.0D;
        String phoneMsg = "";
        PayOrder payOrder = payOrderService.get(orderId);
        Assert.notEmpty(payOrder);

        //手机端过来的，费率不一样，做下区分
        if (loginName.startsWith("wap_")) {
            payFee = mp.getPhoneFee();
            loginName = loginName.replace("wap_", "");
            phoneMsg = "wap";
        } else {
            payFee = mp.getPcFee();
        }
        
        if(mp.getPayPlatform().equals("ffzfb")){
        	payOrder.setMoney(orderAmount);
        }

        loginName = payOrder.getLoginName();
        User user = userService.get(loginName);
        Assert.notEmpty(user);

        if ((1 == payOrder.getType() || payOrder.getType() == 2) && payOrder.getMoney().equals(orderAmount)) {

            String msg = phoneMsg + mp.getPayName() + "单号:" + orderId;
            if (!loginName.equals(payOrder.getLoginName())) {
                throw new BusinessException(ErrorCode.SC_30000_108.getCode(), String.format(ErrorCode.SC_30000_108.getType(),
                        mp.getPayName(), payOrder.getLoginName(), loginName));
            }

            /*** 更新订单*/
            PayOrder new_payOrder = new PayOrder();
            new_payOrder.setBillNo(payOrder.getBillNo());
            new_payOrder.setMsg(msg);
            new_payOrder.setType(0);
            new_payOrder.setReturnTime(DateUtil.getCurrentDate());
            if("1".equals(flag)){
            	new_payOrder.setIp("127.0.0.1");
            }
            if (MyUtils.isNotEmpty(remark)) {
                new_payOrder.setMsg(remark);
            }
            payOrderService.update(new_payOrder);

            PayCredit payCredit = new PayCredit();
            /*** 更新用户 不是首次存款 */
            if (user.getIsCashin() == null || (user.getIsCashin() != null && user.getIsCashin() != 0)) {
                userService.update(new User(loginName, 0));
                payCredit.setFirstDeposit(1);//首存
            }

            /*** 记录额度记录 */
            Double userAmount = NumericUtil.sub(orderAmount, NumericUtil.div(NumericUtil.mul(orderAmount, mp.getFee()), 100));
            updateCredit(user, userAmount, CreditType.NETPAY.getCode(), orderId, msg);
            /*** 加入银行*/
            Double bankAmount = NumericUtil.sub(orderAmount, NumericUtil.div(NumericUtil.mul(orderAmount, payFee), 100));
            bankCreditlogsService.updateAmount(mp.getId(), bankAmount, orderId);

            /*** 加入支付账单*/
            PayOrderBillNo pb = new PayOrderBillNo();
            pb.setBillNo(orderId);
            pb.setLoginName(loginName);
            pb.setPayPlatform(mp.getPayPlatform());
            pb.setMoney(orderAmount);
            pb.setRemark("插入时间：" + DateUtil.format(DateUtil.YYYY_MM_DD_HH_MM_SS, new Date()));
            payOrderBillNoService.insert(pb);

            /*** 记录支付流水 */
            payCredit.setPayOrderNo(orderId);
            payCredit.setLoginname(user.getLoginName());
            payCredit.setAgentName(user.getAgent());
            payCredit.setIntro(user.getIntro());
            payCredit.setPartner(user.getPartner());
            payCredit.setUserCreateTime(DateUtil.getCurrentTimestamp());
            payCredit.setPayPlatform(mp.getPayPlatform());
            payCredit.setPayWay(mp.getPayWay());
            payCredit.setDeposit(orderAmount);
            payCredit.setBankFee(payFee);
            payCredit.setBankGet(bankAmount);
            payCredit.setUserFee(mp.getFee());
            payCredit.setUserGet(userAmount);
            payCredit.setCreateTime(DateUtil.getCurrentDate());
            payCrieditService.create(payCredit);

            log.info("交易成功结束：BillNo：" + payOrder.getBillNo());
        } else {
            log.info("此笔交易已经支付成功:" + payOrder.getBillNo());
        }
    }

    @Override
    public PayFyVo getZfbBanInfo(String loginName, Boolean isImgCode) throws Exception {
        PayFyVo vo = new PayFyVo();

        BankInfo zfbBank = new BankInfo();
        User user = userService.get(loginName);
        Assert.notEmpty(user);

        BankInfo zfb = new BankInfo();

        if (isImgCode) {
            zfb.setZfbImgCode("is not null");
        }
        zfb.setScanAccount(1);
        if (user.getLevel() <= 1) {
            Proposal proposal = new Proposal();
            proposal.setLoginName(loginName);
            proposal.setFlag(2);
            proposal.setType(502);
            Double amount = proposalService.sumAmount(proposal);

            PayOrder payOrder = new PayOrder();
            payOrder.setLoginName(loginName);
            payOrder.setFlag(0);
            payOrder.setType(0);
            Double money = payOrderService.sumMoney(payOrder);

            Double total = NumericUtil.add(amount, money);
            String vpn = "";
            if (total < 1000) {
                vpn = "F";
            } else if (amount >= 1000 && amount < 5000) {
                vpn = "E";
            } else if (amount >= 5000 && amount < 10000) {
                vpn = "A";
            } else if (amount >= 10000 && amount < 50000) {
                vpn = "B";
            } else if (amount >= 50000 && amount < 100000) {
                vpn = "C";
            } else if (amount >= 100000) {
                vpn = "D";
            } else {
                throw new BusinessException(ErrorCode.SC_10001.getCode(), "支付宝附言未知错误");
            }
            zfb.setVpnName(vpn);
        }
        zfb.setType(1);
        zfb.setIsShow(1);
        zfb.setUseable(0);
        zfb.setBankName("支付宝");
        zfb.setUserRole("%" + user.getLevel() + "%");
        List<BankInfo> bankInfos = bankInfoService.findZfbBankInfo(zfb);
        if (bankInfos != null && bankInfos.size() > 0) {
            zfbBank = bankInfos.get(0);
        }

        if (!MyUtils.isNotEmpty(zfbBank)) {
            throw new BusinessException(ErrorCode.SC_10001.getCode(), "您的活跃度没有达到,暂时不能使用支付宝存款方式,请您联系在线客服");
        }
        if (isImgCode) {
            AlipayAccount aa_new = new AlipayAccount();
            aa_new.setLoginName(loginName);
            aa_new.setDisable(0);
            AlipayAccount alipayAccount = alipayAccountService.findLoginNameAndDisable(aa_new);
            if (MyUtils.isNotEmpty(alipayAccount)) {
                vo.setZfbImgCode(null);
            }
            return vo;
        } else {
            String msg = depositOrderService.createDepositId(loginName, zfbBank);
            vo.setAccountNo(zfbBank.getAccountNo());
            vo.setVpnPassword(msg);
            return vo;
        }
    }
    
    @Override
    public PayFyVo createDeposit(String loginName,String mctype, Double money) throws Exception {
    	 PayFyVo vo = new PayFyVo();

         BankInfo bankinfo = new BankInfo();
         User user = userService.get(loginName);
         Assert.notEmpty(user);
         
         String bankName = Assert.getBankName(mctype);
         
         BankInfo tlbank = null;

         bankinfo.setType(1);
         bankinfo.setIsShow(1);
         bankinfo.setUseable(0);
         bankinfo.setBankName(bankName);
         bankinfo.setUserRole("%" + Assert.levels[user.getLevel()] + "%");
         List<BankInfo> bankInfos = bankInfoService.findDepositBankInfo(bankinfo);
         if (bankInfos != null && bankInfos.size() > 0) {
        	 tlbank = bankInfos.get(0);
         }

         if (!MyUtils.isNotEmpty(tlbank)) {
             throw new BusinessException(ErrorCode.SC_10001.getCode(), "该通道已关闭");
         }
         else {
        	 String msg = depositOrderService.createDepositIdMc(loginName,money, tlbank,6);
             vo.setZfbImgCode(tlbank.getZfbImgCode());
             vo.setVpnPassword(msg);
             return vo; 
         }
        
    }
    
    
    
    

    @Override
    public void doPayPointCardFlow(String trade_no, Double money, String loginName, String card_code, MerchantPay mp,String remark,String flag) throws Exception {
        String returnmsg = null;

        log.info("进入充值卡充值..参数：" + "trade_no:" + trade_no + ",money:" + money + ",loginName:" + loginName + ",card_code:" + card_code);

        Assert.notEmpty(trade_no, money, loginName);

        PayOrder payOrder = payOrderService.get(trade_no);

        Double money_old = money;

        if (1 == payOrder.getType() || payOrder.getType() == 2) {
            if (loginName.contains("wap_")) {
                loginName = loginName.replaceAll("wap_", "");
            }

            loginName = payOrder.getLoginName();
            User user = userService.get(loginName);
            Assert.notEmpty(user);

            Double percent = 0D;
            if (card_code != null) {

                String type = mp.getPayCenterUrl().replaceAll("/", "") + "_type";

                Dictionary dict = dictionaryService.findByOne(type, card_code, 1);

                if (MyUtils.isNotEmpty(dict)) {
                    percent = Double.valueOf(dict.getDictValue());
                }
            } else {
                Assert.isTrue(payOrder.getMoney() >= money, "金额不能大于面值金额");
            }

            /*** 更新订单*/
            PayOrder new_payOrder = new PayOrder();
            money = NumericUtil.mul(NumericUtil.sub(1D, percent), money);
            new_payOrder.setMoney(money);
            if("".equals(remark) || remark == null){
            	new_payOrder.setMsg(mp.getPayPlatform() + ",点卡：" + card_code + "点卡单号:" + trade_no);
            }
            else{
            	new_payOrder.setMsg(remark);
            }
            new_payOrder.setBillNo(trade_no);
            new_payOrder.setType(0);
            new_payOrder.setReturnTime(DateUtil.getCurrentDate());
            if("1".equals(flag)){
            	new_payOrder.setIp("127.0.0.1");
            }
            payOrderService.update(new_payOrder);

            PayCredit payCredit = new PayCredit();
            /*** 更新用户 不是首次存款 */
            if (user.getIsCashin() == null || (user.getIsCashin() != null && user.getIsCashin() != 0)) {
                userService.update(new User(loginName, 0));
                payCredit.setFirstDeposit(1);//首存
            }

            updateCredit(user, money, CreditType.NETPAY.getCode(), trade_no, mp.getPayPlatform() + mp.getMerchantCode());
            /*** 加入银行*/
            bankCreditlogsService.updateAmount(mp.getId(), money, trade_no);

            /*** 记录支付流水 */
            payCredit.setPayOrderNo(trade_no);
            payCredit.setLoginname(user.getLoginName());
            payCredit.setAgentName(user.getAgent());
            payCredit.setIntro(user.getIntro());
            payCredit.setPartner(user.getPartner());
            payCredit.setUserCreateTime(DateUtil.getCurrentTimestamp());
            payCredit.setPayPlatform(mp.getPayPlatform());
            payCredit.setPayWay(mp.getPayWay());
            payCredit.setDeposit(money_old);
            payCredit.setBankFee(percent);
            payCredit.setBankGet(money);
            payCredit.setUserFee(percent);
            payCredit.setUserGet(money);
            payCredit.setCreateTime(DateUtil.getCurrentDate());
            payCrieditService.create(payCredit);

            log.info("交易成功结束：BillNo：" + payOrder.getBillNo());
        } else {
            log.info("此笔交易已经支付成功:" + payOrder.getBillNo());
        }
    }
}
