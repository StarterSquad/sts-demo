
    create table Author (
        id int8 not null,
        firstName varchar(255),
        lastName varchar(255),
        primary key (id)
    );

    create table Book (
        id int8 not null,
        publicationDate timestamp,
        title varchar(255),
        author_id int8,
        primary key (id)
    );

    alter table Book 
        add constraint FK1FAF0945D90B90 
        foreign key (author_id) 
        references Author;

    create sequence hibernate_sequence;
