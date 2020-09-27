package app.service.interfaces;

import java.util.List;
import dfh.model.Const;

public interface IConstService {

	List<Const> queryConstList();
	
	String queryConstValue(String id);
	
}