package com.s206.bitfinexbotv2.dto.request;

import java.math.BigDecimal;
import java.util.List;

public class ArbitrageRequest {

	private List<Odds> oddsList;
	private BigDecimal baseMoney;

	public List<Odds> getOddsList() {
		return oddsList;
	}

	public void setOddsList(List<Odds> oddsList) {
		this.oddsList = oddsList;
	}

	public BigDecimal getBaseMoney() {
		return baseMoney;
	}

	public void setBaseMoney(BigDecimal baseMoney) {
		this.baseMoney = baseMoney;
	}

	public static class Odds{
		private BigDecimal homeOdds;
		private BigDecimal awayOdds;

		public BigDecimal getHomeOdds() {
			return homeOdds;
		}

		public void setHomeOdds(BigDecimal homeOdds) {
			this.homeOdds = homeOdds;
		}

		public BigDecimal getAwayOdds() {
			return awayOdds;
		}

		public void setAwayOdds(BigDecimal awayOdds) {
			this.awayOdds = awayOdds;
		}
	}
}
