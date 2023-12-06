-- Author: Uma Mahesh

use Pizzeria;

INSERT INTO Topping
(ToppingName,
ToppingPrice,
ToppingCost,
ToppingMinInventory,
ToppingCurrentInventory,
ToppingSmall,
ToppingMedium,
ToppingLarge,
ToppingXL)
VALUES
('Pepperoni', 1.25, 0.2, 100, 50, 2, 2.75, 3.5, 4.5),
('Sausage', 1.25, 0.15, 100, 50, 2.5, 3, 3.5, 4.25),
('Ham', 1.5, 0.15, 78, 25, 2, 2.5, 3.25, 4),
('Chicken', 1.75, 0.25, 56, 25, 1.5, 2, 2.25, 3),
('Green Pepper', 0.5, 0.02, 79, 25, 1, 1.5, 2, 2.5),
('Onion', 0.5, 0.02, 85, 25, 1, 1.5, 2, 2.75),
('Roma Tomato', 0.75, 0.03, 86, 10, 2, 3, 3.5, 4.5),
('Mushrooms', 0.75, 0.1, 52, 50, 1.5, 2, 2.5, 3),
('Black Olives', 0.6, 0.1, 39, 25, 0.75, 1, 1.5, 2),
('Pineapple', 1, 0.25, 100, 50, 1, 1.25, 1.75, 2),
('Jalapenos', 0.5, 0.05, 64, 50, 0.5, 0.75, 1.25, 1.75),
('Banana Peppers', 0.5, 0.05, 75, 50, 0.6, 1, 1.3, 1.75),
('Regular Cheese', 0.5, 0.12, 250, 50, 2, 3.5, 5, 7),
('Four Cheese Blend', 1, 0.15, 150, 25, 2, 3.5, 5, 7),
('Feta Cheese', 1.5, 0.18, 75, 50, 1.75, 3, 4, 5.5),
('Goat Cheese', 1.5, 0.2, 54, 33, 1.6, 2.75, 4, 5.5),
('Bacon', 1.5, 0.25, 89, 60, 1, 1.5, 2, 3);

INSERT INTO Discount
(DiscountName,
DiscountAmount,
IsPercent)
VALUES
('Employee', 15.00,  TRUE),
('Lunch Special Medium',  1.00, FALSE),
('Lunch Special Large',  2.00, FALSE),
('Specialty Pizza', 1.50, FALSE),
('Gameday Special', 20.00, TRUE),
('Happy Hour', 10.00, TRUE);

INSERT INTO Base
(BasePizzaSize,
BasePizzaCrust,
BasePizzaPrice,
BasePizzaCost)
VALUES
('small', 'Thin', 3.00, 0.50), 
('small', 'Original', 3.00, 0.75),
('small', 'Pan', 3.50, 1.00),
('small', 'Gluten-Free', 4.00, 2.00),
('medium', 'Thin', 5.00, 1.00),
('medium', 'Original', 5.00, 1.50),
('medium', 'Pan', 6.00, 2.25),
('medium', 'Gluten-Free', 6.25, 3.00),
('large', 'Thin', 8.00, 1.25),
('large', 'Original', 8.00, 2.00),
('large', 'Pan', 9.00, 3.00),
('large', 'Gluten-Free', 9.50, 4.00),
('x-large', 'Thin', 10.00, 2.00),
('x-large', 'Original', 10.00, 3.00),
('x-large', 'Pan', 11.50, 4.50),
('x-large', 'Gluten-Free', 12.50, 6.00);

INSERT INTO Customer
(CustomerID, CustomerFName, CustomerLName, CustomerPhone)
VALUES
(1, 'DineIn', 'DineIn', '1212112121');

INSERT INTO Customer
(CustomerID, CustomerFName, CustomerLName, CustomerPhone)
VALUES
(2, 'speed', 'show', '1212112121');


INSERT INTO `Order` 
(OrderID, OrderCustomerID, OrderTime, OrderType, OrderPrice, OrderCost, OrderStatus)
VALUES
(1, 1, '2023-03-05 12:03:00', 'DineIn',20.75, 3.68, TRUE);

