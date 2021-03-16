package com.prosegur;

import com.prosegur.ws.rest.GeneralController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * Main application
 */
//@Slf4j
//@EnableOAuth2Client
//@EnableFeignClients
//@EnableGlobalMethodSecurity(prePostEnabled = true)
//@EnableDiscoveryClient
//@EnableCircuitBreaker
//@EnableSwagger2
//@EnableScheduling
@SpringBootApplication
//@Configuration
//@EnableAutoConfiguration
public class SpringbootinitializerApplication {

//	private static final Logger logger = LoggerFactory.getLogger(SpringbootinitializerApplication.class);

	/**
	 * Main application
	 * @param args list of arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(SpringbootinitializerApplication.class, args);
	}

//	QUITAR PARA USAR CLOUD CONFIG
	/**
	 * Method for read local configuration
	 * @return property sources placeholder
	 */
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		PropertySourcesPlaceholderConfigurer pc = new PropertySourcesPlaceholderConfigurer();
		pc.setIgnoreUnresolvablePlaceholders(true);
		return pc;
	}
//	FIN QUITAR PARA USAR CLOUD CONFIG

	/**
	 * Method for filter registration beans
	 * @return filter registration bean
	 */
	@Bean
	@SuppressWarnings("rawtypes")
	public FilterRegistrationBean corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.addAllowedOrigin("*");
		config.addAllowedHeader("*");
		config.addAllowedMethod("*");
		source.registerCorsConfiguration("/**", config);
		FilterRegistrationBean bean = new FilterRegistrationBean<>(new CorsFilter(source));
		bean.setOrder(0);
		return bean;
	}
}
