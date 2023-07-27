create table usuarios(

    id serial PRIMARY KEY,
    login varchar(100) not null,
    senha varchar(255) not null
);