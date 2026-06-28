# Healthcare Appointment Scheduling Backend

A Java command-line scheduling backend for appointment booking, provider availability, vaccine inventory, and account authentication, backed by a relational database.

## Product Capabilities

- Creates and authenticates patient and caregiver accounts.
- Stores password salts and hashes instead of plaintext passwords.
- Lets caregivers publish availability and manage vaccine doses.
- Lets patients search open slots, reserve appointments, cancel appointments, and view scheduled bookings.
- Uses SQL constraints to protect unique availability and reservation records.
- Reads database configuration from environment variables so credentials are not committed.

## Engineering Focus

- Java service logic for a stateful booking workflow.
- JDBC prepared statements for database access.
- Relational schema design for users, inventory, availability, and reservations.
- Authentication flow with salted password hashing.
- Transaction-oriented command-line application behavior.

## Project Structure

```text
src/main/java/scheduler/
  Scheduler.java
  db/ConnectionManager.java
  model/Caregiver.java
  model/Patient.java
  model/Vaccine.java
  util/Util.java
sql/schema.sql
```

## Database Configuration

`ConnectionManager` reads database settings from environment variables:

```text
Server
DBName
UserID
Password
```

No credentials are committed to this repository.

## Portfolio Context

This project demonstrates backend application logic, relational modeling, secure credential handling, and SQL-backed workflow design. Private evaluation artifacts are excluded.
