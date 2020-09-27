package app.service.interfaces;

import app.model.vo.AgentAccountListVO;
import dfh.utils.Page;

public interface IAgentAccountListService {

	// 查询额度记录信息
	Page queryCreditLogList(AgentAccountListVO vo);
	
	// 查询下线提案信息
	Page queryUnderLineProposalList(AgentAccountListVO vo);
	
	// 查询平台输赢信息
	Page queryPlatformLoseWinList(AgentAccountListVO vo);
	
	// 查询实时输赢信息
	Page queryRealTimeLoseWinList(AgentAccountListVO vo);
	
	// 查询下线会员信息
	Page queryUnderLineMemberList(AgentAccountListVO vo);
}