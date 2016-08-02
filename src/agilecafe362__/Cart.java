/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agilecafe362__;
import java.util.ArrayList;
import javafx.scene.control.Spinner;

/*********************************************************************
* Class Name: Cart
* Programmer: Binh and Kevin
* Description: Stores the cart items list, tax rate and total of
* the sale. Methods are used to retrieve tax rate,
* cart items, subtotal and total.
* Data Structures: Used arraylist to store cart item objects
*********************************************************************/

public class Cart {
    private static Cart cart;                 // Cart instance static
    private double taxRate;                   // tax rate
    private final ArrayList<Item> cartItems;  // List of cart items
    private double subTotal;                  // sub total (before tax applied)
    private double total;                     // total 
    
    // Constructor initializes the arraylist used for cart items
    private Cart(){
        // New cart items initialized to empty
        cartItems = new ArrayList<>(); 
        subTotal = 0;
        total = 0;
        taxRate = .1;
    }
    // Returns instantiated class or creates new one
    // if does not exist
    public static Cart getInstance(){
        if(cart == null)
            cart = new Cart();
        
        return cart;
    }
    // Return the list of cart items
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
    
    // Calculate the total and subtotal for the list of cart items depending on
    // the price of each item as well as the quantity ordered
    public void calcCartTotals(){
        double totalC;
        double subtotalC=0;
        //Calculate subtotal, go through each item in cart list 
        for(int i=0;i<cartItems.size();i++)
        {
            // The price is multiplied by quantity ordered
            subtotalC+= cartItems.get(i).getPrice()* cartItems.get(i).quantityOrderedInCart;
            
            // Go through each addon in the item and see if it has been checked.
            // If so then it is added to the subtotal
            for(int j=0;j<cartItems.get(i).getAddonList().size();j++)
            {
                if(cartItems.get(i).getAddonList().get(j).checkBox.isSelected())
                {
                        subtotalC+= cartItems.get(i).getAddonList().get(j).getPrice()*cartItems.get(i).quantityOrderedInCart;
                }
            }
        }

        //Calculate total by including the tax rate
        totalC=subtotalC+(subtotalC*this.getTaxRate());
        this.setSubTotal(subtotalC);
        this.setTotal(totalC);     
    }
    
    // Sets the tax rate
    public void setTaxRate(double taxRate){
        this.taxRate = taxRate;
    }
    // Gets the tax rate
    public double getTaxRate(){
        return taxRate;
    }
    // Gets the subtotal
    public double getSubTotal(){
        return subTotal;
    } 
    // Gets the total
    public double getTotal(){
        return total;
    }
    // Sets the subtotal of the current order
    public void setSubTotal(double subTotal){ this.subTotal = subTotal; }
    
    // Sets the total of the current order
    public void setTotal(double total) { this.total=total; }
}
