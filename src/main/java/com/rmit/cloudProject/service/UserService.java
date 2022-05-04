package com.rmit.cloudProject.service;

import java.util.List;

import com.rmit.cloudProject.dto.OrderDetails;
import com.rmit.cloudProject.dto.UserCartItems;
import com.rmit.cloudProject.entity.FoodItems;
import com.rmit.cloudProject.entity.Users;
  
public interface UserService {  
  
    public String saveUserDetail(Users adminDetail);  
      
    public int userLogin(String emailId , String password);  
      
    public List<Users> getUserData();

	public List<FoodItems> getMenu();

	public boolean addToCart(String token, int parseInt);

	public List<UserCartItems> getCartItems(String string);

	public boolean confirmOrder(String token);

	public List<OrderDetails> getMyOrders(String token);  
}  