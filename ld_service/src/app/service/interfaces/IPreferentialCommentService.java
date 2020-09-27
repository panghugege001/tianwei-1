package app.service.interfaces;

import java.util.List;
import app.model.po.PreferentialComment;
import app.model.vo.PreferentialCommentVO;

public interface IPreferentialCommentService {

	List<PreferentialComment> queryPreferentialCommentList(PreferentialCommentVO vo);
	
	String savePreferentialComment(PreferentialComment preferentialComment);
	
	Integer countPreferentialComment(PreferentialCommentVO vo);
}