package com.mmt.flights.domain;

import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This domain contains airport data and its originating flights along with connecting airports.
 */
@Getter
@ToString
public class AirportInfo {

	private final String airportName;

	private final List<FlightData> originatingFlights;

	private final Set<String> connectingAirports;

	public AirportInfo(String airportName) {
		this.airportName = airportName;
		originatingFlights = new ArrayList<>();
		connectingAirports = new HashSet<>();
	}

	public void addFlights(FlightData flightData) {
		originatingFlights.add(flightData);
		connectingAirports.add(flightData.getEndAirport());
	}

	public boolean isConnectingAirportPresent(String airportShortCode) {
		return connectingAirports.contains(airportShortCode);
	}

	public List<FlightData> getFlightDataBasedOnDestinationAirport(String airportShortCode) {
		return originatingFlights.stream().filter(i -> i.getEndAirport().equals(airportShortCode)).collect(Collectors.toList());
	}

	public List<FlightData> getFlightDataBasedOnDestinationAirport(String airportShortCode, LocalDateTime time) {
		return originatingFlights.stream().filter(i -> i.getEndAirport().equals(airportShortCode) && i.getStartTime().isAfter(time)).collect(Collectors.toList());
	}

	public LocalDateTime giveTimeTakenByFlightFromFlightId(String flightId) {
		Optional<FlightData> optional= originatingFlights.stream().filter(i -> i.getFlightId().equals(flightId)).findFirst();
		if (optional.isPresent()) {
			FlightData flightData = optional.get();
			return flightData.getStartTime();
		}
		//This should be the case, handle properly.
		return null;
	}
}
