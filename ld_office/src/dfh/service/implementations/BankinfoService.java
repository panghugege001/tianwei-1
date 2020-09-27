package dfh.service.implementations;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import dfh.dao.BankinfoDao;
import dfh.dao.LogDao;
import dfh.dao.UniversalDao;
import dfh.model.Bankinfo;
import dfh.model.Const;
import dfh.model.MifResponseVo;
import dfh.model.MifVo;
import dfh.model.PayMerchant;
import dfh.model.enums.OperationLogType;
import dfh.model.enums.TlyBankFlagEnum;
import dfh.service.interfaces.IBankinfoService;
import dfh.utils.DateUtil;
import dfh.utils.MyWebUtils;
import dfh.utils.TlyDepositUtil;
import net.sf.json.JSONObject;

@Service
public class BankinfoService extends UniversalServiceImpl implements IBankinfoService {

	private BankinfoDao bankinfoDao;
	private UniversalDao universalDao;
	private LogDao logDao;
	
 	public BankinfoDao getBankinfoDao() {
		return bankinfoDao;
	}
	public void setBankinfoDao(BankinfoDao bankinfoDao) {
		this.bankinfoDao = bankinfoDao;
	}
	
	public UniversalDao getUniversalDao() {
		return universalDao;
	}
	public void setUniversalDao(UniversalDao universalDao) {
		this.universalDao = universalDao;
	}
	@Override
	public String addbankinfo(Integer id,String username, Integer type, String bankname,String remark,String vpnname, String vpnpassword,String accountno,String loginname,String password,Integer bankInfoType,String bankcard,String usb,String realname,String remoteip,String samebank,String crossbank,Double transfermoney,Integer autopay,String userrole,Double fee,String zfbImgCode,Integer scanAccount,Integer paytype,Double depositMin,Double depositMax) throws Exception {
		if(id == null)
			id=0;
		Bankinfo bankinfo = (Bankinfo)universalDao.getHibernateTemplate().get(Bankinfo.class, id);
		if(bankinfo != null){
			bankinfo.setBankname(bankname);
			bankinfo.setRemark(remark);
			bankinfo.setType(type);
			bankinfo.setUsername(username);
			bankinfo.setVpnname(vpnname);
			bankinfo.setVpnpassword(vpnpassword);
			bankinfo.setRealname(realname);
			bankinfo.setRemoteip(remoteip);
			bankinfo.setAccountno(accountno);
			if(loginname!=null && !loginname.equals("")){
				bankinfo.setLoginname(loginname);
			}
			bankinfo.setBanktype(bankInfoType);
			if(StringUtils.isNotBlank(usb)){
				bankinfo.setUsb(usb);
			}
			if(StringUtils.isNotBlank(password)){
				bankinfo.setPassword(password);
			}
			if(StringUtils.isNotBlank(bankcard) && !bankcard.contains("*")){
				bankinfo.setBankcard(bankcard);
			}
			bankinfo.setSamebank(samebank);
			bankinfo.setCrossbank(crossbank);
			bankinfo.setTransfermoney(transfermoney);
			bankinfo.setAutopay(autopay);
			bankinfo.setUserrole(userrole);
			bankinfo.setFee(fee);
			bankinfo.setZfbImgCode(zfbImgCode);
			bankinfo.setScanAccount(scanAccount);
			bankinfo.setPaytype(paytype);
			bankinfo.setDepositMin(depositMax);
			bankinfo.setDepositMax(depositMin);
			bankinfoDao.saveorupdate(bankinfo);
		}else{
			Bankinfo t = new Bankinfo();
			t.setBankname(bankname);
			t.setRemark(remark);
			t.setType(type);
			t.setUsername(username);
			t.setVpnname(vpnname);
			t.setVpnpassword(vpnpassword);
			if(StringUtils.isNotBlank(bankcard) && !bankcard.contains("*")){
				t.setBankcard(bankcard);
			}
			t.setUsb(usb);
			t.setRealname(realname);
			t.setAccountno(accountno);
			t.setIsshow(0);
			if(StringUtils.isBlank(loginname)){
				t.setLoginname(null);
			}
			t.setBanktype(bankInfoType);
			t.setPassword(password);
			t.setRemoteip(remoteip);
			t.setIsactive(0);
			
			t.setSamebank(samebank);
			t.setCrossbank(crossbank);
			t.setTransfermoney(transfermoney);
			t.setAutopay(autopay);
			t.setFee(fee);
			t.setTransferswitch(0);
			t.setSamebankswitch(0);
			t.setZfbImgCode(zfbImgCode);
			t.setScanAccount(scanAccount);
			t.setPaytype(paytype); 
			t.setDepositMin(depositMax);
			t.setDepositMax(depositMin);
			bankinfoDao.saveorupdate(t);
		}
		return null;
	}
	
