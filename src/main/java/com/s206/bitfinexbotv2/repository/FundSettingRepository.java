package com.s206.bitfinexbotv2.repository;

import com.s206.bitfinexbotv2.entity.FundSetting;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface FundSettingRepository extends CrudRepository<FundSetting, Long> {

	Optional<FundSetting> findByCurrencyAndStatus(String currency, boolean status);
	Optional<FundSetting> findByCurrency(String currency);

	List<FundSetting> findByStatus(Boolean status);
}
