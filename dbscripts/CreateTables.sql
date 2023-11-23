-- Author: Trinath Sai Subhash Reddy Pittala

DROP SCHEMA IF EXISTS Pizzeria;

CREATE SCHEMA Pizzeria;

use Pizzeria;

DROP TABLE IF EXISTS `PizzaTopping`;
DROP TABLE IF EXISTS `PizzaDiscount`;
DROP TABLE IF EXISTS `OrderDiscount`;
DROP TABLE IF EXISTS `PickUpOrder`;
DROP TABLE IF EXISTS `DeliveryOrder`;
DROP TABLE IF EXISTS `DineInOrder`;
DROP TABLE IF EXISTS `Pizza`;
DROP TABLE IF EXISTS `Order`;
DROP TABLE IF EXISTS `Customer`;
DROP TABLE IF EXISTS `Discount`;
DROP TABLE IF EXISTS `Topping`;
DROP TABLE IF EXISTS `Base`;

CREATE TABLE `Customer` (
  `CustomerID` int NOT NULL,
  `CustomerFName` varchar(15) DEFAULT NULL,
  `CustomerLName` varchar(15) DEFAULT NULL,
  `CustomerPhone` varchar(11) DEFAULT NULL,
  PRIMARY KEY (`CustomerID`)
);

CREATE TABLE `Order` (
  `OrderID` int NOT NULL,
  `OrderCustomerID` int NOT NULL,
  `OrderTime` datetime NOT NULL,
  `OrderType` varchar(15) NOT NULL,
  `OrderPrice` decimal(5,2) NOT NULL,
  `OrderCost` decimal(5,2) NOT NULL,
  `OrderStatus` varchar(20) NOT NULL,
  PRIMARY KEY (`OrderID`),
  KEY `OrderCustomerIDFK1_IDx` (`OrderCustomerID`),
  CONSTRAINT `OrderCustomerIDFK1` FOREIGN KEY (`OrderCustomerID`) REFERENCES `Customer` (`CustomerID`)
);

CREATE TABLE `DineInOrder` (
  `DineInOrderID` int NOT NULL,
  `DineInOrderTableNum` int NOT NULL,
  PRIMARY KEY (`DineInOrderID`),
  CONSTRAINT `DineInOrderIDFK1` FOREIGN KEY (`DineInOrderID`) REFERENCES `Order` (`OrderID`)
) ;

CREATE TABLE `DeliveryOrder` (
  `DeliveryOrderID` int NOT NULL,
  `DeliveryOrderAddress1` varchar(255) NOT NULL,
  `DeliveryOrderAddress2` varchar(255) DEFAULT NULL,
  `DeliveryOrderZip` varchar(5) NOT NULL,
  `DeliveryOrderCity` varchar(15) NOT NULL,
  `DeliveryOrderState` varchar(15) NOT NULL,
  PRIMARY KEY (`DeliveryOrderID`),
  CONSTRAINT `DeliveryOrderIDFK1` FOREIGN KEY (`DeliveryOrderID`) REFERENCES `Order` (`OrderID`)
);


CREATE TABLE `PickUpOrder` (
  `PickUpOrderID` int NOT NULL,
  `PickupStatus` varchar(15) NOT NULL,
  PRIMARY KEY (`PickupOrderID`),
  CONSTRAINT `PickupOrderIDFK1` FOREIGN KEY (`PickupOrderID`) REFERENCES `Order` (`OrderID`)
) ;

CREATE TABLE `Base` (
  `BasePizzaSize` varchar(15) NOT NULL,
  `BasePizzaCrust` varchar(15) NOT NULL,
  `BasePizzaPrice` decimal(5,2) NOT NULL,
  `BasePizzaCost` decimal(5,2) NOT NULL,
  PRIMARY KEY (`BasePizzaSize`,`BasePizzaCrust`)
) ;

