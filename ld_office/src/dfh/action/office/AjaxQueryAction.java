package dfh.action.office;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.RoundingMode;
import java.util.Date;

import dfh.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import dfh.action.SubActionSupport;
import dfh.model.PTTransfer2NewHistory;
import dfh.remote.DocumentParser;
import dfh.remote.bean.KenoResponseBean;
import dfh.service.interfaces.CustomerService;
import dfh.service.interfaces.OperatorService;

public class AjaxQueryAction extends SubActionSupport {

	private static Logger log = Logger.getLogger(AjaxQueryAction.class);
	private OperatorService operatorService;
	private CustomerService customerService;

	private String loginname;
	private Date start;
	private Date end;

	public AjaxQueryAction() {
	}

	public CustomerService getCustomerService() {
		return customerService;
	}

	public Date getEnd() {
		return end;
	}

	public String getLoginname() {
		return loginname;
	}

	public OperatorService getOperatorService() {
		return operatorService;
	}

	public Date getStart() {
		return start;
	}

	public String queryLastXimaTime() {
		String msg = "";
		try {
			msg = customerService.queryUserLastXimaEndTime(loginname);
			getResponse().getWriter().write(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String queryRemoteCredit() {
		String msg = "";
		try {
			Double d = customerService.getRemoteCredit(loginname);
			if(d!=null){
				msg = NumericUtil.formatDouble(d);
			}else{
				msg = "系统繁忙，请稍后尝试";
			}
		} catch (Exception e) {
			e.printStackTrace();
			msg = "系统繁忙，请稍后尝试";
		}
		try {
			getResponse().getWriter().write(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	public String queryKyqpCredit() {
		String msg;
		try {
			msg =  String.valueOf(KYQPUtil.getBalance(loginname,"KYQP",""));
		} catch (Exception e) {
			e.printStackTrace();
			msg = "系统繁忙，请稍后尝试" ;
		}
		try {
			getResponse().getWriter().write(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String queryVRCredit() {
		String msg;
		try {
			msg =  String.valueOf(KYQPUtil.getBalance(loginname,"VR",""));
		} catch (Exception e) {
			e.printStackTrace();
			msg = "系统繁忙，请稍后尝试" ;
		}
		try {
			getResponse().getWriter().write(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	public String queryBbinCredit() {
		String msg;
		try {
			msg =  String.valueOf(BBinUtils.GetBalance(loginname));
		} catch (Exception e) {
			e.printStackTrace();
			msg = "系统繁忙，请稍后尝试" ;
		}
		try {
			getResponse().getWriter().write(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	public String queryFanyaCredit() {
		String msg;
		try {
			msg =  String.valueOf(FanYaUtil.balance(loginname));
		} catch (Exception e) {
			e.printStackTrace();
			msg = "系统繁忙，请稍后尝试" ;
		}
		try {
			getResponse().getWriter().write(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String queryRemoteAgCredit() {
		String msg = "";
		try {
			msg = customerService.getRemoteAgCredit(loginname);
		} catch (Exception e) {
			e.printStackTrace();
			msg = "系统繁忙，请稍后尝试";
		}
		try {
			getResponse().getWriter().write(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String queryRemoteAgInCredit() {
		String msg = "";
		try {
			msg = customerService.getRemoteAgInCredit(loginname);
		} catch (Exception e) {
			e.printStackTrace();
			msg = "系统繁忙，请稍后尝试";
		}
		try {
			getResponse().getWriter().write(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String queryRemoteSbCredit() {
		String msg = "";
		try {
			msg = customerService.getRemoteSbCredit(loginname);
		} catch (Exception e) {
			e.printStackTrace();
			msg = "系统繁忙，请稍后尝试";
		}
		try {
			getResponse().getWriter().write(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String queryRemoteBbinCredit() {
		String msg = "";
		try {
			msg = customerService.getRemoteBbinCredit(loginname);
			if(msg==null){
				msg = "系统繁忙，请稍后尝试";
			}
		} catch (Exception e) {
			e.printStackTrace();
			msg = "系统繁忙，请稍后尝试";
		}
		try {
			getResponse().getWriter().write(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String queryRemoteKenoCredit() {
		String msg = "";
		try {
			msg = customerService.getRemoteKenoCredit(loginname);
			if(msg==null){
				msg = "系统繁忙，请稍后尝试";
			}
		} catch (Exception e) {
			e.printStackTrace();
			msg = "系统繁忙，请稍后尝试";
		}
		try {
			getResponse().getWriter().write(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String queryRemoteKenoCredit2() {
		String msg = "";
		try {
			KenoResponseBean bean = DocumentParser.parseKenocheckcreditResponseRequest(Keno2Util.checkcredit(loginname));
			if (bean != null) {
				if (bean.getName() != null && bean.getName().equals("Credit")) {
					msg = bean.getAmount() + "";
				} else if (bean.getName() != null && bean.getName().equals("Error")) {
					msg = bean.getValue();
				} else {
					msg = "系统繁忙";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			msg = "系统繁忙，请稍后尝试";
		}
		try {
			getResponse().getWriter().write(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String queryRemoteSixLotteryCredit() {
		String msg = "";
		try {
			String balanceStr = SixLotteryUtil.balance(loginname) ;
			String flag = SixLotteryUtil.compileVerifyData("<status>(.*?)</status>", balanceStr) ;
			
			if (StringUtils.isNotBlank(balanceStr)) {
				if (flag.equals("0")) {
					msg = SixLotteryUtil.compileVerifyData("<balance>(.*?)</balance>", balanceStr) ;
				} else if (flag.equals("1")) {
					msg = SixLotteryUtil.compileVerifyData("<error>(.*?)</error>", balanceStr) ;
				} else {
					msg = "系统繁忙";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			msg = "系统繁忙，请稍后尝试";
		}
		try {
			getResponse().getWriter().write(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String queryRemoteEBetAppCredit() {
		String msg;
		try {
			msg =  EBetAppUtil.getBalance(loginname).toString() ;
		} catch (IllegalArgumentException iae){
			String prefix = "EBetApp查询错误 - ";
			if("用户不存在".equals(iae.getMessage())){
				msg = prefix + "用户未登入EBetApp";
			}else{
				msg = prefix + iae.getMessage();
			}
		} catch (Exception e) {
			e.printStackTrace();
			msg = "系统繁忙，请稍后尝试" ;
		}
		try {
			getResponse().getWriter().write(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String querySbaCredit() {
		PrintWriter out = null;
		String msg;
		try {
			out = this.getResponse().getWriter();
			msg =  String.valueOf(ShaBaUtils.CheckUserBalance(loginname));
			if(msg == null){
				out.println("获取余额异常，请联系技术解决！");
			}else{
				out.println(msg);
			}
		}catch (Exception e) {
			out.println("系统繁忙，请稍后尝试！");
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.close();
			}
			out.flush();
		}
		return null;
	}

	public String queryMWGCredit() {
		PrintWriter out = null;
		String msg;
		try {
			out = this.getResponse().getWriter();
			msg =  String.valueOf(MWGUtils.getUserBalance(loginname));
			if(msg == null){
				out.println("获取余额异常，请联系技术解决！");
			}else{
				out.println(msg);
			}
		}catch (Exception e) {
			out.println("系统繁忙，请稍后尝试！");
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.close();
			}
			out.flush();
		}
		return null;
	}

	public String getNTwoBalance() {
		String msg;
		try {
			msg =  String.valueOf(NTwoUtil.checkClient(loginname).getBalance().setScale(2, RoundingMode.HALF_UP));
		} catch (Exception e) {
			e.printStackTrace();
			msg = "系统繁忙，请稍后尝试" ;
		}
		try {
			getResponse().getWriter().write(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String queryChessCredit() {
		String msg;
		try {
			msg =  String.valueOf(ChessUtil.getBalance(loginname));
		} catch (Exception e) {
			e.printStackTrace();
			msg = "系统繁忙，请稍后尝试" ;
		}
		try {
			getResponse().getWriter().write(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String queryValidBetAmount() {
		String msg = "";
		try {
			Double d = customerService.queryValidBetAmount(loginname, start, end);
			if(d!=null){
				msg = NumericUtil.formatDouble(d);
			}
			getResponse().getWriter().write(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String queryPTDetail() {
		String msg;
		try {
			
			PTTransfer2NewHistory pHistory=(PTTransfer2NewHistory) customerService.get(PTTransfer2NewHistory.class, "E"+loginname);
			if (pHistory==null) 
				msg="数据不存在";
			else
				msg = "创建时间  :"+pHistory.getCreateTime()+"~用户旧pt余额  :"+ pHistory.getMoney() ;
				
		} catch (Exception e) {
			e.printStackTrace();
			msg = "系统繁忙，请稍后尝试";
		}
		try {
			getResponse().getWriter().write(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	public void setOperatorService(OperatorService operatorService) {
		this.operatorService = operatorService;
	}

	public void setStart(Date start) {
		this.start = start;
	}

}
