-- Billing Records Table
create table billing_record (
                                 id serial primary key,
                                 "user" varchar(255) not null,
                                 timestamp timestamp not null,
                                 operation_type varchar(255),
                                 segments int,
                                 parcels int,
                                 cost int
);
