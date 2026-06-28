# Vaccine Scheduler

A Java command-line scheduling system for vaccine appointments backed by a relational database.

## What It Does

- Creates and authenticates patient and caregiver accounts.
- Stores password salts and hashes instead of plaintext passwords.
- Lets caregivers upload availability and add vaccine doses.
- Lets patients search availability, reserve appointments, cancel appointments, and view scheduled appointments.
- Uses SQL constraints to model unique availability and reservation records.

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

This project demonstrates relational schema design, JDBC programming, prepared statements, basic authentication flows, and transaction-oriented application logic. Course instructions, tests, and grading artifacts are excluded.

