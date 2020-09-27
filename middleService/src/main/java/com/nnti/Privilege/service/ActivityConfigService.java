package com.nnti.Privilege.service;


import com.nnti.Privilege.model.bean.ActivityConfig;
import com.nnti.Privilege.model.vo.Prize;
import com.nnti.common.model.vo.Proposal;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * Created by Zii on 2017/6/14.
 */
public interface ActivityConfigService {
    void save(ActivityConfig activityConfig);

    List<ActivityConfig> queryVipFreeData(String title, String startTime, String endTime, Integer size, Integer status) throws Exception;

    List<ActivityConfig> checkStatus(String title,String entrance,String level);
    
    public List<ActivityConfig> checkVipFee(String title,String entrance,String level, Timestamp startTime, Timestamp  endTime) ;
    
    List getListBySql(Map<String, Object> params);
    
    Double  getDoubleValueBySql(Map<String, Object> params);
    
    void savePrize(Prize prize);
    
    int updatePrivileges(Map<String, Object> params);
    
    int updateUserCreditSql(Map<String, Object> params);
    
    int insertCreditLog(String loginname,  Integer prize, Double credit, Double amount,  Double allCredit,String remark);
    
    int insertActivityConfig(Map<String, Object> params);
    
    int insertProposal(Proposal proposal);
}
