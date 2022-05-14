create table votes (
    user_id varchar(36) not null primary key,
    value varchar(1) not null,
    check(value in ('Y', 'N'))
);