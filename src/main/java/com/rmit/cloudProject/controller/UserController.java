package com.rmit.cloudProject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;  
import org.springframework.http.HttpHeaders;  
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;  
import org.springframework.web.bind.annotation.CrossOrigin;  
import org.springframework.web.bind.annotation.GetMapping;  
import org.springframework.web.bind.annotation.PathVariable;  
import org.springframework.web.bind.annotation.RequestBody;  
import org.springframework.web.bind.annotation.RequestHeader;  
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rmit.cloudProject.auth.GenerateToken;
import com.rmit.cloudProject.auth.TokenService;
import com.rmit.cloudProject.entity.Users;
import com.rmit.cloudProject.service.UserService;


@RestController
public class UserController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	TokenService tokenService;
	
	GenerateToken generateToken = new GenerateToken();
	private ObjectMapper o = new ObjectMapper();
	
	//check the api's working correctly api
    @RequestMapping(value="/ping", method=RequestMethod.GET)
    @ResponseBody
    public String healthCheck() {
        return "This is working well";
    }
    
    @PostMapping(value="/saveUser", produces=MediaType.APPLICATION_JSON_VALUE)  
    public ResponseEntity<String> saveUserDetail(@RequestBody Users userDetail) {  
    	HttpHeaders httpHeader = new HttpHeaders();
    	
    	return new ResponseEntity<String>(userService.saveUserDetail(userDetail), httpHeader, HttpStatus.OK);  
    }  

    @PostMapping(value="/login", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> login(@RequestBody Users user)  
    {  
    	int status;  
    	HttpHeaders httpHeader = null;  
    	// Authenticate User.  
    	status = userService.userLogin(user.getEmailId(), user.getPassword());  

    	/* 
    	 * If User is authenticated then Do Authorization Task. 
    	 */  
    	if (status > 0)   
    	{  
    		/* 
    		 * Generate token. 
    		 */  
    		String tokenData[] = generateToken.createJWT(user.getEmailId(), "CloudApp", "JWT Token",  
    				user.getRole(), 43200000);  

    		// get Token.  
    		String token = tokenData[0];  

    		System.out.println("Authorization :: " + token);  

    		// Create the Header Object  
    		httpHeader = new HttpHeaders();  

    		// Add token to the Header.  
    		httpHeader.add("Authorization", token);  

    		// Check if token is already exist.  
    		long isUserEmailExists = tokenService.getTokenDetail(user.getEmailId());  

    		/* 
    		 * If token exist then update Token else create and insert the token. 
    		 */  
    		if (isUserEmailExists > 0)   
    		{  
    			tokenService.updateToken(user.getEmailId(), token, tokenData[1]);  
    		}   
    		else   
    		{  
    			tokenService.saveUserEmail(user.getEmailId(), status);  
    			tokenService.updateToken(user.getEmailId(), token, tokenData[1]);  
    		}  

    		return new ResponseEntity<Integer>(status, httpHeader, HttpStatus.OK);  
    	}   

    	// if not authenticated return  status what we get.  
    	else   
    	{  
    		return new ResponseEntity<Integer>(status, httpHeader, HttpStatus.OK);  
    	}  


    }  

    @GetMapping(value="/getUserData/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public String getUserData(@PathVariable String userId, @RequestHeader("Authorization") String authorizationToken)  
    {  
    	System.out.println("Came in here!!:: "+userId);
    	String token[] = authorizationToken.split(" ");  
    	int result = tokenService.tokenAuthentication(token[1], Integer.parseInt(userId));  

    	if (result > 0) {  
    		try {
				return o.writeValueAsString(userService.getUserData());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("Error in deserializing List<Users>");
				e.printStackTrace();
				return null;
			}  
    	} else {  
    		return null;  
    	}
    }
    
    @GetMapping(value="/getMenu", produces = MediaType.APPLICATION_JSON_VALUE)
	public String getMenu(@RequestHeader("Authorization") String authorizationToken)  
    {  
    	String token[] = authorizationToken.split(" ");  
    	int result = tokenService.tokenAuthentication(token[1]);  

    	if (result > 0) {  
    		try {
				return o.writeValueAsString(userService.getMenu());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("Error in deserializing List<Users>");
				e.printStackTrace();
				return null;
			}  
    	} else {  
    		return null;  
    	}
    }
    
    @PostMapping(value="/addToCart/{item}", produces = MediaType.APPLICATION_JSON_VALUE)
	public String addToCart(@PathVariable String item, @RequestHeader("Authorization") String authorizationToken)  
    {  
    	String token[] = authorizationToken.split(" ");  
    	int result = tokenService.tokenAuthentication(token[1]);  

    	if (result > 0) {  
    		try {
    			
				return o.writeValueAsString(userService.addToCart(token[1], Integer.parseInt(item)));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("Error in deserializing List<Users>");
				e.printStackTrace();
				return null;
			}  
    	} else {  
    		return null;  
    	}
    }
    
    
    @GetMapping(value="/getCartItems", produces = MediaType.APPLICATION_JSON_VALUE)
	public String getCartItems(@RequestHeader("Authorization") String authorizationToken)  
    {  
    	String token[] = authorizationToken.split(" ");  
    	int result = tokenService.tokenAuthentication(token[1]);  

    	if (result > 0) {  
    		try {
				return o.writeValueAsString(userService.getCartItems(token[1]));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("Error in deserializing List<Users>");
				e.printStackTrace();
				return null;
			}  
    	} else {  
    		return null;  
    	}
    }
    
    @PostMapping(value="/confirmOrder", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> confirmOrder(@RequestHeader("Authorization") String authorizationToken)  
    {  
    	String token[] = authorizationToken.split(" ");  
    	int result = tokenService.tokenAuthentication(token[1]); 
    	HttpHeaders httpHeader = new HttpHeaders();
    	
    	Boolean status = false;
    	if (result > 0) {  
    		try {
				status = userService.confirmOrder(token[1]);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("Error in deserializing List<Users>");
				e.printStackTrace();
				status = false;
			}  
    	} else {  
    		status = false;  
    	}
    	
    	return new ResponseEntity<String>(status.toString(), httpHeader, HttpStatus.OK);
    }
    
    @GetMapping(value="/getMyOrders", produces = MediaType.APPLICATION_JSON_VALUE)
	public String getMyOrders(@RequestHeader("Authorization") String authorizationToken)  
    {  
    	String token[] = authorizationToken.split(" ");  
    	int result = tokenService.tokenAuthentication(token[1]);  
    	
    	if (result > 0) {  
    		try {
				return o.writeValueAsString(userService.getMyOrders(token[1]));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				System.out.println("Error in deserializing List<Users>");
				e.printStackTrace();
				return null;
			}  
    	} else {  
    		return null;  
    	}
    }
    
}
