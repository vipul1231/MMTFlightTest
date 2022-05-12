package com.mmt.flights.controller;

import com.mmt.flights.domain.FlightSearchResponse;
import com.mmt.flights.domain.ServiceResponse;
import com.mmt.flights.exception.BadRequestException;
import com.mmt.flights.service.FlightService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/flights")
@RequiredArgsConstructor
public class FlightController {

	private final FlightService flightService;

	@GetMapping(value = "/get-route", produces = "application/json")
	public ResponseEntity<ServiceResponse> getFlightData(@RequestParam String startAirport, @RequestParam String endAirport) {
		//Validate
		String[] airportArray ={startAirport, endAirport};
		flightService.validateAirport(airportArray);

		//Now do search
		List<FlightSearchResponse> response = flightService.getFlightsFromSourceToDestination(startAirport, endAirport);
		return new ResponseEntity<>(ServiceResponse.builder().response(response).timeStamp(Instant.now().getEpochSecond()).build(), HttpStatus.OK);
	}

	@ExceptionHandler
	public ResponseEntity<ServiceResponse> handleException(Exception e) {

		if (e instanceof BadRequestException) {
			return new ResponseEntity<>(ServiceResponse.builder().exceptionMessage(e.getMessage()).timeStamp(Instant.now().getEpochSecond()).build(), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(ServiceResponse.builder().exceptionMessage(e.getMessage()).timeStamp(Instant.now().getEpochSecond()).build(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
