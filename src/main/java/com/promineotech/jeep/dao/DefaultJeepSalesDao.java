package com.promineotech.jeep.dao;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;


//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.promineotech.jeep.entity.Image;
import com.promineotech.jeep.entity.ImageMimeType;
import com.promineotech.jeep.entity.Jeep;
import com.promineotech.jeep.entity.JeepModel;

import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class DefaultJeepSalesDao implements JeepSalesDao {
@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

/*
 * 
 */
@Override
public Optional<Image> retrieveImage(String imageId) {

	// @formatter: off
	String sql = ""
			+ "SELECT * "
			+ "FROM images "
			+ "WHERE image_id = :image_id";
	// @formatter: on
	
	Map<String, Object> params = new HashMap<>();
	params.put("image_id", imageId);

	
	
	return jdbcTemplate.query(sql, params, new ResultSetExtractor<>() {

		@Override
		public Optional<Image> extractData(ResultSet rs) 
				throws SQLException {
			if(rs.next()) {
				// @formatter: off
				return Optional.of(Image.builder()
						.imagePK(rs.getLong("image_pk"))
						.modelFK(rs.getLong("model_fk"))
						.imageId(rs.getString("image_id"))
						.width(rs.getInt("width"))
						.height(rs.getInt("height"))
						.mimeType(ImageMimeType.fromString(rs.getString("mime_type")))
						.name(rs.getString("name"))
						.data(rs.getBytes("data"))
						.build());
				// @formatter: on
			}
			
//					
		return Optional.empty();			
//					
		}});
	
} // ends Optional <Img>


	@Override
	public void saveImage(Image image) {
		String sql = ""
				+ "INSERT INTO images ("
				+ "model_fk, image_id, width, height, mime_type, name, data"
				+ ""
				+ ") VALUES ("
				+ ":model_fk, :image_id, :width, :height, :mime_type, :name, :data"
				+")";
		Map<String, Object> params = new HashMap<>();
		
		params.put("model_fk", image.getModelFK());
		params.put("image_id", image.getImageId());
		params.put("width", image.getWidth());
		params.put("height", image.getHeight());
		params.put("mime_type", image.getMimeType().getMimeType()); 
		params.put("name", image.getName());
		params.put("data", image.getData());
		
		// save it
		jdbcTemplate.update(sql, params);
	}

	@Override
	public List <Jeep> fetchJeeps(JeepModel model, String trim) {
		//  Use a log to test if it's actually calling the DAO when the test is run
		log.debug("DAO: model={}, trim={}", model, trim); 
	
		// @formatter:off
		String sql = "" 
				+ "SELECT * "
				+ "FROM models "
				+ "WHERE model_id = :model_id AND trim_level = :trim_level";
		// @formatter:on
		
		Map<String, Object> params = new HashMap<>();
		params.put("model_id", model.toString());
		params.put("trim_level", trim);
		
		
		
		return jdbcTemplate.query(sql, params, 
				new RowMapper<>() {
			@Override
			public Jeep mapRow(ResultSet rs, int rowNum) throws SQLException {
				// @formatter:off
				return Jeep.builder()
						.basePrice(new BigDecimal(rs.getString("base_price")))
						.modelId(JeepModel.valueOf(rs.getString("model_id")))
						.modelPK(rs.getLong("model_pk"))
						.numDoors(rs.getInt("num_doors"))
						.trimLevel(rs.getString("trim_level"))
						.wheelSize(rs.getInt("wheel_size"))
						.build();
				// @formatter:on
			}
			
		});
	}

	
	
}
