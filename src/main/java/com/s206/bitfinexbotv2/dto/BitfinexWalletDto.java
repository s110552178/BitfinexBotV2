package com.s206.bitfinexbotv2.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.math.BigDecimal;

@JsonFormat(shape = JsonFormat.Shape.ARRAY)
@JsonPropertyOrder({"walletType", "currency", "balance", "unsettledInterest", "availableBalance", "lastChange", "traceDetail"})
public class BitfinexWalletDto {

	private String walletType;
	private String currency;
	private BigDecimal balance;
	private BigDecimal unsettledInterest;
	private BigDecimal availableBalance;
	private Object lastChange;
	private Object traceDetail;

	public String getWalletType() {
		return walletType;
	}

	public void setWalletType(String walletType) {
		this.walletType = walletType;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public BigDecimal getUnsettledInterest() {
		return unsettledInterest;
	}

	public void setUnsettledInterest(BigDecimal unsettledInterest) {
		this.unsettledInterest = unsettledInterest;
	}

	public BigDecimal getAvailableBalance() {
		return availableBalance;
	}

	public void setAvailableBalance(BigDecimal availableBalance) {
		this.availableBalance = availableBalance;
	}

	public Object getLastChange() {
		return lastChange;
	}

	public void setLastChange(Object lastChange) {
		this.lastChange = lastChange;
	}

	public Object getTraceDetail() {
		return traceDetail;
	}

	public void setTraceDetail(Object traceDetail) {
		this.traceDetail = traceDetail;
	}
}
