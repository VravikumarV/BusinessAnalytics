package com.saas.generic.business.bootstrapping;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScans;

@SpringBootApplication
public class BootstrappingApplication {


	public static void main(String[] args) {
		System.setProperty("server.servlet.context-path", "/bootstrapping");
		SpringApplication.run(BootstrappingApplication.class, args);
	}

}
