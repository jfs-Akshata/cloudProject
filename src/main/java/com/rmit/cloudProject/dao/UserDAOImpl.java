package com.rmit.cloudProject.dao;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.rmit.cloudProject.auth.Token;
import com.rmit.cloudProject.dto.OrderDetails;
import com.rmit.cloudProject.dto.UserCartItems;
import com.rmit.cloudProject.entity.FoodItems;
import com.rmit.cloudProject.entity.OrderItems;
import com.rmit.cloudProject.entity.Orders;
import com.rmit.cloudProject.entity.Users;

import org.hibernate.Session;  
import org.hibernate.SessionFactory;  
import org.hibernate.query.Query;  
import org.springframework.beans.factory.annotation.Autowired;  
import org.springframework.stereotype.Repository;  


@Repository("userDAO")  
public class UserDAOImpl implements UserDAO {  

	// Autowired SessionFactory Object So that we can get session object used for interaction with Database.  
	@Autowired  
	private SessionFactory sessionFactory;  

	/* 
	 * Register User Details.  
	 */
	@Override
	public String saveUserDetail(Users userDetail) {  

		Session session = null;  
		try  
		{  
			session = sessionFactory.getCurrentSession();  
			if(checkUserExists(userDetail.getEmailId()) <= 0 ) {
				int id = (Integer) session.save(userDetail);  
				return (id>0) ? "User registered." : "Could not register user";  				
			}else {
				return "User Already exists";
			}
		}  
		catch(Exception exception)  
		{  
			System.out.println("Excecption while saving user Details : " + exception.getMessage());
			exception.printStackTrace();
			return "Could not register user";  
		}  
		finally  
		{  
			if(session!=null && session.getTransaction()!=null && session.getTransaction().isActive())
				session.flush();  
		}  
	}  

	public int checkUserExists(String emailId) {  

		Session session = null;  
		try  
		{  
			session = sessionFactory.getCurrentSession();  

			Query query = session.createQuery("from Users where lower(emailId)=:emailId");  
			query.setParameter("emailId", emailId.toLowerCase());  
			List<Users> list = query.list();  

			long size = list.size();  
			if(size == 1)  
			{  
				return list.get(0).getId();  
			}  
			else  
			{  
				return -1;
			}  
		}  
		catch(Exception exception){  
			System.out.println("Excecption while saving admin Details : " + exception.getMessage());  
			return 0;
		}  
		finally{  
			if(session!=null && session.getTransaction()!=null && session.getTransaction().isActive())
				session.flush();  
		}  

	}
	
	public int userLogin(String emailId, String password) {  

		Session session = null;  
		try  
		{  
			session = sessionFactory.getCurrentSession();  

			Query query = session.createQuery("from Users where emailId=:emailId and password=:password");  
			query.setParameter("emailId", emailId);  
			query.setParameter("password", password);  
			List<Users> list = query.list();  

			long size = list.size();  
			if(size == 1)  
			{  
				return list.get(0).getId();  
			}  
			else  
			{  
				return -1;
			}  
		}  
		catch(Exception exception){  
			System.out.println("Excecption while saving admin Details : " + exception.getMessage());  
			return 0;
		}  
		finally{  
			if(session!=null && session.getTransaction()!=null && session.getTransaction().isActive())
				session.flush();  
		}  

	}  


	public List<Users> getUserData() {  
		Session session = null;  
		try  
		{  
			session = sessionFactory.getCurrentSession();  

			Query<Users> query = session.createQuery("from Users");  
			List<Users> list = query.list();  

			if(list.size() > 0)  
			{  
				return list;  
			}  
			else  
			{  
				return null;  
			}  

		}  
		catch(Exception exception)  
		{  
			System.out.println("Excecption while saving admin Details : " + exception.getMessage());  
			return null;  
		}  
		finally  
		{  
			if(session!=null && session.getTransaction()!=null && session.getTransaction().isActive())
				session.flush();  
		}  

	}

	@Override
	public List<FoodItems> getMenu() {  
		Session session = null;  
		try  
		{  
			session = sessionFactory.getCurrentSession();  

			Query<FoodItems> query = session.createQuery("from FoodItems");  
			List<FoodItems> list = query.list();  

			if(list.size() > 0)  
			{  
				return list;  
			}  
			else  
			{  
				return null;  
			}  

		}  
		catch(Exception exception)  
		{  
			System.out.println("Excecption while saving admin Details : " + exception.getMessage());  
			return null;  
		}  
		finally  
		{  
			if(session!=null && session.getTransaction()!=null && session.getTransaction().isActive())
				session.flush();  
		}  

	}

	@Override
	public boolean addToCart(String token, int item) {  
        Session session = null;  
          
        try   
        {  
            session = sessionFactory.getCurrentSession();  
              
            Query query = session.createQuery("from Token where authenticationToken = :token");  
            query.setParameter("token", token);  
            List<Token> tokenDetails = query.list();  
              
            if(tokenDetails.size() > 0){  
            	int userId = tokenDetails.get(0).getUserID();
            	query = session.createNativeQuery("Insert into user_cart (user_id, item_id) VALUES (:userId, :itemId)");  
                
            	query.setParameter("userId", userId);  
            	query.setParameter("itemId", item);  
      
                int result = query.executeUpdate();
                if(result>0) {
                	return true;
                }else {
                	return false;
                }
            }
            else{  
            	return false;
            }  
  
        }  
        catch(Exception exception)  
        {  
            System.out.println("Exception while Authenticating token :: "+ exception);  
            return false;  
        }  
        finally  
        {  
        	if(session!=null && session.getTransaction()!=null && session.getTransaction().isActive())
        		session.flush();  
        }  
          
          
    }

