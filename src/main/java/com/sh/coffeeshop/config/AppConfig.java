package com.sh.coffeeshop.config;

import com.sh.coffeeshop.dao.CoffeeDao;
import com.sh.coffeeshop.dao.OrderDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

@EnableWebMvc
@Configuration
@ComponentScan({ "com.sh.coffeeshop.*" })
@PropertySource("classpath:daoSettings.properties")
@Import({ SecurityConfig.class })
public class AppConfig  extends WebMvcConfigurerAdapter {

	@Autowired
	private ApplicationContext context;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
	}

	@Bean
	public InternalResourceViewResolver viewResolver() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setViewClass(JstlView.class);
		viewResolver.setPrefix("/WEB-INF/jsp/");
		viewResolver.setSuffix(".jsp");
		return viewResolver;
	}

	//for properties
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	@Bean
	@Qualifier("CoffeeDao")
	public CoffeeDao coffeeDao(@Value("${dao.active}") String active,
							   @Value("${file.coffee.dao.class}") String fileDao,
							   @Value("${mysql.coffee.dao.class}") String mySqlDao,
							   @Value("${oracle.coffee.dao.class}") String oracleDao) {

		CoffeeDao coffeeDao;

		if("file".equals(active)){
			coffeeDao = (CoffeeDao) context.getBean(fileDao);
		} else if ("mysql".equals(active)){
			coffeeDao = (CoffeeDao) context.getBean(mySqlDao);
		} else if ("oracle".equals(active)){
			coffeeDao = (CoffeeDao) context.getBean(oracleDao);
		} else {
			coffeeDao = (CoffeeDao) context.getBean(fileDao);
		}

		return coffeeDao;
	}

	@Bean
	@Qualifier("OrderDao")
	public OrderDao orderDao(@Value("${dao.active}") String active,
							  @Value("${file.order.dao.class}") String fileDao,
							  @Value("${mysql.order.dao.class}") String mySqlDao,
							  @Value("${oracle.order.dao.class}") String oracleDao) {

		OrderDao orderDao;

		if("file".equals(active)){
			orderDao = (OrderDao) context.getBean(fileDao);
		} else if ("mysql".equals(active)){
			orderDao = (OrderDao) context.getBean(mySqlDao);
		} else if ("oracle".equals(active)){
			orderDao = (OrderDao) context.getBean(oracleDao);
		} else {
			orderDao = (OrderDao) context.getBean(fileDao);
		}

		return orderDao;
	}
}