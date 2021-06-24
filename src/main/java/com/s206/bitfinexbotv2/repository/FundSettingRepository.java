package com.s206.bitfinexbotv2.repository;

import com.s206.bitfinexbotv2.entity.FundSetting;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface FundSettingRepository extends CrudRepository<FundSetting, Long> {

	Optional<FundSetting> findByCurrency(String currency);

}
