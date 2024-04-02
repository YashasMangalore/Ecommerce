package com.example.ecommerce;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class DbConnection
{
    private final String dbUrl="jdbc:mysql://localhost:3306/ecommerce";
    private final String userName="root";
    private final String password="Bajal123";

    public PreparedStatement prepareStatement(String query) throws SQLException {
        return getConnection().prepareStatement(query);
    }

    private Statement getStatement() // to get
    {
        try
        {
            Connection connection=DriverManager.getConnection(dbUrl,userName,password);
            return connection.createStatement();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public ResultSet getQueryTable(String query)
    {
        try
        {
            Statement statement=getStatement();
            return statement.executeQuery(query);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
    public int updateDataBase(String query)
    {
        try
        {
            Statement statement=getStatement();
            return statement.executeUpdate(query);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return 0;
    }

    public static void main(String[] args)
    {
        DbConnection com=new DbConnection();
        ResultSet rs=com.getQueryTable("SELECT * FROM customer");

        if(rs!=null)
        {
            System.out.println("Connection successful");
        }
        else {
            System.out.println("Connection failed");
        }
    }

    private Connection connection;

    public DbConnection()
    {
        try
        {
            // Initialize the database connection
            connection = DriverManager.getConnection(dbUrl, userName, password);
        } catch (SQLException e) {
            // Handle connection errors
            e.printStackTrace();
        }
    }
    public Connection getConnection() {
        return connection;
    }

    public void close() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                // Handle closure errors
                e.printStackTrace();
            }
        }
    }

}
