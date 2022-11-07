package com.s206.bitfinexbotv2.entity;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "advanced_funding_setting")
public class AdvancedFundSetting {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "funding_setting_id")
	private Long fundSettingId;
	@Column(name = "highest_rate")
	private BigDecimal highestRate;
	@Column(name = "fund_lending_period")
	private int fundLendingPeriod;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getFundSettingId() {
		return fundSettingId;
	}

	public void setFundSettingId(Long fundSettingId) {
		this.fundSettingId = fundSettingId;
	}

	public BigDecimal getHighestRate() {
		return highestRate;
	}

	public void setHighestRate(BigDecimal highestRate) {
		this.highestRate = highestRate;
	}

	public int getFundLendingPeriod() {
		return fundLendingPeriod;
	}

	public void setFundLendingPeriod(int fundLendingPeriod) {
		this.fundLendingPeriod = fundLendingPeriod;
	}
}
