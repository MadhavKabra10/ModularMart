INSERT INTO category (id, description, name) VALUES
(nextval('category_seq'), 'Electronics and gadgets', 'Electronics'),
(nextval('category_seq'), 'Books and educational materials', 'Books'),
(nextval('category_seq'), 'Home and kitchen appliances', 'Home & Kitchen');

-- Inserting initial data into the product table
INSERT INTO product (id, description, name, available_quantity, price, category_id) VALUES
(nextval('product_seq'), 'Laptop with 16GB RAM and 512GB SSD', 'Laptop', 50, 80000, (SELECT id FROM category WHERE name = 'Electronics')),
(nextval('product_seq'), 'Smartphone with 6GB RAM and 128GB storage', 'Smartphone', 100, 30000, (SELECT id FROM category WHERE name = 'Electronics')),
(nextval('product_seq'), 'Non-stick frying pan', 'Frying Pan', 200, 5000, (SELECT id FROM category WHERE name = 'Home & Kitchen')),
(nextval('product_seq'), 'Microwave oven with grill function', 'Microwave Oven', 80, 20000, (SELECT id FROM category WHERE name = 'Home & Kitchen')),
(nextval('product_seq'), 'Hardcover book on Java programming', 'Java Programming Book', 150, 3000, (SELECT id FROM category WHERE name = 'Books'));
