package com.promineotech.jeep.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowCallbackHandler;

import com.promineotech.jeep.entity.Color;
import com.promineotech.jeep.entity.Customer;
import com.promineotech.jeep.entity.Tire;

public class ColorResultSetExtractor implements ResultSetExtractor<Color> {

	public Color extractData(ResultSet rs) 
			throws SQLException, DataAccessException {
		rs.next();
		//formatter:off
		return Color.builder()
				.color(rs.getString("color"))
				.colorId(rs.getString("color_id"))
				.colorPK(rs.getLong("color_pk"))
				.isExterior(rs.getBoolean("is_exterior"))
				.price(rs.getBigDecimal("price"))
				.build();	
		//formatter:on
	}

}
