package com.example.ecommerce;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

public class ProductList
{
    private TableView<Product> productTable;
    private Pagination pagination;
    private ObservableList<Product> allProducts;
    private TableView<Product> productTableView;
    public VBox createTable(ObservableList<Product> data)
    {
        //columns
        TableColumn<Product, Integer> id=new TableColumn<>("ID");//display
        id.setCellValueFactory(new PropertyValueFactory<>("id"));//referring to Product.java-> id

        TableColumn<Product, String> name = new TableColumn<>("NAME");//display
        name.setCellValueFactory(new PropertyValueFactory<>("name"));//referring to Product.java-> name

        TableColumn<Product, Double> price = new TableColumn<>("PRICE");
        price.setCellValueFactory(new PropertyValueFactory<>("price"));

        productTable=new TableView<>(); //creating  table
        productTable.getColumns().addAll(id,name,price);//adding column to table
        productTable.setItems(data);//adding data to table
        productTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);//extra column will be removed and filled accordingly

        VBox vBox=new VBox();
        vBox.setPadding(new Insets(10));
        vBox.getChildren().addAll(productTable);
        return vBox;
    }

    public VBox getAllProducts()
    {
        ObservableList<Product> data= Product.getAllProducts();
        return createTable(data);
    }

    public Product getSelectedProduct()
    {
        if (productTableView != null && productTableView.getSelectionModel() != null) {
            return productTableView.getSelectionModel().getSelectedItem();
        } else {
            System.err.println("Error: productTable or its selection model is null");
            return null;
        }
    }

    public VBox getProductsInCart(ObservableList<Product> data)
    {
        return createTable(data);
    }

    public ObservableList<Product> searchProducts(String searchQuery)
    {
        ObservableList<Product> searchResults = FXCollections.observableArrayList();
        // Iterate over all products and check if they match the search query
        for (Product product : Product.getAllProducts()) {
            if (product.getName().toLowerCase().contains(searchQuery.toLowerCase())) {
                // If the product name contains the search query, add it to the search results
                searchResults.add(product);
            }
        }
        return searchResults;
    }

    public VBox createTableWithPagination() {
        allProducts = Product.getAllProducts();
        int pageSize = 15; // Number of products per page
        int itemCount = allProducts.size();
        int pageCount = (itemCount + pageSize - 1) / pageSize; // Calculate total pages

        pagination = new Pagination(pageCount, 0);
        pagination.setPageFactory(pageIndex -> {
            int fromIndex = pageIndex * pageSize;
            int toIndex = Math.min(fromIndex + pageSize, itemCount);

            productTableView = new TableView<>();
            productTableView.setItems(FXCollections.observableArrayList(allProducts.subList(fromIndex, toIndex)));

            // Set up table columns
            TableColumn<Product, Integer> idColumn = new TableColumn<>("ID");
            idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

            TableColumn<Product, String> nameColumn = new TableColumn<>("Name");
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

            TableColumn<Product, Double> priceColumn = new TableColumn<>("Price");
            priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

            // Add columns to table view
            productTableView.getColumns().addAll(idColumn, nameColumn, priceColumn);
            /// Set resizing policy
            productTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

            return productTableView;
        });
//      pagination.setPadding(new Insets(15));
        VBox vBox = new VBox();
        vBox.getChildren().addAll(pagination);
        return vBox;
    }
}

//old code to add
//    public VBox getDummyTable()
//    {
//        ObservableList<Product> data= FXCollections.observableArrayList();
//        data.add(new Product(2,"iPhone",44565));
//        data.add(new Product(7,"Laptop",112999));
//        return createTable(data);
//    }