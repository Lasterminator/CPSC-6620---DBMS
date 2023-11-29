package cpsc4620;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/*
 * This file is where most of your code changes will occur You will write the code to retrieve
 * information from the database, or save information to the database
 * 
 * The class has several hard coded static variables used for the connection, you will need to
 * change those to your connection information
 * 
 * This class also has static string variables for pickup, delivery and dine-in. If your database
 * stores the strings differently (i.e "pick-up" vs "pickup") changing these static variables will
 * ensure that the comparison is checking for the right string in other places in the program. You
 * will also need to use these strings if you store this as boolean fields or an integer.
 * 
 * 
 */

/**
 * A utility class to help add and retrieve information from the database
 */

public final class DBNinja {
    private static Connection conn;

    // Change these variables to however you record dine-in, pick-up and delivery, and sizes and crusts
    public final static String pickup = "PickUp";
    public final static String delivery = "Delivery";
    public final static String dine_in = "DineIn";

    public final static String size_s = "small";
    public final static String size_m = "medium";
    public final static String size_l = "large";
    public final static String size_xl = "x-large";

    public final static String crust_thin = "Thin";
    public final static String crust_orig = "Original";
    public final static String crust_pan = "Pan";
    public final static String crust_gf = "Gluten-Free";


    private static boolean connect_to_db() throws SQLException, IOException {

        try {
            conn = DBConnector.make_connection();
            return true;
        } catch (SQLException e) {
            return false;
        } catch (IOException e) {
            return false;
        }

    }


    public static void addOrder(Order o) throws SQLException, IOException {
        connect_to_db();
        /*
		 * add code to add the order to the DB. Remember that we're not just
		 * adding the order to the order DB table, but we're also recording
		 * the necessary data for the delivery, dinein, and pickup tables
		 * 
		 */
        String[] generatedId = { "ID" };

        String insertStatement = "INSERT INTO `Order`"
                + "(OrderCost,OrderType,OrderTime,OrderPrice,OrderCustomerId) " + "VALUES ("
                + o.getBusPrice() + ",'" + o.getOrderType() + "','" + o.getDate() + "',"+ o.getCustPrice()+"," +
                o.getCustID() + ")";
        PreparedStatement preparedStatement = conn.prepareStatement(insertStatement, generatedId);
        int result = preparedStatement.executeUpdate();
        try {
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                o.setOrderID(resultSet.getInt(1));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            conn.close();
        }

        if(o instanceof DineinOrder){
            addDineIn((DineinOrder)o);
        } else if(o instanceof PickupOrder){
            addPickUp((PickupOrder) o);
        } else if(o instanceof DeliveryOrder) {
            addDelivery((DeliveryOrder) o);
        }else{
            System.out.println("Invalid order type");
        }

        for(Pizza p : o.getPizzaList()) {
            p.setOrderID(o.getOrderID());
            addPizza(p);
        }
        for(Discount d : o.getDiscountList())
            useOrderDiscount(o, d);
        //DO NOT FORGET TO CLOSE YOUR CONNECTION
    }

