package dfh.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.hibernate.LockMode;

import dfh.model.Proposal;
import dfh.model.enums.ProposalFlagType;
import dfh.service.implementations.AbstractBatchGameinfoServiceImpl;
import dfh.service.implementations.ProposalServiceImpl;
import dfh.service.interfaces.ProposalService;
import dfh.utils.Axis1Util;

public class SelectJobService {
	/**
	 * 网络连接检查
	 * @param string 
	 * @return Boolean
	 */
	private static final String URL="http://192.168.0.169:8080/msWS/services/msBankService?wsdl";
	
	
	
	public  Boolean getConnection(String string) {
		InputStream is = null;
		try {
			URL url = new URL(string);
			is = url.openStream();
			return true;
		} catch (Exception e) {
			return false;
		}finally{
			try {
				is.close();
			} catch (IOException e) {
				//e.printStackTrace();
				return false;
			}
		}
	}
	//flag,orderNum,createTime,amount,bankaccount,accountNo,bankCode,accountName
	/**
	 *  flag:标识该笔订单状态：分为成功（2）、失败(3、5、6)、未知（4）。
		状态位：2:标识交易成功  4:标识结果未知 3:标识客户校验失败 5:民生系统维护中 6：一个帐号登录多次
		orderNum：标识企业订单号
		createTime：订单创建时间
		amount：转款金额
		bankaccout：付款人姓名
		accoutNo：收款人帐号
		bankCode：银行代码
		accoutName：收款人姓名
	 */
	public synchronized  void processStatusData() {
		try{
			if(getConnection(URL)){
				
				String str = (String)Axis1Util.getAxisCall_SelectOrderStatus().invoke(new Object[]{});
				if(str!=null && !"".equals(str) && !"3".equals(str)){
					String[] sData = str.split(";");//每一条数据
					System.out.println(str);
					for(String s:sData){
						String []inData = s.split(",");//每一个字段值
						//再对该笔订单进行操作6：余额不足，7：登录多次，3：客户校验失败，5：系统维护，
					//	DBUtil db = DBUtil.getInstanceDBUtil();
						ProposalService proposalService =(ProposalServiceImpl)SpringFactoryHepler.getInstance("proposalService");
						if("2".equals(inData[0])){
							//System.out.println("交易成功,扣除总金额");
							System.out.println(inData[1]+","+inData[3]);//进行额度扣除
							
							String msg= proposalService.executemscashoutproposal(inData[1], "20");
							
							//对自己数据库的相应订单进行操作
							//拿到数据库中的信息进行操作
							//db.update("update orderinfo set flag = 2 where orderNum = '"+inData[1]+"'");
							//db.update("update userbankinfo set amount = amount - "+inData[3]);
							System.out.println(inData[1]+"交易成功");
						}else if("3".equals(inData[0])){
							System.out.println("客户校验失败");//取消订单相当于失败订单，什么情况下取消订单，基本上是客户校验失败
							System.out.println(inData[1]+","+inData[3]);
							
							String msg= proposalService.canclemscashoutproposal(inData[1], "system", "127.0.0.1", "客户校验失败",3);
							
							/**
							 * 将flag标识为3，
							 */
							//db.update("update orderinfo set flag = 3 where orderNum = '"+inData[1]+"'");
						}else if("4".equals(inData[0])){
							System.out.println("结果未知");
							//对本地数据库中进行某个字段标识
							String msg= proposalService.canclemscashoutproposal(inData[1], "system", "127.0.0.1", "",4);
							System.out.println(inData[1]+","+inData[3]);
							/**
							 * 将flag标识为4
							 * 进行手工查账，后台有三种操作方式：确认转账、重新转账、取消转账
							 */
							//db.update("update orderinfo set flag = 4 where orderNum = '"+inData[1]+"'");
						}else if("5".equals(inData[0])){
							System.out.println("民生系统维护");
							String msg= proposalService.canclemscashoutproposal(inData[1], "system", "127.0.0.1", "民生系统维护",3);
							/**
							 * 将flag标识为5，
							 */
							//db.update("update orderinfo set flag = 5 where orderNum = '"+inData[1]+"'");
						}else if("6".equals(inData[0])){
							
							String msg= proposalService.canclemscashoutproposal(inData[1], "system", "127.0.0.1", "",3);
							/**
							 * 将flag标识为6，
							 */
							System.out.println("余额不足");
							//db.update("update orderinfo set flag = 6 where orderNum = '"+inData[1]+"'");
						}else if("7".equals(inData[0])){
							String msg= proposalService.canclemscashoutproposal(inData[1], "system", "127.0.0.1", "登录多次",3);
							/**
							 * 将flag标识为7，
							 */
							System.out.println("登录多次");
							//db.update("update orderinfo set flag = 7 where orderNum = '"+inData[1]+"'");
						}
						//标识为已处理
						Integer i = (Integer)Axis1Util.getAxisCall_processToOrdered().invoke(new Object[]{inData[1]});	
					}
				}
			}else{
				System.out.println("远程服务器异常！");
			}
		}catch(Exception e){
			//e.printStackTrace();
		}
	}
}
