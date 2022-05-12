package com.mmt.flights.service.impl;

import com.mmt.flights.constants.FlightConstant;
import com.mmt.flights.domain.*;
import com.mmt.flights.exception.BadRequestException;
import com.mmt.flights.processor.FlightDataProcessor;
import com.mmt.flights.service.FlightService;
import com.mmt.flights.util.TimeUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class FlightServiceImpl implements FlightService {

	@Autowired
	private final FlightDataProcessor flightDataProcessor;

	@Override
	public List<FlightSearchResponse> getFlightsFromSourceToDestination(String startAirport, String endAirport) {

		Optional<AirportInfo> startAirportInfoOptional = flightDataProcessor.getAirportInfoFromShortCode(startAirport);
		Optional<AirportInfo> endAirportInfoOptional = flightDataProcessor.getAirportInfoFromShortCode(endAirport);

		List<FlightSearchResponse> flightSearchResponseList = new ArrayList<>();
		if (startAirportInfoOptional.isPresent() && endAirportInfoOptional.isPresent()) {
			AirportInfo startAirportInfo = startAirportInfoOptional.get();
			//Direct flights
			if (startAirportInfo.isConnectingAirportPresent(endAirport)) {
				List<FlightData> flightData = startAirportInfo.getFlightDataBasedOnDestinationAirport(endAirport);
				flightSearchResponseList.add(createFlightServiceResponse(
						startAirport+ FlightConstant.FLIGHT_DELIMIS+endAirport, flightData));
			}
			//Check of ONE STOP Flight
			Set<String> connectingAirports = startAirportInfo.getConnectingAirports();
			for (String airportShortCode : connectingAirports) {
				Optional<AirportInfo> connectingAirportInfoOptional = flightDataProcessor.getAirportInfoFromShortCode(airportShortCode);

				if (connectingAirportInfoOptional.isPresent()) {
					AirportInfo connectingAirportInfo = connectingAirportInfoOptional.get();
					if (connectingAirportInfo.isConnectingAirportPresent(endAirport)) {
						//Get list of all flights from start to this airport.
						List<FlightData> flightFromOriginToConnectingAirport = startAirportInfo.getFlightDataBasedOnDestinationAirport(airportShortCode);

						FlightRoutine flightRoutine = null;
						Long minTime = Long.MAX_VALUE;
						for (FlightData flightData : flightFromOriginToConnectingAirport) {

							LocalDateTime nextValidTime = TimeUtils.getNextValidTimeForConnectingFlight(flightData.getEndTime());

							List<FlightData> flightFromConnAirportToFinalAirport = connectingAirportInfo.getFlightDataBasedOnDestinationAirport(endAirport, nextValidTime);

							if (!flightFromConnAirportToFinalAirport.isEmpty()) {
								//Take time for start to connecting airport
								LocalDateTime startTime = startAirportInfo.giveTimeTakenByFlightFromFlightId(flightData.getFlightId());
								List<FlightRoutine> flightRoutines = createFlightRoutine(flightFromConnAirportToFinalAirport, startTime, flightData.getFlightId());
								if (flightRoutines.get(0).getTimeTaken() < minTime) {
									flightRoutine = flightRoutines.get(0);
									minTime = flightRoutines.get(0).getTimeTaken();
								}
							}
						}

						if(flightRoutine != null) {
							FlightSearchResponse flightSearchResponse = new FlightSearchResponse(startAirport+"_"+airportShortCode+"_"+endAirport, flightRoutine);
							flightSearchResponseList.add(flightSearchResponse);
						}
					}
				}
			}
		}

		Collections.sort(flightSearchResponseList);
		return flightSearchResponseList.subList(0, Math.min(flightSearchResponseList.size(), 5));
	}

	@Override
	public boolean validateAirport(String... airportId) {

		Stream.of(airportId).forEach(i -> {
			Optional<AirportInfo> airportInfo = flightDataProcessor.getAirportInfoFromShortCode(i);
			if (airportInfo.isEmpty()) {
				log.error("Airport {} not found", i);
				throw new BadRequestException("Airport not found: "+i, 400);
			}
		});
		return true;
	}

	private FlightSearchResponse createFlightServiceResponse(String airportShortCode, List<FlightData> flightDataList) {
		List<FlightRoutine> flightRoutines = createFlightRoutine(flightDataList, null, null);
		return new FlightSearchResponse(airportShortCode, flightRoutines.get(0));
	}

	private List<FlightRoutine> createFlightRoutine(List<FlightData> flightData, LocalDateTime time, String flightId) {
		List<FlightRoutine> flightRoutines = new ArrayList<>();
		final String validFlightId = flightId == null ? "" : flightId+"_";
		if (time != null) {
			flightData.forEach(i -> flightRoutines.add(FlightRoutine.builder().flightId(validFlightId+i.getFlightId().trim()).timeTaken(Duration.between(time, i.getEndTime()).toMinutes()).build()));
		}
		else {
			flightData.forEach(i -> flightRoutines.add(FlightRoutine.builder().flightId(validFlightId+i.getFlightId().trim()).timeTaken(Duration.between(i.getStartTime(), i.getEndTime()).toMinutes()).build()));
		}
		Collections.sort(flightRoutines);
		return flightRoutines;
	}
}
