package com.nnti.pay.controller;

import com.nnti.common.model.vo.BankCardinfo;
import com.nnti.common.model.vo.BankInfo;
import com.nnti.common.model.vo.Dictionary;
import com.nnti.common.service.interfaces.IBankInfoService;
import com.nnti.common.utils.Assert;
import com.nnti.pay.controller.vo.BankVo;
import com.nnti.pay.controller.vo.IntransferVo;
import com.nnti.pay.controller.vo.TreeVo;
import com.nnti.pay.model.common.Response;
import com.nnti.pay.model.vo.MerchantPay;
import com.nnti.pay.service.interfaces.IIntransferService;
import com.nnti.pay.service.interfaces.IMerchantPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wander on 2017/3/8.      
 */
@Controller
@RequestMapping("/bank")
public class BankController extends BasePayController {

    @Autowired 
    private IBankInfoService bankInfoService;
    @Autowired
    private IIntransferService intransferService;
    @Autowired
    private IMerchantPayService merchantPayService;
    
    @RequestMapping("/intransfer")
    public ModelAndView intransfer(HttpServletRequest req) {
        ModelAndView model = new ModelAndView("bank/intransfer");

        String loginname = req.getParameter("loginname");

        if (loginname == null || loginname.equals("")) {
            model.addObject("请重新登陆");
            return model;
        }

        model.addObject("loginname", loginname);

        return model;
    }

    @ResponseBody
    @RequestMapping(value = "/intransfer_tree", produces = "application/json; charset=utf-8")
    public String intransfer_tree() throws Exception {
        List<TreeVo> treeVos = new ArrayList<>();

        Map map = new HashMap();
        map.put("useable", 0);
        List<BankInfo> bankInfos = bankInfoService.findBankInfoList2(map);
        TreeVo pvo1 = new TreeVo("1", "内部账号", false);
        TreeVo pvo2 = new TreeVo("2", "外部账户", false);

        List<TreeVo> ptreeVos1 = new ArrayList<>();//内部账户
        List<TreeVo> ptreeVos2 = new ArrayList<>();//外部账户

        List<TreeVo> treeVos1 = new ArrayList<>();
        List<TreeVo> treeVos2 = new ArrayList<>();       
        List<TreeVo> treeVos3 = new ArrayList<>();
        List<TreeVo> treeVos8 = new ArrayList<>();
        List<TreeVo> treeVos9 = new ArrayList<>();
        List<TreeVo> treeVos10 = new ArrayList<>();
        List<TreeVo> treeVos11 = new ArrayList<>();
        for (BankInfo bank : bankInfos) {
            if (bank.getType() == 1) {
                //存款账户
                treeVos1.add(new TreeVo("b_" + bank.getId(), bank.getRemark(), true));
            } else if (bank.getType() == 2) {
                //支付账户
                treeVos2.add(new TreeVo("b_" + bank.getId(), bank.getRemark(), true));
            } else if (bank.getType() == 3) {
                //存储账户
                treeVos3.add(new TreeVo("b_" + bank.getId(), bank.getRemark(), true));
            } else if (bank.getType() == 10) {
                //付款储备
            	treeVos10.add(new TreeVo("b_" + bank.getId(), bank.getRemark(), true));
            }else if (bank.getType() == 111) {
                //下发储备
            	treeVos11.add(new TreeVo("b_" + bank.getId(), bank.getRemark(), true));
            }else if (bank.getType() == 8) {
                //中转账户
                treeVos8.add(new TreeVo("b_" + bank.getId(), bank.getRemark(), true));
            } else if (bank.getBankType() == 1) {
                //外部账户
                treeVos9.add(new TreeVo("b_" + bank.getId(), bank.getRemark(), true));
            }
        }

        TreeVo vo1 = new TreeVo("3", "存款账户", false);
        vo1.setNodes(treeVos1);
        TreeVo vo2 = new TreeVo("4", "支付账户", false);
        vo2.setNodes(treeVos2);
        TreeVo vo3 = new TreeVo("5", "存储账户", false);
        vo3.setNodes(treeVos3);
        TreeVo vo8 = new TreeVo("6", "中转账户", false);
        vo8.setNodes(treeVos8);
        TreeVo vo10 = new TreeVo("13", "付款储备", false);
        vo10.setNodes(treeVos10);
        TreeVo vo18 = new TreeVo("14", "下发储备", false);
        vo18.setNodes(treeVos11);

        ptreeVos1.add(vo1);
        ptreeVos1.add(vo2);
        ptreeVos1.add(vo3);    
        ptreeVos1.add(vo8);
        ptreeVos1.add(vo10);
        ptreeVos1.add(vo18);

        TreeVo vo9 = new TreeVo("7", "外部账户", false);         
        vo9.setNodes(treeVos9);
        ptreeVos2.add(vo9);

        List<MerchantPay> merchantPays = merchantPayService.findByCondition(new MerchantPay(null, 1,null), null);

        List<TreeVo> mp_type1 = new ArrayList<>();
        List<TreeVo> mp_type2 = new ArrayList<>();
        List<TreeVo> mp_type3 = new ArrayList<>();
        List<TreeVo> mp_type4 = new ArrayList<>();
        List<TreeVo> mp_type5 = new ArrayList<>();
        List<TreeVo> mp_type7 = new ArrayList<>();
        List<TreeVo> mp_type10 = new ArrayList<>();
        for (MerchantPay mp : merchantPays) {
            if (mp.getPayWay() == 1) {
                mp_type1.add(new TreeVo("o_" + mp.getId(), mp.getPayName(), true));
            } else if (mp.getPayWay() == 2) {
                mp_type2.add(new TreeVo("o_" + mp.getId(), mp.getPayName(), true));
            } else if (mp.getPayWay() == 3) {
                mp_type3.add(new TreeVo("o_" + mp.getId(), mp.getPayName(), true));
            } else if (mp.getPayWay() == 4) {
                mp_type4.add(new TreeVo("o_" + mp.getId(), mp.getPayName(), true));
            } else if (mp.getPayWay() == 5) {
                mp_type5.add(new TreeVo("o_" + mp.getId(), mp.getPayName(), true));
            } else if (mp.getPayWay() == 7) {
                mp_type7.add(new TreeVo("o_" + mp.getId(), mp.getPayName(), true));
            } else if (mp.getPayWay() == 10) {
            	mp_type10.add(new TreeVo("o_" + mp.getId(), mp.getPayName(), true));
            }
        }
        TreeVo vo11 = new TreeVo("7", "支付宝账户", false);
        vo11.setNodes(mp_type1);
        TreeVo vo12 = new TreeVo("8", "微信账户", false);
        vo12.setNodes(mp_type2);
        TreeVo vo13 = new TreeVo("9", "网银账户", false);
        vo13.setNodes(mp_type3);
        TreeVo vo14 = new TreeVo("10", "快捷账户", false);
        vo14.setNodes(mp_type4);
        TreeVo vo15 = new TreeVo("11", "点卡账户", false);
        vo15.setNodes(mp_type5);
        TreeVo vo16 = new TreeVo("12", "QQ账号", false);
        vo16.setNodes(mp_type7);
        TreeVo vo17 = new TreeVo("13", "京东账户", false);
        vo17.setNodes(mp_type10);
        ptreeVos1.add(vo11);
        ptreeVos1.add(vo12);
        ptreeVos1.add(vo13);        
        ptreeVos1.add(vo14);
        ptreeVos1.add(vo15);
        ptreeVos1.add(vo16);
        ptreeVos1.add(vo17);
        
        pvo1.setNodes(ptreeVos1);
        treeVos.add(pvo1);

        pvo2.setNodes(ptreeVos2);
        treeVos.add(pvo2);

        return gson.toJson(treeVos);
    }

