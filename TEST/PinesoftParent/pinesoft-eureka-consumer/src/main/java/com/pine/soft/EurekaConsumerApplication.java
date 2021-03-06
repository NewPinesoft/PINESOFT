package com.pine.soft;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient // 启用服务注册与发现
@EnableFeignClients // 启用feign进行远程调用(反向代理)
public class EurekaConsumerApplication {
	public static void main(String[] args) throws IOException {
		SpringApplication.run(EurekaConsumerApplication.class, args);
		System.out.println("服务消费者已经开启");
	}
}
