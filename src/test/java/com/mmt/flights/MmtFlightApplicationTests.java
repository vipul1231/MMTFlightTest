package com.mmt.flights;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmt.flights.domain.FlightSearchResponse;
import com.mmt.flights.domain.ServiceResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MmtFlightApplicationTests {

	@Autowired
	private TestRestTemplate testRestTemplate;

	private ObjectMapper objectMapper;

	@BeforeEach
	public void before() {
		objectMapper = new ObjectMapper();
		objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
	}

	@Test
	void testFlightRequest() throws JsonProcessingException {

		ResponseEntity<String> response = testRestTemplate.getForEntity(URI.create("/flights/get-route?startAirport=IXC&endAirport=GAU"), String.class);
		ServiceResponse serviceResponses =  objectMapper.readValue(response.getBody(), ServiceResponse.class);
		Assertions.assertEquals(200, response.getStatusCodeValue());
		Assertions.assertNotNull(serviceResponses);

		List<FlightSearchResponse> flightResponse = (List<FlightSearchResponse>) serviceResponses.getResponse();
		String json = objectMapper.writeValueAsString(flightResponse);
		JsonNode actualJson = objectMapper.readTree(json);
		JsonNode expectedJson = objectMapper.readTree(responseForIXCToGAU());
		Assertions.assertEquals(actualJson, expectedJson);


		//Test More route
		response = testRestTemplate.getForEntity(URI.create("/flights/get-route?startAirport=ATQ&endAirport=BLR"), String.class);
		serviceResponses =  objectMapper.readValue(response.getBody(), ServiceResponse.class);
		Assertions.assertEquals(200, response.getStatusCodeValue());
		Assertions.assertNotNull(serviceResponses);

		flightResponse = (List<FlightSearchResponse>) serviceResponses.getResponse();
		json = objectMapper.writeValueAsString(flightResponse);
		actualJson = objectMapper.readTree(json);
		expectedJson = objectMapper.readTree(responseForATQToBLR());
		Assertions.assertEquals(actualJson, expectedJson);
	}

	@Test
	void testNegativeScenarioForInvalidStartAirport() throws JsonProcessingException {
		ResponseEntity<String> response = testRestTemplate.getForEntity(URI.create("/flights/get-route?startAirport=IRDC&endAirport=GAU"), String.class);
		ServiceResponse serviceResponses =  objectMapper.readValue(response.getBody(), ServiceResponse.class);
		Assertions.assertEquals(400, response.getStatusCodeValue());
		Assertions.assertEquals("Airport not found: IRDC", serviceResponses.getExceptionMessage());
		Assertions.assertNotNull(serviceResponses);
	}

	@Test
	void testNegativeScenarioForInvalidEndAirport() throws JsonProcessingException {
		ResponseEntity<String> response = testRestTemplate.getForEntity(URI.create("/flights/get-route?startAirport=ATQ&endAirport=GAUD"), String.class);
		ServiceResponse serviceResponses =  objectMapper.readValue(response.getBody(), ServiceResponse.class);
		Assertions.assertEquals(400, response.getStatusCodeValue());
		Assertions.assertEquals("Airport not found: GAUD", serviceResponses.getExceptionMessage());
		Assertions.assertNotNull(serviceResponses);
	}

	private String responseForIXCToGAU() {
		return "[" +
				"        {" +
				"            \"sourceToDestination\": \"IXC_GAU\"," +
				"            \"response\": {" +
				"                \"flightId\": \"717\"," +
				"                \"timeTaken\": 220" +
				"            }" +
				"        }," +
				"        {" +
				"            \"sourceToDestination\": \"IXC_DEL_GAU\"," +
				"            \"response\": {" +
				"                \"flightId\": \"2152_694\"," +
				"                \"timeTaken\": 335" +
				"            }" +
				"        }," +
				"        {" +
				"            \"sourceToDestination\": \"IXC_CCU_GAU\"," +
				"            \"response\": {" +
				"                \"flightId\": \"376_833\"," +
				"                \"timeTaken\": 495" +
				"            }" +
				"        }," +
				"        {" +
				"            \"sourceToDestination\": \"IXC_BLR_GAU\"," +
				"            \"response\": {" +
				"                \"flightId\": \"591_457\"," +
				"                \"timeTaken\": 760" +
				"            }" +
				"        }," +
				"        {" +
				"            \"sourceToDestination\": \"IXC_HYD_GAU\"," +
				"            \"response\": {" +
				"                \"flightId\": \"615_6538\"," +
				"                \"timeTaken\": 795" +
				"            }" +
				"        }" +
				"    ]";
	}

	public String responseForATQToBLR() {
		return "[" +
				"        {" +
				"            \"sourceToDestination\": \"ATQ_BLR\"," +
				"            \"response\": {" +
				"                \"flightId\": \"6845\"," +
				"                \"timeTaken\": 185" +
				"            }" +
				"        }," +
				"        {" +
				"            \"sourceToDestination\": \"ATQ_DEL_BLR\"," +
				"            \"response\": {" +
				"                \"flightId\": \"2057_819\"," +
				"                \"timeTaken\": 365" +
				"            }" +
				"        }," +
				"        {" +
				"            \"sourceToDestination\": \"ATQ_BOM_BLR\"," +
				"            \"response\": {" +
				"                \"flightId\": \"6261_283\"," +
				"                \"timeTaken\": 475" +
				"            }" +
				"        }," +
				"        {" +
				"            \"sourceToDestination\": \"ATQ_CCU_BLR\"," +
				"            \"response\": {" +
				"                \"flightId\": \"5926_932\"," +
				"                \"timeTaken\": 555" +
				"            }" +
				"        }," +
				"        {" +
				"            \"sourceToDestination\": \"ATQ_PNQ_BLR\"," +
				"            \"response\": {" +
				"                \"flightId\": \"286_954\"," +
				"                \"timeTaken\": 1120" +
				"            }" +
				"        }" +
				"    ]";
	}
}
