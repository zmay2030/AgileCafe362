/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agilecafe362__;

import static com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type.Int;
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
import javafx.scene.control.PasswordField;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;

public class AgileCafe362 extends Application {
    
    //Elements for Main Menu Stage
    private Button AccessAdminButton;
    private Button addCartButton;
    private VBox rightBox;
    private BorderPane mainPane;
    private Scene mainScene;
    private GridPane foodMenuGrid;
    private GridPane bevMenuGrid; //bev is short for beverage
     
    //Elements for Admin Login Stage
    private Label userName;
    private Label password;
    private TextField userTextField;
    private PasswordField pwBox;
    private Text errorLogin;
    private Scene logInScene;
    private Stage logInStage;
    
    private ArrayList<Item> itemsList;
    
    @Override
    public void init() throws Exception {
        //init() runs before application starts
        //Connect to database
        SQL_DB mysqlDB = new SQL_DB();
        mysqlDB.connect(); 
        
        //Query info from database into application
        this.itemsList = mysqlDB.getAllItems(); 
        
        mysqlDB.closeConn();
    }
    
    @Override
    public void stop() throws Exception {
        //stop() runs after application stops

        //Close mysql connection  
        //Save info into database
        SQL_DB mysqlDB = new SQL_DB();
        mysqlDB.connect(); 
        
        //Save info from application into database
    }
    
    @Override
    public void start(Stage primaryStage) {
  
        buildMainMenuStage(primaryStage);
        
        primaryStage.setTitle("Agile's Cafe Menu");
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }
    
    private void buildMainMenuStage(Stage primaryStage){
        
        //Layout for Main Menu -> BorderPane layout
        mainPane = new BorderPane();
        mainScene = new Scene(mainPane, 800, 680);
        
        //Create checkout Button
        addCartButton = new Button("Add to Cart");
        addCartButton.setMinWidth(100);
        rightBox = new VBox(10);
        rightBox.setPadding(new Insets(0,10,0,0));
        rightBox.getChildren().add(addCartButton);
        mainPane.setRight(rightBox);
        
        //Creates admin login button and sets action event
        AccessAdminButton = new Button("Admin Login");
        AccessAdminButton.setMinWidth(100);
        AccessAdminButton.setOnAction(e->buildLogInStage());
        mainPane.setBottom(AccessAdminButton);

        //Creates two sections of menu
        foodMenuGrid = new GridPane();
        bevMenuGrid = new GridPane();
        foodMenuGrid.setHgap(10);
        foodMenuGrid.setVgap(10);
        bevMenuGrid.setVgap(10);
        bevMenuGrid.setHgap(10);
        
        //Creates section labels
        Label food = new Label("Food");
        Label bev = new Label("Beverage");
        food.setFont(Font.font("Arial", FontWeight.BOLD, 30));
        bev.setFont(Font.font("Arial",FontWeight.BOLD,30));
        
        //Creates center section where the menu items are displayed
        VBox menuSection = new VBox();
        menuSection.setSpacing(20);
        mainPane.setCenter(menuSection);
        mainPane.setPadding(new Insets(20,20,20,20));
        
        //Creates title of the menu
        HBox menuBoxTop = new HBox();
        menuBoxTop.setPadding(new Insets(0,25,15,25));
        Label menuTitle = new Label("Agile's Cafe Menu");
        menuBoxTop.setAlignment(Pos.CENTER);
        menuTitle.setFont(Font.font("Arial",FontWeight.EXTRA_BOLD,40));
        menuBoxTop.getChildren().add(menuTitle);
        mainPane.setTop(menuBoxTop);
        
        //Displays "Food" and "Beverage" Labels
        foodMenuGrid.add(food,0,0,2,1);
        bevMenuGrid.add(bev,0,0,2,1);
        //foodMenuGrid.setGridLinesVisible(true);
        //bevMenuGrid.setGridLinesVisible(true);
        
        //Display each item w/ their info onto GUI
        addItemGUI();
        
        menuSection.getChildren().add(foodMenuGrid);
        menuSection.getChildren().add(bevMenuGrid); 
    }
    
