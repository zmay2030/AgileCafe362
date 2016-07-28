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
import javafx.scene.control.Control;
import javafx.geometry.HPos; 
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.scene.control.ComboBox;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import java.util.List;
import javafx.stage.FileChooser;
import java.io.File;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.awt.Desktop; 
import java.io.IOException; 
import java.util.logging.Level;
import java.util.logging.Logger;
 import javafx.embed.swing.SwingFXUtils;  
import javax.imageio.ImageIO; 
import java.awt.image.BufferedImage; 
import javafx.scene.control.ScrollPane;  
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType; 

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
    private Boolean isCartSceneBuilt = false;
    private final Cart cart = new Cart();
    
    //Used for billing scene
    private Stage tyStage;

    private final String IMAGES_PATH = "images/";
    private final Desktop desktop = Desktop.getDesktop();
    private File imageFileNameToUpload = null;
    
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
        HBox adminHBox = new HBox();
        adminHBox.setPadding(new Insets(0,10,10,0));
        adminHBox.getChildren().add(AccessAdminButton);
        adminHBox.setAlignment(Pos.BOTTOM_RIGHT);
        mainPane.setBottom(adminHBox);

        //Creates two sections of menu
        foodMenuGrid = new GridPane();
        bevMenuGrid = new GridPane();
        foodMenuGrid.setHgap(10);
        foodMenuGrid.setVgap(10);
        bevMenuGrid.setVgap(10);
        bevMenuGrid.setHgap(10);
        foodMenuGrid.getColumnConstraints().add(new ColumnConstraints(550));
        bevMenuGrid.getColumnConstraints().add(new ColumnConstraints(550));
        
        //Creates section labels
        Label food = new Label("Food");
        Label bev = new Label("Beverage");
        food.setFont(Font.font("Arial", FontWeight.BOLD, 30));
        bev.setFont(Font.font("Arial",FontWeight.BOLD,30));
        
        //Creates center section where the menu items are displayed
        VBox menuSection = new VBox();
        menuSection.setSpacing(20);
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
        setComboBoxHandler();
        
        menuSection.getChildren().add(foodMenuGrid);
        menuSection.getChildren().add(bevMenuGrid); 
        menuSection.setPadding(new Insets(20,5,20,50));
        ScrollPane menuScrollPane = new ScrollPane();
        menuScrollPane.setContent(menuSection);
        mainPane.setCenter(menuScrollPane);
    }
    
    //Populates the items on the main menu
    private void addItemGUI(){
        ColumnConstraints colimg = new ColumnConstraints(60);
        ColumnConstraints col1 = new ColumnConstraints(500);
        //ColumnConstraints col2 = new ColumnConstraints(200);
        foodMenuGrid.getColumnConstraints().set(0, colimg);
        foodMenuGrid.getColumnConstraints().add(1, col1);

        //Type 0 = Food
        //Type 1 = Beverage
        int j=1; //Refers to initial row to start populating data.
        //Adds the items into the "Food" section of the menu.
        for (int i=0;i<itemsList.size();i++)
        {
            //If food, add to food section
            if(itemsList.get(i).getType()==0 && !itemsList.get(i).isDeleted())
            {
                foodMenuGrid.add(itemsList.get(i).nameLbl,1,j);
                foodMenuGrid.add(itemsList.get(i).descLbl, 1, j+1);
                foodMenuGrid.add(itemsList.get(i).priceLbl, 2, j+1);
                foodMenuGrid.add(itemsList.get(i).spinBox, 3, j+1);
                foodMenuGrid.add(itemsList.get(i).getImageView(), 0, j);
                //Display Addon for food item
                int skipVar=0;
                for(int m=0; m<itemsList.get(i).getAddonList().size();m++){
                    foodMenuGrid.add(itemsList.get(i).getAddonList().get(m).checkBox,1,m+2+j);
                    skipVar++;
                }
                j+=skipVar;
                j=j+2;
            }
        }
        j=1; //Refers to initial row to start populating data.
        //Adds the items into the "Beverage" section of the menu.
        bevMenuGrid.getColumnConstraints().set(0, colimg);
        bevMenuGrid.getColumnConstraints().add(1, col1);
        for(int i=0; i<itemsList.size();i++)
        {
            //If beverage, add to beverage section
            if(itemsList.get(i).getType()==1 && !itemsList.get(i).isDeleted())
            {
                bevMenuGrid.add(itemsList.get(i).nameLbl,1,j);
                bevMenuGrid.add(itemsList.get(i).descLbl, 1, j+1);
                bevMenuGrid.add(itemsList.get(i).priceLbl, 2, j+1);
                bevMenuGrid.add(itemsList.get(i).spinBox, 3, j+1);
                bevMenuGrid.add(itemsList.get(i).getImageView(), 0, j);
                j=j+2;
            }
        }
        
    }
    
    private void addToCartHandler(){
            loadQuantityToCart();
            //If stage hasn't been built, then load quantity to cart, and build cart stage as usual.
            if(isCartSceneBuilt==false || cart.getCartItems().isEmpty()){
                buildCartScene();
                isCartSceneBuilt=true;
            }
            else if(isCartSceneBuilt==true)
            {
                buildCartScene();
            }
    }
    
    private void setComboBoxHandler(){
        for(int i=0;i<itemsList.size();i++)
        {
           itemsList.get(i).cb.setOnAction(e->comboBoxHandler(e));
        }
    }
    //In cart stage, this handles the event when someone changes quantity using combo box, and 
    //automatically updates the summary amount labels
    public void comboBoxHandler(ActionEvent e)
    {
        for(int i=0;i<cart.getCartItems().size();i++)
        {
            if(((Control)e.getSource()).getId().compareTo(cart.getCartItems().get(i).idLbl.getText())==0)
            {
                cart.getCartItems().get(i).quantityOrderedInCart=cart.getCartItems().get(i).cb.getValue();
                calcCartTotals();
            }
        }
        
    }
    
    //Extract the quantity ordered in the main menu and add to cart
    private void loadQuantityToCart(){
        //If cart stage hasn't been built or if cart is empty, add the items to the cart.
        if(isCartSceneBuilt==false || cart.getCartItems().isEmpty())
        {
            for(int i=0;i<itemsList.size();i++){
            if(itemsList.get(i).spinBox.getValue()>0)
            {
                itemsList.get(i).quantityOrderedInCart = itemsList.get(i).spinBox.getValue();
                itemsList.get(i).isInCart = true;
                cart.getCartItems().add(itemsList.get(i));
            }
        }
        }
        
        //If cart stage has been built and if there are items in the cart, update the quantity value.
        else if(isCartSceneBuilt == true && cart.getCartItems().size()>0)
        {
            for(int i=0;i<itemsList.size();i++)
            {
                //If item is already in cart, just add the quantity to existing cart item.
                if(itemsList.get(i).isInCart){
                    itemsList.get(i).quantityOrderedInCart += itemsList.get(i).spinBox.getValue();
                }
                //If item is not already in the cart, then add the item to the cart with its quantity selected.
                else
                {
                    itemsList.get(i).quantityOrderedInCart += itemsList.get(i).spinBox.getValue();
                    itemsList.get(i).isInCart = true;
                    cart.getCartItems().add(itemsList.get(i));
                }
            }
        }
    }
    
    private void buildCartScene(){
        //Create layouts to organize elements
        cartBorderPane = new BorderPane();
        cartGrid = new GridPane();
        cartGrid.setPadding(new Insets(15,20,15,20));
        cartGrid.setHgap(50);
        cartGrid.setAlignment(Pos.TOP_LEFT);
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
        //cartGrid.setGridLinesVisible(true);
        
        //Diplays the items from cart into summary page.
        displaySummaryItems();
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
        checkoutButton.setOnAction(e->checkoutButtonHandler()); 
        
        //Display the scene
        cartScene = new Scene(cartBorderPane,1100,680);
        theStage.setScene(cartScene);
        theStage.setTitle("Shopping Cart - Agile's Cafe");
    }
    
    private void checkoutButtonHandler(){
        if(cart.getCartItems().isEmpty()){
            errorEmptyCartStage();
        }
        else
        {
            billingScene();
        }
    }
    
    //Diplays the items from cart into summary page.
    private void displaySummaryItems(){
        //Sets the column sizes for the grid
        ColumnConstraints col1 = new ColumnConstraints(170);
        ColumnConstraints col2 = new ColumnConstraints(450);
        col1.setHgrow(Priority.ALWAYS);
        col2.setHgrow(Priority.ALWAYS);
        cartGrid.getColumnConstraints().addAll(col1,col2);
        
        int j=1; //j is used to place the items in the correct row as items are added to the GUI.
        for(int i=0;i<cart.getCartItems().size();i++)
        {
            //Double check to make sure conditions are true before displaying into cart.
            //Item in the cart must actually belong in the cart, and the quantity ordered must be greater than 0.
            //If check is correct, then add the item to the display.
            if(cart.getCartItems().get(i).isInCart==true && cart.getCartItems().get(i).quantityOrderedInCart>0){
                //Since if the cart scene has already been built, remove existing children if it exists and then add updated one.
                if(isCartSceneBuilt==true){
                cartGrid.getChildren().remove(cart.getCartItems().get(i).nameLbl);
                cartGrid.getChildren().remove(cart.getCartItems().get(i).descLbl);
                cartGrid.getChildren().remove(cart.getCartItems().get(i).priceLbl);
                cartGrid.getChildren().remove(cart.getCartItems().get(i).cb);
                cartGrid.getChildren().remove(cart.getCartItems().get(i).removeButton);
                cartGrid.getChildren().remove(cart.getCartItems().get(i).addonInfo);
                }
                cartGrid.add(cart.getCartItems().get(i).nameLbl, 0, j+1);
                cartGrid.add(cart.getCartItems().get(i).descLbl, 1, j+1);
                cartGrid.add(cart.getCartItems().get(i).priceLbl,2,j+1);
                cart.getCartItems().get(i).cb.setValue(cart.getCartItems().get(i).quantityOrderedInCart);
                cartGrid.add(cart.getCartItems().get(i).cb, 3, j+1);
                cartGrid.add(cart.getCartItems().get(i).removeButton, 4, j+1);
                
                //When user presses "Remove", then remove item from cart and refresh the page.
                cart.getCartItems().get(i).removeButton.setOnAction(e->{
                removeFromCart(e);
                buildCartScene();});
                
                //Create the addon for the item if checked in the main menu
                int skipValue=0;
                for(int m=0;m<cart.getCartItems().get(i).getAddonList().size();m++){
                    //If addon is checked, add it to the summary page.
                    if(cart.getCartItems().get(i).getAddonList().get(m).checkBox.isSelected())
                    {
                        cart.getCartItems().get(i).setAddonLabelInfo();
                        cartGrid.add(cart.getCartItems().get(i).addonInfo, 0, j+skipValue+2);
                        skipValue++;
                    }
                }
                j+=2+skipValue;
            }
        }

    }
    
    private void backButtonHandler() {
        for(int i=0;i<itemsList.size();i++)
        {
            //When user goes back to main menu, reset the spin box to 0.
            //Also reset checked addons
            itemsList.get(i).spinBox= new Spinner(0,10,0);
            itemsList.get(i).spinBox.setMaxWidth(65);
            itemsList.get(i).spinBox.setId(itemsList.get(i).idLbl.getText());
            itemsList.get(i).setAddonListUnchecked();
        }
        //Rebuilds the main menu
        start(theStage);
    }
    
    //Calculates totals and sets appropriate labels
    private void calcCartTotals(){
        double totalC;
        double subtotalC=0;
        for(int i=0;i<cart.getCartItems().size();i++){
            subtotalC+= cart.getCartItems().get(i).getPrice()* cart.getCartItems().get(i).quantityOrderedInCart;
        }
        for(int i=0;i<cart.getCartItems().size();i++)
        {
            for(int m=0;m<cart.getCartItems().get(i).getAddonList().size();m++)
            {
                if(cart.getCartItems().get(i).getAddonList().get(m).checkBox.isSelected())
                {
                    for(int j=0;j<cart.getCartItems().get(i).getAddonList().size();j++)
                    {
                        subtotalC+=cart.getCartItems().get(i).getAddonList().get(j).getPrice()* cart.getCartItems().get(i).quantityOrderedInCart;
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
            if(((Control)e.getSource()).getId().compareTo(Integer.toString(cart.getCartItems().get(i).getItemID()))==0)
                    {
                        cart.getCartItems().get(i).quantityOrderedInCart=0;
                        cart.getCartItems().get(i).isInCart=false;
                        cart.getCartItems().get(i).setAddonListUnchecked();
                        cart.getCartItems().remove(i);
                        break;
                    }
        }
    }
    
    //When user clicks "Proceed to Checkout", user is sent to this page for payment
    private void billingScene(){
        //ccBorderPane is used to display the Credit Card Page
        //cashBorderPane is used to display the Pay in Cash Page
        BorderPane ccBorderPane = new BorderPane();
        BorderPane cashBorderPane = new BorderPane();
        //Sets up the top section of the billing page
        VBox topVBox = new VBox();
        topVBox.setSpacing(20);
        topVBox.setPadding(new Insets(30,30,30,40));
        HBox radioHBox = new HBox();
        radioHBox.setSpacing(30);
        Label titleLabel = new Label("Payment Information");
        titleLabel.setPadding(new Insets(0,0,0,380));
        titleLabel.setFont(Font.font("Arial",FontWeight.EXTRA_BOLD,20));
        Double toBeTruncated = cart.getTotal();
        double total = new BigDecimal(toBeTruncated).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
        Label totalLbl = new Label("Total to be charged: $"+total);
        totalLbl.setFont(Font.font("Arial",FontWeight.BOLD,15));
        ccBorderPane.setTop(topVBox);
        
        //Creates the radio buttons
        ToggleGroup group = new ToggleGroup();
        RadioButton ccRadioBtn = new RadioButton("Credit Card");
        RadioButton payCashRadioBtn = new RadioButton("Pay in Cash");
        ccRadioBtn.setToggleGroup(group);
        payCashRadioBtn.setToggleGroup(group);
        ccRadioBtn.setSelected(true);
        radioHBox.getChildren().addAll(ccRadioBtn,payCashRadioBtn);
        
        //Creates the back button
        HBox buttonHBox = new HBox();
        buttonHBox.setSpacing(30);
        Button backButton = new Button("Back to Summary");
        buttonHBox.getChildren().addAll(radioHBox,backButton);
        backButton.setOnAction(e->{
            theStage.setTitle("Shopping Cart - Agile's Cafe");
            theStage.setScene(cartScene);});
        
        //Adds labels and buttons to the display
        topVBox.getChildren().addAll(titleLabel,totalLbl, buttonHBox);
        
        Scene billingScene = new Scene(ccBorderPane,1000,600);
        Scene payNowScene = new Scene(cashBorderPane,1000,600);
        
        //Create credit card form
        GridPane ccGrid = new GridPane();
        ccBorderPane.setCenter(ccGrid);
        ccGrid.setPadding(new Insets(0,20,20,30));
        Label nameCC = new Label("Name");
        Label cvcLbl = new Label("CVC");
        Label ccNumLbl = new Label("Card Number");
        TextField nameTF = new TextField();
        TextField ccNumTF = new TextField();
        TextField cvcTF = new TextField();
        ComboBox cardTypeBox = new ComboBox();
        cardTypeBox.setPromptText("Card Type");
        cardTypeBox.getItems().addAll("Card Type", "Visa", "MasterCard","Amex");
        nameTF.setPromptText("Name (As it appears on the card)");
        ccNumTF.setPromptText("Enter Card Number");
        cvcTF.setPromptText("3-4 Digit Code");
        cvcTF.maxWidth(50);
        ccGrid.getColumnConstraints().add(new ColumnConstraints(500));
        ccGrid.setVgap(10);
        ccGrid.add(nameCC, 0, 0);
        ccGrid.add(nameTF,0,1);
        ccGrid.add(ccNumLbl,0,2);
        ccGrid.add(ccNumTF, 0,3);
        ccGrid.add(cardTypeBox, 0, 4);
        ccGrid.add(cvcLbl, 0, 5);
        ccGrid.add(cvcTF, 0, 6);
        Button ccPayNowBtn = new Button("Submit Payment");
        ccPayNowBtn.setAlignment(Pos.CENTER);
        ccGrid.add(ccPayNowBtn, 0, 7);
        ccPayNowBtn.setOnAction(e->payNowButtonHandler());
        
        //-------------------Sets up "Pay Now" Scene-----------------------
        VBox pnVBox = new VBox();
        pnVBox.setAlignment(Pos.CENTER);
        cashBorderPane.setCenter(pnVBox);
        Text confirmText = new Text("Please see cashier to pay in cash. \n"+" Press submit to submit the order.\n");
        Button confirmPayNowBtn = new Button("Submit Order");
        confirmPayNowBtn.setOnAction(e->payNowButtonHandler());
        pnVBox.getChildren().addAll(confirmText,confirmPayNowBtn);
        //-------------------------------------------------------------------
        
        //Changes scene when user clicks on radio button.
        ccRadioBtn.setOnAction(e->
        {
            cashBorderPane.getChildren().remove(topVBox);
            ccBorderPane.setTop(new VBox(topVBox));
            theStage.setScene(billingScene);});
        
        payCashRadioBtn.setOnAction(e->
        {
            ccBorderPane.getChildren().remove(topVBox);
            cashBorderPane.setTop(new VBox(topVBox));
            theStage.setScene(payNowScene);});
        
        theStage.setTitle("Payment Information");
        theStage.setScene(billingScene);
    }
    
    //When user submits payment, the "Thank You" page is shown
    private void thankYouStage(){
        VBox containVBox = new VBox();
        BorderPane tyBorderPane = new BorderPane();
        containVBox.setPadding(new Insets(50,50,50,50));
        containVBox.setAlignment(Pos.CENTER);
        Text tyText = new Text("Thank you for your payment! \n "+"\n        Order# 000000000 \n");
        Button closeBtn = new Button("Close");
        containVBox.getChildren().addAll(tyText,closeBtn);
        tyBorderPane.setCenter(containVBox);
        closeBtn.setOnAction(e->tyStage.close());
        Scene tyScene = new Scene(tyBorderPane,350,300);
        tyStage = new Stage();
        tyStage.setTitle("Thank you for your paymnet!  =)");
        tyStage.initModality(Modality.APPLICATION_MODAL);
        tyStage.setScene(tyScene);
        tyStage.showAndWait();
    }
    
    //When user submits payment, the "Thank You" page is shown AND the cart is cleared,
    //the application goes back to the main menu, ready to take more orders.
    private void payNowButtonHandler()
    {
        //NOTE: Store sale info into database here!
        SQL_DB mysqlDB = new SQL_DB();
        try{
            mysqlDB.connect(); 
        }catch(Exception e)
        {
            System.out.print("Error connecting");
        }
        for(int i =0;i<cart.getCartItems().size();i++){
            itemsList.get(i).addToQuantityOrdered(itemsList.get(i).quantityOrderedInCart);
        }
        mysqlDB.addSaleOrder(cart.getTotal());
        thankYouStage();
        cart.clear();
        start(theStage);
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
        
        // Create stage for login
        // Needs to be passed to errorLogin()
        logInStage = new Stage();
        //Set handling event if login error
        loginButton.setOnAction(e->errorLogin(logInStage));
        
        //Display Admin window
        logInScene = new Scene(grid,500,500); 
        logInStage.setTitle("Admin Login Page");
        logInStage.setScene(logInScene);
        logInStage.initModality(Modality.APPLICATION_MODAL);
        logInStage.showAndWait();
    }
    
    //Displays error window if cart is empty when checkbutton is pressed.
    private void errorEmptyCartStage(){
        Stage errorEmptyCartStage = new Stage();
        BorderPane errorBorderPane = new BorderPane();
        VBox errorVBox = new VBox();
        errorBorderPane.setCenter(errorVBox);
        errorVBox.setAlignment(Pos.CENTER);
        
        Text errText = new Text("Error: Cart is empty. Cannot checkout.\n");
        errText.setFont(Font.font("Arial",FontWeight.BOLD,15));
        Button closeBtn = new Button("Close");
        closeBtn.setOnAction(e->errorEmptyCartStage.close());
        errorVBox.getChildren().addAll(errText,closeBtn);
        Scene errScene= new Scene(errorBorderPane, 400,300);
        
        errorEmptyCartStage.setScene(errScene);
        errorEmptyCartStage.setTitle("ERROR");
        errorEmptyCartStage.initModality(Modality.APPLICATION_MODAL);
        errorEmptyCartStage.showAndWait();
    }

    private void errorLogin(Stage logInStage){

        if (userTextField.getText().equals("123") 
                 && pwBox.getText().equals("123"))
        { 
            logInStage.close();
            showAdminMenu();
        }
        else
        {
            //User entered incorrect credentials
            //Display error message
            errorLogin.setFill(Color.FIREBRICK);
            errorLogin.setText("Error: Wrong User/PW\nHINT: 123");
        }
    }

    public void editItemByIndex(int index)
    {
        Stage editItem = new Stage();
        editItem.initModality(Modality.APPLICATION_MODAL);
        final FileChooser fileChooser = new FileChooser();
        
        // Get item passed
        Item item = itemsList.get(index);
        
        ImageView viewImage = item.getImageView();
        
        // Labels
        Label itemName = new Label("Name: ");
        Label itemPrice = new Label("Price: ");
        Label itemDesc = new Label("Description: "); 
        Label itemType = new Label("Type: ");
        ComboBox typeComboBox = new ComboBox();
        typeComboBox.getItems().add(0, "Food");
        typeComboBox.getItems().add(1, "Beverage"); 
        typeComboBox.setValue(item.typeTranslate(item.getType()));
        
        // Textfields
        TextField nameTF = new TextField (item.getName());
        TextField priceTF = new TextField (String.valueOf(item.getPrice()));
        TextField descTF  = new TextField (item.getDescription());
        TextField typeTF    = new TextField(item.typeTranslate(item.getType()));
        
        // upload button and submit button
        Button uploadBtn = new Button("Browse Files");
        Button submitBtn = new Button("Save"); 
        submitBtn.setPadding(new Insets(5));
        
        // Manage addons button
        Button manageAddonsBtn = new Button("Manage AddOns");
        manageAddonsBtn.setOnAction(e->manageAddons(index));
        GridPane grid = new GridPane(); 
        
        // Image
        grid.add(viewImage,0,0);
        grid.add(uploadBtn,1,0);
        
        // Item name
        grid.add(itemName,0,2);
        grid.add(nameTF,1,2);
        
        // Item price
        grid.add(itemPrice,0,3);
        grid.add(priceTF,1,3);
        
        // Item description
        grid.add(itemDesc,0,4);
        grid.add(descTF,1,4);
        
        // Item description
        grid.add(itemType,0,5);
        grid.add(typeComboBox,1,5);
        
        // save button
        grid.add(submitBtn,1,6);
        grid.add(manageAddonsBtn,1,7);
        GridPane.setMargin(manageAddonsBtn, new Insets(15,0,0,0));
        grid.setAlignment(Pos.CENTER);
         
        // ON UPLOAD
        uploadBtn.setOnAction(
            new EventHandler<ActionEvent>() {
                @Override
                public void handle(final ActionEvent e) {
                    List<File> list =
                        fileChooser.showOpenMultipleDialog(editItem);
                    if (list != null) {
                        for (File file : list) { 
                            grid.getChildren().remove(uploadBtn);
                            Label fileName = new Label(String.valueOf(file));
                            grid.add(fileName,1,1);
                           
                            imageFileNameToUpload = file;
                        }
                    }
                }
            });
        submitBtn.setOnAction(e->{
                if (imageFileNameToUpload !=null)
                {
                    Image newImg = new Image("file:///"+imageFileNameToUpload.toString());
                    File absp = new File("."); 
                    String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime()); 
                    String fullFileName = timestamp+imageFileNameToUpload.getName();
                    File outputFile = new File("src/images/"+fullFileName);
                    BufferedImage bImage = SwingFXUtils.fromFXImage(newImg, null);
                    try {
                      ImageIO.write(bImage, "png", outputFile);
                    } catch (IOException a) {
                      throw new RuntimeException(a);

                    }
                    item.setImage(fullFileName);
                    imageFileNameToUpload = null;
                }
                // NEED TO ERROR CHECK
                item.setPrice(Double.parseDouble(priceTF.getText()));
                item.setDescription(descTF.getText());
                item.setName(nameTF.getText());
                item.setCategory(typeComboBox.getSelectionModel().getSelectedIndex());
                System.out.print("NEW PRICE: " +Double.parseDouble(priceTF.getText()));
                
                // Save items after edit
                editItem.close();
                adminEditMenuItems();
                start(theStage);
                // Load cart items again
                renameLabels(item);
                //////////////////////////////////////
                
            }
        ); 
        Scene scene = new Scene(grid,400,300);
        editItem.setScene(scene);
        editItem.show();

    }
    
    public void renameLabels(Item item){
        for(int i=0; i<itemsList.size();i++){
            if(item.getItemID()==itemsList.get(i).getItemID()){
                itemsList.get(i).nameLbl.setText(item.getName());
                itemsList.get(i).priceLbl.setText(Double.toString(item.getPrice()));
                itemsList.get(i).descLbl.setText(item.getDescription());
            }
        }
    }
    
    public void adminEditMenuItems()
    {
        Stage editMenuItems = new Stage();
        editMenuItems.initModality(Modality.APPLICATION_MODAL);
        GridPane itemBox = new GridPane();
        itemBox.setId("items");
        // Go through each item and put into grid
        
        // Add item button
        Button addBtn = new Button("Add Item");
        addBtn.setId("Button_addBtn");
        VBox addBtnBox = new VBox(addBtn);
        addBtnBox.setAlignment(Pos.TOP_RIGHT);
        addBtnBox.setId("addItemBtn");
        addBtnBox.getChildren().add(itemBox);
        // Add item action 
        addBtn.setOnAction(e->addNewItem());
        
        int index = 0;
        int currentCol = index;
        File file = new File("."); 
        for(Item item: itemsList)
        {
            if (item.isDeleted())
            {
                index++;
                continue; // skip
            }
            // item image
            ImageView imageOutput = item.getImageView(); 
            GridPane.setMargin(imageOutput, new Insets(3));
            
            // item name
            Label title = new Label(item.getName());
            title.setStyle("-fx-font-weight:bold;");
            title.setPadding(new Insets(3));
            
            // Price label
            Label Price = new Label("$"+String.valueOf(item.getPrice()));
            Price.setPadding(new Insets(3));
            
            // Description label
            Label desc = new Label(item.getDescription());
            desc.setPadding(new Insets(3));
            
            // Type label 
            Label type = new Label(item.typeTranslate(item.getType()));
            type.setPadding(new Insets(3));
            
            // Edit button and delete button 
            Button editBtn = new Button("Edit");  
            Button deleteBtn = new Button("Delete"); 
            
            // Edit button
            int passIndex = index;
            editBtn.setOnAction(e->{editMenuItems.close();editItemByIndex(passIndex);});
            editBtn.setAlignment(Pos.CENTER);
            editBtn.setId("editItemBtn");
            
            // Delete button
            deleteBtn.setId("deleteItem");
            // On delete action
            int indexToDelete = index;
            deleteBtn.setOnAction(e->{confirmItemDelete(indexToDelete, editMenuItems);});
            // Box for edit button
            VBox editBtnBox = new VBox();
            editBtnBox.getChildren().add(editBtn);
            editBtnBox.setId("editItemBtnBox");
            // Box for delete button
            VBox deleteBtnBox = new VBox();
            deleteBtnBox.getChildren().add(deleteBtn);
            deleteBtnBox.setId("deleteItemBox");
            
            GridPane grid = new GridPane();
            grid.add(imageOutput,index,0);
            grid.add(title, index+1, 0);
            grid.add(Price, index+1, 1); 
            grid.add(type, index+1, 2);
            grid.add(desc, index+1, 3);
            grid.add(editBtnBox,index+1,4);
            grid.add(deleteBtnBox,index+1,5);
            // Get all addon list for the current item
            ArrayList<addOn> addonList = item.getAddonList();
            
            System.out.print("\n\n"); 
            
            int column;
            // Get current column 
            if (currentCol % 3 == 0)
            {
                column = 0;
            }
            else if(currentCol % 2  == 0)
                column = 1;
            else
                column = 2;
            
            itemBox.add(grid, column, currentCol/3); 
            itemBox.setPrefWidth(166);
            itemBox.setAlignment(Pos.TOP_CENTER);
            grid.setPrefWidth(166);
            grid.setPadding(new Insets(0,5,0,0));
            grid.setAlignment(Pos.CENTER);
            grid.setId("itemBox");
            index++;
            currentCol++;
        }   
          
        Scene scene = new Scene(addBtnBox,500,500);
        scene.getStylesheets().add("css/adminMenu.css");
        editMenuItems.setScene(scene);
        editMenuItems.show();
    }
    public void addNewItem()
    {
        Stage newItemStage = new Stage();
        GridPane grid = new GridPane();
        newItemStage.initModality(Modality.APPLICATION_MODAL);
        
         // Labels
        Label itemName = new Label("Name: ");
        Label itemPrice = new Label("Price: ");
        Label itemDesc = new Label("Description: "); 
        Label itemType = new Label("Type: ");
        Label itemImg  = new Label("Upload Image: "); 
        ComboBox typeComboBox = new ComboBox();
        typeComboBox.getItems().add(0, "Food");
        typeComboBox.getItems().add(1, "Beverage");  
        typeComboBox.setValue("Food");
        // Textfields
        TextField nameTF = new TextField ();
        TextField priceTF = new TextField ();
        TextField descTF  = new TextField ();
        TextField typeTF    = new TextField();
       
        // upload button and submit button
        Button uploadBtn = new Button("Browse Files");
        Button submitBtn = new Button("Save"); 
        submitBtn.setPadding(new Insets(5)); 
        
        grid.setPadding(new Insets(10,10,10,80));
        // Image
        grid.add(itemImg,0,0);
        grid.add(uploadBtn,1,0);
        
        // Item name
        grid.add(itemName,0,1);
        grid.add(nameTF,1,1);
        
        // Item price
        grid.add(itemPrice,0,2);
        grid.add(priceTF,1,2);
        
        // Item description
        grid.add(itemDesc,0,3);
        grid.add(descTF,1,3);
        
        // Item description
        grid.add(itemType,0,4);
        grid.add(typeComboBox,1,4); 
        // add submit button
        grid.add(submitBtn,1,5);
        grid.setHgap(20);
        
        // on upload
         // ON UPLOAD 
        final FileChooser fileChooser = new FileChooser();
        uploadBtn.setOnAction(
            new EventHandler<ActionEvent>() {
                @Override
                public void handle(final ActionEvent e) {
                    List<File> list =
                        fileChooser.showOpenMultipleDialog(newItemStage);
                    if (list != null) {
                        for (File file : list) { 
                            grid.getChildren().remove(uploadBtn);
                            Label fileName = new Label(String.valueOf(file));
                            grid.add(fileName,1,0);
                           
                            imageFileNameToUpload = file;
                        }
                    }
                }
            });
        // on saving  
         
        submitBtn.setOnAction(e->{ 
                String name = nameTF.getText();
                String desc = descTF.getText();
                int    type = typeComboBox.getSelectionModel().getSelectedIndex();
                double price = -1;
                try{
                    price = Double.parseDouble(priceTF.getText());
                }
                catch(NumberFormatException n)
                {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Price not valid");
                    alert.setHeaderText("Please note that price be an integer or decimal number"); 

                    alert.showAndWait();
                }
                if (imageFileNameToUpload !=null && name!="" && desc!="")
                {
                    Image newImg = new Image("file:///"+imageFileNameToUpload.toString());
                    File absp = new File("."); 
                    String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime()); 
                    String fullFileName = timestamp+imageFileNameToUpload.getName();
                    File outputFile = new File("src/images/"+fullFileName);
                    BufferedImage bImage = SwingFXUtils.fromFXImage(newImg, null);
                    try {
                      ImageIO.write(bImage, "png", outputFile);
                    } catch (IOException a) {
                      throw new RuntimeException(a);

                    }
                     
                    String image_path = fullFileName;
                    
                    SQL_DB mysqlDB = new SQL_DB();
                    try{
                        mysqlDB.connect();
                        // Check if fields are not empty 
                        
                        int itemId = mysqlDB.addItem(name,desc,type,price,image_path);
                        if (itemId > 0) // valid
                        {

                            Item item = new Item(itemId,name,desc,type,price,image_path,0);
                            // Now add to list
                            itemsList.add(item);
                            start(theStage); 
                            // Cart stuff here
                        }
                        else
                        {
                            System.out.print("ERROR ADDING ITEM");
                        } 
                    }catch(Exception a)  
                    {
                        System.out.print("Error connecting");
                    } 
                    imageFileNameToUpload = null;
                    
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Dialog");
                    alert.setHeaderText("Look, an Information Dialog");
                    alert.setContentText("I have a great message for you!");

                    alert.showAndWait();
                    alert.setTitle("Item added");
                    alert.setHeaderText("Item "+name+" has been added");

                    alert.showAndWait();
                }
                else // error
                {
                    if(price >=0)
                    {
                        showErrorMessage("Incomplete form","Please make sure the form is completely filled out before proceeding");
                    }
                }
                
                
            }
        ); 
        Scene scene = new Scene(grid,400,300);
        newItemStage.setScene(scene);
        
        newItemStage.show();
        
        
        
    }
    public void confirmItemDelete(int index, Stage editMenuItems)
    {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Deleting an Item");
        alert.setHeaderText("Are you sure you want to delete this item?"); 
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            Item item = itemsList.get(index); 
            item.spinBox = new Spinner(0,10,0);
            item.SetDelete(); 
            // another alert to notify deletion
            alert = new Alert(AlertType.INFORMATION); 
            alert.setTitle("Item deleted");
            alert.setHeaderText("Item "+item.getName()+" has been deleted!"); 
            alert.showAndWait();
             
            editMenuItems.hide();
            adminEditMenuItems();
        }
        start(theStage);
    }
    public boolean confirmAddonDelete()
    {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Deleting an Addon");
        alert.setHeaderText("Are you sure you want to delete this addon?"); 
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            return true;
        }
        else
        {
            return false;
        }
    }
    public void showAdminMenu()
    {  
        Stage adminMenu = new Stage();
        adminMenu.initModality(Modality.APPLICATION_MODAL);
        GridPane layoutPane = new GridPane();
        
        Label title = new Label("Admin Menu"); 
        title.setId("editMenuTitle");
        title.setPadding(new Insets(0,0,50,0));
        // Edit Menu Button
        Button editMenuBtn = new Button("Manage Menu Items");  
        //editMenuBtn.setOnAction(adminEditMenuItems(adminMenu));
        editMenuBtn.setOnAction(e->{adminMenu.close();adminEditMenuItems();});
        editMenuBtn.setId("editMenuBtn");  
        editMenuBtn.setPrefSize(200,50);
        
        HBox hboxEdit = new HBox(10); 
        hboxEdit.getChildren().add(editMenuBtn);
        
        GridPane.setHalignment(hboxEdit, HPos.CENTER);
        GridPane.setMargin(hboxEdit, new Insets(0,0,5,0));
        // Action on edit menu button
         
        
        // View Reports Button
        Button viewReports = new Button("View Reports"); 
         
        
        HBox hboxreports = new HBox(10); 
        hboxreports.getChildren().add(viewReports);
        viewReports.setId("viewReportsBtn");
        viewReports.setPrefSize(200,50);
        
        
        layoutPane.setPadding(new Insets(10, 0, 0, 60));
        layoutPane.add(title, 1, 1);
        layoutPane.add(hboxEdit,1,2); 
        layoutPane.add(hboxreports,1,3);
         
        Scene menuScene = new Scene(layoutPane,310,250);
        menuScene.getStylesheets().add("css/adminMenu.css"); 
        adminMenu.setScene(menuScene);
        adminMenu.show(); 
        
    }
    public void manageAddons(int index)
    { 
        Item item = itemsList.get(index);
        ArrayList<addOn> addonList = item.getAddOnList();
        
        Stage addonsStage = new Stage();
        GridPane grid = new GridPane();
        Button addBtn = new Button("New addon");
        addBtn.setOnAction(e->{addonsStage.hide();addAddon(index);});
        grid.add(addBtn,3, 0);
         
        
        int addonIndex = 1;
        for(addOn addon : addonList)
        {
            Label name = new Label(addon.getName());
            Label price = new Label(" $"+String.valueOf(addon.getPrice()));
            Button editBtn = new Button("Edit");
            Button deleteBtn = new Button("Delete");
            grid.add(name, 0,addonIndex);
            grid.add(price, 1,addonIndex);
            grid.add(editBtn, 2,addonIndex);
            grid.add(deleteBtn, 3,addonIndex);
            GridPane.setMargin(editBtn, new Insets(0,0,0,10));
            GridPane.setMargin(deleteBtn, new Insets(0,0,0,10));
            
            // On editing button
            int passAddonIndex = addonIndex;
            int itemIndex = index;
            editBtn.setOnAction(e->{addonsStage.hide();editAddon(passAddonIndex,itemIndex);});
            deleteBtn.setOnAction(e->{
                boolean confirmDelete = confirmAddonDelete();
                if (confirmDelete)
                { 
                    addonList.remove(passAddonIndex-1); 
                    grid.getChildren().remove(name);  
                    grid.getChildren().remove(price);
                    grid.getChildren().remove(editBtn); 
                    grid.getChildren().remove(deleteBtn);
                    start(theStage);
                    
                    SQL_DB mysqlDB = new SQL_DB();
                    try{
                        mysqlDB.connect();
                        // Check if fields are not empty 
                        
                        mysqlDB.deleteAddon(addon.getAddOnID());
                    }catch(Exception a)  
                    {
                        System.out.print("Error connecting");
                    }  
                    // deleted addon 

                }
            });
            addonIndex++;
        }
        grid.setPadding(new Insets(10));
        Scene scene = new Scene(grid,300,300);
        addonsStage.setScene(scene);
        addonsStage.show();
    }
    public void editAddon(int addonIndex,int itemIndex)
    {
        Item item = itemsList.get(itemIndex);
        ArrayList<addOn> addonList = item.getAddOnList();
        addOn currentAddon = addonList.get(addonIndex);
        
        // Create stage
        Stage editAddonStage = new Stage();
        GridPane grid = new GridPane();

        // Labels
        Label name = new Label("Name: ");
        Label price = new Label("Price: ");
        TextField nameTF = new TextField(currentAddon.getName());
        TextField priceTF = new TextField(Double.toString(currentAddon.getPrice()));
        Button save = new Button("Save"); 
        grid.add(name, 0,0);
        grid.add(nameTF, 1,0);
        
        grid.add(price, 0,1);
        grid.add(priceTF, 1,1);
        
        grid.add(save,1,2);
        grid.setPadding(new Insets(10));
        
       
        save.setOnAction(e->{
                
                String getName = nameTF.getText();
                double getPrice = -1;
                try
                {
                    getPrice = Double.parseDouble(priceTF.getText());
                }
                catch(NumberFormatException n)
                {
                    showErrorMessage("Invalid Price","Price must be an integer or decimal");
                }
                if(getPrice != -1 && getName!="")
                {
                    currentAddon.setName(getName);
                    currentAddon.setPrice(getPrice);
                    manageAddons(itemIndex);
                    editAddonStage.hide();
                    start(theStage);
                    // saved addon
                }
                else
                {
                    showErrorMessage("Incomplete Form","Please make sure name and price are filled before saving");
                }
        });
        
        Scene scene = new Scene(grid,200,200);
        editAddonStage.setScene(scene);
        editAddonStage.show();
    }
    public void addAddon(int itemIndex)
    {
        Item item = itemsList.get(itemIndex);
        ArrayList<addOn> addonList = item.getAddOnList();
        
        // Create stage
        Stage addAddonStage = new Stage();
        GridPane grid = new GridPane();

        // Labels
        Label name = new Label("Name: ");
        Label price = new Label("Price: ");
        TextField nameTF = new TextField();
        TextField priceTF = new TextField();
        Button save = new Button("Add"); 
        grid.add(name, 0,0);
        grid.add(nameTF, 1,0);
        
        grid.add(price, 0,1);
        grid.add(priceTF, 1,1);
        
        grid.add(save,1,2);
        grid.setPadding(new Insets(10));
        
       
        save.setOnAction(e->{
                
                String getName = nameTF.getText();
                double getPrice = -1;
                try
                {
                    getPrice = Double.parseDouble(priceTF.getText());
                }
                catch(NumberFormatException n)
                {
                    showErrorMessage("Invalid Price","Price must be an integer or decimal");
                }
                if(getPrice != -1 && getName!="")
                { 
                    SQL_DB mysqlDB = new SQL_DB();
                    try{
                        mysqlDB.connect();
                        // Check if fields are not empty 
                        
                        int addonId = mysqlDB.addAddon(getName,getPrice,item.getItemID());
                        if (addonId > 0) // valid
                        {

                            addOn addon = new addOn(getName,getPrice); 
                            addon.setAddOnID(addonId); 
                            addonList.add(addon);
                            
                            manageAddons(itemIndex);
                            addAddonStage.hide();
                            start(theStage);
                            // saved addon
                        }
                        else
                        {
                            System.out.print("ERROR ADDING ITEM");
                        } 
                    }catch(Exception a)  
                    {
                        System.out.print("Error connecting");
                    } 
                    
                }
                else
                {
                    showErrorMessage("Incomplete Form","Please make sure name and price are filled before saving");
                }
        });
        
        Scene scene = new Scene(grid,200,200);
        addAddonStage.setScene(scene);
        addAddonStage.show();
    }
    public void showErrorMessage(String title, String desc)
    {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(desc); 

        alert.showAndWait();
    }
    private void openFile(File file) {
        try {
            desktop.open(file);
        } catch (IOException ex) {
            Logger.getLogger(
                AgileCafe362.class.getName()).log(
                    Level.SEVERE, null, ex
                );
        }
    }
    public static void main(String[] args) { launch(args); }   
}