INSERT INTO Pizza
(PizzaID, PizzaCrust, PizzaSize, PizzaOrderID, PizzaTime, PizzaState, PizzaPrice, PizzaCost)
VALUES
(1001, 'Thin', 'Large', 1, '2023-03-05 12:03:00', TRUE, 20.75, 3.68);

INSERT INTO DineInOrder
(DineInOrderID, DineInOrderTableNum)
VALUES
(1, 21);
    
INSERT INTO PizzaTopping
(PizzaToppingPizzaID, PizzaToppingToppingID, PizzaToppingExtraTopping)
SELECT 1001, T.ToppingID, TRUE 
FROM Topping T WHERE T.ToppingName = 'Regular Cheese'; 

INSERT INTO PizzaTopping
(PizzaToppingPizzaID, PizzaToppingToppingID, PizzaToppingExtraTopping)
SELECT 1001, T.ToppingID, FALSE 
FROM Topping T WHERE T.ToppingName = 'Pepperoni';

INSERT INTO PizzaTopping
(PizzaToppingPizzaID, PizzaToppingToppingID, PizzaToppingExtraTopping)
SELECT 1001, T.ToppingID, FALSE 
FROM Topping T WHERE T.ToppingName = 'Sausage';
	
INSERT INTO OrderDiscount(OrderDiscountOrderID,OrderDiscountDiscountID)
VALUES
(1,3);



INSERT INTO `Order`(OrderID, OrderCustomerID, OrderTime, OrderType, OrderPrice, OrderCost, OrderStatus)
VALUES (2, 1, '2023-04-03 12:05:00', 'DineIn', 19.78, 4.63, TRUE);

INSERT INTO Pizza
(PizzaID, PizzaCrust, PizzaSize, PizzaOrderID, PizzaTime, PizzaState, PizzaPrice, PizzaCost)
VALUES
(1002, 'Pan', 'Medium', 2, '2023-04-03 12:05:00', TRUE, 12.85, 3.23),
(1003, 'Original', 'Small', 2, '2023-04-03 12:05:00', TRUE, 6.93, 1.40);

INSERT INTO DineInOrder
(DineInOrderID, DineInOrderTableNum)
VALUES
(2, 4);

INSERT INTO PizzaTopping
(PizzaToppingPizzaID, PizzaToppingToppingID, PizzaToppingExtraTopping)
SELECT 1002, T.ToppingID, 0
FROM Topping T WHERE T.ToppingName = 'Feta Cheese';

INSERT INTO PizzaTopping
(PizzaToppingPizzaID, PizzaToppingToppingID, PizzaToppingExtraTopping)
SELECT 1002, T.ToppingID, 0
FROM Topping T WHERE T.ToppingName = 'Black Olives';

INSERT INTO PizzaTopping
(PizzaToppingPizzaID, PizzaToppingToppingID, PizzaToppingExtraTopping)
SELECT 1002, T.ToppingID, 0
FROM Topping T WHERE T.ToppingName = 'Roma Tomato';

INSERT INTO PizzaTopping
(PizzaToppingPizzaID, PizzaToppingToppingID, PizzaToppingExtraTopping)
SELECT 1002, T.ToppingID, 0
FROM Topping T WHERE T.ToppingName = 'Mushrooms';

INSERT INTO PizzaTopping
(PizzaToppingPizzaID, PizzaToppingToppingID, PizzaToppingExtraTopping)
SELECT 1002, T.ToppingID, 0
FROM Topping T WHERE T.ToppingName = 'Banana Peppers';

INSERT INTO PizzaTopping
(PizzaToppingPizzaID, PizzaToppingToppingID, PizzaToppingExtraTopping)
SELECT 1003, T.ToppingID, 0
FROM Topping T WHERE T.ToppingName = 'Regular Cheese';

INSERT INTO PizzaTopping
(PizzaToppingPizzaID, PizzaToppingToppingID, PizzaToppingExtraTopping)
SELECT 1003, T.ToppingID, 0
FROM Topping T WHERE T.ToppingName = 'Chicken';

INSERT INTO PizzaTopping
(PizzaToppingPizzaID, PizzaToppingToppingID, PizzaToppingExtraTopping)
SELECT 1003, T.ToppingID, 0
FROM Topping T WHERE T.ToppingName = 'Banana Peppers';

