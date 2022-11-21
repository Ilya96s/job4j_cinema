CREATE TABLE tickets (
    id SERIAL PRIMARY KEY,
    session_id INT NOT NULL REFERENCES sessions(id),
    pos_row INT NOT NULL,
    cell INT NOT NULL,
    user_id INT NOT NULL REFERENCES users(id),
    CONSTRAINT unique_ticket UNIQUE (session_id, pos_row, cell)
);

comment on table tickets is 'Билеты';
comment on column tickets.id is 'Идентификатор билета';
comment on column tickets.session_id is 'идентификатор сеанса';
comment on column tickets.pos_row is 'ряд';
comment on column tickets.cell is 'место';
comment on column tickets.user_id is 'идентификатор пользователя';