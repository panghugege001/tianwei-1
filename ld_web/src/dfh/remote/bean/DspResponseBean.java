package dfh.remote.bean;

public class DspResponseBean {
	private String info;

	public DspResponseBean(String info) {
		super();
		this.info = info;
	}
	
	public DspResponseBean(){
		
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}
	
	@Override
	public String toString() {
		return "DspResponseBean [info=" + info + "]";
	}
}
