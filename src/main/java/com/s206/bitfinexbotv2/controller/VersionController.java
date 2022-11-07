package com.s206.bitfinexbotv2.controller;

import com.s206.bitfinexbotv2.dto.request.ArbitrageRequest;
import com.s206.bitfinexbotv2.dto.response.ArbitrageResponse;
import com.s206.bitfinexbotv2.service.CalculateService;
import com.s206.bitfinexbotv2.service.MarginFundingService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@RestController
public class VersionController {

	@Autowired
	private MarginFundingService service;



	@RequestMapping(value = "version", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> version() throws Exception {


		return new ResponseEntity<>("1.0.0", HttpStatus.OK);
	}

	@RequestMapping(value = "arbitrage", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ArbitrageResponse> arbitrageCaculate(@RequestBody ArbitrageRequest request){

		ArbitrageResponse response = new ArbitrageResponse();

		BigDecimal percentageOfProfit = new BigDecimal(0);

		BigDecimal one = new BigDecimal(1);

		BigDecimal homeOdds = new BigDecimal(0);
		BigDecimal awayOdds = new BigDecimal(0);
//		if(request.getGameAOdds1().compareTo(request.getGameBOdds1()) > 0){
//			base1Odds = request.getGameAOdds1();
//		}
//		else{
//			base1Odds = request.getGameBOdds1();
//		}
//		if(request.getGameAOdds2().compareTo(request.getGameBOdds2()) > 0){
//			base2Odds = request.getGameAOdds2();
//		}
//		else{
//			base2Odds = request.getGameBOdds2();
//		}

		for(int i = 0; i < request.getOddsList().size(); i++){
			ArbitrageRequest.Odds odds = request.getOddsList().get(i);

			if(odds.getHomeOdds().compareTo(homeOdds) > 0){
				homeOdds = odds.getHomeOdds();
			}
			if(odds.getAwayOdds().compareTo(awayOdds) > 0){
				awayOdds = odds.getAwayOdds();
			}

		}


		/*
		 win chance = 1/ base1Odds + 1/ base2Odds
		 */
		BigDecimal winChance = one.divide(homeOdds,4, RoundingMode.DOWN).add(one.divide(awayOdds, 4, RoundingMode.DOWN));
		response.setWinChance(winChance);
		if(winChance.compareTo(one) < 0){
			percentageOfProfit = one.subtract(winChance);
			response.setPercentageOfProfit(percentageOfProfit);
			response.setHomeOddsSelection(homeOdds);
			response.setAwayOddsSelection(awayOdds);

			BigDecimal profit = request.getBaseMoney().multiply(percentageOfProfit);
			response.setProfit(profit);

			BigDecimal homePlacement = request.getBaseMoney().add(profit).divide(homeOdds,4, RoundingMode.DOWN);
			BigDecimal awayPlacement = request.getBaseMoney().add(profit).divide(awayOdds,4, RoundingMode.DOWN);
			response.setHomePlacement(homePlacement);
			response.setAwayPlacement(awayPlacement);
		}
		else{
			// no need to calculate
		}

		return new ResponseEntity<>(response, HttpStatus.OK);

	}

}
