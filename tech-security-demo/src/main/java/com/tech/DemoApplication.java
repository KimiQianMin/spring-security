/**
 * 
 */
package com.tech;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author attpnxg1
 *
 */

@SpringBootApplication
@RestController
public class DemoApplication {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);

	}

	@GetMapping("/hello")
	public String hello() {
		return "hello spring security";
	}
	
	//http://localhost:8060/oauth/authorize?response_type=code&client_id=tech&redirect_uri=http://example.com&scope=all

}
