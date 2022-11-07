package com.s206.bitfinexbotv2.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.lang.Nullable;

import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArbitrageResponse {

	private String message;
	private BigDecimal percentageOfProfit;
	private BigDecimal profit;
	private BigDecimal winChance;
	private BigDecimal homeOddsSelection;
	private BigDecimal awayOddsSelection;
	private BigDecimal homePlacement;
	private BigDecimal awayPlacement;

	public BigDecimal getHomePlacement() {
		return homePlacement;
	}

	public void setHomePlacement(BigDecimal homePlacement) {
		this.homePlacement = homePlacement;
	}

	public BigDecimal getAwayPlacement() {
		return awayPlacement;
	}

	public void setAwayPlacement(BigDecimal awayPlacement) {
		this.awayPlacement = awayPlacement;
	}

	public BigDecimal getProfit() {
		return profit;
	}

	public void setProfit(BigDecimal profit) {
		this.profit = profit;
	}

	public BigDecimal getHomeOddsSelection() {
		return homeOddsSelection;
	}

	public void setHomeOddsSelection(BigDecimal homeOddsSelection) {
		this.homeOddsSelection = homeOddsSelection;
	}

	public BigDecimal getAwayOddsSelection() {
		return awayOddsSelection;
	}

	public void setAwayOddsSelection(BigDecimal awayOddsSelection) {
		this.awayOddsSelection = awayOddsSelection;
	}

	public BigDecimal getWinChance() {
		return winChance;
	}

	public void setWinChance(BigDecimal winChance) {
		this.winChance = winChance;
	}

	public BigDecimal getPercentageOfProfit() {
		return percentageOfProfit;
	}

	public void setPercentageOfProfit(BigDecimal percentageOfProfit) {
		this.percentageOfProfit = percentageOfProfit;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
