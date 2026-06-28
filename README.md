# CS Database Systems Portfolio

Selected database systems work from University of Washington CS coursework, cleaned for portfolio review.

This repository focuses on practical SQL, relational design, semi-structured querying, and a Java/JDBC scheduling application. Course prompts, grading artifacts, hidden tests, and instructor materials are intentionally omitted.

## Projects

### Vaccine Scheduler

`vaccine-scheduler/` contains a Java command-line application backed by Microsoft SQL Server.

Highlights:

- Relational schema for patients, caregivers, vaccines, availabilities, and reservations.
- Java/JDBC data access with prepared statements.
- Password hashing and salting for patient and caregiver login.
- Appointment search, reservation, cancellation, availability upload, and dose management flows.
- Database credentials loaded from environment variables rather than source code.

### SQL Query Practice

`sql/relational-query-practice/` contains selected SQL queries and schema setup files covering filtering, joins, aggregation, grouping, and relational query composition.

### Schema Design

`sql/schema-design/` contains SQL work focused on schema reasoning, constraints, and functional-dependency-style analysis.

### Semi-Structured Query

`sql/semi-structured-query/` contains SQL++ style queries over semi-structured data.

## Tech Stack

- SQL
- Microsoft SQL Server
- Java
- JDBC
- Relational schema design
- Semi-structured query languages

## Notes on Source

The vaccine scheduler public copy preserves the third-party scaffold license included with the original project. The visible repository contains completed source code and portfolio notes only; it does not include assignment prompts, autograder tests, feedback, or grade information.