    //Purpose: Display each item with their info onto GUI
    private void addItemGUI(){
        //Note1: j is the row counter for the grid layout, used for adding items to their
        //correct row.
        //Note2: j = 0 is the 1st row, which contains the label Food or Beverage,
        //depending if it is in foodMenuGrid or bevMenuGrid
        int j = 1; //j=1 represents the 1st entry to add for the menu section
        
        //Adds each Food from list into GUI
        //Type 0 = Food
        //Type 1 = Beverage
        for (int i=0; i< itemsList.size(); i++)
        {
            if (itemsList.get(i).getType() == 0)
            {
                foodMenuGrid.add(new Label(itemsList.get(i).getName()), 0, j);
                foodMenuGrid.add(new Label(itemsList.get(i).getDescription()), 0, j+1);
                foodMenuGrid.add(new Label(Double.toString(itemsList.get(i).getPrice())), 1, j+1);
                Spinner temp = new Spinner(0,10,0);
                temp.setMaxWidth(65);
                foodMenuGrid.add(temp,2,j+1);
                j = j+2;
            }
            
        }
        
        j = 1; //j=1 represents the 1st entry to add for the menu section
        //Adds each Beverage form list into GUI
        for (int i=0; i< itemsList.size(); i++)
        {
            if (itemsList.get(i).getType() == 1)
            {
                //add Name, description, and price
                bevMenuGrid.add(new Label(itemsList.get(i).getName()), 0, j);
                bevMenuGrid.add(new Label(itemsList.get(i).getDescription()), 0, j+1);
                bevMenuGrid.add(new Label(Double.toString(itemsList.get(i).getPrice())), 1, j+1);
                
                //Add spinner box
                Spinner temp = new Spinner(0,10,0);
                temp.setMaxWidth(65);
                bevMenuGrid.add(temp,2,j+1);
                j = j+2;
            }
            
        }
    }

    private void buildLogInStage(){
        
        //Set up grid
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10); 
        grid.setVgap(10);
        grid.setPadding(new Insets(25,25,25,25));
        
        //Add title to scene
        Text title = new Text("Admin Login Page");
        title.setFont(Font.font("Times New Roman", FontWeight.NORMAL, 20));
        grid.add(title,0,0,3,1);
        
        //Add username label
        userName = new Label("Username: ");
        grid.add(userName, 0, 1);
        
        //Add password label
        password = new Label("Password: ");
        grid.add(password,0,2);
        
        //Add username text field for user input
        userTextField = new TextField();
        userTextField.setPromptText("Enter username");
        grid.add(userTextField,1,1);
        
        //Add password field for user input
        pwBox = new PasswordField();
        pwBox.setPromptText("Enter password");
        grid.add(pwBox,1,2);
        
        //Add sign in button
        Button loginButton = new Button("Sign in");
        HBox hboxLogin = new HBox(10);
        hboxLogin.setAlignment(Pos.BOTTOM_RIGHT);
        hboxLogin.getChildren().add(loginButton);
        grid.add(hboxLogin,1,4);
        
        //Add login error message
        errorLogin = new Text();
        grid.add(errorLogin, 1, 6);
        
        //Set handling event if login error
        loginButton.setOnAction(e->errorLogin());
        
        //Display Admin window
        logInScene = new Scene(grid,500,500);
        logInStage = new Stage();
        logInStage.setTitle("Admin Login Page");
        logInStage.setScene(logInScene);
        logInStage.initModality(Modality.APPLICATION_MODAL);
        logInStage.showAndWait();
    }

        //Method to handle action
    private void errorLogin(){
        if (userTextField.getText().equals("123") 
                 && pwBox.getText().equals("123"))
        {
            //User entered correct credentials
            //Switch to Admin stage
        }
        else
        {
            //User entered incorrect credentials
            //Display error message
            errorLogin.setFill(Color.FIREBRICK);
            errorLogin.setText("Error: Wrong User/PW\nHINT: 123");
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
}
