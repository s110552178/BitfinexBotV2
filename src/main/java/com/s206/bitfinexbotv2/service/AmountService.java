package com.s206.bitfinexbotv2.service;

import com.s206.bitfinexbotv2.dto.BitfinexWalletDto;
import com.s206.bitfinexbotv2.util.ConnectionUtil;
import com.s206.bitfinexbotv2.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AmountService {

	@Autowired
	private SecurityUtil securityUtil;

	@Autowired
	private ConnectionUtil connectionUtil;

	@Value("${properties.bitfinex.domain}")
	private String domain;

	public List<BitfinexWalletDto> getAmount(String apiKey, String apiSecret){

		List<BitfinexWalletDto> result = new ArrayList<>();

		String apiPath = "v2/auth/r/wallets";
		String url = domain + apiPath;

		String nonce = Long.toString(System.currentTimeMillis() * 1000);
		String sigPlainText = "/api/" + apiPath + nonce;

		String sigHashText = securityUtil.HmacSHA384(sigPlainText, apiSecret);
		Map<String, String> headerList = new HashMap<>();
		headerList.put("bfx-nonce", nonce);
		headerList.put("bfx-apikey", apiKey);
		headerList.put("bfx-signature", sigHashText);

		Map<String, String> response = connectionUtil.sendPostRequest(url, headerList, "");
		if(response.get("responseCode").equals("200")) {
			String responseMessage = response.get("responseMessage");
			responseMessage = responseMessage.substring(2, responseMessage.length() - 2);
			String[] walletStringList = responseMessage.split("\\],\\[");

			for (int i = 0; i < walletStringList.length; i++) {
				String original = walletStringList[i].replace("\"", "");
				String[] informList = original.split(",");

				BitfinexWalletDto dto = new BitfinexWalletDto();
				dto.setWalletType(informList[0]);
				dto.setCurrency(informList[1]);
				dto.setBalance(new BigDecimal(informList[2]));
				dto.setUnsettledInterest(new BigDecimal(informList[3]));
				dto.setAvailableBalance(new BigDecimal(informList[4]));
				dto.setLastChange(informList[5]);
				dto.setTraceDetail(informList[6]);
				result.add(dto);
			}
		}
		else{
			System.out.println("Error Code: " + response.get("responseCode"));
			System.out.println("Error Message: " + response.get("responseMessage"));
		}


		return result;
	}

}
