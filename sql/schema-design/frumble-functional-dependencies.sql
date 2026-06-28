CREATE TABLE Sales (
    name VARCHAR(255),
    discount REAL,
    month VARCHAR(50),
    price REAL
);

-- Assume data is loaded from mrFrumbleData.txt into the Sales table

-- Check if name -> discount
-- If name determines discount, then the same name should always have the same discount.
SELECT name, COUNT(DISTINCT discount) AS discount_count
FROM Sales
GROUP BY name
HAVING discount_count > 1;
-- If result set is empty, then name -> discount holds.

-- Check if name, month -> price
-- If name and month determine price, the same name and month combination should always have the same price.
SELECT name, month, COUNT(DISTINCT price) AS price_count
FROM Sales
GROUP BY name, month
HAVING price_count > 1;
-- If result set is empty, then name, month -> price holds.

-- Check if name, discount -> month
-- If name and discount determine month, the same combination should always have the same month.
SELECT name, discount, COUNT(DISTINCT month) AS month_count
FROM Sales
GROUP BY name, discount
HAVING month_count > 1;
-- If result set is empty, then name, discount -> month holds.

-- Example of a dependency that does not exist
-- Check if discount -> name
SELECT discount, COUNT(DISTINCT name) AS name_count
FROM Sales
GROUP BY discount
HAVING name_count > 1;
-- If result set is not empty, then discount -> name does not hold.

-- Based on the functional dependencies found:
-- name -> discount
-- name, month -> price

-- Decompose Sales into two tables to satisfy BCNF
CREATE TABLE Product (
    name VARCHAR(255) PRIMARY KEY,
    discount REAL
);

CREATE TABLE Sales_Details (
    name VARCHAR(255),
    month VARCHAR(50),
    price REAL,
    PRIMARY KEY (name, month),
    FOREIGN KEY (name) REFERENCES Product(name)
);

-- Load data into Product table
INSERT INTO Product (name, discount)
SELECT DISTINCT name, discount
FROM Sales;

-- Load data into Sales_Details table
INSERT INTO Sales_Details (name, month, price)
SELECT name, month, price
FROM Sales;

-- Comment the size of the tables after loading
-- Check the number of rows in Product table
SELECT COUNT(*) FROM Product; -- Expected result: number of distinct names in Sales table

-- Check the number of rows in Sales_Details table
SELECT COUNT(*) FROM Sales_Details; -- Expected result: number of rows in Sales table
