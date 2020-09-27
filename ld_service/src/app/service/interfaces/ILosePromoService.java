package app.service.interfaces;

import app.model.vo.LosePromoVO;
import dfh.utils.Page;

public interface ILosePromoService {

	Page queryLosePromoPageList(LosePromoVO vo);
	
}
