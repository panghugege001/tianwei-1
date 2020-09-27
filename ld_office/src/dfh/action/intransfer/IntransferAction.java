package dfh.action.intransfer;

import java.util.Map;
import java.util.ResourceBundle;

import dfh.utils.GsonUtil;
import dfh.utils.MyWebUtils;
import org.apache.log4j.Logger;

import dfh.action.SubActionSupport;
import com.nnti.office.model.auth.Operator;
import dfh.service.interfaces.IIntransferService;
import dfh.utils.Constants;

public class IntransferAction extends SubActionSupport{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3947787158429846206L;
	private Logger log=Logger.getLogger(IntransferAction.class);
	private Integer id;
	private String from;
	private String to;
	private Double amount;
	private Double fee;
	private String remark;
	private Integer transferflag;
	private String redirect_url;
	private String errormsg;
	
	private IIntransferService intransferService;
	
	private static ResourceBundle rb = ResourceBundle.getBundle("config");

	public String pay_intransfer() {
		Operator cus = (Operator) getHttpSession().getAttribute(Constants.SESSION_OPERATORID);
		if (cus != null) {
			getRequest().setAttribute("loginname", cus.getUsername());
		}
		return INPUT;
	}

	public String intransfer_tree() {
		String result = "";
		Operator cus = (Operator) getHttpSession().getAttribute(Constants.SESSION_OPERATORID);
		if (cus != null) {
			String url = rb.getString("middleservice.url.pay") + "bank/intransfer_tree";
			try {
				result = MyWebUtils.getHttpContentByParam(url, null);
				log.info("接收结果：" + result);
			} catch (Exception e) {
				log.error("调用接口异常：", e);
			}
		}
		GsonUtil.GsonObject(result);
		return null;
	}
	
	public String  addintransfer() {
		
		if(from==null||from.equals("0")){
			setErrormsg("转出账户不可为空");
			return INPUT;
		}
		if(to==null||to.equals("0")){
			setErrormsg("转入账户不可为空");
			return INPUT;
		}		
		if(amount==null||amount<=0){
			setErrormsg("转账金额不可为空或者小于等于0");
			return INPUT;
		}
		if(fee==null){
			setErrormsg("转账手续费不可为空");
			return INPUT;
		}
		
		try {
			intransferService.addIntransfer(from, to,getOperatorLoginname(), amount,fee, remark,transferflag);
			setErrormsg("提交成功");
			return INPUT;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return INPUT;
	}

	public String intransfer_save() {
		String result = "";
		Operator cus = (Operator) getHttpSession().getAttribute(Constants.SESSION_OPERATORID);
		if (cus != null) {
			String url = rb.getString("middleservice.url.pay") + "bank/save";
			try {
				Map param = getRequest().getParameterMap();
				result = MyWebUtils.getHttpContentByParam(url, MyWebUtils.getListNamevaluepair(param));
				log.info("接收结果：" + result);
			} catch (Exception e) {
				log.error("调用接口异常：", e);
			}
		}
		GsonUtil.GsonObject(result);
		return null;
	}


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getErrormsg() {
		return errormsg;
	}

	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
	}

	public IIntransferService getIntransferService() {
		return intransferService;
	}

	public void setIntransferService(IIntransferService intransferService) {
		this.intransferService = intransferService;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Double getAmount() {
		return amount;
	}

	public Double getFee() {
		return fee;
	}

	public void setFee(Double fee) {
		this.fee = fee;
	}


	public Integer getTransferflag() {
		return transferflag;
	}

	public void setTransferflag(Integer transferflag) {
		this.transferflag = transferflag;
	}
	
	public String getRedirect_url() {
		return redirect_url;
	}

	public void setRedirect_url(String redirect_url) {
		this.redirect_url = redirect_url;
	}

	
}
