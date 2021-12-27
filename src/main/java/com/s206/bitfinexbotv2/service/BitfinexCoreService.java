package com.s206.bitfinexbotv2.service;

import com.s206.bitfinexbotv2.util.ConnectionUtil;
import com.s206.bitfinexbotv2.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;

public class BitfinexCoreService {

	@Autowired
	private ConnectionUtil connectionUtil;
	@Autowired
	private SecurityUtil securityUtil;

	@Value("${properties.bitfinex.domain}")
	private String domain;


	public Map<String, String> sendAuthEndPointRequest(String apiPath, String apiKey, String apiSecret, String requestBody){

		String url = domain + apiPath;

		String nonce = Long.toString(System.currentTimeMillis() * 1000);
		String sigPlainText = "/api/" + apiPath + nonce;

		String sigHashText = securityUtil.HmacSHA384(sigPlainText, apiSecret);
		Map<String, String> headerList = new HashMap<>();
		headerList.put("bfx-nonce", nonce);
		headerList.put("bfx-apikey", apiKey);
		headerList.put("bfx-signature", sigHashText);

		Map<String, String> response = connectionUtil.sendPostRequest(url, headerList, requestBody);

		return response;
	}

}
