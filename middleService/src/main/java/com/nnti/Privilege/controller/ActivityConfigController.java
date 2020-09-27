package com.nnti.Privilege.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.gson.Gson;
import com.nnti.Privilege.model.bean.ActivityConfig;
import com.nnti.Privilege.model.vo.Prize;
import com.nnti.Privilege.service.ActivityConfigService;
import com.nnti.common.constants.ProposalType;
import com.nnti.common.model.vo.Proposal;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by Zii on 2017/6/15.
 */
@RestController
@RequestMapping("/activity")
public class ActivityConfigController {
    @Autowired
    private ActivityConfigService activityConfigService;

    public static ReadWriteLock lock = new ReentrantReadWriteLock();
	private static Logger log = Logger.getLogger(ActivityConfigController.class);

    @RequestMapping("/activityConfigSave")
    public String activityConfigSave(@RequestParam(value = "data") String data){
        try {
            ActivityConfig activityConfig = new Gson().fromJson(data, ActivityConfig.class);
            activityConfigService.save(activityConfig);
            return "SUCCESS";
        }catch (Exception e){
            return "ERRO";
        }

    }

    @RequestMapping(value = "/queryActivityConfig",produces = "text/html;charset=UTF-8")
    public String queryActivityConfig(@RequestParam(value = "title",required = false) String title,@RequestParam(value = "startTime",required = false) String startTime,@RequestParam(value = "endTime",required = false) String endTime,@RequestParam(value = "size",required = false)  String size ,@RequestParam(value = "status",required = false) String status) throws Exception {
        List<ActivityConfig> activityConfigs = activityConfigService.queryVipFreeData(title,startTime,endTime,Integer.valueOf(size),Integer.valueOf(status));
        String s = JSON.toJSONString(activityConfigs,SerializerFeature.DisableCircularReferenceDetect);
        return s;
    }


    @RequestMapping(value = "/checkStatus",produces = "text/html;charset=UTF-8")
    public String checkStatus(@RequestParam(value = "title") String title,@RequestParam(value = "entrance",required = false) String entrance,@RequestParam(value = "level",required = false) String level ) {
        List<ActivityConfig> activityConfigs = activityConfigService.checkStatus(title,entrance,level);
        if(activityConfigs==null||activityConfigs.size()==0){
            return null;
        }
        String s = JSON.toJSONString(activityConfigs,SerializerFeature.DisableCircularReferenceDetect);
        return s;
    }
    
    
    /**
     *   返回vip 筹码
     * @param title
     * @param entrance
     * @param level
     * @return
     */
    @RequestMapping(value = "/checkVipFee",produces = "text/html;charset=UTF-8")
    public String checkVipFee(@RequestParam(value = "title") String title,@RequestParam(value = "entrance",required = false) String entrance,@RequestParam(value = "level",required = false) String level ) {
        List<ActivityConfig> activityConfigs = activityConfigService.checkVipFee(title,entrance,level,null,null);
        if(activityConfigs==null||activityConfigs.size()==0){
            return null;
        }
        String s = JSON.toJSONString(activityConfigs,SerializerFeature.DisableCircularReferenceDetect);
        return s;
    }
    
    

    
    
