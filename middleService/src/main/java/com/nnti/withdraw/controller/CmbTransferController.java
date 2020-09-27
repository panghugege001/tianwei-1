package com.nnti.withdraw.controller;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.nnti.common.constants.NotifyUrl;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.nnti.common.constants.DictionaryType;
import com.nnti.common.constants.FunctionCode;
import com.nnti.common.controller.BaseController;
import com.nnti.common.model.dto.DataTransferDTO;
import com.nnti.common.model.vo.BankInfo;
import com.nnti.common.model.vo.CmbTransfer;
import com.nnti.common.model.vo.Dictionary;
import com.nnti.common.service.interfaces.IBankInfoService;
import com.nnti.common.service.interfaces.ICmbTransferService;
import com.nnti.common.service.interfaces.IDictionaryService;
import com.nnti.common.utils.Assert;
import com.nnti.common.utils.DateUtil;
import com.nnti.common.utils.HttpUtil;
import com.nnti.common.utils.TLConfigUtil;
import com.nnti.pay.service.interfaces.IMerchantPayService;
import com.nnti.withdraw.util.SHA;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@RestController
@RequestMapping("/pay")
public class CmbTransferController extends BaseController {

	private static Logger log = Logger.getLogger(CmbTransferController.class);

    @Autowired
    private IDictionaryService dictionaryService;
	@Autowired
	private ICmbTransferService cmbTransferService;
	@Autowired
	private IBankInfoService bankinfoService;

