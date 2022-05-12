package com.mmt.flights.config;


import com.mmt.flights.domain.FlightData;
import com.mmt.flights.util.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class ReadCSV {

	private final List<FlightData> flightData = new ArrayList<>();

	@PostConstruct
	public void init() throws IOException {
		LocalDate localDate = LocalDate.now();

		ClassPathResource resource = new ClassPathResource("ivtest-sched.csv");
		InputStream inputStream = resource.getInputStream();

		InputStreamReader streamReader = new InputStreamReader(inputStream);
		BufferedReader reader = new BufferedReader(streamReader);
		int count = 0;


		for (String line; (line = reader.readLine()) != null;) {
			String[] array = line.split(",");
			boolean isAddDay = false;
			int  start= Integer.parseInt(array[3]);
			int  end = Integer.parseInt(array[4]);

			if (start > end) {
				isAddDay = true;
			}

			flightData.add(FlightData.builder().flightId(array[0])
					.startAirport(array[1])
					.endAirport(array[2])
					.startTime(TimeUtils.getLocalDateTime(array[3].trim(), 0))
					.endTime(TimeUtils.getLocalDateTime(array[4].trim(), isAddDay ? 1 : 0)).build());

			flightData.add(FlightData.builder().flightId(array[0])
					.startAirport(array[1])
					.endAirport(array[2])
					.startTime(TimeUtils.getLocalDateTime(array[3].trim(), 1))
					.endTime(TimeUtils.getLocalDateTime(array[4].trim(), isAddDay ? 2 : 1)).build());
			count++;
		}
		System.out.println("Count: "+count);
	}



	public List<FlightData> getFlightData() {
		return flightData;
	}
}
