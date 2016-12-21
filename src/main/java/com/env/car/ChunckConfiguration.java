package com.env.car;

import java.util.Arrays;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;

import com.env.car.model.CarModel;
import com.env.car.step.CarItemProcessor;
import com.env.car.step.writer.CarWriter;

@Configuration
@EnableBatchProcessing
public class ChunckConfiguration {

	@Autowired
	Environment environment;
	@Autowired
	JobBuilderFactory jobBuilderFactory;
	
	@Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    public DataSource dataSource;
    
    @Bean
    Job orientDbJob(){
    	 return jobBuilderFactory.get("orientDbJob")
    	.incrementer(new RunIdIncrementer())
    	.flow(csvStep()).end().build();
    }
    
    @Bean
    public Step csvStep(){
    	 return stepBuilderFactory.get("csvStep").<CarModel, CarModel>chunk(10)
    			 .reader(reader())
    			 .processor(processor())
    			 .writer(writer())
    			 .build();
    	
    }
    
 
	@Bean
	public FlatFileItemReader<CarModel> reader(){
	
		FlatFileItemReader<CarModel> reader = new FlatFileItemReader<CarModel>();
        reader.setResource(new ClassPathResource("CO2_passenger_cars_v5.csv"));
        reader.setLineMapper(new DefaultLineMapper<CarModel>() {{
            setLineTokenizer(new DelimitedLineTokenizer() {{
                setNames(environment.getProperty("org.eu.car.properties", String[].class));
                setDelimiter(DELIMITER_TAB);
                setStrict(false);
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<CarModel>() {{
                setTargetType(CarModel.class);
            }});
        }});
        reader.setStrict(false);
        reader.setLinesToSkip(1);
        return reader;
	}
	@Bean
	public ItemProcessor<CarModel, CarModel> processor(){
		return new CarItemProcessor();
	}
	
	@Bean ItemWriter<CarModel> writer(){
		return new CarWriter();
	}
}
