package com.s206.bitfinexbotv2.scheduler;

import com.s206.bitfinexbotv2.dto.BitfinexActiveOrderDto;
import com.s206.bitfinexbotv2.dto.BitfinexWalletDto;
import com.s206.bitfinexbotv2.entity.FundSetting;
import com.s206.bitfinexbotv2.entity.Secret;
import com.s206.bitfinexbotv2.entity.WaitingOrder;
import com.s206.bitfinexbotv2.repository.FundSettingRepository;
import com.s206.bitfinexbotv2.repository.SecretRepository;
import com.s206.bitfinexbotv2.repository.WaitingOrderRepository;
import com.s206.bitfinexbotv2.service.AmountService;
import com.s206.bitfinexbotv2.service.MarginFundingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

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

	@Value("${properties.order.refresh.time}")
	private Long waitingOrderRefreshTime;
	@Value("${properties.order.expired.time}")
	private Long waitingOrderExpiredTime;

	@Scheduled(fixedDelay = 3600000)
	public void removeExpiredOrder(){
		Iterable<WaitingOrder> waitingOrderIterable = waitingOrderRepository.findAll();
		waitingOrderIterable.forEach( waitingOrder -> {
			if(System.currentTimeMillis() - waitingOrder.getOrder_create_time() > waitingOrderExpiredTime){
				waitingOrderRepository.delete(waitingOrder);
			}
		});

	}
	@Scheduled(cron = "30 * * * * *")
	public void updateWaitingOrder(){
		try{

			Optional<Secret> secretOptional = secretRepository.findById(1L);
			Secret secret = secretOptional.get();

			List<BitfinexActiveOrderDto> activeOrderDtoList = marginFundingService.getActiveWaitingOrder("", secret.getSecretKey(), secret.getSecretPassword());
			Iterable<WaitingOrder> list = waitingOrderRepository.findAll();

			list.forEach( waitingOrder -> {
				try{
					Long orderId = waitingOrder.getOrderId();
					for(int i = 0; i < activeOrderDtoList.size(); i++){
						BitfinexActiveOrderDto bitfinexOrder = activeOrderDtoList.get(i);
						Long bitfinexActiveOrderId = Long.parseLong(bitfinexOrder.getId());
						if(bitfinexActiveOrderId.compareTo(orderId) == 0  &&
								System.currentTimeMillis() - bitfinexOrder.getCreateTime().getTime() > waitingOrderRefreshTime){

							String currency = "";
							switch (bitfinexOrder.getSymbol()){
								case "fUSD":
									currency = "USD";
									break;
								case "eth":
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
							String newOrderId = marginFundingService.submitFundingOrder(bitfinexOrder.getSymbol(), secret.getSecretKey(), secret.getSecretPassword(),
									bitfinexOrder.getAmount(), newRate, fundSetting.getPreferLendingPeriod());
							waitingOrder.setOrderId(Long.parseLong(newOrderId));
							waitingOrder.setOrder_create_time(System.currentTimeMillis());
							waitingOrderRepository.save(waitingOrder);
						}
					}
				}
				catch (Exception ex){
					ex.printStackTrace();
				}

			});

		}
		catch (Exception ex){
			ex.printStackTrace();
		}
	}


	@Scheduled(cron = "0 * * * * *")
	public void checkBalanceAndPlaceOrder(){
		// 檢查餘額放貸
		try{
			Optional<Secret> secretOptional = secretRepository.findById(1L);
			Secret secret = secretOptional.get();
			List<BitfinexWalletDto> walletDtoList = amountService.getAmount(secret.getSecretKey(), secret.getSecretPassword());

			for(int i = 0; i < walletDtoList.size(); i++){
				BitfinexWalletDto walletDto = walletDtoList.get(i);
				FundSetting fundSetting = getFundSetting(walletDto.getCurrency());
				if(fundSetting == null || walletDto.getWalletType().equals("exchange")){
					// wallet Type is not funding, so ignore it
				}
				else{
					String orderId = null;
					switch(fundSetting.getCurrency()){
						case "USD":
							BigDecimal lendingMoney = walletDto.getAvailableBalance().subtract(fundSetting.getAccountLowestBalance());
							if(lendingMoney.compareTo(new BigDecimal(50)) > 0 || lendingMoney.compareTo(new BigDecimal(50)) == 0){
								orderId = marginFundingService.submitFundingOrder("fUSD", secret.getSecretKey(), secret.getSecretPassword(),
										lendingMoney, fundSetting.getRateYearly(), fundSetting.getPreferLendingPeriod());
							}

							break;
						case "DOGE":

							orderId = marginFundingService.submitFundingOrder("fDOGE", secret.getSecretKey(), secret.getSecretPassword(),
									walletDto.getAvailableBalance(), fundSetting.getRateYearly(), fundSetting.getPreferLendingPeriod());
							break;
						default:
							orderId = marginFundingService.submitFundingOrder(fundSetting.getCurrency(), secret.getSecretKey(), secret.getSecretPassword(),
									walletDto.getAvailableBalance(), fundSetting.getRateYearly(), fundSetting.getPreferLendingPeriod());
							break;

					}
					if(orderId != null){
						WaitingOrder waitingOrder = new WaitingOrder();
						waitingOrder.setOrderId(Long.parseLong(orderId));
						waitingOrder.setOrder_create_time(System.currentTimeMillis());
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
			System.out.println(currency + "Fund config is not exist");
		}
		else{
			fundSetting = fundSettingOptional.get();
		}
		return fundSetting;
	}

}
