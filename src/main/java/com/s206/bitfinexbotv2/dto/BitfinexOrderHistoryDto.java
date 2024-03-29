package com.s206.bitfinexbotv2.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.math.BigDecimal;
import java.sql.Timestamp;

@JsonFormat(shape = JsonFormat.Shape.ARRAY)
@JsonPropertyOrder({"id", "symbol", "createTime", "updateTime", "amount", "originalAmount", "type", "placeHolder0", "placeHolder1",
"flags", "status", "placeHolder2", "placeHolder3", "placeHolder4", "rate", "period", "notify", "hidden", "placeHolder5", "renew", "placeHolder6"})
public class BitfinexOrderHistoryDto {
	private String id;
	private String symbol;
	private Timestamp createTime;
	private Timestamp updateTime;
	private BigDecimal amount;
	private BigDecimal originalAmount;
	private String type;
	private Object placeHolder0;
	private Object placeHolder1;
	private Object flags;
	private String status;
	private Object placeHolder2;
	private Object placeHolder3;
	private Object placeHolder4;
	private BigDecimal rate;
	private Integer period;
	private Integer notify;
	private Integer hidden;
	private Object placeHolder5;

	private Integer renew;
	private Object placeHolder6;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getOriginalAmount() {
		return originalAmount;
	}

	public void setOriginalAmount(BigDecimal originalAmount) {
		this.originalAmount = originalAmount;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Object getPlaceHolder0() {
		return placeHolder0;
	}

	public void setPlaceHolder0(Object placeHolder0) {
		this.placeHolder0 = placeHolder0;
	}

	public Object getPlaceHolder1() {
		return placeHolder1;
	}

	public void setPlaceHolder1(Object placeHolder1) {
		this.placeHolder1 = placeHolder1;
	}

	public Object getFlags() {
		return flags;
	}

	public void setFlags(Object flags) {
		this.flags = flags;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Object getPlaceHolder2() {
		return placeHolder2;
	}

	public void setPlaceHolder2(Object placeHolder2) {
		this.placeHolder2 = placeHolder2;
	}

	public Object getPlaceHolder3() {
		return placeHolder3;
	}

	public void setPlaceHolder3(Object placeHolder3) {
		this.placeHolder3 = placeHolder3;
	}

	public Object getPlaceHolder4() {
		return placeHolder4;
	}

	public void setPlaceHolder4(Object placeHolder4) {
		this.placeHolder4 = placeHolder4;
	}

	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	public Integer getPeriod() {
		return period;
	}

	public void setPeriod(Integer period) {
		this.period = period;
	}

	public Integer getNotify() {
		return notify;
	}

	public void setNotify(Integer notify) {
		this.notify = notify;
	}

	public Integer getHidden() {
		return hidden;
	}

	public void setHidden(Integer hidden) {
		this.hidden = hidden;
	}

	public Object getPlaceHolder5() {
		return placeHolder5;
	}

	public void setPlaceHolder5(Object placeHolder5) {
		this.placeHolder5 = placeHolder5;
	}

	public Integer getRenew() {
		return renew;
	}

	public void setRenew(Integer renew) {
		this.renew = renew;
	}

	public Object getPlaceHolder6() {
		return placeHolder6;
	}

	public void setPlaceHolder6(Object placeHolder6) {
		this.placeHolder6 = placeHolder6;
	}
}
