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
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;

public class AgileCafe362 extends Application {
    //Used as a reference to primary stage
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
    private Scene cartScene;
    private final Label subtotalLabel = new Label();
    private final Label taxRateLabel = new Label();
    private final Label totalLabel = new Label();
    private BorderPane cartBorderPane;
    private Boolean isCartStageBuilt = false;
    
    //Cart lists
    private final Cart cart = new Cart();
    private final ArrayList<cartItem> cartList = new ArrayList<>();
    
    @Override
    public void init() throws Exception {
        //init() runs before application starts
        //Connect to database
        SQL_DB mysqlDB = new SQL_DB();
        mysqlDB.connect(); 
        
        //Query info from database into application
        this.itemsList = mysqlDB.getAllItems(); 
        
        // Get each item and output each of its addons
        for(Item item: itemsList)
        {
            System.out.print("*** NAME: "+item.getName()+"\n");
            // Get all addon list for the current item
            ArrayList<addOn> addonList = item.getAddonList();
            for(addOn adn: addonList)
            {
                System.out.print(adn.getName()+" "); 
            }
            System.out.print("\n\n");
            
        }
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
        addCartButton.setOnAction(e->addToCartHandler());
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
        
        //Loads items list and make a "cart item" for each item in the list.
        //If cart stage is already built, then no need to convert list of items again.
        if(isCartStageBuilt==false){
            loadCartItem();
        }
        
        //Display each item w/ their info onto GUI
        addItemGUI();
        
        menuSection.getChildren().add(foodMenuGrid);
        menuSection.getChildren().add(bevMenuGrid); 
    }

