/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agilecafe362__;

/**
 *
 * @author Dragon
 */
public class addOn {
    private String addName;
    private double Price;
    private String addOn;
    private int addOnID;
    private Boolean checked;
    
    public addOn(String addName, double Price){
        this.addName = addName;
        this.Price = Price;
    }
    public Boolean isChecked(){
        return checked;
    }
    public void setChecked(){
        checked = true;
    }
    public int getID(){
        return addOnID;
    }
    public String getAddOn(){
        return addOn;
    }
    
    public void setAddOn(String addOn){
        this.addOn = addOn;
    }
    public String getAddName(){
        return addName;
    }
    
    public void setAddName(String addName){
        this.addName = addName;
    }
    
    public double getAddPrice(){
        return Price;
    }
    
    public void setAddPrice(double addPrice){
        this.Price = addPrice;
    }
    
    
}
