package com.example.ecommerce;

import javafx.collections.ObservableList;
import javafx.scene.layout.VBox;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

public class UserInterface {
    GridPane loginPage;//loginPage variable
    Customer loggedInCustomer;
    ProductList productList=new ProductList();
    Label welcomeLabel;
    Button signInButton;
    Button placeOrderButton=new Button("Place Order");
    HBox headerBar;
    HBox footerBar;
    VBox body;
    VBox productPage;
    ObservableList<Product> itemsInCart= FXCollections.observableArrayList();//for products getting selected
    private VBox productListWithPagination;
    private static final int ITEMS_PER_PAGE = 15;

    public BorderPane createContent()
    {
        BorderPane root = new BorderPane();
        root.setPrefSize(890, 650);

        // Set up header bar
        root.setTop(headerBar);

        // Set up body with pagination
        body = new VBox();
        body.setPadding(new Insets(10));
        body.setAlignment(Pos.CENTER);

        // Create product list with pagination and assign it to productListWithPagination
        productListWithPagination = productList.createTableWithPagination();

        if (productListWithPagination != null) {
            // Add product list with pagination to the body
            body.getChildren().add(productListWithPagination);
        } else {
            // Handle the case where productListWithPagination is null
            System.err.println("Error: productListWithPagination is null");
            // Optionally, you can log the error or display an error message to the user
        }

        // Set body as center of root
        root.setCenter(body);

        // Set up footer bar
        root.setBottom(footerBar);

        return root;
    }

    public UserInterface()
    {
        createLoginPage();
        createHeaderBar();
        createFooterBar();
    }

    private void createLoginPage()
    {
        Text userNameText=new Text("User-Name");
        Text passwordText=new Text("Password");

        //typing text field
        TextField userName=new TextField();//placeholder
        userName.setText("yashas.me18@sahyadri.edu.in");//placeholder
        userName.setPromptText("Type user-name here");

        PasswordField password=new PasswordField();
        password.setText("mangalore");
        password.setPromptText("Type password here");
        Label messageLabel=new Label("");
        //login button
        Button loginButton=new Button("Login");

        loginPage=new GridPane();

//        loginPage.setStyle("-fx-background-color: lightgrey");
        loginPage.setAlignment(Pos.CENTER);
        loginPage.setHgap(10);
        loginPage.setVgap(10);

        loginPage.add(userNameText,0,0);
        loginPage.add(userName,1,0);
        loginPage.add(passwordText,0,1);// 0 refers to the column and 1 refers to the row.
        loginPage.add(password,1,1);

        loginPage.add(messageLabel,0,2);
        loginPage.add(loginButton,1,2);

        loginButton.setOnAction(new EventHandler<>() //Explicit type argument ActionEvent can be replaced with <>
        {
            @Override
            public void handle(ActionEvent actionEvent)
            {
                String name=userName.getText();
                String pass=password.getText();
                Login login =new Login();
                loggedInCustomer=login.customerLogin(name,pass);
                if(loggedInCustomer!=null)
                {
                    messageLabel.setText("Welcome : "+loggedInCustomer.getName());
                    welcomeLabel.setText("welcome "+loggedInCustomer.getName());
                    headerBar.getChildren().add(welcomeLabel);
                    body.getChildren().clear();
//                    body.getChildren().add(productPage);
                    body.getChildren().add(productListWithPagination);
                    footerBar.setVisible(true);
                }
                else
                {
                    messageLabel.setText("Login failed. Please provide correct credentials.");
                }
            }
        });
    }

