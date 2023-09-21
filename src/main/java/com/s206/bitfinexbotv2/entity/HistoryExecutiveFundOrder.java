package com.s206.bitfinexbotv2.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name = "history_executive_fund_order")
public class HistoryExecutiveFundOrder {

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
	@Column(name = "order_executive_time")
	private Timestamp orderExecutiveTime;

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

	public Timestamp getOrderExecutiveTime() {
		return orderExecutiveTime;
	}

	public void setOrderExecutiveTime(Timestamp orderExecutiveTime) {
		this.orderExecutiveTime = orderExecutiveTime;
	}
}
