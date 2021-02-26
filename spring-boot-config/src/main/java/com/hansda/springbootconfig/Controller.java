package com.hansda.springbootconfig;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

	@Value("${my.greetings}")
	private String greetingsText;

	@RequestMapping("/greetings")
	public String getGreetings() {
		return greetingsText;
	}
}
