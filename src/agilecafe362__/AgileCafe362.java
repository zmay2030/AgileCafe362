/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agilecafe362__;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.ArrayList;

public class AgileCafe362 extends Application {
    
    Button AccessAdminButton;
    BorderPane mainPane;
    Scene mainScene;
    
    @Override
    public void init() throws Exception {
        //init() runs before application starts
        //Connect to database
        SQL_DB mysqlDB = new SQL_DB();
        mysqlDB.connect();
        //Query info from database into application
        
    }
    
    @Override
    public void stop() throws Exception {
        //stop() runs after application stops
        //Save info into database
        
        //Close connection to database
    }
    
    @Override
    public void start(Stage primaryStage) {
        
        
        mainPane = new BorderPane();
        AccessAdminButton = new Button("Admin Login");
        //AccessAdminButton.setOnAction(e->buildLogInStage(primaryStage));
        mainPane.setBottom(AccessAdminButton);
        mainPane.setAlignment(AccessAdminButton, Pos.BOTTOM_RIGHT);
        mainPane.setMargin(AccessAdminButton, new Insets(15,25,20,15));
        mainScene = new Scene(mainPane, 700, 700);
        
        primaryStage.setTitle("Main Menu");
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }
    
    private void buildLogInStage(Stage primaryStage){
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        Text title = new Text("Admin LogIn Page");
        title.setFont(Font.font("Times New Roman", FontWeight.BOLD, 15));
        grid.add(title,0,0,3,1);
        
        Label userName = new Label("Username: ");
        grid.add(userName, 0, 1);
        
        Scene logInScene = new Scene(grid,500,500);
        Stage logInStage = new Stage();
        logInStage.setTitle("Admin Log in Page");
        logInStage.setScene(logInScene);
        primaryStage.close();
        primaryStage.setOpacity(.5);
        logInStage.show();
        
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
