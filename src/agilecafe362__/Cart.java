/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agilecafe362__;
import java.util.ArrayList;

/**
 *
 * @author Dragon
 */
public class Cart {
    private double taxRate;
    private ArrayList<Item> cartItems = new ArrayList<Item>();
    private double subTotal;
    private double total;
    
    public Cart(){}
    
    public ArrayList<Item> getCartItems(){
        return cartItems;
    }
    public void clear(){
        cartItems.clear();
    }
    
    public void setTaxRate(double taxRate){
        this.taxRate = taxRate;
    }
    
    public double getTaxRate(){
        return taxRate;
    }
    
    public double getSubTotal(){
        return subTotal;
    }
    
    public void setSubTotal(double subTotal){
        this.subTotal = subTotal;
    }
    
    public double getTotal(){
        return total;
    }
    
    public void setTotal(double total){
        this.total = total;
    }
}
