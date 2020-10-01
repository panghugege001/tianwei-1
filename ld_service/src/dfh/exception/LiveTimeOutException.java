package dfh.exception;

public class LiveTimeOutException extends LiveHttpException{
	private static final long serialVersionUID = -2869204597540457035L;

	public LiveTimeOutException(String desc, String api, Object params) {
        super(TIME_OUT, desc, "网络连接超时，请稍后在试。" + TIME_OUT, api, params, null);
    }
}
