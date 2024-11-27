create table public.articles (
    id int GENERATED ALWAYS AS IDENTITY,
    article varchar UNIQUE not null,
    title varchar not null,
    description varchar,
    paper varchar,
    amount varchar,
    author varchar,
    note varchar,
    grouper int not null,
    date timestamp not null,
    PRIMARY KEY (id)
);

insert into articles (article, title, description, paper, amount, author, note, grouper, date)
values ('KK25A', 'Табличка в шахту', 'надпись вверх там', 'A4', '1', 'Татаринов А', 'Навигация в шахте', 25, '2021-02-13');




