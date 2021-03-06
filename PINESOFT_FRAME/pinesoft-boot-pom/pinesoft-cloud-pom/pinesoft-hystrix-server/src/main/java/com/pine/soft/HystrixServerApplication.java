package com.pine.soft;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.netflix.turbine.EnableTurbine;

@SpringBootApplication
@EnableHystrixDashboard//开启熔断监控，页面地址 http://{host}:{port}/hystrix
@EnableTurbine
@EnableCircuitBreaker
public class HystrixServerApplication {
	public static void main(String[] args) throws IOException {
		SpringApplication.run(HystrixServerApplication.class, args);
		System.out.println("熔断监控中心已经启动");
	}
}
