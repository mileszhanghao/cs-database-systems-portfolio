WITH AllFlights AS (
    SELECT DISTINCT origin_city, dest_city
    FROM Flights
    WHERE actual_time IS NOT NULL
),
ReachableCities AS (
    SELECT dest_city
    FROM AllFlights
    WHERE origin_city = 'Seattle WA'
    UNION
    SELECT af2.dest_city
    FROM AllFlights af1
    JOIN AllFlights af2 ON af1.dest_city = af2.origin_city
    WHERE af1.origin_city = 'Seattle WA'
),
TwoOrMoreStops AS (
    SELECT DISTINCT af3.dest_city
    FROM AllFlights af1
    JOIN AllFlights af2 ON af1.dest_city = af2.origin_city
    JOIN AllFlights af3 ON af2.dest_city = af3.origin_city
    WHERE af1.origin_city = 'Seattle WA' AND af3.dest_city NOT IN (SELECT dest_city FROM ReachableCities)
)
SELECT dest_city AS city
FROM TwoOrMoreStops
ORDER BY city;
