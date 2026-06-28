CREATE TABLE Patients (
    Username VARCHAR(255) PRIMARY KEY,
    Salt BINARY(16),
    Hash BINARY(16)
);

CREATE TABLE Caregivers (
    Username VARCHAR(255) PRIMARY KEY,
    Salt BINARY(16),
    Hash BINARY(16)
);

CREATE TABLE Vaccines (
    Name VARCHAR(255) PRIMARY KEY,
    Doses INT
);

CREATE TABLE Availabilities (
    AvailabilityID INT IDENTITY(1,1) PRIMARY KEY,
    Username VARCHAR(255),
    AvailableDate DATE,
    FOREIGN KEY (Username) REFERENCES Caregivers(Username),
    CONSTRAINT unique_availability UNIQUE (AvailableDate, Username)
);

CREATE TABLE Reservations (
    ReservationID INT IDENTITY(1,1) PRIMARY KEY,
    PatientUsername VARCHAR(255),
    CaregiverUsername VARCHAR(255),
    VaccineName VARCHAR(255),
    ReservationDate DATE,
    FOREIGN KEY (PatientUsername) REFERENCES Patients(Username),
    FOREIGN KEY (CaregiverUsername) REFERENCES Caregivers(Username),
    FOREIGN KEY (VaccineName) REFERENCES Vaccines(Name),
    CONSTRAINT unique_reservation UNIQUE (PatientUsername, CaregiverUsername, ReservationDate)
);