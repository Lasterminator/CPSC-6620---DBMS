-- Author: Uma Mahesh

use Pizzeria;

CREATE VIEW ToppingPopularity AS
SELECT 
  ToppingName AS 'Topping', 
  SUM(CASE 
        WHEN PizzaToppingExtraTopping = 1 THEN 2 
        ELSE 1 
      END) AS 'ToppingCount'
FROM Topping 
JOIN PizzaTopping ON Topping.ToppingID = PizzaTopping.PizzaToppingToppingID 
GROUP BY ToppingName 
ORDER BY ToppingCount DESC;

SELECT * FROM ToppingPopularity;



CREATE VIEW ProfitByPizza AS
SELECT BasePizzaSize AS 'Size', BasePizzaCrust AS 'Crust',
SUM(Pizza.PizzaPrice - Pizza.PizzaCost) AS 'Profit',
DATE_FORMAT(MAX(Pizza.PizzaTime), '%m/%Y') AS 'Order Month'
FROM Pizza
INNER JOIN Base ON Pizza.PizzaSize = Base.BasePizzaSize AND Pizza.PizzaCrust = Base.BasePizzaCrust
GROUP BY PizzaSize, PizzaCrust
ORDER BY Profit DESC;

SELECT * FROM ProfitByPizza;


CREATE VIEW ProfitByOrderType AS 
SELECT 
    OrderType AS 'customerType',
    DATE_FORMAT(OrderTime, '%m/%Y') AS 'Order Month', 
    SUM(OrderPrice) AS 'TotalOrderPrice',
    SUM(OrderCost) AS 'TotalOrderCost', 
    SUM(OrderPrice - OrderCost) AS 'Profit'
FROM `Order`
GROUP BY OrderType, MONTH(OrderTime)
UNION ALL
SELECT 
    '', 
    'GrandTotal', 
    SUM(OrderPrice), 
    SUM(OrderCost), 
    SUM(OrderPrice - OrderCost) AS 'Profit'
FROM `Order`;

SELECT * FROM ProfitByOrderType;



-- DROP VIEW ToppingPopularity;
-- DROP VIEW ProfitByPizza;
-- DROP VIEW ProfitByOrderType;
 