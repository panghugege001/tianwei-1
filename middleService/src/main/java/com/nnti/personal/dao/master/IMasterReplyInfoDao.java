package com.nnti.personal.dao.master;

import com.nnti.personal.model.vo.ReplyInfo;

public interface IMasterReplyInfoDao {

	int delete(Integer topicId);

	int insert(ReplyInfo replyInfo);
}