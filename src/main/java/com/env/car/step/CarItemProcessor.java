package com.env.car.step;

import org.springframework.batch.item.ItemProcessor;

import com.env.car.model.CarModel;

public class CarItemProcessor implements ItemProcessor<CarModel, CarModel> {

	@Override
	public CarModel process(CarModel car) throws Exception {
		return car;
	}

}
