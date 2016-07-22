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
    
    //Elements for Cart Stage
    private Button checkoutButton;
    private ArrayList<Item> itemsList;
    
    //Cart details
    private Cart cart;
    private ArrayList<menuItemGUI> itemsListGUI = new ArrayList<menuItemGUI>();
    
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
        mainScene = new Scene(mainPane, 900, 680);
        
        //Create checkout Button
        addCartButton = new Button("Add to Cart");
        addCartButton.setMinWidth(100);
        addCartButton.setOnAction(e->{
            loadSpinValues();
            buildCartStage(primaryStage);
                });
        rightBox = new VBox(10);
        rightBox.setPadding(new Insets(0,10,0,0));
        rightBox.getChildren().add(addCartButton);
        mainPane.setRight(rightBox);
        
        //Creates admin login button and sets action event
        AccessAdminButton = new Button("Admin Login");
        AccessAdminButton.setMinWidth(100);
        AccessAdminButton.setOnAction(e-> buildLogInStage());
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
                //Create name,des,price label
                Label nameLabel = new Label(itemsList.get(i).getName());
                Label descLabel = new Label(itemsList.get(i).getDescription());
                Label priceLabel = new Label(Double.toString(itemsList.get(i).getPrice()));
                foodMenuGrid.add(nameLabel, 0, j);
                foodMenuGrid.add(descLabel, 0, j+1);
                foodMenuGrid.add(priceLabel, 1, j+1);
                
                //Create spinner
                Spinner<Integer> spinBox = new Spinner(0,10,0);
                spinBox.setMaxWidth(65);
                foodMenuGrid.add(spinBox,2,j+1);
                
                //Place item into list of GUI items
                menuItemGUI temp = new menuItemGUI(
                itemsList.get(i).getItemID(),
                nameLabel,
                descLabel,
                priceLabel,
                spinBox);
                itemsListGUI.add(temp);
                j = j+2;
            }
            
        }
        
        j = 1; //j=1 represents the 1st entry to add for the menu section
        //Adds each Beverage form list into GUI
        for (int i=0; i< itemsList.size(); i++)
        {
            if (itemsList.get(i).getType() == 1)
            {
                //Create name,des,price label
                Label nameLabel = new Label(itemsList.get(i).getName());
                Label descLabel = new Label(itemsList.get(i).getDescription());
                Label priceLabel = new Label(Double.toString(itemsList.get(i).getPrice()));
                bevMenuGrid.add(nameLabel, 0, j);
                bevMenuGrid.add(descLabel, 0, j+1);
                bevMenuGrid.add(priceLabel, 1, j+1);
                
                //Create spinner
                Spinner<Integer> spinBox = new Spinner(0,10,0);
                spinBox.setMaxWidth(65);
                bevMenuGrid.add(spinBox,2,j+1);
                
                //Place item into list of GUI items
                menuItemGUI temp = new menuItemGUI(
                itemsList.get(i).getItemID(),
                nameLabel,
                descLabel,
                priceLabel,
                spinBox);
                itemsListGUI.add(temp);
                j = j+2; 
            }
            
        }
    }
    
    private class menuItemGUI{
        public int id;
        public Label name;
        public Label description;
        public Label price;
        public Spinner<Integer> spinner;
        public int spinValue;
        
        menuItemGUI(int varid, Label varname, Label vardesc, Label varprice,Spinner<Integer> varspinner){
            id = varid;
            name = varname;
            description = vardesc;
            price = varprice;
            spinner = varspinner;
            spinValue =0; //total quantity ordered on cart
        }
    }
    
    //Updates quantity selected
    private void loadSpinValues(){
        for (int i=0;i<itemsList.size();i++){
            itemsListGUI.get(i).spinValue += itemsListGUI.get(i).spinner.getValue();
        }
    }
    
    private void buildCartStage(Stage primaryStage){
        BorderPane cartBorderPane = new BorderPane();
        GridPane cartGrid = new GridPane();
        cartGrid.setPadding(new Insets(15,20,15,20));
        cartGrid.setHgap(50);
        cartBorderPane.setCenter(cartGrid);
        
        //Create category row
        Label nameTitle = new Label("Name");
        Label descTitle = new Label ("Description");
        Label priceTitle = new Label ("Price");
        Label quantityTitle = new Label("Quantity");
        nameTitle.setFont(Font.font("Arial",FontWeight.BOLD,20));
        descTitle.setFont(Font.font("Arial",FontWeight.BOLD,20));
        priceTitle.setFont(Font.font("Arial",FontWeight.BOLD,20));
        quantityTitle.setFont(Font.font("Arial",FontWeight.BOLD,20));
        cartGrid.add(nameTitle, 0, 0);
        cartGrid.add(descTitle, 1, 0);
        cartGrid.add(priceTitle, 2, 0);
        cartGrid.add(quantityTitle, 3, 0);
        cartGrid.setVgap(10);
        
        cart = new Cart();
        int row = 1;
        for (int i=0;i<itemsList.size();i++)
        {
            if(itemsListGUI.get(i).spinner.getValue() > 0){
            cartGrid.add(itemsListGUI.get(i).name, 0, row);
            cartGrid.add(itemsListGUI.get(i).description, 1, row);
            cartGrid.add(itemsListGUI.get(i).price, 2, row);
            cartGrid.add(new Label(Integer.toString(itemsListGUI.get(i).spinner.getValue())),3,row);
            cart.getCartItems().add(itemsListGUI.get(i).id);
            row++;
            }
        }
        
        cartCalculations();
        
        //Create top box for Title
        HBox titleHBox = new HBox();
        titleHBox.setAlignment(Pos.TOP_LEFT);
        titleHBox.setPadding(new Insets(20,20,5,20));
        Label cartTitle = new Label("Shopping Cart");
        cartTitle.setFont(Font.font("Arial",FontWeight.EXTRA_BOLD,25));
        titleHBox.getChildren().add(cartTitle);
        cartBorderPane.setTop(titleHBox);
        
        //Create bottom box for total summary
        VBox total_VBox = new VBox();
        Label subtotal = new Label("Subtotal:   "+cart.getSubTotal());
        Label taxRate = new Label("Tax rate:   "+(cart.getTaxRate()*100)+"%");
        Label total = new Label("Total:      "+cart.getTotal());
        int font_size = 25;
        subtotal.setFont(Font.font("Arial",FontWeight.BOLD,font_size));
        taxRate.setFont(Font.font("Arial",FontWeight.BOLD,font_size));
        total.setFont(Font.font("Arial",FontWeight.EXTRA_BOLD,font_size));
        total_VBox.setSpacing(20);
        total_VBox.getChildren().addAll(subtotal,taxRate,total);
        total_VBox.setAlignment(Pos.BOTTOM_RIGHT);
        total_VBox.setPadding(new Insets(0,50,50,0));
        cartBorderPane.setBottom(total_VBox);
        
        //Create "Proceed to checkout" Button
        checkoutButton = new Button("Proceed to checkout");
        total_VBox.getChildren().add(checkoutButton);
        //checkoutButton.setOnAction(e->);

        Scene cartScene = new Scene(cartBorderPane,900,680);
        primaryStage.setScene(cartScene);
        primaryStage.setTitle("Shopping Cart - Agile's Cafe");
    }
    
    //Calculates subtotal and total in Cart
    private void cartCalculations()
    {
        for(int i=0; i< cart.getCartItems().size();i++)
        {
            for(int j=0; j<itemsListGUI.size();j++)
            {
                if(cart.getCartItems().get(i)== itemsListGUI.get(j).id)
                {
                cart.calcSubTotal(Double.parseDouble(itemsListGUI.get(j).price.getText()),itemsListGUI.get(j).spinValue);
                }
            }
        }
        cart.calcTotal();
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