	@RequestMapping(value = "/deposit/list", method = { RequestMethod.POST })
	public String depositList(@RequestParam(value = "deposit_result", defaultValue = "") String deposit_result, HttpServletRequest request, HttpServletResponse response) {

		String notifyUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
		
		log.info("通联回调数据处理开始----------------------------->：" + deposit_result);
		log.info("通联回调URL：" + notifyUrl);
		String product = null;
		PrintWriter out = null;
		try {
			product = NotifyUrl.getProduct(notifyUrl);
			
			Assert.isTrue(validationTrustIp(request, DictionaryType.MFBTL_TRUST_IP_TYPE.getName()), "回掉的IP，不在白名单内！");
		} catch (Exception e) {
			log.error("没有解析到产品名称，notifyUrl：" + notifyUrl, e);
			return "域名不对";
		}

		JSONObject json = JSONObject.fromObject(deposit_result);
		String sign = String.valueOf(json.get("sign"));
		String data = String.valueOf(json.get("data"));

		Map<String, String> configMap = TLConfigUtil.get(product);

		String mySign = SHA.sign(data + configMap.get("secretkey"), "SHA-256");

		try {
			out = response.getWriter();
			if (!sign.equals(mySign)) {

				failure(FunctionCode.SC_10001.getCode(), "验签失败");
			}

			JSONArray jsonArray = JSONArray.fromObject(data);

			if (null != jsonArray && !jsonArray.isEmpty()) {

				java.util.Date date = new java.util.Date();
				List<CmbTransfer> cmbList = new ArrayList<CmbTransfer>();

				for (int i = 0, len = jsonArray.size(); i < len; i++) {

					Object obj = jsonArray.get(i);
					JSONObject jsonObject = JSONObject.fromObject(obj);

					CmbTransfer cmbTransfer = new CmbTransfer();

					cmbTransfer.setAmount(Double.parseDouble(String.valueOf(jsonObject.get("amount"))));
					cmbTransfer.setBalance(0.00);
					cmbTransfer.setJylx(getString(jsonObject, "client_transtype"));
					cmbTransfer.setNotes(getNoteString(getString(jsonObject, "client_postscript")));
					cmbTransfer.setAcceptCardNum(getString(jsonObject, "deposit_cardnumber"));
					cmbTransfer.setPayDate(DateUtil.parse("yyyy-MM-dd HH:mm:ss", getString(jsonObject, "deposit_time")));
					cmbTransfer.setAdminId(0);
					cmbTransfer.setStatus(0);
					cmbTransfer.setDate(date);
					cmbTransfer.setUserAccountName(getString(jsonObject, "client_accountname"));
					cmbTransfer.setPayType(3);
					cmbTransfer.setOrderNumber(getString(jsonObject, "order_number"));

					if (!isExist(getString(jsonObject, "order_number"),null,null,null)) {

						cmbList.add(cmbTransfer);
					}
				}

				if (!cmbList.isEmpty()) {

					cmbTransferService.createCmbTransferList(cmbList);
				}
			}
		}catch (Exception e){
			log.error("系统异常:处理数据：" + deposit_result, e);
			out.write("业务异常");
			return null;
		}

		out.write("ok");

		return null;
	}
	
	
	/*****
	 * 秒付宝 支付宝 网银 通联 回调
	 * @param deposit_result
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/deposit/mfb_zfb_wy", method = { RequestMethod.POST })
	public String mfb_zfb_wy(@RequestParam(value = "deposit_result", defaultValue = "") String deposit_result, HttpServletRequest request, HttpServletResponse response) {

		String notifyUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";

		log.info("秒付宝_支付宝_网银|数据处理开始----------------------------->：" + deposit_result);
		log.info("秒付宝_支付宝_网银|回调URL：" + notifyUrl);
		String product = null;
		PrintWriter out = null;
		try {
			//Assert.isTrue(validationTrustIp(request, DictionaryType.MFBTL_TRUST_IP_TYPE.getName()), "回掉的IP，不在白名单内！");
			
			product = NotifyUrl.getProduct(notifyUrl);
		} catch (Exception e) {
			log.error("没有解析到产品名称，notifyUrl：" + notifyUrl, e);
			return "域名不对";
		}
		

		JSONObject json = JSONObject.fromObject(deposit_result);
		String sign = String.valueOf(json.get("sign"));
		String data = String.valueOf(json.get("data"));
       
		Map<String, String> configMap = TLConfigUtil.get(product);

		String mySign = SHA.sign(data + configMap.get("secretkey"), "SHA-256");

		try {
			out = response.getWriter();
			
			if (!sign.equals(mySign)) {

				failure(FunctionCode.SC_10001.getCode(), "验签失败");
			}
			

			JSONArray jsonArray = JSONArray.fromObject(data);

			if (null != jsonArray && !jsonArray.isEmpty()) {

				java.util.Date date = new java.util.Date();
				List<CmbTransfer> cmbList = new ArrayList<CmbTransfer>();

				for (int i = 0, len = jsonArray.size(); i < len; i++) {

					Object obj = jsonArray.get(i);
					JSONObject jsonObject = JSONObject.fromObject(obj);

					CmbTransfer cmbTransfer = new CmbTransfer();
					
					cmbTransfer.setAmount(Double.parseDouble(String.valueOf(jsonObject.get("amount"))));
					cmbTransfer.setBalance(0.00);
					cmbTransfer.setJylx(getString(jsonObject, "client_transtype"));
					cmbTransfer.setNotes(getString(jsonObject, "client_postscript"));
					cmbTransfer.setAcceptCardNum(getString(jsonObject, "deposit_cardnumber"));
					cmbTransfer.setPayDate(DateUtil.parse("yyyy-MM-dd HH:mm:ss", getString(jsonObject, "deposit_time")));
					cmbTransfer.setAdminId(0);
					cmbTransfer.setStatus(0);
					cmbTransfer.setDate(date);
					cmbTransfer.setUserAccountName(getString(jsonObject, "client_accountname"));
					cmbTransfer.setUaccountno(getString(jsonObject, "client_cardnumber"));
					
					List list = Arrays.asList(new String[] {"微信转账"});
					if(list.contains(cmbTransfer.getJylx())){
						cmbTransfer.setPayType(4);	
					}
					else if("云闪付".contains(cmbTransfer.getJylx())){
						cmbTransfer.setPayType(5);
					}
					else {
						cmbTransfer.setPayType(1);	
					}
					cmbTransfer.setOrderNumber(getString(jsonObject, "order_number"));

					String order_number = getString(jsonObject, "order_number");
					Double amount = Double.parseDouble(String.valueOf(jsonObject.get("amount")));
					String userAccountName = getString(jsonObject, "client_accountname");
					Date paydate = DateUtil.parse("yyyy-MM-dd HH:mm:ss", getString(jsonObject, "deposit_time"));
					
					if (!isExist(order_number,amount,userAccountName,paydate)) {

						cmbList.add(cmbTransfer);
					}
				}

				if (!cmbList.isEmpty()) {

					cmbTransferService.createCmbTransferList(cmbList);
				}
			}
		}catch (Exception e){
			e.printStackTrace();
			log.error("系统异常:处理数据：" + deposit_result, e);
			out.write("业务异常");
			return null;
		}

		log.info("秒付宝秒存-返回通知：Ok");
		out.write("ok");

		return null;
	}
	

	private Boolean isExist(String orderNumber,Double amount,String userAccountName ,Date paydate ) throws Exception {

		Map<String, Object> paramsMap = new HashMap<String, Object>();

		paramsMap.put("orderNumber", orderNumber);
		paramsMap.put("amount", amount);
		paramsMap.put("userAccountName", userAccountName);
		paramsMap.put("payDate", paydate);

		List<CmbTransfer> cmbList = cmbTransferService.findList(paramsMap);
   
		if (null != cmbList && !cmbList.isEmpty()) {

			return true;
		}

		return false;
	}

	public String getNoteString(String note) {

		note = note.replaceAll("[\u4e00-\u9fa5]+", "").replace(" ", "");
		
		String regEx="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]"; 
		Pattern p = Pattern.compile(regEx); 
		Matcher m = p.matcher(note);
		log.info("过滤特殊字符："+m.replaceAll("").trim());
		return m.replaceAll("").trim();
	}

	public String getString(JSONObject jsonObject, String key) {

		String value = "";

		if (null != jsonObject.get(key)) {

			value = String.valueOf(jsonObject.get(key));
		}

		return value;
	}
	
	 /*** 判断加白ip 
	 * @throws Exception */
    public boolean validationTrustIp(HttpServletRequest request, String type) throws Exception {
        Dictionary dictionary = new Dictionary(type);
        List<Dictionary> ips = dictionaryService.findByCondition(dictionary);
        String requestIp = HttpUtil.getIp(request);
        log.info("requestIp:" + requestIp + ",访问IP：" + requestIp);
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
    
}