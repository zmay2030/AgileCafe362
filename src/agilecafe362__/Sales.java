/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agilecafe362__;

import java.sql.Date;

/*********************************************************************
* Class Name: Sales
* Programmer: Ahmad
* Description: Stores the sale total and date for each order
* and retrieves them
*********************************************************************/
public class Sales {
    private double total;       // total is used for sale order 
    private String saleDate;    // sale date that ordered was purchased
    
    // Sales is initialized by two values (total sales and sales date)
    // when coming from the sql database
    public Sales(double totalSales, String theSalesDate)
    {
        total = totalSales;  
        saleDate = theSalesDate;
    }
    // Get the price total for sale
    public double getTotal()
    {
        return total;
    }
    // Set the date of the sale
    public String getSaleDate()
    {
        return saleDate;
    }
}
