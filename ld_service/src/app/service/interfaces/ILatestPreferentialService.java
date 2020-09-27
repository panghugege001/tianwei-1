package app.service.interfaces;

import java.util.List;
import app.model.po.LatestPreferential;
import app.model.vo.LatestPreferentialVO;

public interface ILatestPreferentialService {
	
	List<LatestPreferential> queryLatestPreferentialList(LatestPreferentialVO latestPreferentialVO);
	
	LatestPreferential queryLatestPreferential(LatestPreferentialVO latestPreferentialVO);
	
}
