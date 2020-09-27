package app.request;

import java.util.List;

/**
 * 
 * @author stan
 *
 */
public class BatchPakRequestBean extends BaseBean {

	private List<PakRequestDataBean> data;

	public List<PakRequestDataBean> getData() {
		return data;
	}

	public void setData(List<PakRequestDataBean> data) {
		this.data = data;
	}


}