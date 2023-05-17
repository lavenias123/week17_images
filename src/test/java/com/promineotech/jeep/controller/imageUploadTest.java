package com.promineotech.jeep.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.InputStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


@SpringBootTest
@AutoConfigureMockMvc
class imageUploadTest {
	
	
	
	private static final String JEEP_IMAGE = "Jeep_Wrangler_Rubicon2D.jpeg";
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
		
	@Test
	void testThatServerCorrectlyRecievesAnImageAndReturnsOKResponse() 
			throws Exception {
		
		int numRows = JdbcTestUtils.countRowsInTable(jdbcTemplate, "images");
		
		// read img file from class path
		Resource image = new ClassPathResource(JEEP_IMAGE);
		
		if(!image.exists()) {
			fail("Couldn't find resource %s", JEEP_IMAGE);
		}
		
		try(InputStream inputStream = image.getInputStream()) {
			MockMultipartFile file = new MockMultipartFile("image", JEEP_IMAGE, MediaType.TEXT_PLAIN_VALUE, inputStream);
			
			// @formatter: off
			MvcResult result  = mockMvc
					.perform(MockMvcRequestBuilders
						.multipart("/jeeps/1/image")
						.file(file))
					.andDo(print())
					.andExpect(status().is(201))
					.andReturn();
			// @formatter: on
		
			String content = result.getResponse().getContentAsString();
			assertThat(content).isNotEmpty();
			
			
			assertThat(JdbcTestUtils.countRowsInTable(jdbcTemplate, "images"))
				.isEqualTo(numRows + 1);
		} // end try
	}

}
