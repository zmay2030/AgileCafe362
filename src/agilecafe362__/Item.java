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

/**
 *
 * @author Dragon
 */
public class Item {
    private double price;
    private int itemID;
    private String name;
    private String description;
    private int type;
    private String image_path;
    private final ArrayList<addOn> addOnList = new ArrayList<>(0);
    private Boolean deleted;
    private Image image;
    private int quantityOrdered;
    
    //FOR GUI
    public Label idLbl = new Label();
    public Label nameLbl = new Label();
    public Label descLbl = new Label();
    public Label priceLbl = new Label();
    public Label addonInfo = new Label();
    public Button removeButton = new Button("Remove");
    public Spinner<Integer> spinBox = new Spinner<>(0,10,0);
    public ComboBox<Integer> cb = new ComboBox<>();
    public Boolean isInCart = false;
    public int quantityOrderedInCart;
    
    public Item(int itemID, String name, String description, int type, double price, String image_path,int ordered){
        //Info pulled from databse into attributes
        this.name = name;
        this.itemID = itemID;
        this.type = type;
        this.description = description;
        this.price = price;
        this.image_path = image_path;
        deleted = false;
        quantityOrdered = ordered;
        image = null;
        updateImg();
        
        //Info for GUI
        idLbl.setText(Integer.toString(itemID));
        nameLbl.setText(name);
        descLbl.setText(description);
        priceLbl.setText(Double.toString(price));
        quantityOrderedInCart=0;
        spinBox.setMaxWidth(65);
        cb.setId(idLbl.getText());
        cb.getItems().addAll(1,2,3,4,5,6,7,8,9,10);
        removeButton.setId(idLbl.getText());
    }
    
    
    public void updateImg()
    {
        // new image view  
        // Load image   
        File file = new File("."); 
        try{
            image = new Image("file:///"+file.getAbsolutePath()+"/src/images/"+image_path);
        }
        catch(IllegalArgumentException e)
        {
            System.out.print(e.toString());
            image = new Image("file:///"+file.getAbsolutePath()+"/src/images/defaultimg.png");
        }  
    }
  
            public void setAddonListUnchecked(){
            if(!this.getAddonList().isEmpty()){
            for(int i=0; i<this.getAddonList().size();i++){
                while(this.getAddonList().get(i).checkBox.isSelected()==true)
                {
                    this.getAddonList().get(i).checkBox.fire();
                }
                this.getAddonList().get(i).setChecked(false);
            }
            }
        }
        
        public void setAddonLabelInfo(){
            String tempText="With ";
            if(!this.getAddonList().isEmpty()){
                for(int i=0;i<this.getAddonList().size();i++)
                {
                    if(this.getAddOnList().get(i).isChecked()){
                    tempText+=this.getAddonList().get(i).getName()+" ($"+Double.toString(this.getAddonList().get(i).getPrice())+") \n";
                    }
                }
            }
            addonInfo.setText(tempText);
        }
    
    public int getItemID(){
        return itemID;
    }
    
    public void setItemID(int itemID){
        this.itemID = itemID;
    }
    
    public int getType(){
        return type;
    }
    
    public void setCategory(int category){
        this.type = category;
    }
    
    public double getPrice(){
        return price;
    }
    public void setPrice(double price){
        this.price = price;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getDescription(){
        return description;
    }
    public void setDescription(String description){
        this.description = description;
    }
    public ImageView getImageView(){ 
        ImageView imgView = new ImageView();
        imgView.setImage(image);
        imgView.setFitWidth(50);
        imgView.setFitHeight(50);
        
        return imgView;
    }
    public Boolean isDeleted()
    {
        return deleted;
    }
    public void SetDelete()
    {
        quantityOrderedInCart=0;
        deleted = true;
    }
    public void setImage(String image_path){
        this.image_path = image_path;
        updateImg();
    }
    public void addAddOn(addOn addon)
    {
        addOnList.add(addon);
    }
    public ArrayList<addOn> getAddonList()
    {
        return addOnList;
    }
    
    @Override
    public String toString(){
        NumberFormat fcurr = NumberFormat.getCurrencyInstance();
        if(image_path == null)
            return name + "(" + type + ")" +"\t" + description + "\t" + fcurr.format(price);
        else
            return name + "(" + type + ")" +"\t" + description + "\t" + fcurr.format(price) + "\t" + image_path;
    }
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
    public void addToQuantityOrdered(int num)
    {
        quantityOrdered += num;
    }
    public ArrayList<addOn> getAddOnList()
    {
        return addOnList;
    }
    
    public int getQuantityOrdered(){
        return quantityOrdered;
    }
    
}