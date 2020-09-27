package dfh.extend.zookeeper;

public enum NodeEnum {

	USER_LOCK_FOR_UPDATE("ULFU");

	private NodeEnum(String nodepath) {
		this.nodepath = nodepath;
	}

	private String nodepath;

	public String getNodepath() {
		return nodepath;
	}

	public void setNodepath(String nodepath) {
		this.nodepath = nodepath;
	}
}