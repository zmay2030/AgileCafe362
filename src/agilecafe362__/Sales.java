/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agilecafe362__;

import java.sql.Date;

/**
 *
 * @author Aven
 */
public class Sales {
    private double total;
    private String saleDate;
    
    public Sales(double totalSales, String theSalesDate)
    {
        total = totalSales;  
        saleDate = theSalesDate;
    }
    public double getTotal()
    {
        return total;
    }
    public String getSaleDate()
    {
        return saleDate;
    }
}
