/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agilecafe362__;
import java.util.ArrayList;
import javafx.scene.control.Spinner;

/**
 *
 * @author Dragon
 */
public class Cart {
    private double taxRate;
    private final ArrayList<Item> cartItems;
    private double subTotal;
    private double total;
    
    public Cart(){
    cartItems = new ArrayList<>();
    subTotal = 0;
    total = 0;
    taxRate = .1;
    }
    
    public ArrayList<Item> getCartItems(){
        return cartItems;
    }
    public void clear(){
        for(int i=0; i< cartItems.size();i++){
            cartItems.get(i).setAddonListUnchecked();
            cartItems.get(i).quantityOrderedInCart=0;
            cartItems.get(i).cb.setValue(0);
            cartItems.get(i).isInCart=false;
            cartItems.get(i).spinBox = new Spinner<>(0,10,0);
            cartItems.get(i).spinBox.setId(cartItems.get(i).idLbl.getText());
            cartItems.get(i).spinBox.setMaxWidth(65);
        }
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
    
    public double getTotal(){
        return total;
    }
    
    public void setSubTotal(double subTotal){ this.subTotal = subTotal; }
    public void setTotal(double total) { this.total=total; }
}
