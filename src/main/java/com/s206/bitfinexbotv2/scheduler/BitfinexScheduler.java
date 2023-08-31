package com.s206.bitfinexbotv2.scheduler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.s206.bitfinexbotv2.dto.BitfinexActiveOrderDto;
import com.s206.bitfinexbotv2.dto.BitfinexWalletDto;
import com.s206.bitfinexbotv2.entity.AdvancedFundSetting;
import com.s206.bitfinexbotv2.entity.FundSetting;
import com.s206.bitfinexbotv2.entity.Secret;
import com.s206.bitfinexbotv2.entity.WaitingOrder;
import com.s206.bitfinexbotv2.repository.AdvancedFundSettingRepository;
import com.s206.bitfinexbotv2.repository.FundSettingRepository;
import com.s206.bitfinexbotv2.repository.SecretRepository;
import com.s206.bitfinexbotv2.repository.WaitingOrderRepository;
import com.s206.bitfinexbotv2.service.AmountService;
import com.s206.bitfinexbotv2.service.CalculateService;
import com.s206.bitfinexbotv2.service.MarginFundingService;
import com.s206.bitfinexbotv2.util.TelegramNotificationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class BitfinexScheduler {

	@Autowired
	private AmountService amountService;

	@Autowired
	private SecretRepository secretRepository;
	@Autowired
	private FundSettingRepository fundSettingRepository;
	@Autowired
	private MarginFundingService marginFundingService;
	@Autowired
	private WaitingOrderRepository waitingOrderRepository;
	@Autowired
	private CalculateService calculateService;
	@Autowired
	private AdvancedFundSettingRepository advancedFundSettingRepository;
//	@Autowired
//	private TelegramNotificationUtil notificationUtil;

//	private ObjectMapper mapper = new ObjectMapper();

	@Value("${properties.order.refresh.time}")
	private Long waitingOrderRefreshTime;
	@Value("${properties.order.expired.time}")
	private Long waitingOrderExpiredTime;
	@Value("${properties.minimum.funding.order.price}")
	private Long minimumFundingOrderPrice;


//	@Scheduled(fixedDelay = 3600000)
//	public void removeExpiredOrder(){
//		Iterable<WaitingOrder> waitingOrderIterable = waitingOrderRepository.findAll();
//
//		waitingOrderIterable.forEach( waitingOrder -> {
//			if(System.currentTimeMillis() - waitingOrder.getOrder_create_time() > waitingOrderExpiredTime){
//				waitingOrderRepository.delete(waitingOrder);
//			}
//		});
//
//	}

	@Scheduled(cron = "20 * * * * *")
	public void updateWaitingOrder() {


			Optional<Secret> secretOptional = secretRepository.findById(1L);
			Secret secret = secretOptional.get();

			List<BitfinexActiveOrderDto> activeOrderDtoList = marginFundingService.getActiveWaitingOrder("", secret.getSecretKey(), secret.getSecretPassword());
			Iterable<WaitingOrder> list = waitingOrderRepository.findAll();

			list.forEach( waitingOrder -> {
				try {
					String orderId = waitingOrder.getSubOrderId();
					for (int i = 0; i < activeOrderDtoList.size(); i++) {
						BitfinexActiveOrderDto bitfinexOrder = activeOrderDtoList.get(i);
						String bitfinexActiveOrderId = bitfinexOrder.getId();
						if (bitfinexActiveOrderId.equals(orderId) &&
								System.currentTimeMillis() - bitfinexOrder.getCreateTime().getTime() > waitingOrderRefreshTime) {

							String currency = "";
							switch (bitfinexOrder.getSymbol()) {
								case "fUSD":
									currency = "USD";
									break;
								case "fETH":
									currency = "ETH";
									break;
								case "fDOGE":
									currency = "DOGE";
								default:
									currency = "USD";
									break;
							}

							FundSetting fundSetting = fundSettingRepository.findByCurrency(currency).get();

							marginFundingService.cancelFundingOrder(bitfinexActiveOrderId, secret.getSecretKey(), secret.getSecretPassword());
							BigDecimal newRate = bitfinexOrder.getRate().multiply(new BigDecimal(100)).multiply(new BigDecimal(365));
							newRate = newRate.subtract(fundSetting.getDropDownRate());
							// try to process advanced funding setting

							if (newRate.compareTo(fundSetting.getLowestRateYearly()) <= 0) {
								newRate = fundSetting.getLowestRateYearly();
							} else {
								List<AdvancedFundSetting> lowerAdvancedFundSettings = advancedFundSettingRepository.findAdvancedFundSettingByFundSettingIdAndHighestRateLessThanOrderByHighestRateDesc(fundSetting.getId(), newRate);
								if (lowerAdvancedFundSettings.size() > 0) {
									AdvancedFundSetting advancedFundSetting = lowerAdvancedFundSettings.get(0);

									fundSetting.setPreferLendingPeriod(advancedFundSetting.getFundLendingPeriod());

								} else {
									List<AdvancedFundSetting> advancedFundSettings = advancedFundSettingRepository.findAdvancedFundSettingByFundSettingIdOrderByHighestRateAsc(fundSetting.getId());

									if (advancedFundSettings.size() > 0) {
										fundSetting.setPreferLendingPeriod(advancedFundSettings.get(0).getFundLendingPeriod());
									}

								}
							}

							//
							String newOrderId = marginFundingService.submitFundingOrder(bitfinexOrder.getSymbol(), secret.getSecretKey(), secret.getSecretPassword(),
									bitfinexOrder.getAmount(), newRate, fundSetting.getPreferLendingPeriod());
							waitingOrder.setSubOrderId(newOrderId);
							waitingOrder.setOrderUpdateTime(new Timestamp(System.currentTimeMillis()));
							waitingOrderRepository.save(waitingOrder);
						}
					}
				}
				catch(Exception ex){
					ex.printStackTrace();
				}

			});

	}


	@Scheduled(cron = "0 * * * * *")
	public void checkBalanceAndPlaceOrder(){
		// 檢查餘額放貸
		try{
			Optional<Secret> secretOptional = secretRepository.findById(1L);
			Secret secret = secretOptional.get();
			List<BitfinexWalletDto> walletDtoList = amountService.getAmount(secret.getSecretKey(), secret.getSecretPassword());
			// remove wallet data which is not "Funding" wallet type
			for(int i = 0; i < walletDtoList.size(); i++){

				BitfinexWalletDto walletDto = walletDtoList.get(i);
				if(!walletDto.getWalletType().equals("funding")){
					walletDtoList.remove(i);
				}
			}
				//
			for(int i = 0; i < walletDtoList.size(); i++){
				BitfinexWalletDto walletDto = walletDtoList.get(i);
				FundSetting fundSetting = getFundSetting(walletDto.getCurrency());
				List<AdvancedFundSetting> advancedFundSettingList = advancedFundSettingRepository.findAdvancedFundSettingByFundSettingIdOrderByHighestRateDesc(fundSetting.getId());
				if(advancedFundSettingList != null && advancedFundSettingList.size() > 0){
					// change setting to advanced setting
					AdvancedFundSetting advancedFundSetting = advancedFundSettingList.get(0);
					fundSetting.setPreferLendingPeriod(advancedFundSetting.getFundLendingPeriod());
					fundSetting.setInitializeRate(advancedFundSetting.getHighestRate());
				}

				if(fundSetting == null || walletDto.getWalletType().equals("exchange")){
					// wallet Type is not funding, so ignore it
				}
				else{
					String orderId = null;
					switch(fundSetting.getCurrency()){
						case "USD":
							BigDecimal lendingMoney = walletDto.getAvailableBalance().subtract(fundSetting.getAccountLowestBalance());
							if(lendingMoney.compareTo(new BigDecimal(minimumFundingOrderPrice)) > 0 || lendingMoney.compareTo(new BigDecimal(minimumFundingOrderPrice)) == 0){
								orderId = marginFundingService.submitFundingOrder("fUSD", secret.getSecretKey(), secret.getSecretPassword(),
										lendingMoney, fundSetting.getInitializeRate(), fundSetting.getPreferLendingPeriod());
							}
							break;
						case "DOGE":
							BigDecimal dogeUSDexchangeRate = calculateService.exchangeRate("DOGE","USD");
							if((walletDto.getAvailableBalance().multiply(dogeUSDexchangeRate)).compareTo(new BigDecimal(minimumFundingOrderPrice)) > 0){
								orderId = marginFundingService.submitFundingOrder("fDOGE", secret.getSecretKey(), secret.getSecretPassword(),
										walletDto.getAvailableBalance(), fundSetting.getInitializeRate(), fundSetting.getPreferLendingPeriod());
							}
							break;
						case "XMR":
							BigDecimal xmrUSDexchangeRate = calculateService.exchangeRate("XMR","USD");
							if((walletDto.getAvailableBalance().multiply(xmrUSDexchangeRate)).compareTo(new BigDecimal(minimumFundingOrderPrice)) > 0){
								orderId = marginFundingService.submitFundingOrder("fXMR", secret.getSecretKey(), secret.getSecretPassword(),
										walletDto.getAvailableBalance(), fundSetting.getInitializeRate(), fundSetting.getPreferLendingPeriod());
							}
							break;
						case "ETH":
							BigDecimal ethUSDExchangeRate = calculateService.exchangeRate("ETH", "USD");
							if((walletDto.getAvailableBalance().multiply(ethUSDExchangeRate)).compareTo(new BigDecimal(minimumFundingOrderPrice)) > 0){
								orderId = marginFundingService.submitFundingOrder("fETH", secret.getSecretKey(), secret.getSecretPassword(),
										walletDto.getAvailableBalance(), fundSetting.getInitializeRate(), fundSetting.getPreferLendingPeriod());
							}
							break;
						case "LTC":
							BigDecimal ltcUSDExchangeRate = calculateService.exchangeRate("LTC", "USD");
							if((walletDto.getAvailableBalance().multiply(ltcUSDExchangeRate)).compareTo(new BigDecimal(minimumFundingOrderPrice)) > 0){
								orderId = marginFundingService.submitFundingOrder("fLTC", secret.getSecretKey(), secret.getSecretPassword(),
										walletDto.getAvailableBalance(), fundSetting.getInitializeRate(), fundSetting.getPreferLendingPeriod());
							}
							break;
						default:
							orderId = marginFundingService.submitFundingOrder(fundSetting.getCurrency(), secret.getSecretKey(), secret.getSecretPassword(),
									walletDto.getAvailableBalance(), fundSetting.getInitializeRate(), fundSetting.getPreferLendingPeriod());
							break;

					}
					if(orderId != null){
						WaitingOrder waitingOrder = new WaitingOrder();
						String newMainOrderId = generateUUID();
						waitingOrder.setMainOrderId(newMainOrderId);
						waitingOrder.setSubOrderId(orderId);
						waitingOrder.setOrderCreateTime(new Timestamp(System.currentTimeMillis()));
						waitingOrder.setOrderUpdateTime(new Timestamp(System.currentTimeMillis()));
						waitingOrderRepository.save(waitingOrder);
					}
				}

			}
		}
		catch (Exception ex){
			ex.printStackTrace();
		}

	}

	private FundSetting getFundSetting(String currency){
		FundSetting fundSetting = null;
		Optional<FundSetting> fundSettingOptional = fundSettingRepository.findByCurrencyAndStatus(currency, true);
		if(!fundSettingOptional.isPresent()){
			System.out.println(currency + " Fund config is not exist");
		}
		else{
			fundSetting = fundSettingOptional.get();
		}

		return fundSetting;
	}

	public String generateUUID(){
		UUID uuid = UUID.randomUUID();
		String uuidString = uuid.toString();
		return uuidString;
	}

}
