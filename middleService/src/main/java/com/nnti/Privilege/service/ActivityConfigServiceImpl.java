package com.nnti.Privilege.service;

import com.nnti.Privilege.dao.master.ActivityConfigMapper;
import com.nnti.Privilege.model.bean.ActivityConfig;
import com.nnti.Privilege.model.bean.ActivityConfigExample;
import com.nnti.Privilege.model.vo.Prize;
import com.nnti.common.model.vo.Proposal;
import com.nnti.common.utils.DateUtil;
import com.nnti.withdraw.service.implementations.BaseService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Zii on 2017/6/14.
 */
@Service("vipService")
@Transactional(rollbackFor = Exception.class)
public class ActivityConfigServiceImpl extends BaseService implements ActivityConfigService {
    @Autowired
    private ActivityConfigMapper activityConfigMapper;
    @Override
    public void save(ActivityConfig activityConfig) {
        activityConfig.setCreatetime(new Date());
        activityConfig.setStatus(0);
        activityConfigMapper.insert(activityConfig);
    }

    @Override
    public List<ActivityConfig> queryVipFreeData(String title,String startTime,String endTime,Integer size,Integer status) throws Exception {
        Date start=null;
        Date end=null;
        if(!"".equals(startTime)|| !StringUtils.isEmpty(startTime)) {
             start = DateUtil.parse(startTime);
        }
        if(!"".equals(endTime)|| !StringUtils.isEmpty(endTime)) {
             end = DateUtil.parse(endTime);
        }
        ActivityConfigExample example = new ActivityConfigExample();
        ActivityConfigExample.Criteria exampleCriteria = example.createCriteria();
        if(StringUtils.isNotEmpty(title)){
            exampleCriteria.andTitleEqualTo(title);
        }
        if(start!=null){
            exampleCriteria.andActivitystarttimeGreaterThan(start);
        }
        if(end!=null) {
            exampleCriteria.andActivityendtimeLessThan(end);
        }
        if(status!=3){
            exampleCriteria.andStatusEqualTo(status);
        }
        List<ActivityConfig> activityConfigs = this.activityConfigMapper.selectByExample(example);
        return activityConfigs;
    }

    @Override
    public List<ActivityConfig> checkStatus(String title,String entrance,String level) {
            List<ActivityConfig> activityConfigs = this.activityConfigMapper.getList(title, entrance,level );
        return activityConfigs;
    }
    
    @Override
    public List<ActivityConfig> checkVipFee(String title,String entrance,String level,Timestamp startTime, Timestamp  endTime ) {
    	List<ActivityConfig> activityConfigs = null;    
    	if(startTime ==null || endTime == null) {
    	    	 activityConfigs = this.activityConfigMapper.getCheckVipFeeList(title, entrance,level,null,  null);
    	}else {
    	    	 activityConfigs = this.activityConfigMapper.getCheckVipFeeList(title, entrance,level,startTime ,  endTime );
    	}
        return activityConfigs;
    }
    
//    /**   
//     * 得到本月的第一天   
//     * @return   
//     */    
//    public Timestamp  getMonthFirstDay() {     
//        Calendar calendar = Calendar.getInstance();     
//        calendar.set(Calendar.DAY_OF_MONTH, calendar     
//                .getActualMinimum(Calendar.DAY_OF_MONTH));     
//         Date date =  calendar.getTime() ;
//         return new Timestamp(date.getTime());
//    }     
    
//    /**   
//     * 得到本月的最后一天   
//     *    
//     * @return   
//     */    
//    public static Timestamp getMonthLastDay() {     
//        Calendar calendar = Calendar.getInstance();     
//        calendar.set(Calendar.DAY_OF_MONTH, calendar     
//                .getActualMaximum(Calendar.DAY_OF_MONTH));     
//        Date date =  calendar.getTime() ;
//        return new Timestamp(date.getTime());    
//    }  
    
    
    
    
    @Override
    public List getListBySql(Map<String, Object> params ) {
    	return this.activityConfigMapper.getListBySql(params);
    }

    @Override
    public Double  getDoubleValueBySql(Map<String, Object> params) {
    	Double bankDeposit =  this.activityConfigMapper.getbankDepositBySql(params); 
    	 
    	Double paymentDeposit =  this.activityConfigMapper.thirdPaymentDepositBySql(params);
    	return   new Double((bankDeposit == null ? 0 : bankDeposit.doubleValue() )+  (paymentDeposit == null ? 0 : paymentDeposit.doubleValue())  );
    }
    
   public void savePrize(Prize prize) {
	   this.activityConfigMapper.savePrize(prize);
   }
   
   public int updatePrivileges(Map<String, Object> params) {
	   return this.activityConfigMapper.updatePrivileges(params);
   }
   
   public int updateUserCreditSql(Map<String, Object> params) {
	   return this.activityConfigMapper.updateUserCreditSql(params);
   }
   
   public int insertCreditLog(String loginname,  Integer prize, Double credit, Double amount,  Double allCredit,String remark) {
	   Map<String, Object> params = new HashMap<String, Object>();
	   params.put("loginname", loginname);
	   params.put("type", prize);
	   params.put("credit", credit);
	   params.put("remit", amount);
	   params.put("newCredit", allCredit);
	   params.put("remark", remark);
	   params.put("createtime", DateUtil.getCurrentTimestamp() );
	   
	   return this.activityConfigMapper.insertCreditLog(params);
   }
   
   public int insertProposal(Proposal proposal ) {
	   Map<String, Object> params = new HashMap<String, Object>();
	   params.put("pno", proposal.getPno());
	   params.put("proposer", proposal.getProposer());
	   params.put("createTime", new Timestamp(proposal.getCreateTime().getTime()));
	   params.put("type", proposal.getType());
	   params.put("quickly", proposal.getQuickly());
	   params.put("loginname",  proposal.getLoginName() );
	   params.put("amount" , proposal.getAmount() );
	   params.put("agent", proposal.getAgent());
	   params.put("flag", proposal.getFlag()  );
	   params.put("whereisfrom", proposal.getWhereIsFrom() );
	   params.put("remark", proposal.getRemark());
	   params.put("generateType", proposal.getGenerateType());
	   return this.activityConfigMapper.insertProposal(params);
   }
   
   
   public int insertActivityConfig(Map<String, Object> params) {
	   return this.activityConfigMapper.insertActivityConfig(params);
   }
}
