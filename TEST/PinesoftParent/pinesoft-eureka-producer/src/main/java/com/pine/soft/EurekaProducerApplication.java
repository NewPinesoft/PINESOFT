package com.pine.soft;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient // 服务提供者，用来向eureka注册
public class EurekaProducerApplication {
	public static void main(String[] args) throws IOException {
		SpringApplication.run(EurekaProducerApplication.class, args);
		System.out.println("测试服务（提供者）已经注册到Eureka服务中心");
	}
}
