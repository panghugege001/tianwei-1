package dfh.service.implementations;



import org.apache.log4j.Logger;

import dfh.action.vo.NetPayVO;
import dfh.dao.NetPayDao;
import dfh.dao.TradeDao;
import dfh.dao.UserDao;
import dfh.email.template.EmailTemplateHelp;
import dfh.model.Payorder;
import dfh.model.Users;
import dfh.model.Userstatus;
import dfh.model.enums.CreditChangeType;
import dfh.model.enums.PayOrderFlagType;
import dfh.security.EncryptionUtil;
import dfh.service.interfaces.IAutoDeposit;
import dfh.utils.Configuration;
import dfh.utils.Constants;
import dfh.utils.DateUtil;
import dfh.utils.DigestUtil;
import dfh.utils.NetPayParserXML;
import dfh.utils.NumericUtil;
import dfh.utils.SSLMailSender;

public class AutoDeposit implements IAutoDeposit {
	
	private Logger log=Logger.getLogger(AutoDeposit.class);
	private UserDao userDao;
	private NetPayDao netPayDao;
	private TradeDao tradeDao;
	private SSLMailSender mailSender;


	public SSLMailSender getMailSender() {
		return mailSender;
	}

	public void setMailSender(SSLMailSender mailSender) {
		this.mailSender = mailSender;
	}

	public String CallBack(String responseXML,String requestIP) {
		// TODO Auto-generated method stub
		String recvMsg="";
		if (responseXML!=null) {
			NetPayParserXML npp=new NetPayParserXML();
			NetPayVO vo=null;
			try {
				vo = npp.xml2BeanOfNetPay(responseXML);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				log.error("解析支付完成后的xml数据发生异常。", e);
				log.error("支付后响应给Office的xml："+responseXML);
				return null;
			} 
			
			StringBuffer value=new StringBuffer();
			value.append(vo.getInner_serialNo()).append(vo.getPayPlatform_serialNo());
			value.append(vo.getBankOrderNo()).append(vo.getAmount());
			value.append(vo.getPayDate()).append(vo.getInformDate());
			value.append(Configuration.getInstance().getValue("netpay_hidden_value")); // 抽取部分数据 + 不在网络上传输的数据,做数据校验
			String key=Configuration.getInstance().getValue("mackey");
			String mac=new DigestUtil().hmacSign(value.toString(), key);
			if (mac.equalsIgnoreCase(vo.getMac())) {
				try {
					Integer.parseInt(vo.getResponseCode());
				} catch (Exception e) {
					// TODO: handle exception
					log.error("支付完成后，上送响应码有误。响应码为："+vo.getResponseCode(), e);
					return npp.String2XmlOfNetPay("3");
				}
				
				// 验证该笔交易是否已经发生过
				if (tradeDao.get(Payorder.class, vo.getInner_serialNo())==null) {
					// 存储数据
//					netPayDao.save(vo);	// 存储在线支付流水
//					tradeDao.changeCredit(vo.getUsername(), Double.parseDouble(vo.getAmount()), CreditChangeType.NETPAY.getCode(), vo.getInner_serialNo(), vo.getBankName());
//					userDao.setUserCashin(vo.getUsername());
					
					Users users = (Users) userDao.get(Users.class, vo.getUsername());
					
					Payorder payorder=new Payorder();
					payorder.setBillno(vo.getInner_serialNo());
					payorder.setPayPlatform(vo.getPayPlatform_Name());
					payorder.setFlag(vo.getResponseCode().equals("1")?PayOrderFlagType.SUCESS.getCode():PayOrderFlagType.FAIL.getCode());
					payorder.setNewaccount(Constants.FLAG_FALSE);
					payorder.setLoginname(vo.getUsername());
					payorder.setAliasName(users.getAccountName());
					payorder.setMoney(Double.parseDouble(vo.getAmount()));
					payorder.setPhone(users.getPhone());
					payorder.setEmail(users.getEmail());
					payorder.setIp(requestIP);
					payorder.setCreateTime(DateUtil.now());
					payorder.setReturnTime(new java.sql.Timestamp(DateUtil.parseDateForStandard(vo.getPayDate()).getTime()));
					payorder.setMsg("易宝单号:"+vo.getPayPlatform_serialNo());
					tradeDao.save(payorder);
					
					tradeDao.changeCredit(vo.getUsername(), Double.parseDouble(vo.getAmount()), CreditChangeType.NETPAY.getCode(), vo.getInner_serialNo(), "易宝单号:"+vo.getPayPlatform_serialNo());
					
					userDao.setUserCashin(vo.getUsername());
					
					try {
						Userstatus userstatus = (Userstatus)userDao.get(Userstatus.class, users.getLoginname());
						if(null ==userstatus ||null==userstatus.getMailflag() ||userstatus.getMailflag()==1){
							//System.out.println("不要发送邮件");
						}else{
							String html = EmailTemplateHelp.toHTML(Constants.EMAIL_CASHIN_BODY_HTML, new Object[]{users.getLoginname(),NumericUtil.double2String(payorder.getMoney()),DateUtil.formatDateForStandard(payorder.getCreateTime())});
							this.mailSender.sendmail(html, users.getEmail(), "存款通知 - 9win国际娱乐城");
						}	
					} catch (Exception e) {
						// TODO: handle exception
						log.error(e.getMessage(),e);
					}
					
				}
				recvMsg=npp.String2XmlOfNetPay("1");
			}else{
				log.error("mac错误，IP已被记录，备查。IP:"+requestIP+"，请求数据："+responseXML);
			}
			// else
			// 非常数据
		}
		return recvMsg;
	}

