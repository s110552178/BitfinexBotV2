package com.s206.bitfinexbotv2.dto.request;

import org.springframework.lang.Nullable;

import java.math.BigDecimal;

public class ModifyFundSettingRequest {

	private String currency;
	@Nullable
	private BigDecimal annualRate;
	@Nullable
	private BigDecimal dropRate;
	@Nullable
	private BigDecimal lowestRate;
	@Nullable
	private BigDecimal accountLowestBalance;
	@Nullable
	private Boolean usingFRR;

	@Nullable
	public String getCurrency() {
		return currency;
	}

	public void setCurrency(@Nullable String currency) {
		this.currency = currency;
	}

	@Nullable
	public BigDecimal getAnnualRate() {
		return annualRate;
	}

	public void setAnnualRate(@Nullable BigDecimal annualRate) {
		this.annualRate = annualRate;
	}

	@Nullable
	public BigDecimal getDropRate() {
		return dropRate;
	}

	public void setDropRate(@Nullable BigDecimal dropRate) {
		this.dropRate = dropRate;
	}

	@Nullable
	public BigDecimal getLowestRate() {
		return lowestRate;
	}

	public void setLowestRate(@Nullable BigDecimal lowestRate) {
		this.lowestRate = lowestRate;
	}

	@Nullable
	public BigDecimal getAccountLowestBalance() {
		return accountLowestBalance;
	}

	public void setAccountLowestBalance(@Nullable BigDecimal accountLowestBalance) {
		this.accountLowestBalance = accountLowestBalance;
	}

	@Nullable
	public Boolean getUsingFRR() {
		return usingFRR;
	}

	public void setUsingFRR(@Nullable Boolean usingFRR) {
		this.usingFRR = usingFRR;
	}
}
