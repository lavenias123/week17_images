package com.promineotech.jeep.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Sql(
		scripts = {"classpath:flyway/migrations/V1.0__Jeep_Schema.sql",
				"classpath:flyway/migrations/V1.1__Jeep_Data.sql"}, 
		config = @SqlConfig(encoding = "utf-8"))


class imageUploadTest {
	
	
	
	private static final String JEEP_IMAGE = "Jeep_Wrangler_Rubicon2D.jpeg";
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
		
	@Test
	void testThatServerCorrectlyReceivesAnImageAndReturnsOKResponse() 
			throws Exception {
		
		String json = assertImageUpload();
//		System.out.println("json = " + json);
		String imageId = extractImageId(json);
		
//		System.out.println("json = " + json);
//		System.out.println("imageId = " + imageId);
		
		assertImageRetrieval(imageId);

		
	}

	/**
	 * 
	 * @param imageId
	 * @throws Exception
	 */
	private void assertImageRetrieval(String imageId) throws Exception {
		// @formatter: off
				mockMvc
					.perform(get("/jeeps/image/" + imageId ))
					.andExpect(status().isOk())
					.andExpect(header().string("Content-Type", "image/jpeg"));
				// @formatter: on
	}

	/**
	 * 
	 * @param json
	 * @return
	 */
	private String extractImageId(String json) {
		
		// json = {"imageId":"c9a534d1-1f5c-4684-8e77-af4ebbf444ab"} remove to access ID
		// (1, is the opening ") - 1 is the closing "
		String[] parts = json.substring(1, json.length() - 1).split(":");
				
				return parts[1].substring(1, parts[1].length() - 1);
	}

	protected String assertImageUpload() throws IOException, Exception, UnsupportedEncodingException {
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
//					.andDo(print())
					.andExpect(status().is(201))
					.andReturn();
			// @formatter: on
		
			String content = result.getResponse().getContentAsString();
			assertThat(content).isNotEmpty();
			
			
			assertThat(JdbcTestUtils.countRowsInTable(jdbcTemplate, "images"))
				.isEqualTo(numRows + 1);
			
			return content;
		} // end try
	}

}
