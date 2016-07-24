/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agilecafe362__;

import java.math.BigDecimal;
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
import javafx.event.ActionEvent;
import javafx.scene.control.Control;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;

public class AgileCafe362 extends Application {
    //Reference to primary stage
    Stage theStage; 
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
    private GridPane cartGrid;
    private Button checkoutButton;
    private ArrayList<Item> itemsList;
    private Button backButton;
    private Label subtotal;
    private Label total;
    private Label taxRate;
    private Scene cartScene;
    private Label subtotalLabel = new Label();
    private Label taxRateLabel = new Label();
    private Label totalLabel = new Label();
    private BorderPane cartBorderPane;
    
    //Cart lists
    private Cart cart = new Cart();
    private ArrayList<cartItem> cartList = new ArrayList<cartItem>();
    

    
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
        theStage = primaryStage;
        buildMainMenuStage();
        
        primaryStage.setTitle("Agile's Cafe Menu");
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }
    
    private void buildMainMenuStage(){
        
        //Layout for Main Menu -> BorderPane layout
        mainPane = new BorderPane();
        mainScene = new Scene(mainPane, 1000, 680);
        
        //Create "Add to Cart" Button
        addCartButton = new Button("Add to Cart");
        addCartButton.setMinWidth(100);
        addCartButton.setOnAction(e->{
            loadQuantityToCart();
            if(cart.cartPageToggle==true)
            {
                theStage.setScene(cartScene);
            }
            else
            {
                cart.cartPageToggle=true;
                buildCartStage();
            }
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
        
        //Loads items list and make a "cart item" for each item in the list
        loadCartItem();
        //Display each item w/ their info onto GUI
        addItemGUI();
        
        menuSection.getChildren().add(foodMenuGrid);
        menuSection.getChildren().add(bevMenuGrid); 
    }
    public void loadCartItem(){
        for(int i=0;i<itemsList.size();i++){
            cartItem temp = new cartItem();
            temp.id.setText(Integer.toString(itemsList.get(i).getItemID()));
            temp.name.setText(itemsList.get(i).getName());
            temp.desc.setText(itemsList.get(i).getDescription());
            temp.price.setText(Double.toString(itemsList.get(i).getPrice()));
            temp.item = itemsList.get(i);
            temp.spinBox.setId(temp.id.getText());
            temp.cb.setId(temp.id.getText());
            temp.removeButton.setId(temp.id.getText());
            cartList.add(temp);
        }
    }
    
    //Extract the quantity ordered and add to cart
    public void loadQuantityToCart(){
        for(int i=0;i<cartList.size();i++){
            if(cartList.get(i).spinBox.getValue()>0)
            {
                cartList.get(i).quantityOrdered += cartList.get(i).spinBox.getValue();
                cart.getCartItems().add(cartList.get(i));
            }
        }
        /*----DEBUG Purposes-------------------
        for(int i=0;i<cart.getCartItems().size();i++){
        System.out.println(cart.getCartItems().get(i).id);
        System.out.println(cart.getCartItems().get(i).quantityOrdered);
        }-------------------------------------*/
    }
    
    public void addItemGUI(){
        //Type 0 = Food
        //Type 1 = Beverage
        int j=1; //Refers to initial row to start populating data.
        for (int i=0;i<itemsList.size();i++)
        {
            //If food, add to food section
            if(itemsList.get(i).getType()==0)
            {
                foodMenuGrid.add(cartList.get(i).name,0,j);
                foodMenuGrid.add(cartList.get(i).desc, 0, j+1);
                foodMenuGrid.add(cartList.get(i).price, 1, j+1);
                foodMenuGrid.add(cartList.get(i).spinBox, 2, j+1);
                j=j+2;
            }
        }
        j=1; //Refers to initial row to start populating data.
        for(int i=0; i<itemsList.size();i++)
        {
            //If beverage, add to beverage section
            if(itemsList.get(i).getType()==1)
            {
                bevMenuGrid.add(cartList.get(i).name,0,j);
                bevMenuGrid.add(cartList.get(i).desc, 0, j+1);
                bevMenuGrid.add(cartList.get(i).price, 1, j+1);
                bevMenuGrid.add(cartList.get(i).spinBox, 2, j+1);
                j=j+2;
            }
        }
        
    }
 
    private void buildCartStage(){
        //Create layouts to organize elements
        cartBorderPane = new BorderPane();
        cartGrid = new GridPane();
        cartGrid.setPadding(new Insets(15,20,15,20));
        cartGrid.setHgap(50);
        cartBorderPane.setCenter(cartGrid);
        
        //Create title box on top
        HBox titleHBox = new HBox();
        titleHBox.setAlignment(Pos.TOP_LEFT);
        titleHBox.setPadding(new Insets(20,20,5,20));
        Label cartTitle = new Label("Shopping Cart");
        cartTitle.setFont(Font.font("Arial",FontWeight.EXTRA_BOLD,25));
        titleHBox.getChildren().add(cartTitle);
        cartBorderPane.setTop(titleHBox);
        
        //Create back to main menu button
        backButton = new Button("Back to Menu");
        titleHBox.getChildren().add(backButton);
        titleHBox.setSpacing(670);
        backButton.setOnAction(e->{
            theStage.setScene(mainScene);
        });
        
        //Create category labels
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
        
        int j=1;
        for(int i=0;i<cart.getCartItems().size();i++)
        {
            cartGrid.add(cart.getCartItems().get(i).name, 0, j);
            cartGrid.add(cart.getCartItems().get(i).desc, 1, j);
            cartGrid.add(cart.getCartItems().get(i).price,2,j);
            cart.getCartItems().get(i).cb.setValue(cart.getCartItems().get(i).quantityOrdered);
            cartGrid.add(cart.getCartItems().get(i).cb, 3, j);
            cartGrid.add(cart.getCartItems().get(i).removeButton, 4, j);
            cart.getCartItems().get(i).removeButton.setOnAction(e->{
                removeFromCart(e);
                buildCartStage();
                    });
            j++;
        }
        //Calculates totals and sets appropriate labels
        calcCartTotals();
        
        //Create bottom box for total summary
        VBox total_VBox = new VBox();
        int font_size = 20;
        subtotalLabel.setFont(Font.font("Arial",FontWeight.BOLD,font_size));
        totalLabel.setFont(Font.font("Arial",FontWeight.BOLD,font_size));
        taxRateLabel.setFont(Font.font("Arial",FontWeight.EXTRA_BOLD,font_size));
        total_VBox.setSpacing(20);
        total_VBox.getChildren().addAll(subtotalLabel,taxRateLabel,totalLabel);
        total_VBox.setAlignment(Pos.BOTTOM_RIGHT);
        total_VBox.setPadding(new Insets(0,50,50,0));
        cartBorderPane.setBottom(total_VBox);
        
        //Create "Proceed to checkout" Button
        checkoutButton = new Button("Proceed to checkout");
        total_VBox.getChildren().add(checkoutButton);
        //checkoutButton.setOnAction(e->); 
        
        //Display the scene
        cartScene = new Scene(cartBorderPane,1000,680);
        theStage.setScene(cartScene);
        theStage.setTitle("Shopping Cart - Agile's Cafe");
    }
    
    //Calculates totals and sets appropriate labels
    private void calcCartTotals(){
        double totalC=0;
        double subtotalC=0;
        for(int i=0;i<cart.getCartItems().size();i++){
            subtotalC+= cart.getCartItems().get(i).item.getPrice()* cart.getCartItems().get(i).quantityOrdered;
        }
        totalC=subtotalC+(subtotalC*cart.getTaxRate());
        
        //Sets precision for values and sets text
        Double toBeTruncated = new Double(totalC);
        totalC = new BigDecimal(toBeTruncated).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
        toBeTruncated = new Double(subtotalC);
        subtotalC= new BigDecimal(toBeTruncated).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
        subtotalLabel.setText("Subtotal:   "+subtotalC);
        totalLabel.setText("Total: "+totalC);
        taxRateLabel.setText("Tax rate:    "+Double.toString(cart.getTaxRate()));
    }
    private void removeFromCart(ActionEvent e){
        for(int i=0; i<cart.getCartItems().size();i++)
        {
            if(((Control)e.getSource()).getId().compareTo(Integer.toString(cart.getCartItems().get(i).item.getItemID()))==0)
                    {
                        cart.getCartItems().get(i).quantityOrdered=0;
                        cart.getCartItems().remove(i);
                        break;
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
