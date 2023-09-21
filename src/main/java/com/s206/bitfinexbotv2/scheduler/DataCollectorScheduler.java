package com.s206.bitfinexbotv2.scheduler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.s206.bitfinexbotv2.dto.BitfinexOrderHistoryDto;
import com.s206.bitfinexbotv2.entity.Config;
import com.s206.bitfinexbotv2.entity.FundSetting;
import com.s206.bitfinexbotv2.entity.HistoryExecutiveFundOrder;
import com.s206.bitfinexbotv2.entity.Secret;
import com.s206.bitfinexbotv2.repository.ConfigRepository;
import com.s206.bitfinexbotv2.repository.FundSettingRepository;
import com.s206.bitfinexbotv2.repository.HistoryExecutiveFundOrderRepository;
import com.s206.bitfinexbotv2.repository.SecretRepository;
import com.s206.bitfinexbotv2.service.MarginFundingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class DataCollectorScheduler {

	@Autowired
	private FundSettingRepository fundSettingRepository;
	@Autowired
	private SecretRepository secretRepository;
	@Autowired
	private MarginFundingService marginFundingService;
	@Autowired
	private ConfigRepository configRepository;
	@Autowired
	private HistoryExecutiveFundOrderRepository historyExecutiveFundOrderRepository;

	@Value("${scheduler.collect.data.last.update.time}")
	private String schedulerJobConfigName;
	@Value("${scheduler.collect.data.update.period}")
	private Long schedulerJobUpdatePeriod;

	@Scheduled(cron = "* */6 * * * *")
	public void collectFundSuccessData() throws JsonProcessingException {

		Optional<Secret> secretOptional = secretRepository.findById(1L);
		Secret secret = secretOptional.get();

		Config config = new Config();
		Optional<Config> configOptional = configRepository.findByConfigName(schedulerJobConfigName);
		if(configOptional.isEmpty()){
			Config generateConfig = new Config();
			generateConfig.setConfigName(schedulerJobConfigName);
			generateConfig.setConfigValue(Long.toString(0L));
			generateConfig.setUpdateTime(new Timestamp(System.currentTimeMillis()));

			configRepository.save(generateConfig);
			config = generateConfig;
		}
		else{
			config = configOptional.get();
		}

		boolean continueTaskFlag = false;

		while(!continueTaskFlag){

			Long lastUpdateTime = Long.parseLong(config.getConfigValue());
			Long currentUpdateTime = lastUpdateTime + 900000;
			if(currentUpdateTime + schedulerJobUpdatePeriod > System.currentTimeMillis()){
				continueTaskFlag = true;
			}
			else{
				List<FundSetting> fundSettingList = fundSettingRepository.findByStatus(true);
				for(int i = 0; i < fundSettingList.size(); i++){
					FundSetting fs = fundSettingList.get(i);
					String currency = "";

					switch (fs.getCurrency()) {
						case "USD":
							currency = "fUSD";
							break;
						case "ETH":
							currency = "fETH";
							break;
						case "DOGE":
							currency = "fDOGE";
						case "XMR":
							currency = "fXMR";
						case "ETHW":
							currency = "fETHW";
						case "LTC":
							currency = "fLTC";
						default:
							currency = "fUSD";
							break;
					}
					List<BitfinexOrderHistoryDto> historyRecordList = marginFundingService.getFundingHistoryData(currency, lastUpdateTime, currentUpdateTime, secret.getSecretKey(), secret.getSecretPassword());
					List<HistoryExecutiveFundOrder> historyExecutiveFundOrderList = new CopyOnWriteArrayList<>();
					if(historyRecordList.size( ) > 0){
						historyRecordList.parallelStream().forEach(
								historyRecord -> {
									String status = historyRecord.getStatus();
									if(status.contains("EXECUTED")){
										HistoryExecutiveFundOrder historyExecutiveFundOrder = new HistoryExecutiveFundOrder();
										historyExecutiveFundOrder.setOrderId(historyRecord.getId());
										historyExecutiveFundOrder.setOrderRate(historyRecord.getRate());
										historyExecutiveFundOrder.setOrderCreateTime(historyRecord.getCreateTime());
										historyExecutiveFundOrder.setOrderExecutiveTime(historyRecord.getUpdateTime());
//										historyExecutiveFundOrderRepository.save(historyExecutiveFundOrder);
										historyExecutiveFundOrderList.add(historyExecutiveFundOrder);
									}

								}
						);
					}
				historyExecutiveFundOrderRepository.saveAll(historyExecutiveFundOrderList);
				}
				config.setConfigValue(Long.toString(currentUpdateTime));
				config.setUpdateTime(new Timestamp(System.currentTimeMillis()));
				configRepository.save(config);

			}
		}
		// marginFundingService.getFundingHistoryData()
	}

}
