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
    private ArrayList<Integer> cartItems;
    private double subTotal;
    private double total;
    
    public Cart(){
    cartItems = new ArrayList<Integer>();
    subTotal = 0;
    total = 0;
    taxRate = .1;
    }
    
    public ArrayList<Integer> getCartItems(){
        return cartItems;
    }
    public void clear(){
        
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
    
    public void calcSubTotal(double price, int quantity){
        this.subTotal += price*quantity;
    }
    
    public double getTotal(){
        return total;
    }
    
    public void calcTotal(){
        total = subTotal+subTotal*taxRate;
    }
}
