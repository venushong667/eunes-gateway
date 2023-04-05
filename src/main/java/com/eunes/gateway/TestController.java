package com.eunes.gateway;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;


@RestController
public class TestController {

    @Autowired
    private DiscoveryClient discoveryClient;

    @GetMapping("/test")
	public String memoService() {
		List<ServiceInstance> list = discoveryClient.getInstances("memoboard");
        if (list != null && list.size() > 0 ) {
            return list.get(0).getUri().toString();
        }
        return null;
	}
    
    @GetMapping("/health")
	public String health() {
		return "Greetings from Spring Boot!";
	}
}
