package com.example.ecommerce;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.layout.VBox;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import java.util.List;

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
  //  VBox productPage;
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
        Text userNameText=new Text("E-mail");
        Text passwordText=new Text("Password");
        //typing text field
        TextField userName=new TextField();//placeholder
        userName.setText("user@gmail.com");//placeholder
        userName.setPromptText("Type user-name here");

        PasswordField password=new PasswordField();
        password.setText("user");
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
                String email=userName.getText();
                String pass=password.getText();
                Login login =new Login();
                loggedInCustomer=login.customerLogin(email,pass);
                if(loggedInCustomer!=null)
                {
                    //messageLabel.setText("Welcome : "+loggedInCustomer.getName());
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
        // New user button
        Button newUserButton = new Button("New User");
        newUserButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                // Create a new dialog or form to get user details
                // For simplicity, let's assume you have a method to create a dialog for new user registration
                showNewUserRegistrationDialog();
            }
        });
        // Add the new user button to the login page
        loginPage.add(newUserButton, 1, 3);
    }
    private void showNewUserRegistrationDialog() {
        // Create a dialog or form to get user details for registration
        Dialog newUserDialog = new Dialog();
        newUserDialog.setTitle("New User Registration");

        // Create text fields for user details
        TextField nameField = new TextField();
        TextField emailField = new TextField();
        PasswordField passwordField = new PasswordField();
        TextField mobileField = new TextField();
        TextField addressField = new TextField(); // Address field added

        // Create labels for text fields
        Label nameLabel = new Label("Name*:");
        Label emailLabel = new Label("Email*:");
        Label passwordLabel = new Label("Password*:");
        Label mobileLabel = new Label("Mobile*:");
        Label addressLabel = new Label("Address (Optional):"); // Address label added

        // Add labels and text fields to the dialog
        GridPane gridPane = new GridPane();
        gridPane.add(nameLabel, 0, 0);
        gridPane.add(nameField, 1, 0);
        gridPane.add(emailLabel, 0, 1);
        gridPane.add(emailField, 1, 1);
        gridPane.add(passwordLabel, 0, 2);
        gridPane.add(passwordField, 1, 2);
        gridPane.add(mobileLabel, 0, 3);
        gridPane.add(mobileField, 1, 3);
        gridPane.add(addressLabel, 0, 4); // Add address label
        gridPane.add(addressField, 1, 4); // Add address field

        newUserDialog.getDialogPane().setContent(gridPane);

        // Add buttons for registration and cancellation
        ButtonType registerButtonType = new ButtonType("Register", ButtonBar.ButtonData.OK_DONE);
        newUserDialog.getDialogPane().getButtonTypes().addAll(registerButtonType, ButtonType.CANCEL);

        // Enable/disable register button based on text fields' content
        Node registerButton = newUserDialog.getDialogPane().lookupButton(registerButtonType);
        registerButton.setDisable(true);
        nameField.textProperty().addListener((observable, oldValue, newValue) -> {
            registerButton.setDisable(newValue.trim().isEmpty());
        });
        emailField.textProperty().addListener((observable, oldValue, newValue) -> {
            registerButton.setDisable(newValue.trim().isEmpty());
        });
        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            registerButton.setDisable(newValue.trim().isEmpty());
        });
        mobileField.textProperty().addListener((observable, oldValue, newValue) -> {
            registerButton.setDisable(newValue.trim().isEmpty());
        });

        newUserDialog.setResultConverter(dialogButton -> {
            if (dialogButton == registerButtonType) {
                // Get user details from text fields
                String name = nameField.getText();
                String email = emailField.getText();
                String password = passwordField.getText();
                String mobile = mobileField.getText();
                String address = addressField.getText();

                // Call addUser method to register the new user
                Login login = new Login();
                // Prepend "91" to the mobile number if it's not already present
                if (mobile.length()!=12) {
                    mobile = "91" + mobile;
                }
                boolean userAdded = login.addUser(name, email, password, mobile, address);
                if (userAdded)
                {
                    loggedInCustomer = login.customerLogin(email, password);
                    if (loggedInCustomer != null) {
                        // Display success message box
                        showAlert(Alert.AlertType.INFORMATION, "User Registration", "User registration successful!");
                        welcomeLabel.setText("welcome " + loggedInCustomer.getName());
                        headerBar.getChildren().add(welcomeLabel);
                        body.getChildren().clear();
                        body.getChildren().add(productListWithPagination);
                        footerBar.setVisible(true);
                    }
                    else
                    {
                        // Handle the case where login after registration fails
                        showAlert(Alert.AlertType.ERROR, "Login Error", "Failed to log in after registration. Please try logging in manually.");
                    }
                }
                else
                {
                    // Handle the case where user registration fails
                    loggedInCustomer = null;
                    showAlert(Alert.AlertType.ERROR, "Registration Error", "Failed to register user.");
                }

            }
            return null;
        });
        newUserDialog.showAndWait();
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
        Button searchButton = new Button("Search");
        searchButton.setOnAction(event -> performSearch(searchBar.getText()));
        
        signInButton=new Button("Sign In");
        welcomeLabel = new Label();
        Button cartButton=new Button("Cart");
        Button orderButton=new Button("Orders");

        headerBar=new HBox();//also can directly -- headerBar=new HBox(10);
        headerBar.setPadding(new Insets(10));
        headerBar.setSpacing(10);
        headerBar.setAlignment(Pos.CENTER);
        headerBar.getChildren().addAll(homeButton,searchBar,searchButton,cartButton,orderButton,signInButton);

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
                if(loggedInCustomer!=null && !itemsInCart.isEmpty())
                    prodPage.getChildren().add(placeOrderButton);
                body.getChildren().add(prodPage);
                if (loggedInCustomer == null && !headerBar.getChildren().contains(signInButton)) {
                    headerBar.getChildren().add(signInButton);
                }
                footerBar.setVisible(false);//makes it invisible
            }
        });

        orderButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent)
            {
                List<Order> orders = Order.getAllOrdersForCustomer(loggedInCustomer); // Fetch orders
                if (loggedInCustomer != null && !orders.isEmpty()) {
                    body.getChildren().clear();
                    VBox orderPage = Order.getOrdersPlaced(15,orders); // Pass orders to the method
                    orderPage.setPadding(new Insets(1));
                    orderPage.setAlignment(Pos.CENTER);
                    orderPage.setSpacing(10);
                    body.getChildren().add(orderPage);
                    if (loggedInCustomer == null && !headerBar.getChildren().contains(signInButton)) {
                        headerBar.getChildren().add(signInButton);
                    }
                    footerBar.setVisible(false);
                }
                else {
                    body.getChildren().clear();
                    VBox prodPage = productList.getProductsInCart(null);
                    prodPage.setPadding(new Insets(1));
                    prodPage.setAlignment(Pos.CENTER);
                    prodPage.setSpacing(10);
                    body.getChildren().add(prodPage);
                    footerBar.setVisible(false);//makes it invisible
                }
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
                    showDialog("Please SIGN-IN to place an order");
                    return;
                }
                int count = Order.placeMultipleOrder(loggedInCustomer,itemsInCart);
                if(count!=0)
                {
                    showDialog("Order for "+count+" products is placed successfully!");
                    itemsInCart.clear(); // Clear the cart
                    body.getChildren().clear();
//                  body.getChildren().add(productPage); --without pagination
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
                    if (productListWithPagination != null)
                    {
                        body.getChildren().add(productListWithPagination);
                    }
                    else
                    {
                        System.err.println("Error: productListWithPagination is null");
                        // Optionally, you can log the error or display an error message to the user
                    }

                    footerBar.setVisible(true);
                    if (loggedInCustomer == null && !headerBar.getChildren().contains(signInButton)) {
                        headerBar.getChildren().add(signInButton);
                    }
                } else
                {
                    System.err.println("Error: Body VBox is null");
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
            Pagination pagination = getPagination(searchResults);
            // Update the body to display search results with pagination
            body.getChildren().clear();
            body.getChildren().add(pagination);
            footerBar.setVisible(true);
            if (loggedInCustomer == null && !headerBar.getChildren().contains(signInButton)) {
                headerBar.getChildren().add(signInButton);
            }
        }
    }

    private Pagination getPagination(ObservableList<Product> searchResults)
    {
        Pagination pagination = new Pagination((searchResults.size() / ITEMS_PER_PAGE) + 1, 0);
        pagination.setPageFactory(pageIndex -> {
            int fromIndex = pageIndex * ITEMS_PER_PAGE;
            int toIndex = Math.min(fromIndex + ITEMS_PER_PAGE, searchResults.size());
            ObservableList<Product> productsOnPage = FXCollections.observableArrayList(searchResults.subList(fromIndex, toIndex));
            VBox searchResultPage = productList.createTable(productsOnPage);
            return searchResultPage;
        });
        return pagination;
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
            public void handle(ActionEvent actionEvent)
            {
                Product product= productList.getSelectedProduct();
                if(loggedInCustomer==null)
                {
                    showDialog("Please SIGN-IN to place an order");
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
                    showDialog("Please SIGN-IN to add to cart");
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

    private void showAlert(Alert.AlertType alertType, String title, String message)
    {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void showDialog(String message)
    {//Alert alert=new Alert(Alert.AlertType.WARNING);
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
//        body=new VBox();
//        body.setPadding(new Insets(10));
//        body.setAlignment(Pos.CENTER);
//        root.setCenter(body);
//        productPage=productList.getAllProducts();
//        body.getChildren().add(productPage);
//        root.setBottom(footerBar);
//        return root;
//    }