INSERT INTO PizzaDiscount
(PizzaDiscountPizzaID, PizzaDiscountDiscountID)
VALUES
(1002, 2), 
(1002, 4);



INSERT INTO Customer
(CustomerID, CustomerFName, CustomerLName, CustomerPhone)
VALUES
(3, 'Andrew', 'Wilkes-Krier', '8642545861');

INSERT INTO `Order`
(OrderID, OrderCustomerID, OrderTime, OrderType, OrderPrice, OrderCost, OrderStatus)
VALUES
(3, 3, '2023-03-03 21:30:00', 'PickUp', 89.28, 19.80, TRUE);

INSERT INTO Pizza
(PizzaID, PizzaCrust, PizzaSize, PizzaOrderID, PizzaState, PizzaPrice, PizzaCost, PizzaTime)
VALUES
(1004, "Original", "large", 3, TRUE, 14.88, 3.30, '2023-03-03 21:30:00'),
(1005, "Original", "large", 3, TRUE, 14.88, 3.30, '2023-03-03 21:30:00'),
(1006, "Original", "large", 3, TRUE, 14.88, 3.30, '2023-03-03 21:30:00'),
(1007, "Original", "large", 3, TRUE, 14.88, 3.30, '2023-03-03 21:30:00'),
(1008, "Original", "large", 3, TRUE, 14.88, 3.30, '2023-03-03 21:30:00'),
(1009, "Original", "large", 3, TRUE, 14.88, 3.30, '2023-03-03 21:30:00');

INSERT INTO PickUpOrder
(PickupOrderID,PickUpStatus)
VALUES
(3, 'Picked Up');

INSERT INTO PizzaTopping
(PizzaToppingPizzaID, PizzaToppingToppingID, PizzaToppingExtraTopping)
SELECT 1004, T.ToppingID, 0 
FROM Topping T WHERE T.ToppingName = 'Regular Cheese';

INSERT INTO PizzaTopping
(PizzaToppingPizzaID, PizzaToppingToppingID, PizzaToppingExtraTopping)
SELECT 1004, T.ToppingID, 0
FROM Topping T WHERE T.ToppingName = 'Pepperoni';

INSERT INTO PizzaTopping
(PizzaToppingPizzaID, PizzaToppingToppingID, PizzaToppingExtraTopping)
SELECT 1005, T.ToppingID, 0
FROM Topping T WHERE T.ToppingName = 'Regular Cheese';

INSERT INTO PizzaTopping
(PizzaToppingPizzaID, PizzaToppingToppingID, PizzaToppingExtraTopping)
SELECT 1005, T.ToppingID, 0
FROM Topping T WHERE T.ToppingName = 'Pepperoni';

INSERT INTO PizzaTopping
(PizzaToppingPizzaID, PizzaToppingToppingID, PizzaToppingExtraTopping)
SELECT 1006, T.ToppingID, 0
FROM Topping T WHERE T.ToppingName = 'Regular Cheese';

INSERT INTO PizzaTopping
(PizzaToppingPizzaID, PizzaToppingToppingID, PizzaToppingExtraTopping)
SELECT 1006, T.ToppingID, 0
FROM Topping T WHERE T.ToppingName = 'Pepperoni';

INSERT INTO PizzaTopping
(PizzaToppingPizzaID, PizzaToppingToppingID, PizzaToppingExtraTopping)
SELECT 1007, T.ToppingID, 0
FROM Topping T WHERE T.ToppingName = 'Regular Cheese';

INSERT INTO PizzaTopping
(PizzaToppingPizzaID, PizzaToppingToppingID, PizzaToppingExtraTopping)
SELECT 1007, T.ToppingID, 0
FROM Topping T WHERE T.ToppingName = 'Pepperoni';

INSERT INTO PizzaTopping
(PizzaToppingPizzaID, PizzaToppingToppingID, PizzaToppingExtraTopping)
SELECT 1008, T.ToppingID, 0
FROM Topping T WHERE T.ToppingName = 'Regular Cheese';

INSERT INTO PizzaTopping
(PizzaToppingPizzaID, PizzaToppingToppingID, PizzaToppingExtraTopping)
SELECT 1008, T.ToppingID, 0
FROM Topping T WHERE T.ToppingName = 'Pepperoni';

