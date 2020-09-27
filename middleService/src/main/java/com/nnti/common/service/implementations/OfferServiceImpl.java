package com.nnti.common.service.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.nnti.common.dao.master.IMasterOfferDao;
import com.nnti.common.model.vo.Offer;
import com.nnti.common.service.interfaces.IOfferService;

@Service
public class OfferServiceImpl implements IOfferService {

	@Autowired
	private IMasterOfferDao masterOfferDao;
	
	public int create(Offer offer) throws Exception {
		
		return masterOfferDao.create(offer);
	}
}