CREATE TABLE `Pizza` (
  `PizzaID` int NOT NULL,
  `PizzaCrust` varchar(15) NOT NULL,
  `PizzaSize` varchar(15) NOT NULL,
  `PizzaOrderID` int NOT NULL,
  `PizzaState` varchar(15) NOT NULL,
  `PizzaPrice` decimal(5,2) NOT NULL,
  `PizzaCost` decimal(5,2) NOT NULL,
  `PizzaTime` datetime NOT NULL,
  PRIMARY KEY (`PizzaID`),
  KEY `PizzaCrustFK1_IDx` (`PizzaCrust`),
  KEY `PizzaOrderIDFK3_IDx` (`PizzaOrderID`),
  KEY `PizzaSizeFK2_IDx` (`PizzaSize`,`PizzaCrust`),
  CONSTRAINT `PizzaSizeFK2` FOREIGN KEY (`PizzaSize`, `PizzaCrust`) REFERENCES `Base` (`BasePizzaSize`, `BasePizzaCrust`),
  CONSTRAINT `PizzaOrderIDFK3` FOREIGN KEY (`PizzaOrderID`) REFERENCES `Order` (`OrderID`)
) ;

CREATE TABLE `Topping` (
  `ToppingID` int NOT NULL AUTO_INCREMENT,
  `ToppingName` varchar(20) NOT NULL,
  `ToppingPrice` decimal(5,2) NOT NULL,
  `ToppingCost` decimal(5,2) NOT NULL,
  `ToppingMinInventory` int NOT NULL,
  `ToppingCurrentInventory` int NOT NULL,
  `ToppingSmall` decimal(5,2) NOT NULL,
  `ToppingMedium` decimal(5,2) NOT NULL,
  `ToppingLarge` decimal(5,2) NOT NULL,
  `ToppingXL` decimal(5,2) NOT NULL,
  PRIMARY KEY (`ToppingID`)
) AUTO_INCREMENT=1 ;

CREATE TABLE `PizzaTopping` (
  `PizzaToppingPizzaID` int  NOT NULL,
  `PizzaToppingToppingID` int  NOT NULL,
  `PizzaToppingExtraTopping` tinyint(1) NOT NULL
  
) ;

ALTER TABLE `PizzaTopping` 
ADD PRIMARY KEY (`PizzaToppingPizzaID`, `PizzaToppingToppingID`);
;

CREATE TABLE `Discount` (
  `DiscountID` int NOT NULL AUTO_INCREMENT,
  `DiscountName` varchar(25) NOT NULL,
  `DiscountAmount` int NOT NULL,
  `DiscountPercent` decimal(5,2) NOT NULL,
  `DiscountType` varchar(1) NOT NULL,
  PRIMARY KEY (`DiscountID`)
) AUTO_INCREMENT=1 ;

CREATE TABLE `PizzaDiscount` (
  `PizzaDiscountPizzaID` int NOT NULL,
  `PizzaDiscountDiscountID` int NOT NULL,
  PRIMARY KEY (`PizzaDiscountPizzaID`,`PizzaDiscountDiscountID`),
  KEY `PizzaDiscountDiscountIDFK2_IDx` (`PizzaDiscountDiscountID`),
  CONSTRAINT `PizzaDiscountDiscountIDFK2` FOREIGN KEY (`PizzaDiscountDiscountID`) REFERENCES `Discount` (`DiscountID`),
  CONSTRAINT `PizzaDiscountPizzaIDFK1` FOREIGN KEY (`PizzaDiscountPizzaID`) REFERENCES `Pizza` (`PizzaID`)
) ;

CREATE TABLE `OrderDiscount` (
  `OrderDiscountOrderID` int NOT NULL,
  `OrderDiscountDiscountID` int NOT NULL,
  PRIMARY KEY (`OrderDiscountOrderID`,`OrderDiscountDiscountID`),
  KEY `OrderDiscountDiscountNum_IDx` (`OrderDiscountDiscountID`),
  CONSTRAINT `OrderDiscountDiscountIDFK2` FOREIGN KEY (`OrderDiscountDiscountID`) REFERENCES `Discount` (`DiscountID`),
  CONSTRAINT `OrderDiscountOrderIDFK1` FOREIGN KEY (`OrderDiscountOrderID`) REFERENCES `Order` (`OrderID`)
);

ALTER TABLE `PizzaTopping` 
ADD INDEX `PizzaToppingToppingID_fk2_IDx` (`PizzaToppingToppingID` ASC) VISIBLE;
;
ALTER TABLE `PizzaTopping` 
ADD CONSTRAINT `pizza_fk1`
  FOREIGN KEY (`PizzaToppingPizzaID`)
  REFERENCES `Pizza` (`PizzaID`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `topping_fk2`
  FOREIGN KEY (`PizzaToppingToppingID`)
  REFERENCES `Topping` (`ToppingID`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;