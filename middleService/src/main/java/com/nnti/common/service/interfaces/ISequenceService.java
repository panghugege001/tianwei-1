package com.nnti.common.service.interfaces;

public interface ISequenceService {

	String generateProposalNo(String type) throws Exception;
	
	String generateTransferId() throws Exception;
	
	String generateYhjId() throws Exception;
}