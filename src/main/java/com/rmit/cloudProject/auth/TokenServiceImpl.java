package com.rmit.cloudProject.auth;

import javax.transaction.Transactional;  
import org.springframework.beans.factory.annotation.Autowired;  
import org.springframework.stereotype.Service;  
  
@Service("tokenService")  
public class TokenServiceImpl implements TokenService {  
  
    @Autowired  
    private TokenDAO tokenDAO;  
      
    @Transactional  
    public void saveUserEmail(String email, int adminId) {  
        tokenDAO.saveUserEmail(email, adminId);  
    }  
  
    @Transactional  
    public boolean updateToken(String email, String authenticationToken, String secretKey) {  
        return tokenDAO.updateToken(email, authenticationToken, secretKey);  
    }  
  
    @Transactional  
    public int getTokenDetail(String email) {  
        return tokenDAO.getTokenDetail(email);  
    }  
  
    @Transactional  
    public int tokenAuthentication(String token, int emailId) {  
        return tokenDAO.tokenAuthentication(token, emailId);  
    }
    
    @Transactional  
    public int tokenAuthentication(String token) {  
        return tokenDAO.tokenAuthentication(token);  
    }
  
}  