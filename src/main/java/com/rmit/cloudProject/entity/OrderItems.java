package com.rmit.cloudProject.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class OrderItems {

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	int id;
	
	@Column
	int orderId;
	
	@Column
	String itemName;

	@Column
	int quantity;
	
	@Column
	int price;

	
	public OrderItems() {};
	public OrderItems(int orderId, String itemName, int quantity, int price) {
		super();
		this.orderId = orderId;
		this.itemName = itemName;
		this.quantity = quantity;
		this.price = price;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public String getItemId() {
		return itemName;
	}

	public void setItemId(String itemId) {
		this.itemName = itemId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}
	
	
}
