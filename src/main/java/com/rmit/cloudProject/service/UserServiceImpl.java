package com.rmit.cloudProject.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rmit.cloudProject.dao.UserDAO;
import com.rmit.cloudProject.dto.OrderDetails;
import com.rmit.cloudProject.dto.UserCartItems;
import com.rmit.cloudProject.entity.FoodItems;
import com.rmit.cloudProject.entity.Users;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	UserDAO userDao;
	
	@Override
	@Transactional
	public String saveUserDetail(Users userDetail) {
		return userDao.saveUserDetail(userDetail);
	}

	@Override
	@Transactional
	public int userLogin(String emailId, String password) {
		return userDao.userLogin(emailId, password);
		
	}

	@Override
	@Transactional
	public List<Users> getUserData() {
		return userDao.getUserData();
	}

	@Override
	@Transactional
	public List<FoodItems> getMenu() {
		return userDao.getMenu();
	}

	@Override
	@Transactional
	public boolean addToCart(String token, int parseInt) {
		return userDao.addToCart(token,parseInt);
	}

	@Override
	@Transactional
	public List<UserCartItems> getCartItems(String token) {
		return userDao.getCartItems(token);
	}

	@Override
	@Transactional
	public boolean confirmOrder(String token) {
		return userDao.confirmOrder(token);
	}

	@Override
	@Transactional
	public List<OrderDetails> getMyOrders(String token) {
		return userDao.getMyOrders(token);
	}

}
