package com.example.ecommerce;

import javafx.scene.Node;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class Order {
    private final SimpleIntegerProperty orderId;
    private final SimpleIntegerProperty groupOrderId;
    private final SimpleIntegerProperty quantity;
    private final SimpleStringProperty orderDate;
    private final SimpleStringProperty orderStatus;
    private final Product product;

    public Order(int orderId, int groupOrderId, Product product, int quantity, String orderDate, String orderStatus) {
        this.orderId = new SimpleIntegerProperty(orderId);
        this.groupOrderId = new SimpleIntegerProperty(groupOrderId);
        this.product = product;
        this.quantity = new SimpleIntegerProperty(quantity);
        this.orderDate = new SimpleStringProperty(orderDate);
        this.orderStatus = new SimpleStringProperty(orderStatus);
    }
    public Product getProduct()
    {
        return product;
    }

    //Getters for properties
    public int getOrderId() {
        return orderId.get();
    }

    public SimpleIntegerProperty orderIdProperty() {
        return orderId;
    }

    public int getQuantity() {
        return quantity.get();
    }

    public SimpleIntegerProperty quantityProperty() {
        return quantity;
    }

    public String getOrderDate() {
        return orderDate.get();
    }

    public SimpleStringProperty orderDateProperty() {
        return orderDate;
    }

    public String getOrderStatus() {
        return orderStatus.get();
    }

    public SimpleStringProperty orderStatusProperty() {
        return orderStatus;
    }


    public int getGroupOrderId() {
        return groupOrderId.get();
    }

    public void setGroupOrderId(int groupOrderId) {
        this.groupOrderId.set(groupOrderId);
    }

    public SimpleIntegerProperty groupOrderIdProperty() {
        return groupOrderId;
    }

    public static boolean placeOrder(Customer customer, Product product)//ordering a single product
    {
        String groupOrderPlacingId = "SELECT max(group_order_id)+1 id FROM orders";//group ordered id
        DbConnection dbConnection = new DbConnection();
        try {
            ResultSet rs = dbConnection.getQueryTable(groupOrderPlacingId);
            if (rs.next()) {
                String placeOrder = "INSERT INTO orders (group_order_id,customer_id,product_id) VALUES (" + rs.getInt("id") + "," + customer.getId() + "," + product.getId() + ")";
                return dbConnection.updateDataBase(placeOrder) != 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static int placeMultipleOrder(Customer customer, ObservableList<Product> productList)//ordering a single product
    {
        String groupMultipleOrderPlacingId = "SELECT max(group_order_id)+1 id FROM orders";//group ordered id
        DbConnection dbConnection = new DbConnection();
        try {
            int count = 0;
            ResultSet rs = dbConnection.getQueryTable(groupMultipleOrderPlacingId);
            if (rs.next()) {
                for (Product product : productList) {
                    String placeOrder = "INSERT INTO orders (group_order_id,customer_id,product_id) VALUES (" + rs.getInt("id") + "," + customer.getId() + "," + product.getId() + ")";
                    count += dbConnection.updateDataBase(placeOrder);
                }
                return count;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    public static VBox getOrdersPlaced(int itemsPerPage,List<Order> orders) {
        String query = "SELECT orders.id, orders.group_order_id, product.name, orders.quantity, " +
                "orders.order_date, orders.order_status " +
                "FROM orders JOIN product ON orders.product_id = product.id " +
                "GROUP BY orders.id " +
                "ORDER BY orders.id DESC";

        DbConnection dbConnection = new DbConnection();

        Pagination pagination = new Pagination((orders.size() + itemsPerPage - 1) / itemsPerPage, 0);
        pagination.setPageFactory ( new Callback<Integer, Node>(){
            @Override
            public Node call(Integer pageIndex) {
                VBox page = new VBox();
                TableView<Order> tableView = createTableViewForPage(pageIndex, itemsPerPage, orders);
                page.getChildren().add(tableView);
                return page;
            }
        });
        VBox vBox = new VBox();
        vBox.getChildren().addAll(pagination);
        return vBox;
    }

    private static TableView<Order> createTableViewForPage(int pageIndex, int itemsPerPage, List<Order> orders) {
        TableView<Order> tableView = new TableView<>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Create columns for the TableView
        TableColumn<Order, Integer> orderIdColumn = new TableColumn<>("Order ID");
        orderIdColumn.setCellValueFactory(new PropertyValueFactory<>("orderId"));

        TableColumn<Order, Integer> groupOrderIdColumn = new TableColumn<>("Group Order ID");
        groupOrderIdColumn.setCellValueFactory(new PropertyValueFactory<>("groupOrderId"));

        TableColumn<Order, String> productNameColumn = new TableColumn<>("Product Name");
        productNameColumn.setCellValueFactory(cellData -> cellData.getValue().getProduct().nameProperty());

        TableColumn<Order, Integer> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        TableColumn<Order, String> orderDateColumn = new TableColumn<>("Order Date");
        orderDateColumn.setCellValueFactory(new PropertyValueFactory<>("orderDate"));

        TableColumn<Order, String> orderStatusColumn = new TableColumn<>("Order Status");
        orderStatusColumn.setCellValueFactory(new PropertyValueFactory<>("orderStatus"));

        // Add the columns to the TableView
        tableView.getColumns().addAll(orderIdColumn, groupOrderIdColumn, productNameColumn, quantityColumn, orderDateColumn, orderStatusColumn);

        // Calculate indices for the current page
        int fromIndex = pageIndex * itemsPerPage;
        int toIndex = Math.min(fromIndex + itemsPerPage, orders.size());

        // Add items for the current page
        tableView.getItems().addAll(orders.subList(fromIndex, toIndex));

        return tableView;
    }
    public static List<Order> getAllOrdersForCustomer(Customer loggedInCustomer) {
        if (loggedInCustomer == null) {
            return new ArrayList<>(); // Return an empty list if no customer is logged in
        }
        int customerId = loggedInCustomer.getId(); // Assuming getId() returns the customer's ID
        String query = "SELECT orders.id, orders.group_order_id, orders.product_id, orders.quantity, " +
                "orders.order_date, orders.order_status, product.name AS product_name " +
                "FROM orders " +
                "JOIN product ON orders.product_id = product.id " +
                "WHERE orders.customer_id = ? " +
                "ORDER BY orders.order_date DESC";

        List<Order> orders = new ArrayList<>();
        try {
            DbConnection dbConnection = new DbConnection();
            PreparedStatement statement = dbConnection.prepareStatement(query);
            statement.setInt(1, customerId);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                int orderId = rs.getInt("id");
                int groupOrderId = rs.getInt("group_order_id");
                int productId = rs.getInt("product_id");
                String productName = rs.getString("product_name");
                int quantity = rs.getInt("quantity");
                String orderDate = rs.getString("order_date");
                String orderStatus = rs.getString("order_status");

                // Retrieve the Product object corresponding to the productName
                Product product = getProductByName(productName);
                // Create Order object and add it to the list
                Order order = new Order(orderId, groupOrderId, product, quantity, orderDate, orderStatus);
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    public static Product getProductByName(String productName) {
        // Query the database to retrieve the product based on the product name
        String query = "SELECT * FROM product WHERE name = ?";
        DbConnection dbConnection = new DbConnection();
        try {
            PreparedStatement statement = dbConnection.getConnection().prepareStatement(query);
            statement.setString(1, productName);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                // If a product with the given name is found, create and return the Product object
                int id = rs.getInt("id");
                double price = rs.getDouble("price");
                return new Product(id, productName, price);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if the product is not found
    }
}