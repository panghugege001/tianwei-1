package com.nnti.common.service.implementations;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.nnti.common.constants.Constant;
import com.nnti.common.dao.master.IMasterLosePromoDao;
import com.nnti.common.dao.slave.ISlaveLosePromoDao;
import com.nnti.common.model.vo.LosePromo;
import com.nnti.common.page.Page;
import com.nnti.common.page.PageHelper;
import com.nnti.common.service.interfaces.ILosePromoService;

@Service
@Transactional(rollbackFor = Exception.class)
public class LosePromoServiceImpl implements ILosePromoService {

	@Autowired
	private ISlaveLosePromoDao slaveLosePromoDao;
	@Autowired
	private IMasterLosePromoDao masterLosePromoDao;

	public Page<LosePromo> findPageList(Map<String, Object> paramsMap) throws Exception {

		Integer pageIndex = Constant.PAGE_INDEX;
		Integer pageSize = Constant.PAGE_SIZE;

		Object indexObject = paramsMap.get("pageIndex");
		Object sizeObject = paramsMap.get("pageSize");

		if (null != indexObject) {

			pageIndex = Integer.parseInt(String.valueOf(indexObject));
		}

		if (null != sizeObject) {

			pageSize = Integer.parseInt(String.valueOf(sizeObject));
		}

		PageHelper.startPage(pageIndex, pageSize, true);

		return slaveLosePromoDao.findPageList(paramsMap);
	}

	public List<LosePromo> findList(Map<String, Object> paramsMap) throws Exception {

		return slaveLosePromoDao.findList(paramsMap);
	}

	public int update(Map<String, Object> paramsMap) throws Exception {

		return masterLosePromoDao.update(paramsMap);
	}
}