	public Boolean updateUserrole(String userrole,Integer id){
		 return universalDao.updateUserrole(userrole,id);
	}
	
	public Boolean updateIsshow(String operator,Integer isshow,Integer id,String bankinfoUsername){
		if(isshow==1){
			logDao.insertOperationLog(operator, OperationLogType.BANKS_OPERATING,bankinfoUsername+"显示账号");
		}else{
			logDao.insertOperationLog(operator, OperationLogType.BANKS_OPERATING,bankinfoUsername+"隐藏账号");
		}
		return universalDao.updateIsshow(isshow,id);
	}
	
	public Boolean updateIstransfer(String operator,Integer istransfer,Integer id,String bankinfoUsername){
		if(istransfer==1){
			logDao.insertOperationLog(operator, OperationLogType.BANKS_OPERATING,bankinfoUsername+"开启转账");
		}else{
			logDao.insertOperationLog(operator, OperationLogType.BANKS_OPERATING,bankinfoUsername+"关闭转账");
		}
		return universalDao.updateIstransfer(istransfer,id);
	}
	
	@Override
	public int getBankinfoCount(String username,Integer type,String bankname) throws Exception {
		return bankinfoDao.getCount(username,type,bankname);
	}
	@Override
	public List getAllBankinfo(String username,Integer type,String bankname,int pageNumber, int rowCount) throws Exception {
		int offset=(pageNumber-1)*rowCount;
		
		try {
			List list = bankinfoDao.findAll(username,type,bankname,offset, rowCount);
			return list;
		} catch (Exception e) {
			throw e;
		}
	}
	@Override
	public HibernateTemplate getHibernateTemplate() {
		return universalDao.getHibernateTemplate();
	}
	@Override
	public String cancle(String operator,Integer id,Integer useable) {
		Bankinfo bankinfo = (Bankinfo)universalDao.get(Bankinfo.class, id);
		bankinfo.setAutopay(0);//禁用的开启后，关闭自动付款
		bankinfo.setUseable(useable);
		if(bankinfo.getType()==1){
			bankinfo.setUserrole("");
		}
		if(useable==-1){
			bankinfo.setUserrole("");
			bankinfo.setRemark(bankinfo.getRemark()+":作废人员"+operator+":时间"+dfh.utils.DateUtil.now());
			bankinfo.setUpdatetime(dfh.utils.DateUtil.now());
		}
		universalDao.getHibernateTemplate().update(bankinfo);
		if(useable==-1){
			logDao.insertOperationLog(operator, OperationLogType.BANKS_OPERATING,bankinfo.getUsername()+bankinfo.getBankname()+"废除");
		}else if(useable==1){
			logDao.insertOperationLog(operator, OperationLogType.BANKS_OPERATING,bankinfo.getUsername()+bankinfo.getBankname()+"禁用");
		}else{
			logDao.insertOperationLog(operator, OperationLogType.BANKS_OPERATING,bankinfo.getUsername()+bankinfo.getBankname()+"启用");
		}
		return null;
	}
	
	@Override
	public String switchmsBank(String id,Integer useable) {
		Const cons = (Const)universalDao.get(Const.class, id);
		cons.setValue(useable.toString());
		universalDao.getHibernateTemplate().update(cons);
		return null;
	}
	
	@Override
	public Object get(Class clazz, Serializable id) {
		return universalDao.getHibernateTemplate().get(clazz, id);
	}
	@Override
	public List getBankinfo(Integer type) throws Exception {
		DetachedCriteria dc = DetachedCriteria.forClass(Bankinfo.class);
		dc.add(Restrictions.eq("useable", 0));
		//dc.add(Restrictions.eq("type", type));
		return universalDao.findByCriteria(dc);
	}
	
