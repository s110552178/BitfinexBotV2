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
					String original = activeOrderStringList[i].replace("\"", "");
					String[] informList = original.split(",");

					BitfinexActiveOrderDto dto = new BitfinexActiveOrderDto();
					dto.setId(informList[0]);
					dto.setSymbol(informList[1]);
					dto.setCreateTime(new Timestamp(Long.parseLong(informList[2])));
					dto.setUpdateTime(new Timestamp(Long.parseLong(informList[3])));
					dto.setAmount(new BigDecimal(informList[4]));
					dto.setAmountOriginal(new BigDecimal(informList[5]));
					dto.setType(informList[6]);
//				dto.setPlaceHolder0(informList[7]);
//				dto.setPlaceHolder1(informList[8]);
					dto.setFlags(informList[9]);
					dto.setStatus(informList[10]);
//				dto.setPlaceHolder2(informList[11]);
//				dto.setPlaceHolder3(informList[12]);
//				dto.setPlaceHolder4(informList[13]);
					dto.setRate(new BigDecimal(informList[14]));
					dto.setPeriod(Integer.parseInt(informList[15]));
					dto.setNotify(Integer.parseInt(informList[16]));
					dto.setHidden(Integer.parseInt(informList[17]));
//				dto.setGetPlaceHolder5(informList[18]);
					dto.setRenew(Integer.parseInt(informList[19]));

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
