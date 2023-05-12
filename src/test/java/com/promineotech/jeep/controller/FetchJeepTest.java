package com.promineotech.jeep.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.doThrow;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

import com.promineotech.jeep.Constants;
import com.promineotech.jeep.controller.support.FetchTestJeepSupport;
import com.promineotech.jeep.entity.Jeep;
import com.promineotech.jeep.entity.JeepModel;
import com.promineotech.jeep.service.JeepSalesService;





class FetchJeepTest {
	
	@Nested
	@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
	@ActiveProfiles("test")
	@Sql(
	   scripts = {"classpath:flyway/migrations/V1.0__Jeep_Schema.sql",
	              "classpath:flyway/migrations/V1.1__Jeep_Data.sql"},
	   config = @SqlConfig(encoding = "utf-8"))
	
	class TestsThatDoNotPolluteTheApplicationContext extends FetchTestJeepSupport {
		
		@Test

//		TestRestTemplate 
		void testThatAnErrorMessageIsReturnedWhenAUnknownTrimIsSupplied() {
//			Given: a valid model, trim, & Uri
			JeepModel model = JeepModel.WRANGLER;
			String trim = "Unknown Value";
			String uri = 
					String.format("%s?model=%s&trim=%s", getBaseUriForJeeps(), model, trim);

//	    	When: a connection is made to the URI

			ResponseEntity<Map<String, Object>> response = 
					getRestTemplate().exchange(uri, HttpMethod.GET, null, 
							new ParameterizedTypeReference<>() {});

			
			// THEN: A not found (404) is returned
			assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
			
			// And: AN err message is returned
			Map<String, Object> error = response.getBody();
			
			assertErrorMessageValid(error, HttpStatus.NOT_FOUND);
			
//			@formatter:on
		}  // close testThatAnErrorMessageIsReturnedWhenAUnknownTrimIsSupplied
		
		
		@ParameterizedTest
		@MethodSource("com.promineotech.jeep.controller.FetchJeepTest#parametersForInvalidInput")
//		TestRestTemplate 
		void testThatAnErrorMessageIsReturnedWhenAInValidValueIsSupplied(String model, String trim, String reason) {
//			GIVEN: A VALID MODEL, TRIM AND, URI
			
			String uri = 
					String.format("%s?model=%s&trim=%s", getBaseUriForJeeps(), model, trim);

//	    	When: a connection is made to the URI

			ResponseEntity<Map<String, Object>> response = 
					getRestTemplate().exchange(uri, HttpMethod.GET, null, 
							new ParameterizedTypeReference<>() {});

			
			// THEN: A not found (404) is returned
			assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
			
			// And: AN err message is returned
			Map<String, Object> error = response.getBody();
			
			assertErrorMessageValid(error, HttpStatus.BAD_REQUEST);
		}
		
		@Test

//		TestRestTemplate 
		void testThatJeepsAreReturnedWhenAValidModelAndTrimAreSupplied() {
//			Given: a valid model, trim, & Uri
			JeepModel model = JeepModel.WRANGLER;
			String trim = "Sport";
			String uri = 
					String.format("%s?model=%s&trim=%s", getBaseUriForJeeps(), model, trim);

//	    	When: a connection is made to the URI

			ResponseEntity<List<Jeep>> response = 
					getRestTemplate().exchange(uri, HttpMethod.GET, null, 
							new ParameterizedTypeReference<>() {});

			
			// THEN: A SUCCESS (OK-200) is returned
			assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
			
			// And: the actual list returned is the same as expected list
			List<Jeep> actual = response.getBody();
			List<Jeep> expected = buildExpected();
			
			
			assertThat(actual).isEqualTo(expected);
			
			
		}  // close testThatJeepsAreReturnedWhenAValidModelAndTrimAreSupplied 
	} // close 1st inner class ---  TestsThatDoNotPolluteTheApplicationContext
	
	static Stream<Arguments> parametersForInvalidInput() {
		//Formatter:off
		return Stream.of(
			arguments("WRANGLER", "**(*(*(_&&*", "Trim returns non-alpha chars"),	
			arguments("WRANGLER", "C".repeat(Constants.TRIM_MAX_LENGTH + 1), "Trim length too long."),
			arguments("INVALID", "Sport", "Model is not an enum value.")
			
		//Formatter:on
				);
		
	}
	
	@Nested
	@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
	@ActiveProfiles("test")
	@Sql(
	   scripts = {"classpath:flyway/migrations/V1.0__Jeep_Schema.sql",
	              "classpath:flyway/migrations/V1.1__Jeep_Data.sql"},
	   config = @SqlConfig(encoding = "utf-8"))
	class TestsThatPolluteTheApplicationContext extends FetchTestJeepSupport {
		// create the mock bean
		@MockBean
//		private JeepSalesService jeeepSalesService;
		public JeepSalesService jeepSalesService;

		/**
		 * 
		 */
		@Test

//		TestRestTemplate 
		void testThatAnUnplannedErrorResultsInA500Status() {
//			Given: a valid model, trim, & Uri
			JeepModel model = JeepModel.WRANGLER;
			String trim = "Invalid";
			String uri = 
					String.format("%s?model=%s&trim=%s", getBaseUriForJeeps(), model, trim);

			doThrow(new RuntimeException("Ouch!")).when(jeepSalesService).fetchJeeps(model, trim);
//	    	When: a connection is made to the URI
//
			ResponseEntity<Map<String, Object>> response = 
					getRestTemplate().exchange(uri, HttpMethod.GET, null, 
							new ParameterizedTypeReference<>() {});


//			// THEN: An Internal_Server_Error (500) is returned
			assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
			
			// And: AN err message is returned
			Map<String, Object> error = response.getBody();
			
			assertErrorMessageValid(error, HttpStatus.INTERNAL_SERVER_ERROR);
			
		}  // close testThatAnUnplannedErrorResultsInA500Status

	
		
		
	} // close 2nd inner class --- TestsThatPolluteTheApplicaationContext
	

	
	


} // close  FetchJeepTest
