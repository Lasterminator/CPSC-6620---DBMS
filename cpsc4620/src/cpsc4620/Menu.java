package cpsc4620;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/*
 * This file is where the front end magic happens.
 *
 * You will have to write the methods for each of the menu options.
 *
 * This file should not need to access your DB at all, it should make calls to the DBNinja that will do all the connections.
 *
 * You can add and remove methods as you see necessary. But you MUST have all of the menu methods (including exit!)
 *
 * Simply removing menu methods because you don't know how to implement it will result in a major error penalty (akin to your program crashing)
 *
 * Speaking of crashing. Your program shouldn't do it. Use exceptions, or if statements, or whatever it is you need to do to keep your program from breaking.
 *
 */

public class Menu {

    public static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    public static void main(String[] args) throws SQLException, IOException {
        System.out.println("Welcome to Pizzas-R-Us!");

        int menu_option = 0;

        // present a menu of options and take their selection

        PrintMenu();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String option = reader.readLine();
        menu_option = Integer.parseInt(option);

        while (menu_option != 9) {
            switch (menu_option) {
                case 1:// enter order
                    EnterOrder();
                    break;
                case 2:// view customers
                    viewCustomers();
                    break;
                case 3:// enter customer
                    EnterCustomer();
                    break;
                case 4:// view order
                    // open/closed/date
                    ViewOrders();
                    break;
                case 5:// mark order as complete
                    MarkOrderAsComplete();
                    break;
                case 6:// view inventory levels
                    ViewInventoryLevels();
                    break;
                case 7:// add to inventory
                    AddInventory();
                    break;
                case 8:// view reports
                    PrintReports();
                    break;
            }
            PrintMenu();
            option = reader.readLine();
            menu_option = Integer.parseInt(option);
        }

    }

    // allow for a new order to be placed
    public static void EnterOrder() throws SQLException, IOException
    {
        /*
         * EnterOrder should do the following:
         *
         * Ask if the order is delivery, pickup, or dinein
         *   if dine in....ask for table number
         *   if pickup...
         *   if delivery...
         *
         * Then, build the pizza(s) for the order (there's a method for this)
         *  until there are no more pizzas for the order
         *  add the pizzas to the order
         *
         * Apply order discounts as needed (including to the DB)
         *
         * return to menu
         *
         * make sure you use the prompts below in the correct order!
         */

        Integer custId;
        double custPrice = 0;
        double busPrice = 0;
        String date = getCurrentTime();

        System.out.println("Is this order for: \n1.) Dine-In\n2.) Pick-Up\n3.) Delivery\nEnter the number of your choice:");

        Integer orderTypeInt = Integer.parseInt(reader.readLine());
        Order order = null;
        switch(orderTypeInt) {
            case 1:
                System.out.println("What is the table number for this order?");
                Integer tablenum = Integer.parseInt(reader.readLine());
                order = new DineinOrder(0, 1, date, custPrice, busPrice, 0, tablenum);
                break;
            case 2:
                System.out.println("Is this order for an existing customer? Answer y/n: ");

                if ("y".equalsIgnoreCase(reader.readLine())) {
                    System.out.println("Here's a list of current customers: ");
                    viewCustomers();
                    System.out.println("Which customer is this order for? Enter ID Number");
                    custId = Integer.parseInt(reader.readLine());
                } else {
                    custId = EnterCustomer();
                }

                order = new PickupOrder(0, custId, date, custPrice, busPrice, 0, 0);
                break;
            case 3:
                System.out.println("Is this order for an existing customer? Answer y/n: ");

                if ("y".equalsIgnoreCase(reader.readLine())) {
                    System.out.println("Here's a list of current customers: ");
                    viewCustomers();
                    System.out.println("Which customer is this order for? Enter ID Number");
                    custId = Integer.parseInt(reader.readLine());
                } else {
                    custId = EnterCustomer();
                }

                System.out.println("What is the House/Apt Number for this order? (e.g., 111)");
                String houseNumber = reader.readLine();
                System.out.println("What is the Street for this order? (e.g., Smile Street)");
                String street = reader.readLine();
                System.out.println("What is the City for this order? (e.g., Greenville)");
                String city = reader.readLine();
                System.out.println("What is the State for this order? (e.g., SC)");
                String state = reader.readLine();
                System.out.println("What is the Zip Code for this order? (e.g., 20605)");
                String zipCode = reader.readLine();
                String customerAddress = houseNumber + " " + street + ", " + city + ", " + state + " " + zipCode;
                order = new DeliveryOrder(0, custId, date, custPrice, busPrice, 0, customerAddress);
                break;
        }


        System.out.println("Let's build a pizza!");
        boolean addMorePizza = true;
        while(addMorePizza) {
            Pizza pizza = buildPizza();
            busPrice += pizza.getBusPrice();
            custPrice += pizza.getCustPrice();
            order.addPizza(pizza);
            System.out.println("Enter -1 to stop adding pizzas...Enter anything else to continue adding pizzas to the order.");
            if((Objects.equals(reader.readLine(), "-1"))) addMorePizza = false;
            else addMorePizza = true;
        }

        order.setCustPrice(custPrice);
        order.setBusPrice(busPrice);

        helperOrderDiscount(order);

        DBNinja.addOrder(order);

        System.out.println("Finished adding order...Returning to menu...");
    }