INSERT INTO PizzaTopping
(PizzaToppingPizzaID, PizzaToppingToppingID, PizzaToppingExtraTopping)
SELECT 1009, T.ToppingID, 0
FROM Topping T WHERE T.ToppingName = 'Regular Cheese';

INSERT INTO PizzaTopping
(PizzaToppingPizzaID, PizzaToppingToppingID, PizzaToppingExtraTopping)
SELECT 1009, T.ToppingID, 0
FROM Topping T WHERE T.ToppingName = 'Pepperoni';



INSERT INTO `Order`
(OrderID, OrderCustomerID, OrderTime, OrderType, OrderPrice, OrderCost, OrderStatus)
VALUES
(4, 3, '2023-04-20 19:11:00', 'Delivery', 86.19, 23.62, TRUE);

INSERT INTO Pizza
(PizzaID, PizzaCrust, PizzaSize, PizzaOrderID, PizzaState, PizzaPrice, PizzaCost, PizzaTime)
VALUES
(1010, 'Original', 'x-large', 4, TRUE, 27.94, 9.19, '2023-04-20 19:11:00'),
(1011, 'Original', 'x-large', 4, TRUE, 31.50, 6.25, '2023-04-20 19:11:00'),
(1012, 'Original', 'x-large', 4, TRUE, 26.75, 8.18, '2023-04-20 19:11:00');

INSERT INTO PizzaTopping
(PizzaToppingPizzaID, PizzaToppingToppingID, PizzaToppingExtraTopping)
SELECT 1010, T.ToppingID, 0 
FROM Topping T WHERE T.ToppingName = 'Pepperoni';

INSERT INTO PizzaTopping
(PizzaToppingPizzaID, PizzaToppingToppingID, PizzaToppingExtraTopping)
SELECT 1010, T.ToppingID, 0 
FROM Topping T WHERE T.ToppingName = 'Sausage';

INSERT INTO PizzaTopping
(PizzaToppingPizzaID, PizzaToppingToppingID, PizzaToppingExtraTopping)
SELECT 1010, T.ToppingID, 0 
FROM Topping T WHERE T.ToppingName = 'Four Cheese Blend';

INSERT INTO PizzaTopping
(PizzaToppingPizzaID, PizzaToppingToppingID, PizzaToppingExtraTopping)
SELECT 1011, T.ToppingID, 1 
FROM Topping T WHERE T.ToppingName = 'Ham';

INSERT INTO PizzaTopping
(PizzaToppingPizzaID, PizzaToppingToppingID, PizzaToppingExtraTopping)
SELECT 1011, T.ToppingID, 1 
FROM Topping T WHERE T.ToppingName = 'Pineapple';

INSERT INTO PizzaTopping
(PizzaToppingPizzaID, PizzaToppingToppingID, PizzaToppingExtraTopping)
SELECT 1011, T.ToppingID, 0 
FROM Topping T WHERE T.ToppingName = 'Four Cheese Blend';

INSERT INTO PizzaTopping
(PizzaToppingPizzaID, PizzaToppingToppingID, PizzaToppingExtraTopping)
SELECT 1012, T.ToppingID, 0 
FROM Topping T WHERE T.ToppingName = 'Chicken';

INSERT INTO PizzaTopping
(PizzaToppingPizzaID, PizzaToppingToppingID, PizzaToppingExtraTopping)
SELECT 1012, T.ToppingID, 0 
FROM Topping T WHERE T.ToppingName = 'Bacon';

INSERT INTO PizzaTopping
(PizzaToppingPizzaID, PizzaToppingToppingID, PizzaToppingExtraTopping)
SELECT 1012, T.ToppingID, 0 
FROM Topping T WHERE T.ToppingName = 'Four Cheese Blend';

INSERT INTO  PizzaDiscount 
(PizzaDiscountPizzaID, PizzaDiscountDiscountID)
VALUES
(1011, 4);

INSERT INTO  OrderDiscount (OrderDiscountOrderID, OrderDiscountDiscountID)
VALUES
(4, 5);

INSERT INTO DeliveryOrder
(DeliveryOrderID, DeliveryOrderAddress)
VALUES
(4, '115 Party Blvd, 29631, Anderson, SC');


INSERT INTO Customer
(CustomerID, CustomerFName, CustomerLName, CustomerPhone)
VALUES
(4, 'Matt', 'Engers', '8644749953');

