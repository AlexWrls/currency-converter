create table public.convert
(
    id           BIGSERIAL        NOT NULL PRIMARY KEY,
    convert_from varchar(100)     not null,
    convert_to   varchar(100)     not null,
    value_from   double precision not null,
    value_to     double precision not null,
    curs_date    date             not null
);

create table public.currency_item
(
    id        varchar(50)      not null unique,
    num_code  varchar(50)      not null,
    char_code varchar(50)      not null,
    nominal   integer          not null,
    name      varchar(100)     not null,
    value     double precision not null,
    curs_date date             not null
);