    private static void helperOrderDiscount(Order order) {
        System.out.println("Do you want to add discounts to this order? Enter y/n?");
        try {
            Boolean addorderDiscount = "y".equals(reader.readLine());
            while (addorderDiscount) {

                ArrayList<Discount> discounts = DBNinja.getDiscountList();
                discounts.forEach(d->System.out.println(d.toString()));

                System.out.println("Which Order Discount do you want to add? Enter the DiscountID. Enter -1 to stop adding Discounts: ");

                Integer discountId = Integer.parseInt(reader.readLine());

                if (discountId == -1) break;
                Discount pizzaDiscount = discounts.stream().filter(d->d.getDiscountID()==discountId).findAny().get();
                order.addDiscount(pizzaDiscount);

                System.out.println("Do you want to add another discount : type y/n:"); // need to remove this statement
                if("y".equalsIgnoreCase(reader.readLine())) addorderDiscount = true;
                else addorderDiscount = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void helperPizzaDiscount(Pizza pizza) {
        try{
            System.out.println("Do you want to add discounts to this Pizza? Enter y/n?");
            Boolean addDiscount = "y".equalsIgnoreCase(reader.readLine());
            while (addDiscount) {

                ArrayList<Discount> discounts = DBNinja.getDiscountList();

                for (Discount discount : discounts) {
                    System.out.println(discount.toString());
                }

                System.out.println("Which Pizza Discount do you want to add? Enter the DiscountID. Enter -1 to stop adding Discounts: ");

                Integer discountId = Integer.parseInt(reader.readLine());

                if (discountId==-1) break;
                Discount pizzaDiscount = discounts.stream().filter(d->d.getDiscountID()==discountId).findAny().get();
                pizza.addDiscounts(pizzaDiscount);

                System.out.println("Do you want to add more discounts to this Pizza? Enter y/n?");
                if("y".equalsIgnoreCase(reader.readLine())) addDiscount = true;
                else addDiscount = false;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void helperToppings(Pizza pizza) {
        while(true) {
            try {

                ArrayList<Topping> toppings = ViewInventoryLevels();

                System.out.println("Which topping do you want to add? Enter the TopID. Enter -1 to stop adding toppings: ");

                Integer toppingId = Integer.parseInt(reader.readLine());

                if (toppingId==-1) {
                    break;
                }
                Topping topping = toppings.stream().filter(t->t.getTopID()==toppingId).findAny().get();
                boolean isExtra = false;
                System.out.println("Do you want to add extra topping? Enter y/n");

                isExtra = "y".equalsIgnoreCase(reader.readLine());
                pizza.addToppings(topping, isExtra);


                // System.out.println("We don't have enough of that topping to add it..."); need to implement
            }
            catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public static void viewCustomers()
    {
        /*
         * Simply print out all of the customers from the database.
         */
        try {
            ArrayList<Customer> customers = DBNinja.getCustomerList();
            customers.forEach(c -> System.out.println(c));
        }
        catch (Exception e) {
            System.out.println("There is an error in displaying the customers " + e);
            // System.out.println("ERROR: I don't understand your input for: Is this order an existing customer?");
            e.printStackTrace();
        }
    }


    // Enter a new customer in the database
    public static Integer EnterCustomer() throws SQLException, IOException
    {
        /*
         * Ask for the name of the customer:
         *   First Name <space> Last Name
         *
         * Ask for the  phone number.
         *   (##########) (No dash/space)
         *
         * Once you get the name and phone number, add it to the DB
         */
        System.out.println("What is this customer's name (first <space> last)");

        String customerName = reader.readLine();

        String[] name = customerName.split(" ");
        if (name.length<2)
            throw new IllegalArgumentException("Please enter full name: " + customerName);
        String firstName = name[0];
        String lastName = name[1];

        System.out.println("What is this customer's phone number (##########) (No dash/space):");

        String phoneNumber = reader.readLine();

        Customer customer = new Customer(0, firstName, lastName, phoneNumber);
        try {
            return DBNinja.addCustomer(customer);
        }
        catch (SQLException | IOException e) {
            System.out.println("There is an error while adding the customer " + e);
            throw e;
        }
    }

    // View any orders that are not marked as completed
    public static void ViewOrders() throws SQLException, IOException
    {
        /*  
		* This method allows the user to select between three different views of the Order history:
		* The program must display:
		* a.	all open orders
		* b.	all completed orders 
		* c.	all the orders (open and completed) since a specific date (inclusive)
		* 
		* After displaying the list of orders (in a condensed format) must allow the user to select a specific order for viewing its details.  
		* The details include the full order type information, the pizza information (including pizza discounts), and the order discounts.
		* 
		*/ 

        System.out.println("Would you like to:\n(a) display all orders [open or closed]\n(b) display all open orders\n(c) display all completed [closed] orders\n(d) display orders since a specific date");

        String selectedOption = reader.readLine();
        try {
            ArrayList<Order> orders = new ArrayList<>();
            switch (selectedOption) {
                case "a":
                    orders = DBNinja.getOrders1(false);
                    ArrayList<Order> additionalOrders = DBNinja.getOrders1(true); // Get another set of orders
                    orders.addAll(additionalOrders);
                    break;
                case "b":
                    orders = DBNinja.getOrders1(false); // Assuming you have a method for open orders
                    break;
                case "c":
                    orders = DBNinja.getOrders1(true); // Assuming you have a method for closed orders
                    break;
                case "d":
                    System.out.println("What is the date you want to restrict by? (FORMAT= YYYY-MM-DD)");
                    String date = reader.readLine();
                    // Validate date format here
                    orders = DBNinja.getOrdersByDate(date);
                    break;
                default:
                    System.out.println("I don't understand that input, returning to menu");
                    return;
            }

            if (orders == null || orders.isEmpty()) {
                System.out.println("No orders to display, returning to menu.");
                return;
            }

            orders.forEach(c -> System.out.println(c.toSimplePrint()));
            System.out.println("Which order would you like to see in detail? Enter the number (-1 to exit): ");
            final Integer orderId;
            try {
                orderId = Integer.parseInt(reader.readLine());
            } catch (NumberFormatException e) {
                System.out.println("Incorrect entry, returning to menu.");
                return;
            }
            Order order = orders.stream().filter(o -> o.getOrderID()==orderId)
                    .findFirst().orElse(null);
            if (order != null) {
                System.out.println(order.toString());
            } else {
                System.out.println("No orders to display, returning to menu.");
            }        }
        catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


    // When an order is completed, we need to make sure it is marked as complete
    public static void MarkOrderAsComplete() throws SQLException, IOException
    {
        /*
		 * All orders that are created through java (part 3, not the orders from part 2) should start as incomplete
		 * 
		 * When this method is called, you should print all of the "opoen" orders marked
		 * and allow the user to choose which of the incomplete orders they wish to mark as complete
		 * 
		 */
        ArrayList<Order> orders = DBNinja.getOrders1(true);
        // System.out.println("There are no open orders currently... returning to menu...");
        for (Order order : orders) {
            System.out.println(order.toSimplePrint());
        }

        System.out.println("Which order would you like mark as complete? Enter the OrderID: ");
        Integer orderId = Integer.parseInt(reader.readLine());
        // System.out.println("Incorrect entry, not an option");
        DBNinja.completeOrder(orders.stream().filter(o->o.getOrderID()==orderId).findFirst().get());
    }

    // See the list of inventory and it's current level
    public static ArrayList<Topping> ViewInventoryLevels() throws SQLException, IOException
    {
        /*
         * Print the inventory. Display the topping ID, name, and current inventory
         */
        ArrayList<Topping> toppings = DBNinja.getToppingList();
        try {
            System.out.printf("%-9s %-25s %9s%n", "ID", "Name", "CurINVT");
            toppings.forEach(topping -> System.out.printf("%-9s %-25s %9s%n", topping.getTopID(), topping.getTopName(),
                    topping.getCurINVT()));

        } catch (Exception e) {
            System.out.println("There is an error while viewing Inventory levels " + e);
            throw e;
        }
        return toppings;
    }

    // Select an inventory item and add more to the inventory level to re-stock the
    // inventory
    public static void AddInventory() throws SQLException, IOException
    {
        /*
		 * This should print the current inventory and then ask the user which topping (by ID) they want to add more to and how much to add
		 */
        try {
            ViewInventoryLevels();
            System.out.println("Which topping do you want to add inventory to? Enter the number: ");
            Integer toppingId = Integer.parseInt(reader.readLine());

            System.out.println("How many units would you like to add? ");

            Double amountToAdd = Double.parseDouble(reader.readLine());
            ArrayList<Topping> toppings = DBNinja.getToppingList();
            Topping topping = toppings.stream().filter(t->t.getTopID()==toppingId).findFirst().orElse(null);
            DBNinja.addToInventory(topping, amountToAdd);
            // System.out.println("Incorrect entry, not an option");
        } catch (Exception e) {
            throw e;
        }
    }

    // A function that builds a pizza. Used in our add new order function
    public static Pizza buildPizza() throws SQLException, IOException
    {

        /*
		 * This is a helper method for first menu option.
		 * 
		 * It should ask which size pizza the user wants and the crustType.
		 * 
		 * Once the pizza is created, it should be added to the DB.
		 * 
		 * We also need to add toppings to the pizza. (Which means we not only need to add toppings here, but also our bridge table)
		 * 
		 * We then need to add pizza discounts (again, to here and to the database)
		 * 
		 * Once the discounts are added, we can return the pizza
		 */
        System.out.println("What size is the pizza?");
        System.out.println("1."+DBNinja.size_s);
        System.out.println("2."+DBNinja.size_m);
        System.out.println("3."+DBNinja.size_l);
        System.out.println("4."+DBNinja.size_xl);
        System.out.println("Enter the corresponding number: ");
        Integer pizzaSizeMenuid = Integer.parseInt(reader.readLine());
        String pizzaSize = getPizzaSize(pizzaSizeMenuid);

        System.out.println("What crust for this pizza?");
        System.out.println("1."+DBNinja.crust_thin);
        System.out.println("2."+DBNinja.crust_orig);
        System.out.println("3."+DBNinja.crust_pan);
        System.out.println("4."+DBNinja.crust_gf);
        System.out.println("Enter the corresponding number: ");
        Integer pizzaCrustMenuid = Integer.parseInt(reader.readLine());
        String pizzaCrust = getPizzaCrust(pizzaCrustMenuid);

        String pizzaTimeStamp = getCurrentTime();
        double pizzaCustPrice = DBNinja.getBaseCustPrice(pizzaSize, pizzaCrust);
        double pizzaBusPrice = DBNinja.getBaseBusPrice(pizzaSize, pizzaCrust);
        Pizza pizza = new Pizza(0, pizzaSize, pizzaCrust, 0, "PREPARING", pizzaTimeStamp, pizzaCustPrice, pizzaBusPrice);

        helperToppings(pizza);
        helperPizzaDiscount(pizza);
        //System.out.println(pizza.toString());
        return pizza;
    }

    private static int getTopIndexFromList(int TopID, ArrayList<Topping> tops)
    {
        /*
         * This is a helper function I used to get a topping index from a list of toppings
         * It's very possible you never need to use a function like this
         *
         */
        int ret = -1;



        return ret;
    }


    public static void PrintReports() throws SQLException, NumberFormatException, IOException
    {
        System.out.println("Which report do you wish to print? Enter\n(a) ToppingPopularity\n(b) ProfitByPizza\n(c) ProfitByOrderType:");

        switch(reader.readLine()) {
            case "a":
                DBNinja.printToppingPopReport();
                break;
            case "b":
                DBNinja.printProfitByPizzaReport();
                break;
            case "c":
                DBNinja.printProfitByOrderType();
                break;
            default:
                System.out.println("I don't understand that input... returning to menu...");
                return;
        }
    }

    public static String getPizzaCrust(int menuId){
        if(menuId==1) return "Thin";
        else if(menuId==2) return "Original";
        else if(menuId==3) return "Pan";
        else return "Gluten-Free";
    }

    public static String getPizzaSize(int menuId){
        if(menuId==1) return "small";
        else if(menuId==2) return "medium";
        else if(menuId==3) return "large";
        else return "x-large";
    }

    public static String getCurrentTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        return dtf.format(LocalDateTime.now());
    }

    //Prompt - NO CODE SHOULD TAKE PLACE BELOW THIS LINE
	// DO NOT EDIT ANYTHING BELOW HERE, THIS IS NEEDED TESTING.
	// IF YOU EDIT SOMETHING BELOW, IT BREAKS THE AUTOGRADER WHICH MEANS YOUR GRADE WILL BE A 0 (zero)!!

	public static void PrintMenu() {
		System.out.println("\n\nPlease enter a menu option:");
		System.out.println("1. Enter a new order");
		System.out.println("2. View Customers ");
		System.out.println("3. Enter a new Customer ");
		System.out.println("4. View orders");
		System.out.println("5. Mark an order as completed");
		System.out.println("6. View Inventory Levels");
		System.out.println("7. Add Inventory");
		System.out.println("8. View Reports");
		System.out.println("9. Exit\n\n");
		System.out.println("Enter your option: ");
	}

	/*
	 * autograder controls....do not modiify!
	 */

	public final static String autograder_seed = "6f1b7ea9aac470402d48f7916ea6a010";

	
	private static void autograder_compilation_check() {

		try {
			Order o = null;
			Pizza p = null;
			Topping t = null;
			Discount d = null;
			Customer c = null;
			ArrayList<Order> alo = null;
			ArrayList<Discount> ald = null;
			ArrayList<Customer> alc = null;
			ArrayList<Topping> alt = null;
			double v = 0.0;
			String s = "";

			DBNinja.addOrder(o);
			DBNinja.addPizza(p);
			DBNinja.useTopping(p, t, false);
			DBNinja.usePizzaDiscount(p, d);
			DBNinja.useOrderDiscount(o, d);
			DBNinja.addCustomer(c);
			DBNinja.completeOrder(o);
			alo = DBNinja.getOrders(false);
			o = DBNinja.getLastOrder();
			alo = DBNinja.getOrdersByDate("01/01/1999");
			ald = DBNinja.getDiscountList();
			d = DBNinja.findDiscountByName("Discount");
			alc = DBNinja.getCustomerList();
			c = DBNinja.findCustomerByPhone("0000000000");
			alt = DBNinja.getToppingList();
			t = DBNinja.findToppingByName("Topping");
			DBNinja.addToInventory(t, 1000.0);
			v = DBNinja.getBaseCustPrice("size", "crust");
			v = DBNinja.getBaseBusPrice("size", "crust");
			DBNinja.printInventory();
			DBNinja.printToppingPopReport();
			DBNinja.printProfitByPizzaReport();
			DBNinja.printProfitByOrderType();
			s = DBNinja.getCustomerName(0);
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}
	}


}