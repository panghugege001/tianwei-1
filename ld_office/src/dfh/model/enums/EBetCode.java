package dfh.model.enums;

/**
 * ebet响应码
 *
 */
public enum EBetCode {

    success(10000, "成功"),
	noAccount(10001, "账号不存在"),
	keyError(10003, "签名错误"),
	addUserFail(10004, "账号已被注册"),
	apiError(44444, "EBet接口错误"),
	accountFrozen(10005, "账户已冻结"),
	paramsIncomplete(10006, "缺失参数"),
	gameCatalogNotExist(10007, "游戏目录不存在"),
	gamePlatformNotExist(10008, "游戏平台不存在"),
	gameMaintenance(10009, "游戏维护中..."),
	apiMaintenance(10010, "EBet平台维护中..."),
	parameterError(10011, "参数错误"),
	repeatTransfer(10012, "已处理过的转账"),
	insufficientCredit(10013, "余额不足"),
	agentNotExist(10014, "代理不存在"),
	transferProgressing(10015, "转账繁忙"),
	transferNotExist(10016, "交易不存在"),
	apiBusy(10017, "服务忙"),
	queryLimit(10018, "查询次数限制"),
	transferFailed(10019, "转账失败"),
	transferCancelled(10020, "交易已取消");


	public static String getText(Integer code) {
		EBetCode[] p = values();
		for (int i = 0; i < p.length; ++i) {
			EBetCode type = p[i];
			if (type.getCode().equals(code))
				return type.getText();
		}
		return null;
	}

	private Integer code;

	private String text;

	private EBetCode(Integer code, String text) {
		this.code = code;
		this.text = text;
	}

	public Integer getCode() {
		return this.code;
	}

	public String getText() {
		return this.text;
	}
}
