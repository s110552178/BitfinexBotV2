package com.s206.bitfinexbotv2.repository;

import com.s206.bitfinexbotv2.entity.Config;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfigRepository extends CrudRepository<Config, Long> {

	Optional<Config> findByConfigName(String configName);

}