INSERT INTO `Order` 
(OrderID, OrderCustomerID, OrderTime, OrderType, OrderPrice, OrderCost, OrderStatus)
VALUES
(5, 4, '2023-03-02 17:30:00', 'PickUp', 27.45, 7.88, TRUE);

INSERT INTO Pizza
(PizzaID, PizzaCrust, PizzaSize, PizzaOrderID, PizzaState, PizzaPrice, PizzaCost, PizzaTime)
VALUES
(1013, 'Gluten-Free', 'x-large', 5, TRUE, 27.45, 7.88, '2023-03-02 17:30:00');

INSERT INTO PizzaTopping
(PizzaToppingPizzaID, PizzaToppingToppingID, PizzaToppingExtraTopping)
SELECT 1013, T.ToppingID, FALSE 
FROM Topping T WHERE T.ToppingName = 'Green Pepper';

INSERT INTO PizzaTopping
(PizzaToppingPizzaID, PizzaToppingToppingID, PizzaToppingExtraTopping)
SELECT 1013, T.ToppingID, FALSE 
FROM Topping T WHERE T.ToppingName = 'Onion';

INSERT INTO PizzaTopping
(PizzaToppingPizzaID, PizzaToppingToppingID, PizzaToppingExtraTopping)
SELECT 1013, T.ToppingID, FALSE 
FROM Topping T WHERE T.ToppingName = 'Roma Tomato';

INSERT INTO PizzaTopping
(PizzaToppingPizzaID, PizzaToppingToppingID, PizzaToppingExtraTopping)
SELECT 1013, T.ToppingID, FALSE 
FROM Topping T WHERE T.ToppingName = 'Mushrooms';

INSERT INTO PizzaTopping
(PizzaToppingPizzaID, PizzaToppingToppingID, PizzaToppingExtraTopping)
SELECT 1013, T.ToppingID, FALSE 
FROM Topping T WHERE T.ToppingName = 'Black Olives';

INSERT INTO PizzaTopping
(PizzaToppingPizzaID, PizzaToppingToppingID, PizzaToppingExtraTopping)
SELECT 1013, T.ToppingID, FALSE 
FROM Topping T WHERE T.ToppingName = 'Goat Cheese';

INSERT INTO  PizzaDiscount 
(PizzaDiscountPizzaID , PizzaDiscountDiscountID )
VALUES
(1013, 4);

INSERT INTO  PickUpOrder
(PickupOrderID, PickUpStatus)
VALUES
(5, 'Picked Up');



INSERT INTO  Customer 
(CustomerID, CustomerFName, CustomerLName, CustomerPhone)
VALUES
(5, 'Frank', 'Turner', 8642328944);

INSERT INTO `Order` 
(OrderID, OrderCustomerID, OrderTime, OrderType, OrderPrice, OrderCost, OrderStatus)
VALUES
(6, 5, '2023-03-02 18:17:00', 'Delivery', 25.81, 4.24, TRUE);

INSERT INTO Pizza 
(PizzaID, PizzaCrust, PizzaSize, PizzaOrderID, PizzaTime, PizzaState, PizzaPrice, PizzaCost)
VALUES
(1014, "Thin", "large", 6, '2023-03-02 18:17:00', TRUE, 25.81, 4.24);

INSERT INTO PizzaTopping 
(PizzaToppingPizzaID, PizzaToppingToppingID, PizzaToppingExtraTopping)
SELECT 1014, T.ToppingID, 0
FROM Topping T WHERE T.ToppingName = 'Chicken';

INSERT INTO PizzaTopping 
(PizzaToppingPizzaID, PizzaToppingToppingID, PizzaToppingExtraTopping)
SELECT 1014, T.ToppingID, 0
FROM Topping T WHERE T.ToppingName = 'Green Pepper';

INSERT INTO PizzaTopping 
(PizzaToppingPizzaID, PizzaToppingToppingID, PizzaToppingExtraTopping)
SELECT 1014, T.ToppingID, 0
FROM Topping T WHERE T.ToppingName = 'Onion';

INSERT INTO PizzaTopping 
(PizzaToppingPizzaID, PizzaToppingToppingID, PizzaToppingExtraTopping)
SELECT 1014, T.ToppingID, 0
FROM Topping T WHERE T.ToppingName = 'Mushrooms';

