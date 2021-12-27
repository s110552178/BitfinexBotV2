package com.s206.bitfinexbotv2.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.s206.bitfinexbotv2.util.ConnectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class CalculateService {

	@Value("${properties.bitfinex.domain}")
	private String domain;

	@Autowired
	private ConnectionUtil connectionUtil;

	private ObjectMapper mapper = new ObjectMapper();

	/**
	 * 轉換匯率
	 * @param baseCurrency
	 * @param quoteCurrency
	 * e.g. baseCurrency = BTC, quoteCurrency = USE, 表示要取得BTC當時兌換美金的匯率
	 */
	public BigDecimal exchangeRate(String baseCurrency, String quoteCurrency){
		BigDecimal result = new BigDecimal(0);
		try{
			String url = domain + "v2/calc/fx";

			ObjectNode node = mapper.createObjectNode();
			node.put("ccy1", baseCurrency);
			node.put("ccy2", quoteCurrency);

			String body = mapper.writeValueAsString(node);
			Map<String, String> headerList = new HashMap<>();

			Map<String, String> response = connectionUtil.sendPostRequest(url, headerList, body);
			if(response.get("responseCode").equals("200")) {
				String stringRate = response.get("responseMessage").replace("[", "").replace("]", "");

				result = new BigDecimal(stringRate);

			}

		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		return result;


	}


}
