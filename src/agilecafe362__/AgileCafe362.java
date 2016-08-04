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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType; 
    
/*************************
*  Module name: AgileCafe362        
*  Description: Responsible for building/displaying 
*  the GUI and for facilitating user interaction between the program.
*  Programmers: Ahmad, Kevin                     
*  Data structures: ArrayList                    
* Important functions: 
*   -init(), which loads information from the database into the program.
*   -start(), initiates the program.
*   -buildCartScene(), displays the main menu.
*   -displaySummaryItems(), displays only the items in the cart onto another page.
*   -ShowAdminMenu(), shows the admin menu where the admin can add/edit/delete items/addons.
**************************/
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
    private final Cart cart = Cart.getInstance();
    
    //Used for billing scene
    private Stage tyStage; 
    Label totalLbl = new Label();
    // List of items to edit/delete stage
    private final Stage editMenuItems = new Stage();
    
    //Used for edit settings scene
    TextField changeTR_TF; //Change Tax Rate text field
    Text statusLbl;

    private final String IMAGES_PATH = "images/";
    private final Desktop desktop = Desktop.getDesktop();
    private File imageFileNameToUpload = null;
    
    //Used for graph
    ArrayList<Sales> salesList;
    
    //Elements for edit menus item stage
    ComboBox typeComboBox;
    TextField nameTF;
    TextField descTF;
    TextField priceTF;
    TextField typeTF;
    Stage newItemStage;
    Stage editMenuItemsStage;
    
    
    /*************************
    *  Function name: Init        Description: Connects to the database, loads sale summary info,    
    *  Date: 8/1/16                          loads the items from the database, along with the
    *  Programmers: Ahmad                    item's list of addons. All info are loaded into a list.
    *  Data structures: ArrayList            This is the first thing that runs when the application starts.
    **************************/
    @Override
    public void init() throws Exception {
        //init() runs before application starts
        //Connect to database
        SQL_DB mysqlDB = SQL_DB.getInstance();
        mysqlDB.connect();
        
        //Query info from database into application
        this.itemsList = mysqlDB.getAllItems();
        
        salesList = mysqlDB.getAllSales();
        
        for(Sales sale : salesList)
        {         
            String saleDate = sale.getSaleDate();
            double total = sale.getTotal();
            System.out.print("OUTPUT: "+saleDate+" "+total+"\n");
        } 
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
    
    /*************************
    *  Function name: stop        Description: Connects to the database, saves sale summary info,    
    *  Date: 8/1/16                          saves the items into the database, along with the
    *  Programmers: Ahmad                    item's list of addons. All info are saved into a database.
    *  Data structures: ArrayList            This function is called when the user closes the application.
    **************************/
    @Override
    public void stop() throws Exception {
        //stop() runs after application stops

        //Close mysql connection  
        //Save info into database
        SQL_DB mysqlDB = SQL_DB.getInstance();
        mysqlDB.connect();
        mysqlDB.saveAllItems(itemsList);
        //Save info from application into database
    }
    
    /*************************
    *  Function name: start       Description: This is the entry point of the JavaFX application.   
    *  Date: 8/1/16                          This function is called when the user starts the application.
    *  Programmers: Kevin, Ahmad             This function builds the main menu, and sets up the primary stage.                    
    *  Data structures: NONE                 
    **************************/
    @Override
    public void start(Stage primaryStage) {
        theStage = primaryStage;
        buildMainMenuStage();
        
        primaryStage.setTitle("Agile's Cafe Menu");
        primaryStage.setScene(mainScene);
        mainScene.getStylesheets().add("css/adminMenu.css");
        primaryStage.show();
    }
    
    /*************************
    *  Function name: buildMainMenuStage       Description: This function is responsible for building the main menu and  
    *  Date: 8/1/16                                       calling a function to display the items from the itemsList and addonList.
    *  Programmers: Kevin, Ahmad                          The user interacts w/ the main menu on this window (stage).                   
    *  Data structures: Layout containers                
    **************************/
    private void buildMainMenuStage(){
        
        //Layout for Main Menu -> BorderPane layout
        mainPane = new BorderPane();
        mainPane.setId("mainPane");
        mainScene = new Scene(mainPane, 1000, 680, Color.BEIGE);
        
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
        foodMenuGrid.getStylesheets().add("css/adminMenu.css");
        bevMenuGrid.getStylesheets().add("css/adminMenu.css");
        foodMenuGrid.setId("foodGrid");
        bevMenuGrid.setId("bevGrid");
        
        //Creates section labels
        Label food = new Label("Food");
        Label bev = new Label("Beverage");
        food.setId("typeLabel");
        bev.setId("typeLabel");
        VBox foodBox = new VBox(food);
        VBox bevBox  = new VBox(bev);
        foodBox.setId("typeLabelBox");
        bevBox.setId("typeLabelBox");
//        food.setFont(Font.font("Arial", FontWeight.BOLD, 30));
//        bev.setFont(Font.font("Arial",FontWeight.BOLD,30));
//        
        //Creates center section where the menu items are displayed
        VBox menuSection = new VBox();
        menuSection.setSpacing(20);
        mainPane.setPadding(new Insets(20,20,20,20));
        
        //Creates title of the menu
        HBox menuBoxTop = new HBox();
        menuBoxTop.setPadding(new Insets(0,25,15,25));
        VBox menuTitleBox = new VBox();
        menuTitleBox.setId("menuTitleBox");
        menuTitleBox.setPrefWidth(380);
        menuTitleBox.setPrefHeight(60);
        menuBoxTop.setAlignment(Pos.CENTER);
//        menuTitle.setFont(Font.font("Arial",FontWeight.EXTRA_BOLD,40));
        menuBoxTop.getChildren().add(menuTitleBox);
        mainPane.setTop(menuBoxTop); 
        //Displays "Food" and "Beverage" Labels
        foodMenuGrid.add(foodBox,0,0,4,1);
        bevMenuGrid.add(bevBox,0,0,4,1); 
        //foodMenuGrid.setGridLinesVisible(true);
        //bevMenuGrid.setGridLinesVisible(true);
        
        //Display each item w/ their info onto GUI using itemsList array
        createSectionMenu(foodMenuGrid, 0);
        createSectionMenu(bevMenuGrid, 1);
        setComboBoxHandler();
        
        menuSection.getChildren().add(foodMenuGrid);
        menuSection.getChildren().add(bevMenuGrid); 
        menuSection.setPadding(new Insets(20,5,20,50));
        ScrollPane menuScrollPane = new ScrollPane();
        menuScrollPane.setContent(menuSection);

        
        mainPane.setCenter(menuScrollPane);
    }
    
    /*************************
    *  Function name: createSectionMenu        Description: This function is responsible for creating the sections of the  
    *  Date: 8/1/16                                       menu. The menu is separated by its "type" such as food or beverage.
    *  Programmers: Kevin                                 The items and its addons are added to their appropriate display.               
    *  Data structures: itemsList, addonList               
    **************************/
    private void createSectionMenu(GridPane grid, int typeNum){
        //Constrains the column size
        ColumnConstraints colimg = new ColumnConstraints(60);
        ColumnConstraints col1 = new ColumnConstraints(500);
        grid.getColumnConstraints().set(0, colimg);
        grid.getColumnConstraints().add(1, col1);
        
        int j=1; //Refers to initial row to start populating data.
        //Adds the items into the appropriate section of the menu.
        for(int i=0; i<itemsList.size();i++)
        {
            //If beverage, add to beverage section
            if(itemsList.get(i).getType()==typeNum && !itemsList.get(i).isDeleted())
            {
                grid.add(itemsList.get(i).nameLbl,1,j);
                grid.add(itemsList.get(i).descLbl, 1, j+1);
                grid.add(itemsList.get(i).priceLbl, 2, j+1);
                grid.add(itemsList.get(i).spinBox, 3, j+1);
                grid.add(itemsList.get(i).getImageView(), 0, j);
                int skipVar=0;
                for(int m=0; m<itemsList.get(i).getAddonList().size();m++){
                    grid.add(itemsList.get(i).getAddonList().get(m).checkBox,1,m+2+j);
                    skipVar++;
                }
                j+=skipVar;
                j=j+2;
            }
        }
    }
    
    /*************************
    *  Function name: addToCartHandler        Description: This function is called when the user presses the "Add to Cart" button.  
    *  Date: 8/1/16                                      The function calls another function which actually builds the cart page.
    *  Programmers: Kevin                                             
    *  Data structures: NONE               
    **************************/
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
    
    /*************************
    *  Function name: setComboBoxHandler        Description: This function is responsible for creating a handler for each combo box  
    *  Date: 8/1/16                                        in the cart summary page for each item.
    *  Programmers: Kevin                                             
    *  Data structures: itemsList               
    **************************/
    private void setComboBoxHandler(){
        for(int i=0;i<itemsList.size();i++)
        {
           itemsList.get(i).cb.setOnAction(e->comboBoxHandler(e));
        }
    }
    
    /*************************
    *  Function name: comboBoxHandler        Description: In cart stage, this handles the event when someone changes quantity using combo box, and 
    *  Date: 8/1/16                                     automatically updates the summary amount labels.
    *  Programmers: Kevin                               
    *  Data structures: cartItems                       
    **************************/
    public void comboBoxHandler(ActionEvent e)
    {
        for(int i=0;i<cart.getCartItems().size();i++)
        {
            if(((Control)e.getSource()).getId().compareTo(cart.getCartItems().get(i).idLbl.getText())==0)
            {
                cart.getCartItems().get(i).quantityOrderedInCart=cart.getCartItems().get(i).cb.getValue();
                cart.calcCartTotals();
                updateTotalsLabels();
            }
        }
        
    }
    
    /*************************
    *  Function name: updateTotalsLabels        Description: This function is responsible for updating the total and subtotal labels 
    *  Date: 8/1/16                                        in the cart summary page. Before calling this function, the programmer
    *  Programmers: Kevin                                  is expected to have calculated the total and subtotal.
    *  Data structures: NONE               
    **************************/
    public void updateTotalsLabels(){
        double totalC;
        double subtotalC;
        //Sets the text for the subtotal, tax rate, and total labels,
        //and also sets the precision of the values.
        Double toBeTruncated = new Double(cart.getTotal());
        totalC = new BigDecimal(toBeTruncated).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
        toBeTruncated = new Double(cart.getSubTotal());
        subtotalC= new BigDecimal(toBeTruncated).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
        subtotalLabel.setText("Subtotal:   "+subtotalC);
        totalLabel.setText("Total: "+totalC);
        taxRateLabel.setText("Tax rate:    "+Double.toString(cart.getTaxRate()*100)+"%");
    }
    
    /*************************
    *  Function name: loadQuantityToCart        Description: Extract the quantity ordered in the main menu and add to cart.
    *  Date: 8/1/16                                        This function is supposed to be called after the user presses
    *  Programmers: Kevin                                  "Add to Cart"
    *  Data structures: itemsList, cartItems               
    **************************/
    //
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
    
    /*************************
    *  Function name: loadQuantityToCart        Description: Extract the quantity ordered in the main menu and add to cart.
    *  Date: 8/1/16                                        This function is supposed to be called after the user presses
    *  Programmers: Kevin                                  "Add to Cart"
    *  Data structures: itemsList, cartItems               
    **************************/
    private void buildCartScene(){
        //Create layouts to organize elements
        cartBorderPane = new BorderPane();
        cartGrid = new GridPane();
        cartGrid.setPadding(new Insets(15,20,15,20));
        cartGrid.setHgap(50);
        cartGrid.setAlignment(Pos.TOP_LEFT);
        ScrollPane sp = new ScrollPane();
        sp.setContent(cartGrid);
        cartBorderPane.setCenter(sp);
        
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
        cart.calcCartTotals();
        updateTotalsLabels();
 
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
        cartScene.getStylesheets().add("css/adminMenu.css");
        theStage.setTitle("Shopping Cart - Agile's Cafe");
    }
    
    //Called when the user checks out the cart
    private void checkoutButtonHandler(){
        if(cart.getCartItems().isEmpty()){
            errorEmptyCartStage();
        }
        else
        {
            //Updates total label in billing scene
            totalLbl.setText(Double.toString(cart.getTotal()));
            //Displays the billing scene
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
        //cartGrid.setGridLinesVisible(true);
        
        int j=0; //j is used to place the items in the correct row as items are added to the GUI.
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
                buildCartScene();
                });
                
                //Create the addon for the item if checked in the main menu
                int skipValue=0;
                for(int m=0;m<cart.getCartItems().get(i).getAddonList().size();m++){
                    //If addon is checked, add it to the summary page.
                    if(cart.getCartItems().get(i).getAddonList().get(m).checkBox.isSelected())
                    {
                        cartGrid.getChildren().remove(cart.getCartItems().get(i).addonInfo);
                        cart.getCartItems().get(i).setAddonLabelInfo();
                        cartGrid.add(cart.getCartItems().get(i).addonInfo, 0, j+2);
                        skipValue++;
                    }
                }
                j+=2;
            }
        }

    }
    
    //When user clisk the back button, this fucntion handles that event.
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
        titleLabel.setId("TitleLbl");
        titleLabel.setPadding(new Insets(0,0,0,165));
        titleLabel.setFont(Font.font("Arial",FontWeight.EXTRA_BOLD,20));
        Double toBeTruncated = cart.getTotal();
        double total = new BigDecimal(toBeTruncated).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
        totalLbl = new Label("Total to be charged: $"+total);
        totalLbl.setId("totalLbl");
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
        
        Scene billingScene = new Scene(ccBorderPane,600,600);
        Scene payNowScene = new Scene(cashBorderPane,600,600);
        
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
        Text confirmText = new Text("Please see cashier to pay in cash. \n"+" Press 'Submit Order' to submit the order.\n");
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
        billingScene.getStylesheets().add("css/adminMenu.css");
        payNowScene.getStylesheets().add("css/adminMenu.css");
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
        SQL_DB mysqlDB = SQL_DB.getInstance();
        try{
            mysqlDB.connect(); 
        }catch(Exception e)
        {
            System.out.print("Error connecting");
        }
        for(int i =0;i<cart.getCartItems().size();i++){
            itemsList.get(i).addToQuantityOrdered(itemsList.get(i).quantityOrderedInCart);
            System.out.print("QO: "+itemsList.get(i).getQuantityOrdered());
        }
        mysqlDB.addSaleOrder(cart.getTotal());
        thankYouStage();
        cart.clear();
        start(theStage);
    }
    
    //Builds the admin window
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
        logInScene.getStylesheets().add("css/adminMenu.css");
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

    //Checks and displays error message if user enters wrong username/password
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

    //The window of the edit items menu
    public void editItemByIndex(int index)
    {
        Stage editItem = new Stage();
        editItem.setTitle("Editing "+itemsList.get(index).getName());
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
        typeComboBox = new ComboBox();
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
        grid.setVgap(10);
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
        Scene scene = new Scene(grid,500,400); 
        scene.getStylesheets().add("css/adminMenu.css");
        editItem.setScene(scene);
        editItem.show();

    }
    
    //Renames the label after an edit has been made.
    public void renameLabels(Item item){
        for(int i=0; i<itemsList.size();i++){
            if(item.getItemID()==itemsList.get(i).getItemID()){
                itemsList.get(i).nameLbl.setText(item.getName());
                itemsList.get(i).priceLbl.setText(Double.toString(item.getPrice()));
                itemsList.get(i).descLbl.setText(item.getDescription());
            }
        }
    }
    
    //Displays the view of all the items to edit, along with their options (delete, add, edit)
    public void adminEditMenuItems()
    { 
        
        // List of items to edit/delete stage
        Stage editMenuItems = new Stage();
        int BTN_SIZE = 160;
        editMenuItems.setTitle("Edit Items");
        editMenuItems.initModality(Modality.APPLICATION_MODAL);
        GridPane itemBox = new GridPane();
        itemBox.setId("items");
        // Go through each item and put into grid
        
        // Add item button
        Button addBtn = new Button("Add Item");
        addBtn.setId("Button_addBtn");
        addBtn.setPadding(new Insets(5));
        VBox addBtnBox = new VBox(addBtn);
        addBtnBox.setAlignment(Pos.TOP_RIGHT);
        addBtnBox.getChildren().add(itemBox); 
        addBtnBox.setPadding(new Insets(7));
        // Add item action 
        addBtn.setOnAction(e->addNewItem(editMenuItems));
       
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
            editBtn.setPrefWidth(BTN_SIZE);
            // Delete button 
            deleteBtn.setId("deleteItemBox");
            // On delete action
            int indexToDelete = index;
            deleteBtn.setOnAction(e->{confirmItemDelete(indexToDelete, editMenuItems);});
            deleteBtn.setPrefWidth(BTN_SIZE);
            // Box for edit button 
            editBtn.setId("editItemBtnBox");
             
            
            GridPane grid = new GridPane();
            VBox vbox = new VBox(); 
            grid.add(imageOutput,0,0);
            grid.add(title, 1, 0);
            grid.add(Price, 1, 1); 
        
            grid.add(type, 1, 2);
            grid.add(desc, 1, 3); 
            
            vbox.setSpacing(5);
            vbox.getChildren().addAll(editBtn,deleteBtn); 
            vbox.setPadding(new Insets(10));
            grid.add(vbox,0,4,2,5);
            
            GridPane.setMargin(grid, new Insets(5,0,0,0)); 
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
            grid.setAlignment(Pos.TOP_CENTER);
            grid.setId("itemBox");
            
            grid.setPrefHeight(220);
            index++;
            currentCol++;
        }   
        
        ScrollPane graphScrollPane = new ScrollPane();
        addBtnBox.setMinWidth(500); 
        graphScrollPane.setContent(addBtnBox);
        
        Scene scene = new Scene(graphScrollPane,533,500);
        scene.getStylesheets().add("css/adminMenu.css");
        editMenuItems.setScene(scene);
        editMenuItems.show();
    }
    
    //The window that displays when the user clicks to add a new item
    public void addNewItem(Stage editMenuItemsStage)
    {
        newItemStage = new Stage();
        GridPane grid = new GridPane();
        newItemStage.initModality(Modality.APPLICATION_MODAL);
        
         // Labels
        Label itemName = new Label("Name: ");
        Label itemPrice = new Label("Price: ");
        Label itemDesc = new Label("Description: "); 
        Label itemType = new Label("Type: ");
        Label itemImg  = new Label("Upload Image: "); 
        typeComboBox = new ComboBox();
        typeComboBox.getItems().add(0, "Food");
        typeComboBox.getItems().add(1, "Beverage");  
        typeComboBox.setValue("Food");
        // Textfields
         nameTF = new TextField ();
         priceTF = new TextField ();
         descTF  = new TextField ();
         typeTF    = new TextField();
       
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
        
        grid.setVgap(10);
        // on upload
         // ON UPLOAD 
        
        uploadBtn.setOnAction( e-> fileChooserHandler(e,newItemStage,grid));
        // on saving  
         
        submitBtn.setOnAction(e-> submitBtnHandler() ); 
        Scene scene = new Scene(grid,400,300);
        newItemStage.setScene(scene);
        newItemStage.setTitle("Add New Item");
        scene.getStylesheets().add("css/adminMenu.css");
        newItemStage.show(); 
        
    }
    
    //Handlers the file chooser button on the edit items page
    public void fileChooserHandler(ActionEvent e, Stage stage, GridPane grid){
        //Takes in: Stage = newItemsStage, grid = grid
        final FileChooser fileChooser = new FileChooser();
        List<File> list =
            fileChooser.showOpenMultipleDialog(stage);
        if (list != null) {
            for (File file : list) { 
                grid.getChildren().remove(((Control)e.getSource()));
                Label fileName = new Label(String.valueOf(file));
                grid.add(fileName,1,0);

                imageFileNameToUpload = file;
            }
        }
    }
    
    //Used when user submits a new item by pressing "Save"
    public void submitBtnHandler(){
                String name = nameTF.getText();
                String desc = descTF.getText();
                int type = typeComboBox.getSelectionModel().getSelectedIndex();
                double price = -1;
                try{
                    price = Double.parseDouble(priceTF.getText());
                }
                catch(NumberFormatException n)
                { 
                    showErrorMessage("Price not valid!","Please note that the price must be an integer or decimal number");
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
                    
                    SQL_DB mysqlDB = SQL_DB.getInstance();
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
                            newItemStage.hide();
                            editMenuItemsStage.hide();
                            adminEditMenuItems();
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
                    
                    showInformation("Item added!","Item "+name+" has been added");
                     
                }
                else // error
                {
                    if(price >=0)
                    {
                        showErrorMessage("Incomplete form","Please make sure the form is completely filled out before proceeding");
                    }
                }
    }
    
    //Asks for confirmation when user clicks the delete button on an item
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
    
    //Asks for confirmation when user clicks the delete button on an addon
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
    
    //Displays the admin menu after the user logs in
    public void showAdminMenu()
    {  
        Stage adminMenu = new Stage();
        adminMenu.initModality(Modality.APPLICATION_MODAL);

        VBox adminMenuVBox = new VBox();
        adminMenuVBox.setAlignment(Pos.CENTER);
        adminMenuVBox.setSpacing(15);
        
      // No need for admin title since it's already on dialog box
//        Label titleLbl = new Label("Admin Menu");
//        titleLbl.setMaxWidth(Double.MAX_VALUE);
//        titleLbl.setAlignment(Pos.CENTER);
//        VBox titleBox  = new VBox(titleLbl);
//        titleBox.setId("TITLEBOX");
//        titleBox.setPadding(new Insets(10,0,10,0));
        

        // Edit Menu Button
        Button editMenuBtn = new Button("Manage Menu Items");  
        //editMenuBtn.setOnAction(adminEditMenuItems(adminMenu));
        editMenuBtn.setOnAction(e->{adminMenu.close();adminEditMenuItems();});
        editMenuBtn.setId("editMenuBtn");  
        editMenuBtn.setPrefSize(200,50);
        
        // View Reports Button

        Button viewReportsBtn = new Button("View Reports"); 
        viewReportsBtn.setId("viewReportsBtn");
        viewReportsBtn.setPrefSize(200,50);
        viewReportsBtn.setOnAction(e->viewReportsBtnHandler());
        
        //Edit settings Button
        Button editSettingsBtn = new Button("Edit Settings");
        editSettingsBtn.setOnAction(e->editSettingsHandler());
        editSettingsBtn.setPrefSize(200,50);
        editSettingsBtn.setId("editMenuBtn"); 
        
        adminMenuVBox.getChildren().addAll(editMenuBtn,viewReportsBtn,editSettingsBtn);
        
        Scene menuScene = new Scene(adminMenuVBox,310,260);

        menuScene.getStylesheets().add("css/adminMenu.css"); 
        adminMenu.setScene(menuScene); 
        adminMenu.setTitle("Administrator Menu");
        adminMenu.show(); 
        
    }
    
    //Create top order and sales report
    public void viewReportsBtnHandler(){
        Stage viewReportsStage = new Stage();
        viewReportsStage.setTitle("Reports");

        //HBox to hold both graphs
        HBox holdGraphsHBox = new HBox();
        holdGraphsHBox.setSpacing(25);

        //Create scrolling feature
        ScrollPane graphScrollPane = new ScrollPane();
        graphScrollPane.setContent(holdGraphsHBox);
        
        
        //----------------CREATE PIE CHART-------------------
        Text pieChartTitleText = new Text("Item popularity");
        pieChartTitleText.setFont(Font.font("Arial",FontWeight.BOLD,25));
        
                        /*--------------FOR DEBUGGING---------------
                        for(int i =0; i<itemsList.size();i++){
                            itemsList.get(i).addToQuantityOrdered(i);
                        }*/
        
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for(int i =0;i<itemsList.size();i++){
            PieChart.Data temp = new PieChart.Data(itemsList.get(i).getName(), itemsList.get(i).getQuantityOrdered());
            pieChartData.add(temp);
        }
        
        final PieChart chart = new PieChart(pieChartData);
        chart.setTitle("Item Popularity");
        chart.setMinWidth(550);
        chart.setMinHeight(550);
        //----------------END CREATE PIE CHART-------------------
        Text salesTitleText = new Text("Sales History");
        Text dateTxt = new Text("Date");
        Text totalTxt = new Text("Total");
        HBox categoryHBox = new HBox(45);
        categoryHBox.setPadding(new Insets(0,0,0,15));
        categoryHBox.setAlignment(Pos.CENTER);
        categoryHBox.getChildren().addAll(dateTxt, totalTxt);
        
        salesTitleText.setFont(Font.font("Arial",FontWeight.BOLD,25));
        VBox salesVBox = new VBox();
        salesVBox.setAlignment(Pos.CENTER);
        salesVBox.setPadding(new Insets(0,0,0,100));
        salesVBox.setSpacing(10);
        salesVBox.getChildren().addAll(salesTitleText, categoryHBox);
        
        for(int i =0; i<salesList.size();i++){
            salesVBox.getChildren().add(new Text(salesList.get(i).getSaleDate()+"         "+Double.toString(salesList.get(i).getTotal())));
        }
        holdGraphsHBox.getChildren().addAll(chart,salesVBox);
        Scene graphScene = new Scene(graphScrollPane,1100,600);
        viewReportsStage.initModality(Modality.APPLICATION_MODAL);
        graphScene.getStylesheets().add("css/adminMenu.css");
        viewReportsStage.setScene(graphScene);
        viewReportsStage.show();
    }
    
    //Displays the settings window when the admin clicks on the edit settings button.
    public void editSettingsHandler(){
        Stage settingsStage = new Stage();
        settingsStage.setTitle("Edit Settings");
        VBox settingsVBox = new VBox();
        settingsVBox.setSpacing(20);
        settingsVBox.setAlignment(Pos.CENTER);
        GridPane settingsGridPane = new GridPane();
        settingsGridPane.setAlignment(Pos.CENTER);
        Scene SettingsScene = new Scene(settingsVBox,500,500);
        
        //Create change tax rate form
        Label changeTRLbl = new Label("Enter new tax rate:    ");
        changeTR_TF = new TextField();
        changeTR_TF.setPromptText("Enter new tax rate");
        Button applyChangeBtn = new Button("Apply Changes");
        applyChangeBtn.setOnAction(e->applyChangesHandler());
        applyChangeBtn.setAlignment(Pos.CENTER);
        //Note: Needs error checking.
        
        //Tells user if changes has been applied
        statusLbl = new Text("");
        statusLbl.setFill(Color.FIREBRICK);
        
        settingsGridPane.add(changeTRLbl, 0, 0);
        settingsGridPane.add(changeTR_TF, 1, 0);
        settingsVBox.getChildren().addAll(settingsGridPane,applyChangeBtn,statusLbl);
        
        settingsStage.initModality(Modality.APPLICATION_MODAL);
        settingsStage.setScene(SettingsScene);
        SettingsScene.getStylesheets().add("css/adminMenu.css");
        settingsStage.show();
    }
    
    //handles the event when the user clicks teh apply changes button.
    public void applyChangesHandler(){
        //Note: Needs error checking.
        cart.setTaxRate(Double.parseDouble(changeTR_TF.getText()));
        statusLbl.setText("Changes Applied!");
    }
    
    //Displays the manage addon window where the user can choose to manage addons
    public void manageAddons(int index)
    { 
        Item item = itemsList.get(index);
        ArrayList<addOn> addonList = item.getAddOnList();
        
        Stage addonsStage = new Stage();
        GridPane grid = new GridPane();
        Button addBtn = new Button("Add Addon");
        addBtn.setId("Button_addBtn");
        addBtn.setOnAction(e->{addonsStage.hide();addAddon(index);});
        grid.add(addBtn,3, 0);
        
        VBox verticalLayout = new VBox(); 
        int addonIndex = 0;
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
                    addonList.remove(passAddonIndex); 
                    grid.getChildren().remove(name);  
                    grid.getChildren().remove(price);
                    grid.getChildren().remove(editBtn); 
                    grid.getChildren().remove(deleteBtn);
                    
                    start(theStage);
                    
                    SQL_DB mysqlDB = SQL_DB.getInstance();
                    try{
                        mysqlDB.connect();
                        // Check if fields are not empty 
                        
                        mysqlDB.deleteAddon(addon.getAddOnID());
                    }catch(Exception a)  
                    {
                        System.out.print("Error connecting");
                    }  
                    // deleted addon                     
                    addonsStage.hide();
                    manageAddons(itemIndex);
                }
            });
            addonIndex++;
        }
        grid.setPadding(new Insets(10));  
        grid.setVgap(5);
        addBtn.setAlignment(Pos.TOP_RIGHT);
        verticalLayout.getChildren().addAll(addBtn,grid);
        verticalLayout.setPadding(new Insets(5));
        Scene scene = new Scene(verticalLayout,300,300);
        addonsStage.setTitle("Manage Addons");
        scene.getStylesheets().add("css/adminMenu.css");
        addonsStage.setScene(scene);
        addonsStage.show();
    }
    
    //The function that edits the addon and changes the view to reflect that change.
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
        grid.setVgap(5);
        grid.setHgap(5);
        Scene scene = new Scene(grid,220,200);
        scene.getStylesheets().add("css/adminMenu.css");
        editAddonStage.setScene(scene);
        editAddonStage.setTitle("Edit Addons");
        editAddonStage.show();
    }
    
    //When the user adds an addon, the GUI is changed to reflect that.
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
                    SQL_DB mysqlDB = SQL_DB.getInstance();
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
        grid.setVgap(8);
        grid.setPadding(new Insets(10));
        
        Scene scene = new Scene(grid,215,250);
        scene.getStylesheets().add("css/adminMenu.css");
        addAddonStage.setScene(scene);
        addAddonStage.setTitle("Add Addon");
        addAddonStage.show();
    }
    
    //Used to create a generic error message
    public void showErrorMessage(String title, String desc)
    {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(desc); 

        alert.showAndWait();
    }
    
    //Calls when the user clicks in order to open a file.
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
    
    //Used to create a generic information message to tell the user what has happened.
    private void showInformation(String title,String desc)
    {
        Alert alert = new Alert(AlertType.INFORMATION);  
        alert.setTitle(title);
        alert.setHeaderText(desc);

        alert.showAndWait();
    }
    
    //Only used to launch the JavaFX application.
    public static void main(String[] args) { launch(args); }   
}