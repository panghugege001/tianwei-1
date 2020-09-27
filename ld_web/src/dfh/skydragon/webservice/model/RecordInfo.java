package dfh.skydragon.webservice.model;

import java.io.Serializable;
import java.util.List;

import dfh.model.Creditlogs;

public class RecordInfo implements Serializable{

	private static final long serialVersionUID = -4974353535832983619L;
	/**
	 * 
	 */
	private List<Creditlogs> dataList;
	private Integer length;
	public List<Creditlogs> getDataList() {
		return dataList;
	}
	public void setDataList(List<Creditlogs> dataList) {
		this.dataList = dataList;
	}
	public Integer getLength() {
		return length;
	}
	public void setLength(Integer length) {
		this.length = length;
	}
}
