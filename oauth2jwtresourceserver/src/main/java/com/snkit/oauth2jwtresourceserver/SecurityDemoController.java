package com.snkit.oauth2jwtresourceserver;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecurityDemoController {
	
	@GetMapping(value="/api/rest/hi")
	public String sayHi() {
		
		
		return "Hello world this resource is protected  = "+SecurityContextHolder.getContext().getAuthentication().getName();
	}
	@GetMapping(value="/nonprotectedurl")
	public String nonprotectedurl() {
		
		
		return "nonprotectedurl ";
	}
	@GetMapping(value="/api/rest/protectedurl")
	public String protectedurl() {
		
		
		return "protectedurl ";
	}

	
	@GetMapping(value="/")
	public String hi() {
		
		
		return "Hello world this resource is protected  = ";
	}
}
