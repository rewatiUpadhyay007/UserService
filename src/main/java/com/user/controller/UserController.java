package com.user.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.user.entity.User;
import com.user.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController 
{
	@Autowired
	private UserService userService;
	
	@Autowired
	private RestTemplate restTemplate;
	
	   
		@Value("${contactService}")
		private  String contactService;
		
	 
	@GetMapping("/{userId}")
	public User getUser(@PathVariable("userId") Long userId)
	{
		//return this.userService.getUser(userId);
		User user = this.userService.getUser(userId);
		/*
		 * List contacts = this.restTemplate.getForObject(contactService +userId,
		 * List.class); if(user != null)
		user.setContacts(contacts);
		 */	
		return user;
	}
	
	@RequestMapping("/validateAndPush/{fileName}")
	public void validateAndPushUsers(@PathVariable("fileName") String fileName)
	{
		System.out.println("entering"); 
		this.userService.validateAndPushUsers(fileName);		
	}

}
