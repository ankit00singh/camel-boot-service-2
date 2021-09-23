package com.online.assignment.camelbootservice2;

import org.apache.camel.component.servlet.CamelHttpTransportServlet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CamelBootService2Application {

	@Value("${rest.api.base.url:/com/user/service2/v1}")
	private String restApiBaseUrl;

	public static void main(String[] args) {
		SpringApplication.run(CamelBootService2Application.class, args);
	}

	/**
	 * Registers Camel HTTP Servlet with Spring application context.
	 *
	 * @return the Servlet registration bean for Camel HTTP servlet.
	 */
	@Bean
	public ServletRegistrationBean camelHttpServletRegistration() {

		final CamelHttpTransportServlet camelHttpServlet = new CamelHttpTransportServlet();
		final ServletRegistrationBean servletRegistration = new ServletRegistrationBean(camelHttpServlet, restApiBaseUrl + "/*");
		servletRegistration.setName("CamelServlet");

		return servletRegistration;
	}

}
