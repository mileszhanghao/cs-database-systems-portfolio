# Database Systems and Backend Portfolio

Backend and database engineering work organized as product-style systems: a healthcare appointment scheduler, relational query sets, schema design exercises, and semi-structured data querying.

## Product Framing

This repository demonstrates how I model business workflows as database-backed applications: define entities and constraints, write safe SQL, connect application code through JDBC, and keep credentials out of source control.

## Projects

| Project | Product-style focus | Stack |
| --- | --- | --- |
| [Healthcare appointment scheduler](vaccine-scheduler/) | Account creation, authentication, provider availability, inventory, booking, cancellation | Java, JDBC, SQL Server |
| [Relational query practice](sql/relational-query-practice/) | Analytics queries over normalized relational data | SQL, joins, aggregation |
| [Schema design](sql/schema-design/) | Entity modeling, constraints, dependency reasoning | SQL, relational design |
| [Semi-structured query](sql/semi-structured-query/) | Querying nested data and document-like records | SQL-style query language |

## Engineering Highlights

- Designed relational tables for patients, caregivers, vaccines, availability, and reservations.
- Implemented Java/JDBC data access using prepared statements.
- Used salted password hashing rather than plaintext credential storage.
- Modeled appointment workflows with search, reservation, cancellation, availability upload, and dose management.
- Loaded database credentials from environment variables.
- Organized SQL work around filtering, joins, grouping, aggregation, constraints, and schema reasoning.

## Target Roles Signaled

Backend engineer, data engineer, database developer, analytics engineer, and software-oriented data analyst.

## Publication Boundary

The public repo keeps completed source code and portfolio-safe notes. Private evaluation artifacts and credentials are excluded.
