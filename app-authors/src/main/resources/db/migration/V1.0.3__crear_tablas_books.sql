CREATE TABLE books
(
    isbn    VARCHAR(255) NOT NULL,
    title   VARCHAR(128),
    price   DECIMAL(12, 2),
    version INTEGER,
    CONSTRAINT pk_books PRIMARY KEY (isbn)
);

insert into books (isbn, title, price, version) values('9780060853976', 'Good Omens', 15.99, 1);
insert into books (isbn, title, price, version) values('9780765356130', 'The Talisman', 18.50, 1);
insert into books (isbn, title, price, version) values('9780345391803', 'The Mote in Gods Eye', 14.95, 1);
insert into books (isbn, title, price, version) values('9780307887894', 'Relic', 16.99, 1);
insert into books (isbn, title, price, version) values('9781455515789', 'Along Came a Spider', 12.99, 1);
insert into books (isbn, title, price, version) values('9780345464187', 'Ringworld', 13.95, 1);
insert into books (isbn, title, price, version) values('9780671578275', 'The Space Merchants', 11.99, 1);
insert into books (isbn, title, price, version) values('9780345334312', 'Nightfall', 14.50, 1);
insert into books (isbn, title, price, version) values('9780425286654', 'Magic Bites', 13.99, 1);
insert into books (isbn, title, price, version) values('9780345368959', 'Dragonriders of Pern', 15.95, 1);
insert into books (isbn, title, price, version) values('9781451648539', 'The Story of Civilization', 29.99, 1);
insert into books (isbn, title, price, version) values('9780765304368', 'Black House', 17.95, 1);
insert into books (isbn, title, price, version) values('9780451457998', 'Reliquary', 16.50, 1);
insert into books (isbn, title, price, version) values('9781455521548', 'Cross Fire', 13.50, 1);
insert into books (isbn, title, price, version) values('9780345419699', 'Lucifers Hammer', 16.95, 1);
insert into books (isbn, title, price, version) values('9780553573329', 'Gateway', 12.95, 1);
insert into books (isbn, title, price, version) values('9780553294385', 'The Positronic Man', 13.95, 1);
insert into books (isbn, title, price, version) values('9780425286661', 'Magic Burns', 14.99, 1);
insert into books (isbn, title, price, version) values('9780345394682', 'Dragonflight', 14.95, 1);
insert into books (isbn, title, price, version) values('9781451648546', 'Caesar and Christ', 24.99, 1);
CREATE TABLE inventory
(
    isbn     VARCHAR(255) NOT NULL,
    sold     INTEGER,
    supplied INTEGER,
    CONSTRAINT pk_inventory PRIMARY KEY (isbn)
);

ALTER TABLE inventory
    ADD CONSTRAINT FK_INVENTORY_ON_ISBN FOREIGN KEY (isbn) REFERENCES books (isbn);

ALTER TABLE books_authors
    ADD CONSTRAINT FK_BOOKS_AUTHORS_ON_BOOKS FOREIGN KEY (books_isbn) REFERENCES books (isbn);