package com.mmt.flights.exception;

public class BadRequestException extends RuntimeException{

	private int responseCode;

	public BadRequestException(String msg, int responseCode) {
		super(msg);
		this.responseCode = responseCode;
	}
}
