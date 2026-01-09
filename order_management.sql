CREATE DATABASE order_management;
USE order_management;

-- =====================
-- USER
-- =====================
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    status ENUM('ACTIVE','BLOCKED') NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME
);

CREATE TABLE user_credential (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    last_login_at DATETIME,
    CONSTRAINT fk_credential_user FOREIGN KEY (user_id) REFERENCES users(id)
);

-- =====================
-- ROLE
-- =====================
CREATE TABLE roles (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255)
);

CREATE TABLE user_role (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user_role_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_user_role_role FOREIGN KEY (role_id) REFERENCES roles(id)
);

-- =====================
-- PRODUCT
-- =====================
CREATE TABLE products (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(150) NOT NULL,
    price DECIMAL(15,2) NOT NULL,
    stock INT NOT NULL,
    active BOOLEAN DEFAULT TRUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME
);

CREATE TABLE categories (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(255),
    active BOOLEAN DEFAULT TRUE
);

CREATE TABLE product_category (
    product_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    PRIMARY KEY (product_id, category_id),
    CONSTRAINT fk_pc_product FOREIGN KEY (product_id) REFERENCES products(id),
    CONSTRAINT fk_pc_category FOREIGN KEY (category_id) REFERENCES categories(id)
);

-- =====================
-- ORDER
-- =====================
CREATE TABLE orders (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    total_amount DECIMAL(15,2) NOT NULL,
    status ENUM('NEW','PAID','CANCELED') NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME,
    CONSTRAINT fk_order_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE order_item (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    price DECIMAL(15,2) NOT NULL,
    CONSTRAINT fk_order_item_order FOREIGN KEY (order_id) REFERENCES orders(id),
    CONSTRAINT fk_order_item_product FOREIGN KEY (product_id) REFERENCES products(id)
);

CREATE TABLE order_status_history (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_id BIGINT NOT NULL,
    old_status ENUM('NEW','PAID','CANCELED'),
    new_status ENUM('NEW','PAID','CANCELED'),
    changed_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    changed_by BIGINT,
    CONSTRAINT fk_osh_order FOREIGN KEY (order_id) REFERENCES orders(id),
    CONSTRAINT fk_osh_user FOREIGN KEY (changed_by) REFERENCES users(id)
);

-- =====================
-- PAYMENT
-- =====================
CREATE TABLE payment (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_id BIGINT NOT NULL,
    amount DECIMAL(15,2) NOT NULL,
    method ENUM('COD','BANK','MOMO','VNPAY') NOT NULL,
    status ENUM('PENDING','SUCCESS','FAILED') NOT NULL,
    transaction_id VARCHAR(100),
    paid_at DATETIME,
    CONSTRAINT fk_payment_order FOREIGN KEY (order_id) REFERENCES orders(id)
);

-- USERS
INSERT INTO users (name, email, status) VALUES
('Nguyen Van A', 'a@gmail.com', 'ACTIVE'),
('Tran Thi B', 'b@gmail.com', 'ACTIVE'),
('Admin', 'admin@gmail.com', 'ACTIVE');

-- USER CREDENTIAL
INSERT INTO user_credential (user_id, username, password) VALUES
(1, 'usera', '$2a$10$hashpasswordA'),
(2, 'userb', '$2a$10$hashpasswordB'),
(3, 'admin', '$2a$10$hashpasswordAdmin');

-- ROLES
INSERT INTO roles (name, description) VALUES
('USER', 'Normal user'),
('ADMIN', 'Administrator');

-- USER_ROLE
INSERT INTO user_role (user_id, role_id) VALUES
(1, 1),
(2, 1),
(3, 2);
-- CATEGORIES
INSERT INTO categories (name, description) VALUES
('Electronics', 'Electronic devices'),
('Books', 'Books & documents');

-- PRODUCTS
INSERT INTO products (name, price, stock) VALUES
('Laptop Dell', 1500.00, 10),
('iPhone 15', 1200.00, 5),
('Clean Code Book', 45.00, 100);

-- PRODUCT_CATEGORY
INSERT INTO product_category (product_id, category_id) VALUES
(1, 1),
(2, 1),
(3, 2);




