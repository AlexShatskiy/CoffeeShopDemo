-- Table coffee--
create table coffee(
id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
name varchar(200) not null,
description text,
price FLOAT);

--insert coffee--
INSERT INTO coffee (name, description, price) 
VALUES ("Espresso (Short Black) mySQL", 
"The espresso (aka “short black”) is the foundation and the most important part to every espresso based drink.",
1);
INSERT INTO coffee (name, description, price) 
VALUES ("Double Espresso (Doppio) mySQL", 
"The espresso (aka “short black”) is the foundation and the most important part to every espresso based drink.",
2);
INSERT INTO coffee (name, description, price) 
VALUES ("Short Macchiato mySQL", 
"The espresso (aka “short black”) is the foundation and the most important part to every espresso based drink.",
3);
INSERT INTO coffee (name, description, price) 
VALUES ("Long Macchiato mySQL", 
"The espresso (aka “short black”) is the foundation and the most important part to every espresso based drink.",
4);

create table orderitem(
id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
coffee_id INT NOT NULL,
order_id INT NOT NULL,
quantity int,
foreign key (coffee_id) references coffee(id),
foreign key (order_id) references orders(id)
);

create table orders(
id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
customername varchar(200),
customeraddress varchar(200), 
phone varchar(200), 
status int,
price float
);