package com.s206.bitfinexbotv2.repository;

import com.s206.bitfinexbotv2.entity.Secret;
import org.springframework.data.repository.CrudRepository;

public interface SecretRepository extends CrudRepository<Secret, Long> {
}
