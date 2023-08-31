package com.s206.bitfinexbotv2.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name = "active_order")
public class ActiveOrder {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "order_id")
	private String orderId;
	@Column(name = "order_rate")
	private BigDecimal orderRate;
	@Column(name = "order_create_time")
	private Timestamp orderCreateTime;
	@Column(name = "order_update_time")
	private Timestamp orderUpdateTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public BigDecimal getOrderRate() {
		return orderRate;
	}

	public void setOrderRate(BigDecimal orderRate) {
		this.orderRate = orderRate;
	}

	public Timestamp getOrderCreateTime() {
		return orderCreateTime;
	}

	public void setOrderCreateTime(Timestamp orderCreateTime) {
		this.orderCreateTime = orderCreateTime;
	}

	public Timestamp getOrderUpdateTime() {
		return orderUpdateTime;
	}

	public void setOrderUpdateTime(Timestamp orderUpdateTime) {
		this.orderUpdateTime = orderUpdateTime;
	}
}