    /**
     * 领取vip 筹码
     * @param title
     * @param entrance
     * @param level
     * @return
     */
    @RequestMapping(value = "/applyActivity",produces = "text/html;charset=UTF-8")
    public String exePrivilegesOfToday(@RequestParam(value = "remark") String remark, @RequestParam(value = "loginname") String loginname,  @RequestParam(value = "role") String role, @RequestParam(value = "credit") Double credit , @RequestParam(value = "level") String level , @RequestParam(value = "agent") String agent,HttpServletResponse response){
    	PrintWriter out = null;
    	try {
			out = response.getWriter();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
    	SimpleDateFormat sf = new SimpleDateFormat("yyyyMM");	
    		String currentMonth =  sf.format(new Date());
    		Map<String, Object> params = new HashMap<String, Object>();
			params.put("status", new Integer("2"));
			params.put("remark", remark);
			params.put("loginname", loginname);
			params.put("distribute_month", currentMonth);
			List privilegesList = null;
			List privilegeList = null;
			List currentPrivilegeList = null;
			String message = null;
			lock.writeLock().lock();
			HashMap hashMap = null;
			try {
				// 查询筹码是否已经领取或者没有配置筹码
				privilegesList = activityConfigService.getListBySql( params);
				if(privilegesList.size() == 0 ) {
					List<ActivityConfig> activityConfigs = activityConfigService.checkVipFee("vipmonthfree","",  level,null,null);
					// 查询筹码是否已经配置
					if(activityConfigs != null && activityConfigs.size() != 0 ) {
						  params = new HashMap<String, Object>();
						  params.put("status", new Integer("1"));
						  params.put("remark", remark);
						  params.put("distribute_month", currentMonth);
						  params.put("loginname", loginname);
						  currentPrivilegeList = activityConfigService.getListBySql( params);
						  if(currentPrivilegeList == null || currentPrivilegeList.size() !=0 ) {
							  out.write( "vip等级筹码已经领取" );
							  return null;
						  }
						  params = new HashMap<String, Object>();
						  params.put("remark", remark);
						  params.put("distribute_month", currentMonth);
						  params.put("loginname", loginname);
						  currentPrivilegeList = activityConfigService.getListBySql( params);
						  if(currentPrivilegeList == null || currentPrivilegeList.size() ==0 ) {
							  ActivityConfig activityConfig = (ActivityConfig)activityConfigs.get(0);
							   Date starttime  = activityConfig.getDepositstarttime();
							   Date endtime  =   activityConfig.getDepositendtime();
							   Double depositAmount  =   activityConfig.getDeposit();
							   Double amount = activityConfig.getAmount(); // 筹码金额
							   params = new HashMap<String, Object>();
							   params.put("amount", amount); 
							   params.put("status",  new Integer("2")); 
							   params.put("loginname", loginname); 
							   params.put("distribute_month", currentMonth); 
							   params.put("starttime", starttime); 
							   params.put("endtime", endtime); 
							   params.put("depositAmount", depositAmount); 
							   params.put("remark", remark); 
							   params.put("createtime", new Timestamp(   new Date().getTime()  )   );
							   activityConfigService.insertActivityConfig(params); 
						  }
						  
						
					}else {
						out.write("请配置vip等级金额");
						return null;
					}
				}
	    		params = new HashMap<String, Object>();
				params.put("status", new Integer("2"));
				params.put("remark", remark);
				params.put("loginname", loginname);
				params.put("distribute_month", currentMonth); 
				privilegesList = activityConfigService.getListBySql( params);
					for(int i=0; i < privilegesList.size() ;i++) {
					    hashMap = (HashMap)privilegesList.get(i);
						if(hashMap != null) {
							Double amount  = ((BigDecimal)hashMap.get("amount")).doubleValue();
							Date starttime  = (Date)hashMap.get("starttime");
							Date endtime  = (Date)hashMap.get("endtime");
							Double depositAmount  =   ((BigDecimal)hashMap.get("depositAmount")).doubleValue();
							params = new HashMap<String, Object>();
							params.put("orderType","0");  //支付成功的订单
							params.put("pType", "502");   //存款提案
							params.put("loginname", loginname);
							params.put("startTime",    new Timestamp(starttime.getTime())  );
							params.put("endTime",        new Timestamp(endtime.getTime())    );
							Double localdeposit = activityConfigService.getDoubleValueBySql(params);
							Integer id  = (Integer)hashMap.get("id");
							if(localdeposit < new Double(depositAmount).doubleValue() ){
								  out.write(  "存款未达到要求或活动未开启" );
								  return null;
							} 
						
							Timestamp timeStamp = new Timestamp(	new Date().getTime());
							List<ActivityConfig> activityConfigs = activityConfigService.checkVipFee("vipmonthfree","",  level,timeStamp,timeStamp);
							if(activityConfigs ==null || activityConfigs.size() ==0) {
								  out.write(  "活动未开启" );
								  return null;
							}
							String pno = ProposalType.VIPFREE.getCode() + RandomStringUtils.randomAlphanumeric(15);
							Prize prize = new Prize(pno, role, loginname, amount, remark);
							activityConfigService.savePrize(prize);
							params = new HashMap<String, Object>();
							params.put("id", id);
							params.put("status", new Integer("1") );
							activityConfigService.updatePrivileges(params);
							params = new HashMap<String, Object>();
							params.put("loginname", loginname);
							params.put("amount", credit.doubleValue()+ amount.doubleValue());
							activityConfigService.updateUserCreditSql(params);
							activityConfigService.insertCreditLog(loginname,ProposalType.VIPFREE.getCode(),credit, amount,credit.doubleValue()+ amount.doubleValue(),
									"referenceNo:" + pno + ";" + remark	);
							Proposal proposal = new Proposal(pno, loginname,new Timestamp(new Date().getTime() ), ProposalType.VIPFREE.getCode(),new Integer(level), loginname, amount,agent,
									2,"前台", null, null);
							activityConfigService.insertProposal(proposal);
							
							
						}
					}
				out.write(message == null ? "领取成功" : message);
			} catch (Exception e) {
				log.error(e.getMessage());
				e.printStackTrace();
				message =  "领取vip筹码优惠异常";
				out.write(message);
				throw new RuntimeException("领取vip筹码优惠异常");
			}finally {
				lock.writeLock().unlock();
			}
    		return null ;
    }


}
