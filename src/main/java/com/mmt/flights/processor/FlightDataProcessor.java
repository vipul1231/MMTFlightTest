package com.mmt.flights.processor;

import com.mmt.flights.config.ReadCSV;
import com.mmt.flights.domain.AirportInfo;
import com.mmt.flights.domain.FlightData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * This class read data from ReadCSV create AirportInfo map; Key is the airport short code
 */

@Component
@ConditionalOnBean(ReadCSV.class)
public class FlightDataProcessor {

	@Autowired
	private ReadCSV readCSV;

	private final Map<String, AirportInfo> airportDataMap = new HashMap<>();

	@PostConstruct
	public void processFlightData() {
		List<FlightData> flightDataList = readCSV.getFlightData();

		for (FlightData flightData: flightDataList) {
			AirportInfo airportInfo = airportDataMap.get(flightData.getStartAirport());
			if (airportInfo == null) {
				airportInfo = new AirportInfo(flightData.getStartAirport());
				airportInfo.addFlights(flightData);
				airportDataMap.put(flightData.getStartAirport(), airportInfo);
			}
			else {
				airportInfo.addFlights(flightData);
			}
		}
	}

	public Optional<AirportInfo> getAirportInfoFromShortCode(String airportShortCode) {
		return Optional.ofNullable(airportDataMap.get(airportShortCode));
	}
}
