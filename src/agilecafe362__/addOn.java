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
    private String name;
    private double Price; 
    private int addOnID;
    private Boolean checked;
    
    public addOn(String addName, double Price){
        this.name = addName;
        this.Price = Price;
    }
    public String getName()
    {
        return name;
    }
    public double getPrice()
    {
        return Price;
    }
    public int getAddOnID()
    {
        return addOnID;
    }
    public Boolean isChecked(){
        return checked;
    }
    public void setChecked(){
        checked = true;
    } 
    public void setAddOnID(int id)
    {
        addOnID = id;
    }    
    
    public void setName(String addName){
        name = addName;
    }
    
    public double getAddPrice(){
        return Price;
    }
    
    public void setAddPrice(double addPrice){
        this.Price = addPrice;
    }
    
    
}