    private void createHeaderBar()
    {
        Button homeButton=new Button();
        Image image=new Image("C:\\Users\\yasha\\Desktop\\Yashas git\\ecommerce\\Ecommerce\\src\\logoHomePage.jpg");
        ImageView imageView=new ImageView();
        imageView.setImage(image);
        imageView.setFitHeight(28);
        imageView.setFitWidth(45);
        homeButton.setGraphic(imageView);

        TextField searchBar=new TextField();
        searchBar.setPromptText("Search here");
        searchBar.setPrefWidth(220);

//        Button searchButton=new Button("Search");
        Button searchButton = new Button("Search");
        searchButton.setOnAction(event -> performSearch(searchBar.getText()));
        
        signInButton=new Button("Sign In");
        welcomeLabel = new Label();

        Button cartButton=new Button("Cart");
        //Button orderButton=new Button("Orders");

        headerBar=new HBox();//also can directly -- headerBar=new HBox(10);
        headerBar.setPadding(new Insets(10));
        headerBar.setSpacing(10);
        headerBar.setAlignment(Pos.CENTER);
        headerBar.getChildren().addAll(homeButton,searchBar,searchButton,signInButton,cartButton);

        signInButton.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                body.getChildren().clear();//clear everything
                body.getChildren().add(loginPage);//put login page
                headerBar.getChildren().remove(signInButton);//Removing particular thing
                footerBar.setVisible(false);
            }
        });

        cartButton.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent actionEvent)
            {
                body.getChildren().clear();
                VBox prodPage=productList.getProductsInCart(itemsInCart);
                prodPage.setPadding(new Insets(1));
                prodPage.setAlignment(Pos.CENTER);
                prodPage.setSpacing(10);
                prodPage.getChildren().add(placeOrderButton);
                body.getChildren().add(prodPage);
                footerBar.setVisible(false);//makes it invisible
            }
        });

        placeOrderButton.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                //need list of products and customer
                if(itemsInCart==null)
                {//prompt to select first
                    showDialog("Please add a product in the cart to place an order!");
                    return;
                }
                if(loggedInCustomer==null)
                {
                    showDialog("Please LOGIN to place an order");
                    return;
                }
                int count = Order.placeMultipleOrder(loggedInCustomer,itemsInCart);
                if(count!=0)
                {
                    showDialog("Order for "+count+" products is placed successfully!");
                    itemsInCart.clear(); // Clear the cart
                    body.getChildren().clear();
//                    body.getChildren().add(productPage);
                    body.getChildren().add(productListWithPagination);
                    footerBar.setVisible(true);
                    if(loggedInCustomer==null && !headerBar.getChildren().contains(signInButton))
                    {   //headerBar.getChildren().indexOf(signInButton)==-1
                        headerBar.getChildren().add(signInButton);
                    }
                }
                else
                {
                    showDialog("Order failed!");
                }
            }
        });

        homeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (body != null) {
                    body.getChildren().clear();
                    if (productListWithPagination != null) {
                        body.getChildren().add(productListWithPagination);
                    } else {
                        System.err.println("Error: productListWithPagination is null");
                        // Optionally, you can log the error or display an error message to the user
                    }
                    footerBar.setVisible(true);
                    if (loggedInCustomer == null && !headerBar.getChildren().contains(signInButton)) {
                        headerBar.getChildren().add(signInButton);
                    }
                } else {
                    System.err.println("Error: Body VBox is null");
                    // Optionally, you can log the error or display an error message to the user
                }
            }
        });

    }

    private void performSearch(String searchQuery)
    {
        if (searchQuery.isEmpty()) {
            showDialog("No results found.");
            return;
        }
        // Perform search based on the search query
        ObservableList<Product> searchResults = productList.searchProducts(searchQuery);

        // Display search results
        if (searchResults.isEmpty()) {
            showDialog("No results found.");
        } else {
            // Create pagination for the search results
            Pagination pagination = new Pagination((searchResults.size() / ITEMS_PER_PAGE) + 1, 0);
            pagination.setPageFactory(pageIndex -> {
                int fromIndex = pageIndex * ITEMS_PER_PAGE;
                int toIndex = Math.min(fromIndex + ITEMS_PER_PAGE, searchResults.size());
                ObservableList<Product> productsOnPage = FXCollections.observableArrayList(searchResults.subList(fromIndex, toIndex));
                VBox searchResultPage = productList.createTable(productsOnPage);
                return searchResultPage;
            });

            // Update the body to display search results with pagination
            body.getChildren().clear();
            body.getChildren().add(pagination);
        }
    }

    private void createFooterBar()
    {
        Button buyNowButton=new Button("Buy Now");
        Button addToCart=new Button("Add to cart");

        footerBar=new HBox();//also can directly -- headerBar=new HBox(10);
        footerBar.setPadding(new Insets(10));
        footerBar.setSpacing(10);
        footerBar.setAlignment(Pos.CENTER);
        footerBar.getChildren().addAll(buyNowButton,addToCart);

        buyNowButton.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Product product= productList.getSelectedProduct();
                if(loggedInCustomer==null)
                {
                    showDialog("Please LOGIN to place an order");
                    return;
                }
                if(product==null)
                {//prompt to select first
                    showDialog("Please select a product to place an order!");
                    return;
                }
                boolean status = Order.placeOrder(loggedInCustomer,product);
                if(status)
                {
                    showDialog("Order placed successfully!");
                }
                else
                {
                    showDialog("Order failed!");
                }
            }
        });

        addToCart.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent)
            {
                Product product= productList.getSelectedProduct();
                if(loggedInCustomer==null)
                {
                    showDialog("Please LOGIN to add to cart");
                    return;
                }
                if(product==null)
                {//prompt to select first
                    showDialog("Please select a product to add to cart!");
                    return;
                }
                itemsInCart.add(product);//else
                showDialog("Selected product is added to the cart!");
            }
        });
    }

    private void showDialog(String message)
    {
//      Alert alert=new Alert(Alert.AlertType.WARNING);
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.setTitle("Message");
        alert.showAndWait();
    }
}

//OLD code without pagination

//    public BorderPane createContent()
//    {
//        BorderPane root=new BorderPane();
//        root.setPrefSize(650,550);//width,height
//        //root.getChildren().add(loginPage);//method to add nodes as children to pane but now were using borderpane so not used
//        root.setTop(headerBar);
//        // root.setCenter(loginPage);
//
//        body=new VBox();
//        body.setPadding(new Insets(10));
//        body.setAlignment(Pos.CENTER);
//
//        root.setCenter(body);
//        productPage=productList.getAllProducts();
//        body.getChildren().add(productPage);
//
//        root.setBottom(footerBar);
//        return root;
//    }