CREATE TABLE parcel_truck (
                              id serial PRIMARY KEY,
                              truck_id int NOT NULL,
                              parcel_name VARCHAR(255) NOT NULL,
                              coordinates VARCHAR(1000) NOT NULL,
                              UNIQUE (truck_id, parcel_name),
                              CONSTRAINT fk_truck FOREIGN KEY (truck_id) REFERENCES truck(id),
                              CONSTRAINT fk_parcel FOREIGN KEY (parcel_name) REFERENCES parcel(name)
);