package dfh.model;

import java.util.List;


/**
 * Created by Addis on 2017/10/12.
 */
public class MifResponseVo {

    private String success;

    private String error_message;

    private List<MifVo> data;
    
	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	public String getError_message() {
		return error_message;
	}

	public void setError_message(String error_message) {
		this.error_message = error_message;
	}

	public List<MifVo> getData() {
		return data;
	}

	public void setData(List<MifVo> data) {
		this.data = data;
	}





    
}
