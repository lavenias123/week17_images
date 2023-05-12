package com.promineotech.jeep.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// import from which pkg 
import com.promineotech.jeep.dao.JeepOrderDao;
import com.promineotech.jeep.entity.Color;
//import com.promineotech.jeep.entity.Customer;
import com.promineotech.jeep.entity.Customer;
import com.promineotech.jeep.entity.Engine;
import com.promineotech.jeep.entity.Jeep;
import com.promineotech.jeep.entity.Option;
import com.promineotech.jeep.entity.Order;
import com.promineotech.jeep.entity.OrderRequest;
import com.promineotech.jeep.entity.Tire;


@Service
public class DefaultJeepOrderService implements JeepOrderService {

	@Autowired
	private JeepOrderDao jeepOrderDao;
	
	@Transactional
	@Override
	public Order createOrder(OrderRequest orderRequest) {
		// fetch all records
		Customer customer = getCustomer(orderRequest);
		Jeep jeep = getModel(orderRequest);
		Color color = getColor(orderRequest);
		Engine engine = getEngine(orderRequest); 
		Tire tire = getTire(orderRequest); 
		List<Option> options = getOption(orderRequest);
		
		BigDecimal price = jeep.getBasePrice().add(color.getPrice()).add(engine.getPrice()).add(tire.getPrice());
		
		for(Option option : options) {
			price = price.add(option.getPrice());
		}
		
		return jeepOrderDao.saveOrder(customer, jeep, color, engine, tire, price, options);
	}

	private List<Option> getOption(OrderRequest orderRequest) {
		return jeepOrderDao.fetchOptions(orderRequest.getOptions());
		
		
	}

	protected Tire getTire(OrderRequest orderRequest) {
		return jeepOrderDao
				.fetchTire(orderRequest.getTire())
				.orElseThrow(() -> new NoSuchElementException("Tire was not found"
						+ orderRequest.getTire() + "was not found"));
	}

	protected Engine getEngine(OrderRequest orderRequest) {
		return jeepOrderDao
				.fetchEngine(orderRequest.getEngine())
				.orElseThrow(() -> new NoSuchElementException("Engine was not found"
						+ orderRequest.getEngine() + "was not found"));
	}

	protected Color getColor(OrderRequest orderRequest) {
		return jeepOrderDao
				.fetchColor(orderRequest.getColor())
				.orElseThrow(() -> new NoSuchElementException("Color was not found"
						+ orderRequest.getColor() + "was not found"));
	}

	protected Jeep getModel(OrderRequest orderRequest) {
		return jeepOrderDao
				.fetchModel(orderRequest.getModel(), orderRequest.getTrim(), orderRequest.getDoors())
				.orElseThrow(() -> new NoSuchElementException("Jeep model with ID = " 
				+ orderRequest.getModel() + ", trim= "
				+ orderRequest.getTrim()
				+ orderRequest.getDoors() + "was not found"));
	}

	protected Customer getCustomer(OrderRequest orderRequest) {
		return jeepOrderDao.fetchCustomer(orderRequest.getCustomer())
				.orElseThrow(() -> new NoSuchElementException("Customer with ID=" 
				+ orderRequest.getCustomer() + "was not found"));
	}

}
