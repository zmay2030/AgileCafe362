/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agilecafe362__;

import java.io.File;
import java.util.ArrayList;
import java.text.NumberFormat;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/*********************************************************************
* Class Name: Item 
* Programmer: Binh, Kevin and Ahmad
* Description: Stores the item attributes such as name, description,
* price, etc. It also contains the list of addons that the item
* contains. It contains also the number of times item has been ordered.
* It also contains the GUI JavaFX Labels that are used to display
* on the menu and cart.
*********************************************************************/
public class Item {
    private double price;       // price of item
    private int itemID;         // item id
    private String name;        // item name
    private String description; // item description
    private int type;           // item type
    private String image_path;  // item image path
    private final ArrayList<addOn> addOnList = new ArrayList<>(0); // list of addons
    private Boolean deleted;    // deleted is for setting item to delete if requested
    private Image image;        // image object for Javafx
    private int quantityOrdered;    // times item has been ordered 
    
    //FOR GUI
    public Label idLbl = new Label();       // item id label    
    public Label nameLbl = new Label();     // name label
    public Label descLbl = new Label();     // description label
    public Label priceLbl = new Label();    // price label
    public Label addonInfo = new Label();   // addon info label
    public Button removeButton = new Button("Remove"); // remove button
    public Spinner<Integer> spinBox = new Spinner<>(0,10,0); // spinbox used 
    public ComboBox<Integer> cb = new ComboBox<>(); // combo box used
    public Boolean isInCart = false; // used to check if item is in cart
    public int quantityOrderedInCart; // quantity ordered in the cart  
    
    // Item constructor passes in the item id, name and other attributes based
    // on the sql DB column values
    public Item(int itemID, String name, String description, int type, double price, String image_path,int ordered){
        //Info pulled from databse into attributes
        this.name = name;       // set item name
        this.itemID = itemID;   // set item id
        this.type = type;       // set item type
        this.description = description; // set item description
        this.price = price;             // set item price
        this.image_path = image_path;   // set item image path
        deleted = false;                //set item deleted to false (default)
        quantityOrdered = ordered;      // set times item has been ordered
        image = null;                   // null image 
        updateImg();                    // call updateImage to instantiate the Image JavaFX
                                        // object
        
        //Info for GUI, set the text for each label
        idLbl.setText(Integer.toString(itemID));    // id label
        nameLbl.setText(name);                      // name label
        descLbl.setText(description);               // desc label
        priceLbl.setText(Double.toString(price));   // price label 
        quantityOrderedInCart=0;    // quantity ordered
        spinBox.setMaxWidth(65);    // spinbox
        cb.setId(idLbl.getText());  
        cb.getItems().addAll(1,2,3,4,5,6,7,8,9,10);
        // Remove button 
        removeButton.setId(idLbl.getText());
    }
    
    // This method gets the image path of the item and creates an Image
    // JavaFX object.
    public void updateImg()
    {
        // new image view  
        // Load image   
        File file = new File("."); 
        try{
            // If image file exists
            image = new Image("file:///"+file.getAbsolutePath()+"/src/images/"+image_path);
        }
        catch(IllegalArgumentException e)
        {
            // Default to defaultimg.png if image path does not exists 
            System.out.print(e.toString());
            image = new Image("file:///"+file.getAbsolutePath()+"/src/images/defaultimg.png");
        }  
    }
    // Set all the addons in the addonlist has unchecked 
    // by default
    public void setAddonListUnchecked(){
            // If addonlist isn't empty, go through each addon
            if(!this.getAddonList().isEmpty()){
                for(int i=0; i<this.getAddonList().size();i++){
                    // Get each addon
                    while(this.getAddonList().get(i).checkBox.isSelected()==true)
                    {
                        this.getAddonList().get(i).checkBox.fire();
                    }
                    //Set addon to false checked
                    this.getAddonList().get(i).setChecked(false);
                }
            }
    }
    // This method sets the label info for each addon
    public void setAddonLabelInfo(){
        String tempText="Addons:\n";
        // If addonlist isn't empty, get each addon's attributes and set them to
        // a temporary string if the addon is checked
        if(!this.getAddonList().isEmpty()){
            for(int i=0;i<this.getAddonList().size();i++)
            {
                // If addon is checked, get the addon
                if(this.getAddOnList().get(i).isChecked()){
                    tempText+=this.getAddonList().get(i).getName()+" ($"+Double.toString(this.getAddonList().get(i).getPrice())+") \n";
                }
            }
        }
        // Sets the text for the addon
        addonInfo.setText(tempText);
    }
    
    // Get item id
    public int getItemID(){
        return itemID;
    }
    // Set item id
    public void setItemID(int itemID){
        this.itemID = itemID;
    }
    // Get item type
    public int getType(){
        return type;
    }
    // Set item category
    public void setCategory(int category){
        this.type = category;
    }
    // Get item price
    public double getPrice(){
        return price;
    }
    // Set item price
    public void setPrice(double price){
        this.price = price;
    }
    // Get item name
    public String getName(){
        return name;
    }
    // Set item name
    public void setName(String name){
        this.name = name;
    }
    // Get item description
    public String getDescription(){
        return description;
    }
    // Set item description
    public void setDescription(String description){
        this.description = description;
    }
    // Returns an imageview javafx object based on the Image object 
    public ImageView getImageView(){ 
        // Create new imgview object
        ImageView imgView = new ImageView();
        imgView.setImage(image);
        // Set width and height to 50 
        imgView.setFitWidth(50);
        imgView.setFitHeight(50);
        
        return imgView;
    }
    
    // Checks if item is deleted
    public Boolean isDeleted()
    {
        return deleted;
    }
    // Set/mark item to delete
    public void SetDelete()
    {
        quantityOrderedInCart=0;
        deleted = true;
    }
    // Set the item image path
    public void setImage(String image_path){
        this.image_path = image_path;
        updateImg();
    }
    // Add addon object to addon list
    public void addAddOn(addOn addon)
    {
        addOnList.add(addon);
    }
    // Gets addonlist of addon objects
    public ArrayList<addOn> getAddonList()
    {
        return addOnList;
    }
    
    // Converts titem to string
    @Override
    public String toString(){
        NumberFormat fcurr = NumberFormat.getCurrencyInstance();
        if(image_path == null)
            return name + "(" + type + ")" +"\t" + description + "\t" + fcurr.format(price);
        else
            return name + "(" + type + ")" +"\t" + description + "\t" + fcurr.format(price) + "\t" + image_path;
    }
    // Translate the type based on type integer
    public String typeTranslate(int type)
    {
        if (type == 0)
        {
            return "Food";
        }
        else
        {
            return "Beverage";
        }
    }
    // Adds to the quantity of items ordered
    public void addToQuantityOrdered(int num)
    {
        quantityOrdered += num;
    }
    // Get total quantity ordered
    public int getQuantityOrdered()
    {
        return quantityOrdered;
    }
    // Gets the list of addons
    public ArrayList<addOn> getAddOnList()
    {
        return addOnList;
    }
    // Gets item image path
    public String getImagePath()
    {
        return image_path;
    }
}