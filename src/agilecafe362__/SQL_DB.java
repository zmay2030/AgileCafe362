/*
 * SQL CLASS
 */
package agilecafe362__;
import java.sql.*; 
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

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
                 int ordered = rs.getInt("ordered");
    	         String itemName = rs.getString("item_name"); 
    	         String itemDesc = rs.getString("item_desc"); 
    	         double itemPrice = rs.getDouble("item_price"); 
                 String itemImgPath = rs.getString("item_image_path");
                 int itemType = rs.getInt("item_type");
                 System.out.println(itemName+"\n");
                 // Create new Item Class
                 Item newItem = new Item(itemId,itemName,itemDesc,itemType,itemPrice,itemImgPath,ordered);
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
    public void addSaleOrder(double total)
    {
        System.out.print("ORDERED PROCSSED SQL "+total);
        Statement stmt = null;
        try
        { 
            stmt = (this.conn).createStatement(); 
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, 1);
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
            String formatted = format1.format(cal.getTime());
            System.out.println(formatted);
             
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
    public int addItem(String name,String desc,int type,double price,String image_path)
    {
        Statement stmt = null;
        int itemId = 0;
        try
        { 
            stmt = (this.conn).createStatement();  
            String query = "INSERT INTO items (item_name,item_desc,item_type,item_price,item_image_path) VALUES('"+name+"','"+desc+"',"+type+","+price+",'"+image_path+"')";
            System.out.print(query);
            PreparedStatement pInsertOid = (this.conn).prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pInsertOid.executeUpdate();
            ResultSet rs = pInsertOid.getGeneratedKeys();
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
    public int addAddon(String name, double price, int itemId)
    {
        Statement stmt = null;
        try
        { 
            stmt = (this.conn).createStatement();  
            String query = "INSERT INTO addon (addon_name,addon_price,item_id) VALUES('"+name+"',"+price+","+itemId+")";
            System.out.print(query);
            PreparedStatement pInsertOid = (this.conn).prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pInsertOid.executeUpdate();
            ResultSet rs = pInsertOid.getGeneratedKeys();
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
    public void deleteAddon(int addonId)
    { 
        Statement stmt = null;
        try
        { 
            stmt = (this.conn).createStatement(); 
             
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
}