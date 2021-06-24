package com.s206.bitfinexbotv2.util;

import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Component
public class SecurityUtil {
	private final static String algo = "HmacSHA384";
	public String HmacSHA384(String plainText, String encryptKey){
		String result = "";
		try{
			Mac sha256Mac = Mac.getInstance(algo);
			SecretKeySpec secretKey = new SecretKeySpec(encryptKey.getBytes("UTF-8"), algo);
			sha256Mac.init(secretKey);

			byte[] bytes = sha256Mac.doFinal(plainText.getBytes("ASCII"));
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < bytes.length; i++) {
				String hex = Integer.toHexString(0xFF & bytes[i]);
				if (hex.length() == 1) {
					sb.append('0');
				}
				sb.append(hex);
			}
			result = sb.toString();
//			SecretKeySpec key = new SecretKeySpec((encryptKey).getBytes("UTF-8"), algo);
//			Mac mac = Mac.getInstance(algo);
//			mac.init(key);
//
//			byte[] bytes = mac.doFinal(plainText.getBytes("ASCII"));
//
//			StringBuffer hash = new StringBuffer();
//			for (int i = 0; i < bytes.length; i++) {
//				String hex = Integer.toHexString(0xFF & bytes[i]);
//				if (hex.length() == 1) {
//					hash.append('0');
//				}
//				hash.append(hex);
//			}
//			result = hash.toString();
		}

		catch (Exception ex){
			ex.printStackTrace();
		}


		return result;
	}


}
