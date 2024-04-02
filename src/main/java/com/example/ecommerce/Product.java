package com.example.ecommerce;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.ResultSet;

public class Product
{ //step1:Simple__
    private final IntegerProperty id;
    private final StringProperty name;
    private final DoubleProperty price;

    public Product(int id, String name, double price) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.price = new SimpleDoubleProperty(price);
    }

    //step3: getters
    public static ObservableList<Product> getAllProducts()
    {
        String selectAllProducts="SELECT id,name,price FROM product";
        return fetchProductData(selectAllProducts);
    }

    public static ObservableList<Product> fetchProductData(String query)
    {
        ObservableList<Product> data= FXCollections.observableArrayList();
        DbConnection dbConnection=new DbConnection();
        try
        {
            ResultSet rs=dbConnection.getQueryTable(query);
            while(rs.next())
            {
                Product product=new Product(rs.getInt("id"), rs.getString("name"),rs.getDouble("price"));
                data.add(product);
            }
            return data;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public int getId()
    {
        return id.get();
    }
    public String getName()
    {
        return name.get();
    }

    public StringProperty nameProperty()
    {
        return name;
    }

    public IntegerProperty idProperty() {
        return id;
    }
//
//    public void setId(int id) {
//        this.id.set(id);
//    }


//    public void setName(String name) {
//        this.name.set(name);
//    }
//
    public double getPrice() {
        return price.get();
    }
//
    public DoubleProperty priceProperty() {
        return price;
    }
//
//    public void setPrice(double price) {
//        this.price.set(price);
//    }
}