package com.nnti.game.service.implementations;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nnti.common.constants.Constant;
import com.nnti.common.model.vo.User;
import com.nnti.common.security.EncryptionUtil;
import com.nnti.common.service.interfaces.IUserService;
import com.nnti.game.model.dto.UsersDTO;
import com.nnti.game.service.interfaces.IGameCenterService;
import com.nnti.pay.security.lf.StringUtil;


@Service
public class GameCenterServiceImpl implements IGameCenterService {
	@Autowired
	private IUserService userService;
	
	private static Logger log = Logger.getLogger(GameCenterServiceImpl.class);
	
	public UsersDTO loginGameCenter(UsersDTO dto){
		
		if(null!= dto && StringUtil.isNotBlank(dto.getLoginName()))
		{
			String msg = null;
			try {
				User customer = userService.get(dto.getLoginName());
				
				if (customer == null) {
					msg = "该会员不存在";
					log.warn(msg);
				} else if (customer.getFlag().intValue() == Constant.DISABLE.intValue() && customer.getRole().equals(Constant.MONEY_CUSTOMER)) {
					msg = "该会员已经被禁用";
					log.warn(msg);
					customer=null;
				} else if (customer.getFlag().intValue() == Constant.DISABLE.intValue() && customer.getRole().equals(Constant.AGENT)) {
					msg = "代理账号核实中......";
					log.warn(msg);
					customer=null;
				} else if (customer.getFlag().intValue() == 2) {
					msg = "输如错误次数过多，密码被锁!请使用找回密码功能!";
					log.warn(msg);
					customer=null;
				} else if (!customer.getPassword().equals(EncryptionUtil.encryptPassword(dto.getPassword()))) {
					msg = "密码错误";
					log.warn(msg);
					customer=null;
				} else {
					log.info("login dt client game successfully:"+customer.getLoginName());
				}
				dto.setMsg(msg);
				return dto;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return null;
	}
}