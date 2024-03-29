package com.s206.bitfinexbotv2.entity;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "waiting_order")
public class WaitingOrder {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;

	@Column(name = "main_order_id")
	private String mainOrderId;
	@Column(name = "sub_order_id")
	private String subOrderId;
	@Column(name = "order_create_time")
	private Timestamp orderCreateTime;

	@Column(name = "order_update_time")
	private Timestamp orderUpdateTime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getMainOrderId() {
		return mainOrderId;
	}

	public void setMainOrderId(String mainOrderId) {
		this.mainOrderId = mainOrderId;
	}

	public String getSubOrderId() {
		return subOrderId;
	}

	public void setSubOrderId(String subOrderId) {
		this.subOrderId = subOrderId;
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
