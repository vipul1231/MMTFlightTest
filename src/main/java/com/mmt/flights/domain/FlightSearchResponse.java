package com.mmt.flights.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class FlightSearchResponse implements Comparable<FlightSearchResponse> {

	private String sourceToDestination;

	private FlightRoutine response;

	public FlightSearchResponse(String sourceToDestination, FlightRoutine flightRoutine) {
		this.sourceToDestination = sourceToDestination;
		this.response = flightRoutine;
	}

	@Override
	public int compareTo(FlightSearchResponse o) {
		if (o.getResponse().getTimeTaken() > this.response.getTimeTaken()) {
			return -1;
		}
		else if(o.getResponse().getTimeTaken() < this.response.getTimeTaken()) {
			return 1;
		}
		return 0;
	}
}
