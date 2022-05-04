package com.rmit.cloudProject.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Orders {
	
	public Orders() {};
	public Orders(int userId, long orderDate){
		this.userId = userId;
		this.orderDate = orderDate;
	}
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	int orderId;
	
	@Column
	int userId;
	
	@Column
	long orderDate;
	
	
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public long getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(long orderDate) {
		this.orderDate = orderDate;
	}
	
	

}
