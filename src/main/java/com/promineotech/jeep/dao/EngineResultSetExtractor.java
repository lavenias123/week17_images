package com.promineotech.jeep.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowCallbackHandler;

import com.promineotech.jeep.entity.Engine;
import com.promineotech.jeep.entity.FuelType;
import com.promineotech.jeep.entity.Tire;

public class EngineResultSetExtractor implements ResultSetExtractor<Engine> {
	public Engine extractData(ResultSet rs) 
			throws SQLException, DataAccessException {
		rs.next();
		//formatter:off
		return Engine.builder()
				.description(rs.getString("description"))
				.engineId(rs.getString("engine_id"))
				.enginePK(rs.getLong("engine_pk"))
				.fuelType(FuelType.valueOf(rs.getString("fuel_type")))
				.hasStartStop(rs.getBoolean("has_start_stop"))
				.mpgCity(rs.getFloat("mpg_City"))
				.mpgHwy(rs.getFloat("mpg_hwy"))
				.name(rs.getString("name"))
				.price(rs.getBigDecimal("price"))
				.sizeInLiters(rs.getFloat("size_in_liters"))
				.build();	
	}	
	
/**

 */
}
