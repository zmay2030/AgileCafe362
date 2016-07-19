/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cafe;

import java.text.NumberFormat;

/**
 *
 * @author Dragon
 */
public class Items {
    private double price;
    private int itemID;
    private String name;
    private String description;
    private String category;
    private String image;
    public Items(int itemID, String name, String description, String category, double price){
        this.name = name;
        this.itemID = itemID;
        this.category = category;
        this.description = description;
        this.price = price;
    }
    
    public Items(int itemID, String name, String description, String category, double price, String image){
        this.name = name;
        this.itemID = itemID;
        this.category = category;
        this.description = description;
        this.price = price;
        this.image = image;
    }
    
    public int getItemID(){
        return itemID;
    }
    
    public void setItemID(int itemID){
        this.itemID = itemID;
    }
    
    public String getCategory(){
        return category;
    }
    
    public void setCategory(String category){
        this.category = category;
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
    public String getImage(){
        return image;
    }
    public void setImage(String image){
        this.image = image;
    }
    public String toString(){
        NumberFormat fcurr = NumberFormat.getCurrencyInstance();
        if(image == null)
            return name + "(" + category + ")" +"\t" + description + "\t" + fcurr.format(price);
        else
            return name + "(" + category + ")" +"\t" + description + "\t" + fcurr.format(price) + "\t" + image;
    }
    
    
}
