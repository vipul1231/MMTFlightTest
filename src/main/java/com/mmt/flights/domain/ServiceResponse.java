package com.mmt.flights.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceResponse {

	private Object response;

	private String exceptionMessage;

	private long timeStamp;
}
