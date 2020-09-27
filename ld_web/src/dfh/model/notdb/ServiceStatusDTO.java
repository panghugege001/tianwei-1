package dfh.model.notdb;

import java.util.List;

import dfh.model.bean.ServiceStatus;


public class ServiceStatusDTO {

	private List<ServiceStatus> status;

	public List<ServiceStatus> getStatus() {
		return status;
	}

	public void setStatus(List<ServiceStatus> status) {
		this.status = status;
	}

}