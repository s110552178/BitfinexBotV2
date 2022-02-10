package com.s206.bitfinexbotv2.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.s206.bitfinexbotv2.dto.BitfinexActiveOrderDto;
import com.s206.bitfinexbotv2.service.CalculateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.Timestamp;

@RestController
public class VersionController {

	@Autowired
	private CalculateService calculateService;


	@RequestMapping(value = "version", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> version() throws Exception {

		return new ResponseEntity<>("1.0.0", HttpStatus.OK);
	}

}
