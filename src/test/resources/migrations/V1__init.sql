drop table if exists users;
create table users
(
    id       bigint       NOT NULL auto_increment,
    username varchar(50)  not null,
    password varchar(255) not null,
    enabled  boolean      not null,
    email    varchar(255) not null,
    object   varchar(4048)         NOT NULL DEFAULT '{}',
    CONSTRAINT pk_users PRIMARY KEY (id)
);