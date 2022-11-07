package com.s206.bitfinexbotv2.repository;

import com.s206.bitfinexbotv2.entity.AdvancedFundSetting;
import org.springframework.data.repository.CrudRepository;

import java.math.BigDecimal;
import java.util.List;

public interface AdvancedFundSettingRepository extends CrudRepository<AdvancedFundSetting, Long> {

	List<AdvancedFundSetting> findAdvancedFundSettingByFundSettingIdOrderByHighestRateDesc(Long fundSettingId);
	List<AdvancedFundSetting> findAdvancedFundSettingByFundSettingIdAndHighestRateLessThanOrderByHighestRateDesc(Long fundSettingId, BigDecimal lowerRate);
	List<AdvancedFundSetting> findAdvancedFundSettingByFundSettingIdOrderByHighestRateAsc(Long fundSettindId);
}
