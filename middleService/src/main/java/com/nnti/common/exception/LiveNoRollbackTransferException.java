package com.nnti.common.exception;

/**
 * 不回滚事务
 * 例如,额度转换时重复订单时。
 */
public class LiveNoRollbackTransferException extends LiveException{

	private static final long serialVersionUID = 6784365683664151639L;

	public LiveNoRollbackTransferException(String code, String desc, String api, Object params, Object response) {
        super(code,desc, api, params, response);
    }

    public LiveNoRollbackTransferException(LiveException ex) {
        this(ex.getCode(),ex.getDesc(), ex.getApi(), ex.getParams(), ex.getResponse());
    }

}
