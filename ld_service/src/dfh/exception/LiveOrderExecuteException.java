package dfh.exception;

public class LiveOrderExecuteException extends LiveHttpException{

	private static final long serialVersionUID = 6669295512400328227L;

	public LiveOrderExecuteException(String code, String api, Object params, Object response) {
       super(code, "订单正在处理中"+code,"订单正在处理中，请联系客服人员确认。", api, params, response);
    }
}