	@Override
	public List getBusinessBankinfo() throws Exception {
		DetachedCriteria dc = DetachedCriteria.forClass(Bankinfo.class);
		dc.add(Restrictions.eq("useable", 0));
//		dc.add(Restrictions.in("type", new Integer[]{5,6}));
		//dc.add(Restrictions.eq("type", type));
		return universalDao.findByCriteria(dc);
	}
	@Override
	public List getAllBankinfo() throws Exception {
		DetachedCriteria dc = DetachedCriteria.forClass(Bankinfo.class);
		dc.add(Restrictions.eq("useable", 0));
		dc.addOrder(Order.asc("type"));
		List<Bankinfo> list = universalDao.findByCriteria(dc);
		List<Bankinfo> bankinfos = new ArrayList<Bankinfo>();
		Integer flag=0;
		for (int i = 0; i < list.size(); i++) {
			Bankinfo bankinfo = list.get(i);
			Integer type = bankinfo.getType();	
			if(i==0&&type==1){
				bankinfos.add(new Bankinfo("--存款账户--", 0, 0, "", ""));
			}if(i==0&&type==2){
				bankinfos.add(new Bankinfo("--支付账户--", 0, 0, "", ""));
			}
			if(type ==2 && flag ==1){
				bankinfos.add(new Bankinfo("--支付账户--", 0, 0, "", ""));
			}
			flag = bankinfo.getType();
			bankinfos.add(bankinfo);
			
		}
		return bankinfos;
	}
	@Override
	public List getCashinBankinfo() throws Exception {
		DetachedCriteria dc = DetachedCriteria.forClass(Bankinfo.class);
		dc.add(Restrictions.eq("useable", 0));
		//dc.add(Restrictions.ne("type", 1));
		return universalDao.findByCriteria(dc);
	}
	
	@Override
	public String gtDepositAmount(int type, int useable) {
		DetachedCriteria dc = DetachedCriteria.forClass(Bankinfo.class);
		//dc.add(Restrictions.or(Restrictions.eq("type", 1), Restrictions.eq("type", 7)));
		dc.add(Restrictions.eq("type", type));
		dc.add(Restrictions.eq("useable", useable));
		
		dc.add(Restrictions.ge("amount", Double.parseDouble("5000")));
		List<Bankinfo> list = universalDao.findByCriteria(dc);
		StringBuffer sbf = new StringBuffer();
		if(null!=list && !list.isEmpty()){
			for(Bankinfo info :list){
				sbf.append(info.getUsername()+"|");
			}
			return sbf.substring(0, sbf.length()-1);
		}
		return sbf.toString();
	}
	@Override
	public String gtDepositTime(int type, int useable) {
		DetachedCriteria dc = DetachedCriteria.forClass(Bankinfo.class);
		dc.add(Restrictions.eq("type", type));
		dc.add(Restrictions.eq("useable", useable));
		dc.add(Restrictions.isNotNull("updatetime")); 
		//dc.add(Restrictions.ge("amount", Double.parseDouble("5000")));
		//扣除3分钟
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date now = new Date();
		Date afterDate = new Date(now .getTime() - 180000);
		dc.add(Restrictions.lt("updatetime",afterDate));
		List<Bankinfo> list = universalDao.findByCriteria(dc);
		StringBuffer sbf = new StringBuffer();
		if(null!=list && !list.isEmpty()){
			for(Bankinfo info :list){
				sbf.append(info.getUsername()+"|");
			}
			return sbf.substring(0, sbf.length()-1);
		}
		return sbf.toString();
	}
	@Override
	public String gtWithdrawTime(int type, int useable) {
		DetachedCriteria dc = DetachedCriteria.forClass(Bankinfo.class);
		dc.add(Restrictions.or(Restrictions.eq("type", 2), Restrictions.eq("type", 8)));
		dc.add(Restrictions.eq("useable", useable));
		dc.add(Restrictions.isNotNull("updatetime")); 
		//dc.add(Restrictions.ge("amount", Double.parseDouble("5000")));
		//扣除15秒
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date now = new Date();
		Date afterDate = new Date(now .getTime() - 15000);
		dc.add(Restrictions.lt("updatetime",afterDate));
		List<Bankinfo> list = universalDao.findByCriteria(dc);
		StringBuffer sbf = new StringBuffer();
		if(null!=list && !list.isEmpty()){
			for(Bankinfo info :list){
				sbf.append(info.getUsername()+"|");
			}
			return sbf.substring(0, sbf.length()-1);
		}
		return sbf.toString();
	}
	public LogDao getLogDao() {
		return logDao;
	}
	public void setLogDao(LogDao logDao) {
		this.logDao = logDao;
	}
	
	@Override
	public String updatePayMerchant(PayMerchant payMer) {
		universalDao.saveOrUpdate(payMer);
		return null;
	}
	@Override
	public String savePayMerchant(PayMerchant payMer) {
		String a=universalDao.save(payMer)+"";
		return a;
	}
	@Override
	public void deletePayMerchant(Class a,int id) {
		universalDao.delete(a, id+"");
	}
	@Override
	public String updateBankInfo(Bankinfo bankinfo) {
		universalDao.update(bankinfo);
		return null;
	}
	@Override
	public void updateBankSwitch(Integer id, String type) {
		Bankinfo bank = (Bankinfo) universalDao.get(Bankinfo.class, id);
		if(null != bank){
			if(type.equals("transferswitch")){
				if(bank.getTransferswitch()==0){
					bank.setTransferswitch(1);
				}else{
					bank.setTransferswitch(0);
				}
			}else if(type.equals("samebankswitch")){
				if(bank.getSamebankswitch()==0){
					bank.setSamebankswitch(1);
				}else if(bank.getSamebankswitch()==1){
					bank.setSamebankswitch(0);
				}
			}
		}
		
	}
	
	
	


