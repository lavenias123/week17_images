package com.promineotech.jeep.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowCallbackHandler;

import com.promineotech.jeep.entity.Jeep;
import com.promineotech.jeep.entity.JeepModel;
import com.promineotech.jeep.entity.Order;

public class OrderResultSetExtractor implements ResultSetExtractor<Order>  {

	@Override
	public Order extractData(ResultSet rs) throws SQLException, DataAccessException {
		return null;
	}
	
	
	// ==============Order.builder() isn't required ==================
//	public Order extractData(ResultSet rs) 
//			throws SQLException, DataAccessException {
//		rs.next();
//		//formatter:off
//		return Order.builder()
//				.orderPK(rs.getLong("order_pk"))
//				.customerFK(rs.getLong("customer_fk"))
//				.colorFK(rs.getInt("color_fk"))
//				.engineFK(rs.getInt("engine_fk"))
//				.tireFK(rs.getInt("tire_fk"))
//				.modelFK(rs.getInt("model_fk"))
//				.price(rs.getBigDecimal("price"))
//				.build();	
//		//formatter:on
//	}

}
/*
 * order_pk int unsigned NOT NULL AUTO_INCREMENT,
   customer_fk int unsigned NOT NULL,
   color_fk int unsigned NOT NULL,
   engine_fk int unsigned NOT NULL,
   tire_fk int unsigned NOT NULL,
   model_fk int unsigned NOT NULL,
   price decimal(9, 2) NOT NULL,
 */
