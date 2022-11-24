CREATE TABLE sessions (
    id SERIAL PRIMARY KEY,
    title text,
    description text,
    photo bytea
);

comment on table sessions is 'Сеансы';
comment on column sessions.id is 'Идентификатор сеанса';
comment on column sessions.title is 'Название сеанса';
comment on column sessions.description is 'Описание сеанса';
comment on column sessions.photo is 'Постер сеанса';