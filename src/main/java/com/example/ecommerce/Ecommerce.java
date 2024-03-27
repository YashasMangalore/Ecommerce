package com.example.ecommerce;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Ecommerce extends Application
{
    UserInterface userInterface = new UserInterface();
    @Override
    public void start(Stage stage)
    {
        Scene scene = new Scene(userInterface.createContent());
        stage.setTitle("E-Commerce");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args)
    {
        launch();
    }
}