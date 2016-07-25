/*
 * SQL CLASS
 */
package agilecafe362__;
import java.sql.*; 
import java.util.ArrayList;

public class SQL_DB  {
    
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
    static final String DB_URL = "jdbc:mysql://97.74.31.83:3306/agilecafe";
    // Username and pass 
    static final String USER = "agilecafe";
    static final String PASS = "Cpsc362@";
    
    private Connection conn;
    public void connect() throws Exception {
           this.conn = null; 
    	   try{
    	      //STEP 2: Register JDBC driver
    	      Class.forName("com.mysql.jdbc.Driver").newInstance();

    	      //STEP 3: Open a connection
    	      System.out.println("Connecting to database...");
    	      this.conn = DriverManager.getConnection(DB_URL,USER,PASS);
   
    	   }catch(SQLException se){
    	      //Handle errors for JDBC
    	      se.printStackTrace();
    	   }catch(Exception e){
    	      //Handle errors for Class.forName
    	      e.printStackTrace();
    	   }finally{
    	      //finally block used to close resources
    	       
    	     
    	   }//end try
    }
    public void addAddonsList(Item item)
    { 
        Statement stmt = null;
        try
        { 
            stmt = (this.conn).createStatement();
            int itemId = item.getItemID();
            ResultSet rs = stmt.executeQuery("SELECT * from addon where item_id="+itemId);
            while(rs.next()){   
                int addonId = rs.getInt("addon_id");
                double price = rs.getInt("addon_price");
                String name  = rs.getString("addon_name");
                addOn newAddon = new addOn(name,price);
                newAddon.setAddOnID(addonId);
                item.addAddOn(newAddon);
            }  
             
        }catch(SQLException se){
    	      //Handle errors for JDBC
    	      se.printStackTrace();
    	   }catch(Exception e){
    	      //Handle errors for Class.forName
    	      e.printStackTrace();
    	   }finally{
    	      //finally block used to close resources
    	       
    	   }//end try 
    }
    public ArrayList<Item> getAllItems()
    {
        // Initialize list
        ArrayList<Item> itemsList = new ArrayList<Item>(0);
        Statement stmt = null;
        // MySQL statement   
        try
        { 
            stmt = (this.conn).createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * from items"); 
            while(rs.next()){ 
    	         //Retrieve by column name
    	         int itemId  = rs.getInt("item_id"); 
    	         String itemName = rs.getString("item_name"); 
    	         String itemDesc = rs.getString("item_desc"); 
    	         double itemPrice = rs.getDouble("item_price"); 
                 String itemImgPath = rs.getString("item_image_path");
                 int itemType = rs.getInt("item_type");
                 System.out.println(itemName+"\n");
                 // Create new Item Class
                 Item newItem = new Item(itemId,itemName,itemDesc,itemType,itemPrice,itemImgPath);
                 addAddonsList(newItem);
                 itemsList.add(newItem); 
            } 
             
        }catch(SQLException se){
    	      //Handle errors for JDBC
    	      se.printStackTrace();
    	   }catch(Exception e){
    	      //Handle errors for Class.forName
    	      e.printStackTrace();
    	   }finally{
    	      //finally block used to close resources
    	      try{
    	         if(stmt!=null)
    	            stmt.close();
    	      }catch(SQLException se2){
    	      }// nothing we can do
    	      try{
    	         if(conn!=null)
    	            conn.close();
    	      }catch(SQLException se){
    	         se.printStackTrace();
    	      }//end finally try
    	   }//end try
         
         
        return itemsList;
    }
    public void closeConn()
    { 
        try
        {
             this.conn.close();
        }catch(SQLException se){
    	      //Handle errors for JDBC
    	      se.printStackTrace();
    	   } 
    }
}