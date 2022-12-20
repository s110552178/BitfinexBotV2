package com.s206.bitfinexbotv2.entity;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "funding_setting")
public class FundSetting {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "currency")
	private String currency;
	@Column(name = "rate_yearly")
	private BigDecimal rateYearly;
	@Column(name = "account_lowest_balance")
	private BigDecimal accountLowestBalance;
	@Column(name = "usingFRR")
	private Boolean usingFRR;
	@Column(name = "prefer_lending_period")
	private Integer preferLendingPeriod;
	@Column(name = "drop_down_rate")
	private BigDecimal dropDownRate;
	@Column(name = "lowest_rate_yearly")
	private BigDecimal lowestRateYearly;
	@Column(name = "status")
	private Boolean status;
	@Column(name = "initialize_rate")
	private BigDecimal initializeRate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public BigDecimal getRateYearly() {
		return rateYearly;
	}

	public void setRateYearly(BigDecimal rateYearly) {
		this.rateYearly = rateYearly;
	}

	public BigDecimal getAccountLowestBalance() {
		return accountLowestBalance;
	}

	public void setAccountLowestBalance(BigDecimal accountLowestBalance) {
		this.accountLowestBalance = accountLowestBalance;
	}

	public Boolean getUsingFRR() {
		return usingFRR;
	}

	public void setUsingFRR(Boolean usingFRR) {
		this.usingFRR = usingFRR;
	}

	public Integer getPreferLendingPeriod() {
		return preferLendingPeriod;
	}

	public void setPreferLendingPeriod(Integer preferLendingPeriod) {
		this.preferLendingPeriod = preferLendingPeriod;
	}

	public BigDecimal getDropDownRate() {
		return dropDownRate;
	}

	public void setDropDownRate(BigDecimal dropDownRate) {
		this.dropDownRate = dropDownRate;
	}

	public BigDecimal getLowestRateYearly() {
		return lowestRateYearly;
	}

	public void setLowestRateYearly(BigDecimal lowestRateYearly) {
		this.lowestRateYearly = lowestRateYearly;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public BigDecimal getInitializeRate() {
		return initializeRate;
	}

	public void setInitializeRate(BigDecimal initializeRate) {
		this.initializeRate = initializeRate;
	}
}
