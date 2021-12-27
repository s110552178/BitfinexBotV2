package com.s206.bitfinexbotv2.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TelegramNotificationUtil {

	@Autowired
	private ConnectionUtil connectionUtil;

	@Value("${send.telegram.notification.config}")
	private Boolean notificationConfig;

	@Value("${telegram.bot.token}")
	private String telegramBotId;
	@Value("${telegram.group.id}")
	private String telegramGroupId;

	public void sendNotification(String msg){
		// https://api.telegram.org/bot1234579:Afskjdpfiu39u4jkJKFS9UJK3248YEqb593tqo/sendMessage?chat_id=-1234567&text=123

		StringBuilder sb = new StringBuilder();
		sb.append("https://api.telegram.org/bot").append(telegramBotId).append("/sendMessage?chat_id=");
		sb.append(telegramGroupId);
		sb.append("&text=").append(msg);

		String requestUrl = sb.toString();
		connectionUtil.sendGetRequest(requestUrl, null, null);

	}

}
