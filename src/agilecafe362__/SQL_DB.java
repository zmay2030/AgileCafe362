/*
 * SQL CLASS
 */
package agilecafe362__;
import java.sql.*; 
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.sql.Date;
import java.util.Calendar;

/*********************************************************************
* Class Name: SQL_DB (MySQL Database) 
* Programmer: Ahmad
* Description: Stores the mysql driver, db url, username, password
* and connects the sql. It also includes all the sql queries such
* as inserting new item, retrieving items, updating items, adding
* addons, retrieving addons, etc.
* Uses the Connector/J SQL for Java.
*********************************************************************/
public class SQL_DB  {
    
    // Driver needed for java sql
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";      
    // The host needed to connect
    static final String DB_URL = "jdbc:mysql://97.74.31.83:3306/agilecafe";
    // Username and pass 
    static final String USER = "agilecafe";
    static final String PASS = "Cpsc362@";
    // Instance of sql is called once
    private static SQL_DB sqlInstance;
    // Instance of the Connection
    private Connection conn;
    private SQL_DB()  {  }
    
    // Used to connect the sql to the server and catches any exception
    // that prevent from connecting.
    public void connect() throws Exception {
            // Default connection to null then attempt to connect
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
    // Returns instantiated class or creates new one
    // if does not exist
    public static SQL_DB getInstance(){
        if(sqlInstance == null)
            sqlInstance = new SQL_DB();
        
        return sqlInstance;
    }
    // This method is called to add all the addon items to the Item's
    // arraylist of addons. It will get all the addons from the database
    // and instantiate each of them one-by-one and add them to the item 
    // addon list. 
    public void addAddonsList(Item item)
    {  
        Statement stmt = null;
        // Create the statement for retrieving addons then get the values
        // for each column and create a new addon object and put the column
        // values retrieved into the constructor
        try
        { 
            // Select statement to addons based on item id 
            stmt = (this.conn).createStatement();
            int itemId = item.getItemID();
            ResultSet rs = stmt.executeQuery("SELECT * from addon where item_id="+itemId);
            // Retrieve the addon id, price and name and add to addon's new
            // instantianted object. Then, add to item addon list
            while(rs.next()){   
                int addonId = rs.getInt("addon_id");          // addon id from DB
                double price = rs.getInt("addon_price");      // addon price from DB
                String name  = rs.getString("addon_name");    // addon name from DB
                // create new addon object
                addOn newAddon = new addOn(name,price);       
                newAddon.setAddOnID(addonId);
                // Add to item list
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
    // This method gets all items from the database and add them to the arraylist of items
    public ArrayList<Item> getAllItems()
    {
        // Initialize list
        ArrayList<Item> itemsList = new ArrayList<Item>(0);
        Statement stmt = null;
        // MySQL statement   
        try
        { 
            // Statement to retrieve all items
            stmt = (this.conn).createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * from items"); 
            // Go through each item and get the id, ordered, item name, description,
            // price, type and image path from DB.
            while(rs.next()){ 
    	         //Retrieve by column name
    	         int itemId  = rs.getInt("item_id");        // item id
                 int ordered = rs.getInt("ordered");        // # of times ordered
    	         String itemName = rs.getString("item_name"); // item name
    	         String itemDesc = rs.getString("item_desc");  // item desc
    	         double itemPrice = rs.getDouble("item_price");  // item price
                 String itemImgPath = rs.getString("item_image_path"); // image path for item
                 int itemType = rs.getInt("item_type");             // item type 
                 // Create new Item Class with all the values as constructor paramteres
                 Item newItem = new Item(itemId,itemName,itemDesc,itemType,itemPrice,itemImgPath,ordered);
                 // Add the addons for the item
                 addAddonsList(newItem);
                 // Add item to item list
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
    	      
    	   }//end try
         
         
        return itemsList;
    }
    // When order is processed, this method will be called to save/add the sale
    // order to the DB.
    public void addSaleOrder(double total)
    { 
        Statement stmt = null;
        // Create statement to add the order
        try
        { 
            stmt = (this.conn).createStatement(); 
            Calendar cal = Calendar.getInstance();  // Calendar used to make date
            cal.add(Calendar.DATE, 1);              // Date is today
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd"); // Format for date (sql form)
            String formatted = format1.format(cal.getTime());  
            // inserting sale order into DB
            stmt.executeUpdate("INSERT INTO sales (sale_date,sales_total) VALUES('"+formatted+"',"+total+")"); 
             
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
    // Method is used to close SQL connection
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
    // Method is used to add item to the DB: name, description, type, price and image path
    public int addItem(String name,String desc,int type,double price,String image_path)
    {
        Statement stmt = null;
        int itemId = 0;
        // Insert all the attributes of items into the database
        try
        { 
            stmt = (this.conn).createStatement();  
            // Insert query
            String query = "INSERT INTO items (item_name,item_desc,item_type,item_price,item_image_path) VALUES('"+name+"','"+desc+"',"+type+","+price+",'"+image_path+"')";
            // Prepare statement to run and retrieve the primary key id and assign it to pInsertOid
            PreparedStatement pInsertOid = (this.conn).prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pInsertOid.executeUpdate();
            // Gets generated keys (primary key)
            ResultSet rs = pInsertOid.getGeneratedKeys();
            // Assign primary key to item id
            if (rs.next()) { 
              itemId = rs.getInt(1);
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
        
            return itemId;
    }
    // Method adds the addon to the DB: name, price and item id
    public int addAddon(String name, double price, int itemId)
    {
        Statement stmt = null;
        int addonId = 0; // default addon id
        // Inserts addon into database based on parameter values
        try
        { 
            stmt = (this.conn).createStatement();  
            // Insert query
            String query = "INSERT INTO addon (addon_name,addon_price,item_id) VALUES('"+name+"',"+price+","+itemId+")";
            System.out.print(query);
            // Prepare statement to run and retrieve the primary key id and assign it to pInsertOid
            PreparedStatement pInsertOid = (this.conn).prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pInsertOid.executeUpdate();
            // Gets generated keys (primary key)
            ResultSet rs = pInsertOid.getGeneratedKeys();
            // Assign primary key to addonId
            if (rs.next()) { 
              addonId = rs.getInt(1);
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
        
            return addonId;
    }
    // Method deletes the addon based on addon ID
    public void deleteAddon(int addonId)
    { 
        Statement stmt = null;
        // create statement to delete addon by id
        try
        { 
            stmt = (this.conn).createStatement(); 
            // Query executing to delete addon by addon_id 
            stmt.executeUpdate("Delete from addon where addon_id="+addonId); 
             
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
    // Save all the items in the item list.. Goes through entire item list
    // and sets the DB column values to all the attributes on that list.
    // If item has attributed deleted set to true, it will delete the item
    // from DB
    public void saveAllItems(ArrayList<Item> itemsList) {
        Statement stmt = null;

        // Going through item list
        for(Item item : itemsList)
        {
            int itemId = item.getItemID();      // item id
            String itemName = item.getName();   // item name
            double itemPrice = item.getPrice(); // item price
            String itemDesc = item.getDescription(); // item description
            String itemImagePath = item.getImagePath(); // item image path
            boolean isDeleted = item.isDeleted();   // checks if item is deleted 
            int numTimesOrdered = item.getQuantityOrdered(); // num times
                                                             // item ordered
            
            // Gets item type
            int itemType = item.getType();
            // Checks if item is deleted (so deletes it), otherwise it
            // updates each item attribute to the corresponding columns
            // in the database based on item id
            try 
            { 
                stmt = (this.conn).createStatement();  
                
                // Check if item has been marked for deletion
                if (isDeleted)
                {
                    // Delete addons first (that match item id), then delete item 
                    stmt.executeUpdate("DELETE from addon WHERE item_id="+itemId); 
                    stmt.executeUpdate("DELETE from items WHERE item_id="+itemId);  
                }
                else
                {
                    // Update each item and set attributes of item class to corresponding columns in
                    // DB based on the item id
                    stmt.executeUpdate("UPDATE items SET item_name='"+itemName+"',item_price="+itemPrice+",item_desc='"+itemDesc+"',item_type="+itemType+",item_image_path='"+itemImagePath+"',ordered="+numTimesOrdered+" WHERE item_id="+itemId); 

                    // Go through each addon and update the DB based on the corresponding attribute
                    // of addons
                    for(addOn currentAddon: item.getAddOnList())
                    {
                        int addonId = currentAddon.getAddOnID();
                        String addonName = currentAddon.getName();
                        double addonPrice = currentAddon.getPrice();
                        stmt.executeUpdate("UPDATE addon SET addon_name='"+addonName+"',addon_price="+addonPrice+" WHERE addon_id="+addonId);
                    } 
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
    }
    // This method gets all the sales of that have been processed
    // It puts them all into a sales arraylist.
    public ArrayList<Sales> getAllSales()
    { 
        // Initialize list
        ArrayList<Sales> salesList = new ArrayList<Sales>();
        Statement stmt = null;
        // Run a query to select the sale id, total and sale date
        try
        { 
            stmt = (this.conn).createStatement();  
            // Run select query to get all the sales
            ResultSet rs = stmt.executeQuery("SELECT sale_id,sales_total,DATE_FORMAT(sale_date,'%m/%d/%Y') as sale_date from sales"); 
            
            // For each sale, create a new object from Sales class with the total sales and sale date
            // as parameters. Then, add the sale to the list of sales
            while(rs.next()){ 
    	         //Retrieve by column name
    	         int salelId  = rs.getInt("sale_id");       // sale id
                 double totalSales = rs.getDouble("sales_total"); // total sales
                 String saleDate = rs.getString("sale_date"); // sales date
    	         
                 // Create new Sales object
                 Sales sale = new Sales(totalSales,saleDate);
                 // Add to list of sales
                 salesList.add(sale);
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
    	       
    	   }//end try
         
         
        return salesList;
    }
}