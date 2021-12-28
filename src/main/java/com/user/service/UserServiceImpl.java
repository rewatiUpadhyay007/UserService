package com.user.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jackson.JsonObjectDeserializer;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.user.entity.*;
import com.user.model.AzureModel;

import ch.qos.logback.core.net.ObjectWriter;

@Service
public class UserServiceImpl implements UserService
{
//Fake User List
	
	/*List<User> list = List.of(
			new User(1111l,"User1","09876541"),
			new User(1112l, "User2","09876542"),
			new User(1113l, "User3","09876543"),
			new User(1114l, "User4","09876544")
			);
			*/
	  
	@Value("${azureBlobService}")
	private  String azureBlobService;
	@Value("${containerName}")
	private  String containerName;
	@Value("${userFile}")
	private  String userFile;
	
	@Value("${azureSBusService}")
	private  String azureSBusService;
	@Value("${topicName}")
	private  String topicName;
	@Value("${batchSize}")
	private  int batchSize;
	
	@Autowired
	private RestTemplate restTemplate;
	
	private List<User> userList = new ArrayList<>();
	
	private List<User> getUserList(String FileName)
	{
		if(userList.size() ==0)
		{
			//var blobFileUrl = azureBlobService+containerName+"/"+FileName;
			//System.out.println("getUserList: Blob url "+blobFileUrl);
			var azModel = new AzureModel(containerName,FileName,null) ;
			
			System.out.print("Making file request with Container name: "+azModel.getContainerName()+", File name :"+azModel.getFileName()+"");
 			String[] usercsv = this.restTemplate.postForEntity(azureBlobService, azModel, String.class).toString().split("\n");

			System.out.println("retrieved tehe usesr");boolean skip = true;
			 for(String s : usercsv) 
			 {
				 if(skip  || s.isBlank() || s.isEmpty())
				 {skip=false; continue;}
				 
				 s=s.replaceAll("\n", "").replaceAll("\r", "");
				 var columns = s.split(",");
				 System.out.println("Printing row from csv: "+s);
				 userList.add(new User(Long.parseLong(columns[0]),columns[1],columns[2],columns[3] ));
			 }
		}		
		return userList;	
	}
	@Override
	public User getUser(Long id)
	{
		System.out.println("|"+id+"|");
		System.out.println("RequestedID|"+id+"|");
		List<User> userList  = getUserList(userFile);
		
		return userList.stream().filter(user -> user.getUserId().equals(id)).findAny().orElse(null);
	}
	
	@Override
	public void validateAndPushUsers(String FileName)
	{
		System.out.println("validateAndPushUsers:Getting the users from teh list");
		var userList = getUserList(FileName);
		System.out.println("validateAndPushUsers:retrieved teh usesr : " + userList.size());
		for(User user : userList) 
		 {
			if( !(user.getDepartment().isBlank() ||user.getDepartment().isEmpty()))
			{
				String json=null;
				ObjectMapper mapper = new ObjectMapper();
				var sbusLink = azureSBusService+topicName;
				System.out.println("link "+sbusLink);
				try 
				{
					//send message t0 topic
					json = mapper.writeValueAsString( user );	
					System.out.println(json);					
					this.restTemplate.postForObject(sbusLink, json,Object.class);
				} 
				catch (JsonProcessingException e) 
				{
					// TODO Auto-generated catch block
					System.out.println(e.getMessage());
					e.printStackTrace();
				}
			}
		 }		
	}

	@Override
	public void GenerateAndPublishReport(String Subscription)
	{
		String matchReport = "";
		String noMatchReport = "";
		//get metadata from cdb
		
		//read the messages
		String readSubscriptionUrl = azureSBusService+topicName+"/"+Subscription+"/"+batchSize;
		List<User> batch = this.restTemplate.getForObject(readSubscriptionUrl, List.class);
		
		
		//get data from 
	}
}
