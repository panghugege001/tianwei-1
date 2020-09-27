package com.nnti.pay.service.interfaces;

import com.nnti.common.exception.BusinessException;
import com.nnti.pay.controller.vo.PayWayVo;
import com.nnti.pay.model.vo.MerchantPay;

import java.util.List;

/**
 * Created by wander on 2017/1/25.
 */
public interface IMerchantPayService {

    Integer add(MerchantPay merchantPay);

    Integer update(MerchantPay merchantPay,String loginname) throws Exception;
    
    Integer controlSwitch(String payway,Integer paySwitch,String operator) throws Exception;

    Integer delete(Long id);

    MerchantPay findByMerNo(String merNo) throws BusinessException;

    Integer findByAmountCut(String platform) throws BusinessException;

    /**
     * 玩家当月的存款量
     */
    Double countCustAmountOnMonth(String loginName) throws Exception;

    MerchantPay findById(Long platformId);

    /*** 根据支付开关获取支付方式 */
    List<MerchantPay> findByPaySwitch(Integer paySwitch,Integer useable,Integer payWay,List<Integer> usetypes) throws Exception;

    List<MerchantPay> findByCondition(MerchantPay merchantPay,List<Integer> usetypes);

    MerchantPay get(Long id);

    MerchantPay findByPlatform(String platform) throws BusinessException;

    Double countCustAmountOnAll(String loginName) throws Exception;

	MerchantPay getcontrolSwitch(String payway);
}
