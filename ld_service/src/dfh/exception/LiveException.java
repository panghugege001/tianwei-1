package dfh.exception;

/**
 * 真人业务异常
 */
public class LiveException extends LiveBaseException {

	private static final long serialVersionUID = 4788874415683008303L;

	public LiveException(String code, String desc,String api, Object params, Object response) {
       super(code, desc, "操作异常，请联系客服人员。"+code, api, params, response);
    }


}
