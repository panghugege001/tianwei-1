package dfh.model.notdb;

import java.util.ArrayList;
import java.util.List;

public class BankVo {
	private Integer id;
	private String name;
	private Integer type;
	
	private List<BankVo> item = new ArrayList<BankVo>();
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public List<BankVo> getItem() {
		return item;
	}
	public void setItem(List<BankVo> item) {
		this.item = item;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	 
}
