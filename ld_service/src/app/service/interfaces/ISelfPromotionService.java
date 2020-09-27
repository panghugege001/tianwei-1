package app.service.interfaces;

import java.util.List;
import app.model.vo.SelfPromotionVO;

public interface ISelfPromotionService {

	List<SelfPromotionVO> querySelfPromotionList(SelfPromotionVO vo);
	
	String checkUpgrade(SelfPromotionVO vo);
	
}