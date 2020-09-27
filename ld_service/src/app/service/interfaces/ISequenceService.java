package app.service.interfaces;

public interface ISequenceService {

	String generateUserId();
	
	String generateProposalNo(Integer code);
	
	String generateTransferId();
	
	String generateYhjId();
	
}