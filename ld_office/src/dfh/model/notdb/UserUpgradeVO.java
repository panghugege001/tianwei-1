package dfh.model.notdb;

public class UserUpgradeVO {

	private Integer level;
	private Integer warnflag;
	
	public UserUpgradeVO() {}
	
	public UserUpgradeVO(Integer level, Integer warnflag) {
		this.level = level;
		this.warnflag = warnflag;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getWarnflag() {
		return warnflag;
	}

	public void setWarnflag(Integer warnflag) {
		this.warnflag = warnflag;
	}
	
}
