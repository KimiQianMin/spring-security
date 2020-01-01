package com.tech.service.impl;

import org.springframework.stereotype.Service;

import com.tech.service.HelloService;

@Service
public class HelloServiceImpl implements HelloService {

	@Override
	public String greeting(String name) {
		System.out.print("greeting ");
		return "hello " + name;
	}

}
