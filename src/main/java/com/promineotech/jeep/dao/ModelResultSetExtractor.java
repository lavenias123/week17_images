package com.promineotech.jeep.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowCallbackHandler;

import com.promineotech.jeep.entity.Color;
import com.promineotech.jeep.entity.Jeep;
import com.promineotech.jeep.entity.JeepModel;

public class ModelResultSetExtractor implements ResultSetExtractor<Jeep> {

	public Jeep extractData(ResultSet rs) 
			throws SQLException, DataAccessException {
		rs.next();
		//formatter:off
		return Jeep.builder()
				.basePrice(rs.getBigDecimal("base_price"))
				.modelId(JeepModel.valueOf(rs.getString("model_id")))
				.modelPK(rs.getLong("model_pk"))
				.numDoors(rs.getInt("num_doors"))
				.trimLevel(rs.getString("trim_level"))
				.wheelSize(rs.getInt("wheel_size"))
				.build();	
		//formatter:on
	}

}
