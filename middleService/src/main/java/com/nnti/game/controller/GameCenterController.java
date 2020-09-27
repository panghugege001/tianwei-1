package com.nnti.game.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nnti.common.constants.FunctionCode;
import com.nnti.common.controller.BaseController;
import com.nnti.common.model.dto.DataTransferDTO;
import com.nnti.game.model.dto.UsersDTO;
import com.nnti.game.service.interfaces.IGameCenterService;

@RestController
@RequestMapping("/gameCenter")
public class GameCenterController extends BaseController {

	@Autowired
	private IGameCenterService gameCenterService;

	private static Logger log = Logger.getLogger(GameCenterController.class);

	@RequestMapping(value = "/loginGameCenter", method = { RequestMethod.POST })
	public DataTransferDTO loginGameCenter(@RequestParam(value = "requestData", defaultValue = "") String requestData,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String requestJsonData = (String) request.getAttribute("requestJsonData");

		Gson gson = new GsonBuilder().create();

		UsersDTO dto = gson.fromJson(requestJsonData, UsersDTO.class);

		UsersDTO user = null;

		try {

			user = gameCenterService.loginGameCenter(dto);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (null == user || StringUtils.isNotBlank(dto.getMsg())) {
			log.info("--------loginGameCenter"+user.getMsg());
			failure(FunctionCode.SC_20000_111.getCode(), user.getMsg());
		}

		return success(user);
	}

}