	@Override
	public List<UserCartItems> getCartItems(String token) {
		
		
		List<UserCartItems> cartItems = new ArrayList<UserCartItems>();
		Session session = null;  
        
        try   
        {  
            session = sessionFactory.getCurrentSession();  
              
            Query query = session.createQuery("from Token where authenticationToken = :token");  
            query.setParameter("token", token);  
            List<Token> tokenDetails = query.list();  
              
            if(tokenDetails.size() > 0){  
            	int userId = tokenDetails.get(0).getUserID();
            	query = session.createNativeQuery("SELECT a.id, a.name, sum(a.price), COUNT(a.id) FROM FoodItems a, user_cart b WHERE a.id=b.item_id AND b.user_id=:userId GROUP BY b.item_id;");  
                
            	query.setParameter("userId", userId);  
      
                List<Object[]> result = query.list();
                if(result!=null && result.size()>0) {
                	result.forEach(item-> {
                		UserCartItems userCartItems = new UserCartItems();
                		userCartItems.setFoodItemName((String)item[1]);
                		userCartItems.setPriceTotal(((BigDecimal)item[2]).intValue());
                		userCartItems.setCount(((BigInteger)item[3]).intValue());
                		cartItems.add(userCartItems);
                	});
                	return cartItems;
            	
                }else {
                	return null;
                }
            }
            else{  
            	return null;
            }  
  
        }  
        catch(Exception exception)  
        {  
            System.out.println("Exception in getCartItems :: ");
            exception.printStackTrace();
            return null;  
        }  
        finally  
        {  
        	if(session!=null && session.getTransaction()!=null && session.getTransaction().isActive())
        		session.flush();  
        }
	}

	@Override
	public boolean confirmOrder(String token) {
		List<UserCartItems> cartItems = this.getCartItems(token);
		
		Session session = null;  
        
        try   
        {  
            session = sessionFactory.getCurrentSession();  
              
            Query query = session.createQuery("from Token where authenticationToken = :token");  
            query.setParameter("token", token);  
            List<Token> tokenDetails = query.list();  
              
            if(tokenDetails.size() > 0){  
            	int userId = tokenDetails.get(0).getUserID();
            	if(cartItems!=null && cartItems.size()>0) {
            		Orders order = new Orders(userId, System.currentTimeMillis());
            		session.save(order);
            		List<Object> result = session.createNativeQuery("SELECT LAST_INSERT_ID()").list();
            		if(result!=null && result.size()>0) {
            			int orderId= ((BigInteger)result.get(0)).intValue();
            			
            			for(UserCartItems item : cartItems){
                			OrderItems orderItems = new OrderItems(orderId, item.getFoodItemName(), item.getCount(), item.getPriceTotal());
                			session.save(orderItems);
                		}
            		}
            		query = session.createNativeQuery("DELETE from user_cart where user_id = :userId");
            		query.setParameter("userId", userId);
            		query.executeUpdate();
            		return true;
            	}
            	return false;
                }else {
                	return false;
                }
            } catch(Exception exception){  
	            System.out.println("Exception while Authenticating token :: "+ exception);  
	            return false;  
	        } finally{  
	        	if(session!=null && session.getTransaction()!=null && session.getTransaction().isActive())
	        		session.flush();  
	        }
	}

	@Override
	public List<OrderDetails> getMyOrders(String token) {
		
		
		List<OrderDetails> orderDetails = new ArrayList<OrderDetails>();
		Session session = null;  
        
        try   
        {  
            session = sessionFactory.getCurrentSession();  
              
            Query query = session.createQuery("from Token where authenticationToken = :token");  
            query.setParameter("token", token);  
            List<Token> tokenDetails = query.list();  
              
            if(tokenDetails.size() > 0){  
            	int userId = tokenDetails.get(0).getUserID();
            	
            	query = session.createQuery("from Orders where userId = :userId");  
                query.setParameter("userId", userId);  
                List<Orders> orders = query.list();
                
                if(orders!=null && orders.size()>0) {
                	for(Orders order : orders) {
                		OrderDetails orderDetail = new OrderDetails();
                		orderDetail.setId(order.getOrderId());
                		orderDetail.setOrderDate(new Date(order.getOrderDate()).toGMTString());
                		
                		query = session.createQuery("from OrderItems where orderId = :orderId");  
                		
                		query.setParameter("orderId", order.getOrderId());  
                		
                		List<OrderItems> result = query.list();
                		if(result!=null && result.size()>0) {
                			orderDetail.setItems(result);
                			orderDetail.setOrderTotal(result.stream().mapToInt(item -> item.getPrice()).sum());
                		}else {
                			return null;
                		}
                		orderDetails.add(orderDetail);
                	}
                	return orderDetails;
                }
                return null;
            }
            else{  
            	return null;
            }  
  
        }  
        catch(Exception exception)  
        {  
            System.out.println("Exception in getCartItems :: ");
            exception.printStackTrace();
            return null;  
        }  
        finally  
        {  
        	if(session!=null && session.getTransaction()!=null && session.getTransaction().isActive())
        		session.flush();  
        }
	}


}