    @ResponseBody
    @RequestMapping(value = "/save", produces = "application/json; charset=utf-8")
    public String save(IntransferVo vo, HttpServletRequest req) {

        try {

            Assert.notEmpty(vo, vo.getOutput(), vo.getInput(), vo.getAmount(), vo.getFee(), vo.getNumber());

            if (vo.getNumber() <= 0)
                return "转出数量必须大于0笔";
            if (vo.getAmount() <= 0)
                return "转出金额必须大于0";
            if (vo.getFee() < 0)
                return "费率不能小于0";
            if(vo.getFee() > 200){
            	return "费率不能大于200";
            }

            intransferService.batchExcute(vo);

        } catch (Exception e) {
            log.error("BankController.save 异常：", e);
            return "后台异常，请联系技术人员";
        }
        return "已经交给后台处理.";
    }
     
    
    @ResponseBody
    @RequestMapping(value = "/getBankInfo", produces = "application/json; charset=utf-8", method = RequestMethod.POST)
    public BankCardinfo getBankInfo(String identifycode) throws Exception {
    	BankCardinfo bankCardinfo= null;
    	  Map map = new HashMap();     
    	try {
     	   for(int i=2 ;i <= 10; i++ ) { 
     		   map.put("identifylegth", i);
     		   map.put("identifycode", identifycode.substring(0, i));
     		   bankCardinfo = bankInfoService.findBankInfo(map);
     		   if(bankCardinfo == null ) {
     			   continue;
     		   }else {
     			 break;
     		   }
     	   }
     	   if(bankCardinfo == null) {
     		  return null; 
     	   }
     	  String bankName = bankCardinfo.getIssuebankname(); 
     	  boolean isbanding = bankInfoService.findBankStatus(bankName);
     	  if(isbanding == true ) {
     		  return  bankCardinfo;
     	  }else {
     		  return null;
     	  }
     	  
		} catch (Exception e) {
			e.printStackTrace();
			bankCardinfo =  null ;
		}
    	return bankCardinfo;  
    }
    
    
}
