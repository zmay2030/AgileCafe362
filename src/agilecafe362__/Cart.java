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
    private static Cart cart;
    private double taxRate;
    private final ArrayList<Item> cartItems;
    private double subTotal;
    private double total;
    
    private Cart(){
        cartItems = new ArrayList<>();
        subTotal = 0;
        total = 0;
        taxRate = .1;
    }
    
    public static Cart getInstance(){
        if(cart == null)
            cart = new Cart();
        
        return cart;
    }
    
    public ArrayList<Item> getCartItems(){
        return cartItems;
    }
    
    //Not only clears the vector, but also resets the spinbox, combobox, etc for future use.
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
    
    //
    public void calcCartTotals(){
        double totalC;
        double subtotalC=0;
        //Calculate subtotal
        for(int i=0;i<cartItems.size();i++)
        {
            subtotalC+= cartItems.get(i).getPrice()* cartItems.get(i).quantityOrderedInCart;
            
            for(int j=0;j<cartItems.get(i).getAddonList().size();j++)
            {
                if(cartItems.get(i).getAddonList().get(j).checkBox.isSelected())
                {
                        subtotalC+= cartItems.get(i).getAddonList().get(j).getPrice()*cartItems.get(i).quantityOrderedInCart;
                }
            }
        }

        //Calculate total
        totalC=subtotalC+(subtotalC*this.getTaxRate());
        this.setSubTotal(subtotalC);
        this.setTotal(totalC);     
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
