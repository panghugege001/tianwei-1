package dfh.exception;

public class BusinessException extends BaseException {
    private static final long serialVersionUID = -3246861528766887817L;
    public static final int status = 400;
    public static final String code = "400";

    public BusinessException() {
        super("业务异常!");
    }

    public BusinessException(String msg) {
        super(400, "400", msg);
    }

    public BusinessException(String msg, Object[] args) {
        super(400, "400", msg, args);
    }

    public BusinessException(String code, String msg, Object[] args) {
        super(400, code, msg, args);
    }
}
