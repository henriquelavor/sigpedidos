package com.henriquelavor.sigpedidos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.henriquelavor.sigpedidos.services.S3Service;



@SpringBootApplication
@RestController
public class SigpedidosApplication extends SpringBootServletInitializer{
	
	/*@Autowired
	private S3Service s3Service;*/
	
	public static void main(String[] args) {
		SpringApplication.run(SigpedidosApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(SigpedidosApplication.class);
	}
	
	@RequestMapping(value="/")
	public String demo() {
		//s3Service.uploadFile("/Users/henriquelavor/Downloads/henrique.jpg");
		return "Aplicação iniciada!";
	}
}
