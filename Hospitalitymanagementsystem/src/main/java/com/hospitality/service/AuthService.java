package com.hospitality.service;

import com.hospitality.dao.UserDAO;
import com.hospitality.model.User;
import java.sql.SQLException;

public class AuthService {
    private UserDAO userDAO;

    public AuthService() {
        this.userDAO = new UserDAO();
    }

    public User login(String username, String password) throws SQLException {
        User user = userDAO.getUserByUsername(username);
        if (user != null && EncryptionUtil.verifyPassword(password, user.getPassword())) {
            return user;
        }
        
        return null;
    }

    public void register(User user) throws SQLException {
        String hashedPassword = EncryptionUtil.hashPassword(user.getPassword());
        user.setPassword(hashedPassword);
        userDAO.addUser(user);
    }

    // Method to create the first admin user if no users exist
    public void setupFirstAdmin() throws SQLException {
        if (userDAO.getAllUsers().isEmpty()) {  // Check if there are no users
          
            User adminUser = new User(4, "atharv", "12345", "admin");
            register(adminUser);  // Add the admin to the database
        }
    }
}
