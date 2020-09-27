package com.nnti.pay.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.nnti.common.constants.DictionaryType;
import com.nnti.common.exception.BusinessException;
import com.nnti.common.model.vo.Dictionary;
import com.nnti.common.service.interfaces.IBankInfoService;
import com.nnti.common.service.interfaces.IDictionaryService;
import com.nnti.common.utils.Assert;
import com.nnti.common.utils.MyUtils;
import com.nnti.common.utils.MyWebUtils;
import com.nnti.pay.model.vo.MerchantPay;
import com.nnti.pay.service.interfaces.IMerchantPayService;

/**
 * Created by wander on 2017/2/8.
 */
@Controller
@RequestMapping("/merchantPay")
public class MerchantPayController extends BasePayController {

    @Autowired
    private IMerchantPayService merchantPayService;
    @Autowired
    private IBankInfoService bankinfoService;
    @Autowired
    private IDictionaryService dictionaryService;

    @RequestMapping("/main")
    public ModelAndView main(HttpServletRequest req) {
        ModelAndView model = new ModelAndView("base/merchantPay");
        return model;
    }

    @RequestMapping("/finance")
    public ModelAndView finance(HttpServletRequest req) {

        ModelAndView model = new ModelAndView("base/merchantPay_finance");
        return model;
    }

    @ResponseBody
    @RequestMapping(value = "/query", method = RequestMethod.POST, produces = "application/json")
    public List<MerchantPay> query(MerchantPay merchantPay) {
        ModelAndView model = new ModelAndView("base/merchantPay_list");
        List<MerchantPay> data = merchantPayService.findByCondition(merchantPay, null);
        model.addObject("data", data);
        return data;
    }
    
    @ResponseBody
    @RequestMapping(value = "/openIpNameList", method = RequestMethod.POST, produces = "application/json")
    public List openIpNameList() {
        ModelAndView model = new ModelAndView("base/merchantPay_list");

        List  list = new ArrayList<>();
        Map map = null;
        for (DictionaryType s : DictionaryType.values())
        {
        	map = new HashMap<>();
        	map.put("id", s.getText());
        	map.put("name", s.getName());
        	list.add(map);
        }
        
        System.out.println("data:"+list.toString());
        model.addObject("data", list);
        return list;
    }
    
    
    /******
     * 新增ip
     */
    @ResponseBody
    @RequestMapping(value = "/saveOpenIp", method = RequestMethod.POST, produces = "application/json")
    public String saveOpenIp(String dict_type,String dict_name ,String dict_value) throws Exception {
        Dictionary dictionary = new Dictionary();
        dictionary.setDictType(dict_type);
        dictionary.setDictName(dict_name);
        dictionary.setDictValue(dict_value.trim());
        dictionary.setUseable(0);
        dictionaryService.add(dictionary);
        return SUCCESS;
    }
    
    /******
     * 查询ip
     */
    @ResponseBody
    @RequestMapping(value = "/queryOpenIp", method = RequestMethod.POST, produces = "application/json")
    public List<Dictionary> queryOpenIp(Dictionary dictionary) throws Exception {
    	ModelAndView model = new ModelAndView("base/merchantPay_list");
        List<Dictionary> data = dictionaryService.findByCondition(dictionary);
        model.addObject("data", data);
        return data;
    }
    
    

    @ResponseBody
    @RequestMapping(value = "/getPayWay", method = RequestMethod.POST, produces = "application/json")
    public List<MerchantPay> getPayWay(Integer useable,Integer paySwitch) {
        return merchantPayService.findByCondition(new MerchantPay(paySwitch, useable,null), null);
    }

    @ResponseBody
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String delete(Long id) throws BusinessException {
        merchantPayService.delete(id);
        return SUCCESS;
    }

    @RequestMapping(value = "/edit")
    public ModelAndView edit(Long id, String loginname) throws BusinessException {
        ModelAndView model = new ModelAndView("base/merchantPay_modal");
        MerchantPay vo = merchantPayService.get(id);
        model.addObject("vo", vo);
        model.addObject("loginname", loginname);
        return model;
    }

    @ResponseBody
    @RequestMapping(value = "/get")
    public MerchantPay get(Long id) throws BusinessException {
        MerchantPay vo = merchantPayService.get(id);
        return vo;
    }

    @RequestMapping(value = "/add")
    public ModelAndView add(Long id) {
        ModelAndView model = new ModelAndView("base/merchantPay_modal");
        return model;
    }

    @ResponseBody
    @RequestMapping(value = "/add_update", method = RequestMethod.POST)
    public String add_update(MerchantPay merchantPay, String loginname) throws Exception {

        try {
            Assert.isTrue(MyUtils.isNotEmpty(loginname), "请重新登陆..");
        } catch (Exception e) {
            log.error("登陆异常：", e);
            return ERROR;
        }
        if (MyUtils.isNotEmpty(merchantPay.getId())) {
            merchantPayService.update(merchantPay, loginname);
        } else {
            merchantPayService.add(merchantPay);
        }
        return SUCCESS;
    }
    
    
    //财务客户端程序控制开关接口
    @ResponseBody
    @RequestMapping(value = "/controlSwitch")
    public String controlSwitch(HttpServletRequest req) {
    	
    	try {
    		log.info("客户端程序启动控制开关：" + MyWebUtils.getRequestParameters(req));
    		
    		  String payway = req.getParameter("payway"); //转账payway=8(通联) payway=6(微信)  
    		  String operator = req.getParameter("operator"); //转账虚拟机
    		  Integer paySwitch = Integer.parseInt(req.getParameter("paySwitch")); //开关状态1开 2关
    		  String bankCardNumber = req.getParameter("bankCardNumber"); //银行卡号
    	      merchantPayService.controlSwitch(payway,paySwitch, operator);
    	      paySwitch = paySwitch==2?0:paySwitch;
    	      bankinfoService.controlBank(bankCardNumber,paySwitch);
		} catch (Exception e) {
			e.printStackTrace();
			 return error;
		}
        return success;
    }
    
    
    
    
    
    
}