INSERT INTO PizzaTopping 
(PizzaToppingPizzaID, PizzaToppingToppingID, PizzaToppingExtraTopping)
SELECT 1014, T.ToppingID, 1
FROM Topping T WHERE T.ToppingName = 'Four Cheese Blend';

INSERT INTO DeliveryOrder
(DeliveryOrderID, DeliveryOrderAddress)
VALUES
(6, '6745 Wesex St, 29621, Anderson, SC');



INSERT INTO  Customer (CustomerID, CustomerFName, CustomerLName, CustomerPhone)VALUES
(6, 'Milo', 'Auckerman', 8648785679);

INSERT INTO  `Order` 
(OrderID, OrderCustomerID, OrderTime, OrderType, OrderPrice, OrderCost, OrderStatus)
VALUES
(7, 6,'2023-04-13 20:32:00', "Delivery",37.25 ,6.00 ,TRUE);

INSERT INTO  Pizza 
(PizzaID, PizzaCrust, PizzaSize, PizzaOrderID, PizzaTime, PizzaState, PizzaPrice, PizzaCost)
VALUES
(1015, "Thin", "large", 7,  '2023-04-13 20:32:00',TRUE, 18.00 ,2.75),
(1016, "Thin", "large", 7,  '2023-04-13 20:32:00',TRUE, 19.25 , 3.25);

INSERT INTO  PizzaTopping 
(PizzaToppingPizzaID, PizzaToppingToppingID, PizzaToppingExtraTopping)
SELECT 1015, T.ToppingID, 1
from Topping T WHERE T.ToppingName = 'Four Cheese Blend';

INSERT INTO  PizzaTopping 
(PizzaToppingPizzaID, PizzaToppingToppingID, PizzaToppingExtraTopping)
SELECT 1016, T.ToppingID, 0
from Topping T WHERE T.ToppingName = 'Regular Cheese';

INSERT INTO  PizzaTopping 
(PizzaToppingPizzaID, PizzaToppingToppingID, PizzaToppingExtraTopping)
SELECT 1016, T.ToppingID, 1
from Topping T WHERE T.ToppingName = 'Pepperoni';

INSERT INTO  OrderDiscount 
(OrderDiscountOrderID ,OrderDiscountDiscountID)
VALUES
(7,1);

INSERT INTO DeliveryOrder
(DeliveryOrderID, DeliveryOrderAddress)
VALUES
(7, '8879 Suburban Home, 29621, Anderson, SC');


INSERT INTO `Order` 
(OrderID, OrderCustomerID, OrderTime, OrderType, OrderPrice, OrderCost, OrderStatus)
VALUES
(8, 1, '2023-03-05 12:03:00', 'DineIn',20.75, 3.68, TRUE);

INSERT INTO Pizza
(PizzaID, PizzaCrust, PizzaSize, PizzaOrderID, PizzaTime, PizzaState, PizzaPrice, PizzaCost)
VALUES
(1017, 'Thin', 'Large', 8, '2023-03-05 12:03:00', TRUE, 20.75, 3.68);

INSERT INTO DineInOrder
(DineInOrderID, DineInOrderTableNum)
VALUES
(8, 21);
    
INSERT INTO PizzaTopping
(PizzaToppingPizzaID, PizzaToppingToppingID, PizzaToppingExtraTopping)
SELECT 1017, T.ToppingID, TRUE 
FROM Topping T WHERE T.ToppingName = 'Regular Cheese'; 

INSERT INTO PizzaTopping
(PizzaToppingPizzaID, PizzaToppingToppingID, PizzaToppingExtraTopping)
SELECT 1017, T.ToppingID, FALSE 
FROM Topping T WHERE T.ToppingName = 'Pepperoni';

INSERT INTO PizzaTopping
(PizzaToppingPizzaID, PizzaToppingToppingID, PizzaToppingExtraTopping)
SELECT 1017, T.ToppingID, FALSE 
FROM Topping T WHERE T.ToppingName = 'Sausage';
	
INSERT INTO OrderDiscount(OrderDiscountOrderID,OrderDiscountDiscountID)
VALUES
(8,3);
