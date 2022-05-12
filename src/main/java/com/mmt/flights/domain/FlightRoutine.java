package com.mmt.flights.domain;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FlightRoutine implements Comparable<FlightRoutine>{

	String flightId;

	Long timeTaken;

	@Override
	public int compareTo(FlightRoutine o) {
		if (o != null) {
			if (o.getTimeTaken() > this.getTimeTaken()) {
				return -1;
			}
			else if (o.getTimeTaken() < this.getTimeTaken()) {
				return 1;
			}
		}
		return 0;
	}


}
