create table public.users
(
    id       serial primary key,
    username varchar not null unique,
    password varchar not null
);

create table public.roles
(
    id       serial primary key,
    name varchar(50) not null
);

create table public.user_roles
(
    user_id int not null references users,
    role_id int not null references roles,
    unique (user_id, role_id)
);

insert into users (id, username, password)
values (1, 'engineer', 'Meteorart32');

insert into users (id, username, password)
values (2, 'view', 'view');

insert into users (id, username, password)
values (3, 'admin', 'cVC3bGPBrVO2U1e');

insert into roles (id, name)
values (1, 'ROLE_USER');

insert into roles (id, name)
values (2, 'ROLE_VIEW');

insert into roles (id, name)
values (3, 'ROLE_ADMIN');

insert into user_roles (user_id, role_id)
values (1, 1);

insert into user_roles (user_id, role_id)
values (2, 2);

insert into user_roles (user_id, role_id)
values (3, 3);
