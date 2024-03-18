drop table if exists `user`;
CREATE TABLE `user`(
    `id` bigint primary key AUTO_INCREMENT,
    `email` varchar(100) not null unique ,
    `password` varchar(255) not null,
    `role` varchar(20) not null,
    `first_name` varchar(100) not null,
    `last_name` varchar(100) not null,
    `birthday` DATE,
    `phone` varchar(10) not null ,
    `gender` varchar(20) not null ,
    `address` varchar(255) not null,
    `user_status` bit(1) not null,
    `create_at` DATETIME,
    `create_by` bigint,
    `update_at` DATETIME,
    `update_by` bigint

) ENGINE = InnoDB;
INSERT INTO user(email, password, role, first_name, last_name, phone, gender, address, user_status )
VALUES
    ('admin', '$2a$10$LgU7ud6cjVmk2e6l8zzBlOsHrlaX8X7w8TOZnaSeezrsYI6EK6NlW', 'ADMIN', 'firstName', 'lastName', 0123456789, 'Nữ', 'test',0),
    ('user', 123, 'USER', 'firstName', 'lastName', 0123456789, 'Nữ', 'test',0);

DROP TABLE if exists `category`;
CREATE table `category`(
    `id` bigint primary key AUTO_INCREMENT,
    `category_name` varchar(255) not null ,
    `category_description` text,
    `category_status` bit(1) not null,
    `create_at` DATETIME,
    `create_by` bigint,
    `update_at` DATETIME,
    `update_by` bigint
) ENGINE = InnoDB;

drop table if exists `products`;
create table `products`(
    `id` bigint primary key AUTO_INCREMENT,
    `product_name` varchar(255) not null ,
    `category_id` bigint ,
    `gender` varchar(30),
    `avatar` varchar(255),
    `images` varchar(500),
    `product_description` TEXT,
    `price` double not null ,
    `discount` double,
    `quantity` int,
    `product_status` bit(1) not null ,
    `sales` int default 0,
    `create_at` DATETIME,
    `create_by` bigint,
    `update_at` DATETIME,
    `update_by` bigint,
    foreign key (category_id) references category(id)
);

create table `cart` (
    `id` bigint primary key AUTO_INCREMENT,
    `user_id` bigint,
    foreign key (user_id) references user(id)
);

create table `cart_items`(
    `id` bigint primary key  auto_increment,
    `cart_id` bigint,
    `product_id` bigint,
    `quantity` int default 1,
    foreign key (cart_id) references cart(id),
    foreign key (product_id) references products(id)
);
drop table if exists `orders`;
create table `orders`(
    `id` bigint primary key auto_increment,
    `user_id` bigint,
    `shipping` double,
    `order_status` varchar(50),
    `time_order` datetime,
    `total` double,
    `receiver_name` varchar(255),
    `receiver_phone` varchar(10),
    `receiver_address` varchar(255),
    `note` varchar(255),
    foreign key (user_id) references user(id)
);
drop table if exists `order_items`;
create table `order_items`(
    `id` bigint primary key auto_increment,
    `order_id` bigint,
    `product_id` bigint,
    `product_price` double,
    `quantity` int,
    foreign key (order_id) references `orders`(id),
    foreign key (product_id) references products(id)
);

drop table if exists `rate`;
create table `rate`(
    `id` bigint primary key auto_increment,
    `user_id` bigint,
    `product_id` bigint,
    `stars` int,
    `comment` varchar(255),
    `time` DATETIME,
    foreign key (user_id) references `user`(id),
    foreign key (product_id) references `products`(id)
);

drop table if exists `favorite`;
create table `favorite`(
    `id` bigint primary key auto_increment,
     `user_id` bigint,
     `product_id` bigint,
    foreign key (user_id) references `user`(id),
    foreign key (product_id) references `products`(id)
)