    private void addToCartHandler(){
            loadQuantityToCart();
            //If stage hasn't been built, then load quantity to cart, and build cart stage as usual.
            if(isCartStageBuilt==false || cart.getCartItems().isEmpty()){
                buildCartStage();
                isCartStageBuilt=true;
            }
            else if(isCartStageBuilt==true)
            {
                buildCartStage();
            }
    }
    //Makes the itemsList(items from database) into cartList(displayable list of items).
    public void loadCartItem(){
        for(int i=0;i<itemsList.size();i++){
            //Adds general item information into the "cartItem" object and plces it into a list.
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
        for(int i=0;i<cartList.size();i++)
        {
            cartList.get(i).cb.setOnAction(e->comboBoxHandler(e));
        }
    }
    
    //In cart stage, this handles the event when someone changes quantity using combo box, and 
    //automatically updates the summary amount labels
    private void comboBoxHandler(ActionEvent e)
    {
        for(int i=0;i<cart.getCartItems().size();i++)
        {
            if(((Control)e.getSource()).getId().compareTo(cart.getCartItems().get(i).id.getText())==0)
            {
                cart.getCartItems().get(i).quantityOrdered=cart.getCartItems().get(i).cb.getValue();
                calcCartTotals();
            }
        }
        
    }
    
    //Extract the quantity ordered in the main menu and add to cart
    public void loadQuantityToCart(){
        //If cart stage hasn't been built or if cart is empty, add the items to the cart.
        if(isCartStageBuilt==false || cart.getCartItems().isEmpty())
        {
            for(int i=0;i<cartList.size();i++){
            if(cartList.get(i).spinBox.getValue()>0)
            {
                cartList.get(i).quantityOrdered = cartList.get(i).spinBox.getValue();
                cartList.get(i).isInCart = true;
                cart.getCartItems().add(cartList.get(i));
            }
        }
        }
        //If cart stage has been built and if there are items in the cart, update the quantity value.
        else if(isCartStageBuilt == true && cart.getCartItems().size()>0)
        {
            for(int i=0;i<cartList.size();i++)
            {
                //If item is already in cart, just add the quantity to existing cart item.
                if(cartList.get(i).isInCart){
                    cartList.get(i).quantityOrdered += cartList.get(i).spinBox.getValue();
                }
                //If item is not already in the cart, then add the item to the cart with its quantity selected.
                else
                {
                    cartList.get(i).quantityOrdered += cartList.get(i).spinBox.getValue();
                    cartList.get(i).isInCart = true;
                    cart.getCartItems().add(cartList.get(i));
                }
            }
        }

        /*----DEBUG Purposes-------------------
        for(int i=0;i<cart.getCartItems().size();i++){
        System.out.println(cart.getCartItems().get(i).id);
        System.out.println(cart.getCartItems().get(i).quantityOrdered);
        }-------------------------------------*/
    }
    
    //Populates the items on the main menu
    public void addItemGUI(){
        //Type 0 = Food
        //Type 1 = Beverage
        int j=1; //Refers to initial row to start populating data.
        //Adds the items into the "Food" section of the menu.
        for (int i=0;i<itemsList.size();i++)
        {
            //If food, add to food section
            if(itemsList.get(i).getType()==0)
            {
                foodMenuGrid.add(cartList.get(i).name,0,j);
                foodMenuGrid.add(cartList.get(i).desc, 0, j+1);
                foodMenuGrid.add(cartList.get(i).price, 1, j+1);
                foodMenuGrid.add(cartList.get(i).spinBox, 2, j+1);
                
                //Display Addon for food item
                int skipVar=0;
                for(int m=0; m<cartList.get(i).item.getAddonList().size();m++){
                    foodMenuGrid.add(cartList.get(i).item.getAddonList().get(m).checkBox,0,m+2+j);
                    skipVar++;
                }
                j+=skipVar;
                j=j+2;
            }
        }
        j=1; //Refers to initial row to start populating data.
        //Adds the items into the "Beverage" section of the menu.
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
        cartGrid.setAlignment(Pos.TOP_LEFT);
        cartBorderPane.setCenter(cartGrid);
        //cartGrid.setGridLinesVisible(true);
        
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
        backButton.setOnAction(e->backButtonHandler());

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
        
        //Diplays the items from cart into summary page.
        int j=1;
        for(int i=0;i<cart.getCartItems().size();i++)
        {
            //Double check to make sure conditions are true before displaying into cart.
            //Item in the cart must actually belong in the cart, and the quantity ordered must be greater than 0.
            //If check is correct, then add the item to the display.
            if(cart.getCartItems().get(i).isInCart==true && cart.getCartItems().get(i).quantityOrdered>0){
                //Since cart scene has already been built, remove existing children if it exists and then add updated one.
                if(isCartStageBuilt==true){
                cartGrid.getChildren().remove(cart.getCartItems().get(i).name);
                cartGrid.getChildren().remove(cart.getCartItems().get(i).desc);
                cartGrid.getChildren().remove(cart.getCartItems().get(i).price);
                cartGrid.getChildren().remove(cart.getCartItems().get(i).cb);
                cartGrid.getChildren().remove(cart.getCartItems().get(i).removeButton);
                cartGrid.getChildren().remove(cart.getCartItems().get(i).addonInfo);
                }
                cartGrid.add(cart.getCartItems().get(i).name, 0, j);
                cartGrid.add(cart.getCartItems().get(i).desc, 1, j);
                cartGrid.add(cart.getCartItems().get(i).price,2,j);
                cart.getCartItems().get(i).cb.setValue(cart.getCartItems().get(i).quantityOrdered);
                cartGrid.add(cart.getCartItems().get(i).cb, 3, j);
                cartGrid.add(cart.getCartItems().get(i).removeButton, 4, j);
                cart.getCartItems().get(i).removeButton.setOnAction(e->{
                //When user presses "Remove", then remove item from cart and refresh the page.
                removeFromCart(e);
                buildCartStage();
                    });
                //Create the addon for the item if checked in the main menu
                int skipValue=0;
                for(int m=0;m<cart.getCartItems().get(i).item.getAddonList().size();m++){
                    //If addon is checked, add it to the summary page.
                    if(cart.getCartItems().get(i).item.getAddonList().get(m).checkBox.isSelected())
                    {
                        cart.getCartItems().get(i).setAddonLabelInfo();
                        cartGrid.add(cart.getCartItems().get(i).addonInfo, 0, j+skipValue+1);
                        skipValue++;
                    }
                }
                j=2+skipValue;
            }
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
        cartScene = new Scene(cartBorderPane,1100,680);
        theStage.setScene(cartScene);
        theStage.setTitle("Shopping Cart - Agile's Cafe");
    }
    
    public void backButtonHandler() {
        for(int i=0;i<cartList.size();i++)
        {
            //When user goes back to main menu, reset the spin box to 0.
            //Also reset checked addons
            cartList.get(i).spinBox= new Spinner(0,10,0);
            cartList.get(i).spinBox.setMaxWidth(65);
            cartList.get(i).spinBox.setId(cartList.get(i).id.getText());
            cartList.get(i).setAddonListUnchecked();
        }
        //Rebuilds the main menu
        start(theStage);
    }
    
    //Calculates totals and sets appropriate labels
    public void calcCartTotals(){
        double totalC;
        double subtotalC=0;
        for(int i=0;i<cart.getCartItems().size();i++){
            subtotalC+= cart.getCartItems().get(i).item.getPrice()* cart.getCartItems().get(i).quantityOrdered;
        }
        for(int i=0;i<cart.getCartItems().size();i++)
        {
            for(int m=0;m<cart.getCartItems().get(i).item.getAddonList().size();m++)
            {
                if(cart.getCartItems().get(i).item.getAddonList().get(m).checkBox.isSelected())
                {
                    for(int j=0;j<cart.getCartItems().get(i).item.getAddonList().size();j++)
                    {
                        subtotalC+=cart.getCartItems().get(i).item.getAddonList().get(j).getPrice()* cart.getCartItems().get(i).quantityOrdered;
                    }
                }

            }

        }
        
        totalC=subtotalC+(subtotalC*cart.getTaxRate());
        
        cart.setSubTotal(subtotalC);
        cart.setTotal(totalC);
        //Sets the text for the subtotal, tax rate, and total labels,
        //and also sets the precision of the values.
        Double toBeTruncated = new Double(totalC);
        totalC = new BigDecimal(toBeTruncated).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
        toBeTruncated = new Double(subtotalC);
        subtotalC= new BigDecimal(toBeTruncated).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
        subtotalLabel.setText("Subtotal:   "+subtotalC);
        totalLabel.setText("Total: "+totalC);
        taxRateLabel.setText("Tax rate:    "+Double.toString(cart.getTaxRate()*100)+"%");
        
    }
    
    //Event handler for when user clicks "Remove" in the cart scene.
    //NOTE: This function doesn't refresh the page, only updates the internal values.
    private void removeFromCart(ActionEvent e){
        for(int i=0; i<cart.getCartItems().size();i++)
        {
            //If "Remove" button matches the item the user wants to remove, then remove the item from cart.
            if(((Control)e.getSource()).getId().compareTo(Integer.toString(cart.getCartItems().get(i).item.getItemID()))==0)
                    {
                        cart.getCartItems().get(i).quantityOrdered=0;
                        cart.getCartItems().get(i).isInCart=false;
                        cart.getCartItems().get(i).setAddonListUnchecked();
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

    //Checks if the user enters the correct user/pw.
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
   
    public static void main(String[] args) { launch(args); }
}