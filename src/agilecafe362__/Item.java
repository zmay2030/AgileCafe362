/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agilecafe362__;

import java.io.File;
import java.util.ArrayList;
import java.text.NumberFormat;
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
    private ImageView image; 
    
    public Item(int itemID, String name, String description, int type, double price, String image_path){
        this.name = name;
        this.itemID = itemID;
        this.type = type;
        this.description = description;
        this.price = price;
        this.image_path = image_path;
        deleted = false;
        updateImg();
    }
    public void updateImg()
    {
        // new image view 
        image = new ImageView();
        // Load image  
        Image imageFile = null;
        File file = new File("."); 
        try{
            imageFile = new Image("file:///"+file.getAbsolutePath()+"/src/images/"+image_path);
        }
        catch(IllegalArgumentException e)
        {
            System.out.print(e.toString());
            imageFile = new Image("file:///"+file.getAbsolutePath()+"/src/images/defaultimg.png");
        } 
        image.setImage(imageFile);
        image.setFitWidth(50);
        image.setFitHeight(50);
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
        return image;
    }
    public Boolean isDeleted()
    {
        return deleted;
    }
    public void SetDelete()
    {
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
    
    
}