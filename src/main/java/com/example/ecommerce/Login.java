package com.example.ecommerce;

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

    public static void main(String[] args) {
        Login login=new Login();
        Customer customer=login.customerLogin("yashas.me18@sahyadri.edu.in","mangalore");
        System.out.println("Welcome "+customer.getName());
//      System.out.println(login.customerLogin("yashas.me18@sahyadri.edu.in","mangalore")); -->to test connection
    }
}


