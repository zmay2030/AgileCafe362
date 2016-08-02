/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agilecafe362__;

import javafx.event.ActionEvent;
import javafx.scene.control.CheckBox;

/*********************************************************************
* Class Name: addOn (Addon class)
* Programmer: Binh, Kevin and Ahmad
* Description: Stores the addon name, price, addon id, is checked
* or not, and javafx's CheckBox object.
*********************************************************************/
public class addOn {
    private String name;
    private double price; 
    private int addOnID;
    private Boolean checked;
    public final CheckBox checkBox;
    
    // Passes the addon name and price to constructor from DB
    public addOn(String addName, double price){
        this.name = addName;    // addon name   
        this.price = price;     // addon price
        checked = false;        // checked to false by default
        // Create new checkbox object
        checkBox = new CheckBox("+ $"+Double.toString(price)+" "+addName);
        checkBox.setIndeterminate(false);
        checkBox.setOnAction(e->checkBoxHandler(e));
        // Call fire() method if checkbox is selected
        while(checkBox.isSelected()==true)
                {
                    checkBox.fire();
                }
    }
    // Method used to set checked to true when user checks the box 
    private void checkBoxHandler(ActionEvent e)
    {
        this.setChecked(this.checkBox.isSelected());
    }
    // Get addon name
    public String getName()
    {
        return name;
    }
    // Get addon price
    public double getPrice()
    {
        return price;
    }
    // Get addon id
    public int getAddOnID()
    {
        return addOnID;
    }
    // Check if addon has been checked
    public Boolean isChecked(){
        return checked;
    }
    // Set checked to true or false (opposite of what it was)
    public void setChecked(Boolean bool){
        checked = bool;
    } 
    // Set addonID
    public void setAddOnID(int id)
    {
        addOnID = id;
    }    
    // Set addon name
    public void setName(String addName){
        name = addName;     // addon name
        // Update the checkbox label
        checkBox.setText("+ $"+Double.toString(price)+" "+addName);
        checkBox.setIndeterminate(false);
        checkBox.setOnAction(e->checkBoxHandler(e));
    }
    // Sets addon price 
    public void setPrice(double num)
    {
        price = num; // addon price
        // Update the checkbox label
        checkBox.setText("+ $"+Double.toString(price)+" "+name);
        checkBox.setIndeterminate(false);
        checkBox.setOnAction(e->checkBoxHandler(e));
    }
    // Get addon price
    public double getAddOnPrice(){
        return price;
    }    
}