package com.s206.bitfinexbotv2.scheduler;

import com.s206.bitfinexbotv2.dto.BitfinexWalletDto;
import com.s206.bitfinexbotv2.entity.FundSetting;
import com.s206.bitfinexbotv2.entity.Secret;
import com.s206.bitfinexbotv2.repository.FundSettingRepository;
import com.s206.bitfinexbotv2.repository.SecretRepository;
import com.s206.bitfinexbotv2.service.AmountService;
import org.springframework.beans.factory.annotation.Autowired;
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

//	@Scheduled(cron = "0 * * * * *")
	public void checkBalanceAndPlaceOrder(){
		// 檢查餘額放貸
		try{
			Optional<Secret> secretOptional = secretRepository.findById(1L);

			List<BitfinexWalletDto> walletDtoList = amountService.getAmount(secretOptional.get().getSecretKey(), secretOptional.get().getSecretPassword());

			for(int i = 0; i < walletDtoList.size(); i++){
				BitfinexWalletDto walletDto = walletDtoList.get(i);
				FundSetting fundSetting = getFundSetting(walletDto.getCurrency());
				if(fundSetting != null){
					break;
				}
				switch(walletDto.getCurrency()){
					case "USD":
						BigDecimal lendingMoney = walletDto.getAvailableBalance().subtract(fundSetting.getAccountLowestBalance());
						if(lendingMoney.compareTo(new BigDecimal(50)) > 0){

						}


						break;
					default:
						break;

				}
			}


			System.out.println("");
		}
		catch (Exception ex){
			ex.printStackTrace();
		}


	}

	private FundSetting getFundSetting(String currency){
		FundSetting fundSetting = null;
		Optional<FundSetting> fundSettingOptional = fundSettingRepository.findByCurrency("USD");
		if(!fundSettingOptional.isPresent()){
			System.out.println(currency + "Fund config is not exist");
		}
		else{
			fundSetting = fundSettingOptional.get();
		}
		return fundSetting;
	}

}
