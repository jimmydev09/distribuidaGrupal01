CREATE TABLE public.authors
(
    id      SERIAL,
    name    VARCHAR(255),
    version INTEGER STORAGE PLAIN,
    CONSTRAINT author_pkey PRIMARY KEY (id)
);

insert into authors(name, version) values('Lord Byron', 1);
insert into authors(name, version) values('Percy Bysshe Shelley', 1);
insert into authors(name, version) values('Mary Shelley', 1);
insert into authors(name, version) values('John Keats', 1);
insert into authors(name, version) values('William Wordsworth', 1);
insert into authors(name, version) values('Samuel Taylor Coleridge', 1);
insert into authors(name, version) values('William Blake', 1);
insert into authors(name, version) values('Edgar Allan Poe', 1);
insert into authors(name, version) values('Nathaniel Hawthorne', 1);
insert into authors(name, version) values('Washington Irving', 1);
insert into authors(name, version) values('Victor Hugo', 1);
insert into authors(name, version) values('Alexandre Dumas', 1);
insert into authors(name, version) values('George Sand', 1);
insert into authors(name, version) values('Alphonse de Lamartine', 1);
insert into authors(name, version) values('Goethe', 1);
insert into authors(name, version) values('Heinrich Heine', 1);
insert into authors(name, version) values('E.T.A. Hoffmann', 1);
insert into authors(name, version) values('Novalis', 1);
insert into authors(name, version) values('Giacomo Leopardi', 1);
insert into authors(name, version) values('Alessandro Manzoni', 1);


insert into authors(name, version) values('Neil Gaiman', 1);
insert into authors(name, version) values('Terry Pratchett', 1);
insert into authors(name, version) values('Douglas Preston', 1);
insert into authors(name, version) values('Lincoln Child', 1);
insert into authors(name, version) values('James Patterson', 1);
insert into authors(name, version) values('Andrew Gross', 1);
insert into authors(name, version) values('Stephen King', 1);
insert into authors(name, version) values('Peter Straub', 1);
insert into authors(name, version) values('Larry Niven', 1);
insert into authors(name, version) values('Jerry Pournelle', 1);
insert into authors(name, version) values('Frederik Pohl', 1);
insert into authors(name, version) values('C.M. Kornbluth', 1);
insert into authors(name, version) values('Isaac Asimov', 1);
insert into authors(name, version) values('Robert Silverberg', 1);
insert into authors(name, version) values('Ilona Andrews', 1);
insert into authors(name, version) values('Gordon Andrews', 1);
insert into authors(name, version) values('Ann McCaffrey', 1);
insert into authors(name, version) values('Mercedes Lackey', 1);
insert into authors(name, version) values('Will Durant', 1);
insert into authors(name, version) values('Ariel Durant', 1);

