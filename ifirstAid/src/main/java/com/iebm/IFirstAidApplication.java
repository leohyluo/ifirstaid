package com.iebm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ImportResource;

@EnableCaching
@ServletComponentScan
@SpringBootApplication
@ImportResource(locations={"classpath:druid-bean.xml"})
/*@EntityScan(basePackages={"com.iebm.aid.pojo"})
@ComponentScan(basePackages={"com.iebm.aid.service"})
@EnableJpaRepositories(basePackages={"com.iebm.aid.repository"})*/
public class IFirstAidApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(IFirstAidApplication.class);
	}
	
	public static void main(String[] args) {
		SpringApplication.run(IFirstAidApplication.class, args);
	}
}
