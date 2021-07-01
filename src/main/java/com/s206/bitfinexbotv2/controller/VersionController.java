package com.s206.bitfinexbotv2.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.s206.bitfinexbotv2.dto.BitfinexActiveOrderDto;
import com.s206.bitfinexbotv2.dto.BitfinexWalletDto;
import com.s206.bitfinexbotv2.scheduler.BitfinexScheduler;
import com.s206.bitfinexbotv2.service.AmountService;
import com.s206.bitfinexbotv2.service.MarginFundingService;
import com.s206.bitfinexbotv2.util.ConnectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class VersionController {


	@RequestMapping(value = "version", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> version() throws Exception {

		return new ResponseEntity<>("1.0.0", HttpStatus.OK);
	}

}
