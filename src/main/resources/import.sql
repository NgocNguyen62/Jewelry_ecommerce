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
    `create_by` varchar(100),
    `update_at` DATETIME,
    `update_by` varchar(100)

) ENGINE = InnoDB;
INSERT INTO user(email, password, role, first_name, last_name, phone, gender, address, user_status )
VALUES
    ('admin', '$2a$10$LgU7ud6cjVmk2e6l8zzBlOsHrlaX8X7w8TOZnaSeezrsYI6EK6NlW', 'ADMIN', 'firstName', 'lastName', 0123456789, 'Nữ', 'test',0),
    ('user', 123, 'USER', 'firstName', 'lastName', 0123456789, 'Nữ', 'test',0);

DROP TABLE if exists `category`;
CREATE table `category`(
    `id` bigint primary key AUTO_INCREMENT,
    `categoryName` varchar(255) not null ,
    `categoryDescription` text,
    `categoryStatus` bit(1) not null
) ENGINE = InnoDB;

