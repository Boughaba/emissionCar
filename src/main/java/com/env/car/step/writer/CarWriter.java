package com.env.car.step.writer;

import java.util.List;

import org.springframework.batch.item.ItemWriter;

import com.env.car.model.CarModel;

public class CarWriter implements ItemWriter<CarModel> {

	@Override
	public void write(List<? extends CarModel> list) throws Exception {
		list.stream().forEach(p -> System.out.println(" id= "+ p.getId() + "  marque = " + p.getCommercialName() + " pays" +p.getMemberState()));
		
	}



}
