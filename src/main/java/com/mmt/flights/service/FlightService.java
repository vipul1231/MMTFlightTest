package com.mmt.flights.service;

import com.mmt.flights.domain.FlightSearchResponse;

import java.util.List;

public interface FlightService {

	public List<FlightSearchResponse> getFlightsFromSourceToDestination(String startAirport, String endAirport);

	public boolean validateAirport(String... airportId);
}
