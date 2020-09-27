package dfh.service.interfaces;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;

import dfh.model.AgDataRecord;
import dfh.model.AlipayTransfers;
import dfh.model.DepositWechat;
import dfh.model.JCProfitData;
import dfh.model.PlatformData;
import dfh.model.Users;
import dfh.spider.PTBetVO;
import net.sf.json.JSONArray;

public interface IGetdateService {
	//更新代付订单状态
	public void updateFPayOrder();

	public void updateoperator(String bankStr);
	
	public void updateoperatorNew(String bankname);
	
	public Object save(Object o);
	
	public List findByCriteria(DetachedCriteria criteria) ;
	
    public String getAgSlotLastTime(String platform);
	
    public void processInsertAgSlotNewData(Map<String,AgDataRecord>data,Date lastTime,String platformType);


	/**
	 * 采集fanya日志
	 */
	public void fayalogData();
	public void processStatusData(Users ptUser);
	
	/**
	 * 根据时间抓取集团下所有玩家的投注额
	 */
	public void processStatusData(Date date);
	
	public Boolean processNewStatusData(String PLAYERNAME,String CODE,String CURRENCYCODE,String ACTIVEPLAYERS,String BALANCECHANGE,String DEPOSITS,String WITHDRAWS,String BONUSES,String COMPS,String PROGRESSIVEBETS,String PROGRESSIVEWINS,String BETS,String WINS,String NETLOSS,String NETPURCHASE,String NETGAMING,String HOUSEEARNINGS,String RNUM,String DATATIME);

	public Boolean dealNewPtData(String pLAYERNAME,
			String fULLNAME, String vIPLEVEL, String cOUNTRY, String gAMES,
			String cURRENCYCODE, String bETS, String wINS, String iNCOME,
			String rNUM, String dateTime);
	
	public Boolean updateNewPtData(String playername,String dateTime,Double betsTiger , Double winsTiger);
	
	public boolean updateJCData(List<JCProfitData> jclist, String executeTime);

	public void saveOrUpdateEBetAppData(List<PlatformData> profits);

	public void updateQtPlatForm(PlatformData data);
	
	public List<PlatformData> selectQtData(Date startT, Date endT);
	
	public void updateSixLotteryPlatForm(PlatformData data) ;
	
	public void updateAlipayStatus();
	
	/**
	 * 处理额度验证存款
	 */
	public void processValiteDeposit();
	/**
	 * 微信 处理额度验证存款
	 */
	public void processWeixinDeposit(DepositWechat depositWechat);
	
	/**
	 * 处理过期存款订单
	 */
	public void discardOrder();
	
	public void dealAlipayData(AlipayTransfers alipayTransfers) ;
	
	/**
	 * 秒存过期处理
	 */
	public void discardOrderMc();
	
	public void detailData();
	
	public void summaryData();
	
	public void collectionChessData();

	public void processAllPtData(JSONArray arr, Map<String, PTBetVO> tiggerAllMap, Map<String, PTBetVO> progressiveMap,
			String executeTime) throws ParseException;
	
	public void collectionSbaData();

	public void collection761Data(String executeTime) throws Exception;

	public void collectionAgData(String platform,String executeTime) throws Exception;
}