	public String Login(String requestXML,String requestIP) {
		// TODO Auto-generated method stub
		String recvMsg="";
		if (requestXML!=null) {
			NetPayParserXML npp=new NetPayParserXML();
			NetPayVO vo=null;
			try {
				vo=npp.xml2BeanOfLogin(requestXML);
			} catch (Exception e) {
				// TODO: handle exception
				log.error("解析登录xml时发生异常。", e);
				log.error("请求的xml："+requestXML);
				return null;
			}
			
			StringBuffer value=new StringBuffer();
			value.append(vo.getUsername()).append(vo.getPassword());
			value.append(Configuration.getInstance().getValue("netpay_hidden_value")); // 加入不在网络上传输的数据
			String key=Configuration.getInstance().getValue("mackey");
			String mac=new DigestUtil().hmacSign(value.toString(), key);
			if (mac.equalsIgnoreCase(vo.getMac())) {
				Object o = userDao.get(Users.class, vo.getUsername());
				if (o==null) {
					recvMsg=npp.String2XmlOfLogin("2", "", "");
				}else{
					Users users=(Users) o;
					if (EncryptionUtil.md5Encrypt(vo.getPassword()).equalsIgnoreCase(users.getPassword())) {
						
						// 增加客户存款次数校验，大于3次的客户才可以使用在线支付功能
						//if (tradeDao.useOnlinePay(users.getLoginname())) {
							// 增加单日存款额度限制 2010,08,27
							// 增加当日总额度限制	  2010,09,01
//							if (tradeDao.amountConfine(users.getLoginname())||tradeDao.amountConfine()) {
							if (tradeDao.amountConfine(users.getLoginname())) {
								recvMsg=npp.String2XmlOfLogin("6", "", "");
							}else{
								recvMsg=npp.String2XmlOfLogin("1", "", users.getAliasName());
							}
							
//						}else{
//							recvMsg=npp.String2XmlOfLogin("5", "", "");
//						}
						
					}else{
						// 密码错误
						recvMsg=npp.String2XmlOfLogin("3", "", "");
					}
				}
				
			}else{
				log.error("mac错误，IP已被记录，备查。IP:"+requestIP+"，请求数据："+requestXML);
			}
				
			// else
			// 非法数据
			
		}
		return recvMsg;
	}
	
	
	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	
	public NetPayDao getNetPayDao() {
		return netPayDao;
	}

	public void setNetPayDao(NetPayDao netPayDao) {
		this.netPayDao = netPayDao;
	}
	
	public TradeDao getTradeDao() {
		return tradeDao;
	}

	public void setTradeDao(TradeDao tradeDao) {
		this.tradeDao = tradeDao;
	}

	@Override
	public String validationAmount(String requestXML, String requestIP) {
		// TODO Auto-generated method stub
		String recvMsg="";
		if (requestXML!=null) {
			NetPayParserXML npp=new NetPayParserXML();
			NetPayVO vo=null;
			try {
				vo=npp.xml2BeanOfLogin(requestXML);
			} catch (Exception e) {
				// TODO: handle exception
				log.error("解析额度验证xml时发生异常。", e);
				log.error("请求的xml："+requestXML);
				return null;
			}
			
			StringBuffer value=new StringBuffer();
			value.append(vo.getUsername());
			value.append(Configuration.getInstance().getValue("netpay_hidden_value")); // 加入不在网络上传输的数据
			String key=Configuration.getInstance().getValue("mackey");
			String mac=new DigestUtil().hmacSign(value.toString(), key);
			if (mac.equalsIgnoreCase(vo.getMac())) {
				Object o = userDao.get(Users.class, vo.getUsername());
				if (o==null) {
					recvMsg=npp.String2XmlOfLogin("2", "", ""); // 用户不存在
				}else{
					if (tradeDao.amountConfine(vo.getUsername())||tradeDao.amountConfine()) {
						recvMsg=npp.String2XmlOfNetPay("0");	// 达到限制额度
					}else{
						recvMsg=npp.String2XmlOfNetPay("1");
					}
				}
			}else{
				log.error("mac错误，IP已被记录，备查。IP:"+requestIP+"，请求数据："+requestIP);
			}
		}
		return recvMsg;
	}


}
