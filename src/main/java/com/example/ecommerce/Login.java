package com.example.ecommerce;

import javafx.scene.control.Alert;

import java.sql.ResultSet;

public class Login
{
    public Customer customerLogin(String userName, String password)
    {
        String query="SELECT * FROM customer WHERE email = '"+userName+"' AND password = '"+password+"'";//In SQL, string values must be enclosed in single quotes (' ') to be treated as string literals. This is necessary to distinguish string values from other data types and to prevent SQL injection attacks.
        DbConnection connection=new DbConnection();
        try
        {
           ResultSet rs= connection.getQueryTable(query);
           if(rs.next())//The code snippet you provided checks if there is at least one row in the ResultSet (rs). If the ResultSet contains at least one row, it returns true; otherwise, it returns false.
           {
               return new Customer(rs.getInt("id"),rs.getString("name"),
                       rs.getString("email"),rs.getString("mobile"));
           }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    // Method for adding a new user login
    public boolean addUser(String name, String email, String password, String mobile, String address) {
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

        // Create an SQL query to insert the new user into the database
        String query = "INSERT INTO customer (name, email, password, mobile, address) VALUES ('" + name + "', '" + email + "', '" + password + "', '" + mobile + "', '" + address + "')";

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
        Customer customer=login.customerLogin("user@gmail.com","user");
        System.out.println("Welcome "+customer.getName());
//      System.out.println(login.customerLogin("yashas.me18@sahyadri.edu.in","mangalore")); -->to test connection
    }
}


