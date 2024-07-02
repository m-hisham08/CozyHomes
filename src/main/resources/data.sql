-- Inserting roles
INSERT INTO roles(created_at,last_modified_at,name) VALUES(NOW(),NOW(),'ROLE_USER');
INSERT INTO roles(created_at,last_modified_at,name) VALUES(NOW(),NOW(),'ROLE_ADMIN');

--Creates an user with role ADMIN and password: 852123hh
INSERT INTO users (phone, first_name, last_name, username, email, password, created_at, last_modified_at)
VALUES ('+911234565475', 'John', 'Doe', 'john-08', 'john@example.com', '$2a$10$4cPFQgVNtnN8IkMOQTfYbePlW.jXxxuUar33L8NnFNCBtYXboBS3y', NOW(), NOW());

-- Allots ADMIN role for the user
INSERT INTO user_roles(user_id, role_id) VALUES (1, 2);

-- MOCK VALUES FOR CATEGORIES, PRODUCTS AND REVIEWS TO BEGIN WITH
INSERT INTO categories (name, created_at, last_modified_at, created_by, last_modified_by)
VALUES ('Beauty', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 1),
       ('Electronics', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 1),
       ('Homeware', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 1),
       ('Fashion', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 1),
       ('Toys', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 1);

INSERT INTO products (name, description, imageurl, price, category_id, stock, created_at, last_modified_at, created_by, last_modified_by)
VALUES ('Luxurious Face Cream', 'A luxurious face cream that hydrates and nourishes your skin.', 'https://example.com/facecream.jpg', 49.99, 1, 10, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 1),
       ('Wireless Headphones', 'High-fidelity wireless headphones with long battery life.', 'https://example.com/headphones.jpg', 129.99, 2, 25, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 1),
       ('Comfy Sofa Set', 'A comfortable and stylish sofa set for your living room.', 'https://example.com/sofaset.jpg', 399.99, 3, 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 1),
       ('Summer Dress', 'A flowy and stylish summer dress.', 'https://example.com/dress.jpg', 34.99, 4, 15, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 1),
       ('Wooden Train Set', 'A classic wooden train set for children.', 'https://example.com/trainset.jpg', 29.99, 5, 20, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 1);

INSERT INTO reviews (rating, comment, created_at, last_modified_at, created_by, last_modified_by, product_id)
VALUES (4.5, 'This product is amazing! Highly recommend!', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 1, 1),
       (3.8, 'Good product, but could be improved.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 1, 2),
       (5.0, 'Love this product! Will definitely buy again.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 1, 3),
       (4.2, 'Nice product, but a bit pricey.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 1, 4),
       (4.7, 'Great quality product, very satisfied.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 1, 1, 5);
