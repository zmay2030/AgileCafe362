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
    private Date saleDate;
    
    public Sales(double totalSales, Date theSalesDate)
    {
        total = totalSales;  
        saleDate = theSalesDate;
    }
    public double getTotal()
    {
        return total;
    }
    public Date getSaleDate()
    {
        return saleDate;
    }
}