	private static MifResponseVo message = new MifResponseVo();
	private static ObjectMapper mapper = new ObjectMapper();
	@Override
	public void updateBankBalance() {
		try {
            DetachedCriteria dc1 = DetachedCriteria.forClass(Bankinfo.class);
            //dc1.add(Restrictions.in("type", new String[]{"1","2","8"}));
            dc1.add(Restrictions.in("type", new Integer[]{1}));
            dc1.add(Restrictions.eq("useable", 0));
            dc1.add(Restrictions.ne("paytype", 7));
            List<Bankinfo> li =universalDao.findByCriteria(dc1);
            Map maps = null;
            List list = new ArrayList<>();
            for(int n=0;n<li.size();n++){
            	 maps = new HashMap();
                 maps.put("card_number", li.get(n).getBankcard());
                 maps.put("data_sign", "z5metd8t1bct99kqpbv05d3ccyzkrm5m");
                 list.add(maps);
            }
            
            System.out.println("本次更新銀行额度数据条数："+list.size());
            String data = mapper.writeValueAsString(list);
            
            String url = "http://mfb-jiekou02.vip:10215/api/BatchCheckBalance";
            System.out.println("請求參數："+data);
            String result = MyWebUtils.getHttpContentByBtParamToJson(url,data);
            System.out.println("返回參數："+result);
            
			message = mapper.readValue(result, MifResponseVo.class);
			List<MifVo> mifList = null; 
			if (null != message.getData() && !"".equals(message.getData())) {
				mifList = message.getData();
	        }
	        for (int i = 0;i< mifList.size();i++) {
                for(int j=0;j<li.size();j++){
                	Bankinfo bankinfo = new Bankinfo();
                	bankinfo = (Bankinfo)li.get(j);
                	if(bankinfo.getBankcard().equals(mifList.get(i).getCardNumber())){
                		bankinfo.setBankamount(Double.parseDouble(mifList.get(i).getBalance()));
                		if(mifList.get(i).getLastUpdateBalanceTime() != null && !"".equals(mifList.get(i).getLastUpdateBalanceTime())){
                			bankinfo.setUpdatetime(DateUtil.toTimestamp(mifList.get(i).getLastUpdateBalanceTime()));
                		}
                		else{
                			bankinfo.setUpdatetime(DateUtil.getCurrentTimestamp());
                		}
                		//更新
                		bankinfoDao.updateBankAmountSql(bankinfo.getBankamount(),bankinfo.getUpdatetime(), bankinfo.getId());
                		System.out.println("完毕！更新户名："+bankinfo.getUsername()+",更新卡号："+bankinfo.getBankcard()+"，更新远程额度："+mifList.get(i).getBalance());
                	}
                }
             }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * bankinfo:paytype =7 同略云类型
	 * 获取在bankinfo: useable = 0  使用状态的银行卡 通过接口获取银行卡当前余额
	 */
	public void updateTlyBankBalance() {
		try {
            DetachedCriteria dc1 = DetachedCriteria.forClass(Bankinfo.class);
            dc1.add(Restrictions.in("type", new Integer[]{1}));
            dc1.add(Restrictions.eq("useable", 0));
            dc1.add(Restrictions.eq("paytype", 7));
            List<Bankinfo> li =universalDao.findByCriteria(dc1);

            System.out.println("本次更新同略云銀行额度数据条数："+li.size());
      			
                for(int j=0;j<li.size();j++){
                	Bankinfo bankinfo = new Bankinfo();
                	bankinfo = (Bankinfo)li.get(j);
                	JSONObject json= TlyDepositUtil.query_bankcard(bankinfo.getBankcard(), TlyBankFlagEnum.getCode(bankinfo.getBankname()));
                	if (json.getBoolean("success")) {
                		Double amount = json.getDouble("balance");
                	 	if(bankinfo.getBankamount()!=amount){
                	 		
                	 		bankinfo.setUpdatetime(DateUtil.getCurrentTimestamp());
                	 		bankinfo.setBankamount(amount);
                    		bankinfoDao.updateBankAmountSql(bankinfo.getBankamount(),bankinfo.getUpdatetime(), bankinfo.getId());

                	 	}
   					}
                }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
