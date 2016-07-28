/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agilecafe362__;

import javafx.event.ActionEvent;
import javafx.scene.control.CheckBox;

/**
 *
 * @author Dragon
 */
public class addOn {
    private String name;
    private double price; 
    private int addOnID;
    private Boolean checked;
    public final CheckBox checkBox;
    
    public addOn(String addName, double price){
        this.name = addName;
        this.price = price;
        checked = false;
        checkBox = new CheckBox("+ $"+Double.toString(price)+" "+addName);
        checkBox.setIndeterminate(false);
        checkBox.setOnAction(e->checkBoxHandler(e));
        while(checkBox.isSelected()==true)
                {
                    checkBox.fire();
                }
    }
    private void checkBoxHandler(ActionEvent e)
    {
        this.setChecked(this.checkBox.isSelected());
    }
    public String getName()
    {
        return name;
    }
    public double getPrice()
    {
        return price;
    }
    public int getAddOnID()
    {
        return addOnID;
    }
    public Boolean isChecked(){
        return checked;
    }
    public void setChecked(Boolean bool){
        checked = bool;
    } 
    public void setAddOnID(int id)
    {
        addOnID = id;
    }    
    
    public void setName(String addName){
        name = addName;
        checkBox.setText("+ $"+Double.toString(price)+" "+addName);
        checkBox.setIndeterminate(false);
        checkBox.setOnAction(e->checkBoxHandler(e));
    }
    public void setPrice(double num)
    {
        price = num;
        checkBox.setText("+ $"+Double.toString(price)+" "+name);
        checkBox.setIndeterminate(false);
        checkBox.setOnAction(e->checkBoxHandler(e));
    }
    public double getAddOnPrice(){
        return price;
    }
    
    public void setAddOnPrice(double addPrice){
        this.price = addPrice;
    }
   
}