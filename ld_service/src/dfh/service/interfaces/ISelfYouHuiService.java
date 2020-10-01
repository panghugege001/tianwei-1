package dfh.service.interfaces;

import java.io.Serializable;
import java.util.List;

import dfh.action.vo.AutoYouHuiVo;
import dfh.model.ActivityConfig;
import dfh.model.YouHuiConfig;

public interface ISelfYouHuiService {
	Object get(Class clazz, Serializable id);
	
	public AutoYouHuiVo selfYouHui90(String loginname , Double remit );
	
	public AutoYouHuiVo selfYouHui91(String loginname , Double remit );
	
	public AutoYouHuiVo selfYouHui92(String loginname , Double remit );
	
	public AutoYouHuiVo selfYouHui93(String loginname , Double remit );
	
	public AutoYouHuiVo selfYouHui94(String loginname , Double remit );
	
	public AutoYouHuiVo selfYouHui95(String loginname , Double remit );
	
	public AutoYouHuiVo selfYouHui96(String loginname, Double remit);
	
	public AutoYouHuiVo selfYouHui97(String loginname, Double remit);
	
	public AutoYouHuiVo selfYouHui98(String loginname, Double remit);
	
	public AutoYouHuiVo selfYouHui99(String loginname, Double remit);
	
	public String selfYouHui8Yuan(String loginname ,String plaform);

	public List<YouHuiConfig> queryYouHuiConfig(Integer level);
	
	public YouHuiConfig queryYouHuiConfigSingle(String title  , Integer level);
	
	public AutoYouHuiVo selfYouHui4GPI702(String loginname , Double remit );
	
	public AutoYouHuiVo selfYouHui4GPI703(String loginname , Double remit );
	
	public AutoYouHuiVo selfYouHui4GPI704(String loginname , Double remit );
	
	public AutoYouHuiVo selfYouHui4PT705(String loginname , Double remit );
	
	public AutoYouHuiVo selfYouHui4TTG706(String loginname , Double remit );
	
	public AutoYouHuiVo selfYouHuiNTFirst(String loginname, Double remit);
	
	public AutoYouHuiVo selfYouHuiNTTwice(String loginname, Double remit);
	
	public AutoYouHuiVo selfYouHuiNTSpec(String loginname, Double remit);

	public String getBirthdayMoney(String loginname);
	//当月存款
	public Double getDeposit(String loginname);

	public String selfTransferMgActivity(String loginname, Double remit, ActivityConfig config) ;

	public String selfTransferDtActivity(String loginname, Double remit,ActivityConfig config) ;

	public String selfTransferTtgActivity(String loginname, Double remit,ActivityConfig config) ;

	public String selfTransferEaActivity(String loginname, Double remit,ActivityConfig config) ;

	public String selfTransferAgActivity(String loginname, Double remit,ActivityConfig config) ;

	public String selfTransferPtActivity(String loginname, Double remit,ActivityConfig config) ;

	public String selfTransferNTActivity(String loginname, Double remit,ActivityConfig config) ;

	public String selfTransferQTActivity(String loginname, Double remit,ActivityConfig config) ;

	public String selfTransferPngActivity(String loginname, Double remit,ActivityConfig config) ;
	
	public AutoYouHuiVo selfYouHuiQTFirst(String loginname, Double remit);
	
	public AutoYouHuiVo selfYouHuiQTTwice(String loginname, Double remit);
	
	public AutoYouHuiVo selfYouHuiQTSpec(String loginname, Double remit);
	
	public String selfTransferMgSign(String loginname, Double remit) ;
	
	public String selfTransferDtSign(String loginname, Double remit) ;
	
	public String selfTransferSlotSign(String loginname, Double remit) ;
	
	public String selfTransferTtgSign(String loginname, Double remit) ;
	
	public String selfTransferPtSign(String loginname, Double remit) ;
	
	public String selfTransferNTSign(String loginname, Double remit) ;
	
	public String selfTransferQTSign(String loginname, Double remit) ;

	public String selfTransferTtgFriend(String loginname, Double remit) ;
	
	public String selfTransferPtFriend(String loginname, Double remit) ;
	
	public String selfTransferNTFriend(String loginname, Double remit) ;
	
	public String selfTransferMgFriend(String loginname, Double remit);

	public String selfTransferDtFriend(String loginname, Double remit);
	
	public String selfTransferQtEmigrated(String loginname, Double remit) ;

	//自助红包转入
	public String selfTransferTtgHB(String loginname, Double remit,String betMultiples,Integer deposit) ;

	public String selfTransferPtHB(String loginname, Double remit,String betMultiples,Integer deposit) ;

	public String selfTransferNTHB(String loginname, Double remit,String betMultiples,Integer deposit) ;

	public String selfTransferQtHB(String loginname, Double remit,String betMultiples,Integer deposit) ;

	public String selfTransferMgHB(String loginname, Double remit,String betMultiples,Integer deposit) ;

	public String selfTransferDtHB(String loginname, Double remit,String betMultiples,Integer deposit) ;
	public String selfTransferSlotHB(String loginname, Double remit,String betMultiples,Integer deposit) ;


	public String selfTransferQtFriend(String loginname, Double remit) ;
	public String selfTransferTtgEmigrated(String loginname, Double remit) ;
	public String selfTransferNTEmigrated(String loginname, Double remit) ;
	public String selfTransferPtEmigrated(String loginname, Double remit) ;

	public AutoYouHuiVo selfYouHuiMGFirst(String loginname, Double remit);
	
	public AutoYouHuiVo selfYouHuiMGTwice(String loginname, Double remit);
	
	public AutoYouHuiVo selfYouHuiMGSpec(String loginname, Double remit);
	
	public AutoYouHuiVo selfYouHuiDTFirst(String loginname, Double remit);
	
	public AutoYouHuiVo selfYouHuiDTTwice(String loginname, Double remit);
	
	public AutoYouHuiVo selfYouHuiDTSpec(String loginname, Double remit);
	
	public String platformAmountAndRecordAPPRewardLog(String loginname , String platform);

	String selfTransferMgRedRain(String loginname, Double remit);
	String selfTransferDtRedRain(String loginname, Double remit);
	String selfTransferRedRain(String loginname, Double remit, String platform);
 
}