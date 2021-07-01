package com.s206.bitfinexbotv2.entity;

import javax.persistence.*;

@Entity
@Table(name = "waiting_order")
public class WaitingOrder {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;

	@Column(name = "order_id")
	private Long orderId;
	@Column(name = "order_create_time")
	private Long order_create_time;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Long getOrder_create_time() {
		return order_create_time;
	}

	public void setOrder_create_time(Long order_create_time) {
		this.order_create_time = order_create_time;
	}
}
