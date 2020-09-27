package app.service.interfaces;

import java.util.List;
import app.model.vo.SlotMachineBigBangVO;

public interface ISlotMachineBigBangService {

	List<SlotMachineBigBangVO> querySlotMachineBigBangList(SlotMachineBigBangVO vo);
	
}