    public static void addPizza(Pizza p) throws SQLException, IOException {
        connect_to_db();
        /*
		 * Add the code needed to insert the pizza into into the database.
		 * Keep in mind adding pizza discounts and toppings associated with the pizza,
		 * there are other methods below that may help with that process.
		 * 
		 */
        String[] generatedId = { "ID" };

        String insertStatement = "insert into Pizza(PizzaOrderId,PizzaSize,PizzaCrust,PizzaCost,PizzaPrice,PizzaTime) "
                + "values (" + p.getOrderID() + ",'" + p.getSize() + "','" + p.getCrustType()
                + "',"  + p.getBusPrice() + "," + p.getCustPrice() + ",CURRENT_TIMESTAMP )";
        PreparedStatement preparedStatement = conn.prepareStatement(insertStatement, generatedId);

        int result = preparedStatement.executeUpdate();
        System.out.println(result);
        if (result > 0) {
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                p.setPizzaID(resultSet.getInt(1));
            }
        }
        int idx=0;
        for(Topping t : p.getToppings()) {
            useTopping(p, t, p.getIsDoubleArray()[idx++]);
        }
        for(Discount pizzaDiscount : p.getDiscounts())
            usePizzaDiscount(p, pizzaDiscount);
        //DO NOT FORGET TO CLOSE YOUR CONNECTION
    }

    public static int getMaxPizzaID() throws SQLException, IOException {
        connect_to_db();
        /*
         * A function I needed because I forgot to make my pizzas auto increment in my DB.
         * It goes and fetches the largest PizzaID in the pizza table.
         * You wont need to implement this function if you didn't forget to do that
         */


        //DO NOT FORGET TO CLOSE YOUR CONNECTION
        return -1;
    }

    public static void useTopping(Pizza p, Topping t, boolean isDoubled) throws SQLException, IOException //this function will update toppings inventory in SQL and add entities to the Pizzatops table. Pass in the p pizza that is using t topping
    {
        connect_to_db();
        /*
		 * This method should do 2 two things.
		 * - update the topping inventory every time we use t topping (accounting for extra toppings as well)
		 * - connect the topping to the pizza
		 *   What that means will be specific to your yimplementatinon.
		 * 
		 * Ideally, you should't let toppings go negative....but this should be dealt with BEFORE calling this method.
		 * 
		 */
        try {
            connect_to_db();
            if((isDoubled && t.getCurINVT()<2) &&(!isDoubled && t.getCurINVT()<1)) {
                System.out.println("Sorry! We have run out of Topping.");
                throw new IOException("Sorry! We have run out of Topping.");
            }
            double updatedInventory = t.getCurINVT();
            if(isDoubled)updatedInventory-= 2;
            else updatedInventory-= 1;
            String updateStatement = "update `Topping` set ToppingCurrentInventory="+updatedInventory+ "where ToppingId="+t.getTopID()+";";

            PreparedStatement preparedStatement= conn.prepareStatement(updateStatement);
            preparedStatement.executeUpdate();

            String insertStatement = "insert into `PizzaTopping`(PizzaToppingPizzaId,PizzaToppingToppingId,PizzaToppingExtraTopping)" + "values" + "("
                    + p.getPizzaID() + "," + t.getTopID() + ","+isDoubled+");";

            PreparedStatement preparedStatement2= conn.prepareStatement(insertStatement);
            preparedStatement2.executeUpdate();


        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            conn.close();
        }

        //DO NOT FORGET TO CLOSE YOUR CONNECTION
    }


    public static void usePizzaDiscount(Pizza p, Discount d) throws SQLException, IOException {
        connect_to_db();
        /*
		 * This method connects a discount with a Pizza in the database.
		 * 
		 * What that means will be specific to your implementatinon.
		 */
		
        String insertStatement = "insert into `PizzaDiscount`(PizzaDiscountPizzaID,PizzaDiscountDiscountID)" + "values" + "(" + p.getPizzaID() + ","
                + d.getDiscountID() + ")";

        PreparedStatement preparedStatement;
        try {
            connect_to_db();
            preparedStatement = conn.prepareStatement(insertStatement);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.close();
        }
        //DO NOT FORGET TO CLOSE YOUR CONNECTION
    }

    public static void useOrderDiscount(Order o, Discount d) throws SQLException, IOException {
        connect_to_db();
        /*
		 * This method connects a discount with an order in the database
		 * 
		 * You might use this, you might not depending on where / how to want to update
		 * this information in the dabast
		 */
		
        String insertStatement = "insert into `OrderDiscount`(OrderDiscountOrderId,OrderDiscountDiscountId)" + "values" + "(" + o.getOrderID() + ","
                + d.getDiscountID() + ")";

        PreparedStatement preparedStatement;
        try {
            connect_to_db();
            preparedStatement = conn.prepareStatement(insertStatement);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.close();
        }

        //DO NOT FORGET TO CLOSE YOUR CONNECTION
    }

    public static void addDineIn(DineinOrder d) {
        try {
            String insertStatement = "INSERT INTO `DineInOrder`" + "(DineInOrderID,DineInOrderTableNum) " + "VALUES (?, ?)";

            connect_to_db();

            PreparedStatement preparedStatement = conn.prepareStatement(insertStatement);

            preparedStatement.setInt(1, d.getOrderID());
            preparedStatement.setInt(2, d.getTableNum());
            preparedStatement.executeUpdate();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                conn.close();
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public static void addDelivery(DeliveryOrder d) throws SQLException {
        try {
            String insertStatement = "INSERT INTO `DeliveryOrder`" + "(DeliveryOrderID, DeliveryOrderAddress) " + "VALUES (?, ?)";

            connect_to_db();

            PreparedStatement preparedStatement = conn.prepareStatement(insertStatement);

            preparedStatement.setInt(1, d.getOrderID());
            preparedStatement.setString(2, d.getAddress());
            preparedStatement.executeUpdate();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            conn.close();
        }

    }

    public static void addPickUp(PickupOrder p ) throws SQLException {

        try {
            connect_to_db();
            String insertStatement = "INSERT INTO `PickUpOrder`" + "(PickUpOrderID) " + "VALUES (?)";

            connect_to_db();

            PreparedStatement preparedStatement = conn.prepareStatement(insertStatement);

            preparedStatement.setInt(1, p.getOrderID());
            preparedStatement.executeUpdate();
            conn.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            conn.close();
        }

    }

    public static Integer addCustomer(Customer c) throws SQLException, IOException {
        connect_to_db();
        /*
		 * This method adds a new customer to the database.
		 * 
		 */

        String insertStatement = "INSERT INTO `Customer`(CustomerFName,CustomerLName,CustomerPhone) VALUES (?,?,?)";
        String[] generatedId = { "ID" };
        PreparedStatement preparedStatement = conn.prepareStatement(insertStatement, generatedId);
        preparedStatement.setString(1, c.getFName());
        preparedStatement.setString(2, c.getLName());
        preparedStatement.setString(3, c.getPhone());

        int result = preparedStatement.executeUpdate();

        if (result > 0) {
            try {
                ResultSet rs = preparedStatement.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            } catch (Exception e) {
                throw e;
            } finally {
                conn.close();
            }
        }

        return null;
        //DO NOT FORGET TO CLOSE YOUR CONNECTION
    }


    public static void completeOrder(Order o) throws SQLException, IOException {
        connect_to_db();
        /*
		 * Find the specifed order in the database and mark that order as complete in the database.
		 * 
		 */
        try {
            connect_to_db();

            String updatePizzaStatement = "update `Order` set OrderStatus = 1 where OrderId = " + o.getOrderID() + ";";

            PreparedStatement pizzaPreparedStatement = conn.prepareStatement(updatePizzaStatement);

            pizzaPreparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.close();
        }
        //DO NOT FORGET TO CLOSE YOUR CONNECTION
    }

    public static void printInventory() throws SQLException, IOException {
        connect_to_db();

        /*
		 * Queries the database and prints the current topping list with quantities.
		 *  
		 * The result should be readable and sorted as indicated in the prompt.
		 * 
		 */


        //DO NOT FORGET TO CLOSE YOUR CONNECTION
    }

    public static ArrayList<Order> getOrders1(boolean openOnly) throws SQLException, IOException {
        connect_to_db(); // Ensure database connection is established

        ArrayList<Order> orders = new ArrayList<>();

        String selectQuery = openOnly ? "select * from `Order` where OrderStatus = 1 order by OrderId desc;"
                : "select * from `Order` where OrderStatus = 0 order by OrderId desc;";

        try (Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery(selectQuery)) {

            while (resultSet.next()) {
                Integer orderId = resultSet.getInt("OrderID");
                String orderType = resultSet.getString("OrderType");
                Integer customerId = resultSet.getInt("OrderCustomerID");
                Double orderCost = resultSet.getDouble("OrderCost");
                Double orderPrice = resultSet.getDouble("OrderPrice");
                String orderTime = resultSet.getString("OrderTime");
                int isComplete = resultSet.getInt("OrderStatus");

                orders.add(new Order(orderId, customerId, orderType, orderTime, orderCost, orderPrice, isComplete));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            conn.close(); // Ensure connection is closed
        }

        return orders;
    }

    public static ArrayList<Order> getOrders(boolean openOnly) throws SQLException, IOException {
        connect_to_db(); // Ensure database connection is established

        /*
		 * Return an arraylist of all of the orders.
		 * 	openOnly == true => only return a list of open (ie orders that have not been marked as completed)
		 *           == false => return a list of all the orders in the database
		 * Remember that in Java, we account for supertypes and subtypes
		 * which means that when we create an arrayList of orders, that really
		 * means we have an arrayList of dineinOrders, deliveryOrders, and pickupOrders.
		 * 
		 * Don't forget to order the data coming from the database appropriately.
		 * 
		 */

        ArrayList<Order> orders = new ArrayList<>();

        String selectQuery = openOnly ? "select * from `Order` where OrderStatus = 0 order by OrderId desc;"
                : "select * from `Order` order by OrderId desc;";

        try (Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery(selectQuery)) {

            while (resultSet.next()) {
                Integer orderId = resultSet.getInt("OrderID");
                String orderType = resultSet.getString("OrderType");
                Integer customerId = resultSet.getInt("OrderCustomerID");
                Double orderCost = resultSet.getDouble("OrderCost");
                Double orderPrice = resultSet.getDouble("OrderPrice");
                String orderTime = resultSet.getString("OrderTime");
                int isComplete = resultSet.getInt("OrderStatus");

                orders.add(new Order(orderId, customerId, orderType, orderTime, orderCost, orderPrice, isComplete));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            conn.close(); // Ensure connection is closed
        }

        return orders;
    }

    public static ArrayList<Order> getOrdersByDate(String date) throws SQLException, IOException{
        connect_to_db();
        /*
		 * Query the database for ALL the orders placed on a specific date
		 * and return a list of those orders.
		 *  
		 */
        ArrayList<Order> orders = new ArrayList<Order>();

        try {
            connect_to_db();

            String selectQuery = "select * from `Order` where (OrderTime >= '" + date + " 00:00:00')" +
                    " order by OrderId desc;";

            Statement statement = conn.createStatement();

            ResultSet resultSet = statement.executeQuery(selectQuery);

            while (resultSet.next()) {
                Integer orderId = resultSet.getInt("OrderId");
                String orderType = resultSet.getString("OrderType");
                Integer customerId = resultSet.getInt("OrderCustomerID");
                Double orderCost = resultSet.getDouble("OrderCost");
                Double orderPrice = resultSet.getDouble("OrderPrice");
                String orderTime = resultSet.getString("OrderTime");
                int isComplete = resultSet.getInt("OrderStatus");
                orders.add(new Order(orderId, customerId, orderType, orderTime, orderCost, orderPrice, isComplete));

            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            conn.close();
        }

        return orders;

        //DO NOT FORGET TO CLOSE YOUR CONNECTION
    }

    public static Order getLastOrder()  throws SQLException, IOException{
        connect_to_db();
        /*
		 * Query the database for the LAST order added
		 * then return an Order object for that order.
		 * NOTE...there should ALWAYS be a "last order"!
		 */
		

        Order lastOrder = null;
        String selectQuery = "SELECT * FROM `Order` ORDER BY OrderId DESC LIMIT 1;"; // Query to get the last order

        try (Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery(selectQuery)) {

            if (resultSet.next()) { // Check if there is an order
                Integer orderId = resultSet.getInt("OrderId");
                String orderType = resultSet.getString("OrderType");
                Integer customerId = resultSet.getInt("CustomerId");
                Double orderCost = resultSet.getDouble("OrderCost");
                Double orderPrice = resultSet.getDouble("OrderPrice");
                String orderTime = resultSet.getString("OrderTime");
                int isComplete = resultSet.getInt("IsComplete");

                lastOrder = new Order(orderId, customerId, orderType, orderTime, orderCost, orderPrice, isComplete);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions
        } finally {
            try {
                if (conn != null) conn.close(); // Close connection
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return lastOrder;
    }

    public static Discount findDiscountByName(String name) throws SQLException, IOException{
        /*
		 * Query the database for a discount using it's name.
		 * If found, then return an OrderDiscount object for the discount.
		 * If it's not found....then return null
		 *  
		 */

        connect_to_db();

        // Use a prepared statement to prevent SQL injection
        String selectQuery = "SELECT * FROM Discount WHERE DiscountName = ?";

        try (PreparedStatement preparedStatement = conn.prepareStatement(selectQuery)) {
            preparedStatement.setString(1, name); // Set the name parameter

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Integer discountId = resultSet.getInt("DiscountId");
                    String discountName = resultSet.getString("DiscountName");
                    Boolean isPercentDiscount = resultSet.getBoolean("IsPercent");
                    Double discountAmount = resultSet.getDouble("DiscountAmount");

                    return new Discount(discountId, discountName, discountAmount, isPercentDiscount);
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions
        } finally {
            try {
                if (conn != null) conn.close(); // Close connection
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return null;

    }

    public static Customer findCustomerByPhone(String phoneNumber) throws SQLException, IOException{
        /*
		 * Query the database for a customer using a phone number.
		 * If found, then return a Customer object for the customer.
		 * If it's not found....then return null
		 *  
		 */

        connect_to_db();

        // Use a prepared statement to prevent SQL injection
        String selectQuery = "SELECT * FROM Customer WHERE CustomerPhone = ?";

        try (PreparedStatement preparedStatement = conn.prepareStatement(selectQuery)) {
            preparedStatement.setString(1, phoneNumber); // Set the name parameter

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Integer customerId = resultSet.getInt("CustomerID");
                    String customerFName = resultSet.getString("CustomerFName");
                    String customerLName = resultSet.getString("CustomerLName");
                    String customerPhone = resultSet.getString("CustomerPhone");

                    return new Customer(customerId, customerFName, customerLName, customerPhone);
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions
        } finally {
            try {
                if (conn != null) conn.close(); // Close connection
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    public static ArrayList<Topping> getToppingList() throws SQLException, IOException {
        connect_to_db();
        /*
		 * Query the database for the aviable toppings and 
		 * return an arrayList of all the available toppings. 
		 * Don't forget to order the data coming from the database appropriately.
		 * 
		 */
        ArrayList<Topping> toppings = new ArrayList<>();

        // Query the database
        String selectQuery = "SELECT * FROM Topping ORDER BY ToppingID";

        try (Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery(selectQuery)) {

            while (resultSet.next()) {
                Integer toppingId = resultSet.getInt("ToppingID");
                String toppingName = resultSet.getString("ToppingName");
                Integer minInventoryLevel = resultSet.getInt("ToppingMinInventory");
                Integer currentInventoryLevel = resultSet.getInt("ToppingCurrentInventory");
                Double busPrice = resultSet.getDouble("ToppingCost");
                Double custPrice = resultSet.getDouble("ToppingPrice");
                Double perAmt = resultSet.getDouble("ToppingSmall");
                Double medAmt = resultSet.getDouble("ToppingMedium");
                Double lgAmt = resultSet.getDouble("ToppingLarge");
                Double xlAmt = resultSet.getDouble("ToppingXL");

                Topping t = new Topping(toppingId, toppingName, perAmt, medAmt, lgAmt, xlAmt, custPrice, busPrice, minInventoryLevel, currentInventoryLevel);
                toppings.add(t);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions
        } finally {
            try {
                if (conn != null) conn.close(); // Close connection
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return toppings;
    }


    public static Topping findToppingByName(String name) throws SQLException, IOException{
        connect_to_db();
        /*
		 * Query the database for the topping using it's name.
		 * If found, then return a Topping object for the topping.
		 * If it's not found....then return null
		 *  
		 */

        ArrayList<Topping> toppings = new ArrayList<>();
        

        // Query the database
        String selectQuery = "SELECT * FROM Topping where ToppingName = ?";

        try (PreparedStatement preparedStatement = conn.prepareStatement(selectQuery)) {
            preparedStatement.setString(1, name); // Set the name parameter

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Integer toppingId = resultSet.getInt("ToppingId");
                    String toppingName = resultSet.getString("ToppingName");
                    Integer minInventoryLevel = resultSet.getInt("ToppingMinInventory");
                    Integer currentInventoryLevel = resultSet.getInt("ToppingCurrentInventory");
                    Double busPrice = resultSet.getDouble("ToppingCost");
                    Double custPrice = resultSet.getDouble("ToppingPrice");
                    Double perAmt = resultSet.getDouble("ToppingSmall");
                    Double medAmt = resultSet.getDouble("ToppingMedium");
                    Double lgAmt = resultSet.getDouble("ToppingLarge");
                    Double xlAmt = resultSet.getDouble("ToppingXL");

                    return new Topping(toppingId, toppingName, perAmt, medAmt, lgAmt, xlAmt, custPrice, busPrice, minInventoryLevel, currentInventoryLevel);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exceptions
        } finally {
            try {
                if (conn != null) conn.close(); // Close connection
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return null;
    }

    public static void addToInventory(Topping t, double quantity) throws SQLException, IOException {
        connect_to_db();

        String updateQuery = "UPDATE Topping SET ToppingCurrentInventory = ToppingCurrentInventory + ? WHERE ToppingID = ?";

        try (PreparedStatement preparedStatement = conn.prepareStatement(updateQuery)) {
            preparedStatement.setDouble(1, quantity);
            preparedStatement.setInt(2, t.getTopID());

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Updating inventory failed, no rows affected.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        //DO NOT FORGET TO CLOSE YOUR CONNECTION
    }

    public static boolean checkDate(int year, int month, int day, String dateOfOrder)
	{
		if(getYear(dateOfOrder) > year)
			return true;
		else if(getYear(dateOfOrder) < year)
			return false;
		else
		{
			if(getMonth(dateOfOrder) > month)
				return true;
			else if(getMonth(dateOfOrder) < month)
				return false;
			else
			{
				if(getDay(dateOfOrder) >= day)
					return true;
				else
					return false;
			}
		}
	}


    /*
	 * The next 3 private methods help get the individual components of a SQL datetime object. 
	 * You're welcome to keep them or remove them.
	 */
    private static int getYear(String date)// assumes date format 'YYYY-MM-DD HH:mm:ss'
    {
        return Integer.parseInt(date.substring(0,4));
    }

    private static int getMonth(String date)// assumes date format 'YYYY-MM-DD HH:mm:ss'
    {
        return Integer.parseInt(date.substring(5, 7));
    }

    private static int getDay(String date)// assumes date format 'YYYY-MM-DD HH:mm:ss'
    {
        return Integer.parseInt(date.substring(8, 10));
    }


    public static double getBaseCustPrice(String size, String crust) throws SQLException, IOException {
        connect_to_db();
        /* 
		 * Query the database fro the base customer price for that size and crust pizza.
		 * 
		*/
        double bp = 0.0;
        try (Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery("select * from `Base` where BasePizzaSize='"+size+"' and BasePizzaCrust='"+crust+"';")) {

            while (resultSet.next()) {
                bp = resultSet.getDouble("BasePizzaPrice");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            conn.close();
        }
        //DO NOT FORGET TO CLOSE YOUR CONNECTION
        return bp;
    }

    public static String getCustomerName(int CustID) throws SQLException, IOException
    {
        /*
		 * This is a helper method to fetch and format the name of a customer
		 * based on a customer ID. This is an example of how to interact with 
		 * your database from Java.  It's used in the model solution for this project...so the code works!
		 * 
		 * OF COURSE....this code would only work in your application if the table & field names match!
		 *
		 */

        connect_to_db();

        /* 
		 * an example query using a constructed string...
		 * remember, this style of query construction could be subject to sql injection attacks!
		 * 
		 */
        String cname1 = "";
        String query = "Select CustomerFName, CustomerLName From Customer WHERE CustomerID=" + CustID + ";";
        Statement stmt = conn.createStatement();
        ResultSet rset = stmt.executeQuery(query);

        while(rset.next())
        {
            cname1 = rset.getString(1) + " " + rset.getString(2);
        }

        /* 
		* an example of the same query using a prepared statement...
		* 
		*/
        String cname2 = "";
        PreparedStatement os;
        ResultSet rset2;
        String query2;
        query2 = "Select CustomerFName, CustomerLName From Customer WHERE CustomerID=?;";
        os = conn.prepareStatement(query2);
        os.setInt(1, CustID);
        rset2 = os.executeQuery();
        while(rset2.next())
        {
            cname2 = rset2.getString("CustomerFName") + " " + rset2.getString("CustomerLName"); // note the use of field names in the getSting methods
        }

        conn.close();
        return cname1; // OR cname2
    }

    public static double getBaseBusPrice(String size, String crust) throws SQLException, IOException {
        connect_to_db();
        /* 
		 * Query the database fro the base business price for that size and crust pizza.
		 * 
		*/
		
        double bp = 0.0;
        try (Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery("select * from `Base` where BasePizzaSize='"+size+"' and BasePizzaCrust='"+crust+"';")) {

            while (resultSet.next()) {

                bp = resultSet.getDouble("BasePizzaCost");

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            conn.close();
        }
        //DO NOT FORGET TO CLOSE YOUR CONNECTION
        return bp;
    }


    public static ArrayList<Discount> getDiscountList() throws SQLException, IOException {
        connect_to_db();
        /* 
		 * Query the database for all the available discounts and 
		 * return them in an arrayList of discounts.
		 * 
		*/
        ArrayList<Discount> discs = new ArrayList<Discount>();
        
        //returns a list of all the discounts.
        try (Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery("select * from `Discount`;")) {

            while (resultSet.next()) {
                Integer discountId = resultSet.getInt("DiscountId");
                String discountName = resultSet.getString("DiscountName");
                Boolean isPercentDiscount = resultSet.getBoolean("IsPercent");
                Double discountAmount = resultSet.getDouble("DiscountAmount");

                discs.add(new Discount(discountId, discountName, discountAmount, isPercentDiscount));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            conn.close();
        }
        //DO NOT FORGET TO CLOSE YOUR CONNECTION
        return discs;
    }


    public static ArrayList<Customer> getCustomerList() throws SQLException, IOException {
        connect_to_db();
        /*
		 * Query the data for all the customers and return an arrayList of all the customers. 
		 * Don't forget to order the data coming from the database appropriately.
		 * 
		*/
        ArrayList<Customer> custs = new ArrayList<Customer>();
        try (Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery("select * from `Customer` where CustomerID != 1 ORDER BY CustomerID ASC;")) {

            while (resultSet.next()) {
                Integer customerId = resultSet.getInt("CustomerId");
                String customerFName = resultSet.getString("CustomerFName");
                String customerLName = resultSet.getString("CustomerLName");
                String customerPhone = resultSet.getString("CustomerPhone");
                custs.add(new Customer(customerId, customerFName, customerLName, customerPhone));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            conn.close();
        }

        return custs;
        //DO NOT FORGET TO CLOSE YOUR CONNECTION
    }

    public static int getNextOrderID() throws SQLException, IOException {
        /*
         * A helper function I had to use because I forgot to make
         * my OrderID auto increment...You can remove it if you
         * did not forget to auto increment your orderID.
         */


        //DO NOT FORGET TO CLOSE YOUR CONNECTION
        return -1;
    }

    public static void printToppingPopReport() throws SQLException, IOException {
        connect_to_db();
        /*
		 * Prints the ToppingPopularity view. Remember that this view
		 * needs to exist in your DB, so be sure you've run your createViews.sql
		 * files on your testing DB if you haven't already.
		 * 
		 * The result should be readable and sorted as indicated in the prompt.
		 * 
		 */
        try {
            connect_to_db();

            Statement statement = conn.createStatement();
            String query = "SELECT * FROM ToppingPopularity;";
            ResultSet resultSet = statement.executeQuery(query);
            System.out.printf("%-20s | %-4s |%n", "Topping", "ToppingCount");
            System.out.printf("-------------------------------------\n");
            while (resultSet.next()) {

                Integer count = resultSet.getInt("ToppingCount");
                String topping = resultSet.getString("Topping");
                System.out.printf("%-20s | %-4s |%n", topping, count);

            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                conn.close();
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }

        //DO NOT FORGET TO CLOSE YOUR CONNECTION
    }

    public static void printProfitByPizzaReport() throws SQLException, IOException {
        connect_to_db();
        /*
		 * Prints the ProfitByPizza view. Remember that this view
		 * needs to exist in your DB, so be sure you've run your createViews.sql
		 * files on your testing DB if you haven't already.
		 * 
		 * The result should be readable and sorted as indicated in the prompt.
		 * 
		 */
        try {
            connect_to_db();

            Statement statement = conn.createStatement();
            String query = "SELECT * FROM ProfitByPizza;";
            ResultSet resultSet = statement.executeQuery(query);
            System.out.printf("%-15s | %-15s | %-10s| %-30s%n", "Pizza Size", "Pizza Crust", "Profit", "Last Order Date");
            System.out.printf("-----------------------------------------------------------------\n");
            while (resultSet.next()) {

                String size = resultSet.getString("Size");
                String crust = resultSet.getString("Crust");
                String lastOrderDate = resultSet.getString("Order Month");
                Double profit = resultSet.getDouble("Profit");
                System.out.printf("%-15s | %-15s | %-10s| %-30s%n", size, crust, profit, lastOrderDate);

            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                conn.close();
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }

        //DO NOT FORGET TO CLOSE YOUR CONNECTION
    }

    public static void printProfitByOrderType() throws SQLException, IOException {
        connect_to_db();
        /*
		 * Prints the ProfitByOrderType view. Remember that this view
		 * needs to exist in your DB, so be sure you've run your createViews.sql
		 * files on your testing DB if you haven't already.
		 * 
		 * The result should be readable and sorted as indicated in the prompt.
		 * 
		 */
        try {
            connect_to_db();

            Statement statement = conn.createStatement();
            String query = "SELECT * FROM ProfitByOrderType;";
            ResultSet resultSet = statement.executeQuery(query);
            System.out.printf("%-15s | %-15s | %-18s| %-18s| %-8s%n", "Customer Type", "Order Month", "Total Order Price",
                    "Total Order Cost", "Profit");
            System.out.printf("-----------------------------------------------------------------------------------\n");
            while (resultSet.next()) {

                String customerType = resultSet.getString("customerType");
                String orderMonth = resultSet.getString("Order Month");
                Double totalOrderPrice = resultSet.getDouble("TotalOrderPrice");
                Double totalOrderCost = resultSet.getDouble("TotalOrderCost");
                Double profit = resultSet.getDouble("Profit");
                System.out.printf("%-15s | %-15s | %-18s| %-18s| %-8s%n", customerType, orderMonth, totalOrderPrice,
                        totalOrderCost, profit);

            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                conn.close();
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }

        //DO NOT FORGET TO CLOSE YOUR CONNECTION
    }
}