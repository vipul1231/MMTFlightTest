package com.mmt.flights.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Duration;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
public class FlightData {

	private String flightId;

	private String startAirport;

	private String endAirport;

	private LocalDateTime startTime;

	private LocalDateTime endTime;

	public long durationOfFlight(){
		return Duration.between(startTime, endTime).toMinutes();
	}
}
