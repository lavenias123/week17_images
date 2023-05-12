package com.promineotech.jeep.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowCallbackHandler;
import com.promineotech.jeep.entity.Tire;


public class TireResultSetExtractor implements ResultSetExtractor<Tire> {
	public Tire extractData(ResultSet rs) 
			throws SQLException, DataAccessException {
		rs.next();
		//formatter:off
		return Tire.builder()
				.tireId(rs.getString("tire_id"))
				.tirePK(rs.getLong("tire_pk"))
				.tireSize(rs.getString("tire_size"))
				.manufacturer(rs.getString("manufacturer"))
				.price(rs.getBigDecimal("price"))
				.warrantyMiles(rs.getInt("warranty_miles"))
				.build();	
	}	
	
}	
	
	//public class TireResultSetExtractor implements RowCallbackHandler {
	//	public void processRow(ResultSet rs) throws SQLException {
//		// TODO Auto-generated method stub
//
//	}


