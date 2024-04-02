package com.example.ecommerce;

import javafx.scene.control.Alert;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login
{
    public Customer customerLogin(String userName, String password) {
        // Query to retrieve user details based on username/email and password
        String query = "SELECT * FROM customer WHERE email = ? AND password = ?";
        DbConnection connection = new DbConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, userName);
            statement.setString(2, password);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                // User found in the database, retrieve details and create a Customer object
                int customerId = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String mobile = rs.getString("mobile");

                // Log user details found in the database
                System.out.println("User found in database: ID=" + customerId + ", Name=" + name + ", Email=" + email);

                // Return a new Customer object with retrieved details
                return new Customer(customerId, name, email, mobile);
            } else {
                // Log if no user found
                System.out.println("User not found in database for username/email: " + userName);
            }
        } catch (SQLException e) {
            // Log any SQL exceptions
            System.err.println("Error executing SQL query: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Close the database connection
            connection.close();
        }
        // Return null if no user found or if an error occurred
        return null;
    }
    public boolean addUser(String name, String email, String password, String mobile, String address) {// Method for adding a new user login
        // You can add necessary validations here for the inputs
        if(name.isEmpty() || email.isEmpty() || password.equals("") || mobile.length()!=12)
        {
            // Display error message box
            showAlert(Alert.AlertType.ERROR, "User Registration", "Failed to register user.");
            return false;
        }
        // Check if email or mobile number already exists in the database
        if (isEmailOrMobileExists(email, mobile))
        {
            showAlert(Alert.AlertType.ERROR, "User Registration", "User is already registered. Please SIGN-IN!");
            return false; // Return false indicating email or mobile already exists
        }
       //Create an SQL query to insert the new user into the database
        String query = "INSERT INTO customer (name, email, password, mobile, address) VALUES " +
                "('" + name + "', '" + email + "', '" + password + "', '" + mobile + "', '" + address + "')";
        DbConnection connection = new DbConnection();
        try {
            // Execute the query to insert the new user
            int rowsAffected = connection.updateDataBase(query);
            // Check if any rows were affected (if insertion was successful)
            if (rowsAffected > 0) {
                return true; // Return true indicating successful addition of user
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Display error message box
        showAlert(Alert.AlertType.ERROR, "User Registration", "Failed to register user.");
        return false; // Return false indicating failure to add user
    }
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private boolean isEmailOrMobileExists(String email, String mobile) {
        // Check if email or mobile number already exists in the database
        String query = "SELECT COUNT(*) FROM customer WHERE email = '" + email + "' OR mobile = '" + mobile + "'";
        DbConnection connection = new DbConnection();
        try {
            ResultSet resultSet = connection.getQueryTable(query);
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0; // Return true if email or mobile exists
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false; // Return false by default
    }
    public static void main(String[] args) {
        Login login=new Login();
        Customer customer=login.customerLogin("user@gmail.com","user");// -->to test connection
        System.out.println("Welcome "+customer.getName());
    }
}