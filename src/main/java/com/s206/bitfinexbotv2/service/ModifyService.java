package com.s206.bitfinexbotv2.service;

import com.s206.bitfinexbotv2.dto.request.ModifyFundSettingRequest;
import com.s206.bitfinexbotv2.entity.FundSetting;
import com.s206.bitfinexbotv2.repository.FundSettingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ModifyService {

	@Autowired
	FundSettingRepository fundSettingRepository;

	public void modifyFundSetting(ModifyFundSettingRequest request){
		Optional<FundSetting> fundSettingOptional = fundSettingRepository.findByCurrency(request.getCurrency());
		if(fundSettingOptional.isPresent()){
			FundSetting fundSetting = fundSettingOptional.get();
			if(request.getAnnualRate() != null){
				fundSetting.setRateYearly(request.getAnnualRate());
			}
			if(request.getUsingFRR() != null){
				fundSetting.setUsingFRR(request.getUsingFRR());
			}
			if(request.getAccountLowestBalance() != null){
				fundSetting.setAccountLowestBalance(request.getAccountLowestBalance());
			}
			if(request.getDropRate() != null){
				fundSetting.setDropDownRate(request.getDropRate());
			}
			if(request.getLowestRate() != null){
				fundSetting.setLowestRateYearly(request.getLowestRate());
			}
			fundSettingRepository.save(fundSetting);
		}

	}

}
