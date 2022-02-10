package com.s206.bitfinexbotv2.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.s206.bitfinexbotv2.dto.BitfinexActiveOrderDto;
import com.s206.bitfinexbotv2.util.ConnectionUtil;
import com.s206.bitfinexbotv2.util.SecurityUtil;
import com.s206.bitfinexbotv2.util.TelegramNotificationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.ConnectException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MarginFundingService {

	@Autowired
	private TelegramNotificationUtil telegramNotificationUtil;

	@Value("${properties.bitfinex.domain}")
	private String domain;
	@Autowired
	private SecurityUtil securityUtil;
	@Autowired
	private ConnectionUtil connectionUtil;

	private ObjectMapper objectMapper = new ObjectMapper();

	public void cancelFundingOrder(Long orderId, String apiKey, String apiSecret) throws JsonProcessingException {
		String apiPath = "v2/auth/w/funding/offer/cancel";
		String url = domain + apiPath;
		String nonce = Long.toString(System.currentTimeMillis() * 1000);

		ObjectNode objectNode = objectMapper.createObjectNode();
		objectNode.put("id", orderId);
		String body = objectMapper.writeValueAsString(objectNode);

		String sigPlainText = "/api/" + apiPath + nonce + body;
		String sigHashText = securityUtil.HmacSHA384(sigPlainText, apiSecret);
		Map<String, String> headerList = new HashMap<>();
		headerList.put("bfx-nonce", nonce);
		headerList.put("bfx-apikey", apiKey);
		headerList.put("bfx-signature", sigHashText);

		Map<String, String> response = connectionUtil.sendPostRequest(url, headerList, body);
	}

	/**
	 *
	 * @param symbol
	 * @param apiKey
	 * @param apiSecret
	 * @param amount
	 * @param rate
	 * @param period
	 * @return OrderId
	 * @throws JsonProcessingException
	 */
	public String submitFundingOrder(String symbol,String apiKey, String apiSecret,
								   BigDecimal amount, BigDecimal rate, Integer period) throws JsonProcessingException {
		String orderId = null;
		String apiPath = "v2/auth/w/funding/offer/submit";
		String url = domain + apiPath;
		String nonce = Long.toString(System.currentTimeMillis() * 1000);


		ObjectNode objectNode = objectMapper.createObjectNode();
		objectNode.put("type", "LIMIT");
		objectNode.put("symbol", symbol);
		objectNode.put("amount", amount.toString());
		objectNode.put("rate", rate.divide(new BigDecimal(365), 12, RoundingMode.HALF_UP).divide(new BigDecimal(100)).toString());
		objectNode.put("period", period);
		objectNode.put("flags", "0");
		String body = objectMapper.writeValueAsString(objectNode);

		String sigPlainText = "/api/" + apiPath + nonce + body;

		String sigHashText = securityUtil.HmacSHA384(sigPlainText, apiSecret);
		Map<String, String> headerList = new HashMap<>();
		headerList.put("bfx-nonce", nonce);
		headerList.put("bfx-apikey", apiKey);
		headerList.put("bfx-signature", sigHashText);

		Map<String, String> response = connectionUtil.sendPostRequest(url, headerList, body);

		try{
			if(response.get("responseCode").equals("200")) {
				String responseMessage = response.get("responseMessage");
				responseMessage = responseMessage.replace("[", "").replace("]", "");

				String[] responseMessageArray = responseMessage.split(",");
				orderId = responseMessageArray[4];

			}
		}
		catch(Exception ex){
			telegramNotificationUtil.sendNotification(ex.toString());
			telegramNotificationUtil.sendNotification("response code: " + response.get("responseCode"));
			telegramNotificationUtil.sendNotification("response msg: " + response.get("responseMessage"));
		}


		return orderId;
	}

	public void getFundingOrderHistory(String symbol, String apiKey, String apiSecret) throws JsonProcessingException {
		String apiPath = "v2/auth/r/funding/offers/" + symbol + "/hist";
		String url = domain + apiPath;

		String nonce = Long.toString(System.currentTimeMillis() * 1000);
		String sigPlainText = "/api/" + apiPath + nonce;

		String sigHashText = securityUtil.HmacSHA384(sigPlainText, apiSecret);
		Map<String, String> headerList = new HashMap<>();
		headerList.put("bfx-nonce", nonce);
		headerList.put("bfx-apikey", apiKey);
		headerList.put("bfx-signature", sigHashText);

		ObjectNode objectNode = objectMapper.createObjectNode();
		objectNode.put("limit",10);
		String body = objectMapper.writeValueAsString(objectNode);

		Map<String, String> response = connectionUtil.sendPostRequest(url, headerList, "");

	}

	public List<BitfinexActiveOrderDto> getActiveWaitingOrder(String symbol, String apiKey, String apiSecret){
		List<BitfinexActiveOrderDto> result = new ArrayList<>();
		String apiPath = "v2/auth/r/funding/offers/" + symbol;
		String url = domain + apiPath;

		String nonce = Long.toString(System.currentTimeMillis() * 1000);
		String sigPlainText = "/api/" + apiPath + nonce;

		String sigHashText = securityUtil.HmacSHA384(sigPlainText, apiSecret);
		Map<String, String> headerList = new HashMap<>();
		headerList.put("bfx-nonce", nonce);
		headerList.put("bfx-apikey", apiKey);
		headerList.put("bfx-signature", sigHashText);

		Map<String, String> response = connectionUtil.sendPostRequest(url, headerList, "");

		try{
			if(response.get("responseCode").equals("200")) {
				String responseMessage = response.get("responseMessage");

				if(responseMessage.equals("[]")){
					return result;
				}

				responseMessage = responseMessage.substring(2, responseMessage.length() - 2);
				String[] activeOrderStringList = responseMessage.split("\\],\\[");
				for(int i = 0; i < activeOrderStringList.length; i++){
					StringBuilder sb = new StringBuilder(activeOrderStringList[i]);
					sb.insert(0, "[");
					sb.insert(sb.length(),"]");
					String finalString = sb.toString();

					BitfinexActiveOrderDto dto = objectMapper.readValue(finalString, BitfinexActiveOrderDto.class);

					result.add(dto);
				}
			}
		}
		catch(Exception ex){
			telegramNotificationUtil.sendNotification(ex.toString());
			telegramNotificationUtil.sendNotification("response code: " + response.get("responseCode"));
			telegramNotificationUtil.sendNotification("response msg: " + response.get("responseMessage"));
		}


		return result;
	}
}
