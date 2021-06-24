package com.s206.bitfinexbotv2.repository;

import com.s206.bitfinexbotv2.entity.WaitingOrder;
import org.springframework.data.repository.CrudRepository;

public interface WaitingOrderRepository extends CrudRepository<WaitingOrder, Long> {
}
