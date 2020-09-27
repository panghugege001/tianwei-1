package dfh.action.vo;

import java.util.ArrayList;
import java.util.List;

public class Messages {

	private Integer count;
	private Integer pageNo;
	private Integer pageSize;
	private List<MessageVo> msgList = new ArrayList<MessageVo>();
	
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public Integer getPageNo() {
		return pageNo;
	}
	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public List<MessageVo> getMsgList() {
		return msgList;
	}
	public void setMsgList(List<MessageVo> msgList) {
		this.msgList = msgList;
	}

}
