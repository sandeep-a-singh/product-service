drop table orders if exists;
drop table user if exists;

CREATE TABLE product (
                      id int(11) NOT NULL AUTO_INCREMENT,
                      name varchar(50) DEFAULT NULL,
                      quantity int(11) DEFAULT NULL,
                      expiry date DEFAULT NULL,
                      price double DEFAULT NULL,
                      PRIMARY KEY (id)
);


INSERT INTO product
VALUES
    (1,'shoes',5,'2022-11-13', 28),
    (2,'trimmer',2,'2023-01-02', 20),
    (3,'blazer',10,'2024-10-10', 68),
    (4,'webcam',3,'2024-12-14', 35),
    (5,'mouse',4,'2025-09-19',10),
    (6,'keyboard',5,'2026-08-17',15),
    (7,'cabinet',6,'2023-05-10',12),
    (8,'iphone',8,'2024-06-11',500);