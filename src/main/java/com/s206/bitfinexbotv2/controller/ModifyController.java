package com.s206.bitfinexbotv2.controller;

import com.s206.bitfinexbotv2.dto.request.ModifyFundSettingRequest;
import com.s206.bitfinexbotv2.service.ModifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/api/")
public class ModifyController {

	@Autowired
	private ModifyService modifyService;

	@RequestMapping(value = "/fundSetting", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> modifyFundSetting(@RequestBody ModifyFundSettingRequest request){

		modifyService.modifyFundSetting(request);
		return new ResponseEntity<>(null, HttpStatus.OK);
	}


}
