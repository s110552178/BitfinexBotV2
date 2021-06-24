package com.s206.bitfinexbotv2.util;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ConnectionUtil {

	public Map<String, String> sendPostRequest(String urlString, Map<String, String> headerList, String body){
		Map<String, String> result = new HashMap<>();

		try{
			URL url = new URL(urlString);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setConnectTimeout(30000);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestProperty("Content-Type", "application/json");
			if(!CollectionUtils.isEmpty(headerList)){
				headerList.forEach((key, value) -> {
							conn.setRequestProperty(key, value);
						}
				);

			}
			// body handle
			OutputStream os = conn.getOutputStream();
			OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
			osw.write(body);
			osw.flush();
			osw.close();
			os.close();
			//

			conn.connect();
			int responseCode = conn.getResponseCode();
			InputStreamReader isr;
			BufferedReader br;
			if(responseCode != 200){
				isr = new InputStreamReader(conn.getErrorStream());
				br = new BufferedReader(isr);
			}
			else {
				isr = new InputStreamReader(conn.getInputStream());
				br = new BufferedReader(isr);
			}
			String line;
			String responseMessage = "";
			while ((line = br.readLine()) != null){
				responseMessage = responseMessage + line;
			}
			result.put("responseCode", Integer.toString(responseCode));
			result.put("responseMessage", responseMessage);
		}
		catch(Exception ex){
			ex.printStackTrace();
		}

		return result;
	}

	public String sendGetRequest(String urlString, Map<String, String> headerList, String body){

		String result = "";

		try{
			URL url = new URL(urlString);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setDoOutput(true);
			conn.setDoInput(true);
			if(!CollectionUtils.isEmpty(headerList)){
				headerList.forEach((key, value) -> {
							conn.setRequestProperty(key, value);
						}
				);

			}

			conn.connect();

			InputStreamReader isr = new InputStreamReader(conn.getInputStream());
			BufferedReader br = new BufferedReader(isr);

			String line;
			while ((line = br.readLine()) != null){
				result = result + line;
			}
		}
		catch(Exception ex){
			ex.printStackTrace();
		}


		return result;
	}

}
