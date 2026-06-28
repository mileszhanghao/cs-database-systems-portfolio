CREATE TABLE Person (
    ssn INTEGER PRIMARY KEY,
    name VARCHAR(255)
);

CREATE TABLE Driver (
    ssn INTEGER PRIMARY KEY,
    driverID INTEGER,
    FOREIGN KEY (ssn) REFERENCES Person(ssn)
);

CREATE TABLE NonProfessionalDriver (
    ssn INTEGER PRIMARY KEY,
    FOREIGN KEY (ssn) REFERENCES Driver(ssn)
);

CREATE TABLE ProfessionalDriver (
    ssn INTEGER PRIMARY KEY,
    medical_history TEXT,
    FOREIGN KEY (ssn) REFERENCES Driver(ssn)
);

CREATE TABLE Vehicle (
    license_plate VARCHAR(20) PRIMARY KEY,
    year INTEGER,
    owner_ssn INTEGER,
    insurance_co_name VARCHAR(255),
    max_liability REAL,
    FOREIGN KEY (owner_ssn) REFERENCES Person(ssn),
    FOREIGN KEY (insurance_co_name) REFERENCES InsuranceCo(name)
);

CREATE TABLE Car (
    license_plate VARCHAR(20) PRIMARY KEY,
    make VARCHAR(255),
    FOREIGN KEY (license_plate) REFERENCES Vehicle(license_plate)
);

CREATE TABLE Truck (
    license_plate VARCHAR(20) PRIMARY KEY,
    capacity INTEGER,
    professional_driver_ssn INTEGER,
    FOREIGN KEY (license_plate) REFERENCES Vehicle(license_plate),
    FOREIGN KEY (professional_driver_ssn) REFERENCES ProfessionalDriver(ssn)
);

CREATE TABLE InsuranceCo (
    name VARCHAR(255) PRIMARY KEY,
    phone INTEGER
);

CREATE TABLE Drives (
    driver_ssn INTEGER,
    car_license_plate VARCHAR(20),
    PRIMARY KEY (driver_ssn, car_license_plate),
    FOREIGN KEY (driver_ssn) REFERENCES Driver(ssn),
    FOREIGN KEY (car_license_plate) REFERENCES Car(license_plate)
);

CREATE TABLE Owns (
    license_plate VARCHAR(20),
    ssn INTEGER,
    PRIMARY KEY (license_plate, ssn),
    FOREIGN KEY (license_plate) REFERENCES Vehicle(license_plate),
    FOREIGN KEY (ssn) REFERENCES Person(ssn)
);

CREATE TABLE Insures (
    license_plate VARCHAR(20),
    name VARCHAR(255),
    max_liability REAL,
    PRIMARY KEY (license_plate, name),
    FOREIGN KEY (license_plate) REFERENCES Vehicle(license_plate),
    FOREIGN KEY (name) REFERENCES InsuranceCo(name)
);

CREATE TABLE Operates (
    license_plate VARCHAR(20),
    driver_ssn INTEGER,
    PRIMARY KEY (license_plate, driver_ssn),
    FOREIGN KEY (license_plate) REFERENCES Truck(license_plate),
    FOREIGN KEY (driver_ssn) REFERENCES ProfessionalDriver(ssn)
);

-- Answer to question (b):
-- The relation in the relational schema that represents the "insures" relationship is the "Insures" table.
-- This table represents the relationship because it links the "InsuranceCo" and "Vehicle" entities, indicating which insurance company insures which vehicle. The table also includes the attribute maxLiability, which is an attribute of the insurance relationship.
-- Since each vehicle is insured by only one insurance company, this multi-to-one relationship is captured with a foreign key in the Insures table.

-- Answer to question (c):
-- The relationships "drives" and "operates" are represented differently because they involve different cardinalities and types of drivers and vehicles.
-- The "Drives" relationship is a many-to-many relationship between NonProfessionalDrivers and Cars. This requires a separate "Drives" table to establish the link, with foreign keys referring to both the driver and the car.
-- The "Operates" relationship, on the other hand, is a one-to-many relationship between Trucks and ProfessionalDrivers. Since each truck is operated by only one professional driver, this relationship is captured using a foreign key (professional_driver_ssn) in the Truck table, avoiding the need for a separate relationship table.
