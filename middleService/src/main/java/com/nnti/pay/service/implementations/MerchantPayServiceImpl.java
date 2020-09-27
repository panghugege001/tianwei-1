package com.nnti.pay.service.implementations;

import com.nnti.common.exception.BusinessException;
import com.nnti.common.model.vo.BankInfo;
import com.nnti.common.model.vo.OperationLog;
import com.nnti.common.model.vo.Proposal;
import com.nnti.common.service.interfaces.IOperationLogService;
import com.nnti.common.service.interfaces.IProposalService;
import com.nnti.common.utils.Assert;
import com.nnti.common.utils.DateUtil;
import com.nnti.common.utils.MyUtils;
import com.nnti.common.utils.NumericUtil;
import com.nnti.pay.controller.vo.PayWayVo;
import com.nnti.pay.dao.master.IMasterMerchantPayDao;
import com.nnti.pay.model.vo.MerchantPay;
import com.nnti.pay.model.vo.PayOrder;
import com.nnti.pay.service.interfaces.IMerchantPayService;
import com.nnti.pay.service.interfaces.IPayOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by wander on 2017/1/25.
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class MerchantPayServiceImpl implements IMerchantPayService {

    @Autowired
    private IMasterMerchantPayDao masterMerchantPayDao;
    @Autowired
    private IPayOrderService payOrderService;
    @Autowired
    private IProposalService proposalService;
    @Autowired
    private IOperationLogService operationLogService;

    @CacheEvict(value = "merchantpay", allEntries = true)
    @Override
    public Integer add(MerchantPay merchantPay) {
        return masterMerchantPayDao.insert(merchantPay);
    }

    @CacheEvict(value = "merchantpay", allEntries = true)
    @Override
    public Integer update(MerchantPay merchantPay, String loginname) throws Exception {
        MerchantPay logM = get(merchantPay.getId());
        merchantPay.setMerchantCode(logM.getMerchantCode());
        merchantPay.setUpdateBy(loginname);
        merchantPay.setUpdateTime(new Date());
        operationLogService.insert(new OperationLog(loginname, "UPDATE", DateUtil.getCurrentTimestamp(), merchantPay.toString()));
        Integer ret = masterMerchantPayDao.update(merchantPay);
        return ret;
    }
    
    
    @CacheEvict(value = "merchantpay", allEntries = true)
    @Override
    public Integer controlSwitch(String payway,Integer paySwitch,String operator) throws Exception {
        MerchantPay merchantPay = getcontrolSwitch(payway);
        merchantPay.setPaySwitch(paySwitch);
        operationLogService.insert(new OperationLog(operator, "UPDATE", DateUtil.getCurrentTimestamp(), "通联客户端控制开关"));
        Integer ret = masterMerchantPayDao.update(merchantPay);
        return ret;
    }

    @CacheEvict(value = "merchantpay", allEntries = true)
    @Override
    public Integer delete(Long id) {
        return masterMerchantPayDao.delete(id);
    }

    @Cacheable(value = "merchantpay", key = "'merchantpay_'+#merNo")
    @Override
    public MerchantPay findByMerNo(String merNo) throws BusinessException {
        Assert.notEmpty(merNo);
        MerchantPay merchantPay = new MerchantPay();
        merchantPay.setMerchantCode(merNo);
        return getMerchantPay(merchantPay);
    }

    @Override
    public Integer findByAmountCut(String platform) throws BusinessException {
        Assert.notEmpty(platform);
        MerchantPay merchantPay = new MerchantPay();
        merchantPay.setPayPlatform(platform);
        return merchantPay.getAmountCut();
    }

    @Override
    public Double countCustAmountOnMonth(String loginName) throws Exception {
        Assert.notEmpty(loginName);
        Double amount = 0.0D, money = 0.0D;
        Date thisMonth = DateUtil.getMonthByFirstDay(new Date());
        PayOrder payOrder = new PayOrder();
        payOrder.setLoginName(loginName);
        payOrder.setType(0);
        payOrder.setCreateTime(thisMonth);
        money = payOrderService.sumMoney(payOrder);
        money = (money != null ? money : 0.0);

        Proposal proposal = new Proposal();
        proposal.setLoginName(loginName);
        proposal.setType(502);
        proposal.setFlag(2);
        proposal.setCreateTime(thisMonth);
        amount = proposalService.sumAmount(proposal);
        amount = (amount != null ? amount : 0.0);

        return NumericUtil.add(amount, money);
    }

    @Cacheable(value = "merchantpay", key = "'merchantpay_'+#platformId")
    @Override
    public MerchantPay findById(Long platformId) {
        return masterMerchantPayDao.get(platformId);
    }

    @Cacheable(value = "merchantpay", key = "'merchantpay_paySwitch_' + #paySwitch + '_' + #useable+'_' + #usetypes + '_' + #payWay+'_' + #payWay")
    @Override
    public List<MerchantPay> findByPaySwitch(Integer paySwitch, Integer useable,Integer payWay, List<Integer> usetypes) throws Exception {
        List<PayWayVo> payWayVos = new ArrayList<PayWayVo>();
        
        List<MerchantPay> merchantPays = findByCondition(new MerchantPay(paySwitch, useable,payWay), usetypes);
        return merchantPays;
    }

    @Cacheable(value = "merchantpay",
            key = "'merchantpay_'+#mp.merchantCode+'_'+#mp.payName+'_'+#mp.paySwitch+'_'+#mp.payPlatform+'_'+#mp.payWay+ '_' + #mp.useable")
    @Override
    public List<MerchantPay> findByCondition(MerchantPay mp, List<Integer> usetypes) {
        Map map = new HashMap();
        map.put("merchantCode", mp.getMerchantCode());
        map.put("payPlatform", mp.getPayPlatform());
        map.put("paySwitch", mp.getPaySwitch());
        map.put("useable", mp.getUseable());
        map.put("payWay", mp.getPayWay());
        map.put("payName", mp.getPayName());
        map.put("usetype", usetypes);
        
        return masterMerchantPayDao.findByCondition(map);
    }

    @Cacheable(value = "merchantpay", key = "'merchantpay_'+#id")
    @Override
    public MerchantPay get(Long id) {
        return masterMerchantPayDao.get(id);
    }
    
    @Override
    public MerchantPay getcontrolSwitch(String payway) {
        return masterMerchantPayDao.controlSwitch(payway);
    }
    

    @Cacheable(value = "merchantpay", key = "'merchantpay_'+#platform")
    @Override
    public MerchantPay findByPlatform(String platform) throws BusinessException {
        Assert.notEmpty(platform);
        return getMerchantPay(new MerchantPay(platform));
    }

    @Override
    public Double countCustAmountOnAll(String loginName) throws Exception {
        Assert.notEmpty(loginName);
        Double amount = 0.0D, money = 0.0D;
        PayOrder payOrder = new PayOrder();
        payOrder.setLoginName(loginName);
        payOrder.setType(0);
        money = payOrderService.sumMoney(payOrder);
        money = (money != null ? money : 0.0);

        Proposal proposal = new Proposal();
        proposal.setLoginName(loginName);
        proposal.setType(502);
        proposal.setFlag(2);
        amount = proposalService.sumAmount(proposal);
        amount = (amount != null ? amount : 0.0);

        return NumericUtil.add(amount, money);
    }

    private MerchantPay getMerchantPay(MerchantPay merchantPay) {
        if (MyUtils.isNotEmpty(merchantPay)) {
            List<MerchantPay> merchantPays = findByCondition(merchantPay, null);
            if (MyUtils.isNotEmpty(merchantPays)) {
                return merchantPays.get(0);
            }
        }
        return new MerchantPay();
    }
}
