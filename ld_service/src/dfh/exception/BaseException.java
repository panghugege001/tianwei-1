package dfh.exception;

public class BaseException extends Exception {
    private static final long serialVersionUID = 1L;
    public static final int DEFAULT_STATUS = 500;
    public static final String DEFAULT_CODE = "system_error";
    public static final String DEFAULT_MESSAGE = "网络连接超时";
    protected int status;
    protected String code;
    protected String message;
    protected Object[] args;

    public BaseException() {
        this(500, "system_error", "网络连接超时", (Object[])null);
    }

    public BaseException(String message) {
        this("system_error", message, (Object[])null);
    }

    public BaseException(String message, Object[] args) {
        this("system_error", message, args);
    }

    public BaseException(String code, String message) {
        this(500, code, message, (Object[])null);
    }

    public BaseException(String code, String message, Object[] args) {
        this(500, code, message, args);
    }

    public BaseException(int status, String code) {
        this(status, code, "网络连接超时", (Object[])null);
    }

    public BaseException(int status, String code, String message) {
        this(status, code, message, (Object[])null);
    }

    public BaseException(int status, String code, String message, Object[] args) {
        super(message);
        this.status = 500;
        this.code = "system_error";
        this.message = "网络连接超时";
        this.status = status;
        this.code = code;
        this.message = message;
        this.args = args;
    }

    public String getCode() {
        return this.code;
    }

    public int getStatus() {
        return this.status;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object[] getArgs() {
        return this.args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }
}
