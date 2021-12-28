package com.user.service;

import com.user.entity.*;

public interface UserService 
{
	public User getUser(Long id);
	public void validateAndPushUsers(String FileName);
	public void GenerateAndPublishReport(String Subscription);
}
