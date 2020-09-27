package com.nnti.personal.dao.slave;

import com.nnti.personal.model.vo.ExperienceSms;

public interface ISlaveExperienceSmsDao {

	ExperienceSms findExperienceSmsByPhone(ExperienceSms experienceSms);
}